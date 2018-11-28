package com.jiuyescm.bms.excel.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class DataRow {
	
	public DataRow(int rowNo){
		this.rowNo = rowNo;
	}
	
	/**
	 * 行索引
	 */
	private int rowNo;
	
	/**
	 * 行包含的列
	 */
	private List<DataColumn> columns = new ArrayList<>();
	
	private Map<Integer, DataColumn> colsMap = new LinkedHashMap<>();

	/**
	 * 行索引
	 */
	public int getRowNo() {
		return rowNo;
	}

	/**
	 * 行包含的列
	 */
	public List<DataColumn> getColumns() {
		return columns;
	}
	
	public DataColumn getColumn(Integer index){
		return colsMap.get(index);
	}
	
	public void addColumn(Integer index,DataColumn dc){
		this.columns.add(dc);
		this.colsMap.put(index, dc);
	}
	
	public void addColumn(DataColumn dc){
		int site = colsMap.size()+1;
		this.columns.add(dc);
		this.colsMap.put(site, dc);
	}
	
	/*public <T> T transToObj(Class<T> clazz) throws Exception{
		T obj;
		try{
			obj = clazz.newInstance();
			for (DataColumn dc : this.getColumns()) {
				Utils.copyProperty(obj, dc.getFieldName(), dc.getColValue());
			}
		}
		catch (Exception ex) {
            throw ex;
        }
		return obj;
	}*/
}
