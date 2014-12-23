package com.techburg.autospring.bo.impl;

import com.techburg.autospring.bo.abstr.IWorkspaceBo;
import com.techburg.autospring.model.business.Workspace;
import com.techburg.autospring.model.entity.WorkspaceEntity;

public class WorkspaceBoImpl implements IWorkspaceBo {
	@Override
	public WorkspaceEntity getEntityFromBusinessObject(Workspace workspace) {
		if (workspace == null) {
			return null;
		}
		WorkspaceEntity entity = new WorkspaceEntity();
		entity.setId(workspace.getId());
		entity.setScriptFilePath(workspace.getScriptFilePath());
		entity.setDirectoryPath(workspace.getDirectoryPath());
		entity.setDescription(workspace.getDescription());
		return entity;
	}
	
	@Override
	public Workspace getBusinessObjectFromEntity(WorkspaceEntity entity) {
		if (entity == null) {
			return null;
		}
		Workspace workspace = new Workspace();
		workspace.setId(entity.getId());
		workspace.setScriptFilePath(entity.getScriptFilePath());
		workspace.setDirectoryPath(entity.getDirectoryPath());
		workspace.setDescription(entity.getDescription());
		return workspace;
	}
}
