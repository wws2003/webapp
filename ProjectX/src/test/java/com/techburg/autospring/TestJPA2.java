package com.techburg.autospring;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.techburg.autospring.model.business.BuildInfo;
import com.techburg.autospring.model.query.BuildInfoPersistenceQuery;
import com.techburg.autospring.model.query.BasePersistenceQuery.DataRange;
import com.techburg.autospring.service.abstr.IBuildInfoPersistenceService;
import com.techburg.autospring.service.abstr.PersistenceResult;

public class TestJPA2 {

	private IBuildInfoPersistenceService mBuildInfoPersistenceService;
	private static final long PERSISTED_BUILD_INFO_NUMBER = 23;
	
	@Before
	public void setUp() throws Exception {
		//Retrieve mPersistenceService by factory method
		String xmlPath = "file:src/main/webapp/WEB-INF/spring-conf/springmvc-conf.xml";
		try {
			ApplicationContext applicationContext = new ClassPathXmlApplicationContext(xmlPath);
			//applicationContext is also a bean factory...
			mBuildInfoPersistenceService = applicationContext.getBean("buildInfoPersistenceService", IBuildInfoPersistenceService.class);
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
		assertNotNull(mBuildInfoPersistenceService);
		List<BuildInfo> builtList = new ArrayList<BuildInfo>();
		long numberOfPersistedBuildInfo = mBuildInfoPersistenceService.getNumberOfPersistedBuildInfo(2);
		assertEquals(PERSISTED_BUILD_INFO_NUMBER, numberOfPersistedBuildInfo);
		BuildInfoPersistenceQuery query = new BuildInfoPersistenceQuery();
		query.dataRange = DataRange.LIMITED_MATCH;
		query.firstId = 1;
		query.lastId = 12;
		assertEquals(PersistenceResult.LOAD_SUCCESSFUL, mBuildInfoPersistenceService.loadPersistedBuildInfo(builtList, query));
		assertEquals(12, builtList.size());
	}

}
