package com.jiuyescm.bms.quotation.transport.repository;

import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.quotation.transport.entity.GenericTemplateEntity;

/**
 * 
 * @author wubangjun
 *
 */
public interface IGenericTemplateRepository {
    
    PageInfo<GenericTemplateEntity> query(Map<String, Object> condition, int pageNo,int pageSize);
    
    GenericTemplateEntity query(Map<String, Object> condition);

    GenericTemplateEntity findById(Long id);

    GenericTemplateEntity save(GenericTemplateEntity entity);

    GenericTemplateEntity update(GenericTemplateEntity entity);

    void delete(Long id);

	void remove(Map<String, Object> map);

}
