package com.jiuyescm.bms.wechat.enquiry.vo;

import java.io.Serializable;

/**
 * 电商仓库返回实体
 * @author yangss
 * 
 */
public class ElecWarehouseVo implements Serializable {

	private static final long serialVersionUID = 7963107170842258069L;

	private String eleBizCode;
	private String eleBizName;
	private String logo;

	public ElecWarehouseVo() {
		super();
	}

	public ElecWarehouseVo(String eleBizCode, String eleBizName, String logo) {
		super();
		this.eleBizCode = eleBizCode;
		this.eleBizName = eleBizName;
		this.logo = logo;
	}

	public String getEleBizCode() {
		return eleBizCode;
	}

	public void setEleBizCode(String eleBizCode) {
		this.eleBizCode = eleBizCode;
	}

	public String getEleBizName() {
		return eleBizName;
	}

	public void setEleBizName(String eleBizName) {
		this.eleBizName = eleBizName;
	}

	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

}
