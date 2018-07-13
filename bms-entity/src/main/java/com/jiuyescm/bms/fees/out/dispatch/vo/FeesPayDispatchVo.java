/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.fees.out.dispatch.vo;

import java.sql.Timestamp;

import com.jiuyescm.cfm.domain.IEntity;

/**
 * 
 * @author stevenl
 * 
 */
public class FeesPayDispatchVo implements IEntity {

	private static final long serialVersionUID = 7245058435703922153L;
	
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
	// 宅配商 纯配送业务
	private String deliveryid;
	// 物流商(九曳/顺丰/...) 
	private String carrierid;
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
	// 目的市
	private String toCityName;
	// 目的区县
	private String toDistrictName;
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

	public FeesPayDispatchVo() {

	}
	
	/**
     * 自增主键
     */
	public Long getId() {
		return this.id;
	}

    /**
     * 自增主键
     *
     * @param id
     */
	public void setId(Long id) {
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
     * 运单号
     */
	public String getWaybillNo() {
		return this.waybillNo;
	}

    /**
     * 运单号
     *
     * @param waybillNo
     */
	public void setWaybillNo(String waybillNo) {
		this.waybillNo = waybillNo;
	}
	
	/**
     * 出库单号
     */
	public String getOutstockNo() {
		return this.outstockNo;
	}

    /**
     * 出库单号
     *
     * @param outstockNo
     */
	public void setOutstockNo(String outstockNo) {
		this.outstockNo = outstockNo;
	}
	
	/**
     * 外部单号
     */
	public String getExternalNo() {
		return this.externalNo;
	}

    /**
     * 外部单号
     *
     * @param externalNo
     */
	public void setExternalNo(String externalNo) {
		this.externalNo = externalNo;
	}
	
	/**
     * 仓库ID
     */
	public String getWarehouseCode() {
		return this.warehouseCode;
	}

    /**
     * 仓库ID
     *
     * @param warehouseCode
     */
	public void setWarehouseCode(String warehouseCode) {
		this.warehouseCode = warehouseCode;
	}
	
	/**
     * 仓库
     */
	public String getWarehouseName() {
		return this.warehouseName;
	}

    /**
     * 仓库
     *
     * @param warehouseName
     */
	public void setWarehouseName(String warehouseName) {
		this.warehouseName = warehouseName;
	}
	
	/**
     * 商家ID
     */
	public String getCustomerid() {
		return this.customerid;
	}

    /**
     * 商家ID
     *
     * @param customerid
     */
	public void setCustomerid(String customerid) {
		this.customerid = customerid;
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
     * 宅配商 纯配送业务
     */
	public String getDeliveryid() {
		return this.deliveryid;
	}

    /**
     * 宅配商 纯配送业务
     *
     * @param deliveryid
     */
	public void setDeliveryid(String deliveryid) {
		this.deliveryid = deliveryid;
	}
	
	/**
     * 物流商(九曳/顺丰/...) 
     */
	public String getCarrierid() {
		return this.carrierid;
	}

    /**
     * 物流商(九曳/顺丰/...) 
     *
     * @param carrierid
     */
	public void setCarrierid(String carrierid) {
		this.carrierid = carrierid;
	}
	
	/**
     * 拆箱标识 0-未拆箱 1-已拆箱
     */
	public String getUnpacking() {
		return this.unpacking;
	}

    /**
     * 拆箱标识 0-未拆箱 1-已拆箱
     *
     * @param unpacking
     */
	public void setUnpacking(String unpacking) {
		this.unpacking = unpacking;
	}
	
	/**
     * 拆箱数量
     */
	public Long getUnpackNum() {
		return this.unpackNum;
	}

    /**
     * 拆箱数量
     *
     * @param unpackNum
     */
	public void setUnpackNum(Long unpackNum) {
		this.unpackNum = unpackNum;
	}
	
	/**
     * 操作时间
     */
	public Timestamp getOperateTime() {
		return this.operateTime;
	}

    /**
     * 操作时间
     *
     * @param operateTime
     */
	public void setOperateTime(Timestamp operateTime) {
		this.operateTime = operateTime;
	}
	
	/**
     * 温度类型
     */
	public String getTemperatureType() {
		return this.temperatureType;
	}

    /**
     * 温度类型
     *
     * @param temperatureType
     */
	public void setTemperatureType(String temperatureType) {
		this.temperatureType = temperatureType;
	}
	
	/**
     * 单据类型
     */
	public String getBillType() {
		return this.billType;
	}

    /**
     * 单据类型
     *
     * @param billType
     */
	public void setBillType(String billType) {
		this.billType = billType;
	}
	
	/**
     * B2B标识
     */
	public String getB2bFlag() {
		return this.b2bFlag;
	}

    /**
     * B2B标识
     *
     * @param b2bFlag
     */
	public void setB2bFlag(String b2bFlag) {
		this.b2bFlag = b2bFlag;
	}
	
	/**
     * 总重量
     */
	public Double getTotalWeight() {
		return this.totalWeight;
	}

    /**
     * 总重量
     *
     * @param totalWeight
     */
	public void setTotalWeight(Double totalWeight) {
		this.totalWeight = totalWeight;
	}
	
	/**
     * 总数量
     */
	public Double getTotalQuantity() {
		return this.totalQuantity;
	}

    /**
     * 总数量
     *
     * @param totalQuantity
     */
	public void setTotalQuantity(Double totalQuantity) {
		this.totalQuantity = totalQuantity;
	}
	
	/**
     * 总品种数
     */
	public Double getTotalVarieties() {
		return this.totalVarieties;
	}

    /**
     * 总品种数
     *
     * @param totalVarieties
     */
	public void setTotalVarieties(Double totalVarieties) {
		this.totalVarieties = totalVarieties;
	}
	
	/**
     * 是否撤单
     */
	public String getSplitSingle() {
		return this.splitSingle;
	}

    /**
     * 是否撤单
     *
     * @param splitSingle
     */
	public void setSplitSingle(String splitSingle) {
		this.splitSingle = splitSingle;
	}
	
	/**
     * 模板编号
     */
	public String getTemplateId() {
		return this.templateId;
	}

    /**
     * 模板编号
     *
     * @param templateId
     */
	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}
	
	/**
     * 报价编号
     */
	public String getPriceId() {
		return this.priceId;
	}

    /**
     * 报价编号
     *
     * @param priceId
     */
	public void setPriceId(String priceId) {
		this.priceId = priceId;
	}
	
	/**
     * 金额
     */
	public Double getAmount() {
		return this.amount;
	}

    /**
     * 金额
     *
     * @param amount
     */
	public void setAmount(Double amount) {
		this.amount = amount;
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
     * 目的省
     */
	public String getToProvinceName() {
		return this.toProvinceName;
	}

    /**
     * 目的省
     *
     * @param toProvinceName
     */
	public void setToProvinceName(String toProvinceName) {
		this.toProvinceName = toProvinceName;
	}
	
	/**
     * 目的市
     */
	public String getToCityName() {
		return this.toCityName;
	}

    /**
     * 目的市
     *
     * @param toCityName
     */
	public void setToCityName(String toCityName) {
		this.toCityName = toCityName;
	}
	
	/**
     * 目的区县
     */
	public String getToDistrictName() {
		return this.toDistrictName;
	}

    /**
     * 目的区县
     *
     * @param toDistrictName
     */
	public void setToDistrictName(String toDistrictName) {
		this.toDistrictName = toDistrictName;
	}
	
	/**
     * 计费重量
     */
	public Double getChargedWeight() {
		return this.chargedWeight;
	}

    /**
     * 计费重量
     *
     * @param chargedWeight
     */
	public void setChargedWeight(Double chargedWeight) {
		this.chargedWeight = chargedWeight;
	}
	
	/**
     * 重量界限
     */
	public Double getWeightLimit() {
		return this.weightLimit;
	}

    /**
     * 重量界限
     *
     * @param weightLimit
     */
	public void setWeightLimit(Double weightLimit) {
		this.weightLimit = weightLimit;
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
     * 首重重量
     */
	public Double getHeadWeight() {
		return this.headWeight;
	}

    /**
     * 首重重量
     *
     * @param headWeight
     */
	public void setHeadWeight(Double headWeight) {
		this.headWeight = headWeight;
	}
	
	/**
     * 续重重量
     */
	public Double getContinuedWeight() {
		return this.continuedWeight;
	}

    /**
     * 续重重量
     *
     * @param continuedWeight
     */
	public void setContinuedWeight(Double continuedWeight) {
		this.continuedWeight = continuedWeight;
	}
	
	/**
     * 首重价格
     */
	public Double getHeadPrice() {
		return this.headPrice;
	}

    /**
     * 首重价格
     *
     * @param headPrice
     */
	public void setHeadPrice(Double headPrice) {
		this.headPrice = headPrice;
	}
	
	/**
     * 续重价格
     */
	public Double getContinuedPrice() {
		return this.continuedPrice;
	}

    /**
     * 续重价格
     *
     * @param continuedPrice
     */
	public void setContinuedPrice(Double continuedPrice) {
		this.continuedPrice = continuedPrice;
	}
	
	/**
     * 揽收时间
     */
	public Timestamp getAcceptTime() {
		return this.acceptTime;
	}

    /**
     * 揽收时间
     *
     * @param acceptTime
     */
	public void setAcceptTime(Timestamp acceptTime) {
		this.acceptTime = acceptTime;
	}
	
	/**
     * 签收时间
     */
	public Timestamp getSignTime() {
		return this.signTime;
	}

    /**
     * 签收时间
     *
     * @param signTime
     */
	public void setSignTime(Timestamp signTime) {
		this.signTime = signTime;
	}
	
	/**
     * 参数1
     */
	public String getParam1() {
		return this.param1;
	}

    /**
     * 参数1
     *
     * @param param1
     */
	public void setParam1(String param1) {
		this.param1 = param1;
	}
	
	/**
     * 参数2
     */
	public String getParam2() {
		return this.param2;
	}

    /**
     * 参数2
     *
     * @param param2
     */
	public void setParam2(String param2) {
		this.param2 = param2;
	}
	
	/**
     * 参数3
     */
	public String getParam3() {
		return this.param3;
	}

    /**
     * 参数3
     *
     * @param param3
     */
	public void setParam3(String param3) {
		this.param3 = param3;
	}
	
	/**
     * 参数4
     */
	public String getParam4() {
		return this.param4;
	}

    /**
     * 参数4
     *
     * @param param4
     */
	public void setParam4(String param4) {
		this.param4 = param4;
	}
	
	/**
     * 参数5
     */
	public String getParam5() {
		return this.param5;
	}

    /**
     * 参数5
     *
     * @param param5
     */
	public void setParam5(String param5) {
		this.param5 = param5;
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
