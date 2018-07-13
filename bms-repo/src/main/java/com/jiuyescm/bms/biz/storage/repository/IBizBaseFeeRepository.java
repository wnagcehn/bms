/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.biz.storage.repository;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.biz.storage.entity.BizBaseFeeEntity;

/**
 * 
 * @author stevenl
 * 
 */
public interface IBizBaseFeeRepository {

	public PageInfo<BizBaseFeeEntity> query(Map<String, Object> condition,
            int pageNo, int pageSize);

    public BizBaseFeeEntity findById(Long id);

    public BizBaseFeeEntity save(BizBaseFeeEntity entity);

    public BizBaseFeeEntity update(BizBaseFeeEntity entity);

    public void delete(Long id);
    
    int saveList(List<BizBaseFeeEntity> addList);
    
    int updateList(List<BizBaseFeeEntity> updateList);
    
    BizBaseFeeEntity selectOne(Map<String, Object> param);
    
    public List<BizBaseFeeEntity> queryList(Map<String, Object> condition);


}
