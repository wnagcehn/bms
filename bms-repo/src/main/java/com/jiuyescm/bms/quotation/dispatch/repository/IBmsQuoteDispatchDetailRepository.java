/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.quotation.dispatch.repository;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.quotation.dispatch.entity.BmsQuoteDispatchDetailEntity;

/**
 * 
 * @author stevenl
 * 
 */
public interface IBmsQuoteDispatchDetailRepository {

	public PageInfo<BmsQuoteDispatchDetailEntity> query(Map<String, Object> condition,
            int pageNo, int pageSize);

	public List<BmsQuoteDispatchDetailEntity> queryAllById(
			Map<String, Object> parameter);
	
	/**
	 * 通过temid查找对应的id
	 * @param temid
	 * @return
	 */
	public Integer getId(String temid);

    public int save(BmsQuoteDispatchDetailEntity entity);
    
    /**
     * 批量插入模板信息
     * @param list
     * @return
     */
    public int insertBatchTmp(List<BmsQuoteDispatchDetailEntity> list);

    public int update(BmsQuoteDispatchDetailEntity entity);
    
    public int deletePriceDistribution(BmsQuoteDispatchDetailEntity entity);
    
    public int removeDispatchByMap(Map<String,Object> condition);
    
    public void delete(Long id);

    public BmsQuoteDispatchDetailEntity queryOne(Map<String,Object> condition);
}
