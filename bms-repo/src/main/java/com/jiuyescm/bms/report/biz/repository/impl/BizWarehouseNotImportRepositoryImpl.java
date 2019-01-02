/**
 * 
 */
package com.jiuyescm.bms.report.biz.repository.impl;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Repository;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.report.biz.entity.BizWarehouseNotImportEntity;
import com.jiuyescm.bms.report.biz.repository.IBizWarehouseNotImportRepository;
import com.jiuyescm.cfm.persistence.mybatis.MyBatisDao;

/**
 * @author liuzhicheng
 */
@Repository("bizWarehouseNotImportRepository")
public class BizWarehouseNotImportRepositoryImpl extends MyBatisDao<BizWarehouseNotImportEntity> implements IBizWarehouseNotImportRepository{

	@SuppressWarnings("unchecked")
	@Override
	public PageInfo<BizWarehouseNotImportEntity> queryNotImport(
			Map<String, Object> condition,int pageNo, int pageSize) {
		List<BizWarehouseNotImportEntity> list = selectList("com.jiuyescm.bms.report.biz.mapper.BizWarehouseImportReportMapper.queryNotImport", condition,new RowBounds(pageNo, pageSize));
		PageInfo<BizWarehouseNotImportEntity> page=new PageInfo<>(list);
		return page;
	}

}
