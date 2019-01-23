package com.jiuyescm.bms.correct.repository;

import java.util.Map;
import java.util.List;
import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.correct.BmsOrderProductEntity;

/**
 * ..Repository
 * @author wangchen
 * 
 */
public interface IBmsOrderProductRepository {

	PageInfo<BmsOrderProductEntity> query(Map<String, Object> condition,
		int pageNo, int pageSize);

	List<BmsOrderProductEntity> query(Map<String, Object> condition);

}
