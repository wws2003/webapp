package com.techburg.autospring.delegate.impl;

import com.techburg.autospring.delegate.abstr.AbstractWorkspaceModifyDelegate;
import com.techburg.autospring.delegate.abstr.IWorkspaceFileSystemDelegate;
import com.techburg.autospring.model.business.Workspace;

public class BuildScriptSavingWorkspaceModifyDelegateImpl extends AbstractWorkspaceModifyDelegate {

	private IWorkspaceFileSystemDelegate mWorkspaceFileSystemDelegate = null;
	private String mBuildScriptContent = null;
	private static final String gErrorMessage = "Build script couldn't be saved properly";
	
	public BuildScriptSavingWorkspaceModifyDelegateImpl(IWorkspaceFileSystemDelegate workspaceFileSystemDelegate, String buildScriptContent) {
		mWorkspaceFileSystemDelegate = workspaceFileSystemDelegate;
		mBuildScriptContent = buildScriptContent;
	}
	
	@Override
	protected void handleWorkspaceModified(Workspace workspace) throws Exception {
		try {
			mWorkspaceFileSystemDelegate.saveWorkspaceBuildScriptContent(workspace, mBuildScriptContent);
		}
		catch(Exception primaryException) {
			primaryException.printStackTrace();
			throw new Exception(gErrorMessage);
		}
	}

}
