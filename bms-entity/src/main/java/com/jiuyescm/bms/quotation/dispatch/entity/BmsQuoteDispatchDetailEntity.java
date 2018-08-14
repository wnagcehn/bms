/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.quotation.dispatch.entity;

import java.sql.Timestamp;

import com.jiuyescm.cfm.domain.IEntity;

/**
 * 宅配报价明细
 * @author stevenl
 */
public class BmsQuoteDispatchDetailEntity implements IEntity {

    // TODO Change serialVersionUID by eclipse
    private static final long serialVersionUID = -1;
    
	// id
	private Long id;
	// 模版编号
	private String templateCode;
	// 始发仓ID
	private String startWarehouseId;
	// 省份ID
	private String provinceId;
	// 城市ID
	private String cityId;
	// 县/区ID
	private String areaId;
	// 重量下限 >=
	private Double weightDown;
	// 重量上限 <
	private Double weightUp;
	// 单价
	private Double unitPrice;
	// 首重重量
	private Double firstWeight;
	// 首重价格
	private Double firstWeightPrice;
	// 续重重量
	private Double continuedWeight;
	// 续重价格
	private Double continuedPrice;
	// 重量界限
	private Double weightLimit;
	// 时效
	private String timeliness;
	// 温度类型
	private String temperatureTypeCode;
	// 特殊计费商品
	private String productCase;
	// 地域类型
	private String areaTypeCode;
	// 宅配商ID
	private String deliverid;
	// 规则标记
	private String mark;
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
	
	private String serviceTypeCode;
	private String servicename;
	private String carrierid;
	public BmsQuoteDispatchDetailEntity() {

	}
	
	/**
     * id
     */
	public Long getId() {
		return this.id;
	}

    /**
     * id
     *
     * @param id
     */
	public void setId(Long id) {
		this.id = id;
	}
	
	/**
     * 模版编号
     */
	public String getTemplateCode() {
		return this.templateCode;
	}

    /**
     * 模版编号
     *
     * @param templateCode
     */
	public void setTemplateCode(String templateCode) {
		this.templateCode = templateCode;
	}
	
	/**
     * 始发仓ID
     */
	public String getStartWarehouseId() {
		return this.startWarehouseId;
	}

    /**
     * 始发仓ID
     *
     * @param startWarehouseId
     */
	public void setStartWarehouseId(String startWarehouseId) {
		this.startWarehouseId = startWarehouseId;
	}
	
	/**
     * 省份ID
     */
	public String getProvinceId() {
		return this.provinceId;
	}

    /**
     * 省份ID
     *
     * @param provinceId
     */
	public void setProvinceId(String provinceId) {
		this.provinceId = provinceId;
	}
	
	/**
     * 城市ID
     */
	public String getCityId() {
		return this.cityId;
	}

    /**
     * 城市ID
     *
     * @param cityId
     */
	public void setCityId(String cityId) {
		this.cityId = cityId;
	}
	
	/**
     * 县/区ID
     */
	public String getAreaId() {
		return this.areaId;
	}

    /**
     * 县/区ID
     *
     * @param areaId
     */
	public void setAreaId(String areaId) {
		this.areaId = areaId;
	}
	
	
	public Double getWeightDown() {
		return weightDown;
	}

	public void setWeightDown(Double weightDown) {
		this.weightDown = weightDown;
	}

	public Double getWeightUp() {
		return weightUp;
	}

	public void setWeightUp(Double weightUp) {
		this.weightUp = weightUp;
	}

	public Double getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(Double unitPrice) {
		this.unitPrice = unitPrice;
	}

	public Double getFirstWeight() {
		return firstWeight;
	}

	public void setFirstWeight(Double firstWeight) {
		this.firstWeight = firstWeight;
	}

	public Double getFirstWeightPrice() {
		return firstWeightPrice;
	}

	public void setFirstWeightPrice(Double firstWeightPrice) {
		this.firstWeightPrice = firstWeightPrice;
	}

	public Double getContinuedWeight() {
		return continuedWeight;
	}

	public void setContinuedWeight(Double continuedWeight) {
		this.continuedWeight = continuedWeight;
	}

	public Double getContinuedPrice() {
		return continuedPrice;
	}

	public void setContinuedPrice(Double continuedPrice) {
		this.continuedPrice = continuedPrice;
	}

	public Double getWeightLimit() {
		return weightLimit;
	}

	public void setWeightLimit(Double weightLimit) {
		this.weightLimit = weightLimit;
	}

	/**
     * 时效
     */
	public String getTimeliness() {
		return this.timeliness;
	}

    /**
     * 时效
     *
     * @param timeliness
     */
	public void setTimeliness(String timeliness) {
		this.timeliness = timeliness;
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
     * 特殊计费商品
     */
	public String getProductCase() {
		return this.productCase;
	}

    /**
     * 特殊计费商品
     *
     * @param productCase
     */
	public void setProductCase(String productCase) {
		this.productCase = productCase;
	}
	
	/**
     * 地域类型
     */
	public String getAreaTypeCode() {
		return this.areaTypeCode;
	}

    /**
     * 地域类型
     *
     * @param areaTypeCode
     */
	public void setAreaTypeCode(String areaTypeCode) {
		this.areaTypeCode = areaTypeCode;
	}
	
	/**
     * 宅配商ID
     */
	public String getDeliverid() {
		return this.deliverid;
	}

    /**
     * 宅配商ID
     *
     * @param deliverid
     */
	public void setDeliverid(String deliverid) {
		this.deliverid = deliverid;
	}
	
	/**
     * 规则标记
     */
	public String getMark() {
		return this.mark;
	}

    /**
     * 规则标记
     *
     * @param mark
     */
	public void setMark(String mark) {
		this.mark = mark;
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

	public String getServiceTypeCode() {
		return serviceTypeCode;
	}

	public void setServiceTypeCode(String serviceTypeCode) {
		this.serviceTypeCode = serviceTypeCode;
	}

	public String getServicename() {
		return servicename;
	}

	public void setServicename(String servicename) {
		this.servicename = servicename;
	}

	public String getCarrierid() {
		return carrierid;
	}

	public void setCarrierid(String carrierid) {
		this.carrierid = carrierid;
	}
    
}
