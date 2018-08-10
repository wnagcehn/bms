package com.jiuyescm.bms.biz.dispatch.web;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.annotation.DataResolver;
import com.bstek.dorado.data.provider.Page;
import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.biz.api.IDispatchService;
import com.jiuyescm.bms.biz.api.IOutstockService;
import com.jiuyescm.bms.biz.vo.BmsDispatchVo;
import com.jiuyescm.bms.biz.vo.OutstockInfoVo;
import com.jiuyescm.bms.biz.vo.OutstockRecordVo;
import com.jiuyescm.bms.common.tool.Session;
import com.jiuyescm.bms.fees.dispatch.entity.FeesReceiveDispatchEntity;
import com.jiuyescm.bms.fees.dispatch.service.IFeesReceiveDispatchService;
import com.jiuyescm.cfm.common.JAppContext;

@Controller("outStockController")
public class OutStockController {
	
	private static final Logger logger = Logger.getLogger(OutStockController.class.getName());
	
	@Resource
	private IDispatchService dispatchService;
	@Resource
	private IOutstockService outstockService;
	@Autowired
	private IFeesReceiveDispatchService service;
	
	/**
	 * 分页查询
	 * 
	 * @param page
	 * @param param
	 */
	@DataProvider
	public void queryAll(Page<BmsDispatchVo> page, Map<String, Object> param) {
		if (param == null){
			param = new HashMap<String, Object>();
		}
		
		PageInfo<BmsDispatchVo> pageInfo = dispatchService.queryAll(param, page.getPageNo(), page.getPageSize());
		if (pageInfo != null) {
			page.setEntities(pageInfo.getList());
			page.setEntityCount((int) pageInfo.getTotal());
		}
	}
	
	/**
	 * 更新
	 * @param datas
	 * @return
	 */
	@DataResolver
	public String updateEntity(BmsDispatchVo data){
		if(Session.isMissing()){
			return "长时间未操作，用户已失效，请重新登录再试！";
		}
		try {
			Timestamp nowdate = JAppContext.currentTimestamp();
			String userid=JAppContext.currentUserName();
				//判断是否生成费用，判断费用的状态是否为未过账
				if(StringUtils.isNotBlank(data.getFeesNo())){
					Map<String,Object> aCondition=new HashMap<>();
					aCondition.put("feesNo", data.getFeesNo());
					FeesReceiveDispatchEntity feesReceiveDispatchEntity = service.queryOne(aCondition);
					if (null != feesReceiveDispatchEntity) {
						//获取此时的费用状态
						String status=String.valueOf(feesReceiveDispatchEntity.getStatus());
						if(status.equals("1")){
							return "该费用已过账，无法调整重量";
						}
					}
				}
				//此为修改业务数据  
				//业务数据有生成过费用，且没有过账的话,则允许调整重量,且调整完后,状态重置为未计算
				//如果没有过账的话,就允许调整重量,且调整完后,状态重置为未计算,定时任务重新扫描到并重新生成应收费用.
				data.setIsCalculated("99");
				data.setLastModifier(userid);
				data.setLastModifyTime(nowdate);
				outstockService.updateBizData(data);			
		} catch (Exception e) {
			logger.error("更新失败:");
			return "更新失败：" + e.getMessage();
		}
		return "更新成功";
	}
	
	/**
	 * 记录分页查询
	 * 
	 * @param page
	 * @param param
	 */
	@DataProvider
	public void queryRecord(Page<OutstockRecordVo> page, Map<String, Object> param) {
		if (param == null){
			param = new HashMap<String, Object>();
		}
		
		PageInfo<OutstockRecordVo> pageInfo = null;
		if (pageInfo != null) {
			page.setEntities(pageInfo.getList());
			page.setEntityCount((int) pageInfo.getTotal());
		}
	}
		
}
