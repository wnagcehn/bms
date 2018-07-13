package com.jiuyescm.bms.rest;

import java.io.Serializable;

/**
 * 始发城市前端返回实体类
 * 
 * @author yangss
 */
public class OriginCityVo implements Serializable {

	private static final long serialVersionUID = -5720143942725709447L;

	//省
	private String fromProvice;
	//市
	private String fromCity;

	public String getFromProvice() {
		return fromProvice;
	}

	public void setFromProvice(String fromProvice) {
		this.fromProvice = fromProvice;
	}

	public String getFromCity() {
		return fromCity;
	}

	public void setFromCity(String fromCity) {
		this.fromCity = fromCity;
	}

}
