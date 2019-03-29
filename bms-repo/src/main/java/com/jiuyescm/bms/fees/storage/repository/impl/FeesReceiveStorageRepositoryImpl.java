/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.fees.storage.repository.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.bill.receive.entity.BmsBillInfoEntity;
import com.jiuyescm.bms.bill.receive.entity.BmsBillSubjectInfoEntity;
import com.jiuyescm.bms.fees.entity.FeesBillEntity;
import com.jiuyescm.bms.fees.entity.FeesBillWareHouseEntity;
import com.jiuyescm.bms.fees.storage.entity.FeesReceiveStorageEntity;
import com.jiuyescm.bms.fees.storage.repository.IFeesReceiveStorageRepository;
import com.jiuyescm.bms.fees.storage.vo.FeesReceiveMaterial;
import com.jiuyescm.cfm.persistence.mybatis.MyBatisDao;
/**
 * 
 * @author stevenl
 * 
 */
@Repository("feesReceiveStorageRepository")
public class FeesReceiveStorageRepositoryImpl extends MyBatisDao implements IFeesReceiveStorageRepository {

	private static final Logger logger = Logger.getLogger(FeesReceiveStorageRepositoryImpl.class.getName());

	public FeesReceiveStorageRepositoryImpl() {
		super();
	}
	
	@Override
    public PageInfo<FeesReceiveStorageEntity> query(Map<String, Object> condition, int pageNo, int pageSize) {
        List<FeesReceiveStorageEntity> list = selectList("com.jiuyescm.bms.fees.storage.FeesReceiveStorageEntityMapper.query", condition, new RowBounds(
                pageNo, pageSize));
        
        Double totalCost = (Double)selectOne("com.jiuyescm.bms.fees.storage.FeesReceiveStorageEntityMapper.querySum",condition);
       
        for(FeesReceiveStorageEntity entity:list)
        	entity.setTotalCost(totalCost);
        PageInfo<FeesReceiveStorageEntity> pageInfo = new PageInfo<FeesReceiveStorageEntity>(list);
        return pageInfo;
    }

    @Override
    public FeesReceiveStorageEntity findById(Long id) {
        FeesReceiveStorageEntity entity = (FeesReceiveStorageEntity)selectOne("com.jiuyescm.bms.fees.storage.FeesReceiveStorageEntityMapper.findById", id);
        return entity;
    }

    @Override
    public FeesReceiveStorageEntity save(FeesReceiveStorageEntity entity) {
        insert("com.jiuyescm.bms.fees.storage.FeesReceiveStorageEntityMapper.save", entity);
        return entity;
    }

    @Override
    public FeesReceiveStorageEntity update(FeesReceiveStorageEntity entity) {
        update("com.jiuyescm.bms.fees.storage.FeesReceiveStorageEntityMapper.update", entity);
        return entity;
    }

    @Override
    public void delete(Long id) {
        delete("com.jiuyescm.bms.fees.storage.FeesReceiveStorageEntityMapper.delete", id);
    }

	@Override
	public List<FeesReceiveStorageEntity> queryAll(Map<String, Object> parameter) {
		 return selectList("com.jiuyescm.bms.fees.storage.FeesReceiveStorageEntityMapper.query", parameter);
	}

	@Override
	public String getUnitPrice(Map<String, Object> param) {
		SqlSession session = getSqlSessionTemplate();
		List<String> list=session.selectList("com.jiuyescm.bms.fees.storage.FeesReceiveStorageEntityMapper.getUnitPrice", param);
		if (list==null || list.size()==0) {
			return "";
		}else {
			return list.get(0);
		}
	}

	@Override
	public int updateBatchBillNo(List<FeesReceiveStorageEntity> feesStorageList) {
		return this.updateBatch("com.jiuyescm.bms.fees.storage.FeesReceiveStorageEntityMapper.updateBillNo", feesStorageList);
	}

