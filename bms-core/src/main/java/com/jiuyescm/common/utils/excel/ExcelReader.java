package com.jiuyescm.common.utils.excel;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.google.common.collect.Lists;

public class ExcelReader implements IFileReader {

	@Override
	public List<Map<String, String>> getFileContent(String fileName) throws Exception {
		InputStream fis=new FileInputStream(fileName);
		return this.getFileContent(fis);
	}

	@Override
	public List<Map<String, String>> getExcelTitle(InputStream fis)
			throws Exception {
		List<Map<String, String>> list = Lists.newArrayList();
		try {
			HSSFWorkbook workbook = new HSSFWorkbook(fis);
			HSSFSheet sheet = workbook.getSheetAt(0);
			HSSFRow row0 =sheet.getRow(0);
			Map<Integer, String> colsName = CellValue.getMapColNames(row0);
			Object[] cols = colsName.keySet().toArray();
			Map<String, String> line = null;
			HSSFRow row=null;
			line = new HashMap<String, String>();
			for (Object col : cols) {
				row =sheet.getRow(0);
				if(row == null)continue;
				String data = CellValue.getStringValue(row.getCell((Integer) col), "");
				String colName = colsName.get(col).trim().toLowerCase();
				if (StringUtils.isBlank(data)) {
					continue;
				}
				line.put(colName, data);
			}
			if(line.size()>0){
				list.add(line);
			}
			return list;
		} catch (Exception e) {
			throw new Exception("文件无法解析");
		}finally{
			if(fis!=null)
				fis.close();
		}
	}
	
	@Override
	public List<Map<String, String>> getFileContent(InputStream fis) throws Exception {
		List<Map<String, String>> list = Lists.newArrayList();
		try {
			HSSFWorkbook workbook = new HSSFWorkbook(fis);
			HSSFSheet sheet = workbook.getSheetAt(0);
			int rows = sheet.getLastRowNum();
			if(rows==0)return null;
			HSSFRow row0 =sheet.getRow(0);
			Map<Integer, String> colsName = CellValue.getMapColNames(row0);
			Object[] cols = colsName.keySet().toArray();
			Map<String, String> line = null;
			HSSFRow row=null;
			for (int i = 1; i <= rows; i++) {
				line = new HashMap<String, String>();
				for (Object col : cols) {
					row =sheet.getRow(i);
					if(row == null)continue;
					String data = CellValue.getStringValue(row.getCell((Integer) col), "");
					String colName = colsName.get(col).trim().toLowerCase();
					if (StringUtils.isBlank(data)) {
						continue;
					}
					line.put(colName, data);
				}
				if(line.size()>0){
					list.add(line);
				}
			}
			return list;
		} catch (Exception e) {
			throw new Exception("文件无法解析");
		}finally{
			if(fis!=null)
				fis.close();
		}
	}
}
