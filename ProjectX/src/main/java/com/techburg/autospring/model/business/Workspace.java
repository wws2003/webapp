package com.techburg.autospring.model.business;

public class Workspace {
	private long mId;
	private String mScriptFilePath;
	private String mDirectoryPath;
	
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

}
