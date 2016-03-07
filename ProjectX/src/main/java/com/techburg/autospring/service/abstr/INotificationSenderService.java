package com.techburg.autospring.service.abstr;

import com.techburg.autospring.model.business.GCMNotification;

public interface INotificationSenderService {
	void sendGCMNotification(GCMNotification gcmNotification);
}
