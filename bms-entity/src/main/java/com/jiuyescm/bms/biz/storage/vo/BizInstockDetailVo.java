package com.jiuyescm.bms.biz.storage.vo;

import java.sql.Timestamp;
import com.jiuyescm.cfm.domain.IEntity;

/**
 * 入库单明细表vo
 * @author yangshuaishuai
 */
public class BizInstockDetailVo implements IEntity {

	private static final long serialVersionUID = 6524048431155156434L;
	
	// id
	private Long id;
	// OmsId
	private String omsId;
	// 入库单号
	private String instockNo;
	// 商品ID
	private String productId;
	// 商品名称
	private String productName;
	// 外部商品编码
	private String externalProductCode;
	// 商品类别
	private String categoryId;
	// CategoryName
	private String categoryName;
	// 温度类型
	private String temperatureCode;
	// 数量
	private Double num;
	// 调整数量
	private Double adjustNum;
	// 单位
	private String unitId;
	// UnitName
	private String unitName;
	// FeesNo
	private String feesNo;
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

	public BizInstockDetailVo() {

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
     * OmsId
     */
	public String getOmsId() {
		return this.omsId;
	}

    /**
     * OmsId
     *
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
     *
     * @param instockNo
     */
	public void setInstockNo(String instockNo) {
		this.instockNo = instockNo;
	}
	
	/**
     * 商品ID
     */
	public String getProductId() {
		return this.productId;
	}

    /**
     * 商品ID
     *
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
     * 商品类别
     */
	public String getCategoryId() {
		return this.categoryId;
	}

    /**
     * 商品类别
     *
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
     *
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
     *
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
     *
     * @param num
     */
	public void setNum(Double num) {
		this.num = num;
	}
	
	/**
     * 调整数量
     */
	public Double getAdjustNum() {
		return this.adjustNum;
	}

    /**
     * 调整数量
     *
     * @param adjustNum
     */
	public void setAdjustNum(Double adjustNum) {
		this.adjustNum = adjustNum;
	}
	
	/**
     * 单位
     */
	public String getUnitId() {
		return this.unitId;
	}

    /**
     * 单位
     *
     * @param unitId
     */
	public void setUnitId(String unitId) {
		this.unitId = unitId;
	}
	
	/**
     * UnitName
     */
	public String getUnitName() {
		return this.unitName;
	}

    /**
     * UnitName
     *
     * @param unitName
     */
	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}
	
	/**
     * FeesNo
     */
	public String getFeesNo() {
		return this.feesNo;
	}

    /**
     * FeesNo
     *
     * @param feesNo
     */
	public void setFeesNo(String feesNo) {
		this.feesNo = feesNo;
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
