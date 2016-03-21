package com.techburg.autospring.factory.impl;

import java.io.File;

import com.techburg.autospring.factory.abstr.IWorkspaceFactory;
import com.techburg.autospring.model.business.Workspace;
import com.techburg.autospring.util.FileUtil;

public class WorkspaceFactoryImpl implements IWorkspaceFactory {

	private String mWorkspaceDefaultDirectoryPath;
	private String mWorkspaceDefaultBuildScriptFileName;
	private String mGithubWorkspaceScriptDirectoryPath;

	private static final String GITHUB_SPARSE_CHECKOUT_SCRIPT_NAME = "git_sparse_checkout.sh";
	private static final String GITHUB_DENSE_CHECKOUT_SCRIPT_NAME = "git_dense_checkout.sh";

	@Override
	public Workspace createWorkspaceDeferringBuildScript(String workspaceName) {
		Workspace workspace = new Workspace();
		String workspaceDirPath = mWorkspaceDefaultDirectoryPath + File.separator + workspaceName;
		workspace.setDirectoryPath(workspaceDirPath);
		setUpWorkspaceDir(workspace);
		return workspace;
	}
	
	@Override
	public Workspace createWorkspace(String workspaceName, String buildScriptFileName) {
		Workspace workspace = new Workspace();
		boolean emptyBuildScriptName = buildScriptFileName == null || buildScriptFileName.isEmpty();
		String workspaceDirPath = mWorkspaceDefaultDirectoryPath + File.separator + workspaceName;
		workspace.setDirectoryPath(workspaceDirPath);
		workspace.setScriptFilePath(workspaceDirPath + File.separator + (emptyBuildScriptName ? mWorkspaceDefaultBuildScriptFileName : buildScriptFileName));
		setUpWorkspaceDir(workspace);
		return workspace;
	}

	public void setWorkspaceDefaultDirectoryPath(String workspaceDefaultDirectoryPath) {
		this.mWorkspaceDefaultDirectoryPath = workspaceDefaultDirectoryPath;
	}

	public void setWorkspaceDefaultBuildScriptFileName(String workspaceDefaultBuildScriptFileName) {
		this.mWorkspaceDefaultBuildScriptFileName = workspaceDefaultBuildScriptFileName;
	}

	public void setGithubWorkspaceScriptDirectoryPath(String githubWorkspaceScriptDirectoryPath) {
		mGithubWorkspaceScriptDirectoryPath = githubWorkspaceScriptDirectoryPath;
	}

	private void setUpWorkspaceDir(Workspace workspace) {
		FileUtil fileUtil = new FileUtil();
		fileUtil.createDirectoryIfNotExist(workspace.getDirectoryPath());
	}

	@Override
	public Workspace createGithubWorkspace(boolean sparseCheckout) {
		Workspace workspace = new Workspace();
		workspace.setScriptFilePath(mGithubWorkspaceScriptDirectoryPath + File.separator + (sparseCheckout ? GITHUB_SPARSE_CHECKOUT_SCRIPT_NAME : GITHUB_DENSE_CHECKOUT_SCRIPT_NAME));
		return workspace;
	}
}
