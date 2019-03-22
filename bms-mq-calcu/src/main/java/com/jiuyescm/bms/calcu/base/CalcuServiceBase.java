package com.jiuyescm.bms.calcu.base;

import java.util.List;
import java.util.Map;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jiuyescm.bms.asyn.vo.BmsCalcuTaskVo;

public abstract class CalcuServiceBase<T,F> implements MessageListener {

	private Logger logger = LoggerFactory.getLogger(CalcuServiceBase.class);
	
	/**
	 * 查询业务数据
	 * @param map
	 * @return
	 */
	protected abstract List<T> queryBizList(Map<String,Object> map);
	
	/**
	 * 是否计算费用
	 * @param t 业务数据对象
	 * @return true-计算费用  false-不计算你费用 状态未不计算，金额至0
	 */
	abstract boolean isCalcu(T t);
	
	/**
	 * 筛选计费参数
	 * @param t 业务数据对象
	 * @return 费用数据对象
	 */
	abstract F initChargeParam(T t);
	
	BmsCalcuTaskVo getTaskVo(String taskId){
		return null;
	}
	
	/**
	 * 判定合同归属
	 * @param t 业务数据对象
	 * @return CONTRACT-合同在线   BMS-bms
	 */
	String queryContractAttr(String customerId){
		//使用redis进行缓存
		return null;
	}
	
	@Override
	public void onMessage(Message message) {
		long start = System.currentTimeMillis();
		String taskId = null;
		try {
			taskId = ((TextMessage)message).getText();
		} catch (JMSException e1) {
			logger.info("取出消息失败");
			return;
		}
		
		try {
			StringBuffer errorMessage=new StringBuffer();
			logger.info("正在处理计算  taskId={}",taskId);
			
			
		} catch (Exception e1) {
			logger.error(taskId+"处理耗材统一失败：{}",e1);
			return;
		}
		
		long end = System.currentTimeMillis();
		try {
			message.acknowledge();
		} catch (JMSException e) {
			logger.info("消息应答失败");
		}
		logger.info("--------------------MQ处理操作日志结束,耗时:"+(end-start)+"ms---------------");
	
	}

}
