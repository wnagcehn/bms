package com.jiuyescm.bms.fees.abnormal.repository;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.fees.abnormal.entity.FeesAbnormalEntity;

public interface IFeesAbnormalNewRepository {
	public PageInfo<FeesAbnormalEntity> query(Map<String, Object> condition,
            int pageNo, int pageSize);
	
    public PageInfo<FeesAbnormalEntity> queryPay(Map<String, Object> condition, int pageNo,
            int pageSize);
    
    PageInfo<FeesAbnormalEntity> queryCount(Map<String, Object> condition, int pageNo,
            int pageSize);
    
    FeesAbnormalEntity queryOne(Map<String, Object> condition);
    
    int updateList(List<FeesAbnormalEntity> list);
    
    int updateOne(FeesAbnormalEntity entity);
}
