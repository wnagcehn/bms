/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.base.unitInfo.service;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.base.unitInfo.entity.BmsChargeUnitInfoEntity;

/**
 * ..Service
 * @author wangchen
 * 
 */
public interface IBmsChargeUnitInfoService {

    BmsChargeUnitInfoEntity findById(Long id);
	
    PageInfo<BmsChargeUnitInfoEntity> query(Map<String, Object> condition, int pageNo,
            int pageSize);

	List<BmsChargeUnitInfoEntity> query(Map<String, Object> condition);

    BmsChargeUnitInfoEntity save(BmsChargeUnitInfoEntity entity);

    BmsChargeUnitInfoEntity update(BmsChargeUnitInfoEntity entity);

	BmsChargeUnitInfoEntity delete(BmsChargeUnitInfoEntity entity);

}
