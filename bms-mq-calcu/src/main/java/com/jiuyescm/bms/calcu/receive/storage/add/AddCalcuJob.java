package com.jiuyescm.bms.calcu.receive.storage.add;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.jiuyescm.bms.asyn.service.IBmsCalcuTaskService;
import com.jiuyescm.bms.asyn.vo.BmsCalcuTaskVo;
import com.jiuyescm.bms.base.group.service.IBmsGroupSubjectService;
import com.jiuyescm.bms.calcu.CalcuLog;
import com.jiuyescm.bms.calcu.base.ICalcuService;
import com.jiuyescm.bms.calcu.receive.CommonService;
import com.jiuyescm.bms.calcu.receive.ContractCalcuService;
import com.jiuyescm.bms.calcu.receive.BmsContractBase;
import com.jiuyescm.bms.common.enumtype.CalculateState;
import com.jiuyescm.bms.drools.IFeesCalcuService;
import com.jiuyescm.bms.general.entity.BizAddFeeEntity;
import com.jiuyescm.bms.general.entity.FeesReceiveStorageEntity;
import com.jiuyescm.bms.general.service.IBizAddFeeService;
import com.jiuyescm.bms.general.service.IFeesReceiveStorageService;
import com.jiuyescm.bms.general.service.IPriceContractInfoService;
import com.jiuyescm.bms.general.service.SequenceService;
import com.jiuyescm.bms.quotation.contract.repository.imp.IPriceContractItemRepository;
import com.jiuyescm.bms.quotation.storage.entity.PriceExtraQuotationEntity;
import com.jiuyescm.bms.quotation.storage.repository.IPriceExtraQuotationRepository;
import com.jiuyescm.bms.quotation.transport.entity.GenericTemplateEntity;
import com.jiuyescm.bms.quotation.transport.repository.IGenericTemplateRepository;
import com.jiuyescm.bms.rule.receiveRule.repository.IReceiveRuleRepository;
import com.jiuyescm.cfm.common.JAppContext;
import com.jiuyescm.common.utils.DoubleUtil;
import com.jiuyescm.constants.CalcuNodeEnum;
import com.jiuyescm.contract.quote.api.IContractQuoteInfoService;
import com.jiuyescm.contract.quote.vo.ContractBizTypeEnum;
import com.jiuyescm.contract.quote.vo.ContractQuoteQueryInfoVo;

@Component("addCalcuJob")
@Scope("prototype")
public class AddCalcuJob extends BmsContractBase implements ICalcuService<BizAddFeeEntity,FeesReceiveStorageEntity> {

	private Logger logger = LoggerFactory.getLogger(AddCalcuJob.class);
		
	@Autowired private IContractQuoteInfoService contractQuoteInfoService;
	@Autowired private IPriceContractInfoService jobPriceContractInfoService;
	@Autowired private IFeesReceiveStorageService feesReceiveStorageService;
	@Autowired private IReceiveRuleRepository receiveRuleRepository;
	@Autowired private IFeesCalcuService feesCalcuService;
	@Autowired private IPriceContractItemRepository priceContractItemRepository;
	@Autowired private ContractCalcuService contractCalcuService;
	@Autowired private SequenceService sequenceService;
	@Autowired private IBizAddFeeService bizAddFeeService;
	@Autowired private IGenericTemplateRepository genericTemplateRepository;
	@Autowired private IPriceExtraQuotationRepository priceExtraQuotationRepository;
	@Autowired private IBmsGroupSubjectService bmsGroupSubjectService;
	@Autowired private CommonService commonService;
	@Autowired IBmsCalcuTaskService bmsCalcuTaskService;


	//private String quoTempleteCode = null;
	private Map<String, Object> errorMap = null;
	private GenericTemplateEntity addQuoTemplete;

	public void process(BmsCalcuTaskVo taskVo,String contractAttr){
		super.process(taskVo, contractAttr);
		logger.info("合同信息{}",contractInfo.getContractNo());
		getQuoTemplete();
		serviceSubjectCode = subjectCode;
		errorMap = new HashMap<String, Object>();
		initConf();
	}
	
	@Override
	public void getQuoTemplete(){
		Map<String, Object> map = new HashMap<>();
		if(quoTempleteCode!=null){
			map.put("templateCode", quoTempleteCode);
			addQuoTemplete = genericTemplateRepository.query(map);
		}
	}
	
	@Override
	public void initConf(){
		
	}
	
	@Override
	public void calcu(Map<String, Object> map){
		
		List<BizAddFeeEntity> bizList = bizAddFeeService.querybizAddFee(map);
		List<FeesReceiveStorageEntity> fees = new ArrayList<>();
		if(bizList == null || bizList.size() == 0){
			commonService.taskCountReport(taskVo, "STORAGE");
			return;
		}
		logger.info("taskId={} 查询行数【{}】",taskVo.getTaskId(),bizList.size());
		for (BizAddFeeEntity entity : bizList) {
			FeesReceiveStorageEntity fee = initFee(entity);
			fees.add(fee);
			if(isNoExe(entity, fee)){
				continue; //如果不计算费用,后面的逻辑不在执行，只是在最后更新数据库状态
			}
			if("BMS".equals(contractAttr)){
				calcuForBms(entity,fee);
			}
			else {
				calcuForContract(entity,fee);
			}
		}
		updateBatch(bizList,fees);
		calceCount += bizList.size();
		int taskRate = (int)Math.floor((calceCount*100)/unCalcuCount);
		try {
			if(unCalcuCount!=0){
				bmsCalcuTaskService.updateRate(taskVo.getTaskId(), taskRate);
			}
		} catch (Exception e) {
			logger.error("更新任务进度异常",e);
		}
		if(bizList!=null && bizList.size() == 1000){
			calcu(map);
		}
	}
	
