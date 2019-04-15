package com.jiuyescm.bms.billimport.handler;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jiuyescm.bms.billimport.entity.BillFeesReceiveDispatchTempEntity;
import com.jiuyescm.bms.billimport.entity.BillFeesReceiveStorageTempEntity;
import com.jiuyescm.bms.billimport.service.IBillFeesReceiveDispatchTempService;
import com.jiuyescm.bms.billimport.service.IBillFeesReceiveStorageTempService;
import com.jiuyescm.bms.excel.data.DataColumn;
import com.jiuyescm.bms.excel.data.DataRow;
import com.jiuyescm.common.utils.DateUtil;
import com.jiuyescm.constants.BmsEnums;
import com.jiuyescm.exception.BizException;

/**
 * 宅配
 * @author zhaofeng
 *
 */
@Component("宅配")
public class DispatchHandler extends CommonHandler<BillFeesReceiveDispatchTempEntity> {

	private static final Logger logger = LoggerFactory.getLogger(DispatchHandler.class);
	
	@Autowired
	private IBillFeesReceiveStorageTempService billFeesReceiveStorageTempService;
	@Autowired
	private IBillFeesReceiveDispatchTempService billFeesReceiveDispatchTempService;
		
	public List<BillFeesReceiveStorageTempEntity> storageList=new ArrayList<BillFeesReceiveStorageTempEntity>();
	
