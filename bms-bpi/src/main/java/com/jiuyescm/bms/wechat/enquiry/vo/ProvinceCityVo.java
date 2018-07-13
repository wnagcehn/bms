package com.jiuyescm.bms.wechat.enquiry.vo;

import java.io.Serializable;

/**
 * 城市前端返回实体类
 * 
 * @author yangss
 */
public class ProvinceCityVo implements Serializable {

	private static final long serialVersionUID = -5720143942725709447L;

	// 始发省
	private String province;
	// 始发市
	private String city;

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

}
