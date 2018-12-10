package com.jiuyescm.bms.billimport.handler;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jiuyescm.bms.base.dict.api.IWarehouseDictService;
import com.jiuyescm.bms.billimport.entity.BillFeesReceiveStorageTempEntity;
import com.jiuyescm.bms.billimport.service.IBillFeesReceiveStorageTempService;
import com.jiuyescm.bms.excel.data.DataColumn;
import com.jiuyescm.bms.excel.data.DataRow;
import com.jiuyescm.common.utils.DateUtil;
import com.jiuyescm.exception.BizException;

/**
 * 耗材商城
 * @author zhaofeng
 *
 */
@Component("耗材商城")
public class MaterialStoreHandler extends CommonHandler<BillFeesReceiveStorageTempEntity> {

	@Autowired IBillFeesReceiveStorageTempService billFeesReceiveStorageTempService;
	@Autowired IWarehouseDictService warehouseDictService;
	
	@Override
	public List<BillFeesReceiveStorageTempEntity> transRowToObj(DataRow dr)
			throws Exception {
		
		//异常信息
		String errorMessage="";
		List<BillFeesReceiveStorageTempEntity> list = new ArrayList<BillFeesReceiveStorageTempEntity>();
		
		DataColumn orderCo=dr.getColumn("定货单号");
		DataColumn customerCo=dr.getColumn("客户名称");
		if(orderCo!=null && customerCo!=null &&StringUtils.isBlank(orderCo.getColValue()+customerCo.getColValue())){
			return list;
		}
		
		BillFeesReceiveStorageTempEntity entity = new BillFeesReceiveStorageTempEntity();
		for (DataColumn dc:dr.getColumns()) {
			try {
				switch (dc.getColName()) {
				case "仓库名称":
					if (StringUtils.isNotBlank(dc.getColValue())) {
						entity.setWarehouseName(dc.getColValue());
						//如果没找到，报错
						String warehouseCode = warehouseDictService.getWarehouseCodeByName(dc.getColValue());		
						if(StringUtils.isNotBlank(warehouseCode)){
							entity.setWarehouseCode(warehouseCode);
						}else{
							errorMessage+="仓库不存在;";
						}
					}else {
						errorMessage+="仓库必填;";
					}
					break;
				case "定货单号":
					if (StringUtils.isNotBlank(dc.getColValue())) {
						entity.setOrderNo(dc.getColValue());
					}else {
						errorMessage+="定货单号必填;";
					}
					break;
				case "定货日期":
					if (StringUtils.isNotBlank(dc.getColValue())) {
						entity.setCreateTime(DateUtil.transStringToTimeStamp(dc.getColValue()));
						entity.setCreateMonth(DateUtil.transStringToInteger(dc.getColValue()));
					}else {
						errorMessage+="定货日期必填;";
					}
					break;
				case "销售额":
					if (StringUtils.isNotBlank(dc.getColValue())) {
						entity.setAmount(new BigDecimal(dc.getColValue()).setScale(2, BigDecimal.ROUND_HALF_UP));	
					}	
					break;
				default:
					break;
				}
			} catch (Exception e) {
				errorMessage+="列【"+ dc.getColName() + "】格式不正确;";
			}
		}
		//商城耗材费
		if (StringUtils.isNotBlank(entity.getOrderNo())) {
			entity.setBillNo(billNo);
			entity.setCustomerName(customerName);
			entity.setCustomerId(customerId);
			entity.setSubjectCode("wh_mall_material");
			list.add(entity);
		}
		
		//重复性校验
		if(StringUtils.isNotBlank(entity.getOrderNo())){
			if(repeatMap.containsKey(entity.getOrderNo())){
				errorMessage += "与第"
						+ repeatMap.get(entity.getOrderNo()) + "行订货单号重复;";
			}else{
				repeatMap.put(entity.getOrderNo(), dr.getRowNo());
			}
		}
		
		if(StringUtils.isNotBlank(errorMessage)){
			throw new BizException(errorMessage);
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

	@Override
	public String validate(List<String> columns) throws Exception {
		// TODO Auto-generated method stub
		String result="";
		String[] str = {"仓库名称", "定货单号", "定货日期"}; //必填列
		
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
