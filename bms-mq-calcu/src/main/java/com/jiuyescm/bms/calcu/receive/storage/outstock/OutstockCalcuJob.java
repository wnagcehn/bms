package com.jiuyescm.bms.calcu.receive.storage.outstock;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;

import com.jiuyescm.bms.asyn.service.IBmsCalcuTaskService;
import com.jiuyescm.bms.asyn.vo.BmsCalcuTaskVo;
import com.jiuyescm.bms.base.dictionary.entity.SystemCodeEntity;
import com.jiuyescm.bms.base.group.service.IBmsGroupSubjectService;
import com.jiuyescm.bms.biz.storage.entity.BizOutstockMasterEntity;
import com.jiuyescm.bms.calcu.base.ICalcuService;
import com.jiuyescm.bms.calcu.receive.BmsContractBase;
import com.jiuyescm.bms.calcu.receive.CommonService;
import com.jiuyescm.bms.calcu.receive.ContractCalcuService;
import com.jiuyescm.bms.calculate.api.IBmsCalcuService;
import com.jiuyescm.bms.calculate.vo.BmsFeesQtyVo;
import com.jiuyescm.bms.common.enumtype.CalculateState;
import com.jiuyescm.bms.drools.IFeesCalcuService;
import com.jiuyescm.bms.general.entity.FeesReceiveStorageEntity;
import com.jiuyescm.bms.general.service.IFeesReceiveStorageService;
import com.jiuyescm.bms.general.service.IPriceContractInfoService;
import com.jiuyescm.bms.general.service.IStorageQuoteFilterService;
import com.jiuyescm.bms.general.service.ISystemCodeService;
import com.jiuyescm.bms.general.service.SequenceService;
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

@Service("outstockCalcuJob")
@Scope("prototype")
public class OutstockCalcuJob extends BmsContractBase implements ICalcuService<BizOutstockMasterEntity,FeesReceiveStorageEntity> {

	private Logger logger = LoggerFactory.getLogger(OutstockCalcuJob.class);
	
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
	@Autowired private ContractCalcuService contractCalcuService;
	@Autowired private CommonService commonService;
	@Autowired IBmsCalcuTaskService bmsCalcuTaskService;
	
	private PriceGeneralQuotationEntity quoTemplete = null;
	private Map<String, Object> errorMap = null;
    Map<String, String> temMap = null;
	
	public void process(BmsCalcuTaskVo taskVo, String contractAttr) {
		super.process(taskVo, contractAttr);
		serviceSubjectCode = subjectCode;
		getQuoTemplete();
		errorMap = new HashMap<String, Object>();
		initConf();
	}
	
	@Override
	public void getQuoTemplete(){
		Map<String, Object> map = new HashMap<>();
		if(quoTempleteCode!=null){
			map.put("subjectId",serviceSubjectCode);
			map.put("quotationNo", quoTempleteCode);
			quoTemplete = priceGeneralQuotationRepository.query(map);
		}
	}
	
	@Override
	public void initConf(){
	    //初始化温度类型
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("typeCode", "TEMPERATURE_TYPE");
        List<SystemCodeEntity> systemCodeList = systemCodeService.querySysCodes(map);
        temMap =new HashMap<String,String>();
        if(systemCodeList!=null && systemCodeList.size()>0){
            for(int i=0;i<systemCodeList.size();i++){
                temMap.put(systemCodeList.get(i).getCode(), systemCodeList.get(i).getCodeName());
            }
        }
	}
	
