package com.jiuyescm.bms.general.service;

import java.util.List;
import java.util.Map;

import com.jiuyescm.bms.quotation.storage.entity.PriceStepQuotationEntity;

public interface IStorageQuoteFilterService {

	PriceStepQuotationEntity quoteFilter(List<PriceStepQuotationEntity> quotes,Map<String,Object> map);
	
}