package com.jiuyescm.bms.billcheck.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

import com.jiuyescm.cfm.domain.IEntity;

public class BillInvoiceEntity implements IEntity{
	 // TODO Change serialVersionUID by eclipse
    private static final long serialVersionUID = -1;
    
	// id
	private Long id;
	// 对账主表id
	private Integer billCheckId;
	// 发票序号
	private Integer sortNo;
	// 发票编号
	private String invoiceNo;
	// InvoiceAmount
	private BigDecimal invoiceAmount;
	// InvoiceDate
	private Date invoiceDate;
	// 快递单号
	private String waybillNo;
	// InvoiceDays
	private Integer invoiceDays;
	// remark
	private String remark;
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

	
	//开票名称
	private String invoiceName;
	//业务月份
	private int createMonth;
	//账单名称
	private String billName;
	//销售员
	private String sellerName;
	//区域
	private String area;
	//合计金额
	private BigDecimal totalPrice;
	
	public BillInvoiceEntity() {

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
	
	/**
     * 发票序号
     */
	public Integer getSortNo() {
		return this.sortNo;
	}

    /**
     * 发票序号
     *
     * @param sortNo
     */
	public void setSortNo(Integer sortNo) {
		this.sortNo = sortNo;
	}
	
	/**
     * 发票编号
     */
	public String getInvoiceNo() {
		return this.invoiceNo;
	}

    /**
     * 发票编号
     *
     * @param invoiceNo
     */
	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}
	
	/**
     * InvoiceAmount
     */
	public BigDecimal getInvoiceAmount() {
		return this.invoiceAmount;
	}

    /**
     * InvoiceAmount
     *
     * @param invoiceAmount
     */
	public void setInvoiceAmount(BigDecimal invoiceAmount) {
		this.invoiceAmount = invoiceAmount;
	}
	

	
	public Date getInvoiceDate() {
		return invoiceDate;
	}

	public void setInvoiceDate(Date invoiceDate) {
		this.invoiceDate = invoiceDate;
	}

	/**
     * 快递单号
     */
	public String getWaybillNo() {
		return this.waybillNo;
	}

    /**
     * 快递单号
     *
     * @param waybillNo
     */
	public void setWaybillNo(String waybillNo) {
		this.waybillNo = waybillNo;
	}
	
	/**
     * InvoiceDays
     */
	public Integer getInvoiceDays() {
		return this.invoiceDays;
	}

    /**
     * InvoiceDays
     *
     * @param invoiceDays
     */
	public void setInvoiceDays(Integer invoiceDays) {
		this.invoiceDays = invoiceDays;
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
