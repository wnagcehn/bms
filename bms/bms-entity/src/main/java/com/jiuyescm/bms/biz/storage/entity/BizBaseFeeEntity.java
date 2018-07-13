/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.biz.storage.entity;

import java.sql.Timestamp;
import com.jiuyescm.cfm.domain.IEntity;

/**
 * 
 * @author stevenl
 * 
 */
public class BizBaseFeeEntity implements IEntity {

    // TODO Change serialVersionUID by eclipse
    private static final long serialVersionUID = -1;
    
	// id
	private Long id;
	// 操作日期
	private Timestamp operationTime;
	// 仓库Id
	private String warehouseCode;
	// WarehouseName
	private String warehouseName;
	// 商家Id
	private String customerid;
	// CustomerName
	private String customerName;
	// 数量
	private Long num;
	// 费用编号
	private String feesNo;
	// 费用类型
	private String feesType;
	// 费用单位
	private String feesUnit;
	// 项目
	private String item;
	// 是否已计算费用 0-未结算 1-已结算 2-结算异常 3-合同不存在 4-不在操作范围 5-其他
	private String isCalculated;
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
	// 费用计算时间
	private Timestamp calculateTime;
	// 写入BMS时间
	private Timestamp writeTime;
	// 调整数量
	private Long adjustNum;

	public BizBaseFeeEntity() {

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
     * 操作日期
     */
	public Timestamp getOperationTime() {
		return this.operationTime;
	}

    /**
     * 操作日期
     *
     * @param operationTime
     */
	public void setOperationTime(Timestamp operationTime) {
		this.operationTime = operationTime;
	}
	
	/**
     * 仓库Id
     */
	public String getWarehouseCode() {
		return this.warehouseCode;
	}

    /**
     * 仓库Id
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
     * CustomerName
     */
	public String getCustomerName() {
		return this.customerName;
	}

    /**
     * CustomerName
     *
     * @param customerName
     */
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	
	/**
     * 数量
     */
	public Long getNum() {
		return this.num;
	}

    /**
     * 数量
     *
     * @param num
     */
	public void setNum(Long num) {
		this.num = num;
	}
	
	/**
     * 费用编号
     */
	public String getFeesNo() {
		return this.feesNo;
	}

    /**
     * 费用编号
     *
     * @param feesNo
     */
	public void setFeesNo(String feesNo) {
		this.feesNo = feesNo;
	}
	
	/**
     * 费用类型
     */
	public String getFeesType() {
		return this.feesType;
	}

    /**
     * 费用类型
     *
     * @param feesType
     */
	public void setFeesType(String feesType) {
		this.feesType = feesType;
	}
	
	/**
     * 费用单位
     */
	public String getFeesUnit() {
		return this.feesUnit;
	}

    /**
     * 费用单位
     *
     * @param feesUnit
     */
	public void setFeesUnit(String feesUnit) {
		this.feesUnit = feesUnit;
	}
	
	/**
     * 项目
     */
	public String getItem() {
		return this.item;
	}

    /**
     * 项目
     *
     * @param item
     */
	public void setItem(String item) {
		this.item = item;
	}
	
	/**
     * 是否已计算费用 0-未结算 1-已结算 2-结算异常 3-合同不存在 4-不在操作范围 5-其他
     */
	public String getIsCalculated() {
		return this.isCalculated;
	}

    /**
     * 是否已计算费用 0-未结算 1-已结算 2-结算异常 3-合同不存在 4-不在操作范围 5-其他
     *
     * @param isCalculated
     */
	public void setIsCalculated(String isCalculated) {
		this.isCalculated = isCalculated;
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
	
	/**
     * 费用计算时间
     */
	public Timestamp getCalculateTime() {
		return this.calculateTime;
	}

    /**
     * 费用计算时间
     *
     * @param calculateTime
     */
	public void setCalculateTime(Timestamp calculateTime) {
		this.calculateTime = calculateTime;
	}
	
	/**
     * 写入BMS时间
     */
	public Timestamp getWriteTime() {
		return this.writeTime;
	}

    /**
     * 写入BMS时间
     *
     * @param writeTime
     */
	public void setWriteTime(Timestamp writeTime) {
		this.writeTime = writeTime;
	}
	
	/**
     * 调整数量
     */
	public Long getAdjustNum() {
		return this.adjustNum;
	}

    /**
     * 调整数量
     *
     * @param adjustNum
     */
	public void setAdjustNum(Long adjustNum) {
		this.adjustNum = adjustNum;
	}
    
}
