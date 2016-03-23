package com.techburg.autospring.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.techburg.autospring.delegate.abstr.AbstractWorkspaceCreateDelegate;
import com.techburg.autospring.delegate.abstr.AbstractWorkspaceModifyDelegate;
import com.techburg.autospring.delegate.abstr.IWorkspaceFileSystemDelegate;
import com.techburg.autospring.delegate.impl.BuildScriptSavingWorkspaceModifyDelegateImpl;
import com.techburg.autospring.delegate.impl.BuildScriptWorkspaceCreateDelegateImpl;
import com.techburg.autospring.delegate.impl.FileUploadWorkspaceCreateDelegateImpl;
import com.techburg.autospring.delegate.impl.PersistingWorkspaceModifyDelegateImpl;
import com.techburg.autospring.factory.abstr.IWorkspaceFactory;
import com.techburg.autospring.model.BasePersistenceQuery.DataRange;
import com.techburg.autospring.model.WorkspacePersistenceQuery;
import com.techburg.autospring.model.business.BrowsingObject;
import com.techburg.autospring.model.business.BrowsingObject.ObjectType;
import com.techburg.autospring.model.business.BrowsingObject.OpenType;
import com.techburg.autospring.model.business.Workspace;
import com.techburg.autospring.model.entity.SimplifiedBrowsingObject;
import com.techburg.autospring.service.abstr.IBrowsingObjectPersistentService;
import com.techburg.autospring.service.abstr.IWorkspacePersistenceService;
import com.techburg.autospring.service.abstr.PersistenceResult;
import com.techburg.autospring.util.FileUtil;

@Controller(value = "workspaceController")
public class WorkspaceController {
	private static final String gWorkspaceListAttributeName = "workspaces";
	private static final String gWorkspaceIdAttributeName = "workspaceId";
	private static final String gEdittingWorkspaceAttributeName = "edittingWorkspace";
	private static final String gWorkspaceDescriptionAttributeName = "workspaceDescription";
	private static final String gWorkspaceScriptContentAttributeName = "scriptContent";
	private static final String gRedirectPageAttributeName = "redirectPage";

	private static final String gWorkspaceDBErrorMessage = "Can't persist workspace";

	private static final String gWorkspaceErrorAttributeName = "errorMessage";

	private static final String gToCreateBuildScriptWorkspaceIdAttributeName = "workspaceId";

	private IWorkspacePersistenceService mWorkspacePersistenceService;
	private IWorkspaceFactory mWorkspaceFactory;
	private IBrowsingObjectPersistentService mBrowsingObjectPersistentService;
	private IWorkspaceFileSystemDelegate mWorkspaceFileSystemDelegate;

	//MARK: Injecting methods
	@Autowired
	public void setWorkspacePersistenceService(IWorkspacePersistenceService workspacePersistenceService) {
		mWorkspacePersistenceService = workspacePersistenceService;
	}

	@Autowired
	public void setWorkspaceFactory(IWorkspaceFactory workspaceFactory) {
		mWorkspaceFactory = workspaceFactory;
	}

	@Autowired
	public void setWorkspaceFileSystemDelegate(IWorkspaceFileSystemDelegate workspaceFileSystemDelegate) {
		mWorkspaceFileSystemDelegate = workspaceFileSystemDelegate;
	}

	@Resource
	public void setBrowsingObjectPersistentService(IBrowsingObjectPersistentService browsingObjectPersistentService) {
		mBrowsingObjectPersistentService = browsingObjectPersistentService;
	}

