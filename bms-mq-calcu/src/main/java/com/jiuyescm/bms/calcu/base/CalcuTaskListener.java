package com.jiuyescm.bms.calcu.base;

import java.util.ArrayList;
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
import com.jiuyescm.bms.chargerule.receiverule.entity.BillRuleReceiveEntity;
import com.jiuyescm.bms.common.enumtype.CalculateState;
import com.jiuyescm.bms.drools.IFeesCalcuService;
import com.jiuyescm.bms.rule.receiveRule.repository.IReceiveRuleRepository;
import com.jiuyescm.bs.util.StringUtil;
import com.jiuyescm.cfm.common.JAppContext;
import com.jiuyescm.common.utils.DateUtil;
import com.jiuyescm.common.utils.MD5Util;
import com.jiuyescm.contract.quote.api.IContractQuoteInfoService;
import com.jiuyescm.contract.quote.vo.ContractQuoteInfoVo;
import com.jiuyescm.contract.quote.vo.ContractQuoteQueryInfoVo;
import com.jiuyescm.exception.BizException;
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
	
	protected String subjectCode = "";
	protected String subjectName = "";
	
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
				subjectCode = taskVo.getSubjectCode();
				subjectName = taskVo.getSubjectName();
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
		lock.lock(lockString, 5, new LockCallback<Map<String, Object>>() {

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
		String succ = handMap.get("success").toString();
		if(!"success".equals(succ)){
			logger.info("taskId={} {}",taskVo.getTaskId(),handMap.get("remark").toString());
			taskVo.setTaskStatus(40);//未获得锁，不处理，丢弃
			taskVo.setRemark(handMap.get("remark").toString());
			taskVo.setTaskRate(99);
			bmsCalcuTaskService.saveTask(taskVo);
			return;
		}
		if("success".equals(succ)){
			//请求成功 发送mq消息
			taskVo.setTaskStatus(20);
			taskVo.setTaskRate(100);
			taskVo.setFinishTime(JAppContext.currentTimestamp());
			bmsCalcuTaskService.saveTask(taskVo);
		}
		logger.info("taskId={} 计算任务处理结束 耗时【{}】ms",taskVo.getTaskId(),(System.currentTimeMillis()-start));
	}	
	
	protected void calcuJob(BmsCalcuTaskVo taskVo){
		try {
			//修改任务的状态和时间  等待->处理中
			taskVo.setTaskStatus(10); //计算状态设为 处理中
			taskVo.setProcessTime(JAppContext.currentTimestamp()); //设置开始处理时间
			//总单量统计，计算单量统计
			logger.info("taskId={} 总单量统计",taskVo.getTaskId());
			BmsFeesQtyVo feesQtyVo = feesCountReport(taskVo.getCustomerId(),taskVo.getSubjectCode(),taskVo.getCreMonth());
			logger.info("taskId={} 总单量【{}】 本次计算单量【{}】",taskVo.getTaskId(),feesQtyVo.getFeesCount(),feesQtyVo.getUncalcuCount());
			
			taskVo.setFeesCount(feesQtyVo.getFeesCount()); 		//设置总的费用数
			taskVo.setUncalcuCount(feesQtyVo.getUncalcuCount());//设置本次待计算的费用数
			bmsCalcuTaskService.update(taskVo);
			
			//查询合同归属
			String contractAttr = bmsCalcuService.queryContractAttr(taskVo.getCustomerId());
			logger.info("taskId={} contractAttr=",taskVo.getTaskId(),contractAttr);
			if(StringUtil.isEmpty(contractAttr)){
				taskVo.setTaskStatus(40);//合同归属不存在，任务丢弃
				taskVo.setTaskRate(99);
				taskVo.setRemark("合同归属不存在，任务丢弃");
				taskCountReport(taskVo);
				logger.info("taskId={} 合同归属不存在，任务丢弃",taskVo.getTaskId());
				return;
			}
			
			//费用计算
			Map<String, Object> cond = getQueryMap(taskVo);
			logger.info("taskId={} 数据查询条件{}",taskVo.getTaskId(),cond);
			Map<String, Object> bmsMap = new HashMap<String, Object>();
			/*if("BMS".equals(contractAttr)){
				//查询bms 合同（ContractInfo），签约服务(ContractInfoItem)，报价模板(QuoModelInfo)等
				//success; is_calculated; msg
				bmsMap.put("success", "succ");		//校验是否通过
				bmsMap.put("ContractNo", ""); 		//合同编号
				bmsMap.put("QuoModelNo", "");		//报价模板编号
				bmsMap.put("msg", "");				//描述信息
				getBmsContract(taskVo,bmsMap); 
			}*/
			generalCalcu(taskVo, contractAttr,cond,bmsMap);
			taskCountReport(taskVo);
		} catch (Exception e1) {
			logger.error("taskId={} 计算任务执行异常",taskVo.getTaskId(),e1);
			return;
		}
	}
	
	protected void taskCountReport(BmsCalcuTaskVo taskVo){
		//统计计算状态
		BmsFeesQtyVo feesQtyVo = feesCountReport(taskVo.getCustomerId(),taskVo.getSubjectCode(),taskVo.getCreMonth());
		taskVo.setFinishTime(JAppContext.currentTimestamp());
		taskVo.setFeesCount(feesQtyVo.getFeesCount());
		taskVo.setBeginCount(feesQtyVo.getBeginCount());
		taskVo.setFinishCount(feesQtyVo.getFinishCount());
		taskVo.setSysErrorCount(feesQtyVo.getSysErrorCount());
		taskVo.setContractMissCount(feesQtyVo.getContractMissCount());
		taskVo.setQuoteMissCount(feesQtyVo.getQuoteMissCount());
		taskVo.setNoExeCount(feesQtyVo.getNoExeCount());
		taskVo.setUncalcuCount(feesQtyVo.getUncalcuCount());
		taskVo.setTaskRate(100);
		try{
			bmsCalcuTaskService.update(taskVo);
		}catch(Exception ex){
			logger.error("计算任务更新异常",ex);
		}
		
	}
	
	
	protected void generalCalcu(BmsCalcuTaskVo taskVo,String contractAttr,Map<String, Object> cond,Map<String, Object> bmsMap){

		Map<String, Object> errorMap = new HashMap<String, Object>();
		List<T> list = queryBillList(cond);
		if(list == null || list.size()==0){
			return;
		}
		logger.info("taskId={} 查询行数【{}】",taskVo.getTaskId(),list.size());
		List<F> fees = new ArrayList<>();
		for (T t : list) {
			errorMap.put("success", "succ");
			F f = initFeeEntity(t); //初始化计费参数
			fees.add(f);
			if(isNoExe(t, f,errorMap)){
				continue; //如果不计算费用,后面的逻辑不在执行，只是在最后更新数据库状态
			}
			if("BMS".equals(contractAttr)){
				calcuForBms(taskVo,t,f);
			}
			else {
				calcuForContract(t, f, taskVo, errorMap);
			}
		}
		updateBatch(fees);
		taskVo.setCalcuCount(taskVo.getCalcuCount()+list.size());
		try{
			bmsCalcuTaskService.update(taskVo);
		}catch(Exception ex){
			logger.info("计算任务更新异常",ex);
		}
		if(list!=null && list.size() == 1000){
			generalCalcu(taskVo, contractAttr, cond,errorMap);
		}
	}
	
	
	/**
	 * 查询bms 合同，签约服务，报价模板等
	 * @param vo
	 * @param errorMap
	 */
	/*protected void getBmsContract(BmsCalcuTaskVo vo,Map<String, Object> bmsMap){
		
		Map<String, Object> cond = new HashMap<>();
		cond.put("customerid", vo.getCustomerId());
		cond.put("contractTypeCode", "CUSTOMER_CONTRACT");
		PriceContractInfoEntity contractEntity = jobPriceContractInfoService.queryContractByCustomer(cond);
		if(contractEntity==null){
			logger.info("taskId={} bms合同缺失",vo.getTaskId());
			bmsMap.put("success", "fail");
			bmsMap.put("is_calculated", CalculateState.Contract_Miss.getCode());
			bmsMap.put("msg", "bms合同缺失");
			return;
	    }
		bmsMap.put("Contract", contractEntity.getContractCode());
		logger.info("taskId={} bms合同编号：{}",vo.getTaskId(),contractEntity.getContractCode());
		
		Map<String,Object> contractItems_map=new HashMap<String,Object>();
		contractItems_map.put("contractCode", contractEntity.getContractCode());
		contractItems_map.put("subjectId", vo.getSubjectCode());
		List<PriceContractItemEntity> contractItems = priceContractItemRepository.query(contractItems_map);
		if(contractItems == null || contractItems.size() == 0 || StringUtils.isEmpty(contractItems.get(0).getTemplateId())) {
			logger.info("taskId={} bms合同未签约服务",vo.getTaskId());
			bmsMap.put("success", "fail");
			bmsMap.put("is_calculated", CalculateState.Contract_Miss.getCode());
			bmsMap.put("msg", "bms合同未签约服务");
			return;
		}
		bmsMap.put("ContractInfoItem", contractItems.get(0).getTemplateId());
		queryQuoModel(vo,bmsMap);
	}*/
	
	protected void calcuForContract(T t, F f, BmsCalcuTaskVo vo, Map<String, Object> errorMap){
		ContractQuoteQueryInfoVo queryVo = getCtConditon(vo,t);
		printLog(vo, t, f, "合同在线合同查询参数：", queryVo, "");
		queryCtForContract(vo,t,f,queryVo,errorMap);
		calcuForContract(f, errorMap);
		
	}
	
	protected void queryCtForContract(BmsCalcuTaskVo vo,T t, F f,ContractQuoteQueryInfoVo queryVo,Map<String, Object> errorMap){
		ContractQuoteInfoVo cqVo = null;
		try{
			cqVo = contractQuoteInfoService.queryUniqueColumns(queryVo);
			printLog(vo, t, f, "合同在线合同明细：", cqVo, "");
		}
		catch(BizException ex){
			printLog(vo, t, f, "合同在线合同缺失：", ex.getMessage(), "");
			errorMap.put("success", "fail");
			errorMap.put("is_calculated", CalculateState.Contract_Miss.getCode());
			errorMap.put("msg", "合同在线合同缺失");
			return;
		}
		if(cqVo == null){
			return;
		}
		if(StringUtil.isEmpty(cqVo.getRuleCode())){
			printLog(vo, t, f, "合同在线规则未绑定", "" , "");
			errorMap.put("success", "fail");
			errorMap.put("is_calculated", CalculateState.Quote_Miss.getCode());
			errorMap.put("msg", "合同在线规则未绑定");
			return;
		}
		try{
			Map<String, Object> con = new HashMap<>();
			con.put("quotationNo", cqVo.getRuleCode());
			BillRuleReceiveEntity ruleEntity = receiveRuleRepository.queryOne(con);
			if (null == ruleEntity) {
				String msg = "规则【"+cqVo.getRuleCode()+"】不存在";
				printLog(vo, t, f, msg, "" , "");
				errorMap.put("success", "fail");
				errorMap.put("is_calculated", CalculateState.Sys_Error.getCode());
				errorMap.put("msg", msg);
				return;
			}
			//获取合同在线查询条件
			Map<String, Object> cond = new HashMap<String, Object>();
			feesCalcuService.ContractCalcuService(t, cond, ruleEntity.getRule(), ruleEntity.getQuotationNo());
			printLog(vo, t, f, "获取合同在线报价参数：", cond , "");
			ContractQuoteInfoVo rtnQuoteInfoVo = null;
			
			try {
			    if(cond == null || cond.size() == 0){
			    	printLog(vo, t, f, "规则引擎拼接条件异常：", "", "");
					errorMap.put("success", "fail");
					errorMap.put("is_calculated", CalculateState.Sys_Error.getCode());
					errorMap.put("msg", "规则引擎拼接条件异常");
					return;
				}
				rtnQuoteInfoVo = contractQuoteInfoService.queryQuotes(cqVo, cond);
			} catch (BizException e) {
				// TODO: handle exception
				String msg = "获取合同在线报价异常:"+e.getMessage();
				printLog(vo, t, f, msg, "", "");
				errorMap.put("success", "fail");
				errorMap.put("is_calculated", CalculateState.Quote_Miss.getCode());
				errorMap.put("msg", msg);
				return;
			}
			
			/*logger.info("获取合同在线报价结果"+JSONObject.fromObject(rtnQuoteInfoVo));*/
			for (Map<String, String> map : rtnQuoteInfoVo.getQuoteMaps()) {
				int i = rtnQuoteInfoVo.getQuoteMaps().indexOf(map);
				printLog(vo, t, f, "合同在线报价信息["+i+"]", map, "");
			}
			//调用规则计算费用
			feesCalcuService.ContractCalcuService(f, rtnQuoteInfoVo.getQuoteMaps(), ruleEntity.getRule(), ruleEntity.getQuotationNo());
			
		}
		catch(Exception ex){
			logger.info("系统异常",ex);
			errorMap.put("success", "fail");
			errorMap.put("is_calculated", CalculateState.Sys_Error.getCode());
			errorMap.put("msg", "系统异常");
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
	
	//主流程控制
	/*protected abstract void calcu(BmsCalcuTaskVo vo, Map<String, Object> errorMap,Map<String, Object> cond);
	*/
	//查询业务数据 （业务数据关联费用数据查询）
	protected abstract List<T> queryBillList(Map<String,Object> map);
	
	//bms费用计算
	protected abstract void calcuForBms(BmsCalcuTaskVo vo,T t,F f);
	
	/**
	 * 合同在线费用计算
	 * @param f
	 * @param errorMap
	 */
	protected abstract void calcuForContract(F f,Map<String, Object> errorMap);
	
	//查询bms报价模板
	protected abstract void queryQuoModel(BmsCalcuTaskVo vo,Map<String, Object> errorMap);
	
	/**
	 * 获取合同在线合同查询条件
	 * @param vo
	 * @param t
	 * @return
	 */
	protected abstract ContractQuoteQueryInfoVo getCtConditon(BmsCalcuTaskVo vo,T t);
	
	//初始化费用对象
	protected abstract F initFeeEntity(T t);
	
	//是否不计费 true-不计费  false-计费   如果不计算，需要设置费用的状态
	protected abstract boolean isNoExe(T t,F f,Map<String, Object> errorMap);
	
	//批量更新费用
	protected abstract void updateBatch(List<F> ts);
	
	//统计商家维度各状态计算单量
	protected abstract BmsFeesQtyVo feesCountReport(String customerId, String subjectCode,Integer creMonth);
	
	protected abstract void printLog(BmsCalcuTaskVo vo,T t,F f,String descrip,Object obj,String nodeName);
	
	/*//查询合同在线合同
	protected abstract ContractQuoteInfoVo queryCtForContract(BmsCalcuTaskVo vo,T t,Map<String, Object> errorMap);
*/

}
