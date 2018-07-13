package com.jiuyescm.bms.receivable.storage.vo;

public class InstockCalcuVo {

	private Long id;
	private String omsId;
	private String instockNo;		//入库单号
	private String warehouseCode;	//仓库
	private String warehouseName;	//仓库名称
	private String shopperCode;		//商家
	private String shopperName;		//商家名称
	private String externalNum;		//外部单号
	private String instockType;		//入库类型
	private String isCalculated;	//结算状态
	private String productId;		//商品ID
	private String productName;		//商品名称	
	private String externalProductCode;//外部商品编码
	private String categoryId;		//商品类别
	private String categoryName;	//CategoryName
	private String temperatureCode;	//温度类型
	private Double num;				//数量
	private String unitId;			//单位ID
	private String unitName;		//单位名称	
	
	
	/**
     * id
     */
	public Long getId() {
		return this.id;
	}

    /**
     * id
     * @param id
     */
	public void setId(Long id) {
		this.id = id;
	}
	
	/**
     * OmsId
     */
	public String getOmsId() {
		return this.omsId;
	}

    /**
     * OmsId
     * @param omsId
     */
	public void setOmsId(String omsId) {
		this.omsId = omsId;
	}
	
	/**
     * 入库单号
     */
	public String getInstockNo() {
		return this.instockNo;
	}

    /**
     * 入库单号
     * @param instockNo
     */
	public void setInstockNo(String instockNo) {
		this.instockNo = instockNo;
	}
	
	/**
     * 仓库
     */
	public String getWarehouseCode() {
		return this.warehouseCode;
	}

    /**
     * 仓库
     * @param warehouseCode
     */
	public void setWarehouseCode(String warehouseCode) {
		this.warehouseCode = warehouseCode;
	}
	
	/**
     * WarehouseName
     */
	public String getWarehouseName() {
		return this.warehouseName;
	}

    /**
     * WarehouseName
     * @param warehouseName
     */
	public void setWarehouseName(String warehouseName) {
		this.warehouseName = warehouseName;
	}
	
	/**
     * 商家
     */
	public String getShopperCode() {
		return this.shopperCode;
	}

    /**
     * 商家
     * @param shopperCode
     */
	public void setShopperCode(String shopperCode) {
		this.shopperCode = shopperCode;
	}
	
	/**
     * ShopperName
     */
	public String getShopperName() {
		return this.shopperName;
	}

    /**
     * ShopperName
     * @param shopperName
     */
	public void setShopperName(String shopperName) {
		this.shopperName = shopperName;
	}
	
	/**
     * 外部单号
     */
	public String getExternalNum() {
		return this.externalNum;
	}

    /**
     * 外部单号
     * @param externalNum
     */
	public void setExternalNum(String externalNum) {
		this.externalNum = externalNum;
	}
	
	/**
     * 入库类型
     */
	public String getInstockType() {
		return this.instockType;
	}

    /**
     * 入库类型
     * @param instockType
     */
	public void setInstockType(String instockType) {
		this.instockType = instockType;
	}

	public String getIsCalculated() {
		return isCalculated;
	}

	public void setIsCalculated(String isCalculated) {
		this.isCalculated = isCalculated;
	}
	
	/**
     * 商品ID
     */
	public String getProductId() {
		return this.productId;
	}

    /**
     * 商品ID
     * @param productId
     */
	public void setProductId(String productId) {
		this.productId = productId;
	}
	
	/**
     * 商品名称
     */
	public String getProductName() {
		return this.productName;
	}

    /**
     * 商品名称
     * @param productName
     */
	public void setProductName(String productName) {
		this.productName = productName;
	}
	
	/**
     * 外部商品编码
     */
	public String getExternalProductCode() {
		return this.externalProductCode;
	}

    /**
     * 外部商品编码
     * @param externalProductCode
     */
	public void setExternalProductCode(String externalProductCode) {
		this.externalProductCode = externalProductCode;
	}
	
	/**
     * 商品类别
     */
	public String getCategoryId() {
		return this.categoryId;
	}

    /**
     * 商品类别
     * @param categoryId
     */
	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}
	
	/**
     * CategoryName
     */
	public String getCategoryName() {
		return this.categoryName;
	}

    /**
     * CategoryName
     * @param categoryName
     */
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
	
	/**
     * 温度类型
     */
	public String getTemperatureCode() {
		return this.temperatureCode;
	}

    /**
     * 温度类型
     * @param temperatureCode
     */
	public void setTemperatureCode(String temperatureCode) {
		this.temperatureCode = temperatureCode;
	}
	
	/**
     * 数量
     */
	public Double getNum() {
		return this.num;
	}

    /**
     * 数量
     * @param num
     */
	public void setNum(Double num) {
		this.num = num;
	}

	public String getUnitId() {
		return unitId;
	}

	public void setUnitId(String unitId) {
		this.unitId = unitId;
	}

	public String getUnitName() {
		return unitName;
	}

	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}
}
