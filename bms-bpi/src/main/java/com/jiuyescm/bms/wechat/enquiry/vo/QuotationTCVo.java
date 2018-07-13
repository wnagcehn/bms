package com.jiuyescm.bms.wechat.enquiry.vo;

import java.io.Serializable;

public class QuotationTCVo<T> implements Serializable {

	private static final long serialVersionUID = 164140187274965785L;

	// 车型-外埠、车型
	private String carModel;
	// 目的地-全部(市或区)
	private String toDistrict;
	// 封装
	private T quote;

	public String getCarModel() {
		return carModel;
	}

	public void setCarModel(String carModel) {
		this.carModel = carModel;
	}

	public String getToDistrict() {
		return toDistrict;
	}

	public void setToDistrict(String toDistrict) {
		this.toDistrict = toDistrict;
	}

	public T getQuote() {
		return quote;
	}

	public void setQuote(T quote) {
		this.quote = quote;
	}

}
