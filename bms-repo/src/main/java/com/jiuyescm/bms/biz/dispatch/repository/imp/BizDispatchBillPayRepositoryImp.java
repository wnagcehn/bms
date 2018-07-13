package com.jiuyescm.bms.biz.dispatch.repository.imp;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.ibatis.session.RowBounds;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.biz.dispatch.entity.BizDispatchBillPayEntity;
import com.jiuyescm.bms.biz.dispatch.repository.IBizDispatchBillPayRepository;
import com.jiuyescm.cfm.persistence.mybatis.MyBatisDao;

@Repository("bizDispatchBillPayRepository")
@SuppressWarnings("rawtypes")
public class BizDispatchBillPayRepositoryImp extends MyBatisDao implements IBizDispatchBillPayRepository{

	private static final Logger logger = Logger.getLogger(BizDispatchBillPayRepositoryImp.class.getName());
	
	@Override
	@SuppressWarnings("unchecked")
	public PageInfo<BizDispatchBillPayEntity> queryAll(
			Map<String, Object> condition, int pageNo, int pageSize) {
		// TODO Auto-generated method stub
		List<BizDispatchBillPayEntity> l=selectList("com.jiuyescm.bms.biz.dispatch.mapper.BizDispatchBillPayMapper.queryAll", condition,new RowBounds(pageNo,pageSize));
		PageInfo<BizDispatchBillPayEntity> p=new PageInfo<>(l);
		
		return p;
	}

	@Override
	@SuppressWarnings("unchecked")
	public int updateBill(List<BizDispatchBillPayEntity> aCondition) {
		// TODO Auto-generated method stub
		return updateBatch("com.jiuyescm.bms.biz.dispatch.mapper.BizDispatchBillPayMapper.updateBill", aCondition);
	}

	@Override
	@SuppressWarnings("unchecked")
	public int updateBillEntity(BizDispatchBillPayEntity aCondition) {
		// TODO Auto-generated method stub
		aCondition.setRemark(null);
		return update("com.jiuyescm.bms.biz.dispatch.mapper.BizDispatchBillPayMapper.updateBill", aCondition);
	}

	@Override
	@SuppressWarnings("unchecked")
	public PageInfo<BizDispatchBillPayEntity> queryAllPrice(
			Map<String, Object> condition, int pageNo, int pageSize) {
		// TODO Auto-generated method stubo
		List<BizDispatchBillPayEntity> list=selectList("com.jiuyescm.bms.biz.dispatch.mapper.BizDispatchBillPayMapper.queryAllPrice", condition,new RowBounds(pageNo,pageSize));
		PageInfo<BizDispatchBillPayEntity> p=new PageInfo<>(list);
		return p;
	}

	@Override
	@SuppressWarnings("unchecked")
	public PageInfo<BizDispatchBillPayEntity> queryAllData(
			Map<String, Object> condition, int pageNo, int pageSize) {
		// TODO Auto-generated method stub
		List<BizDispatchBillPayEntity> list=selectList("com.jiuyescm.bms.biz.dispatch.mapper.BizDispatchBillPayMapper.queryAllData", condition,new RowBounds(pageNo,pageSize));
		PageInfo<BizDispatchBillPayEntity> p=new PageInfo<>(list);
		return p;
	}

	@Override
	public Properties validRetry(Map<String, Object> param) {
		Properties ret = new Properties();
		try{
			Object waybillno = selectOne("com.jiuyescm.bms.biz.dispatch.mapper.BizDispatchBillPayMapper.validBillForRetry", param);
			if(waybillno != null){
				ret.setProperty("key", "Billed");
				ret.setProperty("value", "运单号【"+waybillno+"】已在账单中存在,不能重算,建议删除账单后重试");//已在账单中存在,不能重算,建议删除账单后重试;
				return ret;
			}
			
			waybillno = selectOne("com.jiuyescm.bms.biz.dispatch.mapper.BizDispatchBillPayMapper.validCalcuForRetry", param);
			if(waybillno != null){
				ret.setProperty("key", "Calculated");
				ret.setProperty("value", "存在已计算的数据");//已在账单中存在,不能重算,建议删除账单后重试;
				return ret;
			}
			ret.setProperty("key", "OK");
			ret.setProperty("value", "");
			return ret;
		}
		catch(Exception ex){
			logger.error("系统异常-验证重算异常", ex);
			ret.setProperty("key", "Error");
			ret.setProperty("value", "系统异常-验证重算异常");
			return ret;
		}
	}

	@Override
	public int reCalculate(Map<String, Object> param) {
		try{
			update("com.jiuyescm.bms.biz.dispatch.mapper.BizDispatchBillPayMapper.retryForCalcu", param);
			return 1;
		}
		catch(Exception ex){
			return 0;
		}
	}
	
	@Override
	public int saveList(List<BizDispatchBillPayEntity> list) {
		// TODO Auto-generated method stub
		return insertBatch("com.jiuyescm.bms.biz.dispatch.mapper.BizDispatchBillPayMapper.insertBizList", list);
	}

	@Override
	public List<BizDispatchBillPayEntity> queryBizData(List<String> aCondition) {
		// TODO Auto-generated method stub
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("waybillnos", aCondition);
		return this.selectList("com.jiuyescm.bms.biz.dispatch.mapper.BizDispatchBillPayMapper.queryBizData", map);
	}

	@Override
	public int queryDispatch(Map<String, Object> param) {
		return (Integer)selectOne("com.jiuyescm.bms.biz.dispatch.mapper.BizDispatchBillPayMapper.queryDispatch", param);
	}

	@Override
	public int updateBatchWeight(List<Map<String, Object>> list) {
		return updateBatch("com.jiuyescm.bms.biz.dispatch.mapper.BizDispatchBillPayMapper.updateWeight", list);
	}

	@Override
	public BizDispatchBillPayEntity queryExceptionOne(Map<String,Object> condition) {
		// TODO Auto-generated method stub
		return (BizDispatchBillPayEntity) selectOne("com.jiuyescm.bms.biz.dispatch.mapper.BizDispatchBillPayMapper.queryExceptionOne", condition);
	}

	@Override
	public int adjustBillPayEntity(BizDispatchBillPayEntity temp) {
		return this.update("com.jiuyescm.bms.biz.dispatch.mapper.BizDispatchBillPayMapper.adjustBillPay", temp);
	}

	
}