package com.jiuyescm.bms.billimport.handler;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.stereotype.Component;

import com.jiuyescm.bms.billimport.IFeesHandler;
import com.jiuyescm.bms.billimport.entity.BillFeesReceiveAirTempEntity;
import com.jiuyescm.bms.excel.ExcelXlsxReader;
import com.jiuyescm.bms.excel.callback.SheetReadCallBack;
import com.jiuyescm.bms.excel.data.DataColumn;
import com.jiuyescm.bms.excel.data.DataRow;
import com.jiuyescm.bms.excel.opc.OpcSheet;
import com.jiuyescm.common.utils.DateUtil;
import com.jiuyescm.common.utils.excel.POISXSSUtil;
import com.jiuyescm.exception.BizException;
import com.jiuyescm.framework.fastdfs.model.StorePath;

/**
 * 航空
 * 
 * @author liuzhicheng
 * 
 */
@Component("航空")
public class AirHandler extends CommonHandler<BillFeesReceiveAirTempEntity> {


	@Override
	public List<BillFeesReceiveAirTempEntity> transRowToObj(DataRow dr) throws Exception {
		BillFeesReceiveAirTempEntity entity = new BillFeesReceiveAirTempEntity();
		for (DataColumn dc : dr.getColumns()) {
			try{
				System.out.println("列名【" + dc.getColName() + "】|值【"
						+ dc.getColValue() + "】");

				if (dc.getColName().equals("区域仓")) {
					entity.setWarehouseName(dc.getColValue());
				} else if (dc.getColName().equals("发货日期")) {
					entity.setCreateTime(DateUtil.transStringToTimeStamp(dc
							.getColValue()));
				} else if (dc.getColName().equals("始发站")) {
					entity.setSendSite(dc.getColValue());
				} else if (dc.getColName().equals("目的站")) {
					entity.setReceiveSite(dc.getColValue());
				}else if (dc.getColName().equals("计费重量")) {
					entity.setTotalWeight(new BigDecimal(dc.getColValue()));
				} else if (dc.getColName().equals("运单号")) {
					entity.setWaybillNo(dc.getColValue());
				}else if (dc.getColName().equals("航空运费")) {

				} else if (dc.getColName().equals("其他费用")) {

				} else if (dc.getColName().equals("货物赔偿费")) {

				}
			}
			catch(Exception ex){
				throw new BizException("行【"+dr.getRowNo()+"】，列【"+dc.getColName()+"】格式不正确");
			}
		}
		return entity;
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
