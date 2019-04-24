package com.jiuyescm.bms.general.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.jiuyescm.cfm.persistence.mybatis.MyBatisDao;
import com.jiuyescm.bms.general.entity.FeesReceiveStorageEntity;
import com.jiuyescm.bms.general.service.IFeesReceiveStorageService;
import com.xxl.job.core.log.XxlJobLogger;

@Service("feesReceiveStorageService")
public class FeesReceiveStorageServiceImpl extends MyBatisDao implements IFeesReceiveStorageService {

	
	@SuppressWarnings("unchecked")
	@Override
	public boolean Insert(FeesReceiveStorageEntity entity) {
		try{
			/*this.delete("com.jiuyescm.bms.general.entity.FeesReceiveStorageMapper.delete",entity);*/
			this.insert("com.jiuyescm.bms.general.entity.FeesReceiveStorageMapper.insert", entity);
			return true;
		}
		catch(Exception ex){
			XxlJobLogger.log("操作仓储费用表异常--"+ex.getMessage());
			return false;
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void InsertBatch(List<FeesReceiveStorageEntity> entities) {
		this.insertBatch("com.jiuyescm.bms.general.entity.FeesReceiveStorageMapper.insert", entities);
	}

	@Override
	public void Delete(FeesReceiveStorageEntity entity) {
		this.delete("com.jiuyescm.bms.general.entity.FeesReceiveStorageMapper.delete",entity);
	}

	@Override
	public List<FeesReceiveStorageEntity> queryStorageFee(String feesNo) {
		return this.selectList("com.jiuyescm.bms.general.entity.FeesReceiveStorageMapper.query", feesNo);
	}

	@Override
	public boolean updateStorageFee(FeesReceiveStorageEntity entity) {
		try{
			List<FeesReceiveStorageEntity> currents = this.queryStorageFee(entity.getFeesNo());
			if(currents!=null && currents.size()>0){
				FeesReceiveStorageEntity current = currents.get(0);
				entity.setCreateTime(current.getCreateTime());
				entity.setCreator(current.getCreator());
				this.update("com.jiuyescm.bms.general.entity.FeesReceiveStorageMapper.update", entity);
			}
			else{
				this.Insert(entity);
			}
			return true;
		}
		catch(Exception ex){
			XxlJobLogger.log("更新运输费用表异常--"+ex.getMessage());
			return false;
		}
	}

	@Override
	public FeesReceiveStorageEntity validFeesNo(FeesReceiveStorageEntity entity) {
		FeesReceiveStorageEntity entity2 = (FeesReceiveStorageEntity) selectOne("com.jiuyescm.bms.general.entity.FeesReceiveStorageMapper.ValidFeesNo", entity);
		return entity2;
	}

	@Override
	public void deleteBatch(Map<String, Object> feesNos) {
		this.delete("com.jiuyescm.bms.general.entity.FeesReceiveStorageMapper.deleteBatch", feesNos);
	}
	
    @Override
    public void updateBatchFeeNo(Map<String, Object> feesNos) {
        // TODO Auto-generated method stub
        this.update("com.jiuyescm.bms.general.entity.FeesReceiveStorageMapper.updateBatchFeeNo", feesNos);
    }

	@Override
	public void updateOne(FeesReceiveStorageEntity entity) {
		this.update("com.jiuyescm.bms.general.entity.FeesReceiveStorageMapper.updateOne", entity);
	}

	@Override
	public List<FeesReceiveStorageEntity> queryDailyStorageFee(Map<String, Object> condition) {
		return this.selectList("com.jiuyescm.bms.general.entity.FeesReceiveStorageMapper.queryDailyStorageFee", condition);
	}

	@Override
	public void updateBatch(List<FeesReceiveStorageEntity> entity) {
		this.updateBatch("com.jiuyescm.bms.general.entity.FeesReceiveStorageMapper.update", entity);
	}



}
