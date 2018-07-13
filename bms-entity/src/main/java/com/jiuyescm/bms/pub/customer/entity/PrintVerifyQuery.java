package com.jiuyescm.bms.pub.customer.entity;

public class PrintVerifyQuery {
	private String actualcarriercode;//物流商id
	private String customerid;//商家id
	private String num;
	private String digitalprintflag;//面单打印类型
	private String expressprintstyle;//面单样式
	
	public String getActualcarriercode() {
		return actualcarriercode;
	}
	public void setActualcarriercode(String actualcarriercode) {
		this.actualcarriercode = actualcarriercode;
	}
	public String getCustomerid() {
		return customerid;
	}
	public void setCustomerid(String customerid) {
		this.customerid = customerid;
	}
	public String getNum() {
		return num;
	}
	public void setNum(String num) {
		this.num = num;
	}
	public String getDigitalprintflag() {
		return digitalprintflag;
	}
	public void setDigitalprintflag(String digitalprintflag) {
		this.digitalprintflag = digitalprintflag;
	}
	public String getExpressprintstyle() {
		return expressprintstyle;
	}
	public void setExpressprintstyle(String expressprintstyle) {
		this.expressprintstyle = expressprintstyle;
	}
}
