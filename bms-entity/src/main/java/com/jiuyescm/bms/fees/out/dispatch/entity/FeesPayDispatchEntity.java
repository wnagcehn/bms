/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.fees.out.dispatch.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;

import com.jiuyescm.cfm.domain.IEntity;

/**
 * 应收费用-配送费实体
 * 
 * @author yangshuaishuai
 * 
 */
public class FeesPayDispatchEntity implements IEntity {

	private static final long serialVersionUID = -950929929443698957L;
	private String projectId;//项目ID
	private String projectName;//项目名称
	// 自增主键
	private Long id;
	// 费用编号
	private String feesNo;
	// 运单号
	private String waybillNo;
	// 出库单号
	private String outstockNo;
	// 外部单号
	private String externalNo;
	// 仓库ID
	private String warehouseCode;
	// 仓库
	private String warehouseName;
	// 商家ID
	private String customerid;
	// 商家名称
	private String customerName;
	// 物流商id
	private String carrierid;
	// 物流商名称(九曳/顺丰/...)
	private String carrierName;
	// 宅配商id
	private String deliveryid;
	// 宅配商名称
	private String deliverName;
	// 拆箱标识 0-未拆箱 1-已拆箱
	private String unpacking;
	// 拆箱数量
	private Long unpackNum;
	// 操作时间
	private Timestamp operateTime;
	// 温度类型
	private String temperatureType;
	// 单据类型
	private String billType;
	// B2B标识
	private String b2bFlag;
	// 总重量
	private Double totalWeight;
	// 总数量
	private Double totalQuantity;
	// 总品种数
	private Double totalVarieties;
	// 是否撤单
	private String splitSingle;
	// 模板编号
	private String templateId;
	// 报价编号
	private String priceId;
	// 金额
	private Double amount;
	// 账单编号
	private String billNo;
	// 规则编号
	private String ruleNo;
	// 目的省
	private String toProvinceName;
	// 目的区县
	private String toDistrictName;
	// 目的市
	private String toCityName;
	// 计费重量
	private Double chargedWeight;
	// 重量界限
	private Double weightLimit;
	// 单价
	private Double unitPrice;
	// 首重重量
	private Double headWeight;
	// 续重重量
	private Double continuedWeight;
	// 首重价格
	private Double headPrice;
	// 续重价格
	private Double continuedPrice;
	// 揽收时间
	private Timestamp acceptTime;
	// 签收时间
	private Timestamp signTime;
	// 大状态
	private String bigstatus;
	// 小状态
	private String smallstatus;
	// 费用类别（区分是配送费还是配送增值费）
	private String subjectType;
	// 费用科目
	private String subjectCode;
	// 其他费用科目
	private String otherSubjectCode;
	// 数据类型
	private String bizType;
	// 参数1
	private String param1;
	// 参数2
	private String param2;
	// 参数3
	private String param3;
	// 参数4
	private String param4;
	// 参数5
	private String param5;
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
	
	//合计值
	private Double totalCost;
	//减免金额
	private BigDecimal derateAmount;
	
	private String isCalculated;

