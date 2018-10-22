/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.fees.storage.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;

import com.jiuyescm.cfm.domain.IEntity;

/**
 * 
 * @author stevenl
 * 
 */
public class FeesReceiveStorageEntity implements IEntity {

	private static final long serialVersionUID = -1;
	private String projectId;//项目ID
	private String projectName;//项目名称
	
    private Long id;
	// 创建者
	private String creator;
	// 创建时间
	private Timestamp createTime;
	private Timestamp createTimeBegin;
	private Timestamp createTimeEnd;

	// 修改者
	private String lastModifier;
	// 修改时间
	private Timestamp lastModifyTime;
	// 费用编号
	private String feesNo;
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
	// 单据编号
	private String orderNo;
	// 费用类别
	private String costType;
	// 费用科目
	private String subjectCode;
	
	// 仓储增值费编号
	private String otherSubjectCode;

	private String costSubjectTemp;

	// 温度类型
	private String tempretureType;
	// 商品类别
	private String productType;
	// 商品编号
	private String productNo;
	// 商品名称
	private String productName;
	// 外部商品编号
	private String externalProductNo;
	// 数量
	private Integer quantity;
	//调整数量
	private Integer adjustNum;
	// 单位
	private String unit;
	// 重量
	private Double weight;
	// 体积
	private Double volume;
	//箱数
	private Double box;
	// 品种数
	private Integer varieties;
	// 单价
	private Double unitPrice;
	// 续件价格
	private Double continuedPrice;
	// 金额
	private BigDecimal cost;
	// 数据类型
	private String bizType;
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
	// 状态
	private String status;
	// 操作时间
	private Timestamp operateTime;
	//合计值
	private Double totalCost;
	//业务数据主键
	private String bizId;
	
	//运单号
	private String waybillNo;
	
	private Double receiptAmount;//实收金额
    private Double derateAmount;//减免费用
	private String consumerMaterialCode;//耗材编码
	private String consumerMaterialName;//耗材名称
	private String specDesc;//规格说明
	// 删除标志
	private String delFlag;
	// 是否已计算费用 0-未结算 1-已结算 2-结算异常 3-合同不存在 4-不在操作范围 5-其他
	private String isCalculated;
	// 费用计算时间
	private Timestamp calculateTime;
	
	//关联单号
	private String externalNo;
	//服务内容
	private String serviceContent;
	//详细备注
	private String remarkContent;
	
	private String calcuMsg;
	//出库单号
	private String outstockNo;
	
	//入库单号
	private String instockNo;
	//单据类型
	private String instockType;
	//收货确定时间
	private Timestamp instockDate;
	// 首量
	private Double firstNum;
	// 首价
	private Double firstPrice;
	// 续量
	private Double continueNum;
	// 续价
	private Double continuePrice;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getBizId() {
		return bizId;
	}

	public void setBizId(String bizId) {
		this.bizId = bizId;
	}

	public Double getTotalCost() {
		return totalCost;
	}

	public void setTotalCost(Double totalCost) {
		this.totalCost = totalCost;
	}

	public FeesReceiveStorageEntity() {

	}

	/**
	 * 创建者
	 */
	public String getCreator() {
		return this.creator;
	}

	/**
	 * 创建者
	 * 
	 * @param creator
	 */
	public void setCreator(String creator) {
		this.creator = creator;
	}

	/**
	 * 创建时间
	 */
	public Timestamp getCreateTime() {
		return this.createTime;
	}

	/**
	 * 创建时间
	 * 
	 * @param createTime
	 */
	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	/**
	 * 修改者
	 */
	public String getLastModifier() {
		return this.lastModifier;
	}

	/**
	 * 修改者
	 * 
	 * @param lastModifier
	 */
	public void setLastModifier(String lastModifier) {
		this.lastModifier = lastModifier;
	}

	/**
	 * 修改时间
	 */
	public Timestamp getLastModifyTime() {
		return this.lastModifyTime;
	}

