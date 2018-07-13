package com.jiuyescm.bms.billcheck.vo;

import java.math.BigDecimal;

import com.jiuyescm.cfm.domain.IEntity;

public class BillCheckReceiptSumVo implements IEntity{
	// TODO Change serialVersionUID by eclipse
	private static final long serialVersionUID = -1;
    
    //责任部门id
    private String deptId;
    
    //责任部门名称
    private String deptName;
    
    //开票未回款金额
    private BigDecimal invoiceUnReceiptAmount;
    
    //未开票未回款金额
    private BigDecimal unInvoiceUnReceiptAmount;
    
    //账单未确认金额
    private BigDecimal unConfirmAmount;
    
    //小计金额
    private BigDecimal totalAmount;

	public String getDeptId() {
		return deptId;
	}

	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	public BigDecimal getInvoiceUnReceiptAmount() {
		return invoiceUnReceiptAmount;
	}

	public void setInvoiceUnReceiptAmount(BigDecimal invoiceUnReceiptAmount) {
		this.invoiceUnReceiptAmount = invoiceUnReceiptAmount;
	}

	

	public BigDecimal getUnInvoiceUnReceiptAmount() {
		return unInvoiceUnReceiptAmount;
	}

	public void setUnInvoiceUnReceiptAmount(BigDecimal unInvoiceUnReceiptAmount) {
		this.unInvoiceUnReceiptAmount = unInvoiceUnReceiptAmount;
	}

	public BigDecimal getUnConfirmAmount() {
		return unConfirmAmount;
	}

	public void setUnConfirmAmount(BigDecimal unConfirmAmount) {
		this.unConfirmAmount = unConfirmAmount;
	}

	public BigDecimal getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}
    
    
}
