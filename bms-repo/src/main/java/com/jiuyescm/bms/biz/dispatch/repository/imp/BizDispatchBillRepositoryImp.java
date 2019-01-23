package com.jiuyescm.bms.biz.dispatch.repository.imp;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.ibatis.session.RowBounds;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.base.dictionary.entity.SystemCodeTypeEntity;
import com.jiuyescm.bms.biz.dispatch.entity.BizDispatchBillEntity;
import com.jiuyescm.bms.biz.dispatch.repository.IBizDispatchBillRepository;
import com.jiuyescm.bms.biz.dispatch.vo.BizDispatchBillVo;
import com.jiuyescm.cfm.persistence.mybatis.MyBatisDao;

@Repository("bizDispatchBillRepository")
public class BizDispatchBillRepositoryImp extends MyBatisDao implements IBizDispatchBillRepository{

	private static final Logger logger = Logger.getLogger(BizDispatchBillRepositoryImp.class.getName());
	
	@Override
	@SuppressWarnings("unchecked")
	public PageInfo<BizDispatchBillEntity> queryAll(Map<String, Object> condition, int pageNo, int pageSize) {
		List<BizDispatchBillEntity> l=selectList("com.jiuyescm.bms.biz.dispatch.mapper.BizDispatchBillMapper.queryAll", condition,new RowBounds(pageNo,pageSize));
		PageInfo<BizDispatchBillEntity> p=new PageInfo<>(l);
		
		return p;
	}

