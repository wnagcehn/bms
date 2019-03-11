package com.jiuyescm.bms.biz.dispatch.entity;

import java.sql.Timestamp;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.jiuyescm.bms.excel.annotation.ExcelAnnotation.ExcelField;
import com.jiuyescm.bms.quotation.out.dispatch.entity.vo.PriceOutMainDispatchEntity;
import com.jiuyescm.cfm.domain.IEntity;

/**
 * 
 * @author stevenl
 * 
 */
public class BizDispatchBillPayEntity implements IEntity {

    // TODO Change serialVersionUID by eclipse
    private static final long serialVersionUID = -1;
    
	// id
	private Long id;
	// 出库单号
	private String outstockNo;
	// 商家id
	private String customerid;
	//商家名称
	@ExcelField(title = "商家名称", num = 2)
	private String customerName;
	// 物流商
	private String carrierId;
	// 物流商名称
	private String carrierName;
	// 宅配商id
	private String deliverid;
	// 宅配商名称
	private String deliverName;
	// 运单号
	@ExcelField(title = "运单号", num = 4)
	private String waybillNo;
	// 运单数量
	@ExcelField(title = "运单数量", num = 5)
	private Double waybillNum;
	// 运单列表
	private String waybillList;
	// 重量
	@ExcelField(title = "运单重量", num = 10)
	private Double totalWeight;
	// 调整重量
	private Double adjustWeight;
	// 仓库id
	private String warehouseCode;
	//仓库名称
	@ExcelField(title = "仓库", num = 1)
	private String warehouseName;
	// 服务类型
	private String serviceTypeCode;
	// 配送类型
	private String dispatchTypeCode;
	// 代收金额
	private Double collectMoney;
	// 月结账号
	private String monthFeeCount;
	// 是否已计算费用 0-未结算 1-已结算 2-结算异常 3-合同不存在 4-不在操作范围 5-其他
	private String isCalculated;
	// 寄件人
	private String sendName;
	// 寄件人省份
	private String sendProvinceId;
	// 寄件人城市
	private String sendCityId;
	// 寄件人地区
	private String sendDistrictId;
	// 寄件人街道
	private String sendStreet;
	// 寄件人详细地址
	private String sendDetailAddress;
	// 收件人
	private String receiveName;
	// 收件人省份
	private String receiveProvinceId;
	// 收件人城市
	private String receiveCityId;
	// 收件人地区
	private String receiveDistrictId;
	// 收件人街道
	private String receiveStreet;
	// 收件人详细地址
	private String receiveDetailAddress;
	// 收件时间
	private Timestamp receiveTime;
	// 费用编号
	private String feesNo;
	// 结算状态
	private String accountState;	
	// 外部单号
	@ExcelField(title = "商家订单号", num = 3)
	private String externalNo;
	// 签收时间
	private Timestamp signTime;
	// 揽收时间
	private Timestamp acceptTime;
	// 创建者
	private String creator;
	// 创建时间
	@ExcelField(title = "运单生成时间", num = 6)
	private Timestamp createTime;
	// 修改者
	private String lastModifier;
	// 修改时间
	private Timestamp lastModifyTime;
	// 写入BMS时间
	private Timestamp writeTime;
	// 费用计算时间
	private Timestamp calculateTime;	
	// 删除标志
	private String delFlag;
	// 体积
	private Double totalVolume;
	// 备注
	private String remark;
	// 温度类型
	private String temperatureTypeCode;
	// 温度类型名称
	private String temperatureTypeName;
	// 运单数量
	private Integer workcount;

	//只做记录，不做数据库修改
	//重量
	private Double weight;
	//模板id
	private String mobanId;
	
	private String dispatchId;
	
	// 收件人省份
	private String receiveProvinceName;
	// 收件人城市
	private String receiveCityName;
	// 收件人地区
	private String receiveDistrictName;
	
