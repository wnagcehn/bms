package com.jiuyescm.bms.billimport.handler;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.jiuyescm.bms.billimport.IFeesHandler;
import com.jiuyescm.bms.billimport.entity.BillFeesReceiveStorageTempEntity;
import com.jiuyescm.bms.excel.ExcelXlsxReader;
import com.jiuyescm.bms.excel.data.DataColumn;
import com.jiuyescm.bms.excel.data.DataRow;
import com.jiuyescm.bms.excel.opc.OpcSheet;

/**
 * 仓储
 * @author zhaofeng
 *
 */
@Component("仓储")
public class StorageHandler extends CommonHandler<BillFeesReceiveStorageTempEntity> {

	@Override
	public List<BillFeesReceiveStorageTempEntity> transRowToObj(DataRow dr)
			throws Exception {
		BillFeesReceiveStorageTempEntity entity = new BillFeesReceiveStorageTempEntity();
		DateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
		for (DataColumn dc : dr.getColumns()) {
			switch (dc.getColName()) {
			case "日期":
				Timestamp time = null;
				if (StringUtils.isNotBlank(dc.getColValue())) {
					time = new Timestamp(sdf.parse(dc.getColValue()).getTime());
				}		
				entity.setCreateTime(time);
				break;
			case "冷冻":
				if (StringUtils.isNotBlank(dc.getColValue())) {
					entity.setTempretureType("LD");
					entity.setTotalQty(Integer.valueOf(dc.getColValue()));
				}		
			default:
				break;
			}
		}
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
