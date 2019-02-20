/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.biz.storage.service.impl;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.biz.storage.entity.BizOutstockPackmaterialTempEntity;
import com.jiuyescm.bms.biz.storage.repository.IBizOutstockPackmaterialTempRepository;
import com.jiuyescm.bms.biz.storage.service.IBizOutstockPackmaterialTempService;
import com.jiuyescm.exception.BizException;

/**
 * 
 * @author stevenl
 * 
 */
@Service("bizOutstockPackmaterialTempService")
public class BizOutstockPackmaterialTempServiceImpl implements IBizOutstockPackmaterialTempService {

	private static final Logger logger = Logger.getLogger(BizOutstockPackmaterialTempServiceImpl.class.getName());
	
	@Autowired
    private IBizOutstockPackmaterialTempRepository bizOutstockPackmaterialTempRepository;

    @Override
    public PageInfo<BizOutstockPackmaterialTempEntity> query(Map<String, Object> condition,
            int pageNo, int pageSize) {
        return bizOutstockPackmaterialTempRepository.query(condition, pageNo, pageSize);
    }

    @Override
    public BizOutstockPackmaterialTempEntity findById(Long id) {
        return bizOutstockPackmaterialTempRepository.findById(id);
    }

    @Override
    public BizOutstockPackmaterialTempEntity save(BizOutstockPackmaterialTempEntity entity) {
        return bizOutstockPackmaterialTempRepository.save(entity);
    }

    @Override
    public BizOutstockPackmaterialTempEntity update(BizOutstockPackmaterialTempEntity entity) {
        return bizOutstockPackmaterialTempRepository.update(entity);
    }

    @Override
    public void delete(Long id) {
        bizOutstockPackmaterialTempRepository.delete(id);
    }

	@Override
	public void saveBatch(List<BizOutstockPackmaterialTempEntity> tempList) {
		try {
			bizOutstockPackmaterialTempRepository.saveBatch(tempList);
		} catch (Exception e) {
			throw new BizException(e.getMessage());
		}	
	}

	@Override
	public void deleteBybatchNum(String batchNum) {
		try{
			//吃掉异常  防止导入失败
			bizOutstockPackmaterialTempRepository.deleteBybatchNum(batchNum);
		}catch(Exception e){
			logger.error(e);
		}
	}

	@Override
	public List<BizOutstockPackmaterialTempEntity> querySameData(String batchNum) {

		return bizOutstockPackmaterialTempRepository.querySameData(batchNum);
	}

	@Override
	public List<BizOutstockPackmaterialTempEntity> queryContainsList(
			String batchNum) {
		return bizOutstockPackmaterialTempRepository.queryContainsList(batchNum);
	}

	@Override
	public List<BizOutstockPackmaterialTempEntity> queryContainsList(
			String batchNum, int errorCount) {
		return bizOutstockPackmaterialTempRepository.queryContainsList(batchNum,errorCount);
	}
	
}
