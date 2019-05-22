package com.jiuyescm.bms.base.dict.repository;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.base.dict.entity.PubPackageDictEntity;

/**
 * ..Repository
 * @author wangchen
 * 
 */
public interface IPubPackageDictRepository {

    PubPackageDictEntity findById(Long id);
	
	PageInfo<PubPackageDictEntity> query(Map<String, Object> condition,
		int pageNo, int pageSize);

	List<PubPackageDictEntity> query(Map<String, Object> condition);

    PubPackageDictEntity save(PubPackageDictEntity entity);

    PubPackageDictEntity update(PubPackageDictEntity entity);

    void delete(Long id);

}
