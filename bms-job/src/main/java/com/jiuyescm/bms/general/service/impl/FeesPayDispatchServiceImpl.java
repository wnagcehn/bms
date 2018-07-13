package com.jiuyescm.bms.general.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.jiuyescm.bms.general.entity.FeesPayDispatchEntity;
import com.jiuyescm.bms.general.service.IFeesPayDispatchService;
import com.jiuyescm.cfm.persistence.mybatis.MyBatisDao;
import com.xxl.job.core.log.XxlJobLogger;

@Service("feesPayDispatchService")
public class FeesPayDispatchServiceImpl extends MyBatisDao implements IFeesPayDispatchService {

	@SuppressWarnings("unchecked")
	@Override
	public boolean Insert(FeesPayDispatchEntity entity) {
		
		try{
			this.insert("com.jiuyescm.bms.general.mapper.FeesPayDispatchMapper.insert", entity);
			return true;
		}
		catch(Exception ex){
			XxlJobLogger.log("操作仓储费用表异常--"+ex.getMessage());
			return false;
		}
		
		/*try{
			if(this.validFeesNo(entity) == null){ 
				//费用表没有数据 ,执行写入操作
				this.insert("com.jiuyescm.bms.general.mapper.FeesPayDispatchMapper.insert", entity);
				
			}else{
				//费用表有数据 ,执行更新操作
				update("com.jiuyescm.bms.general.mapper.FeesPayDispatchMapper.updateOne", entity);
			}
			
			return true;
			
		}
		catch(Exception ex){
			XxlJobLogger.log("写入配送费用表异常"+ex.getMessage());
			return false;
		}*/
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void InsertBatch(List<FeesPayDispatchEntity> entities) {
		this.insertBatch("com.jiuyescm.bms.general.mapper.FeesPayDispatchMapper.insert", entities);
	}

	@Override
	public void Delete(String omsId) {
		this.delete("com.jiuyescm.bms.general.mapper.FeesPayDispatchMapper.insert", omsId);
	}

	@Override
	public FeesPayDispatchEntity queryFees(Map<String, Object> aCondition) {
		// TODO Auto-generated method stub
		try{
			FeesPayDispatchEntity fee= (FeesPayDispatchEntity)selectOne("com.jiuyescm.bms.general.mapper.FeesPayDispatchMapper.selectOne", aCondition);
			return fee;
		}
		catch(Exception ex){
			XxlJobLogger.log("查询单个费用异常"+ex.getMessage());
			return null;
		}
	}

	@Override
	public boolean update(FeesPayDispatchEntity entity) {
		try{
			update("com.jiuyescm.bms.general.mapper.FeesPayDispatchMapper.updateOne", entity);
			return true;
		}
		catch(Exception ex){
			XxlJobLogger.log("更新单个费用异常"+ex.getMessage());
			return false;
		}
	}

	@Override
	public FeesPayDispatchEntity validFeesNo(FeesPayDispatchEntity entity) {
		return (FeesPayDispatchEntity)selectOne("com.jiuyescm.bms.general.mapper.FeesPayDispatchMapper.selectOne", entity);
	}

	@Override
	public void deleteBatch(Map<String, Object> feesMap) {
	
		this.delete("com.jiuyescm.bms.general.mapper.FeesPayDispatchMapper.deleteBatch", feesMap);
	}

}
