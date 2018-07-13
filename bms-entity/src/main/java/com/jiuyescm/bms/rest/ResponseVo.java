package com.jiuyescm.bms.rest;

import java.io.Serializable;

/**
 * 接口返回实体类
 * 
 * @author yangss
 */
public class ResponseVo implements Serializable {

	private static final long serialVersionUID = 899057259752271491L;

	private String flag;
	private String code;
	private String message;
	private Object content;
	
	public ResponseVo() {
		super();
	}

	public ResponseVo(String flag, String message) {
		super();
		this.flag = flag;
		this.message = message;
	}

	public ResponseVo(String flag, String message, Object content) {
		super();
		this.flag = flag;
		this.message = message;
		this.content = content;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
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

	public Object getContent() {
		return content;
	}

	public void setContent(Object content) {
		this.content = content;
	}


}
