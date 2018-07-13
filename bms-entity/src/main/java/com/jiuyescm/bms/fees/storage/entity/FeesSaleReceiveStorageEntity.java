package com.jiuyescm.bms.fees.storage.entity;


import java.math.BigDecimal;
import java.sql.Timestamp;

import com.jiuyescm.cfm.domain.IEntity;

public class FeesSaleReceiveStorageEntity implements IEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3030562682461763618L;
	// 自增主键
	private int id;
	// 费用编号
	private String feesNo;
	// 创建者
	private String creator;
	// 创建时间
	private Timestamp createTime;
	// 修改者
	private String lastModifier;
	// 修改时间
	private Timestamp lastModifyTime;
	// 商家id
	private String customerId;
	// 商家名称
	private String customerName;
	// 仓库编号
	private String warehouseCode;
	// 仓库名称
	private String warehouseName;
	// 单据类型
	private String orderType;
	// 单据编号 (OMS orderno)
	private String orderNo;
	// 商品类别
	private String productType;
	// 费用类别 FEE_TYPE_GENEARL-通用 FEE_TYPE_MATERIAL-耗材 FEE_TYPE_ADD-增值
	private String costType;
	// 费用科目
	private String subjectCode;
	// 仓储增值费编号
	private String otherSubjectCode;
	// 温度类型
	private String tempretureType;
	// 商品编号
	private String productNo;
	// 商品名称
	private String productName;
	// 外部商品编号
	private String externalProductNo;
	// 数量
	private Integer quantity;
	// 单位
	private String unit;
	// weight
	private BigDecimal weight;
	// volume
	private BigDecimal volume;
	// 品种数
	private Integer varieties;
	// UnitPrice
	private BigDecimal unitPrice;
	// ContinuedPrice
	private BigDecimal continuedPrice;
	// cost
	private BigDecimal cost;
	// 数据类型
	private String bizType;
	// 结算状态 0-未结算 1-已结算 2-结算异常 3-其他
	private String isCalculated;
	// 费用计算时间
	private Timestamp calculateTime;
	// param1
	private String param1;
	// param2
	private String param2;
	// param3
	private String param3;
	// param4
	private String param4;
	// param6
	private String param6;
	// 规则编号
	private String ruleNo;
	// 账单编号
	private String billNo;
	// 状态(0 未过账 1已过账)
	private String status;
	// OperateTime
	private Timestamp operateTime;
	// 业务数据主键
	private String bizId;
	// 运单号
	private String waybillNo;
	// DerateAmount
	private BigDecimal derateAmount;
	// 删除标志
	private String delFlag;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getFeesNo() {
		return feesNo;
	}
	public void setFeesNo(String feesNo) {
		this.feesNo = feesNo;
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
	public String getCustomerId() {
		return customerId;
	}
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public String getWarehouseCode() {
		return warehouseCode;
	}
	public void setWarehouseCode(String warehouseCode) {
		this.warehouseCode = warehouseCode;
	}
	public String getWarehouseName() {
		return warehouseName;
	}
	public void setWarehouseName(String warehouseName) {
		this.warehouseName = warehouseName;
	}
	public String getOrderType() {
		return orderType;
	}
	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	public String getProductType() {
		return productType;
	}
	public void setProductType(String productType) {
		this.productType = productType;
	}
	public String getCostType() {
		return costType;
	}
	public void setCostType(String costType) {
		this.costType = costType;
	}
	public String getSubjectCode() {
		return subjectCode;
	}
	public void setSubjectCode(String subjectCode) {
		this.subjectCode = subjectCode;
	}
	public String getOtherSubjectCode() {
		return otherSubjectCode;
	}
	public void setOtherSubjectCode(String otherSubjectCode) {
		this.otherSubjectCode = otherSubjectCode;
	}
	public String getTempretureType() {
		return tempretureType;
	}
	public void setTempretureType(String tempretureType) {
		this.tempretureType = tempretureType;
	}
	public String getProductNo() {
		return productNo;
	}
	public void setProductNo(String productNo) {
		this.productNo = productNo;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getExternalProductNo() {
		return externalProductNo;
	}
	public void setExternalProductNo(String externalProductNo) {
		this.externalProductNo = externalProductNo;
	}
	public Integer getQuantity() {
		return quantity;
	}
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public BigDecimal getWeight() {
		return weight;
	}
	public void setWeight(BigDecimal weight) {
		this.weight = weight;
	}
	public BigDecimal getVolume() {
		return volume;
	}
	public void setVolume(BigDecimal volume) {
		this.volume = volume;
	}
	public Integer getVarieties() {
		return varieties;
	}
	public void setVarieties(Integer varieties) {
		this.varieties = varieties;
	}
	public BigDecimal getUnitPrice() {
		return unitPrice;
	}
	public void setUnitPrice(BigDecimal unitPrice) {
		this.unitPrice = unitPrice;
	}
	public BigDecimal getContinuedPrice() {
		return continuedPrice;
	}
	public void setContinuedPrice(BigDecimal continuedPrice) {
		this.continuedPrice = continuedPrice;
	}
	public BigDecimal getCost() {
		return cost;
	}
	public void setCost(BigDecimal cost) {
		this.cost = cost;
	}
	public String getBizType() {
		return bizType;
	}
	public void setBizType(String bizType) {
		this.bizType = bizType;
	}
	public String getIsCalculated() {
		return isCalculated;
	}
	public void setIsCalculated(String isCalculated) {
		this.isCalculated = isCalculated;
	}
	public Timestamp getCalculateTime() {
		return calculateTime;
	}
	public void setCalculateTime(Timestamp calculateTime) {
		this.calculateTime = calculateTime;
	}
	public String getParam1() {
		return param1;
	}
	public void setParam1(String param1) {
		this.param1 = param1;
	}
	public String getParam2() {
		return param2;
	}
	public void setParam2(String param2) {
		this.param2 = param2;
	}
	public String getParam3() {
		return param3;
	}
	public void setParam3(String param3) {
		this.param3 = param3;
	}
	public String getParam4() {
		return param4;
	}
	public void setParam4(String param4) {
		this.param4 = param4;
	}
	public String getParam6() {
		return param6;
	}
	public void setParam6(String param6) {
		this.param6 = param6;
	}
	public String getRuleNo() {
		return ruleNo;
	}
	public void setRuleNo(String ruleNo) {
		this.ruleNo = ruleNo;
	}
	public String getBillNo() {
		return billNo;
	}
	public void setBillNo(String billNo) {
		this.billNo = billNo;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Timestamp getOperateTime() {
		return operateTime;
	}
	public void setOperateTime(Timestamp operateTime) {
		this.operateTime = operateTime;
	}
	public String getBizId() {
		return bizId;
	}
	public void setBizId(String bizId) {
		this.bizId = bizId;
	}
	public String getWaybillNo() {
		return waybillNo;
	}
	public void setWaybillNo(String waybillNo) {
		this.waybillNo = waybillNo;
	}
	public BigDecimal getDerateAmount() {
		return derateAmount;
	}
	public void setDerateAmount(BigDecimal derateAmount) {
		this.derateAmount = derateAmount;
	}
	public String getDelFlag() {
		return delFlag;
	}
	public void setDelFlag(String delFlag) {
		this.delFlag = delFlag;
	}
}
