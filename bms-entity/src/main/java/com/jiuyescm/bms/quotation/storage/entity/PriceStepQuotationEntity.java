package com.jiuyescm.bms.quotation.storage.entity;

import java.sql.Timestamp;

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
	// 生效日期
	private Timestamp startDate;
	// 失效日期
	private Timestamp expireDate;
	//数量上限
	private Double numUpper;
	//数量下限
	private Double numLower;
	// 单价
	private Double unitPrice;	
	// 首量
	private Double firstNum;
	// 首价
	private Double firstPrice;
	//续件
	private Double continuedItem;
	//续价价格
	private Double continuedPrice;
	// 仓库编码
	private String warehouseCode;
	//温度类型
	private String temperatureTypeCode;
	// 备注
	private String remark;
	// 创建者
	private String creator;
	// 创建时间
	private Timestamp createTime;
	// 修改者
	private String lastModifier;
	// 修改时间
	private Timestamp lastModifyTime;
	
	//封顶价
	private Double capPrice;
	
    //重量下限
	private Double weightLower;
	//重量上限
	private Double weightUpper;
	
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
	
	private String contractCode;
	
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
	public Timestamp getStartDate() {
		return startDate;
	}
	public void setStartDate(Timestamp startDate) {
		this.startDate = startDate;
	}
	public Timestamp getExpireDate() {
		return expireDate;
	}
	public void setExpireDate(Timestamp expireDate) {
		this.expireDate = expireDate;
	}
	public Double getFirstNum() {
		return firstNum;
	}
	public void setFirstNum(Double firstNum) {
		this.firstNum = firstNum;
	}
	public Double getFirstPrice() {
		return firstPrice;
	}
	public void setFirstPrice(Double firstPrice) {
		this.firstPrice = firstPrice;
	}
	public Double getContinuedPrice() {
		return continuedPrice;
	}
	public void setContinuedPrice(Double continuedPrice) {
		this.continuedPrice = continuedPrice;
	}
	public String getWarehouseCode() {
		return warehouseCode;
	}
	public void setWarehouseCode(String warehouseCode) {
		this.warehouseCode = warehouseCode;
	}
	public String getCreator() {
		return creator;
	}
	public void setCreator(String creator) {
		this.creator = creator;
	}
	public Timestamp getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}
	public String getLastModifier() {
		return lastModifier;
	}
	public void setLastModifier(String lastModifier) {
		this.lastModifier = lastModifier;
	}
	public Timestamp getLastModifyTime() {
		return lastModifyTime;
	}
	public void setLastModifyTime(Timestamp lastModifyTime) {
		this.lastModifyTime = lastModifyTime;
	}
	public String getContractCode() {
		return contractCode;
	}
	public void setContractCode(String contractCode) {
		this.contractCode = contractCode;
	}
	public Double getCapPrice() {
		return capPrice;
	}
	public void setCapPrice(Double capPrice) {
		this.capPrice = capPrice;
	}
	
	
	
    
}
