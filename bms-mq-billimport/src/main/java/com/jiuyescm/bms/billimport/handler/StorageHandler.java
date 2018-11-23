package com.jiuyescm.bms.billimport.handler;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jiuyescm.bms.billimport.IFeesHandler;
import com.jiuyescm.bms.billimport.test.VisitorRole;
import com.jiuyescm.bms.excel.ExcelXlsxReader;
import com.jiuyescm.bms.excel.opc.OpcSheet;

@Component("仓储")
public class StorageHandler implements IFeesHandler {
	
	private static final Logger logger = LoggerFactory.getLogger(StorageHandler.class);
	//List<BillFeesReceiveDispatchTempEntity> rowList = new ArrayList<>();
	
	@Override
	public void process(ExcelXlsxReader xlsxReader, OpcSheet sheet) throws Exception {

	}
	
	@Override
	public void getRows() {
		// TODO Auto-generated method stub
		System.out.println("仓储费");
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
