/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.biz.storage.repository.impl;

import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.ibatis.session.RowBounds;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.biz.storage.entity.BizPackStorageEntity;
import com.jiuyescm.bms.biz.storage.repository.IBizPackStorageRepository;
import com.jiuyescm.cfm.persistence.mybatis.MyBatisDao;

/**
 * 
 * @author stevenl
 * 
 */
@Repository("bizPackStorageRepository")
public class BizPackStorageRepositoryImpl extends MyBatisDao implements IBizPackStorageRepository {

	private static final Logger logger = Logger.getLogger(BizPackStorageRepositoryImpl.class.getName());

	public BizPackStorageRepositoryImpl() {
		super();
	}
	
	@Override
    public PageInfo<BizPackStorageEntity> query(Map<String, Object> condition, int pageNo, int pageSize) {
        List<BizPackStorageEntity> list = selectList("com.jiuyescm.bms.biz.storage.BizPackStorageEntityMapper.query", condition, new RowBounds(
                pageNo, pageSize));
        PageInfo<BizPackStorageEntity> pageInfo = new PageInfo<BizPackStorageEntity>(list);
        return pageInfo;
    }

    @Override
    public BizPackStorageEntity findById(Long id) {
        /*BizPackStorageEntity entity = selectOne("com.jiuyescm.bms.biz.storage.BizPackStorageEntityMapper.findById", id);
        return entity;*/
    	return null;
    }

    @Override
    public BizPackStorageEntity save(BizPackStorageEntity entity) {
        insert("com.jiuyescm.bms.biz.storage.BizPackStorageEntityMapper.save", entity);
        return entity;
    }

    @Override
    public int update(BizPackStorageEntity entity) {
       
        return super.update("com.jiuyescm.bms.biz.storage.BizPackStorageEntityMapper.update", entity);
    }

    @Override
    public void delete(Long id) {
        delete("com.jiuyescm.bms.biz.storage.BizPackStorageEntityMapper.delete", id);
    }

	@Override
	public int saveList(List<BizPackStorageEntity> list) {
		return insertBatch("com.jiuyescm.bms.biz.storage.BizPackStorageEntityMapper.save", list);
	}

	@Override
	public PageInfo<BizPackStorageEntity> queryGroup(
			Map<String, Object> condition, int pageNo, int pageSize) {
		 List<BizPackStorageEntity> list = selectList("com.jiuyescm.bms.biz.storage.BizPackStorageEntityMapper.queryGroup", condition, new RowBounds(
	                pageNo, pageSize));
	        PageInfo<BizPackStorageEntity> pageInfo = new PageInfo<BizPackStorageEntity>(list);
	        return pageInfo;
	}

	@Override
	public int reCalculate(Map<String, Object> condition) {
		try{
			update("com.jiuyescm.bms.biz.storage.BizPackStorageEntityMapper.retryForCalcu", condition);
			return 1;
		}
		catch(Exception ex){
			return 0;
		}
	}


	//Billed-已在账单中存在,不能重算,建议删除账单后重试;Calculated-已计算,是否继续重算;Error-系统错误;OK-可重算
	@Override
	public Properties validRetry(Map<String, Object> condition) {
		
		Properties ret = new Properties();
		try{
			Object waybillno = selectOne("com.jiuyescm.bms.biz.storage.BizPackStorageEntityMapper.validBillForRetry", condition);
			if(waybillno != null){
				ret.setProperty("key", "Billed");
				ret.setProperty("value", "运单号【"+waybillno+"】已在账单中存在,不能重算,建议删除账单后重试");//已在账单中存在,不能重算,建议删除账单后重试;
				return ret;
			}
			
			waybillno = selectOne("com.jiuyescm.bms.biz.storage.BizPackStorageEntityMapper.validCalcuForRetry", condition);
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

	@Override
	public int deleteList(List<BizPackStorageEntity> list) {
		return deleteBatch("com.jiuyescm.bms.biz.storage.BizPackStorageEntityMapper.delete",list);
	}

	@Override
	public List<BizPackStorageEntity> queryList(Map<String, Object> condition) {
		return selectList("com.jiuyescm.bms.biz.storage.BizPackStorageEntityMapper.queryDelete",condition);
	}

	@Override
	public int deleteFees(Map<String, Object> condition) {
		return  delete("com.jiuyescm.bms.biz.storage.BizPackStorageEntityMapper.deleteFees",condition);
	}

	@Override
	public PageInfo<Map<String, String>> queryByMonth(
			Map<String, Object> condition, int pageNo, int pageSize) {
		List<Map<String, String>> list = selectList("com.jiuyescm.bms.biz.storage.BizPackStorageEntityMapper.queryByMonth", condition, new RowBounds(
                pageNo, pageSize));
        PageInfo<Map<String, String>> pageInfo = new PageInfo<Map<String, String>>(list);
        return pageInfo;
	}

	@Override
	public PageInfo<Map<String, String>> queryCustomeridF(
			Map<String, Object> condition, int pageNo, int pageSize) {
		List<Map<String, String>> list = selectList("com.jiuyescm.bms.biz.storage.BizPackStorageEntityMapper.queryCustomeridF", condition, new RowBounds(
                pageNo, pageSize));
        PageInfo<Map<String, String>> pageInfo = new PageInfo<Map<String, String>>(list);
        return pageInfo;
	}
	
	@Override
	public BizPackStorageEntity queryExceptionOne(Map<String, Object> condition) {
		// TODO Auto-generated method stub
		return (BizPackStorageEntity) selectOne("com.jiuyescm.bms.biz.storage.BizPackStorageEntityMapper.queryExceptionOne", condition);
	}

	@Override
	public List<BizPackStorageEntity> queryAllBycurTime(Map<String, Object> map) {
		return this.selectList("com.jiuyescm.bms.biz.storage.BizPackStorageEntityMapper.queryAllBycurTime", map);
	}
	
	@Override
	public BizPackStorageEntity queryOneByParam(BizPackStorageEntity entity) {
		return (BizPackStorageEntity) selectOne("com.jiuyescm.bms.biz.storage.BizPackStorageEntityMapper.queryOneByParam", entity);
	}

	@Override
	public int updateBatch(List<BizPackStorageEntity> list) {
		return updateBatch("com.jiuyescm.bms.biz.storage.BizPackStorageEntityMapper.updateBatch",list);
	}
}
