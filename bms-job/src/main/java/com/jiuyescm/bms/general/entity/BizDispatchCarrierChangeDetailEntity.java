package com.jiuyescm.bms.general.entity;

import java.sql.Timestamp;
import com.jiuyescm.cfm.domain.IEntity;

 /**
 * ..Entity
 * @author wangchen
 * 
 */
public class BizDispatchCarrierChangeDetailEntity implements IEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 878108915090911248L;
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
	// 处理状态  0-未处理 1-已处理 2-处理失败
	private String isCalculated;
	// 处理时间
	private Timestamp calculateTime;
	// 计算描述
	private String descript;
	// 创建时间
	private Timestamp createTime;

	public BizDispatchCarrierChangeDetailEntity() {
		super();
	}
	
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public String getWarehouseCode() {
		return this.warehouseCode;
	}

	public void setWarehouseCode(String warehouseCode) {
		this.warehouseCode = warehouseCode;
	}
	
	public String getCustomerid() {
		return this.customerid;
	}

	public void setCustomerid(String customerid) {
		this.customerid = customerid;
	}
	
	public String getOldCarrierId() {
		return this.oldCarrierId;
	}

	public void setOldCarrierId(String oldCarrierId) {
		this.oldCarrierId = oldCarrierId;
	}
	
	public String getNewCarrierId() {
		return this.newCarrierId;
	}

	public void setNewCarrierId(String newCarrierId) {
		this.newCarrierId = newCarrierId;
	}
	
	public String getWaybillNo() {
		return this.waybillNo;
	}

	public void setWaybillNo(String waybillNo) {
		this.waybillNo = waybillNo;
	}
	
	public String getUpdateReasonTypeCode() {
		return this.updateReasonTypeCode;
	}

	public void setUpdateReasonTypeCode(String updateReasonTypeCode) {
		this.updateReasonTypeCode = updateReasonTypeCode;
	}
	
	public String getUpdateReasonType() {
		return this.updateReasonType;
	}

	public void setUpdateReasonType(String updateReasonType) {
		this.updateReasonType = updateReasonType;
	}
	
	public String getUpdateReasonDetailCode() {
		return this.updateReasonDetailCode;
	}

	public void setUpdateReasonDetailCode(String updateReasonDetailCode) {
		this.updateReasonDetailCode = updateReasonDetailCode;
	}
	
	public String getUpdateReasonDetail() {
		return this.updateReasonDetail;
	}

	public void setUpdateReasonDetail(String updateReasonDetail) {
		this.updateReasonDetail = updateReasonDetail;
	}
	
	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	public Timestamp getWriteTime() {
		return this.writeTime;
	}

	public void setWriteTime(Timestamp writeTime) {
		this.writeTime = writeTime;
	}
	
	public String getDutyType() {
		return this.dutyType;
	}

	public void setDutyType(String dutyType) {
		this.dutyType = dutyType;
	}
	
	public String getOldUpdateReason() {
		return this.oldUpdateReason;
	}

	public void setOldUpdateReason(String oldUpdateReason) {
		this.oldUpdateReason = oldUpdateReason;
	}
	
	public String getIsCalculated() {
		return this.isCalculated;
	}

	public void setIsCalculated(String isCalculated) {
		this.isCalculated = isCalculated;
	}
	
	public Timestamp getCalculateTime() {
		return this.calculateTime;
	}

	public void setCalculateTime(Timestamp calculateTime) {
		this.calculateTime = calculateTime;
	}
	
	public String getDescript() {
		return this.descript;
	}

	public void setDescript(String descript) {
		this.descript = descript;
	}
	
	public Timestamp getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}
    
}
