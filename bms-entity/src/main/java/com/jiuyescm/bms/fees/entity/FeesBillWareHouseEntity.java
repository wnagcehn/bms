package com.jiuyescm.bms.fees.entity;

import com.jiuyescm.cfm.domain.IEntity;

public class FeesBillWareHouseEntity implements IEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3814557463286294393L;

	private String warehouseCode;// 仓库ID
	private String warehouseName;//仓库名称
	private double amount;// 总金额
	private double discountAmount;//折扣金额
	private double derateAmount;//减免金额
	private double receiveAmount;//应收金额
	private String billNo;// 账单号
	/**
	 * 物流商 配送
	 */
	private String carrierid;
	private String carrierName;
	/**
	 * 承运商 配送
	 */
	private String forwarderId;
	private String forwarderName;
	/**
	 * 宅配商 配送
	 */
	private String deliveryid;
	private String deliveryName;
	/**
	 * 费用科目 运输
	 */
	private String subjectCode;
	private String otherSubjectCode;
	private String subjectName;
	/**
	 * 客诉原因Id 异常
	 */
	private String reasonId;
	/**
	 * 客诉原因 异常
	 */
	private String reason;
	// 费用编号
	private String feesNo;
	// 运单号
	private String waybillNo;

	// 费用类型
	private String subjectType;
	

	public String getWarehouseCode() {
		return warehouseCode;
	}

	public void setWarehouseCode(String warehouseCode) {
		this.warehouseCode = warehouseCode;
	}

	public String getBillNo() {
		return billNo;
	}

	public void setBillNo(String billNo) {
		this.billNo = billNo;
	}

	public String getCarrierid() {
		return carrierid;
	}

	public void setCarrierid(String carrierid) {
		this.carrierid = carrierid;
	}

	public String getCarrierName() {
		return carrierName;
	}

	public void setCarrierName(String carrierName) {
		this.carrierName = carrierName;
	}

	public String getDeliveryid() {
		return deliveryid;
	}

	public void setDeliveryid(String deliveryid) {
		this.deliveryid = deliveryid;
	}

	public String getDeliveryName() {
		return deliveryName;
	}

	public void setDeliveryName(String deliveryName) {
		this.deliveryName = deliveryName;
	}

	public String getSubjectCode() {
		return subjectCode;
	}

	public void setSubjectCode(String subjectCode) {
		this.subjectCode = subjectCode;
	}

	public String getReasonId() {
		return reasonId;
	}

	public void setReasonId(String reasonId) {
		this.reasonId = reasonId;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getFeesNo() {
		return feesNo;
	}

	public void setFeesNo(String feesNo) {
		this.feesNo = feesNo;
	}

	public String getWaybillNo() {
		return waybillNo;
	}

	public void setWaybillNo(String waybillNo) {
		this.waybillNo = waybillNo;
	}

	public String getSubjectType() {
		return subjectType;
	}

	public void setSubjectType(String subjectType) {
		this.subjectType = subjectType;
	}

	public String getForwarderId() {
		return forwarderId;
	}

	public void setForwarderId(String forwarderId) {
		this.forwarderId = forwarderId;
	}

	public String getForwarderName() {
		return forwarderName;
	}

	public void setForwarderName(String forwarderName) {
		this.forwarderName = forwarderName;
	}

	public String getWarehouseName() {
		return warehouseName;
	}

	public void setWarehouseName(String warehouseName) {
		this.warehouseName = warehouseName;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public double getDiscountAmount() {
		return discountAmount;
	}

	public void setDiscountAmount(double discountAmount) {
		this.discountAmount = discountAmount;
	}

	public double getDerateAmount() {
		return derateAmount;
	}

	public void setDerateAmount(double derateAmount) {
		this.derateAmount = derateAmount;
	}

	public double getReceiveAmount() {
		return receiveAmount;
	}

	public void setReceiveAmount(double receiveAmount) {
		this.receiveAmount = receiveAmount;
	}

	public String getOtherSubjectCode() {
		return otherSubjectCode;
	}

	public void setOtherSubjectCode(String otherSubjectCode) {
		this.otherSubjectCode = otherSubjectCode;
	}

	public String getSubjectName() {
		return subjectName;
	}

	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}
	
	
}
