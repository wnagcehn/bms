package com.jiuyescm.bms.wechat.enquiry.vo;

import java.io.Serializable;

/**
 * 温度、车型、单价
 * 
 * @author yangss
 * 
 */
public class TemperatureCarModelPirceVo implements Serializable {

	private static final long serialVersionUID = 7464371482061668986L;

	// 温度
	private String temperature;
	// 车型
	private String carModel;
	// 单价
	private String price;

	public TemperatureCarModelPirceVo() {
		super();
	}

	public TemperatureCarModelPirceVo(String temperature, String carModel,
			String price) {
		super();
		this.temperature = temperature;
		this.carModel = carModel;
		this.price = price;
	}

	public String getTemperature() {
		return temperature;
	}

	public void setTemperature(String temperature) {
		this.temperature = temperature;
	}

	public String getCarModel() {
		return carModel;
	}

	public void setCarModel(String carModel) {
		this.carModel = carModel;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

}
