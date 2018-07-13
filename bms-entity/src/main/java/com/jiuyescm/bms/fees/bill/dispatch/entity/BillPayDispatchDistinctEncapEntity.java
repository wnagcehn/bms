package com.jiuyescm.bms.fees.bill.dispatch.entity;


/**
 * 应付账单宅配对账差异封装实体
 * @author yangshuaishuai
 */
public class BillPayDispatchDistinctEncapEntity extends
		BillPayDispatchDistinctEntity {

	private static final long serialVersionUID = -5606857542222261065L;
	// 账单差异id
	private Long boddid;
	// 费用编号
	private String feeNo;
	// 费用-宅配商id
	private String feeDeliveryId;
	// 费用-宅配商名称
	private String feeDeliveryName;
	// 费用-运单号
	private String feeWayBillNo;
	// 费用-总数量
	private Double feeTotalQuantity;
	// 费用-金额
	private Double feeAmount;
	// 费用-计费重量
	private Double feeChargedWeight;
	// 新增 费用-总重量
	private Double feeTotalWeight;
	// 费用-首重重量
	private Double feeHeadWeight;
	// 费用-首重价格
	private Double feeHeadPrice;
	// 费用-续重重量
	private Double feeContinuedWeight;
	// 费用-续重价格
	private Double feeContinuedPrice;
	// 费用-重量界限
	private Double feeWeightLimit;
	// 费用-单价
	private Double feeUnitPrice;

	public Long getBoddid() {
		return boddid;
	}

	public void setBoddid(Long boddid) {
		this.boddid = boddid;
	}

	public String getFeeNo() {
		return feeNo;
	}

	public void setFeeNo(String feeNo) {
		this.feeNo = feeNo;
	}

	public String getFeeDeliveryId() {
		return feeDeliveryId;
	}

	public void setFeeDeliveryId(String feeDeliveryId) {
		this.feeDeliveryId = feeDeliveryId;
	}

	public String getFeeDeliveryName() {
		return feeDeliveryName;
	}

	public void setFeeDeliveryName(String feeDeliveryName) {
		this.feeDeliveryName = feeDeliveryName;
	}

	public String getFeeWayBillNo() {
		return feeWayBillNo;
	}

	public void setFeeWayBillNo(String feeWayBillNo) {
		this.feeWayBillNo = feeWayBillNo;
	}

	public Double getFeeTotalQuantity() {
		return feeTotalQuantity;
	}

	public void setFeeTotalQuantity(Double feeTotalQuantity) {
		this.feeTotalQuantity = feeTotalQuantity;
	}

	public Double getFeeAmount() {
		return feeAmount;
	}

	public void setFeeAmount(Double feeAmount) {
		this.feeAmount = feeAmount;
	}

	public Double getFeeChargedWeight() {
		return feeChargedWeight;
	}

	public void setFeeChargedWeight(Double feeChargedWeight) {
		this.feeChargedWeight = feeChargedWeight;
	}

	public Double getFeeTotalWeight() {
		return feeTotalWeight;
	}

	public void setFeeTotalWeight(Double feeTotalWeight) {
		this.feeTotalWeight = feeTotalWeight;
	}

	public Double getFeeHeadWeight() {
		return feeHeadWeight;
	}

	public void setFeeHeadWeight(Double feeHeadWeight) {
		this.feeHeadWeight = feeHeadWeight;
	}

	public Double getFeeHeadPrice() {
		return feeHeadPrice;
	}

	public void setFeeHeadPrice(Double feeHeadPrice) {
		this.feeHeadPrice = feeHeadPrice;
	}

	public Double getFeeContinuedWeight() {
		return feeContinuedWeight;
	}

	public void setFeeContinuedWeight(Double feeContinuedWeight) {
		this.feeContinuedWeight = feeContinuedWeight;
	}

	public Double getFeeContinuedPrice() {
		return feeContinuedPrice;
	}

	public void setFeeContinuedPrice(Double feeContinuedPrice) {
		this.feeContinuedPrice = feeContinuedPrice;
	}

	public Double getFeeWeightLimit() {
		return feeWeightLimit;
	}

	public void setFeeWeightLimit(Double feeWeightLimit) {
		this.feeWeightLimit = feeWeightLimit;
	}

	public Double getFeeUnitPrice() {
		return feeUnitPrice;
	}

	public void setFeeUnitPrice(Double feeUnitPrice) {
		this.feeUnitPrice = feeUnitPrice;
	}

}
