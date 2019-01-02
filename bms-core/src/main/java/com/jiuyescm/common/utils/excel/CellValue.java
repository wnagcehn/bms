package com.jiuyescm.common.utils.excel;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;


import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;




public class CellValue {
	
	public static Map<Integer, String> getMapColNames(Row row) {
		int cols = row.getPhysicalNumberOfCells();
		Map<Integer, String> colNames = new HashMap<Integer, String>();
		for (int i = 0; i < cols; i++) {
			String colName = getStringValue(row.getCell(i), "");
			if (StringUtils.isNotBlank(colName))
				colNames.put(i, colName.trim());
		}
		return colNames;
	}
	
	public static Map<Integer, String> getMapColNames(Cell[] cells) {
		Map<Integer, String> colNames = new HashMap<Integer, String>();
		for (int i = 0; i < cells.length; i++) {
			String colName = getStringValue(cells[i], "");
			if (StringUtils.isNotBlank(colName))
				colNames.put(i, colName.trim());
		}
		return colNames;
	}
	
	public static String getStringValue(Cell cell, String defaultValue) {
		String strReturn = defaultValue;
		try {
			if (cell == null)
				return defaultValue;
			int type = cell.getCellType();
			switch (type) {
			case Cell.CELL_TYPE_BLANK:
				strReturn = "";
				break;
			case Cell.CELL_TYPE_STRING:
				strReturn = cell.getStringCellValue().trim();
				break;
			case Cell.CELL_TYPE_NUMERIC:
				boolean isDate = DateUtil.isCellDateFormatted(cell);
				if (isDate) {
					double d = cell.getNumericCellValue();
					Date date = DateUtil.getJavaDate(d);
					strReturn =DateFormatUtils.format(date, "yyyy-M-d HH:mm:ss");
					if (strReturn == null) {
						strReturn = cell.getDateCellValue().toLocaleString();
					}
				} else {
					cell.setCellType(Cell.CELL_TYPE_STRING);  
					String temp = cell.getStringCellValue();  
					if(temp.indexOf(".")>-1){  
						strReturn = String.valueOf(new Double(temp)).trim();  
					}else{  
						strReturn = temp.trim();  
					}  
//					strReturn=cell.toString();
				}
				break;
			case Cell.CELL_TYPE_BOOLEAN:
				strReturn = String.valueOf(cell.getBooleanCellValue());
				break;
			case Cell.CELL_TYPE_ERROR:
				strReturn = defaultValue;
				break;
			default:
				strReturn = defaultValue;

			}
		} catch (Exception e) {
			e.printStackTrace();
			strReturn = defaultValue;
		}
		return strReturn;
	}
}
