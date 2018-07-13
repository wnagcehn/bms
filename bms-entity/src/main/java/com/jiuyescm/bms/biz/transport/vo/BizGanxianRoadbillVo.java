/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.biz.transport.vo;

import java.sql.Timestamp;
import com.jiuyescm.cfm.domain.IEntity;

/**
 * 
 * @author stevenl
 * 
 */
public class BizGanxianRoadbillVo implements IEntity {

    // TODO Change serialVersionUID by eclipse
    private static final long serialVersionUID = -1;
    
	// id
	private Long id;
	// tms id
	private String tmsId;
	// 路单号
	private String roadbillNo;
	// 路单名称
	private String roadbillName;
	// 路单类型
	private String roadbillType;
	// 承运商编码
	private String carrierId;
	// 承运商名称
	private String carrierName;
	// 仓库编码
	private String warehouseCode;
	// 是否已计算费用 0-未结算 1-已结算 2-结算异常 3-合同不存在 4-不在操作范围 5-其他
	private String isCalculated;
	// 发货省
	private String sendProvinceId;
	// 发货市
	private String sendCityId;
	// 发货区县
	private String sendDistrictId;
	// 收货省
	private String receiverProvinceId;
	// 收货市
	private String receiverCityId;
	// 收货区县
	private String receiverDistrictId;
	// 收件人
	private String receiver;
	// 收件人电话
	private String receiverTelephone;
	// 温度类型
	private String temperatureTypeCode;
	// 是否轻货
	private String isLight;
	// 车牌号
	private String vehicleNo;
	// 车型
	private String carModel;
	// TransportType
	private String transportType;
	// 运单数
	private Double waybillNum;
	// 总箱数
	private Double totalBox;
	// 总重量
	private Double totalWeight;
	// 调整重量
	private Double adjustWeight;
	// 总体积
	private Double totalVolume;
	// 发运人
	private String sendPerson;
	// 发车时间
	private Timestamp sendTime;
	// 完成时间
	private Timestamp finishedTime;
	// 备注
	private String remark;
	// 费用编号
	private String feesNo;
	// 状态
	private String accountState;
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

