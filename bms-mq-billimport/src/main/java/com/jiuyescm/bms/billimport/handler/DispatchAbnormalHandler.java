package com.jiuyescm.bms.billimport.handler;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jiuyescm.bms.base.dict.api.IWarehouseDictService;
import com.jiuyescm.bms.billimport.entity.BillFeesReceiveDispatchTempEntity;
import com.jiuyescm.bms.billimport.service.IBillFeesReceiveDispatchTempService;
import com.jiuyescm.bms.excel.data.DataColumn;
import com.jiuyescm.bms.excel.data.DataRow;
import com.jiuyescm.common.utils.DateUtil;
import com.jiuyescm.exception.BizException;

/**
 * 宅配理赔
 * @author zhaofeng
 *
 */
@Component("宅配理赔")
public class DispatchAbnormalHandler extends CommonHandler<BillFeesReceiveDispatchTempEntity>{

	@Autowired
	private IBillFeesReceiveDispatchTempService billFeesReceiveDispatchTempService;
	@Autowired
	private IWarehouseDictService warehouseDictService;

	@Override
	public List<BillFeesReceiveDispatchTempEntity> transRowToObj(DataRow dr) throws Exception {
		//异常信息
		String errorMessage="";
		List<BillFeesReceiveDispatchTempEntity> list = new ArrayList<BillFeesReceiveDispatchTempEntity>();
		DataColumn waybillCo=dr.getColumn("运单号");
		DataColumn customerCo=dr.getColumn("客户");
		if(waybillCo!=null && customerCo!=null &&StringUtils.isBlank(waybillCo.getColValue()+customerCo.getColValue())){
			return list;
		}
		
		BillFeesReceiveDispatchTempEntity entity = new BillFeesReceiveDispatchTempEntity();

		for (DataColumn dc:dr.getColumns()) {
			try {
				switch (dc.getColName()) {
				case "发货仓库":
					if (StringUtils.isNotBlank(dc.getColValue())) {
						entity.setWarehouseName(dc.getColValue());
						String wareId=warehouseDictService.getWarehouseCodeByName(dc.getColValue());
						if(StringUtils.isNotBlank(wareId)){
							entity.setWarehouseCode(wareId);
						}else{
							errorMessage+="仓库不存在;";
						}
					}else{
						errorMessage+="发货仓库不能为空;";
					}
					
					break;
				case "运单日期":
					if (StringUtils.isNotBlank(dc.getColValue())) {
						entity.setCreateTime(DateUtil.transStringToTimeStamp(dc.getColValue()));
						entity.setCreateMonth(DateUtil.transStringToInteger(dc.getColValue()));
					}else{
						errorMessage+="运单日期不能为空;";
					}
					break;
				case "运单号":
					if (StringUtils.isNotBlank(dc.getColValue())) {
						entity.setWaybillNo(dc.getColValue());
					}else{
						errorMessage+="运单号不能为空;";
					}
					break;
				case "赔付商品金额":
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
		
		//重复性校验
		if(StringUtils.isNotBlank(entity.getWaybillNo())){
			if(repeatMap.containsKey(entity.getWaybillNo())){
				errorMessage += "与第"
						+ repeatMap.get(entity.getWaybillNo()) + "行运单号重复;";
			}else{
				repeatMap.put(entity.getWaybillNo(), dr.getRowNo());
			}
		}
		
		if(StringUtils.isNotBlank(errorMessage)){
			throw new BizException(errorMessage);
		}
		
		if(StringUtils.isNotBlank(entity.getWaybillNo())){
			//宅配理赔
			entity.setBillNo(billNo);
			entity.setCustomerName(customerName);
			entity.setCustomerid(customerId);
			entity.setSubjectCode("de_abnormal_pay");
			list.add(entity);
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
			billFeesReceiveDispatchTempService.insertBatch(list);
		}
	}

	@Override
	public String validate(List<String> columns) throws Exception {
		// TODO Auto-generated method stub
		String result="";
		String[] str = {"发货仓库", "运单日期", "运单号"}; //必填列
		
		for (String s : str) {
			if(!columns.contains(s)){
				result+=s+"必须存在;";
			}
		} 
		
		if(StringUtils.isNotBlank(result)){
			result="Excel表头:"+result;
			return result;
		}
		
		return "SUCC";
	}
	
}
