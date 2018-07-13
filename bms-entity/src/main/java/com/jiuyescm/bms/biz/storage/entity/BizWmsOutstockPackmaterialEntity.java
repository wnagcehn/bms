/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.biz.storage.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;

import com.jiuyescm.cfm.domain.IEntity;

/**
 * 
 * @author stevenl
 * 
 */
public class BizWmsOutstockPackmaterialEntity implements IEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 88458191946593325L;
	//ID 主键
	private int id;
	//年
	private String createYear;
	//月
	private String createMonth;
	//出库时间
	private Timestamp createTime;
	//仓库编号
	private String warehouseNo;
	//仓库名称
	private String warehouseName;
	//商家ID
	private String customerId;
	//商家名称
	private String customerName;
	//运单号
	private String waybillNo;
	//耗材编码
	private String consumerMaterialCode;
	//耗材名称
	private String consumerMaterialName;
	//耗材数量
	private BigDecimal num; 
	//耗材重量
	private BigDecimal weight;
	//是否已计算费用
	private String isCalculated;
	//费用计算时间
	private Timestamp calculateTime;
	//备注
	private String remark;
	//费用编号
	private String feesNo;
	
	//费用编号
	public BizWmsOutstockPackmaterialEntity() {

	}
	
	/**
     * id
     */
	public int getId() {
		return this.id;
	}

    /**
     * id
     *
     * @param id
     */
	public void setId(int id) {
		this.id = id;
	}
	
	/**
     * 年
     */
	public String getCreateYear() {
		return this.createYear;
	}

    /**
     * 年
     *
     * @param createYear
     */
	public void setCreateYear(String createYear) {
		this.createYear = createYear;
	}
	
	/**
     * 月
     */
	public String getCreateMonth() {
		return this.createMonth;
	}

    /**
     * 月
     *
     * @param createMonth
     */
	public void setCreateMonth(String createMonth) {
		this.createMonth = createMonth;
	}
	
	/**
     * 出库时间
     */
	public Timestamp getCreateTime() {
		return this.createTime;
	}

    /**
     * 出库时间
     *
     * @param createTime
     */
	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}
	
	/**
     * WarehouseNo
     */
	public String getWarehouseNo() {
		return this.warehouseNo;
	}

    /**
     * WarehouseNo
     *
     * @param warehouseNo
     */
	public void setWarehouseNo(String warehouseNo) {
		this.warehouseNo = warehouseNo;
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
     * CustomerId
     */
	public String getCustomerId() {
		return this.customerId;
	}

    /**
     * CustomerId
     *
     * @param customerId
     */
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
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
     * WaybillNo
     */
	public String getWaybillNo() {
		return this.waybillNo;
	}

    /**
     * WaybillNo
     *
     * @param waybillNo
     */
	public void setWaybillNo(String waybillNo) {
		this.waybillNo = waybillNo;
	}
	
	/**
     * 耗材编码
     */
	public String getConsumerMaterialCode() {
		return this.consumerMaterialCode;
	}

    /**
     * 耗材编码
     *
     * @param consumerMaterialCode
     */
	public void setConsumerMaterialCode(String consumerMaterialCode) {
		this.consumerMaterialCode = consumerMaterialCode;
	}
	
	/**
     * 耗材名称
     */
	public String getConsumerMaterialName() {
		return this.consumerMaterialName;
	}

    /**
     * 耗材名称
     *
     * @param consumerMaterialName
     */
	public void setConsumerMaterialName(String consumerMaterialName) {
		this.consumerMaterialName = consumerMaterialName;
	}
	
	/**
     * 耗材数量
     */
	public BigDecimal getNum() {
		return this.num;
	}

    /**
     * 耗材数量
     *
     * @param num
     */
	public void setNum(BigDecimal num) {
		this.num = num;
	}

	public String getIsCalculated() {
		return isCalculated;
	}

	public void setIsCalculated(String isCalculated) {
		this.isCalculated = isCalculated;
	}

	public Timestamp getCalculateTime() {
		return calculateTime;
	}

	public void setCalculateTime(Timestamp calculateTime) {
		this.calculateTime = calculateTime;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getFeesNo() {
		return feesNo;
	}

	public void setFeesNo(String feesNo) {
		this.feesNo = feesNo;
	}

	public BigDecimal getWeight() {
		return weight;
	}

	public void setWeight(BigDecimal weight) {
		this.weight = weight;
	}
    
}
