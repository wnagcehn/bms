package com.jiuyescm.bms.fees.out.dispatch.repository.imp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.fees.bill.out.entity.FeesPayBillEntity;
import com.jiuyescm.bms.fees.entity.FeesBillWareHouseEntity;
import com.jiuyescm.bms.fees.out.dispatch.entity.FeesPayDispatchEntity;
import com.jiuyescm.bms.fees.out.dispatch.repository.IFeesPayDispatchRepository;
import com.jiuyescm.cfm.persistence.mybatis.MyBatisDao;

@Repository("feePayDistributionRepository")
public class FeesPayDispatchRepositoryImpl extends MyBatisDao<FeesPayDispatchEntity> implements IFeesPayDispatchRepository {

	@Override
	public PageInfo<FeesPayDispatchEntity> query(
			Map<String, Object> condition, int pageNo, int pageSize) {
		List<FeesPayDispatchEntity> list = selectList(
				"com.jiuyescm.bms.fees.out.dispatch.mapper.FeesPayDispatchMapper.query",
				condition, new RowBounds(pageNo, pageSize));
		
		Double totalCost = (Double)selectOne("com.jiuyescm.bms.fees.out.dispatch.mapper.FeesPayDispatchMapper.querySum",condition);
		for(FeesPayDispatchEntity entity:list){
			entity.setTotalCost(totalCost);
		}
		return new PageInfo<FeesPayDispatchEntity>(list);
	}

	@Override
	public List<FeesPayDispatchEntity> queryList(Map<String, Object> condition) {
		List<FeesPayDispatchEntity> list = selectList("com.jiuyescm.bms.fees.out.dispatch.mapper.FeesPayDispatchMapper.query",condition);
		return list;
	}
	
	@Override
	public List<FeesBillWareHouseEntity> queryGroupDispatchAmount(String billNo) {
		 SqlSession session = getSqlSessionTemplate();
		 Map<String,Object> parameter=new HashMap<String,Object>();
		 parameter.put("billNo", billNo);
		 return session.selectList("com.jiuyescm.bms.fees.out.dispatch.mapper.FeesPayDispatchMapper.queryGroupDispatchAmount", parameter);
	}

	@Override
	public PageInfo<FeesPayDispatchEntity> queryDispatchDetailGroupPage(
			FeesBillWareHouseEntity queryEntity, int pageNo, int pageSize) {
		Map<String,Object> parameter=new HashMap<String,Object>();
		parameter.put("billNo", queryEntity.getBillNo());
		if (StringUtils.isNotBlank(queryEntity.getDeliveryid())) {
			parameter.put("deliveryid", queryEntity.getDeliveryid());
		}
		parameter.put("feesNo", queryEntity.getFeesNo());
		parameter.put("waybillNo", queryEntity.getWaybillNo());
		List<FeesPayDispatchEntity> list = selectList("com.jiuyescm.bms.fees.out.dispatch.mapper.FeesPayDispatchMapper.queryDispatchDetailGroupPage",
				parameter, new RowBounds(pageNo, pageSize));
		return new PageInfo<FeesPayDispatchEntity>(list);
	}

	@Override
	public int queryCountByFeesBillInfo(FeesPayBillEntity entity) {
		return selectOneForInt("com.jiuyescm.bms.fees.out.dispatch.mapper.FeesPayDispatchMapper.queryCountByFeesBillInfo", entity);
	}

	@Override
	public double queryAmountByFeesBillInfo(FeesPayBillEntity entity) {
		double sumAmount = 0;
		Object  object= selectOneForObject("com.jiuyescm.bms.fees.out.dispatch.mapper.FeesPayDispatchMapper.queryAmountByFeesBillInfo", entity);
		if (null != object) {
			sumAmount = (double)object;
		}
		return sumAmount;
	}

