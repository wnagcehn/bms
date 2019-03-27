package com.jiuyescm.bms.calcu.base;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.jiuyescm.bms.asyn.service.IBmsCalcuTaskService;
import com.jiuyescm.bms.asyn.vo.BmsCalcuTaskVo;
import com.jiuyescm.bms.calculate.api.IBmsCalcuService;
import com.jiuyescm.bms.calculate.vo.BmsFeesQtyVo;
import com.jiuyescm.bs.util.StringUtil;
import com.jiuyescm.cfm.common.JAppContext;
import com.jiuyescm.common.utils.DateUtil;
import com.jiuyescm.common.utils.MD5Util;
import com.jiuyescm.framework.lock.Lock;
import com.jiuyescm.framework.lock.LockCallback;
import com.jiuyescm.framework.lock.LockCantObtainException;
import com.jiuyescm.framework.lock.LockInsideExecutedException;

public abstract class CalcuServiceBase<T> implements MessageListener,ICalcuService<T> {
	
	@Autowired IBmsCalcuTaskService bmsCalcuTaskService;
	@Autowired IBmsCalcuService bmsCalcuService;
	@Autowired private Lock lock;
	
	private final int handerCount = 1000; //单次查询费用数据
	
	private Logger logger = LoggerFactory.getLogger(CalcuServiceBase.class);
	
	
	@Override
	public void onMessage(Message message) {
		
		String taskId = null;
		try {
			taskId = ((TextMessage)message).getText();
			logger.info("taskId={} subject={} descrip=正在处理计算任务  ");
		} catch (JMSException e1) {
			logger.info("取出消息失败");
			return;
		}
		if(StringUtil.isEmpty(taskId)){
			logger.error("任务参数不合法");
			return;
		}
		BmsCalcuTaskVo taskVo = null;
		try{
			taskVo= bmsCalcuTaskService.queryCalcuTask(taskId);
		}catch(Exception ex){
			logger.warn("未查询到任务 taskId={} msg={}",taskId,ex.getMessage());
		}
		
		if(taskVo==null){
			logger.warn("未查询到任务 taskId={}",taskId);
			return;
		}
		processCalcuJob(taskVo);
		try {
			message.acknowledge();
		} catch (JMSException e) {
			logger.info("消息应答失败");
		}
	}
	
	//计算准备
	protected void processCalcuJob(final BmsCalcuTaskVo taskVo){
		long start = System.currentTimeMillis();
		String customerId = taskVo.getCustomerId();
		String subjectCode = taskVo.getSubjectCode();
		String creMonth = taskVo.getCreMonth().toString();
		String lockString = MD5Util.getMd5("BMS_CALCU_MQ"+customerId+subjectCode+creMonth);
		final Map<String, Object> handMap = new HashMap<>();
		handMap.put("success", "fail");
		handMap.put("remark", "");
		lock.lock(lockString, 5, new LockCallback<Map<String, Object>>() {

			@Override
			public Map<String, Object> handleObtainLock() {
				calcuJob(taskVo);
				handMap.put("success", "success");
				return handMap;
			}

			@Override
			public Map<String, Object> handleNotObtainLock() throws LockCantObtainException {
				handMap.put("success", "fail");
				handMap.put("remark", "已存在计费请求,丢弃");
				return handMap;
			}

			@Override
			public Map<String, Object> handleException(LockInsideExecutedException e) throws LockInsideExecutedException {
				handMap.put("success", "fail");
				handMap.put("remark", "请求异常,丢弃");
				return handMap;
			}
		});
		String succ = handMap.get("success").toString();
		if("success".equals(succ)){
			//请求成功 发送mq消息
			taskVo.setTaskStatus(20);
			taskVo.setTaskRate(100);
			taskVo.setFinishTime(JAppContext.currentTimestamp());
			bmsCalcuTaskService.saveTask(taskVo);
		}
		else{
			taskVo.setTaskStatus(40);//未获得锁，处不理，丢弃
			taskVo.setRemark(handMap.get("remark").toString());
			taskVo.setTaskRate(99);
			bmsCalcuTaskService.saveTask(taskVo);
		}
		logger.info("taskId={} 计算任务处理结束 耗时【{}】ms",taskVo.getTaskId(),(System.currentTimeMillis()-start));
		
	}
	
