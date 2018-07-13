package com.jiuyescm.bms.common.vo;

import com.jiuyescm.cfm.domain.IEntity;

public class ImportDataCompareVoEntity implements IEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String key;
	private String name;
	private String orgValue;
	private String importValue;
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getOrgValue() {
		return orgValue;
	}
	public void setOrgValue(String orgValue) {
		this.orgValue = orgValue;
	}
	public String getImportValue() {
		return importValue;
	}
	public void setImportValue(String importValue) {
		this.importValue = importValue;
	}
	
}
