package com.techburg.autospring.delegate.impl;

import com.techburg.autospring.delegate.abstr.AbstractWorkspaceCreateDelegate;
import com.techburg.autospring.delegate.abstr.IWorkspaceFileSystemDelegate;
import com.techburg.autospring.model.business.Workspace;

public class BuildScriptWorkspaceCreateDelegateImpl extends AbstractWorkspaceCreateDelegate {

	private String mBuildScriptContent;
	private IWorkspaceFileSystemDelegate mWorkspaceFileSystemDelegate;
	private static final String gErrorMessage = "Build script couldn't be saved properly";
	
	public BuildScriptWorkspaceCreateDelegateImpl(String buildScriptContent, 
			IWorkspaceFileSystemDelegate workspaceFileSystemDelegate) {
		
		super();
		mBuildScriptContent = buildScriptContent;
		mWorkspaceFileSystemDelegate = workspaceFileSystemDelegate;
	}

	@Override
	protected void handleWorkspaceCreated(Workspace workspace)
			throws Exception {
		//Assume that workspace already has been assigned a build script
		try {
			mWorkspaceFileSystemDelegate.saveWorkspaceBuildScriptContent(workspace, mBuildScriptContent);
		}
		catch(Exception primaryException) {
			primaryException.printStackTrace();
			throw new Exception(gErrorMessage);
		}
	}

}
