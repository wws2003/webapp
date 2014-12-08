package com.techburg.autospring.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.techburg.autospring.model.business.BuildInfo;
import com.techburg.autospring.service.abstr.IBuildDataService;
import com.techburg.autospring.task.abstr.IBuildTaskProcessor;

public class BuildDataServiceImpl implements IBuildDataService {

	private IBuildTaskProcessor mBuildTaskProcessor = null;
	
	@Autowired
	public void setBuildTaskProcessor(IBuildTaskProcessor buildTaskProcessor) {
		mBuildTaskProcessor = buildTaskProcessor;
	}
	
	@Override
	public void getBuildingBuildInfoList(List<BuildInfo> buildInfoList) {
		mBuildTaskProcessor.getBuildingBuildInfoList(buildInfoList);
	}

	@Override
	public void getWaitingBuildInfoList(List<BuildInfo> buildInfoList) {
		mBuildTaskProcessor.getWaitingBuildInfoList(buildInfoList);
	}

	@Override
	public void getBuildingBuildInfoListOfWorkspace(List<BuildInfo> buildInfoList, long workspaceId) {
		List<BuildInfo> tmpBuildInfoList = new ArrayList<BuildInfo>();
		mBuildTaskProcessor.getBuildingBuildInfoList(tmpBuildInfoList);
		buildInfoList.clear();
		for(BuildInfo buildInfo : tmpBuildInfoList) {
			if(buildInfo.getWorkspace() != null && buildInfo.getWorkspace().getId() == workspaceId) {
				buildInfoList.add(buildInfo);
			}
		}
	}

	@Override
	public void getWaitingBuildInfoListOfWorkspace(List<BuildInfo> buildInfoList, long workspaceId) {
		List<BuildInfo> tmpBuildInfoList = new ArrayList<BuildInfo>();
		mBuildTaskProcessor.getWaitingBuildInfoList(tmpBuildInfoList);
		buildInfoList.clear();
		for(BuildInfo buildInfo : tmpBuildInfoList) {
			if(buildInfo.getWorkspace() != null && buildInfo.getWorkspace().getId() == workspaceId) {
				buildInfoList.add(buildInfo);
			}
		}
	}
}
