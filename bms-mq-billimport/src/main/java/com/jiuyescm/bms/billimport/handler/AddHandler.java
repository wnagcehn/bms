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
import com.jiuyescm.bms.subject.service.IBmsSubjectInfoService;
import com.jiuyescm.bms.subject.vo.BmsSubjectInfoVo;
import com.jiuyescm.common.utils.DateUtil;
import com.jiuyescm.exception.BizException;

/**
 * 增值
 * @author wangchen870
 *
 */

@Component("增值")
public class AddHandler extends CommonHandler<BillFeesReceiveStorageTempEntity>{

	@Autowired IBillFeesReceiveStorageTempService billFeesReceiveStorageTempService;
	@Autowired IWarehouseDictService warehouseDictService;
	@Autowired IBmsSubjectInfoService bmsSubjectInfoService;
	
	@Override
	public List<BillFeesReceiveStorageTempEntity> transRowToObj(DataRow dr) throws Exception {
		//异常信息
		String errorMessage="";
		List<BillFeesReceiveStorageTempEntity> list = new ArrayList<BillFeesReceiveStorageTempEntity>();
		
		/*DataColumn addCo=dr.getColumn("增值编号");
		DataColumn customerCo=dr.getColumn("客户名称");
		if(addCo!=null && customerCo!=null &&StringUtils.isBlank(addCo.getColValue()+customerCo.getColValue())){
			return list;
		}*/
		
		BillFeesReceiveStorageTempEntity entity = new BillFeesReceiveStorageTempEntity();
		for (DataColumn dc:dr.getColumns()) {
			try {
				switch (dc.getColName()) {
				case "增值编号":
					if (StringUtils.isNotBlank(dc.getColValue())) {
						entity.setOrderNo(dc.getColValue());
					}else {
						errorMessage+="增值编号必填";
					}
					break;
				case "日期":
					if (StringUtils.isNotBlank(dc.getColValue())) {
						entity.setCreateTime(DateUtil.transStringToTimeStamp(dc.getColValue()));
						entity.setCreateMonth(DateUtil.transStringToInteger(dc.getColValue()));
					}else {
						errorMessage+="日期必填";
					}	
					break;
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
						errorMessage+="仓库名称必填";
					}		
					break;
				//转Code
				case "增值项目":
					if (StringUtils.isNotBlank(dc.getColValue())) {
						BmsSubjectInfoVo bmsSubjectInfoVo = bmsSubjectInfoService.querySubjectByName("INPUT", "STORAGE", dc.getColValue());
						if (null == bmsSubjectInfoVo) {
							entity.setSubjectCode("wh_other");
						}else {
							entity.setSubjectCode(bmsSubjectInfoVo.getSubjectCode());
						}
					}else {
						errorMessage+="增值项目必填";
					}
					break;
				case "数量":
					if (StringUtils.isNotBlank(dc.getColValue())) {
						entity.setTotalQty(Integer.parseInt(dc.getColValue()));
					}else {
						errorMessage+="数量必填";
					}
					break;
				case "单位":
					entity.setChargeUnit(dc.getColValue());			
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
		
		//增值费
		if (StringUtils.isNotBlank(entity.getOrderNo())) {
			entity.setBillNo(billNo);
			entity.setCustomerName(customerName);
			entity.setCustomerId(customerId);
			//entity.setSubjectCode("wh_value_add_subject");
			list.add(entity);
		}
		
		//重复性校验
		if(StringUtils.isNotBlank(entity.getOrderNo())){
			if(repeatMap.containsKey(entity.getOrderNo())){
				errorMessage += "与第"+ repeatMap.get(entity.getOrderNo()) + "行单据编号重复;";
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
	public int save() {
		int result=billFeesReceiveStorageTempService.insertBatchTemp(list);
		return result;
	}

	@Override
	public String validate(List<String> columns) throws Exception {
		// TODO Auto-generated method stub
		String result="";
		String[] str = {"增值编号", "日期", "仓库名称","增值项目","数量"}; //必填列
		
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
