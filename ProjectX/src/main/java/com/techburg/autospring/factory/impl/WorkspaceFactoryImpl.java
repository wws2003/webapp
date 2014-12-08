package com.techburg.autospring.factory.impl;

import java.io.File;

import com.techburg.autospring.factory.abstr.IWorkspaceFactory;
import com.techburg.autospring.model.business.Workspace;
import com.techburg.autospring.util.FileUtil;

public class WorkspaceFactoryImpl implements IWorkspaceFactory {

	private String mWorkspaceDefaultDirectoryPath;
	private String mWorkspaceDefaultBuildScriptFileName;
	
	@Override
	public Workspace createWorkspace(String workspaceName, String buildScriptFileName) {
		Workspace workspace = new Workspace();
		String workspaceDirPath = mWorkspaceDefaultDirectoryPath + File.separator + workspaceName;
		workspace.setDirectoryPath(workspaceDirPath);
		workspace.setScriptFilePath(workspaceDirPath + File.separator + (buildScriptFileName == null ? mWorkspaceDefaultBuildScriptFileName : buildScriptFileName));
		setUpWorkspaceDir(workspace);
		return workspace;
	}

	public void setWorkspaceDefaultDirectoryPath(String workspaceDefaultDirectoryPath) {
		this.mWorkspaceDefaultDirectoryPath = workspaceDefaultDirectoryPath;
	}
	
	public void setWorkspaceDefaultBuildScriptFileName(String workspaceDefaultBuildScriptFileName) {
		this.mWorkspaceDefaultBuildScriptFileName = workspaceDefaultBuildScriptFileName;
	}
	
	private void setUpWorkspaceDir(Workspace workspace) {
		FileUtil fileUtil = new FileUtil();
		fileUtil.createDirectoryIfNotExist(workspace.getDirectoryPath());
	}
}
