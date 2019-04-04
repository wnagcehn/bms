package com.jiuyescm.bms.calcu.receive.storage.instock;

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
import com.jiuyescm.bms.calcu.CalcuLog;
import com.jiuyescm.bms.calcu.base.ICalcuService;
import com.jiuyescm.bms.calcu.receive.CommonService;
import com.jiuyescm.bms.calcu.receive.ContractCalcuService;
import com.jiuyescm.bms.calcu.receive.BmsContractBase;
import com.jiuyescm.bms.calculate.vo.CalcuInfoVo;
import com.jiuyescm.bms.common.enumtype.CalculateState;
import com.jiuyescm.bms.general.entity.BmsBizInstockInfoEntity;
import com.jiuyescm.bms.general.entity.FeesReceiveStorageEntity;
import com.jiuyescm.bms.general.service.IBmsBizInstockInfoRepository;
import com.jiuyescm.bms.general.service.IFeesReceiveStorageService;
import com.jiuyescm.bms.general.service.IStorageQuoteFilterService;
import com.jiuyescm.bms.quotation.storage.entity.PriceGeneralQuotationEntity;
import com.jiuyescm.bms.quotation.storage.entity.PriceStepQuotationEntity;
import com.jiuyescm.bms.quotation.storage.repository.IPriceGeneralQuotationRepository;
import com.jiuyescm.bms.quotation.storage.repository.IPriceStepQuotationRepository;
import com.jiuyescm.cfm.common.JAppContext;
import com.jiuyescm.common.utils.DoubleUtil;
import com.jiuyescm.constants.CalcuNodeEnum;
import com.jiuyescm.contract.quote.vo.ContractBizTypeEnum;
import com.jiuyescm.contract.quote.vo.ContractQuoteQueryInfoVo;

@Component("instockCalcuJob")
@Scope("prototype")
public class InstockCalcuJob extends BmsContractBase implements ICalcuService<BmsBizInstockInfoEntity,FeesReceiveStorageEntity> {

	private Logger logger = LoggerFactory.getLogger(InstockCalcuJob.class);
		
	@Autowired private IBmsBizInstockInfoRepository bmsBizInstockInfoRepository;
	@Autowired private IPriceStepQuotationRepository repository;
	@Autowired private IStorageQuoteFilterService storageQuoteFilterService;
	@Autowired private IPriceGeneralQuotationRepository priceGeneralQuotationRepository;
	@Autowired private IFeesReceiveStorageService feesReceiveStorageService;
	@Autowired private ContractCalcuService contractCalcuService;
	@Autowired private CommonService commonService;
	@Autowired IBmsCalcuTaskService bmsCalcuTaskService;


	//private String quoTempleteCode = null;
	private PriceGeneralQuotationEntity quoTemplete = null;
	private Map<String, Object> errorMap = null;

	public void process(BmsCalcuTaskVo taskVo,String contractAttr){
		super.process(taskVo, contractAttr);
		getQuoTemplete();
		serviceSubjectCode = subjectCode;
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
		
	}
	
