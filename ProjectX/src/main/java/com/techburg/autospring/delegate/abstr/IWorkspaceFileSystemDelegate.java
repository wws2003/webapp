package com.techburg.autospring.delegate.abstr;

import org.springframework.web.multipart.MultipartFile;

import com.techburg.autospring.model.business.Workspace;

public interface IWorkspaceFileSystemDelegate {
	//Save build script content into workspace folder, assume that workspace has already had a build script
	void saveWorkspaceBuildScriptContent(Workspace workspace, String buildScriptContent) throws Exception;
	
	//Get content of workspace build script. Return null if failed to to that. Don't care about exception
	String getWorkspaceScriptContent(Workspace workspace);
	
	void extractUploadedZipFileToWorkspace(Workspace workspace, MultipartFile uploadedZipFile) throws Exception;
	
	//Remove workspace on file system
	void eraseWorkspace(Workspace workspace) throws Exception; 
}
