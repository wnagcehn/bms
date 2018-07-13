/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.fees.abnormal.repository.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import com.jiuyescm.bms.bill.receive.entity.BmsBillInfoEntity;
import com.jiuyescm.bms.bill.receive.entity.BmsBillSubjectInfoEntity;
import com.jiuyescm.bms.fees.abnormal.entity.FeesAbnormalEntity;
import com.jiuyescm.bms.fees.abnormal.repository.IFeesAbnormalRepository;
import com.jiuyescm.bms.fees.bill.out.entity.FeesPayBillEntity;
import com.jiuyescm.bms.fees.entity.FeesBillEntity;
import com.jiuyescm.bms.fees.entity.FeesBillWareHouseEntity;
import com.jiuyescm.cfm.persistence.mybatis.MyBatisDao;

/**
 * 
 * @author stevenl
 * 
 */
@SuppressWarnings("rawtypes")
@Repository("feesAbnormalRepository")
public class FeesAbnormalRepositoryImpl extends MyBatisDao implements IFeesAbnormalRepository {

	private static final Logger logger = Logger.getLogger(FeesAbnormalRepositoryImpl.class.getName());

	public FeesAbnormalRepositoryImpl() {
		super();
	}
	
	@SuppressWarnings("unchecked")
	@Override
    public PageInfo<FeesAbnormalEntity> query(Map<String, Object> condition, int pageNo, int pageSize) {
        List<FeesAbnormalEntity> list = selectList("com.jiuyescm.bms.fees.abnormal.FeesAbnormalEntityMapper.query", condition, new RowBounds(
                pageNo, pageSize));
        if(condition!=null && null!=condition.get("payment")){
        	if (null!=list&&list.size()>0) 
            {
               Double d = queryTotalPay(condition);
               if(d==null){
            	   d = 0d;
               }
               d = d*-1;
               for(FeesAbnormalEntity entity:list){
            	   if(null!=entity.getPayMoney()){
            		   entity.setPayMoney(entity.getPayMoney()*-1);
            	   }
            	   entity.setTotalPay(d);
               }
            	   
    		}
        }else{
        	if (null!=list&&list.size()>0) 
            {
               Double d = queryTotalPay(condition);
               if(d==null){
            	   d = 0d;
               }
               for(FeesAbnormalEntity entity:list)
            	   entity.setTotalPay(d);
    		}
        }
        
        PageInfo<FeesAbnormalEntity> pageInfo = new PageInfo<FeesAbnormalEntity>(list);
        return pageInfo;
    }
	
