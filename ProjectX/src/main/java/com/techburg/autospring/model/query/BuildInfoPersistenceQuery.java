package com.techburg.autospring.model.query;

public class BuildInfoPersistenceQuery extends BasePersistenceQuery {

	public static class BuildInfoDataRange extends DataRange {
		public static final int PAGE_MATCH = 5;
	}

	private long workspaceId = -1;
	private int page = -1;
	private int nbInstancePerPage = -1;

	/**
	 * @return the workspace
	 */
	public long getWorkspaceId() {
		return workspaceId;
	}

	/**
	 * @return the page
	 */
	public int getPage() {
		return page;
	}

	/**
	 * @return the nbInstancePerPage
	 */
	public int getNbInstancePerPage() {
		return nbInstancePerPage;
	}

	/**
	 * Generate the query instance
	 * 
	 * @param workspaceId
	 * @param page
	 * @param nbInstancePerPage
	 * @return
	 */
	public static BuildInfoPersistenceQuery createBuildInfoQuery(long workspaceId, int page, int nbInstancePerPage) {
		BuildInfoPersistenceQuery query = new BuildInfoPersistenceQuery();
		query.workspaceId = workspaceId;
		query.page = page;
		query.nbInstancePerPage = nbInstancePerPage;
		query.dataRange = BuildInfoDataRange.PAGE_MATCH;
		return query;
	}

	/**
	 * Generate the query instance
	 * 
	 * @return
	 */
	public static BuildInfoPersistenceQuery createBuildInfoQueryForAll() {
		BuildInfoPersistenceQuery query = new BuildInfoPersistenceQuery();
		query.dataRange = BuildInfoDataRange.ALL;
		return query;
	}

	/**
	 * Generate the query instance
	 * 
	 * @param lastReceivedId
	 * @return
	 */
	public static BuildInfoPersistenceQuery createBuildInfoQueryByLastReceivedId(long lastReceivedId) {
		BuildInfoPersistenceQuery query = new BuildInfoPersistenceQuery();
		query.dataRange = DataRange.LIMITED_MATCH;
		query.firstId = lastReceivedId + 1;
		query.lastId = Long.MAX_VALUE;
		return query;
	}

	/**
	 * Generate the query instance
	 * 
	 * @param id
	 * @return
	 */
	public static BuildInfoPersistenceQuery createBuildInfoQueryById(long id) {
		BuildInfoPersistenceQuery query = new BuildInfoPersistenceQuery();
		query.dataRange = DataRange.ID_MATCH;
		query.id = id;
		return query;
	}

	/**
	 * Private constructor
	 */
	private BuildInfoPersistenceQuery() {

	}
}
