/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.billcheck.vo;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

import com.jiuyescm.cfm.domain.IEntity;

/**
 * 
 * @author stevenl
 * 
 */
public class BillReceiptFollowVo implements IEntity {

	// TODO Change serialVersionUID by eclipse
    private static final long serialVersionUID = -1;
    
	// id
	private Integer id;
	// 对账主表id
	private Integer billCheckId;
	// 反馈日期
	private Date receiptTime;
	// 反馈部门id
	private String receiptDeptId;
	// 反馈部门
	private String receiptDept;
	// 反馈人
	private String receiptMan;
	// 反馈人id
	private String receiptManId;
	//预计回款金额
	private BigDecimal expectReceiptMoney;
	// 预计回款时间
	private Date expectReceiptDate;
	// 跟进类型
	private String followType;
	// creator
	private String creator;
	// CreateTime
	private Timestamp createTime;
	// 详细说明
	private String operdesc;
	// 附近名称
	private String fileName;
	// 附件地址
	private String fileUrl;
	
	//开票名称
	private String invoiceName;
	//业务月份
	private int createMonth;
	//账单状态
	private String billStatus;
	//账单名称
	private String billName;
	//删除状态
	private String delFlag;

	public BillReceiptFollowVo() {

	}
	
	/**
     * id
     */
	public Integer getId() {
		return this.id;
	}

    /**
     * id
     *
     * @param id
     */
	public void setId(Integer id) {
		this.id = id;
	}
	
	/**
     * 对账主表id
     */
	public Integer getBillCheckId() {
		return this.billCheckId;
	}

    /**
     * 对账主表id
     *
     * @param billCheckId
     */
	public void setBillCheckId(Integer billCheckId) {
		this.billCheckId = billCheckId;
	}
	
	public Date getReceiptTime() {
		return receiptTime;
	}

	public void setReceiptTime(Date receiptTime) {
		this.receiptTime = receiptTime;
	}

	/**
     * 反馈部门id
     */
	public String getReceiptDeptId() {
		return this.receiptDeptId;
	}

    /**
     * 反馈部门id
     *
     * @param receiptDeptId
     */
	public void setReceiptDeptId(String receiptDeptId) {
		this.receiptDeptId = receiptDeptId;
	}
	
	/**
     * 反馈部门
     */
	public String getReceiptDept() {
		return this.receiptDept;
	}

    /**
     * 反馈部门
     *
     * @param receiptDept
     */
	public void setReceiptDept(String receiptDept) {
		this.receiptDept = receiptDept;
	}
	
	/**
     * 反馈人
     */
	public String getReceiptMan() {
		return this.receiptMan;
	}

    /**
     * 反馈人
     *
     * @param receiptMan
     */
	public void setReceiptMan(String receiptMan) {
		this.receiptMan = receiptMan;
	}
	
	/**
     * 反馈人id
     */
	public String getReceiptManId() {
		return this.receiptManId;
	}

    /**
     * 反馈人id
     *
     * @param receiptManId
     */
	public void setReceiptManId(String receiptManId) {
		this.receiptManId = receiptManId;
	}
	

	
	public Date getExpectReceiptDate() {
		return expectReceiptDate;
	}

	public void setExpectReceiptDate(Date expectReceiptDate) {
		this.expectReceiptDate = expectReceiptDate;
	}

	/**
     * 跟进类型
     */
	public String getFollowType() {
		return this.followType;
	}

    /**
     * 跟进类型
     *
     * @param followType
     */
	public void setFollowType(String followType) {
		this.followType = followType;
	}
	
	/**
     * creator
     */
	public String getCreator() {
		return this.creator;
	}

    /**
     * creator
     *
     * @param creator
     */
	public void setCreator(String creator) {
		this.creator = creator;
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
     * 详细说明
     */
	public String getOperdesc() {
		return this.operdesc;
	}

    /**
     * 详细说明
     *
     * @param operdesc
     */
	public void setOperdesc(String operdesc) {
		this.operdesc = operdesc;
	}
	
	/**
     * 附近名称
     */
	public String getFileName() {
		return this.fileName;
	}

    /**
     * 附近名称
     *
     * @param fileName
     */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	/**
     * 附件地址
     */
	public String getFileUrl() {
		return this.fileUrl;
	}

    /**
     * 附件地址
     *
     * @param fileUrl
     */
	public void setFileUrl(String fileUrl) {
		this.fileUrl = fileUrl;
	}

	public String getInvoiceName() {
		return invoiceName;
	}

	public void setInvoiceName(String invoiceName) {
		this.invoiceName = invoiceName;
	}

	public int getCreateMonth() {
		return createMonth;
	}

	public void setCreateMonth(int createMonth) {
		this.createMonth = createMonth;
	}

	public String getBillStatus() {
		return billStatus;
	}

	public void setBillStatus(String billStatus) {
		this.billStatus = billStatus;
	}

	public String getDelFlag() {
		return delFlag;
	}

	public void setDelFlag(String delFlag) {
		this.delFlag = delFlag;
	}

	public String getBillName() {
		return billName;
	}

	public void setBillName(String billName) {
		this.billName = billName;
	}

	public BigDecimal getExpectReceiptMoney() {
		return expectReceiptMoney;
	}

	public void setExpectReceiptMoney(BigDecimal expectReceiptMoney) {
		this.expectReceiptMoney = expectReceiptMoney;
	}


	
    
}