	@Override
	public void calcu(Map<String, Object> map){

		List<BizOutstockMasterEntity> bizList = bizOutstockMasterService.query(map);
		List<FeesReceiveStorageEntity> fees = new ArrayList<>();
		if(bizList == null || bizList.size() == 0){
			commonService.taskCountReport(taskVo, "STORAGE");
			return;
		}
		logger.info("taskId={} 查询行数【{}】",taskVo.getTaskId(),bizList.size());
		for (BizOutstockMasterEntity entity : bizList) {
			FeesReceiveStorageEntity fee = initFee(entity);
			fees.add(fee);
			try {
				if(isNoExe(entity, fee)){
					continue; //如果不计算费用,后面的逻辑不在执行，只是在最后更新数据库状态
				}
		
				if("BMS".equals(contractAttr)){
					calcuForBms(entity,fee);
				}
				else {
					calcuForContract(entity,fee);
				}
			} catch (Exception e) {
				// TODO: handle exception
				fee.setIsCalculated(CalculateState.Sys_Error.getCode());
				fee.setCalcuMsg("系统异常");
				logger.error("计算异常",e);
			}
		}
		updateBatch(bizList,fees);
		calceCount += bizList.size();
		//更新任务计算各字段
		updateTask(taskVo,calceCount);	
		int taskRate = (int)Math.floor((calceCount*10)/unCalcuCount);
		try {
			if(unCalcuCount!=0){
				bmsCalcuTaskService.updateRate(taskVo.getTaskId(), taskRate);
			}
		} catch (Exception e) {
			logger.error("更新任务进度异常",e);
		}
		calcu(map);
	}
	
	private void updateTask(BmsCalcuTaskVo taskVo,int calcuCount){
		try {
			BmsFeesQtyVo feesQtyVo = bmsCalcuService.queryFeesQtyForStoOutstock(taskVo.getCustomerId(), taskVo.getSubjectCode(), taskVo.getCreMonth());
			taskVo.setUncalcuCount(feesQtyVo.getUncalcuCount()==null?0:feesQtyVo.getUncalcuCount());//本次待计算的费用数
			taskVo.setCalcuCount(calcuCount);
			taskVo.setBeginCount(feesQtyVo.getBeginCount()==null?0:feesQtyVo.getBeginCount());//未计算费用总数
			taskVo.setFinishCount(feesQtyVo.getFinishCount()==null?0:feesQtyVo.getFinishCount());//计算成功总数
			taskVo.setSysErrorCount(feesQtyVo.getSysErrorCount()==null?0:feesQtyVo.getSysErrorCount());//系统错误用总数
			taskVo.setContractMissCount(feesQtyVo.getContractMissCount()==null?0:feesQtyVo.getContractMissCount());//合同缺失总数
			taskVo.setQuoteMissCount(feesQtyVo.getQuoteMissCount()==null?0:feesQtyVo.getQuoteMissCount());//报价缺失总数
			taskVo.setNoExeCount(feesQtyVo.getNoExeCount()==null?0:feesQtyVo.getNoExeCount());//不计算费用总数
			taskVo.setCalcuStatus(feesQtyVo.getCalcuStatus());
			bmsCalcuTaskService.update(taskVo);
		} catch (Exception e) {
			logger.error("更新任务统计信息异常",e);
		}
	}
	
	@Override
	public FeesReceiveStorageEntity initFee(BizOutstockMasterEntity entity){
		//打印业务数据日志
		FeesReceiveStorageEntity fee = new FeesReceiveStorageEntity();
		fee.setVarieties(0);
		fee.setQuantity(0d);
		fee.setWeight(0d);
		fee.setBox(0);
		//塞品种数
		Double varieties=DoubleUtil.isBlank(entity.getResizeVarieties())?entity.getTotalVarieties():entity.getResizeVarieties();
		if(!DoubleUtil.isBlank(varieties)){
			fee.setVarieties(varieties.intValue());
		}
		//塞件数
		Double charge_qty = DoubleUtil.isBlank(entity.getResizeNum())?entity.getTotalQuantity():entity.getResizeNum();
		if(!DoubleUtil.isBlank(charge_qty)){
			fee.setQuantity(charge_qty);
		}
		//塞重量		
		Double charge_weight = DoubleUtil.isBlank(entity.getResizeWeight())?entity.getTotalWeight():entity.getResizeWeight();
		if(!DoubleUtil.isBlank(charge_weight)){
			fee.setWeight(charge_weight);
		}
		//塞箱数
		Integer box=DoubleUtil.isBlank(entity.getAdjustBoxnum())?entity.getBoxnum():entity.getAdjustBoxnum();
		if(!DoubleUtil.isBlank(box)){
			fee.setBox(box);
		}
		fee.setStatus("0");								//状态
		fee.setCostType("FEE_TYPE_GENEARL");
		fee.setUnitPrice(0d);
		fee.setCost(new BigDecimal(0));	//入仓金额
		fee.setFeesNo(entity.getFeesNo());
		fee.setSubjectCode(subjectCode);
		fee.setDelFlag("0");
		fee.setCalculateTime(JAppContext.currentTimestamp());
		
		if(StringUtils.isEmpty(entity.getTemperatureTypeCode())){
		    entity.setTemperatureTypeCode("LD");
            fee.setTempretureType("LD");
        }
        else{
            fee.setTempretureType(entity.getTemperatureTypeCode());
            entity.setTemperatureTypeName(temMap.get(entity.getTemperatureTypeCode()));
        }
        if(StringUtils.isEmpty(entity.getTemperatureTypeName())){
            entity.setTemperatureTypeName("冷冻");
        }
		return fee;	
	}
	
