package com.jiuyescm.bms.report.month.service.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.report.month.entity.ReportReceiptTargetEntity;
import com.jiuyescm.bms.report.month.repository.IReportReceiptTargetRepository;
import com.jiuyescm.bms.report.month.service.IReportReceiptTargetService;

@Service("reportReceiptTargetService")
public class ReportReceiptTargetServiceImpl implements IReportReceiptTargetService{
	
	@Autowired
    private IReportReceiptTargetRepository reportReceiptTargetRepository;
	
	@Override
	public PageInfo<ReportReceiptTargetEntity> queryAll(
			Map<String, Object> condition, int pageNo, int pageSize) {
		// TODO Auto-generated method stub
		return reportReceiptTargetRepository.queryAll(condition, pageNo, pageSize);
	}

}
