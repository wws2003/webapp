package com.techburg.autospring.service.impl;

import java.util.List;

import javax.persistence.EntityManagerFactory;

import org.springframework.beans.factory.annotation.Autowired;

import com.techburg.autospring.bo.abstr.IWorkspaceBo;
import com.techburg.autospring.db.task.abstr.AbstractDBTask;
import com.techburg.autospring.db.task.abstr.IDBTaskExecutor;
import com.techburg.autospring.db.task.impl.WorkspaceDBTaskImpl;
import com.techburg.autospring.model.business.Workspace;
import com.techburg.autospring.model.query.WorkspacePersistenceQuery;
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
	
	//Inject by ref attribute
	public void setDBTaskExecutor(IDBTaskExecutor dbTaskExecutor) {
		mDBTaskExecutor = dbTaskExecutor;
	}
	
	//Inject by ref attribute
	public void setBrowsingObjectPersistentService(IBrowsingObjectPersistentService browsingObjectPersistentService) {
		mBrowsingObjectPersistentService = browsingObjectPersistentService;
	}

	@Override
	public int persistWorkspace(Workspace workspace, boolean isSyncMode) {
		WorkspaceDBTaskImpl workspacePersistTask = new WorkspaceDBTaskImpl(mWorkspaceBo, mEntityManagerFactory);
		try {
			workspacePersistTask.setPersistParams(workspace, mBrowsingObjectPersistentService);
			workspacePersistTask.setScheduleMode(isSyncMode ? AbstractDBTask.SCHEDULE_SYNC_MODE : AbstractDBTask.SCHEDULE_SYNC_MODE);
			mDBTaskExecutor.executeDBTask(workspacePersistTask);
		}
		catch (Exception e) {
			return PersistenceResult.PERSISTENCE_FAILED;
		}
		return isSyncMode ? PersistenceResult.REMOVE_SUCCESSFUL: PersistenceResult.REQUEST_QUEUED;
	}

	@Override
	public int updateWorkspace(Workspace workspaceToUpdate) {
		WorkspaceDBTaskImpl workspaceUpdateTask = new WorkspaceDBTaskImpl(mWorkspaceBo, mEntityManagerFactory);
		try {
			workspaceUpdateTask.setUpdateParams(workspaceToUpdate, mBrowsingObjectPersistentService);
			workspaceUpdateTask.setScheduleMode(AbstractDBTask.SCHEDULE_SYNC_MODE);
			mDBTaskExecutor.executeDBTask(workspaceUpdateTask);
		}
		catch (Exception e) {
			e.printStackTrace();
			return PersistenceResult.PERSISTENCE_FAILED;
		}
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
