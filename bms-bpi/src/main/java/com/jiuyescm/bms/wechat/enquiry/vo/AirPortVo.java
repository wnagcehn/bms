package com.jiuyescm.bms.wechat.enquiry.vo;

import java.io.Serializable;

public class AirPortVo implements Serializable {

	private static final long serialVersionUID = 3525677774125642070L;

	private String airPort;

	public AirPortVo() {
		super();
	}

	public AirPortVo(String airPort) {
		super();
		this.airPort = airPort;
	}

	public String getAirPort() {
		return airPort;
	}

	public void setAirPort(String airPort) {
		this.airPort = airPort;
	}

}