	@Override
	public List<FeesBillWareHouseEntity> querywarehouseStorageAmount(
			String billNo) {
		 SqlSession session = getSqlSessionTemplate();
		 Map<String,Object> parameter=new HashMap<String,Object>();
		 parameter.put("billNo", billNo);
		 return session.selectList("com.jiuyescm.bms.fees.storage.FeesReceiveStorageEntityMapper.querywarehouseStorageAmount",parameter);
	}

	
	@Override
	public PageInfo<FeesReceiveStorageEntity> queryStorageDetailGroupPage(
			FeesBillWareHouseEntity queryEntity, int pageNo, int pageSize) {
		List<FeesReceiveStorageEntity> list = selectList("com.jiuyescm.bms.fees.storage.FeesReceiveStorageEntityMapper.queryStorageDetailGroupPage",
			queryEntity, new RowBounds(pageNo, pageSize));
		return new PageInfo<FeesReceiveStorageEntity>(list);
	}

	/**
	 * 结算账单
	 */
	@Override
	public void confirmFeesBill(FeesBillEntity entity) {
		 SqlSession session = getSqlSessionTemplate();
		 Map<String,Object> parameter=new HashMap<String,Object>();
		 parameter.put("billNo", entity.getBillno());
		 parameter.put("modperson", entity.getModperson());
		 parameter.put("modtime", entity.getModtime());
		 parameter.put("status",String.valueOf(entity.getBillstatus()));
		 session.update("com.jiuyescm.bms.fees.storage.FeesReceiveStorageEntityMapper.confirmFeesBill", parameter);
		
	}
	/**
	 * 删除账单
	 */
	@Override
	public int deleteFeesBill(FeesBillEntity entity) {
		 SqlSession session = getSqlSessionTemplate();
		 Map<String,Object> parameter=new HashMap<String,Object>();
		 parameter.put("billNo", entity.getBillno());
		 parameter.put("modperson", entity.getModperson());
		 parameter.put("modtime", entity.getModtime());
		 parameter.put("status",String.valueOf(entity.getBillstatus()));
		 parameter.put("feesNo", entity.getFeesNo());
		 return session.update("com.jiuyescm.bms.fees.storage.FeesReceiveStorageEntityMapper.deleteFeesBill", parameter);
	}

	@Override
	public PageInfo<FeesReceiveStorageEntity> querystorageDetailByBillNo(
			String billno,int pageNo,int pageSize) {
		 Map<String,Object> parameter=new HashMap<String,Object>();
		 parameter.put("billNo", billno);
		 List<FeesReceiveStorageEntity> list = this.selectList("com.jiuyescm.bms.fees.storage.FeesReceiveStorageEntityMapper.querystorageDetailByBillNo", 
				 parameter, new RowBounds(pageNo, pageSize));
		 return new PageInfo<FeesReceiveStorageEntity>(list);
	}
	
	@Override
	public PageInfo<FeesReceiveStorageEntity> queryStorageByBillNoSubjectId(
			String billno,String subjectId,int pageNo,int pageSize) {
		Map<String,Object> parameter=new HashMap<String,Object>();
		parameter.put("billNo", billno);
		parameter.put("subjectId", subjectId);
		List<FeesReceiveStorageEntity> list = this.selectList("com.jiuyescm.bms.fees.storage.FeesReceiveStorageEntityMapper.queryStorageByBillNoSubjectId", 
				parameter, new RowBounds(pageNo, pageSize));
		return new PageInfo<FeesReceiveStorageEntity>(list);
	}

	@Override
	public int queryCountByFeesBillInfo(FeesBillEntity entity) {
		return selectOneForInt("com.jiuyescm.bms.fees.storage.FeesReceiveStorageEntityMapper.queryCountByFeesBillInfo", entity);
	}
	
	@Override
	public double queryAmountByFeesBillInfo(FeesBillEntity entity) {
		double sumAmount = 0;
		Object  object= selectOneForObject("com.jiuyescm.bms.fees.storage.FeesReceiveStorageEntityMapper.queryAmountByFeesBillInfo", entity);
		if (null != object) {
			sumAmount = (double)object;
		}
		return sumAmount;
	}
	
