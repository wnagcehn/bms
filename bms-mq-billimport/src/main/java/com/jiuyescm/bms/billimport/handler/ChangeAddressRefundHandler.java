package com.jiuyescm.bms.billimport.handler;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
 * 改地址退件费
 * @author zhaofeng
 *
 */
@Component("改地址退件费")
public class ChangeAddressRefundHandler extends CommonHandler<BillFeesReceiveDispatchTempEntity>{

	@Autowired
	private IBillFeesReceiveDispatchTempService billFeesReceiveDispatchTempService;
	@Autowired
	private IWarehouseDictService warehouseDictService;
	
	private Map<String,Integer> repeatMap=new HashMap<String, Integer>();
	
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
				System.out.println("列名【" + dc.getColName() + "】|值【"+ dc.getColValue() + "】");
				
				switch (dc.getColName()) {
				case "发货仓库":
					entity.setWarehouseName(dc.getColValue());
					String wareId=warehouseDictService.getWarehouseCodeByName(dc.getColValue());
					if(StringUtils.isNotBlank(wareId)){
						entity.setWarehouseCode(wareId);
					}else{
						errorMessage+="仓库不存在;";
					}
					break;
				case "运单日期":
					if (StringUtils.isNotBlank(dc.getColValue())) {
						entity.setCreateTime(DateUtil.transStringToTimeStamp(dc.getColValue()));
					}
					break;
				case "运单号":
					entity.setWaybillNo(dc.getColValue());
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
		
		//重复性校验
		if(StringUtils.isNotBlank(entity.getWaybillNo())){
			if(repeatMap.containsKey(entity.getWaybillNo())){
				errorMessage += "数据重复--第【"+repeatMap.get(entity.getWaybillNo())+"】行已存在运单【"+entity.getWaybillNo()+";";
			}else{
				repeatMap.put(entity.getWaybillNo(), dr.getRowNo());
			}
		}
	
		if(StringUtils.isNotBlank(errorMessage)){
			throw new BizException("行【" + dr.getRowNo()+"】"+ errorMessage);
		}
		
		if(StringUtils.isNotBlank(entity.getWaybillNo())){
			//改地址退件费
			entity.setSubjectCode("de_change");
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
	
}
