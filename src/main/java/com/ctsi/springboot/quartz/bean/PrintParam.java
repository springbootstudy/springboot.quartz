package com.ctsi.springboot.quartz.bean;

import java.io.Serializable;

public class PrintParam implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5423551853890814330L;
	
	private int appId;
	private int projectId;

	public int getAppId() {
		return appId;
	}

	public void setAppId(int appId) {
		this.appId = appId;
	}

	public int getProjectId() {
		return projectId;
	}

	public void setProjectId(int projectId) {
		this.projectId = projectId;
	}

}
