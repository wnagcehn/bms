/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.billcheck;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

import com.jiuyescm.cfm.domain.IEntity;

/**
 * 
 * @author stevenl
 * 
 */
public class BillCheckInfoEntity implements IEntity {

	private static final long serialVersionUID = -8844284544168534400L;
	// id
	private Integer id;
	// 业务月份
	private Integer createMonth;
	// 商家合同名称
	private String invoiceName;
	// 账单名称
	private String billName;
	// bms商家名称
	private String customerName;
	// 业务启动时间
	private Date billStartTime;
	// 一级品类
	private String firstClassName;
	// 业务类型
	private String bizTypeName;
	// 项目
	private String projectName;
	// 销售员id
	private String sellerId;
	// 销售员名称
	private String sellerName;
	// 责任部门名称
	private String deptName;
	// 责任部门编码
	private String deptCode;
	// 项目管理员id
	private String projectManagerId;
	// 项目管理员
	private String projectManagerName;
	// 结算员
	private String balanceId;
	// 结算员
	private String balanceName;
	// 账单状态
	private String billStatus;
	// 对账状态
	private String billCheckStatus;
	// 账单确认日期
	private Date billConfirmDate;
	// 开票状态
	private String invoiceStatus;
	// 预计金额
	private BigDecimal expectAmount;
	// 最终确认额
	private BigDecimal confirmAmount;
	// 确认日期
	private Date confirmDate;
	// 确认人ID
	private String confirmManId;
	// 确认人
	private String confirmMan;
	// 是否需要发票  0-否 1-是
	private String isneedInvoice;
	// 是否申请坏账  0-否 1-是
	private String isapplyBad;
	// 发票金额
	private BigDecimal invoiceAmount;
	// 开票日期
	private Date invoiceDate;
	// 预计回款日期
	private Date expectReceiptDate;
	// 未收款金额
	private BigDecimal unReceiptAmount;
	//已确认未开票金额
	private BigDecimal confirmUnInvoiceAmount;
	// 开票未回款金额
	private BigDecimal invoiceUnReceiptAmount;
	// 收款金额
	private BigDecimal receiptAmount;
	// 收款状态
	private String receiptStatus;
	// 收款日期
	private Date receiptDate;
	//区域
	private String area;
	// 创建人ID
	private String creatorId;
	// creator
	private String creator;
	// CreateTime
	private Timestamp createTime;
	// 修改人ID
	private String lastModifierId;
	// LastModifier
	private String lastModifier;
	// LastModifyTime
	private Timestamp lastModifyTime;
	// InvoiceManId
	private String invoiceManId;
	// 开票人
	private String invoiceMan;
	// ReceiptManId
	private String receiptManId;
	// 收款人
	private String receiptMan;
	// 坏账金额
	private BigDecimal badBillAmount;
	// 作废状态 0-未作废 1-已作废
	private String delFlag;	
	//原因描述
	private String badBillDesc;
	//坏账原因
	private String badBillReason;
	// 备注
	private String remark;
	// 账单下载地址
	private String billExcelUrl;
	//账单逾期时间
	private Date overdueDate;
	//预分配金额
	private BigDecimal preDistibutionAmount;
	//待催款金额
	private BigDecimal tbDunAmount;
	//账户余额
	private BigDecimal accountAmount;
	
	private String billNo;
	
	//业务时间
	private String bizYear;
	private String bizMonth;
	
	//开票商家编码
    private String mkId;
	
	public String getMkId() {
        return mkId;
    }

    public void setMkId(String mkId) {
        this.mkId = mkId;
    }

    public String getBizYear() {
		return bizYear;
	}

	public void setBizYear(String bizYear) {
		this.bizYear = bizYear;
	}

	public String getBizMonth() {
		return bizMonth;
	}

	public void setBizMonth(String bizMonth) {
		this.bizMonth = bizMonth;
	}
	
	//开票名称
	private String mkInvoiceName;
	public BillCheckInfoEntity() {

	}
	
	/**
     * id
     */
	public Integer getId() {
		return this.id;
	}

    /**
     * id
     *
     * @param id
     */
	public void setId(Integer id) {
		this.id = id;
	}
	
	/**
     * 业务月份
     */
	public Integer getCreateMonth() {
		return this.createMonth;
	}

    /**
     * 业务月份
     *
     * @param createMonth
     */
	public void setCreateMonth(Integer createMonth) {
		this.createMonth = createMonth;
	}
	
	/**
     * 商家合同名称
     */
	public String getInvoiceName() {
		return this.invoiceName;
	}

    /**
     * 商家合同名称
     *
     * @param invoiceName
     */
	public void setInvoiceName(String invoiceName) {
		this.invoiceName = invoiceName;
	}
	
