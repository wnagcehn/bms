package com.jiuyescm.bms.quotation.storage.service;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.quotation.storage.entity.PriceStepQuotationEntity;

public interface IPriceStepQuotationService {
	
	PageInfo<PriceStepQuotationEntity> query(Map<String, Object> condition, int pageNo, int pageSize);
	
	Integer save(PriceStepQuotationEntity entity);
	
	Integer update(PriceStepQuotationEntity entity);
	
	Integer delete(PriceStepQuotationEntity entity);
	
	int insertBatchTmp(List<PriceStepQuotationEntity> list);
	
	List<PriceStepQuotationEntity> queryPriceStep(Map<String, Object> param);
	
	List<PriceStepQuotationEntity> queryPriceStandardStep(Map<String, Object> param);

	void removeAll(Map<String, Object> map);

	void remove(Map<String, Object> map);

}
