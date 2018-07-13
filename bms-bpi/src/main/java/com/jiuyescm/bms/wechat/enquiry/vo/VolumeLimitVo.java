package com.jiuyescm.bms.wechat.enquiry.vo;

import java.io.Serializable;

/**
 * 体积界限报价VO
 * 
 * @author yangss
 * 
 */
public class VolumeLimitVo implements Serializable {

	private static final long serialVersionUID = -6951598022587447069L;

	// 体积下限
	private double startVolume;
	// 体积上限
	private double endVolume;
	// 单价
	private String price;

	public VolumeLimitVo() {
		super();
	}

	public VolumeLimitVo(double startVolume, double endVolume, String price) {
		super();
		this.startVolume = startVolume;
		this.endVolume = endVolume;
		this.price = price;
	}

	public double getStartVolume() {
		return startVolume;
	}

	public void setStartVolume(double startVolume) {
		this.startVolume = startVolume;
	}

	public double getEndVolume() {
		return endVolume;
	}

	public void setEndVolume(double endVolume) {
		this.endVolume = endVolume;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

}
