package com.jiuyescm.bms.base.monthFeeCount.repository.impl;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Repository;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.base.monthFeeCount.PubMonthFeeCountEntity;
import com.jiuyescm.bms.base.monthFeeCount.repository.IPubMonthFeeCountRepository;
import com.jiuyescm.cfm.persistence.mybatis.MyBatisDao;

@Repository("pubMonthFeeCountRepository")
public class PubMonthFeeCountRepositoryImpl  extends MyBatisDao<PubMonthFeeCountEntity> implements IPubMonthFeeCountRepository{

	@Override
	public PageInfo<PubMonthFeeCountEntity> queryAll(
			Map<String, Object> condition, int pageNo, int pageSize) {
		// TODO Auto-generated method stub
	     List<PubMonthFeeCountEntity> list = selectList("com.jiuyescm.bms.base.monthFeeCount.mapper.PubMonthFeeCountMapper.query", condition, new RowBounds(
	                pageNo, pageSize));
	     return new PageInfo<PubMonthFeeCountEntity>(list);
	}

	@Override
	public List<PubMonthFeeCountEntity> query(Map<String, Object> condition) {
		// TODO Auto-generated method stub
		return selectList("com.jiuyescm.bms.base.monthFeeCount.mapper.PubMonthFeeCountMapper.query", condition);
	}

}
