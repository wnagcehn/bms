package com.jiuyescm.bms.report.month.repository;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.report.month.entity.BmsMonthReportEntity;
import com.jiuyescm.bms.report.month.vo.BmsMonthReportVo;

public interface IBmsMonthReportRepository {

	public PageInfo<BmsMonthReportVo> query(Map<String, Object> condition, int pageNo, int pageSize);

	List<BmsMonthReportVo> getList(Map<String, Object> condition); 

}
