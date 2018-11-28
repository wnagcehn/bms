package com.jiuyescm.bms.billimport.handler;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jiuyescm.bms.base.dict.api.ICarrierDictService;
import com.jiuyescm.bms.base.dict.api.ICustomerDictService;
import com.jiuyescm.bms.base.dict.api.IWarehouseDictService;
import com.jiuyescm.bms.billimport.entity.BillFeesReceiveDispatchTempEntity;
import com.jiuyescm.bms.billimport.entity.BillFeesReceiveStorageTempEntity;
import com.jiuyescm.bms.billimport.service.IBillFeesReceiveDispatchTempService;
import com.jiuyescm.bms.billimport.service.IBillFeesReceiveStorageTempService;
import com.jiuyescm.bms.excel.data.DataColumn;
import com.jiuyescm.bms.excel.data.DataRow;
import com.jiuyescm.common.utils.DateUtil;
import com.jiuyescm.exception.BizException;

/**
 * 宅配
 * @author zhaofeng
 *
 */
@Component("宅配")
public class DispatchHandler extends CommonHandler<BillFeesReceiveDispatchTempEntity> {

	@Autowired
	private IBillFeesReceiveStorageTempService billFeesReceiveStorageTempService;
	@Autowired
	private IBillFeesReceiveDispatchTempService billFeesReceiveDispatchTempService;
	@Autowired
	private IWarehouseDictService warehouseDictService;
	@Autowired
	private ICustomerDictService customerDictService;
	@Autowired
	private ICarrierDictService carrierDictService;
	
		
	public List<BillFeesReceiveStorageTempEntity> storageList=new ArrayList<BillFeesReceiveStorageTempEntity>();
	
