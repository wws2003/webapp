package com.techburg.autospring.bo.abstr;

import com.techburg.autospring.model.business.BrowsingObject;
import com.techburg.autospring.model.entity.BrowsingObjectEntity;

public interface IBrowsingObjectBo {
	BrowsingObject getBusinessObjectFromEntity(BrowsingObjectEntity entity);
	BrowsingObjectEntity getEntityFromBusinessObject(BrowsingObject browsingObject);
}