	//MARK: Controller methods
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
			return createRenderPageForError(model, "Workspace not found");
		}
		model.addAttribute(gEdittingWorkspaceAttributeName, edittingWorkspace);
		model.addAttribute(gWorkspaceScriptContentAttributeName, mWorkspaceFileSystemDelegate.getWorkspaceScriptContent(edittingWorkspace));
		return "workspace";
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
			model.addAttribute(gWorkspaceDescriptionAttributeName, workspace.getDescription());
		}
		model.addAttribute(gWorkspaceIdAttributeName, workspaceId);
		return "workspacedetail";
	}

	@RequestMapping(value="/workspace/browse/{workspaceId}", method=RequestMethod.GET)
	public String browseWorkspace(Model model, @PathVariable long workspaceId) {
		Workspace workspace = getWorkspacebyId(workspaceId);
		if(workspace != null) {
			BrowsingObject workspaceRootBrowsingObject = mBrowsingObjectPersistentService.getBrowsingObjectByPath(workspace.getDirectoryPath());
			if(workspaceRootBrowsingObject != null) {
				return "redirect:/browse/" + workspaceRootBrowsingObject.getId();
			}
		}
		return createRenderPageForError(model, "Couldn't find workspace on the file system");
	}

	@RequestMapping(value="/api/getPossibleBuildScripts/{workspaceId}", produces="application/json", method = RequestMethod.GET)
	@ResponseBody 
	public List<SimplifiedBrowsingObject> getPossibleBuildScripts(@PathVariable long workspaceId) {
		List<SimplifiedBrowsingObject> results = new ArrayList<SimplifiedBrowsingObject>();
		Workspace workspace = getWorkspacebyId(workspaceId);
		if(workspace != null) {
			BrowsingObject workspaceRootBrowsingObject = mBrowsingObjectPersistentService.getBrowsingObjectByPath(workspace.getDirectoryPath());
			List<BrowsingObject> childBrowsingObjects = new LinkedList<BrowsingObject>();
			mBrowsingObjectPersistentService.getChildBrowsingObjects(workspaceRootBrowsingObject, childBrowsingObjects);

			for(BrowsingObject browsingObject : childBrowsingObjects) {
				//FIXME: Possibly refactor to use separated filter
				//Currently only deal with 1-level child files 
				if(browsingObject.getObjectType() == ObjectType.TYPE_FILE && browsingObject.getOpenType() == OpenType.OPEN_BY_BROWSER){
					results.add(new SimplifiedBrowsingObject(browsingObject));
				}
			}
		}
		return results;
	}

	@RequestMapping(value="/workspace/create", method=RequestMethod.POST)
	public String createNewWorkspace(Model model,
			@RequestParam(value = "zipFile", required = false) MultipartFile file,
			@RequestParam(value = "workspacename", required = true) String workspaceName,
			@RequestParam(value = "workspacedescription", required = true) String workspaceDescription,
			@RequestParam(value = "buildscript_timing", required = true) String buildScriptSubmitted,
			@RequestParam(value = "buildscriptname", required = false) String buildScriptName,
			@RequestParam(value = "buildscriptcontent", required = false) String buildScriptContent) {

		boolean buildScriptDeferred = buildScriptSubmitted.equals("0");

		//Create workspace along with an empty directory
		Workspace newWorkspace = createNewWorkspaceInternal(workspaceName, workspaceDescription, buildScriptDeferred, buildScriptName);

		//Store build script content, extract uploaded file ...
		try {
			postProcessWorkspaceCreation(newWorkspace, file, buildScriptDeferred, buildScriptContent);
		}
		catch(Exception e) {
			return createRenderPageForError(model, e.getLocalizedMessage());
		}

		//Persist workspace into DB in synchronous mode
		if(mWorkspacePersistenceService.persistWorkspace(newWorkspace, true) != PersistenceResult.REMOVE_SUCCESSFUL) {	
			return createRenderPageForError(model, gWorkspaceDBErrorMessage);
		}

		return createRenderPageForNewWorkspace(model, newWorkspace);
	}

	@RequestMapping(value="/workspace/create_github", method=RequestMethod.POST)
	public String createNewWorkspaceFromGitHub(Model model, 
			@RequestParam(value = "workspacename", required = true) String workspaceName,
			@RequestParam(value = "workspacedescription", required = true) String workspaceDescription,
			@RequestParam(value = "buildscriptname", required = true) String buildScriptName,
			@RequestParam(value = "buildscriptcontent", required = true) String buildScriptContent) {

		//Create workspace along with an empty directory
		Workspace newWorkspace = createNewWorkspaceInternal(workspaceName, workspaceDescription, false, buildScriptName);

		//Store build script content, extract uploaded file ...
		try {
			postProcessWorkspaceCreation(newWorkspace, null, false, buildScriptContent);
		}
		catch(Exception e) {
			return createRenderPageForError(model, e.getLocalizedMessage());
		}

		//Persist workspace into DB in synchronous mode
		if(mWorkspacePersistenceService.persistWorkspace(newWorkspace, true) != PersistenceResult.REMOVE_SUCCESSFUL) {	
			return createRenderPageForError(model, gWorkspaceDBErrorMessage);
		}

		return createRenderPageForNewWorkspace(model, newWorkspace);
	}

	@RequestMapping(value="/workspace/update/{workspaceId}", method=RequestMethod.POST)
	public String updateWorkspace(Model model, 
			@PathVariable long workspaceId,
			@RequestParam(value = "workspacedescription", required = false, defaultValue = "") String workspaceDescription,
			@RequestParam(value = "buildscriptname", required = false, defaultValue = "") String buildScriptName, 
			@RequestParam(value = "buildscriptcontent", required = true) String buildScriptContent) {

		Workspace updatingWorkspace = getWorkspacebyId(workspaceId);
		if(updatingWorkspace == null) {
			return createRenderPageForError(model, "Workspace not found");
		}

		modifyWorkspace(updatingWorkspace, workspaceDescription, buildScriptName);

		try {
			postProcessWorkspaceModified(updatingWorkspace, buildScriptContent);
			return "redirect:/workspace/detail/" + workspaceId;
		}
		catch (Exception e) {
			e.printStackTrace();
			return createRenderPageForError(model, "Workspace can't updated");
		}
	}

	//MARK: Private methods
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

	private Workspace createNewWorkspaceInternal(String workspaceName, 
			String workspaceDescription, 
			boolean deferredBuildScript, 
			String buildScriptName) {
		//Create workspace along with workspace directory. If no build script name specified -> create in deferred mode
		Workspace newWorkspace = deferredBuildScript ? mWorkspaceFactory.createWorkspaceDeferringBuildScript(workspaceName) : mWorkspaceFactory.createWorkspace(workspaceName, buildScriptName);
		newWorkspace.setDescription(workspaceDescription);
		return newWorkspace;
	}

	private void postProcessWorkspaceCreation(Workspace newWorkspace, 
			MultipartFile uploadedFile, 
			boolean buildScriptDeferred, 
			String buildScriptContent) throws Exception {

		AbstractWorkspaceCreateDelegate workspaceDelegate = getWorkspaceCreateDelegate(uploadedFile, 
				buildScriptDeferred, 
				buildScriptContent);
		try {
			workspaceDelegate.onWorkspaceCreated(newWorkspace);
		}
		catch(Exception e) {
			mWorkspaceFileSystemDelegate.eraseWorkspace(newWorkspace);
			throw e;
		}
	}

	private void modifyWorkspace(Workspace edittingWorkspace, 
			String workspaceDescription, 
			String buildScriptName) {
		if(workspaceDescription != null && !workspaceDescription.isEmpty()) {
			edittingWorkspace.setDescription(workspaceDescription);
		}
		if(buildScriptName != null && !buildScriptName.isEmpty()) {
			edittingWorkspace.setScriptFilePath(edittingWorkspace.getDirectoryPath() + File.separator + buildScriptName);
		}
	}

	private void postProcessWorkspaceModified(Workspace workspace, String buildScriptContent) throws Exception {
		AbstractWorkspaceModifyDelegate workspaceDelegate = getWorkspaceUpdateDelegate(buildScriptContent);
		try {
			workspaceDelegate.onWorkspaceModified(workspace);
		}
		catch(Exception e) {
			throw e;
		}
	}

	//MARK: Method to generate proper delegate
	private AbstractWorkspaceCreateDelegate getWorkspaceCreateDelegate(MultipartFile file, 
			boolean deferredBuildScript, 
			String buildScriptContent) {

		AbstractWorkspaceCreateDelegate fileUploadDelegate = null;
		if(file != null && !file.isEmpty()) {
			fileUploadDelegate = new FileUploadWorkspaceCreateDelegateImpl(file, mWorkspaceFileSystemDelegate);
		}
		AbstractWorkspaceCreateDelegate buildScriptDelegate = null;
		if(!deferredBuildScript) {
			buildScriptDelegate = new BuildScriptWorkspaceCreateDelegateImpl(buildScriptContent, mWorkspaceFileSystemDelegate);
		}
		//Assume that the case both empty file and empty build script never happens as a result of a validation elsewhere.
		if(fileUploadDelegate != null) {
			fileUploadDelegate.setSuccessor(buildScriptDelegate);
			return fileUploadDelegate;
		}
		return buildScriptDelegate;
	}

	private AbstractWorkspaceModifyDelegate getWorkspaceUpdateDelegate(String buildScriptContent) {
		AbstractWorkspaceModifyDelegate buildScriptSavingDelegate = new BuildScriptSavingWorkspaceModifyDelegateImpl(mWorkspaceFileSystemDelegate, buildScriptContent);
		AbstractWorkspaceModifyDelegate persistingDelegate = new PersistingWorkspaceModifyDelegateImpl(mWorkspacePersistenceService, mBrowsingObjectPersistentService);
		buildScriptSavingDelegate.setSuccessor(persistingDelegate);
		return buildScriptSavingDelegate;
	}

	//MARK: To render error pages (may extend to other pages)
	private String createRenderPageForError(Model model, String errorMessage) {
		model.addAttribute(gWorkspaceErrorAttributeName, errorMessage);
		return "forward:/error_page";
	}

	private String createRenderPageForNewWorkspace(Model model, Workspace newWorkspace) {
		//If workspace ready, go to inform page
		if(newWorkspace.getScriptFilePath() != null) {
			model.addAttribute(gRedirectPageAttributeName, "/workspace/list");
			return "inform";
		}

		model.addAttribute(gToCreateBuildScriptWorkspaceIdAttributeName, newWorkspace.getId());
		return "workspace_new_buildscript";
	}
}
