package com.jiuyescm.bms.bill.receive.entity;

import java.sql.Timestamp;
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
	private String createMonth;
	// 商家合同名称
	private String invoiceName;
	// 账单名称
	private String billName;
	// 导入进度
	private Integer taskRate;
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
	// 任务ID
	private String taskId;
	// 原文件名称
	private String originFileName;
	// 原文件路径
	private String originFilePath;
	// 结果文件名称
	private String resultFileName;
	// 结果文件路径
	private String resultFilePath;

	public BillReceiveMasterEntity() {
		super();
	}
	
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public String getCreateMonth() {
		return this.createMonth;
	}

	public void setCreateMonth(String createMonth) {
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
	
	public String getTaskId() {
		return this.taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
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
    
}
