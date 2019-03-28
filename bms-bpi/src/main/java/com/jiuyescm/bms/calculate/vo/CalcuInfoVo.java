package com.jiuyescm.bms.calculate.vo;

public class CalcuInfoVo {

	private String ruleNo; 		//规则编号
	private String chargeType;	//计费方式  一口价  单价  首续价
	private String chargeUnit;	//计费单位 吨 ,箱数...
	private String chargeDescrip;//计费描述
	
	/**
	 * 规则编号
	 * @return
	 */
	public String getRuleNo() {
		return ruleNo;
	}
	
	/**
	 * 规则编号
	 * @param ruleNo
	 */
	public void setRuleNo(String ruleNo) {
		this.ruleNo = ruleNo;
	}
	
	/**
	 * 计费单位 吨 ,箱数...
	 * @return
	 */
	public String getChargeUnit() {
		return chargeUnit;
	}
	
	/**
	 * 计费单位 吨 ,箱数...
	 * @param chargeUnit
	 */
	public void setChargeUnit(String chargeUnit) {
		this.chargeUnit = chargeUnit;
	}
	
	/**
	 * 计费描述
	 * @return
	 */
	public String getChargeDescrip() {
		return chargeDescrip;
	}
	
	/**
	 * 计费描述
	 * @param chargeDescrip
	 */
	public void setChargeDescrip(String chargeDescrip) {
		this.chargeDescrip = chargeDescrip;
	}

	/**
	 * 计费方式  一口价  单价  首续价
	 * @return
	 */
	public String getChargeType() {
		return chargeType;
	}

	/**
	 * 计费方式  一口价  单价  首续价
	 * @param chargeType
	 */
	public void setChargeType(String chargeType) {
		this.chargeType = chargeType;
	}
	
}
