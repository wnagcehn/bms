/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.asyn.web;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.activemq.filter.function.makeListFunction;
import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import javax.annotation.Resource;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Controller;

import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.annotation.DataResolver;
import com.bstek.dorado.data.provider.Page;
import com.bstek.dorado.data.util.DataUtils;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import com.jiuyescm.bms.asyn.service.IBmsCorrectAsynTaskService;
import com.jiuyescm.bms.asyn.vo.BmsCorrectAsynTaskVo;
import com.jiuyescm.bms.base.group.service.IBmsGroupCustomerService;
import com.jiuyescm.bms.base.group.service.IBmsGroupService;
import com.jiuyescm.bms.base.group.vo.BmsGroupVo;
import com.jiuyescm.bms.common.enumtype.BmsCorrectAsynTaskStatusEnum;
import com.jiuyescm.bms.file.asyn.BmsCorrectAsynTaskEntity;
import com.jiuyescm.cfm.common.JAppContext;
import com.jiuyescm.common.utils.DateUtil;

/**
 * 
 * @author stevenl
 * 
 */
@Controller("bmsCorrectAsynTaskController")
public class BmsCorrectAsynTaskController {

	private static final Logger logger = Logger.getLogger(BmsCorrectAsynTaskController.class.getName());

	@Resource
	private IBmsCorrectAsynTaskService bmsCorrectAsynTaskService;

	@Resource
	private JmsTemplate jmsQueueTemplate;
	
	@Resource
	private IBmsGroupCustomerService bmsGroupCustomerService;
	
	@Autowired 
	private IBmsGroupService bmsGroupService;
	
	private static final String BMS_CORRECT_ASYN_TASK = "BMS.CORRECT.ASYN.TASK";
	
