package com.jiuyescm.bms.wechat.enquiry.vo;

import java.io.Serializable;

/**
 * 重量界限报价VO
 * @author yangss
 * 
 */
public class WeightLimitVo implements Serializable {

	private static final long serialVersionUID = 2238556534876881575L;

	// 重量下限
	private double weightLowerLimit;
	// 重量上限
	private double weightUpperLimit;
	// 单价
	private String price;

	public WeightLimitVo() {
		super();
	}

	public WeightLimitVo(double weightLowerLimit, double weightUpperLimit,
			String price) {
		super();
		this.weightLowerLimit = weightLowerLimit;
		this.weightUpperLimit = weightUpperLimit;
		this.price = price;
	}

	public double getWeightLowerLimit() {
		return weightLowerLimit;
	}

	public void setWeightLowerLimit(double weightLowerLimit) {
		this.weightLowerLimit = weightLowerLimit;
	}

	public double getWeightUpperLimit() {
		return weightUpperLimit;
	}

	public void setWeightUpperLimit(double weightUpperLimit) {
		this.weightUpperLimit = weightUpperLimit;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

}
