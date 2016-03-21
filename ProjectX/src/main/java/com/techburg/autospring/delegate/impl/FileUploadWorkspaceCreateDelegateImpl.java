package com.techburg.autospring.delegate.impl;

import org.springframework.web.multipart.MultipartFile;

import com.techburg.autospring.delegate.abstr.AbstractWorkspaceCreateDelegate;
import com.techburg.autospring.delegate.abstr.IWorkspaceFileSystemDelegate;
import com.techburg.autospring.model.business.Workspace;

public class FileUploadWorkspaceCreateDelegateImpl extends AbstractWorkspaceCreateDelegate {

	private MultipartFile mUploadedZipFile;
	private IWorkspaceFileSystemDelegate mWorkspaceFileSystemDelegate;
	private static final String gErrorMessage = "Uploaded file couldn't be extracted";

	public FileUploadWorkspaceCreateDelegateImpl(MultipartFile uploadedZipFile, IWorkspaceFileSystemDelegate workspaceFileSystemDelegate) {
		super();
		mUploadedZipFile = uploadedZipFile;
		mWorkspaceFileSystemDelegate = workspaceFileSystemDelegate;
	}

	@Override
	protected void handleWorkspaceCreated(Workspace workspace)
			throws Exception {
		try {
			mWorkspaceFileSystemDelegate.extractUploadedZipFileToWorkspace(workspace, mUploadedZipFile);	
		}
		catch(Exception primaryException) {
			primaryException.printStackTrace();
			throw new Exception(gErrorMessage);
		}
	}
}
