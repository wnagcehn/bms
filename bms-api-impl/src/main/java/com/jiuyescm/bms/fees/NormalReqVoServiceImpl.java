package com.jiuyescm.bms.fees;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.jiuyescm.bms.base.calcu.vo.CalcuReqVo;
import com.jiuyescm.bms.base.calcu.vo.CalcuResultVo;
import com.jiuyescm.bms.biz.storage.entity.BizAddFeeEntity;
import com.jiuyescm.bms.biz.storage.entity.BizBaseFeeEntity;
import com.jiuyescm.bms.calculate.base.IFeesCalcuService;
import com.jiuyescm.bms.chargerule.receiverule.entity.BillRuleReceiveEntity;
import com.jiuyescm.bms.chargerule.receiverule.service.IReceiveRuleService;
import com.jiuyescm.bms.common.enumtype.CalculateState;
import com.jiuyescm.bms.quotation.contract.entity.PriceContractInfoEntity;
import com.jiuyescm.bms.quotation.contract.entity.PriceContractItemEntity;
import com.jiuyescm.bms.quotation.contract.repository.imp.IPriceContractItemRepository;
import com.jiuyescm.bms.quotation.contract.service.IPriceContractService;
import com.jiuyescm.bms.quotation.dispatch.repository.IPriceDispatchDao;
import com.jiuyescm.bms.quotation.storage.entity.PriceExtraQuotationEntity;
import com.jiuyescm.bms.quotation.storage.entity.PriceGeneralQuotationEntity;
import com.jiuyescm.bms.quotation.storage.entity.PriceStepQuotationEntity;
import com.jiuyescm.bms.quotation.storage.repository.IPriceStepQuotationRepository;
import com.jiuyescm.bms.quotation.storage.service.IPriceExtraQuotationService;
import com.jiuyescm.bms.quotation.storage.service.IPriceGeneralQuotationService;
import com.jiuyescm.bms.quotation.transport.entity.GenericTemplateEntity;
import com.jiuyescm.bms.quotation.transport.service.IGenericTemplateService;

@Service("normalReqVoServiceImpl")
public class NormalReqVoServiceImpl<T> implements INormalReqVoService<T> {
	
	@Resource private IPriceGeneralQuotationService priceGeneralQuotationService;
	@Resource private IPriceStepQuotationRepository repository;
	@Resource private IReceiveRuleService receiveRuleService;
	@Resource private IPriceContractService priceContractInfoService;
	@Resource private IPriceDispatchDao iPriceDispatchDao;
	@Resource private IPriceContractItemRepository priceContractItemRepository;
	@Resource private IPriceExtraQuotationService priceExtraQuotationService;
	@Resource  private IGenericTemplateService genericTemplateService;
	@Resource private IFeesCalcuService feesCalcuService;
	@Resource private IStandardReqVoService standardReqVoService;
	
