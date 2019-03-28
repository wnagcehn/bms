package com.jiuyescm.bms.receivable.storage.service.impl;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.jiuyescm.bms.biz.storage.entity.BizPackStorageEntity;
import com.jiuyescm.bms.receivable.storage.service.IBizPackStorageService;
import com.jiuyescm.cfm.persistence.mybatis.MyBatisDao;

/**
 * 
 * @author cjw
 * 
 */
@Repository("bizPackStorageService")
public class BizPackStorageServiceImpl extends MyBatisDao implements IBizPackStorageService {

	private Logger logger = LoggerFactory.getLogger(BizPackStorageServiceImpl.class);
	
	@Override
	public List<BizPackStorageEntity> query(Map<String, Object> condition) {
		
		List<BizPackStorageEntity> list = selectList("com.jiuyescm.bms.receivable.storage.BizPackStorageMapper.querybizPack", condition);
		return list;
	}

	@Override
	public void update(BizPackStorageEntity entity) {
		try{
			this.update("com.jiuyescm.bms.receivable.storage.BizPackStorageMapper.updatebizPack", entity);
		}
		catch(Exception ex){
			logger.error("【商品存储费任务】更新主表异常{0}",ex);
		}
	}

	@Override
	public void updateBatch(List<BizPackStorageEntity> list) {
		try{
			this.updateBatch("com.jiuyescm.bms.receivable.storage.BizPackStorageMapper.updatebizPack", list);
		}
		catch(Exception ex){
			logger.error("【商品存储费任务】批量更新主表异常{0}",ex);
		}
	}

}
