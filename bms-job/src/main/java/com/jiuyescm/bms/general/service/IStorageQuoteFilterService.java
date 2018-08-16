package com.jiuyescm.bms.general.service;

import java.util.List;

import com.jiuyescm.bms.quotation.storage.entity.PriceStepQuotationEntity;

public interface IStorageQuoteFilterService {

	PriceStepQuotationEntity quoteFilter(List<PriceStepQuotationEntity> quotes,PriceStepQuotationEntity quote);
	
}
