package com.jiuyescm.bms.fees;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.alibaba.druid.util.StringUtils;
import com.jiuyescm.bms.base.calcu.vo.CalcuReqVo;
import com.jiuyescm.bms.chargerule.receiverule.entity.BillRuleReceiveEntity;
import com.jiuyescm.bms.common.log.service.IBmsErrorLogInfoService;
import com.jiuyescm.bms.fees.abnormal.service.impl.FeesAbnormalServiceImpl;
import com.jiuyescm.bms.quotation.contract.service.IPriceContractService;
import com.jiuyescm.bms.quotation.dispatch.entity.PriceDispatchTemplateEntity;
import com.jiuyescm.bms.quotation.dispatch.entity.vo.PriceMainDispatchEntity;
import com.jiuyescm.bms.quotation.dispatch.repository.IPriceDispatchDao;
import com.jiuyescm.bms.quotation.dispatch.service.IPriceDispatchTemplateService;
import com.jiuyescm.bms.quotation.storage.entity.PriceExtraQuotationEntity;
import com.jiuyescm.bms.quotation.storage.entity.PriceGeneralQuotationEntity;
import com.jiuyescm.bms.quotation.storage.entity.PriceMaterialQuotationEntity;
import com.jiuyescm.bms.quotation.storage.entity.PriceStepQuotationEntity;
import com.jiuyescm.bms.quotation.storage.repository.IPriceGeneralQuotationRepository;
import com.jiuyescm.bms.quotation.storage.repository.IPriceMaterialQuotationRepository;
import com.jiuyescm.bms.quotation.storage.repository.IPriceStepQuotationRepository;
import com.jiuyescm.bms.quotation.storage.service.IPriceExtraQuotationService;
import com.jiuyescm.bms.quotation.transport.entity.GenericTemplateEntity;
import com.jiuyescm.bms.quotation.transport.service.IGenericTemplateService;
import com.jiuyescm.bms.rule.receiveRule.repository.IReceiveRuleRepository;

@Service("standardReqVoServiceImpl")
public class StandardReqVoServiceImpl<T> implements IStandardReqVoService<T> {
	
	@Resource private IPriceGeneralQuotationRepository priceGeneralQuotationRepository;
	@Resource private IPriceStepQuotationRepository repository;
	@Resource private IReceiveRuleRepository receiveRuleRepository;
	@Resource private IPriceMaterialQuotationRepository priceMaterialQuotationRepository;
	@Resource private IPriceContractService priceContractInfoService;
	@Resource private IPriceDispatchDao iPriceDispatchDao;
	@Resource private IPriceDispatchTemplateService priceDispatchTemplateService;
	@Resource  private IGenericTemplateService genericTemplateService;
	@Resource private IPriceExtraQuotationService priceExtraQuotationService;
	@Resource
	private IBmsErrorLogInfoService bmsErrorLogInfoService;
	
	private static final Logger logger = Logger.getLogger(StandardReqVoServiceImpl.class.getName());

