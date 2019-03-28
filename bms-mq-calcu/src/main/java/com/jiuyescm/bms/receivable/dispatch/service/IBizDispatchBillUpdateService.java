/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.receivable.dispatch.service;

import java.util.List;
import java.util.Map;

import com.jiuyescm.bms.biz.dispatch.entity.BizDispatchBillUpdateEntity;

/**
 * 
 * @author stevenl
 * 
 */
public interface IBizDispatchBillUpdateService {

	List<BizDispatchBillUpdateEntity> queryData(Map<String, Object> map);

	int updateToIsCalculated(BizDispatchBillUpdateEntity entity);
}
