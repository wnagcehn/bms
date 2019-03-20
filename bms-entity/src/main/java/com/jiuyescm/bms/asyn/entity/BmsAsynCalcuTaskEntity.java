package com.jiuyescm.bms.asyn.entity;

import java.sql.Timestamp;
import com.jiuyescm.cfm.domain.IEntity;

/**
 * 
 * @author cjw
 * 
 */
public class BmsAsynCalcuTaskEntity implements IEntity {

	private static final long serialVersionUID = -3251549084643926475L;
	// 自增标识
	private Long id;
	// 任务ID
	private String taskId;
	// 商家id
	private String customerId;
	// 费用科目
	private String subjectCode;
	// 业务年月 201901
	private Integer creMonth;
	// 任务状态 0-等待 10-处理中 20-成功 30-异常 40-丢弃 50-作废
	private Integer taskStatus;
	// 费用总数
	private Integer feesCount;
	// 本次任务需要计算的费用总数
	private Integer uncalcuCount;
	// 本次任务计算完成的费用总数
	private Integer calcuCount;
	// 未计算费用总数
	private Integer beginCount;
	// 计算成功总数
	private Integer finishCount;
	// 系统错误用总数
	private Integer sysErrorCount;
	// 合同缺失总数
	private Integer contractMissCount;
	// 报价缺失总数
	private Integer quoteMissCount;
	// 未计算费用总数
	private Integer noExeCount;
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

	public BmsAsynCalcuTaskEntity() {

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
     * 商家id
     */
	public String getCustomerId() {
		return this.customerId;
	}

    /**
     * 商家id
     *
     * @param customerId
     */
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	
	/**
     * 费用科目
     */
	public String getSubjectCode() {
		return this.subjectCode;
	}

    /**
     * 费用科目
     *
     * @param subjectCode
     */
	public void setSubjectCode(String subjectCode) {
		this.subjectCode = subjectCode;
	}
	
	/**
     * 业务年月 201901
     */
	public Integer getCreMonth() {
		return this.creMonth;
	}

    /**
     * 业务年月 201901
     *
     * @param creMonth
     */
	public void setCreMonth(Integer creMonth) {
		this.creMonth = creMonth;
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
     * 费用总数
     */
	public Integer getFeesCount() {
		return this.feesCount;
	}

    /**
     * 费用总数
     *
     * @param feesCount
     */
	public void setFeesCount(Integer feesCount) {
		this.feesCount = feesCount;
	}
	
	/**
     * 本次任务需要计算的费用总数
     */
	public Integer getUncalcuCount() {
		return this.uncalcuCount;
	}

    /**
     * 本次任务需要计算的费用总数
     *
     * @param uncalcuCount
     */
	public void setUncalcuCount(Integer uncalcuCount) {
		this.uncalcuCount = uncalcuCount;
	}
	
	/**
     * 本次任务计算完成的费用总数
     */
	public Integer getCalcuCount() {
		return this.calcuCount;
	}

    /**
     * 本次任务计算完成的费用总数
     *
     * @param calcuCount
     */
	public void setCalcuCount(Integer calcuCount) {
		this.calcuCount = calcuCount;
	}
	
	/**
     * 未计算费用总数
     */
	public Integer getBeginCount() {
		return this.beginCount;
	}

    /**
     * 未计算费用总数
     *
     * @param beginCount
     */
	public void setBeginCount(Integer beginCount) {
		this.beginCount = beginCount;
	}
	
	/**
     * 计算成功总数
     */
	public Integer getFinishCount() {
		return this.finishCount;
	}

    /**
     * 计算成功总数
     *
     * @param finishCount
     */
	public void setFinishCount(Integer finishCount) {
		this.finishCount = finishCount;
	}
	
	/**
     * 系统错误用总数
     */
	public Integer getSysErrorCount() {
		return this.sysErrorCount;
	}

    /**
     * 系统错误用总数
     *
     * @param sysErrorCount
     */
	public void setSysErrorCount(Integer sysErrorCount) {
		this.sysErrorCount = sysErrorCount;
	}
	
	/**
     * 合同缺失总数
     */
	public Integer getContractMissCount() {
		return this.contractMissCount;
	}

    /**
     * 合同缺失总数
     *
     * @param contractMissCount
     */
	public void setContractMissCount(Integer contractMissCount) {
		this.contractMissCount = contractMissCount;
	}
	
	/**
     * 报价缺失总数
     */
	public Integer getQuoteMissCount() {
		return this.quoteMissCount;
	}

    /**
     * 报价缺失总数
     *
     * @param quoteMissCount
     */
	public void setQuoteMissCount(Integer quoteMissCount) {
		this.quoteMissCount = quoteMissCount;
	}
	
	/**
     * 未计算费用总数
     */
	public Integer getNoExeCount() {
		return this.noExeCount;
	}

    /**
     * 未计算费用总数
     *
     * @param noExeCount
     */
	public void setNoExeCount(Integer noExeCount) {
		this.noExeCount = noExeCount;
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
    
}
