package com.jiuyescm.bms.biz.storage.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;

import com.jiuyescm.cfm.domain.IEntity;

/**
 * 入库单明细表
 * 
 * @author yangshuaishuai
 * 
 */
public class BizInStockDetailEntity implements IEntity {

	private static final long serialVersionUID = 5789412584082639885L;

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

	private BigDecimal price;
	
	// 写入BMS时间
	private Timestamp writeTime;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getOmsId() {
		return omsId;
	}

	public void setOmsId(String omsId) {
		this.omsId = omsId;
	}

	public String getInstockNo() {
		return instockNo;
	}

	public void setInstockNo(String instockNo) {
		this.instockNo = instockNo;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getExternalProductCode() {
		return externalProductCode;
	}

	public void setExternalProductCode(String externalProductCode) {
		this.externalProductCode = externalProductCode;
	}

	public String getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public String getTemperatureCode() {
		return temperatureCode;
	}

	public void setTemperatureCode(String temperatureCode) {
		this.temperatureCode = temperatureCode;
	}

	public Double getNum() {
		return num;
	}

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

	public String getFeesNo() {
		return feesNo;
	}

	public void setFeesNo(String feesNo) {
		this.feesNo = feesNo;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	public String getLastModifier() {
		return lastModifier;
	}

	public void setLastModifier(String lastModifier) {
		this.lastModifier = lastModifier;
	}

	public Timestamp getLastModifyTime() {
		return lastModifyTime;
	}

	public void setLastModifyTime(Timestamp lastModifyTime) {
		this.lastModifyTime = lastModifyTime;
	}

	public String getDelFlag() {
		return delFlag;
	}

	public void setDelFlag(String delFlag) {
		this.delFlag = delFlag;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public Timestamp getWriteTime() {
		return writeTime;
	}

	public void setWriteTime(Timestamp writeTime) {
		this.writeTime = writeTime;
	}

	
}
