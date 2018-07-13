package com.jiuyescm.bms.quotation.storage.repository;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.quotation.storage.entity.PriceExtraQuotationEntity;


public interface IPriceExtraQuotationRepository {
	
	PageInfo<PriceExtraQuotationEntity> query(Map<String, Object> condition, int pageNo, int pageSize);
	
    int insert(PriceExtraQuotationEntity record);

    int update(PriceExtraQuotationEntity record); 
    
    int  insertBatchTmp(List<PriceExtraQuotationEntity> list);

	void removeAll(Map<String, Object> map);
	
	List<PriceExtraQuotationEntity> queryPrice(Map<String,Object> param);
	
	List<PriceExtraQuotationEntity> queryPriceByParam(Map<String,Object> param);

}