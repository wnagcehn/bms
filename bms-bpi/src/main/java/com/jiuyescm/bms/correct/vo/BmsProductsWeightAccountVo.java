/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.correct.vo;

import java.math.BigDecimal;

import com.jiuyescm.cfm.domain.IEntity;

/**
 * 
 * @author stevenl
 * 
 */
public class BmsProductsWeightAccountVo implements IEntity {

    // TODO Change serialVersionUID by eclipse
    private static final long serialVersionUID = -1;
    
	// 自增标识
	private Long id;
	// 运单商品明细id
	private String productsMark;
	// 重量
	private BigDecimal weight;
	// 百分比
	private BigDecimal percent;
	// 任务ID
	private String taskId;

	//重量集合
	private String weightList;
	public BmsProductsWeightAccountVo() {

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
     * 重量
     */
	public BigDecimal getWeight() {
		return this.weight;
	}

    /**
     * 重量
     *
     * @param weight
     */
	public void setWeight(BigDecimal weight) {
		this.weight = weight;
	}
	
	/**
     * 百分比
     */
	public BigDecimal getPercent() {
		return this.percent;
	}

    /**
     * 百分比
     *
     * @param percent
     */
	public void setPercent(BigDecimal percent) {
		this.percent = percent;
	}
	
	/**
     * 任务ID
     */
	public String getTaskId() {
		return this.taskId;
	}

    /**
     * 任务ID
     *
     * @param taskId
     */
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getWeightList() {
		return weightList;
	}

	public void setWeightList(String weightList) {
		this.weightList = weightList;
	}

}
