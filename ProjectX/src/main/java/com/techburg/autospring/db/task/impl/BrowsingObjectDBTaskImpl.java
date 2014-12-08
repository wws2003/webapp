package com.techburg.autospring.db.task.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceException;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;

import com.techburg.autospring.bo.abstr.IBrowsingObjectBo;
import com.techburg.autospring.db.task.abstr.AbstractDBTask;
import com.techburg.autospring.model.business.BrowsingObject;
import com.techburg.autospring.model.entity.BrowsingObjectEntity;
import com.techburg.autospring.service.abstr.PersistenceResult;

public class BrowsingObjectDBTaskImpl extends AbstractDBTask {

	public static final int TASK_TYPE_PERSIST = 0;
	public static final int TASK_TYPE_GET_BY_ID = 1;
	public static final int TASK_TYPE_GET_BY_PATH = 2;
	public static final int TASK_TYPE_GET_CHILDREN = 3;
	public static final int TASK_TYPE_REMOVE_ALL = 4;

	private int mTaskType;

	private IBrowsingObjectBo mBrowsingObjectBo = null;
	private EntityManagerFactory mEntityManagerFactory = null;

	//parameters set from outside
	private BrowsingObject mParamBrowsingObjectToPersist = null;
	private long mParamGetById = -1;
	private String mParamGetByPath = null;
	private BrowsingObject mParamGetChildrenParent = null;

	//Results calculated by execute()
	private int mResultForPersist = -1;
	private int mResultForGetChildren = -1;
	private int mResultForRemoveAll = -1;

	private BrowsingObject mResultBrowsingObjectGetById = null;
	private BrowsingObject mResultBrowsingObjectGetByPath = null;
	private List<BrowsingObject> mResultBrowsingObjectListGetChildren = new ArrayList<BrowsingObject>();

	public BrowsingObjectDBTaskImpl(IBrowsingObjectBo browsingObjectBo, EntityManagerFactory etmFactory) {
		mBrowsingObjectBo = browsingObjectBo;
		mEntityManagerFactory = etmFactory;
	}
	
	public void setPersistParams(BrowsingObject browsingObjectToPersist) {
		mParamBrowsingObjectToPersist = browsingObjectToPersist;
		mTaskType = TASK_TYPE_PERSIST;
		setDBReadWriteMode(DB_WRITE_MODE);
	}

	public int getPersistResult() {
		return mResultForPersist;
	}

	public void setGetByIdParam(long id) {
		mParamGetById = id;
		mTaskType = TASK_TYPE_GET_BY_ID;
		setDBReadWriteMode(DB_READ_MODE);
	}
	
	public BrowsingObject getGetByIdResult() {
		return mResultBrowsingObjectGetById;
	}
	
	public void setGetByPathParam(String path) {
		mParamGetByPath = path;
		mTaskType = TASK_TYPE_GET_BY_PATH;
		setDBReadWriteMode(DB_READ_MODE);
	}
	
	public BrowsingObject getGetByPathResult() {
		return mResultBrowsingObjectGetByPath;
	}
	
	public void setGetChildrenParam(BrowsingObject parent) {
		mParamGetChildrenParent = parent;
		mTaskType = TASK_TYPE_GET_CHILDREN;
		setDBReadWriteMode(DB_READ_MODE);
	}
	
	public int getGetChildrenResult(List<BrowsingObject> children) {
		children.clear();
		children.addAll(mResultBrowsingObjectListGetChildren);
		mResultBrowsingObjectListGetChildren.clear();
		return mResultForGetChildren;
	}
	
	public void setRemoveAllParam() {
		mTaskType = TASK_TYPE_REMOVE_ALL;
		setDBReadWriteMode(DB_WRITE_MODE);
	}

	public int getRemoveResult() {
		return mResultForRemoveAll;
	}

	@Override
	public void execute() {
		switch (mTaskType) {
		case TASK_TYPE_GET_BY_ID:
			mResultBrowsingObjectGetById = getBrowsingObjectById(mParamGetById);
			break;
		case TASK_TYPE_GET_BY_PATH:
			mResultBrowsingObjectGetByPath = getBrowsingObjectByPath(mParamGetByPath);
			break;
		case TASK_TYPE_GET_CHILDREN:
			mResultBrowsingObjectListGetChildren.clear();
			mResultForGetChildren = getChildBrowsingObjects(mParamGetChildrenParent, mResultBrowsingObjectListGetChildren);
			break;
		case TASK_TYPE_PERSIST:
			mResultForPersist = persistBrowsingObject(mParamBrowsingObjectToPersist);
			break;
		case TASK_TYPE_REMOVE_ALL:
			mResultForRemoveAll = removeAllBrowsingObject();
			break;
		default:
			break;
		}
	}

	@Autowired
	public void setBuildInfoBo(IBrowsingObjectBo browsingObjectBo) {
		mBrowsingObjectBo = browsingObjectBo;
	}

	private int persistBrowsingObject(BrowsingObject browsingObject) {
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

	private BrowsingObject getBrowsingObjectById(long id) {
		EntityManager entityManager = mEntityManagerFactory.createEntityManager();
		BrowsingObjectEntity entity = entityManager.find(BrowsingObjectEntity.class, id);
		if(entity != null) {
			entityManager.detach(entity);
			return mBrowsingObjectBo.getBusinessObjectFromEntity(entity);
		}
		return null;
	}

	private BrowsingObject getBrowsingObjectByPath(String path) {
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

	private int getChildBrowsingObjects(BrowsingObject parent, List<BrowsingObject> children) {
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

	private int removeAllBrowsingObject() {
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
