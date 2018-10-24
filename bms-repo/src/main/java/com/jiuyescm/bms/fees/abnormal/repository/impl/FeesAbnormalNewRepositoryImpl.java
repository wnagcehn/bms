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

}
