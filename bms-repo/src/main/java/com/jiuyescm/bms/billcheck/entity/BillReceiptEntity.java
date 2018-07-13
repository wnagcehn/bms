package com.jiuyescm.bms.billcheck.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

import com.jiuyescm.cfm.domain.IEntity;

public class BillReceiptEntity implements IEntity{
	  // TODO Change serialVersionUID by eclipse
    private static final long serialVersionUID = -1;
    
	// id
	private Long id;
	// 对账主表ID
	private Integer billCheckId;
	// 回款序号
	private Integer sortNo;
	// 回款日期 
	private Date receiptDate;
	// 回款金额
	private BigDecimal receiptAmount;
	// CreatorId
	private String creatorId;
	// creator
	private String creator;
	// CreateTime
	private Timestamp createTime;
	// LastModifier
	private String lastModifier;
	// LastModifyTime
	private Timestamp lastModifyTime;
	// DelFlag
	private String delFlag;
	// remark
	private String remark;
	
	//开票名称
	private String invoiceName;
	//业务月份
	private int createMonth;
	//账单名称
	private String billName;
	//销售员
	private String sellerName;
	//地区
	private String area;
	//统计金额
	private BigDecimal totalPrice;
	
	public BillReceiptEntity() {

	}
	
	/**
     * id
     */
	public Long getId() {
		return this.id;
	}

    /**
     * id
     *
     * @param id
     */
	public void setId(Long id) {
		this.id = id;
	}
	
	/**
     * 对账主表ID
     */
	public Integer getBillCheckId() {
		return this.billCheckId;
	}

    /**
     * 对账主表ID
     *
     * @param billCheckId
     */
	public void setBillCheckId(Integer billCheckId) {
		this.billCheckId = billCheckId;
	}
	
	/**
     * 回款序号
     */
	public Integer getSortNo() {
		return this.sortNo;
	}

    /**
     * 回款序号
     *
     * @param sortNo
     */
	public void setSortNo(Integer sortNo) {
		this.sortNo = sortNo;
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
     * LastModifier
     */
	public String getLastModifier() {
		return this.lastModifier;
	}

    /**
     * LastModifier
     *
     * @param lastModifier
     */
	public void setLastModifier(String lastModifier) {
		this.lastModifier = lastModifier;
	}
	
	/**
     * LastModifyTime
     */
	public Timestamp getLastModifyTime() {
		return this.lastModifyTime;
	}

    /**
     * LastModifyTime
     *
     * @param lastModifyTime
     */
	public void setLastModifyTime(Timestamp lastModifyTime) {
		this.lastModifyTime = lastModifyTime;
	}
	
	/**
     * DelFlag
     */
	public String getDelFlag() {
		return this.delFlag;
	}

    /**
     * DelFlag
     *
     * @param delFlag
     */
	public void setDelFlag(String delFlag) {
		this.delFlag = delFlag;
	}
	
	/**
     * remark
     */
	public String getRemark() {
		return this.remark;
	}

    /**
     * remark
     *
     * @param remark
     */
	public void setRemark(String remark) {
		this.remark = remark;
	}

	public BigDecimal getReceiptAmount() {
		return receiptAmount;
	}

	public void setReceiptAmount(BigDecimal receiptAmount) {
		this.receiptAmount = receiptAmount;
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

	public String getBillName() {
		return billName;
	}

	public void setBillName(String billName) {
		this.billName = billName;
	}

	public Date getReceiptDate() {
		return receiptDate;
	}

	public void setReceiptDate(Date receiptDate) {
		this.receiptDate = receiptDate;
	}

	public String getCreatorId() {
		return creatorId;
	}

	public void setCreatorId(String creatorId) {
		this.creatorId = creatorId;
	}

	public BigDecimal getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(BigDecimal totalPrice) {
		this.totalPrice = totalPrice;
	}

	public String getSellerName() {
		return sellerName;
	}

	public void setSellerName(String sellerName) {
		this.sellerName = sellerName;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}
   
}
