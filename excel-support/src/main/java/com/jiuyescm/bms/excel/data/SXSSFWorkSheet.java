package com.jiuyescm.bms.excel.data;

import java.util.List;
import java.util.Map;


public class SXSSFWorkSheet {

	private int startIndex = 1;//sheet内容的开始行 1为第二行
	private String sheetName;
	
	private int nextIndex = 1;
	
	private List<Map<String, Object>> headInfoList;
	
	public SXSSFWorkSheet(String sheetName,int startIndex,List<Map<String, Object>> headInfoList){
		this.startIndex = startIndex;
		this.sheetName = sheetName;
		this.headInfoList = headInfoList;
		this.setNextIndex(1);
	}

	public int getStartIndex() {
		return startIndex;
	}

	public void setStartIndex(int startIndex) {
		this.startIndex = startIndex;
	}

	public List<Map<String, Object>> getHeadInfoList() {
		return headInfoList;
	}

	public void setHeadInfoList(List<Map<String, Object>> headInfoList) {
		this.headInfoList = headInfoList;
	}

	public int getNextIndex() {
		return nextIndex;
	}

	public void setNextIndex(int nextIndex) {
		this.nextIndex = nextIndex;
	}
	
	public String getSheetName(){
		return sheetName;
	}

}
