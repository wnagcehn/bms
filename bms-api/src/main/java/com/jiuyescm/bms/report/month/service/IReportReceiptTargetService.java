package com.jiuyescm.bms.report.month.service;

import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.report.month.entity.ReportReceiptTargetEntity;

public interface IReportReceiptTargetService {
	
    PageInfo<ReportReceiptTargetEntity> queryAll(Map<String, Object> condition, int pageNo,
            int pageSize);
}
