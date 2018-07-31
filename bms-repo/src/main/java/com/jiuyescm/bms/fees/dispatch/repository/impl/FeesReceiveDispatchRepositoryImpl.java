package com.jiuyescm.bms.fees.dispatch.repository.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.bill.receive.entity.BmsBillInfoEntity;
import com.jiuyescm.bms.bill.receive.entity.BmsBillSubjectInfoEntity;
import com.jiuyescm.bms.fees.dispatch.entity.FeesReceiveDispatchEntity;
import com.jiuyescm.bms.fees.dispatch.repository.IFeesReceiveDispatchRepository;
import com.jiuyescm.bms.fees.dispatch.vo.FeesReceiveDispatchVo;
import com.jiuyescm.bms.fees.entity.FeesBillEntity;
import com.jiuyescm.bms.fees.entity.FeesBillWareHouseEntity;
import com.jiuyescm.cfm.persistence.mybatis.MyBatisDao;

@SuppressWarnings("rawtypes")
@Repository("feeInDistributionRepository")
public class FeesReceiveDispatchRepositoryImpl extends MyBatisDao implements IFeesReceiveDispatchRepository {

	@SuppressWarnings("unchecked")
	@Override
	public PageInfo<FeesReceiveDispatchEntity> query(
			Map<String, Object> condition, int pageNo, int pageSize) {
		List<FeesReceiveDispatchEntity> list = selectList(
				"com.jiuyescm.bms.fees.dispatch.mapper.FeesReceiveDispatchMapper.query",
				condition, new RowBounds(pageNo, pageSize));
		
		Double totalCost = (Double)selectOne("com.jiuyescm.bms.fees.dispatch.mapper.FeesReceiveDispatchMapper.querySum",condition);
		for(FeesReceiveDispatchEntity entity:list){
			entity.setTotalCost(totalCost);
		}
		
		return new PageInfo<FeesReceiveDispatchEntity>(list);
	}

