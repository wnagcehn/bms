package com.jiuyescm.bms.asyn.vo;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;


public class BmsCorrectAsynTaskVo implements Serializable {
    
	/**
	 * 
	 */
	private static final long serialVersionUID = -8627511253116797939L;
	// 自增主键
	private Integer id;
	// 任务ID
	private String taskId;
	// 业务年月 2018-05
	private String createMonth;
	// 商家ID
	private String customerId;
	// 任务名称
	private String taskName;
	// 任务进度
	private Integer taskRate;
	// 任务状态 WAIT-等待 PROCESS-处理 SUCCESS-成功 EXCEPTION-导入异常 CANCEL-取消'
	private String taskStatus;
	// 操作人
	private String creator;
	// 任务创建时间
	private Timestamp createTime;
	// 任务完成时间
	private Timestamp finishTime;
	// 0-启用 1-作废
	private String delFlag;
	//开始时间
	private Date startDate;
	//结束时间
	private Date endDate;
	//备注
	private String remark;
	private String taskProcess;//任务进度
	private String year;
	private String month;
	public BmsCorrectAsynTaskVo(){
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getCreateMonth() {
		return createMonth;
	}

	public void setCreateMonth(String createMonth) {
		this.createMonth = createMonth;
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerid) {
		this.customerId = customerid;
	}

	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	public Integer getTaskRate() {
		return taskRate;
	}

	public void setTaskRate(Integer taskRate) {
		this.taskRate = taskRate;
	}

	public String getTaskStatus() {
		return taskStatus;
	}

	public void setTaskStatus(String taskStatus) {
		this.taskStatus = taskStatus;
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

	public Timestamp getFinishTime() {
		return finishTime;
	}

	public void setFinishTime(Timestamp finishTime) {
		this.finishTime = finishTime;
	}

	public String getDelFlag() {
		return delFlag;
	}

	public void setDelFlag(String delFlag) {
		this.delFlag = delFlag;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getTaskProcess() {
		return taskProcess;
	}

	public void setTaskProcess(String taskProcess) {
		this.taskProcess = taskProcess;
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
	
}
