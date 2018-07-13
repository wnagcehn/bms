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
import com.jiuyescm.bms.fees.abnormal.entity.FeesPayAbnormalEntity;
import com.jiuyescm.bms.fees.abnormal.repository.IFeesPayAbnormalRepository;
import com.jiuyescm.bms.fees.bill.out.entity.FeesPayBillEntity;
import com.jiuyescm.bms.fees.entity.FeesBillWareHouseEntity;
import com.jiuyescm.cfm.persistence.mybatis.MyBatisDao;

/**
 * 
 * @author stevenl
 * 
 */
@SuppressWarnings("rawtypes")
@Repository("feesPayAbnormalRepository")
public class FeesPayAbnormalRepositoryImpl extends MyBatisDao implements IFeesPayAbnormalRepository {

	private static final Logger logger = Logger.getLogger(FeesPayAbnormalRepositoryImpl.class.getName());

	public FeesPayAbnormalRepositoryImpl() {
		super();
	}
	
	@SuppressWarnings("unchecked")
	@Override
    public PageInfo<FeesPayAbnormalEntity> query(Map<String, Object> condition, int pageNo, int pageSize) {
        List<FeesPayAbnormalEntity> list = selectList("com.jiuyescm.bms.fees.abnormal.FeesPayAbnormalEntityMapper.query", condition, new RowBounds(
                pageNo, pageSize));
        if(condition!=null && null!=condition.get("payment")){
        	if (null!=list&&list.size()>0) 
            {
               Double d = queryTotalPay(condition);
               if(d==null){
            	   d = 0d;
               }
               d = d*-1;
               for(FeesPayAbnormalEntity entity:list){
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
               for(FeesPayAbnormalEntity entity:list)
            	   entity.setTotalPay(d);
    		}
        }
        
        PageInfo<FeesPayAbnormalEntity> pageInfo = new PageInfo<FeesPayAbnormalEntity>(list);
        return pageInfo;
    }
	
	@SuppressWarnings("unchecked")
	@Override
	public PageInfo<FeesPayAbnormalEntity> queryPayAll(Map<String, Object> param,
			int pageNo, int pageSize) {
		List<FeesPayAbnormalEntity> list = selectList("com.jiuyescm.bms.fees.abnormal.FeesPayAbnormalEntityMapper.queryPayAll", param, new RowBounds(pageNo, pageSize));
		return new PageInfo<FeesPayAbnormalEntity>(list);
	}

    @Override
    public FeesPayAbnormalEntity findById(Long id) {
        FeesPayAbnormalEntity entity = (FeesPayAbnormalEntity) selectOne("com.jiuyescm.bms.fees.abnormal.FeesPayAbnormalEntityMapper.findById", id);
        return entity;
    }

    @SuppressWarnings("unchecked")
	@Override
    public FeesPayAbnormalEntity save(FeesPayAbnormalEntity entity) {
        insert("com.jiuyescm.bms.fees.abnormal.FeesPayAbnormalEntityMapper.save", entity);
        return entity;
    }

    @SuppressWarnings("unchecked")
	@Override
    public int update(FeesPayAbnormalEntity entity) {
       return  update("com.jiuyescm.bms.fees.abnormal.FeesPayAbnormalEntityMapper.update", entity);
    }

    @Override
    public void delete(Long id) {
        delete("com.jiuyescm.bms.fees.abnormal.FeesPayAbnormalEntityMapper.delete", id);
    }

	@Override
	public List<FeesBillWareHouseEntity> querywarehouseAbnormalAmount(String billNo) {
		SqlSession session = getSqlSessionTemplate();
		Map<String,Object> parameter=new HashMap<String,Object>();
		parameter.put("billNo", billNo);
		return session.selectList("com.jiuyescm.bms.fees.abnormal.FeesPayAbnormalEntityMapper.querywarehouseAbnormalAmount", parameter);
	}

	@SuppressWarnings("unchecked")
	@Override
	public PageInfo<FeesPayAbnormalEntity> queryAbnormalDetailGroupPage(
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
		 List<FeesPayAbnormalEntity> list = selectList("com.jiuyescm.bms.fees.abnormal.FeesPayAbnormalEntityMapper.queryAbnormalDetailByWarehouseidAndBillNo",
				 parameter, new RowBounds(pageNo, pageSize));
		 return new PageInfo<FeesPayAbnormalEntity>(list);
	}

	@SuppressWarnings("unchecked")
	@Override
	public int updateBatchBillNo(List<FeesPayAbnormalEntity> abnormalList) {
		return updateBatch("com.jiuyescm.bms.fees.abnormal.FeesPayAbnormalEntityMapper.updateBillNo", abnormalList);
	}
	
	@Override
	public int deleteFeesBill(FeesPayBillEntity entity) {
		SqlSession session = getSqlSessionTemplate();
		Map<String,Object> parameter=new HashMap<String,Object>();
		parameter.put("billNo", entity.getBillNo());
		parameter.put("status", entity.getStatus());
		parameter.put("feesNo", entity.getFeesNo());
		parameter.put("deliveryid", entity.getDeliveryid());
		return session.update("com.jiuyescm.bms.fees.abnormal.FeesPayAbnormalEntityMapper.deleteBill", parameter);
	}
	
	@Override
	public PageInfo<FeesPayAbnormalEntity> queryabnormalDetailByBillNo(String billno,int pageNo,int pageSize) {
		SqlSession session = getSqlSessionTemplate();
		List<FeesPayAbnormalEntity> list = session.selectList("com.jiuyescm.bms.fees.abnormal.FeesPayAbnormalEntityMapper.queryabnormalDetailByBillNo",
				billno, new RowBounds(pageNo, pageSize));
		return new PageInfo<FeesPayAbnormalEntity>(list);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Double queryTotalPay(Map<String, Object> condition) {
		return (Double)super.selectOne("com.jiuyescm.bms.fees.abnormal.FeesPayAbnormalEntityMapper.queryTotalPay", condition);
	}
	
	@Override
	public int queryCountByFeesPayBillInfo(FeesPayBillEntity entity) {
		return selectOneForInt("com.jiuyescm.bms.fees.abnormal.FeesPayAbnormalEntityMapper.queryCountByFeesPayBillInfo", entity);
	}
	
	@Override
	public int updateByFeesPayBillInfo(FeesPayBillEntity entity) {
		SqlSession session = getSqlSessionTemplate();
		return session.update("com.jiuyescm.bms.fees.abnormal.FeesPayAbnormalEntityMapper.updateByFeesPayBillInfo", entity);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<FeesPayAbnormalEntity> queryCountByFeesPayBillInfoData(Map<String, Object> param) {
		return selectList("com.jiuyescm.bms.fees.abnormal.FeesPayAbnormalEntityMapper.queryCountByFeesPayBillInfoData", param);
	}

	@Override
	public List<FeesBillWareHouseEntity> queryGroupAbnormalAmountByDeliver(
			String billNo, String deliverid) {
		SqlSession session = getSqlSessionTemplate();
		Map<String,Object> parameter=new HashMap<String,Object>();
		parameter.put("billNo", billNo);
		parameter.put("deliverid", deliverid);
		return session.selectList("com.jiuyescm.bms.fees.abnormal.FeesPayAbnormalEntityMapper.queryGroupAbnormalAmountByDeliver", parameter);
	}

	@Override
	public PageInfo<FeesPayAbnormalEntity> queryAbnormalDetailByBillNoAndDeliver(
			FeesPayBillEntity entity, int pageNo, int pageSize) {
		SqlSession session = getSqlSessionTemplate();
		Map<String,Object> parameter=new HashMap<String,Object>();
		parameter.put("billNo",entity.getBillNo() );
		parameter.put("deliverid",entity.getDeliveryid());
		List<FeesPayAbnormalEntity> list = session.selectList("com.jiuyescm.bms.fees.abnormal.FeesPayAbnormalEntityMapper.queryAbnormalDetailByBillNoAndDeliver",
				parameter, new RowBounds(pageNo, pageSize));
		return new PageInfo<FeesPayAbnormalEntity>(list);
	}

	@Override
	public List<FeesBillWareHouseEntity> queryGroupAbnormalAmountSelect(
			String billno, List<String> deliverIdList) {
		SqlSession session = getSqlSessionTemplate();
		Map<String,Object> parameter=new HashMap<String,Object>();
		parameter.put("billNo", billno);
		parameter.put("deliverIdList", deliverIdList);
		return session.selectList("com.jiuyescm.bms.fees.abnormal.FeesPayAbnormalEntityMapper.queryGroupAbnormalAmountSelect", parameter);
	}

	@Override
	public PageInfo<FeesPayAbnormalEntity> queryAbnormalDetailBatch(
			List<FeesPayBillEntity> list, int pageNo, int pageSize) {
		List<String> deliverIdList=new ArrayList<String>();
		for(FeesPayBillEntity entity:list){
			deliverIdList.add(entity.getDeliveryid());
		}
		SqlSession session = getSqlSessionTemplate();
		Map<String,Object> parameter=new HashMap<String,Object>();
		parameter.put("billNo",list.get(0).getBillNo() );
		parameter.put("deliverIdList",deliverIdList);
		List<FeesPayAbnormalEntity> flist = session.selectList("com.jiuyescm.bms.fees.abnormal.FeesPayAbnormalEntityMapper.queryAbnormalDetailBatch",
				parameter, new RowBounds(pageNo, pageSize));
		return new PageInfo<FeesPayAbnormalEntity>(flist);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void deleteBatchFees(List<FeesPayAbnormalEntity> list) {
		this.updateBatch("com.jiuyescm.bms.fees.abnormal.FeesPayAbnormalEntityMapper.deleteBatchFees", list);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void derateBatchAmount(List<FeesPayAbnormalEntity> list) {
		this.updateBatch("com.jiuyescm.bms.fees.abnormal.FeesPayAbnormalEntityMapper.derateBatchAmount", list);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<FeesPayAbnormalEntity> queryFeeByParam(Map<String, Object> param) {
		return selectList("com.jiuyescm.bms.fees.abnormal.FeesPayAbnormalEntityMapper.queryFeeByParam", param);
	}

}
