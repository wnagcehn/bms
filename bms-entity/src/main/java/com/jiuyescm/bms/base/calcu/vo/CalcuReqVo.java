package com.jiuyescm.bms.base.calcu.vo;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class CalcuReqVo<T> implements Serializable{
	
	private static final long serialVersionUID = -917900959783889045L;
	
	private Object bizData;					//业务数据对象
	private String ruleStr;					//规则代码
	private String ruleNo;					//规则编号
	private Map<String, Object> params;		//其他参数
	private List<T> quoEntites;
	private T quoEntity;
	
	public Object getBizData() {
		return bizData;
	}

	public void setBizData(Object bizData) {
		this.bizData = bizData;
	}

	public String getRuleStr() {
		return ruleStr;
	}

	public void setRuleStr(String ruleStr) {
		this.ruleStr = ruleStr;
	}

	public String getRuleNo() {
		return ruleNo;
	}

	public void setRuleNo(String ruleNo) {
		this.ruleNo = ruleNo;
	}

	public Map<String, Object> getParams() {
		return params;
	}

	public void setParams(Map<String, Object> params) {
		this.params = params;
	}

	public List<T> getQuoEntites() {
		return quoEntites;
	}

	public void setQuoEntites(List<T> quoEntites) {
		this.quoEntites = quoEntites;
	}

	public T getQuoEntity() {
		return quoEntity;
	}

	public void setQuoEntity(T quoEntity) {
		this.quoEntity = quoEntity;
	}
	
}
