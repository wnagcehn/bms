package com.jiuyescm.bms.excel.write;

public class ColMergeRegion {

	/**
	 * 列名
	 */
	private String colName;
	
	/**
	 * 单元格值
	 */
	private String colValue;
	
	/**
	 * 开始行
	 */
	private int startRow;
	
	/**
	 * 结束行
	 */
	private int endRow;
	
	public ColMergeRegion(String colName,String colValue,int startRow){
		this.colName = colName;
		this.colValue = colValue;
		this.startRow = startRow;
		this.endRow = startRow;
	}
	
	public String getColName() {
		return colName;
	}

	public void setColName(String colName) {
		this.colName = colName;
	}

	public String getColValue() {
		return colValue;
	}

	public void setColValue(String colValue) {
		this.colValue = colValue;
	}

	public int getStartRow() {
		return startRow;
	}

	public void setStartRow(int startRow) {
		this.startRow = startRow;
	}

	public int getEndRow() {
		return endRow;
	}

	public void setEndRow(int endRow) {
		this.endRow = endRow;
	}

	
	/**
	 * 是否需要合并单元格
	 * @param content 
	 * @return true-合并 false-不合并
	 */
	public boolean mergeRow(String content){
		boolean ret = false;
		if(content == null){
			ret = true;
		}
		if(content.equals(colValue)){
			endRow+=1;
			ret = false;
		}
		else{
			ret = true;
		}
		return ret;
	}
}
