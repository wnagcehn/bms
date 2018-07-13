/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.pub.omsprintconfig.entity;

import java.sql.Timestamp;
import com.jiuyescm.cfm.domain.IEntity;

/**
 * 
 * @author stevenl
 * 
 */
public class PubOmsprintconfigEntity implements IEntity {

    // TODO Change serialVersionUID by eclipse
    private static final long serialVersionUID = -1;
    
	// id
	private Integer id;
	// 仓库ID
	private String warehouseCode;
	// 仓库名称
	private String warehouseName;
	// 物流商代码
	private String carrierCode;
	// 物流商名称
	private String carrierName;
	// 宅配商代码
	private String deliverCode;
	// 宅配商名称
	private String deliverName;
	// 模版代码
	private String templateCode;
	// 模版路径
	private String templateUrl;
	// 模版说明
	private String templateDetails;
	// 启用标记
	private Integer useflag;
	// 作废标记
	private Integer delflag;
	// 备注
	private String remark;
	// 扩展字段1
	private String extAttr1;
	// 扩展字段2
	private String extAttr2;
	// 创建人
	private String crePerson;
	// 创建时间
	private Timestamp creTime;

	public PubOmsprintconfigEntity() {

	}
	
	/**
     * id
     */
	public Integer getId() {
		return this.id;
	}

    /**
     * id
     *
     * @param id
     */
	public void setId(Integer id) {
		this.id = id;
	}
	
	/**
     * 仓库ID
     */
	public String getWarehouseCode() {
		return this.warehouseCode;
	}

    /**
     * 仓库ID
     *
     * @param warehouseCode
     */
	public void setWarehouseCode(String warehouseCode) {
		this.warehouseCode = warehouseCode;
	}
	
	/**
     * 仓库名称
     */
	public String getWarehouseName() {
		return this.warehouseName;
	}

    /**
     * 仓库名称
     *
     * @param warehouseName
     */
	public void setWarehouseName(String warehouseName) {
		this.warehouseName = warehouseName;
	}
	
	/**
     * 物流商代码
     */
	public String getCarrierCode() {
		return this.carrierCode;
	}

    /**
     * 物流商代码
     *
     * @param carrierCode
     */
	public void setCarrierCode(String carrierCode) {
		this.carrierCode = carrierCode;
	}
	
	/**
     * 物流商名称
     */
	public String getCarrierName() {
		return this.carrierName;
	}

    /**
     * 物流商名称
     *
     * @param carrierName
     */
	public void setCarrierName(String carrierName) {
		this.carrierName = carrierName;
	}
	
	/**
     * 宅配商代码
     */
	public String getDeliverCode() {
		return this.deliverCode;
	}

    /**
     * 宅配商代码
     *
     * @param deliverCode
     */
	public void setDeliverCode(String deliverCode) {
		this.deliverCode = deliverCode;
	}
	
	/**
     * 宅配商名称
     */
	public String getDeliverName() {
		return this.deliverName;
	}

    /**
     * 宅配商名称
     *
     * @param deliverName
     */
	public void setDeliverName(String deliverName) {
		this.deliverName = deliverName;
	}
	
	/**
     * 模版代码
     */
	public String getTemplateCode() {
		return this.templateCode;
	}

    /**
     * 模版代码
     *
     * @param templateCode
     */
	public void setTemplateCode(String templateCode) {
		this.templateCode = templateCode;
	}
	
	/**
     * 模版路径
     */
	public String getTemplateUrl() {
		return this.templateUrl;
	}

    /**
     * 模版路径
     *
     * @param templateUrl
     */
	public void setTemplateUrl(String templateUrl) {
		this.templateUrl = templateUrl;
	}
	
	/**
     * 模版说明
     */
	public String getTemplateDetails() {
		return this.templateDetails;
	}

    /**
     * 模版说明
     *
     * @param templateDetails
     */
	public void setTemplateDetails(String templateDetails) {
		this.templateDetails = templateDetails;
	}
	
	/**
     * 启用标记
     */
	public Integer getUseflag() {
		return this.useflag;
	}

    /**
     * 启用标记
     *
     * @param useflag
     */
	public void setUseflag(Integer useflag) {
		this.useflag = useflag;
	}
	
	/**
     * 作废标记
     */
	public Integer getDelflag() {
		return this.delflag;
	}

    /**
     * 作废标记
     *
     * @param delflag
     */
	public void setDelflag(Integer delflag) {
		this.delflag = delflag;
	}
	
	/**
     * 备注
     */
	public String getRemark() {
		return this.remark;
	}

    /**
     * 备注
     *
     * @param remark
     */
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	/**
     * 扩展字段1
     */
	public String getExtAttr1() {
		return this.extAttr1;
	}

    /**
     * 扩展字段1
     *
     * @param extAttr1
     */
	public void setExtAttr1(String extAttr1) {
		this.extAttr1 = extAttr1;
	}
	
	/**
     * 扩展字段2
     */
	public String getExtAttr2() {
		return this.extAttr2;
	}

    /**
     * 扩展字段2
     *
     * @param extAttr2
     */
	public void setExtAttr2(String extAttr2) {
		this.extAttr2 = extAttr2;
	}
	
	/**
     * 创建人
     */
	public String getCrePerson() {
		return this.crePerson;
	}

    /**
     * 创建人
     *
     * @param crePerson
     */
	public void setCrePerson(String crePerson) {
		this.crePerson = crePerson;
	}
	
	/**
     * 创建时间
     */
	public Timestamp getCreTime() {
		return this.creTime;
	}

    /**
     * 创建时间
     *
     * @param creTime
     */
	public void setCreTime(Timestamp creTime) {
		this.creTime = creTime;
	}
    
}
