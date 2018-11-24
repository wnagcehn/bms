package com.jiuyescm.bms.bill.receive.entity;

import java.sql.Timestamp;
import java.util.Date;
import java.math.BigDecimal;
import com.jiuyescm.cfm.domain.IEntity;

 /**
 * ..Entity
 * @author wangchen
 * 
 */
public class BillReceiveMasterEntity implements IEntity {

    
	/**
	 * 
	 */
	private static final long serialVersionUID = -8412660829164489928L;
	// id
	private Long id;
	// 业务月份 1801
	private Integer createMonth;
	// 合同商家名称
	private String invoiceName;
	// 合同商家编码
	private String invoiceId;
	// 账单名称
	private String billName;
	// 导入进度
	private Integer taskRate;
	private String rate;
	// 任务状态
	private String taskStatus;
	// 导入金额
	private Double amount;
	// 调整金额
	private Double adjustAmount;
	// 创建人
	private String creator;
	// 创建人ID
	private String creatorId;
	// 任务创建时间
	private Timestamp createTime;
	// 任务处理完成时间
	private Timestamp finishTime;
	// 修改人姓名
	private String lastModifier;
	// 修改人ID
	private String lastModifierId;
	// 最后修改时间
	private Timestamp lastModifyTime;
	// 作废标识
	private String delFlag;
	// 账单编号
	private String billNo;
	// 原文件名称
	private String originFileName;
	// 原文件路径
	private String originFilePath;
	// 结果文件名称
	private String resultFileName;
	// 结果文件路径
	private String resultFilePath;
	//总金额
	private Double totalAmount;
	
	private Double totalImportCost;
	
	private Double totalAdjustACost;
	
	private Double totalCost;
	
	private String billCheckStatus;
	
	private String isneedInvoice;
	//业务启动时间
	private Date billStartTime;
	//一级品类
	private String firstClassName;
	//业务类型
	private String bizTypeName;
	//项目
	private String projectName;
	//销售员id
	private String sellerId;
	//销售员名称
	private String sellerName;
	//部门名称
	private String deptName;
	//部门ID
	private String deptCode;
	//项目管理员id
	private String projectManagerId;
	//项目管理员
	private String projectManagerName;
	//结算员id
	private String balanceId;
	//结算员
	private String balanceName;
	//确认人ID
	private String confirmManId;
	//确认人
	private String confirmMan;
	//确认日期
	private Date confirmDate;
	
	public BillReceiveMasterEntity() {
		super();
	}
	
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public Integer getCreateMonth() {
		return createMonth;
	}

	public void setCreateMonth(Integer createMonth) {
		this.createMonth = createMonth;
	}

	public String getInvoiceName() {
		return this.invoiceName;
	}

	public void setInvoiceName(String invoiceName) {
		this.invoiceName = invoiceName;
	}
	
	public String getBillName() {
		return this.billName;
	}

	public void setBillName(String billName) {
		this.billName = billName;
	}
	
	public Integer getTaskRate() {
		return this.taskRate;
	}

	public void setTaskRate(Integer taskRate) {
		this.taskRate = taskRate;
	}
	
	public String getTaskStatus() {
		return this.taskStatus;
	}

	public void setTaskStatus(String taskStatus) {
		this.taskStatus = taskStatus;
	}
	

	
	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public Double getAdjustAmount() {
		return adjustAmount;
	}

	public void setAdjustAmount(Double adjustAmount) {
		this.adjustAmount = adjustAmount;
	}

	public String getCreator() {
		return this.creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}
	
	public String getCreatorId() {
		return this.creatorId;
	}

	public void setCreatorId(String creatorId) {
		this.creatorId = creatorId;
	}
	
