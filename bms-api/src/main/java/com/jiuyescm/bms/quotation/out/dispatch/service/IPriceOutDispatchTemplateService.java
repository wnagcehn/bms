package com.jiuyescm.bms.quotation.out.dispatch.service;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.quotation.out.dispatch.entity.PriceOutDispacthTemplateEntity;

/**
 * 报价模板操作API
 * @author wubangjun
 *
 */
public interface IPriceOutDispatchTemplateService {

    PageInfo<PriceOutDispacthTemplateEntity> query(Map<String, Object> condition, int pageNo,int pageSize);
    
    PriceOutDispacthTemplateEntity query(Map<String, Object> condition);
    
    List<PriceOutDispacthTemplateEntity> queryDeliverTemplate(Map<String, Object> condition);

    PriceOutDispacthTemplateEntity findById(Long id);

    PriceOutDispacthTemplateEntity save(PriceOutDispacthTemplateEntity entity);

    PriceOutDispacthTemplateEntity update(PriceOutDispacthTemplateEntity entity);

    int delete(PriceOutDispacthTemplateEntity entity);

}
