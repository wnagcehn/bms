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
 * 仓库理赔
 * @author wangchen
 *
 */

@Component("仓库理赔")
public class PayAbnormalHandler extends CommonHandler<BillFeesReceiveStorageTempEntity>{
	
	@Autowired IBillFeesReceiveStorageTempService billFeesReceiveStorageTempService;
	@Autowired IWarehouseDictService warehouseDictService;
	
	@Override
	public List<BillFeesReceiveStorageTempEntity> transRowToObj(DataRow dr) throws Exception {
		//异常信息
		String errorMessage="";
		List<BillFeesReceiveStorageTempEntity> list = new ArrayList<BillFeesReceiveStorageTempEntity>();
		
		/*DataColumn warehouseCo=dr.getColumn("仓库");
		DataColumn customerCo=dr.getColumn("客户名称");
		if(warehouseCo!=null && customerCo!=null &&StringUtils.isBlank(warehouseCo.getColValue()+customerCo.getColValue())){
			return list;
		}*/
		
		boolean isWarehouseNull = false;
		boolean isCustomerNull = false;
		
		BillFeesReceiveStorageTempEntity entity = new BillFeesReceiveStorageTempEntity();
		//DateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
		for (DataColumn dc:dr.getColumns()) {
			try {
				switch (dc.getTitleName()) {
				case "客户名称":
					if (StringUtils.isBlank(dc.getColValue())) {
						isCustomerNull = true;
					}
					break;
				case "仓库":
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
						isWarehouseNull = true;
						errorMessage+="仓库必填;";
					}
					break;
				case "日期":
					if (StringUtils.isNotBlank(dc.getColValue())) {
						entity.setCreateTime(DateUtil.transStringToTimeStamp(dc.getColValue()));
						entity.setCreateMonth(DateUtil.transStringToInteger(dc.getColValue()));
					}else {
						errorMessage+="日期不存在;";
					}			
					break;
				case "金额":
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
		
		if(isWarehouseNull && isCustomerNull){
			return list;
		}
		
		//仓库理赔费
		if (null != entity) {
			entity.setBillNo(billNo);
			entity.setCustomerName(customerName);
			entity.setCustomerId(customerId);
			entity.setSubjectCode("wh_abnormal_pay");
			list.add(entity);
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
	public int save() {
		int result=	billFeesReceiveStorageTempService.insertBatchTemp(list);
		return result;
	}

	@Override
	public String validate(List<String> columns) throws Exception {
		// TODO Auto-generated method stub
		String result="";
		String[] str = {"仓库", "日期"}; //必填列
		
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
