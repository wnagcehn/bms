package com.jiuyescm.bms.bill.receive.vo;

import java.sql.Date;

import com.jiuyescm.cfm.domain.IEntity;

public class BmsBillCountEntityVo implements IEntity{
    // TODO Change serialVersionUID by eclipse
    private static final long serialVersionUID = -1;
    
    //月份
    private Date countDate;
    //未确认
    private Double unConfirmed;
    //未开票
    private Double unInvoiced;
    //部分开票
    private Double partInvoiced;
    //未回款
    private Double unReturnMoney;
    //超时未回款
    private Double outTimeUnReturnMoney;
    //已回款
    private Double isReturnMoney;
    //超时回款
    private Double outTimeReturnMoney;
    //总商家数
    private Double totalCustomerNum;
    //拓展字段1
    private String extra1;
    //拓展字段2
    private String extra2;
    //拓展字段3
    private String extra3;
    //拓展字段4
    private String extra4;
    //拓展字段5
    private String extra5;
    
  
	
	public Date getCountDate() {
		return countDate;
	}
	public void setCountDate(Date countDate) {
		this.countDate = countDate;
	}
	public Double getUnConfirmed() {
		return unConfirmed;
	}
	public void setUnConfirmed(Double unConfirmed) {
		this.unConfirmed = unConfirmed;
	}
	public Double getUnInvoiced() {
		return unInvoiced;
	}
	public void setUnInvoiced(Double unInvoiced) {
		this.unInvoiced = unInvoiced;
	}
	public Double getPartInvoiced() {
		return partInvoiced;
	}
	public void setPartInvoiced(Double partInvoiced) {
		this.partInvoiced = partInvoiced;
	}
	public Double getUnReturnMoney() {
		return unReturnMoney;
	}
	public void setUnReturnMoney(Double unReturnMoney) {
		this.unReturnMoney = unReturnMoney;
	}
	public Double getOutTimeUnReturnMoney() {
		return outTimeUnReturnMoney;
	}
	public void setOutTimeUnReturnMoney(Double outTimeUnReturnMoney) {
		this.outTimeUnReturnMoney = outTimeUnReturnMoney;
	}
	public Double getIsReturnMoney() {
		return isReturnMoney;
	}
	public void setIsReturnMoney(Double isReturnMoney) {
		this.isReturnMoney = isReturnMoney;
	}
	public Double getOutTimeReturnMoney() {
		return outTimeReturnMoney;
	}
	public void setOutTimeReturnMoney(Double outTimeReturnMoney) {
		this.outTimeReturnMoney = outTimeReturnMoney;
	}
	public Double getTotalCustomerNum() {
		return totalCustomerNum;
	}
	public void setTotalCustomerNum(Double totalCustomerNum) {
		this.totalCustomerNum = totalCustomerNum;
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
