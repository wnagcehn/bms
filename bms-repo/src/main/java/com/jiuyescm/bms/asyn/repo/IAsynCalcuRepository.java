package com.jiuyescm.bms.asyn.repo;

import java.util.Map;
import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.asyn.entity.AsynCalcuEntity;

/**
 * ..Repository
 * @author wangchen
 * 
 */
public interface IAsynCalcuRepository {

	PageInfo<AsynCalcuEntity> query(Map<String, Object> condition,
		int pageNo, int pageSize);

    int save(AsynCalcuEntity entity);

	int update(AsynCalcuEntity entity);

	int delete(AsynCalcuEntity entity);

}
