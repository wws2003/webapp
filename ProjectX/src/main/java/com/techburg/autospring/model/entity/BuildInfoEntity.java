package com.techburg.autospring.model.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@NamedQueries({
	@NamedQuery(name = "findNumberOfRecords",
			query = "select count(bi.id) from BuildInfoEntity bi"),
	@NamedQuery(name = "findNumberOfRecordsOfWorkspace",
			query = "select count(bi.id) from BuildInfoEntity bi where bi.mWorkspaceEntity.id = :workspace"),
})

@Table(name = "build_info")
public class BuildInfoEntity {
	@Id
	@GeneratedValue
	private long id;

	@Column(name = "status")
	private int status;

	@Column(name = "begin_build_time")
	private Date beginBuildTime;

	@Column(name = "end_build_time")
	private Date endBuildTime;

	@Column(name = "log_file_path")
	private String logFilePath;

	@ManyToOne(targetEntity = WorkspaceEntity.class)
	@JoinColumn(name = "workspace_id")
	private WorkspaceEntity mWorkspaceEntity;

	public BuildInfoEntity() {

	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Date getBeginTimeStamp() {
		return beginBuildTime;
	}

	public void setBeginTimeStamp(Date beginTimeStamp) {
		this.beginBuildTime = beginTimeStamp;
	}

	public Date getEndTimeStamp() {
		return endBuildTime;
	}

	public void setEndTimeStamp(Date endTimeStamp) {
		this.endBuildTime = endTimeStamp;
	}

	public String getLogFilePath() {
		return logFilePath;
	}

	public void setLogFilePath(String logFilePath) {
		this.logFilePath = logFilePath;
	}

	public void setWorkspaceEntity(WorkspaceEntity workspaceEntity) {
		mWorkspaceEntity = workspaceEntity;
	}

	public WorkspaceEntity getWorkspaceEntity() {
		return mWorkspaceEntity;
	}
}
