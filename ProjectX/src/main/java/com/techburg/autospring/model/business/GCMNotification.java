package com.techburg.autospring.model.business;

import java.util.List;

public class GCMNotification {
	private BuildInfo mBuildInfo = null;
	private List<String> mGCMEndPoints = null;
	
	public GCMNotification(BuildInfo buildInfo, List<String> gCMEndPoints) {
		mBuildInfo = buildInfo;
		mGCMEndPoints = gCMEndPoints;
	}

	public BuildInfo getBuildInfo() {
		return mBuildInfo;
	}

	public List<String> getGCMEndPoints() {
		return mGCMEndPoints;
	}
	
}