	@SuppressWarnings("unchecked")
	@Override
	public List<BizBaseFeeEntity> getStorageGeneralReqVo(List<BizBaseFeeEntity> bizList) {
		// TODO Auto-generated method stub
		List<BizBaseFeeEntity> listBiz=new ArrayList<BizBaseFeeEntity>();
		
		for(int i=0;i<bizList.size();i++){
			CalcuReqVo reqVo = new CalcuReqVo();		
			BizBaseFeeEntity entity=bizList.get(i);
			String subjectId=entity.getFeesType();
			
			Map<String,Object> conditionMap=new HashMap<String,Object>();
			//验证合同
			conditionMap.put("customerid", entity.getCustomerid());
			conditionMap.put("contractTypeCode", "CUSTOMER_CONTRACT");
			PriceContractInfoEntity contractEntity = priceContractInfoService.queryContractByCustomer(conditionMap);	
			if(contractEntity == null || StringUtils.isEmpty(contractEntity.getContractCode())){
				entity.setRemark("合同不存在");
				entity.setIsCalculated(CalculateState.Contract_Miss.getCode());
				listBiz.add(entity);
				continue;
			}
			
			//验证是否签约服务
			Map<String,Object> contractItems_map=new HashMap<String,Object>();
			contractItems_map.put("contractCode", contractEntity.getContractCode());
			contractItems_map.put("subjectId", entity.getFeesType());
			List<PriceContractItemEntity> contractItems = priceContractItemRepository.query(contractItems_map);
			if(contractItems == null || contractItems.size() == 0 || StringUtils.isEmpty(contractItems.get(0).getTemplateId())) {
				entity.setRemark("未签约服务");
				entity.setIsCalculated(CalculateState.Quote_Miss.getCode());
				listBiz.add(entity);
				continue;
			}
			
			PriceGeneralQuotationEntity quoTemplete = null;			 //报价模板
			List<PriceStepQuotationEntity> priceStepList=null;		 //报价阶梯	
			List<PriceGeneralQuotationEntity> quoTempleteList= null; //报价模板集合
			BillRuleReceiveEntity ruleEntity=null;	  //规则对象
			
			//查询报价
			conditionMap.clear();
			conditionMap.put("customerId",  entity.getCustomerid());
			conditionMap.put("subjectId",subjectId);
			quoTemplete=priceGeneralQuotationService.query(conditionMap);//查询报价模板
			if(quoTemplete == null){ //----------------报价模板不存在			
				//走标准报价
				reqVo=standardReqVoService.getStorageReqVo(subjectId);
			}else{
				if("PRICE_TYPE_STEP".equals(quoTemplete.getPriceType())){
					conditionMap.clear();
					conditionMap.put("quotationId", quoTemplete.getId());
					priceStepList = repository.queryPriceStepByQuatationId(conditionMap);
					if(priceStepList==null||priceStepList.size()==0){
						//走标准报价
						reqVo=standardReqVoService.getStorageReqVo(subjectId);
					}else{
						reqVo.setQuoEntites(priceStepList);
					}
				}
				else{
					quoTempleteList = new ArrayList<PriceGeneralQuotationEntity>();
					quoTempleteList.add(quoTemplete);
					reqVo.setQuoEntites(quoTempleteList);
				}
				
			}
			
			//------------------查询规则
			conditionMap.clear();	
			conditionMap.put("customerid",  entity.getCustomerid());
			conditionMap.put("subjectId",subjectId);
			ruleEntity=receiveRuleService.queryByCustomerId(conditionMap);
			if(ruleEntity == null){			
				entity.setRemark("规则未配置");
				entity.setIsCalculated(CalculateState.Quote_Miss.getCode());
				listBiz.add(entity);
				continue;
			}
			reqVo.setRuleNo(ruleEntity.getQuotationNo());
			reqVo.setRuleStr(ruleEntity.getRule());
			
			//实体类
			entity.setNum(entity.getAdjustNum()==null?entity.getNum():entity.getAdjustNum());
			System.out.println("数量"+entity.getNum());
			reqVo.setBizData(entity);
			//开始进行计算
			CalcuResultVo calcuResultVo=feesCalcuService.FeesCalcuService(reqVo);
			if(calcuResultVo.getPrice() == null){
				entity.setRemark("规则计算失败");
				entity.setIsCalculated(CalculateState.Quote_Miss.getCode());
				listBiz.add(entity);
				continue;
			}
			else{				
				entity.setRemark("规则计算成功");
				entity.setIsCalculated(CalculateState.Finish.getCode());
				entity.setPrice(calcuResultVo.getPrice().doubleValue());
				entity.setUnitPrice(calcuResultVo.getUnitPrice());
				listBiz.add(entity);
				continue;
			}
			
		}
		return listBiz;
	}