	@Override
	public boolean isNoExe(BizOutstockMasterEntity entity,FeesReceiveStorageEntity fee){
		return false;
	}
	
	@Override
	public void calcuForBms(BizOutstockMasterEntity entity,FeesReceiveStorageEntity fee){
		//合同校验
		if(contractInfo == null){
			fee.setIsCalculated(CalculateState.Contract_Miss.getCode());
			fee.setCalcuMsg("bms合同缺失");
			return;
		}
		
		if("fail".equals(quoTempleteCode)){
			fee.setIsCalculated(CalculateState.Quote_Miss.getCode());
			fee.setCalcuMsg("未签约服务");
			return;
		}
		
		if(quoTemplete == null){
			fee.setIsCalculated(CalculateState.Quote_Miss.getCode());
			fee.setCalcuMsg("报价模板缺失");
			return;
		}
		
		String priceType = quoTemplete.getPriceType();
		String unit = quoTemplete.getFeeUnitCode();//计费单位 
		
		double weight = 0d;
		if ((double)fee.getWeight()/1000 < 1) {
			weight = 1d;
		}else {
			weight = (double)fee.getWeight()/1000;
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
				amount=fee.getQuantity()*quoTemplete.getUnitPrice();
			}else if("SKU".equals(unit)){//按sku
				amount=fee.getVarieties()*quoTemplete.getUnitPrice();
			}else if("CARTON".equals(unit)){
				amount=fee.getBox()*quoTemplete.getUnitPrice();
			}else if("KILOGRAM".equals(unit)){
				amount=fee.getWeight()*quoTemplete.getUnitPrice();
			}else if("TONS".equals(unit)){
				amount=weight*quoTemplete.getUnitPrice();
			}
			fee.setUnitPrice(quoTemplete.getUnitPrice());
			fee.setParam3(quoTemplete.getId()+"");
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
				fee.setIsCalculated(CalculateState.Quote_Miss.getCode());
				entity.setRemark(entity.getRemark()+"阶梯报价未配置;");
				fee.setCalcuMsg("阶梯报价未配置");
				//feesList.add(storageFeeEntity);
				return;
			}
			
			//封装数据的仓库和温度
			Map<String, Object> map = new HashMap<>();
			map.put("warehouse_code", entity.getWarehouseCode());
			map.put("temperature_code", entity.getTemperatureTypeCode());
			
			PriceStepQuotationEntity stepQuoEntity=storageQuoteFilterService.quoteFilter(list, map);
			
