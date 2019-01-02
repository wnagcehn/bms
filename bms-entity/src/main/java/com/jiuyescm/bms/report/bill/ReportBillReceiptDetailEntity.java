package com.jiuyescm.bms.report.bill;

import java.math.BigDecimal;
import java.security.Timestamp;

import com.jiuyescm.cfm.domain.IEntity;

public class ReportBillReceiptDetailEntity implements IEntity{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2124517993123257125L;
	//业务月份
	private String createMonth;
	//开票名称
	private String invoiceName;
	//账单名称
	private String billName;
	//账单
	private String bill;
	//业务启动时间
	private Timestamp billStartTime;
	//年度
	private Integer billStartYear;
	//月度	
	private Integer billStartMonth;
	//一级品类
	private String firstClassName;
	//业务类型
	private String bizTypeName;
	//项目
	private String projectName;
	//销售员
	private String sellerName;
	//部门
	private String deptName;
	//应收组
	private String receiptGroup;
	//项目管理员
	private String projectManagerName;
	//结算员
	private String balanceName;
	//快递单号
	private String waybillNo;
	//差异率超5%说明
	private String instruction;
	//宅配单量
	private String dispatchCount;
	//单价
	private String unitPrice;
	//差异率
	private BigDecimal differentRate;
	//准确率
	private BigDecimal correctRate;
	//差异
	private BigDecimal different;
	//实际确认额
	private BigDecimal confirmAmount;
	//实际仓储
	private BigDecimal actualStorage;
	//实际干线
	private BigDecimal actualTransport;
	//实际宅配
	private BigDecimal actualDispatch;
	//实际航空
	private BigDecimal actualAir;
	//索赔
	private BigDecimal claim;
	//预估合计
	private BigDecimal preTotal;
	//预估仓储
	private BigDecimal preStorage;
	//预估干线
	private BigDecimal preTransport;
	//预估宅配
	private BigDecimal preDispatch;
	//预估航空
	private BigDecimal preAir;
	//备注
	private String remark;
	//账单状态
	private String billStatus;
	//账单确认日期
	private String billConfirmDate;
	//账单确认月度
	private String billConfirmMonth;
	//开票状态
	private String invoiceStatus;
	//未开票金额
	private BigDecimal unInvoiceAmount;
	//未收款金额
	private BigDecimal unReceiptAmount;
	//发票金额
	private BigDecimal invoiceAmount;
	//发票号
	private String invoiceNo;
	//开票日期
	private Timestamp invoiceDate;
	//收款金额
	private BigDecimal receiptAmount;
	//收款日期
	private Timestamp receiptDate;
	//理赔及减免款、尾差
	private BigDecimal otherAmount;
	//开票未回款额
	private BigDecimal invoiceUnReceiptAmount;
	public String getCreateMonth() {
		return createMonth;
	}
	public void setCreateMonth(String createMonth) {
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
	public String getBill() {
		return bill;
	}
	public void setBill(String bill) {
		this.bill = bill;
	}
	public Timestamp getBillStartTime() {
		return billStartTime;
	}
	public void setBillStartTime(Timestamp billStartTime) {
		this.billStartTime = billStartTime;
	}
	public Integer getBillStartYear() {
		return billStartYear;
	}
	public void setBillStartYear(Integer billStartYear) {
		this.billStartYear = billStartYear;
	}
	public Integer getBillStartMonth() {
		return billStartMonth;
	}
	public void setBillStartMonth(Integer billStartMonth) {
		this.billStartMonth = billStartMonth;
	}
	public String getFirstClassName() {
		return firstClassName;
	}
	public void setFirstClassName(String firstClassName) {
		this.firstClassName = firstClassName;
	}
	public String getBizTypeName() {
		return bizTypeName;
	}
	public void setBizTypeName(String bizTypeName) {
		this.bizTypeName = bizTypeName;
	}
	public String getProjectName() {
		return projectName;
	}
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	public String getSellerName() {
		return sellerName;
	}
	public void setSellerName(String sellerName) {
		this.sellerName = sellerName;
	}
	public String getDeptName() {
		return deptName;
	}
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	public String getReceiptGroup() {
		return receiptGroup;
	}
	public void setReceiptGroup(String receiptGroup) {
		this.receiptGroup = receiptGroup;
	}
	public String getProjectManagerName() {
		return projectManagerName;
	}
	public void setProjectManagerName(String projectManagerName) {
		this.projectManagerName = projectManagerName;
	}
	public String getBalanceName() {
		return balanceName;
	}
	public void setBalanceName(String balanceName) {
		this.balanceName = balanceName;
	}
	public String getWaybillNo() {
		return waybillNo;
	}
	public void setWaybillNo(String waybillNo) {
		this.waybillNo = waybillNo;
	}
	public String getInstruction() {
		return instruction;
	}
	public void setInstruction(String instruction) {
		this.instruction = instruction;
	}
	public String getDispatchCount() {
		return dispatchCount;
	}
	public void setDispatchCount(String dispatchCount) {
		this.dispatchCount = dispatchCount;
	}
	public String getUnitPrice() {
		return unitPrice;
	}
	public void setUnitPrice(String unitPrice) {
		this.unitPrice = unitPrice;
	}
	public BigDecimal getDifferentRate() {
		return differentRate;
	}
	public void setDifferentRate(BigDecimal differentRate) {
		this.differentRate = differentRate;
	}
	public BigDecimal getCorrectRate() {
		return correctRate;
	}
	public void setCorrectRate(BigDecimal correctRate) {
		this.correctRate = correctRate;
	}
	public BigDecimal getDifferent() {
		return different;
	}
	public void setDifferent(BigDecimal different) {
		this.different = different;
	}
	public BigDecimal getConfirmAmount() {
		return confirmAmount;
	}
	public void setConfirmAmount(BigDecimal confirmAmount) {
		this.confirmAmount = confirmAmount;
	}
	public BigDecimal getActualStorage() {
		return actualStorage;
	}
	public void setActualStorage(BigDecimal actualStorage) {
		this.actualStorage = actualStorage;
	}
	public BigDecimal getActualTransport() {
		return actualTransport;
	}
	public void setActualTransport(BigDecimal actualTransport) {
		this.actualTransport = actualTransport;
	}
	public BigDecimal getActualDispatch() {
		return actualDispatch;
	}
	public void setActualDispatch(BigDecimal actualDispatch) {
		this.actualDispatch = actualDispatch;
	}
	public BigDecimal getActualAir() {
		return actualAir;
	}
	public void setActualAir(BigDecimal actualAir) {
		this.actualAir = actualAir;
	}
	public BigDecimal getClaim() {
		return claim;
	}
	public void setClaim(BigDecimal claim) {
		this.claim = claim;
	}
	public BigDecimal getPreTotal() {
		return preTotal;
	}
	public void setPreTotal(BigDecimal preTotal) {
		this.preTotal = preTotal;
	}
	public BigDecimal getPreStorage() {
		return preStorage;
	}
	public void setPreStorage(BigDecimal preStorage) {
		this.preStorage = preStorage;
	}
	public BigDecimal getPreTransport() {
		return preTransport;
	}
	public void setPreTransport(BigDecimal preTransport) {
		this.preTransport = preTransport;
	}
	public BigDecimal getPreDispatch() {
		return preDispatch;
	}
	public void setPreDispatch(BigDecimal preDispatch) {
		this.preDispatch = preDispatch;
	}
	public BigDecimal getPreAir() {
		return preAir;
	}
	public void setPreAir(BigDecimal preAir) {
		this.preAir = preAir;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getBillStatus() {
		return billStatus;
	}
	public void setBillStatus(String billStatus) {
		this.billStatus = billStatus;
	}
	public String getBillConfirmDate() {
		return billConfirmDate;
	}
	public void setBillConfirmDate(String billConfirmDate) {
		this.billConfirmDate = billConfirmDate;
	}
	public String getBillConfirmMonth() {
		return billConfirmMonth;
	}
	public void setBillConfirmMonth(String billConfirmMonth) {
		this.billConfirmMonth = billConfirmMonth;
	}
	public String getInvoiceStatus() {
		return invoiceStatus;
	}
	public void setInvoiceStatus(String invoiceStatus) {
		this.invoiceStatus = invoiceStatus;
	}
	public BigDecimal getUnInvoiceAmount() {
		return unInvoiceAmount;
	}
	public void setUnInvoiceAmount(BigDecimal unInvoiceAmount) {
		this.unInvoiceAmount = unInvoiceAmount;
	}
	public BigDecimal getUnReceiptAmount() {
		return unReceiptAmount;
	}
	public void setUnReceiptAmount(BigDecimal unReceiptAmount) {
		this.unReceiptAmount = unReceiptAmount;
	}
	public BigDecimal getInvoiceAmount() {
		return invoiceAmount;
	}
	public void setInvoiceAmount(BigDecimal invoiceAmount) {
		this.invoiceAmount = invoiceAmount;
	}
	public String getInvoiceNo() {
		return invoiceNo;
	}
	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}
	public Timestamp getInvoiceDate() {
		return invoiceDate;
	}
	public void setInvoiceDate(Timestamp invoiceDate) {
		this.invoiceDate = invoiceDate;
	}
	public BigDecimal getReceiptAmount() {
		return receiptAmount;
	}
	public void setReceiptAmount(BigDecimal receiptAmount) {
		this.receiptAmount = receiptAmount;
	}
	public Timestamp getReceiptDate() {
		return receiptDate;
	}
	public void setReceiptDate(Timestamp receiptDate) {
		this.receiptDate = receiptDate;
	}
	public BigDecimal getOtherAmount() {
		return otherAmount;
	}
	public void setOtherAmount(BigDecimal otherAmount) {
		this.otherAmount = otherAmount;
	}
	public BigDecimal getInvoiceUnReceiptAmount() {
		return invoiceUnReceiptAmount;
	}
	public void setInvoiceUnReceiptAmount(BigDecimal invoiceUnReceiptAmount) {
		this.invoiceUnReceiptAmount = invoiceUnReceiptAmount;
	}
	
	
}
