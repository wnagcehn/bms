package com.jiuyescm.bms.report.month.vo;

import java.math.BigDecimal;

import com.jiuyescm.bms.report.month.entity.BmsMonthReportEntity;


public class BmsMonthReportVo extends BmsMonthReportEntity {
	
	private static final long serialVersionUID = -1;
	
	private BigDecimal sumAmount;//小计

	public BigDecimal getSumAmount() {
		return sumAmount;
	}
	public void setSumAmount(BigDecimal sumAmount) {
		this.sumAmount = sumAmount;
	}
	
	
}
