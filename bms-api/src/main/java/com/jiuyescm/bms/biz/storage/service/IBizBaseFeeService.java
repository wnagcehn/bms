/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.biz.storage.service;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.biz.storage.entity.BizBaseFeeEntity;

/**
 * 
 * @author stevenl
 * 
 */
public interface IBizBaseFeeService {

    PageInfo<BizBaseFeeEntity> query(Map<String, Object> condition, int pageNo,int pageSize);

    BizBaseFeeEntity findById(Long id);

    BizBaseFeeEntity save(BizBaseFeeEntity entity);

    BizBaseFeeEntity update(BizBaseFeeEntity entity);

    void delete(Long id);
    
    int saveorUpdateList(List<BizBaseFeeEntity> addList,List<BizBaseFeeEntity> updateList);
    
    void  updateFee(List<BizBaseFeeEntity> updateList);
    
    public List<BizBaseFeeEntity> queryList(Map<String, Object> condition);

}
