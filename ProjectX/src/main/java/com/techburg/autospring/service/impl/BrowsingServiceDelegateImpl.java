package com.techburg.autospring.service.impl;

import java.io.File;
import java.io.FileFilter;
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
	private static final String[] gSupportedFileExts = {".c", ".cpp", ".h", ".sh", ".java", ".jsp", ".xml", ".properties", ".js", ".css"};

	private FileFilter mDirectoryFilter;
	private FileFilter mFileFilter;
	
	private IBrowsingObjectPersistentService mBrowsingObjectPersistentService;
	
	public BrowsingServiceDelegateImpl() {
		mDirectoryFilter = new TbgDirectoryFilter();
		mFileFilter = new TbgFileFilter();
	}
	
	@Autowired
	public void setBrowsingRootPath(String browsingRootPath) {
		mBrowsingRootPath = browsingRootPath;
	}
	
	@Autowired
	public void setBrowsingObjectPersistentService(IBrowsingObjectPersistentService browsingObjectPersistentService) {
		mBrowsingObjectPersistentService = browsingObjectPersistentService;
	}

	@Override
	public int construct() throws Exception {
		resetConstruction();
		File rootElement = new File(mBrowsingRootPath);
		BrowsingObject rootBrowsingObject = createBrowsingObjectFromElement(rootElement, null);
		recursiveConstruct(rootElement, rootBrowsingObject);
		return 0;
	}

	@Override
	public BrowsingObject getBrowsingObjectById(long id) {
		return mBrowsingObjectPersistentService.getBrowsingObjectById(id);
	}

	@Override
	public BrowsingObject getRootBrowsingObjectByPath(String path) {
		return mBrowsingObjectPersistentService.getBrowsingObjectByPath(path);
	}

	@Override
	public void getChildBrowsingObject(BrowsingObject parent,
			List<BrowsingObject> children) {
		mBrowsingObjectPersistentService.getChildBrowsingObjects(parent, children);
	}
	
	private void resetConstruction() {
		mBrowsingObjectPersistentService.removeAllBrowsingObject();
	}

	private void recursiveConstruct(File element, BrowsingObject browsingObject) {
		if(element.isDirectory()) {
			File[] subDirElements = element.listFiles(mDirectoryFilter);
			for(File subDirElement : subDirElements) {
				BrowsingObject subBrowsingObject = createBrowsingObjectFromElement(subDirElement, browsingObject);
				recursiveConstruct(subDirElement, subBrowsingObject);
			}
			File[] subFileElements = element.listFiles(mFileFilter);
			for(File subFileElement : subFileElements) {
				createBrowsingObjectFromElement(subFileElement, browsingObject);
			}
		}
	}

	private BrowsingObject createBrowsingObjectFromElement(File element, BrowsingObject parentBrowsingObject) {
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

		return (mBrowsingObjectPersistentService.persistBrowsingObject(browsingObject) == PersistenceResult.PERSISTENCE_SUCCESSFUL) ? browsingObject : null;
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
