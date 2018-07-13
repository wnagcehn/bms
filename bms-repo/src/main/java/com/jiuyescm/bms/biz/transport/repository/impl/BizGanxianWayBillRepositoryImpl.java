/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.biz.transport.repository.impl;

import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.biz.transport.entity.BizGanxianWayBillEntity;
import com.jiuyescm.bms.biz.transport.repository.IBizGanxianWayBillRepository;
import com.jiuyescm.cfm.persistence.mybatis.MyBatisDao;

/**
 * 
 * @author wubangjun
 * 
 */
@Repository("bizGanxianWayBillRepository")
public class BizGanxianWayBillRepositoryImpl extends MyBatisDao<BizGanxianWayBillEntity> implements IBizGanxianWayBillRepository {

	private static final Logger logger = Logger.getLogger(BizGanxianWayBillRepositoryImpl.class.getName());
	
	@Override
    public PageInfo<BizGanxianWayBillEntity> query(Map<String, Object> condition, int pageNo, int pageSize) {
        List<BizGanxianWayBillEntity> list = selectList("com.jiuyescm.bms.biz.transport.mapper.BizGanxianWayBillMapper.query", condition, new RowBounds(
                pageNo, pageSize));
        PageInfo<BizGanxianWayBillEntity> pageInfo = new PageInfo<BizGanxianWayBillEntity>(list);
        return pageInfo;
    }
	
	@Override
	public BizGanxianWayBillEntity query(Map<String, Object> condition) {
		return (BizGanxianWayBillEntity)selectOne("com.jiuyescm.bms.biz.transport.mapper.BizGanxianWayBillMapper.query", condition);
	}

    @Override
    public BizGanxianWayBillEntity findById(Long id) {
        BizGanxianWayBillEntity entity = selectOne("com.jiuyescm.bms.biz.transport.mapper.BizGanxianWayBillMapper.findById", id);
        return entity;
    }

    @Override
    public BizGanxianWayBillEntity save(BizGanxianWayBillEntity entity) {
        insert("com.jiuyescm.bms.biz.transport.mapper.BizGanxianWayBillMapper.save", entity);
        return entity;
    }

    @Override
    public BizGanxianWayBillEntity update(BizGanxianWayBillEntity entity) {
        update("com.jiuyescm.bms.biz.transport.mapper.BizGanxianWayBillMapper.update", entity);
        return entity;
    }

	@Override
	public void updateList(List<BizGanxianWayBillEntity> updateList) {
		updateBatch("com.jiuyescm.bms.biz.transport.mapper.BizGanxianWayBillMapper.update", updateList);
	}

	@Override
	public int saveList(List<BizGanxianWayBillEntity> list) {
		return insertBatch("com.jiuyescm.bms.biz.transport.mapper.BizGanxianWayBillMapper.save", list);
	}

	@Override
	public Properties validRetry(Map<String, Object> param) {
		Properties ret = new Properties();
		try{
			Object orderNo = selectOne("com.jiuyescm.bms.biz.transport.mapper.BizGanxianWayBillMapper.validBillForRetry", param);
			if(orderNo != null){
				ret.setProperty("key", "Billed");
				ret.setProperty("value", "订单号【"+orderNo+"】已在账单中存在,不能重算,建议删除账单后重试");//已在账单中存在,不能重算,建议删除账单后重试;
				return ret;
			}
			
			orderNo = selectOne("com.jiuyescm.bms.biz.transport.mapper.BizGanxianWayBillMapper.validCalcuForRetry", param);
			if(orderNo != null){
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
			SqlSession session = getSqlSessionTemplate();
			session.update("com.jiuyescm.bms.biz.transport.mapper.BizGanxianWayBillMapper.retryForCalcu", param);
			return 1;
		}
		catch(Exception ex){
			return 0;
		}
	}

	@Override
	public PageInfo<BizGanxianWayBillEntity> queryGroup(
			Map<String, Object> condition, int pageNo, int pageSize) {
		 List<BizGanxianWayBillEntity> list = selectList("com.jiuyescm.bms.biz.transport.mapper.BizGanxianWayBillMapper.queryGroup", condition, new RowBounds(
	                pageNo, pageSize));
	        PageInfo<BizGanxianWayBillEntity> pageInfo = new PageInfo<BizGanxianWayBillEntity>(list);
	        return pageInfo;
	}

	@Override
	public List<BizGanxianWayBillEntity> queryDelete(
			Map<String, Object> condition) {
		return selectList("com.jiuyescm.bms.biz.transport.mapper.BizGanxianWayBillMapper.queryDelete",condition);
	}

	@Override
	public int deleteBatch(List<BizGanxianWayBillEntity> list) {
		return deleteBatch("com.jiuyescm.bms.biz.transport.mapper.BizGanxianWayBillMapper.delete",list);
	}

	@Override
	public int deleteFees(Map<String, Object> condition) {
		return delete("com.jiuyescm.bms.biz.transport.mapper.BizGanxianWayBillMapper.deleteFees",condition);
	}

	/**
	 * 同时删除业务数据和费用
	 */
	@Override
	public int deleteBizAndFees(Map<String, Object> condition) {
		return delete("com.jiuyescm.bms.biz.transport.mapper.BizGanxianWayBillMapper.deleteBizAndFees", condition);
	}

	@Override
	public BizGanxianWayBillEntity queryExceptionOne(
			Map<String, Object> condition) {
		// TODO Auto-generated method stub
		return (BizGanxianWayBillEntity) selectOne("com.jiuyescm.bms.biz.transport.mapper.BizGanxianWayBillMapper.queryExceptionOne", condition);
	}
	
}
