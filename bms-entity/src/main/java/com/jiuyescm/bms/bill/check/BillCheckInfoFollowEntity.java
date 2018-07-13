package com.jiuyescm.bms.bill.check;

import java.math.BigDecimal;
import java.sql.Timestamp;

import com.jiuyescm.cfm.domain.IEntity;

public class BillCheckInfoFollowEntity implements IEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3637668144049814763L;

	// id
	private Integer id;
	// 业务月份
	private Integer createMonth;
	// 开票名称
	private String invoiceName;
	// 账单名称
	private String billName;
	// bms商家名称
	private String customerName;
	// 业务启动时间
	private Long billStartTime;
	// 一级品类
	private String firstClassName;
	// 业务类型
	private String bizTypeName;
	// 项目
	private String projectName;
	// 销售员名称
	private String sellerName;
	// 责任部门名称
	private String deptName;
	// 责任部门编码
	private String deptCode;
	// 项目管理员
	private String projectManagerName;
	// 结算员
	private String balanceName;
	// 账单状态
	private String billStatus;
	// 对账状态
	private String billCheckStatus;
	// 账单确认日期
	private Long billConfirmDate;
	// 开票状态
	private String invoiceStatus;
	// 预计金额
	private BigDecimal expectAmount;
	// 最终确认额
	private BigDecimal confirmAmount;
	// 确认日期
	private Long confirmDate;
	// 确认人
	private String confirmMan;
	// 是否需要发票  0-否 1-是
	private String isneedInvoice;
	// 发票金额
	private BigDecimal invoiceAmount;
	// 开票日期
	private Long invoiceDate;
	// 预计回款日期
	private Long expectReceiptDate;
	// 未收款金额
	private BigDecimal unReceiptAmount;
	// 开票未回款金额
	private BigDecimal invoiceUnReceiptAmount;
	// 收款金额
	private BigDecimal receiptAmount;
	// 收款日期
	private Long receiptDate;
	// 收款日期
	private String receiptStatus;
	// creator
	private String creator;
	// CreateTime
	private Timestamp createTime;
	// LastModifier
	private String lastModifier;
	// LastModifyTime
	private Timestamp lastModifyTime;
	// 开票人
	private String invoiceMan;
	// 收款人
	private String receiptMan;
	//坏账金额
	private BigDecimal badBillAmount;
	// 作废状态 0-未作废 1-已作废
	private String delFlag;
	// 备注
	private String remark;
	// 账单下载地址
	private String billExcelUrl;
	private int followId;//follow 表主键
	private String followState;//处理状态
	private Timestamp startTime; //账单开始时间
	private Timestamp endTime;//账单结束时间
	private Integer takesTime;//跟进时效
	private String followMan;//跟进人
	private String followManId;//跟进人ID
	private String followDept;//跟进部门
	private String followDeptId;//跟进部门ID
	
	public String getFollowMan() {
		return followMan;
	}
	public void setFollowMan(String followMan) {
		this.followMan = followMan;
	}
	public String getFollowManId() {
		return followManId;
	}
	public void setFollowManId(String followManId) {
		this.followManId = followManId;
	}
	public String getFollowDept() {
		return followDept;
	}
	public void setFollowDept(String followDept) {
		this.followDept = followDept;
	}
	public String getFollowDeptId() {
		return followDeptId;
	}
	public void setFollowDeptId(String followDeptId) {
		this.followDeptId = followDeptId;
	}
	public int getFollowId() {
		return followId;
	}
	public void setFollowId(int followId) {
		this.followId = followId;
	}
	public Timestamp getStartTime() {
		return startTime;
	}
	public void setStartTime(Timestamp startTime) {
		this.startTime = startTime;
	}
	public Timestamp getEndTime() {
		return endTime;
	}
	public void setEndTime(Timestamp endTime) {
		this.endTime = endTime;
	}
	public Integer getTakesTime() {
		return takesTime;
	}
	public void setTakesTime(Integer takesTime) {
		this.takesTime = takesTime;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getCreateMonth() {
		return createMonth;
	}
	public void setCreateMonth(Integer createMonth) {
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
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public Long getBillStartTime() {
		return billStartTime;
	}
	public void setBillStartTime(Long billStartTime) {
		this.billStartTime = billStartTime;
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
	public String getDeptCode() {
		return deptCode;
	}
	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
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
	public String getBillStatus() {
		return billStatus;
	}
	public void setBillStatus(String billStatus) {
		this.billStatus = billStatus;
	}
	public String getBillCheckStatus() {
		return billCheckStatus;
	}
	public void setBillCheckStatus(String billCheckStatus) {
		this.billCheckStatus = billCheckStatus;
	}
	public Long getBillConfirmDate() {
		return billConfirmDate;
	}
	public void setBillConfirmDate(Long billConfirmDate) {
		this.billConfirmDate = billConfirmDate;
	}
	public String getInvoiceStatus() {
		return invoiceStatus;
	}
	public void setInvoiceStatus(String invoiceStatus) {
		this.invoiceStatus = invoiceStatus;
	}
	public BigDecimal getExpectAmount() {
		return expectAmount;
	}
	public void setExpectAmount(BigDecimal expectAmount) {
		this.expectAmount = expectAmount;
	}
	public BigDecimal getConfirmAmount() {
		return confirmAmount;
	}
	public void setConfirmAmount(BigDecimal confirmAmount) {
		this.confirmAmount = confirmAmount;
	}
	public Long getConfirmDate() {
		return confirmDate;
	}
	public void setConfirmDate(Long confirmDate) {
		this.confirmDate = confirmDate;
	}
	public String getConfirmMan() {
		return confirmMan;
	}
	public void setConfirmMan(String confirmMan) {
		this.confirmMan = confirmMan;
	}
	public String getIsneedInvoice() {
		return isneedInvoice;
	}
	public void setIsneedInvoice(String isneedInvoice) {
		this.isneedInvoice = isneedInvoice;
	}
	public BigDecimal getInvoiceAmount() {
		return invoiceAmount;
	}
	public void setInvoiceAmount(BigDecimal invoiceAmount) {
		this.invoiceAmount = invoiceAmount;
	}
	public Long getInvoiceDate() {
		return invoiceDate;
	}
	public void setInvoiceDate(Long invoiceDate) {
		this.invoiceDate = invoiceDate;
	}
	public Long getExpectReceiptDate() {
		return expectReceiptDate;
	}
	public void setExpectReceiptDate(Long expectReceiptDate) {
		this.expectReceiptDate = expectReceiptDate;
	}
	public BigDecimal getUnReceiptAmount() {
		return unReceiptAmount;
	}
	public void setUnReceiptAmount(BigDecimal unReceiptAmount) {
		this.unReceiptAmount = unReceiptAmount;
	}
	public BigDecimal getInvoiceUnReceiptAmount() {
		return invoiceUnReceiptAmount;
	}
	public void setInvoiceUnReceiptAmount(BigDecimal invoiceUnReceiptAmount) {
		this.invoiceUnReceiptAmount = invoiceUnReceiptAmount;
	}
	public BigDecimal getReceiptAmount() {
		return receiptAmount;
	}
	public void setReceiptAmount(BigDecimal receiptAmount) {
		this.receiptAmount = receiptAmount;
	}
	public Long getReceiptDate() {
		return receiptDate;
	}
	public void setReceiptDate(Long receiptDate) {
		this.receiptDate = receiptDate;
	}
	public String getReceiptStatus() {
		return receiptStatus;
	}
	public void setReceiptStatus(String receiptStatus) {
		this.receiptStatus = receiptStatus;
	}
	public String getCreator() {
		return creator;
	}
	public void setCreator(String creator) {
		this.creator = creator;
	}
	public Timestamp getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}
	public String getLastModifier() {
		return lastModifier;
	}
	public void setLastModifier(String lastModifier) {
		this.lastModifier = lastModifier;
	}
	public Timestamp getLastModifyTime() {
		return lastModifyTime;
	}
	public void setLastModifyTime(Timestamp lastModifyTime) {
		this.lastModifyTime = lastModifyTime;
	}
	public String getInvoiceMan() {
		return invoiceMan;
	}
	public void setInvoiceMan(String invoiceMan) {
		this.invoiceMan = invoiceMan;
	}
	public String getReceiptMan() {
		return receiptMan;
	}
	public void setReceiptMan(String receiptMan) {
		this.receiptMan = receiptMan;
	}
	public BigDecimal getBadBillAmount() {
		return badBillAmount;
	}
	public void setBadBillAmount(BigDecimal badBillAmount) {
		this.badBillAmount = badBillAmount;
	}
	public String getDelFlag() {
		return delFlag;
	}
	public void setDelFlag(String delFlag) {
		this.delFlag = delFlag;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getBillExcelUrl() {
		return billExcelUrl;
	}
	public void setBillExcelUrl(String billExcelUrl) {
		this.billExcelUrl = billExcelUrl;
	}
	public String getFollowState() {
		return followState;
	}
	public void setFollowState(String followState) {
		this.followState = followState;
	}
	
}
