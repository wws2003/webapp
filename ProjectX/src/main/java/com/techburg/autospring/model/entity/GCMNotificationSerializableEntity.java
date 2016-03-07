package com.techburg.autospring.model.entity;

import java.util.List;
import java.util.Map;

public class GCMNotificationSerializableEntity {
	private List<String> registration_ids;
	private Map<String,String> data;

	public GCMNotificationSerializableEntity(List<String> registration_ids, Map<String,String> data) {
		this.registration_ids = registration_ids;
		this.data = data;
	}
	
	public List<String> getRegistration_ids() {
		return registration_ids;
	}
	
	public Map<String,String> getData() {
		return data;
	}
	
}