	// 转寄后运单号
	private String zexpressnum;
	// 大状态
	@ExcelField(title = "大状态", num = 7)
	private String bigstatus;
	// 小状态
	@ExcelField(title = "小状态", num = 8)
	private String smallstatus;
	// 系统逻辑重量
	@ExcelField(title = "系统逻辑重量", num = 9)
	private Double systemWeight;
	// 总数量
	private Integer totalqty;
	// 商品明细
	private String productDetail;
	// 快递业务类型
	private String expresstype;
	// sku数
	private Integer skuNum;
	// extattr1
	private String extattr1;
	// extattr2
	private String extattr2;
	// extattr3
	private String extattr3;
	// extattr4
	private String extattr4;
	// extattr5
	private String extattr5;
	
    private String adjustCarrierId;
	
	private String adjustCarrierName;
	
    private String adjustDeliverid;
	
	private String adjustDeliverName;
	private String timeliness;//时效
	//调整省
	private String adjustProvinceId;
	//调整市
	private String adjustCityId;
	//调整区
	private String adjustDistrictId;
	
	//抛重
	private Double throwWeight;
	//原始抛重
	private Double originThrowWeight;
	
	// 原始重量
	private Double originWeight;
	
	//新的实际重量
	private Double newTotalWeight;
	
	private String servicename;
	
	//报价list
	private List<PriceOutMainDispatchEntity> priceList;
	
	//物流商重量
	private Double carrierWeight;

	public Double getCarrierWeight() {
		return carrierWeight;
	}

	public void setCarrierWeight(Double carrierWeight) {
		this.carrierWeight = carrierWeight;
	}

	public String getAdjustDeliverid() {
		return adjustDeliverid;
	}

	public void setAdjustDeliverid(String adjustDeliverid) {
		this.adjustDeliverid = adjustDeliverid;
	}

	public String getAdjustDeliverName() {
		return adjustDeliverName;
	}

	public void setAdjustDeliverName(String adjustDeliverName) {
		this.adjustDeliverName = adjustDeliverName;
	}
	
	public String getAdjustCarrierId() {
		return adjustCarrierId;
	}

	public void setAdjustCarrierId(String adjustCarrierId) {
		this.adjustCarrierId = adjustCarrierId;
	}

	public String getAdjustCarrierName() {
		return adjustCarrierName;
	}

