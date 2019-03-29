package com.jiuyescm.bms.calculate.vo;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class CalcuBaseInfoVo<T> {

	private String taskId;
	private String feesNo;
	private String node;
	private String subjectCode;
	private String calcuTime;
	private T data;
	
	/**
	 * 
	 * @param taskId
	 * @param node
	 * @param feesNo
	 * @param subjectCode
	 * @param calcuTime
	 */
	public CalcuBaseInfoVo(String taskId,String node,String feesNo,String subjectCode,Timestamp calcuTime){
		this.taskId = taskId;
		this.node = node;
		this.feesNo = feesNo;
		this.subjectCode = subjectCode;
		DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String tsStr = sdf.format(calcuTime);
		this.setCalcuTime(tsStr);
	}
	
	public String getTaskId() {
		return taskId;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	public String getFeesNo() {
		return feesNo;
	}
	public void setFeesNo(String feesNo) {
		this.feesNo = feesNo;
	}
	public String getNode() {
		return node;
	}
	public void setNode(String node) {
		this.node = node;
	}
	public String getSubjectCode() {
		return subjectCode;
	}
	public void setSubjectCode(String subjectCode) {
		this.subjectCode = subjectCode;
	}
	
	//业务参数
	public T getData() {
		return data;
	}
	
	/**
	 * 业务参数
	 * @param t
	 */
	public void setData(T data) {
		this.data = data;
	}

	public String getCalcuTime() {
		return calcuTime;
	}

	public void setCalcuTime(String calcuTime) {
		this.calcuTime = calcuTime;
	}
	
	
}
