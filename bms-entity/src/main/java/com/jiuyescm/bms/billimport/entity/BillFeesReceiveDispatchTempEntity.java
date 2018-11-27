/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.billimport.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;

import com.jiuyescm.cfm.domain.IEntity;

/**
 * 
 * @author stevenl
 * 
 */
public class BillFeesReceiveDispatchTempEntity implements IEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2172015985052553586L;
	// 自增主键
	private Long id;
	// 账单编号
	private String billNo;
	// 费用科目
	private String subjectCode;
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
	private BigDecimal unpackNum;
	// 温度类型
	private String temperatureType;
	// 单据类型
	private String billType;
	// B2B标识
	private String b2bFlag;
	// 发件省
	private String sendProvince;
	// 发件市
	private String sendCity;
	// 发件区
	private String sendDistrict;
	// 收件省
	private String receiveProvince;
	// 收件市
	private String receiveCity;
	// 收件区
	private String receiveDistrict;
	// 物流产品类型
	private String serviceTypeCode;
	// 计费重量
	private BigDecimal totalWeight;
	// 商品总数量
	private BigDecimal totalQty;
	// sku数量
	private BigDecimal totalSku;
	// 金额
	private BigDecimal amount;
	// 折扣
	private BigDecimal derateAmount;
	// 业务时间
	private Timestamp createTime;
	// 业务时间 1810
	private Integer createMonth;
	// 写入bms时间
	private Timestamp writeTime;
	// Excel行号
	private Integer rowExcelNo;
	// Excel列名
	private String rowExcelName;
	// Excel Sheet名称
	private String sheetName;

	public BillFeesReceiveDispatchTempEntity() {

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
     * 物流商id
     */
	public String getCarrierid() {
		return this.carrierid;
	}

    /**
     * 物流商id
     *
     * @param carrierid
     */
	public void setCarrierid(String carrierid) {
		this.carrierid = carrierid;
	}
	
	/**
     * 物流商名称(九曳/顺丰/...) 
     */
	public String getCarrierName() {
		return this.carrierName;
	}

    /**
     * 物流商名称(九曳/顺丰/...) 
     *
     * @param carrierName
     */
	public void setCarrierName(String carrierName) {
		this.carrierName = carrierName;
	}
	
	/**
     * 宅配商id
     */
	public String getDeliveryid() {
		return this.deliveryid;
	}

    /**
     * 宅配商id
     *
     * @param deliveryid
     */
	public void setDeliveryid(String deliveryid) {
		this.deliveryid = deliveryid;
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
	public BigDecimal getUnpackNum() {
		return this.unpackNum;
	}

    /**
     * 拆箱数量
     *
     * @param unpackNum
     */
	public void setUnpackNum(BigDecimal unpackNum) {
		this.unpackNum = unpackNum;
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
     * 发件省
     */
	public String getSendProvince() {
		return this.sendProvince;
	}

    /**
     * 发件省
     *
     * @param sendProvince
     */
	public void setSendProvince(String sendProvince) {
		this.sendProvince = sendProvince;
	}
	
	/**
     * 发件市
     */
	public String getSendCity() {
		return this.sendCity;
	}

    /**
     * 发件市
     *
     * @param sendCity
     */
	public void setSendCity(String sendCity) {
		this.sendCity = sendCity;
	}
	
	/**
     * 发件区
     */
	public String getSendDistrict() {
		return this.sendDistrict;
	}

    /**
     * 发件区
     *
     * @param sendDistrict
     */
	public void setSendDistrict(String sendDistrict) {
		this.sendDistrict = sendDistrict;
	}
	
	/**
     * 收件省
     */
	public String getReceiveProvince() {
		return this.receiveProvince;
	}

    /**
     * 收件省
     *
     * @param receiveProvince
     */
	public void setReceiveProvince(String receiveProvince) {
		this.receiveProvince = receiveProvince;
	}
	
	/**
     * 收件市
     */
	public String getReceiveCity() {
		return this.receiveCity;
	}

    /**
     * 收件市
     *
     * @param receiveCity
     */
	public void setReceiveCity(String receiveCity) {
		this.receiveCity = receiveCity;
	}
	
	/**
     * 收件区
     */
	public String getReceiveDistrict() {
		return this.receiveDistrict;
	}

    /**
     * 收件区
     *
     * @param receiveDistrict
     */
	public void setReceiveDistrict(String receiveDistrict) {
		this.receiveDistrict = receiveDistrict;
	}
	
	/**
     * 物流产品类型
     */
	public String getServiceTypeCode() {
		return this.serviceTypeCode;
	}

    /**
     * 物流产品类型
     *
     * @param serviceTypeCode
     */
	public void setServiceTypeCode(String serviceTypeCode) {
		this.serviceTypeCode = serviceTypeCode;
	}
	
	/**
     * 计费重量
     */
	public BigDecimal getTotalWeight() {
		return this.totalWeight;
	}

    /**
     * 计费重量
     *
     * @param totalWeight
     */
	public void setTotalWeight(BigDecimal totalWeight) {
		this.totalWeight = totalWeight;
	}
	
	/**
     * 商品总数量
     */
	public BigDecimal getTotalQty() {
		return this.totalQty;
	}

    /**
     * 商品总数量
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
	


	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public BigDecimal getDerateAmount() {
		return derateAmount;
	}

	public void setDerateAmount(BigDecimal derateAmount) {
		this.derateAmount = derateAmount;
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
     * 业务时间 1810
     */
	public Integer getCreateMonth() {
		return this.createMonth;
	}

    /**
     * 业务时间 1810
     *
     * @param createMonth
     */
	public void setCreateMonth(Integer createMonth) {
		this.createMonth = createMonth;
	}
	
	/**
     * 写入bms时间
     */
	public Timestamp getWriteTime() {
		return this.writeTime;
	}

    /**
     * 写入bms时间
     *
     * @param writeTime
     */
	public void setWriteTime(Timestamp writeTime) {
		this.writeTime = writeTime;
	}
	
	/**
     * Excel行号
     */
	public Integer getRowExcelNo() {
		return this.rowExcelNo;
	}

    /**
     * Excel行号
     *
     * @param rowExcelNo
     */
	public void setRowExcelNo(Integer rowExcelNo) {
		this.rowExcelNo = rowExcelNo;
	}
	
	/**
     * Excel列名
     */
	public String getRowExcelName() {
		return this.rowExcelName;
	}

    /**
     * Excel列名
     *
     * @param rowExcelName
     */
	public void setRowExcelName(String rowExcelName) {
		this.rowExcelName = rowExcelName;
	}
	
	/**
     * Excel Sheet名称
     */
	public String getSheetName() {
		return this.sheetName;
	}

    /**
     * Excel Sheet名称
     *
     * @param sheetName
     */
	public void setSheetName(String sheetName) {
		this.sheetName = sheetName;
	}
    
}
