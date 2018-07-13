package com.jiuyescm.bms.common.entity;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 
 * @author cjw by 2017-06-08
 *
 */
public class CalculateVo implements Serializable {
	
	private String bizTypeCode;	//业务类型
	private String subjectId;	//费用科目
	private Object obj;			//业务数据
	private String contractCode;//合同编号
	private String mobanCode;  	//模板编号
	private String ruleno;		//计算规则编号
	private BigDecimal price;   //价格
	private Object quateEntity; //模板/报价
	
	private String msg;			//异常说明
	private Boolean success;	//计算结果
	private Double unitPrice; // 报价里边的单价 用做财务
	private String type;   //计算类型，判断是应收还是应付	
	
	private Boolean isCalculate;
	private String extter1;
	private String extter2;
	
	
	public Double getUnitPrice() {
		return unitPrice;
	}
	public void setUnitPrice(Double unitPrice) {
		this.unitPrice = unitPrice;
	}
	
	/**
	 * 业务类型
	 * @return
	 */
	public String getBizTypeCode() {
		return bizTypeCode;
	}
	/**
	 * 业务类型
	 * @param bizTypeCode
	 */
	public void setBizTypeCode(String bizTypeCode) {
		this.bizTypeCode = bizTypeCode;
	}
	
	/**
	 * 费用科目
	 * @return
	 */
	public String getSubjectId() {
		return subjectId;
	}
	/**
	 * 费用科目
	 * @param subjectId
	 */
	public void setSubjectId(String subjectId) {
		this.subjectId = subjectId;
	}
	
	/**
	 * 业务数据
	 * @return
	 */
	public Object getObj() {
		return obj;
	}
	/**
	 * 业务数据
	 * @param obj
	 */
	public void setObj(Object obj) {
		this.obj = obj;
	}
	
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public Boolean getSuccess() {
		return success;
	}
	public void setSuccess(Boolean success) {
		this.success = success;
	}
	public String getContractCode() {
		return contractCode;
	}
	public void setContractCode(String contractCode) {
		this.contractCode = contractCode;
	}
	public String getRuleno() {
		return ruleno;
	}
	public void setRuleno(String ruleno) {
		this.ruleno = ruleno;
	}
	public BigDecimal getPrice() {
		return price;
	}
	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	public Object getQuateEntity() {
		return quateEntity;
	}
	public void setQuateEntity(Object quateEntity) {
		this.quateEntity = quateEntity;
	}
	public String getMobanCode() {
		return mobanCode;
	}
	public void setMobanCode(String mobanCode) {
		this.mobanCode = mobanCode;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Boolean getIsCalculate() {
		return isCalculate;
	}
	public void setIsCalculate(Boolean isCalculate) {
		this.isCalculate = isCalculate;
	}
	public String getExtter1() {
		return extter1;
	}
	public void setExtter1(String extter1) {
		this.extter1 = extter1;
	}
	public String getExtter2() {
		return extter2;
	}
	public void setExtter2(String extter2) {
		this.extter2 = extter2;
	}
	
	
}
