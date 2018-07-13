package com.jiuyescm.bms.pub.transport.entity;

import java.io.Serializable;

/**
 * 运输报价封装实体
 * @author yangss
 *
 */
public class PubTransportRegionEntity implements Serializable {

	private static final long serialVersionUID = 9173844491094315015L;

	// 车型
	private String carModel;
	// 最小单价
	private Double lowerPrice;
	// 最大单价
	private Double upperPrice;

	public String getCarModel() {
		return carModel;
	}

	public void setCarModel(String carModel) {
		this.carModel = carModel;
	}

	public Double getLowerPrice() {
		return lowerPrice;
	}

	public void setLowerPrice(Double lowerPrice) {
		this.lowerPrice = lowerPrice;
	}

	public Double getUpperPrice() {
		return upperPrice;
	}

	public void setUpperPrice(Double upperPrice) {
		this.upperPrice = upperPrice;
	}

}
