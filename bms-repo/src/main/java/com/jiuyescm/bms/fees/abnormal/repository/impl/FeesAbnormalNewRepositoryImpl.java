package com.jiuyescm.bms.fees.abnormal.repository.impl;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Repository;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.fees.abnormal.entity.FeesAbnormalEntity;
import com.jiuyescm.bms.fees.abnormal.repository.IFeesAbnormalNewRepository;
import com.jiuyescm.cfm.persistence.mybatis.MyBatisDao;

@Repository("feesAbnormalNewRepository")
public class FeesAbnormalNewRepositoryImpl extends MyBatisDao<FeesAbnormalEntity> implements IFeesAbnormalNewRepository{

	@Override
	public PageInfo<FeesAbnormalEntity> query(Map<String, Object> condition,
			int pageNo, int pageSize) {
		// TODO Auto-generated method stub
		List<FeesAbnormalEntity> list = selectList("com.jiuyescm.bms.fees.abnormal.FeesAbnormalNewMapper.query", condition, new RowBounds(
                pageNo, pageSize));
        PageInfo<FeesAbnormalEntity> pageInfo = new PageInfo<FeesAbnormalEntity>(list);
		return pageInfo;
	}

	@Override
	public PageInfo<FeesAbnormalEntity> queryPay(Map<String, Object> condition,
			int pageNo, int pageSize) {
		// TODO Auto-generated method stub
		List<FeesAbnormalEntity> list = selectList("com.jiuyescm.bms.fees.abnormal.FeesAbnormalNewMapper.queryPay", condition, new RowBounds(
                pageNo, pageSize));
        PageInfo<FeesAbnormalEntity> pageInfo = new PageInfo<FeesAbnormalEntity>(list);
		return pageInfo;
	}

	@Override
	public FeesAbnormalEntity queryOne(Map<String, Object> condition) {
		// TODO Auto-generated method stub
		FeesAbnormalEntity entity=(FeesAbnormalEntity) selectOne("com.jiuyescm.bms.fees.abnormal.FeesAbnormalNewMapper.queryOne", condition);
		return entity;
	}

	@Override
	public int updateList(List<FeesAbnormalEntity> list) {
		// TODO Auto-generated method stub
		return updateBatch("com.jiuyescm.bms.fees.abnormal.FeesAbnormalNewMapper.update", list);
	}

	@Override
	public int updateOne(FeesAbnormalEntity entity) {
		// TODO Auto-generated method stub
		return update("com.jiuyescm.bms.fees.abnormal.FeesAbnormalNewMapper.update", entity);
	}

	@Override
	public PageInfo<FeesAbnormalEntity> queryCount(
			Map<String, Object> condition, int pageNo, int pageSize) {
		// TODO Auto-generated method stub
		List<FeesAbnormalEntity> list = selectList("com.jiuyescm.bms.fees.abnormal.FeesAbnormalNewMapper.queryCount", condition, new RowBounds(
                pageNo, pageSize));
        PageInfo<FeesAbnormalEntity> pageInfo = new PageInfo<FeesAbnormalEntity>(list);
		return pageInfo;
	}

}
