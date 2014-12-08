package com.techburg.autospring.bo.abstr;

import com.techburg.autospring.model.business.BuildInfo;
import com.techburg.autospring.model.entity.BuildInfoEntity;

public interface IBuildInfoBo {
	public BuildInfoEntity getEntityFromBusinessObject(BuildInfo buildInfo);
	public BuildInfo getBusinessObjectFromEntity(BuildInfoEntity entity);
}
