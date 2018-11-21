package com.jiuyescm.bms.billimport;

import com.jiuyescm.bms.excel.ExcelXlsxReader;
import com.jiuyescm.bms.excel.opc.OpcSheet;


public interface IFeesHandler {

	void process(ExcelXlsxReader xlsxReader,OpcSheet sheet) throws Exception;
	
	void getRows();
	
	void saveTo();
	
	void validate();
	
	void errExport();
	
}
