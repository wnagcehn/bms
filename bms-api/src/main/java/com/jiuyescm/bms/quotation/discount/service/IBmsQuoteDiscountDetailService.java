package com.jiuyescm.bms.quotation.discount.service;

import java.util.Map;
import java.util.List;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.quotation.discount.entity.BmsQuoteDiscountDetailEntity;

/**
 * ..Service
 * @author wangchen
 * 
 */
public interface IBmsQuoteDiscountDetailService {

    BmsQuoteDiscountDetailEntity findById(Long id);
	
    PageInfo<BmsQuoteDiscountDetailEntity> query(Map<String, Object> condition, int pageNo,
            int pageSize);

	List<BmsQuoteDiscountDetailEntity> query(Map<String, Object> condition);

    BmsQuoteDiscountDetailEntity save(BmsQuoteDiscountDetailEntity entity);

    BmsQuoteDiscountDetailEntity update(BmsQuoteDiscountDetailEntity entity);

	void delete(BmsQuoteDiscountDetailEntity entity);

	List<BmsQuoteDiscountDetailEntity> queryByTemplateCode(String code);
	/**
	 * 批量导入
	 * @param list
	 * @return
	 * @throws Exception
	 */
	int insertBatchTmp(List<BmsQuoteDiscountDetailEntity> list) throws Exception;

	String queryServiceTypeName(Map<String, Object> condition);

	String queryServiceTypeCode(Map<String, Object> condition);

}
