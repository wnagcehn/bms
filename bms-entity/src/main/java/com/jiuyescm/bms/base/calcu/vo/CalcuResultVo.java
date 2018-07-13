package com.jiuyescm.bms.base.calcu.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Map;
public class CalcuResultVo implements Serializable {

	private static final long serialVersionUID = 6316473607408715649L;
	
	private String success;					//计算状态
	private String code;					//错误编码
	private String msg;						//错误描述
	private BigDecimal price;				//计算后金额
	private String quoId;					//报价ID
	private String method;					//算法
	private Double unitPrice;               //单价
	private Map<String, Object> params;		//其他参数
	
	public String getSuccess() {
		return success;
	}
	public void setSuccess(String success) {
		this.success = success;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	
	public BigDecimal getPrice() {
		return price;
	}
	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	public Map<String, Object> getParams() {
		return params;
	}
	public void setParams(Map<String, Object> params) {
		this.params = params;
	}
	
	public CalcuResultVo(){
		
	}
	
	/**
	 * 
	 * @param success	返回状态 succ-计算成功 fail-计算失败
	 * @param code		错误编码
	 * @param msg		错误描述
	 * @param price		计算后的价格
	 */
	public CalcuResultVo(String success,String code,String msg,BigDecimal price){
		this.success = success;
		this.code = code;
		this.msg = msg;
		this.price = price;
	}
		
	
	public CalcuResultVo(String success, String code, String msg,
			BigDecimal price, String quoId, String method, Double unitPrice) {
		this.success = success;
		this.code = code;
		this.msg = msg;
		this.price = price;
		this.quoId = quoId;
		this.method = method;
		this.unitPrice = unitPrice;
	}
	public String getQuoId() {
		return quoId;
	}
	public void setQuoId(String quoId) {
		this.quoId = quoId;
	}
	public String getMethod() {
		return method;
	}
	public void setMethod(String method) {
		this.method = method;
	}
	public Double getUnitPrice() {
		return unitPrice;
	}
	public void setUnitPrice(Double unitPrice) {
		this.unitPrice = unitPrice;
	}

	
	
}