	public BizGanxianRoadbillVo() {

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
     * tms id
     */
	public String getTmsId() {
		return this.tmsId;
	}

    /**
     * tms id
     *
     * @param tmsId
     */
	public void setTmsId(String tmsId) {
		this.tmsId = tmsId;
	}
	
	/**
     * 路单号
     */
	public String getRoadbillNo() {
		return this.roadbillNo;
	}

    /**
     * 路单号
     *
     * @param roadbillNo
     */
	public void setRoadbillNo(String roadbillNo) {
		this.roadbillNo = roadbillNo;
	}
	
	/**
     * 路单名称
     */
	public String getRoadbillName() {
		return this.roadbillName;
	}

    /**
     * 路单名称
     *
     * @param roadbillName
     */
	public void setRoadbillName(String roadbillName) {
		this.roadbillName = roadbillName;
	}
	
	/**
     * 路单类型
     */
	public String getRoadbillType() {
		return this.roadbillType;
	}

    /**
     * 路单类型
     *
     * @param roadbillType
     */
	public void setRoadbillType(String roadbillType) {
		this.roadbillType = roadbillType;
	}
	
	/**
     * 承运商编码
     */
	public String getCarrierId() {
		return this.carrierId;
	}

    /**
     * 承运商编码
     *
     * @param carrierId
     */
	public void setCarrierId(String carrierId) {
		this.carrierId = carrierId;
	}
	
	/**
     * 承运商名称
     */
	public String getCarrierName() {
		return this.carrierName;
	}

    /**
     * 承运商名称
     *
     * @param carrierName
     */
	public void setCarrierName(String carrierName) {
		this.carrierName = carrierName;
	}
	
	/**
     * 仓库编码
     */
	public String getWarehouseCode() {
		return this.warehouseCode;
	}

    /**
     * 仓库编码
     *
     * @param warehouseCode
     */
	public void setWarehouseCode(String warehouseCode) {
		this.warehouseCode = warehouseCode;
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
     * 发货省
     */
	public String getSendProvinceId() {
		return this.sendProvinceId;
	}

    /**
     * 发货省
     *
     * @param sendProvinceId
     */
	public void setSendProvinceId(String sendProvinceId) {
		this.sendProvinceId = sendProvinceId;
	}
	
	/**
     * 发货市
     */
	public String getSendCityId() {
		return this.sendCityId;
	}

    /**
     * 发货市
     *
     * @param sendCityId
     */
	public void setSendCityId(String sendCityId) {
		this.sendCityId = sendCityId;
	}
	
	/**
     * 发货区县
     */
	public String getSendDistrictId() {
		return this.sendDistrictId;
	}

    /**
     * 发货区县
     *
     * @param sendDistrictId
     */
	public void setSendDistrictId(String sendDistrictId) {
		this.sendDistrictId = sendDistrictId;
	}
	
	/**
     * 收货省
     */
	public String getReceiverProvinceId() {
		return this.receiverProvinceId;
	}

    /**
     * 收货省
     *
     * @param receiverProvinceId
     */
	public void setReceiverProvinceId(String receiverProvinceId) {
		this.receiverProvinceId = receiverProvinceId;
	}
	
	/**
     * 收货市
     */
	public String getReceiverCityId() {
		return this.receiverCityId;
	}

    /**
     * 收货市
     *
     * @param receiverCityId
     */
	public void setReceiverCityId(String receiverCityId) {
		this.receiverCityId = receiverCityId;
	}
	
	/**
     * 收货区县
     */
	public String getReceiverDistrictId() {
		return this.receiverDistrictId;
	}

    /**
     * 收货区县
     *
     * @param receiverDistrictId
     */
	public void setReceiverDistrictId(String receiverDistrictId) {
		this.receiverDistrictId = receiverDistrictId;
	}
	
	/**
     * 收件人
     */
	public String getReceiver() {
		return this.receiver;
	}

    /**
     * 收件人
     *
     * @param receiver
     */
	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}
	
	/**
     * 收件人电话
     */
	public String getReceiverTelephone() {
		return this.receiverTelephone;
	}

    /**
     * 收件人电话
     *
     * @param receiverTelephone
     */
	public void setReceiverTelephone(String receiverTelephone) {
		this.receiverTelephone = receiverTelephone;
	}
	
	/**
     * 温度类型
     */
	public String getTemperatureTypeCode() {
		return this.temperatureTypeCode;
	}

    /**
     * 温度类型
     *
     * @param temperatureTypeCode
     */
	public void setTemperatureTypeCode(String temperatureTypeCode) {
		this.temperatureTypeCode = temperatureTypeCode;
	}
	
	/**
     * 是否轻货
     */
	public String getIsLight() {
		return this.isLight;
	}

    /**
     * 是否轻货
     *
     * @param isLight
     */
	public void setIsLight(String isLight) {
		this.isLight = isLight;
	}
	
	/**
     * 车牌号
     */
	public String getVehicleNo() {
		return this.vehicleNo;
	}

    /**
     * 车牌号
     *
     * @param vehicleNo
     */
	public void setVehicleNo(String vehicleNo) {
		this.vehicleNo = vehicleNo;
	}
	
	/**
     * 车型
     */
	public String getCarModel() {
		return this.carModel;
	}

    /**
     * 车型
     *
     * @param carModel
     */
	public void setCarModel(String carModel) {
		this.carModel = carModel;
	}
	
	/**
     * TransportType
     */
	public String getTransportType() {
		return this.transportType;
	}

    /**
     * TransportType
     *
     * @param transportType
     */
	public void setTransportType(String transportType) {
		this.transportType = transportType;
	}
	
	/**
     * 运单数
     */
	public Double getWaybillNum() {
		return this.waybillNum;
	}

    /**
     * 运单数
     *
     * @param waybillNum
     */
	public void setWaybillNum(Double waybillNum) {
		this.waybillNum = waybillNum;
	}
	
	/**
     * 总箱数
     */
	public Double getTotalBox() {
		return this.totalBox;
	}

    /**
     * 总箱数
     *
     * @param totalBox
     */
	public void setTotalBox(Double totalBox) {
		this.totalBox = totalBox;
	}
	
	/**
     * 总重量
     */
	public Double getTotalWeight() {
		return this.totalWeight;
	}

    /**
     * 总重量
     *
     * @param totalWeight
     */
	public void setTotalWeight(Double totalWeight) {
		this.totalWeight = totalWeight;
	}
	
	/**
     * 调整重量
     */
	public Double getAdjustWeight() {
		return this.adjustWeight;
	}

    /**
     * 调整重量
     *
     * @param adjustWeight
     */
	public void setAdjustWeight(Double adjustWeight) {
		this.adjustWeight = adjustWeight;
	}
	
	/**
     * 总体积
     */
	public Double getTotalVolume() {
		return this.totalVolume;
	}

    /**
     * 总体积
     *
     * @param totalVolume
     */
	public void setTotalVolume(Double totalVolume) {
		this.totalVolume = totalVolume;
	}
	
	/**
     * 发运人
     */
	public String getSendPerson() {
		return this.sendPerson;
	}

    /**
     * 发运人
     *
     * @param sendPerson
     */
	public void setSendPerson(String sendPerson) {
		this.sendPerson = sendPerson;
	}
	
	/**
     * 发车时间
     */
	public Timestamp getSendTime() {
		return this.sendTime;
	}

    /**
     * 发车时间
     *
     * @param sendTime
     */
	public void setSendTime(Timestamp sendTime) {
		this.sendTime = sendTime;
	}
	
	/**
     * 完成时间
     */
	public Timestamp getFinishedTime() {
		return this.finishedTime;
	}

    /**
     * 完成时间
     *
     * @param finishedTime
     */
	public void setFinishedTime(Timestamp finishedTime) {
		this.finishedTime = finishedTime;
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
     * 状态
     */
	public String getAccountState() {
		return this.accountState;
	}

    /**
     * 状态
     *
     * @param accountState
     */
	public void setAccountState(String accountState) {
		this.accountState = accountState;
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
