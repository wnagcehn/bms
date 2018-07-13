package com.jiuyescm.bms.wechat.enquiry.vo;

import java.io.Serializable;

/**
 * 重量界限报价VO
 * 
 * @author yangss
 * 
 */
public class WeightLimitCJVo implements Serializable {

	private static final long serialVersionUID = 2238556534876881575L;

	// 重量下限
	private double startWeight;
	// 重量上限
	private double endWeight;
	// 单价
	private String price;

	public WeightLimitCJVo() {
		super();
	}

	public WeightLimitCJVo(double startWeight, double endWeight, String price) {
		super();
		this.startWeight = startWeight;
		this.endWeight = endWeight;
		this.price = price;
	}

	public double getStartWeight() {
		return startWeight;
	}

	public void setStartWeight(double startWeight) {
		this.startWeight = startWeight;
	}

	public double getEndWeight() {
		return endWeight;
	}

	public void setEndWeight(double endWeight) {
		this.endWeight = endWeight;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

}
