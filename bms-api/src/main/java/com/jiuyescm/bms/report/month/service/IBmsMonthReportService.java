package com.jiuyescm.bms.report.month.service;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.report.month.vo.BmsMonthReportVo;



public interface IBmsMonthReportService {

    PageInfo<BmsMonthReportVo> query(Map<String, Object> condition, int pageNo, int pageSize);

    List<BmsMonthReportVo> getList(Map<String, Object> condition);
}