	@Override
	public int updateByFeesBillInfo(FeesPayBillEntity entity) {
		SqlSession session = getSqlSessionTemplate();
		return session.update("com.jiuyescm.bms.fees.out.dispatch.mapper.FeesPayDispatchMapper.updateByFeesBillInfo", entity);
	}

	@Override
	public int updateBatchBillNo(List<FeesPayDispatchEntity> feesDispatchList) {
		return updateBatch("com.jiuyescm.bms.fees.out.dispatch.mapper.FeesPayDispatchMapper.updateBillNo", feesDispatchList);
	}

	@Override
	public int confirmFeesBill(FeesPayBillEntity entity) {
		 SqlSession session = getSqlSessionTemplate();
		 Map<String,Object> parameter=new HashMap<String,Object>();
		 parameter.put("billNo", entity.getBillNo());
		 parameter.put("lastModifier", entity.getLastModifier());
		 parameter.put("lastModifyTime", entity.getLastModifyTime());
		 parameter.put("status", entity.getStatus());
		 return session.update("com.jiuyescm.bms.fees.out.dispatch.mapper.FeesPayDispatchMapper.confirmFeesBill", parameter);
	}

	@Override
	public int deleteFeesBill(FeesPayBillEntity entity) {
		 SqlSession session = getSqlSessionTemplate();
		 Map<String,Object> parameter=new HashMap<String,Object>();
		 parameter.put("billNo", entity.getBillNo());
		 parameter.put("lastModifier", entity.getLastModifier());
		 parameter.put("lastModifyTime", entity.getLastModifyTime());
		 parameter.put("status", entity.getStatus());
		 parameter.put("feesNo", entity.getFeesNo());
		 parameter.put("deliveryid", entity.getDeliveryid());
		 return session.update("com.jiuyescm.bms.fees.out.dispatch.mapper.FeesPayDispatchMapper.deleteFeesBill", parameter);
	}
	
	@Override
	public int deleteFeesBillAbnormal(Map<String, Object> parameter) {
		SqlSession session = getSqlSessionTemplate();
		return session.update("com.jiuyescm.bms.fees.out.dispatch.mapper.FeesPayDispatchMapper.deleteFeesBillAbnormal", parameter);
	}

	@Override
	public List<FeesPayDispatchEntity> queryDispatchDetailByBillNo(String billno) {
		Map<String,Object> parameter=new HashMap<String,Object>();
		parameter.put("billNo", billno);
		return this.selectList("com.jiuyescm.bms.fees.out.dispatch.mapper.FeesPayDispatchMapper.queryDispatchDetailByBillNo", 
				parameter);
	}
	
	@Override
	public PageInfo<FeesPayDispatchEntity> queryDispatchDetailByBillNo(String billno,int pageNo,int pageSize) {
		 Map<String,Object> parameter=new HashMap<String,Object>();
		 parameter.put("billNo", billno);
		 List<FeesPayDispatchEntity> list = this.selectList("com.jiuyescm.bms.fees.out.dispatch.mapper.FeesPayDispatchMapper.queryDispatchDetailByBillNo", 
				 parameter, new RowBounds(pageNo, pageSize));
		return new PageInfo<FeesPayDispatchEntity>(list);
	}

