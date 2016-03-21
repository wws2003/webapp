package com.techburg.autospring.factory.abstr;

import com.techburg.autospring.model.business.Workspace;

public interface IWorkspaceFactory {
	//Create workspace instance along with workspace folder but do not specify build script yet
	public Workspace createWorkspaceDeferringBuildScript(String workspaceName);

	//Create workspace instance along with workspace folder. If buildScriptFileName is not set, use default builds cript name instead
	public Workspace createWorkspace(String workspaceName, String buildScriptFileName);

	//Create workspace instance only for temporary use
	public Workspace createGithubWorkspace(boolean sparseCheckout);
}
