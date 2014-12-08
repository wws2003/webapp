package com.techburg.autospring.task.abstr;

import java.io.File;
import java.util.Date;

import com.techburg.autospring.model.business.BuildInfo;
import com.techburg.autospring.service.abstr.IBuildInfoPersistenceService;

public abstract class AbstractBuildTask implements IBuildTask{
	protected long mId;
	private int mStatus;
	private Date mBeginBuildTime;
	private Date mEndBuildTime;
	
	private String mLogFileNamePrefix;
	private String mLogFileNameExtension;
	
	private static final String G_UNKNOWN_LOG_FILE_NAME = "---";
	private static final String LOG_DIR_NAME = "Logs";
	
	protected IBuildInfoPersistenceService mPersistenceService;
	
	public AbstractBuildTask(String logFilePathPrefix, String logFileNameExtension, String logFileLocation) {
		mLogFileNamePrefix = logFilePathPrefix;
		mLogFileNameExtension  = logFileNameExtension;
		//mLogFileLocation = logFileLocation;
	}

	public void setBuildInfoPersistenceService(IBuildInfoPersistenceService persistenceService) {
		mPersistenceService = persistenceService;
	}

	@Override
	public void setIdInQueue(long id) {
		mId = id;
	}
	
	@Override
	public long getIdInQueue() {
		return mId;
	}

	@Override
	public void cancel() {
		mStatus = BuildTaskResult.CANCELLED;
	}
	
	@Override
	public int execute() {
		if(mStatus == BuildTaskResult.CANCELLED) {
			return BuildTaskResult.CANCELLED;
		}
		mBeginBuildTime = new Date();
		int ret = mainExecute();
		mEndBuildTime = new Date();
		mStatus = ret;
		return ret;
	}

	@Override
	public void storeToBuildInfo(BuildInfo buildInfo, boolean toPersist) {
		buildInfo.setBeginTimeStamp(mBeginBuildTime);
		buildInfo.setEndTimeStamp(mEndBuildTime);
		buildInfo.setStatus(mStatus);
		buildInfo.setWorkspace(getWorkspace());
		if(!toPersist) {
			buildInfo.setId(mId);
		}
		String logFilePath = (mBeginBuildTime != null) ? getLogFileFullPath() : G_UNKNOWN_LOG_FILE_NAME;
		buildInfo.setLogFilePath(logFilePath);
		
		if(toPersist && mPersistenceService != null) {
			System.out.println("--------------------------Trying to persist new build info---------------------");
			mPersistenceService.persistBuildInfo(buildInfo);
		}
	}

	protected String getLogFileFullPath() {
		StringBuilder logFileNameBuilder = new StringBuilder();
		logFileNameBuilder.append(getWorkspace().getDirectoryPath())
						.append(File.separator)
						.append(LOG_DIR_NAME)
						.append(File.separator)
						.append(mLogFileNamePrefix)
						.append("_")
						.append(mBeginBuildTime.getTime())
						.append(".")
						.append(mLogFileNameExtension);
	
		return logFileNameBuilder.toString();
	}
	
	protected abstract int mainExecute();
}
