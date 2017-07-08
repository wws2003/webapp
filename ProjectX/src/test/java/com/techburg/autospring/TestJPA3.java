package com.techburg.autospring;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.techburg.autospring.model.business.BuildInfo;
import com.techburg.autospring.model.business.BuildInfo.Status;
import com.techburg.autospring.model.business.Workspace;
import com.techburg.autospring.model.query.BuildInfoPersistenceQuery;
import com.techburg.autospring.model.query.WorkspacePersistenceQuery;
import com.techburg.autospring.model.query.BasePersistenceQuery.DataRange;
import com.techburg.autospring.service.abstr.IBuildInfoPersistenceService;
import com.techburg.autospring.service.abstr.IWorkspacePersistenceService;
import com.techburg.autospring.service.abstr.PersistenceResult;

public class TestJPA3 {

	private IBuildInfoPersistenceService mBuildInfoPersistenceService;
	private IWorkspacePersistenceService mBuildScriptPersistenceService;

	@Before
	public void setUp() throws Exception {
		//Retrieve mPersistenceService by factory method
		String xmlPath = "file:src/main/webapp/WEB-INF/spring-conf/springmvc-conf.xml";
		try {
			ApplicationContext applicationContext = new ClassPathXmlApplicationContext(xmlPath);
			//applicationContext is also a bean factory...
			mBuildInfoPersistenceService = applicationContext.getBean("buildInfoPersistenceService", IBuildInfoPersistenceService.class);
			mBuildScriptPersistenceService = applicationContext.getBean("buildScriptPersistenceService", IWorkspacePersistenceService.class);
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
	public void testPersist() {
		assertNotNull(mBuildInfoPersistenceService);
		assertNotNull(mBuildScriptPersistenceService);
		
		Workspace buildScript = new Workspace();
		buildScript.setScriptFilePath("gafin.vn");
		assertEquals(mBuildScriptPersistenceService.persistWorkspace(buildScript, true), PersistenceResult.PERSISTENCE_SUCCESSFUL);

		
		List<Workspace> buildScripts = new ArrayList<Workspace>();
		WorkspacePersistenceQuery query2 = new WorkspacePersistenceQuery();
		query2.dataRange = DataRange.ID_MATCH;
		query2.id = 1;
		mBuildScriptPersistenceService.loadWorkspace(buildScripts, query2);
		
		assertEquals(buildScripts.size(), 1);
		
		BuildInfo buildInfo = new BuildInfo();
		buildInfo.setLogFilePath("LogFilePath");
		buildInfo.setStatus(Status.BUILD_SUCCESSFUL);
		buildInfo.setBeginTimeStamp(new Date());
		buildInfo.setEndTimeStamp(new Date());
		buildInfo.setWorkspace(buildScripts.get(0));
		
		assertEquals(mBuildInfoPersistenceService.persistBuildInfo(buildInfo), PersistenceResult.PERSISTENCE_SUCCESSFUL);
	
		List<BuildInfo> buildInfoList = new ArrayList<BuildInfo>();
		BuildInfoPersistenceQuery query = new BuildInfoPersistenceQuery();
		query.dataRange = DataRange.ALL;
		mBuildInfoPersistenceService.loadPersistedBuildInfo(buildInfoList, query);
		
		assertEquals(1, buildInfoList.size());
		Workspace targetBuildScript = buildInfoList.get(0).getWorkspace();
		assertNotNull(targetBuildScript);
		assertEquals("gafin.vn", targetBuildScript.getScriptFilePath());
	}
	
	@Test
	public void testUpdate() {
		Workspace buildScript = new Workspace();
		buildScript.setScriptFilePath("gafin.vn");
		assertEquals(mBuildScriptPersistenceService.persistWorkspace(buildScript, true), PersistenceResult.PERSISTENCE_SUCCESSFUL);

		List<Workspace> buildScripts = new ArrayList<Workspace>();
		WorkspacePersistenceQuery query2 = new WorkspacePersistenceQuery();
		query2.dataRange = DataRange.ALL;
		mBuildScriptPersistenceService.loadWorkspace(buildScripts, query2);
		
		assertEquals(1, buildScripts.size());
		Workspace buildScriptToUpdate = buildScripts.get(0);
		buildScriptToUpdate.setScriptFilePath("cafef.vn");
		
		assertEquals(PersistenceResult.UPDATE_SUCCESSFUL, mBuildScriptPersistenceService.updateWorkspace(buildScriptToUpdate));
	}

}