	public Timestamp getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}
	
	public Timestamp getFinishTime() {
		return this.finishTime;
	}

	public void setFinishTime(Timestamp finishTime) {
		this.finishTime = finishTime;
	}
	
	public String getLastModifier() {
		return this.lastModifier;
	}

	public void setLastModifier(String lastModifier) {
		this.lastModifier = lastModifier;
	}
	
	public String getLastModifierId() {
		return this.lastModifierId;
	}

	public void setLastModifierId(String lastModifierId) {
		this.lastModifierId = lastModifierId;
	}
	
	public Timestamp getLastModifyTime() {
		return this.lastModifyTime;
	}

	public void setLastModifyTime(Timestamp lastModifyTime) {
		this.lastModifyTime = lastModifyTime;
	}
	
	public String getDelFlag() {
		return this.delFlag;
	}

	public void setDelFlag(String delFlag) {
		this.delFlag = delFlag;
	}
	
	
	public String getBillNo() {
		return billNo;
	}

	public void setBillNo(String billNo) {
		this.billNo = billNo;
	}

	public String getOriginFileName() {
		return this.originFileName;
	}

	public void setOriginFileName(String originFileName) {
		this.originFileName = originFileName;
	}
	
	public String getOriginFilePath() {
		return this.originFilePath;
	}

	public void setOriginFilePath(String originFilePath) {
		this.originFilePath = originFilePath;
	}
	
	public String getResultFileName() {
		return this.resultFileName;
	}

	public void setResultFileName(String resultFileName) {
		this.resultFileName = resultFileName;
	}
	
	public String getResultFilePath() {
		return this.resultFilePath;
	}

	public void setResultFilePath(String resultFilePath) {
		this.resultFilePath = resultFilePath;
	}

	public Double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(Double totalAmount) {
		this.totalAmount = totalAmount;
	}

	public Double getTotalImportCost() {
		return totalImportCost;
	}

	public void setTotalImportCost(Double totalImportCost) {
		this.totalImportCost = totalImportCost;
	}


	public Double getTotalAdjustACost() {
		return totalAdjustACost;
	}

	public void setTotalAdjustACost(Double totalAdjustACost) {
		this.totalAdjustACost = totalAdjustACost;
	}

	public Double getTotalCost() {
		return totalCost;
	}

	public void setTotalCost(Double totalCost) {
		this.totalCost = totalCost;
	}

	public String getRate() {
		return rate;
	}

	public void setRate(String rate) {
		this.rate = rate;
	}

	public String getBillCheckStatus() {
		return billCheckStatus;
	}

	public void setBillCheckStatus(String billCheckStatus) {
		this.billCheckStatus = billCheckStatus;
	}

	public String getIsneedInvoice() {
		return isneedInvoice;
	}

	public void setIsneedInvoice(String isneedInvoice) {
		this.isneedInvoice = isneedInvoice;
	}

	public String getInvoiceId() {
		return invoiceId;
	}

	public void setInvoiceId(String invoiceId) {
		this.invoiceId = invoiceId;
	}

	public Date getBillStartTime() {
		return billStartTime;
	}

	public void setBillStartTime(Date billStartTime) {
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

	public String getProjectManagerId() {
		return projectManagerId;
	}

	public void setProjectManagerId(String projectManagerId) {
		this.projectManagerId = projectManagerId;
	}

	public String getProjectManagerName() {
		return projectManagerName;
	}

	public void setProjectManagerName(String projectManagerName) {
		this.projectManagerName = projectManagerName;
	}

	public String getBalanceId() {
		return balanceId;
	}

	public void setBalanceId(String balanceId) {
		this.balanceId = balanceId;
	}

	public String getBalanceName() {
		return balanceName;
	}

	public void setBalanceName(String balanceName) {
		this.balanceName = balanceName;
	}

	public String getConfirmManId() {
		return confirmManId;
	}

	public void setConfirmManId(String confirmManId) {
		this.confirmManId = confirmManId;
	}

	public String getConfirmMan() {
		return confirmMan;
	}

	public void setConfirmMan(String confirmMan) {
		this.confirmMan = confirmMan;
	}

	public Date getConfirmDate() {
		return confirmDate;
	}

	public void setConfirmDate(Date confirmDate) {
		this.confirmDate = confirmDate;
	}
    
}
