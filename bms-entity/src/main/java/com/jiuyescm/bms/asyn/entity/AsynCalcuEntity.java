package com.jiuyescm.bms.asyn.entity;

import java.sql.Timestamp;

import com.jiuyescm.cfm.domain.IEntity;

 /**
 * ..Entity
 * @author wangchen
 * 
 */
public class AsynCalcuEntity implements IEntity {
    
	/**
	 * 
	 */
	private static final long serialVersionUID = 8326343258164128099L;
	// id
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
	
	private String taskRateProcess;

	public AsynCalcuEntity() {
		super();
	}
	
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public String getInOutTypecode() {
		return this.inOutTypecode;
	}

	public void setInOutTypecode(String inOutTypecode) {
		this.inOutTypecode = inOutTypecode;
	}
	
	public String getBizTypecode() {
		return this.bizTypecode;
	}

	public void setBizTypecode(String bizTypecode) {
		this.bizTypecode = bizTypecode;
	}
	
	public String getParentTaskId() {
		return this.parentTaskId;
	}

	public void setParentTaskId(String parentTaskId) {
		this.parentTaskId = parentTaskId;
	}
	
	public String getTaskId() {
		return this.taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
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
	
	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getTaskRateProcess() {
		return taskRateProcess;
	}

	public void setTaskRateProcess(String taskRateProcess) {
		this.taskRateProcess = taskRateProcess;
	}
    
}
