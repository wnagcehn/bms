package com.jiuyescm.bms.report.month.service;

import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.biz.storage.entity.BizOutstockPackmaterialEntity;
import com.jiuyescm.bms.biz.storage.entity.BizWmsOutstockPackmaterialEntity;
import com.jiuyescm.bms.report.month.entity.BmsMaterialReportEntity;

public interface IBmsMaterialReportService {
    PageInfo<BmsMaterialReportEntity> query(Map<String, Object> condition, int pageNo, int pageSize);
    
    //查询BMS耗材出库原始数据
    PageInfo<BizOutstockPackmaterialEntity> queryBmsMaterial(Map<String, Object> condition, int pageNo, int pageSize);
    
    //查询WMS耗材出库原始数据
    PageInfo<BizWmsOutstockPackmaterialEntity> queryWmsMaterial(Map<String, Object> condition, int pageNo, int pageSize);
}
