package com.jiuyescm.bms.calcu.receive.storage.pallet;

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
import org.springframework.util.StopWatch;

import com.jiuyescm.bms.asyn.service.IBmsCalcuTaskService;
import com.jiuyescm.bms.asyn.vo.BmsCalcuTaskVo;
import com.jiuyescm.bms.base.group.service.IBmsGroupCustomerService;
import com.jiuyescm.bms.base.group.service.IBmsGroupService;
import com.jiuyescm.bms.base.group.service.IBmsGroupSubjectService;
import com.jiuyescm.bms.base.group.vo.BmsGroupCustomerVo;
import com.jiuyescm.bms.base.group.vo.BmsGroupVo;
import com.jiuyescm.bms.calcu.base.ICalcuService;
import com.jiuyescm.bms.calcu.receive.BmsContractBase;
import com.jiuyescm.bms.calcu.receive.CommonService;
import com.jiuyescm.bms.calcu.receive.ContractCalcuService;
import com.jiuyescm.bms.calculate.api.IBmsCalcuService;
import com.jiuyescm.bms.calculate.vo.BmsFeesQtyVo;
import com.jiuyescm.bms.calculate.vo.CalcuContractVo;
import com.jiuyescm.bms.common.enumtype.CalculateState;
import com.jiuyescm.bms.common.enumtype.TemplateTypeEnum;
import com.jiuyescm.bms.drools.IFeesCalcuService;
import com.jiuyescm.bms.general.entity.BizPalletInfoEntity;
import com.jiuyescm.bms.general.entity.FeesReceiveStorageEntity;
import com.jiuyescm.bms.general.service.IBizPalletInfoRepository;
import com.jiuyescm.bms.general.service.IFeesReceiveStorageService;
import com.jiuyescm.bms.general.service.IPriceContractInfoService;
import com.jiuyescm.bms.general.service.IStorageQuoteFilterService;
import com.jiuyescm.bms.general.service.ISystemCodeService;
import com.jiuyescm.bms.quotation.contract.repository.imp.IPriceContractItemRepository;
import com.jiuyescm.bms.quotation.storage.entity.PriceGeneralQuotationEntity;
import com.jiuyescm.bms.quotation.storage.entity.PriceStepQuotationEntity;
import com.jiuyescm.bms.quotation.storage.repository.IPriceGeneralQuotationRepository;
import com.jiuyescm.bms.quotation.storage.repository.IPriceStepQuotationRepository;
import com.jiuyescm.bms.rule.receiveRule.repository.IReceiveRuleRepository;
import com.jiuyescm.cfm.common.JAppContext;
import com.jiuyescm.common.utils.DoubleUtil;
import com.jiuyescm.contract.quote.api.IContractQuoteInfoService;
import com.jiuyescm.contract.quote.vo.ContractBizTypeEnum;
import com.jiuyescm.contract.quote.vo.ContractQuoteQueryInfoVo;

@Component("palletCalcuJob")
@Scope("prototype")
public class PalletCalcuJob extends BmsContractBase implements ICalcuService<BizPalletInfoEntity,FeesReceiveStorageEntity> {

	private Logger logger = LoggerFactory.getLogger(PalletCalcuJob.class);
	
	@Autowired private IPriceContractInfoService jobPriceContractInfoService;
	@Autowired private IFeesReceiveStorageService feesReceiveStorageService;
	@Autowired private IPriceGeneralQuotationRepository priceGeneralQuotationRepository;
	@Autowired private IPriceStepQuotationRepository  repository;
	@Autowired private IReceiveRuleRepository receiveRuleRepository;
	@Autowired private IFeesCalcuService feesCalcuService;
	@Autowired private IPriceContractItemRepository priceContractItemRepository;
	@Autowired private ISystemCodeService systemCodeService;
	@Autowired private IBmsGroupService bmsGroupService;
	@Autowired private IBmsGroupCustomerService bmsGroupCustomerService;
	@Autowired private IStorageQuoteFilterService storageQuoteFilterService;
	@Autowired private IContractQuoteInfoService contractQuoteInfoService;
	@Autowired private IBmsGroupSubjectService bmsGroupSubjectService;
	@Autowired private IBizPalletInfoRepository bizPalletInfoService;
	@Autowired private ContractCalcuService contractCalcuService;
	@Autowired private CommonService commonService;
	@Autowired IBmsCalcuTaskService bmsCalcuTaskService;
	@Autowired private IBmsCalcuService bmsCalcuService;

	
	private PriceGeneralQuotationEntity quoTemplete = null;
	private Map<String, Object> errorMap = null;
	
