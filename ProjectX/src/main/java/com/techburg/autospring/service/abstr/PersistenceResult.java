package com.techburg.autospring.service.abstr;

public class PersistenceResult {
	public static final int INVALID_QUERY = -1;
	public static final int PERSISTENCE_SUCCESSFUL = 0;
	public static final int PERSISTENCE_FAILED = 1;
	public static final int LOAD_SUCCESSFUL = 2;
	public static final int LOAD_FAILED = 3;
	public static final int REMOVE_SUCCESSFUL = 4;
	public static final int REMOVE_FAILED = 5;
	public static final int UPDATE_SUCCESSFUL = 6;
	public static final int UPDATE_FAILED = 7;
	public static final int REQUEST_QUEUED = 8;
}
