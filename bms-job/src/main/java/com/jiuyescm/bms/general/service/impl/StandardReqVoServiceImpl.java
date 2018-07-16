package com.jiuyescm.bms.general.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jiuyescm.bms.base.calcu.vo.CalcuReqVo;
import com.jiuyescm.bms.chargerule.receiverule.entity.BillRuleReceiveEntity;
import com.jiuyescm.bms.general.service.IPriceContractInfoService;
import com.jiuyescm.bms.general.service.IStandardReqVoService;
import com.jiuyescm.bms.quotation.dispatch.entity.vo.PriceMainDispatchEntity;
import com.jiuyescm.bms.quotation.dispatch.repository.IPriceDispatchDao;
import com.jiuyescm.bms.quotation.storage.entity.PriceGeneralQuotationEntity;
import com.jiuyescm.bms.quotation.storage.entity.PriceMaterialQuotationEntity;
import com.jiuyescm.bms.quotation.storage.entity.PriceStepQuotationEntity;
import com.jiuyescm.bms.quotation.storage.repository.IPriceGeneralQuotationRepository;
import com.jiuyescm.bms.quotation.storage.repository.IPriceMaterialQuotationRepository;
import com.jiuyescm.bms.quotation.storage.repository.IPriceStepQuotationRepository;
import com.jiuyescm.bms.rule.receiveRule.repository.IReceiveRuleRepository;

@Service("standardReqVoServiceImpl")
public class StandardReqVoServiceImpl<T> implements IStandardReqVoService<T> {

	private static String customerId = "001";
	private static final Logger logger = Logger.getLogger(StandardReqVoServiceImpl.class.getName());
	
	@Autowired private IPriceGeneralQuotationRepository priceGeneralQuotationRepository;
	@Autowired private IPriceStepQuotationRepository repository;
	@Autowired private IReceiveRuleRepository receiveRuleRepository;
	@Autowired private IPriceMaterialQuotationRepository priceMaterialQuotationRepository;
	@Autowired private IPriceContractInfoService jobPriceContractInfoService;
	@Autowired private IPriceDispatchDao iPriceDispatchDao;
	
	@SuppressWarnings("unchecked")
	@Override
	public CalcuReqVo<T> getStorageReqVo(String subjectID) {
		// TODO Auto-generated method stub
		CalcuReqVo reqVo = new CalcuReqVo();
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("customerid", customerId);
		map.put("subjectId",subjectID);
		PriceGeneralQuotationEntity quoTemplete = null;			 //报价模板
		List<PriceStepQuotationEntity> priceStepList=null;		 //报价阶梯	
		List<PriceGeneralQuotationEntity> quoTempleteList= null; //报价模板集合
		BillRuleReceiveEntity ruleEntity=null;				     //规则对象
		try{
			quoTemplete=priceGeneralQuotationRepository.query(map);//查询标准报价模板
			if(quoTemplete == null){ //----------------标准报价模板不存在
				map.put("succ", "false");
				map.put("msg", "标准报价模板不存在");
				reqVo.setParams(map);
				return reqVo;
			}
			if("PRICE_TYPE_STEP".equals(quoTemplete.getPriceType())){
				map.clear();
				map.put("quotationId", quoTemplete.getId());
				priceStepList = repository.queryPriceStepByQuatationId(map);
				if(priceStepList==null||priceStepList.size()==0){
					map.put("succ", "false");
					map.put("msg", "标准报价阶梯不存在");
					reqVo.setParams(map);
					return reqVo;
				}else{
					reqVo.setQuoEntites(priceStepList);
				}
			}
			else{
				quoTempleteList = new ArrayList<PriceGeneralQuotationEntity>();
				quoTempleteList.add(quoTemplete);
				reqVo.setQuoEntites(quoTempleteList);
			}
			
			//------------------查询规则
			map.clear();
			map.put("customerid",customerId);
			map.put("subjectId", subjectID);
			ruleEntity=receiveRuleRepository.queryByCustomerId(map);
			if(ruleEntity == null){
				map.put("succ", "false");
				map.put("msg", "标准规则未配置");
				reqVo.setParams(map);
				return reqVo;
			}
			reqVo.setRuleNo(ruleEntity.getQuotationNo());
			reqVo.setRuleStr(ruleEntity.getRule());
			map.put("succ", "true");
			map.put("msg", "标准报价和规则验证通过");
			reqVo.setParams(map);
		}
		catch(Exception ex){
			map.put("succ", "false");
			map.put("msg", ex.getMessage());
			reqVo.setParams(map);
			logger.error(ex);
			return reqVo;
		}
		return reqVo;
	}

