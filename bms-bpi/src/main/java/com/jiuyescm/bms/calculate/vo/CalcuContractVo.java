package com.jiuyescm.bms.calculate.vo;

public class CalcuContractVo {

	private String contractNo; 	//合同编号
	private String modelNo;		//报价模板编号
	private String contractAttr;//合同归属
	
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
	
	
}
