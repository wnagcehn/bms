package com.jiuyescm.bms.correct.discount;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.jms.TextMessage;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.asyn.service.IBmsDiscountAsynTaskService;
import com.jiuyescm.bms.base.dictionary.entity.SystemCodeEntity;
import com.jiuyescm.bms.base.dictionary.service.ISystemCodeService;
import com.jiuyescm.bms.base.file.entity.BillPrepareExportTaskEntity;
import com.jiuyescm.bms.base.file.service.IBillPrepareExportTaskService;
import com.jiuyescm.bms.biz.discount.entity.BmsDiscountAsynTaskEntity;
import com.jiuyescm.bms.biz.dispatch.entity.BizDispatchBillEntity;
import com.jiuyescm.bms.biz.dispatch.service.IBizDispatchBillService;
import com.jiuyescm.bms.chargerule.receiverule.service.IReceiveRuleService;
import com.jiuyescm.bms.common.enumtype.BmsCorrectAsynTaskStatusEnum;
import com.jiuyescm.bms.common.enumtype.CalculateState;
import com.jiuyescm.bms.discount.service.IBmsDiscountService;
import com.jiuyescm.bms.discount.vo.BmsDiscountAccountVo;
import com.jiuyescm.bms.discount.vo.FeesReceiveDispatchDiscountVo;
import com.jiuyescm.bms.discount.vo.FeesReceiveStorageDiscountVo;
import com.jiuyescm.bms.drools.IFeesCalcuService;
import com.jiuyescm.bms.fees.dispatch.entity.FeesReceiveDispatchEntity;
import com.jiuyescm.bms.fees.dispatch.service.IFeesReceiveDispatchService;
import com.jiuyescm.bms.fees.storage.entity.FeesReceiveStorageEntity;
import com.jiuyescm.bms.fees.storage.service.IFeesReceiveStorageService;
import com.jiuyescm.bms.quotation.contract.entity.PriceContractDiscountItemEntity;
import com.jiuyescm.bms.quotation.contract.service.IPriceContractDiscountService;
import com.jiuyescm.bms.quotation.discount.entity.BmsQuoteDiscountDetailEntity;
import com.jiuyescm.bms.quotation.discount.entity.BmsQuoteDiscountTemplateEntity;
import com.jiuyescm.bms.quotation.discount.service.IBmsQuoteDiscountTemplateService;
import com.jiuyescm.bms.quotation.dispatch.entity.vo.BmsQuoteDispatchDetailVo;
import com.jiuyescm.bms.quotation.dispatch.service.IBmsQuoteDispatchDetailService;
import com.jiuyescm.cfm.common.JAppContext;
import com.jiuyescm.common.utils.DoubleUtil;
import com.jiuyescm.constants.MQConstants;
import com.jiuyescm.contract.base.vo.ContractDiscountConfigVo;
import com.jiuyescm.contract.quote.api.IContractDiscountService;
import com.jiuyescm.contract.quote.vo.ContractBizTypeEnum;
import com.jiuyescm.contract.quote.vo.ContractDiscountQueryVo;
import com.jiuyescm.contract.quote.vo.ContractDiscountVo;
import com.jiuyescm.utils.JsonUtils;

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
	private IFeesReceiveStorageService feesReceiveStorageService;
	
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
	
	@Autowired
	private IBizDispatchBillService bizDispatchBillService;
	
	@Resource
	private IContractDiscountService contractDiscountService;
	
	@Autowired 
	private ISystemCodeService systemCodeService;
	
    @Autowired
    private IBillPrepareExportTaskService billPrepareExportTaskService;
    
    @Resource
    private JmsTemplate jmsQueueTemplate;

	
	@Override
	public void onMessage(Message message) {
		
		logger.info("--------------------MQ应收折扣消费---------------------------");
		long start = System.currentTimeMillis();
		String json = "";
		try {
			json = ((TextMessage)message).getText();
		} catch (JMSException e1) {
			logger.info("获取消息失败");
			return;
		}
	    
		Map<String,Object> map=resolveJsonToMap(json);
		String taskId=map.get("taskId").toString();
		
		Map<String,Object> condition=new HashMap<>();
		condition.put("taskId", taskId);
	      //查出该任务用于继续累加备注
        BillPrepareExportTaskEntity entity = billPrepareExportTaskService.queryBillTask(condition);
		
		try {
			logger.info(taskId+"正在消费");
			//处理折扣计算
			logger.info(taskId+"正在处理折扣计算");
			discount(taskId,entity);
			logger.info(taskId+"折扣计算结束");
			
		} catch (Exception e1) {
			logger.error(taskId+"折扣计算失败：{}",e1);
			if(entity!=null){
			    entity.setProgress(0d);
			    entity.setRemark("折扣失败");
			}
			try {
				
			} catch (Exception e2) {
				logger.error("保存mq错误日志失败，错误日志：{}",e2);
			}
			return;
		}
		
		if(entity!=null){
		    //更新预账单
		    billPrepareExportTaskService.update(entity);
		    sendMq(MQConstants.BUINESSDATA_EXPORT, map);
		}
		
		
		long end = System.currentTimeMillis();
		try {
			message.acknowledge();
		} catch (JMSException e) {
			logger.info("消息应答失败");
		}
		logger.info("--------------------MQ处理结束,耗时:"+(end-start)+"ms---------------");
		
	}
	
	private void discount(String taskId,BillPrepareExportTaskEntity entity){
		/**
		 * 配送费折扣计算
		 * @param taskId 能唯一指定指定商家  指定物流商  指定科目的任务
		 */	
		Map<String,Object> condition=new HashMap<String,Object>();
		condition.put("taskId", taskId);
		//查询任务信息
		List<BmsDiscountAsynTaskEntity> taskList=bmsDiscountAsynTaskService.query(condition);
		if(taskList.size()<=0){
			logger.info("没有查询到折扣任务记录;");
			return;
		}
				
		
		for(BmsDiscountAsynTaskEntity task:taskList){
		    if("STORAGE".equals(task.getBizTypecode())){
	            logger.info(taskId+"进入仓储折扣计算");
	            //仓储折扣
	            discountStorage(task,entity);
	        }else if("DISPATCH".equals(task.getBizTypecode())){
	            logger.info(taskId+"进入配送折扣计算");
	            //配送折扣(除特殊折扣的物流产品类型)
	            discountDispatch(task,entity);
	            if(task.getRemark().contains("该商家存在未计算或待重算的业务数据")){
	                return;
	            }       
	            //配送折扣(特殊折扣的物流产品类型)
	            discountServiceDispatch(task,entity);
	        }
		}
	}
	
	/**
	 * 仓储费折扣计算
	 * @param task
	 */
	private void discountStorage(BmsDiscountAsynTaskEntity task,BillPrepareExportTaskEntity entity){
		String taskId=task.getTaskId();
		Map<String,Object> condition=new HashMap<String,Object>();
		task.setTaskStatus(BmsCorrectAsynTaskStatusEnum.PROCESS.getCode());
		bmsDiscountAsynTaskService.update(task);
		try {
			logger.info(taskId+"判断是否有计算失败的单子");
			//判断该商家是否有计算失败的单子
			condition=new HashMap<String,Object>();
			condition.put("startTime", task.getStartDate());
			condition.put("endTime", task.getEndDate());
			condition.put("customerId", task.getCustomerId());
			condition.put("subjectCode", task.getSubjectCode());			
			if("wh_b2c_work".equals(task.getSubjectCode())){
				condition.put("tempretureType", "tempreture");
			}
			logger.info(taskId+"查询费用和统计的参数"+JSONObject.fromObject(condition));		
			List<FeesReceiveStorageEntity> feeList=feesReceiveStorageService.queryCalculateFail(condition);
			if(feeList.size()>0){
				logger.info(taskId+"该商家存在计算失败的费用");
				task.setRemark(taskId+"该商家存在计算失败的费用");
				if(entity!=null){
	                entity.setRemark(StringUtils.isBlank(entity.getRemark())?task.getSubjectName()+"该商家存在计算失败的费用":entity.getRemark()+";"+task.getSubjectName()+"该商家存在计算失败的费用");
	                entity.setProgress(0d);
				}
				task.setTaskRate(80);
				task.setTaskStatus(BmsCorrectAsynTaskStatusEnum.FAIL.getCode());
				bmsDiscountAsynTaskService.update(task);	
				return;
			}
			//统计商家的月单量和金额  商家，费用科目进行统计
			logger.info(taskId+"统计商家的月单量和金额  商家，费用科目进行统计");
			BmsDiscountAccountVo discountAccountVo=bmsDiscountService.queryStorageAccount(condition);
			if(discountAccountVo==null){
				logger.info(taskId+"没有查询到该商家的统计记录");
				task.setRemark(taskId+"没有查询到该商家的统计记录");
			    if(entity!=null){
	                 entity.setRemark(StringUtils.isBlank(entity.getRemark())?task.getSubjectName()+"没有查询到该商家的统计记录":entity.getRemark()+";"+task.getSubjectName()+"没有查询到该商家的统计记录");
	                 entity.setProgress(0d);
			    }
				task.setTaskRate(80);
				task.setTaskStatus(BmsCorrectAsynTaskStatusEnum.FAIL.getCode());
				bmsDiscountAsynTaskService.update(task);	
				return;
			}
			
			updateProgress(task,20);
			
			ContractDiscountConfigVo configVo=null;
			if("contract".equals(task.getCustomerType())){//目前仅支持合同在线折扣计算
				logger.info(taskId+"进入合同在线查询折扣报价");
				ContractDiscountQueryVo queryVo=new ContractDiscountQueryVo();
				queryVo.setCustomerId(task.getCustomerId());
				queryVo.setSettlementTime(task.getCreateMonth());
				logger.info(taskId+"查询合同在线服务订购号的参数"+JSONObject.fromObject(queryVo));
				List<ContractDiscountVo> disCountVoList=contractDiscountService.querySubject(queryVo);
				logger.info(taskId+"查询合同在线服务订购号的结果"+JSONArray.fromObject(disCountVoList));
				if(disCountVoList.size()>0){
					ContractDiscountVo vo=disCountVoList.get(0);
					queryVo.setSubjectId(task.getSubjectCode());
					queryVo.setServiceOrderNo(vo.getServiceOrderNo());
					queryVo.setBizTypeCode(ContractBizTypeEnum.STORAGE.getCode());				
					if("MONTH_COUNT".equals(task.getDiscountType())){
						queryVo.setDiscountType("MONTH_COUNT");
						queryVo.setMonthCount(new BigDecimal(discountAccountVo.getOrderCount()));
					}else if("MONTH_AMOUNT".equals(task.getDiscountType())){
						queryVo.setDiscountType("MONTH_AMOUNT");
						queryVo.setMonthCount(new BigDecimal(discountAccountVo.getAmount()));
					}
					queryVo.setCarrierId("");
					queryVo.setCarrierServiceType("");
					queryVo.setWarehouseCode("");
					
					logger.info(taskId+"查询合同在线折扣报价参数"+JSONObject.fromObject(queryVo));
					try {
						configVo=contractDiscountService.queryDiscount(queryVo);
					} catch (Exception e) {
						// TODO: handle exception
						logger.info(taskId+"合同在线查询折扣报价失败"+e.getMessage());
						task.setRemark(taskId+"合同在线查询折扣报价失败"+e.getMessage());				
					    if(entity!=null){
		                     entity.setRemark(StringUtils.isBlank(entity.getRemark())?task.getSubjectName()+"合同在线查询折扣报价失败":entity.getRemark()+";"+task.getSubjectName()+"合同在线查询折扣报价失败");
		                     entity.setProgress(0d);
					    }
						task.setTaskRate(80);
						task.setTaskStatus(BmsCorrectAsynTaskStatusEnum.FAIL.getCode());
						bmsDiscountAsynTaskService.update(task);	
						return;
					}					
					logger.info(taskId+"查询合同在线折扣报价"+JSONObject.fromObject(configVo));
				}
				
				if(configVo==null){
					logger.info(taskId+"合同在线未查询到折扣报价");
					task.setRemark(taskId+"合同在线未查询到折扣报价");
					if(entity!=null){
                           entity.setRemark(StringUtils.isBlank(entity.getRemark())?task.getSubjectName()+"合同在线未查询到折扣报价":entity.getRemark()+";"+task.getSubjectName()+"合同在线未查询到折扣报价");
                           entity.setProgress(0d);
					}
					task.setTaskStatus(BmsCorrectAsynTaskStatusEnum.FAIL.getCode());
					task.setTaskRate(80);
					bmsDiscountAsynTaskService.update(task);
					return;
				}
			}
			
			logger.info(taskId+"删除原折扣费用表的记录");
			bmsDiscountService.deleteFeeStorageDiscount(condition);
			
			
			logger.info(taskId+"插入新折扣费用");
			
			//更新taskId到折扣费用表中
			condition.put("taskId", task.getTaskId());
			int updateResult=bmsDiscountService.insertFeeStorageDiscount(condition);
			if(updateResult<=0){
				logger.info(taskId+"更新taskId到折扣费用表中失败");
				task.setRemark(taskId+"更新taskId到折扣费用表中失败");
				if(entity!=null){
                    entity.setRemark(StringUtils.isBlank(entity.getRemark())?task.getSubjectName()+"更新taskId到折扣费用表中失败":entity.getRemark()+";"+task.getSubjectName()+"更新taskId到折扣费用表中失败");
                    entity.setProgress(0d);
				}
				task.setTaskRate(80);
				task.setTaskStatus(BmsCorrectAsynTaskStatusEnum.FAIL.getCode());
				bmsDiscountAsynTaskService.update(task);
				return;
			}	
			
			updateProgress(task,50);

			//批量获取业务数据 1000条一次（根据taskId关联）
			//int pageNo = 1;
			logger.info(taskId+"进入批量循环处理");
			
			if("contract".equals(task.getCustomerType())){
				logger.info(taskId+"进入合同在线折扣报价计算");
				boolean doLoop = true;
				while (doLoop) {
					try {
						PageInfo<FeesReceiveStorageDiscountVo> pageInfo = 
								bmsDiscountService.queryStorageAll(condition, 1,1000);
						if (null != pageInfo && pageInfo.getList().size() > 0) {
							if (pageInfo.getList().size() < 1000) {
								doLoop = false;
							}
							handContractStorageDiscount(pageInfo.getList(),task,configVo);
						}else {
							doLoop = false;
						}
					} catch (Exception e) {
						// TODO: handle exception
						logger.info(taskId+"循环同在线折扣报价计算异常");
						doLoop = false;
					}
				}
			}
			
			task.setRemark(taskId+"折扣计算成功");
			task.setTaskStatus(BmsCorrectAsynTaskStatusEnum.SUCCESS.getCode());
			if(entity!=null){
		         entity.setProgress(30d);
			}
			updateProgress(task,100);
			
		} catch (Exception e1) {
			logger.info(taskId+"折扣处理失败",e1);
			 if(entity!=null){
                 entity.setRemark(StringUtils.isBlank(entity.getRemark())?task.getSubjectName()+"折扣处理失败":entity.getRemark()+";"+task.getSubjectName()+"折扣处理失败");
                 entity.setProgress(0d);
             }
			task.setTaskRate(80);
			task.setTaskStatus(BmsCorrectAsynTaskStatusEnum.FAIL.getCode());
			task.setRemark("折扣处理失败");
			bmsDiscountAsynTaskService.update(task);
		}
	}
	
	/**
	 * 仓储费折扣计算
	 * @param list
	 */
	public void handContractStorageDiscount(List<FeesReceiveStorageDiscountVo> list,BmsDiscountAsynTaskEntity task,ContractDiscountConfigVo configVo){
		String taskId=task.getTaskId();
		//循环处理
		  //获取单条业务数据
		  //获取对应的原始报价 和 计算规则
		  //调用配送折扣规则（将原始报价和折扣报价带入）获取新的报价
		  //调用原始规则 进行计算
		//计算结果批量保存至配送折扣费用表
		//计算出折扣价（差值） 批量更新至原始费用表中的 减免金额中
		
		List<FeesReceiveStorageEntity> feeList=new ArrayList<FeesReceiveStorageEntity>();
		
		for(FeesReceiveStorageDiscountVo discountVo:list){
			
			String feeNo=discountVo.getFeesNo();
			
			Map<String,Object> condition=new HashMap<String,Object>();
			//根据运单号查询业务数据对应得费用，判断是否是计算成功的，成功的继续折扣，失败的返回计算失败
			BigDecimal amount=new BigDecimal(0);
			condition.put("feesNo", feeNo);
			condition.put("subjectCode", task.getSubjectCode());
			FeesReceiveStorageEntity fee=feesReceiveStorageService.queryOne(condition);
			
			if(fee!=null && "1".equals(fee.getIsCalculated())){				
				if(configVo.getTotalDiscountPrice()!=null){
					//整单折扣价
					amount=configVo.getTotalDiscountPrice();
				}else if(configVo.getTotalDiscountRate()!=null){
					//整单折扣率
					if(fee.getCost()!=null){
						amount=fee.getCost().multiply(configVo.getTotalDiscountRate());
					}
				}else{
					//其余的（包含首重续重折扣）
					//查询原始报价
					if(DoubleUtil.isBlank(fee.getUnitPrice())){						
						amount=getStorageAmount(fee,configVo);					
					}	
				}			
				handStorageAmount(discountVo,fee,amount);
				//保存折扣方式
				discountVo.setUnitPrice(configVo.getTotalDiscountPrice());
				discountVo.setUnitRate(configVo.getTotalDiscountRate());
				discountVo.setFirstPrice(configVo.getFirstWeightDiscountPrice());
				discountVo.setFirstRate(configVo.getFirstWeightDiscountRate());
				discountVo.setContinuePrice(configVo.getContinueWeightDiscountPrice());
				discountVo.setContinueRate(configVo.getContinueWeightDiscountRate());
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
		logger.info(taskId+"批量更新折扣费用条数为"+list.size()+"原始费用条数为"+feeList.size());
		int result=bmsDiscountService.updateStorageList(list);
		if(result<=0){
			logger.info(taskId+"批量更新折扣费用失败");
		}else{
			//批量更新原始费用中的减免费用
			int feeResult=feesReceiveStorageService.updateBatch(feeList);
			if(feeResult<=0){
				logger.info(taskId+"批量更新原始费用中的减免费用");
			}
		}
	}
	
	/**
	 * 配送费折扣计算(排除所有指定的物流产品类型)
	 * @param task 能唯一指定指定商家  指定物流商  指定科目的任务
	 */
	private void discountDispatch(BmsDiscountAsynTaskEntity task,BillPrepareExportTaskEntity entity){
		String taskId=task.getTaskId();
		Map<String,Object> condition=new HashMap<String,Object>();
		task.setTaskStatus(BmsCorrectAsynTaskStatusEnum.PROCESS.getCode());
		Map<String,BmsDiscountAccountVo> discountMap=new HashMap<String,BmsDiscountAccountVo>();
		bmsDiscountAsynTaskService.update(task);
		try {
			//判断该商家是否有未计算的单子
			condition=new HashMap<String,Object>();
			condition.put("startTime", task.getStartDate());
			condition.put("endTime", task.getEndDate());
			condition.put("customerId", task.getCustomerId());
			condition.put("carrierId", task.getCarrierId());
					
			logger.info(taskId+"查询费用和统计的参数"+JSONObject.fromObject(condition));		

			List<BizDispatchBillEntity> bizList=bizDispatchBillService.queryNotCalculate(condition);
			if(bizList.size()>0){
				logger.info(taskId+"该商家存在未计算或待重算的业务数据");
				task.setRemark(taskId+"该商家存在未计算或待重算的业务数据");
				if(entity!=null){
                    entity.setRemark(StringUtils.isBlank(entity.getRemark())?task.getSubjectName()+"该商家存在未计算或待重算的业务数据":entity.getRemark()+";"+task.getSubjectName()+"该商家存在未计算或待重算的业务数据");
                    entity.setProgress(0d);
                }
				task.setTaskStatus(BmsCorrectAsynTaskStatusEnum.FAIL.getCode());
				bmsDiscountAsynTaskService.update(task);	
				return;
			}
			
			
			//统计商家的月单量和金额  商家，物流商维度进行统计
			//物流产品类型,总单量,总金额(需要排除具体的折扣科目)
			//获取所有的折扣类型
			List<String> serviceList=new ArrayList<String>();
			Map<String,Object> map=new HashMap<String,Object>();
			map.put("typeCode", "DISCOUNT_SERVICE_CODE");
			map.put("code", task.getCarrierId());
	        List<SystemCodeEntity> discountServiceList = systemCodeService.queryCodeList(map);
	        for(SystemCodeEntity s:discountServiceList){
	            serviceList.add(s.getExtattr1());
	        }
	        if(serviceList.size()>0){
	            condition.put("notServiceList", serviceList);
	        }

			BmsDiscountAccountVo discountAccountVo=bmsDiscountService.queryAccount(condition);
            logger.info("统计商家的月单量和金额，商家，物流商维度"+JSONObject.fromObject(discountAccountVo));

			//******日志
			if(discountAccountVo==null){
				logger.info(taskId+"没有查询到该商家的统计记录");
				task.setRemark(taskId+"没有查询到该商家的统计记录");
				if(entity!=null){
                    entity.setRemark(StringUtils.isBlank(entity.getRemark())?task.getSubjectName()+"没有查询到该商家的统计记录":entity.getRemark()+";"+task.getSubjectName()+"没有查询到该商家的统计记录");
                    entity.setProgress(0d);
                }
				task.setTaskRate(80);
				task.setTaskStatus(BmsCorrectAsynTaskStatusEnum.FAIL.getCode());
				bmsDiscountAsynTaskService.update(task);	
				return;
			}
			
			//统计商家的月单量和金额  商家，物流商 物流产品类型维度进行统计
			List<BmsDiscountAccountVo> discountAccountVoList=bmsDiscountService.queryAccountServiceList(condition);
			logger.info("统计商家的月单量和金额，商家，物流商,物流产品类型维度"+JSONArray.fromObject(discountAccountVoList));
			if(discountAccountVoList.size()>0){
				for(BmsDiscountAccountVo vo:discountAccountVoList){
					discountMap.put(vo.getServiceTypeCode(), vo);
				}
			}
			
			updateProgress(task,20);
			
			BmsQuoteDiscountTemplateEntity template=null;
			ContractDiscountQueryVo queryVo=new ContractDiscountQueryVo();
			if("bms".equals(task.getCustomerType())){
				logger.info(taskId+"进去bms查询折扣报价");
				//判断该科目是否签约折扣报价
				condition.put("customerId", task.getCustomerId());
				condition.put("contractTypeCode", "CUSTOMER_CONTRACT");
				condition.put("bizTypeCode", "DISPATCH");
				condition.put("subjectId", task.getSubjectCode());
				logger.info(taskId+"查询签约折扣服务的参数"+JSONObject.fromObject(condition));
				PriceContractDiscountItemEntity item=priceContractDiscountService.query(condition);
				logger.info(taskId+"查询签约折扣服务的结果"+JSONObject.fromObject(item));
				if(item==null){
					logger.info(taskId+"该商家未签约折扣服务");
					task.setRemark(taskId+"该商家未签约折扣服务");
				    if(entity!=null){
	                    entity.setRemark(StringUtils.isBlank(entity.getRemark())?task.getSubjectName()+"bms该商家未签约折扣服务":entity.getRemark()+";"+task.getSubjectName()+"bms该商家未签约折扣服务");
	                    entity.setProgress(0d);
	                }
					task.setTaskRate(80);
					task.setTaskStatus(BmsCorrectAsynTaskStatusEnum.FAIL.getCode());
					bmsDiscountAsynTaskService.update(task);
					return;
				}
				
				updateProgress(task,30);
				
				//查询折扣报价模板
				condition=new HashMap<String,Object>();
				condition.put("templateCode", item.getTemplateCode());
				template=bmsQuoteDiscountTemplateService.queryOne(condition);
				logger.info(taskId+"查询签约折扣模板的结果"+JSONObject.fromObject(template));
				if(template==null){
					logger.info(taskId+"未查询到折扣报价模板");
					task.setRemark(taskId+"未查询到折扣报价模板");
				    if(entity!=null){
                        entity.setRemark(StringUtils.isBlank(entity.getRemark())?task.getSubjectName()+"bms未查询到折扣报价模板":entity.getRemark()+";"+task.getSubjectName()+"bms未查询到折扣报价模板");
                        entity.setProgress(0d);
                    }
					task.setTaskRate(80);
					task.setTaskStatus(BmsCorrectAsynTaskStatusEnum.FAIL.getCode());
					bmsDiscountAsynTaskService.update(task);
					return;
				}
				
				//查询折扣方式
				if(StringUtils.isBlank(template.getDiscountType())){
					logger.info(taskId+"模板"+template.getTemplateCode()+"未查询到折扣方式");
					task.setRemark(taskId+"模板"+template.getTemplateCode()+"未查询到折扣方式");
				    if(entity!=null){
                        entity.setRemark(StringUtils.isBlank(entity.getRemark())?task.getSubjectName()+"bms未查询到折扣方式":entity.getRemark()+";"+task.getSubjectName()+"bms未查询未查询到折扣方式");
                        entity.setProgress(0d);
                    }
					task.setTaskStatus(BmsCorrectAsynTaskStatusEnum.FAIL.getCode());
					task.setTaskRate(80);
					bmsDiscountAsynTaskService.update(task);
					return;
				}
				updateProgress(task,40);
			}else if("contract".equals(task.getCustomerType())){
				logger.info(taskId+"进入合同在线查询折扣报价");
				queryVo.setCustomerId(task.getCustomerId());
				queryVo.setSettlementTime(task.getCreateMonth());
				queryVo.setBizTypeCode("");
				logger.info(taskId+"查询合同在线服务订购号的参数"+JSONObject.fromObject(queryVo));
				List<ContractDiscountVo> disCountVoList=contractDiscountService.querySubject(queryVo);
				logger.info(taskId+"查询合同在线服务订购号的结果"+JSONArray.fromObject(disCountVoList));
				if(disCountVoList.size()>0){
					ContractDiscountVo vo=disCountVoList.get(0);
					queryVo.setSubjectId("");
					//queryVo.setServiceOrderNo(vo.getServiceOrderNo());
					queryVo.setServiceOrderNo(vo.getServiceOrderNo());
					queryVo.setBizTypeCode("DISTRIBUTION");				
					queryVo.setCarrierId(task.getCarrierId());
					queryVo.setWarehouseCode("");
				}

				updateProgress(task,40);
			}
			
			//更新taskId到折扣费用表中
			condition.put("taskId", task.getTaskId());
			condition.put("startTime", task.getStartDate());
			condition.put("endTime", task.getEndDate());
			condition.put("customerId", task.getCustomerId());
			condition.put("carrierId", task.getCarrierId());
			condition.put("notServiceList", serviceList);
			logger.info(taskId+"更新折扣费用表的参数"+JSONObject.fromObject(condition));
			int updateResult=bmsDiscountService.updateFeeDiscountTask(condition);
			if(updateResult<=0){
				logger.info(taskId+"更新taskId到折扣费用表中失败");
				task.setRemark(taskId+"更新taskId到折扣费用表中失败");
				task.setTaskRate(80);
				if(entity!=null){
                     entity.setRemark(StringUtils.isBlank(entity.getRemark())?task.getSubjectName()+"更新taskId到折扣费用表中失败":entity.getRemark()+";"+task.getSubjectName()+"更新taskId到折扣费用表中失败");
                     entity.setProgress(0d);
                }
				task.setTaskStatus(BmsCorrectAsynTaskStatusEnum.FAIL.getCode());
				bmsDiscountAsynTaskService.update(task);
				return;
			}	
			
			updateProgress(task,50);
			//批量获取业务数据 1000条一次（根据taskId关联）
			//int pageNo = 1;
			logger.info(taskId+"进入批量循环处理");
			
			boolean doLoop = true;
			while (doLoop) {
				try {
					PageInfo<FeesReceiveDispatchDiscountVo> pageInfo = 
							bmsDiscountService.queryAll(condition, 1,1000);
					if (null != pageInfo && pageInfo.getList().size() > 0) {
						if (pageInfo.getList().size() < 1000) {
							doLoop = false;
						}
						if("bms".equals(task.getCustomerType())){
							logger.info(taskId+"进入bms折扣报价计算");
							handBmsDiscount(pageInfo.getList(),task,template,discountMap,discountAccountVo);
						}else if("contract".equals(task.getCustomerType())){
							logger.info(taskId+"进入合同在线折扣报价计算");
							handContractDiscount(pageInfo.getList(),task,queryVo,discountMap,discountAccountVo);
						}					
					}else {
						doLoop = false;
					}	
				} catch (Exception e) {
					// TODO: handle exception
					logger.info(taskId+"循环bms折扣报价计算异常");
					doLoop = false;
				}
						
			}
			
			task.setRemark(taskId+"折扣计算成功");
			if(entity!=null){
                entity.setProgress(30d);
           }
			task.setTaskStatus(BmsCorrectAsynTaskStatusEnum.SUCCESS.getCode());
			updateProgress(task,100);
		} catch (Exception e1) {
			logger.info(taskId+"折扣处理失败",e1);
			task.setTaskRate(80);
			task.setTaskStatus(BmsCorrectAsynTaskStatusEnum.FAIL.getCode());
			if(entity!=null){
                entity.setRemark(StringUtils.isBlank(entity.getRemark())?task.getSubjectName()+"折扣处理失败":entity.getRemark()+";"+task.getSubjectName()+"折扣处理失败");
                entity.setProgress(0d);
           }
			bmsDiscountAsynTaskService.update(task);
		}
		
	}
	
	/**
	 * 折扣计算
	 * @param list
	 */
	public void handContractDiscount(List<FeesReceiveDispatchDiscountVo> list,BmsDiscountAsynTaskEntity task,ContractDiscountQueryVo queryVo,Map<String,BmsDiscountAccountVo> discountMap,BmsDiscountAccountVo discountAccount){
		String taskId=task.getTaskId();
		//循环处理
		  //获取单条业务数据
		  //获取对应的原始报价 和 计算规则
		  //调用配送折扣规则（将原始报价和折扣报价带入）获取新的报价
		  //调用原始规则 进行计算
		//计算结果批量保存至配送折扣费用表
		//计算出折扣价（差值） 批量更新至原始费用表中的 减免金额中		
		List<FeesReceiveDispatchEntity> feeList=new ArrayList<FeesReceiveDispatchEntity>();
		
		//增加缓存
		Map<String,ContractDiscountConfigVo> sessionMap=new HashMap<String,ContractDiscountConfigVo>();
		
		for(FeesReceiveDispatchDiscountVo discountVo:list){
			BigDecimal amount=new BigDecimal(0);						
			Map<String,Object> condition=new HashMap<String,Object>();
			//根据运单号查询业务数据对应得费用，判断是否是计算成功的，成功的继续折扣，失败的返回计算失败
			condition.put("waybillNo", discountVo.getWaybillNo());
			FeesReceiveDispatchEntity fee=feesReceiveDispatchService.queryOne(condition);
			//初始化折扣费用 key 物流产品类型+折扣方式+金额
			setValue(discountVo,amount);
			if(fee!=null && "1".equals(fee.getIsCalculated())){	
				
				//物流产品类型
				String serviceTypeCode=StringUtils.isNotBlank(discountVo.getAdjustServiceTypeCode())?discountVo.getAdjustServiceTypeCode():discountVo.getServiceTypeCode();
				BmsDiscountAccountVo discountAccountVo=new BmsDiscountAccountVo();
				if(StringUtils.isNotBlank(serviceTypeCode)){
					discountAccountVo=discountMap.get(serviceTypeCode);
				}else{
					discountAccountVo=discountAccount;
				}
				
				if(discountAccountVo==null){
					discountVo.setRemark(discountVo.getWaybillNo()+"没有查询到该商家的统计记录");
					fee.setDerateAmount(0d);
					feeList.add(fee);
					continue;
				}
				
				if("MONTH_COUNT".equals(task.getDiscountType())){
					queryVo.setDiscountType("MONTH_COUNT");
					queryVo.setMonthCount(new BigDecimal(discountAccountVo.getOrderCount()));
				}else if("MONTH_AMOUNT".equals(task.getDiscountType())){
					queryVo.setDiscountType("MONTH_AMOUNT");
					queryVo.setMonthCount(new BigDecimal(discountAccountVo.getAmount()));
				}
			
				String key=serviceTypeCode+task.getDiscountType()+queryVo.getMonthCount().doubleValue()+"";
				//查询报价
				if(StringUtils.isNotBlank(serviceTypeCode)){
					queryVo.setCarrierServiceType(serviceTypeCode);
				}
				ContractDiscountConfigVo configVo=null;
				try {
					if(sessionMap.containsKey(key)){
						configVo=sessionMap.get(key);
					}else{
						logger.info(discountVo.getWaybillNo()+"查询合同在线折扣报价参数"+JSONObject.fromObject(queryVo));
						configVo=contractDiscountService.queryDiscount(queryVo);
					   if(configVo==null){
                            //费用计算失败的、未查询到费用的、者报价为空的、计算规则为空的
                            discountVo.setRemark(discountVo.getWaybillNo()+"合同在线未查询到折扣报价");
                            fee.setDerateAmount(0d);
                            feeList.add(fee);
                            continue;
                        }
						sessionMap.put(key, configVo);
					}			
					logger.info(discountVo.getWaybillNo()+"查询合同在线折扣报价结果"+JSONObject.fromObject(configVo));
				} catch (Exception e) {
					// TODO: handle exception
					//费用计算失败的、未查询到费用的、者报价为空的、计算规则为空的
					discountVo.setRemark(discountVo.getWaybillNo()+"合同在线未查询到折扣报价");
					fee.setDerateAmount(0d);
					feeList.add(fee);
					continue;
				}	
				
				
				if(configVo.getTotalDiscountPrice()!=null){
					//整单折扣价
					amount=configVo.getTotalDiscountPrice();
					discountVo.setUnitPrice(configVo.getTotalDiscountPrice());
				}else if(configVo.getTotalDiscountRate()!=null){
					//整单折扣率
					if(!DoubleUtil.isBlank(fee.getAmount())){
						BigDecimal newAmount=new BigDecimal(fee.getAmount());
						amount=newAmount.multiply(configVo.getTotalDiscountRate());
						discountVo.setUnitRate(configVo.getTotalDiscountRate());
					}
				}else{
					//其余的（包含首重续重折扣）
					//查询原始报价
					if(DoubleUtil.isBlank(fee.getUnitPrice())){	
						amount=getDispatchAmount(fee,configVo);
						discountVo.setFirstPrice(configVo.getFirstWeightDiscountPrice());
						discountVo.setFirstRate(configVo.getFirstWeightDiscountRate());
						discountVo.setContinuePrice(configVo.getContinueWeightDiscountPrice());
						discountVo.setContinueRate(configVo.getContinueWeightDiscountRate());
					}
				}			
				handAmount(discountVo,fee,amount);
				discountVo.setRemark("折扣成功");
				feeList.add(fee);
			}else{
				//费用计算失败的、未查询到费用的、者报价为空的、计算规则为空的
				discountVo.setRemark(taskId+"费用计算失败或者报价为空或者计算规则为空");
				fee.setDerateAmount(0d);
				feeList.add(fee);
			}
		}
		
		//批量更新折扣费用
		logger.info(taskId+"批量更新折扣费用条数为"+list.size()+"原始费用条数为"+feeList.size());
		int result=bmsDiscountService.updateList(list);
		if(result<=0){
			logger.info(taskId+"批量更新折扣费用失败");
		}else{
			//批量更新原始费用中的减免费用
			int feeResult=feesReceiveDispatchService.updateBatch(feeList);
			if(feeResult<=0){
				logger.info(taskId+"批量更新原始费用中的减免费用");
			}
		}

		
	}
	
	/**
	 * 折扣计算
	 * @param list
	 */
	public void handBmsDiscount(List<FeesReceiveDispatchDiscountVo> list,BmsDiscountAsynTaskEntity task,BmsQuoteDiscountTemplateEntity template,Map<String,BmsDiscountAccountVo> discountMap,BmsDiscountAccountVo discountAccount){
		String taskId=task.getTaskId();
		//循环处理
		  //获取单条业务数据
		  //获取对应的原始报价 和 计算规则
		  //调用配送折扣规则（将原始报价和折扣报价带入）获取新的报价
		  //调用原始规则 进行计算
		//计算结果批量保存至配送折扣费用表
		//计算出折扣价（差值） 批量更新至原始费用表中的 减免金额中
		
		List<FeesReceiveDispatchEntity> feeList=new ArrayList<FeesReceiveDispatchEntity>();
		
		//
		
		for(FeesReceiveDispatchDiscountVo discountVo:list){
			
			String waybillNo=discountVo.getWaybillNo();
			
			Map<String,Object> condition=new HashMap<String,Object>();
			//根据运单号查询业务数据对应得费用，判断是否是计算成功的，成功的继续折扣，失败的返回计算失败
			BigDecimal amount=new BigDecimal(0);
			condition.put("waybillNo", waybillNo);
			FeesReceiveDispatchEntity fee=feesReceiveDispatchService.queryOne(condition);
			//初始化折扣费用
			setValue(discountVo,amount);
			if(fee!=null && "1".equals(fee.getIsCalculated()) && StringUtils.isNotBlank(fee.getPriceId())){
				//查询明细报价
				condition=new HashMap<String,Object>();
				condition.put("templateCode", template.getTemplateCode());
				condition.put("createTime", fee.getCreateTime());
				
				logger.info(taskId+"原始报价id为"+fee.getPriceId());
				//物流产品类型
				String serviceTypeCode=StringUtils.isNotBlank(discountVo.getAdjustServiceTypeCode())?
				        discountVo.getAdjustServiceTypeCode():discountVo.getServiceTypeCode();				
				BmsDiscountAccountVo discountAccountVo=new BmsDiscountAccountVo();
				//为空时取该商家该物流商下所有的单量和金额
				//不为空时匹配到物流产品类型，匹配到了则取该物流商对应得单量和金额,匹配不到则取所有的单量和金额
				//discountMap中此时应为所有需要折扣的物流产品类型对应得单量和金额
				if(StringUtils.isNotBlank(serviceTypeCode)){
	                discountAccountVo=discountMap.get(serviceTypeCode);             
				}else{
					discountAccountVo=discountAccount;
				}
					
				if(discountAccountVo==null){
					discountVo.setRemark(discountVo.getWaybillNo()+"没有查询到该商家的统计记录");
					fee.setDerateAmount(0d);
					feeList.add(fee);
					continue;
				}
				
				//查询折扣报价
				if("MONTH_COUNT".equals(template.getDiscountType())){
					//月单量
					condition.put("count", discountAccountVo.getOrderCount());
				}else if("MONTH_AMOUNT".equals(template.getDiscountType())){
					//月金额
					condition.put("count", discountAccountVo.getAmount());
				}
				
				logger.info("查询折扣报价明细的参数"+JSONObject.fromObject(condition));
				
				List<BmsQuoteDiscountDetailEntity> discountPriceList=priceContractDiscountService.queryDiscountPrice(condition);
				if(discountPriceList==null || discountPriceList.size()<=0){
					logger.info(taskId+"未查询到折扣报价明细");
					discountVo.setRemark(taskId+"未查询到折扣报价明细");
					fee.setDerateAmount(0d);
					feeList.add(fee);
					continue;
				}
				
				//报价筛选	
				BmsQuoteDiscountDetailEntity discountPrice=quoteFilter(discountPriceList,serviceTypeCode);
				if(discountPrice==null){
					logger.info(taskId+"未筛选到折扣报价明细");
					discountVo.setRemark(taskId+"未筛选到折扣报价明细");
					fee.setDerateAmount(0d);
					feeList.add(fee);
					continue;
				}
				
				logger.info("最后得到的报价"+JSONObject.fromObject(discountPrice));
				
				//判断是否是整单折扣
				logger.info(taskId+"折扣报价的id"+discountPrice.getId());		
				if(!DoubleUtil.isBlank(discountPrice.getUnitPrice())){
					logger.info(waybillNo+"进入整单折扣价计算");
					//整单折扣价
					amount=BigDecimal.valueOf(discountPrice.getUnitPrice());
					discountVo.setUnitPrice(BigDecimal.valueOf(discountPrice.getUnitPrice()));
				}else if(!DoubleUtil.isBlank(discountPrice.getUnitPriceRate())){
					logger.info(waybillNo+"进入整单折扣率计算");
					//整单折扣率
					amount=BigDecimal.valueOf(fee.getAmount()*discountPrice.getUnitPriceRate()/100);
					discountVo.setUnitRate(BigDecimal.valueOf(discountPrice.getUnitPriceRate()/100));
				}else{
					//其余的（包含首重续重折扣）
					//查询原始报价
					logger.info(waybillNo+"进入首重续重折扣计算");
					condition=new HashMap<String,Object>();
					condition.put("id", fee.getPriceId());
					BmsQuoteDispatchDetailVo oldPrice=priceDspatchService.queryOne(condition);
					if(oldPrice==null){
						setValue(discountVo,amount);
						discountVo.setIsCalculated("2");
						discountVo.setRemark("未查询到原始报价");
						fee.setDerateAmount(0d);
						feeList.add(fee);
						continue;
					}
					
					//===========================通过原始报价和折扣报价，得到最后计算的首重续重===============================
					BmsQuoteDispatchDetailVo newprice=getNewPrice(oldPrice,discountPrice,discountVo);	
					
					//开始进行计算
					if(!DoubleUtil.isBlank(newprice.getUnitPrice())){
						amount=BigDecimal.valueOf(newprice.getUnitPrice());
						discountVo.setUnitPrice(amount);
					}else{
						amount=BigDecimal.valueOf(newprice.getFirstWeight()<fee.getChargedWeight()?newprice.getFirstWeightPrice()+newprice.getContinuedPrice()*((fee.getChargedWeight()-newprice.getFirstWeight())/newprice.getContinuedWeight()):newprice.getFirstWeightPrice());	        		
					}
				}	
				handAmount(discountVo,fee,amount);
				discountVo.setQuoteId(discountPrice.getId().longValue());
				discountVo.setRemark("折扣成功");
				feeList.add(fee);
			}else{
				//费用计算失败的、未查询到费用的、者报价为空的、计算规则为空的
				discountVo.setRemark(taskId+"费用计算失败或者报价为空或者计算规则为空");
				fee.setDerateAmount(0d);
				feeList.add(fee);
			}
		}
		
		//批量更新折扣费用
		logger.info(taskId+"批量更新折扣费用条数为"+list.size()+"原始费用条数为"+feeList.size());
		int result=bmsDiscountService.updateList(list);
		if(result<=0){
			logger.info(taskId+"批量更新折扣费用失败");
		}else{
			//批量更新原始费用中的减免费用
			int feeResult=feesReceiveDispatchService.updateBatch(feeList);
			if(feeResult<=0){
				logger.info(taskId+"批量更新原始费用中的减免费用失败");
			}
		}

		
	}
	
	
	
	/**
     * 配送费折扣计算(排除所有指定的物流产品类型)
     * @param task 能唯一指定指定商家  指定物流商  指定科目的任务
     */
    private void discountServiceDispatch(BmsDiscountAsynTaskEntity task,BillPrepareExportTaskEntity entity){
        String taskId=task.getTaskId();
        Map<String,Object> condition=new HashMap<String,Object>();
        task.setTaskStatus(BmsCorrectAsynTaskStatusEnum.PROCESS.getCode());
        Map<String,BmsDiscountAccountVo> discountMap=new HashMap<String,BmsDiscountAccountVo>();
        bmsDiscountAsynTaskService.update(task);
        try {
            condition.put("startTime", task.getStartDate());
            condition.put("endTime", task.getEndDate());
            condition.put("customerId", task.getCustomerId());
            condition.put("carrierId", task.getCarrierId());
                    
            logger.info(taskId+"查询费用和统计的参数"+JSONObject.fromObject(condition));  
            //统计商家的月单量和金额  商家，物流商维度,特殊物流产品类型进行统计
            //物流产品类型,总单量,总金额(需要具体的折扣科目)
            //获取所有的折扣类型
            List<String> serviceList=new ArrayList<String>();
            Map<String,Object> map=new HashMap<String,Object>();
            map.put("typeCode", "DISCOUNT_SERVICE_CODE");
            map.put("code", task.getCarrierId());
            List<SystemCodeEntity> discountServiceList = systemCodeService.queryCodeList(map);
            for(SystemCodeEntity s:discountServiceList){
                serviceList.add(s.getExtattr1());
            }
            if(serviceList.size()>0){
                condition.put("serviceList", serviceList);
            }
     
            //统计商家的月单量和金额  商家，物流商 物流产品类型维度进行统计
            List<BmsDiscountAccountVo> discountAccountVoList=bmsDiscountService.queryAccountServiceList(condition);
            logger.info(taskId+"统计商家的月单量和金额，商家，物流商,物流产品类型维度"+JSONArray.fromObject(discountAccountVoList));
            if(discountAccountVoList.size()>0){
                for(BmsDiscountAccountVo vo:discountAccountVoList){
                    discountMap.put(vo.getServiceTypeCode(), vo);
                }
            }
            
            updateProgress(task,20);
            
            BmsQuoteDiscountTemplateEntity template=null;
            ContractDiscountQueryVo queryVo=new ContractDiscountQueryVo();
            if("bms".equals(task.getCustomerType())){
                logger.info(taskId+"进去bms查询折扣报价");
                //判断该科目是否签约折扣报价
                condition.put("customerId", task.getCustomerId());
                condition.put("contractTypeCode", "CUSTOMER_CONTRACT");
                condition.put("bizTypeCode", "DISPATCH");
                condition.put("subjectId", task.getSubjectCode());
                logger.info(taskId+"查询签约折扣服务的参数"+JSONObject.fromObject(condition));
                PriceContractDiscountItemEntity item=priceContractDiscountService.query(condition);
                logger.info(taskId+"查询签约折扣服务的结果"+JSONObject.fromObject(item));
                if(item==null){
                    logger.info(taskId+"bms该商家未签约折扣服务");
                    task.setRemark(taskId+"bms该商家未签约折扣服务");
                    if(entity!=null){
                        entity.setRemark(StringUtils.isBlank(entity.getRemark())?task.getSubjectName()+"bms特殊物流产品类型没有查询到该商家的统计记录":entity.getRemark()+";"+task.getSubjectName()+"bms特殊物流产品类型没有查询到该商家的统计记录");
                        entity.setProgress(0d);
                    }
                    task.setTaskRate(80);
                    task.setTaskStatus(BmsCorrectAsynTaskStatusEnum.FAIL.getCode());
                    bmsDiscountAsynTaskService.update(task);
                    return;
                }
                
                updateProgress(task,30);
                
                //查询折扣报价模板
                condition=new HashMap<String,Object>();
                condition.put("templateCode", item.getTemplateCode());
                template=bmsQuoteDiscountTemplateService.queryOne(condition);
                logger.info(taskId+"查询签约折扣模板的结果"+JSONObject.fromObject(template));
                if(template==null){
                    logger.info(taskId+"bms未查询到折扣报价模板");
                    task.setRemark(taskId+"bms未查询到折扣报价模板");
                    if(entity!=null){
                        entity.setRemark(StringUtils.isBlank(entity.getRemark())?task.getSubjectName()+"bms特殊物流产品类型未查询到折扣报价模板":entity.getRemark()+";"+task.getSubjectName()+"bms特殊物流产品类型未查询到折扣报价模板");
                        entity.setProgress(0d);
                    }
                    task.setTaskRate(80);
                    task.setTaskStatus(BmsCorrectAsynTaskStatusEnum.FAIL.getCode());
                    bmsDiscountAsynTaskService.update(task);
                    return;
                }
                
                //查询折扣方式
                if(StringUtils.isBlank(template.getDiscountType())){
                    logger.info(taskId+"模板"+template.getTemplateCode()+"未查询到折扣方式");
                    task.setRemark(taskId+"模板"+template.getTemplateCode()+"未查询到折扣方式");
                    if(entity!=null){
                        entity.setRemark(StringUtils.isBlank(entity.getRemark())?task.getSubjectName()+"bms特殊物流产品类型未查询到折扣方式":entity.getRemark()+";"+task.getSubjectName()+"bms特殊物流产品类型未查询到折扣方式");
                        entity.setProgress(0d);
                    }
                    task.setTaskStatus(BmsCorrectAsynTaskStatusEnum.FAIL.getCode());
                    task.setTaskRate(80);
                    bmsDiscountAsynTaskService.update(task);
                    return;
                }
                updateProgress(task,40);
            }else if("contract".equals(task.getCustomerType())){
                logger.info(taskId+"进入合同在线查询折扣报价");
                queryVo.setCustomerId(task.getCustomerId());
                queryVo.setSettlementTime(task.getCreateMonth());
                queryVo.setBizTypeCode("");
                logger.info(taskId+"查询合同在线服务订购号的参数"+JSONObject.fromObject(queryVo));
                List<ContractDiscountVo> disCountVoList=contractDiscountService.querySubject(queryVo);
                logger.info(taskId+"查询合同在线服务订购号的结果"+JSONArray.fromObject(disCountVoList));
                if(disCountVoList.size()>0){
                    ContractDiscountVo vo=disCountVoList.get(0);
                    queryVo.setSubjectId("");
                    //queryVo.setServiceOrderNo(vo.getServiceOrderNo());
                    queryVo.setServiceOrderNo(vo.getServiceOrderNo());
                    queryVo.setBizTypeCode("DISTRIBUTION");             
                    queryVo.setCarrierId(task.getCarrierId());
                    queryVo.setWarehouseCode("");
                }

                updateProgress(task,40);
            }
            
            //更新taskId到折扣费用表中
            condition.put("taskId", task.getTaskId());
            condition.put("startTime", task.getStartDate());
            condition.put("endTime", task.getEndDate());
            condition.put("customerId", task.getCustomerId());
            condition.put("carrierId", task.getCarrierId());
            condition.put("serviceList", serviceList);
            logger.info(taskId+"更新特殊物流产品类型折扣费用表的参数"+JSONObject.fromObject(condition));
            try{
                bmsDiscountService.updateFeeDiscountTask(condition);
            }catch(Exception ex){
                logger.info(taskId+"更新taskId到特殊物流产品类型折扣费用表中异常");
                task.setRemark(taskId+"更新taskId到特殊物流产品类型折扣费用表中异常");
                if(entity!=null){
                    entity.setRemark(StringUtils.isBlank(entity.getRemark())?task.getSubjectName()+"更新taskId到特殊物流产品类型折扣费用表中异常":entity.getRemark()+";"+task.getSubjectName()+"更新taskId到特殊物流产品类型折扣费用表中异常");
                    entity.setProgress(0d);
                }
                task.setTaskRate(80);
                task.setTaskStatus(BmsCorrectAsynTaskStatusEnum.FAIL.getCode());
                bmsDiscountAsynTaskService.update(task);
                return;
            }
            
            updateProgress(task,50);
            //批量获取业务数据 1000条一次（根据taskId关联）
            //int pageNo = 1;
            logger.info(taskId+"特殊物流产品类型进入批量循环处理");
            
            boolean doLoop = true;
            while (doLoop) {
                try {
                    PageInfo<FeesReceiveDispatchDiscountVo> pageInfo = 
                            bmsDiscountService.queryAll(condition, 1,1000);
                    if (null != pageInfo && pageInfo.getList().size() > 0) {
                        if (pageInfo.getList().size() < 1000) {
                            doLoop = false;
                        }
                        if("bms".equals(task.getCustomerType())){
                            logger.info(taskId+"进入bms折扣报价特殊物流产品类型计算");
                            handBmsServiceDiscount(pageInfo.getList(),task,template,discountMap);
                        }else if("contract".equals(task.getCustomerType())){
                            logger.info(taskId+"进入合同在线折扣报价特殊物流产品类型计算");
                            handContractServiceDiscount(pageInfo.getList(),task,queryVo,discountMap);
                        }                   
                    }else {
                        doLoop = false;
                    }   
                } catch (Exception e) {
                    // TODO: handle exception
                    logger.info(taskId+"循环bms折扣报价特殊物流产品类型计算异常");
                    doLoop = false;
                }
                        
            }
            
            task.setRemark(taskId+"特殊物流产品类型折扣计算成功");
            if(entity!=null){
                entity.setProgress(30d);
           }
            task.setTaskStatus(BmsCorrectAsynTaskStatusEnum.SUCCESS.getCode());
            updateProgress(task,100);
        } catch (Exception e1) {
            logger.info(taskId+"特殊物流产品类型折扣处理失败",e1);
            task.setTaskRate(80);
            task.setTaskStatus(BmsCorrectAsynTaskStatusEnum.FAIL.getCode());
            if(entity!=null){
                entity.setRemark(StringUtils.isBlank(entity.getRemark())?task.getSubjectName()+"特殊物流产品类型折扣处理失败":entity.getRemark()+";"+task.getSubjectName()+"特殊物流产品类型折扣处理失败");
                entity.setProgress(0d);
            }
            bmsDiscountAsynTaskService.update(task);
        }
        
    }
    
    /**
     * 折扣计算
     * @param list
     */
    public void handContractServiceDiscount(List<FeesReceiveDispatchDiscountVo> list,BmsDiscountAsynTaskEntity task,ContractDiscountQueryVo queryVo,Map<String,BmsDiscountAccountVo> discountMap){
        String taskId=task.getTaskId();
        //循环处理
          //获取单条业务数据
          //获取对应的原始报价 和 计算规则
          //调用配送折扣规则（将原始报价和折扣报价带入）获取新的报价
          //调用原始规则 进行计算
        //计算结果批量保存至配送折扣费用表
        //计算出折扣价（差值） 批量更新至原始费用表中的 减免金额中     
        List<FeesReceiveDispatchEntity> feeList=new ArrayList<FeesReceiveDispatchEntity>();
        
        //增加缓存
        Map<String,ContractDiscountConfigVo> sessionMap=new HashMap<String,ContractDiscountConfigVo>();
        
        for(FeesReceiveDispatchDiscountVo discountVo:list){
            BigDecimal amount=new BigDecimal(0);                        
            Map<String,Object> condition=new HashMap<String,Object>();
            //根据运单号查询业务数据对应得费用，判断是否是计算成功的，成功的继续折扣，失败的返回计算失败
            condition.put("waybillNo", discountVo.getWaybillNo());
            FeesReceiveDispatchEntity fee=feesReceiveDispatchService.queryOne(condition);
            //初始化折扣费用 key 物流产品类型+折扣方式+金额
            setValue(discountVo,amount);
            if(fee!=null && "1".equals(fee.getIsCalculated())){ 
                
                //物流产品类型
                String serviceTypeCode=StringUtils.isNotBlank(discountVo.getAdjustServiceTypeCode())?discountVo.getAdjustServiceTypeCode():discountVo.getServiceTypeCode();
                BmsDiscountAccountVo discountAccountVo=new BmsDiscountAccountVo();
                if(StringUtils.isNotBlank(serviceTypeCode)){
                    discountAccountVo=discountMap.get(serviceTypeCode);
                }
                
                if(discountAccountVo==null){
                    discountVo.setRemark(discountVo.getWaybillNo()+"没有查询到该商家的统计记录");
                    fee.setDerateAmount(0d);
                    feeList.add(fee);
                    continue;
                }
                
                if("MONTH_COUNT".equals(task.getDiscountType())){
                    queryVo.setDiscountType("MONTH_COUNT");
                    queryVo.setMonthCount(new BigDecimal(discountAccountVo.getOrderCount()));
                }else if("MONTH_AMOUNT".equals(task.getDiscountType())){
                    queryVo.setDiscountType("MONTH_AMOUNT");
                    queryVo.setMonthCount(new BigDecimal(discountAccountVo.getAmount()));
                }
            
                String key=serviceTypeCode+task.getDiscountType()+queryVo.getMonthCount().doubleValue()+"";
                //查询报价
                if(StringUtils.isNotBlank(serviceTypeCode)){
                    queryVo.setCarrierServiceType(serviceTypeCode);
                }
                ContractDiscountConfigVo configVo=null;
                try {
                    if(sessionMap.containsKey(key)){
                        configVo=sessionMap.get(key);
                    }else{
                        logger.info(discountVo.getWaybillNo()+"查询合同在线折扣报价参数"+JSONObject.fromObject(queryVo));
                        configVo=contractDiscountService.queryDiscount(queryVo);
                        logger.info(discountVo.getWaybillNo()+"查询合同在线折扣报价结果"+JSONObject.fromObject(configVo));
                        if(configVo==null){
                            //特殊产品类型找不到折扣报价
                            discountVo.setRemark(discountVo.getWaybillNo()+"不折扣");
                            discountVo.setIsCalculated(CalculateState.No_Exe.getCode());
                            fee.setDerateAmount(0d);
                            feeList.add(fee);
                            continue;
                        }
                        
                        sessionMap.put(key, configVo);
                    }   
                    
                    logger.info(discountVo.getWaybillNo()+"查询合同在线折扣报价结果"+JSONObject.fromObject(configVo));
                    
                    if(!serviceTypeCode.equals(configVo.getCarrierServiceType())){
                        discountVo.setRemark(discountVo.getWaybillNo()+"不折扣");
                        discountVo.setIsCalculated(CalculateState.No_Exe.getCode());
                        fee.setDerateAmount(0d);
                        feeList.add(fee);
                        continue;
                    }
                    
                    
                } catch (Exception e) {
                    // TODO: handle exception
                    //费用计算失败的、未查询到费用的、者报价为空的、计算规则为空的
                    discountVo.setRemark(discountVo.getWaybillNo()+"合同在线未查询到折扣报价");
                    fee.setDerateAmount(0d);
                    feeList.add(fee);
                    continue;
                }   
                
                
                if(configVo.getTotalDiscountPrice()!=null){
                    //整单折扣价
                    amount=configVo.getTotalDiscountPrice();
                    discountVo.setUnitPrice(configVo.getTotalDiscountPrice());
                }else if(configVo.getTotalDiscountRate()!=null){
                    //整单折扣率
                    if(!DoubleUtil.isBlank(fee.getAmount())){
                        BigDecimal newAmount=new BigDecimal(fee.getAmount());
                        amount=newAmount.multiply(configVo.getTotalDiscountRate());
                        discountVo.setUnitRate(configVo.getTotalDiscountRate());
                    }
                }else{
                    //其余的（包含首重续重折扣）
                    //查询原始报价
                    if(DoubleUtil.isBlank(fee.getUnitPrice())){ 
                        amount=getDispatchAmount(fee,configVo);
                        discountVo.setFirstPrice(configVo.getFirstWeightDiscountPrice());
                        discountVo.setFirstRate(configVo.getFirstWeightDiscountRate());
                        discountVo.setContinuePrice(configVo.getContinueWeightDiscountPrice());
                        discountVo.setContinueRate(configVo.getContinueWeightDiscountRate());
                    }
                }           
                handAmount(discountVo,fee,amount);
                discountVo.setRemark("折扣成功");
                feeList.add(fee);
            }else{
                //费用计算失败的、未查询到费用的、者报价为空的、计算规则为空的
                discountVo.setRemark(taskId+"费用计算失败或者报价为空或者计算规则为空");
                fee.setDerateAmount(0d);
                feeList.add(fee);
            }
        }
        
        //批量更新折扣费用
        logger.info(taskId+"批量更新折扣费用条数为"+list.size()+"原始费用条数为"+feeList.size());
        int result=bmsDiscountService.updateList(list);
        if(result<=0){
            logger.info(taskId+"批量更新折扣费用失败");
        }else{
            //批量更新原始费用中的减免费用
            int feeResult=feesReceiveDispatchService.updateBatch(feeList);
            if(feeResult<=0){
                logger.info(taskId+"批量更新原始费用中的减免费用");
            }
        }
    }
    
    /**
     * 折扣计算
     * @param list
     */
    public void handBmsServiceDiscount(List<FeesReceiveDispatchDiscountVo> list,BmsDiscountAsynTaskEntity task,BmsQuoteDiscountTemplateEntity template,Map<String,BmsDiscountAccountVo> discountMap){
        String taskId=task.getTaskId();
        //循环处理
          //获取单条业务数据
          //获取对应的原始报价 和 计算规则
          //调用配送折扣规则（将原始报价和折扣报价带入）获取新的报价
          //调用原始规则 进行计算
        //计算结果批量保存至配送折扣费用表
        //计算出折扣价（差值） 批量更新至原始费用表中的 减免金额中
        
        List<FeesReceiveDispatchEntity> feeList=new ArrayList<FeesReceiveDispatchEntity>();

        for(FeesReceiveDispatchDiscountVo discountVo:list){
            
            String waybillNo=discountVo.getWaybillNo();
            
            Map<String,Object> condition=new HashMap<String,Object>();
            //根据运单号查询业务数据对应得费用，判断是否是计算成功的，成功的继续折扣，失败的返回计算失败
            BigDecimal amount=new BigDecimal(0);
            condition.put("waybillNo", waybillNo);
            FeesReceiveDispatchEntity fee=feesReceiveDispatchService.queryOne(condition);
            //初始化折扣费用
            setValue(discountVo,amount);
            if(fee!=null && "1".equals(fee.getIsCalculated()) && StringUtils.isNotBlank(fee.getPriceId())){
                //查询明细报价
                condition=new HashMap<String,Object>();
                condition.put("templateCode", template.getTemplateCode());
                
                logger.info(taskId+"原始报价id为"+fee.getPriceId());
                //物流产品类型
                String serviceTypeCode=StringUtils.isNotBlank(discountVo.getAdjustServiceTypeCode())?
                        discountVo.getAdjustServiceTypeCode():discountVo.getServiceTypeCode();    
                condition.put("serviceTypeCode", serviceTypeCode);
                logger.info(taskId+"查询特殊物流产品类型折扣报价明细的参数"+JSONObject.fromObject(condition));
                List<BmsQuoteDiscountDetailEntity> discountPriceList=priceContractDiscountService.queryDiscountPrice(condition);
                //未查询到该物流产品类型对应得折扣，则不折扣
                if(discountPriceList==null || discountPriceList.size()<=0){
                    discountVo.setRemark(discountVo.getWaybillNo()+"不折扣");
                    discountVo.setIsCalculated(CalculateState.No_Exe.getCode());
                    fee.setDerateAmount(0d);
                    feeList.add(fee);
                    continue;
                }        
                BmsDiscountAccountVo discountAccountVo=new BmsDiscountAccountVo();
                //特殊折扣的物流产品类型，必须按照物流产品类型匹配，匹配不到单量和金额则直接返回
                if(StringUtils.isNotBlank(serviceTypeCode)){
                    discountAccountVo=discountMap.get(serviceTypeCode);             
                }
                    
                if(discountAccountVo==null){
                   continue;
                }
                
                condition=new HashMap<String,Object>();
                condition.put("templateCode", template.getTemplateCode());
                condition.put("createTime", fee.getCreateTime());
                //查询折扣报价
                if("MONTH_COUNT".equals(template.getDiscountType())){
                    //月单量
                    condition.put("count", discountAccountVo.getOrderCount());
                }else if("MONTH_AMOUNT".equals(template.getDiscountType())){
                    //月金额
                    condition.put("count", discountAccountVo.getAmount());
                }
                condition.put("serviceTypeCode", serviceTypeCode);
                List<BmsQuoteDiscountDetailEntity> priceList=priceContractDiscountService.queryDiscountPrice(condition);
                if(priceList==null || priceList.size()<=0){
                    logger.info(taskId+"未筛选到折扣报价明细");
                    discountVo.setRemark(taskId+"未筛选到折扣报价明细");
                    fee.setDerateAmount(0d);
                    feeList.add(fee);
                    continue;
                }     
                BmsQuoteDiscountDetailEntity discountPrice=priceList.get(0);
                                         
                logger.info("最后得到的报价"+JSONObject.fromObject(discountPrice));
                
                //判断是否是整单折扣
                logger.info(taskId+"折扣报价的id"+discountPrice.getId());        
                if(!DoubleUtil.isBlank(discountPrice.getUnitPrice())){
                    logger.info(waybillNo+"进入整单折扣价计算");
                    //整单折扣价
                    amount=BigDecimal.valueOf(discountPrice.getUnitPrice());
                    discountVo.setUnitPrice(BigDecimal.valueOf(discountPrice.getUnitPrice()));
                }else if(!DoubleUtil.isBlank(discountPrice.getUnitPriceRate())){
                    logger.info(waybillNo+"进入整单折扣率计算");
                    //整单折扣率
                    amount=BigDecimal.valueOf(fee.getAmount()*discountPrice.getUnitPriceRate()/100);
                    discountVo.setUnitRate(BigDecimal.valueOf(discountPrice.getUnitPriceRate()/100));
                }else{
                    //其余的（包含首重续重折扣）
                    //查询原始报价
                    logger.info(waybillNo+"进入首重续重折扣计算");
                    condition=new HashMap<String,Object>();
                    condition.put("id", fee.getPriceId());
                    BmsQuoteDispatchDetailVo oldPrice=priceDspatchService.queryOne(condition);
                    if(oldPrice==null){
                        setValue(discountVo,amount);
                        discountVo.setIsCalculated("2");
                        discountVo.setRemark("未查询到原始报价");
                        fee.setDerateAmount(0d);
                        feeList.add(fee);
                        continue;
                    }
                    
                    //===========================通过原始报价和折扣报价，得到最后计算的首重续重===============================
                    BmsQuoteDispatchDetailVo newprice=getNewPrice(oldPrice,discountPrice,discountVo);   
                    
                    //开始进行计算
                    if(!DoubleUtil.isBlank(newprice.getUnitPrice())){
                        amount=BigDecimal.valueOf(newprice.getUnitPrice());
                        discountVo.setUnitPrice(amount);
                    }else{
                        amount=BigDecimal.valueOf(newprice.getFirstWeight()<fee.getChargedWeight()?newprice.getFirstWeightPrice()+newprice.getContinuedPrice()*((fee.getChargedWeight()-newprice.getFirstWeight())/newprice.getContinuedWeight()):newprice.getFirstWeightPrice());                  
                    }
                }   
                handAmount(discountVo,fee,amount);
                discountVo.setQuoteId(discountPrice.getId().longValue());
                discountVo.setRemark("折扣成功");
                feeList.add(fee);
            }else{
                //费用计算失败的、未查询到费用的、者报价为空的、计算规则为空的
                discountVo.setRemark(taskId+"费用计算失败或者报价为空或者计算规则为空");
                fee.setDerateAmount(0d);
                feeList.add(fee);
            }
        }
        
        //批量更新折扣费用
        logger.info(taskId+"批量更新折扣费用条数为"+list.size()+"原始费用条数为"+feeList.size());
        int result=bmsDiscountService.updateList(list);
        if(result<=0){
            logger.info(taskId+"批量更新折扣费用失败");
        }else{
            //批量更新原始费用中的减免费用
            int feeResult=feesReceiveDispatchService.updateBatch(feeList);
            if(feeResult<=0){
                logger.info(taskId+"批量更新原始费用中的减免费用失败");
            }
        }

        
    }
	
	public void setValue(FeesReceiveDispatchDiscountVo discountVo,BigDecimal amount){
		discountVo.setIsCalculated("2");
		discountVo.setCalculateTime(JAppContext.currentTimestamp());
		discountVo.setDerateAmount(amount);
		discountVo.setDiscountAmount(amount);
		discountVo.setUnitRate(BigDecimal.ZERO);
		discountVo.setUnitPrice(BigDecimal.ZERO);
		discountVo.setFirstRate(BigDecimal.ZERO);
		discountVo.setFirstPrice(BigDecimal.ZERO);
		discountVo.setContinueRate(BigDecimal.ZERO);
		discountVo.setContinuePrice(BigDecimal.ZERO);
	}
	
	/**
	 * 处理仓储折扣费用
	 * @param vo
	 * @param fees
	 * @param amount
	 */
	public void handStorageAmount(FeesReceiveStorageDiscountVo vo,FeesReceiveStorageEntity fees,BigDecimal amount){
		//对折扣后价格四舍五入 	
		vo.setDiscountAmount(amount);//折扣后价格
		BigDecimal derateAmount=fees.getCost().subtract(amount);//减免金额
		vo.setDerateAmount(derateAmount);//减免金额
		vo.setIsCalculated("1");//计算状态
		vo.setRemark("计算成功");
		vo.setCalculateTime(JAppContext.currentTimestamp());//计算时间
		fees.setDerateAmount(derateAmount.doubleValue());//费用里的减免金额
	}
	
	/**
	 * 处理配送折扣费用
	 * @param vo
	 * @param fees
	 * @param amount
	 */
	public void handAmount(FeesReceiveDispatchDiscountVo vo,FeesReceiveDispatchEntity fees,BigDecimal amount){
		//对折扣后价格四舍五入 	
		vo.setDiscountAmount(amount);//折扣后价格
		BigDecimal oldAmount=BigDecimal.valueOf(fees.getAmount());//原始价格
		BigDecimal derateAmount=oldAmount.subtract(amount);//减免金额
		vo.setDerateAmount(derateAmount);//减免金额
		vo.setIsCalculated("1");//计算状态
		vo.setRemark("计算成功");
		vo.setCalculateTime(JAppContext.currentTimestamp());//计算时间
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
	public BmsQuoteDispatchDetailVo getNewPrice(BmsQuoteDispatchDetailVo oldPrice,BmsQuoteDiscountDetailEntity discount,FeesReceiveDispatchDiscountVo discountVo){
		
		if(!DoubleUtil.isBlank(discount.getFirstPrice())){
			//折扣首价
			oldPrice.setFirstWeightPrice(discount.getFirstPrice());
			discountVo.setFirstPrice(BigDecimal.valueOf(discount.getFirstPrice()));
		}else if(!DoubleUtil.isBlank(discount.getFirstPriceRate())){
			//首价折扣率
			oldPrice.setFirstWeightPrice(oldPrice.getFirstWeightPrice()*discount.getFirstPriceRate()/100);
			discountVo.setFirstRate(BigDecimal.valueOf(discount.getFirstPriceRate()/100));
		}else if(!DoubleUtil.isBlank(discount.getContinuePrice())){
			//折扣续重价
			oldPrice.setContinuedPrice(discount.getContinuePrice());
			discountVo.setContinuePrice(BigDecimal.valueOf(discount.getContinuePrice()));
		}else if(!DoubleUtil.isBlank(discount.getContinuePirceRate())){
			//续重折扣率
			oldPrice.setContinuedPrice(oldPrice.getContinuedPrice()*discount.getContinuePirceRate()/100);
			discountVo.setContinueRate(BigDecimal.valueOf(discount.getContinuePirceRate()/100));
		}
		
		return oldPrice;
	}
	
	
	/**
	 * 获取仓储折扣后的价格
	 * @param fee
	 * @param configVo
	 * @return
	 */
	public BigDecimal getStorageAmount(FeesReceiveStorageEntity fee,ContractDiscountConfigVo configVo){
		//总计算重量
		Double quantity=fee.getQuantity().doubleValue();
		//获取费用表中的
		Double firstNum=fee.getFirstNum(); //首重
		Double firstPrice=fee.getFirstPrice();   //首重价格
		Double continueNum=fee.getContinueNum();//续重
		Double continuePrice=fee.getContinuedPrice();//续重价格
		
		if(configVo.getFirstWeightDiscountPrice()!=null){
			firstPrice=configVo.getFirstWeightDiscountPrice().doubleValue();
		}
		if(configVo.getFirstWeightDiscountRate()!=null){
			firstPrice=configVo.getFirstWeightDiscountRate().doubleValue()*firstPrice;
		}
		if(configVo.getContinueWeightDiscountPrice()!=null){
			continuePrice=configVo.getContinueWeightDiscountPrice().doubleValue();
		}
		if(configVo.getContinueWeightDiscountRate()!=null){
			continuePrice=configVo.getContinueWeightDiscountRate().doubleValue()*continuePrice;
		}						
		BigDecimal amount = BigDecimal.valueOf(firstNum<quantity?firstPrice+ ((quantity-firstNum)/continueNum)*continuePrice:firstPrice);	
		return amount;
	}
	
	/**
	 * 获取配送折扣后的价格
	 * @param fee
	 * @param configVo
	 * @return
	 */
	public BigDecimal getDispatchAmount(FeesReceiveDispatchEntity fee,ContractDiscountConfigVo configVo){
		//总计算重量
		Double weight=fee.getChargedWeight();
		//获取费用表中的
		Double firstWeight=fee.getHeadWeight(); //首重
		Double firstPrice=fee.getHeadPrice();   //首重价格
		Double continueWeight=fee.getContinuedWeight();//续重
		Double continuePrice=fee.getContinuedPrice();//续重价格
		
		if(configVo.getFirstWeightDiscountPrice()!=null){
			firstPrice=configVo.getFirstWeightDiscountPrice().doubleValue();
		}
		if(configVo.getFirstWeightDiscountRate()!=null){
			firstPrice=configVo.getFirstWeightDiscountRate().doubleValue()*firstPrice;
		}
		if(configVo.getContinueWeightDiscountPrice()!=null){
			continuePrice=configVo.getContinueWeightDiscountPrice().doubleValue();
		}
		if(configVo.getContinueWeightDiscountRate()!=null){
			continuePrice=configVo.getContinueWeightDiscountRate().doubleValue()*continuePrice;
		}						
		BigDecimal amount = BigDecimal.valueOf(firstWeight<weight?firstPrice+ ((weight-firstWeight)/continueWeight)*continuePrice:firstPrice);	
		
		return amount;
	}
	
	/**
	 * 物流产品类型过滤
	 * @param quotes
	 * @param map
	 * @return
	 */
	public BmsQuoteDiscountDetailEntity quoteFilter(List<BmsQuoteDiscountDetailEntity> quotes,String serviceTypeCode) {
		
		try{
			Integer level = 3;
			BmsQuoteDiscountDetailEntity entity = new BmsQuoteDiscountDetailEntity();
						
			for (BmsQuoteDiscountDetailEntity quoteEntity : quotes) {
				
				String service_quote = StringUtils.isEmpty(quoteEntity.getServiceTypeCode())?"":quoteEntity.getServiceTypeCode();
				
				if(!serviceTypeCode.equals(service_quote) && StringUtils.isNotEmpty(service_quote)){
					continue;//物流产品类型不匹配
				}
			
				Integer servicelevel = serviceTypeCode.equals(service_quote)?1:2;//物流产品类型	
				
				if(servicelevel<level){
					level = servicelevel;
					entity = quoteEntity;
				}
			}
			if(level == 3){
				return null;
			}
			else{
				return entity;
			}
		}
		catch(Exception ex){
			logger.error("物流产品类型过滤异常", ex);
			return null;
		}
		
	}
	
	
	/*
     * 解析JSON---->Map
     */
    @SuppressWarnings("unchecked")
    private Map<String, Object> resolveJsonToMap(String json) {
        //解析JSON
        Map<String, Object> map = null;
        try {
            map = (Map<String, Object>)JSON.parse(json);
        } catch (Exception e) {
            logger.error("JSON解析异常 {}", e);
            return null;
        }
        return map;
    }

    public void sendMq(String destinationName, Map<String, Object> condition){
        //Map------>JSON
        final String msg = JsonUtils.toJson(condition);
        jmsQueueTemplate.send(destinationName, new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                return session.createTextMessage(msg);
            }
        });
    }
}
