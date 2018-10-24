package com.jiuyescm.bms.fees.abnormal.service;

import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.fees.abnormal.entity.FeesAbnormalEntity;

public interface IFeesAbnormalNewService {
    PageInfo<FeesAbnormalEntity> query(Map<String, Object> condition, int pageNo,
            int pageSize);
}
