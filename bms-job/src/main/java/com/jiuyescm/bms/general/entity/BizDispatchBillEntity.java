package com.jiuyescm.bms.general.entity;

import java.sql.Timestamp;
import java.math.BigDecimal;
import com.jiuyescm.cfm.domain.IEntity;

 /**
 * ..Entity
 * @author wangchen
 * 
 */
public class BizDispatchBillEntity implements IEntity {
    
	/**
	 * 
	 */
	private static final long serialVersionUID = 7894199222036114095L;
	// id
	private Long id;
	// 出库单号
	private String outstockNo;
	// 外部单号
	private String externalNo;
	// 运单号
	private String waybillNo;
	// 费用编号
	private String feesNo;
	// WaybillNum
	private BigDecimal waybillNum;
	// 运单列表
	private String waybillList;
	// 仓库id
	private String warehouseCode;
	// 仓库名称
	private String warehouseName;
	// 商家id
	private String customerid;
	// 商家名称
	private String customerName;
	// 物流商ID
	private String carrierId;
	// 物流商名称
	private String carrierName;
	// 宅配商id
	private String deliverid;
	// 宅配商名称
	private String deliverName;
	// TotalWeight
	private BigDecimal totalWeight;
	// AdjustWeight
	private BigDecimal adjustWeight;
	// TotalVolume
	private BigDecimal totalVolume;
	// 服务类型
	private String serviceTypeCode;
	// 配送类型
	private String dispatchTypeCode;
	// CollectMoney
	private BigDecimal collectMoney;
	// 月结账号
	private String monthFeeCount;
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
	// 结算状态
	private String accountState;
	// 签收时间
	private Timestamp signTime;
	// 揽收时间
	private Timestamp acceptTime;
	// 收件时间
	private Timestamp receiveTime;
	// 是否已计算费用 0-未结算 1-已结算 2-结算异常 3-合同不存在 4-不在操作范围 5-其他
	private String isCalculated;
	// 创建者
	private String creator;
	// 单据创建时间
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
	// 温度类型
	private String temperatureTypeCode;
	// 温度类型名称
	private String temperatureTypeName;
	// remark
	private String remark;
	// 转寄后运单号
	private String zexpressnum;
	// 大状态
	private String bigstatus;
	// 小状态
	private String smallstatus;
	// SystemWeight
	private BigDecimal systemWeight;
	// 抛重
	private BigDecimal throwWeight;
	// totalqty
	private Integer totalqty;
	// 商品明细
	private Long productDetail;
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
	// 调整宅配商id
	private String adjustDeliverId;
	// 调整宅配商名称
	private String adjustDeliverName;
	// 调整物流商ID
	private String adjustCarrierId;
	// 调整物流商名称
	private String adjustCarrierName;
	// BizType
	private String bizType;
	// 调整省
	private String adjustProvinceId;
	// 调整市
	private String adjustCityId;
	// 调整区
	private String adjustDistrictId;
	// 订单物流商名称
	private String originCarrierName;
	// 订单物流商ID
	private String originCarrierId;
	// 发件省
	private String senderProvince;
	// 发件市
	private String senderCity;
	// 发件区
	private String senderDistrict;

	public BizDispatchBillEntity() {
		super();
	}
	
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public String getOutstockNo() {
		return this.outstockNo;
	}

	public void setOutstockNo(String outstockNo) {
		this.outstockNo = outstockNo;
	}
	
	public String getExternalNo() {
		return this.externalNo;
	}

	public void setExternalNo(String externalNo) {
		this.externalNo = externalNo;
	}
	
	public String getWaybillNo() {
		return this.waybillNo;
	}

	public void setWaybillNo(String waybillNo) {
		this.waybillNo = waybillNo;
	}
	
	public String getFeesNo() {
		return this.feesNo;
	}

	public void setFeesNo(String feesNo) {
		this.feesNo = feesNo;
	}
	
	public BigDecimal getWaybillNum() {
		return this.waybillNum;
	}

	public void setWaybillNum(BigDecimal waybillNum) {
		this.waybillNum = waybillNum;
	}
	
