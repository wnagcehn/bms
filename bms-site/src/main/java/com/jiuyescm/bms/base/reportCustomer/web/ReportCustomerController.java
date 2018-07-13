package com.jiuyescm.bms.base.reportCustomer.web;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.annotation.DataResolver;
import com.bstek.dorado.data.provider.Page;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import com.jiuyescm.bms.base.reportCustomer.service.IReportWarehouseCustomerService;
import com.jiuyescm.bms.base.reportCustomer.vo.ReportWarehouseCustomerVo;
import com.jiuyescm.bms.common.constants.MessageConstant;
import com.jiuyescm.bms.common.system.ResponseVo;
import com.jiuyescm.bms.common.tool.Session;
import com.jiuyescm.cfm.common.JAppContext;

@Controller("reportCustomerController#updateList")
public class ReportCustomerController {
	
	private static final Logger logger = Logger.getLogger(ReportCustomerController.class.getName());

	
	@Autowired
	private IReportWarehouseCustomerService reportWarehouseCustomerService;
	
	@DataProvider
	public void query(Page<ReportWarehouseCustomerVo> page,Map<String, Object> param){
		if(param==null){
			param=Maps.newHashMap();
		}
		PageInfo<ReportWarehouseCustomerVo> tmpPageInfo = new PageInfo<ReportWarehouseCustomerVo>();
		List<ReportWarehouseCustomerVo> list=reportWarehouseCustomerService.query(param);
		if (tmpPageInfo != null) {
			page.setEntities(list);
			page.setEntityCount((int) list.size());
		}
	}
	
	
	@DataResolver
	public ResponseVo save(ReportWarehouseCustomerVo entity){
		try{
			if(Session.isMissing()){
				return new ResponseVo(ResponseVo.FAIL, MessageConstant.SESSION_INVALID_MSG);
			}else if(entity == null){
				return new ResponseVo(ResponseVo.FAIL, MessageConstant.PAGE_PARAM_ERROR_MSG);
			}
			//校验是否为标准报价
			entity.setCreateTime(JAppContext.currentTimestamp());
			entity.setCreator(JAppContext.currentUserName());
			entity.setLastModifier(JAppContext.currentUserName());
			entity.setLastModifyTime(JAppContext.currentTimestamp());
			entity.setDelFlag("0");
			reportWarehouseCustomerService.save(entity);
			
		}catch(Exception e){
			logger.error("记录日志失败,失败原因:"+e.getMessage());
		}
		return new ResponseVo(ResponseVo.SUCCESS, MessageConstant.OPERATOR_SUCCESS_MSG);
	}
	
	@DataResolver
	public ResponseVo update(ReportWarehouseCustomerVo temp) {
		if(Session.isMissing()){
			return new ResponseVo(ResponseVo.FAIL, MessageConstant.SESSION_INVALID_MSG);
		}else if(temp == null){
			return new ResponseVo(ResponseVo.FAIL, MessageConstant.PAGE_PARAM_ERROR_MSG);
		}
		
		temp.setLastModifier(JAppContext.currentUserName());
		temp.setLastModifyTime(JAppContext.currentTimestamp());
		reportWarehouseCustomerService.update(temp);
		
		return new ResponseVo(ResponseVo.SUCCESS, MessageConstant.OPERATOR_SUCCESS_MSG);
	}
	
	@DataResolver
	public ResponseVo updateList(List<ReportWarehouseCustomerVo> list) {
		if(Session.isMissing()){
			return new ResponseVo(ResponseVo.FAIL, MessageConstant.SESSION_INVALID_MSG);
		}else if(list == null){
			return new ResponseVo(ResponseVo.FAIL, MessageConstant.PAGE_PARAM_ERROR_MSG);
		}
		for(ReportWarehouseCustomerVo vo:list){
			vo.setDelFlag("1");
			vo.setLastModifier(JAppContext.currentUserName());
			vo.setLastModifyTime(JAppContext.currentTimestamp());
		}
			
		reportWarehouseCustomerService.updateList(list);
		
		return new ResponseVo(ResponseVo.SUCCESS, MessageConstant.OPERATOR_SUCCESS_MSG);
	}
	
}
