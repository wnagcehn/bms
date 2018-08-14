package com.jiuyescm.bms.base.servicetype.repository;

import java.util.Map;
import java.util.List;
import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.base.servicetype.entity.PubCarrierServicetypeEntity;

/**
 * ..Repository
 * @author wangchen
 * 
 */
public interface IPubCarrierServicetypeRepository {

    PubCarrierServicetypeEntity findById(Long id);
	
	PageInfo<PubCarrierServicetypeEntity> query(Map<String, Object> condition,
		int pageNo, int pageSize);

	List<PubCarrierServicetypeEntity> query(Map<String, Object> condition);

    PubCarrierServicetypeEntity save(PubCarrierServicetypeEntity entity);

    PubCarrierServicetypeEntity update(PubCarrierServicetypeEntity entity);

}
