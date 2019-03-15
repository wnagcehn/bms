package com.jiuyescm.bms.excel.data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Maps;
import com.jiuyescm.bms.excel.write.ColMergeRegion;


public class WorkSheet {

	private int startIndex = 1;//sheet内容的开始行 1为第二行
	private String sheetName;
	private int nextIndex = 1;
	private List<Map<String, Object>> headInfoList;
	
	private List<String> vMergeColumn;
	private Map<String,CellRangeAddress> mergeRegions;
	
	private Map<String, ColMergeRegion> tempCols; //列名  合并区域映射
	
	private Map<String, List<String>> colValuesMap;
	
	private String lastColValue;//上一个单元格值
	private String firstColName;//第一个要合并的列
	private int startRowIndex;
	
	private Logger logger = LoggerFactory.getLogger(WorkSheet.class);
	
	public WorkSheet(String sheetName,int startIndex,List<Map<String, Object>> headInfoList){
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

	/**
	 * 设置合并单元格的列
	 * @param mergeColumn 多列用逗号隔开 eg(用户名,密码)
	 */
	public void setvMergeColumn(String mergeColumn) {
		
		if(mergeColumn == null || mergeColumn.length()==0){
			return;
		}
		colValuesMap = Maps.newLinkedHashMap();
		String[] columns = mergeColumn.split(",");//所有要合并的列
		firstColName = columns[0];//第一个要合并的列
		startRowIndex=startIndex;
		tempCols = new HashMap<String, ColMergeRegion>();
		this.vMergeColumn = Arrays.asList(columns);
		mergeRegions = new HashMap<String, CellRangeAddress>();
		logger.info("sheetName={},mergeColumn={},startRowIndex={}",sheetName,mergeColumn,startRowIndex);
		for (String columnName : vMergeColumn) {
			/*ColMergeRegion mergeRegion = new ColMergeRegion(columnName, columnName, startIndex);
			mergeRegions.put(columnName, mergeRegion);*/
			for (Map<String, Object> map : headInfoList) {
				if(map.containsKey(columnName)){
					int firstCol = headInfoList.indexOf(map);
					int lastCol =firstCol+1;
					CellRangeAddress rAddress = new CellRangeAddress(startIndex,0,firstCol,lastCol);
					mergeRegions.put(columnName, rAddress);
				}
			}
			
		}
	}
	
	/**
	 * 缓存不重复列数据
	 * @param colName 列名
	 * @param colValue 单元格值
	 */
	public void insertRow(Sheet sheet , Map<String, Object> dataItem){
		//第一行
		if(colValuesMap.size() == 0){
			//如果是第一个要合并的列
			for (String s : vMergeColumn) {
				if(s.equals(firstColName)){
					lastColValue = dataItem.get(s).toString();
				}
				colValuesMap.put(s, new ArrayList<String>());
				colValuesMap.get(s).add(dataItem.get(s).toString());
			}
		}
		else{
			//首列单元格相等
			if(lastColValue.equals(dataItem.get(firstColName).toString())){
				for (String s : vMergeColumn) {
					colValuesMap.get(s).add(dataItem.get(s).toString());
				}
			}
			else{
				mergeRegions.get(firstColName).setLastRow(startRowIndex+colValuesMap.get(firstColName).size());
				sheet.addMergedRegion(mergeRegions.get(firstColName));
				lastColValue = dataItem.get(firstColName).toString();
				startRowIndex += colValuesMap.get(firstColName).size();
				colValuesMap.put(firstColName, new ArrayList<String>());
				
				//合并
				//初始化 1：开始行 2：集合
			}
			
		}
		
		//如果此列包含在merge列中
		/*if(tempCols.containsKey(colName)){
			ColMergeRegion colreRegion = tempCols.get(colName);
			boolean ret = colreRegion.mergeRow(colValue);
			if(ret == false){ //当前单元格值与上一行的值相等
				
			}
			else{ //当前单元格值与上一行的值不相等,此时和并单元格,同时创建新的region
				CellRangeAddress cr = mergeRegions.get("colName");
				cr.setFirstRow(colreRegion.getStartRow());
				cr.setFirstRow(colreRegion.getEndRow());
				colreRegion = new ColMergeRegion(colName, colValue, startIndex);
			}
		}
		else{
			List<String> list = new ArrayList<String>();
			list.add(colValue);
			tempCols.put(colName, list);
		}*/
	}
	
	public List<String> getvMergeColumn(){
		return vMergeColumn;
	}

}
