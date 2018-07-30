package com.jiuyescm.bms.correct.discount;

import java.math.BigDecimal;
import java.util.ArrayList;
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
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.asyn.service.IBmsDiscountAsynTaskService;
import com.jiuyescm.bms.base.calcu.vo.CalcuReqVo;
import com.jiuyescm.bms.base.calcu.vo.CalcuResultVo;
import com.jiuyescm.bms.biz.discount.entity.BmsDiscountAsynTaskEntity;
import com.jiuyescm.bms.biz.dispatch.entity.BizDispatchBillEntity;
import com.jiuyescm.bms.calculate.base.IFeesCalcuService;
import com.jiuyescm.bms.chargerule.receiverule.entity.BillRuleReceiveEntity;
import com.jiuyescm.bms.chargerule.receiverule.service.IReceiveRuleService;
import com.jiuyescm.bms.common.enumtype.BmsCorrectAsynTaskStatusEnum;
import com.jiuyescm.bms.discount.service.IBmsDiscountService;
import com.jiuyescm.bms.discount.vo.BmsDiscountAccountVo;
import com.jiuyescm.bms.discount.vo.FeesReceiveDispatchDiscountVo;
import com.jiuyescm.bms.fees.dispatch.entity.FeesReceiveDispatchEntity;
import com.jiuyescm.bms.fees.dispatch.service.IFeesReceiveDispatchService;
import com.jiuyescm.bms.quotation.contract.entity.PriceContractDiscountItemEntity;
import com.jiuyescm.bms.quotation.contract.service.IPriceContractDiscountService;
import com.jiuyescm.bms.quotation.discount.entity.BmsQuoteDiscountDetailEntity;
import com.jiuyescm.bms.quotation.discount.entity.BmsQuoteDiscountTemplateEntity;
import com.jiuyescm.bms.quotation.discount.service.IBmsQuoteDiscountTemplateService;
import com.jiuyescm.bms.quotation.dispatch.entity.vo.BmsQuoteDispatchDetailVo;
import com.jiuyescm.bms.quotation.dispatch.service.IBmsQuoteDispatchDetailService;
import com.jiuyescm.cfm.common.JAppContext;
import com.jiuyescm.common.utils.DoubleUtil;

