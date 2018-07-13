/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.base.file.entity;

import java.sql.Timestamp;

import com.jiuyescm.cfm.domain.IEntity;

/**
 * 文件导出任务列表实体类
 * @author yangss
 */
public class FileExportTaskEntity implements IEntity {

	private static final long serialVersionUID = 4778218223613571777L;

	// 任务id
	private String taskId;
	// 任务名称
	private String taskName;
	// 账单编号
	private String billNo;
	// 商家id
	private String customerid;
	// 开始时间
	private Timestamp startTime;
	// 结束时间
	private Timestamp endTime;
	// 任务类型
	private String taskType;
	// 状态(0:已创建，1:进行中，2:成功，3:失败)
	private String taskState;
	// 进度
	private Double progress;
	// 文件路径
	private String filePath;
	// 参数1
	private String param1;
	// 参数2
	private String param2;
	// 参数3
	private String param3;
	// 参数4
	private String param4;
	// 参数5
	private String param5;
	// 创建人
	private String creator;
	// 创建时间
	private Timestamp createTime;
	// 修改者
	private String lastModifier;
	// 修改时间
	private Timestamp lastModifyTime;
	// 删除标志 0-未作废 1-作废
	private String delFlag;

	public FileExportTaskEntity() {

	}

	/**
	 * 任务id
	 */
	public String getTaskId() {
		return this.taskId;
	}

	/**
	 * 任务id
	 * 
	 * @param taskId
	 */
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	/**
	 * 任务名称
	 */
	public String getTaskName() {
		return this.taskName;
	}

	/**
	 * 任务名称
	 * 
	 * @param taskName
	 */
	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	/**
	 * 账单编号
	 */
	public String getBillNo() {
		return this.billNo;
	}

	/**
	 * 账单编号
	 * 
	 * @param billNo
	 */
	public void setBillNo(String billNo) {
		this.billNo = billNo;
	}

	/**
	 * 商家id
	 */
	public String getCustomerid() {
		return this.customerid;
	}

	/**
	 * 商家id
	 * 
	 * @param customerid
	 */
	public void setCustomerid(String customerid) {
		this.customerid = customerid;
	}

	/**
	 * 开始时间
	 */
	public Timestamp getStartTime() {
		return this.startTime;
	}

	/**
	 * 开始时间
	 * 
	 * @param startTime
	 */
	public void setStartTime(Timestamp startTime) {
		this.startTime = startTime;
	}

	/**
	 * 结束时间
	 */
	public Timestamp getEndTime() {
		return this.endTime;
	}

	/**
	 * 结束时间
	 * 
	 * @param endTime
	 */
	public void setEndTime(Timestamp endTime) {
		this.endTime = endTime;
	}

	/**
	 * 任务类型
	 */
	public String getTaskType() {
		return this.taskType;
	}

	/**
	 * 任务类型
	 * 
	 * @param taskType
	 */
	public void setTaskType(String taskType) {
		this.taskType = taskType;
	}

	/**
	 * 状态(0:已创建，1:进行中，2:成功，3:失败)
	 */
	public String getTaskState() {
		return this.taskState;
	}

	/**
	 * 状态(0:已创建，1:进行中，2:成功，3:失败)
	 * 
	 * @param taskState
	 */
	public void setTaskState(String taskState) {
		this.taskState = taskState;
	}

	/**
	 * 进度
	 */
	public Double getProgress() {
		return this.progress;
	}

	/**
	 * 进度
	 * 
	 * @param progress
	 */
	public void setProgress(Double progress) {
		this.progress = progress;
	}

	/**
	 * 文件路径
	 */
	public String getFilePath() {
		return this.filePath;
	}

	/**
	 * 文件路径
	 * 
	 * @param filePath
	 */
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	/**
	 * 参数1
	 */
	public String getParam1() {
		return this.param1;
	}

	/**
	 * 参数1
	 * 
	 * @param param1
	 */
	public void setParam1(String param1) {
		this.param1 = param1;
	}

	/**
	 * 参数2
	 */
	public String getParam2() {
		return this.param2;
	}

	/**
	 * 参数2
	 * 
	 * @param param2
	 */
	public void setParam2(String param2) {
		this.param2 = param2;
	}

	/**
	 * 参数3
	 */
	public String getParam3() {
		return this.param3;
	}

	/**
	 * 参数3
	 * 
	 * @param param3
	 */
	public void setParam3(String param3) {
		this.param3 = param3;
	}

	/**
	 * 参数4
	 */
	public String getParam4() {
		return this.param4;
	}

	/**
	 * 参数4
	 * 
	 * @param param4
	 */
	public void setParam4(String param4) {
		this.param4 = param4;
	}

	/**
	 * 参数5
	 */
	public String getParam5() {
		return this.param5;
	}

	/**
	 * 参数5
	 * 
	 * @param param5
	 */
	public void setParam5(String param5) {
		this.param5 = param5;
	}

	/**
	 * 创建人
	 */
	public String getCreator() {
		return this.creator;
	}

	/**
	 * 创建人
	 * 
	 * @param creator
	 */
	public void setCreator(String creator) {
		this.creator = creator;
	}

	/**
	 * 创建时间
	 */
	public Timestamp getCreateTime() {
		return this.createTime;
	}

	/**
	 * 创建时间
	 * 
	 * @param createTime
	 */
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

	/**
	 * 删除标志 0-未作废 1-作废
	 */
	public String getDelFlag() {
		return this.delFlag;
	}

	/**
	 * 删除标志 0-未作废 1-作废
	 * 
	 * @param delFlag
	 */
	public void setDelFlag(String delFlag) {
		this.delFlag = delFlag;
	}

}
