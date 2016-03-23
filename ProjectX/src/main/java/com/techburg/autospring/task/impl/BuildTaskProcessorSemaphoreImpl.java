package com.techburg.autospring.task.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

import com.techburg.autospring.model.business.BuildInfo;
import com.techburg.autospring.model.business.GCMNotification;
import com.techburg.autospring.model.business.Workspace;
import com.techburg.autospring.service.abstr.IBrowsingObjectPersistentService;
import com.techburg.autospring.service.abstr.IBuildInfoPersistenceService;
import com.techburg.autospring.service.abstr.INotificationPersistenceService;
import com.techburg.autospring.service.abstr.INotificationSenderService;
import com.techburg.autospring.task.abstr.IBuildTask;
import com.techburg.autospring.task.abstr.IBuildTaskProcessor;
import com.techburg.autospring.task.abstr.IBuildTaskQueue;

public class BuildTaskProcessorSemaphoreImpl implements IBuildTaskProcessor, DisposableBean, InitializingBean {

	//private IBrowsingService mBrowsingService = null;
	private IBrowsingObjectPersistentService mBrowsingObjectPersistentService = null;
	private IBuildInfoPersistenceService mBuildInfoPersistenceService = null;
	private INotificationPersistenceService mNotificationPersistenceService = null;
	private INotificationSenderService mNotificationSenderService = null;
	
	public BuildTaskProcessorSemaphoreImpl() {
		mQueueSemaphore = new Semaphore(0);
		mBuildThreadSemaphore = new Semaphore(1); //This semaphore actually works as a lock
		mStopped = true;
	}

	@Autowired
	public void setWaitingBuildTaskQueue(IBuildTaskQueue buildTaskQueue) {
		mWaitingTaskQueue = buildTaskQueue;
	}
	
	@Autowired
	public void setBuildInfoPersistenceService(IBuildInfoPersistenceService buildInfoPersistenceService) {
		mBuildInfoPersistenceService = buildInfoPersistenceService;
	}
	
	//Inject by ref attribute
	public void setBrowsingObjectPersistentService(IBrowsingObjectPersistentService browsingObjectPersistentService) {
		mBrowsingObjectPersistentService = browsingObjectPersistentService;
	}
	
	@Autowired
	public void setNotificationPersistenceService(INotificationPersistenceService notificationPersistenceService) {
		mNotificationPersistenceService = notificationPersistenceService;
	}
	
	@Autowired
	public void setNotificationSenderService(INotificationSenderService notificationSenderService) {
		mNotificationSenderService = notificationSenderService;
	}
	
	@Override
	public int start() {
		int result = BuildTaskProcessorResult.START_SUCCESSFUL;
		try {
			mBuildThreadSemaphore.acquire();
			if(!mStopped) {
				result = BuildTaskProcessorResult.ALREADY_STARTED;
			}
			else {
				mStopped = false;
				Runnable runnableTaskBuild = new Runnable() {	
					@Override
					public void run() {
						runBuildTasks();
					}
				};
				mBuildTaskThread = new Thread(runnableTaskBuild);
				mBuildTaskThread.start();
			}
			mBuildThreadSemaphore.release();
			return result;
		} 
		catch (InterruptedException e) {
			e.printStackTrace();
			return BuildTaskProcessorResult.START_FAILED;
		}
	}

	@Override
	public int addBuildTask(IBuildTask buildTask) {
		try {
			buildTask.setIdInQueue(mWaitingTaskQueue.genNextQueueId());
			mWaitingTaskQueue.addBuildTaskToQueue(buildTask);
			mQueueSemaphore.release();
			return BuildTaskProcessorResult.ADD_TASK_SUCCESSFUL;
		} catch (Exception e) {
			e.printStackTrace();
			return BuildTaskProcessorResult.ADD_TASK_FAILED;
		}
	}

	@Override
	public int cancelTask(long taskId) {
		mWaitingTaskQueue.markBuildTaskToBeCancelled(taskId);
		mQueueSemaphore.release();
		return BuildTaskProcessorResult.CANCEL_TASK_SUCCESSFUL;
	}