	@Override
	@SuppressWarnings("unchecked")
	public PageInfo<BizDispatchBillVo> queryData(Map<String, Object> condition, int pageNo, int pageSize) {
		List<BizDispatchBillVo> l=selectList("com.jiuyescm.bms.biz.dispatch.mapper.BizDispatchBillMapper.queryData", condition,new RowBounds(pageNo,pageSize));
		PageInfo<BizDispatchBillVo> p=new PageInfo<>(l);
		
		return p;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public int updateBill(List<BizDispatchBillEntity> aCondition) {
		return updateBatch("com.jiuyescm.bms.biz.dispatch.mapper.BizDispatchBillMapper.updateBill", aCondition);
	}

	@Override
	@SuppressWarnings("unchecked")
	public int updateBillEntity(BizDispatchBillEntity aCondition) {
		return update("com.jiuyescm.bms.biz.dispatch.mapper.BizDispatchBillMapper.updateBill", aCondition);
	}

	@Override
	@SuppressWarnings("unchecked")
	public PageInfo<BizDispatchBillEntity> queryAllPrice(
			Map<String, Object> condition, int pageNo, int pageSize) {
		List<BizDispatchBillEntity> list=selectList("com.jiuyescm.bms.biz.dispatch.mapper.BizDispatchBillMapper.queryAllPrice", condition,new RowBounds(pageNo,pageSize));
		PageInfo<BizDispatchBillEntity> p=new PageInfo<>(list);
		return p;
	}

	@Override
	@SuppressWarnings("unchecked")
	public PageInfo<BizDispatchBillEntity> queryAllData(
			Map<String, Object> condition, int pageNo, int pageSize) {
		List<BizDispatchBillEntity> list=selectList("com.jiuyescm.bms.biz.dispatch.mapper.BizDispatchBillMapper.queryAllData", condition,new RowBounds(pageNo,pageSize));
		PageInfo<BizDispatchBillEntity> p=new PageInfo<>(list);
		return p;
	}

	@SuppressWarnings("unchecked")
	@Override
	public PageInfo<BizDispatchBillEntity> queryAllCalculate(
			Map<String, Object> condition, int pageNo, int pageSize) {
		List<BizDispatchBillEntity> list=selectList("com.jiuyescm.bms.biz.dispatch.mapper.BizDispatchBillMapper.queryAllCalculate", condition,new RowBounds(pageNo,pageSize));
		PageInfo<BizDispatchBillEntity> p=new PageInfo<>(list);
		return p;
	}
	
	@Override
	public Properties validRetry(Map<String, Object> param) {
		Properties ret = new Properties();
		try{
			if(param.get("merchantId")==null || param.get("merchantId")==""){
				int num=(Integer)selectOne("com.jiuyescm.bms.biz.dispatch.mapper.BizDispatchBillMapper.queryCount", param);			
				SystemCodeTypeEntity system=(SystemCodeTypeEntity) selectOne("com.jiuyescm.bms.base.dictionary.SystemCodeTypeMapper.findByTypeCode", "RETYR_COUNT");
				if(system!=null && system.getTypeDesc()!=null){
					int systemNum=Integer.parseInt(system.getTypeDesc());
					if(num>systemNum){
						ret.setProperty("key", "Over");
						ret.setProperty("value", "超过系统设定的重算量,建议减少重算量后重试");//超过系统设定的重算量,建议减少重算量后重试
						return ret;
					}
				}
			}
			Object waybillno = selectOne("com.jiuyescm.bms.biz.dispatch.mapper.BizDispatchBillMapper.validBillForRetry", param);
			if(waybillno != null){
				ret.setProperty("key", "Billed");
				ret.setProperty("value", "运单号【"+waybillno+"】已在账单中存在,不能重算,建议删除账单后重试");//已在账单中存在,不能重算,建议删除账单后重试;
				return ret;
			}
			
			waybillno = selectOne("com.jiuyescm.bms.biz.dispatch.mapper.BizDispatchBillMapper.validCalcuForRetry", param);
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
			update("com.jiuyescm.bms.biz.dispatch.mapper.BizDispatchBillMapper.retryForCalcu", param);
			return 1;
		}
		catch(Exception ex){
			return 0;
		}
	}

	@Override
	public int saveList(List<BizDispatchBillEntity> list) {
		return insertBatch("com.jiuyescm.bms.biz.dispatch.mapper.BizDispatchBillMapper.insertBizList", list);
	}

	@Override
	public List<BizDispatchBillEntity> queryBizData(List<String> aCondition) {
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("waybillnos", aCondition);
		return this.selectList("com.jiuyescm.bms.biz.dispatch.mapper.BizDispatchBillMapper.queryBizData", map);
	}
	
	@Override
	public BizDispatchBillEntity queryByWayNo(Map<String, String> aCondition) {
		List<BizDispatchBillEntity> list = selectList("com.jiuyescm.bms.biz.dispatch.mapper.BizDispatchBillMapper.queryByWayNo", aCondition);
		return list.size() > 0?list.get(0):null;
	}

	@Override
	public BizDispatchBillEntity queryExceptionOne(Map<String, Object> condition) {
		return (BizDispatchBillEntity) selectOne("com.jiuyescm.bms.biz.dispatch.mapper.BizDispatchBillMapper.queryExceptionOne", condition);
	}

	
	@Override
	public int queryDispatch(Map<String, Object> param) {
		return (Integer)selectOne("com.jiuyescm.bms.biz.dispatch.mapper.BizDispatchBillMapper.queryDispatch", param);
	}

	@Override
	public int updateBatchWeight(List<Map<String, Object>> list) {
		return updateBatch("com.jiuyescm.bms.biz.dispatch.mapper.BizDispatchBillMapper.updateWeight", list);
	}

	@Override
	public int adjustBillEntity(BizDispatchBillEntity temp) {
		return this.update("com.jiuyescm.bms.biz.dispatch.mapper.BizDispatchBillMapper.adjustBillEntity", temp);
	}

	@Override
	public int retryByWaybillNo(List<String> aCondition) {
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("waybillnos", aCondition);
		return this.update("com.jiuyescm.bms.biz.dispatch.mapper.BizDispatchBillMapper.retryByWaybillNo", map);
	
	}

	@Override
	public List<String> queryWayBillNo(Map<String, Object> condition) {
		return selectList("com.jiuyescm.bms.biz.dispatch.mapper.BizDispatchBillMapper.queryWayBillNo", condition);
	}

	@Override
	public int retryByMaterialMark(Map<String, Object> condition) {
		return this.update("com.jiuyescm.bms.biz.dispatch.mapper.BizDispatchBillMapper.retryByMaterialMark", condition);
	}

	@Override
	public List<BizDispatchBillEntity> queryNotCalculate(
			Map<String, Object> condition) {
		return selectList("com.jiuyescm.bms.biz.dispatch.mapper.BizDispatchBillMapper.queryNotCalculate", condition);
	}
	
	@Override
	public List<BizDispatchBillEntity> queryBizCustomerid(Map<String, Object> condition) {
		List<BizDispatchBillEntity> list = this.selectList("com.jiuyescm.bms.biz.dispatch.mapper.BizDispatchBillMapper.queryBizCustomerid", condition);
		return list;
	}
}