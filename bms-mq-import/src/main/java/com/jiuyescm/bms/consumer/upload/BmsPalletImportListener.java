package com.jiuyescm.bms.consumer.upload;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import com.jiuyescm.bms.base.dictionary.entity.SystemCodeEntity;
import com.jiuyescm.bms.base.dictionary.service.ISystemCodeService;
import com.jiuyescm.bms.biz.storage.entity.BizOutstockPackmaterialTempEntity;
import com.jiuyescm.bms.biz.storage.entity.BizPackStorageEntity;
import com.jiuyescm.bms.biz.storage.entity.BizProductPalletStorageEntity;
import com.jiuyescm.bms.biz.storage.entity.BizProductPalletStorageTempEntity;
import com.jiuyescm.bms.biz.storage.service.IBizPackStorageTempService;
import com.jiuyescm.bms.biz.storage.service.IBizProductPalletStorageTempService;
import com.jiuyescm.bs.util.ExportUtil;
import com.jiuyescm.bs.util.StringUtil;
import com.jiuyescm.cfm.common.JAppContext;
import com.jiuyescm.mdm.customer.api.ICustomerService;
import com.jiuyescm.mdm.customer.vo.CustomerVo;
import com.jiuyescm.mdm.warehouse.api.IWarehouseService;
import com.jiuyescm.mdm.warehouse.vo.WarehouseVo;

public class BmsPalletImportListener extends BmsCommonImportListener {

	@Autowired private IWarehouseService warehouseService;
	@Autowired private ISystemCodeService systemCodeService; //业务类型
	@Autowired private ICustomerService customerService;
	@Autowired private IBizProductPalletStorageTempService bizProductPalletStorageTempService;
	@Autowired private IBizPackStorageTempService bizPackStorageTempService;
	
	@Override
	protected List<String> initColumnNames() {
		String[] str = {"库存日期","仓库","商家全称","商品冷冻","商品冷藏","商品常温","商品恒温","耗材冷冻","耗材冷藏","耗材常温","耗材恒温"};
		return Arrays.asList(str); 
	}

	@Override
	protected String[] initColumnsNamesForNeed() {
		String[] str = {"库存日期","仓库","商家全称"};
		return str;
	}

