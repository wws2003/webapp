package com.techburg.autospring.service.abstr;

import java.util.List;

public interface INotificationPersistenceService {
	void getGCMEndPointsForWorkspace(long workspaceId, List<String> gcmEndPoints);
	
	int registerGCMEndPoinForWorkspace(long workspaceId, String gcmEndPoint);
	
	int unregisterGCMEndPoinForWorkspace(long workspaceId, String gcmEndPoint);
}
