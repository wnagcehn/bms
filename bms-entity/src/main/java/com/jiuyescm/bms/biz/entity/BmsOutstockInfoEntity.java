/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.biz.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;

import com.jiuyescm.cfm.domain.IEntity;

/**
 * 
 * @author stevenl
 * 
 */
public class BmsOutstockInfoEntity implements IEntity {

    // TODO Change serialVersionUID by eclipse
    private static final long serialVersionUID = -1;
    
	// 自增主键
	private Long id;
	// 运单号
	private String waybillNo;
	// 转寄后运单号
	private String zexpressnum;
	// 月结账号
	private String monthFeeCount;
	// 快递服务类型
	private String serviceTypeCode;
	// 抛重
	private BigDecimal throwWeight;
	// 纠正抛重
	private BigDecimal correctThrowWeight;
	// 调整重量
	private BigDecimal adjustWeight;
	// 纠正重量
	private BigDecimal correctWeight;
	// 调整箱数
	private BigDecimal adjustBox;
	// 调整商品数量
	private BigDecimal adjustQty;
	// 调整发件省
	private String adjustSendProvince;
	// 调整发件市
	private String adjustSendCity;
	// 调整发件区
	private String adjustSendArea;
	// 调整收件省
	private String adjustReceiveProvince;
	// 调整收件市
	private String adjustReceiveCity;
	// 调整收件区
	private String adjustReceiveArea;
	// 转寄物流商id
	private String forwardCarrierId;
	// 转寄宅配商id
	private String forwardDeliverId;
	// 调整物流商id
	private String adjustCarrierId;
	// 调整宅配商id
	private String adjustDeliverId;
	// 结算状态（账单使用）
	private String accountState;
	// 小状态
	private String bigstatus;
	// 大状态
	private String smallstatus;
	// 签收时间
	private Timestamp signTime;
	// 揽收时间
	private Timestamp acceptTime;
	// 运单状态
	private String waybillStatus;
	// 订单状态
	private String orderStatus;
	// 作废标识
	private String delFlag;
	// 修改者
	private String lastModifier;
	// 修改者id
	private String lastModifierId;
	// 修改时间
	private Timestamp lastModifyTime;

	public BmsOutstockInfoEntity() {

	}
	
	/**
     * 自增主键
     */
	public Long getId() {
		return this.id;
	}

    /**
     * 自增主键
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
     * 转寄后运单号
     */
	public String getZexpressnum() {
		return this.zexpressnum;
	}

    /**
     * 转寄后运单号
     *
     * @param zexpressnum
     */
	public void setZexpressnum(String zexpressnum) {
		this.zexpressnum = zexpressnum;
	}
	
	/**
     * 月结账号
     */
	public String getMonthFeeCount() {
		return this.monthFeeCount;
	}

    /**
     * 月结账号
     *
     * @param monthFeeCount
     */
	public void setMonthFeeCount(String monthFeeCount) {
		this.monthFeeCount = monthFeeCount;
	}
	
	/**
     * 快递服务类型
     */
	public String getServiceTypeCode() {
		return this.serviceTypeCode;
	}

    /**
     * 快递服务类型
     *
     * @param serviceTypeCode
     */
	public void setServiceTypeCode(String serviceTypeCode) {
		this.serviceTypeCode = serviceTypeCode;
	}
	
	/**
     * 抛重
     */
	public BigDecimal getThrowWeight() {
		return this.throwWeight;
	}

    /**
     * 抛重
     *
     * @param throwWeight
     */
	public void setThrowWeight(BigDecimal throwWeight) {
		this.throwWeight = throwWeight;
	}
	
	/**
     * 纠正抛重
     */
	public BigDecimal getCorrectThrowWeight() {
		return this.correctThrowWeight;
	}

    /**
     * 纠正抛重
     *
     * @param correctThrowWeight
     */
	public void setCorrectThrowWeight(BigDecimal correctThrowWeight) {
		this.correctThrowWeight = correctThrowWeight;
	}
	
	/**
     * 调整重量
     */
	public BigDecimal getAdjustWeight() {
		return this.adjustWeight;
	}

    /**
     * 调整重量
     *
     * @param adjustWeight
     */
	public void setAdjustWeight(BigDecimal adjustWeight) {
		this.adjustWeight = adjustWeight;
	}
	
	/**
     * 纠正重量
     */
	public BigDecimal getCorrectWeight() {
		return this.correctWeight;
	}

    /**
     * 纠正重量
     *
     * @param correctWeight
     */
	public void setCorrectWeight(BigDecimal correctWeight) {
		this.correctWeight = correctWeight;
	}
	
	/**
     * 调整箱数
     */
	public BigDecimal getAdjustBox() {
		return this.adjustBox;
	}

    /**
     * 调整箱数
     *
     * @param adjustBox
     */
	public void setAdjustBox(BigDecimal adjustBox) {
		this.adjustBox = adjustBox;
	}
	
	/**
     * 调整商品数量
     */
	public BigDecimal getAdjustQty() {
		return this.adjustQty;
	}

    /**
     * 调整商品数量
     *
     * @param adjustQty
     */
	public void setAdjustQty(BigDecimal adjustQty) {
		this.adjustQty = adjustQty;
	}
	
	/**
     * 调整发件省
     */
	public String getAdjustSendProvince() {
		return this.adjustSendProvince;
	}

    /**
     * 调整发件省
     *
     * @param adjustSendProvince
     */
	public void setAdjustSendProvince(String adjustSendProvince) {
		this.adjustSendProvince = adjustSendProvince;
	}
	
