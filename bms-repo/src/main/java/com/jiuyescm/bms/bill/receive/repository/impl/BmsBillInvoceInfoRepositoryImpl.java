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
import com.jiuyescm.bms.bill.receive.entity.BmsBillInvoceInfoEntity;
import com.jiuyescm.bms.bill.receive.repository.IBmsBillInvoceInfoRepository;
import com.jiuyescm.cfm.persistence.mybatis.MyBatisDao;

/**
 * 
 * @author stevenl
 * 
 */
@Repository("bmsBillInvoceInfoRepository")
public class BmsBillInvoceInfoRepositoryImpl extends MyBatisDao<BmsBillInvoceInfoEntity> implements IBmsBillInvoceInfoRepository {

	private static final Logger logger = Logger.getLogger(BmsBillInvoceInfoRepositoryImpl.class.getName());

	public BmsBillInvoceInfoRepositoryImpl() {
		super();
	}
	
	@Override
    public PageInfo<BmsBillInvoceInfoEntity> query(Map<String, Object> condition, int pageNo, int pageSize) {
        List<BmsBillInvoceInfoEntity> list = selectList("com.jiuyescm.bms.bill.receive.BmsBillInvoceInfoMapper.query", condition, new RowBounds(
                pageNo, pageSize));
        PageInfo<BmsBillInvoceInfoEntity> pageInfo = new PageInfo<BmsBillInvoceInfoEntity>(list);
        return pageInfo;
    }

	@Override
	public List<BmsBillInvoceInfoEntity> queryInvoce(
			Map<String, Object> condition) {
		// TODO Auto-generated method stub
		return selectList("com.jiuyescm.bms.bill.receive.BmsBillInvoceInfoMapper.query", condition);
	}
	
    @Override
    public BmsBillInvoceInfoEntity findById(Long id) {
        BmsBillInvoceInfoEntity entity = selectOne("com.jiuyescm.bms.bill.receive.BmsBillInvoceInfoMapper.findById", id);
        return entity;
    }

    @Override
    public int save(BmsBillInvoceInfoEntity entity) {
        return insert("com.jiuyescm.bms.bill.receive.BmsBillInvoceInfoMapper.save", entity);
    }

    @Override
    public int update(BmsBillInvoceInfoEntity entity) {
        return update("com.jiuyescm.bms.bill.receive.BmsBillInvoceInfoMapper.update", entity);
    }

	@Override
	public BmsBillInvoceInfoEntity queryCountInvoceInfo(Map<String, Object> condition) {
		SqlSession session = getSqlSessionTemplate();
		return session.selectOne("com.jiuyescm.bms.bill.receive.BmsBillInvoceInfoMapper.queryCountInvoceInfo", condition);
	}

	@Override
	public BmsBillInvoceInfoEntity queryCountReceiptInfo(Map<String, Object> condition) {
		SqlSession session = getSqlSessionTemplate();
		return session.selectOne("com.jiuyescm.bms.bill.receive.BmsBillInvoceInfoMapper.queryCountReceiptInfo", condition);
	}

	@Override
	public List<BmsBillInvoceInfoEntity> query(Map<String, Object> condition) {
		return selectList("com.jiuyescm.bms.bill.receive.BmsBillInvoceInfoMapper.query", condition);
	}

	@Override
	public int deleteFeesBill(BmsBillInfoEntity entity) {
		SqlSession session = getSqlSessionTemplate();
		Map<String,Object> parameter=new HashMap<String,Object>();
		parameter.put("billNo", entity.getBillNo());
		parameter.put("delFlag", entity.getDelFlag());
		parameter.put("lastModifier", entity.getLastModifier());
		parameter.put("lastModifyTime", entity.getLastModifyTime());
		return session.update("com.jiuyescm.bms.bill.receive.BmsBillInvoceInfoMapper.deleteFeesBill", parameter);
	}

	
	
}
