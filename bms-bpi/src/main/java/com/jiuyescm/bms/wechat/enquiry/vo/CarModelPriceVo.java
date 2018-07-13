package com.jiuyescm.bms.wechat.enquiry.vo;

import java.io.Serializable;

/**
 * 车型、单价
 * @author yangss
 * 
 */
public class CarModelPriceVo implements Serializable {

	private static final long serialVersionUID = 5910650143546527135L;

	// 车型
	private String carModel;
	// 单价
	private double price;

	public CarModelPriceVo() {
		super();
	}

	public CarModelPriceVo(String carModel, double price) {
		super();
		this.carModel = carModel;
		this.price = price;
	}

	public String getCarModel() {
		return carModel;
	}

	public void setCarModel(String carModel) {
		this.carModel = carModel;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

}
