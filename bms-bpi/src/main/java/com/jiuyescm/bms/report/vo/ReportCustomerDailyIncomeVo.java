package com.jiuyescm.bms.report.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

public class ReportCustomerDailyIncomeVo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5136584912618754231L;

	// id
	private int id;
	// 商家ID
	private String customerId;
	// 商家名称
	private String customerName;
	// 区域ID
	private String regionId;
	// 区域名称
	private String regionName;
	// 负责部门名称
	private String deptName;
	// 销售员名称
	private String seller;
	// 项目管理员
	private String manager;
	// 项目结算员
	private String settleOfficer;
	// 费用产生日期
	private Date feesDate;
	// 费用科目
	private String subjectCode;
	// 总金额
	private BigDecimal amount;
	// 创建者
	private String creator;
	// 创建时间
	private Timestamp createTime;
	// 修改者
	private String lastModifier;
	// 修改时间
	private Timestamp lastModifyTime;
	// 删除标志
	private String delFlag;
	//耗材费用
	private BigDecimal materialAmount;
	//仓储费用
	private BigDecimal storageAmount;
	//运输费
	private BigDecimal transportAmount;
	//配送费
	private BigDecimal deliverAmount;

	public BigDecimal getMaterialAmount() {
		return materialAmount;
	}

	public void setMaterialAmount(BigDecimal materialAmount) {
		this.materialAmount = materialAmount;
	}

	public BigDecimal getStorageAmount() {
		return storageAmount;
	}

	public void setStorageAmount(BigDecimal storageAmount) {
		this.storageAmount = storageAmount;
	}

	public BigDecimal getTransportAmount() {
		return transportAmount;
	}

	public void setTransportAmount(BigDecimal transportAmount) {
		this.transportAmount = transportAmount;
	}

	public BigDecimal getDeliverAmount() {
		return deliverAmount;
	}

	public void setDeliverAmount(BigDecimal deliverAmount) {
		this.deliverAmount = deliverAmount;
	}

	public ReportCustomerDailyIncomeVo() {

	}
	
	/**
     * id
     */
	public int getId() {
		return this.id;
	}

    /**
     * id
     *
     * @param id
     */
	public void setId(int id) {
		this.id = id;
	}
	
	/**
     * 商家ID
     */
	public String getCustomerId() {
		return this.customerId;
	}

    /**
     * 商家ID
     *
     * @param customerId
     */
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	
	/**
     * 商家名称
     */
	public String getCustomerName() {
		return this.customerName;
	}

    /**
     * 商家名称
     *
     * @param customerName
     */
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	
	/**
     * 区域ID
     */
	public String getRegionId() {
		return this.regionId;
	}

    /**
     * 区域ID
     *
     * @param regionId
     */
	public void setRegionId(String regionId) {
		this.regionId = regionId;
	}
	
	/**
     * 区域名称
     */
	public String getRegionName() {
		return this.regionName;
	}

    /**
     * 区域名称
     *
     * @param regionName
     */
	public void setRegionName(String regionName) {
		this.regionName = regionName;
	}
	
	/**
     * 负责部门名称
     */
	public String getDeptName() {
		return this.deptName;
	}

    /**
     * 负责部门名称
     *
     * @param deptName
     */
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	
	/**
     * 销售员名称
     */
	public String getSeller() {
		return this.seller;
	}

    /**
     * 销售员名称
     *
     * @param seller
     */
	public void setSeller(String seller) {
		this.seller = seller;
	}
	
	/**
     * 项目管理员
     */
	public String getManager() {
		return this.manager;
	}

    /**
     * 项目管理员
     *
     * @param manager
     */
	public void setManager(String manager) {
		this.manager = manager;
	}
	
	/**
     * 项目结算员
     */
	public String getSettleOfficer() {
		return this.settleOfficer;
	}

    /**
     * 项目结算员
     *
     * @param settleOfficer
     */
	public void setSettleOfficer(String settleOfficer) {
		this.settleOfficer = settleOfficer;
	}
	
	/**
     * 费用产生日期
     */
	public Date getFeesDate() {
		return this.feesDate;
	}

    /**
     * 费用产生日期
     *
     * @param feesDate
     */
	public void setFeesDate(Date feesDate) {
		this.feesDate = feesDate;
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
     * 总金额
     */
	public BigDecimal getAmount() {
		return this.amount;
	}

    /**
     * 总金额
     *
     * @param amount
     */
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	
	/**
     * 创建者
     */
	public String getCreator() {
		return this.creator;
	}

    /**
     * 创建者
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
	
	/**
     * 修改者
     */
	public String getLastModifier() {
		return this.lastModifier;
	}

    /**
     * 修改者
     *
     * @param lastModifier
     */
	public void setLastModifier(String lastModifier) {
		this.lastModifier = lastModifier;
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
	
	/**
     * 删除标志
     */
	public String getDelFlag() {
		return this.delFlag;
	}

    /**
     * 删除标志
     *
     * @param delFlag
     */
	public void setDelFlag(String delFlag) {
		this.delFlag = delFlag;
	}
}
