package com.jiuyescm.bms.calcu.receive.storage.outstock;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.jiuyescm.bms.asyn.vo.BmsCalcuTaskVo;
import com.jiuyescm.bms.base.dictionary.entity.SystemCodeEntity;
import com.jiuyescm.bms.base.group.service.IBmsGroupSubjectService;
import com.jiuyescm.bms.biz.storage.entity.BizOutstockMasterEntity;
import com.jiuyescm.bms.calcu.base.CalcuTaskListener;
import com.jiuyescm.bms.calculate.api.IBmsCalcuService;
import com.jiuyescm.bms.calculate.vo.BmsFeesQtyVo;
import com.jiuyescm.bms.calculate.vo.CalcuContractVo;
import com.jiuyescm.bms.common.enumtype.CalculateState;
import com.jiuyescm.bms.drools.IFeesCalcuService;
import com.jiuyescm.bms.general.entity.FeesReceiveStorageEntity;
import com.jiuyescm.bms.general.service.IFeesReceiveStorageService;
import com.jiuyescm.bms.general.service.IPriceContractInfoService;
import com.jiuyescm.bms.general.service.IStorageQuoteFilterService;
import com.jiuyescm.bms.general.service.ISystemCodeService;
import com.jiuyescm.bms.general.service.SequenceService;
import com.jiuyescm.bms.quotation.contract.entity.PriceContractInfoEntity;
import com.jiuyescm.bms.quotation.contract.entity.PriceContractItemEntity;
import com.jiuyescm.bms.quotation.contract.repository.imp.IPriceContractItemRepository;
import com.jiuyescm.bms.quotation.storage.entity.PriceGeneralQuotationEntity;
import com.jiuyescm.bms.quotation.storage.entity.PriceStepQuotationEntity;
import com.jiuyescm.bms.quotation.storage.repository.IPriceGeneralQuotationRepository;
import com.jiuyescm.bms.quotation.storage.repository.IPriceStepQuotationRepository;
import com.jiuyescm.bms.receivable.storage.service.IBizOutstockMasterService;
import com.jiuyescm.bms.rule.receiveRule.repository.IReceiveRuleRepository;
import com.jiuyescm.cfm.common.JAppContext;
import com.jiuyescm.common.utils.DoubleUtil;
import com.jiuyescm.contract.quote.api.IContractQuoteInfoService;
import com.jiuyescm.contract.quote.vo.ContractBizTypeEnum;
import com.jiuyescm.contract.quote.vo.ContractQuoteQueryInfoVo;

public class OutstockCalcuBase extends CalcuTaskListener<BizOutstockMasterEntity,FeesReceiveStorageEntity>{

	private Logger logger = LoggerFactory.getLogger(OutstockCalcuBase.class);
	
	@Autowired private IContractQuoteInfoService contractQuoteInfoService;
	@Autowired private IBizOutstockMasterService bizOutstockMasterService;
	@Autowired private IPriceContractInfoService jobPriceContractInfoService;
	@Autowired private SequenceService sequenceService;
	@Autowired private IFeesReceiveStorageService feesReceiveStorageService;
	@Autowired private IPriceGeneralQuotationRepository priceGeneralQuotationRepository;
	@Autowired private IPriceStepQuotationRepository  repository;
	@Autowired private IReceiveRuleRepository receiveRuleRepository;
	@Autowired private IFeesCalcuService feesCalcuService;
	@Autowired private IPriceContractItemRepository priceContractItemRepository;
	@Autowired private IStorageQuoteFilterService storageQuoteFilterService;
	@Autowired private IBmsGroupSubjectService bmsGroupSubjectService;
	@Autowired private ISystemCodeService systemCodeService;
	@Autowired private IBmsCalcuService bmsCalcuService;
	
	//温度类型
	protected Map<String, String> temMap = null;

