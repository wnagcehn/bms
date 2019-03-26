package com.jiuyescm.bms.biz.storage.entity;

import java.math.BigDecimal;
import java.security.Timestamp;


public class StoFeeGeneralEntity{

	
	//费用科目
	private String subjectCode;
	//二级费用科目
	private String otherSubjectCode;
	//计费温度类型
	private String chargeTempCode;
	//计费数量
	private BigDecimal chargeQty;
	//计费重量
	private BigDecimal chargeWeight;
	//计费体积
	private BigDecimal chargeVolumn;
	//计费商品种类数
	private Integer chargeSkus;
	//计费箱数
	private BigDecimal chargeBox;
	//计费单位
	private String chargeUnit;
	//计费单价
	private BigDecimal chargeUnitPrice;
	//计费续价
	private BigDecimal chargeContinuedPrice;
	
	//首重重量/首量
	private BigDecimal chargeFirstNum;
	//首重价格/首价
	private BigDecimal chargeFirstPrice;
	//续重重量/续量
	private BigDecimal chargeContinueNum;
	//续重价格/续价
	private BigDecimal chargeContinuePrice;
	
	//金额
	private BigDecimal chargeAmount;
	//折扣金额
	private BigDecimal derateAmount;
	
	//计费规则编号
	private String chargeRuleNo;
	//计费状态
	private String chargeIsCalculated;
	//计费时间
	private Timestamp chargeCalculateTime;
	//计费异常描述
	private String calcu_msg;
	
	/**
	 * 费用科目编码
	 * @return
	 */
	public String getSubjectCode() {
		return subjectCode;
	}
	
	/**
	 * 费用科目编码
	 * @param subjectCode
	 */
	public void setSubjectCode(String subjectCode) {
		this.subjectCode = subjectCode;
	}
	
	/**
	 * 二级费用科目编码 针对增值费
	 * @return
	 */
	public String getOtherSubjectCode() {
		return otherSubjectCode;
	}
	
	/**
	 * 二级费用科目编码 针对增值费
	 * @param subjectCode
	 */
	public void setOtherSubjectCode(String otherSubjectCode) {
		this.otherSubjectCode = otherSubjectCode;
	}
	
	/**
	 * 计费温度类型
	 * @return
	 */
	public String getChargeTempCode() {
		return chargeTempCode;
	}
	
	/**
	 * 计费温度类型
	 * @param chargeTempCode
	 */
	public void setChargeTempCode(String chargeTempCode) {
		this.chargeTempCode = chargeTempCode;
	}
	
	/**
	 * 计费数量
	 * @return
	 */
	public BigDecimal getChargeQty() {
		return chargeQty;
	}
	
	/**
	 * 计费数量
	 * @param chargeQty
	 */
	public void setChargeQty(BigDecimal chargeQty) {
		this.chargeQty = chargeQty;
	}
	
	/**
	 * 计费重量
	 * @return
	 */
	public BigDecimal getChargeWeight() {
		return chargeWeight;
	}
	
	/**
	 * 计费重量
	 * @param chargeWeight
	 */
	public void setChargeWeight(BigDecimal chargeWeight) {
		this.chargeWeight = chargeWeight;
	}
	
	/**
	 * 计费体积
	 * @return
	 */
	public BigDecimal getChargeVolumn() {
		return chargeVolumn;
	}
	
	/**
	 * 计费体积
	 * @param chargeVolumn
	 */
	public void setChargeVolumn(BigDecimal chargeVolumn) {
		this.chargeVolumn = chargeVolumn;
	}
	
	/**
	 * 计费sku数
	 * @return
	 */
	public Integer getChargeSkus() {
		return chargeSkus;
	}
	
	/**
	 * 计费sku数
	 * @param chargeSkus
	 */
	public void setChargeSkus(Integer chargeSkus) {
		this.chargeSkus = chargeSkus;
	}
	
	/**
	 * 计费箱数
	 * @return
	 */
	public BigDecimal getChargeBox() {
		return chargeBox;
	}
	
	/**
	 * 计费箱数
	 * @param chargeBox
	 */
	public void setChargeBox(BigDecimal chargeBox) {
		this.chargeBox = chargeBox;
	}
	
	/**
	 * 计费单位
	 * @return
	 */
	public String getChargeUnit() {
		return chargeUnit;
	}
	
	/**
	 * 计费单位
	 * @param chargeUnit
	 */
	public void setChargeUnit(String chargeUnit) {
		this.chargeUnit = chargeUnit;
	}
	
