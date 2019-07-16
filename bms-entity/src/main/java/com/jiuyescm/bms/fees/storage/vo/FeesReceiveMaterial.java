/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.fees.storage.vo;

import java.sql.Timestamp;

import com.jiuyescm.cfm.domain.IEntity;

/**
 * 
 * @author stevenl
 * 
 */
public class FeesReceiveMaterial implements IEntity {

	private static final long serialVersionUID = 6670974846240880462L;
	
	

	private String customerId;
	
	private String customerName;
	
	private String warehouseCode;
	
	private String warehouseName;
	
	private String productType;
	
	private String productNo;
	
	private String productName;
	
	private String externalProductNo;
	
	private Integer quantity;
	
	private String unit;
	
	private Double weight;
	
	private Double volume;
	
	private Integer varieties;
	
	private Double unitPrice;
	
	private Double continuedPrice;
	
	private Double cost;
	
	private String receiveName;
	
	private String receiveProvinceId;
	
	private String receiveCityId;
	
	private String receiveDetailAddress;
	
	private String waybillNo;
	
	private String zexpressnum;
	
	private Timestamp createTime;
	
	private String carrierName;
	
	private Double systemWeight;

	private Integer totalqty;

	private String productDetail;
	
	private String externalNo;
	
	private String outstockNo;
	private String specDesc;
	
	private String isCalculated;
	
	//包材方案编号，名称，包材组编号
	private String packPlanNo;
	private String packPlanName;
	private Double packPlanCost;
	private String packGroupNo;
	
	public FeesReceiveMaterial(){
		
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

	public String getProductType() {
		return productType;
	}

	public void setProductType(String productType) {
		this.productType = productType;
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

	public Double getWeight() {
		return weight;
	}

	public void setWeight(Double weight) {
		this.weight = weight;
	}

	public Double getVolume() {
		return volume;
	}

	public void setVolume(Double volume) {
		this.volume = volume;
	}

	public Integer getVarieties() {
		return varieties;
	}

	public void setVarieties(Integer varieties) {
		this.varieties = varieties;
	}

	public Double getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(Double unitPrice) {
		this.unitPrice = unitPrice;
	}

	public Double getContinuedPrice() {
		return continuedPrice;
	}

	public void setContinuedPrice(Double continuedPrice) {
		this.continuedPrice = continuedPrice;
	}

	public Double getCost() {
		return cost;
	}

	public void setCost(Double cost) {
		this.cost = cost;
	}

	public String getReceiveName() {
		return receiveName;
	}

	public void setReceiveName(String receiveName) {
		this.receiveName = receiveName;
	}

	public String getReceiveProvinceId() {
		return receiveProvinceId;
	}

	public void setReceiveProvinceId(String receiveProvinceId) {
		this.receiveProvinceId = receiveProvinceId;
	}

	public String getReceiveCityId() {
		return receiveCityId;
	}

	public void setReceiveCityId(String receiveCityId) {
		this.receiveCityId = receiveCityId;
	}

	public String getReceiveDetailAddress() {
		return receiveDetailAddress;
	}

	public void setReceiveDetailAddress(String receiveDetailAddress) {
		this.receiveDetailAddress = receiveDetailAddress;
	}

	public String getWaybillNo() {
		return waybillNo;
	}

	public void setWaybillNo(String waybillNo) {
		this.waybillNo = waybillNo;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	public String getCarrierName() {
		return carrierName;
	}

	public void setCarrierName(String carrierName) {
		this.carrierName = carrierName;
	}

	public Double getSystemWeight() {
		return systemWeight;
	}

	public void setSystemWeight(Double systemWeight) {
		this.systemWeight = systemWeight;
	}

	public Integer getTotalqty() {
		return totalqty;
	}

	public void setTotalqty(Integer totalqty) {
		this.totalqty = totalqty;
	}

	public String getProductDetail() {
		return productDetail;
	}

	public void setProductDetail(String productDetail) {
		this.productDetail = productDetail;
	}

	public String getExternalNo() {
		return externalNo;
	}

	public void setExternalNo(String externalNo) {
		this.externalNo = externalNo;
	}

	public String getOutstockNo() {
		return outstockNo;
	}

	public void setOutstockNo(String outstockNo) {
		this.outstockNo = outstockNo;
	}

	public String getSpecDesc() {
		return specDesc;
	}

	public void setSpecDesc(String specDesc) {
		this.specDesc = specDesc;
	}

    public String getPackPlanNo() {
        return packPlanNo;
    }

    public void setPackPlanNo(String packPlanNo) {
        this.packPlanNo = packPlanNo;
    }

    public String getPackPlanName() {
        return packPlanName;
    }

    public void setPackPlanName(String packPlanName) {
        this.packPlanName = packPlanName;
    }

    public Double getPackPlanCost() {
        return packPlanCost;
    }

    public void setPackPlanCost(Double packPlanCost) {
        this.packPlanCost = packPlanCost;
    }

    public String getPackGroupNo() {
        return packGroupNo;
    }

    public void setPackGroupNo(String packGroupNo) {
        this.packGroupNo = packGroupNo;
    }

    public String getZexpressnum() {
        return zexpressnum;
    }

    public void setZexpressnum(String zexpressnum) {
        this.zexpressnum = zexpressnum;
    }

    public String getIsCalculated() {
        return isCalculated;
    }

    public void setIsCalculated(String isCalculated) {
        this.isCalculated = isCalculated;
    }
	
    
}
