package com.techburg.autospring.model.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@NamedQueries({
	@NamedQuery(name = "findBrowsingObjectByPath",
			query = "select bo from BrowsingObjectEntity bo where bo.absolutePath = :path"),
	@NamedQuery(name = "findBrowsingObjectByParent",
			query = "select bo from BrowsingObjectEntity bo where bo.parent.id = :parentId"),
	@NamedQuery(name = "removeAll",
			query = "delete from BrowsingObjectEntity")
})

@Table(name="browsing_object")
public class BrowsingObjectEntity {
	@Id
	@GeneratedValue
	private long id;

	@Column(name="object_type")
	private int objectType;

	@Column(name="open_type")
	private int openType;

	@Column(name="absolute_path", unique = true)
	private String absolutePath;

	@Column(name="modified_time")
	private Date modifiedTime;

	@ManyToOne(targetEntity = BrowsingObjectEntity.class, fetch = FetchType.LAZY)
	@JoinColumn(name = "parent_id", nullable = true)
	private BrowsingObjectEntity parent;

	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}

	public String getAbsolutePath() {
		return absolutePath;
	}
	public void setAbsolutePath(String absolutePath) {
		this.absolutePath = absolutePath;
	}

	public int getObjectType() {
		return objectType;
	}
	public void setObjectType(int objectType) {
		this.objectType = objectType;
	}

	public int getOpenType() {
		return openType;
	}
	public void setOpenType(int openType) {
		this.openType = openType;
	}

	public Date getModifiedTime() {
		return modifiedTime;
	}
	public void setModifiedTime(Date modifiedTime) {
		this.modifiedTime = modifiedTime;
	}

	public BrowsingObjectEntity getParent() {
		return parent;
	}
	public void setParent(BrowsingObjectEntity parent) {
		this.parent = parent;
	}
}
