package com.techburg.autospring.delegate.abstr;

import com.techburg.autospring.model.business.Workspace;

public abstract class AbstractWorkspaceCreateDelegate {
	private AbstractWorkspaceCreateDelegate mSuccessor;
	
	public AbstractWorkspaceCreateDelegate(AbstractWorkspaceCreateDelegate successor) {
		mSuccessor = successor;
	}
	
	public AbstractWorkspaceCreateDelegate() {
		mSuccessor = null;
	}
	
	public void setSuccessor(AbstractWorkspaceCreateDelegate successor) {
		mSuccessor = successor;
	}
	
	//Save build script content, extract zip file or actions
	public void onWorkspaceCreated(Workspace workspace) throws Exception {
		handleWorkspaceCreated(workspace);
		if(mSuccessor != null) {
			mSuccessor.onWorkspaceCreated(workspace);
		}
	}
	
	protected abstract void handleWorkspaceCreated(Workspace workspace) throws Exception;
}
