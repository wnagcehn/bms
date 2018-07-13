package com.jiuyescm.bms.general.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.google.common.collect.Maps;
import com.jiuyescm.bms.general.entity.PackCostReportEntity;
import com.jiuyescm.bms.general.service.IPackCostReportService;
import com.jiuyescm.cfm.persistence.mybatis.MyBatisDao;

@Service("packCostReportService")
public class PackCostReportServiceImpl extends MyBatisDao<PackCostReportEntity>  implements IPackCostReportService{

	@Override
	public List<PackCostReportEntity> queryAllByYearMonth(int year, int month) {
		Map<String,Object> map=Maps.newHashMap();
		map.put("year", year);
		map.put("month", month);
		return this.selectList("com.jiuyescm.bms.general.mapper.PackCostReportMapper.queryAllByYearMonth", map);
	}

	@Override
	public List<PackCostReportEntity> queryAllByYearMonth(int year,
			int startMonth, int endMonth) {
		Map<String,Object> map=Maps.newHashMap();
		map.put("year", year);
		map.put("startMonth", startMonth);
		map.put("endMonth", endMonth);
		return this.selectList("com.jiuyescm.bms.general.mapper.PackCostReportMapper.queryAllByYearAndMonthArea", map);
	}

	@Override
	public List<PackCostReportEntity> queryCostReportEntity(Map<String, Object> map) {
		
		return this.selectList("com.jiuyescm.bms.general.mapper.PackCostReportMapper.queryPackCostEntity", map);
	}
	
	

}
