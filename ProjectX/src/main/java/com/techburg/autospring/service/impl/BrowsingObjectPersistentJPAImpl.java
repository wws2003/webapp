package com.techburg.autospring.service.impl;

import java.util.List;

import javax.persistence.EntityManagerFactory;

import org.springframework.beans.factory.annotation.Autowired;

import com.techburg.autospring.bo.abstr.IBrowsingObjectBo;
import com.techburg.autospring.db.task.abstr.AbstractDBTask;
import com.techburg.autospring.db.task.abstr.IDBTaskExecutor;
import com.techburg.autospring.db.task.impl.BrowsingObjectDBTaskImpl;
import com.techburg.autospring.db.task.impl.NoLockingDBTaskExecutorImpl;
import com.techburg.autospring.model.business.BrowsingObject;
import com.techburg.autospring.service.abstr.IBrowsingObjectPersistentService;
import com.techburg.autospring.service.abstr.IBrowsingServiceDelegate;

public class BrowsingObjectPersistentJPAImpl implements IBrowsingObjectPersistentService {

	private EntityManagerFactory mEntityManagerFactory = null;
	private IBrowsingObjectBo mBrowsingObjectBo = null;
	private IDBTaskExecutor mDBTaskExecutor = null;
	private IBrowsingServiceDelegate mBrowsingServiceDelegate = null; 
	
	@Autowired
	public BrowsingObjectPersistentJPAImpl(EntityManagerFactory entityManagerFactory) {
		mEntityManagerFactory = entityManagerFactory;
	}

	@Autowired
	public void setBuildInfoBo(IBrowsingObjectBo browsingObjectBo) {
		mBrowsingObjectBo = browsingObjectBo;
	}
	
	@Autowired
	public void setBrowsingServiceDelegate(IBrowsingServiceDelegate browsingServiceDelegate) {
		mBrowsingServiceDelegate = browsingServiceDelegate;
	}
	
	@Autowired
	public void setDBTaskExecutor(IDBTaskExecutor dbTaskExecutor) {
		mDBTaskExecutor = dbTaskExecutor;
	}
	
	@Override
	public int persistBrowsingObject(BrowsingObject browsingObject) {
		BrowsingObjectDBTaskImpl persistTask = new BrowsingObjectDBTaskImpl(mBrowsingObjectBo, mEntityManagerFactory);
		persistTask.setPersistParams(browsingObject);
		persistTask.setScheduleMode(AbstractDBTask.SCHEDULE_SYNC_MODE);
		mDBTaskExecutor.executeDBTask(persistTask);
		return persistTask.getPersistResult();
	}

	@Override
	public BrowsingObject getBrowsingObjectById(long id) {
		BrowsingObjectDBTaskImpl findTask = new BrowsingObjectDBTaskImpl(mBrowsingObjectBo, mEntityManagerFactory);
		findTask.setGetByIdParam(id);
		findTask.setScheduleMode(AbstractDBTask.SCHEDULE_SYNC_MODE);
		mDBTaskExecutor.executeDBTask(findTask);
		return findTask.getGetByIdResult();
	}

	@Override
	public BrowsingObject getBrowsingObjectByPath(String path) {
		BrowsingObjectDBTaskImpl findTask = new BrowsingObjectDBTaskImpl(mBrowsingObjectBo, mEntityManagerFactory);
		findTask.setGetByPathParam(path);
		findTask.setScheduleMode(AbstractDBTask.SCHEDULE_SYNC_MODE);
		mDBTaskExecutor.executeDBTask(findTask);
		return findTask.getGetByPathResult();
	}

	@Override
	public int getChildBrowsingObjects(BrowsingObject parent, List<BrowsingObject> children) {
		BrowsingObjectDBTaskImpl getChildrenTask = new BrowsingObjectDBTaskImpl(mBrowsingObjectBo, mEntityManagerFactory);
		getChildrenTask.setGetChildrenParam(parent);
		getChildrenTask.setScheduleMode(AbstractDBTask.SCHEDULE_SYNC_MODE);
		mDBTaskExecutor.executeDBTask(getChildrenTask);
		return getChildrenTask.getGetChildrenResult(children);
	}

	@Override
	public int removeAllBrowsingObject() {
		BrowsingObjectDBTaskImpl removeAllTask = new BrowsingObjectDBTaskImpl(mBrowsingObjectBo, mEntityManagerFactory);
		removeAllTask.setRemoveAllParam();
		removeAllTask.setScheduleMode(AbstractDBTask.SCHEDULE_SYNC_MODE);
		mDBTaskExecutor.executeDBTask(removeAllTask);
		return removeAllTask.getRemoveAllResult();
	}

	@Override
	public int persistBrowsingObjectInDirectory(String directoryPath, boolean isSyncMode) {
		return mBrowsingServiceDelegate.persistBrowsingObjectInDirectory(directoryPath, isSyncMode ? nonblokingPrototype() : this);
	}

	@Override
	public int removeBrowsingObjectInDirectory(String directoryPath, boolean isSyncMode) {
		return mBrowsingServiceDelegate.removeBrowsingObjectInDirectory(directoryPath, isSyncMode ? nonblokingPrototype() : this);
	}

	@Override
	public int removeBrowsingObjectById(long id) {
		BrowsingObjectDBTaskImpl removeTask = new BrowsingObjectDBTaskImpl(mBrowsingObjectBo, mEntityManagerFactory);
		removeTask.setRemoveByIdParam(id);
		removeTask.setScheduleMode(AbstractDBTask.SCHEDULE_SYNC_MODE);
		mDBTaskExecutor.executeDBTask(removeTask);
		return removeTask.getRemoveByIdResult();
	}
	
	private IBrowsingObjectPersistentService nonblokingPrototype() {
		BrowsingObjectPersistentJPAImpl instance = new BrowsingObjectPersistentJPAImpl(mEntityManagerFactory);
		instance.setBuildInfoBo(mBrowsingObjectBo);
		instance.setDBTaskExecutor(new NoLockingDBTaskExecutorImpl());
		return instance;
	}

}
