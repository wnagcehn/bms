package com.jiuyescm.bms.excel.data;

public class DataColumn {
	
	/**
	 * 
	 * @param colNo 列索引
	 * @param colValue 列值
	 */
	public DataColumn(int colNo,String colValue){
		this.colNo = colNo;
		this.colValue = colValue;
	}
	
	public DataColumn(String colName,String colValue){
		this.colName = colName;
		this.colValue = colValue;
	}
	
	/**
	 * 列索引
	 */
	private int colNo;
	private String colName;
	
	private String fieldName;

	/**
	 * 列值
	 */
	private String colValue;

	/**
	 * 列索引
	 */
	public int getColNo() {
		return colNo;
	}

	/**
	 * 列值
	 */
	public String getColValue() {
		return colValue;
	}
	
	public void setColValue(String colValue) {
		this.colValue = colValue;
	}

	public String getColName() {
		return colName;
	}

	public void setColName(String colName) {
		this.colName = colName;
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}
	
}
