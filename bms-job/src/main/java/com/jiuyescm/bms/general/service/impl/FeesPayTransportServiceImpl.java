package com.jiuyescm.bms.general.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.jiuyescm.bms.fees.out.transport.entity.FeesPayTransportEntity;
import com.jiuyescm.bms.fees.out.transport.entity.FeesPayTransportQueryEntity;
import com.jiuyescm.cfm.persistence.mybatis.MyBatisDao;
import com.jiuyescm.bms.general.service.IFeesPayTransportService;
import com.xxl.job.core.log.XxlJobLogger;

@Service("feesPayTransportService")
public class FeesPayTransportServiceImpl extends MyBatisDao implements IFeesPayTransportService {

	@SuppressWarnings("unchecked")
	@Override
	public boolean insert(FeesPayTransportEntity entity) {
		try{
			this.insert("com.jiuyescm.bms.general.mapper.FeesPayTransportMapper.save", entity);
			return true;
		}
		catch(Exception ex){
			XxlJobLogger.log("写入干线路单费用表异常--"+ex.getMessage());
			return false;
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void insertBatch(List<FeesPayTransportEntity> entities) {
		this.insertBatch("com.jiuyescm.bms.general.mapper.FeesPayTransportMapper.save", entities);
	}

	
	@Override
	public void delete(String omsId) {
		this.delete("com.jiuyescm.bms.general.mapper.FeesPayTransportMapper.delete", omsId);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<FeesPayTransportEntity> query(FeesPayTransportQueryEntity queryEntity) {
		return this.selectList("com.jiuyescm.bms.general.mapper.FeesPayTransportMapper.query", queryEntity);
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean update(FeesPayTransportEntity entity) {
		try{
			update("com.jiuyescm.general.mapper.FeesPayTransportMapper.update", entity);
			return true;
		}
		catch(Exception ex){
			XxlJobLogger.log("更新干线路单应付费用表异常--"+ex.getMessage());
			return false;
		}
	}

}
