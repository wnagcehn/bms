package com.jiuyescm.bms.quotation.storage.service;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.quotation.storage.entity.PriceMaterialQuotationEntity;

public interface IPriceMaterialQuotationService {
	
	PageInfo<PriceMaterialQuotationEntity> query(Map<String, Object> condition, int pageNo, int pageSize);
	
	Integer save(PriceMaterialQuotationEntity entity);
	
	Integer update(PriceMaterialQuotationEntity entity);
	
	Integer delete(PriceMaterialQuotationEntity entity);
	
	Integer insertBatchTmp(List<PriceMaterialQuotationEntity> list);
	
	List<PriceMaterialQuotationEntity> queryStepMaterial(Map<String, Object> condition);
	
	//查询单个报价
	PriceMaterialQuotationEntity queryOneMaterial(Map<String, Object> condition);
	
	//查询标准报价
	List<PriceMaterialQuotationEntity> queryStandardMaterial(Map<String, Object> condition);

	void removeDetail(Map<String, Object> map);

	void removeAll(Map<String, Object> map);

	List<PriceMaterialQuotationEntity> queryById(Map<String, Object> parameter);


}