			if(stepQuoEntity==null){
				entity.setIsCalculated(CalculateState.Quote_Miss.getCode());
				fee.setIsCalculated(CalculateState.Quote_Miss.getCode());
				entity.setRemark(entity.getRemark()+"阶梯报价未配置;");
				fee.setCalcuMsg("阶梯报价未配置");
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
					amount=fee.getQuantity()*stepQuoEntity.getUnitPrice();
					fee.setUnitPrice(stepQuoEntity.getUnitPrice());
				}else{
					amount=stepQuoEntity.getFirstNum()<fee.getQuantity()?stepQuoEntity.getFirstPrice()+(fee.getQuantity()-stepQuoEntity.getFirstNum())/stepQuoEntity.getContinuedItem()*stepQuoEntity.getContinuedPrice():stepQuoEntity.getFirstPrice();
				}
			}else if("SKU".equals(unit)){//按sku
				if(!DoubleUtil.isBlank(stepQuoEntity.getUnitPrice())){
					amount=fee.getVarieties()*stepQuoEntity.getUnitPrice();
					fee.setUnitPrice(stepQuoEntity.getUnitPrice());
				}else{
					amount=stepQuoEntity.getFirstNum()<fee.getVarieties()?stepQuoEntity.getFirstPrice()+(fee.getVarieties()-stepQuoEntity.getFirstNum())/stepQuoEntity.getContinuedItem()*stepQuoEntity.getContinuedPrice():stepQuoEntity.getFirstPrice();
				}
			}else if("CARTON".equals(unit)){//按箱
				if(!DoubleUtil.isBlank(stepQuoEntity.getUnitPrice())){
					amount=fee.getBox()*stepQuoEntity.getUnitPrice();
					fee.setUnitPrice(stepQuoEntity.getUnitPrice());
				}else{
					amount=stepQuoEntity.getFirstNum()<fee.getBox()?stepQuoEntity.getFirstPrice()+(fee.getBox()-stepQuoEntity.getFirstNum())/stepQuoEntity.getContinuedItem()*stepQuoEntity.getContinuedPrice():stepQuoEntity.getFirstPrice();
				}
			}else if("KILOGRAM".equals(unit)){//按千克
				if(!DoubleUtil.isBlank(stepQuoEntity.getUnitPrice())){
					amount=fee.getWeight()*stepQuoEntity.getUnitPrice();
					fee.setUnitPrice(stepQuoEntity.getUnitPrice());
				}else{
					amount=stepQuoEntity.getFirstNum()<fee.getWeight()?stepQuoEntity.getFirstPrice()+(fee.getWeight()-stepQuoEntity.getFirstNum())/stepQuoEntity.getContinuedItem()*stepQuoEntity.getContinuedPrice():stepQuoEntity.getFirstPrice();
				}
			}else if("TONS".equals(unit)){//按吨
				if(!DoubleUtil.isBlank(stepQuoEntity.getUnitPrice())){
					amount=weight*quoTemplete.getUnitPrice();
					fee.setUnitPrice(stepQuoEntity.getUnitPrice());
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
			
			fee.setParam3(stepQuoEntity.getId()+"");
			break;
		default:
			break;
		}
		
		fee.setUnit(unit);
		fee.setCost(BigDecimal.valueOf(amount));
		fee.setParam4(priceType);
		fee.setBizType(entity.getextattr1());//用于判断是否是遗漏数据
		entity.setRemark(entity.getRemark()+"计算成功;");
		fee.setCalcuMsg("计算成功");
		fee.setIsCalculated(CalculateState.Finish.getCode());
	}
	
	@Override
	public void calcuForContract(BizOutstockMasterEntity entity,FeesReceiveStorageEntity fee){
		ContractQuoteQueryInfoVo queryVo = getCtConditon(entity);
		contractCalcuService.calcuForContract(entity, fee, taskVo, errorMap, queryVo,cbiVo,fee.getFeesNo());
		if("succ".equals(errorMap.get("success").toString())){
			if(fee.getCost().compareTo(BigDecimal.ZERO) == 1){
				fee.setIsCalculated(CalculateState.Finish.getCode());
				logger.info("计算成功，费用【{}】",fee.getCost());
				fee.setCalcuMsg("计算成功");
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
	public ContractQuoteQueryInfoVo getCtConditon(BizOutstockMasterEntity entity) {
		ContractQuoteQueryInfoVo queryVo = new ContractQuoteQueryInfoVo();
		queryVo.setCustomerId(entity.getCustomerid());
		queryVo.setBizTypeCode(ContractBizTypeEnum.STORAGE.getCode());
		queryVo.setSubjectCode(subjectCode);
		queryVo.setCurrentTime(entity.getCreateTime());
		queryVo.setWarehouseCode(entity.getWarehouseCode());
		return queryVo;

	}
	
	@Override
	public void updateBatch(List<BizOutstockMasterEntity> bizList,List<FeesReceiveStorageEntity> feeList) {
		StopWatch sw = new StopWatch();
		sw.start();
		feesReceiveStorageService.updateFee(feeList);
		sw.stop();
		logger.info("taskId={} 更新仓储费用行数【{}】 耗时【{}】",taskVo.getTaskId(),feeList.size(),sw.getLastTaskTimeMillis());
	}
	
}
