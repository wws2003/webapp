package com.techburg.autospring;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.techburg.autospring.db.task.abstr.IDBTaskExecutor;
import com.techburg.autospring.factory.abstr.IWorkspaceFactory;
import com.techburg.autospring.model.business.BrowsingObject;
import com.techburg.autospring.model.business.Workspace;
import com.techburg.autospring.service.abstr.IBrowsingObjectPersistentService;
import com.techburg.autospring.service.abstr.IWorkspacePersistenceService;

public class TestBrowsingObjectDB {

	//private EntityManagerFactory mEntityManagerFactory;
	private IBrowsingObjectPersistentService mBrowsingObjectService;
	private IWorkspaceFactory mWorkspaceFactory;
	private IWorkspacePersistenceService mWorkspacePersistenceService;
	private IDBTaskExecutor mTaskExecutor;
	
	@Before
	public void setUp() throws Exception {
		//Retrieve mPersistenceService by factory method
		String xmlPath = "file:src/main/webapp/WEB-INF/spring-conf/springmvc-conf.xml";
		try {
			ApplicationContext applicationContext = new ClassPathXmlApplicationContext(xmlPath);
			//applicationContext is also a bean factory...
			//mEntityManagerFactory = applicationContext.getBean("entityManagerFactory", EntityManagerFactory.class);
			mBrowsingObjectService = applicationContext.getBean("browsingObjectPersistentService", IBrowsingObjectPersistentService.class);
			mWorkspaceFactory = applicationContext.getBean("workspaceFactory", IWorkspaceFactory.class);
			mTaskExecutor = applicationContext.getBean("dBTaskExecutor", IDBTaskExecutor.class);
			mWorkspacePersistenceService = applicationContext.getBean("workspacePersistenceService", IWorkspacePersistenceService.class);
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
		BrowsingObject browsingObject = mBrowsingObjectService.getBrowsingObjectById(1);
		assertNotNull(browsingObject);
		List<BrowsingObject> childBrowsingObjects = new ArrayList<BrowsingObject>();
		mBrowsingObjectService.getChildBrowsingObjects(browsingObject, childBrowsingObjects);
		assertTrue(childBrowsingObjects.size() > 0);
		
		//Test new workspace
		Workspace newWorkspace = mWorkspaceFactory.createWorkspace("workspace5", "script.sh");
		mWorkspacePersistenceService.persistWorkspace(newWorkspace, true);
		
		//mTaskExecutor.waitAllFinish();
	}

}
