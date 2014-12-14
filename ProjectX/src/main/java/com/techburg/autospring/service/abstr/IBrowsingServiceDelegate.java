package com.techburg.autospring.service.abstr;

public interface IBrowsingServiceDelegate {
	int persistBrowsingObjectInDirectory(String directoryPath, IBrowsingObjectPersistentService browsingObjectPersistentService);
	int removeBrowsingObjectInDirectory(String directoryPath, IBrowsingObjectPersistentService browsingObjectPersistentService);
}
