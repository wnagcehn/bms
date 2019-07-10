/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.biz.transport.service.impl;

import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.biz.transport.entity.BizGanxianWayBillEntity;
import com.jiuyescm.bms.biz.transport.repository.IBizGanxianWayBillRepository;
import com.jiuyescm.bms.biz.transport.service.IBizGanxianWayBillService;

/**
 * 
 * @author wubangjun
 * 
 */
@Service("bizGanxianWayBillService")
public class BizGanxianWayBillServiceImpl implements IBizGanxianWayBillService {
	
	@Autowired
    private IBizGanxianWayBillRepository bizGanxianWayBillRepository;

    @Override
    public PageInfo<BizGanxianWayBillEntity> query(Map<String, Object> condition,
            int pageNo, int pageSize) {
        return bizGanxianWayBillRepository.query(condition, pageNo, pageSize);
    }
    
    @Override
    public BizGanxianWayBillEntity query(Map<String, Object> condition) {
    	return bizGanxianWayBillRepository.query(condition);
    }

    @Override
    public BizGanxianWayBillEntity findById(Long id) {
        return bizGanxianWayBillRepository.findById(id);
    }

    @Override
    public BizGanxianWayBillEntity save(BizGanxianWayBillEntity entity) {
        return bizGanxianWayBillRepository.save(entity);
    }

    @Override
    public BizGanxianWayBillEntity update(BizGanxianWayBillEntity entity) {
        return bizGanxianWayBillRepository.update(entity);
    }

	@Override
	public void updateList(List<BizGanxianWayBillEntity> updateList) {
		bizGanxianWayBillRepository.updateList(updateList);
	}

	@Override
	public int saveList(List<BizGanxianWayBillEntity> list) {
		return bizGanxianWayBillRepository.saveList(list);
	}

	@Override
	public Properties validRetry(Map<String, Object> param) {
		return bizGanxianWayBillRepository.validRetry(param);
	}

	@Override
	public int reCalculate(Map<String, Object> param) {
		return bizGanxianWayBillRepository.reCalculate(param);
	}

	@Override
	public PageInfo<BizGanxianWayBillEntity> queryGroup(
			Map<String, Object> condition, int pageNo, int pageSize) {
		return bizGanxianWayBillRepository.queryGroup(condition, pageNo, pageSize);
	}

	@Override
	public List<BizGanxianWayBillEntity> queryDelete(
			Map<String, Object> condition) {
		return  bizGanxianWayBillRepository.queryDelete(condition);
	}

	@Override
	public int deleteBatch(List<BizGanxianWayBillEntity> list) {
		return  bizGanxianWayBillRepository.deleteBatch(list);
	}

	@Override
	public int deleteFees(Map<String, Object> condition) {
		return bizGanxianWayBillRepository.deleteFees(condition);
	}

	/**
	 * 同时删除业务数据和费用
	 */
	@Override
	public int deleteBizAndFees(Map<String, Object> condition) {
		return bizGanxianWayBillRepository.deleteBizAndFees(condition);
	}

	@Override
	public BizGanxianWayBillEntity queryExceptionOne(
			Map<String, Object> condition) {
		// TODO Auto-generated method stub
		return bizGanxianWayBillRepository.queryExceptionOne(condition);
	}
	
}
