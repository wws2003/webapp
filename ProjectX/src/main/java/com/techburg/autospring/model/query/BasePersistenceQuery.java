package com.techburg.autospring.model.query;

public class BasePersistenceQuery {
	public static class DataRange {
		public static final int ALL = 0;
		public static final int FIRST_MATCH = 1;
		public static final int ANY_MATCH = 2;
		public static final int LIMITED_MATCH = 3;
		public static final int ID_MATCH = 4;
	}

	protected int dataRange;
	protected long id;
	protected long firstId;
	protected long lastId;

	/**
	 * @return the dataRange
	 */
	public int getDataRange() {
		return dataRange;
	}

	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}

	/**
	 * @return the firstId
	 */
	public long getFirstId() {
		return firstId;
	}

	/**
	 * @return the lastId
	 */
	public long getLastId() {
		return lastId;
	}

}
