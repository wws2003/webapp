package com.techburg.autospring.service.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;

import org.springframework.beans.factory.annotation.Autowired;

import com.techburg.autospring.bo.abstr.IWorkspaceBo;
import com.techburg.autospring.db.task.abstr.AbstractDBTask;
import com.techburg.autospring.db.task.abstr.IDBTaskExecutor;
import com.techburg.autospring.db.task.impl.WorkspaceDBTaskImpl;
import com.techburg.autospring.model.WorkspacePersistenceQuery;
import com.techburg.autospring.model.business.Workspace;
import com.techburg.autospring.model.entity.WorkspaceEntity;
import com.techburg.autospring.service.abstr.IWorkspacePersistenceService;
import com.techburg.autospring.service.abstr.PersistenceResult;

public class WorkspacePersistenceServiceJPAImpl implements IWorkspacePersistenceService {

	private EntityManagerFactory mEntityManagerFactory = null;
	private IWorkspaceBo mWorkspaceBo = null;
	private IDBTaskExecutor mDBTaskExecutor = null;
	
	@Autowired
	public WorkspacePersistenceServiceJPAImpl(EntityManagerFactory entityManagerFactory) {
		mEntityManagerFactory = entityManagerFactory;	
	}

	@Autowired
	public void setWorkspaceBo(IWorkspaceBo workspaceBo) {
		mWorkspaceBo = workspaceBo;
	}
	
	@Autowired
	public void setDBTaskExecutor(IDBTaskExecutor dbTaskExecutor) {
		mDBTaskExecutor = dbTaskExecutor;
	}

	@Override
	public int persistWorkspace(Workspace workspace) {
		WorkspaceDBTaskImpl workspacePersistTask = new WorkspaceDBTaskImpl(mWorkspaceBo, mEntityManagerFactory);
		try {
			workspacePersistTask.setPersistParams(workspace);
			workspacePersistTask.setScheduleMode(AbstractDBTask.SCHEDULE_ASYNC_MODE);
			mDBTaskExecutor.executeDBTask(workspacePersistTask);
		}
		catch (Exception e) {
			return PersistenceResult.PERSISTENCE_FAILED;
		}
		return PersistenceResult.REQUEST_QUEUED;
	}

	//TODO Update using db task
	@Override
	public int updateWorkspace(Workspace workspace) {
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

	@Override
	public int loadWorkspace(List<Workspace> workspaceList, WorkspacePersistenceQuery query) {
		WorkspaceDBTaskImpl workspaceLoadTask = new WorkspaceDBTaskImpl(mWorkspaceBo, mEntityManagerFactory);
		workspaceLoadTask.setLoadParam(query);
		workspaceLoadTask.setScheduleMode(AbstractDBTask.SCHEDULE_SYNC_MODE);
		mDBTaskExecutor.executeDBTask(workspaceLoadTask);
		return workspaceLoadTask.getLoadResult(workspaceList);
	}

	@Override
	public int removeWorkspaceByID(long id) {
		// TODO Auto-generated method stub
		return 0;
	}

}
