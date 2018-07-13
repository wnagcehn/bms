package com.jiuyescm.bms.quotation.transport.service;

import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.quotation.transport.entity.GenericTemplateEntity;

/**
 * 报价模板操作API
 * @author wubangjun
 *
 */
public interface IGenericTemplateService {

    PageInfo<GenericTemplateEntity> query(Map<String, Object> condition, int pageNo,int pageSize);
    
    GenericTemplateEntity query(Map<String, Object> condition);

    GenericTemplateEntity findById(Long id);

    GenericTemplateEntity save(GenericTemplateEntity entity);

    GenericTemplateEntity update(GenericTemplateEntity entity);

    void delete(Long id);

}
