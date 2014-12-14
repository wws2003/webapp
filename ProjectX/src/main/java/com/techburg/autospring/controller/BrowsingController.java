package com.techburg.autospring.controller;

import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.techburg.autospring.model.business.BrowsingObject;
import com.techburg.autospring.model.business.BrowsingObject.ObjectType;
import com.techburg.autospring.model.business.BrowsingObject.OpenType;
import com.techburg.autospring.service.abstr.IBrowsingObjectPersistentService;
import com.techburg.autospring.util.FileUtil;

@Controller
public class BrowsingController {

	private static final String gChildBrowsingObjectsAttribute = "childBrowsingObjects";
	private static final String gBrowsingObjectPathAttribute = "browsingObjectPath";
	private static final String gIsFileObjectAttribute = "isFileObject";
	private static final String gObjectContentAttribute = "objectContent";
	private static final String gObjectCanOpened = "objectCanOpened";

	private IBrowsingObjectPersistentService mBrowsingPersistentService;

	@Autowired
	public void setBrowsingObjectPersistentService(IBrowsingObjectPersistentService browsingService) {
		mBrowsingPersistentService = browsingService;
	}

	@RequestMapping(value="/browse/{id}", method = RequestMethod.GET)
	public String browse(@PathVariable long id, 
			Model model) throws Exception {
		String browsePage = "browse";
		String waitPage = "wait";

		if(mBrowsingPersistentService == null) {
			return waitPage;
		}

		try {
			BrowsingObject browsingObject = mBrowsingPersistentService.getBrowsingObjectById(id);
			if(browsingObject != null) {
				model.addAttribute(gBrowsingObjectPathAttribute, browsingObject.getAbsolutePath());
				model.addAttribute(gIsFileObjectAttribute, browsingObject.getObjectType() == ObjectType.TYPE_FILE);
				List<BrowsingObject> childBrowsingObjects = new LinkedList<BrowsingObject>();
				mBrowsingPersistentService.getChildBrowsingObjects(browsingObject, childBrowsingObjects);
				if(browsingObject.getParent() != null) {
					BrowsingObject parent = browsingObject.getParent();
					parent.setObjectType(ObjectType.TYPE_FOLDER);
					parent.setOpenType(OpenType.OPEN_BY_BROWSER);
					parent.setAbsolutePath("../");
					childBrowsingObjects.add(0, parent);
				}
				model.addAttribute(gChildBrowsingObjectsAttribute, childBrowsingObjects);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		return browsePage;
	}

	@RequestMapping(value="/open/{id}", method = RequestMethod.GET)
	public String openObject(@PathVariable long id,
			Model model) throws Exception {
		String openPage = "open";
		String waitPage = "wait";

		if(mBrowsingPersistentService == null) {
			return waitPage;
		}
		
		try {
			BrowsingObject browsingObject = mBrowsingPersistentService.getBrowsingObjectById(id);
			if(browsingObject != null && browsingObject.getOpenType() == OpenType.OPEN_BY_BROWSER) {
				FileUtil util = new FileUtil();
				String objectContent = util.getStringFromFile(browsingObject.getAbsolutePath());
				model.addAttribute(gObjectContentAttribute, objectContent);
				model.addAttribute(gObjectCanOpened, true);
			}
			else {
				model.addAttribute(gObjectCanOpened, false);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		return openPage;
	}
}
