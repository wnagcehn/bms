package com.jiuyescm.bms.file.asyn;

import java.sql.Timestamp;
import java.util.Date;

import com.jiuyescm.cfm.domain.IEntity;

public class BmsCorrectAsynTaskEntity implements IEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6092061643585293405L;
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
	
	private Date startDate;
	private Date endDate;
	
	//备注
	private String remark;
	
	//业务类型  weight_correct-重量纠正  material_correct-耗材纠正
	private String bizType;
	
	private String lastModifier;
	private Timestamp lastModifierTime;
	
	public String getBizType() {
		return bizType;
	}

	public void setBizType(String bizType) {
		this.bizType = bizType;
	}

	public BmsCorrectAsynTaskEntity(){
		
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

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
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

	public String getLastModifier() {
		return lastModifier;
	}

	public void setLastModifier(String lastModifier) {
		this.lastModifier = lastModifier;
	}

	public Timestamp getLastModifierTime() {
		return lastModifierTime;
	}

	public void setLastModifierTime(Timestamp lastModifierTime) {
		this.lastModifierTime = lastModifierTime;
	}
	
	
	
}
