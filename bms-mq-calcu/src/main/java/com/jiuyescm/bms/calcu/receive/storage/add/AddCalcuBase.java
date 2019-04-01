package com.jiuyescm.bms.calcu.receive.storage.add;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.jiuyescm.bms.asyn.vo.BmsCalcuTaskVo;
import com.jiuyescm.bms.base.group.service.IBmsGroupSubjectService;
import com.jiuyescm.bms.calcu.base.CalcuTaskListener;
import com.jiuyescm.bms.calculate.api.IBmsCalcuService;
import com.jiuyescm.bms.calculate.vo.BmsFeesQtyVo;
import com.jiuyescm.bms.common.enumtype.CalculateState;
import com.jiuyescm.bms.drools.IFeesCalcuService;
import com.jiuyescm.bms.general.entity.BizAddFeeEntity;
import com.jiuyescm.bms.general.entity.FeesReceiveStorageEntity;
import com.jiuyescm.bms.general.service.IBizAddFeeService;
import com.jiuyescm.bms.general.service.IFeesReceiveStorageService;
import com.jiuyescm.bms.general.service.IPriceContractInfoService;
import com.jiuyescm.bms.general.service.SequenceService;
import com.jiuyescm.bms.quotation.contract.entity.PriceContractInfoEntity;
import com.jiuyescm.bms.quotation.contract.entity.PriceContractItemEntity;
import com.jiuyescm.bms.quotation.contract.repository.imp.IPriceContractItemRepository;
import com.jiuyescm.bms.quotation.storage.entity.PriceExtraQuotationEntity;
import com.jiuyescm.bms.quotation.storage.repository.IPriceExtraQuotationRepository;
import com.jiuyescm.bms.quotation.transport.entity.GenericTemplateEntity;
import com.jiuyescm.bms.quotation.transport.repository.IGenericTemplateRepository;
import com.jiuyescm.bms.rule.receiveRule.repository.IReceiveRuleRepository;
import com.jiuyescm.cfm.common.JAppContext;
import com.jiuyescm.common.utils.DoubleUtil;
import com.jiuyescm.contract.quote.api.IContractQuoteInfoService;
import com.jiuyescm.contract.quote.vo.ContractBizTypeEnum;
import com.jiuyescm.contract.quote.vo.ContractQuoteQueryInfoVo;


public class AddCalcuBase extends CalcuTaskListener<BizAddFeeEntity,FeesReceiveStorageEntity>{

	private Logger logger = LoggerFactory.getLogger(AddCalcuBase.class);
	
	@Autowired private IContractQuoteInfoService contractQuoteInfoService;
	@Autowired private IPriceContractInfoService jobPriceContractInfoService;
	@Autowired private IFeesReceiveStorageService feesReceiveStorageService;
	//@Autowired private IPriceGeneralQuotationRepository priceGeneralQuotationRepository;
	@Autowired private IReceiveRuleRepository receiveRuleRepository;
	@Autowired private IFeesCalcuService feesCalcuService;
	@Autowired private IPriceContractItemRepository priceContractItemRepository;
	
	@Autowired private SequenceService sequenceService;
	@Autowired private IBizAddFeeService bizAddFeeService;
	@Autowired private IGenericTemplateRepository genericTemplateRepository;
	@Autowired private IPriceExtraQuotationRepository priceExtraQuotationRepository;
	@Autowired private IBmsGroupSubjectService bmsGroupSubjectService;
	@Autowired private IBmsCalcuService bmsCalcuService;

	@Override
	protected void initDict() {
		// TODO Auto-generated method stub
		
	}
	
	
	@Override
	protected List<BizAddFeeEntity> queryBillList(Map<String, Object> map) {
		List<BizAddFeeEntity> bizList = bizAddFeeService.querybizAddFee(map);
		return bizList;
	}
	
