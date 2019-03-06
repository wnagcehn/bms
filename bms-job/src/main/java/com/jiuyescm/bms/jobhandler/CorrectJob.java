package com.jiuyescm.bms.jobhandler;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

import com.jiuyescm.bms.biz.dispatch.entity.BizDispatchBillEntity;
import com.jiuyescm.bms.biz.dispatch.repository.IBizDispatchBillRepository;
import com.jiuyescm.bms.file.asyn.BmsCorrectAsynTaskEntity;
import com.jiuyescm.bms.file.asyn.repository.IBmsCorrectAsynTaskRepository;
import com.jiuyescm.cfm.common.JAppContext;
import com.jiuyescm.common.utils.DateUtil;
import com.jiuyescm.framework.sequence.api.ISequenceService;
//import com.jiuyescm.framework.sequence.api.ISnowflakeSequenceService;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHander;
import com.xxl.job.core.log.XxlJobLogger;

/**
 * 纠正job
 * @author liuzhicheng
 */
@JobHander(value="correctJob")
@Service
public class CorrectJob  extends IJobHandler{

		@Autowired
		private IBizDispatchBillRepository bizDispatchBillRepository;
		@Autowired
		private IBmsCorrectAsynTaskRepository bmsCorrectAsynTaskRepository;
		@Autowired
		private ISequenceService sequenceService1;
		@Resource
		private JmsTemplate jmsQueueTemplate;
		
		private static final String BMS_CORRECT_WEIGHT_TASK = "BMS.CORRECT.WEIGHT.ASYN.TASK";
		private static final String BMS_CORRECT_MATERIAL_TASK = "BMS.CORRECT.MATERIAL.ASYN.TASK";

		@Override
		public ReturnT<String> execute(String... params) throws Exception {
			XxlJobLogger.log("CorrectJob start.");
			XxlJobLogger.log("开始纠正任务");
	        return CalcJob(params);
		}
		
