package com.jiuyescm.bms.report.month.repository;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.report.month.entity.ReportReceiptTargetEntity;

public interface IReportReceiptTargetRepository {
    PageInfo<ReportReceiptTargetEntity> queryAll(Map<String, Object> condition, int pageNo,
            int pageSize);
    
    int saveList(List<ReportReceiptTargetEntity> list);
    
    int delete(Map<String,Object> param);
}