	/**
	 * 修改时间
	 * 
	 * @param lastModifyTime
	 */
	public void setLastModifyTime(Timestamp lastModifyTime) {
		this.lastModifyTime = lastModifyTime;
	}

	/**
	 * 费用编号
	 */
	public String getFeesNo() {
		return this.feesNo;
	}

	/**
	 * 费用编号
	 * 
	 * @param feesNo
	 */
	public void setFeesNo(String feesNo) {
		this.feesNo = feesNo;
	}

	/**
	 * 商家id
	 */
	public String getCustomerId() {
		return this.customerId;
	}

	/**
	 * 商家id
	 * 
	 * @param customerId
	 */
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	/**
	 * 商家名称
	 */
	public String getCustomerName() {
		return this.customerName;
	}

	/**
	 * 商家名称
	 * 
	 * @param customerName
	 */
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getWarehouseCode() {
		return warehouseCode;
	}

	public void setWarehouseCode(String warehouseCode) {
		this.warehouseCode = warehouseCode;
	}

	/**
	 * 仓库名称
	 */
	public String getWarehouseName() {
		return this.warehouseName;
	}

	/**
	 * 仓库名称
	 * 
	 * @param warehouseName
	 */
	public void setWarehouseName(String warehouseName) {
		this.warehouseName = warehouseName;
	}

	/**
	 * 单据类型
	 */
	public String getOrderType() {
		return this.orderType;
	}

