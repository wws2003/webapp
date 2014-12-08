package com.techburg.autospring.model.business;

import java.io.File;
import java.util.Date;

public class BrowsingObject {
	public static class ObjectType {
		public static final int TYPE_FILE = 0;
		public static final int TYPE_FOLDER = 1;
	}
	public static class OpenType {
		public static final int OPEN_BY_BROWSER = 0;
		public static final int OPEN_BY_DOWNLOADER = 1;
	}
	
	private int mObjectType;
	private int mOpenType;
	private long mId;
	private String mAbsolutePath;
	private Date mModifiedTime;
	private BrowsingObject mParent;

	public long getId() {
		return mId;
	}
	public void setId(long id) {
		this.mId = id;
	}

	public String getAbsolutePath() {
		return mAbsolutePath;
	}
	public void setAbsolutePath(String absolutePath) {
		this.mAbsolutePath = absolutePath;
	}

	public int getObjectType() {
		return mObjectType;
	}
	public void setObjectType(int objectType) {
		mObjectType = objectType;
	}

	public int getOpenType() {
		return mOpenType;
	}
	public void setOpenType(int openType) {
		this.mOpenType = openType;
	}
	
	public Date getModifiedTime() {
		return mModifiedTime;
	}
	public void setModifiedTime(Date modifiedTime) {
		this.mModifiedTime = modifiedTime;
	}
	
	public BrowsingObject getParent() {
		return mParent;
	}
	public void setParent(BrowsingObject parent) {
		mParent = parent;
	}
	
	public String getName() {
		File file = new File(mAbsolutePath);
		return file.getName();
	}
}