	@Override
	public FeesPayDispatchEntity queryOne(Map<String, Object> condition) {
		// TODO Auto-generated method stub
		return (FeesPayDispatchEntity)this.selectOne("com.jiuyescm.bms.fees.out.dispatch.mapper.FeesPayDispatchMapper.queryOne", condition);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public boolean Insert(FeesPayDispatchEntity entity) {
		try{
			if(this.validFeesNo(entity) == null){ 
				//费用表没有数据 ,执行写入操作
				this.insert("com.jiuyescm.bms.fees.out.dispatch.mapper.FeesPayDispatchMapper.insert", entity);
				
			}else{
				//费用表有数据 ,执行更新操作
				update("com.jiuyescm.bms.fees.out.dispatch.mapper.FeesPayDispatchMapper.updateOne", entity);
			}
			
			return true;
			
		}
		catch(Exception ex){
			return false;
		}
		
	}
	
	@Override
	public int insertBatchTmp(List<FeesPayDispatchEntity> list) {
		return insertBatch("com.jiuyescm.bms.fees.out.dispatch.mapper.FeesPayDispatchMapper.insert", list);
	}
	
	@Override
	public FeesPayDispatchEntity validFeesNo(FeesPayDispatchEntity entity) {
		return (FeesPayDispatchEntity)selectOne("com.jiuyescm.bms.fees.out.dispatch.mapper.FeesPayDispatchMapper.selectOne", entity);
	}

	@Override
	public int deleteFeesByMap(Map<String, Object> condition) {
		// TODO Auto-generated method stub
		return delete("com.jiuyescm.bms.fees.out.dispatch.mapper.FeesPayDispatchMapper.deleteFeesByMap", condition);
	}

	@Override
	public List<FeesBillWareHouseEntity> queryGroupDispatchAmountByDeliver(
			String billno, String deliverid) {
		 SqlSession session = getSqlSessionTemplate();
		 Map<String,Object> parameter=new HashMap<String,Object>();
		 parameter.put("billNo", billno);
		 parameter.put("deliverId",deliverid);
		 return session.selectList("com.jiuyescm.bms.fees.out.dispatch.mapper.FeesPayDispatchMapper.queryGroupDispatchAmountByDeliver", parameter);
	}

	@Override
	public PageInfo<FeesPayDispatchEntity> queryDispatchDetailByBillNoAndDeliver(
			FeesPayBillEntity entity, int pageNo, int pageSize) {
		Map<String,Object> parameter=new HashMap<String,Object>();
		parameter.put("billNo",entity.getBillNo() );
		parameter.put("deliverid",entity.getDeliveryid());
		 List<FeesPayDispatchEntity> list = this.selectList("com.jiuyescm.bms.fees.out.dispatch.mapper.FeesPayDispatchMapper.queryDispatchDetailByBillNoAndDeliver", 
				 parameter, new RowBounds(pageNo, pageSize));
		return new PageInfo<FeesPayDispatchEntity>(list);
	}

	@Override
	public List<FeesBillWareHouseEntity> queryGroupDispatchAmountSelect(
			String billno, List<String> deliverIdList) {
	    SqlSession session = getSqlSessionTemplate();
		Map<String,Object> parameter=new HashMap<String,Object>();
		parameter.put("billNo",billno );
		parameter.put("deliverIdList",deliverIdList);
		return session.selectList("com.jiuyescm.bms.fees.out.dispatch.mapper.FeesPayDispatchMapper.queryGroupDispatchAmountSelect", parameter);
	}

	@Override
	public PageInfo<FeesPayDispatchEntity> queryDispatchDetailBatch(
			List<FeesPayBillEntity> list, int pageNo, int pageSize) {
		List<String> deliverIdList=new ArrayList<String>();
		for(FeesPayBillEntity entity:list){
			deliverIdList.add(entity.getDeliveryid());
		}
		Map<String,Object> parameter=new HashMap<String,Object>();
		parameter.put("billNo",list.get(0).getBillNo() );
		parameter.put("deliverIdList",deliverIdList);
		 List<FeesPayDispatchEntity> paylist = this.selectList("com.jiuyescm.bms.fees.out.dispatch.mapper.FeesPayDispatchMapper.queryDispatchDetailBatch", 
				 parameter, new RowBounds(pageNo, pageSize));
		return new PageInfo<FeesPayDispatchEntity>(paylist);
	}

	@Override
	public boolean InsertOne(FeesPayDispatchEntity entity) {
		// TODO Auto-generated method stub
		try{
			//费用表没有数据 ,执行写入操作
			this.insert("com.jiuyescm.bms.fees.out.dispatch.mapper.FeesPayDispatchMapper.insert", entity);
			return true;
			
		}
		catch(Exception ex){
			return false;
		}
	}

	
}
