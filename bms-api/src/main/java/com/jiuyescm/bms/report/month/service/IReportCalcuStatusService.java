package com.jiuyescm.bms.report.month.service;

import java.util.Map;
import java.util.List;
import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.report.biz.entity.ReportCalcuStatusEntity;

/**
 * ..Service
 * @author wangchen
 * 
 */
public interface IReportCalcuStatusService {

    ReportCalcuStatusEntity findById(Long id);
	
    PageInfo<ReportCalcuStatusEntity> query(Map<String, Object> condition, int pageNo,
            int pageSize);

	List<ReportCalcuStatusEntity> query(Map<String, Object> condition);

    ReportCalcuStatusEntity save(ReportCalcuStatusEntity entity);

    ReportCalcuStatusEntity update(ReportCalcuStatusEntity entity);

    void delete(Long id);

}