	public String getWaybillList() {
		return this.waybillList;
	}

	public void setWaybillList(String waybillList) {
		this.waybillList = waybillList;
	}
	
	public String getWarehouseCode() {
		return this.warehouseCode;
	}

	public void setWarehouseCode(String warehouseCode) {
		this.warehouseCode = warehouseCode;
	}
	
	public String getWarehouseName() {
		return this.warehouseName;
	}

	public void setWarehouseName(String warehouseName) {
		this.warehouseName = warehouseName;
	}
	
	public String getCustomerid() {
		return this.customerid;
	}

	public void setCustomerid(String customerid) {
		this.customerid = customerid;
	}
	
	public String getCustomerName() {
		return this.customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	
	public String getCarrierId() {
		return this.carrierId;
	}

	public void setCarrierId(String carrierId) {
		this.carrierId = carrierId;
	}
	
	public String getCarrierName() {
		return this.carrierName;
	}

	public void setCarrierName(String carrierName) {
		this.carrierName = carrierName;
	}
	
	public String getDeliverid() {
		return this.deliverid;
	}

	public void setDeliverid(String deliverid) {
		this.deliverid = deliverid;
	}
	
	public String getDeliverName() {
		return this.deliverName;
	}

	public void setDeliverName(String deliverName) {
		this.deliverName = deliverName;
	}
	
	public BigDecimal getTotalWeight() {
		return this.totalWeight;
	}

	public void setTotalWeight(BigDecimal totalWeight) {
		this.totalWeight = totalWeight;
	}
	
	public BigDecimal getAdjustWeight() {
		return this.adjustWeight;
	}

	public void setAdjustWeight(BigDecimal adjustWeight) {
		this.adjustWeight = adjustWeight;
	}
	
	public BigDecimal getTotalVolume() {
		return this.totalVolume;
	}

	public void setTotalVolume(BigDecimal totalVolume) {
		this.totalVolume = totalVolume;
	}
	
	public String getServiceTypeCode() {
		return this.serviceTypeCode;
	}

	public void setServiceTypeCode(String serviceTypeCode) {
		this.serviceTypeCode = serviceTypeCode;
	}
	
	public String getDispatchTypeCode() {
		return this.dispatchTypeCode;
	}

	public void setDispatchTypeCode(String dispatchTypeCode) {
		this.dispatchTypeCode = dispatchTypeCode;
	}
	
	public BigDecimal getCollectMoney() {
		return this.collectMoney;
	}

	public void setCollectMoney(BigDecimal collectMoney) {
		this.collectMoney = collectMoney;
	}
	
	public String getMonthFeeCount() {
		return this.monthFeeCount;
	}

	public void setMonthFeeCount(String monthFeeCount) {
		this.monthFeeCount = monthFeeCount;
	}
	
	public String getSendName() {
		return this.sendName;
	}

	public void setSendName(String sendName) {
		this.sendName = sendName;
	}
	
	public String getSendProvinceId() {
		return this.sendProvinceId;
	}

	public void setSendProvinceId(String sendProvinceId) {
		this.sendProvinceId = sendProvinceId;
	}
	
	public String getSendCityId() {
		return this.sendCityId;
	}

	public void setSendCityId(String sendCityId) {
		this.sendCityId = sendCityId;
	}
	
	public String getSendDistrictId() {
		return this.sendDistrictId;
	}

	public void setSendDistrictId(String sendDistrictId) {
		this.sendDistrictId = sendDistrictId;
	}
	
	public String getSendStreet() {
		return this.sendStreet;
	}

	public void setSendStreet(String sendStreet) {
		this.sendStreet = sendStreet;
	}
	
	public String getSendDetailAddress() {
		return this.sendDetailAddress;
	}

	public void setSendDetailAddress(String sendDetailAddress) {
		this.sendDetailAddress = sendDetailAddress;
	}
	
	public String getReceiveName() {
		return this.receiveName;
	}

	public void setReceiveName(String receiveName) {
		this.receiveName = receiveName;
	}
	
	public String getReceiveProvinceId() {
		return this.receiveProvinceId;
	}

	public void setReceiveProvinceId(String receiveProvinceId) {
		this.receiveProvinceId = receiveProvinceId;
	}
	
	public String getReceiveCityId() {
		return this.receiveCityId;
	}

	public void setReceiveCityId(String receiveCityId) {
		this.receiveCityId = receiveCityId;
	}
	
	public String getReceiveDistrictId() {
		return this.receiveDistrictId;
	}

	public void setReceiveDistrictId(String receiveDistrictId) {
		this.receiveDistrictId = receiveDistrictId;
	}
	
	public String getReceiveStreet() {
		return this.receiveStreet;
	}

	public void setReceiveStreet(String receiveStreet) {
		this.receiveStreet = receiveStreet;
	}
	
	public String getReceiveDetailAddress() {
		return this.receiveDetailAddress;
	}

	public void setReceiveDetailAddress(String receiveDetailAddress) {
		this.receiveDetailAddress = receiveDetailAddress;
	}
	
	public String getAccountState() {
		return this.accountState;
	}

	public void setAccountState(String accountState) {
		this.accountState = accountState;
	}
	
	public Timestamp getSignTime() {
		return this.signTime;
	}

	public void setSignTime(Timestamp signTime) {
		this.signTime = signTime;
	}
	
	public Timestamp getAcceptTime() {
		return this.acceptTime;
	}

	public void setAcceptTime(Timestamp acceptTime) {
		this.acceptTime = acceptTime;
	}
	
	public Timestamp getReceiveTime() {
		return this.receiveTime;
	}

	public void setReceiveTime(Timestamp receiveTime) {
		this.receiveTime = receiveTime;
	}
	
	public String getIsCalculated() {
		return this.isCalculated;
	}

	public void setIsCalculated(String isCalculated) {
		this.isCalculated = isCalculated;
	}
	
	public String getCreator() {
		return this.creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}
	
	public Timestamp getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}
	
