/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.quotation.transport.entity;

import java.util.List;

import com.jiuyescm.bms.common.BmsCommonAttribute;

/**
 * 运输报价模板
 * 
 * @author wubangjun
 */
public class PriceTransportTemplateEntity extends BmsCommonAttribute {

	private static final long serialVersionUID = 1L;

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
	 * 运输报价模板类型： 运输运费报价模板、运输增值费报价模板
	 */
	private String templateTypeCode;

	/**
	 * 备注
	 */
	private String remark;

	// 参数1
	private String param1;

	// 商家id
	private String customerId;
	// 商家名称
	private String customerName;
	
	private List<PriceTransportLineEntity> child;

	public PriceTransportTemplateEntity() {

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

	public String getTemplateType() {
		return templateType;
	}

	public void setTemplateType(String templateType) {
		this.templateType = templateType;
	}

	public String getTemplateTypeCode() {
		return templateTypeCode;
	}

	public void setTemplateTypeCode(String templateTypeCode) {
		this.templateTypeCode = templateTypeCode;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getParam1() {
		return param1;
	}

	public void setParam1(String param1) {
		this.param1 = param1;
	}

	public List<PriceTransportLineEntity> getChild() {
		return child;
	}

	public void setChild(List<PriceTransportLineEntity> child) {
		this.child = child;
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

	
}
