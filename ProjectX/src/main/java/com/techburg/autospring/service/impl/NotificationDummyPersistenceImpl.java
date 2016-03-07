package com.techburg.autospring.service.impl;

import java.util.List;

import com.techburg.autospring.service.abstr.INotificationPersistenceService;
import com.techburg.autospring.service.abstr.PersistenceResult;

public class NotificationDummyPersistenceImpl implements
		INotificationPersistenceService {

	@Override
	public void getGCMEndPointsForWorkspace(long workspaceId,
			List<String> gcmEndPoints) {
		gcmEndPoints.clear();
		gcmEndPoints.add("APA91bEP9y4zKfrcJhw6VVZmJij6Exa_SPLKu15McwDrqr4z6pS8VYcg2GcjfQzNUNQKSunAaea8PwvadvzneqX4MEkKkxnh63a7byrPYdwh_3T_j7y-aGg0clhzJCUC38_xmByz6ezETlleViwDdww8Tq32CFDQ8w");
	}

	@Override
	public int registerGCMEndPoinForWorkspace(long workspaceId,
			String gcmEndPoint) {
		return PersistenceResult.PERSISTENCE_SUCCESSFUL;
	}

	@Override
	public int unregisterGCMEndPoinForWorkspace(long workspaceId,
			String gcmEndPoint) {
		return PersistenceResult.REMOVE_SUCCESSFUL;
	}

}
