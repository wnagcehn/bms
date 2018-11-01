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
    
    /**
     * 明细查询
     * @param condition
     * @return
     */
	List<ReportCalcuStatusEntity> queryDetail(Map<String, Object> condition);
	
	/**
	 * 新分页查询
	 * @param condition
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	PageInfo<ReportCalcuStatusEntity> queryAll(Map<String, Object> condition, int pageNo, int pageSize);
	
	/**
	 * 新不分页查询
	 * @param condition
	 * @return
	 */
	List<ReportCalcuStatusEntity> queryAll(Map<String, Object> condition);

}
