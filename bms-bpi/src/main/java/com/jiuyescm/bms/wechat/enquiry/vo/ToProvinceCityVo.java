package com.jiuyescm.bms.wechat.enquiry.vo;

import java.io.Serializable;
import java.util.List;

/**
 * 目的城市前端返回实体类
 * 
 * @author yangss
 */
public class ToProvinceCityVo implements Serializable {

	private static final long serialVersionUID = -5720143942725709447L;

	// 目的省
	private String province;
	// 目的市
	private String city;
	// 目的市
	private List<String> district;

	public ToProvinceCityVo() {
		super();
	}

	public ToProvinceCityVo(String province, String city, List<String> district) {
		super();
		this.province = province;
		this.city = city;
		this.district = district;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public List<String> getDistrict() {
		return district;
	}

	public void setDistrict(List<String> district) {
		this.district = district;
	}

}
