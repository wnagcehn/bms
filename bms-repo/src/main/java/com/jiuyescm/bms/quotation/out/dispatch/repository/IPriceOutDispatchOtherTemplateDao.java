package com.jiuyescm.bms.quotation.out.dispatch.repository;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.quotation.out.dispatch.entity.PriceOutDispatchOtherTemplateEntity;

/**
 * 
 * @author wubangjun
 *
 */
public interface IPriceOutDispatchOtherTemplateDao {
	
    PageInfo<PriceOutDispatchOtherTemplateEntity> query(Map<String, Object> condition, int pageNo,int pageSize);
    
    PriceOutDispatchOtherTemplateEntity query(Map<String, Object> condition);
    
    List<PriceOutDispatchOtherTemplateEntity> queryDeliverTemplate(Map<String, Object> condition);

    PriceOutDispatchOtherTemplateEntity findById(Long id);

    PriceOutDispatchOtherTemplateEntity save(PriceOutDispatchOtherTemplateEntity entity);

    PriceOutDispatchOtherTemplateEntity update(PriceOutDispatchOtherTemplateEntity entity);

    int delete(PriceOutDispatchOtherTemplateEntity entity);

}
