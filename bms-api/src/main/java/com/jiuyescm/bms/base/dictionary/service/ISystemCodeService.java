package com.jiuyescm.bms.base.dictionary.service;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.base.dictionary.entity.SystemCodeEntity;


/**
 * 
 * @author cjw
 * 
 */
public interface ISystemCodeService {

    PageInfo<SystemCodeEntity> query(Map<String, Object> condition, int pageNo,int pageSize);

    SystemCodeEntity findById(Long id);

    SystemCodeEntity save(SystemCodeEntity entity) throws Exception;

    SystemCodeEntity update(SystemCodeEntity entity) throws Exception;

    void delete(Long id);
    
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
	 * @param typeCode 默认值为 GLOBAL_PARAM 系统全局参数
	 * @param code 
	 * @return
	 */
	public SystemCodeEntity getSystemCode(String typeCode,String code);
 
	
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

	/**
	 * 获取excel导入路径
	 * @return
	 */
	public String getImpExcelFilePath();

	int getPartOutboundStatus();

	SystemCodeEntity queryEntityByCode(String code);
	
	/**
	 * 根据新建的时间去排序
	 * @param param
	 * @return
	 */
	List<SystemCodeEntity> queryBycreateDt(Map<String, Object> param);
}
