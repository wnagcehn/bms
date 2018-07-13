package com.jiuyescm.bms.common.invmessage.entity;

import java.util.Date;

public class OmsOrderInvMessageEntity {

	private String orderno;
	private Integer invtype;
	private String message;
	private Date cretime;
	private String productid;
	private String warehouseid;
	public String getOrderno() {
		return orderno;
	}
	public void setOrderno(String orderno) {
		this.orderno = orderno;
	}
	public Integer getInvtype() {
		return invtype;
	}
	public void setInvtype(Integer invtype) {
		this.invtype = invtype;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Date getCretime() {
		return cretime;
	}
	public void setCretime(Date cretime) {
		this.cretime = cretime;
	}
	public String getProductid() {
		return productid;
	}
	public void setProductid(String productid) {
		this.productid = productid;
	}
	public String getWarehouseid() {
		return warehouseid;
	}
	public void setWarehouseid(String warehouseid) {
		this.warehouseid = warehouseid;
	}
	
	
}
