package com.jiuyescm.bms.billimport.handler;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jiuyescm.bms.base.dict.api.IWarehouseDictService;
import com.jiuyescm.bms.billimport.entity.BillFeesReceiveAirTempEntity;
import com.jiuyescm.bms.billimport.service.IBillFeesReceiveAirTempService;
import com.jiuyescm.bms.excel.data.DataColumn;
import com.jiuyescm.bms.excel.data.DataRow;
import com.jiuyescm.bms.subject.service.IBmsSubjectInfoService;
import com.jiuyescm.bms.subject.vo.BmsSubjectInfoVo;
import com.jiuyescm.common.utils.DateUtil;

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
	@Autowired IWarehouseDictService warehouseDictService;
	@Autowired
	IBmsSubjectInfoService bmsSubjectService;

	@Override
	public List<BillFeesReceiveAirTempEntity> transRowToObj(DataRow dr)
			throws Exception {
		String errorMessage = "";
		List<BillFeesReceiveAirTempEntity> listEntity = new ArrayList<BillFeesReceiveAirTempEntity>();
		
		DataColumn weight=dr.getColumn("计费重量");
		DataColumn orderNo=dr.getColumn("运单号");
		if(weight!=null && orderNo!=null &&StringUtils.isBlank(weight.getColValue()+orderNo.getColValue())){
			return listEntity;
		}
		
		BillFeesReceiveAirTempEntity entity = new BillFeesReceiveAirTempEntity();
		entity.setRowExcelNo(dr.getRowNo());
		BillFeesReceiveAirTempEntity entity1 = new BillFeesReceiveAirTempEntity();
		BillFeesReceiveAirTempEntity entity2 = new BillFeesReceiveAirTempEntity();
		BillFeesReceiveAirTempEntity entity3 = new BillFeesReceiveAirTempEntity();
		BmsSubjectInfoVo subject1 = bmsSubjectService.querySubjectByName(
				"INPUT", "AIRTRANSPORT", "航空运费");
		BmsSubjectInfoVo subject2 = bmsSubjectService.querySubjectByName(
				"INPUT", "AIRTRANSPORT", "其他费用");
		BmsSubjectInfoVo subject3 = bmsSubjectService.querySubjectByName(
				"INPUT", "AIRTRANSPORT", "货物赔偿费");

		for (DataColumn dc : dr.getColumns()) {
			try {
				switch (dc.getColName()) {
				case "区域仓":
					if (StringUtils.isNotBlank(dc.getColValue())) {
						entity.setWarehouseName(dc.getColValue());					
					}
					break;
				case "发货日期":
					if (StringUtils.isNotBlank(dc.getColValue())) {
						Timestamp createTime = DateUtil
								.transStringToTimeStamp(dc.getColValue());
						entity.setCreateTime(createTime);
						entity.setCreateMonth(DateUtil
								.timeStamp2YYMM(createTime));
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
					} else {
						errorMessage += "计费重量不能为空;";
					}
					break;
				case "运单号":
					if (StringUtils.isNotBlank(dc.getColValue())) {
						entity.setWaybillNo(dc.getColValue());
					} else {
						errorMessage += "运单号不能为空;";
					}
					break;
				default:
					break;
				}

			} catch (Exception e) {
				errorMessage += "列名(" + dc.getColName() + ")值("
						+ dc.getColValue() + ")" + "转换失败;";
			}
		}

		entity.setBillNo(billNo);
		entity.setCustomerName(customerName);
		entity.setCustomerId(customerId);
		for (DataColumn dc : dr.getColumns()) {
			try {
				switch (dc.getColName()) {
				case "航空运费":
					if (StringUtils.isNotBlank(dc.getColValue())) {
						PropertyUtils.copyProperties(entity1, entity);
						entity1.setAmount(new BigDecimal(dc.getColValue()));
						// entity1.setFeesType("BASE");
						if (subject1.getSubjectCode() != null) {							
							entity1.setSubjectCode(subject1.getSubjectCode());
						}
						listEntity.add(entity1);
					}
					break;
				case "其他费用":
					if (StringUtils.isNotBlank(dc.getColValue())) {
						PropertyUtils.copyProperties(entity2, entity);
						entity2.setAmount(new BigDecimal(dc.getColValue()));
						// entity2.setFeesType("OTHER");
						if (subject2.getSubjectCode() != null) {
							entity2.setSubjectCode(subject2.getSubjectCode());
						}
						listEntity.add(entity2);
					}
					break;
				case "货物赔偿费":
					if (StringUtils.isNotBlank(dc.getColValue())) {
						PropertyUtils.copyProperties(entity3, entity);
						entity3.setAmount(new BigDecimal(dc.getColValue()));
						// entity3.setFeesType("BASE");
						if (subject3.getSubjectCode() != null) {
							entity3.setSubjectCode(subject3.getSubjectCode());
						}
						listEntity.add(entity3);
					}
					break;
				default:
					break;
				}
			} catch (Exception e) {
				errorMessage += "列名(" + dc.getColName() + ")值("
						+ dc.getColValue() + ")转换失败;";
			}

		}

		// 重复性校验
		if (StringUtils.isNotBlank(entity.getWaybillNo())) {
			if (repeatMap.containsKey(entity.getWaybillNo())) {
				errorMessage += "与第"
						+ repeatMap.get(entity.getWaybillNo()) + "行运单号重复;";
			} else {
				repeatMap.put(entity.getWaybillNo(), dr.getRowNo());
			}
		}
		
		if (!errorMessage.equals("")) {
			throw new Exception(errorMessage);
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

	@Override
	public String validate(List<String> columns) throws Exception {
		// TODO Auto-generated method stub
		String result="";
		String[] str = {"计费重量", "运单号"}; //必填列
		
		for (String s : str) {
			if(!columns.contains(s)){
				result+=s+"必须存在;";
			}
		} 
		
		if(StringUtils.isNotBlank(result)){
			result="【"+sheetName+"】表头:"+result;
			return result;
		}
		
		return "SUCC";
	}

}
