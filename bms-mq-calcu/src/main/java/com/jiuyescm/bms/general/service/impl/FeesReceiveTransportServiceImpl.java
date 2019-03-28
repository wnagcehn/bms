package com.jiuyescm.bms.general.service.impl;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.jiuyescm.bms.fees.entity.FeesReceiveDeliverQueryEntity;
import com.jiuyescm.cfm.persistence.mybatis.MyBatisDao;
import com.jiuyescm.bms.general.entity.FeesReceiveTransportEntity;
import com.jiuyescm.bms.general.service.IFeesReceiveTransportService;

@Service("feesReceiveTransportService")
public class FeesReceiveTransportServiceImpl extends MyBatisDao implements IFeesReceiveTransportService {

	private Logger logger = LoggerFactory.getLogger(FeesReceiveTransportServiceImpl.class);
	
	@SuppressWarnings("unchecked")
	@Override
	public boolean Insert(FeesReceiveTransportEntity entity) {
		try{
			this.delete("com.jiuyescm.bms.general.entity.FeesReceiveTransportMapper.delete", entity);
			this.insert("com.jiuyescm.bms.general.entity.FeesReceiveTransportMapper.save", entity);
			return true;
		}
		catch(Exception ex){
			logger.error("写入干线运单费用表异常--"+ex);
			return false;
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void InsertBatch(List<FeesReceiveTransportEntity> entities) {
		this.insertBatch("com.jiuyescm.bms.general.entity.FeesReceiveTransportMapper.save", entities);
	}

	@Override
	public void Delete(String omsId) {
		this.delete("com.jiuyescm.bms.general.entity.FeesReceiveTransportMapper.delete", omsId);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<FeesReceiveTransportEntity> query(FeesReceiveDeliverQueryEntity queryEntity) {
		return this.selectList("com.jiuyescm.bms.general.entity.FeesReceiveTransportMapper.query", queryEntity);
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean update(FeesReceiveTransportEntity entity) {
		try{
			update("com.jiuyescm.bms.general.entity.FeesReceiveTransportMapper.update", entity);
			return true;
		}
		catch(Exception ex){
			logger.error("更新干线运单费用表异常--"+ex);
			return false;
		}
	}

	@Override
	public List<FeesReceiveTransportEntity> queryDailyFees(Map<String, Object> condition) {
		return this.selectList("com.jiuyescm.bms.general.entity.FeesReceiveTransportMapper.queryDailyFees", condition);
	}

}
