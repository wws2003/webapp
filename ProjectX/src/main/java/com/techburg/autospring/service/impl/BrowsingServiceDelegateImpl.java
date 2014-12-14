package com.techburg.autospring.service.impl;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.techburg.autospring.model.business.BrowsingObject;
import com.techburg.autospring.model.business.BrowsingObject.ObjectType;
import com.techburg.autospring.model.business.BrowsingObject.OpenType;
import com.techburg.autospring.service.abstr.IBrowsingObjectPersistentService;
import com.techburg.autospring.service.abstr.IBrowsingServiceDelegate;
import com.techburg.autospring.service.abstr.PersistenceResult;

public class BrowsingServiceDelegateImpl implements IBrowsingServiceDelegate {

	private String mBrowsingRootPath;
	private static final String[] gSupportedFileNames = {"makefile"};
	private static final String[] gSupportedFileExts = {".c", ".cpp", ".h", ".sh", ".java", ".jsp", ".xml", ".properties", ".js", ".css", ".log"};

	private FileFilter mDirectoryFilter;
	private FileFilter mFileFilter;

	public BrowsingServiceDelegateImpl() {
		mDirectoryFilter = new TbgDirectoryFilter();
		mFileFilter = new TbgFileFilter();
	}
	
	@Autowired
	public void setBrowsingRootPath(String browsingRootPath) {
		mBrowsingRootPath = browsingRootPath;
	}

	@Override
	public int persistBrowsingObjectInDirectory(String directoryPath, IBrowsingObjectPersistentService browsingObjectPersistentService) {
		if(directoryPath == null) {
			//Reconstruct all browsing object DB
			resetConstruction(browsingObjectPersistentService);
			File rootElement = new File(mBrowsingRootPath);
			BrowsingObject rootBrowsingObject = createBrowsingObjectFromElement(rootElement, null, browsingObjectPersistentService);
			recursiveConstruct(rootElement, rootBrowsingObject, browsingObjectPersistentService);
		}
		else {
			//Add to DB elements in directory given directory path
			BrowsingObject rootBrowsingObject = browsingObjectPersistentService.getBrowsingObjectById(1);
			if(rootBrowsingObject != null) {
				File workspaceRootElement = new File(directoryPath);
				System.out.println("<<<<<<<<<<<<<To persist elements in directory " + directoryPath + " >>>>>>>>>>>>>>>>>>>>>>>>>>>");
				BrowsingObject workspaceRootBrowsingObject = createBrowsingObjectFromElement(workspaceRootElement, rootBrowsingObject, browsingObjectPersistentService);
				recursiveConstruct(workspaceRootElement, workspaceRootBrowsingObject, browsingObjectPersistentService);	
			}
		}
		return 0;
	}

	@Override
	public int removeBrowsingObjectInDirectory(String directoryPath, IBrowsingObjectPersistentService browsingObjectPersistentService) {
		if(directoryPath == null) {
			//Remove all browsing object from DB
			resetConstruction(browsingObjectPersistentService);
		}
		else {
			//Remove all browsing object in directory from DB, given directory path
			BrowsingObject parentBrowsingObject = browsingObjectPersistentService.getBrowsingObjectByPath(directoryPath);
			recursiveRemove(parentBrowsingObject , browsingObjectPersistentService);
		}
		return 0;
	}

	private void resetConstruction(IBrowsingObjectPersistentService browsingObjectPersistentService) {
		browsingObjectPersistentService.removeAllBrowsingObject();
	}

	private void recursiveRemove(BrowsingObject parentBrowsingObject, IBrowsingObjectPersistentService browsingObjectPersistentService) {
		List<BrowsingObject> childBrowsingObjects = new ArrayList<BrowsingObject>();
		browsingObjectPersistentService.getChildBrowsingObjects(parentBrowsingObject, childBrowsingObjects);
		for(BrowsingObject childBrowsingObject : childBrowsingObjects) {
			recursiveRemove(childBrowsingObject, browsingObjectPersistentService);
		}
		if(parentBrowsingObject != null)
			browsingObjectPersistentService.removeBrowsingObjectById(parentBrowsingObject.getId());
	}

	private void recursiveConstruct(File element, BrowsingObject browsingObject, IBrowsingObjectPersistentService browsingObjectPersistentService) {
		if(element.isDirectory()) {
			File[] subDirElements = element.listFiles(mDirectoryFilter);
			for(File subDirElement : subDirElements) {
				BrowsingObject subBrowsingObject = createBrowsingObjectFromElement(subDirElement, browsingObject, browsingObjectPersistentService);
				recursiveConstruct(subDirElement, subBrowsingObject, browsingObjectPersistentService);
			}
			File[] subFileElements = element.listFiles(mFileFilter);
			for(File subFileElement : subFileElements) {
				createBrowsingObjectFromElement(subFileElement, browsingObject, browsingObjectPersistentService);
			}
		}
	}

	private BrowsingObject createBrowsingObjectFromElement(File element, BrowsingObject parentBrowsingObject, IBrowsingObjectPersistentService browsingObjectPersistentService) {
		BrowsingObject browsingObject = new BrowsingObject();
		browsingObject.setAbsolutePath(element.getAbsolutePath());
		browsingObject.setModifiedTime(new Date(element.lastModified()));
		browsingObject.setParent(parentBrowsingObject);
		if(element.isFile()) {
			browsingObject.setObjectType(ObjectType.TYPE_FILE);
			browsingObject.setOpenType(isOpenFileBrowserFile(element) ? OpenType.OPEN_BY_BROWSER : OpenType.OPEN_BY_DOWNLOADER);
		}
		if(element.isDirectory()) {
			browsingObject.setObjectType(ObjectType.TYPE_FOLDER);
			browsingObject.setOpenType(OpenType.OPEN_BY_BROWSER);
		}

		return (browsingObjectPersistentService.persistBrowsingObject(browsingObject) == PersistenceResult.PERSISTENCE_SUCCESSFUL) ? browsingObject : null;
	}

	private boolean isOpenFileBrowserFile(File file) {
		String fileName = file.getName();
		for(String supportedName : gSupportedFileNames) {
			if(supportedName.equals(fileName)) {
				return true;
			}
		}
		String fileExt = getFileExtension(file);
		for(String supportedExt : gSupportedFileExts) {
			if(supportedExt.equals(fileExt)) {
				return true;
			}
		}
		return false;
	}

	private String getFileExtension(File file) {
		String name = file.getName();
		int lastIndexOf = name.lastIndexOf(".");
		if (lastIndexOf == -1) {
			return ""; // empty extension
		}
		return name.substring(lastIndexOf);
	}

	private class TbgDirectoryFilter implements FileFilter {
		@Override
		public boolean accept(File file) {
			return file.isDirectory() && !file.getName().startsWith(".");
		}
	}

	private class TbgFileFilter implements FileFilter {
		@Override
		public boolean accept(File file) {
			return file.isFile() && !file.getName().startsWith(".");
		}
	}

}
