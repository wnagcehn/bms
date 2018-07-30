package com.jiuyescm.bms.quotation.discount.entity;

import java.sql.Timestamp;
import com.jiuyescm.cfm.domain.IEntity;

 /**
 * ..Entity
 * @author wangchen
 * 
 */
public class BmsQuoteDiscountDetailEntity implements IEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4412311952030246546L;
	// 自增标识
	private Integer id;
	// 模版编号
	private String templateCode;
	// 生效时间
	private Timestamp startTime;
	// 失效时间
	private Timestamp endTime;
	// 仓库编码
	private String warehouseCode;
	// 仓库名称
	private String warehouseName;
	// 下限
	private Double downLimit;
	// 上限
	private Double upLimit;
	// 折扣首价
	private Double firstPrice;
	// 首价折扣率
	private Double firstPriceRate;
	private String firstPriceRateDT;
	// 折扣续重价格
	private Double continuePrice;
	// 续重折扣率
	private Double continuePirceRate;
	private String continuePirceRateDT;
	// 折扣一口价
	private Double unitPrice;
	// 一口价折扣率
	private Double unitPriceRate;
	private String unitPriceRateDT;
	// 创建时间
	private String creator;
	// 创建时间
	private Timestamp createTime;
	// 修改人
	private String lastModifier;
	// 修改时间
	private Timestamp lastModifyTime;
	// 作废标志（0：未作废，1：已作废）
	private String delFlag;

	public BmsQuoteDiscountDetailEntity() {
		super();
	}
	
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	public String getTemplateCode() {
		return this.templateCode;
	}

	public void setTemplateCode(String templateCode) {
		this.templateCode = templateCode;
	}
	
	public Timestamp getStartTime() {
		return this.startTime;
	}

	public void setStartTime(Timestamp startTime) {
		this.startTime = startTime;
	}
	
	public Timestamp getEndTime() {
		return this.endTime;
	}

	public void setEndTime(Timestamp endTime) {
		this.endTime = endTime;
	}
	
	public String getWarehouseCode() {
		return this.warehouseCode;
	}

	public void setWarehouseCode(String warehouseCode) {
		this.warehouseCode = warehouseCode;
	}
	
	public String getWarehouseName() {
		return this.warehouseName;
	}

	public void setWarehouseName(String warehouseName) {
		this.warehouseName = warehouseName;
	}
	
	public Double getDownLimit() {
		return this.downLimit;
	}

	public void setDownLimit(Double downLimit) {
		this.downLimit = downLimit;
	}
	
	public Double getUpLimit() {
		return this.upLimit;
	}

	public void setUpLimit(Double upLimit) {
		this.upLimit = upLimit;
	}
	
	public Double getFirstPrice() {
		return this.firstPrice;
	}

	public void setFirstPrice(Double firstPrice) {
		this.firstPrice = firstPrice;
	}
	
	public Double getFirstPriceRate() {
		return this.firstPriceRate;
	}

	public void setFirstPriceRate(Double firstPriceRate) {
		this.firstPriceRate = firstPriceRate;
	}
	
	public Double getContinuePrice() {
		return this.continuePrice;
	}

	public void setContinuePrice(Double continuePrice) {
		this.continuePrice = continuePrice;
	}
	
	public Double getContinuePirceRate() {
		return this.continuePirceRate;
	}

	public void setContinuePirceRate(Double continuePirceRate) {
		this.continuePirceRate = continuePirceRate;
	}
	
	public Double getUnitPrice() {
		return this.unitPrice;
	}

	public void setUnitPrice(Double unitPrice) {
		this.unitPrice = unitPrice;
	}
	
	public Double getUnitPriceRate() {
		return this.unitPriceRate;
	}

	public void setUnitPriceRate(Double unitPriceRate) {
		this.unitPriceRate = unitPriceRate;
	}
	
	public String getCreator() {
		return this.creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}
	
	public Timestamp getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}
	
	public String getLastModifier() {
		return this.lastModifier;
	}

	public void setLastModifier(String lastModifier) {
		this.lastModifier = lastModifier;
	}
	
	public Timestamp getLastModifyTime() {
		return this.lastModifyTime;
	}

	public void setLastModifyTime(Timestamp lastModifyTime) {
		this.lastModifyTime = lastModifyTime;
	}
	
	public String getDelFlag() {
		return this.delFlag;
	}

	public void setDelFlag(String delFlag) {
		this.delFlag = delFlag;
	}

	public String getFirstPriceRateDT() {
		return firstPriceRateDT;
	}

	public void setFirstPriceRateDT(String firstPriceRateDT) {
		this.firstPriceRateDT = firstPriceRateDT;
	}

	public String getContinuePirceRateDT() {
		return continuePirceRateDT;
	}

	public void setContinuePirceRateDT(String continuePirceRateDT) {
		this.continuePirceRateDT = continuePirceRateDT;
	}

	public String getUnitPriceRateDT() {
		return unitPriceRateDT;
	}

	public void setUnitPriceRateDT(String unitPriceRateDT) {
		this.unitPriceRateDT = unitPriceRateDT;
	}

    
}
