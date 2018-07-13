package com.jiuyescm.bms.fees.bill.dispatch.entity;

/**
 * 应收账单-宅配对账封装实体
 * 
 * @author yangshuaishuai
 */
public class BillDispatchCompareEntity extends BillDispatchDistinctEntity {

	private static final long serialVersionUID = 7295757737702029598L;
	// id
	private Long boddid;
	// 物流商id
	private String carrierId;
	// 物流商
	private String carrierName;
	// 运单号
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

	public String getFeeWayBillNo() {
		return feeWayBillNo;
	}

	public void setFeeWayBillNo(String feeWayBillNo) {
		this.feeWayBillNo = feeWayBillNo;
	}

	public Long getBoddid() {
		return boddid;
	}

	public void setBoddid(Long boddid) {
		this.boddid = boddid;
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

	public Double getFeeTotalWeight() {
		return feeTotalWeight;
	}

	public void setFeeTotalWeight(Double feeTotalWeight) {
		this.feeTotalWeight = feeTotalWeight;
	}

	public String getCarrierId() {
		return carrierId;
	}

	public void setCarrierId(String carrierId) {
		this.carrierId = carrierId;
	}

	public String getCarrierName() {
		return carrierName;
	}

	public void setCarrierName(String carrierName) {
		this.carrierName = carrierName;
	}
	
}
