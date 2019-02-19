/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.biz.storage.service;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.biz.storage.entity.BizOutstockPackmaterialTempEntity;

/**
 * 
 * @author stevenl
 * 
 */
public interface IBizOutstockPackmaterialTempService {

    PageInfo<BizOutstockPackmaterialTempEntity> query(Map<String, Object> condition, int pageNo,
            int pageSize);

    BizOutstockPackmaterialTempEntity findById(Long id);

    BizOutstockPackmaterialTempEntity save(BizOutstockPackmaterialTempEntity entity);

    BizOutstockPackmaterialTempEntity update(BizOutstockPackmaterialTempEntity entity);

    void delete(Long id);

	void saveBatch(List<BizOutstockPackmaterialTempEntity> tempList);

	void deleteBybatchNum(String batchNum);

	List<BizOutstockPackmaterialTempEntity> querySameData(String batchNum);

	List<BizOutstockPackmaterialTempEntity> queryContainsList(String batchNum);

	List<BizOutstockPackmaterialTempEntity> queryContainsList(String batchNum,
			int errorCount);

}
