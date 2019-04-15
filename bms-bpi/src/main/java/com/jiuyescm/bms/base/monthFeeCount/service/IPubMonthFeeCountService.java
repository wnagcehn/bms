/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.base.monthFeeCount.service;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.base.monthFeeCount.vo.PubMonthFeeCountVo;

public interface IPubMonthFeeCountService {
	
    PageInfo<PubMonthFeeCountVo> queryAll(Map<String, Object> condition, int pageNo,
	            int pageSize);

	List<PubMonthFeeCountVo> query(Map<String, Object> condition);
}
