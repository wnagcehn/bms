/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.billimport.service;

import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.billimport.entity.BillFeesReceiveDispatchTempEntity;

/**
 * 
 * @author stevenl
 * 
 */
public interface IBillFeesReceiveDispatchTempService {

    PageInfo<BillFeesReceiveDispatchTempEntity> query(Map<String, Object> condition, int pageNo,
            int pageSize);

    BillFeesReceiveDispatchTempEntity findById(Long id);

    BillFeesReceiveDispatchTempEntity save(BillFeesReceiveDispatchTempEntity entity);

    BillFeesReceiveDispatchTempEntity update(BillFeesReceiveDispatchTempEntity entity);

    void delete(Long id);

}
