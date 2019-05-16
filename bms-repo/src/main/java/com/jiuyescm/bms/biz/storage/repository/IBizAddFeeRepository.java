/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.biz.storage.repository;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.asyn.entity.BmsAsynCalcuTaskEntity;
import com.jiuyescm.bms.biz.storage.entity.BizAddFeeEntity;

/**
 * 
 * @author stevenl
 * 
 */
public interface IBizAddFeeRepository {

	public PageInfo<BizAddFeeEntity> query(Map<String, Object> condition,
            int pageNo, int pageSize);

    public BizAddFeeEntity findById(Long id);

    public BizAddFeeEntity save(BizAddFeeEntity entity);

    public BizAddFeeEntity update(BizAddFeeEntity entity);

    int saveList(List<BizAddFeeEntity> addList);
    
    int updateList(List<BizAddFeeEntity> updateList);
    
    BizAddFeeEntity selectOne(Map<String, Object> param);
    
    public List<BizAddFeeEntity> queryList(Map<String, Object> condition);
    
    public BizAddFeeEntity queryWms(Map<String, Object> param);
    
    /**
     * 定时任务查询业务数据
     * @param condition
     * @return
     */
	List<BizAddFeeEntity> querybizAddFee(Map<String, Object> condition);

	void delete(BizAddFeeEntity entity);
	
	/**
	 * 分组统计
	 * @param condition
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	PageInfo<BizAddFeeEntity> groupCount(Map<String, Object> condition, int pageNo, int pageSize);
	
	int updateByMap(Map<String, Object> condition);

	/**
	 * 重算（新）
	 * @param condition
	 * @return
	 */
	int retryCalcu(Map<String, Object> condition);
	
	public List<BmsAsynCalcuTaskEntity> queryTask(Map<String, Object> condition);

    int omssave(List<BizAddFeeEntity> addList);
    
    public BizAddFeeEntity queryPayNo(Map<String, Object> param);
}