		private ReturnT<String> CalcJob(String[] params) {
			
			long starttime= System.currentTimeMillis();// 系统开始时间
			Timestamp createTime = JAppContext.currentTimestamp();
			String lastMonthFirstDay = DateUtil.getFirstDayOfMonth(1,"yyyy-MM-dd");
			lastMonthFirstDay+=" 00:00:00";
			String currentMonthFirstDay = DateUtil.getFirstDayOfMonth(0,"yyyy-MM-dd");
			currentMonthFirstDay+=" 00:00:00";
			XxlJobLogger.log("上个月第一天：{0}",lastMonthFirstDay);
			XxlJobLogger.log("本月第一天：{0}",currentMonthFirstDay);
			//对日期进行分组
			Map<String, String> dataMap = com.jiuyescm.common.utils.DateUtil.getSplitTime(lastMonthFirstDay,currentMonthFirstDay,1);
			//去除本月第一天
			dataMap.remove(currentMonthFirstDay+".0");
			
			//查询上月所有纠正任务
			Map<String, Object> taskCondition = new HashMap<>();
			String taskStartDate = DateUtil.getFirstDayOfMonth(1,"yyyy-MM-dd");
			taskCondition.put("taskStartDate", taskStartDate);
			List<BmsCorrectAsynTaskEntity> correctAsynTaskList = new ArrayList<>();
			try {
				correctAsynTaskList = bmsCorrectAsynTaskRepository.queryList(taskCondition);
			} catch (Exception e) {
				e.printStackTrace();
			}
			//对customerid去重
			HashSet<String> existCustomeridSet = new HashSet<>();
			if(CollectionUtils.isNotEmpty(correctAsynTaskList)){
				for (BmsCorrectAsynTaskEntity entity : correctAsynTaskList) {
					existCustomeridSet.add(entity.getCustomerId());
				}
			}

			//查询上月发生业务的商家id，因为数据量大，使用一天天查询
			HashSet<String> customeridSet = new HashSet<>();
			Map<String, Object> param = new HashMap<>();
			for (Map.Entry<String, String> entry : dataMap.entrySet()) {
				param.put("startTime", entry.getKey());
				param.put("endTime", entry.getValue());
				List<BizDispatchBillEntity> entityList=  bizDispatchBillRepository.queryBizCustomerid(param);
				//查询的customerId若没有生成纠正任务，则放入set去重
				for (BizDispatchBillEntity entity : entityList) {
					if(!existCustomeridSet.contains(entity.getCustomerid()))customeridSet.add(entity.getCustomerid());
				}
			}
			//创建未生成的任务
			if(CollectionUtils.isNotEmpty(customeridSet)){
				List<BmsCorrectAsynTaskEntity> list = new ArrayList<>();
				taskStartDate=taskStartDate.substring(0,7);
				taskStartDate=taskStartDate.replace("-","");
				Date startDate = DateUtil.getFirstDayOfMonth(1);
				Date endDate = DateUtil.getFirstDayOfMonth(0);
				
				/*long ids[] = new long[customeridSet.size()*2];
				ids = snowflakeSequenceService.nextId(customeridSet.size()*2);*/
				int i = 0;
				for (String customerid : customeridSet) {
					/*String id = String.valueOf(sequenceService.nextSeq("BMS.CORRECT")) ;
					String taskId = "CT";
					for(int i = 1;i<=10-id.length();i++){
						taskId +="0";
					}
					taskId += id;
					BmsCorrectAsynTaskEntity entity = createEntity(taskStartDate,createTime,startDate,endDate,customerid,"weight_correct");
					entity.setTaskId(ids[i]+"");
					i++;
					list.add(entity);
					BmsCorrectAsynTaskEntity entity2 = createEntity(taskStartDate,createTime,startDate,endDate,customerid,"material_correct");
					entity2.setTaskId(ids[i]+"");
					i++;
					list.add(entity2);*/
					
					String id1 = String.valueOf(sequenceService1.nextSeq("BMS.CORRECT")) ;
					String taskId1 = "CT";
					//任务的结束时间为开始时间的月份最后一天
					Calendar calendar = Calendar.getInstance();  
					calendar.setTime(endDate);  
					calendar.add(Calendar.DAY_OF_MONTH, -1);  
					Date end = calendar.getTime();
					for(int j = 1;j<=10-id1.length();j++){
						taskId1 +="0";
					}
					taskId1 += id1;
					BmsCorrectAsynTaskEntity entity = createEntity(taskStartDate,createTime,startDate,end,customerid,"weight_correct");
					entity.setTaskId(taskId1);
					i++;
					list.add(entity);
					BmsCorrectAsynTaskEntity entity2 = createEntity(taskStartDate,createTime,startDate,end,customerid,"material_correct");
					String id2 = String.valueOf(sequenceService1.nextSeq("BMS.CORRECT")) ;
					String taskId2 = "CT";
					for(int j = 1;j<=10-id2.length();j++){
						taskId2 +="0";
					}
					taskId2 += id2;
					entity2.setTaskId(taskId2);
					i++;
					list.add(entity2);
				}
				bmsCorrectAsynTaskRepository.saveBatch(list);
				
				for (BmsCorrectAsynTaskEntity entity : list) {
					try {
						final String msg = entity.getTaskId();
						String task = "";
						if("weight_correct".equals(entity.getBizType())){
							//发送重量调整MQ
							task = BMS_CORRECT_WEIGHT_TASK;
						}else if("material_correct".equals(entity.getBizType())) {
							//发送耗材调整MQ
							task = BMS_CORRECT_MATERIAL_TASK;
						}
						jmsQueueTemplate.send(task, new MessageCreator() {
							@Override
							public Message createMessage(Session session) throws JMSException {
								return session.createTextMessage(msg);
							}
						});
					} catch (Exception e) {
						XxlJobLogger.log("send MQ:", e);
						XxlJobLogger.log("fail", "MQ发送失败！");
					}
				}
			}
	        XxlJobLogger.log("纠正总耗时："+ (System.currentTimeMillis() - starttime) + "毫秒");
	        return ReturnT.SUCCESS;
		}

		private BmsCorrectAsynTaskEntity createEntity(String createMonth,Timestamp createTime,Date startDate,Date endDate,String customerid,String bizType) {
			BmsCorrectAsynTaskEntity entity = new BmsCorrectAsynTaskEntity();
			entity.setCreator("系统创建");
			entity.setCreateTime(createTime);
			entity.setDelFlag("0");
			entity.setTaskRate(0);
			entity.setTaskStatus("WAIT");
			entity.setCreateMonth(createMonth);
			entity.setStartDate(startDate);
			entity.setEndDate(endDate);
			entity.setCustomerId(customerid);
			entity.setBizType(bizType);
			return entity;
		}
		
}
