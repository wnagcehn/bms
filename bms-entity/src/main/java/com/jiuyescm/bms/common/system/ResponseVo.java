package com.jiuyescm.bms.common.system;

import java.io.Serializable;

/**
 * 统一返回前端信息实体
 * @author yangss
 */
public class ResponseVo implements Serializable {

	private static final long serialVersionUID = 6577700598265216653L;

	public static final String SUCCESS = "SUCC";
	public static final String FAIL = "FAIL";
	public static final String ERROR = "ERROR";
	
	//返回编码
	private String code;
	//返回信息
	private String message;
	
	public ResponseVo() {
		super();
	}

	public ResponseVo(String code, String message) {
		super();
		this.code = code;
		this.message = message;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
}
