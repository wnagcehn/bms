package com.jiuyescm.bms.biz.storage.repository;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.biz.storage.entity.StoInstockEntity;

public interface IStoInstockRepository {

	List<StoInstockEntity> queryBiz(Map<String, Object> map);
	
	PageInfo<StoInstockEntity> queryBiz(Map<String, Object> condition,int pageNo, int pageSize);
	
	List<StoInstockEntity> queryFee(Map<String, Object> map);
}
