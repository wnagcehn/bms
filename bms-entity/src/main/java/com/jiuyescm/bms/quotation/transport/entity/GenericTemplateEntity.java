package com.jiuyescm.bms.quotation.transport.entity;


import com.jiuyescm.bms.common.BmsCommonAttribute;

/**
 * 通用费用报价模板
 * @author wubangjun
 *
 */
public class GenericTemplateEntity extends BmsCommonAttribute {

	private static final long serialVersionUID = -6313003147157980528L;
	/**
	 * 模板编号
	 */
	private String templateCode;
	/**
	 * 模板名称
	 */
	private String templateName;
	// 模版类型（S:标准，C:常规）
	private String templateType;

	/**
	 * 业务类型
	 */
	private String bizTypeCode;

	/**
	 * 费用科目
	 */
	private String subjectId;
	/**
	 * 计费方式
	 */
	private Double billWayCode;
	
	/**
	 * 备注
	 */
	private String remark;
	// 参数1
	private String param1;
	
	private String storageTemplateType;
	//商家ID
	private String customerId;
	//商家名称
	private String customerName;
	
	//模板编号/名称
	private String quotationNo;
	private String quotationName;
	public String getStorageTemplateType() {
		return storageTemplateType;
	}

	public void setStorageTemplateType(String storageTemplateType) {
		this.storageTemplateType = storageTemplateType;
	}

	public String getTemplateCode() {
		return templateCode;
	}

	public void setTemplateCode(String templateCode) {
		this.templateCode = templateCode;
	}

	public String getTemplateName() {
		return templateName;
	}

	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}

	public String getBizTypeCode() {
		return bizTypeCode;
	}

	public void setBizTypeCode(String bizTypeCode) {
		this.bizTypeCode = bizTypeCode;
	}

	public String getSubjectId() {
		return subjectId;
	}

	public void setSubjectId(String subjectId) {
		this.subjectId = subjectId;
	}

	public Double getBillWayCode() {
		return billWayCode;
	}

	public void setBillWayCode(Double billWayCode) {
		this.billWayCode = billWayCode;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
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

	public String getQuotationNo() {
		return quotationNo;
	}

	public void setQuotationNo(String quotationNo) {
		this.quotationNo = quotationNo;
	}

	public String getQuotationName() {
		return quotationName;
	}

	public void setQuotationName(String quotationName) {
		this.quotationName = quotationName;
	}
	
}
