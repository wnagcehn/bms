package com.jiuyescm.bms.biz.discount.entity;

import java.sql.Timestamp;
import com.jiuyescm.cfm.domain.IEntity;

 /**
 * ..Entity
 * @author wangchen
 * 
 */
public class BmsDiscountAsynTaskEntity implements IEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = -562726300675971525L;
	// 自增主键
	private Long id;
	// 任务ID
	private String taskId;
	// 业务年月 2018-05
	private String createMonth;
	// 商家ID
	private String customerId;
	// 业务类型（STORAGE-仓，TRANSPORT-干，DISPATCH-配）
	private String bizTypecode;
	// 费用科目编码
	private String subjectCode;
	private String subjectName;
	// 任务名称
	private String taskName;
	// 任务进度
	private Integer taskRate;
	// 任务状态 WAIT-等待 PROCESS-处理 SUCCESS-成功 EXCEPTION-处理异常 CANCEL-取消'
	private String taskStatus;
	// 操作人
	private String creator;
	// 任务创建时间
	private Timestamp createTime;
	// 任务完成时间
	private Timestamp finishTime;
	// 0-启用 1-作废
	private String delFlag;
	// 开始时间
	private Timestamp startDate;
	// 结束时间
	private Timestamp endDate;
	// 备注
	private String remark;
	//年、月
	private String year;
	private String month;

	public BmsDiscountAsynTaskEntity() {
		super();
	}
	
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public String getTaskId() {
		return this.taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	
	public String getCreateMonth() {
		return this.createMonth;
	}

	public void setCreateMonth(String createMonth) {
		this.createMonth = createMonth;
	}
	
	public String getCustomerId() {
		return this.customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	
	public String getBizTypecode() {
		return this.bizTypecode;
	}

	public void setBizTypecode(String bizTypecode) {
		this.bizTypecode = bizTypecode;
	}
	
	public String getSubjectCode() {
		return this.subjectCode;
	}

	public void setSubjectCode(String subjectCode) {
		this.subjectCode = subjectCode;
	}
	
	public String getTaskName() {
		return this.taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
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
	
	public String getCreator() {
		return this.creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
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
	
	public String getDelFlag() {
		return this.delFlag;
	}

	public void setDelFlag(String delFlag) {
		this.delFlag = delFlag;
	}
	
	public Timestamp getStartDate() {
		return this.startDate;
	}

	public void setStartDate(Timestamp startDate) {
		this.startDate = startDate;
	}
	
	public Timestamp getEndDate() {
		return this.endDate;
	}

	public void setEndDate(Timestamp endDate) {
		this.endDate = endDate;
	}
	
	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public String getSubjectName() {
		return subjectName;
	}

	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}
    
}
