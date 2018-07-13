package com.jiuyescm.bms.wechat.enquiry.vo;

import java.io.Serializable;
import java.util.List;

/**
 * 航鲜达返回实体vo
 * 
 * @author yangss
 * 
 */
public class QuotationHXDVo implements Serializable {

	private static final long serialVersionUID = -951953316587837519L;

	private String airPort;
	private String toProvince;
	private String toCity;
	private double minPriceShipment;
	private List<WeightLimitVo> weightLimit;

	public String getAirPort() {
		return airPort;
	}

	public void setAirPort(String airPort) {
		this.airPort = airPort;
	}

	public String getToProvince() {
		return toProvince;
	}

	public void setToProvince(String toProvince) {
		this.toProvince = toProvince;
	}

	public String getToCity() {
		return toCity;
	}

	public void setToCity(String toCity) {
		this.toCity = toCity;
	}

	public double getMinPriceShipment() {
		return minPriceShipment;
	}

	public void setMinPriceShipment(double minPriceShipment) {
		this.minPriceShipment = minPriceShipment;
	}

	public List<WeightLimitVo> getWeightLimit() {
		return weightLimit;
	}

	public void setWeightLimit(List<WeightLimitVo> weightLimit) {
		this.weightLimit = weightLimit;
	}

}
