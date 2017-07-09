package com.techburg.autospring.model.query;

public class WorkspacePersistenceQuery extends BasePersistenceQuery {

	public static class WorkspaceDataRange extends DataRange {
		public static final int PATH_MATCH = 5;
	}

	/**
	 * Path to workspace directory
	 */
	private String path;

	/**
	 * @return the path
	 */
	public String getPath() {
		return path;
	}

	/**
	 * Create the query to retrieve workspace by id
	 * 
	 * @param id
	 * @return
	 */
	public static WorkspacePersistenceQuery createQueryById(long id) {
		WorkspacePersistenceQuery query = new WorkspacePersistenceQuery();
		query.dataRange = DataRange.ID_MATCH;
		query.id = id;
		return query;
	}

	/**
	 * Create the query to retrieve all workspaces
	 * 
	 * @return
	 */
	public static WorkspacePersistenceQuery createWorkspaceQueryForAll() {
		WorkspacePersistenceQuery query = new WorkspacePersistenceQuery();
		query.dataRange = DataRange.ALL;
		return query;
	}

	/**
	 * Create the query to retrieve workspace matching the specified path
	 * 
	 * @param path
	 * @return
	 */
	public static WorkspacePersistenceQuery createWorkspaceQueryForPath(String path) {
		WorkspacePersistenceQuery query = new WorkspacePersistenceQuery();
		query.dataRange = WorkspaceDataRange.PATH_MATCH;
		query.path = path;
		return query;
	}

	/**
	 * Private constructor
	 */
	private WorkspacePersistenceQuery() {

	}
}