	@Override
	@SuppressWarnings("unchecked")
	public PageInfo<FeesReceiveDispatchVo> query1(Map<String, Object> condition, int pageNo, int pageSize) {
		// TODO Auto-generated method stub
		List<FeesReceiveDispatchVo> list = selectList("com.jiuyescm.bms.fees.dispatch.mapper.FeesReceiveDispatchMapper.query1",condition, new RowBounds(pageNo, pageSize));
		
		Double totalCost = (Double)selectOne("com.jiuyescm.bms.fees.dispatch.mapper.FeesReceiveDispatchMapper.querySum",condition);
		for(FeesReceiveDispatchVo entity:list){
			entity.setTotalCost(totalCost);
		}
		
		return new PageInfo<FeesReceiveDispatchVo>(list);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<FeesReceiveDispatchEntity> queryList(Map<String, Object> condition) {
		List<FeesReceiveDispatchEntity> list = selectList(
				"com.jiuyescm.bms.fees.dispatch.mapper.FeesReceiveDispatchMapper.query",
				condition);
		return list;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public int updateBatchTmp(List<FeesReceiveDispatchEntity> list) {
		return updateBatch("com.jiuyescm.bms.fees.dispatch.mapper.FeesReceiveDispatchMapper.updateBatchFees", list);
	}

	@Override
	@SuppressWarnings("unchecked")
	public int updateBatchBillNo(
			List<FeesReceiveDispatchEntity> feesDistributionList) {
		
		return this.updateBatch("com.jiuyescm.bms.fees.dispatch.mapper.FeesReceiveDispatchMapper.updateBillNo", feesDistributionList);
	}

	@Override
	public FeesReceiveDispatchEntity queryById(Long id) {
		return (FeesReceiveDispatchEntity) selectOne("com.jiuyescm.bms.fees.dispatch.mapper.FeesReceiveDispatchMapper.queryById", id);
	}

	@Override
	public List<FeesBillWareHouseEntity> querywarehouseDistributionAmount(
			String billNo) {
		 SqlSession session = getSqlSessionTemplate();
		 Map<String,Object> parameter=new HashMap<String,Object>();
		 parameter.put("billNo", billNo);
		 return session.selectList("com.jiuyescm.bms.fees.dispatch.mapper.FeesReceiveDispatchMapper.querywarehouseDistributionAmount", parameter);
	}

	@SuppressWarnings("unchecked")
	@Override
	public PageInfo<FeesReceiveDispatchEntity> queryDispatchDetailGroupPage(
			FeesBillWareHouseEntity queryEntity, int pageNo, int pageSize) {
		Map<String,Object> parameter=new HashMap<String,Object>();
		parameter.put("billNo", queryEntity.getBillNo());
		if (StringUtils.isNotBlank(queryEntity.getCarrierid())) {
			parameter.put("carrierid", queryEntity.getCarrierid());
		}
		parameter.put("feesNo", queryEntity.getFeesNo());
		parameter.put("waybillNo", queryEntity.getWaybillNo());
		List<FeesReceiveDispatchEntity> list = selectList("com.jiuyescm.bms.fees.dispatch.mapper.FeesReceiveDispatchMapper.queryDistributionDetailByWarehouseidAndBillNo",
				parameter, new RowBounds(pageNo, pageSize));
		return new PageInfo<FeesReceiveDispatchEntity>(list);
	}

	@Override
	public void confirmFeesBill(FeesBillEntity entity) {
		 SqlSession session = getSqlSessionTemplate();
		 Map<String,Object> parameter=new HashMap<String,Object>();
		 parameter.put("billNo", entity.getBillno());
		 parameter.put("modperson", entity.getModperson());
		 parameter.put("modtime", entity.getModtime());
		 parameter.put("status", entity.getBillstatus());
		 session.update("com.jiuyescm.bms.fees.dispatch.mapper.FeesReceiveDispatchMapper.confirmFeesBill", parameter);
	}

	@Override
	public int deleteFeesBill(FeesBillEntity entity) {
		 SqlSession session = getSqlSessionTemplate();
		 Map<String,Object> parameter=new HashMap<String,Object>();
		 parameter.put("billNo", entity.getBillno());
		 parameter.put("modperson", entity.getModperson());
		 parameter.put("modtime", entity.getModtime());
		 parameter.put("status", entity.getBillstatus());
		 parameter.put("feesNo", entity.getFeesNo());
		 return session.update("com.jiuyescm.bms.fees.dispatch.mapper.FeesReceiveDispatchMapper.deleteFeesBill", parameter);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<FeesReceiveDispatchEntity> querydistributionDetailByBillNo(
			String billno) {
		Map<String,Object> parameter=new HashMap<String,Object>();
		parameter.put("billNo", billno);
		return selectList("com.jiuyescm.bms.fees.dispatch.mapper.FeesReceiveDispatchMapper.querydistributionDetailByBillNo", parameter);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public PageInfo<FeesReceiveDispatchEntity> querydistributionDetailByBillNo(
			String billno,int pageNo,int pageSize) {
		 Map<String,Object> parameter=new HashMap<String,Object>();
		 parameter.put("billNo", billno);
		 List<FeesReceiveDispatchEntity> list = this.selectList("com.jiuyescm.bms.fees.dispatch.mapper.FeesReceiveDispatchMapper.querydistributionDetailByBillNo", 
				 parameter, new RowBounds(pageNo, pageSize));
		return new PageInfo<FeesReceiveDispatchEntity>(list);
	}

	@SuppressWarnings("unchecked")
	@Override
	public int insertBatchTmp(List<FeesReceiveDispatchEntity> list) {
		return insertBatch("com.jiuyescm.bms.fees.dispatch.mapper.FeesReceiveDispatchMapper.insertBatchTmp", list);
	}

	@SuppressWarnings("unchecked")
	@Override
	public int insertOne(FeesReceiveDispatchEntity entity) {
		return insert("com.jiuyescm.bms.fees.dispatch.mapper.FeesReceiveDispatchMapper.insertBatchTmp", entity);
	}
	
	@Override
	public int queryCountByFeesBillInfo(FeesBillEntity entity) {
		return selectOneForInt("com.jiuyescm.bms.fees.dispatch.mapper.FeesReceiveDispatchMapper.queryCountByFeesBillInfo", entity);
	}
	
	@Override
	public double queryAmountByFeesBillInfo(FeesBillEntity entity) {
		double sumAmount = 0;
		Object  object= selectOneForObject("com.jiuyescm.bms.fees.dispatch.mapper.FeesReceiveDispatchMapper.queryAmountByFeesBillInfo", entity);
		if (null != object) {
			sumAmount = (double)object;
		}
		return sumAmount;
	}

	@Override
	public int updateByFeesBillInfo(FeesBillEntity entity) {
		SqlSession session = getSqlSessionTemplate();
		return session.update("com.jiuyescm.bms.fees.dispatch.mapper.FeesReceiveDispatchMapper.updateByFeesBillInfo", entity);
	}

	@SuppressWarnings("unchecked")
	@Override
	public FeesReceiveDispatchEntity queryOne(Map<String, Object> condition) {
		return (FeesReceiveDispatchEntity)this.selectOne("com.jiuyescm.bms.fees.dispatch.mapper.FeesReceiveDispatchMapper.queryOne", condition);
	}

	@Override
	public int queryCountByBillInfo(BmsBillInfoEntity entity) {
		return selectOneForInt("com.jiuyescm.bms.fees.dispatch.mapper.FeesReceiveDispatchMapper.queryCountByBillInfo", entity);
	}

	@Override
	public double queryAmountByBillInfo(BmsBillInfoEntity entity) {
		double sumAmount = 0;
		Object  object= selectOneForObject("com.jiuyescm.bms.fees.dispatch.mapper.FeesReceiveDispatchMapper.queryAmountByBillInfo", entity);
		if (null != object) {
			sumAmount = (double)object;
		}
		return sumAmount;
	}

	@Override
	public int updateByBillInfo(BmsBillInfoEntity entity) {
		SqlSession session = getSqlSessionTemplate();
		return session.update("com.jiuyescm.bms.fees.dispatch.mapper.FeesReceiveDispatchMapper.updateByBillInfo", entity);
	}

	@Override
	public List<BmsBillSubjectInfoEntity> queryFeesBillSubjectInfo(BmsBillInfoEntity entity) {
		SqlSession session = getSqlSessionTemplate();
		return session.selectList("com.jiuyescm.bms.fees.dispatch.mapper.FeesReceiveDispatchMapper.queryFeesBillSubjectInfo", entity);
	}

	@Override
	public int deleteFeesBill(BmsBillInfoEntity entity) {
		 SqlSession session = getSqlSessionTemplate();
		 Map<String,Object> parameter=new HashMap<String,Object>();
		 parameter.put("billNo", entity.getBillNo());
		 parameter.put("modperson", entity.getLastModifier());
		 parameter.put("modtime", entity.getLastModifyTime());
		 parameter.put("status", entity.getStatus());
		 parameter.put("derateAmount", 0);		 
		 return session.update("com.jiuyescm.bms.fees.dispatch.mapper.FeesReceiveDispatchMapper.deleteFeesBill", parameter);
	}
	
	@Override
	public int deleteFeesBillAbnormal(BmsBillInfoEntity entity) {
		SqlSession session = getSqlSessionTemplate();
		Map<String,Object> parameter=new HashMap<String,Object>();
		parameter.put("billNo", entity.getBillNo());
		return session.update("com.jiuyescm.bms.fees.dispatch.mapper.FeesReceiveDispatchMapper.deleteFeesBillAbnormal", parameter);
	}
	
	@Override
	public int deleteFeesBillAbnormal(Map<String, Object> parameter) {
		SqlSession session = getSqlSessionTemplate();
		return session.update("com.jiuyescm.bms.fees.dispatch.mapper.FeesReceiveDispatchMapper.deleteFeesBillAbnormal", parameter);
	}

	@Override
	public void deleteDispatchBill(String billNo, String warehouseCode,
			String subjectCode) {
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("billNo", billNo);
		map.put("warehouseCode", warehouseCode);
		map.put("subjectCode", subjectCode);
		SqlSession session = getSqlSessionTemplate();
		session.update("com.jiuyescm.bms.fees.dispatch.mapper.FeesReceiveDispatchMapper.deleteDispatchBill", map);
	}

	@Override
	public List<FeesReceiveDispatchEntity> queryAllByBillSubjectCondition(
			BmsBillSubjectInfoEntity bill, BmsBillInfoEntity billInfoEntity) {
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("warehouseCode", bill.getWarehouseCode());
		map.put("subjectCode",bill.getSubjectCode());
		map.put("customerId", billInfoEntity.getCustomerId());
		map.put("startTime",billInfoEntity.getStartTime());
		map.put("endTime", billInfoEntity.getEndTime());
		map.put("billNo", billInfoEntity.getBillNo());
		SqlSession session = getSqlSessionTemplate();
		return session.selectList("com.jiuyescm.bms.fees.dispatch.mapper.FeesReceiveDispatchMapper.queryAllByBillSubjectCondition",map);
	}

	@Override
	public List<FeesReceiveDispatchEntity> queryAllByBillSubjectInfo(
			BmsBillSubjectInfoEntity bill) {
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("warehouseCode", bill.getWarehouseCode());
		map.put("billNo", bill.getBillNo());
		map.put("subjectCode",bill.getSubjectCode());
		SqlSession session = getSqlSessionTemplate();
		return session.selectList("com.jiuyescm.bms.fees.dispatch.mapper.FeesReceiveDispatchMapper.queryAllByBillSubjectInfo",map);
	}

	@Override
	public PageInfo<FeesReceiveDispatchEntity> queryDispatchPage(
			Map<String, Object> parameter, int pageNo, int pageSize) {
		SqlSession session = getSqlSessionTemplate();
		List<FeesReceiveDispatchEntity> list = session.selectList(
				"com.jiuyescm.bms.fees.dispatch.mapper.FeesReceiveDispatchMapper.queryDispatchPage",
				parameter, new RowBounds(pageNo, pageSize));
		
		return new PageInfo<FeesReceiveDispatchEntity>(list);
	}

	@Override
	public void deleteBatchFees(List<FeesReceiveDispatchEntity> list) {
		this.updateBatch("com.jiuyescm.bms.fees.dispatch.mapper.FeesReceiveDispatchMapper.deleteBatchFees", list);
	}

	@Override
	public void derateBatchAmount(List<FeesReceiveDispatchEntity> list) {
		this.updateBatch("com.jiuyescm.bms.fees.dispatch.mapper.FeesReceiveDispatchMapper.derateBatchAmount", list);
	}

	@Override
	public List<FeesReceiveDispatchEntity> queryAllByBillSubjectInfoMap(
			Map<String, Object> parameter) {
		SqlSession session = getSqlSessionTemplate();
		return session.selectList("com.jiuyescm.bms.fees.dispatch.mapper.FeesReceiveDispatchMapper.queryAllByBillSubjectInfo",parameter);
	}

	@SuppressWarnings("unchecked")
	@Override
	public BmsBillSubjectInfoEntity sumSubjectMoney(Map<String, Object> param) {
		// TODO Auto-generated method stub
		return (BmsBillSubjectInfoEntity) selectOne("com.jiuyescm.bms.fees.dispatch.mapper.FeesReceiveDispatchMapper.sumSubjectMoney", param);
	}

	@SuppressWarnings("unchecked")
	@Override
	public int updateFeeByParam(Map<String, Object> param) {
		// TODO Auto-generated method stub
		return update("com.jiuyescm.bms.fees.dispatch.mapper.FeesReceiveDispatchMapper.updateFeeByParam", param);
	}

	@SuppressWarnings("unchecked")
	@Override
	public PageInfo<FeesReceiveDispatchVo> queryFeesImport(Map<String, Object> condition, int pageNo, int pageSize) {
		
		List<FeesReceiveDispatchVo> list = selectList("com.jiuyescm.bms.fees.dispatch.mapper.FeesReceiveDispatchMapper.queryFeesImport",condition, new RowBounds(pageNo, pageSize));
		return new PageInfo<FeesReceiveDispatchVo>(list);
	}

	@Override
	public List<Map<String, Object>> queryGroup(Map<String, Object> param) {
		return this.selectList("com.jiuyescm.bms.fees.dispatch.mapper.FeesReceiveDispatchMapper.queryGroup", param);
	}

	@Override
	public PageInfo<FeesReceiveDispatchEntity> querydistributionDetailByBizData(
			Map<String, Object> condition,int pageNo, int pageSize) {
		List<FeesReceiveDispatchEntity> list=this.selectList("com.jiuyescm.bms.fees.dispatch.mapper.FeesReceiveDispatchMapper.querydistributionDetailByBizData", condition,new RowBounds(pageNo, pageSize));
		PageInfo<FeesReceiveDispatchEntity> pageInfo= new PageInfo<FeesReceiveDispatchEntity>(list);
		return pageInfo;
	}

	@Override
	public int updateBatch(List<FeesReceiveDispatchEntity> list) {
		// TODO Auto-generated method stub
		return this.updateBatch("com.jiuyescm.bms.fees.dispatch.mapper.FeesReceiveDispatchMapper.updateBatch", list);
	}
	
}
