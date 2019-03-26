package com.jiuyescm.bms.biz.storage.repository.impl;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Repository;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.biz.storage.entity.StoInstockEntity;
import com.jiuyescm.bms.biz.storage.repository.IStoInstockRepository;
import com.jiuyescm.cfm.persistence.mybatis.MyBatisDao;

@Repository("stoInstockRepositoryImpl")
public class StoInstockRepositoryImpl extends MyBatisDao<StoInstockEntity> implements IStoInstockRepository {

	@Override
	public List<StoInstockEntity> queryBiz(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PageInfo<StoInstockEntity> queryBiz(Map<String, Object> map,int pageNo, int pageSize) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<StoInstockEntity> queryFee(Map<String, Object> map) {
		return null;
	}

	@Override
	public List<StoInstockEntity> queryALL(Map<String, Object> map) {
		List<StoInstockEntity> list = selectList("com.jiuyescm.bms.biz.storage.StoInstockMapper.queryAll", map);
		return list;
	}

	@Override
	public PageInfo<StoInstockEntity> queryALL(Map<String, Object> map,int pageNo, int pageSize) {
		List<StoInstockEntity> list = selectList("com.jiuyescm.bms.biz.storage.StoInstockMapper.queryAll", map, new RowBounds(
                pageNo, pageSize));
        PageInfo<StoInstockEntity> pageInfo = new PageInfo<StoInstockEntity>(list);
        return pageInfo;
	}

	@Override
	public void updateFees(List<StoInstockEntity> stoList) {
		updateBatch("com.jiuyescm.bms.biz.storage.StoInstockMapper.updateStoFee", stoList);
	}

}
