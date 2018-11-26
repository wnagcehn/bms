package com.jiuyescm.bms.billimport.handler;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.stereotype.Component;

import com.jiuyescm.bms.billimport.IFeesHandler;
import com.jiuyescm.bms.billimport.entity.BillFeesReceiveStorageTempEntity;
import com.jiuyescm.bms.excel.ExcelXlsxReader;
import com.jiuyescm.bms.excel.data.DataColumn;
import com.jiuyescm.bms.excel.data.DataRow;
import com.jiuyescm.bms.excel.opc.OpcSheet;

/**
 * TB
 * @author wangchen
 *
 */

@Component("TB")
public class TBHandler implements IFeesHandler {

	@Override
	public void process(ExcelXlsxReader xlsxReader, OpcSheet sheet, Map<String, Object> param) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Object transRowToObj(DataRow dr) throws Exception {
		BillFeesReceiveStorageTempEntity entity = new BillFeesReceiveStorageTempEntity();
		BillFeesReceiveStorageTempEntity zEntity = new BillFeesReceiveStorageTempEntity();
		for (DataColumn dc:dr.getColumns()) {
			switch (dc.getColName()) {
			case "仓库名称":
				entity.setWarehouseName(dc.getColValue());
				break;
			case "发货时间":
				entity.setCreateTime(Timestamp.valueOf(dc.getColValue()));
				break;
			case "商家名称":
				entity.setCustomerName(dc.getColValue());
				break;
			case "单据类型":
				entity.setOrderType(dc.getColValue());
				break;
			case "出库单号":
				entity.setOrderNo(dc.getColValue());
				break;
			case "温度类型":
				entity.setTempretureType(dc.getColValue());
				break;
			case "出库件数":
				entity.setTotalQty(Integer.valueOf(dc.getColValue()));
				break;
			case "出库箱数":
				entity.setTotalBox(new BigDecimal(dc.getColValue()));
				break;
			case "出库重量(吨)":
				entity.setTotalWeight(new BigDecimal(dc.getColValue()));
				break;
			case "B2B订单操作费":
				entity.setAmount(new BigDecimal(dc.getColValue()));
				break;
			case "出库装车费":
				PropertyUtils.copyProperties(zEntity, entity);
				zEntity.setAmount(new BigDecimal(dc.getColValue()));
				break;
			default:
				break;
			}
		}
		return null;
	}

}
