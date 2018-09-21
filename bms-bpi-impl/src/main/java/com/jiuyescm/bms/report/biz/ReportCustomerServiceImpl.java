/**
 * 
 */
package com.jiuyescm.bms.report.biz;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.report.biz.entity.ReportCustomerInOutEntity;
import com.jiuyescm.bms.report.biz.repository.IReportCustomerRepository;
import com.jiuyescm.bms.report.biz.service.IReportCustomerService;
import com.jiuyescm.bms.report.vo.ReportCustomerInOutVo;

/**
 * @author yangss
 */
@Service("reportCustomerService")
public class ReportCustomerServiceImpl implements IReportCustomerService{
	
	private static final Logger logger = Logger.getLogger(ReportCustomerServiceImpl.class.getName());

	@Autowired
	private IReportCustomerRepository reportCustomerRepository;

	@Override
	public PageInfo<ReportCustomerInOutVo> queryIn(
			Map<String, Object> condition, int pageNo, int pageSize) {
		// TODO Auto-generated method stub
		PageInfo<ReportCustomerInOutVo> pageVoInfo = null;
		try {
			pageVoInfo = new PageInfo<ReportCustomerInOutVo>();
			PageInfo<ReportCustomerInOutEntity> pageInfo = reportCustomerRepository.queryIn(condition, pageNo, pageSize);
			
			PropertyUtils.copyProperties(pageVoInfo, pageInfo);
			if(pageInfo!=null&&pageInfo.getList().size()>0){
				List<ReportCustomerInOutVo> list=new ArrayList<ReportCustomerInOutVo>();
				for(ReportCustomerInOutEntity entity : pageInfo.getList()){
					ReportCustomerInOutVo vo = new ReportCustomerInOutVo();
					PropertyUtils.copyProperties(vo, entity);
					list.add(vo);
				}
				pageVoInfo.setList(list);
			}
		} catch (Exception e) {
			logger.error("各仓业务数据导入查询异常:",e);
		}
		return pageVoInfo;
	}

	@Override
	public PageInfo<ReportCustomerInOutVo> queryOut(
			Map<String, Object> condition, int pageNo, int pageSize) {
		// TODO Auto-generated method stub
		PageInfo<ReportCustomerInOutVo> pageVoInfo = null;
		try {
			pageVoInfo = new PageInfo<ReportCustomerInOutVo>();
			PageInfo<ReportCustomerInOutEntity> pageInfo = reportCustomerRepository.queryOut(condition, pageNo, pageSize);
			
			PropertyUtils.copyProperties(pageVoInfo, pageInfo);
			if(pageInfo!=null&&pageInfo.getList().size()>0){
				List<ReportCustomerInOutVo> list=new ArrayList<ReportCustomerInOutVo>();
				for(ReportCustomerInOutEntity entity : pageInfo.getList()){
					ReportCustomerInOutVo vo = new ReportCustomerInOutVo();
					PropertyUtils.copyProperties(vo, entity);
					list.add(vo);
				}
				pageVoInfo.setList(list);
			}
		} catch (Exception e) {
			logger.error("各仓业务数据导入查询异常:",e);
		}
		return pageVoInfo;
	}
	


}
