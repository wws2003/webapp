package com.techburg.autospring.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.techburg.autospring.factory.abstr.IWorkspaceFactory;
import com.techburg.autospring.model.BasePersistenceQuery.DataRange;
import com.techburg.autospring.model.WorkspacePersistenceQuery;
import com.techburg.autospring.model.business.BrowsingObject;
import com.techburg.autospring.model.business.Workspace;
import com.techburg.autospring.service.abstr.IBrowsingObjectPersistentService;
import com.techburg.autospring.service.abstr.IWorkspacePersistenceService;
import com.techburg.autospring.service.abstr.PersistenceResult;
import com.techburg.autospring.util.FileUtil;

@Controller
public class WorkspaceController {
	private static final String gWorkspaceListAttributeName = "workspaces";
	private static final String gWorkspaceIdAttributeName = "workspaceId";
	private static final String gWorkspaceRootBrowsingObjectIdAttributeName = "workspaceRootBrowsingObjectId";
	
	private static final String gScriptFileAvailable = "scriptFileAvailable";
	private static final String gScriptFileContent = "scriptFileContent";
	private static final String gScriptFileContentUpdated = "scriptFileContentUpdated";
	private static final String gRedirectPageAttributeName = "redirectPage";
	
	private IWorkspacePersistenceService mWorkspacePersistenceService;
	private IWorkspaceFactory mWorkspaceFactory;
	private IBrowsingObjectPersistentService mBrowsingObjectPersistentService;

	@Autowired
	public void setWorkspacePersistenceService(IWorkspacePersistenceService workspacePersistenceService) {
		mWorkspacePersistenceService = workspacePersistenceService;
	}

	@Autowired
	public void setWorkspaceFactory(IWorkspaceFactory workspaceFactory) {
		mWorkspaceFactory = workspaceFactory;
	}
	
	@Autowired
	public void setBrowsingObjectPersistentService(IBrowsingObjectPersistentService browsingObjectPersistentService) {
		mBrowsingObjectPersistentService = browsingObjectPersistentService;
	}

	@RequestMapping(value="/workspace/new", method=RequestMethod.GET)
	public String toNewWorkspacePage() {
		return "workspace";
	}
	
	@RequestMapping(value="/workspace/new", method=RequestMethod.POST)
	public String newWorkspace(Model model,
			@RequestParam(value = "workspacename", required = true) String workspaceName,
			@RequestParam(value = "buildscriptname", required = true) String buildScriptName,
			@RequestParam(value = "buildscriptcontent", required = true) String buildScriptContent) {
		
		Workspace newWorkspace = null;
		newWorkspace = mWorkspaceFactory.createWorkspace(workspaceName, buildScriptName);
		try {
			saveWorkspaceBuildScriptContent(newWorkspace, buildScriptContent);
			if(mWorkspacePersistenceService.persistWorkspace(newWorkspace) == PersistenceResult.REQUEST_QUEUED) {	
				model.addAttribute(gRedirectPageAttributeName, "/workspace/list");
				return "inform";
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			return "error";
		}
		return "error";
	}

	@RequestMapping(value="/workspace/list", method=RequestMethod.GET)
	public String listWorkspaces(Model model) {
		List<Workspace> workspaces = new ArrayList<Workspace>();
		WorkspacePersistenceQuery query = new WorkspacePersistenceQuery();
		query.dataRange = DataRange.ALL;
		mWorkspacePersistenceService.loadWorkspace(workspaces, query);
		model.addAttribute(gWorkspaceListAttributeName, workspaces);
		return "workspacelist";
	}

	@RequestMapping(value="/workspace/detail/{workspaceId}", method=RequestMethod.GET)
	public String detailWorkspace(Model model, @PathVariable long workspaceId) {
		Workspace workspace = getWorkspacebyId(workspaceId);
		if(workspace != null) {
			BrowsingObject workspaceRootBrowsingObject = mBrowsingObjectPersistentService.getBrowsingObjectByPath(workspace.getDirectoryPath());
			model.addAttribute(gWorkspaceRootBrowsingObjectIdAttributeName, workspaceRootBrowsingObject.getId());
		}
		model.addAttribute(gWorkspaceIdAttributeName, workspaceId);
		return "workspacedetail";
	}

	@RequestMapping(value="/script/edit", method=RequestMethod.GET)
	public String editScriptContent(
			@RequestParam(value = gScriptFileContentUpdated, required = false) boolean scriptFileContentUpdated,
			Model model) {

		//Detect if redirected from successful edit submission
		model.addAttribute(gScriptFileContentUpdated, scriptFileContentUpdated);

		long id = 1; //TODO In the future, id will be read from proper parameters
		Workspace workspace = getWorkspacebyId(id);

		if(workspace != null) {
			String scriptFilePath = workspace.getScriptFilePath();
			FileUtil fileUtil = new FileUtil();
			try {
				String scriptFileContent = fileUtil.getStringFromFile(scriptFilePath);
				System.out.println("File content " + scriptFileContent);
				model.addAttribute(gScriptFileContent, scriptFileContent);
				model.addAttribute(gScriptFileAvailable, true);
			}
			catch (Exception e) {
				model.addAttribute(gScriptFileAvailable, false);
			}
		}
		else  {
			model.addAttribute(gScriptFileAvailable, false);
		}
		return "script";
	}

	@RequestMapping(value="/script/submit", method=RequestMethod.POST)
	public String submitScriptContent(
			@RequestParam(value = "content", required = true) String content,
			Model model) {
		//TODO Avoid concurrency when read/write file
		long id = 1; //TODO In the future, id will be read from proper parameters
		Workspace workspace = getWorkspacebyId(id);

		if(workspace != null) {
			try {
				saveWorkspaceBuildScriptContent(workspace, content);
			}
			catch (Exception e) {
				return "home";
			}
			model.addAttribute(gScriptFileContentUpdated, true);
		}
		return "redirect:/script/edit";
	}

	private Workspace getWorkspacebyId(long id) {
		List<Workspace> workspaces = new ArrayList<Workspace>();
		if(mWorkspacePersistenceService != null) {
			WorkspacePersistenceQuery query = new WorkspacePersistenceQuery();
			query.dataRange = DataRange.ID_MATCH;
			query.id = id;
			mWorkspacePersistenceService.loadWorkspace(workspaces, query);
			return workspaces.isEmpty() ? null : workspaces.get(0);
		}
		return null;
	}

	private void saveWorkspaceBuildScriptContent(Workspace workspace, String buildScriptContent) throws Exception {
		String scriptFilePath = workspace.getScriptFilePath();

		//Avoid script run error due to \r character
		String content = buildScriptContent.replace("\r\n", "\n");

		FileUtil fileUtil = new FileUtil();
		fileUtil.storeContentToFile(content, scriptFilePath);
	}
}