	@DataProvider
	public void query(Page<BmsCorrectAsynTaskVo> page, Map<String, Object> param) throws Exception {
		if(param==null){
			return;
		}
		String year="";
		String month="";
		if(param.containsKey("year")&&param.containsKey("month")){
			year=param.get("year").toString();
			month=param.get("month").toString();
		}
		if(StringUtils.isNotBlank(year)&&StringUtils.isNotBlank(month)){
			String startDateStr=year+"-"+month+"-01 00:00:00";  
		    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
		    Date startDate = sdf.parse(startDateStr);  
		    Date endDate=DateUtils.addMonths(startDate,1);
		    param.put("startDate", startDate);
		    param.put("endDate", endDate);
		}
		PageInfo<BmsCorrectAsynTaskVo> pageInfo = bmsCorrectAsynTaskService.query(param, page.getPageNo(), page.getPageSize());
		if (pageInfo != null) {
			List<BmsCorrectAsynTaskVo> voList=pageInfo.getList();
			initList(voList);
			page.setEntities(voList);
			page.setEntityCount((int) pageInfo.getTotal());
		}
	}
	private void initList(List<BmsCorrectAsynTaskVo> voList){
		for(BmsCorrectAsynTaskVo voEntity:voList){
			if(voEntity.getTaskRate()!=null){
				voEntity.setTaskProcess(String.valueOf(voEntity.getTaskRate())+"%");
			}else{
				voEntity.setTaskProcess("%");
			}
		}
	}
	@DataProvider
	public Map<String,String> getStatusMap(){
		return BmsCorrectAsynTaskStatusEnum.getMap();
	}
	private String getUuid(){
		String uuid = java.util.UUID.randomUUID().toString(); //获取UUID并转化为String对象  
	    uuid = uuid.replace("-", "");       
	    return uuid;
	}
	@DataResolver
	public Map<String, String> save(BmsCorrectAsynTaskVo voEntity) throws Exception {
		Map<String, String> result = new HashMap<>();
		SimpleDateFormat format=new SimpleDateFormat("yyyyMMddHHmmss");
		//String taskId=format.format(JAppContext.currentTimestamp())+"_"+JAppContext.currentUserID();
		if(StringUtils.isNotBlank(voEntity.getYear())&&StringUtils.isNotBlank(voEntity.getMonth())){
			String startDateStr=voEntity.getYear()+"-"+voEntity.getMonth()+"-01 00:00:00";  
		    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
		    Date startDate = sdf.parse(startDateStr);  
		    Date endDate=DateUtils.addMonths(startDate,1);
		    voEntity.setStartDate(startDate);
		    voEntity.setEndDate(endDate);
		}
		
		// 查询不需要运单纠正的商家
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("groupCode", "notpartin_orderCorrent_customer");
		map.put("bizType", "group_customer");
		BmsGroupVo bmsCancelCus=bmsGroupService.queryOne(map);
		List<String> notCurCustList = bmsGroupCustomerService.queryCustomerByGroupId(bmsCancelCus.getId());
		
		if(StringUtils.isBlank(voEntity.getCustomerId())){
			Map<String,Object> conditionMap=Maps.newHashMap();
			conditionMap.put("startDate", voEntity.getStartDate());
			conditionMap.put("endDate", voEntity.getEndDate());
			List<String> customerIdList=bmsCorrectAsynTaskService.queryCorrectCustomerList(conditionMap);
			List<BmsCorrectAsynTaskVo> voList=new ArrayList<BmsCorrectAsynTaskVo>();
			if(customerIdList==null||customerIdList.size()==0){
				return result;
			}
			
			if (notCurCustList != null && notCurCustList.size() > 0) { 
				for (String customerId:customerIdList) {
					boolean exe = false;
					for(String notCurCust : notCurCustList){
						if (notCurCust.equals(customerId)) {
							exe = true;
							break;
						}
					}
					if (!exe) {
						BmsCorrectAsynTaskVo vo=new BmsCorrectAsynTaskVo();
						vo.setCreator(JAppContext.currentUserName());
						vo.setCreateTime(JAppContext.currentTimestamp());
						vo.setDelFlag("0");
						vo.setTaskId(getUuid());
						vo.setTaskStatus(BmsCorrectAsynTaskStatusEnum.WAIT.getCode());
						format=new SimpleDateFormat("yyyyMM");
						vo.setStartDate(voEntity.getStartDate());
						vo.setEndDate(voEntity.getEndDate());
						vo.setCreateMonth(format.format(voEntity.getStartDate()));
						vo.setCustomerId(customerId);
						vo.setTaskName(voEntity.getTaskName());
						vo.setTaskRate(voEntity.getTaskRate());
						if(!bmsCorrectAsynTaskService.existTask(vo)){
							voList.add(vo);
						}
					}
				}
			}
			
			if(voList.size()>0){
				bmsCorrectAsynTaskService.saveBatch(voList);
				result.put("success", "保存成功！");
				for(BmsCorrectAsynTaskVo vo:voList){
					try{
						final String msg = vo.getTaskId();
						jmsQueueTemplate.send(BMS_CORRECT_ASYN_TASK, new MessageCreator() {
							@Override
							public Message createMessage(Session session) throws JMSException {
								return session.createTextMessage(msg);
							}
						});
					}catch(Exception e){
						logger.error("send MQ:",e);
						result.put("fail", "MQ发送失败！");
					}
				
				}
				
				return result;
				
			}
			
		}else{
			for (String notCurCust : notCurCustList) {
				if (notCurCust.equals(voEntity.getCustomerId())) {
					result.put("fail", "该商家不参与运单纠正！");
					return result;
				}
			}
			if(!bmsCorrectAsynTaskService.existTask(voEntity)){
				voEntity.setCreator(JAppContext.currentUserName());
				voEntity.setCreateTime(JAppContext.currentTimestamp());
				voEntity.setDelFlag("0");
				voEntity.setTaskId(getUuid());
				voEntity.setTaskStatus(BmsCorrectAsynTaskStatusEnum.WAIT.getCode());
				format=new SimpleDateFormat("yyyyMM");
				voEntity.setCreateMonth(format.format(voEntity.getStartDate()));
				voEntity.setTaskRate(0);
				bmsCorrectAsynTaskService.save(voEntity);
				result.put("success", "保存成功!");
				try {
					final String msg = voEntity.getTaskId();
					jmsQueueTemplate.send(BMS_CORRECT_ASYN_TASK, new MessageCreator() {
						@Override
						public Message createMessage(Session session) throws JMSException {
							return session.createTextMessage(msg);
						}
					});
				} catch (Exception e) {
					logger.error("send MQ:",e);
					result.put("fail", "MQ发送异常");
				}

			}
			
		}	
		return result;
	}
	
}
