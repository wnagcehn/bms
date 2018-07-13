/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.report.month.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;

import com.jiuyescm.cfm.domain.IEntity;

/**
 * 
 * @author stevenl
 * 
 */
public class ReportGoodsProfitEntity implements IEntity {

    // TODO Change serialVersionUID by eclipse
    private static final long serialVersionUID = -1;
    
	// SellerId
	private String sellerId;
	// SellerName
	private String sellerName;
	// WarehouseCode
	private String warehouseCode;
	// WarehouseName
	private String warehouseName;
	// GoodsCode
	private String goodsCode;
	// GoodsName
	private String goodsName;
	// GoodsQty
	private Integer goodsQty01;
	// FeesType
	private int feesType;
	// Amount01
	private BigDecimal amount01;
	// Amount02
	private BigDecimal amount02;
	// Amount03
	private BigDecimal amount03;
	// Amount04
	private BigDecimal amount04;
	// Amount05
	private BigDecimal amount05;
	// Amount06
	private BigDecimal amount06;
	// Amount07
	private BigDecimal amount07;
	// Amount08
	private BigDecimal amount08;
	// Amount09
	private BigDecimal amount09;
	// Amount10
	private BigDecimal amount10;
	// Amount11
	private BigDecimal amount11;
	// Amount12
	private BigDecimal amount12;
	// AmountSum
	private BigDecimal amountSum;
	// ReportYear
	private String reportYear;
	// CreateTime
	private Timestamp createTime;
	// ModifyTime
	private Timestamp modifyTime;
	
	private Integer goodsQty02;
	private Integer goodsQty03;
	private Integer goodsQty04;
	private Integer goodsQty05;
	private Integer goodsQty06;
	private Integer goodsQty07;
	private Integer goodsQty08;
	private Integer goodsQty09;
	private Integer goodsQty10;
	private Integer goodsQty11;
	private Integer goodsQty12;
	private Integer goodsQtySum;

	public ReportGoodsProfitEntity() {

	}
	
	/**
     * SellerId
     */
	public String getSellerId() {
		return this.sellerId;
	}

    /**
     * SellerId
     *
     * @param sellerId
     */
	public void setSellerId(String sellerId) {
		this.sellerId = sellerId;
	}
	
	/**
     * SellerName
     */
	public String getSellerName() {
		return this.sellerName;
	}

    /**
     * SellerName
     *
     * @param sellerName
     */
	public void setSellerName(String sellerName) {
		this.sellerName = sellerName;
	}
	
	/**
     * WarehouseCode
     */
	public String getWarehouseCode() {
		return this.warehouseCode;
	}

    /**
     * WarehouseCode
     *
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
     *
     * @param warehouseName
     */
	public void setWarehouseName(String warehouseName) {
		this.warehouseName = warehouseName;
	}
	
	/**
     * GoodsCode
     */
	public String getGoodsCode() {
		return this.goodsCode;
	}

    /**
     * GoodsCode
     *
     * @param goodsCode
     */
	public void setGoodsCode(String goodsCode) {
		this.goodsCode = goodsCode;
	}
	
	/**
     * GoodsName
     */
	public String getGoodsName() {
		return this.goodsName;
	}

