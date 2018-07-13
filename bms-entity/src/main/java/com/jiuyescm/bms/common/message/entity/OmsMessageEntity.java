package com.jiuyescm.bms.common.message.entity;

/**
 * 请求，响应报文
 * @author chenfei
 *
 */
public class OmsMessageEntity {
	private String id; //主键
	private String request; //请求报文
	private String response; //响应报文
	private String operperson; //操作人
	private java.sql.Timestamp opertime; //操作时间
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getRequest() {
		return request;
	}
	public void setRequest(String request) {
		this.request = request;
	}
	public String getResponse() {
		return response;
	}
	public void setResponse(String response) {
		this.response = response;
	}
	public String getOperperson() {
		return operperson;
	}
	public void setOperperson(String operperson) {
		this.operperson = operperson;
	}
	public java.sql.Timestamp getOpertime() {
		return opertime;
	}
	public void setOpertime(java.sql.Timestamp opertime) {
		this.opertime = opertime;
	}
}
