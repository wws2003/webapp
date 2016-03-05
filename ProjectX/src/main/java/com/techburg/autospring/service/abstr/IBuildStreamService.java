package com.techburg.autospring.service.abstr;


public interface IBuildStreamService {
	StringBuffer getBuildOutputBufferForWorkspace(long workspaceId);
	
	void bindWorkspaceToBuildOutputBuffer(long workspaceId, StringBuffer buildStringBuffer);
	
	void releaseWorkspaceBuildOutputBuffer(long workspaceId);
}
