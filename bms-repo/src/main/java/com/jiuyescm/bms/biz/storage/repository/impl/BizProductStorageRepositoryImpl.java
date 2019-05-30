/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.biz.storage.repository.impl;

import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Repository;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.cfm.persistence.mybatis.MyBatisDao;
import com.jiuyescm.bms.asyn.entity.BmsAsynCalcuTaskEntity;
import com.jiuyescm.bms.biz.storage.entity.BizProductStorageEntity;
import com.jiuyescm.bms.biz.storage.repository.IBizProductStorageRepository;

/**
 * 
 * @author stevenl
 * 
 */
@SuppressWarnings("rawtypes")
@Repository("bizProductStorageRepository")
public class BizProductStorageRepositoryImpl extends MyBatisDao implements IBizProductStorageRepository {

	private static final Logger logger = Logger.getLogger(BizProductStorageRepositoryImpl.class.getName());

	public BizProductStorageRepositoryImpl() {
		super();
	}
	
	@SuppressWarnings("unchecked")
	@Override
    public PageInfo<BizProductStorageEntity> query(Map<String, Object> condition, int pageNo, int pageSize) {
        List<BizProductStorageEntity> list = selectList("com.jiuyescm.bms.biz.storage.BizProductStorageEntityMapper.query", condition, new RowBounds(
                pageNo, pageSize));
        PageInfo<BizProductStorageEntity> pageInfo = new PageInfo<BizProductStorageEntity>(list);
        return pageInfo;
    }

    @Override
    public BizProductStorageEntity findById(Long id) {
        BizProductStorageEntity entity = (BizProductStorageEntity) selectOne("com.jiuyescm.bms.biz.storage.BizProductStorageEntityMapper.findById", id);
        return entity;
    }

    @SuppressWarnings("unchecked")
	@Override
    public BizProductStorageEntity save(BizProductStorageEntity entity) {
        insert("com.jiuyescm.bms.biz.storage.BizProductStorageEntityMapper.save", entity);
        return entity;
    }

    @SuppressWarnings("unchecked")
	@Override
    public BizProductStorageEntity update(BizProductStorageEntity entity) {
        update("com.jiuyescm.bms.biz.storage.BizProductStorageEntityMapper.update", entity);
        return entity;
    }

    @Override
    public void delete(Long id) {
        delete("com.jiuyescm.bms.biz.storage.BizProductStorageEntityMapper.delete", id);
    }

	@SuppressWarnings("unchecked")
	@Override
	public BizProductStorageEntity queryExceptionOne(
			Map<String, Object> condition) {
		// TODO Auto-generated method stub
		return (BizProductStorageEntity) selectOne("com.jiuyescm.bms.biz.storage.BizProductStorageEntityMapper.queryExceptionOne", condition);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Properties validRetry(Map<String, Object> param) {
		Properties ret = new Properties();
		try{
			Object waybillno = selectOne("com.jiuyescm.bms.biz.storage.BizProductStorageEntityMapper.validBillForRetry", param);
			if(waybillno != null){
				ret.setProperty("key", "Billed");
				ret.setProperty("value", "部分数据已在账单中存在,不能重算,建议删除账单后重试");//已在账单中存在,不能重算,建议删除账单后重试;
				return ret;
			}
			
			waybillno = selectOne("com.jiuyescm.bms.biz.storage.BizProductStorageEntityMapper.validCalcuForRetry", param);
			if(waybillno != null){
				ret.setProperty("key", "Calculated");
				ret.setProperty("value", "存在已计算的数据");//已在账单中存在,不能重算,建议删除账单后重试;
				return ret;
			}
			ret.setProperty("key", "OK");
			ret.setProperty("value", "");
			return ret;
		}
		catch(Exception ex){
			logger.error("系统异常-验证重算异常", ex);
			ret.setProperty("key", "Error");
			ret.setProperty("value", "系统异常-验证重算异常");
			return ret;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public int reCalculate(Map<String, Object> param) {
		try{
			update("com.jiuyescm.bms.biz.storage.BizProductStorageEntityMapper.retryForCalcu", param);
			return 1;
		}
		catch(Exception ex){
			return 0;
		}
	}
	
	@SuppressWarnings("unchecked")
    @Override
    public int reCalculateForAll(Map<String, Object> param) {
        try{
            update("com.jiuyescm.bms.biz.storage.BizProductStorageEntityMapper.reCalculate", param);
            return 1;
        }
        catch(Exception ex){
            return 0;
        }
    }

	@Override
    public List<BizProductStorageEntity> queryList(Map<String, Object> condition) {
        List<BizProductStorageEntity> list = selectList("com.jiuyescm.bms.biz.storage.BizProductStorageEntityMapper.queryList", condition);
        return list;
    }
	
	@SuppressWarnings("unchecked")
	@Override
	public List<BmsAsynCalcuTaskEntity> queryTask(Map<String, Object> condition) {
		return this.selectList("com.jiuyescm.bms.biz.storage.BizProductStorageEntityMapper.queryTask", condition);
	}
}