	@Override
	protected boolean batchHander() {
		Map<String, String> wareHouseMap = new HashMap<String, String>();
		Map<String, String> customerMap = new HashMap<String, String>();
		Map<String, String> temperatureMap = new HashMap<String, String>();
		//仓库
		List<WarehouseVo> wareHouseList = warehouseService.queryAllWarehouse();
		if(wareHouseList != null && wareHouseList.size()>0)
		{
			for(WarehouseVo wareHouse : wareHouseList){
				wareHouseMap.put(wareHouse.getWarehousename().trim(),wareHouse.getWarehouseid());
			}
		}
		//商家
		PageInfo<CustomerVo> tmpPageInfo = customerService.query(null, 0, Integer.MAX_VALUE);
		if (tmpPageInfo != null && tmpPageInfo.getList() != null && tmpPageInfo.getList().size()>0) 
		{
			for(CustomerVo customer : tmpPageInfo.getList()){
				if(customer != null){
					customerMap.put(customer.getCustomername().trim(), customer.getCustomerid());
				}
			}
		}
		//温度类型
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("typeCode", "TEMPERATURE_TYPE");
		List<SystemCodeEntity> temperatureList = systemCodeService.queryCodeList(param);
		if(temperatureList != null && temperatureList.size()>0){
		    for(SystemCodeEntity scEntity : temperatureList){
		    	temperatureMap.put(scEntity.getCodeName().trim(), scEntity.getCode());
		    }
		}
		
		List<BizProductPalletStorageEntity> palletList = new ArrayList<BizProductPalletStorageEntity>();
		List<BizPackStorageEntity> packList = new ArrayList<BizPackStorageEntity>();
		
		for(Entry<Integer, Map<String, String>> row : reader.getContents().entrySet()) { 
			Map<String, String> cells = row.getValue();
			int rowNo = row.getKey(); 	//行号
			String errorMsg="";			//行错误信息
			String warehouseName = "";		//仓库
			String warehouseCode = "";
			String customerName  = "";		//商家
			String customerId  = "";
			Timestamp createTime = null; 	//库存时间
			//****************************************************************** 仓库校验
			warehouseName = cells.get("仓库");//仓库名称
			if(!StringUtil.isEmpty(warehouseName)){
				if(wareHouseMap.containsKey(warehouseName)){
					warehouseCode = wareHouseMap.get(warehouseName);
				}
				else{
					errorMsg += "仓库【"+warehouseName+"】不存在;";
				}
			}
			
			//****************************************************************** 商家校验
			customerName = cells.get("商家");//商家名称
			if(!StringUtil.isEmpty(warehouseName)){
				if(customerMap.containsKey(customerName)){
					customerId = customerMap.get(customerName);
				}
				else{
					errorMsg += "商家【"+customerName+"】不存在;";
				}
			}
			
			//****************************************************************** 日期校验
			String outTimeExcel=cells.get("库存日期");//出库日期
			try {
				createTime = reader.changeValueToTimestamp(outTimeExcel);
				DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String tsStr = sdf.format(createTime);
				reader.getContents().get(rowNo).put("出库日期", tsStr);
			} catch (Exception e) {
				errorMsg += "出库日期【"+outTimeExcel+"】格式不正确;";
			}
			
			//****************************************************************** 校验数量的类型
			String[] strs = {"商品冷冻","商品冷藏","商品常温","商品恒温","耗材冷冻","耗材冷藏","耗材常温","耗材恒温"};
			for (String str : strs) {
				BizProductPalletStorageEntity product_entity = new BizProductPalletStorageEntity();
				BizPackStorageEntity pack_entity = new BizPackStorageEntity();
				product_entity.setWarehouseCode(warehouseCode);
				product_entity.setWarehouseName(warehouseName);
				product_entity.setCustomerId(customerId);
				product_entity.setCustomerName(customerName);
				product_entity.setStockTime(createTime);
				product_entity.setCreateTime(createTime);
				product_entity.setIsCalculated("0");
				product_entity.setWriteTime(JAppContext.currentTimestamp());
				product_entity.setDelFlag("0");
				product_entity.setCreator(taskEntity.getCreator());
				
				pack_entity.setWarehouseCode(warehouseCode);
				pack_entity.setWarehouseName(warehouseName);
				pack_entity.setCustomerid(customerId);
				pack_entity.setCustomerName(customerName);
				pack_entity.setCreateTime(createTime);
				product_entity.setIsCalculated("0");
				product_entity.setWriteTime(JAppContext.currentTimestamp());
				product_entity.setDelFlag("0");
				product_entity.setCreator(taskEntity.getCreator());
				
				boolean isAllEmpty = true;
				
				if(StringUtils.isNotEmpty(cells.get(str)))
				{
					if(ExportUtil.isNumber(cells.get(str)))
					{
						isAllEmpty = false;
						product_entity.setPalletNum(Double.valueOf(cells.get(str)));//托数
						product_entity.setAdjustPalletNum(Double.valueOf(cells.get(str)));
						if(str.equals("商品冷冻")){
							product_entity.setTemperatureTypeName("冷冻");
							product_entity.setTemperatureTypeCode(temperatureMap.get("冷冻"));
							palletList.add(product_entity);
						}
						else if(str.equals("商品冷藏")){
							product_entity.setTemperatureTypeName("冷藏");
							product_entity.setTemperatureTypeCode(temperatureMap.get("冷藏"));
							palletList.add(product_entity);
						}
						else if(str.equals("商品常温")){
							product_entity.setTemperatureTypeName("常温");
							product_entity.setTemperatureTypeCode(temperatureMap.get("常温"));
							palletList.add(product_entity);
						}
						else if(str.equals("商品恒温")){
							product_entity.setTemperatureTypeName("恒温");
							product_entity.setTemperatureTypeCode(temperatureMap.get("恒温"));
							palletList.add(product_entity);
						}
						else if(str.equals("耗材冷冻")){
							pack_entity.setTemperatureTypeName("冷冻");
							pack_entity.setTemperatureTypeCode(temperatureMap.get("冷冻"));
							packList.add(pack_entity);
						}
						else if(str.equals("耗材冷藏")){
							pack_entity.setTemperatureTypeName("冷藏");
							pack_entity.setTemperatureTypeCode(temperatureMap.get("冷藏"));
							packList.add(pack_entity);
						}
						else if(str.equals("耗材常温")){
							pack_entity.setTemperatureTypeName("常温");
							pack_entity.setTemperatureTypeCode(temperatureMap.get("常温"));
							packList.add(pack_entity);
						}
						else if(str.equals("耗材恒温")){
							pack_entity.setTemperatureTypeName("恒温");
							pack_entity.setTemperatureTypeCode(temperatureMap.get("恒温"));
							packList.add(pack_entity);
						}
						
					}else{
						errorMsg+="列【"+str+"】为非数字;";
					}
				}
				if(isAllEmpty){
					errorMsg+="数值列不能都为空;";
				}
			}
			if(!StringUtil.isEmpty(errorMsg)){
				if(errMap.containsKey(rowNo)){
					errMap.put(rowNo, errMap.get(rowNo)+errorMsg);
				}
				else{
					errMap.put(rowNo, errorMsg);
				}
			}
		}
		
		//重复性校验
		
		
		return false;
	}
	
	private boolean dbProductCheck(){
		
		List<BizProductPalletStorageTempEntity> list = bizProductPalletStorageTempService.queryInBiz(taskEntity.getTaskId());
			if(null == list || list.size() <= 0){
				return true;
			}
			Map<String,String> map=Maps.newLinkedHashMap();
			//存在重复记录
			/*for(BizProductPalletStorageTempEntity entity:list){
				String row=String.valueOf(entity.getRowExcelNo());
				String mes="";
				if(map.containsKey(row)){
					mes=map.get(row);
					mes+=","+entity.getRowExcelName()+"【"+entity.getConsumerMaterialCode()+"】";
					map.put(row,mes);
				}else{
					mes="系统已存在库存【" + entity.getWaybillNo() + "】,"+entity.getRowExcelName()+"【"+entity.getConsumerMaterialCode()+"】";
					map.put(row,mes);
				}
			}
			Set<String> set=map.keySet();
			for(String key:set){
				Integer rowNum=Integer.valueOf(key);
				if(errMap.containsKey(rowNum)){
					errMap.put(rowNum, errMap.get(rowNum)+","+map.get(key));
				}else{
					errMap.put(rowNum, map.get(key));
				}
			}*/
			return false;
	}
	
}


