package com.jiuyescm.bms.common.entity;


import com.jiuyescm.cfm.domain.IEntity;

/**
 * 获取运单号提示VO
 * @author chenfei
 *
 */
public class ResultMessageVo implements IEntity{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8256892915384187876L;

	private String lineNo; //获取运单号的唯一标识
	
	private String msg=""; //提示信息


	public String getLineNo() {
		return lineNo;
	}

	public void setLineNo(String lineNo) {
		this.lineNo = lineNo;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
}
