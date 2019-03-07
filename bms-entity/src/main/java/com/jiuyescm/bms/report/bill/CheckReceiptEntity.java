package com.jiuyescm.bms.report.bill;

import java.math.BigDecimal;
import java.util.Date;

import com.jiuyescm.cfm.domain.IEntity;

public class CheckReceiptEntity implements IEntity{

	private static final long serialVersionUID = -4218736185462783227L;
	//预计回款日期，即快照日期
	private Date expectDate;
	private String area;
	private BigDecimal expectAmount;
	private BigDecimal finishAmount;
	private String finishRate;
	
	public BigDecimal getFinishAmount() {
		return finishAmount;
	}
	public void setFinishAmount(BigDecimal finishAmount) {
		this.finishAmount = finishAmount;
	}
	public String getFinishRate() {
		return finishRate;
	}
	public void setFinishRate(String finishRate) {
		this.finishRate = finishRate;
	}
	public Date getExpectDate() {
		return expectDate;
	}
	public void setExpectDate(Date expectDate) {
		this.expectDate = expectDate;
	}
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}
	public BigDecimal getExpectAmount() {
		return expectAmount;
	}
	public void setExpectAmount(BigDecimal expectAmount) {
		this.expectAmount = expectAmount;
	}

}