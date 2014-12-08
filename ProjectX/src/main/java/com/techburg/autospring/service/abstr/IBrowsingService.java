package com.techburg.autospring.service.abstr;

import java.util.List;

import com.techburg.autospring.model.business.BrowsingObject;

public interface IBrowsingService {
	public static class ConstructResult{
		public static final int CONSTRUCT_FAILED = -1;
		public static final int CONSTRUCT_SUCCESSFUL = 0;
	}
	
	int construct() throws Exception;
	boolean isBrowsingPossible();

	boolean acquireReadLock() throws Exception; //Return true only if the read lock can be acquired

	void releaseReadLock() throws Exception;

	void acquireWriteLock() throws Exception;

	void releaseWriteLock() throws Exception;

	BrowsingObject getBrowsingObjectById(long id);
	BrowsingObject getRootBrowsingObjectByPath(String path);
	void getChildBrowsingObject(BrowsingObject parent, List<BrowsingObject> children);
}
