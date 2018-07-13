package com.jiuyescm.bms.general.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.jiuyescm.bms.base.dictionary.entity.SystemCodeEntity;
import com.jiuyescm.cfm.persistence.mybatis.MyBatisDao;
import com.jiuyescm.bms.general.service.ISystemCodeService;
import com.xxl.job.core.log.XxlJobLogger;

/**
 * 
 * @author cjw
 * 
 */
@Repository("systemCodeService")
public class SystemCodeServiceImpl extends MyBatisDao implements ISystemCodeService {


	public SystemCodeServiceImpl() {
		super();
	}

	@Override
	public List<SystemCodeEntity> querySysCodes(Map<String, Object> condition) {
		try{
			List<SystemCodeEntity> list = selectList("com.jiuyescm.bms.general.SystemCodeMapper.query", condition);
			return list;
		}
		catch(Exception ex){
			XxlJobLogger.log("查询承运商异常" + ex.getMessage());
			return null;
		}
	}

	@Override
	public Map<String, SystemCodeEntity> querySysCodesMap(String typeCode) {
		
		Map<String, SystemCodeEntity> map = new HashMap<String, SystemCodeEntity>();
		try{
			List<SystemCodeEntity> list = selectList("com.jiuyescm.bms.general.SystemCodeMapper.query", typeCode);
			if(list == null || list.size() == 0){
				return map;
			}
			for (SystemCodeEntity entity : list) {
				map.put(entity.getCode(), entity);
			}
			return map;
		}
		catch(Exception ex){
			XxlJobLogger.log("查询不计算按托存储费商家异常" + ex.getMessage());
			return map;
		}
	}
	
}
