package com.techburg.autospring.model.query;


public class BuildInfoPersistenceQuery extends BasePersistenceQuery {
	public static class BuildInfoDataRange extends DataRange {
		public static final int PAGE_MATCH = 5;
	}
	
	public long workspace = -1;
	public int page = -1;
	public int nbInstancePerPage = -1;
}
