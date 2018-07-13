package com.jiuyescm.bms.pub;

import java.sql.Timestamp;

import com.jiuyescm.cfm.domain.IEntity;

public class PubRecordLogEntity implements IEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2118259802149544111L;
	
	private int id;
	private String bizType;
	private String operateType;
	private String operateTable;
	private String operateDesc;
	private String operatePerson;
	private Timestamp operateTime;
	private String remark;
	private String oldData;
	private String newData;
	private String urlName;
	private String operateTableKey;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getBizType() {
		return bizType;
	}
	public void setBizType(String bizType) {
		this.bizType = bizType;
	}
	public String getOperateType() {
		return operateType;
	}
	public void setOperateType(String operateType) {
		this.operateType = operateType;
	}
	public String getOperateTable() {
		return operateTable;
	}
	public void setOperateTable(String operateTable) {
		this.operateTable = operateTable;
	}
	public String getOperateDesc() {
		return operateDesc;
	}
	public void setOperateDesc(String operateDesc) {
		this.operateDesc = operateDesc;
	}
	public String getOperatePerson() {
		return operatePerson;
	}
	public void setOperatePerson(String operatePerson) {
		this.operatePerson = operatePerson;
	}
	public Timestamp getOperateTime() {
		return operateTime;
	}
	public void setOperateTime(Timestamp operateTime) {
		this.operateTime = operateTime;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getOldData() {
		return oldData;
	}
	public void setOldData(String oldData) {
		this.oldData = oldData;
	}
	public String getNewData() {
		return newData;
	}
	public void setNewData(String newData) {
		this.newData = newData;
	}
	public String getUrlName() {
		return urlName;
	}
	public void setUrlName(String urlName) {
		this.urlName = urlName;
	}
	public String getOperateTableKey() {
		return operateTableKey;
	}
	public void setOperateTableKey(String operateTableKey) {
		this.operateTableKey = operateTableKey;
	}
	
}
