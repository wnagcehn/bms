package com.jiuyescm.bms.excel.write;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class XSSFExporter extends ExportBase {

	public XSSFExporter(){
		super();
		workbook = new XSSFWorkbook();
	}
}
