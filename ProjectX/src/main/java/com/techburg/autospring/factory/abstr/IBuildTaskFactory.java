package com.techburg.autospring.factory.abstr;

import com.techburg.autospring.task.abstr.IBuildTask;

public interface IBuildTaskFactory {
	IBuildTask getNewBuildTask();
}
