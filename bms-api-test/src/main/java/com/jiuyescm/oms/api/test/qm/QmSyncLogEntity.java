package com.jiuyescm.oms.api.test.qm;

import java.util.Date;

public class QmSyncLogEntity {
	
	private int id;
	private String method;
	private String format;
	private String app_key;
	private String v;
	private String sign;
	private String sign_method;
	private String customerid;
	private String content;
	private Date createtime;
	
	public QmSyncLogEntity() {
		super(); 
	}

	public QmSyncLogEntity(int id, String method, String format, String app_key, String v, String sign,
			String sign_method, String customerid, String content, Date createtime) {
		super();
		this.id = id;
		this.method = method;
		this.format = format;
		this.app_key = app_key;
		this.v = v;
		this.sign = sign;
		this.sign_method = sign_method;
		this.customerid = customerid;
		this.content = content;
		this.createtime = createtime;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public String getApp_key() {
		return app_key;
	}

	public void setApp_key(String app_key) {
		this.app_key = app_key;
	}

	public String getV() {
		return v;
	}

	public void setV(String v) {
		this.v = v;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getSign_method() {
		return sign_method;
	}

	public void setSign_method(String sign_method) {
		this.sign_method = sign_method;
	}

	public String getCustomerid() {
		return customerid;
	}

	public void setCustomerid(String customerid) {
		this.customerid = customerid;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Date getCreatetime() {
		return createtime;
	}

	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	} 
}
