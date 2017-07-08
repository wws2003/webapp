package com.techburg.autospring.model.web;

import java.io.Serializable;

public class BreadCrumbModel implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * Relative URL
	 */
	private String path;

	/**
	 * Display name on the breadcrumb
	 */
	private String dispName;

	/**
	 * Flag to determine is the current page
	 */
	private boolean bCurrentPage;

	/**
	 * Order on the breadcrumb. Default = 0
	 */
	private int orderNo = 0;

	/**
	 * @param path
	 * @param dispName
	 * @param bCurrentPage
	 */
	public BreadCrumbModel(String path, String dispName, boolean bCurrentPage) {
		this.path = path;
		this.dispName = dispName;
		this.bCurrentPage = bCurrentPage;
	}

	/**
	 * @param path
	 * @param dispName
	 * @param bCurrentPage
	 * @param orderNo
	 */
	public BreadCrumbModel(String path, String dispName, boolean bCurrentPage,
			int orderNo) {
		this.path = path;
		this.dispName = dispName;
		this.bCurrentPage = bCurrentPage;
		this.orderNo = orderNo;
	}

	/**
	 * @return the path
	 */
	public String getPath() {
		return path;
	}

	/**
	 * @param path
	 *            the path to set
	 */
	public void setPath(String path) {
		this.path = path;
	}

	/**
	 * @return the dispName
	 */
	public String getDispName() {
		return dispName;
	}

	/**
	 * @param dispName
	 *            the dispName to set
	 */
	public void setDispName(String dispName) {
		this.dispName = dispName;
	}

	/**
	 * @return the bCurrentPage
	 */
	public boolean getbCurrentPage() {
		return bCurrentPage;
	}

	/**
	 * @param bCurrentPage
	 *            the bCurrentPage to set
	 */
	public void setbCurrentPage(boolean bCurrentPage) {
		this.bCurrentPage = bCurrentPage;
	}

	/**
	 * @return the orderNo
	 */
	public int getOrderNo() {
		return orderNo;
	}

	/**
	 * @param orderNo
	 *            the orderNo to set
	 */
	public void setOrderNo(int orderNo) {
		this.orderNo = orderNo;
	}
}