	@Override
	public List<BillFeesReceiveDispatchTempEntity> transRowToObj(DataRow dr)
			throws Exception {
		//异常信息
		String errorMessage="";
		
		// TODO Auto-generated method stub
		//配送费
		List<BillFeesReceiveDispatchTempEntity> dispatchList = new ArrayList<BillFeesReceiveDispatchTempEntity>();
		BillFeesReceiveDispatchTempEntity dispatchEntity = new BillFeesReceiveDispatchTempEntity();
		//仓储费
		BillFeesReceiveStorageTempEntity storageEntity = new BillFeesReceiveStorageTempEntity();
		//操作费
		BillFeesReceiveStorageTempEntity storageEntity1 = new BillFeesReceiveStorageTempEntity();
		//包材费
		BillFeesReceiveStorageTempEntity storageEntity2 = new BillFeesReceiveStorageTempEntity();
		
		for (DataColumn dc : dr.getColumns()) {
			try {
				System.out.println("列名【" + dc.getColName() + "】|值【"+ dc.getColValue() + "】");

				switch (dc.getColName()) {
				case "仓库":
					dispatchEntity.setWarehouseName(dc.getColValue());
					storageEntity.setWarehouseName(dc.getColValue());
					String wareId=warehouseDictService.getWarehouseCodeByName(dc.getColValue());
					if(StringUtils.isNotBlank(wareId)){
						dispatchEntity.setWarehouseCode(wareId);
						storageEntity.setWarehouseCode(wareId);
					}else{
						errorMessage+="仓库不存在;";
					}
					break;
				case "九曳订单号":
					dispatchEntity.setOutstockNo(dc.getColValue());
					storageEntity.setOrderNo(dc.getColValue());
					break;
				case "商家订单号":
					dispatchEntity.setExternalNo(dc.getColValue());
					break;
				case "运单号":
					dispatchEntity.setWaybillNo(dc.getColValue());
					storageEntity.setWaybillNo(dc.getColValue());
					break;
				case "运单生成时间":
					if (StringUtils.isNotBlank(dc.getColValue())) {
						dispatchEntity.setCreateTime(DateUtil.transStringToTimeStamp(dc.getColValue()));
						storageEntity.setCreateTime(DateUtil.transStringToTimeStamp(dc.getColValue()));
					}
					break;
				case "商品数量":
					if (StringUtils.isNotBlank(dc.getColValue())) {
						dispatchEntity.setTotalQty(new BigDecimal(dc.getColValue()));
						storageEntity.setTotalQty(Integer.parseInt(dc.getColValue()));
					}
					break;
				case "计费物流商":
					dispatchEntity.setCarrierName(dc.getColValue());
					String carrierCode=carrierDictService.getCarrierCodeByName(dc.getColValue());
					if(StringUtils.isNotBlank(carrierCode)){
						dispatchEntity.setCarrierid(carrierCode);
					}else{
						errorMessage+="计费物流商不存在;";
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
					dispatchEntity.setTemperatureType(dc.getColValue());
					storageEntity.setTempretureType(dc.getColValue());
					break;
				case "计费重量":
					if (StringUtils.isNotBlank(dc.getColValue())) {
						dispatchEntity.setTotalWeight(new BigDecimal(dc.getColValue()));
						storageEntity.setTotalWeight(new BigDecimal(dc.getColValue()));
					}
					break;
				default:
					break;
				}

			} catch (Exception ex) {
				errorMessage+="列【"+ dc.getColName() + "】格式不正确;";
				
			}
		}
		
		for(DataColumn dc : dr.getColumns()){
			try {
				switch (dc.getColName()) {
				case "运费":
					if (StringUtils.isNotBlank(dc.getColValue()) && StringUtils.isNotBlank(dispatchEntity.getWaybillNo())) {
						dispatchEntity.setAmount(new BigDecimal(dc.getColValue()).setScale(2, BigDecimal.ROUND_HALF_UP));
						dispatchEntity.setSubjectCode("de_delivery_amount");
						dispatchList.add(dispatchEntity);
					}
					break;
				case "折扣后运费":
					if (StringUtils.isNotBlank(dc.getColValue()) && dispatchEntity.getAmount()!=null) {
						BigDecimal derateAmount=new BigDecimal(dc.getColValue());
						dispatchEntity.setDerateAmount(dispatchEntity.getAmount().subtract(derateAmount));	
					}	
					break;
				case "操作费":
					if (StringUtils.isNotBlank(dc.getColValue()) && StringUtils.isNotBlank(dispatchEntity.getWaybillNo())) {
						PropertyUtils.copyProperties(storageEntity1, storageEntity);
						storageEntity1.setAmount(new BigDecimal(dc.getColValue()).setScale(2, BigDecimal.ROUND_HALF_UP));
						storageEntity1.setSubjectCode("wh_b2c_work");
						storageList.add(storageEntity1);
					}	
					break;
				case "包材费":
					if (StringUtils.isNotBlank(dc.getColValue()) && StringUtils.isNotBlank(dispatchEntity.getWaybillNo())) {
						PropertyUtils.copyProperties(storageEntity2, storageEntity);
						storageEntity2.setAmount(new BigDecimal(dc.getColValue()).setScale(2, BigDecimal.ROUND_HALF_UP));
						storageEntity2.setSubjectCode("wh_package");
						storageList.add(storageEntity2);
					}			
					break;
				default:
					break;
				}
			} catch (Exception ex) {
				errorMessage+="列【"+ dc.getColName() + "】格式不正确;";
				
			}
		}
		
		if(StringUtils.isNotBlank(errorMessage)){
			throw new BizException("行【" + dr.getRowNo()+"】"+ errorMessage);
		}
		
		return dispatchList;
	}

	@Override
	public void transErr(DataRow dr) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void save() {
		// TODO Auto-generated method stub		
		if(null != list && list.size()>0){
			billFeesReceiveDispatchTempService.insertBatch(list);
		}
		if(null != storageList && storageList.size()>0){
			int result=billFeesReceiveStorageTempService.insertBatchTemp(storageList);
			if(result>0){
				storageList.clear();
			}
		}
		
	}

	
}
