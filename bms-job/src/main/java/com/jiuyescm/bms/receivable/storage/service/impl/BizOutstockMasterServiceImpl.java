package com.jiuyescm.bms.receivable.storage.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.jiuyescm.bms.biz.storage.entity.BizOutstockDetailEntity;
import com.jiuyescm.bms.biz.storage.entity.BizOutstockMasterEntity;
import com.jiuyescm.bms.general.entity.FeesReceiveStorageEntity;
import com.jiuyescm.cfm.persistence.mybatis.MyBatisDao;
import com.jiuyescm.bms.receivable.storage.service.IBizOutstockMasterService;
import com.xxl.job.core.log.XxlJobLogger;

/**
 * @author cjw
 */
@Repository("bizOutstockMasterService")
public class BizOutstockMasterServiceImpl extends MyBatisDao implements IBizOutstockMasterService {

	public BizOutstockMasterServiceImpl() {
		super();
	}

	@Override
	public List<BizOutstockMasterEntity> query(Map<String, Object> condition) {
		try{
			List<BizOutstockMasterEntity> list = selectList("com.jiuyescm.bms.receivable.storage.BizOutstockMasterMapper.queryCalculate", condition);
			return list;
		}
		catch(Exception ex){
			XxlJobLogger.log("【订单操作费订单任务】主查询异常"+ex.getMessage());
			return null;
		}
	}

	@Override
	public void update(BizOutstockMasterEntity entity) {
		try{
			this.update("com.jiuyescm.bms.receivable.storage.BizOutstockMasterMapper.updateCalculate", entity);
		}
		catch(Exception ex){
			XxlJobLogger.log("【订单操作费订单任务】更新主表异常"+ex.getMessage());
		}
	}

	@Override
	public List<BizOutstockDetailEntity> getInstockDetailByMaster(String outstockNo) {
		try{
			List<BizOutstockDetailEntity> list = selectList("com.jiuyescm.bms.receivable.storage.BizOutstockMasterMapper.queryOutStockDetail", outstockNo);
			return list;
		}
		catch(Exception ex){
			XxlJobLogger.log("【订单操作费订单任务】子查询异常"+ex.getMessage());
			return null;
		}
	}

	@Override
	public void updateOutstockBatch(List<BizOutstockMasterEntity> OutStocks) {
		try{
		    
		    Map<String,Object> map=new HashMap<String, Object>();
		    map.put("list", OutStocks);
			this.update("com.jiuyescm.bms.receivable.storage.BizOutstockMasterMapper.updateCalculate", map);
		}
		catch(Exception ex){
			XxlJobLogger.log("【订单操作费订单任务】更新主表异常"+ex.getMessage());
		}
	}
	
	@Override
	public void updateOutstockBatchByFees(List<FeesReceiveStorageEntity> OutStocks) {
		try{
			this.updateBatch("com.jiuyescm.bms.receivable.storage.BizOutstockMasterMapper.updateCalculateByFees", OutStocks);
		}
		catch(Exception ex){
			XxlJobLogger.log("【订单操作费订单任务】更新主表异常"+ex.getMessage());
		}
	}

	@Override
	public List<Map<String, String>> getTypeList(String type) {
		List<Map<String,String>> list = selectList("com.jiuyescm.bms.receivable.storage.BizOutstockMasterMapper.querySysCode", type);
		return list;
	}
	
}
