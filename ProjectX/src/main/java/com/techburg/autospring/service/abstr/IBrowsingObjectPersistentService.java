package com.techburg.autospring.service.abstr;

import java.util.List;

import com.techburg.autospring.model.business.BrowsingObject;

public interface IBrowsingObjectPersistentService {
	int persistBrowsingObject(BrowsingObject browsingObject);
	BrowsingObject getBrowsingObjectById(long id);
	BrowsingObject getBrowsingObjectByPath(String path);
	int getChildBrowsingObjects(BrowsingObject parent, List<BrowsingObject> children);
	int removeAllBrowsingObject();
}