	public String getLastModifier() {
		return this.lastModifier;
	}

	public void setLastModifier(String lastModifier) {
		this.lastModifier = lastModifier;
	}
	
	public Timestamp getLastModifyTime() {
		return this.lastModifyTime;
	}

	public void setLastModifyTime(Timestamp lastModifyTime) {
		this.lastModifyTime = lastModifyTime;
	}
	
	public Timestamp getWriteTime() {
		return this.writeTime;
	}

	public void setWriteTime(Timestamp writeTime) {
		this.writeTime = writeTime;
	}
	
	public Timestamp getCalculateTime() {
		return this.calculateTime;
	}

	public void setCalculateTime(Timestamp calculateTime) {
		this.calculateTime = calculateTime;
	}
	
	public String getDelFlag() {
		return this.delFlag;
	}

	public void setDelFlag(String delFlag) {
		this.delFlag = delFlag;
	}
	
	public String getTemperatureTypeCode() {
		return this.temperatureTypeCode;
	}

	public void setTemperatureTypeCode(String temperatureTypeCode) {
		this.temperatureTypeCode = temperatureTypeCode;
	}
	
	public String getTemperatureTypeName() {
		return this.temperatureTypeName;
	}

	public void setTemperatureTypeName(String temperatureTypeName) {
		this.temperatureTypeName = temperatureTypeName;
	}
	
	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	public String getZexpressnum() {
		return this.zexpressnum;
	}

	public void setZexpressnum(String zexpressnum) {
		this.zexpressnum = zexpressnum;
	}
	
	public String getBigstatus() {
		return this.bigstatus;
	}

	public void setBigstatus(String bigstatus) {
		this.bigstatus = bigstatus;
	}
	
	public String getSmallstatus() {
		return this.smallstatus;
	}

	public void setSmallstatus(String smallstatus) {
		this.smallstatus = smallstatus;
	}
	
	public BigDecimal getSystemWeight() {
		return this.systemWeight;
	}

	public void setSystemWeight(BigDecimal systemWeight) {
		this.systemWeight = systemWeight;
	}
	
	public BigDecimal getThrowWeight() {
		return this.throwWeight;
	}

