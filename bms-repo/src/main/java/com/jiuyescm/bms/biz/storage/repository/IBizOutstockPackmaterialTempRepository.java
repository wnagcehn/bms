/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.biz.storage.repository;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.biz.storage.entity.BizOutstockPackmaterialTempEntity;

/**
 * 
 * @author stevenl
 * 
 */
public interface IBizOutstockPackmaterialTempRepository {

	public PageInfo<BizOutstockPackmaterialTempEntity> query(Map<String, Object> condition,
            int pageNo, int pageSize);

    public BizOutstockPackmaterialTempEntity findById(Long id);

    public BizOutstockPackmaterialTempEntity save(BizOutstockPackmaterialTempEntity entity);

    public BizOutstockPackmaterialTempEntity update(BizOutstockPackmaterialTempEntity entity);

    public void delete(Long id);

	public void saveBatch(List<BizOutstockPackmaterialTempEntity> tempList);

	public void deleteBybatchNum(String batchNum);

	public List<BizOutstockPackmaterialTempEntity> querySameData(String batchNum);

	public List<BizOutstockPackmaterialTempEntity> queryContainsList(
			String batchNum);

	public List<BizOutstockPackmaterialTempEntity> queryContainsList(
			String batchNum, int errorCount);

}
