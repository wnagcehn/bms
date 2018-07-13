package com.jiuyescm.bms.report.month.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;

import com.jiuyescm.cfm.domain.IEntity;

public class BmsMonthReportEntity implements IEntity {

	 // TODO Change serialVersionUID by eclipse
    private static final long serialVersionUID = -1;
    
	// 报表类型 公司利润报表,商家利润报表...
	private String reportType;
	// 级别1编码
	private String level1Code;
	// 级别1名称
	private String level1Name;
	// 级别2编码
	private String level2Code;
	// 级别2名称
	private String level2Name;
	// 级别3编码
	private String level3Code;
	// 级别3名称
	private String level3Name;
	// 收支项目 3-收入,2-成本,1-利润
	private Integer bothType;
	// 1月收支金额
	private BigDecimal januaryAmount;
	// 2月收支金额
	private BigDecimal februaryAmount;
	// 3月收支金额
	private BigDecimal marchAmount;
	// 4月收支金额
	private BigDecimal aprilAmount;
	// 5月收支金额
	private BigDecimal mayAmount;
	// 6月收支金额
	private BigDecimal juneAmount;
	// 7月收支金额
	private BigDecimal julyAmount;
	// 8月收支金额
	private BigDecimal augustAmount;
	// 9月收支金额
	private BigDecimal septemberAmount;
	// 10月收支金额
	private BigDecimal octoberAmount;
	// 11月收支金额
	private BigDecimal novemberAmount;
	// 12月收支金额
	private BigDecimal decemberAmount;
	// 所在年份
	private Integer reportYear;
	// 创建时间
	private Timestamp createTime;
	// 修改时间
	private Timestamp lastModifyTime;

	public BmsMonthReportEntity() {

	}
	
	/**
     * 报表类型 公司利润报表,商家利润报表...
     */
	public String getReportType() {
		return this.reportType;
	}

    /**
     * 报表类型 公司利润报表,商家利润报表...
     *
     * @param reportType
     */
	public void setReportType(String reportType) {
		this.reportType = reportType;
	}
	
	/**
     * 级别1编码
     */
	public String getLevel1Code() {
		return this.level1Code;
	}

    /**
     * 级别1编码
     *
     * @param level1Code
     */
	public void setLevel1Code(String level1Code) {
		this.level1Code = level1Code;
	}
	
	/**
     * 级别1名称
     */
	public String getLevel1Name() {
		return this.level1Name;
	}

    /**
     * 级别1名称
     *
     * @param level1Name
     */
	public void setLevel1Name(String level1Name) {
		this.level1Name = level1Name;
	}
	
	/**
     * 级别2编码
     */
	public String getLevel2Code() {
		return this.level2Code;
	}

    /**
     * 级别2编码
     *
     * @param level2Code
     */
	public void setLevel2Code(String level2Code) {
		this.level2Code = level2Code;
	}
	
	/**
     * 级别2名称
     */
	public String getLevel2Name() {
		return this.level2Name;
	}

    /**
     * 级别2名称
     *
     * @param level2Name
     */
	public void setLevel2Name(String level2Name) {
		this.level2Name = level2Name;
	}
	
	/**
     * 级别3编码
     */
	public String getLevel3Code() {
		return this.level3Code;
	}

    /**
     * 级别3编码
     *
     * @param level3Code
     */
	public void setLevel3Code(String level3Code) {
		this.level3Code = level3Code;
	}
	
	/**
     * 级别3名称
     */
	public String getLevel3Name() {
		return this.level3Name;
	}

    /**
     * 级别3名称
     *
     * @param level3Name
     */
	public void setLevel3Name(String level3Name) {
		this.level3Name = level3Name;
	}
	
	/**
     * 收支项目 3-收入,2-成本,1-利润
     */
	public Integer getBothType() {
		return this.bothType;
	}

    /**
     * 收支项目 3-收入,2-成本,1-利润
     *
     * @param bothType
     */
	public void setBothType(Integer bothType) {
		this.bothType = bothType;
	}
	
	/**
     * 1月收支金额
     */
	public BigDecimal getJanuaryAmount() {
		return this.januaryAmount;
	}

    /**
     * 1月收支金额
     *
     * @param januaryAmount
     */
	public void setJanuaryAmount(BigDecimal januaryAmount) {
		this.januaryAmount = januaryAmount;
	}
	
