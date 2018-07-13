package com.jiuyescm.bms.report.month.repository.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Repository;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.report.month.repository.IBmsMonthReportRepository;
import com.jiuyescm.bms.report.month.vo.BmsMonthReportVo;
import com.jiuyescm.cfm.persistence.mybatis.MyBatisDao;

@Repository("bmsReportCompanyProfitRepository")
public class BmsMonthReportRepositoryImpl extends MyBatisDao<BmsMonthReportVo> implements IBmsMonthReportRepository {

	private static final Logger logger = Logger.getLogger(BmsMonthReportRepositoryImpl.class.getName());

	public BmsMonthReportRepositoryImpl() {
		super();
	}
	
	@Override
    public PageInfo<BmsMonthReportVo> query(Map<String, Object> condition, int pageNo, int pageSize) {
		
        List<BmsMonthReportVo> list = selectList("com.jiuyescm.bms.report.month.mapper.BmsMonthReportMapper.query", condition, new RowBounds(pageNo, pageSize));
        for(int i=0;i<list.size();i++){
        	BmsMonthReportVo monthReportVo=list.get(i);
        	monthReportVo.setSumAmount(monthReportVo.getJanuaryAmount().
        	add(monthReportVo.getFebruaryAmount()).add(monthReportVo.getMarchAmount()).add(monthReportVo.getAprilAmount()).
        	add(monthReportVo.getMayAmount()).add(monthReportVo.getJuneAmount()).add(monthReportVo.getJulyAmount()).
        	add(monthReportVo.getAugustAmount()).add(monthReportVo.getSeptemberAmount()).add(monthReportVo.getOctoberAmount()).
        	add(monthReportVo.getNovemberAmount()).add(monthReportVo.getDecemberAmount()));
        }
        PageInfo<BmsMonthReportVo> pageInfo = new PageInfo<BmsMonthReportVo>(list);
        return pageInfo;
        
    }

	@Override
	public List<BmsMonthReportVo> getList(Map<String, Object> condition) {
		 List<BmsMonthReportVo> list = selectList("com.jiuyescm.bms.report.month.mapper.BmsMonthReportMapper.query",condition);
		 return list;
	}

    
	
}
