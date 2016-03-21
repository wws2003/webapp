package com.techburg.autospring.service.abstr;

import java.util.List;

import com.techburg.autospring.model.WorkspacePersistenceQuery;
import com.techburg.autospring.model.business.Workspace;

public interface IWorkspacePersistenceService {
	public int persistWorkspace(Workspace workspace, boolean isSyncMode);
	public int updateWorkspace(Workspace workspace);
	public int loadWorkspace(List<Workspace> buildInfoList, WorkspacePersistenceQuery query);
	public int removeWorkspaceByID(long id);
}