	public void setAdjustCarrierName(String adjustCarrierName) {
		this.adjustCarrierName = adjustCarrierName;
	}

	
	public BizDispatchBillPayEntity() {

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
     * 出库单号
     */
	public String getOutstockNo() {
		return this.outstockNo;
	}

    /**
     * 出库单号
     *
     * @param outstockNo
     */
	public void setOutstockNo(String outstockNo) {
		this.outstockNo = outstockNo;
	}
	
	/**
     * 商家id
     */
	public String getCustomerid() {
		return this.customerid;
	}

    /**
     * 商家id
     *
     * @param customerid
     */
	public void setCustomerid(String customerid) {
		this.customerid = customerid;
	}
	
	/**
     * 物流商
     */
	public String getCarrierId() {
		return this.carrierId;
	}

    /**
     * 物流商
     *
     * @param carrierId
     */
	public void setCarrierId(String carrierId) {
		this.carrierId = carrierId;
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
     * 运单数量
     */
	public Double getWaybillNum() {
		return this.waybillNum;
	}

    /**
     * 运单数量
     *
     * @param waybillNum
     */
	public void setWaybillNum(Double waybillNum) {
		this.waybillNum = waybillNum;
	}
	
	/**
     * 运单列表
     */
	public String getWaybillList() {
		return this.waybillList;
	}

    /**
     * 运单列表
     *
     * @param waybillList
     */
	public void setWaybillList(String waybillList) {
		this.waybillList = waybillList;
	}
	
	/**
     * 重量
     */
	public Double getTotalWeight() {
		return this.totalWeight;
	}

    /**
     * 重量
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
     * 仓库id
     */
	public String getWarehouseCode() {
		return this.warehouseCode;
	}

    /**
     * 仓库id
     *
     * @param warehouseCode
     */
	public void setWarehouseCode(String warehouseCode) {
		this.warehouseCode = warehouseCode;
	}
	
	/**
     * 服务类型
     */
	public String getServiceTypeCode() {
		return this.serviceTypeCode;
	}

    /**
     * 服务类型
     *
     * @param serviceTypeCode
     */
	public void setServiceTypeCode(String serviceTypeCode) {
		this.serviceTypeCode = serviceTypeCode;
	}
	
	/**
     * 配送类型
     */
	public String getDispatchTypeCode() {
		return this.dispatchTypeCode;
	}

    /**
     * 配送类型
     *
     * @param dispatchTypeCode
     */
	public void setDispatchTypeCode(String dispatchTypeCode) {
		this.dispatchTypeCode = dispatchTypeCode;
	}
	
	/**
     * 代收金额
     */
	public Double getCollectMoney() {
		return this.collectMoney;
	}

    /**
     * 代收金额
     *
     * @param collectMoney
     */
	public void setCollectMoney(Double collectMoney) {
		this.collectMoney = collectMoney;
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
     * 寄件人
     */
	public String getSendName() {
		return this.sendName;
	}

    /**
     * 寄件人
     *
     * @param sendName
     */
	public void setSendName(String sendName) {
		this.sendName = sendName;
	}
	
	/**
     * 寄件人省份
     */
	public String getSendProvinceId() {
		return this.sendProvinceId;
	}

    /**
     * 寄件人省份
     *
     * @param sendProvinceId
     */
	public void setSendProvinceId(String sendProvinceId) {
		this.sendProvinceId = sendProvinceId;
	}
	
	/**
     * 寄件人城市
     */
	public String getSendCityId() {
		return this.sendCityId;
	}

    /**
     * 寄件人城市
     *
     * @param sendCityId
     */
	public void setSendCityId(String sendCityId) {
		this.sendCityId = sendCityId;
	}
	
	/**
     * 寄件人地区
     */
	public String getSendDistrictId() {
		return this.sendDistrictId;
	}

    /**
     * 寄件人地区
     *
     * @param sendDistrictId
     */
	public void setSendDistrictId(String sendDistrictId) {
		this.sendDistrictId = sendDistrictId;
	}
	
	/**
     * 寄件人街道
     */
	public String getSendStreet() {
		return this.sendStreet;
	}

    /**
     * 寄件人街道
     *
     * @param sendStreet
     */
	public void setSendStreet(String sendStreet) {
		this.sendStreet = sendStreet;
	}
	
	/**
     * 寄件人详细地址
     */
	public String getSendDetailAddress() {
		return this.sendDetailAddress;
	}

    /**
     * 寄件人详细地址
     *
     * @param sendDetailAddress
     */
	public void setSendDetailAddress(String sendDetailAddress) {
		this.sendDetailAddress = sendDetailAddress;
	}
	
	/**
     * 收件人
     */
	public String getReceiveName() {
		return this.receiveName;
	}

    /**
     * 收件人
     *
     * @param receiveName
     */
	public void setReceiveName(String receiveName) {
		this.receiveName = receiveName;
	}
	
	/**
     * 收件人省份
     */
	public String getReceiveProvinceId() {
		return this.receiveProvinceId;
	}

    /**
     * 收件人省份
     *
     * @param receiveProvinceId
     */
	public void setReceiveProvinceId(String receiveProvinceId) {
		this.receiveProvinceId = receiveProvinceId;
	}
	
	/**
     * 收件人城市
     */
	public String getReceiveCityId() {
		return this.receiveCityId;
	}

    /**
     * 收件人城市
     *
     * @param receiveCityId
     */
	public void setReceiveCityId(String receiveCityId) {
		this.receiveCityId = receiveCityId;
	}
	
	/**
     * 收件人地区
     */
	public String getReceiveDistrictId() {
		return this.receiveDistrictId;
	}

    /**
     * 收件人地区
     *
     * @param receiveDistrictId
     */
	public void setReceiveDistrictId(String receiveDistrictId) {
		this.receiveDistrictId = receiveDistrictId;
	}
	
	/**
     * 收件人街道
     */
	public String getReceiveStreet() {
		return this.receiveStreet;
	}

    /**
     * 收件人街道
     *
     * @param receiveStreet
     */
	public void setReceiveStreet(String receiveStreet) {
		this.receiveStreet = receiveStreet;
	}
	
	/**
     * 收件人详细地址
     */
	public String getReceiveDetailAddress() {
		return this.receiveDetailAddress;
	}

    /**
     * 收件人详细地址
     *
     * @param receiveDetailAddress
     */
	public void setReceiveDetailAddress(String receiveDetailAddress) {
		this.receiveDetailAddress = receiveDetailAddress;
	}
	
	/**
     * 收件时间
     */
	public Timestamp getReceiveTime() {
		return this.receiveTime;
	}

    /**
     * 收件时间
     *
     * @param receiveTime
     */
	public void setReceiveTime(Timestamp receiveTime) {
		this.receiveTime = receiveTime;
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
     * 结算状态
     */
	public String getAccountState() {
		return this.accountState;
	}

    /**
     * 结算状态
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
	
	/**
     * 体积
     */
	public Double getTotalVolume() {
		return this.totalVolume;
	}

    /**
     * 体积
     *
     * @param totalVolume
     */
	public void setTotalVolume(Double totalVolume) {
		this.totalVolume = totalVolume;
	}

	public Double getWeight() {
		return weight;
	}

	public void setWeight(Double weight) {
		this.weight = weight;
	}

	public String getMobanId() {
		return mobanId;
	}

	public void setMobanId(String mobanId) {
		this.mobanId = mobanId;
	}

	public String getDispatchId() {
		return dispatchId;
	}

	public void setDispatchId(String dispatchId) {
		this.dispatchId = dispatchId;
	}

	public String getExternalNo() {
		return externalNo;
	}

	public void setExternalNo(String externalNo) {
		this.externalNo = externalNo;
	}

	public Timestamp getSignTime() {
		return signTime;
	}

	public void setSignTime(Timestamp signTime) {
		this.signTime = signTime;
	}

	public Timestamp getAcceptTime() {
		return acceptTime;
	}

	public void setAcceptTime(Timestamp acceptTime) {
		this.acceptTime = acceptTime;
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

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getCarrierName() {
		return carrierName;
	}

	public void setCarrierName(String carrierName) {
		this.carrierName = carrierName;
	}

	public String getDeliverid() {
		return deliverid;
	}

	public void setDeliverid(String deliverid) {
		this.deliverid = deliverid;
	}

	public String getDeliverName() {
		return deliverName;
	}

	public void setDeliverName(String deliverName) {
		this.deliverName = deliverName;
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

	public String getZexpressnum() {
		return zexpressnum;
	}

	public void setZexpressnum(String zexpressnum) {
		this.zexpressnum = zexpressnum;
	}

	public String getBigstatus() {
		return bigstatus;
	}

	public void setBigstatus(String bigstatus) {
		this.bigstatus = bigstatus;
	}

	public String getSmallstatus() {
		return smallstatus;
	}

	public void setSmallstatus(String smallstatus) {
		this.smallstatus = smallstatus;
	}

	public Double getSystemWeight() {
		return systemWeight;
	}

	public void setSystemWeight(Double systemWeight) {
		this.systemWeight = systemWeight;
	}

	public Integer getTotalqty() {
		return totalqty;
	}

	public void setTotalqty(Integer totalqty) {
		this.totalqty = totalqty;
	}

	public String getProductDetail() {
		return productDetail;
	}

	public void setProductDetail(String productDetail) {
		this.productDetail = productDetail;
	}

	public String getExpresstype() {
		return expresstype;
	}

	public void setExpresstype(String expresstype) {
		this.expresstype = expresstype;
	}

	public Integer getSkuNum() {
		return skuNum;
	}

	public void setSkuNum(Integer skuNum) {
		this.skuNum = skuNum;
	}

	public String getExtattr1() {
		return extattr1;
	}

	public void setExtattr1(String extattr1) {
		this.extattr1 = extattr1;
	}

	public String getExtattr2() {
		return extattr2;
	}

	public void setExtattr2(String extattr2) {
		this.extattr2 = extattr2;
	}

	public String getExtattr3() {
		return extattr3;
	}

	public void setExtattr3(String extattr3) {
		this.extattr3 = extattr3;
	}

	public String getExtattr4() {
		return extattr4;
	}

	public void setExtattr4(String extattr4) {
		this.extattr4 = extattr4;
	}

	public String getExtattr5() {
		return extattr5;
	}

	public void setExtattr5(String extattr5) {
		this.extattr5 = extattr5;
	}

	public String getReceiveProvinceName() {
		return receiveProvinceName;
	}

	public void setReceiveProvinceName(String receiveProvinceName) {
		this.receiveProvinceName = receiveProvinceName;
	}

	public String getReceiveCityName() {
		return receiveCityName;
	}

	public void setReceiveCityName(String receiveCityName) {
		this.receiveCityName = receiveCityName;
	}

	public String getReceiveDistrictName() {
		return receiveDistrictName;
	}

	public void setReceiveDistrictName(String receiveDistrictName) {
		this.receiveDistrictName = receiveDistrictName;
	}

	public Integer getWorkcount() {
		return workcount;
	}

	public void setWorkcount(Integer workcount) {
		this.workcount = workcount;
	}

	public String getWarehouseName() {
		return warehouseName;
	}

	public void setWarehouseName(String warehouseName) {
		this.warehouseName = warehouseName;
	}

	public String getTimeliness() {
		return timeliness;
	}

	public void setTimeliness(String timeliness) {
		this.timeliness = timeliness;
	}

	public List<PriceOutMainDispatchEntity> getPriceList() {
		return priceList;
	}

	public void setPriceList(List<PriceOutMainDispatchEntity> priceList) {
		this.priceList = priceList;
	}

	public String getAdjustProvinceId() {
		return adjustProvinceId;
	}

	public void setAdjustProvinceId(String adjustProvinceId) {
		this.adjustProvinceId = adjustProvinceId;
	}

	public String getAdjustCityId() {
		return adjustCityId;
	}

	public void setAdjustCityId(String adjustCityId) {
		this.adjustCityId = adjustCityId;
	}

	public String getAdjustDistrictId() {
		return adjustDistrictId;
	}

	public void setAdjustDistrictId(String adjustDistrictId) {
		this.adjustDistrictId = adjustDistrictId;
	}

	public Double getThrowWeight() {
		return throwWeight;
	}

	public void setThrowWeight(Double throwWeight) {
		this.throwWeight = throwWeight;
	}

	public Double getOriginWeight() {
		return originWeight;
	}

	public void setOriginWeight(Double originWeight) {
		this.originWeight = originWeight;
	}

	public Double getNewTotalWeight() {
		return newTotalWeight;
	}

	public void setNewTotalWeight(Double newTotalWeight) {
		this.newTotalWeight = newTotalWeight;
	}

	public String getServicename() {
		return servicename;
	}

	public void setServicename(String servicename) {
		this.servicename = servicename;
	}

	public Double getOriginThrowWeight() {
		return originThrowWeight;
	}

	public void setOriginThrowWeight(Double originThrowWeight) {
		this.originThrowWeight = originThrowWeight;
	}
    
	
}