	@Override
	public int updateByFeesBillInfo(FeesBillEntity entity) {
		SqlSession session = getSqlSessionTemplate();
		return session.update("com.jiuyescm.bms.fees.storage.FeesReceiveStorageEntityMapper.updateByFeesBillInfo", entity);
	}

	@Override
	public void insertBatch(List<FeesReceiveStorageEntity> list)throws Exception {
		super.insertBatch("com.jiuyescm.bms.fees.storage.FeesReceiveStorageEntityMapper.save", list);
		
	}

	@Override
	public PageInfo<FeesReceiveStorageEntity> queryPackMaterialByBillNo(String billno,int pageNo,int pageSize) {
		 Map<String,Object> parameter=new HashMap<String,Object>();
		 parameter.put("billNo", billno);
		 List<FeesReceiveStorageEntity> list = this.selectList("com.jiuyescm.bms.fees.storage.FeesReceiveStorageEntityMapper.queryPackMaterialByBillNo",
				 parameter, new RowBounds(pageNo, pageSize));
		 return new PageInfo<FeesReceiveStorageEntity>(list);
	}

	@Override
	public PageInfo<FeesReceiveStorageEntity> queryFees(
			Map<String, Object> condition, int pageNo, int pageSize) {
		// TODO Auto-generated method stub
		 List<FeesReceiveStorageEntity> list = selectList("com.jiuyescm.bms.fees.storage.FeesReceiveStorageEntityMapper.query", condition, new RowBounds(
	                pageNo, pageSize));
	     PageInfo<FeesReceiveStorageEntity> pageInfo = new PageInfo<FeesReceiveStorageEntity>(list);
		return pageInfo;
	}


	@Override
	public int deleteEntity(String feesNo) {
		return delete("com.jiuyescm.bms.fees.storage.FeesReceiveStorageEntityMapper.deleteEntity",feesNo);
	}

	@Override
	public List<FeesReceiveStorageEntity> queryAllByBillSubject(
			BmsBillSubjectInfoEntity bill) {
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("billno", bill.getBillNo());
		map.put("subjectCode", bill.getSubjectCode());
		return this.selectList("com.jiuyescm.bms.fees.storage.FeesReceiveStorageEntityMapper.queryAllByBillSubject", map);
	}

	@Override
	public void updateDiscountAmountBatch(
			List<FeesReceiveStorageEntity> feesList) {
		this.updateBatch("com.jiuyescm.bms.fees.storage.FeesReceiveStorageEntityMapper.updateDiscountAmountBatch", feesList);
	}

	

	@Override
	public PageInfo<FeesReceiveStorageEntity> queryOutStockPage(
			Map<String, Object> parameter, int pageNo, int pageSize) {
		// TODO Auto-generated method stub
		List<FeesReceiveStorageEntity> list = selectList("com.jiuyescm.bms.fees.storage.FeesReceiveStorageEntityMapper.queryOutStockPage", parameter, new RowBounds(
                pageNo, pageSize));
		PageInfo<FeesReceiveStorageEntity> pageInfo = new PageInfo<FeesReceiveStorageEntity>(list);
		return pageInfo;
	}
	
	@Override
	public PageInfo<FeesReceiveStorageEntity> queryStoragePage(
			Map<String, Object> parameter, int pageNo, int pageSize) {
		List<FeesReceiveStorageEntity> list = selectList("com.jiuyescm.bms.fees.storage.FeesReceiveStorageEntityMapper.queryStoragePage", parameter, new RowBounds(
                pageNo, pageSize));
		PageInfo<FeesReceiveStorageEntity> pageInfo = new PageInfo<FeesReceiveStorageEntity>(list);
		return pageInfo;
	}

	@Override
	public PageInfo<FeesReceiveStorageEntity> queryMaterialPage(
			Map<String, Object> parameter, int pageNo, int pageSize) {
		List<FeesReceiveStorageEntity> list = selectList("com.jiuyescm.bms.fees.storage.FeesReceiveStorageEntityMapper.queryMaterialPage", parameter, new RowBounds(
                pageNo, pageSize));
		PageInfo<FeesReceiveStorageEntity> pageInfo = new PageInfo<FeesReceiveStorageEntity>(list);
		return pageInfo;
	}
	
