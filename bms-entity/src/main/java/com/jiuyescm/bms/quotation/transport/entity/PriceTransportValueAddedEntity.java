/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.quotation.transport.entity;

import com.jiuyescm.bms.common.BmsCommonAttribute;

/**
 * 运输报价模板对应的运输增值报价
 * 
 * @author wubangjun
 */
public class PriceTransportValueAddedEntity extends BmsCommonAttribute {

	private static final long serialVersionUID = 1L;

	/**
	 * 报价编号
	 */
	private String quotationNo;
	/**
	 * 车型
	 */
	private String carModelCode;
	/**
	 * 重量界限
	 */
	private Double weightLimit;
	/**
	 * 计费单位
	 */
	private String feeUnitCode;
	/**
	 * 单价
	 */
	private Double unitPrice;
	/**
	 * 费用科目
	 */
	private String subjectCode;
	/**
	 * 对应模版编号
	 */
	private String templateId;
	/**
	 * 备注
	 */
	private String remark;
	
	private String carModelName;
	private String feeUnitName;
	private String subjectName;

	private String extra1;
	
	private String extra2;
	
	private String extra3;
	
	private String extra4;
	
	private String extra5;
	
	public PriceTransportValueAddedEntity() {

	}

	public String getQuotationNo() {
		return quotationNo;
	}

	public void setQuotationNo(String quotationNo) {
		this.quotationNo = quotationNo;
	}

	public String getCarModelCode() {
		return carModelCode;
	}

	public void setCarModelCode(String carModelCode) {
		this.carModelCode = carModelCode;
	}

	public String getFeeUnitCode() {
		return feeUnitCode;
	}

	public void setFeeUnitCode(String feeUnitCode) {
		this.feeUnitCode = feeUnitCode;
	}

	public Double getWeightLimit() {
		return weightLimit;
	}

	public void setWeightLimit(Double weightLimit) {
		this.weightLimit = weightLimit;
	}

	public Double getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(Double unitPrice) {
		this.unitPrice = unitPrice;
	}

	public String getSubjectCode() {
		return subjectCode;
	}

	public void setSubjectCode(String subjectCode) {
		this.subjectCode = subjectCode;
	}

	public String getTemplateId() {
		return templateId;
	}

	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getCarModelName() {
		return carModelName;
	}

	public void setCarModelName(String carModelName) {
		this.carModelName = carModelName;
	}

	public String getFeeUnitName() {
		return feeUnitName;
	}

	public void setFeeUnitName(String feeUnitName) {
		this.feeUnitName = feeUnitName;
	}

	public String getSubjectName() {
		return subjectName;
	}

	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}

	public String getExtra1() {
		return extra1;
	}

	public void setExtra1(String extra1) {
		this.extra1 = extra1;
	}

	public String getExtra2() {
		return extra2;
	}

	public void setExtra2(String extra2) {
		this.extra2 = extra2;
	}

	public String getExtra3() {
		return extra3;
	}

	public void setExtra3(String extra3) {
		this.extra3 = extra3;
	}

	public String getExtra4() {
		return extra4;
	}

	public void setExtra4(String extra4) {
		this.extra4 = extra4;
	}

	public String getExtra5() {
		return extra5;
	}

	public void setExtra5(String extra5) {
		this.extra5 = extra5;
	}
	
}