	/**
     * 调整发件市
     */
	public String getAdjustSendCity() {
		return this.adjustSendCity;
	}

    /**
     * 调整发件市
     *
     * @param adjustSendCity
     */
	public void setAdjustSendCity(String adjustSendCity) {
		this.adjustSendCity = adjustSendCity;
	}
	
	/**
     * 调整发件区
     */
	public String getAdjustSendArea() {
		return this.adjustSendArea;
	}

    /**
     * 调整发件区
     *
     * @param adjustSendArea
     */
	public void setAdjustSendArea(String adjustSendArea) {
		this.adjustSendArea = adjustSendArea;
	}
	
	/**
     * 调整收件省
     */
	public String getAdjustReceiveProvince() {
		return this.adjustReceiveProvince;
	}

    /**
     * 调整收件省
     *
     * @param adjustReceiveProvince
     */
	public void setAdjustReceiveProvince(String adjustReceiveProvince) {
		this.adjustReceiveProvince = adjustReceiveProvince;
	}
	
	/**
     * 调整收件市
     */
	public String getAdjustReceiveCity() {
		return this.adjustReceiveCity;
	}

    /**
     * 调整收件市
     *
     * @param adjustReceiveCity
     */
	public void setAdjustReceiveCity(String adjustReceiveCity) {
		this.adjustReceiveCity = adjustReceiveCity;
	}
	
	/**
     * 调整收件区
     */
	public String getAdjustReceiveArea() {
		return this.adjustReceiveArea;
	}

    /**
     * 调整收件区
     *
     * @param adjustReceiveArea
     */
	public void setAdjustReceiveArea(String adjustReceiveArea) {
		this.adjustReceiveArea = adjustReceiveArea;
	}
	
	/**
     * 转寄物流商id
     */
	public String getForwardCarrierId() {
		return this.forwardCarrierId;
	}

    /**
     * 转寄物流商id
     *
     * @param forwardCarrierId
     */
	public void setForwardCarrierId(String forwardCarrierId) {
		this.forwardCarrierId = forwardCarrierId;
	}
	
	/**
     * 转寄宅配商id
     */
	public String getForwardDeliverId() {
		return this.forwardDeliverId;
	}

    /**
     * 转寄宅配商id
     *
     * @param forwardDeliverId
     */
	public void setForwardDeliverId(String forwardDeliverId) {
		this.forwardDeliverId = forwardDeliverId;
	}
	
	/**
     * 调整物流商id
     */
	public String getAdjustCarrierId() {
		return this.adjustCarrierId;
	}

    /**
     * 调整物流商id
     *
     * @param adjustCarrierId
     */
	public void setAdjustCarrierId(String adjustCarrierId) {
		this.adjustCarrierId = adjustCarrierId;
	}
	
	/**
     * 调整宅配商id
     */
	public String getAdjustDeliverId() {
		return this.adjustDeliverId;
	}

    /**
     * 调整宅配商id
     *
     * @param adjustDeliverId
     */
	public void setAdjustDeliverId(String adjustDeliverId) {
		this.adjustDeliverId = adjustDeliverId;
	}
	
	/**
     * 结算状态（账单使用）
     */
	public String getAccountState() {
		return this.accountState;
	}

    /**
     * 结算状态（账单使用）
     *
     * @param accountState
     */
	public void setAccountState(String accountState) {
		this.accountState = accountState;
	}
	
	/**
     * 小状态
     */
	public String getBigstatus() {
		return this.bigstatus;
	}

    /**
     * 小状态
     *
     * @param bigstatus
     */
	public void setBigstatus(String bigstatus) {
		this.bigstatus = bigstatus;
	}
	
	/**
     * 大状态
     */
	public String getSmallstatus() {
		return this.smallstatus;
	}

    /**
     * 大状态
     *
     * @param smallstatus
     */
	public void setSmallstatus(String smallstatus) {
		this.smallstatus = smallstatus;
	}
	
	/**
     * 签收时间
     */
	public Timestamp getSignTime() {
		return this.signTime;
	}

    /**
     * 签收时间
     *
     * @param signTime
     */
	public void setSignTime(Timestamp signTime) {
		this.signTime = signTime;
	}
	
	/**
     * 揽收时间
     */
	public Timestamp getAcceptTime() {
		return this.acceptTime;
	}

    /**
     * 揽收时间
     *
     * @param acceptTime
     */
	public void setAcceptTime(Timestamp acceptTime) {
		this.acceptTime = acceptTime;
	}
	
	/**
     * 运单状态
     */
	public String getWaybillStatus() {
		return this.waybillStatus;
	}

    /**
     * 运单状态
     *
     * @param waybillStatus
     */
	public void setWaybillStatus(String waybillStatus) {
		this.waybillStatus = waybillStatus;
	}
	
	/**
     * 订单状态
     */
	public String getOrderStatus() {
		return this.orderStatus;
	}

    /**
     * 订单状态
     *
     * @param orderStatus
     */
	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}
	
	/**
     * 作废标识
     */
	public String getDelFlag() {
		return this.delFlag;
	}

    /**
     * 作废标识
     *
     * @param delFlag
     */
	public void setDelFlag(String delFlag) {
		this.delFlag = delFlag;
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
     * 修改者id
     */
	public String getLastModifierId() {
		return this.lastModifierId;
	}

    /**
     * 修改者id
     *
     * @param lastModifierId
     */
	public void setLastModifierId(String lastModifierId) {
		this.lastModifierId = lastModifierId;
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
    
}
