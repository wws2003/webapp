package com.techburg.autospring;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.techburg.autospring.model.business.BrowsingObject;
import com.techburg.autospring.service.abstr.IBrowsingObjectPersistentService;
import com.techburg.autospring.service.abstr.IBrowsingServiceDelegate;

public class TestJPA4 {

	private IBrowsingObjectPersistentService mBrowsingObjectPersistentService;
	private IBrowsingServiceDelegate mBrowsingServiceDelegate;
	
	private static final int ROOT_CHILD_NUMBER = 3;
	
	@Before
	public void setUp() throws Exception {
		//Retrieve mPersistenceService by factory method
		String xmlPath = "file:src/main/webapp/WEB-INF/spring-conf/springmvc-conf.xml";
		try {
			ApplicationContext applicationContext = new ClassPathXmlApplicationContext(xmlPath);
			//applicationContext is also a bean factory...
			mBrowsingServiceDelegate = applicationContext.getBean("browsingServiceDelegate", IBrowsingServiceDelegate.class);
			mBrowsingObjectPersistentService = applicationContext.getBean("browsingObjectPersistentService", IBrowsingObjectPersistentService.class);	
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
		assertNotNull(mBrowsingObjectPersistentService);
		assertNotNull(mBrowsingServiceDelegate);
		
		BrowsingObject browsingObject = mBrowsingServiceDelegate.getBrowsingObjectById(3);
		assertNotNull(browsingObject);
		assertNotNull(browsingObject.getParent());
		assertEquals(2, browsingObject.getParent().getId());
		assertNull(browsingObject.getParent().getParent());
		
		BrowsingObject rootBrowsingObject = mBrowsingServiceDelegate.getBrowsingObjectById(1);
		assertNotNull(rootBrowsingObject);
		
		List<BrowsingObject> subBrowingObjects = new ArrayList<BrowsingObject>();
		mBrowsingServiceDelegate.getChildBrowsingObject(rootBrowsingObject, subBrowingObjects);
		assertEquals(ROOT_CHILD_NUMBER, subBrowingObjects.size());
	}
}
