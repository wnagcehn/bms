package com.jiuyescm.bms.report.month.service.impl;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.report.month.repository.IBmsMonthReportRepository;
import com.jiuyescm.bms.report.month.service.IBmsMonthReportService;
import com.jiuyescm.bms.report.month.vo.BmsMonthReportVo;

@Service("bmsMonthReportService")
public class BmsMonthReportServiceImpl implements IBmsMonthReportService {

	private static final Logger logger = Logger.getLogger(BmsMonthReportServiceImpl.class.getName());
	
	@Autowired
    private IBmsMonthReportRepository bmsReportCompanyProfitRepository;

    @Override
    public PageInfo<BmsMonthReportVo> query(Map<String, Object> condition,int pageNo, int pageSize) {
        return bmsReportCompanyProfitRepository.query(condition, pageNo, pageSize);
    }

	@Override
	public List<BmsMonthReportVo> getList(Map<String, Object> condition) {
		 return bmsReportCompanyProfitRepository.getList(condition);
	}

	
}
