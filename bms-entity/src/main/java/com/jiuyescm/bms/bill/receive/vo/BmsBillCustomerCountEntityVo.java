package com.jiuyescm.bms.bill.receive.vo;

import com.jiuyescm.cfm.domain.IEntity;

public class BmsBillCustomerCountEntityVo implements IEntity{
    // TODO Change serialVersionUID by eclipse
    private static final long serialVersionUID = -1;
    
    //商家id
    private String customerId;   
    //商家名称
    private String customerName;    
    //结算员
    private String jieSuanPerson;
    private String countDate;
    //1月份金额
    private Double januaryMoney; 
    //2月份金额
    private Double februaryMoney; 
    //3月份金额
    private Double marchMoney; 
    //4月份金额
    private Double aprilMoney; 
    //5月份金额
    private Double mayMoney; 
    //6月份金额
    private Double juneMoney; 
    //7月份金额
    private Double julyMoney; 
    //8月份金额
    private Double augustMoney; 
    //9月份金额
    private Double septemberMoney; 
    //10月份金额
    private Double octoberMoney; 
    //11月份金额
    private Double novemberMoney; 
    //12月份金额
    private Double decemberMoney;   
    //合计金额
    private Double totalMoney;
    //未确认（单）
    private int noConfirm;
    //已确认未开票（元）
    private Double confirmUnInvoiced;
    //已确认无需发票未回款（元）
    private Double confirmUnNeedInvoiced;
    
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
	public String getJieSuanPerson() {
		return jieSuanPerson;
	}
	public void setJieSuanPerson(String jieSuanPerson) {
		this.jieSuanPerson = jieSuanPerson;
	}
	public String getCountDate() {
		return countDate;
	}
	public void setCountDate(String countDate) {
		this.countDate = countDate;
	}
	public Double getTotalMoney() {
		return totalMoney;
	}
	public void setTotalMoney(Double totalMoney) {
		this.totalMoney = totalMoney;
	}
	public int getNoConfirm() {
		return noConfirm;
	}
	public void setNoConfirm(int noConfirm) {
		this.noConfirm = noConfirm;
	}
	public Double getConfirmUnInvoiced() {
		return confirmUnInvoiced;
	}
	public void setConfirmUnInvoiced(Double confirmUnInvoiced) {
		this.confirmUnInvoiced = confirmUnInvoiced;
	}
	public Double getConfirmUnNeedInvoiced() {
		return confirmUnNeedInvoiced;
	}
	public void setConfirmUnNeedInvoiced(Double confirmUnNeedInvoiced) {
		this.confirmUnNeedInvoiced = confirmUnNeedInvoiced;
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
	public Double getJanuaryMoney() {
		return januaryMoney;
	}
	public void setJanuaryMoney(Double januaryMoney) {
		this.januaryMoney = januaryMoney;
	}
	public Double getFebruaryMoney() {
		return februaryMoney;
	}
	public void setFebruaryMoney(Double februaryMoney) {
		this.februaryMoney = februaryMoney;
	}
	public Double getMarchMoney() {
		return marchMoney;
	}
	public void setMarchMoney(Double marchMoney) {
		this.marchMoney = marchMoney;
	}
	public Double getAprilMoney() {
		return aprilMoney;
	}
	public void setAprilMoney(Double aprilMoney) {
		this.aprilMoney = aprilMoney;
	}
	public Double getMayMoney() {
		return mayMoney;
	}
	public void setMayMoney(Double mayMoney) {
		this.mayMoney = mayMoney;
	}
	public Double getJuneMoney() {
		return juneMoney;
	}
	public void setJuneMoney(Double juneMoney) {
		this.juneMoney = juneMoney;
	}
	public Double getJulyMoney() {
		return julyMoney;
	}
	public void setJulyMoney(Double julyMoney) {
		this.julyMoney = julyMoney;
	}
	public Double getAugustMoney() {
		return augustMoney;
	}
	public void setAugustMoney(Double augustMoney) {
		this.augustMoney = augustMoney;
	}
	public Double getSeptemberMoney() {
		return septemberMoney;
	}
	public void setSeptemberMoney(Double septemberMoney) {
		this.septemberMoney = septemberMoney;
	}
	public Double getOctoberMoney() {
		return octoberMoney;
	}
	public void setOctoberMoney(Double octoberMoney) {
		this.octoberMoney = octoberMoney;
	}
	public Double getNovemberMoney() {
		return novemberMoney;
	}
	public void setNovemberMoney(Double novemberMoney) {
		this.novemberMoney = novemberMoney;
	}
	public Double getDecemberMoney() {
		return decemberMoney;
	}
	public void setDecemberMoney(Double decemberMoney) {
		this.decemberMoney = decemberMoney;
	}
	public BmsBillCustomerCountEntityVo(){
		
	}
	
	public BmsBillCustomerCountEntityVo(String customerName,
			String jieSuanPerson, Double januaryMoney, Double februaryMoney,
			Double marchMoney, Double aprilMoney, Double mayMoney,
			Double juneMoney, Double julyMoney, Double augustMoney,
			Double septemberMoney, Double octoberMoney, Double novemberMoney,
			Double decemberMoney, Double totalMoney, int noConfirm,
			Double confirmUnInvoiced, Double confirmUnNeedInvoiced) {
		super();
		this.customerName = customerName;
		this.jieSuanPerson = jieSuanPerson;
		this.januaryMoney = januaryMoney;
		this.februaryMoney = februaryMoney;
		this.marchMoney = marchMoney;
		this.aprilMoney = aprilMoney;
		this.mayMoney = mayMoney;
		this.juneMoney = juneMoney;
		this.julyMoney = julyMoney;
		this.augustMoney = augustMoney;
		this.septemberMoney = septemberMoney;
		this.octoberMoney = octoberMoney;
		this.novemberMoney = novemberMoney;
		this.decemberMoney = decemberMoney;
		this.totalMoney = totalMoney;
		this.noConfirm = noConfirm;
		this.confirmUnInvoiced = confirmUnInvoiced;
		this.confirmUnNeedInvoiced = confirmUnNeedInvoiced;
	}
	
	
}
