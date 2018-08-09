package com.jiuyescm.bms.biz.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.security.Timestamp;

/**
 * 配送数据vo
 * @author caojianwei
 *
 */
public class BmsDispatchVo implements Serializable{

	private static final long serialVersionUID = -2349987740911795503L;

	
	// 出库单号
		private String outstockNo;
		// 运单号
		private String waybillNo;
		// 转寄后运单号
		private String originZexpressnum;
		// 外部单号
		private String externalNo;
		// 仓库编号
		private String warehouseCode;
		// 商家编号
		private String customerId;
		// 物流商编号
		private String originCarrierId;
		// 转寄物流商id
		private String forwardCarrierId;
		// 调整物流商id
		private String adjustCarrierId;
		// 宅配商编号
		private String originDeliverId;
		// 转寄宅配商id
		private String forwardDeliverId;
		// 调整宅配商id
		private String adjustDeliverId;
		// 温度类型
		private String temperatureTypeCode;
		// 是否拆箱
		private String splitboxFlag;
		// B2B标识
		private String b2bFlag;
		// 月结账号
		private String originMonthFeeCount;
		// 月结账号
		private String monthFeeCount;
		// 商品明细
		private Long productDetail;
		// 服务类型
		private String originServiceTypeCode;
		// 快递服务类型
		private String serviceTypeCode;
		// 系统重量/逻辑重量
		private BigDecimal systemWeight;
		// 称重重量
		private BigDecimal originWeight;
		// 订单重量
		private BigDecimal totalWeight;
		// 订单抛重
		private BigDecimal originThrowWeight;
		// 抛重
		private BigDecimal throwWeight;
		// 纠正抛重
		private BigDecimal correctThrowWeight;
		// 纠正重量
		private BigDecimal correctWeight;
		// 调整重量
		private BigDecimal adjustWeight;
		// 商品数量
		private BigDecimal totalQty;
		// 调整商品数量
		private BigDecimal adjustQty;
		// sku数量
		private BigDecimal totalSku;
		// 体积
		private BigDecimal totalVolume;
		// 箱数
		private BigDecimal totalBox;
		// 寄件人省份
		private String originSendProvince;
		// 调整发件省
		private String adjustSendProvince;
		// 寄件人城市
		private String originSendCity;
		// 调整发件市
		private String adjustSendCity;
		// 寄件人地区
		private String originSendArea;
		// 调整发件区
		private String adjustSendArea;
		// 收件人省份
		private String originReceiveProvince;
		// 调整收件省
		private String adjustReceiveProvince;
		// 收件人城市
		private String originReceiveCity;
		// 调整收件市
		private String adjustReceiveCity;
		// 收件人地区
		private String originReceiveArea;
		// 调整收件区
		private String adjustReceiveArea;
		// 业务时间
		private Timestamp createTime;
		// 写入BMS时间
		private Timestamp writeTime;
		// 出库时间
		private Timestamp outingTime;
		// 修改者id
		private String lastModifierId;
		// 修改者
		private String lastModifier;
		// 修改时间
		private Timestamp lastModifyTime;
		// 订单状态
		private String orderStatus;
		// 作废标识
		private String delFlag;
		// 费用科目
		private String subjectCode;
		// 计费商品数量
		private BigDecimal chargeQty;
		// 计费sku数量
		private BigDecimal chargeSku;
		// 计费箱数
		private BigDecimal chargeBox;
		// 计费重量
		private BigDecimal chargeWeight;
		// 计费物流商id
		private String chargeCarrierId;
		// 计费报价ID
		private String quoteId;
		// 金额
		private BigDecimal amount;
		// 减免金额
		private BigDecimal derateAmount;
		// 计算状态 0-未计算 1-计算成功 2-计算异常 3-不计算
		private String isCalculated;
		// 备注
		private String remark;
		// 计算说明
		private String calcuMsg;
		// 计算时间
		private Timestamp calcuTime;

