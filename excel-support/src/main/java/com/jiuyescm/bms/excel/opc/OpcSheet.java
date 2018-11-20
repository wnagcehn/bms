package com.jiuyescm.bms.excel.opc;

import java.util.HashMap;
import java.util.Map;

import org.xml.sax.InputSource;

import com.google.common.collect.Maps;

public class OpcSheet {

	private String sheetName;
	private Integer sheetId;
	private Integer rows;
	
	// <行号 ,<列号,值>>
	private Map<Integer, Map<Integer,String>> contents = Maps.newLinkedHashMap();
	
	//表头信息
	private Map<String, String> headColumn = new HashMap<String, String>(); 
	
	private Map<Integer, String> colValueMap = new HashMap<Integer, String>();
	private Map<String, Integer> colkeyMap = new HashMap<String, Integer>();
	
	public Map<String, String> getHeadColumn(){
		return headColumn;
	}
	
	public OpcSheet(Integer sheetId,String sheetName){
		this.sheetId = sheetId;
		this.sheetName = sheetName;
	}
	
	public String getSheetName() {
		return sheetName;
	}


	public Integer getSheetId() {
		return sheetId;
	}


	public Integer getRows() {
		return rows;
	}

	public Map<Integer, String> getColValueMap() {
		return colValueMap;
	}

	public Map<Integer, Map<Integer,String>> getContents() {
		return contents;
	}
	
}
