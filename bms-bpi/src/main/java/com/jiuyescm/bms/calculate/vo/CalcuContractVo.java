package com.jiuyescm.bms.calculate.vo;

import java.sql.Timestamp;
import java.util.Map;

public class CalcuContractVo {

	private String contractNo; 	//合同编号
	private String modelNo;		//报价模板编号
	private String contractAttr;//合同归属
	private String ruleNo;  	//规则编号    
    private Timestamp startDate;// 生效日期  
    private Timestamp expireDate;  // 失效日期
	private Map<String,String> itemMap;// 配送签约服务
	
	/**
	 * 合同编号
	 * @return
	 */
	public String getContractNo() {
		return contractNo;
	}
	/**
	 * 合同编号
	 * @param contractNo
	 */
	public void setContractNo(String contractNo) {
		this.contractNo = contractNo;
	}
	
	/**
	 * 报价模板编号
	 * @return
	 */
	public String getModelNo() {
		return modelNo;
	}
	
	/**
	 * 报价模板编号
	 * @param modelNo
	 */
	public void setModelNo(String modelNo) {
		this.modelNo = modelNo;
	}
	
	/**
	 * 合同归属
	 * @return
	 */
	public String getContractAttr() {
		return contractAttr;
	}
	
	/**
	 * 合同归属
	 * @param contractAttr
	 */
	public void setContractAttr(String contractAttr) {
		this.contractAttr = contractAttr;
	}
	public String getRuleNo() {
		return ruleNo;
	}
	public void setRuleNo(String ruleNo) {
		this.ruleNo = ruleNo;
	}
   
    public Timestamp getStartDate() {
        return startDate;
    }
    public void setStartDate(Timestamp startDate) {
        this.startDate = startDate;
    }
    public Timestamp getExpireDate() {
        return expireDate;
    }
    public void setExpireDate(Timestamp expireDate) {
        this.expireDate = expireDate;
    }
    public Map<String, String> getItemMap() {
        return itemMap;
    }
    public void setItemMap(Map<String, String> itemMap) {
        this.itemMap = itemMap;
    }
	
    
}