	@Override
	@SuppressWarnings("unchecked")
	public List<FeesAbnormalEntity> queryByFeesNos(Map<String, Object> param) {
		List<FeesAbnormalEntity> resultList = selectList("com.jiuyescm.bms.fees.abnormal.FeesAbnormalEntityMapper.queryByFeesNos", param);
		return resultList;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public PageInfo<FeesAbnormalEntity> queryAll(Map<String, Object> param,
            int pageNo, int pageSize) {
		List<FeesAbnormalEntity> list = selectList("com.jiuyescm.bms.fees.abnormal.FeesAbnormalEntityMapper.queryAll", param, new RowBounds(pageNo, pageSize));
		return new PageInfo<FeesAbnormalEntity>(list);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public PageInfo<FeesAbnormalEntity> queryPayAll(Map<String, Object> param,
			int pageNo, int pageSize) {
		List<FeesAbnormalEntity> list = selectList("com.jiuyescm.bms.fees.abnormal.FeesAbnormalEntityMapper.queryPayAll", param, new RowBounds(pageNo, pageSize));
		return new PageInfo<FeesAbnormalEntity>(list);
	}

    @Override
    public FeesAbnormalEntity findById(Long id) {
        FeesAbnormalEntity entity = (FeesAbnormalEntity) selectOne("com.jiuyescm.bms.fees.abnormal.FeesAbnormalEntityMapper.findById", id);
        return entity;
    }

    @SuppressWarnings("unchecked")
	@Override
    public FeesAbnormalEntity save(FeesAbnormalEntity entity) {
        insert("com.jiuyescm.bms.fees.abnormal.FeesAbnormalEntityMapper.save", entity);
        return entity;
    }

    @SuppressWarnings("unchecked")
	@Override
    public int update(FeesAbnormalEntity entity) {
       return  update("com.jiuyescm.bms.fees.abnormal.FeesAbnormalEntityMapper.update", entity);
    }

    @Override
    public void delete(Long id) {
        delete("com.jiuyescm.bms.fees.abnormal.FeesAbnormalEntityMapper.delete", id);
    }

	@Override
	public List<FeesBillWareHouseEntity> querywarehouseAbnormalAmount(String billNo) {
		SqlSession session = getSqlSessionTemplate();
		Map<String,Object> parameter=new HashMap<String,Object>();
		parameter.put("billNo", billNo);
		return session.selectList("com.jiuyescm.bms.fees.abnormal.FeesAbnormalEntityMapper.querywarehouseAbnormalAmount", parameter);
	}

	@SuppressWarnings("unchecked")
	@Override
	public PageInfo<FeesAbnormalEntity> queryAbnormalDetailGroupPage(
			FeesBillWareHouseEntity queryEntity, int pageNo, int pageSize) {
		 Map<String,Object> parameter=new HashMap<String,Object>();
		 parameter.put("billNo", queryEntity.getBillNo());
		 parameter.put("reasonId", queryEntity.getReasonId());
		 parameter.put("deliverid", queryEntity.getDeliveryid());
		 if(StringUtils.isNotBlank(queryEntity.getFeesNo())){
			 parameter.put("feeNo", queryEntity.getFeesNo());
		 }
		 if (StringUtils.isNotBlank(queryEntity.getWaybillNo())) {
			 parameter.put("expressnum", queryEntity.getWaybillNo());
		 }
		 List<FeesAbnormalEntity> list = selectList("com.jiuyescm.bms.fees.abnormal.FeesAbnormalEntityMapper.queryAbnormalDetailByWarehouseidAndBillNo",
				 parameter, new RowBounds(pageNo, pageSize));
		 return new PageInfo<FeesAbnormalEntity>(list);
	}

	@SuppressWarnings("unchecked")
	@Override
	public int updateBatchBillNo(List<FeesAbnormalEntity> abnormalList) {
		return updateBatch("com.jiuyescm.bms.fees.abnormal.FeesAbnormalEntityMapper.updateBillNo", abnormalList);
	}

	@Override
	public void confirmFeesBill(FeesBillEntity entity) {
		SqlSession session = getSqlSessionTemplate();
		Map<String,Object> parameter=new HashMap<String,Object>();
		parameter.put("billNo", entity.getBillno());
		parameter.put("status", entity.getBillstatus());
		session.update("com.jiuyescm.bms.fees.abnormal.FeesAbnormalEntityMapper.confirmFeesBill", parameter);
	}

	@Override
	public int deleteFeesBill(FeesBillEntity entity) {
		SqlSession session = getSqlSessionTemplate();
		Map<String,Object> parameter=new HashMap<String,Object>();
		parameter.put("billNo", entity.getBillno());
		parameter.put("status", entity.getBillstatus());
		parameter.put("feesNo", entity.getFeesNo());
		return session.update("com.jiuyescm.bms.fees.abnormal.FeesAbnormalEntityMapper.deleteFeesBill", parameter);
	}
	
	@Override
	public int confirmFeesBill(FeesPayBillEntity entity) {
		SqlSession session = getSqlSessionTemplate();
		Map<String,Object> parameter=new HashMap<String,Object>();
		parameter.put("billNo", entity.getBillNo());
		parameter.put("status", entity.getStatus());
		return session.update("com.jiuyescm.bms.fees.abnormal.FeesAbnormalEntityMapper.confirmFeesBill", parameter);
	}
	
	@Override
	public int deleteFeesBill(FeesPayBillEntity entity) {
		SqlSession session = getSqlSessionTemplate();
		Map<String,Object> parameter=new HashMap<String,Object>();
		parameter.put("billNo", entity.getBillNo());
		parameter.put("status", entity.getStatus());
		parameter.put("feesNo", entity.getFeesNo());
		parameter.put("deliveryid", entity.getDeliveryid());
		return session.update("com.jiuyescm.bms.fees.abnormal.FeesAbnormalEntityMapper.deleteFeesBill", parameter);
	}

	@Override
	public List<FeesAbnormalEntity> queryabnormalDetailByBillNo(String billno) {
		SqlSession session = getSqlSessionTemplate();
		return session.selectList("com.jiuyescm.bms.fees.abnormal.FeesAbnormalEntityMapper.queryabnormalDetailByBillNo", billno);
	}
	
	@Override
	public PageInfo<FeesAbnormalEntity> queryabnormalDetailByBillNo(String billno,int pageNo,int pageSize) {
		SqlSession session = getSqlSessionTemplate();
		List<FeesAbnormalEntity> list = session.selectList("com.jiuyescm.bms.fees.abnormal.FeesAbnormalEntityMapper.queryabnormalDetailByBillNo",
				billno, new RowBounds(pageNo, pageSize));
		return new PageInfo<FeesAbnormalEntity>(list);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Double queryTotalPay(Map<String, Object> condition) {
		return (Double)super.selectOne("com.jiuyescm.bms.fees.abnormal.FeesAbnormalEntityMapper.queryTotalPay", condition);
	}

	
	@Override
	public int queryCountByFeesBillInfo(FeesBillEntity entity) {
		return selectOneForInt("com.jiuyescm.bms.fees.abnormal.FeesAbnormalEntityMapper.queryCountByFeesBillInfo", entity);
	}
	
	@Override
	public int queryCountByFeesPayBillInfo(FeesPayBillEntity entity) {
		return selectOneForInt("com.jiuyescm.bms.fees.abnormal.FeesAbnormalEntityMapper.queryCountByFeesPayBillInfo", entity);
	}
	
	@Override
	public double queryAmountByFeesBillInfo(FeesBillEntity entity) {
		double sumAmount = 0;
		Object  object= selectOneForObject("com.jiuyescm.bms.fees.abnormal.FeesAbnormalEntityMapper.queryAmountByFeesBillInfo", entity);
		if (null != object) {
			sumAmount = (double)object;
		}
		return sumAmount;
	}
	
	@Override
	public double queryAmountByFeesPayBillInfo(FeesPayBillEntity entity) {
		double sumAmount = 0;
		Object  object= selectOneForObject("com.jiuyescm.bms.fees.abnormal.FeesAbnormalEntityMapper.queryAmountByFeesPayBillInfo", entity);
		if (null != object) {
			sumAmount = (double)object;
		}
		return sumAmount;
	}

	@Override
	public int updateByFeesBillInfo(FeesBillEntity entity) {
		SqlSession session = getSqlSessionTemplate();
		return session.update("com.jiuyescm.bms.fees.abnormal.FeesAbnormalEntityMapper.updateByFeesBillInfo", entity);
	}

	@Override
	public int updateByFeesPayBillInfo(FeesPayBillEntity entity) {
		SqlSession session = getSqlSessionTemplate();
		return session.update("com.jiuyescm.bms.fees.abnormal.FeesAbnormalEntityMapper.updateByFeesPayBillInfo", entity);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<FeesAbnormalEntity> queryCountByFeesPayBillInfoData(Map<String, Object> param) {
		return selectList("com.jiuyescm.bms.fees.abnormal.FeesAbnormalEntityMapper.queryCountByFeesPayBillInfoData", param);
	}

	@Override
	public List<FeesBillWareHouseEntity> queryGroupAbnormalAmountByDeliver(
			String billNo, String deliverid) {
		SqlSession session = getSqlSessionTemplate();
		Map<String,Object> parameter=new HashMap<String,Object>();
		parameter.put("billNo", billNo);
		parameter.put("deliverid", deliverid);
		return session.selectList("com.jiuyescm.bms.fees.abnormal.FeesAbnormalEntityMapper.queryGroupAbnormalAmountByDeliver", parameter);
	}

	@Override
	public PageInfo<FeesAbnormalEntity> queryAbnormalDetailByBillNoAndDeliver(
			FeesPayBillEntity entity, int pageNo, int pageSize) {
		SqlSession session = getSqlSessionTemplate();
		Map<String,Object> parameter=new HashMap<String,Object>();
		parameter.put("billNo",entity.getBillNo() );
		parameter.put("deliverid",entity.getDeliveryid());
		List<FeesAbnormalEntity> list = session.selectList("com.jiuyescm.bms.fees.abnormal.FeesAbnormalEntityMapper.queryAbnormalDetailByBillNoAndDeliver",
				parameter, new RowBounds(pageNo, pageSize));
		return new PageInfo<FeesAbnormalEntity>(list);
	}

	@Override
	public List<FeesBillWareHouseEntity> queryGroupAbnormalAmountSelect(
			String billno, List<String> deliverIdList) {
		SqlSession session = getSqlSessionTemplate();
		Map<String,Object> parameter=new HashMap<String,Object>();
		parameter.put("billNo", billno);
		parameter.put("deliverIdList", deliverIdList);
		return session.selectList("com.jiuyescm.bms.fees.abnormal.FeesAbnormalEntityMapper.queryGroupAbnormalAmountSelect", parameter);
	}

	@Override
	public PageInfo<FeesAbnormalEntity> queryAbnormalDetailBatch(
			List<FeesPayBillEntity> list, int pageNo, int pageSize) {
		List<String> deliverIdList=new ArrayList<String>();
		for(FeesPayBillEntity entity:list){
			deliverIdList.add(entity.getDeliveryid());
		}
		SqlSession session = getSqlSessionTemplate();
		Map<String,Object> parameter=new HashMap<String,Object>();
		parameter.put("billNo",list.get(0).getBillNo() );
		parameter.put("deliverIdList",deliverIdList);
		List<FeesAbnormalEntity> flist = session.selectList("com.jiuyescm.bms.fees.abnormal.FeesAbnormalEntityMapper.queryAbnormalDetailBatch",
				parameter, new RowBounds(pageNo, pageSize));
		return new PageInfo<FeesAbnormalEntity>(flist);
	}

	@Override
	public int queryCountByBillInfo(BmsBillInfoEntity entity) {
		return selectOneForInt("com.jiuyescm.bms.fees.abnormal.FeesAbnormalEntityMapper.queryCountByBillInfo", entity);
	}

	@Override
	public double queryAmountByBillInfo(BmsBillInfoEntity entity) {
		double sumAmount = 0;
		Object  object= selectOneForObject("com.jiuyescm.bms.fees.abnormal.FeesAbnormalEntityMapper.queryAmountByBillInfo", entity);
		if (null != object) {
			sumAmount = (double)object;
		}
		return sumAmount;
	}
	
	/**
	 * 承运商原因
	 * @param entity
	 * @return
	 */
	@Override
	public double queryCYSAmountByBillInfo(BmsBillInfoEntity entity) {
		double sumAmount = 0;
		Object  object= selectOneForObject("com.jiuyescm.bms.fees.abnormal.FeesAbnormalEntityMapper.queryCYSAmountByBillInfo", entity);
		if (null != object) {
			sumAmount = (double)object;
		}
		return sumAmount;
	}

	@Override
	public int updateByBillInfo(BmsBillInfoEntity entity) {
		SqlSession session = getSqlSessionTemplate();
		return session.update("com.jiuyescm.bms.fees.abnormal.FeesAbnormalEntityMapper.updateByBillInfo", entity);
	}

	@Override
	public List<BmsBillSubjectInfoEntity> queryFeesBillSubjectInfo(BmsBillInfoEntity entity) {
		SqlSession session = getSqlSessionTemplate();
		return session.selectList("com.jiuyescm.bms.fees.abnormal.FeesAbnormalEntityMapper.queryFeesBillSubjectInfo", entity);
	}
	
	@Override
	public List<BmsBillSubjectInfoEntity> queryFeesBillSubjectInfoDis(BmsBillInfoEntity entity) {
		SqlSession session = getSqlSessionTemplate();
		return session.selectList("com.jiuyescm.bms.fees.abnormal.FeesAbnormalEntityMapper.queryFeesBillSubjectInfoDis", entity);
	}
	
	@Override
	public List<BmsBillSubjectInfoEntity> queryFeesBillSubjectInfoDisChange(BmsBillInfoEntity entity) {
		SqlSession session = getSqlSessionTemplate();
		return session.selectList("com.jiuyescm.bms.fees.abnormal.FeesAbnormalEntityMapper.queryFeesBillSubjectInfoDisChange", entity);
	}

	@Override
	public PageInfo<FeesAbnormalEntity> queryAbnormalDetailPage(
			Map<String, Object> parameter, int pageNo, int pageSize) {
		SqlSession session = getSqlSessionTemplate();
		List<FeesAbnormalEntity> list = session.selectList("com.jiuyescm.bms.fees.abnormal.FeesAbnormalEntityMapper.queryAbnormalDetailPage",
				parameter, new RowBounds(pageNo, pageSize));
		return new PageInfo<FeesAbnormalEntity>(list);
	}

	@Override
	public int deleteFeesBill(BmsBillInfoEntity entity) {
		SqlSession session = getSqlSessionTemplate();
		Map<String,Object> parameter=new HashMap<String,Object>();
		parameter.put("billNo", entity.getBillNo());
		parameter.put("status", entity.getStatus());
		parameter.put("derateAmount", 0);	
		return session.update("com.jiuyescm.bms.fees.abnormal.FeesAbnormalEntityMapper.deleteFeesBill", parameter);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void deleteBatchFees(List<FeesAbnormalEntity> list) {
		this.updateBatch("com.jiuyescm.bms.fees.abnormal.FeesAbnormalEntityMapper.deleteBatchFees", list);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void derateBatchAmount(List<FeesAbnormalEntity> list) {
		this.updateBatch("com.jiuyescm.bms.fees.abnormal.FeesAbnormalEntityMapper.derateBatchAmount", list);
	}

	@Override
	public void deleteAbnormalBill(String billNo, String warehouseCode,
			String subjectCode) throws Exception {
		List<String> reasonIds=new ArrayList<String>();
		switch(subjectCode){
		case "Abnormal_Dispatch":
			reasonIds.add("2");
			break;
		case "Abnormal_Storage":
			reasonIds.add("1");
			reasonIds.add("56");
			reasonIds.add("3");
			break;
		case "Abnormal_DisChange":
			reasonIds.add("4");
			default:
				throw new Exception("未知的subjectCode【"+subjectCode+"】");
		}
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("billNo", billNo);
		map.put("warehouseCode", warehouseCode);
		map.put("reasonIds", reasonIds);
		SqlSession session = getSqlSessionTemplate();
		session.update("com.jiuyescm.bms.fees.abnormal.FeesAbnormalEntityMapper.deleteAbnormalBill",map);
		
	}

	@Override
	public List<FeesAbnormalEntity> queryAllByBillSubjectInfo(
			BmsBillSubjectInfoEntity bill) throws Exception {
		String subjectCode=bill.getSubjectCode();
		List<String> reasonIds=new ArrayList<String>();
		switch(subjectCode){
		case "Abnormal_Dispatch":
			reasonIds.add("2");
			break;
		case "Abnormal_Storage":
			reasonIds.add("1");
			reasonIds.add("56");
			reasonIds.add("3");
			break;
		case "Abnormal_DisChange":
			reasonIds.add("4");
			default:
				return null;
		}
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("billNo", bill.getBillNo());
		map.put("warehouseCode", bill.getWarehouseCode());
		map.put("reasonIds", reasonIds);
		SqlSession session = getSqlSessionTemplate();
		return session.selectList("com.jiuyescm.bms.fees.abnormal.FeesAbnormalEntityMapper.queryAllByBillSubjectInfo",map);
	}

	@Override
	public List<FeesAbnormalEntity> queryAllByBillSubjectInfoCondition(
			BmsBillSubjectInfoEntity bill, BmsBillInfoEntity billInfoEntity) {
		String subjectCode=bill.getSubjectCode();
		List<String> reasonIds=new ArrayList<String>();
		switch(subjectCode){
		case "Abnormal_Dispatch":
			reasonIds.add("2");
			break;
		case "Abnormal_Storage":
			reasonIds.add("1");
			reasonIds.add("56");
			reasonIds.add("3");
			break;
		case "Abnormal_DisChange":
			reasonIds.add("4");
			break;
			default:
				return null;
		}
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("warehouseCode", bill.getWarehouseCode());
		map.put("reasonIds", reasonIds);
		map.put("customerId", billInfoEntity.getCustomerId());
		map.put("startTime",billInfoEntity.getStartTime());
		map.put("endTime", billInfoEntity.getEndTime());
		SqlSession session = getSqlSessionTemplate();
		return session.selectList("com.jiuyescm.bms.fees.abnormal.FeesAbnormalEntityMapper.queryAllByBillSubjectInfoCondition",map);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void reSetBatchBillNo(List<FeesAbnormalEntity> list) {
		this.updateBatch("com.jiuyescm.bms.fees.abnormal.FeesAbnormalEntityMapper.reSetBatchBillNo", list);
		
	}

	@Override
	public PageInfo<FeesAbnormalEntity> queryAbnormalByBillNo(Map<String, Object> param, int pageNo, int pageSize) {
		BmsBillInfoEntity entity = new BmsBillInfoEntity();
		entity.setBillNo(param.get("billno").toString());
		SqlSession session = getSqlSessionTemplate();
		List<FeesAbnormalEntity> list = session.selectList("com.jiuyescm.bms.fees.abnormal.FeesAbnormalEntityMapper.queryAbnormalNewByBillNo",
				entity, new RowBounds(pageNo, pageSize));
		return new PageInfo<FeesAbnormalEntity>(list);
	}

	@SuppressWarnings("unchecked")
	@Override
	public BmsBillSubjectInfoEntity sumSubjectMoney(Map<String, Object> param) {
		// TODO Auto-generated method stub
		return (BmsBillSubjectInfoEntity) selectOne("com.jiuyescm.bms.fees.abnormal.FeesAbnormalEntityMapper.sumSubjectMoney", param);
	}

	@SuppressWarnings("unchecked")
	@Override
	public int updateFeeByParam(Map<String, Object> param) {
		// TODO Auto-generated method stub
		return update("com.jiuyescm.bms.fees.abnormal.FeesAbnormalEntityMapper.updateFeeByParam",param);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<FeesAbnormalEntity> queryFeeBySubject(Map<String, Object> param) {
		// TODO Auto-generated method stub
		return selectList("com.jiuyescm.bms.fees.abnormal.FeesAbnormalEntityMapper.queryFeeBySubject", param);
	}

	@Override
	public PageInfo<FeesAbnormalEntity> queryAbnormalChangeByBillNo(
			Map<String, Object> param, int pageNo, int pageSize) {
		BmsBillInfoEntity entity = new BmsBillInfoEntity();
		entity.setBillNo(param.get("billno").toString());
		SqlSession session = getSqlSessionTemplate();
		List<FeesAbnormalEntity> list = session.selectList("com.jiuyescm.bms.fees.abnormal.FeesAbnormalEntityMapper.queryAbnormalChangeByBillNo",
				entity, new RowBounds(pageNo, pageSize));
		return new PageInfo<FeesAbnormalEntity>(list);
	}

	@Override
	public FeesAbnormalEntity sumDispatchAmount(String billNo) {
		Map<String,Object> map=Maps.newHashMap();
		map.put("billNo", billNo);
		List<String> reasonIds=new ArrayList<String>();
		reasonIds.add("2");
		map.put("reasonIds", reasonIds);
		return (FeesAbnormalEntity)this.selectOneForObject("com.jiuyescm.bms.fees.abnormal.FeesAbnormalEntityMapper.sumDispatchAmount", map);
	}

	@Override
	public FeesAbnormalEntity sumDispatchChangeAmount(String billNo) {
		Map<String,Object> map=Maps.newHashMap();
		map.put("billNo", billNo);
		List<String> reasonIds=new ArrayList<String>();
		reasonIds.add("4");
		map.put("reasonIds", reasonIds);
		return (FeesAbnormalEntity)this.selectOneForObject("com.jiuyescm.bms.fees.abnormal.FeesAbnormalEntityMapper.sumDispatchAmount", map);
	}

	@Override
	public PageInfo<FeesAbnormalEntity> queryPreBillAbnormal(
			Map<String, Object> param, int pageNo, int pageSize) {
		SqlSession session = getSqlSessionTemplate();
		List<FeesAbnormalEntity> list = session.selectList("com.jiuyescm.bms.fees.abnormal.FeesAbnormalEntityMapper.queryPreBillAbnormal",
				param, new RowBounds(pageNo, pageSize));
		return new PageInfo<FeesAbnormalEntity>(list);
	}

	@Override
	public PageInfo<FeesAbnormalEntity> queryPreBillAbnormalChange(
			Map<String, Object> param, int pageNo, int pageSize) {
		SqlSession session = getSqlSessionTemplate();
		List<FeesAbnormalEntity> list = session.selectList("com.jiuyescm.bms.fees.abnormal.FeesAbnormalEntityMapper.queryPreBillAbnormalChange",
				param, new RowBounds(pageNo, pageSize));
		return new PageInfo<FeesAbnormalEntity>(list);
	}

}
