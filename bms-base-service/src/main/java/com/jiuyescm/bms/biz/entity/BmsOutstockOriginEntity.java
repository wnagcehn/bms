/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.biz.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;

import com.jiuyescm.cfm.domain.IEntity;

/**
 * 
 * @author stevenl
 * 
 */
public class BmsOutstockOriginEntity implements IEntity {

    // TODO Change serialVersionUID by eclipse
    private static final long serialVersionUID = -1;
    
	// 自增主键
	private Long id;
	// 出库单号
	private String outstockNo;
	// 运单号
	private String waybillNo;
	// 转寄后运单号
	private String zexpressnum;
	// 外部单号
	private String externalNo;
	// 仓库编号
	private String warehouseCode;
	// 仓库名称
	private String warehouseName;
	// 商家编号
	private String customerId;
	// 商家名称
	private String customerName;
	// 物流商编号
	private String carrierId;
	// 物流商名称
	private String carrierName;
	// 宅配商编号
	private String deliverId;
	// 宅配商名称
	private String deliverName;
	// 温度类型
	private String temperatureTypeCode;
	// 是否拆箱
	private String splitboxFlag;
	// B2B标识
	private String b2bFlag;
	// 月结账号
	private String monthFeeCount;
	// 商品明细
	private Long productDetail;
	// 服务类型
	private String serviceTypeCode;
	// 单据类型编码 销售出库单,退货入库单...
	private String billTypeCode;
	// 系统重量/逻辑重量
	private BigDecimal systemWeight;
	// 称重重量
	private BigDecimal originWeight;
	// 订单重量
	private BigDecimal totalWeight;
	// 订单抛重
	private BigDecimal throwWeight;
	// 商品数量
	private BigDecimal totalQty;
	// sku数量
	private BigDecimal totalSku;
	// 箱数
	private BigDecimal totalBox;
	// 体积
	private BigDecimal totalVolume;
	// 寄件人
	private String sendName;
	// 寄件人省份
	private String sendProvince;
	// 寄件人城市
	private String sendCity;
	// 寄件人地区
	private String sendArea;
	// 寄件人详细地址
	private String sendAddress;
	// 收件人
	private String receiveName;
	// 收件人省份
	private String receiveProvince;
	// 收件人城市
	private String receiveCity;
	// 收件人地区
	private String receiveArea;
	// 收件人详细地址
	private String receiveAddress;
	// 商品明细标
	private String productsMark;
	// 打标重量
	private BigDecimal weightMark;
	// 出库时间
	private Timestamp outingTime;
	// 业务时间
	private Timestamp createTime;
	// 写入BMS时间
	private Timestamp writeTime;

