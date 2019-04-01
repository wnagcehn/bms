package com.jiuyescm.bms.calcu.base;

import java.util.HashMap;
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
import com.jiuyescm.bms.drools.IFeesCalcuService;
import com.jiuyescm.bms.rule.receiveRule.repository.IReceiveRuleRepository;
import com.jiuyescm.bs.util.StringUtil;
import com.jiuyescm.cfm.common.JAppContext;
import com.jiuyescm.common.utils.DateUtil;
import com.jiuyescm.common.utils.MD5Util;
import com.jiuyescm.contract.quote.api.IContractQuoteInfoService;
import com.jiuyescm.framework.lock.Lock;
import com.jiuyescm.framework.lock.LockCallback;
import com.jiuyescm.framework.lock.LockCantObtainException;
import com.jiuyescm.framework.lock.LockInsideExecutedException;

public abstract class CalcuTaskListener<T,F> implements MessageListener{
	
	@Autowired IBmsCalcuTaskService bmsCalcuTaskService;
	@Autowired IBmsCalcuService bmsCalcuService;
	@Autowired private Lock lock;
	@Autowired private IContractQuoteInfoService contractQuoteInfoService;
	@Autowired private IReceiveRuleRepository receiveRuleRepository;
	@Autowired private IFeesCalcuService feesCalcuService;

	
	private Logger logger = LoggerFactory.getLogger(CalcuTaskListener.class);

	@Override
	public void onMessage(Message message) {
		
		String taskId = null;
		try {
			taskId = ((TextMessage)message).getText();
			logger.info("taskId={} 正在处理计算任务 ",taskId);
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
			if(taskVo != null){
				String subjectName = taskVo.getSubjectName();
				logger.info("taskId={} customerName={} subjectName={} creMonth={}",taskId,taskVo.getCustomerName(),subjectName,taskVo.getCreMonth());
				processCalcuJob(taskVo);
			}
			else{
				logger.warn("taskId={} 未查询到任务 ",taskId);
			}
		}catch(Exception ex){
			logger.warn("taskId={} 查询计算任务异常",taskId,ex.getMessage());
		}
		finally{
			try {
				message.acknowledge();
			} catch (JMSException e) {
				logger.info("taskId={} 消息应答失败 ",taskId);
			}
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
		logger.info("taskId={} 加锁处理",taskVo.getTaskId());
		lock.lock(lockString, 7200, new LockCallback<Map<String, Object>>() {

			@Override
			public Map<String, Object> handleObtainLock() {
				logger.info("taskId={} 成功得到锁 ",taskVo.getTaskId());
				calcuJob(taskVo);
				handMap.put("success", "success");
				return handMap;
			}

			@Override
			public Map<String, Object> handleNotObtainLock() throws LockCantObtainException {
				logger.info("taskId={} 未得到锁 ",taskVo.getTaskId());
				handMap.put("success", "fail");
				handMap.put("remark", "已存在计费请求,丢弃");
				return handMap;
			}

			@Override
			public Map<String, Object> handleException(LockInsideExecutedException e) throws LockInsideExecutedException {
				logger.info("taskId={} 获取锁异常 ",taskVo.getTaskId());
				handMap.put("success", "fail");
				handMap.put("remark", "获取锁异常,任务丢弃");
				return handMap;
			}
		});
		/*String succ = handMap.get("success").toString();
		try{
			if(!"success".equals(succ)){
				logger.info("taskId={} {}",taskVo.getTaskId(),handMap.get("remark").toString());
				taskVo.setTaskStatus(0);//未获得锁，不处理
				taskVo.setRemark(handMap.get("remark").toString());
				taskVo.setTaskRate(99);
				bmsCalcuTaskService.update(taskVo);
				return;
			}
		}catch(Exception ex){
			logger.error("taskId={} ",taskVo.getTaskId(),ex);
		}*/
		logger.info("taskId={} 计算任务处理结束 耗时【{}】ms",taskVo.getTaskId(),(System.currentTimeMillis()-start));
	}	
	
	protected void calcuJob(BmsCalcuTaskVo taskVo){
		try {
			//修改任务的状态和时间  等待->处理中
			taskVo.setTaskStatus(10); //计算状态设为 处理中
			taskVo.setProcessTime(JAppContext.currentTimestamp()); //设置开始处理时间
			//总单量统计，计算单量统计
			logger.info("taskId={} 总单量统计",taskVo.getTaskId());
			BmsFeesQtyVo feesQtyVo = feesCountReport(taskVo);
			logger.info("taskId={} 总单量【{}】 本次计算单量【{}】",taskVo.getTaskId(),feesQtyVo.getFeesCount(),feesQtyVo.getUncalcuCount());
			
			taskVo.setFeesCount(feesQtyVo.getFeesCount()); 		//设置总的费用数
			taskVo.setUncalcuCount(feesQtyVo.getUncalcuCount());//设置本次待计算的费用数
			bmsCalcuTaskService.update(taskVo);
			
			//查询合同归属
			String contractAttr = bmsCalcuService.queryContractAttr(taskVo.getCustomerId());
			logger.info("taskId={} contractAttr={}",taskVo.getTaskId(),contractAttr);
			if(StringUtil.isEmpty(contractAttr)){
				taskVo.setTaskStatus(30);//合同归属不存在，计算异常
				taskVo.setTaskRate(99);
				taskVo.setRemark("合同归属不存在，任务丢弃");
				bmsCalcuTaskService.update(taskVo);
				logger.info("taskId={} 合同归属不存在，任务丢弃",taskVo.getTaskId());
				return;
			}
			//费用计算
			Map<String, Object> cond = getQueryMap(taskVo);
			logger.info("taskId={} 数据查询条件{}",taskVo.getTaskId(),cond);
			
			generalCalcu(taskVo, contractAttr,cond);
			
			taskVo.setTaskStatus(20);//合同归属不存在，计算异常
			taskVo.setTaskRate(100);
			bmsCalcuTaskService.update(taskVo);
			
		} catch (Exception e1) {
			logger.error("taskId={} 计算任务执行异常",taskVo.getTaskId(),e1);
			taskVo.setTaskStatus(30);
			taskVo.setTaskRate(99);
			taskVo.setRemark("系统错误");
			return;
		}
	}
	
	
	//获取业务数据查询条件
	protected Map<String,Object> getQueryMap(BmsCalcuTaskVo vo){
		String creMonth = vo.getCreMonth().toString();
		int startYear = Integer.parseInt(creMonth.substring(0, 4));
		int startMonth = Integer.parseInt(creMonth.substring(4, 6));
		String startTime = startYear+"-"+startMonth+"-01";
		String endTime = DateUtil.getFirstDayOfGivenMonth(startTime,1,"yyyy-MM-dd");
		Map<String,Object> cond = new HashMap<>();
		cond.put("customerId", vo.getCustomerId());
		cond.put("beginTime", startTime);
		cond.put("endTime", endTime);
		cond.put("num", 1000);
		cond.put("isCalculated", "99");
		cond.put("subjectCode", vo.getSubjectCode());
		return cond;
	}
	
	protected abstract void generalCalcu(BmsCalcuTaskVo vo,String contractAttr,Map<String, Object>cond);

	protected abstract BmsFeesQtyVo feesCountReport(BmsCalcuTaskVo taskVo);
	
	
}
