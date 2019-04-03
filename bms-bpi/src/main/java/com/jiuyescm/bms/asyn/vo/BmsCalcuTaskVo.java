package com.jiuyescm.bms.asyn.vo;

import java.sql.Timestamp;

import com.jiuyescm.bms.calculate.vo.BmsFeesQtyVo;

/**
 * 
 * @author cjw
 * 
 */
public class BmsCalcuTaskVo extends BmsFeesQtyVo{

	private static final long serialVersionUID = -3251549084643926475L;
	// 自增标识
	private Long id;
	// 任务ID
	private String taskId;
	// 任务状态 0-等待 10-处理中 20-成功 30-异常 40-丢弃 50-作废
	private Integer taskStatus;
	// 计算进度
	private Integer taskRate;
	// 操作人
	private String crePerson;
	// 操作人id
	private String crePersonId;
	// 操作时间
	private Timestamp creTime;
	// 任务开始处理时间
	private Timestamp processTime;
	// 任务完成时间
	private Timestamp finishTime;
	// 备注
	private String remark;
	
	//费用类型
	private String feesType;
	
	//状态
	private String customerStatus;
	//科目数
	private Integer subjectNum;

	public BmsCalcuTaskVo() {

	}
	
	/**
     * 自增标识
     */
	public Long getId() {
		return this.id;
	}

    /**
     * 自增标识
     *
     * @param id
     */
	public void setId(Long id) {
		this.id = id;
	}
	
	/**
     * 任务ID
     */
	public String getTaskId() {
		return this.taskId;
	}

    /**
     * 任务ID
     *
     * @param taskId
     */
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	
	
	/**
     * 任务状态 0-等待 10-处理中 20-成功 30-异常 40-丢弃 50-作废
     */
	public Integer getTaskStatus() {
		return this.taskStatus;
	}

    /**
     * 任务状态 0-等待 10-处理中 20-成功 30-异常 40-丢弃 50-作废
     *
     * @param taskStatus
     */
	public void setTaskStatus(Integer taskStatus) {
		this.taskStatus = taskStatus;
	}

	/**
     * 计算进度
     */
	public Integer getTaskRate() {
		return this.taskRate;
	}

    /**
     * 计算进度
     *
     * @param taskRate
     */
	public void setTaskRate(Integer taskRate) {
		this.taskRate = taskRate;
	}
	
	/**
     * 操作人
     */
	public String getCrePerson() {
		return this.crePerson;
	}

    /**
     * 操作人
     *
     * @param crePerson
     */
	public void setCrePerson(String crePerson) {
		this.crePerson = crePerson;
	}
	
	/**
     * 操作人id
     */
	public String getCrePersonId() {
		return this.crePersonId;
	}

    /**
     * 操作人id
     *
     * @param crePersonId
     */
	public void setCrePersonId(String crePersonId) {
		this.crePersonId = crePersonId;
	}
	
	/**
     * 操作时间
     */
	public Timestamp getCreTime() {
		return this.creTime;
	}

    /**
     * 操作时间
     *
     * @param creTime
     */
	public void setCreTime(Timestamp creTime) {
		this.creTime = creTime;
	}
	
	/**
     * 任务开始处理时间
     */
	public Timestamp getProcessTime() {
		return this.processTime;
	}

    /**
     * 任务开始处理时间
     *
     * @param processTime
     */
	public void setProcessTime(Timestamp processTime) {
		this.processTime = processTime;
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
	 * 费用类型  商品按件存储-item
	 * @return
	 */
	public String getFeesType() {
		return feesType;
	}

	/**
	 * 费用类型  商品按件存储-item
	 * @param feesType
	 */
	public void setFeesType(String feesType) {
		this.feesType = feesType;
	}

	public String getCustomerStatus() {
		return customerStatus;
	}

	public void setCustomerStatus(String customerStatus) {
		this.customerStatus = customerStatus;
	}

	public Integer getSubjectNum() {
		return subjectNum;
	}

	public void setSubjectNum(Integer subjectNum) {
		this.subjectNum = subjectNum;
	}
    
}
