package com.jiuyescm.bms.quotation.storage.repository;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.quotation.storage.entity.PriceStepQuotationEntity;


public interface IPriceStepQuotationRepository {
	
	PageInfo<PriceStepQuotationEntity> query(Map<String, Object> condition, int pageNo, int pageSize);
	
    int delete(Long id);

    int insert(PriceStepQuotationEntity record);

    int update(PriceStepQuotationEntity record);
    
    int insertBatchTmp(List<PriceStepQuotationEntity> list);
    
    List<PriceStepQuotationEntity> queryPriceStep(Map<String,Object> param);

    
	void removeAll(Map<String, Object> map);
    List<PriceStepQuotationEntity> queryPriceStandardStep(Map<String,Object> param);

	List<PriceStepQuotationEntity> queryPriceStepByQuatationId(Map<String,Object> map);

 
}