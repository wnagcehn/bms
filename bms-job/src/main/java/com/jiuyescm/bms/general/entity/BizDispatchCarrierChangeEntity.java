/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.general.entity;
import java.sql.Timestamp;

import com.jiuyescm.cfm.domain.IEntity;

/**
 * 
 * @author stevenl
 * 
 */
public class BizDispatchCarrierChangeEntity implements IEntity {

    // TODO Change serialVersionUID by eclipse
    private static final long serialVersionUID = -1;
    
	// id
	private Long id;
	// WarehouseCode
	private String warehouseCode;
	// customerid
	private String customerid;
	// OldCarrierId
	private String oldCarrierId;
	// NewCarrierId
	private String newCarrierId;
	// WaybillNo
	private String waybillNo;
	// UpdateReasonTypeCode
	private String updateReasonTypeCode;
	// UpdateReasonType
	private String updateReasonType;
	// UpdateReasonDetailCode
	private String updateReasonDetailCode;
	// UpdateReasonDetail
	private String updateReasonDetail;
	// remark
	private String remark;
	// WriteTime
	private Timestamp writeTime;
	// 责任方
	private String dutyType;
	// 原修改原因
	private String oldUpdateReason;

	public BizDispatchCarrierChangeEntity() {

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
     * WarehouseCode
     */
	public String getWarehouseCode() {
		return this.warehouseCode;
	}

    /**
     * WarehouseCode
     *
     * @param warehouseCode
     */
	public void setWarehouseCode(String warehouseCode) {
		this.warehouseCode = warehouseCode;
	}
	
	/**
     * customerid
     */
	public String getCustomerid() {
		return this.customerid;
	}

    /**
     * customerid
     *
     * @param customerid
     */
	public void setCustomerid(String customerid) {
		this.customerid = customerid;
	}
	
	/**
     * OldCarrierId
     */
	public String getOldCarrierId() {
		return this.oldCarrierId;
	}

    /**
     * OldCarrierId
     *
     * @param oldCarrierId
     */
	public void setOldCarrierId(String oldCarrierId) {
		this.oldCarrierId = oldCarrierId;
	}
	
	/**
     * NewCarrierId
     */
	public String getNewCarrierId() {
		return this.newCarrierId;
	}

    /**
     * NewCarrierId
     *
     * @param newCarrierId
     */
	public void setNewCarrierId(String newCarrierId) {
		this.newCarrierId = newCarrierId;
	}
	
	/**
     * WaybillNo
     */
	public String getWaybillNo() {
		return this.waybillNo;
	}

    /**
     * WaybillNo
     *
     * @param waybillNo
     */
	public void setWaybillNo(String waybillNo) {
		this.waybillNo = waybillNo;
	}
	
	/**
     * UpdateReasonTypeCode
     */
	public String getUpdateReasonTypeCode() {
		return this.updateReasonTypeCode;
	}

    /**
     * UpdateReasonTypeCode
     *
     * @param updateReasonTypeCode
     */
	public void setUpdateReasonTypeCode(String updateReasonTypeCode) {
		this.updateReasonTypeCode = updateReasonTypeCode;
	}
	
	/**
     * UpdateReasonType
     */
	public String getUpdateReasonType() {
		return this.updateReasonType;
	}

    /**
     * UpdateReasonType
     *
     * @param updateReasonType
     */
	public void setUpdateReasonType(String updateReasonType) {
		this.updateReasonType = updateReasonType;
	}
	
	/**
     * UpdateReasonDetailCode
     */
	public String getUpdateReasonDetailCode() {
		return this.updateReasonDetailCode;
	}

    /**
     * UpdateReasonDetailCode
     *
     * @param updateReasonDetailCode
     */
	public void setUpdateReasonDetailCode(String updateReasonDetailCode) {
		this.updateReasonDetailCode = updateReasonDetailCode;
	}
	
	/**
     * UpdateReasonDetail
     */
	public String getUpdateReasonDetail() {
		return this.updateReasonDetail;
	}

    /**
     * UpdateReasonDetail
     *
     * @param updateReasonDetail
     */
	public void setUpdateReasonDetail(String updateReasonDetail) {
		this.updateReasonDetail = updateReasonDetail;
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
     * WriteTime
     */
	public Timestamp getWriteTime() {
		return this.writeTime;
	}

    /**
     * WriteTime
     *
     * @param writeTime
     */
	public void setWriteTime(Timestamp writeTime) {
		this.writeTime = writeTime;
	}
	
	/**
     * 责任方
     */
	public String getDutyType() {
		return this.dutyType;
	}

    /**
     * 责任方
     *
     * @param dutyType
     */
	public void setDutyType(String dutyType) {
		this.dutyType = dutyType;
	}

	public String getOldUpdateReason() {
		return oldUpdateReason;
	}

	public void setOldUpdateReason(String oldUpdateReason) {
		this.oldUpdateReason = oldUpdateReason;
	}
    
	
}
