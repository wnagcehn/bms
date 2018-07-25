package com.jiuyescm.bms.correct.discount;

import java.util.HashMap;
import java.util.Map;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.apache.log4j.Logger;

public class BmsReceiveDispatchListener implements MessageListener{

	private static final Logger logger = Logger.getLogger(BmsReceiveDispatchListener.class.getName());
	
	@Override
	public void onMessage(Message message) {
		
		logger.info("--------------------MQ应收配送折扣消费---------------------------");
		long start = System.currentTimeMillis();
		String taskId = "";
		try {
			taskId = ((TextMessage)message).getText();
		} catch (JMSException e1) {
			logger.info("获取消息失败");
			return;
		}
		
		
		long end = System.currentTimeMillis();
		try {
			message.acknowledge();
		} catch (JMSException e) {
			logger.info("消息应答失败");
		}
		logger.info("--------------------MQ处理结束,耗时:"+(end-start)+"ms---------------");
		
	}
	
	/**
	 * 配送费折扣计算
	 * @param taskId 能唯一指定指定商家  指定物流商  指定科目的任务
	 */
	private void discount(String taskId){
		
		Map<String,Object> condition=new HashMap<String,Object>();
		condition.put("taskId", taskId);
		//查询任务信息
		
		//统计商家的月单量和金额  商家，物流商维度进行统计
		
		//更新taskId到折扣费用表中
		
		//批量获取业务数据 1000条一次（根据waybill_no关联）
		
		//循环处理
		  //获取单条业务数据
		  //获取对应的原始报价 和 计算规则
		  //调用配送折扣规则（将原始报价和折扣报价带入）获取新的报价
		  //调用原始规则 进行计算
		//计算结果批量保存至配送折扣费用表
		//计算出折扣价（差值） 批量更新至原始费用表中的 减免金额中
	}

}
