package com.jiuyescm.bms.billimport.handler;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.jiuyescm.bms.billimport.entity.BillFeesReceiveTransportTempEntity;
import com.jiuyescm.bms.excel.data.DataRow;

@Component("干线")
public class TransportHandler extends CommonHandler<BillFeesReceiveTransportTempEntity> {

	@Override
	public List<BillFeesReceiveTransportTempEntity> transRowToObj(DataRow dr) throws Exception {
		// TODO Auto-generated method stub
		try{
			
		}
		catch(Exception ex){
			transErr(dr);
		}
		return null;
	}

	@Override
	public void transErr(DataRow dr) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void save() {
		
		
		
	}

	

	

}
