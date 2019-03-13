package com.jiuyescm.bms.base.monthFeeCount.repository;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.base.monthFeeCount.PubMonthFeeCountEntity;

public interface IPubMonthFeeCountRepository {
    PageInfo<PubMonthFeeCountEntity> queryAll(Map<String, Object> condition, int pageNo,
            int pageSize);

    List<PubMonthFeeCountEntity> query(Map<String, Object> condition);
}
