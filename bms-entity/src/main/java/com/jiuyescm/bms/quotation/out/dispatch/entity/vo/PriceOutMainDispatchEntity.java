package com.jiuyescm.bms.quotation.out.dispatch.entity.vo;

import java.sql.Timestamp;

import com.jiuyescm.cfm.domain.IEntity;

public class PriceOutMainDispatchEntity implements IEntity{
	// TODO Change serialVersionUID by eclipse
    private static final long serialVersionUID = -1;
    
	// id
	private Long id;
	// 模版编号
	private String templateId;
	// 物流商ID
	private String carrierId;
	// 始发仓ID
	private String startWarehouseId;
	
	//始发仓名称
	private String startWarehouseName;
	
	// 省份ID
	private String provinceId;
	
	//省份名称
	private String provinceName;
	
	// 城市ID
	private String cityId;
	
	private String cityName;
	// 县/区ID
	private String areaId;
	
	//县区名称
	private String areaName;
	
	
	// 地域类型
	private String areaTypeCode;
	// 时效
	private String timeliness;
	// 重量界限
	private Double weightLimit;
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
	
	// 温度类型
	private String temperatureTypeCode;
	// extra1
	private String extra1;
	// extra2
	private String extra2;
	// extra3
	private String extra3;
	// extra4
	private String extra4;

	public PriceOutMainDispatchEntity() {

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
	public String getTemplateId() {
		return this.templateId;
	}

    /**
     * 模版编号
     *
     * @param templateId
     */
	public void setTemplateId(String templateId) {
		this.templateId = templateId;
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
	public Double getFirstWeight() {
		return this.firstWeight;
	}

    /**
     * 首重重量
     *
     * @param firstWeight
     */
	public void setFirstWeight(Double firstWeight) {
		this.firstWeight = firstWeight;
	}
	
	/**
     * 首重价格
     */
	public Double getFirstWeightPrice() {
		return this.firstWeightPrice;
	}

    /**
     * 首重价格
     *
     * @param firstWeightPrice
     */
	public void setFirstWeightPrice(Double firstWeightPrice) {
		this.firstWeightPrice = firstWeightPrice;
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

	public String getStartWarehouseName() {
		return startWarehouseName;
	}

	public void setStartWarehouseName(String startWarehouseName) {
		this.startWarehouseName = startWarehouseName;
	}

	public String getProvinceName() {
		return provinceName;
	}

	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public String getAreaName() {
		return areaName;
	}

	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}

	public String getTemperatureTypeCode() {
		return temperatureTypeCode;
	}

	public void setTemperatureTypeCode(String temperatureTypeCode) {
		this.temperatureTypeCode = temperatureTypeCode;
	}

	public String getExtra1() {
		return extra1;
	}

	public void setExtra1(String extra1) {
		this.extra1 = extra1;
	}

	public String getExtra2() {
		return extra2;
	}

	public void setExtra2(String extra2) {
		this.extra2 = extra2;
	}

	public String getExtra3() {
		return extra3;
	}

	public void setExtra3(String extra3) {
		this.extra3 = extra3;
	}

	public String getExtra4() {
		return extra4;
	}

	public void setExtra4(String extra4) {
		this.extra4 = extra4;
	}
	
	
	
}