	/**
     * 2月收支金额
     */
	public BigDecimal getFebruaryAmount() {
		return this.februaryAmount;
	}

    /**
     * 2月收支金额
     *
     * @param februaryAmount
     */
	public void setFebruaryAmount(BigDecimal februaryAmount) {
		this.februaryAmount = februaryAmount;
	}
	
	/**
     * 3月收支金额
     */
	public BigDecimal getMarchAmount() {
		return this.marchAmount;
	}

    /**
     * 3月收支金额
     *
     * @param marchAmount
     */
	public void setMarchAmount(BigDecimal marchAmount) {
		this.marchAmount = marchAmount;
	}
	
	/**
     * 4月收支金额
     */
	public BigDecimal getAprilAmount() {
		return this.aprilAmount;
	}

    /**
     * 4月收支金额
     *
     * @param aprilAmount
     */
	public void setAprilAmount(BigDecimal aprilAmount) {
		this.aprilAmount = aprilAmount;
	}
	
	/**
     * 5月收支金额
     */
	public BigDecimal getMayAmount() {
		return this.mayAmount;
	}

    /**
     * 5月收支金额
     *
     * @param mayAmount
     */
	public void setMayAmount(BigDecimal mayAmount) {
		this.mayAmount = mayAmount;
	}
	
	/**
     * 6月收支金额
     */
	public BigDecimal getJuneAmount() {
		return this.juneAmount;
	}

    /**
     * 6月收支金额
     *
     * @param juneAmount
     */
	public void setJuneAmount(BigDecimal juneAmount) {
		this.juneAmount = juneAmount;
	}
	
	/**
     * 7月收支金额
     */
	public BigDecimal getJulyAmount() {
		return this.julyAmount;
	}

    /**
     * 7月收支金额
     *
     * @param julyAmount
     */
	public void setJulyAmount(BigDecimal julyAmount) {
		this.julyAmount = julyAmount;
	}
	
	/**
     * 8月收支金额
     */
	public BigDecimal getAugustAmount() {
		return this.augustAmount;
	}

    /**
     * 8月收支金额
     *
     * @param augustAmount
     */
	public void setAugustAmount(BigDecimal augustAmount) {
		this.augustAmount = augustAmount;
	}
	
	/**
     * 9月收支金额
     */
	public BigDecimal getSeptemberAmount() {
		return this.septemberAmount;
	}

    /**
     * 9月收支金额
     *
     * @param septemberAmount
     */
	public void setSeptemberAmount(BigDecimal septemberAmount) {
		this.septemberAmount = septemberAmount;
	}
	
	/**
     * 10月收支金额
     */
	public BigDecimal getOctoberAmount() {
		return this.octoberAmount;
	}

    /**
     * 10月收支金额
     *
     * @param octoberAmount
     */
	public void setOctoberAmount(BigDecimal octoberAmount) {
		this.octoberAmount = octoberAmount;
	}
	
	/**
     * 11月收支金额
     */
	public BigDecimal getNovemberAmount() {
		return this.novemberAmount;
	}

    /**
     * 11月收支金额
     *
     * @param novemberAmount
     */
	public void setNovemberAmount(BigDecimal novemberAmount) {
		this.novemberAmount = novemberAmount;
	}
	
	/**
     * 12月收支金额
     */
	public BigDecimal getDecemberAmount() {
		return this.decemberAmount;
	}

    /**
     * 12月收支金额
     *
     * @param decemberAmount
     */
	public void setDecemberAmount(BigDecimal decemberAmount) {
		this.decemberAmount = decemberAmount;
	}
	
	/**
     * 所在年份
     */
	public Integer getReportYear() {
		return this.reportYear;
	}

    /**
     * 所在年份
     *
     * @param reportYear
     */
	public void setReportYear(Integer reportYear) {
		this.reportYear = reportYear;
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
	
	/**
     * 修改时间
     */
	public Timestamp getLastModifyTime() {
		return this.lastModifyTime;
	}

    /**
     * 修改时间
     *
     * @param lastModifyTime
     */
	public void setLastModifyTime(Timestamp lastModifyTime) {
		this.lastModifyTime = lastModifyTime;
	}
    
    
}
