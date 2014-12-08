package com.techburg.autospring.service.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceException;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;

import com.techburg.autospring.bo.abstr.IBrowsingObjectBo;
import com.techburg.autospring.model.business.BrowsingObject;
import com.techburg.autospring.model.entity.BrowsingObjectEntity;
import com.techburg.autospring.service.abstr.IBrowsingObjectPersistentService;
import com.techburg.autospring.service.abstr.PersistenceResult;

public class BrowsingObjectPersistentJPAImpl implements IBrowsingObjectPersistentService {

	private EntityManagerFactory mEntityManagerFactory;
	private IBrowsingObjectBo mBrowsingObjectBo = null;

	@Autowired
	public BrowsingObjectPersistentJPAImpl(EntityManagerFactory entityManagerFactory) {
		mEntityManagerFactory = entityManagerFactory;
	}

	@Autowired
	public void setBuildInfoBo(IBrowsingObjectBo browsingObjectBo) {
		mBrowsingObjectBo = browsingObjectBo;
	}
	
	@Override
	public int persistBrowsingObject(BrowsingObject browsingObject) {
		BrowsingObjectEntity entity = mBrowsingObjectBo.getEntityFromBusinessObject(browsingObject);
		EntityManager entityManager = mEntityManagerFactory.createEntityManager();
		EntityTransaction tx = entityManager.getTransaction();
		try {
			tx.begin();
			entityManager.persist(entity);
			entityManager.detach(entity); //Do not need to manage this object longer !
			browsingObject.setId(entity.getId());
			tx.commit();
		}
		catch (PersistenceException pe) {
			pe.printStackTrace();
			tx.rollback();
			return PersistenceResult.PERSISTENCE_FAILED;
		}
		finally {
			entityManager.close();
		}
		return PersistenceResult.PERSISTENCE_SUCCESSFUL;
	}

	@Override
	public BrowsingObject getBrowsingObjectById(long id) {
		EntityManager entityManager = mEntityManagerFactory.createEntityManager();
		BrowsingObjectEntity entity = entityManager.find(BrowsingObjectEntity.class, id);
		if(entity != null) {
			entityManager.detach(entity);
			return mBrowsingObjectBo.getBusinessObjectFromEntity(entity);
		}
		return null;
	}

	@Override
	public BrowsingObject getBrowsingObjectByPath(String path) {
		EntityManager entityManager = mEntityManagerFactory.createEntityManager();
		Query query = entityManager.createNamedQuery("findBrowsingObjectByPath");
		query.setParameter("path", path);
		BrowsingObjectEntity entity = (BrowsingObjectEntity) query.getSingleResult();
		if(entity != null) {
			BrowsingObject browsingObject = mBrowsingObjectBo.getBusinessObjectFromEntity(entity);
			entityManager.detach(entity);
			return browsingObject;
		}
		return null;
	}

	@Override
	public int getChildBrowsingObjects(BrowsingObject parent, List<BrowsingObject> children) {
		children.clear();
		EntityManager entityManager = mEntityManagerFactory.createEntityManager();
		Query query = entityManager.createNamedQuery("findBrowsingObjectByParent");
		query.setParameter("parentId", parent.getId());
		@SuppressWarnings("unchecked")
		List<BrowsingObjectEntity> entities = query.getResultList();
		for(BrowsingObjectEntity entity : entities) {
			BrowsingObject browsingObject = mBrowsingObjectBo.getBusinessObjectFromEntity(entity);
			children.add(browsingObject);
			entityManager.detach(entity);
		}
		return PersistenceResult.LOAD_SUCCESSFUL;
	}

	@Override
	public int removeAllBrowsingObject() {
		EntityManager entityManager = mEntityManagerFactory.createEntityManager();
		EntityTransaction tx = entityManager.getTransaction();
		try {
			tx.begin();
			Query query = entityManager.createNamedQuery("removeAll");
			query.executeUpdate();
			tx.commit();
		}
		catch (PersistenceException pe) {
			pe.printStackTrace();
			tx.rollback();
			return PersistenceResult.REMOVE_FAILED;
		}
		finally {
			entityManager.close();
		}
		return PersistenceResult.REMOVE_SUCCESSFUL;
	}

}
