package com.jiuyescm.bms.common.entity;

/**
 * 错误消息提示VO
 * @author zhengyishan
 *
 */
public class ErrorMessageVo {
	
	private int lineNo; //行号
	
	private String msg=""; //错误提示信息

	public int getLineNo() {
		return lineNo;
	}

	public void setLineNo(int lineNo) {
		this.lineNo = lineNo;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
	
	public ErrorMessageVo(){
		
	}
	public ErrorMessageVo(int lineNo,String msg){
		this.lineNo=lineNo;
		this.msg=msg;
	}

}
