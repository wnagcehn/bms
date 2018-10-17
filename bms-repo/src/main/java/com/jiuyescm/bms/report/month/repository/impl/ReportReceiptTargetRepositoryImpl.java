package com.jiuyescm.bms.report.month.repository.impl;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Repository;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.report.month.entity.ReportReceiptTargetEntity;
import com.jiuyescm.bms.report.month.repository.IReportReceiptTargetRepository;
import com.jiuyescm.cfm.persistence.mybatis.MyBatisDao;

@Repository("reportReceiptTargetRepository")
public class ReportReceiptTargetRepositoryImpl extends MyBatisDao<ReportReceiptTargetEntity> implements IReportReceiptTargetRepository{

	@Override
	public PageInfo<ReportReceiptTargetEntity> queryAll(
			Map<String, Object> condition, int pageNo, int pageSize) {
		// TODO Auto-generated method stub
		List<ReportReceiptTargetEntity> list=selectList("com.jiuyescm.bms.report.month.ReportReceiptTargetMapper.queryAll", condition, new RowBounds(pageNo, pageSize));
		PageInfo<ReportReceiptTargetEntity> pageInfo=new PageInfo<>(list);
		return pageInfo;
	}

	@Override
	public int saveList(List<ReportReceiptTargetEntity> list) {
		// TODO Auto-generated method stub
		return insertBatch("com.jiuyescm.bms.report.month.ReportReceiptTargetMapper.saveList", list);
	}

	@Override
	public int delete(Map<String, Object> param) {
		// TODO Auto-generated method stub
		return delete("com.jiuyescm.bms.report.month.ReportReceiptTargetMapper.delete", param);
	}

}
