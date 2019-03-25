package com.jiuyescm.bms.calcu.base;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.MessageCreator;

import com.jiuyescm.bms.asyn.service.IBmsCalcuTaskService;
import com.jiuyescm.bms.asyn.vo.BmsCalcuTaskVo;
import com.jiuyescm.bms.base.dict.api.ICustomerDictService;
import com.jiuyescm.bms.base.dict.vo.PubCustomerVo;
import com.jiuyescm.bms.calculate.vo.BmsFeesQtyVo;
import com.jiuyescm.bs.util.StringUtil;
import com.jiuyescm.cfm.common.JAppContext;
import com.jiuyescm.common.utils.DateUtil;
import com.jiuyescm.common.utils.MD5Util;
import com.jiuyescm.exception.BizException;
import com.jiuyescm.framework.lock.Lock;
import com.jiuyescm.framework.lock.LockCallback;
import com.jiuyescm.framework.lock.LockCantObtainException;
import com.jiuyescm.framework.lock.LockInsideExecutedException;

public abstract class CalcuServiceBase<T,F> implements MessageListener,ICalcuService<T, F> {
	
	@Autowired IBmsCalcuTaskService bmsCalcuTaskService;
	@Autowired ICustomerDictService customerDictService;
	@Autowired private Lock lock;
	
	private final int handerCount = 1000; //单次查询费用数据
	
	private Logger logger = LoggerFactory.getLogger(CalcuServiceBase.class);
	
	@Override
	public void onMessage(Message message) {
		
		String taskId = null;
		try {
			taskId = ((TextMessage)message).getText();
			logger.info("正在处理计算任务  taskId={}",taskId);
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
		logger.info("计算任务处理结束 taskId={} 耗时【{}】ms",taskVo.getTaskId(),(System.currentTimeMillis()-start));
		
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
			String contractAttr = queryContractAttr(taskVo.getCustomerId());
			//费用计算
			calcuJob(taskVo,contractAttr);
			//最终统计
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
	
	//
	protected void calcuJob(BmsCalcuTaskVo taskVo,String contractAttr){
		//业务数据对象集合
		Map<String, Object> map = getQueryMap(taskVo);
		List<T> bizList = queryBizList(map);	
		List<F> feeList = new ArrayList<>();	//费用数据对象集合
		if(bizList!=null){
			for (T t : bizList) {
				if("BMS".equals(contractAttr)){
					//合同归属于BMS，走bms计算逻辑
					F f = bmsCalcu(t);
					feeList.add(f);
				}
				else{
					//合同归属于合同在线，走合同在线计算逻辑
					F f = contractCalcu(t);
					feeList.add(f);
				}
			}
		}
		updateFees(feeList);//更新费用表
		
		if(bizList.size()==handerCount){
			calcuJob(taskVo,contractAttr);
		}
	}
	
	/**
	 * 判定合同归属
	 * @param t 业务数据对象
	 * @return CONTRACT-合同在线   BMS-bms
	 */
	String queryContractAttr(String customerId){
		PubCustomerVo vo = customerDictService.queryById(customerId);
		if(vo == null){
			return null;
		}
		String contractAttr = null;
		if(vo.getContractAttr() == 1){
			contractAttr = "BMS";
		}
		else{
			contractAttr = "CONTRACT";
		}
		return contractAttr;
	}
	
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
