package com.jiuyescm.oms.api.test.edb;

public class EdbReceiveEBPInfoEntity {

	private int id;
	private String jsonStr;
	private String clientCode;
	private String interfaceType; 
	private String dcode;
	private String delveryCode;
	
	public EdbReceiveEBPInfoEntity() {
		super(); 
	}
	
	public EdbReceiveEBPInfoEntity(int id, String jsonStr, String clientCode, String interfaceType, String dcode,
			String delveryCode) {
		super();
		this.id = id;
		this.jsonStr = jsonStr;
		this.clientCode = clientCode;
		this.interfaceType = interfaceType;
		this.dcode = dcode;
		this.delveryCode = delveryCode;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getJsonStr() {
		return jsonStr;
	}
	public void setJsonStr(String jsonStr) {
		this.jsonStr = jsonStr;
	}
	public String getClientCode() {
		return clientCode;
	}
	public void setClientCode(String clientCode) {
		this.clientCode = clientCode;
	}
	public String getInterfaceType() {
		return interfaceType;
	}
	public void setInterfaceType(String interfaceType) {
		this.interfaceType = interfaceType;
	}
	public String getDcode() {
		return dcode;
	}
	public void setDcode(String dcode) {
		this.dcode = dcode;
	}
	public String getDelveryCode() {
		return delveryCode;
	}
	public void setDelveryCode(String delveryCode) {
		this.delveryCode = delveryCode;
	}
	
}
