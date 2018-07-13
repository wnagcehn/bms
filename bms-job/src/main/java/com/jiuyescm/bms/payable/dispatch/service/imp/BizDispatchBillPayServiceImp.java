package com.jiuyescm.bms.payable.dispatch.service.imp;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.jiuyescm.bms.biz.dispatch.entity.BizDispatchBillPayEntity;
import com.jiuyescm.cfm.persistence.mybatis.MyBatisDao;
import com.jiuyescm.bms.payable.dispatch.service.IBizDispatchBillPayService;

@Service("bizDispatchBillPayService")
@SuppressWarnings("rawtypes")
public class BizDispatchBillPayServiceImp extends MyBatisDao implements IBizDispatchBillPayService{

	private static final Logger logger = Logger.getLogger(BizDispatchBillPayServiceImp.class.getName());
	
	@Override
	@SuppressWarnings("unchecked")
	public List<BizDispatchBillPayEntity> query(Map<String, Object> condition) {
		try{
			List<BizDispatchBillPayEntity> list=selectList("BMS","com.jiuyescm.bms.payable.dispatch.mapper.BizDispatchBillPayMapper.querybizDispatchBillMap", condition);
			return list;
		}
		catch(Exception ex){
			logger.error("查询运单异常"+ex.getMessage());
			return null;
		}
		
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public int updateBatch(List<BizDispatchBillPayEntity> list) {
		return updateBatch("com.jiuyescm.bms.payable.dispatch.mapper.BizDispatchBillPayMapper.updatebizDispatchBillMap", list);
	}

	@Override
	@SuppressWarnings("unchecked")
	public int update(BizDispatchBillPayEntity entity) {
		return update("com.jiuyescm.bms.payable.dispatch.mapper.BizDispatchBillPayMapper.updatebizDispatchBillMap", entity);
	}

}