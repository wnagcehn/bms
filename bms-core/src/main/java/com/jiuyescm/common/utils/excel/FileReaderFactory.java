package com.jiuyescm.common.utils.excel;

public class FileReaderFactory {
	private static final String FILE_EXT_EXCEL = "XLS";
	private static final String FILE_EXT_EXCELX = "XLSX";

	public static IFileReader getFileReader(String fileExt) {
		if (fileExt.equalsIgnoreCase(FILE_EXT_EXCEL)) {
			return new ExcelReader();
		} else if (fileExt.equalsIgnoreCase(FILE_EXT_EXCELX)) {
			return new ExcelxReader();
		} else {
			return null;
		}

	}
}
