package com.jiuyescm.bms.payable.transport.service.impl;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.jiuyescm.bms.biz.transport.entity.BizGanxianRoadBillEntity;
import com.jiuyescm.cfm.persistence.mybatis.MyBatisDao;
import com.jiuyescm.bms.payable.transport.service.IBizGanxianRoadBillService;

/**
 * 
 * @author wubangjun
 * 
 */
@Service("bizGanxianRoadBillService")
public class BizGanxianRoadBillServiceImpl extends MyBatisDao implements IBizGanxianRoadBillService {

	private static final Logger logger = Logger.getLogger(BizGanxianRoadBillServiceImpl.class.getName());
    
    @Override
	public List<BizGanxianRoadBillEntity> getGanxianRoadBillList(Map<String, Object> condition) {
		try{
			return selectList("BMS","com.jiuyescm.payable.transport.mapper.BizGanxianRoadBillMapper.querybizGanxianRoadBill", condition);
		}
		catch(Exception ex){
			logger.error("查询干线路单异常"+ex.getMessage());
			return null;
		}
	}

    @Override
    public void update(BizGanxianRoadBillEntity entity) {
    	try{
    		update("BMS","com.jiuyescm.payable.transport.mapper.BizGanxianRoadBillMapper.updatebizGanxianRoadBill", entity);
    	}
    	catch(Exception ex){
    		logger.error("更新干线路单异常"+ex.getMessage());
    	}
    }
    
    @Override
    public void updateBatch(List<BizGanxianRoadBillEntity> list) {
    	try{
    		updateBatch("BMS","com.jiuyescm.payable.transport.mapper.BizGanxianRoadBillMapper.updatebizGanxianRoadBill", list);
    	}
    	catch(Exception ex){
    		logger.error("更新干线路单异常"+ex.getMessage());
    	}
    }
	
}
