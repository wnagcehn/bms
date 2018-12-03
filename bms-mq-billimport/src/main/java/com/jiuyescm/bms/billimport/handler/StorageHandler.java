package com.jiuyescm.bms.billimport.handler;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
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
import com.jiuyescm.constants.BmsEnums;
import com.jiuyescm.exception.BizException;

/**
 * 仓储
 * @author zhaofeng
 *
 */
@Component("仓储")
public class StorageHandler extends CommonHandler<BillFeesReceiveStorageTempEntity> {

	@Autowired IBillFeesReceiveStorageTempService billFeesReceiveStorageTempService;
	@Autowired IWarehouseDictService warehouseDictService;
	
	@Override
	public List<BillFeesReceiveStorageTempEntity> transRowToObj(DataRow dr)
			throws Exception {
		
		//异常信息 
		String errorMessage="";
		List<BillFeesReceiveStorageTempEntity> lists = new ArrayList<BillFeesReceiveStorageTempEntity>();
		
		DataColumn createTime=dr.getColumn("日期");
		if(createTime!=null &&StringUtils.isBlank(createTime.getColValue())){
			return lists;
		}
		
		BillFeesReceiveStorageTempEntity entity = new BillFeesReceiveStorageTempEntity();
		//商品按托存储--LD
		BillFeesReceiveStorageTempEntity entity1 = null;
		//商品按托存储--LC
		BillFeesReceiveStorageTempEntity entity2 = null;
		//商品按托存储--CW
		BillFeesReceiveStorageTempEntity entity3 = null;
		//商品按托存储--HW
		BillFeesReceiveStorageTempEntity entity4 = null;
		//耗材按托存储--CW
		BillFeesReceiveStorageTempEntity entity5 = null;
		//耗材按托存储--LD
		BillFeesReceiveStorageTempEntity entity6 = null;
		//商品按件存储
		BillFeesReceiveStorageTempEntity entity7 = null;
		//处置费--入库
		BillFeesReceiveStorageTempEntity entity8 = null;
		//出库
		BillFeesReceiveStorageTempEntity entity9 = null;
		
		String warehouseCode = warehouseDictService.getWarehouseCodeByName(sheetName);
		if (StringUtils.isBlank(warehouseCode)) {
			errorMessage+="仓库不存在;";
		}
		
		for (DataColumn dc : dr.getColumns()) {
			try {
				switch (dc.getColName()) {
				case "日期":
					if (StringUtils.isNotBlank(dc.getColValue())) {
						entity.setCreateTime(DateUtil.transStringToTimeStamp(dc.getColValue()));
						entity.setCreateMonth(DateUtil.transStringToInteger(dc.getColValue()));
					}		
					break;
				case "冷冻":
					if (StringUtils.isNotBlank(dc.getColValue())) {
						entity1 = new BillFeesReceiveStorageTempEntity();
						entity1.setWarehouseName(sheetName);
						//如果没找到，报错
						if(StringUtils.isNotBlank(warehouseCode)){
							entity1.setWarehouseCode(warehouseCode);
						}	
						entity1.setSubjectCode("wh_product_storage");
						entity1.setChargeUnit("PALLETS");
						entity1.setCreateTime(entity.getCreateTime());
						entity1.setCreateMonth(entity.getCreateMonth());
						entity1.setTempretureType(BmsEnums.tempretureType.getCode(dc.getColName()));
						entity1.setTotalQty(Integer.valueOf(dc.getColValue()));
						entity1.setBillNo(billNo);
						entity1.setCustomerId(customerId);
						entity1.setCustomerName(customerName);
					}
					break;
				case "冷冻费小计/元":
					if (StringUtils.isNotBlank(dc.getColValue()) && null != entity1) {
						entity1.setAmount(new BigDecimal(dc.getColValue()).setScale(2, BigDecimal.ROUND_HALF_UP));
					}
					break;
				case "冷藏":
					if (StringUtils.isNotBlank(dc.getColValue())) {
						entity2 = new BillFeesReceiveStorageTempEntity();
						entity2.setWarehouseName(sheetName);
						//如果没找到，报错
						if(StringUtils.isNotBlank(warehouseCode)){
							entity2.setWarehouseCode(warehouseCode);
						}	
						entity2.setSubjectCode("wh_product_storage");
						entity2.setChargeUnit("PALLETS");
						entity2.setCreateTime(entity.getCreateTime());
						entity2.setCreateMonth(entity.getCreateMonth());
						entity2.setTempretureType(BmsEnums.tempretureType.getCode(dc.getColName()));
						entity2.setTotalQty(Integer.valueOf(dc.getColValue()));
						entity2.setBillNo(billNo);
						entity2.setCustomerId(customerId);
						entity2.setCustomerName(customerName);
					}
					break;
				case "冷藏费小计/元":
					if (StringUtils.isNotBlank(dc.getColValue()) && null != entity2) {
						entity2.setAmount(new BigDecimal(dc.getColValue()).setScale(2, BigDecimal.ROUND_HALF_UP));
					}
					break;
				case "恒温":
					if (StringUtils.isNotBlank(dc.getColValue())) {
						entity3 = new BillFeesReceiveStorageTempEntity();
						entity3.setWarehouseName(sheetName);
						//如果没找到，报错
						if(StringUtils.isNotBlank(warehouseCode)){
							entity3.setWarehouseCode(warehouseCode);
						}	
						entity3.setSubjectCode("wh_product_storage");
						entity3.setChargeUnit("PALLETS");
						entity3.setCreateTime(entity.getCreateTime());
						entity3.setCreateMonth(entity.getCreateMonth());
						entity3.setTempretureType(BmsEnums.tempretureType.getCode(dc.getColName()));
						entity3.setTotalQty(Integer.valueOf(dc.getColValue()));
						entity3.setBillNo(billNo);
						entity3.setCustomerId(customerId);
						entity3.setCustomerName(customerName);
					}
					break;
				case "恒温费小计/元":
					if (StringUtils.isNotBlank(dc.getColValue()) && null != entity3) {
						entity3.setAmount(new BigDecimal(dc.getColValue()).setScale(2, BigDecimal.ROUND_HALF_UP));
					}
					break;
				case "常温":
					if (StringUtils.isNotBlank(dc.getColValue())) {
						entity4 = new BillFeesReceiveStorageTempEntity();
						entity4.setWarehouseName(sheetName);
						//如果没找到，报错
						if(StringUtils.isNotBlank(warehouseCode)){
							entity4.setWarehouseCode(warehouseCode);
						}
						entity4.setSubjectCode("wh_product_storage");
						entity4.setChargeUnit("PALLETS");
						entity4.setCreateTime(entity.getCreateTime());
						entity4.setCreateMonth(entity.getCreateMonth());
						entity4.setTempretureType(BmsEnums.tempretureType.getCode(dc.getColName()));
						entity4.setTotalQty(Integer.valueOf(dc.getColValue()));
						entity4.setBillNo(billNo);
						entity4.setCustomerId(customerId);
						entity4.setCustomerName(customerName);
					}
					break;
				case "常温费小计/元":
					if (StringUtils.isNotBlank(dc.getColValue()) && null != entity4) {
						entity4.setAmount(new BigDecimal(dc.getColValue()).setScale(2, BigDecimal.ROUND_HALF_UP));
					}
					break;
				case "常温包材":
					if (StringUtils.isNotBlank(dc.getColValue())) {
						entity5 = new BillFeesReceiveStorageTempEntity();
						entity5.setWarehouseName(sheetName);
						//如果没找到，报错
						if(StringUtils.isNotBlank(warehouseCode)){
							entity5.setWarehouseCode(warehouseCode);
						}	
						entity5.setSubjectCode("wh_material_storage");
						entity5.setChargeUnit("PALLETS");
						entity5.setCreateTime(entity.getCreateTime());
						entity5.setCreateMonth(entity.getCreateMonth());
						entity5.setTempretureType("CW");
						entity5.setTotalQty(Integer.valueOf(dc.getColValue()));
						entity5.setBillNo(billNo);
						entity5.setCustomerId(customerId);
						entity5.setCustomerName(customerName);
					}
					break;
				case "常温包材费小计/元":
					if (StringUtils.isNotBlank(dc.getColValue()) && null != entity5) {
						entity5.setAmount(new BigDecimal(dc.getColValue()).setScale(2, BigDecimal.ROUND_HALF_UP));
					}
					break;
				case "冷冻包材":
					if (StringUtils.isNotBlank(dc.getColValue())) {
						entity6 = new BillFeesReceiveStorageTempEntity();
						entity6.setWarehouseName(sheetName);
						//如果没找到，报错
						if(StringUtils.isNotBlank(warehouseCode)){
							entity6.setWarehouseCode(warehouseCode);
						}	
						entity6.setSubjectCode("wh_material_storage");
						entity6.setChargeUnit("PALLETS");
						entity6.setCreateTime(entity.getCreateTime());
						entity6.setCreateMonth(entity.getCreateMonth());
						entity6.setTempretureType("LD");
						entity6.setTotalQty(Integer.valueOf(dc.getColValue()));
						entity6.setBillNo(billNo);
						entity6.setCustomerId(customerId);
						entity6.setCustomerName(customerName);

					}
					break;
				case "冷冻包材费小计/元":
					if (StringUtils.isNotBlank(dc.getColValue()) && null != entity6) {
						entity6.setAmount(new BigDecimal(dc.getColValue()).setScale(2, BigDecimal.ROUND_HALF_UP));
					}
					break;
				case "库存件数":
					if (StringUtils.isNotBlank(dc.getColValue())) {
						entity7 = new BillFeesReceiveStorageTempEntity();
						entity7.setWarehouseName(sheetName);
						//如果没找到，报错
						if(StringUtils.isNotBlank(warehouseCode)){
							entity7.setWarehouseCode(warehouseCode);
						}	
						entity7.setSubjectCode("wh_product_storage");
						entity7.setChargeUnit("ITEMS");
						entity7.setCreateTime(entity.getCreateTime());
						entity7.setCreateMonth(entity.getCreateMonth());
						entity7.setTotalQty(Integer.valueOf(dc.getColValue()));
						entity7.setBillNo(billNo);
						entity7.setCustomerId(customerId);
						entity7.setCustomerName(customerName);
						
					}
					break;
				case "存储费按件小计/元":
					if (StringUtils.isNotBlank(dc.getColValue()) && null != entity7) {
						entity7.setAmount(new BigDecimal(dc.getColValue()).setScale(2, BigDecimal.ROUND_HALF_UP));
					}
					break;
				case "入库板数":
					if (StringUtils.isNotBlank(dc.getColValue())) {
						entity8 = new BillFeesReceiveStorageTempEntity();
						entity8.setWarehouseName(sheetName);
						//如果没找到，报错
						if(StringUtils.isNotBlank(warehouseCode)){
							entity8.setWarehouseCode(warehouseCode);
						}	
						entity8.setSubjectCode("wh_disposal");
						//entity8.setChargeUnit("ITEMS");
						entity8.setCreateTime(entity.getCreateTime());
						entity8.setCreateMonth(entity.getCreateMonth());
						entity8.setTotalQty(Integer.valueOf(dc.getColValue()));
						entity8.setBillNo(billNo);
						entity8.setCustomerId(customerId);
						entity8.setCustomerName(customerName);

					}
					break;
				case "处置费小计/元":
					if (StringUtils.isNotBlank(dc.getColValue()) && null != entity8) {
						entity8.setAmount(new BigDecimal(dc.getColValue()).setScale(2, BigDecimal.ROUND_HALF_UP));
					}
					break;
				case "出库板数":
					if (StringUtils.isNotBlank(dc.getColValue())) {
						entity9 = new BillFeesReceiveStorageTempEntity();
						entity9.setWarehouseName(sheetName);
						//如果没找到，报错
						if(StringUtils.isNotBlank(warehouseCode)){
							entity9.setWarehouseCode(warehouseCode);
						}	
						entity9.setSubjectCode("outstock_pallet_vm");
						entity9.setCreateTime(entity.getCreateTime());
						entity9.setCreateMonth(entity.getCreateMonth());
						entity9.setTotalQty(Integer.valueOf(dc.getColValue()));
						entity9.setAmount(BigDecimal.ZERO);
						entity9.setBillNo(billNo);
						entity9.setCustomerId(customerId);
						entity9.setCustomerName(customerName);
					}
					break;
				default:
					break;
				}
			
			} catch (Exception e) {
				System.out.println("行【"+dr.getRowNo()+"】，列【"+dc.getColName()+"】格式不正确");
				errorMessage+="列【"+ dc.getColName() + "】格式不正确;";
			}
		}
		
		//商品按托+耗材按托+商品按件+入库+出库
		if (null != entity1) {
			lists.add(entity1);
		}
		if (null != entity2) {
			lists.add(entity2);
		}
		if (null != entity3) {
			lists.add(entity3);
		}
		if (null != entity4) {
			lists.add(entity4);
		}
		if (null != entity5) {
			lists.add(entity5);
		}
		if (null != entity6) {
			lists.add(entity6);
		}
		if (null != entity7) {
			lists.add(entity7);
		}
		if (null != entity8) {
			lists.add(entity8);
		}
		if (null != entity9) {
			lists.add(entity9);
		}
		
		DateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		
		//重复性校验
		if(null != entity.getCreateTime()){
			String time = sdf.format(entity.getCreateTime());
			if(repeatMap.containsKey(time)){
				errorMessage += "与第"
						+ repeatMap.get(entity.getCreateTime()) + "行日期重复;";
			}else{
				repeatMap.put(time, dr.getRowNo());
			}
		}
		
		if(StringUtils.isNotBlank(errorMessage)){
			throw new BizException(errorMessage);
		}
		
		return lists;
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