	/**
     * 账单名称
     */
	public String getBillName() {
		return this.billName;
	}

    /**
     * 账单名称
     *
     * @param billName
     */
	public void setBillName(String billName) {
		this.billName = billName;
	}
	
	/**
     * bms商家名称
     */
	public String getCustomerName() {
		return this.customerName;
	}

    /**
     * bms商家名称
     *
     * @param customerName
     */
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	

	
	/**
     * 一级品类
     */
	public String getFirstClassName() {
		return this.firstClassName;
	}

    /**
     * 一级品类
     *
     * @param firstClassName
     */
	public void setFirstClassName(String firstClassName) {
		this.firstClassName = firstClassName;
	}
	
	/**
     * 业务类型
     */
	public String getBizTypeName() {
		return this.bizTypeName;
	}

    /**
     * 业务类型
     *
     * @param bizTypeName
     */
	public void setBizTypeName(String bizTypeName) {
		this.bizTypeName = bizTypeName;
	}
	
	/**
     * 项目
     */
	public String getProjectName() {
		return this.projectName;
	}

    /**
     * 项目
     *
     * @param projectName
     */
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	
	/**
     * 销售员名称
     */
	public String getSellerName() {
		return this.sellerName;
	}

    /**
     * 销售员名称
     *
     * @param sellerName
     */
	public void setSellerName(String sellerName) {
		this.sellerName = sellerName;
	}
	
	/**
     * 责任部门名称
     */
	public String getDeptName() {
		return this.deptName;
	}

    /**
     * 责任部门名称
     *
     * @param deptName
     */
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	
	/**
     * 责任部门编码
     */
	public String getDeptCode() {
		return this.deptCode;
	}

