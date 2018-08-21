package com.jiuyescm.bms.general.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.jiuyescm.bms.general.entity.FeesReceiveDispatchEntity;
import com.jiuyescm.bms.general.service.IFeesReceiveDispatchService;
import com.jiuyescm.cfm.persistence.mybatis.MyBatisDao;
import com.xxl.job.core.log.XxlJobLogger;

@Service("feesReceiveDispatchService")
public class FeesReceiveDispatchServiceImpl extends MyBatisDao implements IFeesReceiveDispatchService {

	@SuppressWarnings("unchecked")
	@Override
	public boolean Insert(FeesReceiveDispatchEntity entity) {
		try{
			//this.delete("com.jiuyescm.bms.general.entity.FeesReceiveDispatchMapper.delete", entity);
			this.insert("com.jiuyescm.bms.general.entity.FeesReceiveDispatchMapper.insert", entity);
			return true;
		}
		catch(Exception ex){
			XxlJobLogger.log("操作配送费用表异常"+ex.getMessage());
			return false;
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void InsertBatch(List<FeesReceiveDispatchEntity> entities) {
		this.insertBatch("com.jiuyescm.bms.general.entity.FeesReceiveDispatchMapper.insert", entities);
	}

	@Override
	public void Delete(String omsId) {
		//this.delete("com.jiuyescm.bms.general.entity.FeesReceiveDispatchMapper.insert", omsId);
	}

	@Override
	public FeesReceiveDispatchEntity queryFees(Map<String, Object> aCondition) {
		// TODO Auto-generated method stub
		try{
			FeesReceiveDispatchEntity fee=(FeesReceiveDispatchEntity)selectOne("com.jiuyescm.bms.general.entity.FeesReceiveDispatchMapper.selectOne", aCondition);
			return fee;
		}
		catch(Exception ex){
			XxlJobLogger.log("查询单个费用异常"+ex.getMessage());
			return null;
		}
	}

	@Override
	public boolean update(FeesReceiveDispatchEntity entity) {
		// TODO Auto-generated method stub
		try{
			update("com.jiuyescm.bms.general.entity.FeesReceiveDispatchMapper.updateOne", entity);
			return true;
		}
		catch(Exception ex){
			XxlJobLogger.log("更新单个费用异常"+ex.getMessage());
			return false;
		}
	}

	@Override
	public FeesReceiveDispatchEntity validFeesNo(FeesReceiveDispatchEntity entity) {
		return (FeesReceiveDispatchEntity)selectOne("com.jiuyescm.bms.general.entity.FeesReceiveDispatchMapper.ValidFeesNo", entity);
	}

	@Override
	public void deleteBatch(Map<String, Object> feesMap) {
		this.delete("com.jiuyescm.bms.general.entity.FeesReceiveDispatchMapper.deleteBatch", feesMap);
		
	}

	@SuppressWarnings("unchecked")
	@Override
	public int deleteByWayBillNo(String waybillNo) {
		int ret = this.update("com.jiuyescm.bms.general.entity.FeesReceiveDispatchMapper.deleteByWayBillNo", waybillNo);
		return ret;
	}

	@Override
	public List<FeesReceiveDispatchEntity> queryDailyFees(Map<String, Object> condition) {
		return this.selectList("com.jiuyescm.bms.general.entity.FeesReceiveDispatchMapper.queryDailyFees", condition);
	}

	@Override
	public void updateBatch(List<FeesReceiveDispatchEntity> entity) {
		this.updateBatch("com.jiuyescm.bms.general.entity.FeesReceiveDispatchMapper.updateBatch", entity);
	}

}