	public FeesPayDispatchEntity() {

	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public String getOutstockNo() {
		return outstockNo;
	}

	public void setOutstockNo(String outstockNo) {
		this.outstockNo = outstockNo;
	}

	public String getExternalNo() {
		return externalNo;
	}

	public void setExternalNo(String externalNo) {
		this.externalNo = externalNo;
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

	public String getCustomerid() {
		return customerid;
	}

	public void setCustomerid(String customerid) {
		this.customerid = customerid;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
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

	public String getDeliverName() {
		return deliverName;
	}

	public void setDeliverName(String deliverName) {
		this.deliverName = deliverName;
	}

	public String getUnpacking() {
		return unpacking;
	}

	public void setUnpacking(String unpacking) {
		this.unpacking = unpacking;
	}

	public Long getUnpackNum() {
		return unpackNum;
	}

	public void setUnpackNum(Long unpackNum) {
		this.unpackNum = unpackNum;
	}

	public Timestamp getOperateTime() {
		return operateTime;
	}

	public void setOperateTime(Timestamp operateTime) {
		this.operateTime = operateTime;
	}

	public String getTemperatureType() {
		return temperatureType;
	}

	public void setTemperatureType(String temperatureType) {
		this.temperatureType = temperatureType;
	}

	public String getBillType() {
		return billType;
	}

	public void setBillType(String billType) {
		this.billType = billType;
	}

	public String getB2bFlag() {
		return b2bFlag;
	}

	public void setB2bFlag(String b2bFlag) {
		this.b2bFlag = b2bFlag;
	}

	public Double getTotalWeight() {
		return totalWeight;
	}

	public void setTotalWeight(Double totalWeight) {
		this.totalWeight = totalWeight;
	}

	public Double getTotalQuantity() {
		return totalQuantity;
	}

	public void setTotalQuantity(Double totalQuantity) {
		this.totalQuantity = totalQuantity;
	}

	public Double getTotalVarieties() {
		return totalVarieties;
	}

	public void setTotalVarieties(Double totalVarieties) {
		this.totalVarieties = totalVarieties;
	}

	public String getSplitSingle() {
		return splitSingle;
	}

	public void setSplitSingle(String splitSingle) {
		this.splitSingle = splitSingle;
	}

	public String getTemplateId() {
		return templateId;
	}

	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}

	public String getPriceId() {
		return priceId;
	}

	public void setPriceId(String priceId) {
		this.priceId = priceId;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public String getBillNo() {
		return billNo;
	}

	public void setBillNo(String billNo) {
		this.billNo = billNo;
	}

	public String getRuleNo() {
		return ruleNo;
	}

	public void setRuleNo(String ruleNo) {
		this.ruleNo = ruleNo;
	}

	public String getToProvinceName() {
		return toProvinceName;
	}

	public void setToProvinceName(String toProvinceName) {
		this.toProvinceName = toProvinceName;
	}

	public String getToDistrictName() {
		return toDistrictName;
	}

	public void setToDistrictName(String toDistrictName) {
		this.toDistrictName = toDistrictName;
	}

	public String getToCityName() {
		return toCityName;
	}

	public void setToCityName(String toCityName) {
		this.toCityName = toCityName;
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

	public Double getHeadWeight() {
		return headWeight;
	}

	public void setHeadWeight(Double headWeight) {
		this.headWeight = headWeight;
	}

	public Double getContinuedWeight() {
		return continuedWeight;
	}

	public void setContinuedWeight(Double continuedWeight) {
		this.continuedWeight = continuedWeight;
	}

	public Double getHeadPrice() {
		return headPrice;
	}

	public void setHeadPrice(Double headPrice) {
		this.headPrice = headPrice;
	}

	public Double getContinuedPrice() {
		return continuedPrice;
	}

	public void setContinuedPrice(Double continuedPrice) {
		this.continuedPrice = continuedPrice;
	}

	public Timestamp getAcceptTime() {
		return acceptTime;
	}

	public void setAcceptTime(Timestamp acceptTime) {
		this.acceptTime = acceptTime;
	}

	public Timestamp getSignTime() {
		return signTime;
	}

	public void setSignTime(Timestamp signTime) {
		this.signTime = signTime;
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

	public String getParam5() {
		return param5;
	}

	public void setParam5(String param5) {
		this.param5 = param5;
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

	public Double getTotalCost() {
		return totalCost;
	}

	public void setTotalCost(Double totalCost) {
		this.totalCost = totalCost;
	}

	public String getSubjectType() {
		return subjectType;
	}

	public void setSubjectType(String subjectType) {
		this.subjectType = subjectType;
	}

	public String getSubjectCode() {
		return subjectCode;
	}

	public void setSubjectCode(String subjectCode) {
		this.subjectCode = subjectCode;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getProjectId() {
		return projectId;
	}

	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}

	public String getBigstatus() {
		return bigstatus;
	}

	public void setBigstatus(String bigstatus) {
		this.bigstatus = bigstatus;
	}

	public String getSmallstatus() {
		return smallstatus;
	}

	public void setSmallstatus(String smallstatus) {
		this.smallstatus = smallstatus;
	}

	public String getBizType() {
		return bizType;
	}

	public void setBizType(String bizType) {
		this.bizType = bizType;
	}

	public String getOtherSubjectCode() {
		return otherSubjectCode;
	}

	public void setOtherSubjectCode(String otherSubjectCode) {
		this.otherSubjectCode = otherSubjectCode;
	}

	public BigDecimal getDerateAmount() {
		return derateAmount;
	}

	public void setDerateAmount(BigDecimal derateAmount) {
		this.derateAmount = derateAmount;
	}

	public String getIsCalculated() {
		return isCalculated;
	}

	public void setIsCalculated(String isCalculated) {
		this.isCalculated = isCalculated;
	}
	
}
