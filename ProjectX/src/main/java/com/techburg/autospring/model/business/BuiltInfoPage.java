package com.techburg.autospring.model.business;

import java.util.List;

public class BuiltInfoPage {
	public static final int BUILD_INFO_PER_PAGE = 20;
	private int mPage;
	private int mMaxPageNumber;
	private List<BuildInfo> mBuiltInfoList;
	private long mWorkspaceId;
	
	public int getPage() {
		return mPage;
	}
	
	public void setPage(int page) {
		this.mPage = page;
	}

	public List<BuildInfo> getBuiltInfoList() {
		return mBuiltInfoList;
	}
	public void setBuiltInfoList(List<BuildInfo> buildInfoList) {
		this.mBuiltInfoList = buildInfoList;
	}
	
	public int getFirstPage() {
		return 1;
	}
	
	public int getMaxPageNumber() {
		return mMaxPageNumber;
	}
	
	public void setMaxPageNumber(int maxPageNumber) {
		this.mMaxPageNumber = maxPageNumber;
	}

	public long getWorkspaceId() {
		return mWorkspaceId;
	}

	public void setWorkspaceId(long workspaceId) {
		this.mWorkspaceId = workspaceId;
	}
	
}
