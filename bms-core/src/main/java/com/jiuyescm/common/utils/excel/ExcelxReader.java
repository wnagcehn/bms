package com.jiuyescm.common.utils.excel;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.google.common.collect.Lists;

public class ExcelxReader implements IFileReader {

	private static final Logger logger = Logger.getLogger(ExcelxReader.class.getName());

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
			XSSFWorkbook workbook = new XSSFWorkbook(fis);
			XSSFSheet sheet = workbook.getSheetAt(0);
			XSSFRow row0 =sheet.getRow(sheet.getFirstRowNum());
			Map<Integer, String> colsName = CellValue.getMapColNames(row0);
			Object[] cols = colsName.keySet().toArray();
			Map<String, String> line = null;
			line = new HashMap<String, String>();
			for (Object col : cols) {
				String data = CellValue.getStringValue(sheet.getRow(0)
						.getCell((Integer) col), "");
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
			logger.error("文件解析异常", e);
			throw new Exception("文件无法解析");
		}finally{
			if(fis!=null)
				fis.close();
		}
	}

	@Override
	public List<Map<String, String>> getFileContent(InputStream fis)
			throws Exception {
		List<Map<String, String>> list = Lists.newArrayList();
		try {
			XSSFWorkbook workbook = new XSSFWorkbook(fis);
			XSSFSheet sheet = workbook.getSheetAt(0);
			int rows = sheet.getLastRowNum();
			if(rows==0)return null;
			XSSFRow row0 =sheet.getRow(sheet.getFirstRowNum());
			Map<Integer, String> colsName = CellValue.getMapColNames(row0);
			Object[] cols = colsName.keySet().toArray();
			Map<String, String> line = null;
			for (int i = 1; i <= rows; i++) {
				line = new HashMap<String, String>();
				for (Object col : cols) {
					String data = CellValue.getStringValue(sheet.getRow(i)
							.getCell((Integer) col), "");
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
			logger.error("文件解析异常", e);
			throw new Exception("文件无法解析");
		}finally{
			if(fis!=null)
				fis.close();
		}
	}
}