	/**
	 * 计费单价
	 * @return
	 */
	public BigDecimal getChargeUnitPrice() {
		return chargeUnitPrice;
	}
	
	/**
	 * 计费单价
	 * @param chargeUnitPrice
	 */
	public void setChargeUnitPrice(BigDecimal chargeUnitPrice) {
		this.chargeUnitPrice = chargeUnitPrice;
	}
	
	/**
	 * 计费续价
	 * @return
	 */
	public BigDecimal getChargeContinuedPrice() {
		return chargeContinuedPrice;
	}
	
	/**
	 * 计费续价
	 * @param chargeContinuedPrice
	 */
	public void setChargeContinuedPrice(BigDecimal chargeContinuedPrice) {
		this.chargeContinuedPrice = chargeContinuedPrice;
	}
	
	/**
	 * 计费首重/首量
	 * @return
	 */
	public BigDecimal getChargeFirstNum() {
		return chargeFirstNum;
	}
	
	/**
	 * 计费首重/首量
	 * @param chargeFirstNum
	 */
	public void setChargeFirstNum(BigDecimal chargeFirstNum) {
		this.chargeFirstNum = chargeFirstNum;
	}
	
	/**
	 * 计费首价
	 * @return
	 */
	public BigDecimal getChargeFirstPrice() {
		return chargeFirstPrice;
	}
	
	/**
	 * 计费首价
	 * @param chargeFirstPrice
	 */
	public void setChargeFirstPrice(BigDecimal chargeFirstPrice) {
		this.chargeFirstPrice = chargeFirstPrice;
	}
	
	/**
	 * 计费续重/续量
	 * @return
	 */
	public BigDecimal getChargeContinueNum() {
		return chargeContinueNum;
	}
	
	/**
	 * 计费续重/续量
	 * @param chargeContinueNum
	 */
	public void setChargeContinueNum(BigDecimal chargeContinueNum) {
		this.chargeContinueNum = chargeContinueNum;
	}
	
	/**
	 * 计费续价
	 * @return
	 */
	public BigDecimal getChargeContinuePrice() {
		return chargeContinuePrice;
	}
	
	/**
	 * 计费续价
	 * @param chargeContinuePrice
	 */
	public void setChargeContinuePrice(BigDecimal chargeContinuePrice) {
		this.chargeContinuePrice = chargeContinuePrice;
	}
	
	/**
	 * 计费金额
	 * @return
	 */
	public BigDecimal getChargeAmount() {
		return chargeAmount;
	}
	
	/**
	 * 计费金额
	 * @param chargeAmount
	 */
	public void setChargeAmount(BigDecimal chargeAmount) {
		this.chargeAmount = chargeAmount;
	}
	
	/**
	 * 计费规则编号
	 * @return
	 */
	public String getChargeRuleNo() {
		return chargeRuleNo;
	}
	
	/**
	 * 计费规则编号
	 * @param chargeRuleNo
	 */
	public void setChargeRuleNo(String chargeRuleNo) {
		this.chargeRuleNo = chargeRuleNo;
	}
	
	/**
	 * 计费状态
	 * @return
	 */
	public String getChargeIsCalculated() {
		return chargeIsCalculated;
	}
	
	/**
	 * 计费状态
	 * @param chargeIsCalculated
	 */
	public void setChargeIsCalculated(String chargeIsCalculated) {
		this.chargeIsCalculated = chargeIsCalculated;
	}
	
	/**
	 * 计费时间
	 * @return
	 */
	public Timestamp getChargeCalculateTime() {
		return chargeCalculateTime;
	}
	
	/**
	 * 计费时间
	 * @param chargeCalculateTime
	 */
	public void setChargeCalculateTime(Timestamp chargeCalculateTime) {
		this.chargeCalculateTime = chargeCalculateTime;
	}
	
	/**
	 * 计费描述
	 * @return
	 */
	public String getCalcu_msg() {
		return calcu_msg;
	}
	
	/**
	 * 计费描述
	 * @param calcu_msg
	 */
	public void setCalcu_msg(String calcu_msg) {
		this.calcu_msg = calcu_msg;
	}

	/**
	 * 折扣金额
	 * @return
	 */
	public BigDecimal getDerateAmount() {
		return derateAmount;
	}

	/**
	 * 折扣金额
	 * @param derateAmount
	 */
	public void setDerateAmount(BigDecimal derateAmount) {
		this.derateAmount = derateAmount;
	}
}
