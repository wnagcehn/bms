package com.jiuyescm.bms.billcheck.vo;

import java.math.BigDecimal;
import java.util.Date;

import com.jiuyescm.cfm.domain.IEntity;

public class BillExpectReceiptVo implements IEntity{
	// TODO Change serialVersionUID by eclipse
    private static final long serialVersionUID = -1;
    
    //业务月份
	private int createMonth;
	// 开票名称
	private String invoiceName;
	// 账单名称
	private String billName;
	// 账单状态
	private String billStatus;
	// 开票状态
	private String invoiceStatus;
	//收款状态
	private String receiptStatus;
	// 销售员id
	private String sellerId;
	// 销售员名称
	private String sellerName;
	// 最终确认额
	private BigDecimal confirmAmount;
	//截止日前最后一次收款日期
	private Date nowLastReceiptDate;
	//截止日前已回款金额
	private BigDecimal nowReceiptMoney;
	//截止日前最后一次预计回款日期
	private Date nowLastExpectReceiptDate;
	//截止日前剩余待回款金额
	private BigDecimal nowTbReceiptMoney;
	//区域
	private String area;
	//调整金额
	private BigDecimal adjustMoney;
	
	//确认金额汇总
	private BigDecimal totalConfirmAmount;
	//截止日前已回款金额汇总
	private BigDecimal totalNowReceiptMoney;
	//截止日前剩余待回款金额汇总
	private BigDecimal totalNowTbReceiptMoney;
	//调整金额汇总
	private BigDecimal totalAdjustMoney;
	
	
	public int getCreateMonth() {
		return createMonth;
	}
	public void setCreateMonth(int createMonth) {
		this.createMonth = createMonth;
	}
	public String getInvoiceName() {
		return invoiceName;
	}
	public void setInvoiceName(String invoiceName) {
		this.invoiceName = invoiceName;
	}
	public String getBillName() {
		return billName;
	}
	public void setBillName(String billName) {
		this.billName = billName;
	}
	public String getBillStatus() {
		return billStatus;
	}
	public void setBillStatus(String billStatus) {
		this.billStatus = billStatus;
	}
	public String getInvoiceStatus() {
		return invoiceStatus;
	}
	public void setInvoiceStatus(String invoiceStatus) {
		this.invoiceStatus = invoiceStatus;
	}
	public String getReceiptStatus() {
		return receiptStatus;
	}
	public void setReceiptStatus(String receiptStatus) {
		this.receiptStatus = receiptStatus;
	}
	public String getSellerId() {
		return sellerId;
	}
	public void setSellerId(String sellerId) {
		this.sellerId = sellerId;
	}
	public String getSellerName() {
		return sellerName;
	}
	public void setSellerName(String sellerName) {
		this.sellerName = sellerName;
	}
	public BigDecimal getConfirmAmount() {
		return confirmAmount;
	}
	public void setConfirmAmount(BigDecimal confirmAmount) {
		this.confirmAmount = confirmAmount;
	}
	public Date getNowLastReceiptDate() {
		return nowLastReceiptDate;
	}
	public void setNowLastReceiptDate(Date nowLastReceiptDate) {
		this.nowLastReceiptDate = nowLastReceiptDate;
	}
	public BigDecimal getNowReceiptMoney() {
		return nowReceiptMoney;
	}
	public void setNowReceiptMoney(BigDecimal nowReceiptMoney) {
		this.nowReceiptMoney = nowReceiptMoney;
	}
	public Date getNowLastExpectReceiptDate() {
		return nowLastExpectReceiptDate;
	}
	public void setNowLastExpectReceiptDate(Date nowLastExpectReceiptDate) {
		this.nowLastExpectReceiptDate = nowLastExpectReceiptDate;
	}
	public BigDecimal getNowTbReceiptMoney() {
		return nowTbReceiptMoney;
	}
	public void setNowTbReceiptMoney(BigDecimal nowTbReceiptMoney) {
		this.nowTbReceiptMoney = nowTbReceiptMoney;
	}
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}
	public BigDecimal getAdjustMoney() {
		return adjustMoney;
	}
	public void setAdjustMoney(BigDecimal adjustMoney) {
		this.adjustMoney = adjustMoney;
	}
	public BigDecimal getTotalConfirmAmount() {
		return totalConfirmAmount;
	}
	public void setTotalConfirmAmount(BigDecimal totalConfirmAmount) {
		this.totalConfirmAmount = totalConfirmAmount;
	}
	public BigDecimal getTotalNowReceiptMoney() {
		return totalNowReceiptMoney;
	}
	public void setTotalNowReceiptMoney(BigDecimal totalNowReceiptMoney) {
		this.totalNowReceiptMoney = totalNowReceiptMoney;
	}
	public BigDecimal getTotalNowTbReceiptMoney() {
		return totalNowTbReceiptMoney;
	}
	public void setTotalNowTbReceiptMoney(BigDecimal totalNowTbReceiptMoney) {
		this.totalNowTbReceiptMoney = totalNowTbReceiptMoney;
	}
	public BigDecimal getTotalAdjustMoney() {
		return totalAdjustMoney;
	}
	public void setTotalAdjustMoney(BigDecimal totalAdjustMoney) {
		this.totalAdjustMoney = totalAdjustMoney;
	}
	
	
}
