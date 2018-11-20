package com.jiuyescm.bms.excel.data;

import java.util.ArrayList;
import java.util.List;

import com.jiuyescm.bms.excel.utils.Utils;

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
		return columns.get(index);
	}
	
	public void addColumn(DataColumn dc){
		this.columns.add(dc);
	}
	
	public <T> T transToObj(Class<T> clazz) throws Exception{
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
	}
}
