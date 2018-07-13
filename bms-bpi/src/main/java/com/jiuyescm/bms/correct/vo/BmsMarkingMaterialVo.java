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
public class BmsMarkingMaterialVo implements IEntity {

    // TODO Change serialVersionUID by eclipse
    private static final long serialVersionUID = -1;
    
	// 自增标识
	private Long id;
	// 运单号
	private String waybillNo;
	// 耗材使用明细id
	private String materialMark;
	// 耗材类别 JY,SH,WH
	private String materialType;

	public BmsMarkingMaterialVo() {

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
    
}
