/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.quotation.out.transport.entity;

import java.util.List;

import com.jiuyescm.bms.common.BmsCommonAttribute;

/**
 * 应付运输报价模板
 * @author wubangjun
 */
public class PriceTransportPayTemplateEntity extends BmsCommonAttribute {

	
	private static final long serialVersionUID = 1L;
	
	/**
	 * 模板编号
	 */
	private String templateCode;
	/**
	 * 模板名称
	 */
	private String templateName;

	/**
	 * 运输报价模板类型： 运输运费报价模板、运输增值费报价模板
	 */
	private String templateTypeCode;
	/**
	 * 运输产品类型
	 */
	private String transportTypeCode;

	
	/**
	 * 备注
	 */
	private String remark;
	
	private List<PriceTransportPayLineEntity>  child;
	
	public PriceTransportPayTemplateEntity() {

	}

	public List<PriceTransportPayLineEntity> getChild() {
		return child;
	}


	public void setChild(List<PriceTransportPayLineEntity> child) {
		this.child = child;
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


	public String getTransportTypeCode() {
		return transportTypeCode;
	}


	public void setTransportTypeCode(String transportTypeCode) {
		this.transportTypeCode = transportTypeCode;
	}


	public String getRemark() {
		return remark;
	}


	public void setRemark(String remark) {
		this.remark = remark;
	}


	public String getTemplateTypeCode() {
		return templateTypeCode;
	}


	public void setTemplateTypeCode(String templateTypeCode) {
		this.templateTypeCode = templateTypeCode;
	}
    
}
