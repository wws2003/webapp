package com.techburg.autospring;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManagerFactory;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.techburg.autospring.bo.abstr.IBuildInfoBo;
import com.techburg.autospring.db.task.abstr.AbstractDBTask;
import com.techburg.autospring.db.task.abstr.IDBTaskExecutor;
import com.techburg.autospring.db.task.impl.BuildInfoDBTaskImpl;
import com.techburg.autospring.model.business.BuildInfo;
import com.techburg.autospring.model.query.BuildInfoPersistenceQuery;

public class TestDBTaskConcurrency {

	private IDBTaskExecutor mTaskExecutor;
	private IBuildInfoBo mBuildInfoBo;
	private EntityManagerFactory mEntityManagerFactory;

	@Before
	public void setUp() throws Exception {
		// Retrieve mPersistenceService by factory method
		String xmlPath = "file:src/main/webapp/WEB-INF/spring-conf/springmvc-conf.xml";
		try {
			ApplicationContext applicationContext = new ClassPathXmlApplicationContext(xmlPath);
			// applicationContext is also a bean factory...
			mTaskExecutor = applicationContext.getBean("dBTaskExecutor", IDBTaskExecutor.class);
			mEntityManagerFactory = applicationContext.getBean("entityManagerFactory", EntityManagerFactory.class);
			mBuildInfoBo = applicationContext.getBean("buildInfoBo", IBuildInfoBo.class);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() {
		testReadRead();
		testReadWrite();
		// persistOneBuildInfo();
	}

	private void testReadRead() {
		/*
		 * Try many threads to read from DB. Read task: select from build_info
		 */
		int numberOfThreads = 100;
		Runnable task = new Runnable() {
			@Override
			public void run() {
				doReadTask();
			}
		};

		runThreadsOfTask(numberOfThreads, task);
	}

	private void testReadWrite() {
		/*
		 * Try many threads to read and write against DB. Read task: select from
		 * build_info Write task: insert new build_info
		 */
		int numberOfThreads = 100;
		Runnable task = new Runnable() {
			@Override
			public void run() {
				boolean toDoReadTask = Math.random() > 0.5;
				if (toDoReadTask) {
					doReadTask();
				} else {
					doWriteTask();
				}
			}
		};

		runThreadsOfTask(numberOfThreads, task);
	}

	private void doReadTask() {
		// Sleep a random duration to increase the concurrency
		try {
			Thread.sleep((long) (100 * Math.random()));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		loadOneBuildInfo();
	}

	private void doWriteTask() {
		// Sleep a random duration to increase the concurrency
		try {
			Thread.sleep((long) (100 * Math.random()));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		persistOneBuildInfo();
	}

	private void loadOneBuildInfo() {
		// Create the DB read operation
		BuildInfoDBTaskImpl dbTask = new BuildInfoDBTaskImpl(mBuildInfoBo, mEntityManagerFactory);
		BuildInfoPersistenceQuery paramBuildInfoLoadQuery = BuildInfoPersistenceQuery.createBuildInfoQueryById((long) (Math
				.random() * 100));
		dbTask.setLoadParam(paramBuildInfoLoadQuery);
		dbTask.setScheduleMode(AbstractDBTask.SCHEDULE_ASYNC_MODE);

		// Execute the DB read operation
		mTaskExecutor.executeDBTask(dbTask);

		List<BuildInfo> buildInfoList = new ArrayList<BuildInfo>();
		dbTask.getLoadResult(buildInfoList);

		System.out.println("------------------------------------Number of results = " + buildInfoList.size() + " for id = "
				+ paramBuildInfoLoadQuery.getId() + "------------------------------------");
	}

	private void persistOneBuildInfo() {
		// Create the DB read operation
		BuildInfoDBTaskImpl dbTask = new BuildInfoDBTaskImpl(mBuildInfoBo, mEntityManagerFactory);
		BuildInfo buildInfoToPersist = new BuildInfo();
		buildInfoToPersist.setBeginTimeStamp(new Date());
		buildInfoToPersist.setEndTimeStamp(new Date());
		buildInfoToPersist.setStatus(-60);
		dbTask.setPersistParam(buildInfoToPersist);
		dbTask.setScheduleMode(AbstractDBTask.SCHEDULE_SYNC_MODE);

		mTaskExecutor.executeDBTask(dbTask);

		int persistResult = dbTask.getPersistResult();
		System.out.println("------------------------------------Persist result = " + persistResult
				+ "------------------------------------");
	}

	private void runThreadsOfTask(int numberOfThreads, Runnable task) {
		List<Thread> threads = new ArrayList<Thread>();
		for (int i = 0; i < numberOfThreads; i++) {
			Thread t = new Thread(task);
			threads.add(t);
		}
		for (Thread t : threads) {
			t.start();
		}
		for (Thread t : threads) {
			try {
				t.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
