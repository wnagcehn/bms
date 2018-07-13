package com.jiuyescm.bms.quotation.storage.entity;

import com.jiuyescm.bms.common.BmsCommonAttribute;

public class PriceExtraQuotationEntity extends BmsCommonAttribute{
	
    private static final long serialVersionUID = -1775517093701283171L;

	private String templateId;

    private String subjectId;

    private String feeUnitCode;
    
    private Double unitPrice;

	private String remark;
	
	// 操作类型
	private String operateType;
	// 温度类型
	private String temperatureTypeCode;
	// 报价类型
	private String priceType;
	// B2B标识
	private String b2bFlag;
	// 续件价格
	private Double continuedItem;
	// 重量下限
	private Double weightLower;
	// 重量上限
	private Double weightUpper;
	// 数量下限
	private Double numLower;
	// 数量上限
	private Double numUpper;
	// sku下限
	private Double skuLower;
	// sku上限
	private Double skuUpper;
	// 体积下限
	private Double volumeLower;
	// 体积上限
	private Double volumeUpper;
	// 拓展1
	private String userDefine1;
	// 拓展2
	private String userDefine2;
	// 拓展3
	private String userDefine3;
	// 拓展4
	private String userDefine4;
	// 拓展5
	private String userDefine5;

	public Double getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(Double unitPrice) {
		this.unitPrice = unitPrice;
	}

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId == null ? null : templateId.trim();
    }

    public String getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId == null ? null : subjectId.trim();
    }


    public String getFeeUnitCode() {
		return feeUnitCode;
	}

	public void setFeeUnitCode(String feeUnitCode) {
		this.feeUnitCode = feeUnitCode;
	}

	public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

	public String getOperateType() {
		return operateType;
	}

	public void setOperateType(String operateType) {
		this.operateType = operateType;
	}

	public String getTemperatureTypeCode() {
		return temperatureTypeCode;
	}

	public void setTemperatureTypeCode(String temperatureTypeCode) {
		this.temperatureTypeCode = temperatureTypeCode;
	}

	public String getPriceType() {
		return priceType;
	}

	public void setPriceType(String priceType) {
		this.priceType = priceType;
	}

	public String getB2bFlag() {
		return b2bFlag;
	}

	public void setB2bFlag(String b2bFlag) {
		this.b2bFlag = b2bFlag;
	}

	public Double getContinuedItem() {
		return continuedItem;
	}

	public void setContinuedItem(Double continuedItem) {
		this.continuedItem = continuedItem;
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

	public Double getNumLower() {
		return numLower;
	}

	public void setNumLower(Double numLower) {
		this.numLower = numLower;
	}

	public Double getNumUpper() {
		return numUpper;
	}

	public void setNumUpper(Double numUpper) {
		this.numUpper = numUpper;
	}

	public Double getSkuLower() {
		return skuLower;
	}

	public void setSkuLower(Double skuLower) {
		this.skuLower = skuLower;
	}

	public Double getSkuUpper() {
		return skuUpper;
	}

	public void setSkuUpper(Double skuUpper) {
		this.skuUpper = skuUpper;
	}

	public Double getVolumeLower() {
		return volumeLower;
	}

	public void setVolumeLower(Double volumeLower) {
		this.volumeLower = volumeLower;
	}

	public Double getVolumeUpper() {
		return volumeUpper;
	}

	public void setVolumeUpper(Double volumeUpper) {
		this.volumeUpper = volumeUpper;
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


}