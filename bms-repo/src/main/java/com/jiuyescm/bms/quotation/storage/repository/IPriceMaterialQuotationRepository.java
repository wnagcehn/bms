package com.jiuyescm.bms.quotation.storage.repository;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.quotation.storage.entity.PriceMaterialQuotationEntity;

public interface IPriceMaterialQuotationRepository {
	
	PageInfo<PriceMaterialQuotationEntity> query(Map<String, Object> condition, int pageNo, int pageSize);
	
    int insert(PriceMaterialQuotationEntity record);

    int update(PriceMaterialQuotationEntity record);
    
    int insertBatchTmp(List<PriceMaterialQuotationEntity> list);
    
    List<PriceMaterialQuotationEntity> queryStepMaterial(Map<String, Object> condition);
    
    PriceMaterialQuotationEntity queryOneMaterial(Map<String, Object> condition);
    
    List<PriceMaterialQuotationEntity> queryStandardMaterial(Map<String, Object> condition);
    
    int delete(Long id);

	void removeDetail(Map<String, Object> map);

	List<PriceMaterialQuotationEntity> queryAllById(
			Map<String, Object> parameter);

	List<PriceMaterialQuotationEntity> queryMaterialQuatationByContract(
			Map<String, Object> map);

	List<PriceMaterialQuotationEntity> queryByTemplateId(Map<String, Object> parameter);
}