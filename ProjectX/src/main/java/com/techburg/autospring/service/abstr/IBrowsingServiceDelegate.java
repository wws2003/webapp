package com.techburg.autospring.service.abstr;

import java.util.List;

import com.techburg.autospring.model.business.BrowsingObject;

public interface IBrowsingServiceDelegate {
	int construct() throws Exception;
	BrowsingObject getBrowsingObjectById(long id);
	BrowsingObject getRootBrowsingObjectByPath(String path);
	void getChildBrowsingObject(BrowsingObject parent, List<BrowsingObject> children);
}
