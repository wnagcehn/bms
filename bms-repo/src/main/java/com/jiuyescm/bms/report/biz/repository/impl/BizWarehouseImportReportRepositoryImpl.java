/**
 * 
 */
package com.jiuyescm.bms.report.biz.repository.impl;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Repository;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.report.biz.entity.BizWarehouseImportReportEntity;
import com.jiuyescm.bms.report.biz.repository.IBizWarehouseImportReportRepository;
import com.jiuyescm.cfm.persistence.mybatis.MyBatisDao;

/**
 * @author yangss
 */
@Repository("bizWarehouseImportReportRepository")
public class BizWarehouseImportReportRepositoryImpl extends MyBatisDao<BizWarehouseImportReportEntity> implements IBizWarehouseImportReportRepository{

	@Override
	public PageInfo<BizWarehouseImportReportEntity> query(
			Map<String, Object> condition, int pageNo, int pageSize) {
		List<BizWarehouseImportReportEntity> list = selectList("com.jiuyescm.bms.report.biz.mapper.BizWarehouseImportReportMapper.query", condition, new RowBounds(pageNo, pageSize));
		PageInfo<BizWarehouseImportReportEntity> pageInfo=new PageInfo<>(list);
		return pageInfo;
	}
	
	@Override
	public List<BizWarehouseImportReportEntity> queryAll(Map<String, Object> condition) {
		return selectList("com.jiuyescm.bms.report.biz.mapper.BizWarehouseImportReportMapper.query", condition);
	}

}
