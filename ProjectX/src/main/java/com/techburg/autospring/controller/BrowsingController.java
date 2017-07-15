package com.techburg.autospring.controller;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.techburg.autospring.model.business.BrowsingObject;
import com.techburg.autospring.model.business.BrowsingObject.ObjectType;
import com.techburg.autospring.model.business.BrowsingObject.OpenType;
import com.techburg.autospring.service.abstr.IBrowsingObjectPersistentService;
import com.techburg.autospring.util.FileUtil;

@Controller(value = "browsingController")
public class BrowsingController {

	private static final String gWorkspaceId = "workspaceId";
	private static final String gChildBrowsingObjectsAttribute = "childBrowsingObjects";
	private static final String gBrowsingObjectPathAttribute = "browsingObjectPath";
	private static final String gIsFileObjectAttribute = "isFileObject";
	private static final String gObjectContentAttribute = "objectContent";
	private static final String gObjectCanOpened = "objectCanOpened";

	private IBrowsingObjectPersistentService mBrowsingPersistentService;

	@Resource
	public void setBrowsingObjectPersistentService(IBrowsingObjectPersistentService browsingService) {
		mBrowsingPersistentService = browsingService;
	}

	@RequestMapping(value = "/browse/{workspaceId}/{id}", method = RequestMethod.GET)
	public String browse(@PathVariable long workspaceId, @PathVariable long id, Model model) throws Exception {
		String browsePage = "browse";
		String waitPage = "wait";

		if (mBrowsingPersistentService == null) {
			return waitPage;
		}

		try {
			BrowsingObject browsingObject = mBrowsingPersistentService.getBrowsingObjectById(id);
			if (browsingObject != null) {
				// Put to model info attributes
				model.addAttribute(gWorkspaceId, workspaceId);
				model.addAttribute(gBrowsingObjectPathAttribute, browsingObject.getAbsolutePath());
				model.addAttribute(gIsFileObjectAttribute, browsingObject.getObjectType() == ObjectType.TYPE_FILE);

				// Get children browsing object
				List<BrowsingObject> childBrowsingObjects = new LinkedList<BrowsingObject>();
				mBrowsingPersistentService.getChildBrowsingObjects(browsingObject, childBrowsingObjects);

				// Add parent browsing object to the first position of the
				// children browsing object list
				if (browsingObject.getParent() != null) {
					BrowsingObject parent = browsingObject.getParent();
					parent.setObjectType(ObjectType.TYPE_FOLDER);
					parent.setOpenType(OpenType.OPEN_BY_BROWSER);
					parent.setAbsolutePath("../");
					childBrowsingObjects.add(0, parent);
				}

				// Add children browsing object list to model
				model.addAttribute(gChildBrowsingObjectsAttribute, childBrowsingObjects);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return browsePage;
	}

	@RequestMapping(value = "/open/{workspaceId}/{id}", method = RequestMethod.GET)
	public String openObject(@PathVariable long workspaceId, @PathVariable long id, Model model) throws Exception {
		String openPage = "open";
		String waitPage = "wait";

		if (mBrowsingPersistentService == null) {
			return waitPage;
		}

		try {
			BrowsingObject browsingObject = mBrowsingPersistentService.getBrowsingObjectById(id);
			if (browsingObject != null && browsingObject.getOpenType() == OpenType.OPEN_BY_BROWSER) {
				// Try to put workspace id into model
				model.addAttribute(gWorkspaceId, workspaceId);

				// Open the browsing file content on browser directly
				FileUtil util = new FileUtil();
				String objectContent = util.getStringFromFile(browsingObject.getAbsolutePath());
				model.addAttribute(gObjectContentAttribute, objectContent);
				model.addAttribute(gObjectCanOpened, true);
			} else {
				// Download the file
				return "redirect:/download/" + id;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return openPage;
	}

	@RequestMapping(value = "/api/getFileContent/{id}", method = RequestMethod.GET)
	@ResponseBody
	public String getFileContent(@PathVariable long id) throws Exception {
		try {
			BrowsingObject browsingObject = mBrowsingPersistentService.getBrowsingObjectById(id);
			if (browsingObject != null && browsingObject.getOpenType() == OpenType.OPEN_BY_BROWSER) {
				FileUtil util = new FileUtil();
				String objectContent = util.getStringFromFile(browsingObject.getAbsolutePath());
				return objectContent;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "[WARNING]File content not avaiable";
	}

	@RequestMapping(value = "/download/{id}", method = RequestMethod.GET)
	public void getFile(@PathVariable long id, HttpServletResponse response) {
		BrowsingObject browsingObject = mBrowsingPersistentService.getBrowsingObjectById(id);
		if (browsingObject != null) {

			response.setContentType("application/octet-stream");
			response.setHeader("Content-Disposition",
					"attachment;filename=" + new File(browsingObject.getAbsolutePath()).getName());

			ServletOutputStream out = null;
			InputStream fileInputStream = null;
			try {
				out = response.getOutputStream();
				fileInputStream = new BufferedInputStream(new FileInputStream(browsingObject.getAbsolutePath()));
				byte[] outputByte = new byte[4096];
				while (fileInputStream.read(outputByte, 0, 4096) != -1) {
					out.write(outputByte, 0, 4096);
				}

			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					fileInputStream.close();
					out.flush();
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
