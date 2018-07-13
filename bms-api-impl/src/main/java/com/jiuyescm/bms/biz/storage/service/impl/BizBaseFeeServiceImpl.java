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

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.biz.storage.entity.BizBaseFeeEntity;
import com.jiuyescm.bms.biz.storage.repository.IBizBaseFeeRepository;
import com.jiuyescm.bms.biz.storage.service.IBizBaseFeeService;
import com.jiuyescm.bms.fees.storage.repository.IFeesReceiveStorageRepository;

/**
 * 
 * @author stevenl
 * 
 */
@Service("bizBaseFeeService")
public class BizBaseFeeServiceImpl implements IBizBaseFeeService {

	private static final Logger logger = Logger.getLogger(BizBaseFeeServiceImpl.class.getName());
	
	@Autowired
    private IBizBaseFeeRepository bizBaseFeeRepository;
	
	@Autowired
    private IFeesReceiveStorageRepository feesReceiveStorageRepository;

    @Override
    public PageInfo<BizBaseFeeEntity> query(Map<String, Object> condition,
            int pageNo, int pageSize) {
        return bizBaseFeeRepository.query(condition, pageNo, pageSize);
    }

    @Override
    public BizBaseFeeEntity findById(Long id) {
        return bizBaseFeeRepository.findById(id);
    }

    @Override
    public BizBaseFeeEntity save(BizBaseFeeEntity entity) {
        return bizBaseFeeRepository.save(entity);
    }

    @Override
    public BizBaseFeeEntity update(BizBaseFeeEntity entity) {
        return bizBaseFeeRepository.update(entity);
    }

    @Override
    public void delete(Long id) {
        bizBaseFeeRepository.delete(id);
    }

	@Override
	public int saveorUpdateList(List<BizBaseFeeEntity> addList,List<BizBaseFeeEntity> updateList) {
		int add = 0;
		int update = 0;
		
		
		List<BizBaseFeeEntity> allList=new ArrayList<BizBaseFeeEntity>();
		allList.addAll(addList);
		allList.addAll(updateList);
		
		List<BizBaseFeeEntity>  uList = new ArrayList<BizBaseFeeEntity>();
		List<BizBaseFeeEntity>  aList = new ArrayList<BizBaseFeeEntity>();
		Map<String, Object> param = new HashMap<String,Object>();
		
		for(BizBaseFeeEntity entity:allList)
		{
			param.put("operationTime",entity.getOperationTime());
			param.put("warehouseCode",entity.getWarehouseCode());
			param.put("customerid",entity.getCustomerid());
			param.put("item",entity.getItem());
			BizBaseFeeEntity temp = bizBaseFeeRepository.selectOne(param);
		    if(temp==null){
		    	aList.add(entity);
		    }else{
		    	entity.setIsCalculated("99");
		    	entity.setId(temp.getId());
		    	uList.add(entity);
		    }
		}
		
		/*addList.addAll(aList);
		add = bizBaseFeeRepository.saveList(addList);
		update = bizBaseFeeRepository.updateList(uList);*/
		
		if(aList.size()>0){
			add = bizBaseFeeRepository.saveList(aList);
		}
		if(uList.size()>0){
			update = bizBaseFeeRepository.updateList(uList);
		}
			
		return  add+update;
	}
	
	@Override
	public void  updateFee(List<BizBaseFeeEntity> updateList){
		
		bizBaseFeeRepository.updateList(updateList);
	}

	@Override
	public List<BizBaseFeeEntity> queryList(Map<String, Object> condition) {
		return bizBaseFeeRepository.queryList(condition);
	}
	
}
