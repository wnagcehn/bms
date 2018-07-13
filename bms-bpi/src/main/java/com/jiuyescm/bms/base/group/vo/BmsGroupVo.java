package com.jiuyescm.bms.base.group.vo;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

import javax.persistence.Transient;

public class BmsGroupVo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1030523224475301007L;

	private int id;
	private int parentId;
	private String groupName;
	//分组编码
	private String groupCode;
	private String bizType;
	private String creator;
	private Timestamp createTime;
	private String lastModifier;
	private Timestamp lastModifyTime;
	@Transient
	private List<BmsGroupVo> children;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getParentId() {
		return parentId;
	}
	public void setParentId(int parentId) {
		this.parentId = parentId;
	}
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	public String getBizType() {
		return bizType;
	}
	public void setBizType(String bizType) {
		this.bizType = bizType;
	}
	public String getCreator() {
		return creator;
	}
	public void setCreator(String creator) {
		this.creator = creator;
	}
	public Timestamp getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}
	public String getLastModifier() {
		return lastModifier;
	}
	public void setLastModifier(String lastModifier) {
		this.lastModifier = lastModifier;
	}
	public Timestamp getLastModifyTime() {
		return lastModifyTime;
	}
	public void setLastModifyTime(Timestamp lastModifyTime) {
		this.lastModifyTime = lastModifyTime;
	}
	public List<BmsGroupVo> getChildren() {
		return children;
	}
	public void setChildren(List<BmsGroupVo> children) {
		this.children = children;
	}
	public String getGroupCode() {
		return groupCode;
	}
	public void setGroupCode(String groupCode) {
		this.groupCode = groupCode;
	}
	
}
