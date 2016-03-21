package com.techburg.autospring.model.business;

import java.io.File;

public class Workspace {
	private long mId;
	private String mScriptFilePath;
	private String mDirectoryPath;
	private String mDescription;
	
	public long getId() {
		return mId;
	}
	public void setId(long mId) {
		this.mId = mId;
	}
	
	public String getScriptFilePath() {
		return mScriptFilePath;
	}
	public void setScriptFilePath(String scriptFilePath) {
		this.mScriptFilePath = scriptFilePath;
	}
	
	public String getDirectoryPath() {
		return mDirectoryPath;
	}
	public void setDirectoryPath(String directoryPath) {
		this.mDirectoryPath = directoryPath;
	}
	
	public String getDescription() {
		return mDescription;
	}
	public void setDescription(String description) {
		this.mDescription = description;
	}
	
	public String getName() {
		File workspaceFolder = new File(mDirectoryPath);
		if(workspaceFolder.exists() && workspaceFolder.isDirectory()) {
			return workspaceFolder.getName();
		}
		return "Unknown name";
	}

}