	public void setThrowWeight(BigDecimal throwWeight) {
		this.throwWeight = throwWeight;
	}
	
	public Integer getTotalqty() {
		return this.totalqty;
	}

	public void setTotalqty(Integer totalqty) {
		this.totalqty = totalqty;
	}
	
	public Long getProductDetail() {
		return this.productDetail;
	}

	public void setProductDetail(Long productDetail) {
		this.productDetail = productDetail;
	}
	
	public String getExpresstype() {
		return this.expresstype;
	}

	public void setExpresstype(String expresstype) {
		this.expresstype = expresstype;
	}
	
	public Integer getSkuNum() {
		return this.skuNum;
	}

	public void setSkuNum(Integer skuNum) {
		this.skuNum = skuNum;
	}
	
	public String getExtattr1() {
		return this.extattr1;
	}

	public void setExtattr1(String extattr1) {
		this.extattr1 = extattr1;
	}
	
	public String getExtattr2() {
		return this.extattr2;
	}

	public void setExtattr2(String extattr2) {
		this.extattr2 = extattr2;
	}
	
	public String getExtattr3() {
		return this.extattr3;
	}

	public void setExtattr3(String extattr3) {
		this.extattr3 = extattr3;
	}
	
	public String getExtattr4() {
		return this.extattr4;
	}

	public void setExtattr4(String extattr4) {
		this.extattr4 = extattr4;
	}
	
	public String getExtattr5() {
		return this.extattr5;
	}

	public void setExtattr5(String extattr5) {
		this.extattr5 = extattr5;
	}
	
	public String getAdjustDeliverId() {
		return this.adjustDeliverId;
	}

	public void setAdjustDeliverId(String adjustDeliverId) {
		this.adjustDeliverId = adjustDeliverId;
	}
	
	public String getAdjustDeliverName() {
		return this.adjustDeliverName;
	}

	public void setAdjustDeliverName(String adjustDeliverName) {
		this.adjustDeliverName = adjustDeliverName;
	}
	
	public String getAdjustCarrierId() {
		return this.adjustCarrierId;
	}

	public void setAdjustCarrierId(String adjustCarrierId) {
		this.adjustCarrierId = adjustCarrierId;
	}
	
	public String getAdjustCarrierName() {
		return this.adjustCarrierName;
	}

	public void setAdjustCarrierName(String adjustCarrierName) {
		this.adjustCarrierName = adjustCarrierName;
	}
	
	public String getBizType() {
		return this.bizType;
	}

	public void setBizType(String bizType) {
		this.bizType = bizType;
	}
	
	public String getAdjustProvinceId() {
		return this.adjustProvinceId;
	}

	public void setAdjustProvinceId(String adjustProvinceId) {
		this.adjustProvinceId = adjustProvinceId;
	}
	
	public String getAdjustCityId() {
		return this.adjustCityId;
	}

	public void setAdjustCityId(String adjustCityId) {
		this.adjustCityId = adjustCityId;
	}
	
	public String getAdjustDistrictId() {
		return this.adjustDistrictId;
	}

	public void setAdjustDistrictId(String adjustDistrictId) {
		this.adjustDistrictId = adjustDistrictId;
	}
	
	public String getOriginCarrierName() {
		return this.originCarrierName;
	}

	public void setOriginCarrierName(String originCarrierName) {
		this.originCarrierName = originCarrierName;
	}
	
	public String getOriginCarrierId() {
		return this.originCarrierId;
	}

	public void setOriginCarrierId(String originCarrierId) {
		this.originCarrierId = originCarrierId;
	}
	
	public String getSenderProvince() {
		return this.senderProvince;
	}

	public void setSenderProvince(String senderProvince) {
		this.senderProvince = senderProvince;
	}
	
	public String getSenderCity() {
		return this.senderCity;
	}

	public void setSenderCity(String senderCity) {
		this.senderCity = senderCity;
	}
	
	public String getSenderDistrict() {
		return this.senderDistrict;
	}

	public void setSenderDistrict(String senderDistrict) {
		this.senderDistrict = senderDistrict;
	}
    
}
