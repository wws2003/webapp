package com.techburg.autospring.controller;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.techburg.autospring.factory.abstr.IBuildTaskFactory;
import com.techburg.autospring.model.BasePersistenceQuery.DataRange;
import com.techburg.autospring.model.BuildInfoPersistenceQuery;
import com.techburg.autospring.model.BuildInfoPersistenceQuery.BuildInfoDataRange;
import com.techburg.autospring.model.WorkspacePersistenceQuery;
import com.techburg.autospring.model.business.BuildInfo;
import com.techburg.autospring.model.business.BuiltInfoPage;
import com.techburg.autospring.model.business.Workspace;
import com.techburg.autospring.service.abstr.IBuildDataService;
import com.techburg.autospring.service.abstr.IBuildInfoPersistenceService;
import com.techburg.autospring.service.abstr.IWorkspacePersistenceService;
import com.techburg.autospring.service.abstr.PersistenceResult;
import com.techburg.autospring.task.abstr.IBuildTask;
import com.techburg.autospring.task.abstr.IBuildTaskProcessor;
import com.techburg.autospring.util.FileUtil;

@Controller
public class BuildController {

	private static final String gServiceAvailableAttributeName = "serviceAvailable";
	private static final String gBuildingListAttributeName = "buildingList";
	private static final String gWaitingListAttributeName = "waitingList";
	private static final String gPage = "page";
	private static final String gBuiltInfoPage = "builtInfoPage";
	
	private IBuildDataService mBuildDataService;
	private IBuildTaskProcessor mBuildTaskProcessor;
	private IBuildTaskFactory mBuildTaskFactory;
	private IBuildInfoPersistenceService mBuildInfoPersistenceService;
	private IWorkspacePersistenceService mWorkspacePersistenceService;

	@Autowired
	public void setBuildDataService(IBuildDataService buildDataService) {
		mBuildDataService = buildDataService;
	}

	@Autowired
	public void setBuildTaskProcessor(IBuildTaskProcessor buildTaskProcessor) {
		mBuildTaskProcessor = buildTaskProcessor;
	}

	@Autowired
	public void setBuildTaskFactory(IBuildTaskFactory buildTaskFactory) {
		mBuildTaskFactory = buildTaskFactory;
	}

	@Autowired
	public void setBuildInfoPersistenceService(IBuildInfoPersistenceService persistenceService) {
		mBuildInfoPersistenceService = persistenceService;
	}

	@Autowired
	public void setWorkspacePersistenceService(IWorkspacePersistenceService workspacePersistenceService) {
		mWorkspacePersistenceService = workspacePersistenceService;
	}

	@RequestMapping(value="/buildlist/{workspaceId}/", method=RequestMethod.GET) 
	public String listBuildInfo(
			@PathVariable long workspaceId,
			@RequestParam(value = gPage, required = false, defaultValue = "1") int page,
			Model model) {
		if(mBuildDataService == null) {
			model.addAttribute(gServiceAvailableAttributeName, false);
			return "buildlist";
		}
		model.addAttribute(gServiceAvailableAttributeName, true);

		List<BuildInfo> buildingList = new ArrayList<BuildInfo>();
		List<BuildInfo> waitingList = new ArrayList<BuildInfo>();

		mBuildDataService.getBuildingBuildInfoListOfWorkspace(buildingList, workspaceId);
		mBuildDataService.getWaitingBuildInfoListOfWorkspace(waitingList, workspaceId);
		model.addAttribute(gBuildingListAttributeName, buildingList);
		model.addAttribute(gWaitingListAttributeName, waitingList);
		
		BuiltInfoPage builtInfoPage = getBuiltInfoPage(page, workspaceId);
		if(builtInfoPage != null)
			model.addAttribute(gBuiltInfoPage, builtInfoPage);

		return "buildlist";
	}

	@RequestMapping(value="/testbuild/{workspaceId}/{numberOfBuildTask}", method=RequestMethod.GET) 
	public String testBuild(@PathVariable long workspaceId, @PathVariable int numberOfBuildTask, Model model) {

		String redirectURL = "redirect:/buildlist/" + workspaceId + "/";
		
		if(mBuildDataService == null) {
			model.addAttribute(gServiceAvailableAttributeName, false);
			return redirectURL;
		}

		for(int i = 0; i < numberOfBuildTask; i++) {
			Workspace workspace = getWorkspaceById(workspaceId);
			IBuildTask buildTask = mBuildTaskFactory.getNewBuildTask();
			buildTask.setWorkspace(workspace);
			mBuildTaskProcessor.addBuildTask(buildTask);
		}

		return redirectURL;
	}

	@RequestMapping(value="/build", method=RequestMethod.GET) 
	public String tryToBuildATask(Model model) {
		if(mBuildDataService == null) {
			model.addAttribute(gServiceAvailableAttributeName, false);
		}
		else {
			model.addAttribute(gServiceAvailableAttributeName, true);
		}
		return "build";
	}

