package com.jiuyescm.bms.quotation.dispatch.repository;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.quotation.dispatch.entity.PriceDispatchTemplateEntity;

/**
 * 
 * @author wubangjun
 *
 */
public interface IPriceDispatchTemplateDao {
    
    PageInfo<PriceDispatchTemplateEntity> query(Map<String, Object> condition, int pageNo,int pageSize);
    
    PriceDispatchTemplateEntity query(Map<String, Object> condition);
    
    List<PriceDispatchTemplateEntity> queryDeliverTemplate(Map<String, Object> condition);

    PriceDispatchTemplateEntity findById(Long id);

    PriceDispatchTemplateEntity save(PriceDispatchTemplateEntity entity);

    PriceDispatchTemplateEntity update(PriceDispatchTemplateEntity entity);

    int delete(PriceDispatchTemplateEntity entity);

}
