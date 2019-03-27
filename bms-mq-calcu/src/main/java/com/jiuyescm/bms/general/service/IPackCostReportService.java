package com.jiuyescm.bms.general.service;

import java.util.List;
import java.util.Map;

import com.jiuyescm.bms.general.entity.PackCostReportEntity;

public interface IPackCostReportService {

	public List<PackCostReportEntity> queryAllByYearMonth(int year,int month);
	public List<PackCostReportEntity> queryAllByYearMonth(int year,int startMonth,int endMonth);
	
	public List<PackCostReportEntity> queryCostReportEntity(Map<String, Object> map);
}