	@Override
	protected void initDict() {
		// TODO Auto-generated method stub
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("typeCode", "TEMPERATURE_TYPE");
		List<SystemCodeEntity> systemCodeList = systemCodeService.querySysCodes(map);
		temMap =new ConcurrentHashMap<String,String>();
		if(systemCodeList!=null && systemCodeList.size()>0){
			for(int i=0;i<systemCodeList.size();i++){
				temMap.put(systemCodeList.get(i).getCode(), systemCodeList.get(i).getCodeName());
			}
		}
	}
	
	
	@Override
	protected List<BizOutstockMasterEntity> queryBillList(Map<String, Object> map) {
		List<BizOutstockMasterEntity> bizList = bizOutstockMasterService.query(map);
		return bizList;
	}
	
	@Override
	protected void calcuForBms(BmsCalcuTaskVo vo,BizOutstockMasterEntity entity,FeesReceiveStorageEntity feeEntity){
		feeEntity.setSubjectCode(vo.getSubjectCode());
		PriceContractInfoEntity contractEntity = null;
		PriceGeneralQuotationEntity quoTemplete = null;
		
		//查询合同
		String customerId=entity.getCustomerid();
		String SubjectId = vo.getSubjectCode();
		Map<String,Object> map=new HashMap<String,Object>();
		map.clear();
		map.put("customerid", customerId);
		map.put("contractTypeCode", "CUSTOMER_CONTRACT");
		contractEntity = jobPriceContractInfoService.queryContractByCustomer(map);
		
		CalcuContractVo cVo = new CalcuContractVo();
		cVo.setContractAttr("BMS");
		if(contractEntity == null || StringUtils.isEmpty(contractEntity.getContractCode())){
			//打印合同归属
			printLog(vo.getTaskId(), "contractInfo", entity.getFeesNo(), vo.getSubjectName(), "bms合同缺失", cVo);
			feeEntity.setIsCalculated(CalculateState.Contract_Miss.getCode());
			feeEntity.setCalcuMsg("bms合同缺失");
			return;
		}
		cVo.setContractNo(contractEntity.getContractCode());
		
		//验证签约服务
		Map<String,Object> contractItems_map=new HashMap<String,Object>();
		contractItems_map.put("contractCode", contractEntity.getContractCode());
		contractItems_map.put("subjectId", SubjectId);
		List<PriceContractItemEntity> contractItems = priceContractItemRepository.query(contractItems_map);
		if(contractItems == null || contractItems.size() == 0 || StringUtils.isEmpty(contractItems.get(0).getTemplateId())) {
			printLog(vo.getTaskId(), "contractInfo", entity.getFeesNo(), vo.getSubjectName(), "未签约服务", cVo);
			feeEntity.setIsCalculated(CalculateState.Contract_Miss.getCode());
			feeEntity.setCalcuMsg("未签约服务");
			return;
		}
		
		/*验证报价 报价*/
		map.clear();
		map.put("subjectId",SubjectId);
		map.put("quotationNo", contractItems.get(0).getTemplateId());
		quoTemplete = priceGeneralQuotationRepository.query(map);
		if(quoTemplete==null){
			printLog(vo.getTaskId(), "contractInfo", entity.getFeesNo(), vo.getSubjectName(), "报价模板缺失", cVo);
			feeEntity.setIsCalculated(CalculateState.Quote_Miss.getCode());
			feeEntity.setCalcuMsg("报价模板缺失");
			return;
		}
		
		printLog(vo.getTaskId(), "contractInfo", entity.getFeesNo(), vo.getSubjectName(), "合同存在", cVo);

		
		String priceType = quoTemplete.getPriceType();
		String unit = quoTemplete.getFeeUnitCode();//计费单位 
		
		double weight = 0d;
		if ((double)feeEntity.getWeight()/1000 < 1) {
			weight = 1d;
		}else {
			weight = (double)feeEntity.getWeight()/1000;
		}
		
		//计算方法
		double amount=0d;
		switch(priceType){
		case "PRICE_TYPE_NORMAL"://一口价				
			//如果计费单位是 单 -> 费用 = 模板价格
            //如果计费单位是 件 -> 费用 = 商品件数*模板价格
            //如果计费单位是 sku数 -> 费用 = 商家sku数*模板价格
			if("BILL".equals(unit)){//按单
				amount=quoTemplete.getUnitPrice();				
			}else if("ITEMS".equals(unit)){//按件				
				amount=feeEntity.getQuantity()*quoTemplete.getUnitPrice();
			}else if("SKU".equals(unit)){//按sku
				amount=feeEntity.getVarieties()*quoTemplete.getUnitPrice();
			}else if("CARTON".equals(unit)){
				amount=feeEntity.getBox()*quoTemplete.getUnitPrice();
			}else if("KILOGRAM".equals(unit)){
				amount=feeEntity.getWeight()*quoTemplete.getUnitPrice();
			}else if("TONS".equals(unit)){
				amount=weight*quoTemplete.getUnitPrice();
			}
			feeEntity.setUnitPrice(quoTemplete.getUnitPrice());
			feeEntity.setParam3(quoTemplete.getId()+"");
			break;
		case "PRICE_TYPE_STEP"://阶梯价			
			//=====================================查询子报价==================================
			List<PriceStepQuotationEntity> list=new ArrayList<PriceStepQuotationEntity>();
			Map<String,Object> pricemap=new HashMap<String,Object>();
			//寻找阶梯报价
			pricemap.clear();
			pricemap.put("quotationId", quoTemplete.getId());
			//根据报价单位判断
			if("BILL".equals(quoTemplete.getFeeUnitCode())){//按单
				pricemap.put("num", "1");
			}else if("ITEMS".equals(quoTemplete.getFeeUnitCode())){//按件
				pricemap.put("num", entity.getTotalQuantity());
			}else if("SKU".equals(quoTemplete.getFeeUnitCode())){//按sku
				pricemap.put("num", entity.getTotalVarieties());
			}
			//查询出的所有子报价
			list=repository.queryPriceStepByQuatationId(pricemap);					
			if(list==null || list.size() == 0){
				entity.setIsCalculated(CalculateState.Quote_Miss.getCode());
				feeEntity.setIsCalculated(CalculateState.Quote_Miss.getCode());
				entity.setRemark(entity.getRemark()+"阶梯报价未配置;");
				feeEntity.setCalcuMsg("阶梯报价未配置");
				//feesList.add(storageFeeEntity);
				return;
			}
			
			//封装数据的仓库和温度
			map.clear();
			map.put("warehouse_code", entity.getWarehouseCode());
			map.put("temperature_code", entity.getTemperatureTypeCode());
			
			PriceStepQuotationEntity stepQuoEntity=storageQuoteFilterService.quoteFilter(list, map);
			
			if(stepQuoEntity==null){
				entity.setIsCalculated(CalculateState.Quote_Miss.getCode());
				feeEntity.setIsCalculated(CalculateState.Quote_Miss.getCode());
				entity.setRemark(entity.getRemark()+"阶梯报价未配置;");
				feeEntity.setCalcuMsg("阶梯报价未配置");
				return;
			}
			
			// 如果计费单位是 单 ->费用 = 单价 （根据仓库和温度和上下限帅选出唯一报价）
            // 如果计费单位是 件或者sku数 
            //   如果单价为空或为0，-> 费用 = 首价+（商品件数-首量）/续量 * 续价
            //   如果单价>0 ， 费用=单价*数量
			if("BILL".equals(unit)){//按单
				amount=stepQuoEntity.getUnitPrice();
			}else if("ITEMS".equals(unit)){//按件	
				if(!DoubleUtil.isBlank(stepQuoEntity.getUnitPrice())){
					amount=feeEntity.getQuantity()*stepQuoEntity.getUnitPrice();
					feeEntity.setUnitPrice(stepQuoEntity.getUnitPrice());
				}else{
					amount=stepQuoEntity.getFirstNum()<feeEntity.getQuantity()?stepQuoEntity.getFirstPrice()+(feeEntity.getQuantity()-stepQuoEntity.getFirstNum())/stepQuoEntity.getContinuedItem()*stepQuoEntity.getContinuedPrice():stepQuoEntity.getFirstPrice();
				}
			}else if("SKU".equals(unit)){//按sku
				if(!DoubleUtil.isBlank(stepQuoEntity.getUnitPrice())){
					amount=feeEntity.getVarieties()*stepQuoEntity.getUnitPrice();
					feeEntity.setUnitPrice(stepQuoEntity.getUnitPrice());
				}else{
					amount=stepQuoEntity.getFirstNum()<feeEntity.getVarieties()?stepQuoEntity.getFirstPrice()+(feeEntity.getVarieties()-stepQuoEntity.getFirstNum())/stepQuoEntity.getContinuedItem()*stepQuoEntity.getContinuedPrice():stepQuoEntity.getFirstPrice();
				}
			}else if("CARTON".equals(unit)){//按箱
				if(!DoubleUtil.isBlank(stepQuoEntity.getUnitPrice())){
					amount=feeEntity.getBox()*stepQuoEntity.getUnitPrice();
					feeEntity.setUnitPrice(stepQuoEntity.getUnitPrice());
				}else{
					amount=stepQuoEntity.getFirstNum()<feeEntity.getBox()?stepQuoEntity.getFirstPrice()+(feeEntity.getBox()-stepQuoEntity.getFirstNum())/stepQuoEntity.getContinuedItem()*stepQuoEntity.getContinuedPrice():stepQuoEntity.getFirstPrice();
				}
			}else if("KILOGRAM".equals(unit)){//按千克
				if(!DoubleUtil.isBlank(stepQuoEntity.getUnitPrice())){
					amount=feeEntity.getWeight()*stepQuoEntity.getUnitPrice();
					feeEntity.setUnitPrice(stepQuoEntity.getUnitPrice());
				}else{
					amount=stepQuoEntity.getFirstNum()<feeEntity.getWeight()?stepQuoEntity.getFirstPrice()+(feeEntity.getWeight()-stepQuoEntity.getFirstNum())/stepQuoEntity.getContinuedItem()*stepQuoEntity.getContinuedPrice():stepQuoEntity.getFirstPrice();
				}
			}else if("TONS".equals(unit)){//按吨
				if(!DoubleUtil.isBlank(stepQuoEntity.getUnitPrice())){
					amount=weight*quoTemplete.getUnitPrice();
					feeEntity.setUnitPrice(stepQuoEntity.getUnitPrice());
				}else{
					amount=(double)(stepQuoEntity.getFirstNum()<weight?stepQuoEntity.getFirstPrice()+(weight-stepQuoEntity.getFirstNum())/stepQuoEntity.getContinuedItem()*stepQuoEntity.getContinuedPrice():stepQuoEntity.getFirstPrice());
				}					
			}			
			//判断封顶价
			if(!DoubleUtil.isBlank(stepQuoEntity.getCapPrice())){
				if(stepQuoEntity.getCapPrice()<amount){
					amount=stepQuoEntity.getCapPrice();
				}
			}
			
			feeEntity.setParam3(stepQuoEntity.getId()+"");
			break;
		default:
			break;
		}
		
		feeEntity.setUnit(unit);
		feeEntity.setCost(BigDecimal.valueOf(amount));
		feeEntity.setParam4(priceType);
		feeEntity.setBizType(entity.getextattr1());//用于判断是否是遗漏数据
		entity.setRemark(entity.getRemark()+"计算成功;");
		feeEntity.setCalcuMsg("计算成功");
		entity.setIsCalculated(CalculateState.Finish.getCode());
		feeEntity.setIsCalculated(CalculateState.Finish.getCode());
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
	protected FeesReceiveStorageEntity initFeeEntity(BmsCalcuTaskVo vo,BizOutstockMasterEntity outstock) {
		//打印业务数据日志
		FeesReceiveStorageEntity storageFeeEntity = new FeesReceiveStorageEntity();
		//塞品种数
		Double varieties=DoubleUtil.isBlank(outstock.getResizeVarieties())?outstock.getTotalVarieties():outstock.getResizeVarieties();
		if(!DoubleUtil.isBlank(varieties)){
			storageFeeEntity.setVarieties(varieties.intValue());
		}
		
		//塞件数
		Double charge_qty = DoubleUtil.isBlank(outstock.getResizeNum())?outstock.getTotalQuantity():outstock.getResizeNum();
		storageFeeEntity.setQuantity(charge_qty);
		//塞重量
		
		Double charge_weight = DoubleUtil.isBlank(outstock.getResizeWeight())?outstock.getTotalWeight():outstock.getResizeWeight();
		storageFeeEntity.setWeight(charge_weight);
		//塞箱数
		storageFeeEntity.setBox(isBlank(outstock.getAdjustBoxnum())?outstock.getBoxnum():outstock.getAdjustBoxnum());
		
		storageFeeEntity.setProductType("");							//商品类型		
		storageFeeEntity.setStatus("0");								//状态
		storageFeeEntity.setOperateTime(outstock.getCreateTime());
		storageFeeEntity.setCostType("FEE_TYPE_GENEARL");
		storageFeeEntity.setUnitPrice(0d);
		storageFeeEntity.setCost(new BigDecimal(0));	//入仓金额
		storageFeeEntity.setBizId(String.valueOf(outstock.getId()));//业务数据主键
		storageFeeEntity.setFeesNo(outstock.getFeesNo());
		
		storageFeeEntity.setCostType("FEE_TYPE_GENEARL");
		storageFeeEntity.setUnitPrice(0d);
		storageFeeEntity.setCost(new BigDecimal(0));	
		storageFeeEntity.setDelFlag("0");
		storageFeeEntity.setFeesNo(outstock.getFeesNo());
		storageFeeEntity.setCalculateTime(JAppContext.currentTimestamp());
		
		if(StringUtils.isEmpty(outstock.getTemperatureTypeCode())){
			outstock.setTemperatureTypeCode("LD");
			storageFeeEntity.setTempretureType("LD");
		}
		else{
			storageFeeEntity.setTempretureType(outstock.getTemperatureTypeCode());
			
			outstock.setTemperatureTypeName(temMap.get(outstock.getTemperatureTypeCode()));		
		}
		if(StringUtils.isEmpty(outstock.getTemperatureTypeName())){
			outstock.setTemperatureTypeName("冷冻");
		}
		return storageFeeEntity;
	}



	@Override
	protected boolean isNoExe(BizOutstockMasterEntity t,FeesReceiveStorageEntity f,Map<String, Object> errorMap) {
		/*if(!"succ".equals(errorMap.get("success"))){
			f.setIsCalculated(errorMap.get("is_calculated").toString());
			f.setCalcuMsg(errorMap.get("msg").toString());
			return true;
		}*/
		return false;
	}

	@Override
	protected ContractQuoteQueryInfoVo getCtConditon(BmsCalcuTaskVo vo,BizOutstockMasterEntity entity) {
		ContractQuoteQueryInfoVo queryVo = new ContractQuoteQueryInfoVo();
		queryVo.setCustomerId(entity.getCustomerid());
		queryVo.setBizTypeCode(ContractBizTypeEnum.STORAGE.getCode());
		queryVo.setSubjectCode(vo.getSubjectCode());
		queryVo.setCurrentTime(entity.getCreateTime());
		queryVo.setWarehouseCode(entity.getWarehouseCode());
		return queryVo;

	}

	/**
	 * 判断Integer类型数据是否为空值
	 * @param d
	 * @return
	 * null -> true
	 * 0 -> true   0.0 -> true  -0.0 -> true
	 * 其他返回 false
	 */
	public boolean isBlank(Integer d){
		
		if(d == null){
			return true;
		}
		if(d == 0){
			return true;
		}
		return false;
	}


}
