package com.jiuyescm.bms.jobhandler;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
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
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

import com.jiuyescm.bms.base.group.service.IBmsGroupCustomerService;
import com.jiuyescm.bms.base.group.service.IBmsGroupService;
import com.jiuyescm.bms.base.group.vo.BmsGroupVo;
import com.jiuyescm.bms.biz.dispatch.entity.BizDispatchBillEntity;
import com.jiuyescm.bms.biz.dispatch.entity.BizDispatchPackageEntity;
import com.jiuyescm.bms.biz.dispatch.repository.IBizDispatchBillRepository;
import com.jiuyescm.bms.common.enumtype.BmsCorrectAsynTaskStatusEnum;
import com.jiuyescm.bms.file.asyn.BmsCorrectAsynTaskEntity;
import com.jiuyescm.bms.file.asyn.repository.IBmsCorrectAsynTaskRepository;
import com.jiuyescm.bms.receivable.storage.service.IBizDispatchPackageService;
import com.jiuyescm.cfm.common.JAppContext;
import com.jiuyescm.common.utils.DateUtil;
import com.jiuyescm.framework.sequence.api.ISequenceService;
import com.jiuyescm.framework.sequence.api.ISnowflakeSequenceService;
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
		@Resource
		private IBmsGroupCustomerService bmsGroupCustomerService;
		@Autowired
		private IBmsGroupService bmsGroupService;
		@Autowired
		private ISnowflakeSequenceService snowflakeSequenceService;
		@Autowired
		private IBizDispatchPackageService bizDispatchPackageService;
		
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

			// 查询不需要运单纠正的商家
			List<String> notCurCustList = null;
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("groupCode", "notpartin_orderCorrent_customer");
			map.put("bizType", "group_customer");
			BmsGroupVo bmsCancelCus = bmsGroupService.queryOne(map);
			if (null != bmsCancelCus) {
				notCurCustList = bmsGroupCustomerService.queryCustomerByGroupId(bmsCancelCus.getId());
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
					if(!existCustomeridSet.contains(entity.getCustomerid())){
						String idString = entity.getCustomerid();
						//去除不需要纠正商家id
						if(CollectionUtils.isNotEmpty(notCurCustList)){
							if(!notCurCustList.contains(idString)){
								customeridSet.add(idString);
							}
						}else {
							customeridSet.add(idString);
						}
						
					}
				}
			}

			//创建未生成的任务
			if(CollectionUtils.isNotEmpty(customeridSet)){
				List<BmsCorrectAsynTaskEntity> list = new ArrayList<>();
				taskStartDate=taskStartDate.substring(0,7);
				taskStartDate=taskStartDate.replace("-","");
				Date startDate = DateUtil.getFirstDayOfMonth(1);
				Date endDate = DateUtil.getFirstDayOfMonth(0);
				
				//使用了包装方案的商家
				List<String> dispatchPackgeList=new ArrayList<String>();
				for (String customerid : customeridSet) {
					//判断该商家是否使用了包装方案，如果使用了生成mq，重量纠正，耗材不纠正，备注里面标记该商家是标准包装方案商家不纠正   
				    Map<String,Object> condition=new HashMap<>();
				    condition.put("customerid", customerid);
				    BizDispatchPackageEntity dispatchPack=bizDispatchPackageService.queryOne(condition);
				    if(dispatchPack!=null){
				        dispatchPackgeList.add(customerid);
				    }
				    
			
                    //任务的结束时间为开始时间的月份最后一天
                    Calendar calendar = Calendar.getInstance();  
                    calendar.setTime(endDate);  
                    calendar.add(Calendar.DAY_OF_MONTH, -1);  
                    Date end = calendar.getTime();
				    
					String taskId1 = "STO" + snowflakeSequenceService.nextStringId();
					BmsCorrectAsynTaskEntity entity = createEntity(taskStartDate,createTime,startDate,end,customerid,"weight_correct");
					entity.setTaskId(taskId1);
					list.add(entity);
					
	                String taskId2 =  "STO" + snowflakeSequenceService.nextStringId();
					BmsCorrectAsynTaskEntity entity2 = createEntity(taskStartDate,createTime,startDate,end,customerid,"material_correct");
					entity2.setTaskId(taskId2);
					if(dispatchPackgeList.contains(customerid)){
					    entity2.setTaskRate(100);
					    entity2.setTaskStatus(BmsCorrectAsynTaskStatusEnum.NOTCORRECT.getCode());
					    entity2.setRemark("使用了标准包装方案的商家，不纠正耗材");
					}
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
						}else if("material_correct".equals(entity.getBizType()) && !dispatchPackgeList.contains(entity.getCustomerId())) {
							//发送耗材调整MQ
							task = BMS_CORRECT_MATERIAL_TASK;
						}
						if(StringUtils.isNotBlank(task)){
		                      jmsQueueTemplate.send(task, new MessageCreator() {
		                            @Override
		                            public Message createMessage(Session session) throws JMSException {
		                                return session.createTextMessage(msg);
		                            }
		                       });
						}
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
			entity.setTaskStatus(BmsCorrectAsynTaskStatusEnum.WAIT.getCode());
			entity.setCreateMonth(createMonth);
			entity.setStartDate(startDate);
			entity.setEndDate(endDate);
			entity.setCustomerId(customerid);
			entity.setBizType(bizType);
			return entity;
		}
		
}
