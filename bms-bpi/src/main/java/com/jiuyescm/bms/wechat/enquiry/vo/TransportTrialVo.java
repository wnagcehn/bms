package com.jiuyescm.bms.wechat.enquiry.vo;

import java.io.Serializable;

/**
 * 微信试算返回实体vo
 * 
 * @author yangss
 * 
 */
public class TransportTrialVo implements Serializable {

	private static final long serialVersionUID = -3803329304953471793L;

	private String unitPrice;
	private String price;

	public TransportTrialVo() {
		super();
	}

	public TransportTrialVo(String unitPrice, String price) {
		super();
		this.unitPrice = unitPrice;
		this.price = price;
	}

	public String getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(String unitPrice) {
		this.unitPrice = unitPrice;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

}
