package com.jiuyescm.bms.correct.discount;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.asyn.service.IBmsDiscountAsynTaskService;
import com.jiuyescm.bms.biz.discount.entity.BmsDiscountAsynTaskEntity;
import com.jiuyescm.bms.discount.service.IBmsDiscountService;
import com.jiuyescm.bms.discount.vo.BmsDiscountAccountVo;
import com.jiuyescm.bms.discount.vo.FeesReceiveDispatchDiscountVo;
import com.jiuyescm.bms.fees.dispatch.entity.FeesReceiveDispatchEntity;
import com.jiuyescm.bms.fees.dispatch.service.IFeesReceiveDispatchService;
import com.jiuyescm.bms.quotation.contract.entity.PriceContractDiscountItemEntity;
import com.jiuyescm.bms.quotation.contract.service.IPriceContractDiscountService;
import com.jiuyescm.bms.quotation.contract.service.IPriceContractService;
import com.jiuyescm.bms.quotation.contract.vo.PriceContractDiscountItemVo;

public class BmsReceiveDispatchListener implements MessageListener{

	private static final Logger logger = Logger.getLogger(BmsReceiveDispatchListener.class.getName());
	
	@Autowired 
	private IBmsDiscountAsynTaskService bmsDiscountAsynTaskService;	
	
	@Autowired
	private IBmsDiscountService bmsDiscountService;
	
	@Autowired
	private IFeesReceiveDispatchService feesReceiveDispatchService;
	
	@Autowired
	private IPriceContractDiscountService priceContractDiscountService;
	
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
		BmsDiscountAsynTaskEntity task=bmsDiscountAsynTaskService.queryTask(condition);
		if(task==null){
			logger.info("没有查询到折扣任务记录;");
			return;
		}
		//统计商家的月单量和金额  商家，物流商维度进行统计
		condition=new HashMap<String,Object>();
		condition.put("startTime", task.getStartDate());
		condition.put("endTime", task.getEndDate());
		condition.put("customerId", task.getCustomerId());
		condition.put("carrierId", task.getCarrierId());
		BmsDiscountAccountVo discountAccountVo=bmsDiscountService.queryAccount(condition);
		if(discountAccountVo==null){
			logger.info("没有查询到该商家的统计记录");
			return;
		}
		//更新taskId到折扣费用表中
		condition.put("taskId", taskId);
		int updateResult=bmsDiscountService.updateFeeDiscountTask(condition);
		if(updateResult<=0){
			logger.info("更新taskId到折扣费用表中");
			return;
		}			
		//批量获取业务数据 1000条一次（根据taskId关联）
		int pageNo = 1;
		boolean doLoop = true;
		while (doLoop) {			
			PageInfo<FeesReceiveDispatchDiscountVo> pageInfo = 
					bmsDiscountService.queryAll(condition, pageNo,1000);
			if (null != pageInfo && pageInfo.getList().size() > 0) {
				if (pageInfo.getList().size() < 1000) {
					doLoop = false;
				}else {
					pageNo += 1; 
				}
				handDiscount(pageInfo.getList());
			}else {
				doLoop = false;
			}			
		}
		
		
	}
	
	/**
	 * 折扣计算
	 * @param list
	 */
	public void handDiscount(List<FeesReceiveDispatchDiscountVo> list){
		//循环处理
		  //获取单条业务数据
		  //获取对应的原始报价 和 计算规则
		  //调用配送折扣规则（将原始报价和折扣报价带入）获取新的报价
		  //调用原始规则 进行计算
		//计算结果批量保存至配送折扣费用表
		//计算出折扣价（差值） 批量更新至原始费用表中的 减免金额中
		for(FeesReceiveDispatchDiscountVo vo:list){
			Map<String,Object> condition=new HashMap<String,Object>();
			//根据运单号查询业务数据对应得费用，判断是否是计算成功的，成功的继续折扣，失败的返回计算失败
			condition.put("waybillNo", vo.getWaybillNo());
			FeesReceiveDispatchEntity fees=feesReceiveDispatchService.queryOne(condition);
			if(fees!=null && "1".equals(fees.getIsCalculated()) && StringUtils.isNotBlank(fees.getPriceId()) && StringUtils.isNotBlank(fees.getRuleNo())){
				//费用计算成功的
				//判断该科目是否签约折扣报价
				condition.put("customerId", vo.getCustomerId());
				/*condition.put("contractTypeCode", value)*/
				PriceContractDiscountItemEntity item=priceContractDiscountService.query(condition);
				
				
				
				
			}else{
				//费用计算失败的、未查询到费用的、者报价为空的、计算规则为空的
				vo.setIsCalculated("2");
			}
		}
	}

}
