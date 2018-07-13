package com.jiuyescm.bms.quotation.contract.entity;

import com.jiuyescm.cfm.domain.IEntity;

public class ContractDetailEntity implements IEntity{
	private static final long serialVersionUID = -1;
	
	//合同编号
	private String contractCode;
	
	//运输类型判断是否是增值
	private String transportCode;
	
	private String transportName;
	
	private String transportRemark;
	
	//仓储类型判断是否是增值
	private String storageCode;
	
	private String storageName;
	
	private String storageRemark;
	
	private String generalStorageRemark;
	
	//模板编号
	private String templateId;
	
	//科目名称
	private String codeName;
	
	//区别判断是哪种类型
	private String codeType;
	
	private String code;
	
	//具体的科目编号
	private String subjectId;

	//配送报价名称
	private String dispatchName;
	
	private String dispatchRemark;
	
	//以下属性为 新增属性，只做记录
	//新增属性，确定费用科目
	private String subjectName;	
	//新增属性 ,确定费用科目，不加入数据库
	private String theLastName;
	//模板备注
	private String remark;
	
	private String ruleInstruction;//规则说明
	private String ruleNo;//规则编号
	
	public String getTransportCode() {
		return transportCode;
	}

	public void setTransportCode(String transportCode) {
		this.transportCode = transportCode;
	}

	public String getTransportName() {
		return transportName;
	}

	public void setTransportName(String transportName) {
		this.transportName = transportName;
	}

	public String getStorageCode() {
		return storageCode;
	}

	public void setStorageCode(String storageCode) {
		this.storageCode = storageCode;
	}

	public String getStorageName() {
		return storageName;
	}

	public void setStorageName(String storageName) {
		this.storageName = storageName;
	}

	public String getCodeName() {
		return codeName;
	}

	public void setCodeName(String codeName) {
		this.codeName = codeName;
	}

	public String getCodeType() {
		return codeType;
	}

	public void setCodeType(String codeType) {
		this.codeType = codeType;
	}

	public String getSubjectId() {
		return subjectId;
	}

	public void setSubjectId(String subjectId) {
		this.subjectId = subjectId;
	}

	public String getSubjectName() {
		return subjectName;
	}

	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}

	public String getTheLastName() {
		return theLastName;
	}

	public void setTheLastName(String theLastName) {
		this.theLastName = theLastName;
	}

	public String getDispatchName() {
		return dispatchName;
	}

	public void setDispatchName(String dispatchName) {
		this.dispatchName = dispatchName;
	}

	public String getTemplateId() {
		return templateId;
	}

	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}

	public String getContractCode() {
		return contractCode;
	}

	public void setContractCode(String contractCode) {
		this.contractCode = contractCode;
	}

	public String getTransportRemark() {
		return transportRemark;
	}

	public void setTransportRemark(String transportRemark) {
		this.transportRemark = transportRemark;
	}

	public String getStorageRemark() {
		return storageRemark;
	}

	public void setStorageRemark(String storageRemark) {
		this.storageRemark = storageRemark;
	}

	public String getDispatchRemark() {
		return dispatchRemark;
	}

	public void setDispatchRemark(String dispatchRemark) {
		this.dispatchRemark = dispatchRemark;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getGeneralStorageRemark() {
		return generalStorageRemark;
	}

	public void setGeneralStorageRemark(String generalStorageRemark) {
		this.generalStorageRemark = generalStorageRemark;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getRuleInstruction() {
		return ruleInstruction;
	}

	public void setRuleInstruction(String ruleInstruction) {
		this.ruleInstruction = ruleInstruction;
	}

	public String getRuleNo() {
		return ruleNo;
	}

	public void setRuleNo(String ruleNo) {
		this.ruleNo = ruleNo;
	}
	
	
	
}
