package com.jiuyescm.bms.report.biz.repository;

import java.util.Map;
import java.util.List;
import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.report.biz.entity.ReportCalcuStatusEntity;

/**
 * ..Repository
 * @author wangchen
 * 
 */
public interface IReportCalcuStatusRepository {

    ReportCalcuStatusEntity findById(Long id);
	
	PageInfo<ReportCalcuStatusEntity> query(Map<String, Object> condition,
		int pageNo, int pageSize);

	List<ReportCalcuStatusEntity> query(Map<String, Object> condition);

    ReportCalcuStatusEntity save(ReportCalcuStatusEntity entity);

    ReportCalcuStatusEntity update(ReportCalcuStatusEntity entity);

    void delete(Long id);

}
