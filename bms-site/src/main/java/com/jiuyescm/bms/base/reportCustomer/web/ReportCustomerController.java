package com.jiuyescm.bms.base.reportCustomer.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
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

@Controller("reportCustomerController")
public class ReportCustomerController {
	
	private static final Logger logger = Logger.getLogger(ReportCustomerController.class.getName());

	
	@Autowired
	private IReportWarehouseCustomerService reportWarehouseCustomerService;
	
	/**
	 * 查询
	 * @param page
	 * @param param
	 */
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
	
	/**
	 * 保存
	 * @param entity
	 * @return
	 */
	@DataResolver
	public ResponseVo save(ReportWarehouseCustomerVo entity){
		try{
			if(Session.isMissing()){
				return new ResponseVo(ResponseVo.FAIL, MessageConstant.SESSION_INVALID_MSG);
			}else if(entity == null){
				return new ResponseVo(ResponseVo.FAIL, MessageConstant.PAGE_PARAM_ERROR_MSG);
			}
			//重复性校验
			//根据仓库精确匹配
			if(StringUtils.isNotBlank(entity.getWarehouseCode())){
				Map<String,Object> condition=new HashMap<String,Object>();
				condition.put("createMonth", entity.getCreateMonth());
				condition.put("customerId", entity.getCustomerId());
				condition.put("warehouseCode", entity.getWarehouseCode());
				condition.put("bizType", entity.getBizType());
				ReportWarehouseCustomerVo vo=reportWarehouseCustomerService.queryOne(condition);
				if(vo!=null){
					return new ResponseVo(ResponseVo.FAIL, "商家仓库当前月份已存在");
				}
				
				condition=new HashMap<String,Object>();
				condition.put("createMonth", entity.getCreateMonth());
				condition.put("customerId", entity.getCustomerId());
				condition.put("warehouseCode", "");
				condition.put("bizType", entity.getBizType());
				ReportWarehouseCustomerVo vo2=reportWarehouseCustomerService.queryOne(condition);
				if(vo2!=null){
					return new ResponseVo(ResponseVo.FAIL, "商家已存在");
				}
			}
			
			if(StringUtils.isBlank(entity.getWarehouseCode())){
				Map<String,Object> condition=new HashMap<String,Object>();
				condition.put("createMonth", entity.getCreateMonth());
				condition.put("customerId", entity.getCustomerId());
				condition.put("bizType", entity.getBizType());
				ReportWarehouseCustomerVo vo=reportWarehouseCustomerService.queryOne(condition);
				if(vo!=null){
					return new ResponseVo(ResponseVo.FAIL, "商家已存在");
				}
			}
			
			
			//不根据仓库匹配			
			entity.setCreateTime(JAppContext.currentTimestamp());
			entity.setCreator(JAppContext.currentUserName());
			entity.setLastModifier(JAppContext.currentUserName());
			entity.setLastModifyTime(JAppContext.currentTimestamp());
			entity.setDelFlag("0");
			reportWarehouseCustomerService.save(entity);
			
		}catch(Exception e){
			logger.error("新增失败,失败原因:"+e.getMessage());
			return new ResponseVo(ResponseVo.FAIL, "新增失败");
		}
		return new ResponseVo(ResponseVo.SUCCESS, "新增成功");
	}
	
