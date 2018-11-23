package com.jiuyescm.bms.billimport.handler;

import org.springframework.stereotype.Component;

import com.jiuyescm.bms.billimport.IFeesHandler;
import com.jiuyescm.bms.excel.ExcelXlsxReader;
import com.jiuyescm.bms.excel.opc.OpcSheet;

@Component("宅配")
public class DispatchHandler implements IFeesHandler {

	
	@Override
	public void process(ExcelXlsxReader xlsxReader, OpcSheet sheet) throws Exception{
		
	}
	
	@Override
	public void getRows() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void saveTo() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void validate() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void errExport() {
		// TODO Auto-generated method stub
		
	}

	



	
	
}
