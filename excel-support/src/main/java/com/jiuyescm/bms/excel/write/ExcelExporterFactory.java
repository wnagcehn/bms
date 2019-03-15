package com.jiuyescm.bms.excel.write;

import com.jiuyescm.bms.excel.constants.ExportConstants;

public class ExcelExporterFactory {
	
	public static IExcelExporter createExporter(String type){
		IExcelExporter exporter = null;
		switch (type) {
		case ExportConstants.SXSSF:
			exporter = new SXSSFExporter();
			break;
		case ExportConstants.XSSF:
			exporter = new XSSFExporter();
			break;
		default:
			break;
		}
		return exporter;
	}
	
}
