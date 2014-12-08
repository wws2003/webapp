package com.techburg.autospring.db.task.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceException;
import javax.persistence.Query;

import com.techburg.autospring.bo.abstr.IWorkspaceBo;
import com.techburg.autospring.db.task.abstr.AbstractDBTask;
import com.techburg.autospring.model.BasePersistenceQuery.DataRange;
import com.techburg.autospring.model.WorkspacePersistenceQuery;
import com.techburg.autospring.model.business.Workspace;
import com.techburg.autospring.model.entity.WorkspaceEntity;
import com.techburg.autospring.service.abstr.PersistenceResult;

public class WorkspaceDBTaskImpl extends AbstractDBTask {

	public static final int TASK_TYPE_PERSIST = 0;
	public static final int TASK_TYPE_UPDATE = 1;
	public static final int TASK_TYPE_LOAD = 2;
	public static final int TASK_TYPE_REMOVE_BY_ID = 3;

	private int mTaskType = 0;

	//parameters set from outside
	private Workspace mParamWorkspaceToPersist = null;
	private Workspace mParamWorkspaceToUpdate = null;
	private WorkspacePersistenceQuery mParamWorkspaceLoadQuery = null;
	private long mParamIdForRemove = -1;

	//Results calculated by execute()
	private int mResultForPersist = -1;
	private int mResultForLoad = -1;
	private int mResultForRemove = -1;
	private int mResultForUpdate = -1;
	private List<Workspace> mResultWorkspaceListLoad = new ArrayList<Workspace>();
	
	private IWorkspaceBo mWorkspaceBo = null;
	private EntityManagerFactory mEntityManagerFactory;

	public WorkspaceDBTaskImpl(IWorkspaceBo workspaceBo, EntityManagerFactory etmFactory) {
		mWorkspaceBo = workspaceBo;
		mEntityManagerFactory = etmFactory;
	}

	public void setPersistParams(Workspace workspaceToPersist) {
		mParamWorkspaceToPersist = workspaceToPersist;
		mTaskType = TASK_TYPE_PERSIST;
		setDBReadWriteMode(DB_WRITE_MODE);
	}
	
	public int getPersistResult() {
		return mResultForPersist;
	}
	
	public void setGetWorkspaceUpdateParam(Workspace workspaceToUpdate) {
		mParamWorkspaceToUpdate = workspaceToUpdate;
		mTaskType = TASK_TYPE_UPDATE;
		setDBReadWriteMode(DB_READ_MODE);
	}
	
	public long getWorkspaceUpdateResult() {
		return mResultForUpdate;
	}
	
	public void setLoadParam(WorkspacePersistenceQuery paramWorkspaceLoadQuery) {
		mParamWorkspaceLoadQuery = paramWorkspaceLoadQuery;
		mTaskType = TASK_TYPE_LOAD;
		setDBReadWriteMode(DB_READ_MODE);
	}
	
	public int getLoadResult(List<Workspace> resultWorkspaceListLoad) {
		resultWorkspaceListLoad.clear();
		resultWorkspaceListLoad.addAll(mResultWorkspaceListLoad);
		mResultWorkspaceListLoad.clear();
		return mResultForLoad;
	}
	
	public void setRemoveParam(long removeId) {
		mParamIdForRemove = removeId;
		mTaskType = TASK_TYPE_REMOVE_BY_ID;
		setDBReadWriteMode(DB_WRITE_MODE);
	}
	
	public int getRemoveResult() {
		return mResultForRemove;
	}
	
	@Override
	public void execute() {
		switch (mTaskType) {
		case TASK_TYPE_LOAD:
			mResultWorkspaceListLoad.clear();
			mResultForLoad = loadWorkspace(mResultWorkspaceListLoad, mParamWorkspaceLoadQuery);
			break;
		case TASK_TYPE_PERSIST:
			mResultForPersist = persistWorkspace(mParamWorkspaceToPersist);
			break;
		case TASK_TYPE_UPDATE:
			mResultForUpdate = updateWorkspace(mParamWorkspaceToUpdate);
			break;
		case TASK_TYPE_REMOVE_BY_ID:
			mResultForRemove = removeWorkspaceByID(mParamIdForRemove);
			break;
		default:
			break;
		}
	}

	private int persistWorkspace(Workspace workspace) {
		WorkspaceEntity entity = mWorkspaceBo.getEntityFromBusinessObject(workspace);
		EntityManager entityManager = mEntityManagerFactory.createEntityManager();
		EntityTransaction ts = entityManager.getTransaction();
		try {
			ts.begin();
			entityManager.persist(entity);
			entityManager.detach(entity); //Do not need to manage this object longer !
			ts.commit();
		}
		catch (PersistenceException pe) {
			pe.printStackTrace();
			ts.rollback();
			return PersistenceResult.PERSISTENCE_FAILED;
		}
		finally {
			entityManager.close();
		}
		return PersistenceResult.PERSISTENCE_SUCCESSFUL;
	}

	private int updateWorkspace(Workspace workspace) {
		EntityManager entityManager = mEntityManagerFactory.createEntityManager();
		WorkspaceEntity entity = entityManager.find(WorkspaceEntity.class, workspace.getId());
		if(entity == null) {
			return PersistenceResult.UPDATE_FAILED;
		}
		EntityTransaction tx = entityManager.getTransaction();
		tx.begin();
		entity.setScriptFilePath(workspace.getScriptFilePath());
		tx.commit();
		entityManager.close();
		return PersistenceResult.UPDATE_SUCCESSFUL;
	}

	private int loadWorkspace(List<Workspace> buildInfoList, WorkspacePersistenceQuery query) {
		Query loadQuery = null;
		buildInfoList.clear();
		EntityManager entityManager = mEntityManagerFactory.createEntityManager();

		switch (query.dataRange) {
		case DataRange.ALL:
			loadQuery = entityManager.createNativeQuery("select * from workspace;", WorkspaceEntity.class);
			try {
				@SuppressWarnings("unchecked")
				List<WorkspaceEntity> entities = loadQuery.getResultList();
				for(WorkspaceEntity entity : entities) {
					entityManager.detach(entity);
					buildInfoList.add(mWorkspaceBo.getBusinessObjectFromEntity(entity));
				}
				return PersistenceResult.LOAD_SUCCESSFUL;
			}
			catch (Exception e) {
				e.printStackTrace();
				return PersistenceResult.INVALID_QUERY;
			}
		case DataRange.ID_MATCH:
			long id = query.id;

			WorkspaceEntity entity = entityManager.find(WorkspaceEntity.class, id);
			if(entity != null) {
				entityManager.detach(entity);
				buildInfoList.add(mWorkspaceBo.getBusinessObjectFromEntity(entity));
			}
			return PersistenceResult.LOAD_SUCCESSFUL;
		default:
			return PersistenceResult.INVALID_QUERY;
		}
	}

	private int removeWorkspaceByID(long id) {
		// TODO Auto-generated method stub
		return 0;
	}

}