	/**
	 * 单据类型
	 * 
	 * @param orderType
	 */
	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}

	/**
	 * 单据编号
	 */
	public String getOrderNo() {
		return this.orderNo;
	}

	/**
	 * 单据编号
	 * 
	 * @param orderNo
	 */
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	/**
	 * 费用类别
	 */
	public String getCostType() {
		return this.costType;
	}

	/**
	 * 费用类别
	 * 
	 * @param costType
	 */
	public void setCostType(String costType) {
		this.costType = costType;
	}

	public String getSubjectCode() {
		return subjectCode;
	}

	public void setSubjectCode(String subjectCode) {
		this.subjectCode = subjectCode;
	}

	/**
	 * 温度类型
	 */
	public String getTempretureType() {
		return this.tempretureType;
	}

	/**
	 * 温度类型
	 * 
	 * @param tempretureType
	 */
	public void setTempretureType(String tempretureType) {
		this.tempretureType = tempretureType;
	}

	/**
	 * 商品类别
	 */
	public String getProductType() {
		return this.productType;
	}

	/**
	 * 商品类别
	 * 
	 * @param productType
	 */
	public void setProductType(String productType) {
		this.productType = productType;
	}

	/**
	 * 商品编号
	 */
	public String getProductNo() {
		return this.productNo;
	}

	/**
	 * 商品编号
	 * 
	 * @param productNo
	 */
	public void setProductNo(String productNo) {
		this.productNo = productNo;
	}

	/**
	 * 商品名称
	 */
	public String getProductName() {
		return this.productName;
	}

	/**
	 * 商品名称
	 * 
	 * @param productName
	 */
	public void setProductName(String productName) {
		this.productName = productName;
	}

	/**
	 * 外部商品编号
	 */
	public String getExternalProductNo() {
		return this.externalProductNo;
	}

	/**
	 * 外部商品编号
	 * 
	 * @param externalProductNo
	 */
	public void setExternalProductNo(String externalProductNo) {
		this.externalProductNo = externalProductNo;
	}


	

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	/**
	 * 单位
	 */
	public String getUnit() {
		return this.unit;
	}

	/**
	 * 单位
	 * 
	 * @param unit
	 */
	public void setUnit(String unit) {
		this.unit = unit;
	}

	/**
	 * 重量
	 */
	public Double getWeight() {
		return this.weight;
	}

	/**
	 * 重量
	 * 
	 * @param weight
	 */
	public void setWeight(Double weight) {
		this.weight = weight;
	}

	/**
	 * 体积
	 */
	public Double getVolume() {
		return this.volume;
	}

	/**
	 * 体积
	 * 
	 * @param volume
	 */
	public void setVolume(Double volume) {
		this.volume = volume;
	}

	/**
	 * 品种数
	 */
	public Integer getVarieties() {
		return this.varieties;
	}

	/**
	 * 品种数
	 * 
	 * @param varieties
	 */
	public void setVarieties(Integer varieties) {
		this.varieties = varieties;
	}

	/**
	 * 单价
	 */
	public Double getUnitPrice() {
		return this.unitPrice;
	}

	/**
	 * 单价
	 * 
	 * @param unitPrice
	 */
	public void setUnitPrice(Double unitPrice) {
		this.unitPrice = unitPrice;
	}

	/**
	 * 续件价格
	 */
	public Double getContinuedPrice() {
		return this.continuedPrice;
	}

	/**
	 * 续件价格
	 * 
	 * @param continuedPrice
	 */
	public void setContinuedPrice(Double continuedPrice) {
		this.continuedPrice = continuedPrice;
	}

	/**
	 * 金额
	 */
	public BigDecimal getCost() {
		return this.cost;
	}

	/**
	 * 金额
	 * 
	 * @param cost
	 */
	public void setCost(BigDecimal cost) {
		this.cost = cost;
	}

	/**
	 * param1
	 */
	public String getParam1() {
		return this.param1;
	}

	/**
	 * param1
	 * 
	 * @param param1
	 */
	public void setParam1(String param1) {
		this.param1 = param1;
	}

	/**
	 * param2
	 */
	public String getParam2() {
		return this.param2;
	}

	/**
	 * param2
	 * 
	 * @param param2
	 */
	public void setParam2(String param2) {
		this.param2 = param2;
	}

	/**
	 * param3
	 */
	public String getParam3() {
		return this.param3;
	}

	/**
	 * param3
	 * 
	 * @param param3
	 */
	public void setParam3(String param3) {
		this.param3 = param3;
	}

	/**
	 * param4
	 */
	public String getParam4() {
		return this.param4;
	}

	/**
	 * param4
	 * 
	 * @param param4
	 */
	public void setParam4(String param4) {
		this.param4 = param4;
	}

	/**
	 * param6
	 */
	public String getParam6() {
		return this.param6;
	}

	/**
	 * param6
	 * 
	 * @param param6
	 */
	public void setParam6(String param6) {
		this.param6 = param6;
	}

	/**
	 * 规则编号
	 */
	public String getRuleNo() {
		return this.ruleNo;
	}

	/**
	 * 规则编号
	 * 
	 * @param ruleNo
	 */
	public void setRuleNo(String ruleNo) {
		this.ruleNo = ruleNo;
	}

	/**
	 * 账单编号
	 */
	public String getBillNo() {
		return this.billNo;
	}

	/**
	 * 账单编号
	 * 
	 * @param billNo
	 */
	public void setBillNo(String billNo) {
		this.billNo = billNo;
	}

	/**
	 * 状态
	 */
	public String getStatus() {
		return this.status;
	}

	/**
	 * 状态
	 * 
	 * @param status
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	public String getCostSubjectTemp() {
		return costSubjectTemp;
	}

	public void setCostSubjectTemp(String costSubjectTemp) {
		this.costSubjectTemp = costSubjectTemp;
	}

	public Timestamp getCreateTimeBegin() {
		return createTimeBegin;
	}

	public void setCreateTimeBegin(Timestamp createTimeBegin) {
		this.createTimeBegin = createTimeBegin;
	}

	public Timestamp getCreateTimeEnd() {
		return createTimeEnd;
	}

	public void setCreateTimeEnd(Timestamp createTimeEnd) {
		this.createTimeEnd = createTimeEnd;
	}

	public Timestamp getOperateTime() {
		return operateTime;
	}

	public void setOperateTime(Timestamp operateTime) {
		this.operateTime = operateTime;
	}
	
	public String getOtherSubjectCode() {
		return otherSubjectCode;
	}

	public void setOtherSubjectCode(String otherSubjectCode) {
		this.otherSubjectCode = otherSubjectCode;
	}

	public String getWaybillNo() {
		return waybillNo;
	}

	public void setWaybillNo(String waybillNo) {
		this.waybillNo = waybillNo;
	}

	public String getProjectId() {
		return projectId;
	}

	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getBizType() {
		return bizType;
	}

	public void setBizType(String bizType) {
		this.bizType = bizType;
	}


	public Double getReceiptAmount() {
		return receiptAmount;
	}

	public void setReceiptAmount(Double receiptAmount) {
		this.receiptAmount = receiptAmount;
	}


	public String getConsumerMaterialCode() {
		return consumerMaterialCode;
	}

	public void setConsumerMaterialCode(String consumerMaterialCode) {
		this.consumerMaterialCode = consumerMaterialCode;
	}

	public String getConsumerMaterialName() {
		return consumerMaterialName;
	}

	public void setConsumerMaterialName(String consumerMaterialName) {
		this.consumerMaterialName = consumerMaterialName;
	}

	public String getSpecDesc() {
		return specDesc;
	}

	public void setSpecDesc(String specDesc) {
		this.specDesc = specDesc;
	}

	public Double getDerateAmount() {
		return derateAmount;
	}

	public void setDerateAmount(Double derateAmount) {
		this.derateAmount = derateAmount;
	}

	public String getDelFlag() {
		return delFlag;
	}

	public void setDelFlag(String delFlag) {
		this.delFlag = delFlag;
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

	public String getServiceContent() {
		return serviceContent;
	}

	public void setServiceContent(String serviceContent) {
		this.serviceContent = serviceContent;
	}

	public String getRemarkContent() {
		return remarkContent;
	}

	public void setRemarkContent(String remarkContent) {
		this.remarkContent = remarkContent;
	}

	public String getExternalNo() {
		return externalNo;
	}

	public void setExternalNo(String externalNo) {
		this.externalNo = externalNo;
	}

	public String getCalcuMsg() {
		return calcuMsg;
	}

	public void setCalcuMsg(String calcuMsg) {
		this.calcuMsg = calcuMsg;
	}

	public String getOutstockNo() {
		return outstockNo;
	}

	public void setOutstockNo(String outstockNo) {
		this.outstockNo = outstockNo;
	}

	public Double getBox() {
		return box;
	}

	public void setBox(Double box) {
		this.box = box;
	}

	public String getInstockNo() {
		return instockNo;
	}

	public void setInstockNo(String instockNo) {
		this.instockNo = instockNo;
	}

	public String getInstockType() {
		return instockType;
	}

	public void setInstockType(String instockType) {
		this.instockType = instockType;
	}

	public Timestamp getInstockDate() {
		return instockDate;
	}

	public void setInstockDate(Timestamp instockDate) {
		this.instockDate = instockDate;
	}

	public Integer getAdjustNum() {
		return adjustNum;
	}

	public void setAdjustNum(Integer adjustNum) {
		this.adjustNum = adjustNum;
	}

	public Double getFirstNum() {
		return firstNum;
	}

	public void setFirstNum(Double firstNum) {
		this.firstNum = firstNum;
	}

	public Double getFirstPrice() {
		return firstPrice;
	}

	public void setFirstPrice(Double firstPrice) {
		this.firstPrice = firstPrice;
	}

	public Double getContinueNum() {
		return continueNum;
	}

	public void setContinueNum(Double continueNum) {
		this.continueNum = continueNum;
	}

	public Double getContinuePrice() {
		return continuePrice;
	}

	public void setContinuePrice(Double continuePrice) {
		this.continuePrice = continuePrice;
	}
	
	
}