	List<String> cusList=null;
	List<String> cusNames = null;

	public void process(BmsCalcuTaskVo taskVo,String contractAttr){
		super.process(taskVo, contractAttr);
		serviceSubjectCode = subjectCode;
		getQuoTemplete();
		errorMap = new HashMap<String, Object>();
		initConf();
	}
	
	@Override
	public void getQuoTemplete() {
		/*Map<String, Object> map = new HashMap<>();
		if(quoTempleteCode!=null){
			map.put("subjectId",serviceSubjectCode);
			map.put("quotationNo", quoTempleteCode);
			quoTemplete = priceGeneralQuotationRepository.query(map);
		}*/
	}

	@Override
	public void calcu(Map<String, Object> map) {
		
		List<BizPalletInfoEntity> bizList = bizPalletInfoService.querybizPallet(map);
		List<FeesReceiveStorageEntity> fees = new ArrayList<>();
		if(bizList == null || bizList.size() == 0){
			commonService.taskCountReport(taskVo, "STORAGE");
			return;
		}
		logger.info("taskId={} 查询行数【{}】",taskVo.getTaskId(),bizList.size());
		for (BizPalletInfoEntity entity : bizList) {
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
		int taskRate = (int)Math.floor((calceCount*100)/unCalcuCount);
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
			BmsFeesQtyVo feesQtyVo = bmsCalcuService.queryFeesQtyForStoPallet(taskVo.getCustomerId(), taskVo.getSubjectCode(), taskVo.getCreMonth());
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
	public void initConf() {
		//《使用导入商品托数的商家》
		Map<String, Object> cond= new HashMap<String, Object>();
		cond.put("groupCode", "Product_Pallet");
		cond.put("bizType", "group_customer");
		BmsGroupVo bmsGroup=bmsGroupService.queryOne(cond);
		if(bmsGroup!=null){
			cusNames = new ArrayList<String>();
			List<BmsGroupCustomerVo> cusList = null;
			try {
				cusList = bmsGroupCustomerService.queryAllByGroupId(bmsGroup.getId());
			} catch (Exception e) {
				logger.error("查询使用导入商品托数的商家异常:", e);
			}
			for(BmsGroupCustomerVo vo:cusList){
				cusNames.add(vo.getCustomerid());
			}
		}
		
		//指定的商家
		Map<String,Object> map= new HashMap<String, Object>();
		map.put("groupCode", "customer_unit");
		map.put("bizType", "group_customer");
		BmsGroupVo bmsGroup1=bmsGroupService.queryOne(map);
		if(bmsGroup!=null){
			cusList=bmsGroupCustomerService.queryCustomerByGroupId(bmsGroup1.getId());
		}
			
	}

	@Override
	public FeesReceiveStorageEntity initFee(BizPalletInfoEntity entity) {
		FeesReceiveStorageEntity fee = new FeesReceiveStorageEntity();	
		//如果商家不在《使用导入商品托数的商家》, 更新计费来源是系统, 同时使用系统托数计费
		//如果商家在《使用导入商品托数的商家》,更新计费来源是导入,同时使用导入托数计费
		//**** 以上逻辑可以放在定时任务中
		double num = 0d;
		if (cusNames.contains(entity.getCustomerId())) {
			entity.setChargeSource("import");
		}else {
			entity.setChargeSource("system");
		}
		//调整托数优先级最高
		if (DoubleUtil.isBlank(entity.getAdjustPalletNum())) {
			if (cusNames.contains(entity.getCustomerId())) {
				num = entity.getPalletNum();
			}else {
				num = entity.getSysPalletNum();
			}
		}else {
			num = entity.getAdjustPalletNum();
		}
		
		fee.setQuantity(num);		
		fee.setUnit("PALLETS");
		fee.setTempretureType(entity.getTemperatureTypeCode());//设置温度类型
		fee.setCost(new BigDecimal(0));	//入仓金额
		fee.setUnitPrice(0d);
		//托数类型
		fee.setBizType(entity.getBizType());
		fee.setFeesNo(entity.getFeesNo());
		fee.setSubjectCode(subjectCode);
		fee.setParam1(TemplateTypeEnum.COMMON.getCode());
		fee.setCalcuMsg("");
		fee.setCalculateTime(JAppContext.currentTimestamp());
		return fee;
	}

	@Override
	public boolean isNoExe(BizPalletInfoEntity entity,FeesReceiveStorageEntity fee) {
		//1.出库托数不计算费用
		if("outstock".equals(entity.getBizType())){
			fee.setIsCalculated(CalculateState.No_Exe.getCode());
			fee.setCalcuMsg("出库托数不计算费用;");
			return true;
		}
		//2.商家已经按件收取存储费,按托存储不计费
		if ("product".equals(entity.getBizType())) {
			//如果商家已经按件收取存储费，则按托存储不计费
			if(cusList.size()>0 && cusList.contains(entity.getCustomerId())){
				fee.setIsCalculated(CalculateState.No_Exe.getCode());
				fee.setCalcuMsg("商家已经按件收取存储费,按托存储不计费");
				return true;
			}
		}
		return false;		
	}

	@Override
	public void calcuForBms(BizPalletInfoEntity entity,FeesReceiveStorageEntity fee) {
		
		//合同校验
		if(contractList.size()<=0){
			fee.setIsCalculated(CalculateState.Contract_Miss.getCode());
			fee.setCalcuMsg("bms合同缺失");
			return;
		}
		
		//业务时间和合同时间进行匹配
        //合同
        CalcuContractVo contract=null;
        for(CalcuContractVo con:contractList){
            if(con.getStartDate().before(entity.getCreateTime()) && entity.getCreateTime().before(con.getExpireDate())){
                contract=con;
                break;
            }
        }
		
        if(contract==null){
            fee.setIsCalculated(CalculateState.Contract_Miss.getCode());
            fee.setCalcuMsg("bms合同缺失");
            return;
        }
        
		logger.info("合同信息{}",contract.getContractNo());
		
		 //模板编号
        String quoTempleteCode=contract.getModelNo();
		
		if("fail".equals(quoTempleteCode)){
			fee.setIsCalculated(CalculateState.Quote_Miss.getCode());
			fee.setCalcuMsg("未签约服务");
			return;
		}
		
		  //查询报价模板
        Map<String, Object> con = new HashMap<>();
        con.put("subjectId",serviceSubjectCode);
        con.put("quotationNo", quoTempleteCode);
        quoTemplete = priceGeneralQuotationRepository.query(con);
		
		if(quoTemplete == null){
			fee.setIsCalculated(CalculateState.Quote_Miss.getCode());
			fee.setCalcuMsg("报价模板缺失");
			return;
		}
		
		String priceType = quoTemplete.getPriceType();
		
		try{
			double amount=0d;
			switch(priceType){
			case "PRICE_TYPE_NORMAL"://一口价				
	            // -> 费用 = 托数*模板单价
				amount=fee.getQuantity()*quoTemplete.getUnitPrice();					
				fee.setUnitPrice(quoTemplete.getUnitPrice());
				fee.setParam3(quoTemplete.getId()+"");
				break;
			case "PRICE_TYPE_STEP"://阶梯价
				Map<String,Object> map=new HashMap<String,Object>();
				map.put("quotationId", quoTemplete.getId());
				map.put("num", fee.getQuantity());//根据报价单位判断	
				List<PriceStepQuotationEntity> list=repository.queryPriceStepByQuatationId(map);
				
				
				map.clear();
				map.put("warehouse_code", entity.getWarehouseCode());
				map.put("temperature_code", entity.getTemperatureTypeCode());
				PriceStepQuotationEntity stepQuoEntity=storageQuoteFilterService.quoteFilter(list, map);
				
				if(stepQuoEntity==null){
					logger.info("阶梯报价未配置");
					fee.setIsCalculated(CalculateState.Quote_Miss.getCode());
					fee.setCalcuMsg("阶梯报价未配置");
					return;
				}
				
				if(!DoubleUtil.isBlank(stepQuoEntity.getUnitPrice())){
					amount=fee.getQuantity()*stepQuoEntity.getUnitPrice();
					fee.setUnitPrice(stepQuoEntity.getUnitPrice());
				}else{
					amount=stepQuoEntity.getFirstNum()<fee.getQuantity()?stepQuoEntity.getFirstPrice()+(fee.getQuantity()-stepQuoEntity.getFirstNum())/stepQuoEntity.getContinuedItem()*stepQuoEntity.getContinuedPrice():stepQuoEntity.getFirstPrice();
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
			fee.setCost(BigDecimal.valueOf(amount));
			fee.setParam4(priceType);
			fee.setIsCalculated(CalculateState.Finish.getCode());
			fee.setCalcuMsg("计算成功");
		}catch(Exception ex){
			fee.setIsCalculated(CalculateState.Sys_Error.getCode());
			fee.setCalcuMsg("系统异常");
			logger.error("系统异常，费用【0】",ex);
		}
	}

	@Override
	public void calcuForContract(BizPalletInfoEntity entity,FeesReceiveStorageEntity fee) {
		ContractQuoteQueryInfoVo queryVo = getCtConditon(entity);
		contractCalcuService.calcuForContract(entity, fee, taskVo, errorMap, queryVo,cbiVo,fee.getFeesNo());
		if("succ".equals(errorMap.get("success").toString())){
			if(fee.getCost().compareTo(BigDecimal.ZERO) == 1){
				fee.setIsCalculated(CalculateState.Finish.getCode());
				fee.setCalcuMsg("计算成功");
				logger.info("计算成功，费用【{}】",fee.getCost());
			}
			else{
			    if (fee.getQuantity() == 0 || fee.getUnitPrice() == 0) {
			        fee.setIsCalculated(CalculateState.Finish.getCode());
	                logger.info("计算成功，费用【{}】",fee.getCost());
	                fee.setCalcuMsg("计算成功");
                }else {
                    fee.setIsCalculated(CalculateState.Sys_Error.getCode());
                    logger.info("计算不成功，费用【{}】",fee.getCost());
                    fee.setCalcuMsg("未计算出金额");
                }
			}
		}
		else{
			fee.setIsCalculated(errorMap.get("is_calculated").toString());
			fee.setCalcuMsg(errorMap.get("msg").toString());
		}
		
	}

	@Override
	public ContractQuoteQueryInfoVo getCtConditon(BizPalletInfoEntity entity) {
		ContractQuoteQueryInfoVo queryVo = new ContractQuoteQueryInfoVo();
		queryVo.setCustomerId(entity.getCustomerId());
		queryVo.setBizTypeCode(ContractBizTypeEnum.STORAGE.getCode());
		queryVo.setSubjectCode(subjectCode);
		queryVo.setCurrentTime(entity.getCreateTime());
		queryVo.setWarehouseCode(entity.getWarehouseCode());
		return queryVo;
	}

	@Override
	public void updateBatch(List<BizPalletInfoEntity> bizList,List<FeesReceiveStorageEntity> feeList) {
		//业务表更新计费来源
		
		StopWatch sw = new StopWatch();
		
		sw.start();
		bizPalletInfoService.updatebizPallet(bizList);
		sw.stop();
		logger.info("taskId={} 更新托数业务数据行数【{}】 耗时【{}】",taskVo.getTaskId(),bizList.size(),sw.getLastTaskTimeMillis());
		
		sw.start();
		feesReceiveStorageService.updateBatch(feeList);
		sw.stop();
		logger.info("taskId={} 更新仓储费用行数【{}】 耗时【{}】",taskVo.getTaskId(),feeList.size(),sw.getLastTaskTimeMillis());
		
	}
	
	

}
