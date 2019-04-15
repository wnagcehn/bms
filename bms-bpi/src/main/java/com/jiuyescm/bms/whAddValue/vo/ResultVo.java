/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.whAddValue.vo;

import com.jiuyescm.cfm.domain.IEntity;

public class ResultVo implements IEntity{

	 // TODO Change serialVersionUID by eclipse
    private static final long serialVersionUID = -1;
	
	private String returnId;
	
	private String code;
	
	private String remark;

	
	
	public ResultVo(String returnId, String code, String remark) {
		super();
		this.returnId = returnId;
		this.code = code;
		this.remark = remark;
	}

	public String getReturnId() {
		return returnId;
	}

	public void setReturnId(String returnId) {
		this.returnId = returnId;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	
	
}
