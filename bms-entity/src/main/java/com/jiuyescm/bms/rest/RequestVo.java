package com.jiuyescm.bms.rest;

import java.util.Map;

import com.jiuyescm.cfm.domain.IEntity;

public class RequestVo implements IEntity {

	private static final long serialVersionUID = 6755808441619045420L;

	private Map<String, String> params;

	public Map<String, String> getParams() {
		return params;
	}

	public void setParams(Map<String, String> params) {
		this.params = params;
	}
}
