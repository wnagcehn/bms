/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.fees.abnormal.service;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.fees.abnormal.entity.FeesAbnormalEntity;

/**
 * 
 * @author stevenl
 * 
 */
public interface IFeesAbnormalService {

    PageInfo<FeesAbnormalEntity> query(Map<String, Object> condition, int pageNo,
            int pageSize);
    
    // 根据费用标号集合查询异常费用
    List<FeesAbnormalEntity> queryByFeesNos(Map<String, Object> condition);

    FeesAbnormalEntity findById(Long id);

    FeesAbnormalEntity save(FeesAbnormalEntity entity);

    int update(FeesAbnormalEntity entity);

    void delete(Long id);
    /**
     * 如果异常费用里边是免运费 则账单费用需要 费用加运费 
     * @param list
     * @return
     */
    void handlFeesAbnormal(Map<String,Object> param);

	void deleteDispatchBatchFees(List<FeesAbnormalEntity> list) throws Exception;

	void derateDispatchBatchAmount(List<FeesAbnormalEntity> list);

	void derateStorageBatchAmount(List<FeesAbnormalEntity> list);

	void deleteStorageBatchFees(List<FeesAbnormalEntity> list);

	/**
	 * 批量更新异常数据
	 * @param list
	 */
	int updateBatchBillNo(List<FeesAbnormalEntity> list);

	void deleteAbnormalBill(String billNo, String warehouseCode,
			String subjectCode) throws Exception;

	FeesAbnormalEntity sumDispatchAmount(
			String billNo);

	FeesAbnormalEntity sumDispatchChangeAmount(String billNo);

	/**
	 * 预账单理赔
	 * @param condition
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	PageInfo<FeesAbnormalEntity> queryPreBillAbnormal(Map<String, Object> condition, int pageNo,
            int pageSize);
	
	/**
	 * 预账单改地址
	 * @param condition
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	PageInfo<FeesAbnormalEntity> queryPreBillAbnormalChange(Map<String, Object> condition, int pageNo,
            int pageSize);
}
