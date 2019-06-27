package com.jiuyescm.bms.calculate.vo;

import java.math.BigDecimal;

import com.jiuyescm.cfm.domain.IEntity;

/**
 * 费用数量统计
 * @author caojianwei
 *
 */
public class BmsFeesQtyVo implements IEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4421310923875816242L;
	
	// 商家id
	private String customerId;
		
	private String customerName;
		
	// 费用科目
	private String subjectCode;
		
	private String subjectName;
	// 业务年月 201901
	private Integer creMonth;
	
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
	// 不计算费用总数
	private Integer noExeCount;	
	//计算状态
	private Integer calcuStatus;
	
	private Integer noDinggouCount;
	
	private BigDecimal totalAmount;
	
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
	 * 商家名称
	 * @return
	 */
	public String getCustomerName() {
		return customerName;
	}

	/**
	 * 商家名称
	 * @param customerName
	 */
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	/**
	 * 费用科目名称
	 * @param subjectName
	 */
	public String getSubjectName() {
		return subjectName;
	}

	/**
	 * 费用科目名称
	 * @param subjectName
	 */
	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}

	public Integer getCalcuStatus() {
		return calcuStatus;
	}

	public void setCalcuStatus(Integer calcuStatus) {
		this.calcuStatus = calcuStatus;
	}

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Integer getNoDinggouCount() {
        return noDinggouCount;
    }

    public void setNoDinggouCount(Integer noDinggouCount) {
        this.noDinggouCount = noDinggouCount;
    }
	

	
}
