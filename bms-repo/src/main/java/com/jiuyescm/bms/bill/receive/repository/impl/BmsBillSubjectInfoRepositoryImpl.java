/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.bill.receive.repository.impl;

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
import com.jiuyescm.bms.bill.receive.repository.IBmsBillSubjectInfoRepository;
import com.jiuyescm.bms.fees.entity.FeesBillWareHouseEntity;
import com.jiuyescm.cfm.persistence.mybatis.MyBatisDao;

/**
 * 
 * @author stevenl
 * 
 */
@Repository("bmsBillSubjectInfoRepository")
public class BmsBillSubjectInfoRepositoryImpl extends MyBatisDao<BmsBillSubjectInfoEntity> implements IBmsBillSubjectInfoRepository {

	private static final Logger logger = Logger.getLogger(BmsBillSubjectInfoRepositoryImpl.class.getName());

	public BmsBillSubjectInfoRepositoryImpl() {
		super();
	}
	
	@Override
    public PageInfo<BmsBillSubjectInfoEntity> query(Map<String, Object> condition, int pageNo, int pageSize) {
        List<BmsBillSubjectInfoEntity> list = selectList("com.jiuyescm.bms.bill.receive.BmsBillSubjectInfoMapper.query", condition, new RowBounds(
                pageNo, pageSize));
        PageInfo<BmsBillSubjectInfoEntity> pageInfo = new PageInfo<BmsBillSubjectInfoEntity>(list);
        return pageInfo;
    }

    @Override
    public BmsBillSubjectInfoEntity findById(Long id) {
        BmsBillSubjectInfoEntity entity = selectOne("com.jiuyescm.bms.bill.receive.BmsBillSubjectInfoMapper.findById", id);
        return entity;
    }

    @Override
    public BmsBillSubjectInfoEntity save(BmsBillSubjectInfoEntity entity) {
        insert("com.jiuyescm.bms.bill.receive.BmsBillSubjectInfoMapper.save", entity);
        return entity;
    }

    @Override
    public BmsBillSubjectInfoEntity update(BmsBillSubjectInfoEntity entity) {
        update("com.jiuyescm.bms.bill.receive.BmsBillSubjectInfoMapper.update", entity);
        return entity;
    }

    @Override
    public void delete(Long id) {
        delete("com.jiuyescm.bms.bill.receive.BmsBillSubjectInfoMapper.delete", id);
    }


	@Override
	public void updateDiscountStorageBill(BmsBillSubjectInfoEntity bill) {
		this.update("com.jiuyescm.bms.bill.receive.BmsBillSubjectInfoMapper.updateDiscountStorageBill",bill);
	}

	@Override
	public int saveBillSubjectList(List<BmsBillSubjectInfoEntity> list) {
		return insertBatch("com.jiuyescm.bms.bill.receive.BmsBillSubjectInfoMapper.save", list);
	}

	@Override
	public List<BmsBillSubjectInfoEntity> queryAllByBillNoAndwarehouse(
			Map<String, Object> parameter) {
		
		return this.selectList("com.jiuyescm.bms.bill.receive.BmsBillSubjectInfoMapper.queryAllByBillNoAndwarehouse", parameter);
	}

	@Override
	public List<FeesBillWareHouseEntity> querywarehouseAmount(String billNo,
			String feesType) {
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("billNo", billNo);
		map.put("feesType",feesType);
	    SqlSession session = getSqlSessionTemplate();
		return session.selectList("com.jiuyescm.bms.bill.receive.BmsBillSubjectInfoMapper.querywarehouseAmount",map);
	}

	@Override
	public List<FeesBillWareHouseEntity> querywarehouseAmount(String billNo) {
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("billNo", billNo);
	    SqlSession session = getSqlSessionTemplate();
		return session.selectList("com.jiuyescm.bms.bill.receive.BmsBillSubjectInfoMapper.queryTotlewarehouseAmount",map);
	}

	@Override
	public int deleteFeesBill(BmsBillInfoEntity entity) {
		SqlSession session = getSqlSessionTemplate();
		Map<String,Object> parameter=new HashMap<String,Object>();
		parameter.put("billNo", entity.getBillNo());
		parameter.put("delFlag", entity.getDelFlag());
		parameter.put("lastModifier", entity.getLastModifier());
		parameter.put("lastModifyTime", entity.getLastModifyTime());
		return session.update("com.jiuyescm.bms.bill.receive.BmsBillSubjectInfoMapper.deleteFeesBill", parameter);
	}

	@Override
	public List<BmsBillSubjectInfoEntity> queryBillSubjectStatus(Map<String, Object> parameter) {
		return selectList("com.jiuyescm.bms.bill.receive.BmsBillSubjectInfoMapper.queryBillSubjectStatus", parameter);
	}

	@Override
	public void deleteStorageBill(BmsBillSubjectInfoEntity bill) {
		this.update("com.jiuyescm.bms.bill.receive.BmsBillSubjectInfoMapper.updateDiscountStorageBill",bill);
	}

	@Override
	public void updateStatus(String billNo, String warehouseCode,
			String subjectCode, String status) {
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("billNo", billNo);
		map.put("warehouseCode", warehouseCode);
		map.put("subjectCode", subjectCode);
		map.put("status", status);
		SqlSession session = getSqlSessionTemplate();
		session.update("com.jiuyescm.bms.bill.receive.BmsBillSubjectInfoMapper.updateStatus",map);
	}

	@Override
	public void deleteSubjectBill(BmsBillSubjectInfoEntity bill) {
		this.update("com.jiuyescm.bms.bill.receive.BmsBillSubjectInfoMapper.deleteSubjectBill",bill);	
	}

	@Override
	public int updateBillSubject(
			Map<String, Object> condition) {
		// TODO Auto-generated method stub
		SqlSession session = getSqlSessionTemplate();
		return session.update("com.jiuyescm.bms.bill.receive.BmsBillSubjectInfoMapper.updateBillSubject", condition);
	}

	@Override
	public BmsBillSubjectInfoEntity queryTransportSum(Map<String, Object> param) {
		return (BmsBillSubjectInfoEntity)selectOne("com.jiuyescm.bms.bill.receive.BmsBillSubjectInfoMapper.queryTransportSum",param);
	}

	@Override
	public int updateDerateFee(Map<String, Object> condition) {
		// TODO Auto-generated method stub
		SqlSession session = getSqlSessionTemplate();
		return session.update("com.jiuyescm.bms.bill.receive.BmsBillSubjectInfoMapper.updateBillSubjectDerateFee", condition);
	}

	@Override
	public int updateSubjectList(List<BmsBillSubjectInfoEntity> condition) {
		// TODO Auto-generated method stub
		return updateBatch("com.jiuyescm.bms.bill.receive.BmsBillSubjectInfoMapper.updateSubjectList", condition);
	}

	@Override
	public void updateAbnormalTransportBillSubject(
			Map<String, Object> conditionMap) {
		SqlSession session = getSqlSessionTemplate();
	    session.update("com.jiuyescm.bms.bill.receive.BmsBillSubjectInfoMapper.updateAbnormalTransportBillSubject", conditionMap);
	}

	@Override
	public void updateTransportBillSubject(Map<String, Object> conditionMap) {
		SqlSession session = getSqlSessionTemplate();
	    session.update("com.jiuyescm.bms.bill.receive.BmsBillSubjectInfoMapper.updateTransportBillSubject", conditionMap);
	}
	
}
