package com.techburg.autospring.service.impl;

import java.util.List;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.springframework.beans.factory.annotation.Autowired;

import com.techburg.autospring.model.business.BrowsingObject;
import com.techburg.autospring.service.abstr.IBrowsingService;
import com.techburg.autospring.service.abstr.IBrowsingServiceDelegate;

public class BrowsingServiceImpl implements IBrowsingService {
	private boolean mIsConstructing = false;
	private IBrowsingServiceDelegate mBrowsingServiceDelegate;
	private ReentrantReadWriteLock mLock = new ReentrantReadWriteLock();

	@Autowired
	public void setBrowsingServiceDelegate(IBrowsingServiceDelegate browsingServiceDelegate) {
		mBrowsingServiceDelegate = browsingServiceDelegate;
	}

	@Override
	public int construct() throws Exception {
		mIsConstructing = true;
		int ret = mBrowsingServiceDelegate.construct();
		mIsConstructing = false;
		return ret;
	}

	@Override
	public boolean isBrowsingPossible() {
		return !mIsConstructing;
	}

	@Override
	public boolean acquireReadLock() throws Exception {
		return mLock.readLock().tryLock();
	}

	@Override
	public void releaseReadLock() throws Exception {
		mLock.readLock().unlock();
	}

	@Override
	public void acquireWriteLock() throws Exception {
		mLock.writeLock().lock();
	}

	@Override
	public void releaseWriteLock() throws Exception {
		mLock.writeLock().unlock();
	}

	@Override
	public BrowsingObject getBrowsingObjectById(long id) {
		return mBrowsingServiceDelegate.getBrowsingObjectById(id);
	}

	@Override
	public BrowsingObject getRootBrowsingObjectByPath(String path) {
		return mBrowsingServiceDelegate.getRootBrowsingObjectByPath(path);
	}

	@Override
	public void getChildBrowsingObject(BrowsingObject parent, List<BrowsingObject> children) {
		mBrowsingServiceDelegate.getChildBrowsingObject(parent, children);
	}
}
