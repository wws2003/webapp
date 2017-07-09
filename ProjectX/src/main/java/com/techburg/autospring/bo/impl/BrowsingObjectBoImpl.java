package com.techburg.autospring.bo.impl;

import com.techburg.autospring.bo.abstr.IBrowsingObjectBo;
import com.techburg.autospring.model.business.BrowsingObject;
import com.techburg.autospring.model.entity.BrowsingObjectEntity;

public class BrowsingObjectBoImpl implements IBrowsingObjectBo {

	@Override
	public BrowsingObject getBusinessObjectFromEntity(BrowsingObjectEntity entity) {
		BrowsingObject browsingObject = new BrowsingObject();
		browsingObject.setAbsolutePath(entity.getAbsolutePath());
		browsingObject.setId(entity.getId());
		browsingObject.setModifiedTime(entity.getModifiedTime());
		browsingObject.setObjectType(entity.getObjectType());
		browsingObject.setOpenType(entity.getOpenType());
		BrowsingObject browsingObjectParent = null;
		if (entity.getParent() != null) {
			BrowsingObjectEntity parentEntity = entity.getParent();
			browsingObjectParent = new BrowsingObject();
			browsingObjectParent.setId(parentEntity.getId());
			browsingObjectParent.setAbsolutePath(parentEntity.getAbsolutePath());
		}
		browsingObject.setParent(browsingObjectParent);
		return browsingObject;
	}

	@Override
	public BrowsingObjectEntity getEntityFromBusinessObject(BrowsingObject browsingObject) {
		BrowsingObjectEntity entity = new BrowsingObjectEntity();
		entity.setAbsolutePath(browsingObject.getAbsolutePath());
		entity.setId(browsingObject.getId());
		entity.setModifiedTime(browsingObject.getModifiedTime());
		entity.setObjectType(browsingObject.getObjectType());
		entity.setOpenType(browsingObject.getOpenType());
		BrowsingObjectEntity entityParent = null;
		if (browsingObject.getParent() != null) {
			entityParent = new BrowsingObjectEntity();
			entityParent.setId(browsingObject.getParent().getId());
		}
		entity.setParent(entityParent);
		return entity;
	}

}
