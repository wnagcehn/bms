/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.biz.dispatch.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;

import com.jiuyescm.cfm.domain.IEntity;

/**
 * 
 * @author stevenl
 * 
 */
public class BizDispatchBillUpdateEntity implements IEntity {
    
	/**
	 * 
	 */
	private static final long serialVersionUID = -8808542017069767946L;
	// id
	private int id;
	// 出库单号
	private String outstockNo;
	// 外部单号
	private String externalNo;
	// 运单号
	private String waybillNo;
	// 费用编号
	private String feesNo;
	// WaybillNum
	private BigDecimal waybillNum;
	// 运单列表
	private String waybillList;
	// 仓库id
	private String warehouseCode;
	// 仓库名称
	private String warehouseName;
	// 商家id
	private String customerid;
	// 商家名称
	private String customerName;
	// 物流商ID
	private String carrierId;
	// 物流商名称
	private String carrierName;
	// 宅配商id
	private String deliverid;
	// 宅配商名称
	private String deliverName;
	// TotalWeight
	private BigDecimal totalWeight;
	// AdjustWeight
	private BigDecimal adjustWeight;
	// TotalVolume
	private BigDecimal totalVolume;
	// 服务类型
	private String serviceTypeCode;
	// 配送类型
	private String dispatchTypeCode;
	// CollectMoney
	private BigDecimal collectMoney;
	// 月结账号
	private String monthFeeCount;
	// 寄件人
	private String sendName;
	// 寄件人省份
	private String sendProvinceId;
	// 寄件人城市
	private String sendCityId;
	// 寄件人地区
	private String sendDistrictId;
	// 寄件人街道
	private String sendStreet;
	// 寄件人详细地址
	private String sendDetailAddress;
	// 收件人
	private String receiveName;
	// 收件人省份
	private String receiveProvinceId;
	// 收件人城市
	private String receiveCityId;
	// 收件人地区
	private String receiveDistrictId;
	// 收件人街道
	private String receiveStreet;
	// 收件人详细地址
	private String receiveDetailAddress;
	// 结算状态
	private String accountState;
	// 签收时间
	private Timestamp signTime;
	// 揽收时间
	private Timestamp acceptTime;
	// 收件时间
	private Timestamp receiveTime;
	// 是否已计算费用 0-未结算 1-已结算 2-结算异常 3-合同不存在 4-不在操作范围 5-其他
	private String isCalculated;
	// 创建者
	private String creator;
	// 单据创建时间
	private Timestamp createTime;
	// 修改者
	private String lastModifier;
	// 修改时间
	private Timestamp lastModifyTime;
	// 写入BMS时间
	private Timestamp writeTime;
	// 费用计算时间
	private Timestamp calculateTime;
	// 删除标志
	private String delFlag;
	// 温度类型
	private String temperatureTypeCode;
	// 温度类型名称
	private String temperatureTypeName;
	// remark
	private String remark;
	// 转寄后运单号
	private String zexpressnum;
	// 大状态
	private String bigstatus;
	// 小状态
	private String smallstatus;
	// SystemWeight
	private BigDecimal systemWeight;
	// 抛重
	private BigDecimal throwWeight;
	// totalqty
	private Integer totalqty;
	// 商品明细
	private String productDetail;
	// 快递业务类型
	private String expresstype;
	// sku数
	private Integer skuNum;
	// extattr1
	private String extattr1;
	// extattr2
	private String extattr2;
	// extattr3
	private String extattr3;
	// extattr4
	private String extattr4;
	// extattr5
	private String extattr5;
	// 调整宅配商id
	private String adjustDeliverId;
	// 调整宅配商名称
	private String adjustDeliverName;
	// 调整物流商ID
	private String adjustCarrierId;
	// 调整物流商名称
	private String adjustCarrierName;
	// BizType
	private String bizType;
	// 调整省
	private String adjustProvinceId;
	// 调整市
	private String adjustCityId;
	// 调整区
	private String adjustDistrictId;
	// 订单物流商名称
	private String originCarrierName;
	// 订单物流商ID
	private String originCarrierId;
	// 是否已计算费用
	private String updateType;

