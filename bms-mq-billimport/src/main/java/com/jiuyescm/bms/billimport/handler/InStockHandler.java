package com.jiuyescm.bms.billimport.handler;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.jiuyescm.bms.billimport.IFeesHandler;
import com.jiuyescm.bms.excel.ExcelXlsxReader;
import com.jiuyescm.bms.excel.data.DataRow;
import com.jiuyescm.bms.excel.opc.OpcSheet;

/**
 * 入库
 * @author zhaofeng
 *
 */
@Component("入库")
public class InStockHandler implements IFeesHandler {

	@Override
	public void process(ExcelXlsxReader xlsxReader, OpcSheet sheet,
			Map<String, Object> param) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Object transRowToObj(DataRow dr) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	


}