	@Override
	@SuppressWarnings("unchecked")
	public List<BizAddFeeEntity> getStorageExtraReqVo(List<BizAddFeeEntity> bizlist) {
		// TODO Auto-generated method stub
		List<BizAddFeeEntity> listBiz=new ArrayList<BizAddFeeEntity>();
		for(int i=0;i<bizlist.size();i++){
			CalcuReqVo reqVo = new CalcuReqVo();		
			BizAddFeeEntity entity=bizlist.get(i);
			String subjectId="wh_value_add_subject";
			Map<String,Object> conditionMap=new HashMap<String,Object>();
			//验证合同
			conditionMap.put("customerid", entity.getCustomerid());
			conditionMap.put("contractTypeCode", "CUSTOMER_CONTRACT");
			PriceContractInfoEntity contractEntity = priceContractInfoService.queryContractByCustomer(conditionMap);	
			if(contractEntity == null || StringUtils.isEmpty(contractEntity.getContractCode())){
				entity.setRemark("合同不存在");
				entity.setIsCalculated(CalculateState.Contract_Miss.getCode());
				listBiz.add(entity);
				continue;
			}
			
			//验证是否签约服务
			Map<String,Object> contractItems_map=new HashMap<String,Object>();
			contractItems_map.put("contractCode", contractEntity.getContractCode());
			contractItems_map.put("subjectId",subjectId);
			List<PriceContractItemEntity> contractItems = priceContractItemRepository.query(contractItems_map);
			if(contractItems == null || contractItems.size() == 0 || StringUtils.isEmpty(contractItems.get(0).getTemplateId())) {
				entity.setRemark("未签约服务");
				entity.setIsCalculated(CalculateState.Quote_Miss.getCode());
				listBiz.add(entity);
				continue;
			}
			
			GenericTemplateEntity quoTemplete= null;			 //报价模板
			List<PriceExtraQuotationEntity> priceStepList=null;		 //报价阶梯	
			BillRuleReceiveEntity ruleEntity=null;				     //规则对象	
			//查询报价
			conditionMap.clear();
			conditionMap.put("customerId", entity.getCustomerid());
			conditionMap.put("storageTemplateType", "STORAGE_EXTRA_PRICE_TEMPLATE");
			quoTemplete=genericTemplateService.query(conditionMap);//查询标准报价模板
			if(quoTemplete == null){ //----------------标准报价模板不存在
				Map<String,Object> reqMap=new HashMap<>();
				reqVo=standardReqVoService.getOtherReqVo(reqMap);
			}else{
				conditionMap.clear();
				conditionMap.put("templateId", quoTemplete.getId());		
				priceStepList = priceExtraQuotationService.queryPrice(conditionMap);
				if(priceStepList==null||priceStepList.size()==0){
					reqVo=standardReqVoService.getOtherReqVo(conditionMap);
				}else{
					reqVo.setQuoEntites(priceStepList);
				}
			}
						
			//------------------查询规则
			conditionMap.clear();
			conditionMap.put("customerid",entity.getCustomerid());
			conditionMap.put("subjectId", subjectId);
			ruleEntity=receiveRuleService.queryByCustomerId(conditionMap);
			if(ruleEntity == null){
				entity.setRemark("规则未配置");
				entity.setIsCalculated(CalculateState.Quote_Miss.getCode());
				listBiz.add(entity);
				continue;
			}
			reqVo.setRuleNo(ruleEntity.getQuotationNo());
			reqVo.setRuleStr(ruleEntity.getRule());
			
			
			//实体类
			entity.setNum(entity.getAdjustNum()==null?entity.getNum():entity.getAdjustNum());
			reqVo.setBizData(entity);
			//开始进行计算
			CalcuResultVo calcuResultVo=feesCalcuService.FeesCalcuService(reqVo);
			if(calcuResultVo.getPrice() == null){
				entity.setRemark("规则计算失败");
				entity.setIsCalculated(CalculateState.Quote_Miss.getCode());
				listBiz.add(entity);
				continue;
			}
			else{				
				entity.setRemark("规则计算成功");
				entity.setIsCalculated(CalculateState.Finish.getCode());
				entity.setPrice(calcuResultVo.getPrice().doubleValue());
				entity.setUnitPrice(calcuResultVo.getUnitPrice());
				listBiz.add(entity);
				continue;
			}
			
		}
		return listBiz;
	}

}
