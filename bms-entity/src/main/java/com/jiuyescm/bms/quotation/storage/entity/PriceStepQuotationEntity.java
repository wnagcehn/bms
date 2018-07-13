package com.jiuyescm.bms.quotation.storage.entity;

import com.jiuyescm.bms.common.BmsCommonAttribute;

/**
 * 
 * @author zzw
 * 
 */
public class PriceStepQuotationEntity extends BmsCommonAttribute{
	
	
	private static final long serialVersionUID = -6280172759526293122L;
		//模版编号
	    private String quotationId;
	    //重量下限
		private Double weightLower;
		//重量上限
		private Double weightUpper;
		//温度类型
		private String temperatureTypeCode;
		// 单价
		private Double unitPrice;
		//数量上限
		private Double numUpper;
		//数量下限
		private Double numLower;
		
		private Double skuUpper;
		
		private Double skuLower;
		//体积上限
		private Double volumeUpper;
		
		private Double volumeLower;
		
		private String userDefine1;
		
		private String userDefine2;
		
		private String userDefine3;
		
		private String userDefine4;
		
		private String userDefine5;
		// 备注
		private String remark;
		//续件价格
		private Double continuedItem;
	
	public Double getContinuedItem() {
			return continuedItem;
		}
		public void setContinuedItem(Double continuedItem) {
			this.continuedItem = continuedItem;
		}
	public String getQuotationId() {
		return quotationId;
	}
	public void setQuotationId(String quotationId) {
		this.quotationId = quotationId;
	}
	
	public Double getWeightLower() {
		return weightLower;
	}
	public void setWeightLower(Double weightLower) {
		this.weightLower = weightLower;
	}
	public Double getWeightUpper() {
		return weightUpper;
	}
	public void setWeightUpper(Double weightUpper) {
		this.weightUpper = weightUpper;
	}
	public String getTemperatureTypeCode() {
		return temperatureTypeCode;
	}
	public void setTemperatureTypeCode(String temperatureTypeCode) {
		this.temperatureTypeCode = temperatureTypeCode;
	}
	public Double getUnitPrice() {
		return unitPrice;
	}
	public void setUnitPrice(Double unitPrice) {
		this.unitPrice = unitPrice;
	}
	public Double getNumUpper() {
		return numUpper;
	}
	public void setNumUpper(Double numUpper) {
		this.numUpper = numUpper;
	}
	public Double getNumLower() {
		return numLower;
	}
	public void setNumLower(Double numLower) {
		this.numLower = numLower;
	}
	public Double getSkuUpper() {
		return skuUpper;
	}
	public void setSkuUpper(Double skuUpper) {
		this.skuUpper = skuUpper;
	}
	public Double getSkuLower() {
		return skuLower;
	}
	public void setSkuLower(Double skuLower) {
		this.skuLower = skuLower;
	}
	public Double getVolumeUpper() {
		return volumeUpper;
	}
	public void setVolumeUpper(Double volumeUpper) {
		this.volumeUpper = volumeUpper;
	}
	public Double getVolumeLower() {
		return volumeLower;
	}
	public void setVolumeLower(Double volumeLower) {
		this.volumeLower = volumeLower;
	}
	public String getUserDefine1() {
		return userDefine1;
	}
	public void setUserDefine1(String userDefine1) {
		this.userDefine1 = userDefine1;
	}
	public String getUserDefine2() {
		return userDefine2;
	}
	public void setUserDefine2(String userDefine2) {
		this.userDefine2 = userDefine2;
	}
	public String getUserDefine3() {
		return userDefine3;
	}
	public void setUserDefine3(String userDefine3) {
		this.userDefine3 = userDefine3;
	}
	public String getUserDefine4() {
		return userDefine4;
	}
	public void setUserDefine4(String userDefine4) {
		this.userDefine4 = userDefine4;
	}
	public String getUserDefine5() {
		return userDefine5;
	}
	public void setUserDefine5(String userDefine5) {
		this.userDefine5 = userDefine5;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	
	
    
}