	private void calcuJob(BmsCalcuTaskVo taskVo){
		try {
			//修改任务的状态和时间  等待->处理中
			taskVo.setTaskStatus(10); //计算状态设为 处理中
			taskVo.setProcessTime(JAppContext.currentTimestamp()); //设置开始处理时间
			//总单量统计，计算单量统计
			BmsFeesQtyVo feesQtyVo = feesCountReport(taskVo.getCustomerId(),taskVo.getSubjectCode(),taskVo.getCreMonth());
			taskVo.setFeesCount(feesQtyVo.getFeesCount()); 		//设置总的费用数
			taskVo.setUncalcuCount(feesQtyVo.getUncalcuCount());//设置本次待计算的费用数
			bmsCalcuTaskService.update(taskVo);
			//查询合同归属
			String contractAttr = bmsCalcuService.queryContractAttr(taskVo.getCustomerId());
			logger.info("taskId={} msg=合同归属:{}",taskVo.getTaskId(),contractAttr);
			if(contractAttr == null){
				taskVo.setTaskRate(99);
				taskVo.setTaskStatus(40);
				bmsCalcuTaskService.update(taskVo);
				return;
			}
			//费用计算
			calcuJob(taskVo,contractAttr);
			//统计计算状态
			feesQtyVo = feesCountReport(taskVo.getCustomerId(),taskVo.getSubjectCode(),taskVo.getCreMonth());
			taskVo.setFinishTime(JAppContext.currentTimestamp());
			taskVo.setFeesCount(feesQtyVo.getFeesCount());
			taskVo.setBeginCount(feesQtyVo.getBeginCount());
			taskVo.setFinishCount(feesQtyVo.getFinishCount());
			taskVo.setSysErrorCount(feesQtyVo.getSysErrorCount());
			taskVo.setContractMissCount(feesQtyVo.getContractMissCount());
			taskVo.setQuoteMissCount(feesQtyVo.getQuoteMissCount());
			taskVo.setNoExeCount(feesQtyVo.getNoExeCount());
			taskVo.setUncalcuCount(feesQtyVo.getUncalcuCount());
			bmsCalcuTaskService.update(taskVo);
			
		} catch (Exception e1) {
			logger.error("计算任务执行异常 taskId={}",taskVo.getTaskId(),e1);
			return;
		}
	}
	
	//计算
	protected void calcuJob(BmsCalcuTaskVo taskVo,String contractAttr){
		//业务数据对象集合
		Map<String, Object> map = getQueryMap(taskVo);
		List<T> bizList = queryBizList(map);	
		if(bizList!=null){
			for (T t : bizList) {
				if("BMS".equals(contractAttr)){
					//合同归属于BMS，走bms计算逻辑
					bmsCalcu(t);
				}
				else{
					//合同归属于合同在线，走合同在线计算逻辑
					contractCalcu(t);
				}
			}
		}
		updateFees(bizList);//更新费用表
		
		if(bizList.size()==handerCount){
			calcuJob(taskVo,contractAttr);
		}
	}
	
	/**
	 * 业务数据查询参数
	 * @param taskVo
	 * @return
	 */
	private Map<String, Object> getQueryMap(BmsCalcuTaskVo taskVo){
		String creMonth = taskVo.getCreMonth().toString();
		int startYear = Integer.parseInt(creMonth.substring(0, 4));
		int startMonth = Integer.parseInt(creMonth.substring(4, 6));
		String startTime = startYear+"-"+startMonth+"-01";
		String endTime = DateUtil.getFirstDayOfGivenMonth(startTime,1,"yyyy-MM-dd");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("customerId", taskVo.getCustomerId());
		map.put("subjectCode", taskVo.getSubjectCode());
		map.put("startTime", startTime);
		map.put("endTime", endTime);
		return map;
		
	}
	
	

}
