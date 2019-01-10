package com.jiuyescm.bms.excel.data;

public class Sheet {
	
	private String sheetNo;	//sheet索引值
	private Integer sheetId;		//sheet编号
	private String sheetName; 	//sheet名称
	
	private int rowCount;
	
	public Sheet(){
		
	}
	public Sheet(String sheetNo,Integer sheetId,String sheetName){
		this.sheetNo = sheetNo;
		this.sheetId = sheetId;
		this.sheetName = sheetName;
	}
	
	
	public String getSheetNo() {
		return sheetNo;
	}
	public String getSheetName() {
		return sheetName;
	}
	public Integer getSheetId() {
		return sheetId;
	}
	public int getRowCount() {
		return rowCount;
	}
	public void setRowCount(int rowCount) {
		this.rowCount = rowCount;
	}
	
}
