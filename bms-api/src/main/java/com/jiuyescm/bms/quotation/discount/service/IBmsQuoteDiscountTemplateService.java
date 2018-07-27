package com.jiuyescm.bms.quotation.discount.service;

import java.util.Map;
import java.util.List;
import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.quotation.discount.entity.BmsQuoteDiscountTemplateEntity;

/**
 * ..Service
 * @author wangchen
 * 
 */
public interface IBmsQuoteDiscountTemplateService {

    BmsQuoteDiscountTemplateEntity findById(Long id);
	
    PageInfo<BmsQuoteDiscountTemplateEntity> query(Map<String, Object> condition, int pageNo,
            int pageSize);

	List<BmsQuoteDiscountTemplateEntity> query(Map<String, Object> condition);

	BmsQuoteDiscountTemplateEntity queryOne(Map<String, Object> condition);
	
    BmsQuoteDiscountTemplateEntity save(BmsQuoteDiscountTemplateEntity entity);

    BmsQuoteDiscountTemplateEntity update(BmsQuoteDiscountTemplateEntity entity);

	void delete(BmsQuoteDiscountTemplateEntity entity);

}
