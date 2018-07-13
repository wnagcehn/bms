package com.jiuyescm.bms.wechat.enquiry.vo;

import java.io.Serializable;

/**
 * 外埠-报价上下限
 * 
 * @author yangss
 * 
 */
public class RegionPriceLimitVo implements Serializable {

	private static final long serialVersionUID = -3459940176992456207L;

	// 区域
	private String region;
	// 单价下限
	private String lowerPrice;
	// 单价上限
	private String upperPrice;

	public RegionPriceLimitVo() {
		super();
	}

	public RegionPriceLimitVo(String region, String lowerPrice,
			String upperPrice) {
		super();
		this.region = region;
		this.lowerPrice = lowerPrice;
		this.upperPrice = upperPrice;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public String getLowerPrice() {
		return lowerPrice;
	}

	public void setLowerPrice(String lowerPrice) {
		this.lowerPrice = lowerPrice;
	}

	public String getUpperPrice() {
		return upperPrice;
	}

	public void setUpperPrice(String upperPrice) {
		this.upperPrice = upperPrice;
	}

}
