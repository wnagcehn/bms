package com.jiuyescm.bms.billimport.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.jiuyescm.bms.billimport.IFeesHandler;
import com.jiuyescm.bms.excel.ExcelXlsxReader;
import com.jiuyescm.bms.excel.opc.OpcSheet;

/**
 * 耗材使用费
 * @author zhaofeng
 *
 */
@Component("耗材使用费")
public class MaterialUseHandler implements IFeesHandler {

	private static final Logger logger = LoggerFactory.getLogger(MaterialUseHandler.class);
	//List<BillFeesReceiveDispatchTempEntity> rowList = new ArrayList<>();
	
	@Override
	public void process(ExcelXlsxReader xlsxReader, OpcSheet sheet) throws Exception {

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