	@RequestMapping(value="/build/{workspaceId}", method=RequestMethod.POST) 
	public String buildTask(Model model, @PathVariable long workspaceId) {
		if(mBuildDataService == null) {
			model.addAttribute(gServiceAvailableAttributeName, false);
			return "buildlist";
		}

		IBuildTask buildTask = mBuildTaskFactory.getNewBuildTask();
		Workspace workspace = getWorkspaceById(workspaceId);
		buildTask.setWorkspace(workspace);
		mBuildTaskProcessor.addBuildTask(buildTask);

		return "hello";
	}

	//TODO Apply workspace parameter
	@RequestMapping(value="/service/buildlist/{lastReceivedId}", method=RequestMethod.GET)
	public @ResponseBody List<BuildInfo> listBuildInfoFromLastId(@PathVariable long lastReceivedId) {
		List<BuildInfo> buildInfoList = new ArrayList<BuildInfo>();

		List<BuildInfo> buildingList = new ArrayList<BuildInfo>();
		List<BuildInfo> waitingList = new ArrayList<BuildInfo>();

		mBuildDataService.getBuildingBuildInfoList(buildingList);
		mBuildDataService.getWaitingBuildInfoList(waitingList);

		//Temporally solution for SQLite, which is not safe to read while writing
		if(buildingList.isEmpty() && waitingList.isEmpty()) {
			BuildInfoPersistenceQuery query = new BuildInfoPersistenceQuery();
			query.dataRange = DataRange.LIMITED_MATCH;
			query.firstId = lastReceivedId + 1;
			query.lastId = Long.MAX_VALUE;
			mBuildInfoPersistenceService.loadPersistedBuildInfo(buildInfoList, query);
		}

		return buildInfoList;
	}

	@RequestMapping(value="/cancel/{taskId}", method=RequestMethod.GET)
	public String cancelBuildTask(@PathVariable long taskId) {
		mBuildTaskProcessor.cancelTask(taskId);
		return "cancel";
	}

	@RequestMapping(value="/log/{buildId}", method=RequestMethod.GET) 
	public void showLog(@PathVariable long buildId, HttpServletResponse response) throws IOException {
		BuildInfoPersistenceQuery query = new BuildInfoPersistenceQuery();
		query.dataRange = DataRange.ID_MATCH;
		query.id = buildId;

		List<BuildInfo> buildInfoList = new ArrayList<BuildInfo>();
		mBuildInfoPersistenceService.loadPersistedBuildInfo(buildInfoList, query);
		if (buildInfoList.size() > 0) {
			BuildInfo buildInfo = buildInfoList.get(0);
			String logFilePath = buildInfo.getLogFilePath();
			try {
				writeLogFileContentToServletResponse(logFilePath, response);
			}
			catch (Exception e) {
				e.printStackTrace();
				response.getWriter().println("Some error occured");
			}
		}
	}

	private BuiltInfoPage getBuiltInfoPage(int page, long workspaceId) {
		long nbInstance = mBuildInfoPersistenceService.getNumberOfPersistedBuildInfo(workspaceId);
		int maxPage = (int) (nbInstance / BuiltInfoPage.BUILD_INFO_PER_PAGE) + (nbInstance % BuiltInfoPage.BUILD_INFO_PER_PAGE == 0 ? 0 : 1);

		List<BuildInfo> builtInfoList = new ArrayList<BuildInfo>();
		BuildInfoPersistenceQuery query = new BuildInfoPersistenceQuery();
		query.dataRange = BuildInfoDataRange.PAGE_MATCH;
		query.page = page;
		query.nbInstancePerPage = BuiltInfoPage.BUILD_INFO_PER_PAGE;
		query.workspace = workspaceId;

		int loadResult = mBuildInfoPersistenceService.loadPersistedBuildInfo(builtInfoList, query);
		if(loadResult != PersistenceResult.LOAD_SUCCESSFUL) {
			return null;
		}
		
		BuiltInfoPage builtInfoPage = new BuiltInfoPage();
		builtInfoPage.setWorkspaceId(workspaceId);
		builtInfoPage.setMaxPageNumber(maxPage);
		builtInfoPage.setPage(page);
		builtInfoPage.setBuiltInfoList(builtInfoList);

		return builtInfoPage;
	}

	private void writeLogFileContentToServletResponse(String logFilePath, HttpServletResponse response) throws Exception {
		InputStream logFileInputStream = null;
		FileUtil fileUtil = new FileUtil();
		try {
			logFileInputStream = new BufferedInputStream(new FileInputStream(logFilePath));
			StringBuilder outputBuilder = new StringBuilder();
			fileUtil.getStringFromInputStream(logFileInputStream, outputBuilder);
			response.getWriter().println(outputBuilder.toString());
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			logFileInputStream.close();
		}
	}

	private Workspace getWorkspaceById(long id) {
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
}