	@Override
	public CalcuReqVo<T> getMaterialReqVo(Map<String, Object> map) {
		CalcuReqVo reqVo = new CalcuReqVo();
		map.put("customerid", customerId);
		List<PriceMaterialQuotationEntity> quoTempleteList= null; //报价模板集合
		BillRuleReceiveEntity ruleEntity=null;				     //规则对象
		try{
			quoTempleteList=priceMaterialQuotationRepository.queryStandardMaterial(map);
			if(quoTempleteList == null || quoTempleteList.size() == 0){ //----------------标准报价模板不存在
				map.put("succ", "false");
				map.put("msg", "标准耗材报价未配置");
				reqVo.setParams(map);
				return reqVo;
			}
			
			//------------------查询规则
			ruleEntity=receiveRuleRepository.queryByCustomerId(map);
			if(ruleEntity == null){
				map.put("succ", "false");
				map.put("msg", "标准规则未配置");
				reqVo.setParams(map);
				return reqVo;
			}
			reqVo.setQuoEntites(quoTempleteList);
			reqVo.setRuleNo(ruleEntity.getQuotationNo());
			reqVo.setRuleStr(ruleEntity.getRule());
			map.put("succ", "true");
			map.put("msg", "标准报价和规则验证通过");
			reqVo.setParams(map);
		}
		catch(Exception ex){
			map.put("succ", "false");
			map.put("msg", ex.getMessage());
			reqVo.setParams(map);
			logger.error(ex);
			return reqVo;
		}
		return reqVo;
	}

	@Override
	public CalcuReqVo<T> getDispatchReceiveReqVo(String subjectId,String wareHouseId,String province) {
		CalcuReqVo reqVo = new CalcuReqVo();
		BillRuleReceiveEntity ruleEntity=null;				     //规则对象
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("customerId",customerId);
		map.put("subjectId",subjectId);
		try{
			String templateId=jobPriceContractInfoService.queryStandardTemplateId(map);
			if(templateId == null){
				map.put("succ", "false");
				map.put("msg", "标准配送报价模板未配置");
				reqVo.setParams(map);
				return reqVo;
			}
			
			map=new HashMap<String,Object>();
			map.put("templateId", templateId);
			map.put("wareHouseId", wareHouseId);
			map.put("province", province);
			List<PriceMainDispatchEntity> priceList = iPriceDispatchDao.queryAllByTemplateId(map);
			if(priceList==null || priceList.size() == 0){
				map.put("succ", "false");
				map.put("msg", "标准配送报价未配置");
				reqVo.setParams(map);
				return reqVo;
			}
			map.clear();
			map.put("customerid",customerId);
			map.put("subjectId", subjectId);
			ruleEntity=receiveRuleRepository.queryByCustomerId(map);
			if(ruleEntity == null){
				map.put("succ", "false");
				map.put("msg", "标准配送规则未配置");
				reqVo.setParams(map);
				return reqVo;
			}
			
			reqVo.setQuoEntites(priceList);
			reqVo.setRuleNo(ruleEntity.getQuotationNo());
			reqVo.setRuleStr(ruleEntity.getRule());
			map.put("succ", "true");
			map.put("msg", "标准报价和规则验证通过");
			reqVo.setParams(map);
		}
		catch(Exception ex){
			map.put("succ", "false");
			map.put("msg", ex.getMessage());
			reqVo.setParams(map);
			logger.error(ex);
			return reqVo;
		}
		return reqVo;
	}

	@Override
	public CalcuReqVo<T> getDispatchPayReqVo(String subjectId,String wareHouseId,String province) {
		// TODO Auto-generated method stub
		return null;
	}

}