package com.jiuyescm.bms.report;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.report.month.entity.ReportCustomerDailyIncomeEntity;
import com.jiuyescm.bms.report.month.repository.IReportCustomerDailyIncomeRepository;
import com.jiuyescm.bms.report.service.IReportCustomerDailyIncomeService;
import com.jiuyescm.bms.report.vo.ReportCustomerDailyIncomeVo;

@Service("reportCustomerDailyIncomeService")
public class ReportCustomerDailyIncomeServiceImpl implements IReportCustomerDailyIncomeService{

	private static final Logger logger = Logger.getLogger(ReportCustomerDailyIncomeServiceImpl.class.getName());
	
	@Autowired
	private IReportCustomerDailyIncomeRepository reportCustomerDailyIncomeRepository;
	
	@Override
	public PageInfo<ReportCustomerDailyIncomeVo> queryGroup(
			Map<String,Object> queryCondition, int pageNo, int pageSize) throws Exception {
		PageInfo<ReportCustomerDailyIncomeVo> pageVoInfo=null;
		try{
			pageVoInfo=new PageInfo<ReportCustomerDailyIncomeVo>();
			PageInfo<ReportCustomerDailyIncomeEntity> pageInfo=reportCustomerDailyIncomeRepository.queryGroup(queryCondition, pageNo, pageSize);
			PropertyUtils.copyProperties(pageVoInfo, pageInfo);
			if(pageInfo!=null&&pageInfo.getList().size()>0){
				List<ReportCustomerDailyIncomeVo> list=new ArrayList<ReportCustomerDailyIncomeVo>();
				for(ReportCustomerDailyIncomeEntity entity:pageInfo.getList()){
					ReportCustomerDailyIncomeVo vo=new ReportCustomerDailyIncomeVo();
					PropertyUtils.copyProperties(vo, entity);
					list.add(vo);
				}
				pageVoInfo.setList(list);
			}
		}catch(Exception e){
			logger.error("queryGroup:",e);
			throw e;
		}
		return pageVoInfo;
	}
	
	@Override
	public List<ReportCustomerDailyIncomeVo> queryDetail(
			Map<String, Object> parameter) throws Exception {

		List<ReportCustomerDailyIncomeVo> voList=null;
		try{
			voList=new ArrayList<ReportCustomerDailyIncomeVo>();
			List<ReportCustomerDailyIncomeEntity> list=reportCustomerDailyIncomeRepository.queryDetail(parameter);
			for(ReportCustomerDailyIncomeEntity entity:list){
				ReportCustomerDailyIncomeVo vo=new ReportCustomerDailyIncomeVo();
				PropertyUtils.copyProperties(vo, entity);
				voList.add(vo);
			}
		}catch(Exception e){
			logger.error("queryDetail:",e);
			throw e;
		}
		return voList;
	}
	
	@Override
	public PageInfo<ReportCustomerDailyIncomeVo> queryDetailList(Map<String, Object> parameter, 
			int pageNo, int pageSize) throws Exception {
		PageInfo<ReportCustomerDailyIncomeVo> pageVoInfo=null;
		try{
			pageVoInfo=new PageInfo<ReportCustomerDailyIncomeVo>();
			PageInfo<ReportCustomerDailyIncomeEntity> pageInfo=reportCustomerDailyIncomeRepository.queryDetailList(parameter, pageNo, pageSize);
			PropertyUtils.copyProperties(pageVoInfo, pageInfo);
			if(pageInfo!=null&&pageInfo.getList().size()>0){
				List<ReportCustomerDailyIncomeVo> list=new ArrayList<ReportCustomerDailyIncomeVo>();
				for(ReportCustomerDailyIncomeEntity entity:pageInfo.getList()){
					ReportCustomerDailyIncomeVo vo=new ReportCustomerDailyIncomeVo();
					PropertyUtils.copyProperties(vo, entity);
					list.add(vo);
				}
				pageVoInfo.setList(list);
			}
		}catch(Exception e){
			logger.error("queryDetailList:",e);
			throw e;
		}
		return pageVoInfo;
	}

}
