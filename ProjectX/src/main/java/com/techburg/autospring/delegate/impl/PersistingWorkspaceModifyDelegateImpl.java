package com.techburg.autospring.delegate.impl;

import java.io.File;
import java.util.Date;

import com.techburg.autospring.delegate.abstr.AbstractWorkspaceModifyDelegate;
import com.techburg.autospring.model.business.BrowsingObject;
import com.techburg.autospring.model.business.BrowsingObject.ObjectType;
import com.techburg.autospring.model.business.Workspace;
import com.techburg.autospring.model.business.BrowsingObject.OpenType;
import com.techburg.autospring.service.abstr.IBrowsingObjectPersistentService;
import com.techburg.autospring.service.abstr.IWorkspacePersistenceService;
import com.techburg.autospring.service.abstr.PersistenceResult;

public class PersistingWorkspaceModifyDelegateImpl extends AbstractWorkspaceModifyDelegate {

	private IWorkspacePersistenceService mWorkspacePersistenceService;
	private IBrowsingObjectPersistentService mBrowsingObjectPersistentService;

	private static final String gErrorMessage = "Workspace couldn't be persisted properly";
	private static final String gErrorMessageForBuildScript = "Workspace build script couldn't be persisted properly";

	public PersistingWorkspaceModifyDelegateImpl(IWorkspacePersistenceService workspacePersistenceService, IBrowsingObjectPersistentService browsingObjectPersistentService) {
		mWorkspacePersistenceService = workspacePersistenceService;
		mBrowsingObjectPersistentService = browsingObjectPersistentService;
	}

	@Override
	protected void handleWorkspaceModified(Workspace workspace) throws Exception {
		//Update workspace into DB
		if(mWorkspacePersistenceService.updateWorkspace(workspace) != PersistenceResult.UPDATE_SUCCESSFUL) {;
			throw new Exception(gErrorMessage);	
		}
		try {
			//Persist browsing object for build script into DB if necessary
			if(!hasBrowsingObjectForBuildScriptPersisted(workspace)) {
				persistBrowsingObjectForBuildScript(workspace);
			}
		}
		catch(Exception primaryException) {
			primaryException.printStackTrace();
			throw new Exception(gErrorMessageForBuildScript);
		}
	}

	private boolean hasBrowsingObjectForBuildScriptPersisted(Workspace workspace) {
		String buildScriptPath = workspace.getScriptFilePath();
		BrowsingObject browsingObjectForBuildScript = mBrowsingObjectPersistentService.getBrowsingObjectByPath(buildScriptPath);
		return browsingObjectForBuildScript != null;
	}

	private void persistBrowsingObjectForBuildScript(Workspace workspace) throws Exception {
		BrowsingObject workspaceBrowsingObject = mBrowsingObjectPersistentService.getBrowsingObjectByPath(workspace.getDirectoryPath());

		//Create browsing object for build script
		File buildScriptFile = new File(workspace.getScriptFilePath());
		BrowsingObject browsingObjectForBuildScript = new BrowsingObject();
		browsingObjectForBuildScript.setAbsolutePath(workspace.getScriptFilePath());
		browsingObjectForBuildScript.setObjectType(ObjectType.TYPE_FILE);
		browsingObjectForBuildScript.setModifiedTime(new Date(buildScriptFile.lastModified()));
		browsingObjectForBuildScript.setParent(workspaceBrowsingObject);
		browsingObjectForBuildScript.setOpenType(OpenType.OPEN_BY_BROWSER);

		//Persist browsing object for build script
		mBrowsingObjectPersistentService.persistBrowsingObject(browsingObjectForBuildScript);
	}

}
