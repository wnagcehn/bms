/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.correct;

import java.math.BigDecimal;

import com.jiuyescm.cfm.domain.IEntity;

/**
 * 
 * @author stevenl
 * 
 */
public class BmsMarkingProductsEntity implements IEntity {

    // TODO Change serialVersionUID by eclipse
    private static final long serialVersionUID = -1;
    
	// 自增标识
	private Long id;
	// 运单号
	private String waybillNo;
	// 运单商品明细id
	private String productsMark;
	// 进位重量
	private BigDecimal weight;
	// 纠正重量
	private BigDecimal correctWeight;
	//泡沫箱纸箱mark
	private String pmxzxMark;
	//保温袋mark
	private String bwdMark;
	//耗材类别 JY,SH,WH
	private String materialType;
	
	
	// 运单表字段   出库单号
	private String outstockNo;
	// 外部单号
	private String externalNo;
	// 费用编号
	private String feesNo;
	// WaybillNum
	private Double waybillNum;
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
	
	private String consumerMaterialCode;
	private String consumerMaterialName;

	public BmsMarkingProductsEntity() {

	}
	
	/**
     * 自增标识
     */
	public Long getId() {
		return this.id;
	}

    /**
     * 自增标识
     *
     * @param id
     */
	public void setId(Long id) {
		this.id = id;
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
     * 运单商品明细id
     */
	public String getProductsMark() {
		return this.productsMark;
	}

    /**
     * 运单商品明细id
     *
     * @param productsMark
     */
	public void setProductsMark(String productsMark) {
		this.productsMark = productsMark;
	}
	
	/**
     * 进位重量
     */
	public BigDecimal getWeight() {
		return this.weight;
	}

    /**
     * 进位重量
     *
     * @param weight
     */
	public void setWeight(BigDecimal weight) {
		this.weight = weight;
	}

	public BigDecimal getCorrectWeight() {
		return correctWeight;
	}

	public void setCorrectWeight(BigDecimal correctWeight) {
		this.correctWeight = correctWeight;
	}

	public String getPmxzxMark() {
		return pmxzxMark;
	}

	public void setPmxzxMark(String pmxzxMark) {
		this.pmxzxMark = pmxzxMark;
	}

	public String getBwdMark() {
		return bwdMark;
	}

	public void setBwdMark(String bwdMark) {
		this.bwdMark = bwdMark;
	}

	public String getMaterialType() {
		return materialType;
	}

	public void setMaterialType(String materialType) {
		this.materialType = materialType;
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

	public String getFeesNo() {
		return feesNo;
	}

	public void setFeesNo(String feesNo) {
		this.feesNo = feesNo;
	}

	public Double getWaybillNum() {
		return waybillNum;
	}

	public void setWaybillNum(Double waybillNum) {
		this.waybillNum = waybillNum;
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

	public String getDeliverid() {
		return deliverid;
	}

	public void setDeliverid(String deliverid) {
		this.deliverid = deliverid;
	}

	public String getDeliverName() {
		return deliverName;
	}

	public void setDeliverName(String deliverName) {
		this.deliverName = deliverName;
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
	
    
}
