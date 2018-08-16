package com.jiuyescm.bms.general.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.jiuyescm.bms.general.service.IStorageQuoteFilterService;
import com.jiuyescm.bms.quotation.storage.entity.PriceStepQuotationEntity;
import com.jiuyescm.bs.util.StringUtil;

@Service("storageQuoteFilterServiceImpl")
public class StorageQuoteFilterServiceImpl implements IStorageQuoteFilterService{

	@Override
	public PriceStepQuotationEntity quoteFilter(List<PriceStepQuotationEntity> quotes,PriceStepQuotationEntity quote) {
		
		Integer level = 33;
		PriceStepQuotationEntity entity = new PriceStepQuotationEntity();
		
		String temperature_code = StringUtil.isEmpty(quote.getTemperatureTypeCode())?"":quote.getTemperatureTypeCode();
		String warehouse_code = StringUtil.isEmpty(quote.getWarehouseCode())?"":quote.getWarehouseCode();
		
		for (PriceStepQuotationEntity quoteEntity : quotes) {
			String temperature_quote = StringUtil.isEmpty(quoteEntity.getTemperatureTypeCode())?"":quoteEntity.getTemperatureTypeCode();
			String warehouse_quote = StringUtil.isEmpty(quote.getWarehouseCode())?"":quote.getWarehouseCode();
			
			if(!temperature_code.equals(temperature_quote) && StringUtil.isEmpty(temperature_quote)){
				continue;//温度不匹配
			}
			if(!warehouse_code.equals(warehouse_quote) && StringUtil.isEmpty(warehouse_quote)){
				continue;//仓库不匹配
			}
			Integer temperaturelevel = temperature_code.equals(temperature_quote)?1:2; //温度优先级
			Integer warehouselevel = warehouse_code.equals(warehouse_quote)?1:2;		//仓库优先级
			
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
}
