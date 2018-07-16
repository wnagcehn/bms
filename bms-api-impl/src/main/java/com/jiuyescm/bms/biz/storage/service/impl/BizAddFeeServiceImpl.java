/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.biz.storage.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.biz.storage.entity.BizAddFeeEntity;
import com.jiuyescm.bms.biz.storage.repository.IBizAddFeeRepository;
import com.jiuyescm.bms.biz.storage.service.IBizAddFeeService;

/**
 * 
 * @author stevenl
 * 
 */
@Service("bizAddFeeService")
public class BizAddFeeServiceImpl implements IBizAddFeeService {

	private static final Logger logger = Logger.getLogger(BizAddFeeServiceImpl.class.getName());
	
	@Autowired
    private IBizAddFeeRepository bizAddFeeRepository;

    @Override
    public PageInfo<BizAddFeeEntity> query(Map<String, Object> condition,
            int pageNo, int pageSize) {
        return bizAddFeeRepository.query(condition, pageNo, pageSize);
    }

    @Override
    public BizAddFeeEntity findById(Long id) {
        return bizAddFeeRepository.findById(id);
    }

    @Override
    public BizAddFeeEntity save(BizAddFeeEntity entity) {
        return bizAddFeeRepository.save(entity);
    }

    @Override
    public BizAddFeeEntity update(BizAddFeeEntity entity) {
        return bizAddFeeRepository.update(entity);
    }

    @Override
    public void delete(Long id) {
        bizAddFeeRepository.delete(id);
    }

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = { Exception.class })
	public int saveorUpdateList(List<BizAddFeeEntity> addList,List<BizAddFeeEntity> updateList) {
		int addNum = 0;
		int updateNum = 0;
		
		List<BizAddFeeEntity> allList=new ArrayList<BizAddFeeEntity>();
		allList.addAll(addList);
		allList.addAll(updateList);
		
		List<BizAddFeeEntity>  uList = new ArrayList<BizAddFeeEntity>();
		List<BizAddFeeEntity>  aList = new ArrayList<BizAddFeeEntity>();
		Map<String, Object> param = new HashMap<String,Object>();
		
		for(BizAddFeeEntity entity:allList){
			param.put("operationTime",entity.getOperationTime());
			param.put("warehouseCode",entity.getWarehouseCode());
			param.put("customerid",entity.getCustomerid());
			param.put("feesType",entity.getFeesType());
			param.put("externalNo", entity.getExternalNo());
			BizAddFeeEntity temp = bizAddFeeRepository.selectOne(param);
		    if(temp==null){
		    	aList.add(entity);
		    }else{
		    	entity.setIsCalculated("99");
		    	entity.setId(temp.getId());
		    	uList.add(entity);
		    }
		}
		
		//addList.addAll(aList);
		if(aList.size()>0){
			addNum = bizAddFeeRepository.saveList(aList);
		}
		if(uList.size()>0){
			updateNum = bizAddFeeRepository.updateList(uList);
		}
		
		return  addNum+updateNum;
	}

	@Override
	public void updateFee(List<BizAddFeeEntity> updateList) {
		 bizAddFeeRepository.updateList(updateList);
	}

	@Override
	public List<BizAddFeeEntity> queryList(Map<String, Object> condition) {
		return bizAddFeeRepository.queryList(condition);
	}
	
}