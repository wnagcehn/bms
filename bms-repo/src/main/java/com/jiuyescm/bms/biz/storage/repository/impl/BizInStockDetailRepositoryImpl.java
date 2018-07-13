package com.jiuyescm.bms.biz.storage.repository.impl;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Repository;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.biz.storage.entity.BizInStockDetailEntity;
import com.jiuyescm.bms.biz.storage.repository.IBizInStockDetailRepository;
import com.jiuyescm.cfm.persistence.mybatis.MyBatisDao;

/**
 * 入库单明细表
 * 
 * @author yangshuaishuai
 * 
 */
@Repository("inStockDetailRepository")
public class BizInStockDetailRepositoryImpl extends
		MyBatisDao<BizInStockDetailEntity> implements IBizInStockDetailRepository {

	@Override
	public PageInfo<BizInStockDetailEntity> query(Map<String, Object> condition,
			int pageNo, int pageSize) {
		List<BizInStockDetailEntity> list = selectList(
				"com.jiuyescm.bms.biz.storage.mapper.BizInStockDetailMapper.query",
				condition, new RowBounds(pageNo, pageSize));
		return new PageInfo<BizInStockDetailEntity>(list);
	}

}
