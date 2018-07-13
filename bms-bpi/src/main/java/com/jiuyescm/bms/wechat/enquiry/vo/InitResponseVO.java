package com.jiuyescm.bms.wechat.enquiry.vo;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 城际初始化查询返回对象
 * 
 * @author yangss
 * 
 */
public class InitResponseVO implements Serializable {

	private static final long serialVersionUID = -8580104117028340538L;

	// 始发省
	private String fromProvince;
	// 始发市
	private String fromCity;
	// 目的省市
	private List<Map<String, Object>> toCitys;
	// 产品类型
	private List<ProductTypeVo> products;

	public String getFromProvince() {
		return fromProvince;
	}

	public void setFromProvince(String fromProvince) {
		this.fromProvince = fromProvince;
	}

	public String getFromCity() {
		return fromCity;
	}

	public void setFromCity(String fromCity) {
		this.fromCity = fromCity;
	}

	public List<Map<String, Object>> getToCitys() {
		return toCitys;
	}

	public void setToCitys(List<Map<String, Object>> toCitys) {
		this.toCitys = toCitys;
	}

	public List<ProductTypeVo> getProducts() {
		return products;
	}

	public void setProducts(List<ProductTypeVo> products) {
		this.products = products;
	}

}
