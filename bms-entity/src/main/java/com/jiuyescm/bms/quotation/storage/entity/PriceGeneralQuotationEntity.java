package com.jiuyescm.bms.quotation.storage.entity;

import com.jiuyescm.bms.common.BmsCommonAttribute;

/**
 * 通用费用报价实体类
 * 
 * @author cjw
 * 
 */
public class PriceGeneralQuotationEntity extends BmsCommonAttribute {

	private static final long serialVersionUID = -4022063368989633228L;
	// 报价编号
	private String quotationNo;
	// 业务类型
	private String bizTypeCode;
	// 模版类型（S:标准，C:常规）
	private String templateType;
	// 费用科目
	private String subjectId;
	private String subjectName;
	// 计费单位
	private String feeUnitCode;
	// 单价
	private Double unitPrice;
	// 计费说明
	private String description;
	// 报价类型
	private String priceType;
	// 备注
	private String remark;
	//商家ID
	private String customerId;
	//商家名称
	private String customerName;
	// 规则编号
	private String ruleNo;
	private String ruleName;
	
	// 参数1
	private String param1;

	public PriceGeneralQuotationEntity() {

	}

	/**
	 * 报价编号
	 */
	public String getQuotationNo() {
		return this.quotationNo;
	}

	/**
	 * 报价编号
	 * 
	 * @param quotationNo
	 */
	public void setQuotationNo(String quotationNo) {
		this.quotationNo = quotationNo;
	}

	/**
	 * 业务类型
	 */
	public String getBizTypeCode() {
		return this.bizTypeCode;
	}

	/**
	 * 业务类型
	 * 
	 * @param bizTypeCode
	 */
	public void setBizTypeCode(String bizTypeCode) {
		this.bizTypeCode = bizTypeCode;
	}

	/**
	 * 费用科目
	 */
	public String getSubjectId() {
		return this.subjectId;
	}

	/**
	 * 费用科目
	 * 
	 * @param subjectId
	 */
	public void setSubjectId(String subjectId) {
		this.subjectId = subjectId;
	}

	/**
	 * 计费单位
	 */
	public String getFeeUnitCode() {
		return this.feeUnitCode;
	}

	/**
	 * 计费单位
	 * 
	 * @param feeUnitCode
	 */
	public void setFeeUnitCode(String feeUnitCode) {
		this.feeUnitCode = feeUnitCode;
	}

	/**
	 * 单价
	 */
	public Double getUnitPrice() {
		return this.unitPrice;
	}

	/**
	 * 单价
	 * 
	 * @param unitPrice
	 */
	public void setUnitPrice(Double unitPrice) {
		this.unitPrice = unitPrice;
	}

	/**
	 * 计费说明
	 */
	public String getDescription() {
		return this.description;
	}

	/**
	 * 计费说明
	 * 
	 * @param description
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * 备注
	 */
	public String getRemark() {
		return this.remark;
	}

	/**
	 * 备注
	 * 
	 * @param remark
	 */
	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getPriceType() {
		return priceType;
	}

	public void setPriceType(String priceType) {
		this.priceType = priceType;
	}


	public String getCustomerId() {
		return customerId;
	}


	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}


	public String getCustomerName() {
		return customerName;
	}


	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getTemplateType() {
		return templateType;
	}

	public void setTemplateType(String templateType) {
		this.templateType = templateType;
	}

	public String getParam1() {
		return param1;
	}

	public void setParam1(String param1) {
		this.param1 = param1;
	}

	public String getRuleNo() {
		return ruleNo;
	}

	public void setRuleNo(String ruleNo) {
		this.ruleNo = ruleNo;
	}

	public String getRuleName() {
		return ruleName;
	}

	public void setRuleName(String ruleName) {
		this.ruleName = ruleName;
	}

	public String getSubjectName() {
		return subjectName;
	}

	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}

}
