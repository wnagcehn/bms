package com.jiuyescm.bms.general.service;

import java.util.List;
import java.util.Map;

import com.jiuyescm.bms.base.dictionary.entity.SystemCodeEntity;

/**
 * 
 * @author cjw
 * 
 */
public interface ISystemCodeService {

	public List<SystemCodeEntity> querySysCodes(Map<String, Object> condition);
	
	public Map<String, SystemCodeEntity> querySysCodesMap(String typeCode);
}