	/**
	 * 更新
	 * @param temp
	 * @return
	 */
	@DataResolver
	public ResponseVo update(ReportWarehouseCustomerVo temp) {
		try{
			if(Session.isMissing()){
				return new ResponseVo(ResponseVo.FAIL, MessageConstant.SESSION_INVALID_MSG);
			}else if(temp == null){
				return new ResponseVo(ResponseVo.FAIL, MessageConstant.PAGE_PARAM_ERROR_MSG);
			}
			
			Map<String,Object> condition=new HashMap<String,Object>();
			//根据仓库精确匹配
			if(StringUtils.isNotBlank(temp.getWarehouseCode())){
				condition.put("createMonth", temp.getCreateMonth());
				condition.put("customerId", temp.getCustomerId());
				condition.put("warehouseCode", temp.getWarehouseCode());
				condition.put("bizType", temp.getBizType());
				ReportWarehouseCustomerVo vo=reportWarehouseCustomerService.queryOne(condition);
				if(vo!=null && vo.getId()!=null && !vo.getId().equals(temp.getId())){
					return new ResponseVo(ResponseVo.FAIL, "商家仓库当前月份已存在");
				}
			}
			condition=new HashMap<String,Object>();
			condition.put("createMonth", temp.getCreateMonth());
			condition.put("customerId", temp.getCustomerId());
			condition.put("warehouseCode", "");
			condition.put("bizType", temp.getBizType());
			ReportWarehouseCustomerVo vo=reportWarehouseCustomerService.queryOne(condition);
			if(vo!=null && vo.getId()!=null && !vo.getId().equals(temp.getId())){
				return new ResponseVo(ResponseVo.FAIL, "商家已存在");
			}
			
			
			temp.setLastModifier(JAppContext.currentUserName());
			temp.setLastModifyTime(JAppContext.currentTimestamp());
			reportWarehouseCustomerService.update(temp);
			
		}catch(Exception e){
			logger.error("更新失败,失败原因:"+e.getMessage());
			return new ResponseVo(ResponseVo.FAIL, "更新失败");
		}
		return new ResponseVo(ResponseVo.SUCCESS, "更新成功");	
	}
	
	/**
	 * 批量更新
	 * @param list
	 * @return
	 */
	@DataResolver
	public ResponseVo updateList(List<ReportWarehouseCustomerVo> list) {
		try{
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
			
		}catch(Exception e){
			logger.error("批量删除失败,失败原因:"+e.getMessage());
			return new ResponseVo(ResponseVo.FAIL, "批量删除失败");
		}
		return new ResponseVo(ResponseVo.SUCCESS, "批量删除成功");
	}
	
	/**
	 * 批量复制
	 * @param list
	 * @return
	 */
	@DataResolver
	public String copyList(List<ReportWarehouseCustomerVo> list) {
		try{
			if(Session.isMissing()){
				return MessageConstant.SESSION_INVALID_MSG;
			}else if(list == null){
				return MessageConstant.PAGE_PARAM_ERROR_MSG;
			}
			
			//判断数据是否已存在
			for(ReportWarehouseCustomerVo vo:list){
				//重复性校验
				//根据仓库精确匹配
				if(StringUtils.isNotBlank(vo.getWarehouseCode())){
					Map<String,Object> condition=new HashMap<String,Object>();
					condition.put("createMonth", vo.getCreateMonth());
					condition.put("customerId", vo.getCustomerId());
					condition.put("warehouseCode", vo.getWarehouseCode());
					condition.put("bizType", vo.getBizType());
					ReportWarehouseCustomerVo reportVo=reportWarehouseCustomerService.queryOne(condition);
					if(reportVo!=null){
						return "存在重复数据";
					}
				}
				Map<String,Object> condition=new HashMap<String,Object>();
				condition.put("createMonth", vo.getCreateMonth());
				condition.put("customerId", vo.getCustomerId());
				condition.put("warehouseCode", "");
				condition.put("bizType", vo.getBizType());
				ReportWarehouseCustomerVo reportVo=reportWarehouseCustomerService.queryOne(condition);
				if(reportVo!=null){
					return "存在重复数据";
				}
			}
			
			for(ReportWarehouseCustomerVo vo:list){
				vo.setCreateTime(JAppContext.currentTimestamp());
				vo.setCreator(JAppContext.currentUserName());
				vo.setLastModifier(JAppContext.currentUserName());
				vo.setLastModifyTime(JAppContext.currentTimestamp());
				vo.setDelFlag("0");
			}
			reportWarehouseCustomerService.saveList(list);
			
		}catch(Exception e){
			logger.error("批量复制失败,失败原因:"+e.getMessage());
			return MessageConstant.OPERATOR_FAIL_MSG;
		}
		return "SUCC";	
	}
	
	
}
