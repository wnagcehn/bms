/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.biz.storage.repository.impl;

import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import com.jiuyescm.bms.biz.storage.entity.BizProductPalletStorageEntity;
import com.jiuyescm.bms.biz.storage.repository.IBizProductPalletStorageRepository;
import com.jiuyescm.cfm.persistence.mybatis.MyBatisDao;

/**
 * 商品按托存储库存
 * @author wubangjun
 * 
 */
@SuppressWarnings("unchecked")
@Repository("bizProductPalletStorageRepository")
public class BizProductPalletStorageRepositoryImpl extends MyBatisDao implements IBizProductPalletStorageRepository {

	private static final Logger logger = Logger.getLogger(BizProductPalletStorageRepositoryImpl.class.getName());

	public BizProductPalletStorageRepositoryImpl() {
		super();
	}
	
	@Override
    public PageInfo<BizProductPalletStorageEntity> query(Map<String, Object> condition, int pageNo, int pageSize) {
        List<BizProductPalletStorageEntity> list = selectList("com.jiuyescm.bms.biz.storage.mapper.BizProductPalletStorageEntityMapper.query", condition, new RowBounds(
                pageNo, pageSize));
        PageInfo<BizProductPalletStorageEntity> pageInfo = new PageInfo<BizProductPalletStorageEntity>(list);
        return pageInfo;
    }

    @Override
    public BizProductPalletStorageEntity findById(Long id) {
        /*BizProductPalletStorageEntity entity = selectOne("com.jiuyescm.bms.biz.storage.mapper.BizProductPalletStorageEntityMapper.findById", id);
        return entity;*/
    	return null;
    }

    @Override
    public BizProductPalletStorageEntity save(BizProductPalletStorageEntity entity) {
        insert("com.jiuyescm.bms.biz.storage.mapper.BizProductPalletStorageEntityMapper.save", entity);
        return entity;
    }

    @Override
    public BizProductPalletStorageEntity update(BizProductPalletStorageEntity entity) {
        update("com.jiuyescm.bms.biz.storage.mapper.BizProductPalletStorageEntityMapper.update", entity);
        return entity;
    }

    @Override
    public void delete(Long id) {
        delete("com.jiuyescm.bms.biz.storage.mapper.BizProductPalletStorageEntityMapper.delete", id);
    }

	@Override
	public int saveList(List<BizProductPalletStorageEntity> dataList) {
		return insertBatch("com.jiuyescm.bms.biz.storage.mapper.BizProductPalletStorageEntityMapper.save", dataList);
	}

	@Override
	public PageInfo<BizProductPalletStorageEntity> queryGroup(
			Map<String, Object> condition, int pageNo, int pageSize) {
		List<BizProductPalletStorageEntity> list = selectList("com.jiuyescm.bms.biz.storage.mapper.BizProductPalletStorageEntityMapper.queryGroup", condition, new RowBounds(
                pageNo, pageSize));
        PageInfo<BizProductPalletStorageEntity> pageInfo = new PageInfo<BizProductPalletStorageEntity>(list);
        return pageInfo;
	}

