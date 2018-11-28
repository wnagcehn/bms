package com.jiuyescm.bms.billimport.handler;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jiuyescm.bms.billimport.IFeesHandler;
import com.jiuyescm.bms.billimport.entity.BillFeesReceiveAirTempEntity;
import com.jiuyescm.bms.billimport.service.IBillFeesReceiveAirTempService;
import com.jiuyescm.bms.excel.ExcelXlsxReader;
import com.jiuyescm.bms.excel.callback.SheetReadCallBack;
import com.jiuyescm.bms.excel.data.DataColumn;
import com.jiuyescm.bms.excel.data.DataRow;
import com.jiuyescm.bms.excel.opc.OpcSheet;
import com.jiuyescm.bms.subject.service.IBmsSubjectInfoService;
import com.jiuyescm.bms.subject.vo.BmsSubjectInfoVo;
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
	
	@Autowired
	private IBillFeesReceiveAirTempService billFeesReceiveAirTempService;
	
	@Autowired IBmsSubjectInfoService bmsSubjectService;

	@Override
	public List<BillFeesReceiveAirTempEntity> transRowToObj(DataRow dr)
			throws Exception {
		List<BillFeesReceiveAirTempEntity> listEntity = new ArrayList<BillFeesReceiveAirTempEntity>();
		BillFeesReceiveAirTempEntity entity = new BillFeesReceiveAirTempEntity();
		entity.setRowExcelNo(dr.getRowNo());
		BillFeesReceiveAirTempEntity entity1 = new BillFeesReceiveAirTempEntity();
		BillFeesReceiveAirTempEntity entity2 = new BillFeesReceiveAirTempEntity();
		BillFeesReceiveAirTempEntity entity3 = new BillFeesReceiveAirTempEntity();
		BmsSubjectInfoVo  subject1 = bmsSubjectService.querySubjectByName("INPUT", "AIRTRANSPORT", "航空运费");
		BmsSubjectInfoVo  subject2 = bmsSubjectService.querySubjectByName("INPUT", "AIRTRANSPORT", "其他费用");
		BmsSubjectInfoVo  subject3 = bmsSubjectService.querySubjectByName("INPUT", "AIRTRANSPORT", "货物赔偿费");
		
		for (DataColumn dc : dr.getColumns()) {
			try {
				System.out.println("列名【" + dc.getColName() + "】|值【"
						+ dc.getColValue() + "】");
				
				switch (dc.getColName()) {
				case "区域仓":
					if (StringUtils.isNotBlank(dc.getColValue())) {
						entity.setWarehouseName(dc.getColValue());
					}
					break;
				case "发货日期":
					if (StringUtils.isNotBlank(dc.getColValue())) {
						Timestamp createTime =DateUtil.transStringToTimeStamp(dc
								.getColValue());
						entity.setCreateTime(createTime);
						entity.setCreateMonth(DateUtil.timeStamp2YYMM(createTime));
						}
					break;
				case "始发站":
					if (StringUtils.isNotBlank(dc.getColValue())) {
						entity.setSendSite(dc.getColValue());
					}
					break;
				case "目的站":
					if (StringUtils.isNotBlank(dc.getColValue())) {
						entity.setReceiveSite(dc.getColValue());
					}
					break;
				case "计费重量":
					if (StringUtils.isNotBlank(dc.getColValue())) {
						entity.setTotalWeight(new BigDecimal(dc.getColValue()));	
					}
					break;
				case "运单号":
					if (StringUtils.isNotBlank(dc.getColValue())) {
						entity.setWaybillNo(dc.getColValue());	
					}
					break;
				default:
					break;
				}

			} catch (Exception ex) {
				throw new BizException("行【" + dr.getRowNo() + "】，列【"
						+ dc.getColName() + "】格式不正确");
			}
		}
	
		for(DataColumn dc : dr.getColumns()){
			switch (dc.getColName()) {
			case "航空运费":
				if (StringUtils.isNotBlank(dc.getColValue())) {
					PropertyUtils.copyProperties(entity1, entity);
					entity1.setAmount(new BigDecimal(dc.getColValue()));
//					entity1.setFeesType("BASE");
//					if(subject1.getSubjectCode()!=null){
//						entity1.setSubjectCode(subject1.getSubjectCode());
//					}
					listEntity.add(entity1);	
				}
				break;
			case "其他费用":
				if (StringUtils.isNotBlank(dc.getColValue())) {
					PropertyUtils.copyProperties(entity2, entity);
					entity2.setAmount(new BigDecimal(dc.getColValue()));
//					entity2.setFeesType("OTHER");
//					if(subject2.getSubjectCode()!=null){
//						entity2.setSubjectCode(subject2.getSubjectCode());
//					}
					listEntity.add(entity2);	
				}
				break;
			case "货物赔偿费":
				if (StringUtils.isNotBlank(dc.getColValue())) {
					PropertyUtils.copyProperties(entity3, entity);
					entity3.setAmount(new BigDecimal(dc.getColValue()));
//					entity3.setFeesType("BASE");
//					if(subject3.getSubjectCode()!=null){
//						entity3.setSubjectCode(subject3.getSubjectCode());
//					}
					listEntity.add(entity3);
				}
				break;
			default:
				break;
			}

		}
		
		return listEntity;
	}

	@Override
	public void transErr(DataRow dr) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void save() {
		billFeesReceiveAirTempService.insertBatchTemp(list);

	}

}
