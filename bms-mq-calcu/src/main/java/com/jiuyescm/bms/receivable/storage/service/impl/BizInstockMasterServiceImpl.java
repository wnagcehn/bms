package com.jiuyescm.bms.receivable.storage.service.impl;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.jiuyescm.bms.biz.storage.entity.BizInStockDetailEntity;
import com.jiuyescm.bms.biz.storage.entity.BizInStockMasterEntity;
import com.jiuyescm.bms.general.entity.FeesReceiveStorageEntity;
import com.jiuyescm.cfm.persistence.mybatis.MyBatisDao;
import com.jiuyescm.bms.receivable.storage.service.IBizInstockMasterService;
/**
 * 
 * @author cjw
 * 
 */
@Service("bizInstockMasterService")
public class BizInstockMasterServiceImpl extends MyBatisDao implements IBizInstockMasterService {

	private Logger logger = LoggerFactory.getLogger(BizInstockMasterServiceImpl.class);
	
	public BizInstockMasterServiceImpl() {
		super();
	}

	@Override
	public List<BizInStockMasterEntity> getInStockMasterList(Map<String, Object> condition) {
		try{
			List<BizInStockMasterEntity> list = selectList("com.jiuyescm.bms.receivable.storage.BizInstockMasterMapper.querybizInstockMaster", condition);
			return list;
		}
		catch(Exception ex){
			logger.error("入库单主表查询异常"+ex);
			return null;
		}
	}

	@Override
	public void updateInstock(BizInStockMasterEntity entity) {
		this.update("com.jiuyescm.bms.receivable.storage.BizInstockMasterMapper.updatebizInstockMaster", entity);
	}

	@Override
	public void updateInstockBatch(List<BizInStockMasterEntity> entities) {
		this.updateBatch("com.jiuyescm.bms.receivable.storage.BizInstockMasterMapper.updatebizInstockMaster", entities);
	}
	
	@Override
	public void updateInstockBatchByFees(List<FeesReceiveStorageEntity> entities) {
		this.updateBatch("com.jiuyescm.bms.receivable.storage.BizInstockMasterMapper.updatebizInstockMasterByFees", entities);
	}

	@Override
	public List<BizInStockDetailEntity> getDetailByMaster(String instockNo) {
		List<BizInStockDetailEntity> list = selectList("com.jiuyescm.bms.receivable.storage.BizInstockMasterMapper.queryInstockDetailByMaster", instockNo);
		return list;
	}

	@Override
	public List<BizInStockMasterEntity> getInStockWorkList(Map<String, Object> condition) {
		try{
			List<BizInStockMasterEntity> list = selectList("com.jiuyescm.bms.receivable.storage.BizInstockMasterMapper.querybizInstockWork", condition);
			return list;
		}
		catch(Exception ex){
			logger.error("入库单主表查询异常"+ex);
			return null;
		}
	}

	@Override
	public void updateInstockWork(BizInStockMasterEntity entity) {
		this.update("com.jiuyescm.bms.receivable.storage.BizInstockMasterMapper.updatebizInstockWork", entity);
		
	}

	@Override
	public void updateInstockWorkBatch(List<BizInStockMasterEntity> entities) {
		this.updateBatch("com.jiuyescm.bms.receivable.storage.BizInstockMasterMapper.updatebizInstockWork", entities);
	}
}
