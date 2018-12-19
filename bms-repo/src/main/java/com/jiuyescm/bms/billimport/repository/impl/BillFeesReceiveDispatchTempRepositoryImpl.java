/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.billimport.repository.impl;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import com.jiuyescm.bms.billimport.entity.BillFeesReceiveDispatchTempEntity;
import com.jiuyescm.bms.billimport.repository.IBillFeesReceiveDispatchTempRepository;
import com.jiuyescm.cfm.persistence.mybatis.MyBatisDao;

/**
 * 
 * @author stevenl
 * 
 */
@Repository("billFeesReceiveDispatchTempRepository")
public class BillFeesReceiveDispatchTempRepositoryImpl extends MyBatisDao<BillFeesReceiveDispatchTempEntity> implements IBillFeesReceiveDispatchTempRepository {

	private static final Logger logger = Logger.getLogger(BillFeesReceiveDispatchTempRepositoryImpl.class.getName());

	public BillFeesReceiveDispatchTempRepositoryImpl() {
		super();
	}
	
	@Override
    public PageInfo<BillFeesReceiveDispatchTempEntity> query(Map<String, Object> condition, int pageNo, int pageSize) {
        List<BillFeesReceiveDispatchTempEntity> list = selectList("com.jiuyescm.bms.billimport.BillFeesReceiveDispatchTempMapper.query", condition, new RowBounds(
                pageNo, pageSize));
        PageInfo<BillFeesReceiveDispatchTempEntity> pageInfo = new PageInfo<BillFeesReceiveDispatchTempEntity>(list);
        return pageInfo;
    }

    @Override
    public BillFeesReceiveDispatchTempEntity findById(Long id) {
        BillFeesReceiveDispatchTempEntity entity = selectOne("com.jiuyescm.bms.billimport.BillFeesReceiveDispatchTempMapper.findById", id);
        return entity;
    }

    @Override
    public BillFeesReceiveDispatchTempEntity save(BillFeesReceiveDispatchTempEntity entity) {
        insert("com.jiuyescm.bms.billimport.BillFeesReceiveDispatchTempMapper.save", entity);
        return entity;
    }

    @Override
    public BillFeesReceiveDispatchTempEntity update(BillFeesReceiveDispatchTempEntity entity) {
        update("com.jiuyescm.bms.billimport.BillFeesReceiveDispatchTempMapper.update", entity);
        return entity;
    }

    @Override
    public void delete(Long id) {
        delete("com.jiuyescm.bms.billimport.BillFeesReceiveDispatchTempMapper.delete", id);
    }
    
	@Override
	public int delete(String billNo) {
		// TODO Auto-generated method stub
		int d = delete("com.jiuyescm.bms.billimport.BillFeesReceiveDispatchTempMapper.delete", billNo);
		return d;
	}

	@Override
	public int insertBatch(List<BillFeesReceiveDispatchTempEntity> list)throws Exception {
		// TODO Auto-generated method stub
		SqlSession session = getSqlSessionTemplate();
        int result=session.insert("com.jiuyescm.bms.billimport.BillFeesReceiveDispatchTempMapper.insertBatch", list);
		return result;
	}

	@Override
	public int deleteBatch(String billNo) {
		// TODO Auto-generated method stub
		return delete("com.jiuyescm.bms.billimport.BillFeesReceiveDispatchTempMapper.deleteBatch", billNo);
	}

	@Override
	public int saveDataFromTemp(String billNo) {
		// TODO Auto-generated method stub
		 SqlSession session = getSqlSessionTemplate();
		 Map<String,String> map=Maps.newHashMap();
		 map.put("billNo", billNo);
		return session.insert("com.jiuyescm.bms.billimport.BillFeesReceiveDispatchTempMapper.saveDataFromTemp", map);
	}

	@Override
	public Double getImportDispatchAmount(String billNo) {
		// TODO Auto-generated method stub
		Object object=selectOne("com.jiuyescm.bms.billimport.BillFeesReceiveDispatchTempMapper.getImportDispatchAmount", billNo);
		Double money=0d;
		if(object!=null){
			money=Double.valueOf(object.toString());
		}
		return money;
		
	}
	
}
