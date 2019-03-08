/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.correct.vo;

import com.jiuyescm.cfm.domain.IEntity;

/**
 * 
 * @author stevenl
 * 
 */
public class BmsProductsMaterialAccountVo implements IEntity {

    // TODO Change serialVersionUID by eclipse
    private static final long serialVersionUID = -1;
    
	// 自增标识
	private Long id;
	// 运单商品明细id
	private String productsMark;
	// 耗材类别 JY,SH,WH
	private String materialType;
	// 耗材使用明细id
	private String materialMark;
	//耗材明细
	private String materialDetail;
	// 百分比
	private Double percent;
	// 任务ID
	private String taskId;
	// 运单号
	private String waybillNo;
	
	private String type;
	
	public BmsProductsMaterialAccountVo() {

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
     * 耗材类别 JY,SH,WH
     */
	public String getMaterialType() {
		return this.materialType;
	}

    /**
     * 耗材类别 JY,SH,WH
     *
     * @param materialType
     */
	public void setMaterialType(String materialType) {
		this.materialType = materialType;
	}
	
	/**
     * 耗材使用明细id
     */
	public String getMaterialMark() {
		return this.materialMark;
	}

    /**
     * 耗材使用明细id
     *
     * @param materialMark
     */
	public void setMaterialMark(String materialMark) {
		this.materialMark = materialMark;
	}
	
	/**
     * 百分比
     */
	public Double getPercent() {
		return this.percent;
	}

    /**
     * 百分比
     *
     * @param percent
     */
	public void setPercent(Double percent) {
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

	public String getWaybillNo() {
		return waybillNo;
	}

	public void setWaybillNo(String waybillNo) {
		this.waybillNo = waybillNo;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getMaterialDetail() {
		return materialDetail;
	}

	public void setMaterialDetail(String materialDetail) {
		this.materialDetail = materialDetail;
	}
    
	
}
