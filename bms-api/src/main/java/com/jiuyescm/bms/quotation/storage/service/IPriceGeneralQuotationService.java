package com.jiuyescm.bms.quotation.storage.service;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.quotation.storage.entity.PriceGeneralQuotationEntity;

/**
 * 通用报价service
 * @author cjw
 * 
 */
public interface IPriceGeneralQuotationService {

    PageInfo<PriceGeneralQuotationEntity> query(Map<String, Object> condition, int pageNo,int pageSize);
    
    PriceGeneralQuotationEntity query(Map<String, Object> condition);

    PriceGeneralQuotationEntity findById(Long id);

    PriceGeneralQuotationEntity save(PriceGeneralQuotationEntity entity);

    PriceGeneralQuotationEntity update(PriceGeneralQuotationEntity entity);

    void delete(Long id);
    
    Integer insert(PriceGeneralQuotationEntity entity);
    
    PriceGeneralQuotationEntity findByNo(String quotationNo);
     
    List<PriceGeneralQuotationEntity>  queryPriceGeneral(Map<String, Object> condition);

	void removeAll(Map<String, Object> map);
    
    List<PriceGeneralQuotationEntity>  queryPriceStandardGeneral(Map<String, Object> condition);

	PriceGeneralQuotationEntity queryByQuotationNo(String quotationNo);
}
