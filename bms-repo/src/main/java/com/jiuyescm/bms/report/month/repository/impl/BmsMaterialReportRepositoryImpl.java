package com.jiuyescm.bms.report.month.repository.impl;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.biz.storage.entity.BizOutstockPackmaterialEntity;
import com.jiuyescm.bms.biz.storage.entity.BizWmsOutstockPackmaterialEntity;
import com.jiuyescm.bms.report.month.entity.BmsMaterialReportEntity;
import com.jiuyescm.bms.report.month.repository.IBmsMaterialReportRepository;
import com.jiuyescm.cfm.persistence.mybatis.MyBatisDao;

@Repository("bmsMaterialReportRepository")
public class BmsMaterialReportRepositoryImpl extends MyBatisDao<BmsMaterialReportEntity> implements IBmsMaterialReportRepository{

	@Override
	public PageInfo<BmsMaterialReportEntity> query(
			Map<String, Object> condition, int pageNo, int pageSize) {
		// TODO Auto-generated method stub
		List<BmsMaterialReportEntity> list=selectList("com.jiuyescm.bms.report.month.mapper.BmsMaterialReportMapper.query", condition, new RowBounds(pageNo, pageSize));
		PageInfo<BmsMaterialReportEntity> pageInfo=new PageInfo<>(list);
		return pageInfo;
	}

	@Override
	public PageInfo<BizOutstockPackmaterialEntity> queryBmsMaterial(
			Map<String, Object> condition, int pageNo, int pageSize) {
		// TODO Auto-generated method stub
		SqlSession session=this.getSqlSessionTemplate();
		List<BizOutstockPackmaterialEntity> list=session.selectList("com.jiuyescm.bms.report.month.mapper.BmsMaterialReportMapper.queryBmsMaterial", condition, new RowBounds(pageNo, pageSize));
		PageInfo<BizOutstockPackmaterialEntity> pageInfo=new PageInfo<>(list);
		return pageInfo;
	
	}

	@Override
	public PageInfo<BizWmsOutstockPackmaterialEntity> queryWmsMaterial(
			Map<String, Object> condition, int pageNo, int pageSize) {
		// TODO Auto-generated method stub
		SqlSession session=this.getSqlSessionTemplate();
		List<BizWmsOutstockPackmaterialEntity> list=session.selectList("com.jiuyescm.bms.report.month.mapper.BmsMaterialReportMapper.queryWmsMaterial", condition, new RowBounds(pageNo, pageSize));
		PageInfo<BizWmsOutstockPackmaterialEntity> pageInfo=new PageInfo<>(list);
		return pageInfo;
	
	}

}
