package com.techburg.autospring.factory.impl;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

import com.techburg.autospring.factory.abstr.IBuildTaskFactory;
import com.techburg.autospring.service.abstr.IWorkspacePersistenceService;
import com.techburg.autospring.task.abstr.IBuildTask;
import com.techburg.autospring.task.impl.BuildTaskUnixScriptImpl;

public class UnixScriptBuildTaskFactoryImpl implements IBuildTaskFactory, InitializingBean {

	//private IBuildInfoPersistenceService mPersistenceService;
	//private IWorkspacePersistenceService mWorkspacePersistenceService;
	
	private static final String gLogFilePathPrefix = "log";
	private static final String gLogFileExtension = "log";
	
	private String mLogFileLocation;
	private String mDefaultScriptFileLocation;
	private String mDefaultScriptFileName;
	
	@Autowired
	public void setWorkspacePersistenceService(IWorkspacePersistenceService workspacePersistenceService) {
		//mWorkspacePersistenceService = workspacePersistenceService;
	}
	
	public void setLogFileLocation(String logFileLocation) {
		mLogFileLocation = logFileLocation;
	}
	
	public void setDefaultScriptFileLocation(String scriptFileLocation) {
		mDefaultScriptFileLocation = scriptFileLocation;
	}
	
	public void setDefaultScriptFileName(String scriptFileName) {
		mDefaultScriptFileName = scriptFileName;
	}

	@Override
	public IBuildTask getNewBuildTask() {
		BuildTaskUnixScriptImpl buildTask = new BuildTaskUnixScriptImpl(gLogFilePathPrefix, gLogFileExtension, mLogFileLocation, mDefaultScriptFileLocation, mDefaultScriptFileName);
		return buildTask;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		//Fake to create a fixed workspace
		/*System.out.println("--------------------------Load workspace from DB (just to determine if there is any one exists in the DB)--------------------------");
		
		List<Workspace> workspaces = new ArrayList<Workspace>();
		if(mWorkspacePersistenceService != null) {
			WorkspacePersistenceQuery query = new WorkspacePersistenceQuery();
			query.dataRange = DataRange.ID_MATCH;
			query.id = 1;
			mWorkspacePersistenceService.loadWorkspace(workspaces, query);
			if(workspaces.isEmpty()) {
				Workspace workspace = new Workspace();
				workspace.setScriptFilePath(mDefaultScriptFileLocation + File.separator + mDefaultScriptFileName);
				mWorkspacePersistenceService.persistWorkspace(workspace);
			}
		}*/
	}

}
