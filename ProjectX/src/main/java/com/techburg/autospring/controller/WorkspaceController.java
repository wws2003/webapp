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
import org.springframework.web.bind.annotation.ResponseBody;

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
	private static final String gEdittingWorkspaceAttributeName = "edittingWorkspace";
	private static final String gWorkspaceDescriptionAttributeName = "workspaceDescription";
	private static final String gWorkspaceRootBrowsingObjectIdAttributeName = "workspaceRootBrowsingObjectId";
	private static final String gWorkspaceScriptContentAttributeName = "scriptContent";
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
	
	@RequestMapping(value="/workspace/github/new", method=RequestMethod.GET)
	public String toNewGithubWorkspacePage() {
		return "workspace_github";
	}
	
	@RequestMapping(value="/workspace/github/script/{pullMethod}", method=RequestMethod.GET)
	@ResponseBody
	public String getGithubBuildScriptContent(@PathVariable int pullMethod) {
		boolean sparseCheckout = (pullMethod == 1);
		Workspace githubWorkspace = mWorkspaceFactory.createGithubWorkspace(sparseCheckout);
		String scriptFilePath = githubWorkspace.getScriptFilePath();
		String scriptFileContent = "";
		try {
			FileUtil fileUtil = new FileUtil();
			scriptFileContent = fileUtil.getStringFromFile(scriptFilePath);
		}
		catch (Exception e) {
			e.printStackTrace();
			scriptFileContent = "Failed to load script !";
		}
		return scriptFileContent;
	}
	
	@RequestMapping(value="/workspace/edit/{workspaceId}", method=RequestMethod.GET)
	public String editWorkspace(Model model, @PathVariable long workspaceId) {
		Workspace edittingWorkspace = getWorkspacebyId(workspaceId);
		if(edittingWorkspace == null) {
			return "error";
		}
		model.addAttribute(gEdittingWorkspaceAttributeName, edittingWorkspace);
		model.addAttribute(gWorkspaceScriptContentAttributeName, getWorkspaceScriptContent(edittingWorkspace));
		return "workspace";
	}
	
	@RequestMapping(value="/workspace/new", method=RequestMethod.POST)
	public String newWorkspace(Model model,
			@RequestParam(value = "workspacename", required = true) String workspaceName,
			@RequestParam(value = "workspacedescription", required = true) String workspaceDescription,
			@RequestParam(value = "buildscriptname", required = true) String buildScriptName,
			@RequestParam(value = "buildscriptcontent", required = true) String buildScriptContent) {
		
		Workspace newWorkspace = null;
		newWorkspace = mWorkspaceFactory.createWorkspace(workspaceName, buildScriptName);
		newWorkspace.setDescription(workspaceDescription);
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
	
	@RequestMapping(value="/workspace/update/{workspaceId}", method=RequestMethod.POST)
	public String updateWorkspace(@PathVariable long workspaceId,
			@RequestParam(value = "workspacedescription", required = true) String workspaceDescription,
			@RequestParam(value = "buildscriptcontent", required = true) String buildScriptContent) {
		
		Workspace edittingWorkspace = getWorkspacebyId(workspaceId);
		edittingWorkspace.setDescription(workspaceDescription);
		try {
			saveWorkspaceBuildScriptContent(edittingWorkspace, buildScriptContent);
			if(mWorkspacePersistenceService.updateWorkspace(edittingWorkspace) == PersistenceResult.UPDATE_SUCCESSFUL) {	
				return "redirect:/workspace/detail/" + workspaceId;
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
			model.addAttribute(gWorkspaceDescriptionAttributeName, workspace.getDescription());
		}
		model.addAttribute(gWorkspaceIdAttributeName, workspaceId);
		return "workspacedetail";
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
	
	private String getWorkspaceScriptContent(Workspace workspace) {
		FileUtil fileUtil = new FileUtil();
		try {
			return fileUtil.getStringFromFile(workspace.getScriptFilePath());
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}
}
