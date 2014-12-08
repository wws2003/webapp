package com.techburg.autospring;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.techburg.autospring.factory.abstr.IBuildTaskFactory;
import com.techburg.autospring.task.abstr.IBuildTask;
import com.techburg.autospring.task.abstr.IBuildTask.BuildTaskResult;

public class TestBuildProcess {

	private IBuildTaskFactory mBuildTaskFactory;

	@Before
	public void setUp() throws Exception {
		//Retrieve mPersistenceService by factory method
		String xmlPath = "file:src/main/webapp/WEB-INF/spring-conf/springmvc-conf.xml";
		try {
			ApplicationContext applicationContext = new ClassPathXmlApplicationContext(xmlPath);
			//applicationContext is also a bean factory...
			mBuildTaskFactory = applicationContext.getBean("buildTaskFactory", IBuildTaskFactory.class);
		}
		catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() {
		assertNotNull(mBuildTaskFactory);
		IBuildTask buildTask = mBuildTaskFactory.getNewBuildTask();
		int ret = buildTask.execute();
		assertEquals(BuildTaskResult.SUCCESSFUL, ret);
	}

}
