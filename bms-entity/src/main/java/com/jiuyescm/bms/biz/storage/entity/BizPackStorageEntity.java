/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.biz.storage.entity;

import java.sql.Timestamp;

import org.apache.commons.lang.StringUtils;

import com.jiuyescm.cfm.domain.IEntity;

/**
 * 
 * @author stevenl
 * 
 */
public class BizPackStorageEntity implements IEntity {

    // TODO Change serialVersionUID by eclipse
    private static final long serialVersionUID = -1;
    
	// id
	private Long id;
	// WmsId
	private String wmsId;
	
	// 数据编号
	private String dataNum;
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
	// 温度类型
	private String temperatureTypeCode;
	// TemperatureTypeName
	private String temperatureTypeName;
	// 耗材编号
	private String packNo;
	// SKU名称
	private String packName;
	// 数量
	private Double qty;
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
	//试算托数
	private Double palletNum;
	//试算用 只是显示
	private String contractCode;
	// 是否已计算费用 0-未结算 1-已结算 2-结算异常 3-其他
	private String isCalculated;
	// 写入BMS时间
	private Timestamp writeTime;
	// 费用计算时间
	private Timestamp calculateTime;
	// 备注
	private String remark;
	
	private Double adjustPalletNum;
	
	public Double getAdjustPalletNum() {
		return adjustPalletNum;
	}

	public void setAdjustPalletNum(Double adjustPalletNum) {
		this.adjustPalletNum = adjustPalletNum;
	}

	// 拓展字段1
	private String extattr1;
	// 拓展字段2
	private String extattr2;
	// 拓展字段3
	private String extattr3;
	// 拓展字段4
	private String extattr4;
	// 拓展字段5
	private String extattr5;
	public String getIsCalculated() {
		return isCalculated;
	}

	public void setIsCalculated(String isCalculated) {
		this.isCalculated = isCalculated;
	}

	public String getContractCode() {
		return contractCode;
	}

	public void setContractCode(String contractCode) {
		this.contractCode = contractCode;
	}

	public Double getPalletNum() {
		return palletNum;
	}

	public void setPalletNum(Double palletNum) {
		this.palletNum = palletNum;
	}

	public BizPackStorageEntity() {

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

	public Timestamp getWriteTime() {
		return writeTime;
	}

	public void setWriteTime(Timestamp writeTime) {
		this.writeTime = writeTime;
	}

	public Timestamp getCalculateTime() {
		return calculateTime;
	}

	public void setCalculateTime(Timestamp calculateTime) {
		this.calculateTime = calculateTime;
	}

	public String getDataNum() {
		return dataNum;
	}

	public void setDataNum(String dataNum) {
		this.dataNum = dataNum;
	}

	public String getTemperatureTypeCode() {
		return temperatureTypeCode;
	}

	public void setTemperatureTypeCode(String temperatureTypeCode) {
		this.temperatureTypeCode = temperatureTypeCode;
	}

	public String getTemperatureTypeName() {
		return temperatureTypeName;
	}

	public void setTemperatureTypeName(String temperatureTypeName) {
		this.temperatureTypeName = temperatureTypeName;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		if (StringUtils.isNotEmpty(remark) && remark.length() > 128) {
			this.remark = remark.substring(0, 100);
		}else {
			this.remark = remark;
		}
	}

	public String getextattr1() {
		return extattr1;
	}

	public void setextattr1(String extattr1) {
		this.extattr1 = extattr1;
	}

	public String getextattr2() {
		return extattr2;
	}

	public void setextattr2(String extattr2) {
		this.extattr2 = extattr2;
	}

	public String getextattr3() {
		return extattr3;
	}

	public void setextattr3(String extattr3) {
		this.extattr3 = extattr3;
	}

	public String getextattr4() {
		return extattr4;
	}

	public void setextattr4(String extattr4) {
		this.extattr4 = extattr4;
	}

	public String getextattr5() {
		return extattr5;
	}

	public void setextattr5(String extattr5) {
		this.extattr5 = extattr5;
	}
	
}
