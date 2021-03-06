package com.techburg.autospring.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@NamedQueries({ @NamedQuery(name = "findWorkspaceByPath", query = "select bo from WorkspaceEntity bo where bo.mDirectoryPath = :path") })
@Table(name = "workspace")
public class WorkspaceEntity {
	@Id
	@GeneratedValue
	private long id;

	@Column(name = "script_file_path")
	private String mScriptFilePath;

	@Column(name = "directory_path")
	private String mDirectoryPath;

	@Column(name = "description")
	private String mDescription;

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

	public String getDescription() {
		return mDescription;
	}

	public void setDescription(String description) {
		this.mDescription = description;
	}
}
