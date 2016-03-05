package com.techburg.autospring.service.impl;

import java.util.HashMap;
import java.util.Map;

import com.techburg.autospring.service.abstr.IBuildStreamService;

public class MapBasedBuildStreamServiceImpl implements IBuildStreamService {

	private Map<Long, StringBuffer> mWorkspaceBuildInputStreamMap = new HashMap<Long, StringBuffer>();

	@Override
	public StringBuffer getBuildOutputBufferForWorkspace(long workspaceId) {
		return mWorkspaceBuildInputStreamMap.get(workspaceId);
	}

	@Override
	public void bindWorkspaceToBuildOutputBuffer(long workspaceId,
			StringBuffer buildStringBuffer) {
		mWorkspaceBuildInputStreamMap.put(workspaceId, buildStringBuffer);
	}

	@Override
	public void releaseWorkspaceBuildOutputBuffer(long workspaceId) {
		mWorkspaceBuildInputStreamMap.remove(workspaceId);
	}
	
}
