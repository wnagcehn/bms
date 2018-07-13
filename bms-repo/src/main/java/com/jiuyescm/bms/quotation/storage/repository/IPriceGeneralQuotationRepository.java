package com.jiuyescm.bms.quotation.storage.repository;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.quotation.storage.entity.PriceGeneralQuotationEntity;

/**
 * 
 * @author cjw
 * 
 */
public interface IPriceGeneralQuotationRepository {

	public PageInfo<PriceGeneralQuotationEntity> query(Map<String, Object> condition,int pageNo, int pageSize);
	
	public List<PriceGeneralQuotationEntity> queryList(Map<String, Object> condition);
	
	public PriceGeneralQuotationEntity query(Map<String, Object> condition);

    public PriceGeneralQuotationEntity findById(Long id);

    public PriceGeneralQuotationEntity save(PriceGeneralQuotationEntity entity);

    public PriceGeneralQuotationEntity update(PriceGeneralQuotationEntity entity);

    public void delete(Long id);
    
    Integer insert(PriceGeneralQuotationEntity entity);
    
    PriceGeneralQuotationEntity  findByNo(String quotationNo);
    
    List<PriceGeneralQuotationEntity>  queryPriceGeneral(Map<String, Object> condition);

    
	public void removeAll(Map<String, Object> map);
    List<PriceGeneralQuotationEntity>  queryPriceStandardGeneral(Map<String, Object> condition);

	public List<PriceGeneralQuotationEntity> queryPriceGeneralByContract(Map<String, Object> map);
	
	public List<PriceGeneralQuotationEntity> queryStandardModel(Map<String, Object> map);

	public PriceGeneralQuotationEntity queryByQuotationNo(String quotationNo);

}
