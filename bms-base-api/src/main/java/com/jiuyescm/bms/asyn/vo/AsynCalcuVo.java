package com.jiuyescm.bms.asyn.vo;

import java.sql.Timestamp;

import com.jiuyescm.cfm.domain.IEntity;

public class AsynCalcuVo implements IEntity {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8325139372505796036L;
	private Long id;
	// 收支类型（INPUT-收入 OUTPUT-支出）
	private String inOutTypecode;
	// 业务类型（STORAGE-仓，TRANSPORT-干，DISPATCH-配）
	private String bizTypecode;
	// 父任务ID
	private String parentTaskId;
	// 任务ID
	private String taskId;
	// 费用科目
	private String subjectCode;
	// 任务名称
	private String taskName;
	// 任务进度
	private Integer taskRate;
	// 任务状态 WAIT-等待 PROCESS-处理 SUCCESS-成功 FAIL-失败
	private String taskStatus;
	// 操作人
	private String creator;
	// 任务创建时间
	private Timestamp createTime;
	// 任务完成时间
	private Timestamp finishTime;
	// 0-启用 1-作废
	private String delFlag;
	// 备注
	private String remark;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getInOutTypecode() {
		return inOutTypecode;
	}
	public void setInOutTypecode(String inOutTypecode) {
		this.inOutTypecode = inOutTypecode;
	}
	public String getBizTypecode() {
		return bizTypecode;
	}
	public void setBizTypecode(String bizTypecode) {
		this.bizTypecode = bizTypecode;
	}
	public String getParentTaskId() {
		return parentTaskId;
	}
	public void setParentTaskId(String parentTaskId) {
		this.parentTaskId = parentTaskId;
	}
	public String getTaskId() {
		return taskId;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	public String getSubjectCode() {
		return subjectCode;
	}
	public void setSubjectCode(String subjectCode) {
		this.subjectCode = subjectCode;
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
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}

}
