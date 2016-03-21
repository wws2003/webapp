package com.techburg.autospring.delegate.abstr;

import com.techburg.autospring.model.business.Workspace;

public abstract class AbstractWorkspaceModifyDelegate {
	private AbstractWorkspaceModifyDelegate mSuccessor;
	
	public AbstractWorkspaceModifyDelegate(AbstractWorkspaceModifyDelegate successor) {
		mSuccessor = successor;
	}
	
	public AbstractWorkspaceModifyDelegate() {
		mSuccessor = null;
	}
	
	public void setSuccessor(AbstractWorkspaceModifyDelegate successor) {
		mSuccessor = successor;
	}
	
	//Persist to DB, save build script..., i.e. actions after workspace properties changed
	public void onWorkspaceModified(Workspace workspace) throws Exception {
		handleWorkspaceModified(workspace);
		if(mSuccessor != null) {
			mSuccessor.onWorkspaceModified(workspace);
		}
	}
	
	protected abstract void handleWorkspaceModified(Workspace workspace) throws Exception;
}