	@Override
	public PageInfo<FeesReceiveStorageEntity> queryStorageAddFeePage(
			Map<String, Object> parameter, int pageNo, int pageSize) {
		List<FeesReceiveStorageEntity> list = selectList("com.jiuyescm.bms.fees.storage.FeesReceiveStorageEntityMapper.queryStorageAddFeePage", parameter, new RowBounds(
                pageNo, pageSize));
		PageInfo<FeesReceiveStorageEntity> pageInfo = new PageInfo<FeesReceiveStorageEntity>(list);
		return pageInfo;
	}
	
	@Override
	public PageInfo<FeesReceiveStorageEntity> queryPreBillStorageAddFee(
			Map<String, Object> parameter, int pageNo, int pageSize) {
		List<FeesReceiveStorageEntity> list = selectList("com.jiuyescm.bms.fees.storage.FeesReceiveStorageEntityMapper.queryPreBillStorageAddFee", parameter, new RowBounds(
				pageNo, pageSize));
		PageInfo<FeesReceiveStorageEntity> pageInfo = new PageInfo<FeesReceiveStorageEntity>(list);
		return pageInfo;
	}
	
	@Override
	public int queryCountByBillInfo(BmsBillInfoEntity entity) {
		return selectOneForInt("com.jiuyescm.bms.fees.storage.FeesReceiveStorageEntityMapper.queryCountByBillInfo", entity);
	}

	@Override
	public double queryAmountByBillInfo(BmsBillInfoEntity entity) {
		double sumAmount = 0;
		Object  object= selectOneForObject("com.jiuyescm.bms.fees.storage.FeesReceiveStorageEntityMapper.queryAmountByBillInfo", entity);
		if (null != object) {
			sumAmount = (double)object;
		}
		return sumAmount;
	}

	@Override
	public int updateByBillInfo(BmsBillInfoEntity entity) {
		SqlSession session = getSqlSessionTemplate();
		return session.update("com.jiuyescm.bms.fees.storage.FeesReceiveStorageEntityMapper.updateByBillInfo", entity);
	}

	@Override
	public List<BmsBillSubjectInfoEntity> queryFeesBillSubjectInfo(BmsBillInfoEntity entity) {
		SqlSession session = getSqlSessionTemplate();
		return session.selectList("com.jiuyescm.bms.fees.storage.FeesReceiveStorageEntityMapper.queryFeesBillSubjectInfo", entity);
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
		 return session.update("com.jiuyescm.bms.fees.storage.FeesReceiveStorageEntityMapper.deleteFeesBill", parameter);
	}
	
	@Override
	public int deleteFeesBillAbnormal(BmsBillInfoEntity entity) {
		SqlSession session = getSqlSessionTemplate();
		Map<String,Object> parameter=new HashMap<String,Object>();
		parameter.put("billNo", entity.getBillNo());
		return session.update("com.jiuyescm.bms.fees.storage.FeesReceiveStorageEntityMapper.deleteFeesBillAbnormal", parameter);
	}
	
	@Override
	public int deleteFeesBillAbnormal(Map<String, Object> parameter) {
		SqlSession session = getSqlSessionTemplate();
		return session.update("com.jiuyescm.bms.fees.storage.FeesReceiveStorageEntityMapper.deleteFeesBillAbnormal", parameter);
	}

	@Override
	public void deleteStorageBill(String billNo, String warehouseCode,
			String feesType) {
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("warehouseCode", warehouseCode);
		map.put("billNo", billNo);
		map.put("subjectCode", feesType);
		SqlSession session = getSqlSessionTemplate();
		session.update("com.jiuyescm.bms.fees.storage.FeesReceiveStorageEntityMapper.deleteStorageBill",map);
	}

