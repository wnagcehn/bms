package com.jiuyescm.bms.receivable.storage.service.impl;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import com.jiuyescm.bms.biz.storage.entity.BizProductPalletStorageEntity;
import com.jiuyescm.bms.biz.storage.entity.BizProductStorageEntity;
import com.jiuyescm.cfm.persistence.mybatis.MyBatisDao;
import com.jiuyescm.bms.receivable.storage.service.IBizProductPalletStorageService;
import com.jiuyescm.bms.receivable.storage.service.IBizProductStorageService;

/**
 * 商品库存dao层
 * @author cjw
 * 
 */
@Service("bizProductPalletStorageService")
@SuppressWarnings("rawtypes")
public class BizProductPalletStorageServiceImpl extends MyBatisDao implements IBizProductPalletStorageService {

	private static final Logger logger = Logger.getLogger(BizProductPalletStorageServiceImpl.class.getName());

	public BizProductPalletStorageServiceImpl() {
		super();
	}

	@Override
	public List<BizProductPalletStorageEntity> query(Map<String, Object> condition) {
		List<BizProductPalletStorageEntity> list = selectList("com.jiuyescm.bms.receivable.storage.BizProductPalletStorageMapper.querybizProductPallet", condition);
		return list;
	}

	@Override
	public void update(BizProductPalletStorageEntity entity) {
		try{
			this.update("com.jiuyescm.bms.receivable.storage.BizProductPalletStorageMapper.updatebizProductPallet", entity);
		}
		catch(Exception ex){
			logger.error("【商品存储费任务】更新主表异常"+ex.getMessage());
		}
		
	}

	@Override
	public void updateBatch(List<BizProductPalletStorageEntity> list) {
		try{
			this.updateBatch("com.jiuyescm.bms.receivable.storage.BizProductPalletStorageMapper.updatebizProductPallet", list);
		}
		catch(Exception ex){
			logger.error("【商品存储费任务】批量更新主表异常"+ex.getMessage());
		}
	}
	
	
	
}
