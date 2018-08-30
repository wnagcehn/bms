package com.jiuyescm.bms.general.service.impl;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.jiuyescm.bms.general.service.IStorageQuoteFilterService;
import com.jiuyescm.bms.quotation.storage.entity.PriceStepQuotationEntity;

@Service("storageQuoteFilterServiceImpl")
public class StorageQuoteFilterServiceImpl implements IStorageQuoteFilterService{

	private static final Logger logger = LoggerFactory.getLogger(StorageQuoteFilterServiceImpl.class.getName());
	
	@Override
	public PriceStepQuotationEntity quoteFilter(List<PriceStepQuotationEntity> quotes,Map<String,Object> map) {
		
		try{
			Integer level = 33;
			PriceStepQuotationEntity entity = new PriceStepQuotationEntity();
			
			String warehouse_code = map.get("warehouse_code")==null?"":map.get("warehouse_code").toString();
			String temperature_code =map.get("temperature_code")==null?"":map.get("temperature_code").toString();
			
			for (PriceStepQuotationEntity quoteEntity : quotes) {
				
				String warehouse_quote = StringUtils.isEmpty(quoteEntity.getWarehouseCode())?"":quoteEntity.getWarehouseCode();
				String temperature_quote = StringUtils.isEmpty(quoteEntity.getTemperatureTypeCode())?"":quoteEntity.getTemperatureTypeCode();
				
				if(!warehouse_code.equals(warehouse_quote) && StringUtils.isNotEmpty(warehouse_quote)){
					continue;//仓库不匹配
				}
				
				if(!temperature_code.equals(temperature_quote) && StringUtils.isNotEmpty(temperature_quote)){
					continue;//温度不匹配
				}
			
				Integer warehouselevel = warehouse_code.equals(warehouse_quote)?1:2;		//仓库优先级	
				Integer temperaturelevel = temperature_code.equals(temperature_quote)?1:2; //温度优先级
				
				Integer temLevel = Integer.valueOf(temperaturelevel.toString()+warehouselevel.toString());
				if(temLevel<level){
					level = temLevel;
					entity = quoteEntity;
				}
			}
			if(level == 33){
				return null;
			}
			else{
				return entity;
			}
		}
		catch(Exception ex){
			logger.error("仓储阶梯报价过滤异常", ex);
			return null;
		}
		
	}
}