	@Override
	public List<FeesReceiveStorageEntity> queryAllByBillSubjectCondition(
			BmsBillSubjectInfoEntity bill,BmsBillInfoEntity billInfoEntity) {
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("warehouseCode", bill.getWarehouseCode());
		map.put("billNo", bill.getBillNo());
		map.put("subjectCode",bill.getSubjectCode());
		map.put("customerId", billInfoEntity.getCustomerId());
		map.put("startTime",billInfoEntity.getStartTime());
		map.put("endTime", billInfoEntity.getEndTime());
		SqlSession session = getSqlSessionTemplate();
		return session.selectList("com.jiuyescm.bms.fees.storage.FeesReceiveStorageEntityMapper.queryAllByBillSubjectCondition",map);
	}

	@Override
	public int updateBatchBillNoById(List<FeesReceiveStorageEntity> list) {
		return this.updateBatch("com.jiuyescm.bms.fees.storage.FeesReceiveStorageEntityMapper.updateBillNoById", list);
	}

	@Override
	public List<FeesReceiveStorageEntity> queryAllByBillSubjectInfo(
			BmsBillSubjectInfoEntity bill, BmsBillInfoEntity billInfoEntity) {
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("warehouseCode", bill.getWarehouseCode());
		map.put("billNo", bill.getBillNo());
		map.put("subjectCode",bill.getSubjectCode());
		SqlSession session = getSqlSessionTemplate();
		return session.selectList("com.jiuyescm.bms.fees.storage.FeesReceiveStorageEntityMapper.queryAllByBillSubjectInfo",map);
	}
	
	public int updateBatch(List<FeesReceiveStorageEntity> list){
		return updateBatch("com.jiuyescm.bms.fees.storage.FeesReceiveStorageEntityMapper.update", list);
	}

	@Override
	public void derateBatchAmount(List<FeesReceiveStorageEntity> list) {
		this.updateBatch("com.jiuyescm.bms.fees.storage.FeesReceiveStorageEntityMapper.derateBatchAmount", list);
	}

	@Override
	public void deleteBatchFees(List<FeesReceiveStorageEntity> list) {
		this.updateBatch("com.jiuyescm.bms.fees.storage.FeesReceiveStorageEntityMapper.deleteBatchFees", list);
	}

	@Override
	public List<FeesReceiveStorageEntity> queryFeesData(Map<String, Object> param) {
		return selectList("com.jiuyescm.bms.fees.storage.FeesReceiveStorageEntityMapper.queryStorageByWarehouse",param);
	}

	@Override
	public PageInfo<FeesReceiveMaterial> queryNewMaterialByBillNo(
			Map<String, Object> param, int pageNo, int pageSize) {
		 List<FeesReceiveMaterial> list = this.selectList("com.jiuyescm.bms.fees.storage.FeesReceiveStorageEntityMapper.queryFeeMaterialByWarehouse",
				 param, new RowBounds(pageNo, pageSize));
		 return new PageInfo<FeesReceiveMaterial>(list);
	}

	@Override
	public List<FeesReceiveStorageEntity> queryStorageAdd(Map<String, Object> param) {
		return selectList("com.jiuyescm.bms.fees.storage.FeesReceiveStorageEntityMapper.queryFeeAdd",param);
	}

	@Override
	public BmsBillSubjectInfoEntity sumSubjectMoney(Map<String, Object> param) {
		// TODO Auto-generated method stub
		return (BmsBillSubjectInfoEntity) selectOne("com.jiuyescm.bms.fees.storage.FeesReceiveStorageEntityMapper.sumSubjectMoney", param);
	}

	@Override
	public int updateFeeByParam(Map<String, Object> param) {
		// TODO Auto-generated method stub
		return update("com.jiuyescm.bms.fees.storage.FeesReceiveStorageEntityMapper.updateFeeByParam", param);
	}

	@Override
	public List<Map<String, Object>> queryGroup(Map<String, Object> param) {
		return this.selectList("com.jiuyescm.bms.fees.storage.FeesReceiveStorageEntityMapper.queryGroup", param);
	}

	@Override
	public int updateByFeeNoList(Map<String, Object> condition) {
		// TODO Auto-generated method stub
		return update("com.jiuyescm.bms.fees.storage.FeesReceiveStorageEntityMapper.updateByFeeNoList", condition);
	}

