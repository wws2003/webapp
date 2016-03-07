package com.techburg.autospring.bo.abstr;

import com.techburg.autospring.model.business.GCMNotification;
import com.techburg.autospring.model.entity.GCMNotificationSerializableEntity;

public interface IGCMNotificationBo {
	//MARK: This interface maps GCMNotification to dictionary in JSON format to be ready for sending.
	//So in some meaning, it is similar to other Bo interfaces, those map business data object into
	//entity object to be ready for persisting
	
	GCMNotificationSerializableEntity getGCMNotificationDictionary(GCMNotification gcmNotification);
}
