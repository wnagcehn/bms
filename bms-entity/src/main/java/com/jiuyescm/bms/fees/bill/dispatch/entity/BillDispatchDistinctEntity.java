package com.jiuyescm.bms.fees.bill.dispatch.entity;

import java.sql.Timestamp;

import com.jiuyescm.cfm.domain.IEntity;

/**
 * 应收账单-宅配对账差异实体类
 * 
 * @author yangshuaishuai
 */
public class BillDispatchDistinctEntity implements IEntity {

	private static final long serialVersionUID = 9141507089712822687L;

	// id
	private Long id;
	// 运单号
	private String waybillNo;
	// 宅配商
	private String deliveryid;
	// 费用编号
	private String feesNo;
	// 账单编号
	private String billNo;
	// 首重重量
	private Double headWeight;
	// 首重价格
	private Double headPrice;
	// 续重重量
	private Double continuedWeight;
	// 续重价格
	private Double continuedPrice;
	// 计费重量
	private Double chargedWeight;
	// 总重量
	private Double totalWeight;
	// 重量界限
	private Double weightLimit;
	// 单价
	private Double unitPrice;
	// 金额
	private Double amount;
	// 差额
	private Double diffAmount;
	// 状态
	private String status;
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

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getWaybillNo() {
		return waybillNo;
	}

	public void setWaybillNo(String waybillNo) {
		this.waybillNo = waybillNo;
	}

	public String getDeliveryid() {
		return deliveryid;
	}

	public void setDeliveryid(String deliveryid) {
		this.deliveryid = deliveryid;
	}

	public String getFeesNo() {
		return feesNo;
	}

	public void setFeesNo(String feesNo) {
		this.feesNo = feesNo;
	}

	public Double getHeadWeight() {
		return headWeight;
	}

	public void setHeadWeight(Double headWeight) {
		this.headWeight = headWeight;
	}

	public Double getHeadPrice() {
		return headPrice;
	}

	public void setHeadPrice(Double headPrice) {
		this.headPrice = headPrice;
	}

	public Double getContinuedWeight() {
		return continuedWeight;
	}

	public void setContinuedWeight(Double continuedWeight) {
		this.continuedWeight = continuedWeight;
	}

	public Double getContinuedPrice() {
		return continuedPrice;
	}

	public void setContinuedPrice(Double continuedPrice) {
		this.continuedPrice = continuedPrice;
	}

	public Double getChargedWeight() {
		return chargedWeight;
	}

	public void setChargedWeight(Double chargedWeight) {
		this.chargedWeight = chargedWeight;
	}

	public Double getWeightLimit() {
		return weightLimit;
	}

	public void setWeightLimit(Double weightLimit) {
		this.weightLimit = weightLimit;
	}

	public Double getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(Double unitPrice) {
		this.unitPrice = unitPrice;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public Double getDiffAmount() {
		return diffAmount;
	}

	public void setDiffAmount(Double diffAmount) {
		this.diffAmount = diffAmount;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	public String getLastModifier() {
		return lastModifier;
	}

	public void setLastModifier(String lastModifier) {
		this.lastModifier = lastModifier;
	}

	public Timestamp getLastModifyTime() {
		return lastModifyTime;
	}

	public void setLastModifyTime(Timestamp lastModifyTime) {
		this.lastModifyTime = lastModifyTime;
	}

	public String getDelFlag() {
		return delFlag;
	}

	public void setDelFlag(String delFlag) {
		this.delFlag = delFlag;
	}

	public String getBillNo() {
		return billNo;
	}

	public void setBillNo(String billNo) {
		this.billNo = billNo;
	}

	public Double getTotalWeight() {
		return totalWeight;
	}

	public void setTotalWeight(Double totalWeight) {
		this.totalWeight = totalWeight;
	}

	
}