	@Override
	public Properties validRetry(Map<String, Object> param) {
		Properties ret = new Properties();
		try{
			Object waybillno = selectOne("com.jiuyescm.bms.biz.storage.mapper.BizProductPalletStorageEntityMapper.validBillForRetry", param);
			if(waybillno != null){
				ret.setProperty("key", "Billed");
				ret.setProperty("value", "部分数据已在账单中存在,不能重算,建议删除账单后重试");//已在账单中存在,不能重算,建议删除账单后重试;
				return ret;
			}
			
			waybillno = selectOne("com.jiuyescm.bms.biz.storage.mapper.BizProductPalletStorageEntityMapper.validCalcuForRetry", param);
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
	public int reCalculate(Map<String, Object> param) {
		try{
			update("com.jiuyescm.bms.biz.storage.mapper.BizProductPalletStorageEntityMapper.retryForCalcu", param);
			return 1;
		}
		catch(Exception ex){
			return 0;
		}
	}

	@Override
	public List<BizProductPalletStorageEntity> queryList(Map<String, Object> condition) {
		return selectList("com.jiuyescm.bms.biz.storage.mapper.BizProductPalletStorageEntityMapper.queryDelete",condition);
	}

	@Override
	public int deleteList(List<BizProductPalletStorageEntity> list) {
		return deleteBatch("com.jiuyescm.bms.biz.storage.mapper.BizProductPalletStorageEntityMapper.delete",list);
	}

	@Override
	public int deleteFees(Map<String, Object> condition) {
		return delete("com.jiuyescm.bms.biz.storage.mapper.BizProductPalletStorageEntityMapper.deleteFees",condition);
	}

	@Override
	public PageInfo<Map<String, String>> queryByMonth(Map<String, Object> condition,int pageNo, int pageSize) {
		List<Map<String, String>> list = selectList("com.jiuyescm.bms.biz.storage.mapper.BizProductPalletStorageEntityMapper.queryByMonth", condition, new RowBounds(
                pageNo, pageSize));
        PageInfo<Map<String, String>> pageInfo = new PageInfo<Map<String, String>>(list);
        return pageInfo;
	}

	@Override
	public PageInfo<Map<String, String>> queryCustomeridF(Map<String, Object> condition,int pageNo, int pageSize) {
		List<Map<String, String>> list = selectList("com.jiuyescm.bms.biz.storage.mapper.BizProductPalletStorageEntityMapper.queryCustomeridF", condition, new RowBounds(
                pageNo, pageSize));
        PageInfo<Map<String, String>> pageInfo = new PageInfo<Map<String, String>>(list);
        return pageInfo;
	}

	@Override
	public BizProductPalletStorageEntity queryOneByParam(BizProductPalletStorageEntity entity) {
		return (BizProductPalletStorageEntity)selectOne("com.jiuyescm.bms.biz.storage.mapper.BizProductPalletStorageEntityMapper.queryOneByParam",entity);
	}


	@Override
	public int updateBatch(List<BizProductPalletStorageEntity> dataList) {
		return  updateBatch("com.jiuyescm.bms.biz.storage.mapper.BizProductPalletStorageEntityMapper.updateBatch",dataList);
	}


	@Override
	public List<BizProductPalletStorageEntity> queryAllBystockTime(
			Map<String, Object> parameter) {
		return this.selectList("com.jiuyescm.bms.biz.storage.mapper.BizProductPalletStorageEntityMapper.queryAllBystockTime", parameter);
	}

	@Override
	public PageInfo<BizProductPalletStorageEntity> queryStorageDiff(
			Map<String, Object> condition, int pageNo, int pageSize) {
		List<BizProductPalletStorageEntity> list = selectList("com.jiuyescm.bms.biz.storage.mapper.BizProductPalletStorageEntityMapper.queryStorageDiff", 
				condition, 
				new RowBounds(pageNo, pageSize));
        PageInfo<BizProductPalletStorageEntity> pageInfo = new PageInfo<BizProductPalletStorageEntity>(list);
        return pageInfo;
	}

	@Override
	public List<BizProductPalletStorageEntity> queryStorageDiff(Map<String, Object> condition) {
		return this.selectList("com.jiuyescm.bms.biz.storage.mapper.BizProductPalletStorageEntityMapper.queryStorageDiff", condition);
	}

	@Override
	public int saveTempData(String taskId) {
		// TODO Auto-generated method stub
		 SqlSession session = getSqlSessionTemplate();
		 Map<String,String> map=Maps.newHashMap();
		 map.put("taskId", taskId);
		 return session.insert("com.jiuyescm.bms.biz.storage.mapper.BizProductPalletStorageEntityMapper.saveDataFromTemp", map);
	}

	
}
