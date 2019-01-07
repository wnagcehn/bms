package com.jiuyescm.bms.billimport;

import java.util.Map;
import com.jiuyescm.bms.excel.data.Sheet;
import com.jiuyescm.bms.excel.data.XlsxWorkBook;


public interface IFeesHandler {

	void process(XlsxWorkBook book,Sheet sheet,Map<String, Object> param) throws Exception;
	
	
	/*void saveTo();
	
	void validate();
	
	void errExport();*/
	
	
	
}