    /**
     * 责任部门编码
     *
     * @param deptCode
     */
	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}
	
	/**
     * 项目管理员
     */
	public String getProjectManagerName() {
		return this.projectManagerName;
	}

    /**
     * 项目管理员
     *
     * @param projectManagerName
     */
	public void setProjectManagerName(String projectManagerName) {
		this.projectManagerName = projectManagerName;
	}
	
	/**
     * 结算员
     */
	public String getBalanceName() {
		return this.balanceName;
	}

    /**
     * 结算员
     *
     * @param balanceName
     */
	public void setBalanceName(String balanceName) {
		this.balanceName = balanceName;
	}
	
	/**
     * 账单状态
     */
	public String getBillStatus() {
		return this.billStatus;
	}

    /**
     * 账单状态
     *
     * @param billStatus
     */
	public void setBillStatus(String billStatus) {
		this.billStatus = billStatus;
	}
	
	/**
     * 对账状态
     */
	public String getBillCheckStatus() {
		return this.billCheckStatus;
	}

    /**
     * 对账状态
     *
     * @param billCheckStatus
     */
	public void setBillCheckStatus(String billCheckStatus) {
		this.billCheckStatus = billCheckStatus;
	}
	

	
	/**
     * 开票状态
     */
	public String getInvoiceStatus() {
		return this.invoiceStatus;
	}

    /**
     * 开票状态
     *
     * @param invoiceStatus
     */
	public void setInvoiceStatus(String invoiceStatus) {
		this.invoiceStatus = invoiceStatus;
	}
	
	/**
     * 预计金额
     */
	public BigDecimal getExpectAmount() {
		return this.expectAmount;
	}

    /**
     * 预计金额
     *
     * @param expectAmount
     */
	public void setExpectAmount(BigDecimal expectAmount) {
		this.expectAmount = expectAmount;
	}
	
	/**
     * 最终确认额
     */
	public BigDecimal getConfirmAmount() {
		return this.confirmAmount;
	}

    /**
     * 最终确认额
     *
     * @param confirmAmount
     */
	public void setConfirmAmount(BigDecimal confirmAmount) {
		this.confirmAmount = confirmAmount;
	}
	

	
	/**
     * 确认人ID
     */
	public String getConfirmManId() {
		return this.confirmManId;
	}

    /**
     * 确认人ID
     *
     * @param confirmManId
     */
	public void setConfirmManId(String confirmManId) {
		this.confirmManId = confirmManId;
	}
	
	/**
     * 确认人
     */
	public String getConfirmMan() {
		return this.confirmMan;
	}

    /**
     * 确认人
     *
     * @param confirmMan
     */
	public void setConfirmMan(String confirmMan) {
		this.confirmMan = confirmMan;
	}
	
	/**
     * 是否需要发票  0-否 1-是
     */
	public String getIsneedInvoice() {
		return this.isneedInvoice;
	}

    /**
     * 是否需要发票  0-否 1-是
     *
     * @param isneedInvoice
     */
	public void setIsneedInvoice(String isneedInvoice) {
		this.isneedInvoice = isneedInvoice;
	}
	
	/**
     * 发票金额
     */
	public BigDecimal getInvoiceAmount() {
		return this.invoiceAmount;
	}

    /**
     * 发票金额
     *
     * @param invoiceAmount
     */
	public void setInvoiceAmount(BigDecimal invoiceAmount) {
		this.invoiceAmount = invoiceAmount;
	}
	
	
	/**
     * 未收款金额
     */
	public BigDecimal getUnReceiptAmount() {
		return this.unReceiptAmount;
	}

    /**
     * 未收款金额
     *
     * @param unReceiptAmount
     */
	public void setUnReceiptAmount(BigDecimal unReceiptAmount) {
		this.unReceiptAmount = unReceiptAmount;
	}
	
	/**
     * 开票未回款金额
     */
	public BigDecimal getInvoiceUnReceiptAmount() {
		return this.invoiceUnReceiptAmount;
	}

    /**
     * 开票未回款金额
     *
     * @param invoiceUnReceiptAmount
     */
	public void setInvoiceUnReceiptAmount(BigDecimal invoiceUnReceiptAmount) {
		this.invoiceUnReceiptAmount = invoiceUnReceiptAmount;
	}
	
	/**
     * 收款金额
     */
	public BigDecimal getReceiptAmount() {
		return this.receiptAmount;
	}

    /**
     * 收款金额
     *
     * @param receiptAmount
     */
	public void setReceiptAmount(BigDecimal receiptAmount) {
		this.receiptAmount = receiptAmount;
	}
	
	/**
     * 创建人ID
     */
	public String getCreatorId() {
		return this.creatorId;
	}

    /**
     * 创建人ID
     *
     * @param creatorId
     */
	public void setCreatorId(String creatorId) {
		this.creatorId = creatorId;
	}
	
	/**
     * creator
     */
	public String getCreator() {
		return this.creator;
	}

    /**
     * creator
     *
     * @param creator
     */
	public void setCreator(String creator) {
		this.creator = creator;
	}
	
	/**
     * CreateTime
     */
	public Timestamp getCreateTime() {
		return this.createTime;
	}

    /**
     * CreateTime
     *
     * @param createTime
     */
	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}
	
	/**
     * 修改人ID
     */
	public String getLastModifierId() {
		return this.lastModifierId;
	}

    /**
     * 修改人ID
     *
     * @param lastModifierId
     */
	public void setLastModifierId(String lastModifierId) {
		this.lastModifierId = lastModifierId;
	}
	
	/**
     * LastModifier
     */
	public String getLastModifier() {
		return this.lastModifier;
	}

    /**
     * LastModifier
     *
     * @param lastModifier
     */
	public void setLastModifier(String lastModifier) {
		this.lastModifier = lastModifier;
	}
	
	/**
     * LastModifyTime
     */
	public Timestamp getLastModifyTime() {
		return this.lastModifyTime;
	}

    /**
     * LastModifyTime
     *
     * @param lastModifyTime
     */
	public void setLastModifyTime(Timestamp lastModifyTime) {
		this.lastModifyTime = lastModifyTime;
	}
	
	/**
     * InvoiceManId
     */
	public String getInvoiceManId() {
		return this.invoiceManId;
	}

    /**
     * InvoiceManId
     *
     * @param invoiceManId
     */
	public void setInvoiceManId(String invoiceManId) {
		this.invoiceManId = invoiceManId;
	}
	
	/**
     * 开票人
     */
	public String getInvoiceMan() {
		return this.invoiceMan;
	}

    /**
     * 开票人
     *
     * @param invoiceMan
     */
	public void setInvoiceMan(String invoiceMan) {
		this.invoiceMan = invoiceMan;
	}
	
	/**
     * ReceiptManId
     */
	public String getReceiptManId() {
		return this.receiptManId;
	}

    /**
     * ReceiptManId
     *
     * @param receiptManId
     */
	public void setReceiptManId(String receiptManId) {
		this.receiptManId = receiptManId;
	}
	
	/**
     * 收款人
     */
	public String getReceiptMan() {
		return this.receiptMan;
	}

    /**
     * 收款人
     *
     * @param receiptMan
     */
	public void setReceiptMan(String receiptMan) {
		this.receiptMan = receiptMan;
	}
	
	/**
     * 坏账金额
     */
	public BigDecimal getBadBillAmount() {
		return this.badBillAmount;
	}

    /**
     * 坏账金额
     *
     * @param badBillAmount
     */
	public void setBadBillAmount(BigDecimal badBillAmount) {
		this.badBillAmount = badBillAmount;
	}
	
	/**
     * 作废状态 0-未作废 1-已作废
     */
	public String getDelFlag() {
		return this.delFlag;
	}

    /**
     * 作废状态 0-未作废 1-已作废
     *
     * @param delFlag
     */
	public void setDelFlag(String delFlag) {
		this.delFlag = delFlag;
	}
	
	/**
     * 备注
     */
	public String getRemark() {
		return this.remark;
	}

    /**
     * 备注
     *
     * @param remark
     */
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	/**
     * 账单下载地址
     */
	public String getBillExcelUrl() {
		return this.billExcelUrl;
	}

    /**
     * 账单下载地址
     *
     * @param billExcelUrl
     */
	public void setBillExcelUrl(String billExcelUrl) {
		this.billExcelUrl = billExcelUrl;
	}

	public Date getBillStartTime() {
		return billStartTime;
	}

	public void setBillStartTime(Date billStartTime) {
		this.billStartTime = billStartTime;
	}

	public Date getBillConfirmDate() {
		return billConfirmDate;
	}

	public void setBillConfirmDate(Date billConfirmDate) {
		this.billConfirmDate = billConfirmDate;
	}

	public Date getConfirmDate() {
		return confirmDate;
	}

	public void setConfirmDate(Date confirmDate) {
		this.confirmDate = confirmDate;
	}

	public Date getInvoiceDate() {
		return invoiceDate;
	}

	public void setInvoiceDate(Date invoiceDate) {
		this.invoiceDate = invoiceDate;
	}

	public Date getExpectReceiptDate() {
		return expectReceiptDate;
	}

	public void setExpectReceiptDate(Date expectReceiptDate) {
		this.expectReceiptDate = expectReceiptDate;
	}

	public Date getReceiptDate() {
		return receiptDate;
	}

	public void setReceiptDate(Date receiptDate) {
		this.receiptDate = receiptDate;
	}

	public String getSellerId() {
		return sellerId;
	}

	public void setSellerId(String sellerId) {
		this.sellerId = sellerId;
	}

	public String getProjectManagerId() {
		return projectManagerId;
	}

	public void setProjectManagerId(String projectManagerId) {
		this.projectManagerId = projectManagerId;
	}

	public String getBalanceId() {
		return balanceId;
	}

	public void setBalanceId(String balanceId) {
		this.balanceId = balanceId;
	}

	public String getBadBillDesc() {
		return badBillDesc;
	}

	public void setBadBillDesc(String badBillDesc) {
		this.badBillDesc = badBillDesc;
	}

	public String getBadBillReason() {
		return badBillReason;
	}

	public void setBadBillReason(String badBillReason) {
		this.badBillReason = badBillReason;
	}

	public String getReceiptStatus() {
		return receiptStatus;
	}

	public void setReceiptStatus(String receiptStatus) {
		this.receiptStatus = receiptStatus;
	}

	public BigDecimal getConfirmUnInvoiceAmount() {
		return confirmUnInvoiceAmount;
	}

	public void setConfirmUnInvoiceAmount(BigDecimal confirmUnInvoiceAmount) {
		this.confirmUnInvoiceAmount = confirmUnInvoiceAmount;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getIsapplyBad() {
		return isapplyBad;
	}

	public void setIsapplyBad(String isapplyBad) {
		this.isapplyBad = isapplyBad;
	}

	public Date getOverdueDate() {
		return overdueDate;
	}

	public void setOverdueDate(Date overdueDate) {
		this.overdueDate = overdueDate;
	}

	public BigDecimal getPreDistibutionAmount() {
		return preDistibutionAmount;
	}

	public void setPreDistibutionAmount(BigDecimal preDistibutionAmount) {
		this.preDistibutionAmount = preDistibutionAmount;
	}

	public BigDecimal getTbDunAmount() {
		return tbDunAmount;
	}

	public void setTbDunAmount(BigDecimal tbDunAmount) {
		this.tbDunAmount = tbDunAmount;
	}

	public BigDecimal getAccountAmount() {
		return accountAmount;
	}

	public void setAccountAmount(BigDecimal accountAmount) {
		this.accountAmount = accountAmount;
	}

	public String getBillNo() {
		return billNo;
	}

	public void setBillNo(String billNo) {
		this.billNo = billNo;
	}

	public String getMkInvoiceName() {
		return mkInvoiceName;
	}

	public void setMkInvoiceName(String mkInvoiceName) {
		this.mkInvoiceName = mkInvoiceName;
	}

	
}