	public BizDispatchBillUpdateEntity() {

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
     * WaybillNum
     */
	public BigDecimal getWaybillNum() {
		return this.waybillNum;
	}

    /**
     * WaybillNum
     *
     * @param waybillNum
     */
	public void setWaybillNum(BigDecimal waybillNum) {
		this.waybillNum = waybillNum;
	}
	
	/**
     * 运单列表
     */
	public String getWaybillList() {
		return this.waybillList;
	}

    /**
     * 运单列表
     *
     * @param waybillList
     */
	public void setWaybillList(String waybillList) {
		this.waybillList = waybillList;
	}
	
	/**
     * 仓库id
     */
	public String getWarehouseCode() {
		return this.warehouseCode;
	}

    /**
     * 仓库id
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
     * 商家id
     */
	public String getCustomerid() {
		return this.customerid;
	}

    /**
     * 商家id
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
     * 物流商ID
     */
	public String getCarrierId() {
		return this.carrierId;
	}

    /**
     * 物流商ID
     *
     * @param carrierId
     */
	public void setCarrierId(String carrierId) {
		this.carrierId = carrierId;
	}
	
	/**
     * 物流商名称
     */
	public String getCarrierName() {
		return this.carrierName;
	}

    /**
     * 物流商名称
     *
     * @param carrierName
     */
	public void setCarrierName(String carrierName) {
		this.carrierName = carrierName;
	}
	
	/**
     * 宅配商id
     */
	public String getDeliverid() {
		return this.deliverid;
	}

    /**
     * 宅配商id
     *
     * @param deliverid
     */
	public void setDeliverid(String deliverid) {
		this.deliverid = deliverid;
	}
	
	/**
     * 宅配商名称
     */
	public String getDeliverName() {
		return this.deliverName;
	}

    /**
     * 宅配商名称
     *
     * @param deliverName
     */
	public void setDeliverName(String deliverName) {
		this.deliverName = deliverName;
	}
	
	/**
     * TotalWeight
     */
	public BigDecimal getTotalWeight() {
		return this.totalWeight;
	}

    /**
     * TotalWeight
     *
     * @param totalWeight
     */
	public void setTotalWeight(BigDecimal totalWeight) {
		this.totalWeight = totalWeight;
	}
	
	/**
     * AdjustWeight
     */
	public BigDecimal getAdjustWeight() {
		return this.adjustWeight;
	}

    /**
     * AdjustWeight
     *
     * @param adjustWeight
     */
	public void setAdjustWeight(BigDecimal adjustWeight) {
		this.adjustWeight = adjustWeight;
	}
	
	/**
     * TotalVolume
     */
	public BigDecimal getTotalVolume() {
		return this.totalVolume;
	}

    /**
     * TotalVolume
     *
     * @param totalVolume
     */
	public void setTotalVolume(BigDecimal totalVolume) {
		this.totalVolume = totalVolume;
	}
	
	/**
     * 服务类型
     */
	public String getServiceTypeCode() {
		return this.serviceTypeCode;
	}

    /**
     * 服务类型
     *
     * @param serviceTypeCode
     */
	public void setServiceTypeCode(String serviceTypeCode) {
		this.serviceTypeCode = serviceTypeCode;
	}
	
	/**
     * 配送类型
     */
	public String getDispatchTypeCode() {
		return this.dispatchTypeCode;
	}

    /**
     * 配送类型
     *
     * @param dispatchTypeCode
     */
	public void setDispatchTypeCode(String dispatchTypeCode) {
		this.dispatchTypeCode = dispatchTypeCode;
	}
	
	/**
     * CollectMoney
     */
	public BigDecimal getCollectMoney() {
		return this.collectMoney;
	}

    /**
     * CollectMoney
     *
     * @param collectMoney
     */
	public void setCollectMoney(BigDecimal collectMoney) {
		this.collectMoney = collectMoney;
	}
	
	/**
     * 月结账号
     */
	public String getMonthFeeCount() {
		return this.monthFeeCount;
	}

    /**
     * 月结账号
     *
     * @param monthFeeCount
     */
	public void setMonthFeeCount(String monthFeeCount) {
		this.monthFeeCount = monthFeeCount;
	}
	
	/**
     * 寄件人
     */
	public String getSendName() {
		return this.sendName;
	}

    /**
     * 寄件人
     *
     * @param sendName
     */
	public void setSendName(String sendName) {
		this.sendName = sendName;
	}
	
	/**
     * 寄件人省份
     */
	public String getSendProvinceId() {
		return this.sendProvinceId;
	}

    /**
     * 寄件人省份
     *
     * @param sendProvinceId
     */
	public void setSendProvinceId(String sendProvinceId) {
		this.sendProvinceId = sendProvinceId;
	}
	
	/**
     * 寄件人城市
     */
	public String getSendCityId() {
		return this.sendCityId;
	}

    /**
     * 寄件人城市
     *
     * @param sendCityId
     */
	public void setSendCityId(String sendCityId) {
		this.sendCityId = sendCityId;
	}
	
	/**
     * 寄件人地区
     */
	public String getSendDistrictId() {
		return this.sendDistrictId;
	}

    /**
     * 寄件人地区
     *
     * @param sendDistrictId
     */
	public void setSendDistrictId(String sendDistrictId) {
		this.sendDistrictId = sendDistrictId;
	}
	
	/**
     * 寄件人街道
     */
	public String getSendStreet() {
		return this.sendStreet;
	}

    /**
     * 寄件人街道
     *
     * @param sendStreet
     */
	public void setSendStreet(String sendStreet) {
		this.sendStreet = sendStreet;
	}
	
	/**
     * 寄件人详细地址
     */
	public String getSendDetailAddress() {
		return this.sendDetailAddress;
	}

    /**
     * 寄件人详细地址
     *
     * @param sendDetailAddress
     */
	public void setSendDetailAddress(String sendDetailAddress) {
		this.sendDetailAddress = sendDetailAddress;
	}
	
	/**
     * 收件人
     */
	public String getReceiveName() {
		return this.receiveName;
	}

    /**
     * 收件人
     *
     * @param receiveName
     */
	public void setReceiveName(String receiveName) {
		this.receiveName = receiveName;
	}
	
	/**
     * 收件人省份
     */
	public String getReceiveProvinceId() {
		return this.receiveProvinceId;
	}

    /**
     * 收件人省份
     *
     * @param receiveProvinceId
     */
	public void setReceiveProvinceId(String receiveProvinceId) {
		this.receiveProvinceId = receiveProvinceId;
	}
	
	/**
     * 收件人城市
     */
	public String getReceiveCityId() {
		return this.receiveCityId;
	}

    /**
     * 收件人城市
     *
     * @param receiveCityId
     */
	public void setReceiveCityId(String receiveCityId) {
		this.receiveCityId = receiveCityId;
	}
	
	/**
     * 收件人地区
     */
	public String getReceiveDistrictId() {
		return this.receiveDistrictId;
	}

    /**
     * 收件人地区
     *
     * @param receiveDistrictId
     */
	public void setReceiveDistrictId(String receiveDistrictId) {
		this.receiveDistrictId = receiveDistrictId;
	}
	
	/**
     * 收件人街道
     */
	public String getReceiveStreet() {
		return this.receiveStreet;
	}

    /**
     * 收件人街道
     *
     * @param receiveStreet
     */
	public void setReceiveStreet(String receiveStreet) {
		this.receiveStreet = receiveStreet;
	}
	
	/**
     * 收件人详细地址
     */
	public String getReceiveDetailAddress() {
		return this.receiveDetailAddress;
	}

    /**
     * 收件人详细地址
     *
     * @param receiveDetailAddress
     */
	public void setReceiveDetailAddress(String receiveDetailAddress) {
		this.receiveDetailAddress = receiveDetailAddress;
	}
	
	/**
     * 结算状态
     */
	public String getAccountState() {
		return this.accountState;
	}

    /**
     * 结算状态
     *
     * @param accountState
     */
	public void setAccountState(String accountState) {
		this.accountState = accountState;
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
     * 收件时间
     */
	public Timestamp getReceiveTime() {
		return this.receiveTime;
	}

    /**
     * 收件时间
     *
     * @param receiveTime
     */
	public void setReceiveTime(Timestamp receiveTime) {
		this.receiveTime = receiveTime;
	}
	
	/**
     * 是否已计算费用 0-未结算 1-已结算 2-结算异常 3-合同不存在 4-不在操作范围 5-其他
     */
	public String getIsCalculated() {
		return this.isCalculated;
	}

    /**
     * 是否已计算费用 0-未结算 1-已结算 2-结算异常 3-合同不存在 4-不在操作范围 5-其他
     *
     * @param isCalculated
     */
	public void setIsCalculated(String isCalculated) {
		this.isCalculated = isCalculated;
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
     * 单据创建时间
     */
	public Timestamp getCreateTime() {
		return this.createTime;
	}

    /**
     * 单据创建时间
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
     * 写入BMS时间
     */
	public Timestamp getWriteTime() {
		return this.writeTime;
	}

    /**
     * 写入BMS时间
     *
     * @param writeTime
     */
	public void setWriteTime(Timestamp writeTime) {
		this.writeTime = writeTime;
	}
	
	/**
     * 费用计算时间
     */
	public Timestamp getCalculateTime() {
		return this.calculateTime;
	}

    /**
     * 费用计算时间
     *
     * @param calculateTime
     */
	public void setCalculateTime(Timestamp calculateTime) {
		this.calculateTime = calculateTime;
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
	
	/**
     * 温度类型
     */
	public String getTemperatureTypeCode() {
		return this.temperatureTypeCode;
	}

    /**
     * 温度类型
     *
     * @param temperatureTypeCode
     */
	public void setTemperatureTypeCode(String temperatureTypeCode) {
		this.temperatureTypeCode = temperatureTypeCode;
	}
	
	/**
     * 温度类型名称
     */
	public String getTemperatureTypeName() {
		return this.temperatureTypeName;
	}

    /**
     * 温度类型名称
     *
     * @param temperatureTypeName
     */
	public void setTemperatureTypeName(String temperatureTypeName) {
		this.temperatureTypeName = temperatureTypeName;
	}
	
	/**
     * remark
     */
	public String getRemark() {
		return this.remark;
	}

    /**
     * remark
     *
     * @param remark
     */
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	/**
     * 转寄后运单号
     */
	public String getZexpressnum() {
		return this.zexpressnum;
	}

    /**
     * 转寄后运单号
     *
     * @param zexpressnum
     */
	public void setZexpressnum(String zexpressnum) {
		this.zexpressnum = zexpressnum;
	}
	
	/**
     * 大状态
     */
	public String getBigstatus() {
		return this.bigstatus;
	}

    /**
     * 大状态
     *
     * @param bigstatus
     */
	public void setBigstatus(String bigstatus) {
		this.bigstatus = bigstatus;
	}
	
	/**
     * 小状态
     */
	public String getSmallstatus() {
		return this.smallstatus;
	}

    /**
     * 小状态
     *
     * @param smallstatus
     */
	public void setSmallstatus(String smallstatus) {
		this.smallstatus = smallstatus;
	}
	
	/**
     * SystemWeight
     */
	public BigDecimal getSystemWeight() {
		return this.systemWeight;
	}

    /**
     * SystemWeight
     *
     * @param systemWeight
     */
	public void setSystemWeight(BigDecimal systemWeight) {
		this.systemWeight = systemWeight;
	}
	
	/**
     * 抛重
     */
	public BigDecimal getThrowWeight() {
		return this.throwWeight;
	}

    /**
     * 抛重
     *
     * @param throwWeight
     */
	public void setThrowWeight(BigDecimal throwWeight) {
		this.throwWeight = throwWeight;
	}
	
	/**
     * totalqty
     */
	public Integer getTotalqty() {
		return this.totalqty;
	}

    /**
     * totalqty
     *
     * @param totalqty
     */
	public void setTotalqty(Integer totalqty) {
		this.totalqty = totalqty;
	}
	
	/**
     * 商品明细
     */
	public String getProductDetail() {
		return this.productDetail;
	}

    /**
     * 商品明细
     *
     * @param productDetail
     */
	public void setProductDetail(String productDetail) {
		this.productDetail = productDetail;
	}
	
	/**
     * 快递业务类型
     */
	public String getExpresstype() {
		return this.expresstype;
	}

    /**
     * 快递业务类型
     *
     * @param expresstype
     */
	public void setExpresstype(String expresstype) {
		this.expresstype = expresstype;
	}
	
	/**
     * sku数
     */
	public Integer getSkuNum() {
		return this.skuNum;
	}

    /**
     * sku数
     *
     * @param skuNum
     */
	public void setSkuNum(Integer skuNum) {
		this.skuNum = skuNum;
	}
	
	/**
     * extattr1
     */
	public String getExtattr1() {
		return this.extattr1;
	}

    /**
     * extattr1
     *
     * @param extattr1
     */
	public void setExtattr1(String extattr1) {
		this.extattr1 = extattr1;
	}
	
	/**
     * extattr2
     */
	public String getExtattr2() {
		return this.extattr2;
	}

    /**
     * extattr2
     *
     * @param extattr2
     */
	public void setExtattr2(String extattr2) {
		this.extattr2 = extattr2;
	}
	
	/**
     * extattr3
     */
	public String getExtattr3() {
		return this.extattr3;
	}

    /**
     * extattr3
     *
     * @param extattr3
     */
	public void setExtattr3(String extattr3) {
		this.extattr3 = extattr3;
	}
	
	/**
     * extattr4
     */
	public String getExtattr4() {
		return this.extattr4;
	}

    /**
     * extattr4
     *
     * @param extattr4
     */
	public void setExtattr4(String extattr4) {
		this.extattr4 = extattr4;
	}
	
	/**
     * extattr5
     */
	public String getExtattr5() {
		return this.extattr5;
	}

    /**
     * extattr5
     *
     * @param extattr5
     */
	public void setExtattr5(String extattr5) {
		this.extattr5 = extattr5;
	}
	
	/**
     * 调整宅配商id
     */
	public String getAdjustDeliverId() {
		return this.adjustDeliverId;
	}

    /**
     * 调整宅配商id
     *
     * @param adjustDeliverId
     */
	public void setAdjustDeliverId(String adjustDeliverId) {
		this.adjustDeliverId = adjustDeliverId;
	}
	
	/**
     * 调整宅配商名称
     */
	public String getAdjustDeliverName() {
		return this.adjustDeliverName;
	}

    /**
     * 调整宅配商名称
     *
     * @param adjustDeliverName
     */
	public void setAdjustDeliverName(String adjustDeliverName) {
		this.adjustDeliverName = adjustDeliverName;
	}
	
	/**
     * 调整物流商ID
     */
	public String getAdjustCarrierId() {
		return this.adjustCarrierId;
	}

    /**
     * 调整物流商ID
     *
     * @param adjustCarrierId
     */
	public void setAdjustCarrierId(String adjustCarrierId) {
		this.adjustCarrierId = adjustCarrierId;
	}
	
	/**
     * 调整物流商名称
     */
	public String getAdjustCarrierName() {
		return this.adjustCarrierName;
	}

    /**
     * 调整物流商名称
     *
     * @param adjustCarrierName
     */
	public void setAdjustCarrierName(String adjustCarrierName) {
		this.adjustCarrierName = adjustCarrierName;
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
     * 调整省
     */
	public String getAdjustProvinceId() {
		return this.adjustProvinceId;
	}

    /**
     * 调整省
     *
     * @param adjustProvinceId
     */
	public void setAdjustProvinceId(String adjustProvinceId) {
		this.adjustProvinceId = adjustProvinceId;
	}
	
	/**
     * 调整市
     */
	public String getAdjustCityId() {
		return this.adjustCityId;
	}

    /**
     * 调整市
     *
     * @param adjustCityId
     */
	public void setAdjustCityId(String adjustCityId) {
		this.adjustCityId = adjustCityId;
	}
	
	/**
     * 调整区
     */
	public String getAdjustDistrictId() {
		return this.adjustDistrictId;
	}

    /**
     * 调整区
     *
     * @param adjustDistrictId
     */
	public void setAdjustDistrictId(String adjustDistrictId) {
		this.adjustDistrictId = adjustDistrictId;
	}
	
	/**
     * 订单物流商名称
     */
	public String getOriginCarrierName() {
		return this.originCarrierName;
	}

    /**
     * 订单物流商名称
     *
     * @param originCarrierName
     */
	public void setOriginCarrierName(String originCarrierName) {
		this.originCarrierName = originCarrierName;
	}
	
	/**
     * 订单物流商ID
     */
	public String getOriginCarrierId() {
		return this.originCarrierId;
	}

    /**
     * 订单物流商ID
     *
     * @param originCarrierId
     */
	public void setOriginCarrierId(String originCarrierId) {
		this.originCarrierId = originCarrierId;
	}
	
	/**
     * 是否已计算费用
     */
	public String getUpdateType() {
		return this.updateType;
	}

    /**
     * 是否已计算费用
     *
     * @param updateType
     */
	public void setUpdateType(String updateType) {
		this.updateType = updateType;
	}


	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}
    
}
