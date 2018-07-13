package com.jiuyescm.bms.pub.printconfig.entity;

import com.jiuyescm.cfm.domain.IEntity;

/**
 * 打印配置基础数据
 */
public class PrintConfigEntity implements IEntity {

	private static final long serialVersionUID = 8168599889215963983L;
	// 打印配置id
	private String id;
	// 用户编号
	private String usercode;
	// ip地址
	private String ip_port;
	// mac地址
	private String macaddress;
	// 打印类型
	private String printtype;
	// 打印机名称
	private String printname;
	//面单样式
	private String expressprintstyle;

	public PrintConfigEntity() {

	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUsercode() {
		return usercode;
	}

	public void setUsercode(String usercode) {
		this.usercode = usercode;
	}

	public String getIp_port() {
		return ip_port;
	}

	public void setIp_port(String ip_port) {
		this.ip_port = ip_port;
	}

	public String getMacaddress() {
		return macaddress;
	}

	public void setMacaddress(String macaddress) {
		this.macaddress = macaddress;
	}

	public String getPrintname() {
		return printname;
	}

	public void setPrintname(String printname) {
		this.printname = printname;
	}

	

	public String getPrinttype() {
		return printtype;
	}

	public void setPrinttype(String printtype) {
		this.printtype = printtype;
	}

	public String getExpressprintstyle() {
		return expressprintstyle;
	}

	public void setExpressprintstyle(String expressprintstyle) {
		this.expressprintstyle = expressprintstyle;
	}
	
	public String toString() {
		StringBuffer returnString = new StringBuffer();
		returnString.append("com.jiuye.oms.pub.printconfig.entity.PrintConfigEntity[");
		returnString.append("id = " + this.id + ";\n");
		returnString.append("usercode = " + this.usercode + ";\n");
		returnString.append("ip_port = " + this.ip_port + ";\n");
		returnString.append("macaddress = " + this.macaddress + ";\n");
		returnString.append("printtype = " + this.printtype + ";\n");
		returnString.append("printname = " + this.printname + ";\n");
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