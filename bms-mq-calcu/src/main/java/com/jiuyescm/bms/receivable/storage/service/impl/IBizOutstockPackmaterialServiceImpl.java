package com.jiuyescm.bms.receivable.storage.service.impl;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.jiuyescm.bms.biz.storage.entity.BizOutstockPackmaterialEntity;
import com.jiuyescm.cfm.persistence.mybatis.MyBatisDao;
import com.jiuyescm.bms.receivable.storage.service.IBizOutstockPackmaterialService;

/**
 * 商品库存dao层
 * @author cjw
 * 
 */
@Repository("bizOutstockPackmaterialService")
public class IBizOutstockPackmaterialServiceImpl extends MyBatisDao implements IBizOutstockPackmaterialService {

	private static final Logger logger = Logger.getLogger(BizOutstockPackmaterialEntity.class.getName());

	public IBizOutstockPackmaterialServiceImpl() {
		super();
	}

	@Override
	public List<BizOutstockPackmaterialEntity> query(Map<String, Object> condition) {
		List<BizOutstockPackmaterialEntity> list = selectList("com.jiuyescm.bms.receivable.storage.BizOutstockPackmeterialMapper.querybizOutstockMeterial", condition);
		return list;
	}

	@Override
	public void update(BizOutstockPackmaterialEntity entity) {
		try{
			this.update("com.jiuyescm.bms.receivable.storage.BizOutstockPackmeterialMapper.updatebizOutstockMeterial", entity);
		}
		catch(Exception ex){
			logger.error("【商品存储费任务】更新主表异常"+ex.getMessage());
		}
		
	}

	@Override
	public void updateBatch(List<BizOutstockPackmaterialEntity> list) {
		try{
			this.updateBatch("com.jiuyescm.bms.receivable.storage.BizOutstockPackmeterialMapper.updatebizOutstockMeterial", list);
		}
		catch(Exception ex){
			logger.error("【商品存储费任务】批量更新主表异常"+ex.getMessage());
		}
	}

	@Override
	public List<BizOutstockPackmaterialEntity> queryCost(Map<String, Object> map) {
		
		List<BizOutstockPackmaterialEntity> list = selectList("com.jiuyescm.bms.receivable.storage.BizOutstockPackmeterialMapper.querybizOutstockMeterialCost", map);
		return list;
	}
	
	
	
}