		public BmsDispatchVo() {

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
		public String getOriginZexpressnum() {
			return this.originZexpressnum;
		}

	    /**
	     * 转寄后运单号
	     *
	     * @param originZexpressnum
	     */
		public void setOriginZexpressnum(String originZexpressnum) {
			this.originZexpressnum = originZexpressnum;
		}
		
		/**
	     * 外部单号
	     */
		public String getExternalNo() {
			return this.externalNo;
		}

	    /**
	     * 外部单号
	     *
	     * @param externalNo
	     */
		public void setExternalNo(String externalNo) {
			this.externalNo = externalNo;
		}
		
		/**
	     * 仓库编号
	     */
		public String getWarehouseCode() {
			return this.warehouseCode;
		}

	    /**
	     * 仓库编号
	     *
	     * @param warehouseCode
	     */
		public void setWarehouseCode(String warehouseCode) {
			this.warehouseCode = warehouseCode;
		}
		
		/**
	     * 商家编号
	     */
		public String getCustomerId() {
			return this.customerId;
		}

	    /**
	     * 商家编号
	     *
	     * @param customerId
	     */
		public void setCustomerId(String customerId) {
			this.customerId = customerId;
		}
		
		/**
	     * 物流商编号
	     */
		public String getOriginCarrierId() {
			return this.originCarrierId;
		}

	    /**
	     * 物流商编号
	     *
	     * @param originCarrierId
	     */
		public void setOriginCarrierId(String originCarrierId) {
			this.originCarrierId = originCarrierId;
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
	     * 宅配商编号
	     */
		public String getOriginDeliverId() {
			return this.originDeliverId;
		}

	    /**
	     * 宅配商编号
	     *
	     * @param originDeliverId
	     */
		public void setOriginDeliverId(String originDeliverId) {
			this.originDeliverId = originDeliverId;
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
	     * 是否拆箱
	     */
		public String getSplitboxFlag() {
			return this.splitboxFlag;
		}

	    /**
	     * 是否拆箱
	     *
	     * @param splitboxFlag
	     */
		public void setSplitboxFlag(String splitboxFlag) {
			this.splitboxFlag = splitboxFlag;
		}
		
		/**
	     * B2B标识
	     */
		public String getB2bFlag() {
			return this.b2bFlag;
		}

	    /**
	     * B2B标识
	     *
	     * @param b2bFlag
	     */
		public void setB2bFlag(String b2bFlag) {
			this.b2bFlag = b2bFlag;
		}
		
		/**
	     * 月结账号
	     */
		public String getOriginMonthFeeCount() {
			return this.originMonthFeeCount;
		}

	    /**
	     * 月结账号
	     *
	     * @param originMonthFeeCount
	     */
		public void setOriginMonthFeeCount(String originMonthFeeCount) {
			this.originMonthFeeCount = originMonthFeeCount;
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
	     * 商品明细
	     */
		public Long getProductDetail() {
			return this.productDetail;
		}

	    /**
	     * 商品明细
	     *
	     * @param productDetail
	     */
		public void setProductDetail(Long productDetail) {
			this.productDetail = productDetail;
		}
		
		/**
	     * 服务类型
	     */
		public String getOriginServiceTypeCode() {
			return this.originServiceTypeCode;
		}

	    /**
	     * 服务类型
	     *
	     * @param originServiceTypeCode
	     */
		public void setOriginServiceTypeCode(String originServiceTypeCode) {
			this.originServiceTypeCode = originServiceTypeCode;
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
	     * 系统重量/逻辑重量
	     */
		public BigDecimal getSystemWeight() {
			return this.systemWeight;
		}

	    /**
	     * 系统重量/逻辑重量
	     *
	     * @param systemWeight
	     */
		public void setSystemWeight(BigDecimal systemWeight) {
			this.systemWeight = systemWeight;
		}
		
		/**
	     * 称重重量
	     */
		public BigDecimal getOriginWeight() {
			return this.originWeight;
		}

	    /**
	     * 称重重量
	     *
	     * @param originWeight
	     */
		public void setOriginWeight(BigDecimal originWeight) {
			this.originWeight = originWeight;
		}
		
		/**
	     * 订单重量
	     */
		public BigDecimal getTotalWeight() {
			return this.totalWeight;
		}

	    /**
	     * 订单重量
	     *
	     * @param totalWeight
	     */
		public void setTotalWeight(BigDecimal totalWeight) {
			this.totalWeight = totalWeight;
		}
		
		/**
	     * 订单抛重
	     */
		public BigDecimal getOriginThrowWeight() {
			return this.originThrowWeight;
		}

	    /**
	     * 订单抛重
	     *
	     * @param originThrowWeight
	     */
		public void setOriginThrowWeight(BigDecimal originThrowWeight) {
			this.originThrowWeight = originThrowWeight;
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
	     * 商品数量
	     */
		public BigDecimal getTotalQty() {
			return this.totalQty;
		}

	    /**
	     * 商品数量
	     *
	     * @param totalQty
	     */
		public void setTotalQty(BigDecimal totalQty) {
			this.totalQty = totalQty;
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
	     * sku数量
	     */
		public BigDecimal getTotalSku() {
			return this.totalSku;
		}

	    /**
	     * sku数量
	     *
	     * @param totalSku
	     */
		public void setTotalSku(BigDecimal totalSku) {
			this.totalSku = totalSku;
		}
		
		/**
	     * 体积
	     */
		public BigDecimal getTotalVolume() {
			return this.totalVolume;
		}

	    /**
	     * 体积
	     *
	     * @param totalVolume
	     */
		public void setTotalVolume(BigDecimal totalVolume) {
			this.totalVolume = totalVolume;
		}
		
		/**
	     * 箱数
	     */
		public BigDecimal getTotalBox() {
			return this.totalBox;
		}

	    /**
	     * 箱数
	     *
	     * @param totalBox
	     */
		public void setTotalBox(BigDecimal totalBox) {
			this.totalBox = totalBox;
		}
		
		/**
	     * 寄件人省份
	     */
		public String getOriginSendProvince() {
			return this.originSendProvince;
		}

	    /**
	     * 寄件人省份
	     *
	     * @param originSendProvince
	     */
		public void setOriginSendProvince(String originSendProvince) {
			this.originSendProvince = originSendProvince;
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
	     * 寄件人城市
	     */
		public String getOriginSendCity() {
			return this.originSendCity;
		}

	    /**
	     * 寄件人城市
	     *
	     * @param originSendCity
	     */
		public void setOriginSendCity(String originSendCity) {
			this.originSendCity = originSendCity;
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
	     * 寄件人地区
	     */
		public String getOriginSendArea() {
			return this.originSendArea;
		}

	    /**
	     * 寄件人地区
	     *
	     * @param originSendArea
	     */
		public void setOriginSendArea(String originSendArea) {
			this.originSendArea = originSendArea;
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
	     * 收件人省份
	     */
		public String getOriginReceiveProvince() {
			return this.originReceiveProvince;
		}

	    /**
	     * 收件人省份
	     *
	     * @param originReceiveProvince
	     */
		public void setOriginReceiveProvince(String originReceiveProvince) {
			this.originReceiveProvince = originReceiveProvince;
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
	     * 收件人城市
	     */
		public String getOriginReceiveCity() {
			return this.originReceiveCity;
		}

	    /**
	     * 收件人城市
	     *
	     * @param originReceiveCity
	     */
		public void setOriginReceiveCity(String originReceiveCity) {
			this.originReceiveCity = originReceiveCity;
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
	     * 收件人地区
	     */
		public String getOriginReceiveArea() {
			return this.originReceiveArea;
		}

	    /**
	     * 收件人地区
	     *
	     * @param originReceiveArea
	     */
		public void setOriginReceiveArea(String originReceiveArea) {
			this.originReceiveArea = originReceiveArea;
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
	     * 业务时间
	     */
		public Timestamp getCreateTime() {
			return this.createTime;
		}

	    /**
	     * 业务时间
	     *
	     * @param createTime
	     */
		public void setCreateTime(Timestamp createTime) {
			this.createTime = createTime;
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
	     * 出库时间
	     */
		public Timestamp getOutingTime() {
			return this.outingTime;
		}

	    /**
	     * 出库时间
	     *
	     * @param outingTime
	     */
		public void setOutingTime(Timestamp outingTime) {
			this.outingTime = outingTime;
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
	     * 费用科目
	     */
		public String getSubjectCode() {
			return this.subjectCode;
		}

	    /**
	     * 费用科目
	     *
	     * @param subjectCode
	     */
		public void setSubjectCode(String subjectCode) {
			this.subjectCode = subjectCode;
		}
		
		/**
	     * 计费商品数量
	     */
		public BigDecimal getChargeQty() {
			return this.chargeQty;
		}

	    /**
	     * 计费商品数量
	     *
	     * @param chargeQty
	     */
		public void setChargeQty(BigDecimal chargeQty) {
			this.chargeQty = chargeQty;
		}
		
		/**
	     * 计费sku数量
	     */
		public BigDecimal getChargeSku() {
			return this.chargeSku;
		}

	    /**
	     * 计费sku数量
	     *
	     * @param chargeSku
	     */
		public void setChargeSku(BigDecimal chargeSku) {
			this.chargeSku = chargeSku;
		}
		
		/**
	     * 计费箱数
	     */
		public BigDecimal getChargeBox() {
			return this.chargeBox;
		}

	    /**
	     * 计费箱数
	     *
	     * @param chargeBox
	     */
		public void setChargeBox(BigDecimal chargeBox) {
			this.chargeBox = chargeBox;
		}
		
		/**
	     * 计费重量
	     */
		public BigDecimal getChargeWeight() {
			return this.chargeWeight;
		}

	    /**
	     * 计费重量
	     *
	     * @param chargeWeight
	     */
		public void setChargeWeight(BigDecimal chargeWeight) {
			this.chargeWeight = chargeWeight;
		}
		
		/**
	     * 计费物流商id
	     */
		public String getChargeCarrierId() {
			return this.chargeCarrierId;
		}

	    /**
	     * 计费物流商id
	     *
	     * @param chargeCarrierId
	     */
		public void setChargeCarrierId(String chargeCarrierId) {
			this.chargeCarrierId = chargeCarrierId;
		}
		
		/**
	     * 计费报价ID
	     */
		public String getQuoteId() {
			return this.quoteId;
		}

	    /**
	     * 计费报价ID
	     *
	     * @param quoteId
	     */
		public void setQuoteId(String quoteId) {
			this.quoteId = quoteId;
		}
		
		/**
	     * 金额
	     */
		public BigDecimal getAmount() {
			return this.amount;
		}

	    /**
	     * 金额
	     *
	     * @param amount
	     */
		public void setAmount(BigDecimal amount) {
			this.amount = amount;
		}
		
		/**
	     * 减免金额
	     */
		public BigDecimal getDerateAmount() {
			return this.derateAmount;
		}

	    /**
	     * 减免金额
	     *
	     * @param derateAmount
	     */
		public void setDerateAmount(BigDecimal derateAmount) {
			this.derateAmount = derateAmount;
		}
		
		/**
	     * 计算状态 0-未计算 1-计算成功 2-计算异常 3-不计算
	     */
		public String getIsCalculated() {
			return this.isCalculated;
		}

	    /**
	     * 计算状态 0-未计算 1-计算成功 2-计算异常 3-不计算
	     *
	     * @param isCalculated
	     */
		public void setIsCalculated(String isCalculated) {
			this.isCalculated = isCalculated;
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
	     * 计算说明
	     */
		public String getCalcuMsg() {
			return this.calcuMsg;
		}

	    /**
	     * 计算说明
	     *
	     * @param calcuMsg
	     */
		public void setCalcuMsg(String calcuMsg) {
			this.calcuMsg = calcuMsg;
		}
		
		/**
	     * 计算时间
	     */
		public Timestamp getCalcuTime() {
			return this.calcuTime;
		}

	    /**
	     * 计算时间
	     *
	     * @param calcuTime
	     */
		public void setCalcuTime(Timestamp calcuTime) {
			this.calcuTime = calcuTime;
		}
	    
}
