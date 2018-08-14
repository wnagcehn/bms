package com.jiuyescm.bms.biz.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

public class OutstockInfoVo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1513667950684308362L;

	// 自增主键
		private Long id;
		// 运单号
		private String waybillNo;
		// 转寄后运单号
		private String zexpressnum;
		// 月结账号
		private String monthFeeCount;
		// 服务类型
		private String serviceTypeCode;
		// 配送类型
		private String dispatchTypeCode;
		// 快递业务类型
		private String expresstype;
		// 抛重
		private BigDecimal throwWeight;
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
		// 创建人
		private String lastModifier;
		// 创建人ID
		private String lastModifierId;
		// LastModifyTime
		private Timestamp lastModifyTime;
		// 创建时间
		private Timestamp createTime;
		// 计算状态 0-未处理 1-处理完成 2-系统异常
		private String isCalculated;
		public Long getId() {
			return id;
		}
		public void setId(Long id) {
			this.id = id;
		}
		public String getWaybillNo() {
			return waybillNo;
		}
		public void setWaybillNo(String waybillNo) {
			this.waybillNo = waybillNo;
		}
		public String getZexpressnum() {
			return zexpressnum;
		}
		public void setZexpressnum(String zexpressnum) {
			this.zexpressnum = zexpressnum;
		}
		public String getMonthFeeCount() {
			return monthFeeCount;
		}
		public void setMonthFeeCount(String monthFeeCount) {
			this.monthFeeCount = monthFeeCount;
		}
		public String getServiceTypeCode() {
			return serviceTypeCode;
		}
		public void setServiceTypeCode(String serviceTypeCode) {
			this.serviceTypeCode = serviceTypeCode;
		}
		public String getDispatchTypeCode() {
			return dispatchTypeCode;
		}
		public void setDispatchTypeCode(String dispatchTypeCode) {
			this.dispatchTypeCode = dispatchTypeCode;
		}
		public String getExpresstype() {
			return expresstype;
		}
		public void setExpresstype(String expresstype) {
			this.expresstype = expresstype;
		}
		public BigDecimal getThrowWeight() {
			return throwWeight;
		}
		public void setThrowWeight(BigDecimal throwWeight) {
			this.throwWeight = throwWeight;
		}
		public BigDecimal getAdjustWeight() {
			return adjustWeight;
		}
		public void setAdjustWeight(BigDecimal adjustWeight) {
			this.adjustWeight = adjustWeight;
		}
		public BigDecimal getCorrectWeight() {
			return correctWeight;
		}
		public void setCorrectWeight(BigDecimal correctWeight) {
			this.correctWeight = correctWeight;
		}
		public BigDecimal getAdjustBox() {
			return adjustBox;
		}
		public void setAdjustBox(BigDecimal adjustBox) {
			this.adjustBox = adjustBox;
		}
		public BigDecimal getAdjustQty() {
			return adjustQty;
		}
		public void setAdjustQty(BigDecimal adjustQty) {
			this.adjustQty = adjustQty;
		}
		public String getAdjustSendProvince() {
			return adjustSendProvince;
		}
		public void setAdjustSendProvince(String adjustSendProvince) {
			this.adjustSendProvince = adjustSendProvince;
		}
		public String getAdjustSendCity() {
			return adjustSendCity;
		}
		public void setAdjustSendCity(String adjustSendCity) {
			this.adjustSendCity = adjustSendCity;
		}
		public String getAdjustSendArea() {
			return adjustSendArea;
		}
		public void setAdjustSendArea(String adjustSendArea) {
			this.adjustSendArea = adjustSendArea;
		}
		public String getAdjustReceiveProvince() {
			return adjustReceiveProvince;
		}
		public void setAdjustReceiveProvince(String adjustReceiveProvince) {
			this.adjustReceiveProvince = adjustReceiveProvince;
		}
		public String getAdjustReceiveCity() {
			return adjustReceiveCity;
		}
		public void setAdjustReceiveCity(String adjustReceiveCity) {
			this.adjustReceiveCity = adjustReceiveCity;
		}
		public String getAdjustReceiveArea() {
			return adjustReceiveArea;
		}
		public void setAdjustReceiveArea(String adjustReceiveArea) {
			this.adjustReceiveArea = adjustReceiveArea;
		}
		public String getForwardCarrierId() {
			return forwardCarrierId;
		}
		public void setForwardCarrierId(String forwardCarrierId) {
			this.forwardCarrierId = forwardCarrierId;
		}
		public String getForwardDeliverId() {
			return forwardDeliverId;
		}
		public void setForwardDeliverId(String forwardDeliverId) {
			this.forwardDeliverId = forwardDeliverId;
		}
		public String getAdjustCarrierId() {
			return adjustCarrierId;
		}
		public void setAdjustCarrierId(String adjustCarrierId) {
			this.adjustCarrierId = adjustCarrierId;
		}
		public String getAdjustDeliverId() {
			return adjustDeliverId;
		}
		public void setAdjustDeliverId(String adjustDeliverId) {
			this.adjustDeliverId = adjustDeliverId;
		}
		public String getAccountState() {
			return accountState;
		}
		public void setAccountState(String accountState) {
			this.accountState = accountState;
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
		public String getWaybillStatus() {
			return waybillStatus;
		}
		public void setWaybillStatus(String waybillStatus) {
			this.waybillStatus = waybillStatus;
		}
		public String getOrderStatus() {
			return orderStatus;
		}
		public void setOrderStatus(String orderStatus) {
			this.orderStatus = orderStatus;
		}
		public String getDelFlag() {
			return delFlag;
		}
		public void setDelFlag(String delFlag) {
			this.delFlag = delFlag;
		}
		public String getLastModifier() {
			return lastModifier;
		}
		public void setLastModifier(String lastModifier) {
			this.lastModifier = lastModifier;
		}
		public String getLastModifierId() {
			return lastModifierId;
		}
		public void setLastModifierId(String lastModifierId) {
			this.lastModifierId = lastModifierId;
		}
		public Timestamp getLastModifyTime() {
			return lastModifyTime;
		}
		public void setLastModifyTime(Timestamp lastModifyTime) {
			this.lastModifyTime = lastModifyTime;
		}
		public Timestamp getCreateTime() {
			return createTime;
		}
		public void setCreateTime(Timestamp createTime) {
			this.createTime = createTime;
		}
		public String getIsCalculated() {
			return isCalculated;
		}
		public void setIsCalculated(String isCalculated) {
			this.isCalculated = isCalculated;
		}
		
	
}
