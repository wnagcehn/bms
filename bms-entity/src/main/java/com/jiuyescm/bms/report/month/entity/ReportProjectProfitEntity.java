/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.report.month.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;

import com.jiuyescm.cfm.domain.IEntity;

/**
 * 
 * @author stevenl
 * 
 */
public class ReportProjectProfitEntity implements IEntity {

    
	/**
	 * 
	 */
	private static final long serialVersionUID = -5710132274723670743L;
	// ProjectId
	private String projectId;
	// ProjectName
	private String projectName;
	// FeesType
	private int feesType;
	// Amount01
	private BigDecimal amount01;
	// Amount02
	private BigDecimal amount02;
	// Amount03
	private BigDecimal amount03;
	// Amount04
	private BigDecimal amount04;
	// Amount05
	private BigDecimal amount05;
	// Amount06
	private BigDecimal amount06;
	// Amount07
	private BigDecimal amount07;
	// Amount08
	private BigDecimal amount08;
	// Amount09
	private BigDecimal amount09;
	// Amount10
	private BigDecimal amount10;
	// Amount11
	private BigDecimal amount11;
	// Amount12
	private BigDecimal amount12;
	// AmountSum
	private BigDecimal amountSum;
	// ReportYear
	private String reportYear;
	// CreateTime
	private Timestamp createTime;
	// ModifyTime
	private Timestamp modifyTime;

	public ReportProjectProfitEntity() {

	}
	
	/**
     * ProjectId
     */
	public String getProjectId() {
		return this.projectId;
	}

    /**
     * ProjectId
     *
     * @param projectId
     */
	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}
	
	/**
     * ProjectName
     */
	public String getProjectName() {
		return this.projectName;
	}

    /**
     * ProjectName
     *
     * @param projectName
     */
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	
	/**
     * FeesType
     */
	public int getFeesType() {
		return this.feesType;
	}

    /**
     * FeesType
     *
     * @param feesType
     */
	public void setFeesType(int feesType) {
		this.feesType = feesType;
	}
	
	/**
     * Amount01
     */
	public BigDecimal getAmount01() {
		return this.amount01;
	}

    /**
     * Amount01
     *
     * @param amount01
     */
	public void setAmount01(BigDecimal amount01) {
		this.amount01 = amount01;
	}
	
	/**
     * Amount02
     */
	public BigDecimal getAmount02() {
		return this.amount02;
	}

    /**
     * Amount02
     *
     * @param amount02
     */
	public void setAmount02(BigDecimal amount02) {
		this.amount02 = amount02;
	}
	
	/**
     * Amount03
     */
	public BigDecimal getAmount03() {
		return this.amount03;
	}

    /**
     * Amount03
     *
     * @param amount03
     */
	public void setAmount03(BigDecimal amount03) {
		this.amount03 = amount03;
	}
	
	/**
     * Amount04
     */
	public BigDecimal getAmount04() {
		return this.amount04;
	}

    /**
     * Amount04
     *
     * @param amount04
     */
	public void setAmount04(BigDecimal amount04) {
		this.amount04 = amount04;
	}
	
	/**
     * Amount05
     */
	public BigDecimal getAmount05() {
		return this.amount05;
	}

    /**
     * Amount05
     *
     * @param amount05
     */
	public void setAmount05(BigDecimal amount05) {
		this.amount05 = amount05;
	}
	
	/**
     * Amount06
     */
	public BigDecimal getAmount06() {
		return this.amount06;
	}

    /**
     * Amount06
     *
     * @param amount06
     */
	public void setAmount06(BigDecimal amount06) {
		this.amount06 = amount06;
	}
	
	/**
     * Amount07
     */
	public BigDecimal getAmount07() {
		return this.amount07;
	}

    /**
     * Amount07
     *
     * @param amount07
     */
	public void setAmount07(BigDecimal amount07) {
		this.amount07 = amount07;
	}
	
	/**
     * Amount08
     */
	public BigDecimal getAmount08() {
		return this.amount08;
	}

    /**
     * Amount08
     *
     * @param amount08
     */
	public void setAmount08(BigDecimal amount08) {
		this.amount08 = amount08;
	}
	
	/**
     * Amount09
     */
	public BigDecimal getAmount09() {
		return this.amount09;
	}

    /**
     * Amount09
     *
     * @param amount09
     */
	public void setAmount09(BigDecimal amount09) {
		this.amount09 = amount09;
	}
	
	/**
     * Amount10
     */
	public BigDecimal getAmount10() {
		return this.amount10;
	}

    /**
     * Amount10
     *
     * @param amount10
     */
	public void setAmount10(BigDecimal amount10) {
		this.amount10 = amount10;
	}
	
	/**
     * Amount11
     */
	public BigDecimal getAmount11() {
		return this.amount11;
	}

    /**
     * Amount11
     *
     * @param amount11
     */
	public void setAmount11(BigDecimal amount11) {
		this.amount11 = amount11;
	}
	
	/**
     * Amount12
     */
	public BigDecimal getAmount12() {
		return this.amount12;
	}

    /**
     * Amount12
     *
     * @param amount12
     */
	public void setAmount12(BigDecimal amount12) {
		this.amount12 = amount12;
	}
	
	/**
     * AmountSum
     */
	public BigDecimal getAmountSum() {
		return this.amountSum;
	}

    /**
     * AmountSum
     *
     * @param amountSum
     */
	public void setAmountSum(BigDecimal amountSum) {
		this.amountSum = amountSum;
	}
	
	/**
     * ReportYear
     */
	public String getReportYear() {
		return this.reportYear;
	}

    /**
     * ReportYear
     *
     * @param reportYear
     */
	public void setReportYear(String reportYear) {
		this.reportYear = reportYear;
	}
	
	/**
     * CreateTime
     */
	public Timestamp getCreateTime() {
		return this.createTime;
	}

    /**
     * CreateTime
     *
     * @param createTime
     */
	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}
	
	/**
     * ModifyTime
     */
	public Timestamp getModifyTime() {
		return this.modifyTime;
	}

    /**
     * ModifyTime
     *
     * @param modifyTime
     */
	public void setModifyTime(Timestamp modifyTime) {
		this.modifyTime = modifyTime;
	}
    
}
