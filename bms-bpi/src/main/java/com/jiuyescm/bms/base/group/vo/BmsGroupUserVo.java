package com.jiuyescm.bms.base.group.vo;

import java.io.Serializable;
import java.sql.Timestamp;

public class BmsGroupUserVo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3813521158899025692L;
	private int id;
	private int groupId;//数据组id
	private String groupName;//权限组名称
	private String userId;//用户ID
	private String userName;//用户姓名
	private String administrator;//权限 0-管理员 1-普通用户
	private String creator;
	private Timestamp createTime;
	private String lastModifier;
	private Timestamp lastModifyTime;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getGroupId() {
		return groupId;
	}
	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getAdministrator() {
		return administrator;
	}
	public void setAdministrator(String administrator) {
		this.administrator = administrator;
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
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	
}
