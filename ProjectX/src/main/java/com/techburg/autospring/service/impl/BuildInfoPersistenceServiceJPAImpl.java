package com.techburg.autospring.service.impl;

import java.util.List;

import javax.persistence.EntityManagerFactory;

import org.springframework.beans.factory.annotation.Autowired;

import com.techburg.autospring.bo.abstr.IBuildInfoBo;
import com.techburg.autospring.db.task.abstr.AbstractDBTask;
import com.techburg.autospring.db.task.abstr.IDBTaskExecutor;
import com.techburg.autospring.db.task.impl.BuildInfoDBTaskImpl;
import com.techburg.autospring.model.business.BuildInfo;
import com.techburg.autospring.model.query.BuildInfoPersistenceQuery;
import com.techburg.autospring.service.abstr.IBuildInfoPersistenceService;

public class BuildInfoPersistenceServiceJPAImpl implements IBuildInfoPersistenceService {

	private EntityManagerFactory mEntityManagerFactory;
	private IBuildInfoBo mBuildInfoBo = null;
	private IDBTaskExecutor mDBTaskExecutor = null;
	
	@Autowired
	public BuildInfoPersistenceServiceJPAImpl(EntityManagerFactory entityManagerFactory) {
		mEntityManagerFactory = entityManagerFactory;
	}

	@Autowired
	public void setBuildInfoBo(IBuildInfoBo buildInfoBo) {
		mBuildInfoBo = buildInfoBo;
	}
	
	//Inject by ref attribute
	public void setDBTaskExecutor(IDBTaskExecutor dbTaskExecutor) {
		mDBTaskExecutor = dbTaskExecutor;
	}

	@Override
	public int persistBuildInfo(BuildInfo buildInfo) {
		BuildInfoDBTaskImpl persistBuildInfoTask = new BuildInfoDBTaskImpl(mBuildInfoBo, mEntityManagerFactory);
		persistBuildInfoTask.setPersistParam(buildInfo);
		persistBuildInfoTask.setScheduleMode(AbstractDBTask.SCHEDULE_SYNC_MODE);
		mDBTaskExecutor.executeDBTask(persistBuildInfoTask);
		int persistResult = persistBuildInfoTask.getPersistResult();
		return persistResult;
	}

	@Override
	public int loadPersistedBuildInfo(List<BuildInfo> buildInfoList, BuildInfoPersistenceQuery query) {
		BuildInfoDBTaskImpl loadBuildInfoTask = new BuildInfoDBTaskImpl(mBuildInfoBo, mEntityManagerFactory);
		loadBuildInfoTask.setScheduleMode(AbstractDBTask.SCHEDULE_SYNC_MODE);
		loadBuildInfoTask.setLoadParam(query);
		mDBTaskExecutor.executeDBTask(loadBuildInfoTask);
		int loadResult = loadBuildInfoTask.getLoadResult(buildInfoList);
		return loadResult;
	}

	@Override
	public int removeBuildInfoByID(long id) {
		BuildInfoDBTaskImpl removeBuildInfoTask = new BuildInfoDBTaskImpl(mBuildInfoBo, mEntityManagerFactory);
		removeBuildInfoTask.setRemoveParam(id);
		removeBuildInfoTask.setScheduleMode(AbstractDBTask.SCHEDULE_SYNC_MODE);
		mDBTaskExecutor.executeDBTask(removeBuildInfoTask);
		return removeBuildInfoTask.getRemoveResult();
	}

	@Override
	public long getNumberOfPersistedBuildInfo(long workspaceId) {
		BuildInfoDBTaskImpl nbPersistedBuildInfo = new  BuildInfoDBTaskImpl(mBuildInfoBo, mEntityManagerFactory);
		nbPersistedBuildInfo.setGetNumberParam(workspaceId);
		nbPersistedBuildInfo.setScheduleMode(AbstractDBTask.SCHEDULE_SYNC_MODE);
		mDBTaskExecutor.executeDBTask(nbPersistedBuildInfo);
		return nbPersistedBuildInfo.getBuildInfoNumberResult();
	}
}
