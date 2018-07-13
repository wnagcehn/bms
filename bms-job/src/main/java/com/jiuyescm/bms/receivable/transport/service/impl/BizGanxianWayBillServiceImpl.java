package com.jiuyescm.bms.receivable.transport.service.impl;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.jiuyescm.bms.biz.transport.entity.BizGanxianWayBillEntity;
import com.jiuyescm.bms.receivable.transport.service.IBizGanxianWayBillService;
import com.jiuyescm.cfm.persistence.mybatis.MyBatisDao;

/**
 * 
 * @author wubangjun
 * 
 */
@Service("bizGanxianWayBillService")
public class BizGanxianWayBillServiceImpl extends MyBatisDao<BizGanxianWayBillEntity> implements IBizGanxianWayBillService {

	private static final Logger logger = Logger.getLogger(BizGanxianWayBillServiceImpl.class.getName());
    
    @Override
	public List<BizGanxianWayBillEntity> getGanxianWayBillList(Map<String, Object> condition) {
		try{
			return selectList("com.jiuyescm.bms.receivable.transport.mapper.BizGanxianWayBillMapper.querybizGanxianWayBill", condition);
		}
		catch(Exception ex){
			logger.error("查询干线运单异常"+ex.getMessage());
			return null;
		}
	}

    @Override
    public void update(BizGanxianWayBillEntity entity) {
    	try{
    		update("com.jiuyescm.bms.receivable.transport.mapper.BizGanxianWayBillMapper.updatebizGanxianWayBill", entity);
    	}
    	catch(Exception ex){
    		logger.error("更新干线运单异常"+ex.getMessage());
    	}
    }
    
    @Override
    public void updateBatch(List<BizGanxianWayBillEntity> list) {
    	try{
    		updateBatch("com.jiuyescm.bms.receivable.transport.mapper.BizGanxianWayBillMapper.updatebizGanxianWayBill", list);
    	}
    	catch(Exception ex){
    		logger.error("更新干线运单异常"+ex.getMessage());
    	}
    }
	
}