	@Override
	public void calcu(Map<String, Object> map){
		
		List<BmsBizInstockInfoEntity> bizList = bmsBizInstockInfoRepository.getInStockInfoList(map);
		List<FeesReceiveStorageEntity> fees = new ArrayList<>();
		if(bizList == null || bizList.size() == 0){
			commonService.taskCountReport(taskVo, "STORAGE");
			return;
		}
		logger.info("taskId={} 查询行数【{}】",taskVo.getTaskId(),bizList.size());
		for (BmsBizInstockInfoEntity entity : bizList) {
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
		calcu(map);
		
	}
	
	@Override
	public FeesReceiveStorageEntity initFee(BmsBizInstockInfoEntity entity){
		//打印业务数据日志
		cbiVo.setFeesNo(entity.getFeesNo());
		CalcuLog.printLog(CalcuNodeEnum.BIZ.getCode().toString(), "", entity, cbiVo);
		FeesReceiveStorageEntity fee = new FeesReceiveStorageEntity();
		fee.setQuantity(0d);
		fee.setWeight(0d);
		fee.setBox(0);
		double num=DoubleUtil.isBlank(entity.getAdjustQty())?entity.getTotalQty():entity.getAdjustQty();
		if(!DoubleUtil.isBlank(num)){
			fee.setQuantity(num);//商品数量
		}
		//重量
		Double weight = DoubleUtil.isBlank(entity.getAdjustWeight())?entity.getTotalWeight():entity.getAdjustWeight();
		fee.setWeight(weight);
		
		//箱数
		Double box=DoubleUtil.isBlank(entity.getAdjustBox())?entity.getTotalBox():entity.getTotalBox();
		if(!DoubleUtil.isBlank(box)){
			fee.setBox(box.intValue());
		}
		fee.setCostType("FEE_TYPE_GENEARL");
		fee.setUnitPrice(0d);
		fee.setCost(new BigDecimal(0));	
		fee.setDelFlag("0");
		fee.setFeesNo(entity.getFeesNo());
		fee.setSubjectCode(subjectCode);
		fee.setCalculateTime(JAppContext.currentTimestamp());
		CalcuLog.printLog(CalcuNodeEnum.CHARGE.getCode().toString(), "", fee, cbiVo);
		return fee;
		
	}
	
	@Override
	public boolean isNoExe(BmsBizInstockInfoEntity entity,FeesReceiveStorageEntity fee){
		return false;
	}
	
	@Override
	public void calcuForBms(BmsBizInstockInfoEntity entity,FeesReceiveStorageEntity fee){
		//合同校验
		if(contractInfo == null){
			fee.setIsCalculated(CalculateState.Contract_Miss.getCode());
			fee.setCalcuMsg("bms合同缺失");
			CalcuLog.printLog(CalcuNodeEnum.CONTRACT.getCode().toString(), "bms合同缺失", null, cbiVo);
			return;
		}
		logger.info("合同信息{}",contractInfo.getContractNo());
		
		if("fail".equals(quoTempleteCode)){
			fee.setIsCalculated(CalculateState.Quote_Miss.getCode());
			fee.setCalcuMsg("未签约服务");
			CalcuLog.printLog(CalcuNodeEnum.CONTRACT.getCode().toString(), "未签约服务", contractInfo, cbiVo);
			return;
		}
		
		if(quoTemplete == null){
			fee.setIsCalculated(CalculateState.Quote_Miss.getCode());
			fee.setCalcuMsg("报价模板缺失");
			CalcuLog.printLog(CalcuNodeEnum.CONTRACT.getCode().toString(), "报价模板缺失", contractInfo, cbiVo);
			return;
		}
		
		String priceType = quoTemplete.getPriceType();
		String unit = quoTemplete.getFeeUnitCode();//计费单位 
		double num = fee.getQuantity();
		CalcuInfoVo civo = new CalcuInfoVo();
		civo.setRuleNo("");
		civo.setChargeUnit(unit);
		switch (unit) {
		case "ITEMS":
			num = fee.getQuantity();
			break;
		case "CARTON":
			num = fee.getBox();
			break;
		case "TONS":
			double weight = 0d;
			if ((double)fee.getWeight()/1000 < 1) {
				weight = 1d;
			}else {
				weight = (double)fee.getWeight()/1000;
			}
			num = weight;
			break;
		case "KILOGRAM":
			num = fee.getWeight();
			break;
		default:
			break;
		}
		
		//计算方法
		double amount=0d;
		switch(priceType){
			case "PRICE_TYPE_NORMAL"://一口价		
				//打印报价
				//printLog(taskVo.getTaskId(), "quoteInfo", entity.getFeesNo(), taskVo.getSubjectName(), "", quoTemplete);
				civo.setChargeType("unitPrice");
				civo.setChargeDescrip("金额=单价*数量");
				amount=num*quoTemplete.getUnitPrice();
				fee.setUnitPrice(quoTemplete.getUnitPrice());
				fee.setParam3(quoTemplete.getId().toString());
				//printLog(vo.getTaskId(), "ruleInfo", entity.getFeesNo(), vo.getSubjectName(), "", civo);
				break;
			case "PRICE_TYPE_STEP"://阶梯价	
				civo.setChargeType("stepPrice");
				Map<String,Object> map=new HashMap<String,Object>();
				map.put("quotationId", quoTemplete.getId());
				map.put("num", fee.getQuantity());//根据报价单位判断			
				//查询出的所有子报价
				List<PriceStepQuotationEntity> list=repository.queryPriceStepByQuatationId(map);
				
				if(list==null || list.size() == 0){
					logger.info(entity.getId()+"阶梯报价未配置");
					fee.setIsCalculated(CalculateState.Quote_Miss.getCode());
					fee.setCalcuMsg("阶梯报价未配置");
					return;
				}
				
				//封装数据的仓库和温度
				map.clear();
				map.put("warehouse_code", entity.getWarehouseCode());
				PriceStepQuotationEntity stepQuoEntity=storageQuoteFilterService.quoteFilter(list, map);			
				
				if(stepQuoEntity==null){
					//printLog(vo.getTaskId(), "quoteInfo", entity.getFeesNo(), vo.getSubjectName(), "阶梯报价未配置", null);
					fee.setIsCalculated(CalculateState.Quote_Miss.getCode());
					fee.setCalcuMsg("阶梯报价未配置");
					return;
				}
				//printLog(vo.getTaskId(), "quoteInfo", entity.getFeesNo(), vo.getSubjectName(), "", stepQuoEntity);
				
				if(!DoubleUtil.isBlank(stepQuoEntity.getUnitPrice())){
					civo.setChargeType("unitPrice");
					civo.setChargeDescrip("金额=单价*数量");
					fee.setUnitPrice(stepQuoEntity.getUnitPrice());
					amount=num*stepQuoEntity.getUnitPrice();
				}else{
					civo.setChargeType("stepPrice");
					civo.setChargeDescrip("金额=首量价/首量价+续价");
					amount=stepQuoEntity.getFirstNum()<num?stepQuoEntity.getFirstPrice()+(num-stepQuoEntity.getFirstNum())/stepQuoEntity.getContinuedItem()*stepQuoEntity.getContinuedPrice():stepQuoEntity.getFirstPrice();
				}
				//判断封顶价
				if(!DoubleUtil.isBlank(stepQuoEntity.getCapPrice())){
					if(stepQuoEntity.getCapPrice()<amount){
						civo.setChargeType("capPrice");
						amount=stepQuoEntity.getCapPrice();
					}
				}
				//打印计费规则
				//printLog(vo.getTaskId(), "ruleInfo", entity.getFeesNo(), vo.getSubjectName(), "", civo);
				fee.setParam3(stepQuoEntity.getId()+"");
				break;
			default:
				break;
		}
		fee.setCost(BigDecimal.valueOf(amount));
		fee.setParam4(priceType);
		entity.setRemark(entity.getRemark()+"计算成功;");
		entity.setIsCalculated(CalculateState.Finish.getCode());
		fee.setCalcuMsg("计算成功");
		fee.setIsCalculated(CalculateState.Finish.getCode());
		fee.setCalculateTime(JAppContext.currentTimestamp());
	}
	
	@Override
	public void calcuForContract(BmsBizInstockInfoEntity entity,FeesReceiveStorageEntity fee){
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
	public ContractQuoteQueryInfoVo getCtConditon(BmsBizInstockInfoEntity entity) {
		ContractQuoteQueryInfoVo queryVo = new ContractQuoteQueryInfoVo();
		queryVo.setCustomerId(entity.getCustomerId());
		queryVo.setBizTypeCode(ContractBizTypeEnum.STORAGE.getCode());
		queryVo.setSubjectCode(subjectCode);
		queryVo.setCurrentTime(entity.getCreateTime());
		queryVo.setWarehouseCode(entity.getWarehouseCode());
		return queryVo;

	}
	
	@Override
	public void updateBatch(List<BmsBizInstockInfoEntity> bizList,List<FeesReceiveStorageEntity> feeList) {
		
		StopWatch sw = new StopWatch();
		sw.start();
		feesReceiveStorageService.updateBatch(feeList);
		sw.stop();
		logger.info("taskId={} 更新仓储费用行数【{}】 耗时【{}】",taskVo.getTaskId(),feeList.size(),sw.getLastTaskTimeMillis());
	}


	

	
	
}
