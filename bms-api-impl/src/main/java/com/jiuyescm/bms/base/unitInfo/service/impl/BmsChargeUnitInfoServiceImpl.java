/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.base.unitInfo.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.base.unitInfo.entity.BmsChargeUnitInfoEntity;
import com.jiuyescm.bms.base.unitInfo.repository.IBmsChargeUnitInfoRepository;
import com.jiuyescm.bms.base.unitInfo.service.IBmsChargeUnitInfoService;

/**
 * ..ServiceImpl
 * @author wangchen
 * 
 */
@Service("bmsChargeUnitInfoService")
public class BmsChargeUnitInfoServiceImpl implements IBmsChargeUnitInfoService {

	@Autowired
    private IBmsChargeUnitInfoRepository bmsChargeUnitInfoRepository;

	/**
	 * 根据id查询
	 * @param id
	 * @return
	 * @throws Exception
	*/
	@Override
    public BmsChargeUnitInfoEntity findById(Long id) {
        return bmsChargeUnitInfoRepository.findById(id);
    }
	
	/**
	 * 分页查询
	 * @param page
	 * @param param
	 */
    @Override
    public PageInfo<BmsChargeUnitInfoEntity> query(Map<String, Object> condition,
            int pageNo, int pageSize) {
        return bmsChargeUnitInfoRepository.query(condition, pageNo, pageSize);
    }
    
     /**
	 * 查询
	 * @param page
	 * @param param
	 */
	@Override
    public List<BmsChargeUnitInfoEntity> query(Map<String, Object> condition){
		return bmsChargeUnitInfoRepository.query(condition);
	}
	
	/**
	 * 保存
	 * @param entity
	 * @return
	 */
    @Override
    public BmsChargeUnitInfoEntity save(BmsChargeUnitInfoEntity entity) {
        return bmsChargeUnitInfoRepository.save(entity);
    }

	/**
	 * 更新
	 * @param entity
	 * @return
	 */
    @Override
    public BmsChargeUnitInfoEntity update(BmsChargeUnitInfoEntity entity) {
        return bmsChargeUnitInfoRepository.update(entity);
    }

	/**
	 * 删除
	 * @param entity
	 */
    @Override
    public BmsChargeUnitInfoEntity delete(BmsChargeUnitInfoEntity entity) {
    	return bmsChargeUnitInfoRepository.delete(entity);
    }
	
}
