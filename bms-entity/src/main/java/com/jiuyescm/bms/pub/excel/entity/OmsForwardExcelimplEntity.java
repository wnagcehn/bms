package com.jiuyescm.bms.pub.excel.entity;

/**
 * 转寄模板导入类
 *
 */
public class OmsForwardExcelimplEntity {
	//行号
	private String lineNo;
	//运单号
	private String expressnum;
	//物流商代码
	private String carrierId;
	//宅配商代码
	private String deliverId;
	//新运单号
	private String zexpressnum;
	
	public String getDeliverId() {
		return deliverId;
	}
	public void setDeliverId(String deliverId) {
		this.deliverId = deliverId;
	}
	public String getLineNo() {
		return lineNo;
	}
	public void setLineNo(String lineNo) {
		this.lineNo = lineNo;
	}
	public String getExpressnum() {
		return expressnum;
	}
	public void setExpressnum(String expressnum) {
		this.expressnum = expressnum;
	}
	public String getCarrierId() {
		return carrierId;
	}
	public void setCarrierId(String carrierId) {
		this.carrierId = carrierId;
	}
	public String getZexpressnum() {
		return zexpressnum;
	}
	public void setZexpressnum(String zexpressnum) {
		this.zexpressnum = zexpressnum;
	}
}
