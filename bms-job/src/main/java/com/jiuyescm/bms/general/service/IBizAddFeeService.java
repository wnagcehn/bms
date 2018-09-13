/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.general.service;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.general.entity.BizAddFeeEntity;

/**
 * 
 * @author stevenl
 * 
 */
public interface IBizAddFeeService {

	public PageInfo<BizAddFeeEntity> query(Map<String, Object> condition,
            int pageNo, int pageSize);

    public BizAddFeeEntity findById(Long id);

    public BizAddFeeEntity save(BizAddFeeEntity entity);

    public BizAddFeeEntity update(BizAddFeeEntity entity);

    public void delete(Long id);
    
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
	
	/**
	 * 根据费用数据更新业务表
	 * @param updateList
	 * @return
	 */
	int updateByFees(List<BizAddFeeEntity> updateList);
}