    /**
     * GoodsName
     *
     * @param goodsName
     */
	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}
	
	/**
     * GoodsQty
     */
	public Integer getGoodsQty01() {
		return this.goodsQty01;
	}

    /**
     * GoodsQty
     *
     * @param goodsQty
     */
	public void setGoodsQty01(Integer goodsQty01) {
		this.goodsQty01 = goodsQty01;
	}
	
	/**
     * FeesType
     */
	public int getFeesType() {
		return this.feesType;
	}

    /**
     * FeesType
     *
     * @param feesType
     */
	public void setFeesType(int feesType) {
		this.feesType = feesType;
	}
	
	/**
     * Amount01
     */
	public BigDecimal getAmount01() {
		return this.amount01;
	}

    /**
     * Amount01
     *
     * @param amount01
     */
	public void setAmount01(BigDecimal amount01) {
		this.amount01 = amount01;
	}
	
	/**
     * Amount02
     */
	public BigDecimal getAmount02() {
		return this.amount02;
	}

    /**
     * Amount02
     *
     * @param amount02
     */
	public void setAmount02(BigDecimal amount02) {
		this.amount02 = amount02;
	}
	
	/**
     * Amount03
     */
	public BigDecimal getAmount03() {
		return this.amount03;
	}

    /**
     * Amount03
     *
     * @param amount03
     */
	public void setAmount03(BigDecimal amount03) {
		this.amount03 = amount03;
	}
	
	/**
     * Amount04
     */
	public BigDecimal getAmount04() {
		return this.amount04;
	}

    /**
     * Amount04
     *
     * @param amount04
     */
	public void setAmount04(BigDecimal amount04) {
		this.amount04 = amount04;
	}
	
	/**
     * Amount05
     */
	public BigDecimal getAmount05() {
		return this.amount05;
	}

    /**
     * Amount05
     *
     * @param amount05
     */
	public void setAmount05(BigDecimal amount05) {
		this.amount05 = amount05;
	}
	
	/**
     * Amount06
     */
	public BigDecimal getAmount06() {
		return this.amount06;
	}

    /**
     * Amount06
     *
     * @param amount06
     */
	public void setAmount06(BigDecimal amount06) {
		this.amount06 = amount06;
	}
	
	/**
     * Amount07
     */
	public BigDecimal getAmount07() {
		return this.amount07;
	}

    /**
     * Amount07
     *
     * @param amount07
     */
	public void setAmount07(BigDecimal amount07) {
		this.amount07 = amount07;
	}
	
	/**
     * Amount08
     */
	public BigDecimal getAmount08() {
		return this.amount08;
	}

    /**
     * Amount08
     *
     * @param amount08
     */
	public void setAmount08(BigDecimal amount08) {
		this.amount08 = amount08;
	}
	
	/**
     * Amount09
     */
	public BigDecimal getAmount09() {
		return this.amount09;
	}

    /**
     * Amount09
     *
     * @param amount09
     */
	public void setAmount09(BigDecimal amount09) {
		this.amount09 = amount09;
	}
	
	/**
     * Amount10
     */
	public BigDecimal getAmount10() {
		return this.amount10;
	}

    /**
     * Amount10
     *
     * @param amount10
     */
	public void setAmount10(BigDecimal amount10) {
		this.amount10 = amount10;
	}
	
	/**
     * Amount11
     */
	public BigDecimal getAmount11() {
		return this.amount11;
	}

    /**
     * Amount11
     *
     * @param amount11
     */
	public void setAmount11(BigDecimal amount11) {
		this.amount11 = amount11;
	}
	
	/**
     * Amount12
     */
	public BigDecimal getAmount12() {
		return this.amount12;
	}

    /**
     * Amount12
     *
     * @param amount12
     */
	public void setAmount12(BigDecimal amount12) {
		this.amount12 = amount12;
	}
	
	/**
     * AmountSum
     */
	public BigDecimal getAmountSum() {
		return this.amountSum;
	}

    /**
     * AmountSum
     *
     * @param amountSum
     */
	public void setAmountSum(BigDecimal amountSum) {
		this.amountSum = amountSum;
	}
	
	/**
     * ReportYear
     */
	public String getReportYear() {
		return this.reportYear;
	}

    /**
     * ReportYear
     *
     * @param reportYear
     */
	public void setReportYear(String reportYear) {
		this.reportYear = reportYear;
	}
	
	/**
     * CreateTime
     */
	public Timestamp getCreateTime() {
		return this.createTime;
	}

    /**
     * CreateTime
     *
     * @param createTime
     */
	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}
	
	/**
     * ModifyTime
     */
	public Timestamp getModifyTime() {
		return this.modifyTime;
	}

    /**
     * ModifyTime
     *
     * @param modifyTime
     */
	public void setModifyTime(Timestamp modifyTime) {
		this.modifyTime = modifyTime;
	}

	public Integer getGoodsQty02() {
		return goodsQty02;
	}

	public void setGoodsQty02(Integer goodsQty02) {
		this.goodsQty02 = goodsQty02;
	}

	public Integer getGoodsQty03() {
		return goodsQty03;
	}

	public void setGoodsQty03(Integer goodsQty03) {
		this.goodsQty03 = goodsQty03;
	}

	public Integer getGoodsQty04() {
		return goodsQty04;
	}

	public void setGoodsQty04(Integer goodsQty04) {
		this.goodsQty04 = goodsQty04;
	}

	public Integer getGoodsQty05() {
		return goodsQty05;
	}

	public void setGoodsQty05(Integer goodsQty05) {
		this.goodsQty05 = goodsQty05;
	}

	public Integer getGoodsQty06() {
		return goodsQty06;
	}

	public void setGoodsQty06(Integer goodsQty06) {
		this.goodsQty06 = goodsQty06;
	}

	public Integer getGoodsQty07() {
		return goodsQty07;
	}

	public void setGoodsQty07(Integer goodsQty07) {
		this.goodsQty07 = goodsQty07;
	}

	public Integer getGoodsQty08() {
		return goodsQty08;
	}

	public void setGoodsQty08(Integer goodsQty08) {
		this.goodsQty08 = goodsQty08;
	}

	public Integer getGoodsQty09() {
		return goodsQty09;
	}

	public void setGoodsQty09(Integer goodsQty09) {
		this.goodsQty09 = goodsQty09;
	}

	public Integer getGoodsQty10() {
		return goodsQty10;
	}

	public void setGoodsQty10(Integer goodsQty10) {
		this.goodsQty10 = goodsQty10;
	}

	public Integer getGoodsQty11() {
		return goodsQty11;
	}

	public void setGoodsQty11(Integer goodsQty11) {
		this.goodsQty11 = goodsQty11;
	}

	public Integer getGoodsQty12() {
		return goodsQty12;
	}

	public void setGoodsQty12(Integer goodsQty12) {
		this.goodsQty12 = goodsQty12;
	}

	public Integer getGoodsQtySum() {
		return goodsQtySum;
	}

	public void setGoodsQtySum(Integer goodsQtySum) {
		this.goodsQtySum = goodsQtySum;
	}
    
}
