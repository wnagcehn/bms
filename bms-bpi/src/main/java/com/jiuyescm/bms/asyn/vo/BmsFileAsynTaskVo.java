package com.jiuyescm.bms.asyn.vo;

import java.sql.Timestamp;

import com.jiuyescm.cfm.domain.IEntity;

public class BmsFileAsynTaskVo implements IEntity {

	private static final long serialVersionUID = 664206307229147587L;
	
	// 自增主键
	private Integer id;
	// 任务编号
	private String taskId;
	// 任务名称
	private String taskName;
	// 任务进度
	private Integer taskRate;
	// 任务状态 WAIT-等待 PROCESS-处理 SUCCESS-成功 EXCEPTION-导入异常 CANCEL-取消
	private String taskStatus;
	// 业务类型 IMPORT-导入 EXPORT-导出
	private String bizType;
	// 任务类型
	private String taskType;
	// 文件行数（不包括列名行）
	private Integer fileRows;
	// 创建人ID
	private String creatorId;
	// 操作人
	private String creator;
	// 任务创建时间
	private Timestamp createTime;
	// 任务完成时间
	private Timestamp finishTime;
	// 原文件名称
	private String originFileName;
	// 原文件路径
	private String originFilePath;
	// 结果文件名称
	private String resultFileName;
	// 结果文件路径
	private String resultFilePath;
	// 备注
	private String remark;

	public BmsFileAsynTaskVo() {

	}
	
	/**
	 * 
	 * @param taskId 			任务编号
	 * @param taskRate			任务进度
	 * @param taskStatus		任务状态
	 * @param fileRows			文件行数（不包括列名行）
	 * @param finishTime		任务完成时间
	 * @param resultFileName	结果文件名称
	 * @param resultFilePath	结果文件路径
	 * @param remark			备注
	 */
	public BmsFileAsynTaskVo(String taskId,	   Integer   taskRate,   String taskStatus,
							 Integer fileRows, Timestamp finishTime, String resultFileName,
							 String resultFilePath,String remark){
		this.taskId = taskId;
		this.taskRate = taskRate;
		this.taskStatus = taskStatus;
		this.fileRows = fileRows;
		this.finishTime = finishTime;
		this.resultFileName = resultFileName;
		this.resultFilePath = resultFilePath;
		this.remark = remark;
	}
	
	/**
     * 自增主键
     */
	public Integer getId() {
		return this.id;
	}

    /**
     * 自增主键
     *
     * @param id
     */
	public void setId(Integer id) {
		this.id = id;
	}
	
	/**
     * 任务编号
     */
	public String getTaskId() {
		return this.taskId;
	}

    /**
     * 任务编号
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
     * 任务进度
     */
	public Integer getTaskRate() {
		return this.taskRate;
	}

    /**
     * 任务进度
     *
     * @param taskRate
     */
	public void setTaskRate(Integer taskRate) {
		this.taskRate = taskRate;
	}
	
	/**
     * 任务状态 WAIT-等待 PROCESS-处理 SUCCESS-成功 EXCEPTION-导入异常 CANCEL-取消
     */
	public String getTaskStatus() {
		return this.taskStatus;
	}

    /**
     * 任务状态 WAIT-等待 PROCESS-处理 SUCCESS-成功 EXCEPTION-导入异常 CANCEL-取消
     *
     * @param taskStatus
     */
	public void setTaskStatus(String taskStatus) {
		this.taskStatus = taskStatus;
	}
	
	/**
     * 业务类型 IMPORT-导入 EXPORT-导出
     */
	public String getBizType() {
		return this.bizType;
	}

    /**
     * 业务类型 IMPORT-导入 EXPORT-导出
     *
     * @param bizType
     */
	public void setBizType(String bizType) {
		this.bizType = bizType;
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
     * 文件行数（不包括列名行）
     */
	public Integer getFileRows() {
		return this.fileRows;
	}

    /**
     * 文件行数（不包括列名行）
     *
     * @param fileRows
     */
	public void setFileRows(Integer fileRows) {
		this.fileRows = fileRows;
	}
	
	public String getCreatorId() {
		return creatorId;
	}

	public void setCreatorId(String creatorId) {
		this.creatorId = creatorId;
	}

	/**
     * 操作人
     */
	public String getCreator() {
		return this.creator;
	}

    /**
     * 操作人
     *
     * @param creator
     */
	public void setCreator(String creator) {
		this.creator = creator;
	}
	
	/**
     * 任务创建时间
     */
	public Timestamp getCreateTime() {
		return this.createTime;
	}

    /**
     * 任务创建时间
     *
     * @param createTime
     */
	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}
	
	/**
     * 任务完成时间
     */
	public Timestamp getFinishTime() {
		return this.finishTime;
	}

    /**
     * 任务完成时间
     *
     * @param finishTime
     */
	public void setFinishTime(Timestamp finishTime) {
		this.finishTime = finishTime;
	}
	
	/**
     * 原文件名称
     */
	public String getOriginFileName() {
		return this.originFileName;
	}

    /**
     * 原文件名称
     *
     * @param originFileName
     */
	public void setOriginFileName(String originFileName) {
		this.originFileName = originFileName;
	}
	
	/**
     * 原文件路径
     */
	public String getOriginFilePath() {
		return this.originFilePath;
	}

    /**
     * 原文件路径
     *
     * @param originFilePath
     */
	public void setOriginFilePath(String originFilePath) {
		this.originFilePath = originFilePath;
	}
	
	/**
     * 结果文件名称
     */
	public String getResultFileName() {
		return this.resultFileName;
	}

    /**
     * 结果文件名称
     *
     * @param resultFileName
     */
	public void setResultFileName(String resultFileName) {
		this.resultFileName = resultFileName;
	}
	
	/**
     * 结果文件路径
     */
	public String getResultFilePath() {
		return this.resultFilePath;
	}

    /**
     * 结果文件路径
     *
     * @param resultFilePath
     */
	public void setResultFilePath(String resultFilePath) {
		this.resultFilePath = resultFilePath;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
    
}
