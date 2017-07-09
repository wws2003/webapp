package com.techburg.autospring.service.abstr;

import java.util.List;

import com.techburg.autospring.model.business.Workspace;
import com.techburg.autospring.model.query.WorkspacePersistenceQuery;

public interface IWorkspacePersistenceService {
	/**
	 * Store the workspace
	 * 
	 * @param workspace
	 * @param isSyncMode
	 * @return
	 */
	public int persistWorkspace(Workspace workspace, boolean isSyncMode);

	/**
	 * Update the workspace
	 * 
	 * @param workspace
	 * @return
	 */
	public int updateWorkspace(Workspace workspace);

	/**
	 * Retrieve the workspaces matching the specified query
	 * 
	 * @param buildInfoList
	 * @param query
	 * @return
	 */
	public int loadWorkspace(List<Workspace> buildInfoList, WorkspacePersistenceQuery query);

	/**
	 * Remove the workspace with Id specified
	 * 
	 * @param id
	 * @return
	 */
	public int removeWorkspaceByID(long id);
}
