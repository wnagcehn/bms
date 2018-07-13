package com.jiuyescm.bms.biz.transport.vo;

import java.io.Serializable;

public class BizGanxianWaybillReturnVo implements Serializable {

	private static final long serialVersionUID = 2787259904499704120L;

	// tmsId
	private String tmsId;
	// 是否泡货
	private String isLight;
	// 理论金额
	private double sysAmount;
	// 返回码
	private String returnCode;
	// 返回信息
	private String returnMsg;

	
	public BizGanxianWaybillReturnVo(String tmsId, String returnCode, String returnMsg) {
		this.tmsId = tmsId;
		this.returnCode = returnCode;
		this.returnMsg = returnMsg;
	}
	
	public BizGanxianWaybillReturnVo(String tmsId, String isLight,
			double sysAmount, String returnCode, String returnMsg) {
		this.tmsId = tmsId;
		this.isLight = isLight;
		this.sysAmount = sysAmount;
		this.returnCode = returnCode;
		this.returnMsg = returnMsg;
	}

	public String getTmsId() {
		return tmsId;
	}

	public void setTmsId(String tmsId) {
		this.tmsId = tmsId;
	}

	public String getIsLight() {
		return isLight;
	}

	public void setIsLight(String isLight) {
		this.isLight = isLight;
	}

	public double getSysAmount() {
		return sysAmount;
	}

	public void setSysAmount(double sysAmount) {
		this.sysAmount = sysAmount;
	}

	public String getReturnCode() {
		return returnCode;
	}

	public void setReturnCode(String returnCode) {
		this.returnCode = returnCode;
	}

	public String getReturnMsg() {
		return returnMsg;
	}

	public void setReturnMsg(String returnMsg) {
		this.returnMsg = returnMsg;
	}

}
