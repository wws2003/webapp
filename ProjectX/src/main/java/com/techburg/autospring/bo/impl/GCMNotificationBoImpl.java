package com.techburg.autospring.bo.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.techburg.autospring.bo.abstr.IGCMNotificationBo;
import com.techburg.autospring.model.business.BuildInfo;
import com.techburg.autospring.model.business.GCMNotification;
import com.techburg.autospring.model.business.Workspace;
import com.techburg.autospring.model.entity.GCMNotificationSerializableEntity;

public class GCMNotificationBoImpl implements IGCMNotificationBo {

	private static final String NOTIFICATION_TITLE_KEY = "title";
	private static final String NOTIFICATION_STATUS_KEY = "status";
	private static final String NOTIFICATION_LINK_KEY = "link";
	
	private static final String NOTIFICATION_TITLE_VAL_PREFIX = "Latest build from workspace ";
	private static final String NOTIFICATION_STATUS_SUCCESS_VAL = "OK (possibly...)";
	private static final String NOTIFICATION_STATUS_FAILED_VAL = "!!!NG, NG, NG!!!";
	private static final String NOTIFICATION_STATUS_CANCELLED_VAL = "Cancelled by user";
	
	private String mRootURLPath = null;
	
	//FIXME: Any better way to retrieve root URL path, such as from a ApplicationContext like android
	public void setRootURLPath(String rootURLPath) {
		mRootURLPath = rootURLPath;
	}
	
	@Override
	public GCMNotificationSerializableEntity getGCMNotificationDictionary(
			GCMNotification gcmNotification) {
		
		List<String> registrationIds = new ArrayList<String>();
		for(String registrationId : gcmNotification.getGCMEndPoints()) {
			registrationIds.add(registrationId);
		}
		
		Map<String, String> dataMap = new HashMap<String, String>();
		dataMap.put(NOTIFICATION_TITLE_KEY, NOTIFICATION_TITLE_VAL_PREFIX + gcmNotification.getBuildInfo().getWorkspace().getId()); 
		dataMap.put(NOTIFICATION_STATUS_KEY, getBuildStatusString(gcmNotification.getBuildInfo()));
		dataMap.put(NOTIFICATION_LINK_KEY, getWorkspaceLink(gcmNotification.getBuildInfo().getWorkspace()));
		
		return new GCMNotificationSerializableEntity(registrationIds, dataMap);
	}

	private String getBuildStatusString(BuildInfo buildInfo) {
		int status = buildInfo.getStatus();
		if(status == BuildInfo.Status.BUILD_SUCCESSFUL) {
			return NOTIFICATION_STATUS_SUCCESS_VAL;
		}
		else {
			//FIXME: This check is just a temporary solution
			if(status == 2 && buildInfo.getBeginTimeStamp() == null) {
				return NOTIFICATION_STATUS_CANCELLED_VAL;
			}
			return NOTIFICATION_STATUS_FAILED_VAL;
		}
	}
	
	private String getWorkspaceLink(Workspace workspace) {
		return mRootURLPath + "/buildlist/" + workspace.getId() + "/";
	}
}