	@Override
	protected void calcuForBms(BmsCalcuTaskVo vo,BizAddFeeEntity entity,FeesReceiveStorageEntity storageFeeEntity){
		Timestamp time=JAppContext.currentTimestamp();
		entity.setCalculateTime(time);
		Map<String,Object> map=new HashMap<String,Object>();
		String customerId=entity.getCustomerid();
		//FeesReceiveStorageEntity storageFeeEntity = initFeeEntity(entity);
		storageFeeEntity.setCalculateTime(time);
		entity.setCalculateTime(JAppContext.currentTimestamp());
		PriceContractInfoEntity contractEntity =null;

		map.clear();
		map.put("customerid", customerId);
		map.put("contractTypeCode", "CUSTOMER_CONTRACT");
	    contractEntity = jobPriceContractInfoService.queryContractByCustomer(map);
	
		if(contractEntity == null || StringUtils.isEmpty(contractEntity.getContractCode())){
			//XxlJobLogger.log(String.format("-->"+entity.getId()+"未查询到合同  订单号【%s】--商家【%s】", entity.getId(),customerId));
			storageFeeEntity.setIsCalculated(CalculateState.Contract_Miss.getCode());
			storageFeeEntity.setCalcuMsg("bms合同缺失");
			//feesList.add(storageFeeEntity);
			return;
		}
		//XxlJobLogger.log("-->"+entity.getId()+"验证合同   耗时【{0}】毫秒  合同编号 【{1}】",(current - start),contractEntity.getContractCode());
		
		//----验证签约服务
		Map<String,Object> contractItems_map=new HashMap<String,Object>();
		contractItems_map.put("contractCode", contractEntity.getContractCode());
		contractItems_map.put("subjectId", "wh_value_add_subject");
		List<PriceContractItemEntity> contractItems = priceContractItemRepository.query(contractItems_map);
		if(contractItems == null || contractItems.size() == 0 || StringUtils.isEmpty(contractItems.get(0).getTemplateId())) {
			//XxlJobLogger.log("-->"+entity.getId()+"未签约服务  订单号【{0}】--商家【{1}】", entity.getId(),entity.getCustomerid());
			storageFeeEntity.setIsCalculated(CalculateState.Contract_Miss.getCode());
			storageFeeEntity.setCalcuMsg(entity.getRemark()+"未签约服务;");
			//feesList.add(storageFeeEntity);
			return;
		}
		//XxlJobLogger.log("-->"+entity.getId()+"验证签约服务耗时：【{0}】毫秒  ",(current - start));		
		
		/*验证报价 报价*/
		GenericTemplateEntity generalEntity=null;

		map.clear();
		//map.put("subjectId","wh_value_add_subject");
		//quoTemplete=priceGeneralQuotationRepository.query(map);
		map.put("templateCode", contractItems.get(0).getTemplateId());
		generalEntity=genericTemplateRepository.query(map);
		
		if(generalEntity==null){
			//XxlJobLogger.log("-->"+entity.getId()+"报价未配置");
			storageFeeEntity.setIsCalculated(CalculateState.Quote_Miss.getCode());
			storageFeeEntity.setCalcuMsg(entity.getRemark()+"报价未配置;");
			//feesList.add(storageFeeEntity);
			return;
		}
		
		
		
		entity.setCalculateTime(JAppContext.currentTimestamp());
		storageFeeEntity.setCalculateTime(entity.getCalculateTime());
		
		//数量
		double num=DoubleUtil.isBlank(entity.getAdjustNum())?entity.getNum():entity.getAdjustNum();
				
		//计算方法
		double amount=0d;
		//一口价				
        //费用 = 商品数量*模板单价
		Map<String, Object> param = new HashMap<>();
		param.put("templateId", generalEntity.getId().toString());
		param.put("subjectId", entity.getFeesType());
		List<PriceExtraQuotationEntity> extraList= priceExtraQuotationRepository.queryPriceByParam(param);
		if (extraList == null || extraList.size() <= 0) {
			entity.setIsCalculated(CalculateState.Other.getCode());
			storageFeeEntity.setIsCalculated(CalculateState.Other.getCode());
			entity.setRemark(entity.getRemark()+"没有维护增值费一口价报价;");
		}else {
			amount=num*extraList.get(0).getUnitPrice();							
			storageFeeEntity.setUnitPrice(extraList.get(0).getUnitPrice());
			storageFeeEntity.setParam3(generalEntity.getId()+"");

			storageFeeEntity.setCost(BigDecimal.valueOf(amount));
			entity.setRemark(entity.getRemark()+"计算成功;");
			entity.setIsCalculated(CalculateState.Finish.getCode());
			storageFeeEntity.setIsCalculated(CalculateState.Finish.getCode());	
		}				
		
	}

	@Override
	protected void calcuForContract(FeesReceiveStorageEntity fee,Map<String, Object> errorMap) {
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
	protected void updateBatch(List<FeesReceiveStorageEntity> fees) {
		feesReceiveStorageService.updateBatch(fees);
	}

	@Override
	protected BmsFeesQtyVo feesCountReport(String customerId,String subjectCode, Integer creMonth) {
		BmsFeesQtyVo vo = bmsCalcuService.queryFeesQtyForSto(customerId, subjectCode, creMonth);
		return vo;
	}
	
	@Override
	protected FeesReceiveStorageEntity initFeeEntity(BmsCalcuTaskVo vo,BizAddFeeEntity entity) {
		entity.setRemark("");
		FeesReceiveStorageEntity fee = new FeesReceiveStorageEntity();
		if(entity.getPrice()!=null){
    		fee.setCost(new BigDecimal(entity.getPrice()));
    	}else{
    		fee.setCost(new BigDecimal(0));
    	}
		double num=DoubleUtil.isBlank(entity.getAdjustNum())?entity.getNum():entity.getAdjustNum();
		if(!DoubleUtil.isBlank(num)){
			fee.setQuantity(num);
		}
		return fee;
	}



	@Override
	protected boolean isNoExe(BizAddFeeEntity t,FeesReceiveStorageEntity f,Map<String, Object> errorMap) {
		/*if(!"succ".equals(errorMap.get("success"))){
			f.setIsCalculated(errorMap.get("is_calculated").toString());
			f.setCalcuMsg(errorMap.get("msg").toString());
			return true;
		}*/
		return false;
	}

	@Override
	protected ContractQuoteQueryInfoVo getCtConditon(BmsCalcuTaskVo vo,BizAddFeeEntity entity) {
		ContractQuoteQueryInfoVo queryVo = new ContractQuoteQueryInfoVo();
		queryVo.setCustomerId(entity.getCustomerid());
		queryVo.setBizTypeCode(ContractBizTypeEnum.STORAGE.getCode());
		queryVo.setSubjectCode(vo.getSubjectCode());
		queryVo.setCurrentTime(entity.getCreateTime());
		queryVo.setWarehouseCode(entity.getWarehouseCode());
		return queryVo;

	}


}
