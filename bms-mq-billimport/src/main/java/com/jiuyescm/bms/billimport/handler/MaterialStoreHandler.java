package com.jiuyescm.bms.billimport.handler;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.jiuyescm.bms.billimport.IFeesHandler;
import com.jiuyescm.bms.billimport.entity.BillFeesReceiveAirTempEntity;
import com.jiuyescm.bms.billimport.entity.BillFeesReceiveStorageTempEntity;
import com.jiuyescm.bms.excel.ExcelXlsxReader;
import com.jiuyescm.bms.excel.data.DataRow;
import com.jiuyescm.bms.excel.opc.OpcSheet;

/**
 * 耗材商城
 * @author zhaofeng
 *
 */
@Component("耗材商城")
public class MaterialStoreHandler extends CommonHandler<BillFeesReceiveStorageTempEntity> {


	@Override
	public BillFeesReceiveStorageTempEntity transRowToObj(DataRow dr)
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
