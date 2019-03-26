package com.jiuyescm.bms.receivable.storage.service.impl;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.jiuyescm.bms.biz.storage.entity.BizProductStorageEntity;
import com.jiuyescm.bms.general.entity.FeesReceiveStorageEntity;
import com.jiuyescm.cfm.persistence.mybatis.MyBatisDao;
import com.jiuyescm.bms.receivable.storage.service.IBizProductStorageService;

/**
 * 商品库存dao层
 * @author cjw
 * 
 */
@Repository("bizProductStorageService")
public class BizProductStorageServiceImpl extends MyBatisDao implements IBizProductStorageService {

	private static final Logger logger = Logger.getLogger(BizProductStorageServiceImpl.class.getName());

	public BizProductStorageServiceImpl() {
		super();
	}

	@Override
	public List<BizProductStorageEntity> query(Map<String, Object> condition) {
		List<BizProductStorageEntity> list = selectList("com.jiuyescm.bms.receivable.storage.BizProductStorageMapper.queryProductStorage", condition);
		return list;
	}

	@Override
	public void update(BizProductStorageEntity entity) {
		try{
			this.update("com.jiuyescm.bms.receivable.storage.BizProductStorageMapper.updateProductStorage", entity);
		}
		catch(Exception ex){
			logger.error("【商品存储费任务】更新主表异常"+ex.getMessage());
		}
		
	}

	@Override
	public void updateBatch(List<BizProductStorageEntity> list) {
		try{
			this.updateBatch("com.jiuyescm.bms.receivable.storage.BizProductStorageMapper.updateProductStorage", list);
		}
		catch(Exception ex){
			logger.error("【商品存储费任务】批量更新主表异常"+ex.getMessage());
		}
	}
	
	@Override
	public void updateProductStorageById(List<FeesReceiveStorageEntity> list) {
		try{
			this.updateBatch("com.jiuyescm.bms.receivable.storage.BizProductStorageMapper.updateProductStorageById", list);
		}
		catch(Exception ex){
			logger.error("【商品存储费任务】批量更新主表异常"+ex.getMessage());
		}
	}
	
	
}
