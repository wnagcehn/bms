package com.jiuyescm.bms.base.group.web;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Transient;

public class DeptVoEntity implements Serializable{


	/**
	 * 
	 */
	private static final long serialVersionUID = 9220107377975189262L;
	private String id;
	private String parentId;
	private String deptName;
	@Transient
	private List<DeptVoEntity> children;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getParentId() {
		return parentId;
	}
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	public String getDeptName() {
		return deptName;
	}
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	public List<DeptVoEntity> getChildren() {
		return children;
	}
	public void setChildren(List<DeptVoEntity> children) {
		this.children = children;
	}
}
