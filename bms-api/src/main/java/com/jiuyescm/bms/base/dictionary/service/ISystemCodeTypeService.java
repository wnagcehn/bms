package com.jiuyescm.bms.base.dictionary.service;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.base.dictionary.entity.SystemCodeTypeEntity;

/**
 * 
 * @author cjw
 * 
 */
public interface ISystemCodeTypeService {

    PageInfo<SystemCodeTypeEntity> query(Map<String, Object> condition, int pageNo,
            int pageSize);

    SystemCodeTypeEntity findById(Long id);

    SystemCodeTypeEntity save(SystemCodeTypeEntity entity) throws Exception;

    SystemCodeTypeEntity update(SystemCodeTypeEntity entity) throws Exception;
    
    int updateByParam(Map<String, Object> condition) throws Exception;

    void delete(Long id);

    /**
     * 类型编码查询参数类型信息
     * @param typeCode
     * @return
     */
    SystemCodeTypeEntity findByTypeCode(String typeCode);

    /**
     * 根据参数查询参数类型信息
     * @param condition
     * @return
     */
    List<SystemCodeTypeEntity> query(Map<String, Object> condition);
}
