package com.jiuyescm.bms.quotation.storage.service;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.quotation.storage.entity.PriceExtraQuotationEntity;

public interface IPriceExtraQuotationService {
	
    PageInfo<PriceExtraQuotationEntity> query(Map<String, Object> condition, int pageNo, int pageSize);
	
	Integer save(PriceExtraQuotationEntity entity);
	
	Integer update(PriceExtraQuotationEntity entity);
	
	Integer delete(PriceExtraQuotationEntity entity);
	
	Integer insertBatchTmp(List<PriceExtraQuotationEntity>  list);

	void removeAll(Map<String, Object> map);

	void remove(Map<String, Object> map);
	
	List<PriceExtraQuotationEntity> queryPrice(Map<String,Object> param);
	
	List<PriceExtraQuotationEntity> queryPriceByParam(Map<String,Object> param);

}