	private static String customerId = "001";
	private static final String CUSTOMERID="customerid";
	private static final String SUBJECTID="subjectId";
	private static final String SUCC="succ";
	private static final String MSG="msg";
	private static final String NOSTANDARDRULE="标准规则未配置";
	@SuppressWarnings("unchecked")
	@Override
	public CalcuReqVo<T> getStorageReqVo(String subjectID) {
		CalcuReqVo reqVo = new CalcuReqVo();
		Map<String,Object> map=new HashMap<String,Object>();
		map.put(CUSTOMERID, customerId);
		map.put(SUBJECTID,subjectID);
		PriceGeneralQuotationEntity quoTemplete = null;			 //报价模板
		List<PriceStepQuotationEntity> priceStepList=null;		 //报价阶梯	
		List<PriceGeneralQuotationEntity> quoTempleteList= null; //报价模板集合
		BillRuleReceiveEntity ruleEntity=null;				     //规则对象
		try{
			quoTemplete=priceGeneralQuotationRepository.query(map);//查询标准报价模板
			if(quoTemplete == null){ //----------------标准报价模板不存在
				map.put(SUCC, "false");
				map.put(MSG, "标准报价模板不存在");
				reqVo.setParams(map);
				return reqVo;
			}
			if("PRICE_TYPE_STEP".equals(quoTemplete.getPriceType())){
				map.clear();
				map.put("quotationId", quoTemplete.getId());
				priceStepList = repository.queryPriceStepByQuatationId(map);
				if(priceStepList==null||priceStepList.isEmpty()){
					map.put(SUCC, "false");
					map.put(MSG, "标准报价阶梯不存在");
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
			map.put(CUSTOMERID,customerId);
			map.put(SUBJECTID, subjectID);
			ruleEntity=receiveRuleRepository.queryByCustomerId(map);
			if(ruleEntity == null){
				map.put(SUCC, "false");
				map.put(MSG, NOSTANDARDRULE);
				reqVo.setParams(map);
				return reqVo;
			}
			reqVo.setRuleNo(ruleEntity.getQuotationNo());
			reqVo.setRuleStr(ruleEntity.getRule());
			map.put(SUCC, "true");
			map.put(MSG, "标准报价和规则验证通过");
			reqVo.setParams(map);
		}
		catch(Exception ex){
			logger.error(ex);
			map.put(SUCC, "false");
			map.put(MSG, ex.getMessage());
			reqVo.setParams(map);
			//写入日志
			bmsErrorLogInfoService.insertLog(this.getClass().getSimpleName(),Thread.currentThread().getStackTrace()[1].getMethodName(), "", ex.toString());

			return reqVo;
		}
		return reqVo;
	}

	@SuppressWarnings("unchecked")
	@Override
	public CalcuReqVo<T> getMaterialReqVo(Map<String, Object> map) {
		CalcuReqVo reqVo = new CalcuReqVo();
		map.put(CUSTOMERID, customerId);
		List<PriceMaterialQuotationEntity> quoTempleteList= null; //报价模板集合
		BillRuleReceiveEntity ruleEntity=null;				     //规则对象
		try{
			quoTempleteList=priceMaterialQuotationRepository.queryStandardMaterial(map);
			if(quoTempleteList == null || quoTempleteList.isEmpty()){ //----------------标准报价模板不存在
				map.put(SUCC, "false");
				map.put(MSG, "标准耗材报价未配置");
				reqVo.setParams(map);
				return reqVo;
			}
			
			//------------------查询规则
			ruleEntity=receiveRuleRepository.queryByCustomerId(map);
			if(ruleEntity == null){
				map.put(SUCC, "false");
				map.put(MSG, NOSTANDARDRULE);
				reqVo.setParams(map);
				return reqVo;
			}
			reqVo.setQuoEntites(quoTempleteList);
			reqVo.setRuleNo(ruleEntity.getQuotationNo());
			reqVo.setRuleStr(ruleEntity.getRule());
			map.put(SUCC, "true");
			map.put(MSG, "标准报价和规则验证通过");
			reqVo.setParams(map);
		}
		catch(Exception ex){
			logger.error(ex);			
			map.put(SUCC, "false");
			map.put(MSG, ex.getMessage());
			reqVo.setParams(map);
			//写入日志
			bmsErrorLogInfoService.insertLog(this.getClass().getSimpleName(),Thread.currentThread().getStackTrace()[1].getMethodName(), "", ex.toString());

			return reqVo;
		}
		return reqVo;
	}

	@SuppressWarnings("unchecked")
	@Override
	public CalcuReqVo<T> getDispatchReceiveReqVo(String subjectId,String wareHouseId,String province) {
		CalcuReqVo reqVo = new CalcuReqVo();
		BillRuleReceiveEntity ruleEntity=null;				     //规则对象
		Map<String,Object> map=new HashMap<String,Object>();
		map.put(CUSTOMERID,customerId);
		map.put(SUBJECTID,subjectId);
		map.put("templateType", "S");
		try{
			PriceDispatchTemplateEntity template=priceDispatchTemplateService.query(map);
			if(template == null || StringUtils.isEmpty(template.getTemplateCode())){
				map.put(SUCC, "false");
				map.put(MSG, "标准配送报价模板未配置");
				reqVo.setParams(map);
				return reqVo;
			}
			
			map=new HashMap<String,Object>();
			map.put("templateId", template.getTemplateCode());
			map.put("wareHouseId", wareHouseId);
			map.put("province", province);
			List<PriceMainDispatchEntity> priceList = iPriceDispatchDao.queryAllByTemplateId(map);
			if(priceList==null || priceList.isEmpty()){
				map.put(SUCC, "false");
				map.put(MSG, "标准配送报价未配置");
				reqVo.setParams(map);
				return reqVo;
			}
			map.clear();
			map.put(CUSTOMERID,customerId);
			map.put(SUBJECTID, subjectId);
			ruleEntity=receiveRuleRepository.queryByCustomerId(map);
			if(ruleEntity == null){
				map.put(SUCC, "false");
				map.put(MSG, "标准配送规则未配置");
				reqVo.setParams(map);
				return reqVo;
			}
			
			reqVo.setQuoEntites(priceList);
			reqVo.setRuleNo(ruleEntity.getQuotationNo());
			reqVo.setRuleStr(ruleEntity.getRule());
			map.put(SUCC, "true");
			map.put(MSG, "标准报价和规则验证通过");
			reqVo.setParams(map);
		}
		catch(Exception ex){
			logger.error(ex);
			map.put(SUCC, "false");
			map.put(MSG, ex.getMessage());
			reqVo.setParams(map);
			//写入日志
			bmsErrorLogInfoService.insertLog(this.getClass().getSimpleName(),Thread.currentThread().getStackTrace()[1].getMethodName(), "", ex.toString());

			return reqVo;
		}
		return reqVo;
	}

	@Override
	public CalcuReqVo<T> getDispatchPayReqVo(String subjectId,String wareHouseId,String province) {
		return null;
	}

	@Override
	public CalcuReqVo<T> getOtherReqVo(Map<String,Object> reqMap) {
		CalcuReqVo reqVo = new CalcuReqVo();
		Map<String,Object> map=new HashMap<String,Object>();
		map.put(CUSTOMERID, customerId);
		GenericTemplateEntity quoTemplete = null;			 //报价模板
		List<PriceExtraQuotationEntity> priceStepList=null;		 //报价阶梯	
		BillRuleReceiveEntity ruleEntity=null;				     //规则对象
		try{
			quoTemplete=genericTemplateService.query(map);//查询标准报价模板
			if(quoTemplete == null){ //----------------标准报价模板不存在
				map.put(SUCC, "false");
				map.put(MSG, "标准报价模板不存在");
				reqVo.setParams(map);
				return reqVo;
			}
			
			map.clear();
			map.put("templateId", quoTemplete.getId());
			priceStepList = priceExtraQuotationService.queryPrice(map);
			if(priceStepList==null||priceStepList.isEmpty()){
				map.put(SUCC, "false");
				map.put(MSG, "标准报价阶梯不存在");
				reqVo.setParams(map);
				return reqVo;
			}else{
				reqVo.setQuoEntites(priceStepList);
			}
			
			//------------------查询规则
			map.clear();
			map.put(CUSTOMERID,customerId);
			map.put(SUBJECTID, reqMap.get(SUBJECTID));
			ruleEntity=receiveRuleRepository.queryByCustomerId(map);
			if(ruleEntity == null){
				map.put(SUCC, "false");
				map.put(MSG, NOSTANDARDRULE);
				reqVo.setParams(map);
				return reqVo;
			}
			reqVo.setRuleNo(ruleEntity.getQuotationNo());
			reqVo.setRuleStr(ruleEntity.getRule());
			map.put(SUCC, "true");
			map.put(MSG, "标准报价和规则验证通过");
			reqVo.setParams(map);
		}
		catch(Exception ex){
			logger.error(ex);
			map.put(SUCC, "false");
			map.put(MSG, ex.getMessage());
			reqVo.setParams(map);
			//写入日志
			bmsErrorLogInfoService.insertLog(this.getClass().getSimpleName(),Thread.currentThread().getStackTrace()[1].getMethodName(), "", ex.toString());

			return reqVo;
		}
		return reqVo;
	}

}
