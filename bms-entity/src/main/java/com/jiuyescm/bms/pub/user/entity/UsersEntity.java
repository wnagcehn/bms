package com.jiuyescm.bms.pub.user.entity;

import com.jiuyescm.cfm.domain.IEntity;

/**
 * 用户基础数据
 */
public class UsersEntity implements IEntity {

	private static final long serialVersionUID = 8168599889215963983L;
	
	private String id;
	private String username;//用户名
	private String cname;//中文名
	private String address;//地址
	private String email;//邮箱
	private String mobile;//电话
	private String company;//公司
	
	
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getCname() {
		return cname;
	}

	public void setCname(String cname) {
		this.cname = cname;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String toString() {
		StringBuffer returnString = new StringBuffer();
		returnString.append("com.jiuyescm.bms.pub.user.entity.UsersEntity[");
		returnString.append("username = " + this.username + ";\n");
		returnString.append("address = " + this.address + ";\n");
		returnString.append("email = " + this.email + ";\n");
		returnString.append("cname = " + this.cname + ";\n");
		returnString.append("mobile = " + this.mobile + ";\n");
		returnString.append("company = " + this.company + ";\n");
		returnString.append("]\n");
		return returnString.toString();
	}

	/******************* 辅助方法 *******************/
	/*
	 * 实体Key字符
	 */
	public String getKey() {
		if (id == null || "".equals(id.trim())) {
			return null;
		}
		return id;
	}
}