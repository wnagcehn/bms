/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.fees.abnormal.service;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.fees.abnormal.entity.FeesPayAbnormalEntity;

/**
 * 
 * @author stevenl
 * 
 */
public interface IFeesPayAbnormalService {

    PageInfo<FeesPayAbnormalEntity> query(Map<String, Object> condition, int pageNo,
            int pageSize);

    FeesPayAbnormalEntity findById(Long id);

    FeesPayAbnormalEntity save(FeesPayAbnormalEntity entity);

    int update(FeesPayAbnormalEntity entity);

    void delete(Long id);
    /**
     * 如果异常费用里边是免运费 则账单费用需要 费用加运费 
     * @param list
     * @return
     */
    void handlFeesAbnormal(Map<String,Object> param);

	void deleteDispatchBatchFees(List<FeesPayAbnormalEntity> list);

	void derateDispatchBatchAmount(List<FeesPayAbnormalEntity> list);

	void derateStorageBatchAmount(List<FeesPayAbnormalEntity> list);

	void deleteStorageBatchFees(List<FeesPayAbnormalEntity> list);

	/**
	 * 批量更新异常数据
	 * @param list
	 */
	int updateBatchBillNo(List<FeesPayAbnormalEntity> list);

	//根据条件查询应付异常费用
	public List<FeesPayAbnormalEntity> queryFeeByParam(Map<String,Object> param);
}