	@Override
	public List<BillFeesReceiveDispatchTempEntity> transRowToObj(DataRow dr) throws Exception {
		//异常信息
		String errorMessage="";
		// TODO Auto-generated method stub
		//配送费
		List<BillFeesReceiveDispatchTempEntity> dispatchList = new ArrayList<BillFeesReceiveDispatchTempEntity>();
		
		boolean isWaybillNull = false;
		boolean isCustomerNull = false;
		
		BillFeesReceiveDispatchTempEntity dispatchEntity = new BillFeesReceiveDispatchTempEntity();
		//仓储费
		BillFeesReceiveStorageTempEntity storageEntity = new BillFeesReceiveStorageTempEntity();
		//操作费
		BillFeesReceiveStorageTempEntity storageEntity1 = new BillFeesReceiveStorageTempEntity();
		//包材费
		BillFeesReceiveStorageTempEntity storageEntity2 = new BillFeesReceiveStorageTempEntity();
		
		for (DataColumn dc : dr.getColumns()) {
			try {
				switch (dc.getTitleName()) {
				case "商家名称":
					if (StringUtils.isBlank(dc.getColValue())) {
						isCustomerNull = true;
					}
					break;
				case "仓库":
					if(StringUtils.isNotBlank(dc.getColValue())){
						dispatchEntity.setWarehouseName(dc.getColValue());
						storageEntity.setWarehouseName(dc.getColValue());
						String wareId = warehouseMap.get(dc.getColValue()).toString();
						//String wareId=warehouseDictService.getWarehouseCodeByName(dc.getColValue());
						if(StringUtils.isNotBlank(wareId)){
							dispatchEntity.setWarehouseCode(wareId);
							storageEntity.setWarehouseCode(wareId);
						}else{
							errorMessage+="仓库不存在;";
						}
					}else{
						errorMessage+="仓库不能为空;";
					}				
					break;
				case "九曳订单号":
					dispatchEntity.setOutstockNo(dc.getColValue());
					storageEntity.setOrderNo(dc.getColValue());			
					break;
				case "商家订单号":
					if(StringUtils.isNotBlank(dc.getColValue())){
						dispatchEntity.setExternalNo(dc.getColValue());
					}else{
						errorMessage+="商家订单号不能为空;";
					}
					break;
				case "运单号":
					if(StringUtils.isNotBlank(dc.getColValue())){
						dispatchEntity.setWaybillNo(dc.getColValue());
						storageEntity.setWaybillNo(dc.getColValue());
					}else{
						isWaybillNull = true;
						errorMessage+="运单号不能为空;";
					}	
					break;
				case "运单生成时间":
					if (StringUtils.isNotBlank(dc.getColValue())) {
						dispatchEntity.setCreateTime(DateUtil.transStringToTimeStamp(dc.getColValue()));
						storageEntity.setCreateTime(DateUtil.transStringToTimeStamp(dc.getColValue()));
						dispatchEntity.setCreateMonth(DateUtil.transStringToInteger(dc.getColValue()));
						storageEntity.setCreateMonth(DateUtil.transStringToInteger(dc.getColValue()));
					}else{
						errorMessage+="运单生成时间不能为空;";
					}
					break;
				case "商品数量":
					if (StringUtils.isNotBlank(dc.getColValue())) {
						dispatchEntity.setTotalQty(new BigDecimal(dc.getColValue()));
						storageEntity.setTotalQty(Integer.parseInt(dc.getColValue()));
					}
					break;
				case "计费物流商":
					if (StringUtils.isNotBlank(dc.getColValue())) {
						dispatchEntity.setCarrierName(dc.getColValue());
						//String carrierCode=carrierDictService.getCarrierCodeByName(dc.getColValue());
						String carrierCode = carrierMap.get(dc.getColValue());
						if(StringUtils.isNotBlank(carrierCode)){
							dispatchEntity.setCarrierid(carrierCode);
						}else{
							errorMessage+="计费物流商不存在;";
						}	
					}else{
						errorMessage+="计费物流商不能为空;";
					}								
					break;
				case "收件人省":
					dispatchEntity.setReceiveProvince(dc.getColValue());
					break;
				case "收件人市":
					dispatchEntity.setReceiveCity(dc.getColValue());
					break;
				case "收件人区县":
					dispatchEntity.setReceiveDistrict(dc.getColValue());
					break;
				case "温度":
					if(StringUtils.isNotBlank(dc.getColValue())){
						dispatchEntity.setTemperatureType(BmsEnums.tempretureType.getCode(dc.getColValue()));
						storageEntity.setTempretureType(BmsEnums.tempretureType.getCode(dc.getColValue()));
					}
					break;
				case "计费重量":
					if (StringUtils.isNotBlank(dc.getColValue())) {
						dispatchEntity.setTotalWeight(new BigDecimal(dc.getColValue()));
						storageEntity.setTotalWeight(new BigDecimal(dc.getColValue()));
					}else{
						errorMessage+="计费重量不能为空;";
					}
					break;
				default:
					break;
				}
			} catch (Exception ex) {
				errorMessage+="列【"+ dc.getColName() + "】格式不正确;";
				
			}
		}
		
		if(isWaybillNull && isCustomerNull){
			return dispatchList;
		}
		
		for(DataColumn dc : dr.getColumns()){
			try {
				switch (dc.getTitleName()) {
				case "运费":
					dispatchEntity.setSubjectCode("de_delivery_amount");
					dispatchEntity.setBillNo(billNo);
					dispatchEntity.setCustomerName(customerName);
					dispatchEntity.setCustomerid(customerId);
					if (StringUtils.isNotBlank(dc.getColValue())) {
						dispatchEntity.setAmount(new BigDecimal(dc.getColValue()).setScale(2, BigDecimal.ROUND_HALF_UP));
					}else{
						dispatchEntity.setAmount(new BigDecimal(0));
					}
					dispatchList.add(dispatchEntity);
					break;
				case "折扣后运费":
					if (StringUtils.isNotBlank(dc.getColValue()) && dispatchEntity.getAmount()!=null) {
						BigDecimal derateAmount=new BigDecimal(dc.getColValue());
						dispatchEntity.setDerateAmount(dispatchEntity.getAmount().subtract(derateAmount));	
					}
					break;
				case "操作费":
					PropertyUtils.copyProperties(storageEntity1, storageEntity);
					if (StringUtils.isNotBlank(dc.getColValue())) {
						storageEntity1.setAmount(new BigDecimal(dc.getColValue()).setScale(2, BigDecimal.ROUND_HALF_UP));				
					}else{
						storageEntity1.setAmount(new BigDecimal(0));
					}
					storageEntity1.setBillNo(billNo);
					storageEntity1.setCustomerId(customerId);
					storageEntity1.setCustomerName(customerName);
					storageEntity1.setSubjectCode("wh_b2c_work");
					storageList.add(storageEntity1);
					break;
				case "包材费":
					PropertyUtils.copyProperties(storageEntity2, storageEntity);
					if (StringUtils.isNotBlank(dc.getColValue())) {
						storageEntity2.setAmount(new BigDecimal(dc.getColValue()).setScale(2, BigDecimal.ROUND_HALF_UP));
					}else{
						storageEntity2.setAmount(new BigDecimal(0));
					}
					storageEntity2.setBillNo(billNo);
					storageEntity2.setCustomerName(customerName);
					storageEntity2.setCustomerId(customerId);
					storageEntity2.setSubjectCode("wh_package");
					storageList.add(storageEntity2);
					break;
				default:
					break;
				}
			} catch (Exception ex) {
				errorMessage+="列【"+ dc.getColName() + "】格式不正确;";
				
			}
		}
		
		//重复性校验
		if(StringUtils.isNotBlank(dispatchEntity.getWaybillNo())){
			if(repeatMap.containsKey(dispatchEntity.getWaybillNo())){
				errorMessage += "数据重复--第【"+repeatMap.get(dispatchEntity.getWaybillNo())+"】行已存在运单【"+dispatchEntity.getWaybillNo()+";";
			}else{
				repeatMap.put(dispatchEntity.getWaybillNo(), dr.getRowNo());
			}
		}
		
		if(StringUtils.isNotBlank(errorMessage)){
			throw new BizException(errorMessage);
		}
		
		return dispatchList;
	}

	@Override
	public void transErr(DataRow dr) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int save() {
		long start = System.currentTimeMillis();// 系统开始时间
		// TODO Auto-generated method stub
		int result=0;
		if(null != list && list.size()>0){
			result=billFeesReceiveDispatchTempService.insertBatch(list);
			logger.info(billNo+"保存宅配到宅配临时表耗时"+(System.currentTimeMillis()-start));
		}
		if(null != storageList && storageList.size()>0){
			billFeesReceiveStorageTempService.insertBatchTemp(storageList);		
			storageList.clear();		
			logger.info(billNo+"保存操作费和包材费到仓储临时表耗时"+(System.currentTimeMillis()-start));
		}
		return result;
	}

	@Override
	public String validate(List<String> columns) throws Exception {
		// TODO Auto-generated method stub
		String result="";
		String[] str = {"仓库", "商家名称", "商家订单号","运单号","运单生成时间","计费物流商","计费重量"}; //必填列
		
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
