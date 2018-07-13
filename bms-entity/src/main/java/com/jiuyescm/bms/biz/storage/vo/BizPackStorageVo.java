/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.biz.storage.vo;

import java.sql.Timestamp;
import com.jiuyescm.cfm.domain.IEntity;

/**
 * 
 * @author stevenl
 * 
 */
public class BizPackStorageVo implements IEntity {

	private static final long serialVersionUID = 6404605541642968153L;
	
	// id
	private Long id;
	// WmsId
	private String wmsId;
	// 日期(当前日期)
	private Timestamp curTime;
	// 仓库号
	private String warehouseCode;
	// 仓库名称
	private String warehouseName;
	// 商家Id
	private String customerid;
	// 商家名称
	private String customerName;
	// 耗材编号
	private String packNo;
	// SKU名称
	private String packName;
	// 数量
	private Double qty;
	// 托数
	private Double palletNum;
	// 费用编码
	private String feesNo;
	// 所属哪个仓库的数据库
	private String dbname;
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

	public BizPackStorageVo() {

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
     * WmsId
     */
	public String getWmsId() {
		return this.wmsId;
	}

    /**
     * WmsId
     *
     * @param wmsId
     */
	public void setWmsId(String wmsId) {
		this.wmsId = wmsId;
	}
	
	/**
     * 日期(当前日期)
     */
	public Timestamp getCurTime() {
		return this.curTime;
	}

    /**
     * 日期(当前日期)
     *
     * @param curTime
     */
	public void setCurTime(Timestamp curTime) {
		this.curTime = curTime;
	}
	
	/**
     * 仓库号
     */
	public String getWarehouseCode() {
		return this.warehouseCode;
	}

    /**
     * 仓库号
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
     * 商家Id
     */
	public String getCustomerid() {
		return this.customerid;
	}

    /**
     * 商家Id
     *
     * @param customerid
     */
	public void setCustomerid(String customerid) {
		this.customerid = customerid;
	}
	
	/**
     * 商家名称
     */
	public String getCustomerName() {
		return this.customerName;
	}

    /**
     * 商家名称
     *
     * @param customerName
     */
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	
	/**
     * 耗材编号
     */
	public String getPackNo() {
		return this.packNo;
	}

    /**
     * 耗材编号
     *
     * @param packNo
     */
	public void setPackNo(String packNo) {
		this.packNo = packNo;
	}
	
	/**
     * SKU名称
     */
	public String getPackName() {
		return this.packName;
	}

    /**
     * SKU名称
     *
     * @param packName
     */
	public void setPackName(String packName) {
		this.packName = packName;
	}
	
	/**
     * 数量
     */
	public Double getQty() {
		return this.qty;
	}

    /**
     * 数量
     *
     * @param qty
     */
	public void setQty(Double qty) {
		this.qty = qty;
	}
	
	/**
     * 托数
     */
	public Double getPalletNum() {
		return this.palletNum;
	}

    /**
     * 托数
     *
     * @param palletNum
     */
	public void setPalletNum(Double palletNum) {
		this.palletNum = palletNum;
	}
	
	/**
     * 费用编码
     */
	public String getFeesNo() {
		return this.feesNo;
	}

    /**
     * 费用编码
     *
     * @param feesNo
     */
	public void setFeesNo(String feesNo) {
		this.feesNo = feesNo;
	}
	
	/**
     * 所属哪个仓库的数据库
     */
	public String getDbname() {
		return this.dbname;
	}

    /**
     * 所属哪个仓库的数据库
     *
     * @param dbname
     */
	public void setDbname(String dbname) {
		this.dbname = dbname;
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
