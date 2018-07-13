/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.biz.storage.entity;

import java.sql.Timestamp;

import com.jiuyescm.cfm.domain.IEntity;

/**
 * 
 * @author stevenl
 * 
 */
public class BizOutstockDetailEntity implements IEntity {

    private static final long serialVersionUID = -1;
    
	// id
	private Long id;
	// oms_id
	private String omsId;
	// oms_item_id
	private String omsItemId;
	// 出库单号
	private String outstockNo;
	// 商品ID
	private String productCode;
	// 商品名称
	private String productName;
	// 外部商品编码
	private String externalProductCode;
	// 商品类别编码
	private String productCategoryCode;
	// 商品类别名称
	private String productCategoryName;
	// 温度类型编码
	private String temperatureCode;
	// 温度类型名称
	private String temperatureName;
	// 数量
	private Double num;
	// 调整数量
	private Double resizeNum;
	// 单位编号
	private String unitCode;
	// 单位
	private String unitName;
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
	//合同
    private String contractCode;
  
    //试算用 只是显示
  	private String price;

  	// 写入BMS时间
 	private Timestamp writeTime;

	public String getContractCode() {
		return contractCode;
	}

	public void setContractCode(String contractCode) {
		this.contractCode = contractCode;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public BizOutstockDetailEntity() {

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
     * oms_id
     */
	public String getOmsId() {
		return this.omsId;
	}

    /**
     * oms_id
     *
     * @param omsId
     */
	public void setOmsId(String omsId) {
		this.omsId = omsId;
	}
	
	/**
     * oms_item_id
     */
	public String getOmsItemId() {
		return this.omsItemId;
	}

    /**
     * oms_item_id
     *
     * @param omsItemId
     */
	public void setOmsItemId(String omsItemId) {
		this.omsItemId = omsItemId;
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
     * 商品ID
     */
	public String getProductCode() {
		return this.productCode;
	}

    /**
     * 商品ID
     *
     * @param productCode
     */
	public void setProductCode(String productCode) {
		this.productCode = productCode;
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
     * 外部商品编码
     */
	public String getExternalProductCode() {
		return this.externalProductCode;
	}

    /**
     * 外部商品编码
     *
     * @param externalProductCode
     */
	public void setExternalProductCode(String externalProductCode) {
		this.externalProductCode = externalProductCode;
	}
	
	/**
     * 商品类别编码
     */
	public String getProductCategoryCode() {
		return this.productCategoryCode;
	}

    /**
     * 商品类别编码
     *
     * @param productCategoryCode
     */
	public void setProductCategoryCode(String productCategoryCode) {
		this.productCategoryCode = productCategoryCode;
	}
	
	/**
     * 商品类别名称
     */
	public String getProductCategoryName() {
		return this.productCategoryName;
	}

    /**
     * 商品类别名称
     *
     * @param productCategoryName
     */
	public void setProductCategoryName(String productCategoryName) {
		this.productCategoryName = productCategoryName;
	}
	
	/**
     * 温度类型编码
     */
	public String getTemperatureCode() {
		return this.temperatureCode;
	}

    /**
     * 温度类型编码
     *
     * @param temperatureCode
     */
	public void setTemperatureCode(String temperatureCode) {
		this.temperatureCode = temperatureCode;
	}
	
	/**
     * 温度类型名称
     */
	public String getTemperatureName() {
		return this.temperatureName;
	}

    /**
     * 温度类型名称
     *
     * @param temperatureName
     */
	public void setTemperatureName(String temperatureName) {
		this.temperatureName = temperatureName;
	}
	
	/**
     * 数量
     */
	public Double getNum() {
		return this.num;
	}

    /**
     * 数量
     *
     * @param num
     */
	public void setNum(Double num) {
		this.num = num;
	}
	
	/**
     * 调整数量
     */
	public Double getResizeNum() {
		return this.resizeNum;
	}

    /**
     * 调整数量
     *
     * @param resizeNum
     */
	public void setResizeNum(Double resizeNum) {
		this.resizeNum = resizeNum;
	}
	
	/**
     * 单位编号
     */
	public String getUnitCode() {
		return this.unitCode;
	}

    /**
     * 单位编号
     *
     * @param unitCode
     */
	public void setUnitCode(String unitCode) {
		this.unitCode = unitCode;
	}
	
	/**
     * 单位
     */
	public String getUnitName() {
		return this.unitName;
	}

    /**
     * 单位
     *
     * @param unitName
     */
	public void setUnitName(String unitName) {
		this.unitName = unitName;
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

	public Timestamp getWriteTime() {
		return writeTime;
	}

	public void setWriteTime(Timestamp writeTime) {
		this.writeTime = writeTime;
	}
    
	
}
