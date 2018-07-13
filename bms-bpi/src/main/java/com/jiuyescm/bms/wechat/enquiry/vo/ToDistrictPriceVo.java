package com.jiuyescm.bms.wechat.enquiry.vo;

import java.io.Serializable;

/**
 * 目的地、单价
 * @author yangss
 * 
 */
public class ToDistrictPriceVo implements Serializable {

	private static final long serialVersionUID = 7829498374568019170L;

	// 目的地
	private String toDistrict;
	// 单价
	private double price;

	public ToDistrictPriceVo() {
		super();
	}

	public ToDistrictPriceVo(String toDistrict, double price) {
		super();
		this.toDistrict = toDistrict;
		this.price = price;
	}

	public String getToDistrict() {
		return toDistrict;
	}

	public void setToDistrict(String toDistrict) {
		this.toDistrict = toDistrict;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

}
