package com.jiuyescm.bms.excel.data;

import java.util.ArrayList;
import java.util.List;

public class DataRow {
	
	private int rowNo; //行号
	private List<DataColumn> columns = new ArrayList<>(); //行包含的列

	public DataRow(){
		
	}
	public DataRow(int curRow){
		
	}

	/**
	 * 行索引
	 */
	public int getRowNo() {
		return rowNo;
	}
	
	public void setRowNo(int rowNo){
		this.rowNo = rowNo;
	}

	/**
	 * 行包含的列
	 */
	public List<DataColumn> getColumns() {
		return columns;
	}
	
	public void addColumn(DataColumn dc){
		columns.add(dc);
	}
	
}
