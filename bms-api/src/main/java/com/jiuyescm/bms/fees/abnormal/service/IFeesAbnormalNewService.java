package com.jiuyescm.bms.fees.abnormal.service;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.fees.abnormal.entity.FeesAbnormalEntity;

public interface IFeesAbnormalNewService {
    PageInfo<FeesAbnormalEntity> query(Map<String, Object> condition, int pageNo,
            int pageSize);
    
    PageInfo<FeesAbnormalEntity> queryPay(Map<String, Object> condition, int pageNo,
            int pageSize);
    
    PageInfo<FeesAbnormalEntity> queryCount(Map<String, Object> condition, int pageNo,
            int pageSize);
    
    FeesAbnormalEntity queryOne(Map<String, Object> condition);
    
    int updateList(List<FeesAbnormalEntity> list);
    
    int updateOne(FeesAbnormalEntity entity);
}
