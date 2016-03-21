package com.techburg.autospring.model.entity;

import com.techburg.autospring.model.business.BrowsingObject;

public class SimplifiedBrowsingObject {
	private long mId;
	private String mName;
	
	public SimplifiedBrowsingObject(BrowsingObject browsingObject) {
		mId = browsingObject.getId();
		mName = browsingObject.getName();
	}
	
	public long getId() {
		return mId;
	}
	
	public String getName() {
		return mName;
	}
}
