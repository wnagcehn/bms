/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.biz.storage.service.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.biz.storage.entity.BizPackStorageEntity;
import com.jiuyescm.bms.biz.storage.repository.IBizPackStorageRepository;
import com.jiuyescm.bms.biz.storage.service.IBizPackStorageService;
import com.jiuyescm.bms.fees.storage.entity.FeesReceiveStorageEntity;
import com.jiuyescm.bms.fees.storage.repository.IFeesReceiveStorageRepository;

/**
 * 
 * @author stevenl
 * 
 */
@Service("bizPackStorageService")
public class BizPackStorageServiceImpl implements IBizPackStorageService {

	private static final Logger logger = Logger.getLogger(BizPackStorageServiceImpl.class.getName());
	
	@Autowired
    private IBizPackStorageRepository bizPackStorageRepository;
	@Autowired
	private IFeesReceiveStorageRepository feesReceiveStorageRepository;

    @Override
    public PageInfo<BizPackStorageEntity> query(Map<String, Object> condition,
            int pageNo, int pageSize) {
        return bizPackStorageRepository.query(condition, pageNo, pageSize);
    }

    @Override
    public BizPackStorageEntity findById(Long id) {
        return bizPackStorageRepository.findById(id);
    }

    @Override
    public BizPackStorageEntity save(BizPackStorageEntity entity) {
        return bizPackStorageRepository.save(entity);
    }

    @Override
    public int update(BizPackStorageEntity entity) {
        return bizPackStorageRepository.update(entity);
    }
    
    /**
     * 删除费用/删除业务数据
     */
    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor=Exception.class)
    public void delete(BizPackStorageEntity entity) {
    	FeesReceiveStorageEntity feeEntity = new FeesReceiveStorageEntity();
    	feeEntity.setFeesNo(entity.getFeesNo());
    	feeEntity.setDelFlag(entity.getDelFlag());
    	try {
    		bizPackStorageRepository.delete(entity);
		} catch (Exception e) {
			logger.error("业务数据删除失败", e);
		}
    	
    	try {
			feesReceiveStorageRepository.update(feeEntity);
		} catch (Exception e) {
			logger.error("费用删除失败", e);
		}
    }   

	@Override
	public int saveList(List<BizPackStorageEntity> list) {
		return bizPackStorageRepository.saveList(list);
	}
	
	@Override
    public int checkIsNotExist(BizPackStorageEntity entity) {
		return bizPackStorageRepository.checkIsNotExist(entity);
	}

	@Override
	public PageInfo<BizPackStorageEntity> queryGroup(
			Map<String, Object> condition, int pageNo, int pageSize) {
		return  bizPackStorageRepository.queryGroup(condition, pageNo, pageSize);
	}

	@Override
	public Properties validRetry(Map<String, Object> param) {
		return bizPackStorageRepository.validRetry(param);
	}

	@Override
	public int reCalculate(Map<String, Object> param) {
		return bizPackStorageRepository.reCalculate(param);
	}

	@Override
	public int deleteBatch(List<BizPackStorageEntity> list) {
		return bizPackStorageRepository.deleteList(list);
	}

	@Override
	public List<BizPackStorageEntity> queryList(Map<String, Object> condition) {
		return bizPackStorageRepository.queryList(condition);
	}

	@Override
	public int deleteFees(Map<String, Object> condition) {
		return bizPackStorageRepository.deleteFees(condition);
	}

	@Override
	public PageInfo<Map<String, String>> queryByMonth(
			Map<String, Object> condition, int pageNo, int pageSize) {
		return  bizPackStorageRepository.queryByMonth(condition, pageNo, pageSize);
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
		
		return bizPackStorageRepository.queryCustomeridF(condition, pageNo, pageSize);
	}
	
	@Override
	public BizPackStorageEntity queryExceptionOne(Map<String, Object> condition) {
		// TODO Auto-generated method stub
		return bizPackStorageRepository.queryExceptionOne(condition);
	}

	@Override
	public List<BizPackStorageEntity> queryAllBycurTime(Map<String, Object> map) {
		return bizPackStorageRepository.queryAllBycurTime(map);
	}
	
	@Override
	public BizPackStorageEntity queryOneByParam(BizPackStorageEntity entity) {
		return bizPackStorageRepository.queryOneByParam(entity);
	}

	@Override
	public int saveTempData(String taskId) {
		// TODO Auto-generated method stub
		return bizPackStorageRepository.saveTempData(taskId);
	}
}