	public BmsOutstockOriginEntity() {

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
     * 商家编号
     */
	public String getCustomerId() {
		return this.customerId;
	}

    /**
     * 商家编号
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
     * 物流商编号
     */
	public String getCarrierId() {
		return this.carrierId;
	}

    /**
     * 物流商编号
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
     * 宅配商编号
     */
	public String getDeliverId() {
		return this.deliverId;
	}

    /**
     * 宅配商编号
     *
     * @param deliverId
     */
	public void setDeliverId(String deliverId) {
		this.deliverId = deliverId;
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
     * 是否拆箱
     */
	public String getSplitboxFlag() {
		return this.splitboxFlag;
	}

    /**
     * 是否拆箱
     *
     * @param splitboxFlag
     */
	public void setSplitboxFlag(String splitboxFlag) {
		this.splitboxFlag = splitboxFlag;
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
     * 商品明细
     */
	public Long getProductDetail() {
		return this.productDetail;
	}

    /**
     * 商品明细
     *
     * @param productDetail
     */
	public void setProductDetail(Long productDetail) {
		this.productDetail = productDetail;
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
     * 单据类型编码 销售出库单,退货入库单...
     */
	public String getBillTypeCode() {
		return this.billTypeCode;
	}

    /**
     * 单据类型编码 销售出库单,退货入库单...
     *
     * @param billTypeCode
     */
	public void setBillTypeCode(String billTypeCode) {
		this.billTypeCode = billTypeCode;
	}
	
	/**
     * 系统重量/逻辑重量
     */
	public BigDecimal getSystemWeight() {
		return this.systemWeight;
	}

    /**
     * 系统重量/逻辑重量
     *
     * @param systemWeight
     */
	public void setSystemWeight(BigDecimal systemWeight) {
		this.systemWeight = systemWeight;
	}
	
	/**
     * 称重重量
     */
	public BigDecimal getOriginWeight() {
		return this.originWeight;
	}

    /**
     * 称重重量
     *
     * @param originWeight
     */
	public void setOriginWeight(BigDecimal originWeight) {
		this.originWeight = originWeight;
	}
	
	/**
     * 订单重量
     */
	public BigDecimal getTotalWeight() {
		return this.totalWeight;
	}

    /**
     * 订单重量
     *
     * @param totalWeight
     */
	public void setTotalWeight(BigDecimal totalWeight) {
		this.totalWeight = totalWeight;
	}
	
	/**
     * 订单抛重
     */
	public BigDecimal getThrowWeight() {
		return this.throwWeight;
	}

    /**
     * 订单抛重
     *
     * @param throwWeight
     */
	public void setThrowWeight(BigDecimal throwWeight) {
		this.throwWeight = throwWeight;
	}
	
	/**
     * 商品数量
     */
	public BigDecimal getTotalQty() {
		return this.totalQty;
	}

    /**
     * 商品数量
     *
     * @param totalQty
     */
	public void setTotalQty(BigDecimal totalQty) {
		this.totalQty = totalQty;
	}
	
	/**
     * sku数量
     */
	public BigDecimal getTotalSku() {
		return this.totalSku;
	}

    /**
     * sku数量
     *
     * @param totalSku
     */
	public void setTotalSku(BigDecimal totalSku) {
		this.totalSku = totalSku;
	}
	
	/**
     * 箱数
     */
	public BigDecimal getTotalBox() {
		return this.totalBox;
	}

    /**
     * 箱数
     *
     * @param totalBox
     */
	public void setTotalBox(BigDecimal totalBox) {
		this.totalBox = totalBox;
	}
	
	/**
     * 体积
     */
	public BigDecimal getTotalVolume() {
		return this.totalVolume;
	}

    /**
     * 体积
     *
     * @param totalVolume
     */
	public void setTotalVolume(BigDecimal totalVolume) {
		this.totalVolume = totalVolume;
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
	public String getSendProvince() {
		return this.sendProvince;
	}

    /**
     * 寄件人省份
     *
     * @param sendProvince
     */
	public void setSendProvince(String sendProvince) {
		this.sendProvince = sendProvince;
	}
	
	/**
     * 寄件人城市
     */
	public String getSendCity() {
		return this.sendCity;
	}

    /**
     * 寄件人城市
     *
     * @param sendCity
     */
	public void setSendCity(String sendCity) {
		this.sendCity = sendCity;
	}
	
	/**
     * 寄件人地区
     */
	public String getSendArea() {
		return this.sendArea;
	}

    /**
     * 寄件人地区
     *
     * @param sendArea
     */
	public void setSendArea(String sendArea) {
		this.sendArea = sendArea;
	}
	
	/**
     * 寄件人详细地址
     */
	public String getSendAddress() {
		return this.sendAddress;
	}

    /**
     * 寄件人详细地址
     *
     * @param sendAddress
     */
	public void setSendAddress(String sendAddress) {
		this.sendAddress = sendAddress;
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
	public String getReceiveProvince() {
		return this.receiveProvince;
	}

    /**
     * 收件人省份
     *
     * @param receiveProvince
     */
	public void setReceiveProvince(String receiveProvince) {
		this.receiveProvince = receiveProvince;
	}
	
	/**
     * 收件人城市
     */
	public String getReceiveCity() {
		return this.receiveCity;
	}

    /**
     * 收件人城市
     *
     * @param receiveCity
     */
	public void setReceiveCity(String receiveCity) {
		this.receiveCity = receiveCity;
	}
	
	/**
     * 收件人地区
     */
	public String getReceiveArea() {
		return this.receiveArea;
	}

    /**
     * 收件人地区
     *
     * @param receiveArea
     */
	public void setReceiveArea(String receiveArea) {
		this.receiveArea = receiveArea;
	}
	
	/**
     * 收件人详细地址
     */
	public String getReceiveAddress() {
		return this.receiveAddress;
	}

    /**
     * 收件人详细地址
     *
     * @param receiveAddress
     */
	public void setReceiveAddress(String receiveAddress) {
		this.receiveAddress = receiveAddress;
	}
	
	/**
     * 商品明细标
     */
	public String getProductsMark() {
		return this.productsMark;
	}

    /**
     * 商品明细标
     *
     * @param productsMark
     */
	public void setProductsMark(String productsMark) {
		this.productsMark = productsMark;
	}
	
	/**
     * 打标重量
     */
	public BigDecimal getWeightMark() {
		return this.weightMark;
	}

    /**
     * 打标重量
     *
     * @param weightMark
     */
	public void setWeightMark(BigDecimal weightMark) {
		this.weightMark = weightMark;
	}
	
	/**
     * 出库时间
     */
	public Timestamp getOutingTime() {
		return this.outingTime;
	}

    /**
     * 出库时间
     *
     * @param outingTime
     */
	public void setOutingTime(Timestamp outingTime) {
		this.outingTime = outingTime;
	}
	
	/**
     * 业务时间
     */
	public Timestamp getCreateTime() {
		return this.createTime;
	}

    /**
     * 业务时间
     *
     * @param createTime
     */
	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
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
    
}
