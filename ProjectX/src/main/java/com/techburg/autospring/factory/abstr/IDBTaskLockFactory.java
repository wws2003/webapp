package com.techburg.autospring.factory.abstr;

import java.util.concurrent.locks.Lock;

import com.techburg.autospring.db.task.abstr.AbstractDBTask;

public interface IDBTaskLockFactory {
	Lock getReadWriteLockInstanceForDBTask(AbstractDBTask dbTask);
}
