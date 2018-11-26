package com.jiuyescm.bms.billimport.handler;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.jiuyescm.bms.billimport.IFeesHandler;
import com.jiuyescm.bms.billimport.entity.BillFeesReceiveDispatchTempEntity;
import com.jiuyescm.bms.excel.ExcelXlsxReader;
import com.jiuyescm.bms.excel.data.DataRow;
import com.jiuyescm.bms.excel.opc.OpcSheet;

/**
 * 宅配
 * @author zhaofeng
 *
 */
@Component("宅配")
public class DispatchHandler extends CommonHandler<BillFeesReceiveDispatchTempEntity> {

	@Override
	public BillFeesReceiveDispatchTempEntity transRowToObj(DataRow dr)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void transErr(DataRow dr) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void save() {
		// TODO Auto-generated method stub
		
	}

	
}
