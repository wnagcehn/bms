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

	/**
	 * 根据taskId将运单查出来
	 * <用来判断是否使用标准包装方案>
	 * 
	 * @author wangchen870
	 * @date 2019年4月19日 下午12:02:26
	 *
	 * @param batchNum
	 * @return
	 */
    List<BizOutstockPackmaterialTempEntity> queryWaybillByTaskId(String batchNum);

    /**
     * 通过taskid查询运单号并去重
     * <功能描述>
     * 
     * @author wangchen870
     * @date 2019年5月17日 上午9:29:41
     *
     * @param batchNum
     * @return
     */
    List<BizOutstockPackmaterialTempEntity> queryDistinctWaybillNoBytaskId(String batchNum);

}
