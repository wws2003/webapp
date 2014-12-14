package com.techburg.autospring.service.impl;

import java.util.List;

import javax.persistence.EntityManagerFactory;

import org.springframework.beans.factory.annotation.Autowired;

import com.techburg.autospring.bo.abstr.IWorkspaceBo;
import com.techburg.autospring.db.task.abstr.AbstractDBTask;
import com.techburg.autospring.db.task.abstr.IDBTaskExecutor;
import com.techburg.autospring.db.task.impl.WorkspaceDBTaskImpl;
import com.techburg.autospring.model.WorkspacePersistenceQuery;
import com.techburg.autospring.model.business.Workspace;
import com.techburg.autospring.service.abstr.IBrowsingObjectPersistentService;
import com.techburg.autospring.service.abstr.IWorkspacePersistenceService;
import com.techburg.autospring.service.abstr.PersistenceResult;

public class WorkspacePersistenceServiceJPAImpl implements IWorkspacePersistenceService {

	private EntityManagerFactory mEntityManagerFactory = null;
	private IWorkspaceBo mWorkspaceBo = null;
	private IDBTaskExecutor mDBTaskExecutor = null;
	private IBrowsingObjectPersistentService mBrowsingObjectPersistentService = null;
	
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
	
	@Autowired
	public void setBrowsingObjectPersistentService(IBrowsingObjectPersistentService browsingObjectPersistentService) {
		mBrowsingObjectPersistentService = browsingObjectPersistentService;
	}

	@Override
	public int persistWorkspace(Workspace workspace) {
		WorkspaceDBTaskImpl workspacePersistTask = new WorkspaceDBTaskImpl(mWorkspaceBo, mEntityManagerFactory);
		try {
			workspacePersistTask.setPersistParams(workspace, mBrowsingObjectPersistentService);
			workspacePersistTask.setScheduleMode(AbstractDBTask.SCHEDULE_ASYNC_MODE);
			mDBTaskExecutor.executeDBTask(workspacePersistTask);
		}
		catch (Exception e) {
			return PersistenceResult.PERSISTENCE_FAILED;
		}
		return PersistenceResult.REQUEST_QUEUED;
	}

	@Override
	public int updateWorkspace(Workspace workspace) {
		WorkspaceDBTaskImpl workspacePersistTask = new WorkspaceDBTaskImpl(mWorkspaceBo, mEntityManagerFactory);
		try {
			workspacePersistTask.setPersistParams(workspace, mBrowsingObjectPersistentService);
			workspacePersistTask.setScheduleMode(AbstractDBTask.SCHEDULE_SYNC_MODE);
			mDBTaskExecutor.executeDBTask(workspacePersistTask);
		}
		catch (Exception e) {
			return PersistenceResult.PERSISTENCE_FAILED;
		}
		return PersistenceResult.REQUEST_QUEUED;
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
