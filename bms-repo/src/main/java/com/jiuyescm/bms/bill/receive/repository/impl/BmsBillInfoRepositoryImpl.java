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
import com.jiuyescm.bms.bill.receive.repository.IBmsBillInfoRepository;
import com.jiuyescm.bms.bill.receive.vo.BmsBillCountEntityVo;
import com.jiuyescm.bms.bill.receive.vo.BmsBillCustomerCountEntityVo;
import com.jiuyescm.bms.fees.entity.FeesBillWareHouseEntity;
import com.jiuyescm.cfm.persistence.mybatis.MyBatisDao;

/**
 * 
 * @author stevenl
 * 
 */
@SuppressWarnings("rawtypes")
@Repository("bmsBillInfoRepository")
public class BmsBillInfoRepositoryImpl extends MyBatisDao implements IBmsBillInfoRepository {

	private static final Logger logger = Logger.getLogger(BmsBillInfoRepositoryImpl.class.getName());

	public BmsBillInfoRepositoryImpl() {
		super();
	}
	
	@SuppressWarnings("unchecked")
	@Override
    public PageInfo<BmsBillInfoEntity> query(Map<String, Object> condition, int pageNo, int pageSize) {
        List<BmsBillInfoEntity> list = selectList("com.jiuyescm.bms.bill.receive.BmsBillInfoMapper.query", condition, new RowBounds(
                pageNo, pageSize));
        PageInfo<BmsBillInfoEntity> pageInfo = new PageInfo<BmsBillInfoEntity>(list);
        return pageInfo;
    }

    @Override
    public BmsBillInfoEntity findById(Long id) {
        BmsBillInfoEntity entity = (BmsBillInfoEntity) selectOne("com.jiuyescm.bms.bill.receive.BmsBillInfoMapper.findById", id);
        return entity;
    }

    @SuppressWarnings("unchecked")
	@Override
    public int save(BmsBillInfoEntity entity) {
        return insert("com.jiuyescm.bms.bill.receive.BmsBillInfoMapper.save", entity);
    }

    @SuppressWarnings("unchecked")
	@Override
    public int update(BmsBillInfoEntity entity) {
    	 return update("com.jiuyescm.bms.bill.receive.BmsBillInfoMapper.update", entity);
    }
    
    @SuppressWarnings("unchecked")
	@Override
    public int updateByBillNo(BmsBillInfoEntity entity) {
    	return update("com.jiuyescm.bms.bill.receive.BmsBillInfoMapper.updateByBillNo", entity);
    }

    @Override
    public void delete(Long id) {
        delete("com.jiuyescm.bms.bill.receive.BmsBillInfoMapper.delete", id);
    }

	@Override
	public BmsBillInfoEntity queryLastBillTime(Map<String, Object> condition) {
		SqlSession session = getSqlSessionTemplate();
		return session.selectOne("com.jiuyescm.bms.bill.receive.BmsBillInfoMapper.queryLastBillTime", condition);
	}

	@Override
	public List<FeesBillWareHouseEntity> querywarehouseAmount(
			String billNo) {
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("billno", billNo);
		SqlSession session = getSqlSessionTemplate();
		return session.selectList("com.jiuyescm.bms.bill.receive.BmsBillInfoMapper.querywarehouseAmount", map);
	}

	@SuppressWarnings("unchecked")
	@Override
	public int deleteFeesBill(BmsBillInfoEntity entity) {
		return this.update("com.jiuyescm.bms.bill.receive.BmsBillInfoMapper.deleteFeesBill", entity);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void updateDiscountStorageBill(BmsBillInfoEntity billInfoEntity) {
		this.update("com.jiuyescm.bms.bill.receive.BmsBillInfoMapper.updateDiscountStorageBill",billInfoEntity);
		
	}

	@SuppressWarnings("unchecked")
	@Override
	public BmsBillInfoEntity queryEntityByBillNo(String billNo) {
		
		List<BmsBillInfoEntity> list=this.selectList("com.jiuyescm.bms.bill.receive.BmsBillInfoMapper.queryEntityByBillNo", billNo);
		if(list!=null&&list.size()>0){
			return list.get(0);
		}
		return null;
	}

	@Override
	public void refeshAmount(BmsBillSubjectInfoEntity bill) {

		Map<String,Object> map=new HashMap<String,Object>();
		map.put("billNo", bill.getBillNo());
		SqlSession session = getSqlSessionTemplate();
		session.update("com.jiuyescm.bms.bill.receive.BmsBillInfoMapper.refeshAmount", map);
		
	}

	@SuppressWarnings("unchecked")
	@Override
    public PageInfo<BmsBillCountEntityVo> countBill(Map<String, Object> condition, int pageNo, int pageSize) {
        List<BmsBillCountEntityVo> list = selectList("com.jiuyescm.bms.bill.receive.BmsBillInfoMapper.countBill", condition, new RowBounds(
                pageNo, pageSize));
        PageInfo<BmsBillCountEntityVo> pageInfo = new PageInfo<BmsBillCountEntityVo>(list);
        return pageInfo;
    }
	
	
	@SuppressWarnings("unchecked")
	@Override
	public List<BmsBillInfoEntity> queryBmsBill(Map<String, Object> condition) {
		// TODO Auto-generated method stub
		return selectList("com.jiuyescm.bms.bill.receive.BmsBillInfoMapper.queryBmsBill", condition);
	}

	@SuppressWarnings("unchecked")
	@Override
	public BmsBillCustomerCountEntityVo queryCustomerVo(Map<String, Object> condition) {
		// TODO Auto-generated method stub
		BmsBillCustomerCountEntityVo entityVo = (BmsBillCustomerCountEntityVo) selectOne("com.jiuyescm.bms.bill.receive.BmsBillInfoMapper.countCustomerBill", condition);
        return entityVo;
	}
	
}
