package com.jiuyescm.bms.base.dictionary.repository;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.base.dictionary.entity.SystemCodeEntity;

/**
 * 
 * @author cjw
 * 
 */
public interface ISystemCodeRepository {

	public PageInfo<SystemCodeEntity> query(Map<String, Object> condition,
            int pageNo, int pageSize);

    public SystemCodeEntity findById(Long id);

    public SystemCodeEntity save(SystemCodeEntity entity);

    public SystemCodeEntity update(SystemCodeEntity entity);

    public void delete(Long id);

    /**
     * 根据系统参数类型查询参数枚举集合数据
     * 
     * @param typeCode
     * @return
     */
	public List<SystemCodeEntity> findEnumList(String typeCode);
	
	/**
	 * 根据参数类型及参数代码获取系统参数数据
	 * 
	 * @param typeCode
	 * @param code
	 * @return
	 */
	public SystemCodeEntity getSystemCode(String typeCode,String code);
 
	/**
	 * 根据code 查询实体
	 * @param string
	 * @return
	 */
	public SystemCodeEntity queryEntityByCode(String code);
	
	
	/**
	 * 专用接口
	 * 根据温度类型 查询数据
	 * 
	 * @param param
	 * @return
	 */
	public List<SystemCodeEntity>  queryTempType(Map<String, Object> param);
	
	/**
	 * 根据系统参数属性 查询数据
	 * 
	 * @param param
	 * @return
	 */
	public List<SystemCodeEntity>  queryCodeList(Map<String, Object> param);

	public int getPartOutboundStatus();

	SystemCodeEntity getSystemCodeByattr(String extattr1, String extattr3);

	public List<SystemCodeEntity> queryValueAddList(
			Map<String, Object> mapCondition);

}
