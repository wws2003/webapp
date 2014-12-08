package com.techburg.autospring.factory.impl;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import com.techburg.autospring.db.task.abstr.AbstractDBTask;
import com.techburg.autospring.factory.abstr.IDBTaskLockFactory;

public class DBTaskLockFactorySimpleImpl implements IDBTaskLockFactory {

	private ReadWriteLock mLock;
	
	public DBTaskLockFactorySimpleImpl() {
		mLock = new ReentrantReadWriteLock(true);
	}
	
	@Override
	public Lock getReadWriteLockInstanceForDBTask(AbstractDBTask dbTask) {
		return dbTask.getDBReadWriteMode() == AbstractDBTask.DB_READ_MODE ? mLock.readLock() : mLock.writeLock();
	}

}
