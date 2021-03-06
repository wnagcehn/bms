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
public class FeesPayStorageEntity implements IEntity {

    
	/**
	 * 
	 */
	private static final long serialVersionUID = -6201254678378391515L;
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
	// CalculateTime
	private Timestamp calculateTime;
	// IsCalculated
	private String isCalculated;
	// BizType
	private String bizType;
	// cost
	private BigDecimal cost;
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
	// WaybillNo
	private String waybillNo;
	// DerateAmount
	private BigDecimal derateAmount;
	// 删除标志
	private String delFlag;

	public FeesPayStorageEntity() {

	}
	
	/**
     * 自增主键
     */
	public int getId() {
		return this.id;
	}

    /**
     * 自增主键
     *
     * @param id
     */
	public void setId(int id) {
		this.id = id;
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
	
	/**
     * 仓库编号
     */
	public String getWarehouseCode() {
		return this.warehouseCode;
	}

    /**
     * 仓库编号
     *
     * @param warehouseCode
     */
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
     * 单据编号 (OMS orderno)
     */
	public String getOrderNo() {
		return this.orderNo;
	}

    /**
     * 单据编号 (OMS orderno)
     *
     * @param orderNo
     */
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
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
     * 费用类别 FEE_TYPE_GENEARL-通用 FEE_TYPE_MATERIAL-耗材 FEE_TYPE_ADD-增值
     */
	public String getCostType() {
		return this.costType;
	}

    /**
     * 费用类别 FEE_TYPE_GENEARL-通用 FEE_TYPE_MATERIAL-耗材 FEE_TYPE_ADD-增值
     *
     * @param costType
     */
	public void setCostType(String costType) {
		this.costType = costType;
	}
	
	/**
     * 费用科目
     */
	public String getSubjectCode() {
		return this.subjectCode;
	}

    /**
     * 费用科目
     *
     * @param subjectCode
     */
	public void setSubjectCode(String subjectCode) {
		this.subjectCode = subjectCode;
	}
	
	/**
     * 仓储增值费编号
     */
	public String getOtherSubjectCode() {
		return this.otherSubjectCode;
	}

    /**
     * 仓储增值费编号
     *
     * @param otherSubjectCode
     */
	public void setOtherSubjectCode(String otherSubjectCode) {
		this.otherSubjectCode = otherSubjectCode;
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
	
	/**
     * 数量
     */
	public Integer getQuantity() {
		return this.quantity;
	}

    /**
     * 数量
     *
     * @param quantity
     */
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
     * weight
     */
	public BigDecimal getWeight() {
		return this.weight;
	}

    /**
     * weight
     *
     * @param weight
     */
	public void setWeight(BigDecimal weight) {
		this.weight = weight;
	}
	
	/**
     * volume
     */
	public BigDecimal getVolume() {
		return this.volume;
	}

    /**
     * volume
     *
     * @param volume
     */
	public void setVolume(BigDecimal volume) {
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
     * UnitPrice
     */
	public BigDecimal getUnitPrice() {
		return this.unitPrice;
	}

    /**
     * UnitPrice
     *
     * @param unitPrice
     */
	public void setUnitPrice(BigDecimal unitPrice) {
		this.unitPrice = unitPrice;
	}
	
	/**
     * ContinuedPrice
     */
	public BigDecimal getContinuedPrice() {
		return this.continuedPrice;
	}

    /**
     * ContinuedPrice
     *
     * @param continuedPrice
     */
	public void setContinuedPrice(BigDecimal continuedPrice) {
		this.continuedPrice = continuedPrice;
	}
	
	/**
     * CalculateTime
     */
	public Timestamp getCalculateTime() {
		return this.calculateTime;
	}

    /**
     * CalculateTime
     *
     * @param calculateTime
     */
	public void setCalculateTime(Timestamp calculateTime) {
		this.calculateTime = calculateTime;
	}
	
	/**
     * IsCalculated
     */
	public String getIsCalculated() {
		return this.isCalculated;
	}

    /**
     * IsCalculated
     *
     * @param isCalculated
     */
	public void setIsCalculated(String isCalculated) {
		this.isCalculated = isCalculated;
	}
	
	/**
     * BizType
     */
	public String getBizType() {
		return this.bizType;
	}

    /**
     * BizType
     *
     * @param bizType
     */
	public void setBizType(String bizType) {
		this.bizType = bizType;
	}
	
	/**
     * cost
     */
	public BigDecimal getCost() {
		return this.cost;
	}

    /**
     * cost
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
     * 状态(0 未过账 1已过账)
     */
	public String getStatus() {
		return this.status;
	}

    /**
     * 状态(0 未过账 1已过账)
     *
     * @param status
     */
	public void setStatus(String status) {
		this.status = status;
	}
	
	/**
     * OperateTime
     */
	public Timestamp getOperateTime() {
		return this.operateTime;
	}

    /**
     * OperateTime
     *
     * @param operateTime
     */
	public void setOperateTime(Timestamp operateTime) {
		this.operateTime = operateTime;
	}
	
	/**
     * 业务数据主键
     */
	public String getBizId() {
		return this.bizId;
	}

    /**
     * 业务数据主键
     *
     * @param bizId
     */
	public void setBizId(String bizId) {
		this.bizId = bizId;
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
     * DerateAmount
     */
	public BigDecimal getDerateAmount() {
		return this.derateAmount;
	}

    /**
     * DerateAmount
     *
     * @param derateAmount
     */
	public void setDerateAmount(BigDecimal derateAmount) {
		this.derateAmount = derateAmount;
	}
	
	/**
     * 删除标志
     */
	public String getDelFlag() {
		return this.delFlag;
	}

    /**
     * 删除标志
     *
     * @param delFlag
     */
	public void setDelFlag(String delFlag) {
		this.delFlag = delFlag;
	}
    
}
