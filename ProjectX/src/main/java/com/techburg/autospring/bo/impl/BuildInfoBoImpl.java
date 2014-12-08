package com.techburg.autospring.bo.impl;

import org.springframework.beans.factory.annotation.Autowired;

import com.techburg.autospring.bo.abstr.IBuildInfoBo;
import com.techburg.autospring.bo.abstr.IWorkspaceBo;
import com.techburg.autospring.model.business.BuildInfo;
import com.techburg.autospring.model.entity.BuildInfoEntity;

public class BuildInfoBoImpl implements IBuildInfoBo {
	
	private IWorkspaceBo mWorkspaceBo;
	
	@Autowired
	public void setWorkspaceBo(IWorkspaceBo workspaceBo) {
		mWorkspaceBo = workspaceBo;
	}
	
	public BuildInfoEntity getEntityFromBusinessObject(BuildInfo buildInfo) {
		BuildInfoEntity entity = new BuildInfoEntity();
		entity.setBeginTimeStamp(buildInfo.getBeginTimeStamp());
		entity.setEndTimeStamp(buildInfo.getEndTimeStamp());
		entity.setId(buildInfo.getId());
		entity.setStatus(buildInfo.getStatus());
		entity.setLogFilePath(buildInfo.getLogFilePath());
		entity.setWorkspaceEntity(mWorkspaceBo.getEntityFromBusinessObject(buildInfo.getWorkspace()));
		return entity;
	}

	public BuildInfo getBusinessObjectFromEntity(BuildInfoEntity entity) {
		BuildInfo buildInfo = new BuildInfo();
		buildInfo.setBeginTimeStamp(entity.getBeginTimeStamp());
		buildInfo.setEndTimeStamp(entity.getEndTimeStamp());
		buildInfo.setId(entity.getId());
		buildInfo.setStatus(entity.getStatus());
		buildInfo.setLogFilePath(entity.getLogFilePath());
		buildInfo.setWorkspace(mWorkspaceBo.getBusinessObjectFromEntity(entity.getWorkspaceEntity()));
		return buildInfo;
	}
}
