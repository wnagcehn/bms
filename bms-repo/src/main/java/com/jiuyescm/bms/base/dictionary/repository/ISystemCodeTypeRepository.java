package com.jiuyescm.bms.base.dictionary.repository;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.base.dictionary.entity.SystemCodeTypeEntity;

/**
 * 
 * @author cjw
 * 
 */
public interface ISystemCodeTypeRepository {

	public PageInfo<SystemCodeTypeEntity> query(Map<String, Object> condition,
            int pageNo, int pageSize);

    public SystemCodeTypeEntity findById(Long id);

    public SystemCodeTypeEntity save(SystemCodeTypeEntity entity);

    public SystemCodeTypeEntity update(SystemCodeTypeEntity entity);
    
    int updateByParam(Map<String, Object> condition) throws Exception;

    public void delete(Long id);


    /**
     * 类型编码查询参数类型信息
     * @param typeCode
     * @return
     */
    public SystemCodeTypeEntity findByTypeCode(String typeCode);

    /**
     * 根据参数查询参数类型信息
     * @param condition
     * @return
     */
    public List<SystemCodeTypeEntity> query(Map<String, Object> condition);
}
