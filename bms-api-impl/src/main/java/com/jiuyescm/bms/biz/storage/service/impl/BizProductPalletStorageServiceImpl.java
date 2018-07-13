/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.biz.storage.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.biz.storage.entity.BizPackStorageEntity;
import com.jiuyescm.bms.biz.storage.entity.BizProductPalletStorageEntity;
import com.jiuyescm.bms.biz.storage.repository.IBizPackStorageRepository;
import com.jiuyescm.bms.biz.storage.repository.IBizProductPalletStorageRepository;
import com.jiuyescm.bms.biz.storage.service.IBizProductPalletStorageService;
import com.jiuyescm.bms.fees.storage.entity.FeesReceiveStorageEntity;
import com.jiuyescm.bms.fees.storage.repository.IFeesReceiveStorageRepository;
import com.jiuyescm.bms.fees.storage.service.IFeesReceiveStorageService;

/**
 * 商品按托存储库存
 * @author wubangjun
 */
@Service("bizProductPalletStorageService")
public class BizProductPalletStorageServiceImpl implements IBizProductPalletStorageService {

	private static final Logger logger = Logger.getLogger(BizProductPalletStorageServiceImpl.class.getName());
	
	@Autowired
    private IBizProductPalletStorageRepository bizProductPalletStorageRepository;
	
	@Autowired
    private IBizPackStorageRepository bizPackStorageRepository;
	
	@Autowired
    private IFeesReceiveStorageRepository feesReceiveStorageRepository;

    @Override
    public PageInfo<BizProductPalletStorageEntity> query(Map<String, Object> condition,
            int pageNo, int pageSize) {
        return bizProductPalletStorageRepository.query(condition, pageNo, pageSize);
    }

    @Override
    public BizProductPalletStorageEntity findById(Long id) {
        return bizProductPalletStorageRepository.findById(id);
    }

    @Override
    public BizProductPalletStorageEntity save(BizProductPalletStorageEntity entity) {
        return bizProductPalletStorageRepository.save(entity);
    }

    @Override
    public BizProductPalletStorageEntity update(BizProductPalletStorageEntity entity) {
        return bizProductPalletStorageRepository.update(entity);
    }

    @Override
    public void delete(Long id) {
        bizProductPalletStorageRepository.delete(id);
    }

	@Override
	public int saveList(List<BizProductPalletStorageEntity> dataList) {
		return bizProductPalletStorageRepository.saveList(dataList);
	}

	@Override
	public PageInfo<BizProductPalletStorageEntity> queryGroup(
			Map<String, Object> condition, int pageNo, int pageSize) {
		
		return bizProductPalletStorageRepository.queryGroup(condition, pageNo, pageSize);
	}

	@Override
	public Properties validRetry(Map<String, Object> param) {
		return bizProductPalletStorageRepository.validRetry(param);
	}

	@Override
	public int reCalculate(Map<String, Object> param) {
		return bizProductPalletStorageRepository.reCalculate(param);
	}

	@Override
	public List<BizProductPalletStorageEntity> queryList(
			Map<String, Object> condition) {
		return bizProductPalletStorageRepository.queryList(condition);
	}

	@Override
	public int deleteList(List<BizProductPalletStorageEntity> dataList) {
		return bizProductPalletStorageRepository.deleteList(dataList);
		
	}

	@Override
	public int deleteFees(Map<String, Object> condition) {
		return bizProductPalletStorageRepository.deleteFees(condition);
	}

	@Override
	public PageInfo<Map<String,String>> queryByMonth(Map<String, Object> condition, int pageNo, int pageSize) {
		return  bizProductPalletStorageRepository.queryByMonth(condition, pageNo, pageSize);
	}

	@Override
	public PageInfo<Map<String, String>> queryCustomeridF(
			Map<String, Object> condition, int pageNo, int pageSize) {
		
		Object startTime = condition.get("startTime");
		Object endTime = condition.get("endTime");
		
		Calendar stcalendar = Calendar.getInstance();//日历对象
		Calendar encalendar = Calendar.getInstance();//日历对象
			
		Date stTime = (Date)startTime;
		Date enTime = (Date)endTime;
		stcalendar.setTime(stTime);
		encalendar.setTime(enTime);
		
		stcalendar.add(Calendar.MONTH, -1);
		encalendar.add(Calendar.MONTH, -1);
		
		condition.put("bstartTime", stcalendar.getTime());
		condition.put("bendTime", encalendar.getTime());
		
		return bizProductPalletStorageRepository.queryCustomeridF(condition, pageNo, pageSize);
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = { Exception.class })
	public int saveListAll(List<BizProductPalletStorageEntity> palletaddList,List<BizPackStorageEntity> packaddList,List<BizProductPalletStorageEntity> palletupdateList,List<BizPackStorageEntity> packupdateList) {
		int num = 0;
		int pnum = 0;
		int updatePallet = 0;
		int updatePack = 0;
	
		if(palletupdateList.size()>0)
		{
			updatePallet = bizProductPalletStorageRepository.updateBatch(palletupdateList);
		}
		if(packupdateList.size()>0)
		{
			updatePack = bizPackStorageRepository.updateBatch(packupdateList);
		}
		if(palletaddList.size()>0){
			num = bizProductPalletStorageRepository.saveList(palletaddList);
		}
		if(packaddList.size()>0){
			pnum = bizPackStorageRepository.saveList(packaddList);
		}	
		return num+pnum+updatePallet+updatePack;
	}

	@Override
	public BizProductPalletStorageEntity queryOneByParam(BizProductPalletStorageEntity entity) {
		return bizProductPalletStorageRepository.queryOneByParam(entity);
	}

	@Override
	public List<BizProductPalletStorageEntity> queryAllBystockTime(
			Map<String, Object> parameter) {
		return bizProductPalletStorageRepository.queryAllBystockTime(parameter);
	}

	@Override
	public PageInfo<BizProductPalletStorageEntity> queryStorageDiff(
			Map<String, Object> condition, int pageNo, int pageSize) {
		return bizProductPalletStorageRepository.queryStorageDiff(condition, pageNo, pageSize);
	}

	@Override
	public List<BizProductPalletStorageEntity> queryStorageDiff(Map<String, Object> condition) {
		return bizProductPalletStorageRepository.queryStorageDiff(condition);
	}
	
	
}
