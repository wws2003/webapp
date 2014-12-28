package com.techburg.autospring.factory.abstr;

import com.techburg.autospring.model.business.Workspace;

public interface IWorkspaceFactory {
	public Workspace createWorkspace(String workspaceName, String buildScriptFileName);
	public Workspace createGithubWorkspace(boolean sparseCheckout);
}
