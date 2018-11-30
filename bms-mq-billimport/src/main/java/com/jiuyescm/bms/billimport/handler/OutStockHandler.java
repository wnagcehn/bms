package com.jiuyescm.bms.billimport.handler;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jiuyescm.bms.base.dict.api.IWarehouseDictService;
import com.jiuyescm.bms.billimport.entity.BillFeesReceiveStorageTempEntity;
import com.jiuyescm.bms.billimport.service.IBillFeesReceiveStorageTempService;
import com.jiuyescm.bms.excel.data.DataColumn;
import com.jiuyescm.bms.excel.data.DataRow;
import com.jiuyescm.common.utils.DateUtil;
import com.jiuyescm.constants.BmsEnums;
import com.jiuyescm.exception.BizException;

/**
 * TB
 * @author zhaofeng
 *
 */
@Component("TB")
public class OutStockHandler extends CommonHandler<BillFeesReceiveStorageTempEntity> {

	@Autowired IBillFeesReceiveStorageTempService billFeesReceiveStorageTempService;
	@Autowired IWarehouseDictService warehouseDictService;
	
	@Override
	public List<BillFeesReceiveStorageTempEntity> transRowToObj(DataRow dr)
			throws Exception {
		
		//异常信息
		String errorMessage="";
		
		List<BillFeesReceiveStorageTempEntity> list = new ArrayList<BillFeesReceiveStorageTempEntity>();
		
		//判断空白行
		DataColumn outStockCo=dr.getColumn("出库单号");
		DataColumn customerCo=dr.getColumn("商家名称");
		if(outStockCo!=null && customerCo!=null &&StringUtils.isBlank(outStockCo.getColValue()+customerCo.getColValue())){
			return list;
		}
		
		BillFeesReceiveStorageTempEntity entity = new BillFeesReceiveStorageTempEntity();
		BillFeesReceiveStorageTempEntity entity1 = new BillFeesReceiveStorageTempEntity();
		BillFeesReceiveStorageTempEntity entity2 = new BillFeesReceiveStorageTempEntity();
		for (DataColumn dc:dr.getColumns()) {
			System.out.println("列名【" + dc.getColName() + "】|值【"+ dc.getColValue() + "】");
			try {
				switch (dc.getColName()) {
				case "仓库名称":
					if (StringUtils.isNotBlank(dc.getColValue())) {
						entity.setWarehouseName(dc.getColValue());
						//如果没找到，报错
						String warehouseCode = warehouseDictService.getWarehouseCodeByName(dc.getColValue());
						if (StringUtils.isNotBlank(warehouseCode)) {
							entity.setWarehouseCode(warehouseCode);
						}else {
							errorMessage+="仓库不存在;";
						}
					}else {
						errorMessage+="仓库必填;";
					}		
					break;
				case "发货时间":
					if (StringUtils.isNotBlank(dc.getColValue())) {
						entity.setCreateTime(DateUtil.transStringToTimeStamp(dc.getColValue()));
					}else {
						errorMessage+="发货时间必填;";
					}
					break;
				case "单据类型":
					entity.setOrderType(dc.getColValue());
					break;
				case "出库单号":
					if (StringUtils.isNotBlank(dc.getColValue())) {
						entity.setOrderNo(dc.getColValue());
					}else {
						errorMessage+="出库单号必填;";
					}
					break;
				case "温度类型":
					entity.setTempretureType(BmsEnums.tempretureType.getCode(dc.getColValue()));
					break;
				case "出库件数":
					if (StringUtils.isNotBlank(dc.getColValue())) {
						entity.setTotalQty(Integer.valueOf(dc.getColValue()));
					}	
					break;
				case "出库箱数":
					if (StringUtils.isNotBlank(dc.getColValue())) {
						entity.setTotalBox(new BigDecimal(dc.getColValue()));
					}else {
						errorMessage+="出库箱数必填;";
					}
					break;
				case "出库重量(吨)":
					if (StringUtils.isNotBlank(dc.getColValue())) {
						entity.setTotalWeight(new BigDecimal(dc.getColValue()));
					}else {
						errorMessage+="出库重量必填;";
					}
					break;
				default:
					break;
				}
			} catch (Exception e) {
				errorMessage+="列【"+ dc.getColName() + "】格式不正确;";
			}
		}
	
		for (DataColumn dc:dr.getColumns()) {
			try {
				switch (dc.getColName()) {
				case "B2B订单操作费":
					if (StringUtils.isNotBlank(dc.getColValue())) {
						PropertyUtils.copyProperties(entity1, entity);
						entity1.setBillNo(billNo);
						entity1.setSubjectCode("wh_b2b_work");
						entity1.setAmount(new BigDecimal(dc.getColValue()).setScale(2, BigDecimal.ROUND_HALF_UP));
						//list.add(entity1);
					}else {
						errorMessage+="B2B订单操作费必填;";
					}
					break;
				case "出库装车费":
					if (StringUtils.isNotBlank(dc.getColValue())) {
						PropertyUtils.copyProperties(entity2, entity);
						entity2.setBillNo(billNo);
						entity2.setSubjectCode("wh_b2b_handwork");
						entity2.setAmount(new BigDecimal(dc.getColValue()).setScale(2, BigDecimal.ROUND_HALF_UP));
						//list.add(entity2);
					}else {
						errorMessage+="出库装车费必填;";
					}
					break;
				default:
					break;
				}
			} catch (Exception e) {
				errorMessage+="列【"+ dc.getColName() + "】格式不正确;";
			}	
		}
		
		//重复性校验
		if(StringUtils.isNotBlank(entity.getOrderNo())){
			if(repeatMap.containsKey(entity.getOrderNo())){
				errorMessage += "数据重复--第【"+repeatMap.get(entity.getOrderNo())+"】行已存在出库单号【"+entity.getOrderNo()+";";
			}else{
				repeatMap.put(entity.getOrderNo(), dr.getRowNo());
			}
		}
		
		if (StringUtils.isNotBlank(entity1.getOrderNo())) {
			list.add(entity1);
		}
		if (StringUtils.isNotBlank(entity2.getOrderNo())) {
			list.add(entity2);
		}
		
		if(StringUtils.isNotBlank(errorMessage)){
			throw new BizException("行【" + dr.getRowNo()+"】"+ errorMessage);
		}
		
		return list;
	}

	@Override
	public void transErr(DataRow dr) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void save() {
		if (null != list && list.size() > 0) {
			billFeesReceiveStorageTempService.insertBatchTemp(list);
		}
	}



}
