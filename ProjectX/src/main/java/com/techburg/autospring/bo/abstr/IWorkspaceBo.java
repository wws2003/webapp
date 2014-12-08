package com.techburg.autospring.bo.abstr;

import com.techburg.autospring.model.business.Workspace;
import com.techburg.autospring.model.entity.WorkspaceEntity;

public interface IWorkspaceBo {
	public WorkspaceEntity getEntityFromBusinessObject(Workspace workspace);
	public Workspace getBusinessObjectFromEntity(WorkspaceEntity entity);
}
