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
    
}
