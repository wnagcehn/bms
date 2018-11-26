package com.jiuyescm.bms.billimport;

import java.util.Map;

import org.apache.poi.ss.formula.functions.T;

import com.jiuyescm.bms.excel.ExcelXlsxReader;
import com.jiuyescm.bms.excel.data.DataRow;
import com.jiuyescm.bms.excel.opc.OpcSheet;


public interface IFeesHandler {

	void process(ExcelXlsxReader xlsxReader,OpcSheet sheet,Map<String, Object> param) throws Exception;
	
	
	/*void saveTo();
	
	void validate();
	
	void errExport();*/
	
	
	
}
