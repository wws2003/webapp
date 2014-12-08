package com.techburg.autospring.model;

public class BasePersistenceQuery {
	public static class DataRange {
		public static final int ALL = 0;
		public static final int FIRST_MATCH = 1;
		public static final int ANY_MATCH = 2;
		public static final int LIMITED_MATCH = 3;
		public static final int ID_MATCH = 4;
	}
	
	public int dataRange;
	public long id;
	public long firstId;
	public long lastId;
}
