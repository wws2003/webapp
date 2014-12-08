package com.techburg.autospring;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.techburg.autospring.model.BasePersistenceQuery.DataRange;
import com.techburg.autospring.model.BuildInfoPersistenceQuery;
import com.techburg.autospring.model.business.BuildInfo;
import com.techburg.autospring.model.business.BuildInfo.Status;
import com.techburg.autospring.service.abstr.IBuildInfoPersistenceService;
import com.techburg.autospring.service.abstr.PersistenceResult;

public class TestJPA {

	//TODO Inject mPersistenceService if possible
	private IBuildInfoPersistenceService mPersistenceService;

	@Before
	public void setUp() throws Exception {
		//Retrieve mPersistenceService by factory method
		String xmlPath = "file:src/main/webapp/WEB-INF/spring-conf/springmvc-conf.xml";
		try {
			ApplicationContext applicationContext = new ClassPathXmlApplicationContext(xmlPath);
			//applicationContext is also a bean factory...
			mPersistenceService = applicationContext.getBean("buildInfoPersistenceService", IBuildInfoPersistenceService.class);
		}
		catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	@After
	public void tearDown() throws Exception {
		//TODO Release resource if necessary
	}

	@Test
	public void test() {
		assertNotNull(mPersistenceService);
		BuildInfo buildInfo = new BuildInfo();
		buildInfo.setLogFilePath("LogFilePath");
		buildInfo.setStatus(Status.BUILD_SUCCESSFUL);
		buildInfo.setBeginTimeStamp(new Date());
		buildInfo.setEndTimeStamp(new Date());
		assertEquals(mPersistenceService.persistBuildInfo(buildInfo), PersistenceResult.PERSISTENCE_SUCCESSFUL);
		List<BuildInfo> buildInfoList = new ArrayList<BuildInfo>();
		BuildInfoPersistenceQuery query = new BuildInfoPersistenceQuery();
		query.dataRange = DataRange.ALL;
		try {
			mPersistenceService.loadPersistedBuildInfo(buildInfoList, query);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		assertTrue(buildInfoList.size() > 0);
		
		//assertEquals(mPersistenceService.removeBuildInfoByID(1), PersistenceResult.REMOVE_SUCCESSFUL);
		assertEquals(mPersistenceService.removeBuildInfoByID(13333), PersistenceResult.REMOVE_FAILED);
	}
}
