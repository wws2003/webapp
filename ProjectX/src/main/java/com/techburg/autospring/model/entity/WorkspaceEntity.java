package com.techburg.autospring.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="workspace")
public class WorkspaceEntity {
	@Id
	@GeneratedValue
	private long id;
	
	@Column(name="script_file_path")
	private String mScriptFilePath;

	@Column(name="directory_path")
	private String mDirectoryPath;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
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
