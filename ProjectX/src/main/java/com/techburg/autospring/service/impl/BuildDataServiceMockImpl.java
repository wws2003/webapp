package com.techburg.autospring.service.impl;

import java.util.Date;
import java.util.List;

import com.techburg.autospring.model.business.BuildInfo;
import com.techburg.autospring.service.abstr.IBuildDataService;

public class BuildDataServiceMockImpl implements IBuildDataService {

	@Override
	public void getBuildingBuildInfoList(List<BuildInfo> buildInfoList) {
		buildInfoList.clear();
		buildInfoList.add(createSampleBuildInfo());
	}

	@Override
	public void getWaitingBuildInfoList(List<BuildInfo> buildInfoList) {
		buildInfoList.clear();
		for(int i = 0; i < 5; i++)
			buildInfoList.add(createSampleBuildInfo());
	}

	private BuildInfo createSampleBuildInfo() {
		BuildInfo buildInfo = new BuildInfo();
		buildInfo.setId(0);
		buildInfo.setBeginTimeStamp(new Date());
		buildInfo.setEndTimeStamp(new Date());
		buildInfo.setLogFilePath("LogFilePath");
		return buildInfo;
	}

	@Override
	public void getBuildingBuildInfoListOfWorkspace(
			List<BuildInfo> buildInfoList, long workspaceId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void getWaitingBuildInfoListOfWorkspace(
			List<BuildInfo> buildInfoList, long workspaceId) {
		// TODO Auto-generated method stub
		
	}
}