package com.jiuyescm.bms.customercalc.vo;

import com.jiuyescm.cfm.domain.IEntity;

/**
 * 商家计算状况
 * @author zhaofeng
 *
 */
public class CustomerCalcStatusVo implements IEntity{
	
	private static final long serialVersionUID = -1;
	
	//计算日期
	private String calcDate;
	// 商家id
	private String customerId;
	// 商家全称
	private String customerName;
	//计算状态
	private String calcStatus;
	//科目数
	private String subjectNum;
	
	public String getCalcDate() {
		return calcDate;
	}
	public void setCalcDate(String calcDate) {
		this.calcDate = calcDate;
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
	public String getCalcStatus() {
		return calcStatus;
	}
	public void setCalcStatus(String calcStatus) {
		this.calcStatus = calcStatus;
	}
	public String getSubjectNum() {
		return subjectNum;
	}
	public void setSubjectNum(String subjectNum) {
		this.subjectNum = subjectNum;
	}
	
	
}