@Service("bmsReceiveDispatchListener")
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
	
	@Autowired
	private IBmsQuoteDispatchDetailService priceDspatchService;
	
	@Autowired
	private IReceiveRuleService receiveRuleService;
	
	@Autowired
	private IBmsQuoteDiscountTemplateService bmsQuoteDiscountTemplateService;
	
	@Autowired
	private IFeesCalcuService feesCalcuService;
	
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
		try {
			logger.info("正在消费"+taskId);
			//处理折扣计算
			logger.info("正在处理折扣计算");
			discount(taskId);
			logger.info("折扣计算结束");
		} catch (Exception e1) {
			logger.error("折扣计算失败：{}",e1);
			try {
				
			} catch (Exception e2) {
				logger.error("保存mq错误日志失败，错误日志：{}",e2);
			}
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
		
		task.setTaskStatus(BmsCorrectAsynTaskStatusEnum.PROCESS.getCode());
		bmsDiscountAsynTaskService.update(task);
		
		try {
			//统计商家的月单量和金额  商家，物流商维度进行统计
			condition=new HashMap<String,Object>();
			condition.put("startTime", task.getStartDate());
			condition.put("endTime", task.getEndDate());
			condition.put("customerId", task.getCustomerId());
			condition.put("carrierId", task.getCarrierId());
			BmsDiscountAccountVo discountAccountVo=bmsDiscountService.queryAccount(condition);
			if(discountAccountVo==null){
				logger.info("没有查询到该商家的统计记录");
				task.setRemark("没有查询到该商家的统计记录");
				task.setTaskStatus(BmsCorrectAsynTaskStatusEnum.FAIL.getCode());
				bmsDiscountAsynTaskService.update(task);	
				return;
			}
			
			updateProgress(task,20);
			
			//判断该科目是否签约折扣报价
			condition.put("customerId", task.getCustomerId());
			condition.put("contractTypeCode", "CUSTOMER_CONTRACT");
			condition.put("bizTypeCode", "DISPATCH");
			condition.put("subjectId", task.getSubjectCode());
			PriceContractDiscountItemEntity item=priceContractDiscountService.query(condition);
			if(item==null){
				logger.info("该商家未签约折扣服务");
				task.setRemark("该商家未签约折扣服务");
				task.setTaskStatus(BmsCorrectAsynTaskStatusEnum.FAIL.getCode());
				bmsDiscountAsynTaskService.update(task);
				return;
			}
			
			updateProgress(task,30);
			
			//查询折扣报价模板
			condition=new HashMap<String,Object>();
			condition.put("templateCode", item.getTemplateCode());
			BmsQuoteDiscountTemplateEntity template=bmsQuoteDiscountTemplateService.queryOne(condition);
			if(template==null){
				logger.info("未查询到折扣报价模板");
				task.setRemark("未查询到折扣报价模板");
				task.setTaskStatus(BmsCorrectAsynTaskStatusEnum.FAIL.getCode());
				bmsDiscountAsynTaskService.update(task);
				return;
			}
			
			//查询折扣方式
			if(StringUtils.isBlank(template.getDiscountType())){
				logger.info("模板"+template.getTemplateCode()+"未查询到折扣方式");
				task.setRemark("模板"+template.getTemplateCode()+"未查询到折扣方式");
				task.setTaskStatus(BmsCorrectAsynTaskStatusEnum.FAIL.getCode());
				bmsDiscountAsynTaskService.update(task);
				return;
			}
			
			//查询折扣规则
			condition=new HashMap<String,Object>();
			condition.put("quoModus", template.getDiscountType());
			BillRuleReceiveEntity rule=receiveRuleService.queryRuleByPriceType(condition);
			if(rule==null){
				logger.info("未查询到折扣规则");
				task.setRemark("未查询到折扣规则");
				task.setTaskStatus(BmsCorrectAsynTaskStatusEnum.FAIL.getCode());
				bmsDiscountAsynTaskService.update(task);
				return;
			}
			
			updateProgress(task,40);
			
			//更新taskId到折扣费用表中
			condition.put("taskId", taskId);
			condition.put("startTime", task.getStartDate());
			condition.put("endTime", task.getEndDate());
			condition.put("customerId", task.getCustomerId());
			condition.put("carrierId", task.getCarrierId());
			int updateResult=bmsDiscountService.updateFeeDiscountTask(condition);
			if(updateResult<=0){
				logger.info("更新taskId到折扣费用表中");
				task.setRemark("更新taskId到折扣费用表中");
				task.setTaskStatus(BmsCorrectAsynTaskStatusEnum.FAIL.getCode());
				bmsDiscountAsynTaskService.update(task);
				return;
			}	
			
			updateProgress(task,50);
			//批量获取业务数据 1000条一次（根据taskId关联）
			//int pageNo = 1;
			logger.info("进入批量循环处理");
			boolean doLoop = true;
			while (doLoop) {			
				PageInfo<FeesReceiveDispatchDiscountVo> pageInfo = 
						bmsDiscountService.queryAll(condition, 1,1000);
				if (null != pageInfo && pageInfo.getList().size() > 0) {
					if (pageInfo.getList().size() < 1000) {
						doLoop = false;
					}
					handDiscount(pageInfo.getList(),task,item,rule,template,discountAccountVo);
				}else {
					doLoop = false;
				}			
			}
			
			task.setRemark("折扣计算成功");
			updateProgress(task,100);
		} catch (Exception e1) {
			logger.info("折扣处理失败",e1);
			task.setTaskStatus(BmsCorrectAsynTaskStatusEnum.FAIL.getCode());
			bmsDiscountAsynTaskService.update(task);
		}
		
	}
	
	/**
	 * 折扣计算
	 * @param list
	 */
	public void handDiscount(List<FeesReceiveDispatchDiscountVo> list,BmsDiscountAsynTaskEntity task,PriceContractDiscountItemEntity item,BillRuleReceiveEntity rule,BmsQuoteDiscountTemplateEntity template,BmsDiscountAccountVo discountAccountVo){
		//循环处理
		  //获取单条业务数据
		  //获取对应的原始报价 和 计算规则
		  //调用配送折扣规则（将原始报价和折扣报价带入）获取新的报价
		  //调用原始规则 进行计算
		//计算结果批量保存至配送折扣费用表
		//计算出折扣价（差值） 批量更新至原始费用表中的 减免金额中
		
		List<FeesReceiveDispatchEntity> feeList=new ArrayList<FeesReceiveDispatchEntity>();
		
		for(FeesReceiveDispatchDiscountVo discountVo:list){
			
			logger.info("运单号为"+discountVo.getWaybillNo());
			
			Map<String,Object> condition=new HashMap<String,Object>();
			//根据运单号查询业务数据对应得费用，判断是否是计算成功的，成功的继续折扣，失败的返回计算失败
			BigDecimal amount=new BigDecimal(0);
			condition.put("waybillNo", discountVo.getWaybillNo());
			FeesReceiveDispatchEntity fee=feesReceiveDispatchService.queryOne(condition);
			
			if(fee!=null && "1".equals(fee.getIsCalculated()) && StringUtils.isNotBlank(fee.getPriceId()) && StringUtils.isNotBlank(fee.getRuleNo())){
				logger.info("原始报价id为"+fee.getPriceId()+",原始规则为"+fee.getRuleNo());
				
				//查询明细报价
				condition=new HashMap<String,Object>();
				condition.put("customerId", task.getCustomerId());
				condition.put("contractTypeCode", "CUSTOMER_CONTRACT");
				condition.put("bizTypeCode", "DISPATCH");
				condition.put("subjectId", task.getSubjectCode());
				condition.put("createTime", fee.getCreateTime()); 
				//查询折扣报价
				if("MONTH_COUNT".equals(template.getDiscountType())){
					//月单量
					condition.put("count", discountAccountVo.getOrderCount());
				}else if("MONTH_AMOUNT".equals(template.getDiscountType())){
					//月金额
					condition.put("count", discountAccountVo.getAmount());
				}
				
				List<BmsQuoteDiscountDetailEntity> discountPriceList=priceContractDiscountService.queryDiscountPrice(condition);
				if(discountPriceList==null || discountPriceList.size()<=0){
					logger.info("未查询到折扣报价明细");
					discountVo.setIsCalculated("2");
					discountVo.setDerateAmount(amount);
					discountVo.setDiscountAmount(amount);
					discountVo.setCalculateTime(JAppContext.currentTimestamp());
					discountVo.setRemark("未查询到折扣报价明细");
					fee.setDerateAmount(0d);
					feeList.add(fee);
					continue;
				}
				if(discountPriceList.size()>1){
					logger.info("折扣报价明细存在多条");
					discountVo.setIsCalculated("2");
					discountVo.setDerateAmount(amount);
					discountVo.setDiscountAmount(amount);
					discountVo.setCalculateTime(JAppContext.currentTimestamp());
					discountVo.setRemark("折扣报价明细存在多条");
					fee.setDerateAmount(0d);
					feeList.add(fee);
					continue;
				}
				BmsQuoteDiscountDetailEntity discountPrice=discountPriceList.get(0);
				
				//判断是否是整单折扣
				logger.info("折扣报价的id"+discountPrice.getId());
				
				if(!DoubleUtil.isBlank(discountPrice.getUnitPrice())){
					logger.info("进入整单折扣价计算");
					//整单折扣价
					amount=BigDecimal.valueOf(discountPrice.getUnitPrice());
				}else if(!DoubleUtil.isBlank(discountPrice.getUnitPriceRate())){
					logger.info("进入整单折扣率计算");
					//整单折扣率
					amount=BigDecimal.valueOf(fee.getAmount()*discountPrice.getUnitPriceRate());
				}else{
					//其余的（包含首重续重折扣）
					//查询原始报价
					logger.info("进入首重续重折扣计算");
					condition=new HashMap<String,Object>();
					condition.put("id", fee.getPriceId());
					BmsQuoteDispatchDetailVo oldPrice=priceDspatchService.queryOne(condition);
					if(oldPrice==null){
						discountVo.setIsCalculated("2");
						discountVo.setDerateAmount(amount);
						discountVo.setDiscountAmount(amount);
						discountVo.setCalculateTime(JAppContext.currentTimestamp());
						discountVo.setRemark("未查询到原始报价");
						fee.setDerateAmount(0d);
						feeList.add(fee);
						continue;
					}
					
					//===========================通过原始报价和折扣报价，得到最后计算的首重续重===============================
					BmsQuoteDispatchDetailVo newprice=getNewPrice(oldPrice,discountPrice);	
					
					//============================开始费用计算========================================
					//进入费用计算
					BizDispatchBillEntity biz=new BizDispatchBillEntity();
					biz.setWeight(fee.getChargedWeight());
					CalcuReqVo<BmsQuoteDispatchDetailVo> reqCalVo = new CalcuReqVo<BmsQuoteDispatchDetailVo>();
					reqCalVo.setBizData(biz);//业务数据
					reqCalVo.setQuoEntity(newprice);//折扣后报价			
					//获取原来的计算规则
					condition=new HashMap<String,Object>();
					condition.put("quotationNo", fee.getRuleNo());
					BillRuleReceiveEntity oldRule=receiveRuleService.queryOne(condition);				
					reqCalVo.setRuleNo(oldRule.getQuotationNo());
					reqCalVo.setRuleStr(oldRule.getRule());
								
					
					CalcuResultVo resultCalVo = feesCalcuService.FeesCalcuService(reqCalVo);
					if(resultCalVo!=null && "succ".equals(resultCalVo.getSuccess()) && resultCalVo.getPrice()!=null){
						//计算成功的
						amount=resultCalVo.getPrice();	
					}
					//=============================费用计算结束========================================
				}			
				handAmount(discountVo,fee,amount,discountPrice);
				feeList.add(fee);
			}else{
				//费用计算失败的、未查询到费用的、者报价为空的、计算规则为空的
				discountVo.setIsCalculated("2");
				discountVo.setCalculateTime(JAppContext.currentTimestamp());
				discountVo.setDerateAmount(amount);
				discountVo.setDiscountAmount(amount);
				discountVo.setRemark("费用计算失败或者报价为空或者计算规则为空");
				fee.setDerateAmount(0d);
				feeList.add(fee);
			}
		}
		
		//批量更新折扣费用
		logger.info("批量更新折扣费用条数为"+list.size()+"原始费用条数为"+feeList.size());
		int result=bmsDiscountService.updateList(list);
		if(result<=0){
			logger.info("批量更新折扣费用失败");
		}else{
			//批量更新原始费用中的减免费用
			int feeResult=feesReceiveDispatchService.updateBatch(feeList);
			if(feeResult<=0){
				logger.info("批量更新原始费用中的减免费用");
			}
		}

		
	}
	
	public void handAmount(FeesReceiveDispatchDiscountVo vo,FeesReceiveDispatchEntity fees,BigDecimal amount,BmsQuoteDiscountDetailEntity discountPrice){
		vo.setDiscountAmount(amount);//折扣后价格
		BigDecimal oldAmount=BigDecimal.valueOf(fees.getAmount());//原始价格
		BigDecimal derateAmount=oldAmount.subtract(amount);//减免金额
		vo.setDerateAmount(derateAmount);//减免金额
		vo.setIsCalculated("1");//计算状态
		vo.setRemark("计算成功");
		vo.setCalculateTime(JAppContext.currentTimestamp());//计算时间
		vo.setQuoteId(discountPrice.getId().longValue());//报价id
		fees.setDerateAmount(derateAmount.doubleValue());//费用里的减免金额
	}

	/**
	 * 进度更新
	 * @param taskVo
	 * @param num
	 * @throws Exception
	 */
	public void updateProgress(BmsDiscountAsynTaskEntity taskVo,int num){
		taskVo.setTaskRate(num);
		bmsDiscountAsynTaskService.update(taskVo);
	}
	
	/**
	 * 获取新的计算报价报价
	 * @param oldPrice
	 * @param discount
	 * @return
	 */
	public BmsQuoteDispatchDetailVo getNewPrice(BmsQuoteDispatchDetailVo oldPrice,BmsQuoteDiscountDetailEntity discount){
		
		if(!DoubleUtil.isBlank(discount.getFirstPrice())){
			//折扣首价
			oldPrice.setFirstWeightPrice(discount.getFirstPrice());
		}else if(!DoubleUtil.isBlank(discount.getFirstPriceRate())){
			//首价折扣率
			oldPrice.setFirstWeightPrice(oldPrice.getFirstWeightPrice()*discount.getFirstPriceRate());
		}else if(!DoubleUtil.isBlank(discount.getContinuePrice())){
			//折扣续重价
			oldPrice.setContinuedPrice(discount.getContinuePrice());
		}else if(!DoubleUtil.isBlank(discount.getContinuePirceRate())){
			//续重折扣率
			oldPrice.setContinuedPrice(oldPrice.getContinuedPrice()*discount.getContinuePirceRate());
		}
		
		return oldPrice;
	}
}
