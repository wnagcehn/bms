/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.systemCode.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jiuyescm.bms.base.dictionary.entity.SystemCodeEntity;
import com.jiuyescm.bms.base.dictionary.repository.ISystemCodeRepository;
import com.jiuyescm.bms.systemCode.service.api.ISystemCodeService;
import com.jiuyescm.bms.systemCode.service.vo.SystemCodeVo;

@Service("sysValueAddService")
public class SystemCodeServiceImpl implements ISystemCodeService {
	
	private static final Logger logger = Logger.getLogger(SystemCodeServiceImpl.class.getName());
	
	@Autowired
    private ISystemCodeRepository systemCodeRepository;

	@Override
	public List<SystemCodeVo> queryValueAddList(Map<String, Object> mapCondition) {
		List<SystemCodeVo> voList=null;
		try{
			voList=new ArrayList<SystemCodeVo>();
			List<SystemCodeEntity> list=systemCodeRepository.queryValueAddList(mapCondition);
			for(SystemCodeEntity entity:list){
				SystemCodeVo vo=new SystemCodeVo();
				PropertyUtils.copyProperties(vo, entity);
				voList.add(vo);
			}

		}catch(Exception e){
			logger.error("queryValueAddList: ",e);
		}
		return voList;
	}

	
}