	@Override
	public List<FeesReceiveStorageEntity> queryBizFeesData(
			Map<String, Object> condition) {
		return this.selectList("com.jiuyescm.bms.fees.storage.FeesReceiveStorageEntityMapper.queryBizFeesData", condition);
	}

	@Override
	public int deleteBatch(Map<String, Object> feesNos) {
		return this.delete("com.jiuyescm.bms.fees.storage.FeesReceiveStorageEntityMapper.deleteBatch", feesNos);
	}

	@Override
	public int deleteMaterialFee(Map<String, Object> parameter) {
		// TODO Auto-generated method stub
		return delete("com.jiuyescm.bms.fees.storage.FeesReceiveStorageEntityMapper.deleteMaterialFee",parameter);
	}
	

	@Override
	public int deletePmxFee(Map<String, Object> parameter) {
		// TODO Auto-generated method stub
		return delete("com.jiuyescm.bms.fees.storage.FeesReceiveStorageEntityMapper.deletePmxFee",parameter);
	}

	@Override
	public int deleteZxFee(Map<String, Object> parameter) {
		// TODO Auto-generated method stub
		return delete("com.jiuyescm.bms.fees.storage.FeesReceiveStorageEntityMapper.deleteZxFee",parameter);
	}
	
	@Override
	public int deleteBwdFee(Map<String, Object> parameter) {
		// TODO Auto-generated method stub
		return delete("com.jiuyescm.bms.fees.storage.FeesReceiveStorageEntityMapper.deleteBwdFee",parameter);
	}
	
	@Override
	public List<String> queryPreBillWarehouse(Map<String, Object> condition) {
		return (List<String>)selectList("com.jiuyescm.bms.fees.storage.FeesReceiveStorageEntityMapper.queryPreBillWarehouse", condition);
	}

	@Override
	public List<FeesReceiveStorageEntity> queryPreBillStorage(Map<String, Object> parameter) {
		return selectList("com.jiuyescm.bms.fees.storage.FeesReceiveStorageEntityMapper.queryPreBillStorage", parameter);
	}
	
	@Override
	public List<FeesReceiveStorageEntity> queryPreBillStorageByItems(Map<String, Object> parameter) {
		return selectList("com.jiuyescm.bms.fees.storage.FeesReceiveStorageEntityMapper.queryPreBillStorageByItems", parameter);
	}
	
	@Override
	public List<FeesReceiveStorageEntity> queryPreBillPallet(Map<String, Object> parameter) {
		return selectList("com.jiuyescm.bms.fees.storage.FeesReceiveStorageEntityMapper.queryPreBillPallet", parameter);
	}
	
	@Override
	public List<FeesReceiveStorageEntity> queryPreBillMaterialStorage(Map<String, Object> parameter) {
		return selectList("com.jiuyescm.bms.fees.storage.FeesReceiveStorageEntityMapper.queryPreBillMaterialStorage", parameter);
	}
	
	@Override
	public List<FeesReceiveStorageEntity> queryByFeesNo(String FeesNo){
		return selectList("com.jiuyescm.bms.fees.storage.FeesReceiveStorageEntityMapper.queryByFeesNo", FeesNo);
	}

	@Override
	public List<FeesReceiveStorageEntity> queryCalculateFail(
			Map<String, Object> parameter) {
		// TODO Auto-generated method stub
		return selectList("com.jiuyescm.bms.fees.storage.FeesReceiveStorageEntityMapper.queryCalculateFail", parameter);
	}

	@Override
	public FeesReceiveStorageEntity queryOne(Map<String, Object> condition) {
		// TODO Auto-generated method stub
		return (FeesReceiveStorageEntity) selectOne("com.jiuyescm.bms.fees.storage.FeesReceiveStorageEntityMapper.queryOne", condition);
	}

	@Override
	public int updateIsCalcuByFeesNo(Map<String, Object> condition) {
		// TODO Auto-generated method stub
		return update("com.jiuyescm.bms.fees.storage.FeesReceiveStorageEntityMapper.updateIsCalcuByFeesNo", condition);
	}

}