	@Override
	public FeesReceiveStorageEntity initFee(BizAddFeeEntity entity){
		//打印业务数据日志
		CalcuLog.printLog(CalcuNodeEnum.BIZ.getCode().toString(), "", entity, cbiVo);
		FeesReceiveStorageEntity fee = new FeesReceiveStorageEntity();
    	fee.setFeesNo(entity.getFeesNo());
    	if(entity.getPrice()!=null){
    		fee.setCost(new BigDecimal(entity.getPrice()));
    	}else{
    		fee.setCost(new BigDecimal(0));
    	}
    	fee.setCalculateTime(JAppContext.currentTimestamp());
		fee.setUnitPrice(entity.getUnitPrice());
		fee.setSubjectCode("wh_value_add_subject");
		fee.setOtherSubjectCode(entity.getFeesType());
		fee.setUnit(entity.getFeesUnit());
		double num=DoubleUtil.isBlank(entity.getAdjustNum())?entity.getNum():entity.getAdjustNum();
		if(!DoubleUtil.isBlank(num)){
			fee.setQuantity(num);
		}
        fee.setParam1(entity.getItem());
		fee.setCostType("FEE_TYPE_GENEARL");
		return fee;
	}
	
	@Override
	public boolean isNoExe(BizAddFeeEntity entity,FeesReceiveStorageEntity fee){
		return false;
	}
	
	@Override
	public void calcuForBms(BizAddFeeEntity entity,FeesReceiveStorageEntity fee){
		//合同校验
		if(contractInfo == null){
			fee.setIsCalculated(CalculateState.Contract_Miss.getCode());
			fee.setCalcuMsg("bms合同缺失");
			CalcuLog.printLog(CalcuNodeEnum.CONTRACT.getCode().toString(), "bms合同缺失", null, cbiVo);
			return;
		}
		
		if("fail".equals(quoTempleteCode)){
			fee.setIsCalculated(CalculateState.Quote_Miss.getCode());
			fee.setCalcuMsg("未签约服务");
			CalcuLog.printLog(CalcuNodeEnum.CONTRACT.getCode().toString(), "未签约服务", contractInfo, cbiVo);
			return;
		}
		
		if(addQuoTemplete == null){
			fee.setIsCalculated(CalculateState.Quote_Miss.getCode());
			fee.setCalcuMsg("报价模板缺失");
			CalcuLog.printLog(CalcuNodeEnum.CONTRACT.getCode().toString(), "报价模板缺失", contractInfo, cbiVo);
			return;
		}
		
		try{
			//数量
			double num=DoubleUtil.isBlank(entity.getAdjustNum())?entity.getNum():entity.getAdjustNum();
			//计算方法
			double amount=0d;
			//一口价				
            //费用 = 商品数量*模板单价
			Map<String, Object> param = new HashMap<>();
			param.put("templateId", addQuoTemplete.getId());
			param.put("subjectId", entity.getFeesType());
			List<PriceExtraQuotationEntity> extraList= priceExtraQuotationRepository.queryPriceByParam(param);
			if (extraList == null || extraList.size() <= 0) {
				fee.setIsCalculated(CalculateState.Other.getCode());
				fee.setCalcuMsg(entity.getRemark()+"没有维护增值费一口价报价;");
			}else {
				amount=num*extraList.get(0).getUnitPrice();							
				fee.setUnitPrice(extraList.get(0).getUnitPrice());
				fee.setParam3(addQuoTemplete.getId()+"");
				fee.setCost(BigDecimal.valueOf(amount));
				//fee.setParam4(priceType);
				fee.setCalcuMsg(entity.getRemark()+"计算成功;");
				fee.setIsCalculated(CalculateState.Finish.getCode());	
			}				
		}catch(Exception ex){
			fee.setIsCalculated(CalculateState.Sys_Error.getCode());
			fee.setCalcuMsg("费用计算异常");
		}
	}
	
	@Override
	public void calcuForContract(BizAddFeeEntity entity,FeesReceiveStorageEntity fee){
		ContractQuoteQueryInfoVo queryVo = getCtConditon(entity);
		contractCalcuService.calcuForContract(entity, fee, taskVo, errorMap, queryVo,cbiVo);
		if("succ".equals(errorMap.get("success").toString())){
			if(fee.getCost().compareTo(BigDecimal.ZERO) == 1){
				fee.setIsCalculated(CalculateState.Finish.getCode());
				logger.info("计算成功，费用【{}】",fee.getCost());
			}
			else{
				fee.setIsCalculated(CalculateState.Sys_Error.getCode());
				logger.info("计算不成功，费用【{}】",fee.getCost());
				fee.setCalcuMsg("未计算出金额");
			}
		}
		else{
			fee.setIsCalculated(errorMap.get("is_calculated").toString());
			fee.setCalcuMsg(errorMap.get("msg").toString());
		}
	}
	
	@Override
	public ContractQuoteQueryInfoVo getCtConditon(BizAddFeeEntity entity) {
		ContractQuoteQueryInfoVo queryVo = new ContractQuoteQueryInfoVo();
		queryVo.setCustomerId(entity.getCustomerid());
		queryVo.setBizTypeCode(ContractBizTypeEnum.STORAGE.getCode());
		queryVo.setSubjectCode(subjectCode);
		queryVo.setCurrentTime(entity.getCreateTime());
		queryVo.setWarehouseCode(entity.getWarehouseCode());
		return queryVo;

	}
	
	@Override
	public void updateBatch(List<BizAddFeeEntity> bizList,List<FeesReceiveStorageEntity> feeList) {
		feesReceiveStorageService.updateBatch(feeList);
	}


	

	
	
}
