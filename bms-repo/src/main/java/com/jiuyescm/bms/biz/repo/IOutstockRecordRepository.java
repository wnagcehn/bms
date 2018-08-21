package com.jiuyescm.bms.biz.repo;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.biz.entity.BmsOutstockRecordEntity;

public interface IOutstockRecordRepository {
	
	/**
	 * 查询出所有的改动记录
	 * @param condition
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
    PageInfo<BmsOutstockRecordEntity> query(Map<String, Object> condition, int pageNo,
            int pageSize);
	
	public int insert(BmsOutstockRecordEntity entity);
	
	public int insertList(List<BmsOutstockRecordEntity> list);
}