	@Override
	public int cancelAllBuildTask() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int stop() {
		int result = BuildTaskProcessorResult.STOP_SUCCESSFUL;
		try {
			mBuildThreadSemaphore.acquire();
			if(mStopped) {
				result = BuildTaskProcessorResult.ALREADY_STOPPED;
			}
			else {
				mStopped = true;
				mQueueSemaphore.release();
				mBuildTaskThread.join();
			}
			mBuildThreadSemaphore.release();
		} catch (InterruptedException e) {
			e.printStackTrace();
			result = BuildTaskProcessorResult.STOP_FAILED;
		}

		return result;
	}

	@Override
	public void getBuildingBuildInfoList(List<BuildInfo> buildInfoList) {
		buildInfoList.clear();
		mWaitingTaskQueue.getBuildInfoListFromBuildingTask(buildInfoList);
	}

	@Override
	public void getWaitingBuildInfoList(List<BuildInfo> buildInfoList) {
		buildInfoList.clear();
		mWaitingTaskQueue.getBuildInfoListFromWaitingTaskList(buildInfoList);
	}
	
	@Override
	public void destroy() throws Exception {
		System.out.println("--------------------------To destroy the build task processor by stopping task build thread--------------------------");
		int result = stop();
		System.out.println("--------------------------Stop result " + result +" -------------------------");
	}

	private void runBuildTasks() {
		//At the beginning, reconstruct the browsing structure to reflect any changes in folders and files in the workspace
		try {
			recontructBrowsingStructure(null);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		while(!mStopped) {
			try {
				//Waiting for a new task to to release the semaphore
				mQueueSemaphore.acquire();
				
				//Cancel tasks requested
				long cancelledTaskId = mWaitingTaskQueue.getCancelledBuildTaskId();
				//System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$Cancelled task Id " + cancelledTaskId);
				if(cancelledTaskId > 0) {
					mWaitingTaskQueue.cancelBuildTask(cancelledTaskId);
					continue;
				}
				
				//Retrieve next task from queue and execute
				IBuildTask nextBuildTask = mWaitingTaskQueue.popBuildTaskFromQueue();
				if(nextBuildTask != null) {
					executeBuildTask(nextBuildTask);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	private void executeBuildTask(IBuildTask nextBuildTask) throws Exception {
		mWaitingTaskQueue.setBuildingTask(nextBuildTask);
		nextBuildTask.execute();
		BuildInfo buildInfo = new BuildInfo();
		nextBuildTask.storeToBuildInfo(buildInfo, true);
		mWaitingTaskQueue.setBuildingTask(null);
		mBuildInfoPersistenceService.persistBuildInfo(buildInfo);
		
		//FIXME Post process. Should use advice or something similar for the spirit of AOP 
		notifyBuildInfo(buildInfo);
		recontructBrowsingStructure(nextBuildTask.getWorkspace());
	}
	
	private void recontructBrowsingStructure(Workspace workspace) throws Exception {
		if(mBrowsingObjectPersistentService != null) {
			if(workspace == null) {
				//Entirely reconstruct DB
				mBrowsingObjectPersistentService.removeBrowsingObjectInDirectory(null);
				mBrowsingObjectPersistentService.persistBrowsingObjectInDirectory(null);
			}
			else {
				//Partially reconstruct DB
				mBrowsingObjectPersistentService.removeBrowsingObjectInDirectory(workspace.getDirectoryPath());
				mBrowsingObjectPersistentService.persistBrowsingObjectInDirectory(workspace.getDirectoryPath());
			}
		}
	}
	
	private void notifyBuildInfo(BuildInfo buildInfo) {
		//TODO Perhaps add another abstraction layer, to handle future notification such as E-mail, Mobile... rather than just GCM
		List<String> gcmEndPoints = new ArrayList<String>();
		mNotificationPersistenceService.getGCMEndPointsForWorkspace(buildInfo.getWorkspace().getId(), gcmEndPoints);
		GCMNotification gcmNotification = new GCMNotification(buildInfo, gcmEndPoints);
		mNotificationSenderService.sendGCMNotification(gcmNotification);
	}

	private boolean mStopped;
	private IBuildTaskQueue mWaitingTaskQueue; //Object with synchronized method
	private Thread mBuildTaskThread = null;
	private Semaphore mQueueSemaphore;
	private Semaphore mBuildThreadSemaphore;

	@Override
	public void afterPropertiesSet() throws Exception {
		System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$Property set!$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$ " + this);
		start();
	}
}
