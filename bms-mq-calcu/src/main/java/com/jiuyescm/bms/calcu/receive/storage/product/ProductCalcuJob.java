package com.jiuyescm.bms.calcu.receive.storage.product;

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
import com.jiuyescm.bms.base.group.vo.BmsGroupVo;
import com.jiuyescm.bms.biz.storage.entity.BizProductStorageEntity;
import com.jiuyescm.bms.calcu.base.ICalcuService;
import com.jiuyescm.bms.calcu.receive.CommonService;
import com.jiuyescm.bms.calcu.receive.ContractCalcuService;
import com.jiuyescm.bms.calcu.receive.BmsContractBase;
import com.jiuyescm.bms.calculate.vo.CalcuInfoVo;
import com.jiuyescm.bms.common.enumtype.CalculateState;
import com.jiuyescm.bms.common.enumtype.TemplateTypeEnum;
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
import com.jiuyescm.bms.receivable.storage.service.IBizProductStorageService;
import com.jiuyescm.bms.rule.receiveRule.repository.IReceiveRuleRepository;
import com.jiuyescm.cfm.common.JAppContext;
import com.jiuyescm.common.utils.DoubleUtil;
import com.jiuyescm.contract.quote.api.IContractQuoteInfoService;
import com.jiuyescm.contract.quote.vo.ContractBizTypeEnum;
import com.jiuyescm.contract.quote.vo.ContractQuoteQueryInfoVo;

@Component("productCalcuJob")
@Scope("prototype")
public class ProductCalcuJob extends BmsContractBase implements ICalcuService<BizProductStorageEntity,FeesReceiveStorageEntity> {

	private Logger logger = LoggerFactory.getLogger(ProductCalcuJob.class);
		
	
	@Autowired private IContractQuoteInfoService contractQuoteInfoService;
	@Autowired private IBizProductStorageService bizProductStorageService;
	@Autowired private IFeesCalcuService feesCalcuService;
	@Autowired private IPriceContractInfoService jobPriceContractInfoService;
	@Autowired private SequenceService sequenceService;
	@Autowired private IPriceGeneralQuotationRepository priceGeneralQuotationRepository;
	@Autowired private IPriceStepQuotationRepository  repository;
	@Autowired private IReceiveRuleRepository receiveRuleRepository;
	@Autowired private IFeesReceiveStorageService feesReceiveStorageService;
	@Autowired private IPriceContractItemRepository priceContractItemRepository;
	@Autowired private IBmsGroupService bmsGroupService;
	@Autowired private IBmsGroupCustomerService bmsGroupCustomerService;
	@Autowired private IStorageQuoteFilterService storageQuoteFilterService;
	@Autowired private ISystemCodeService systemCodeService;
	@Autowired private IBmsGroupSubjectService bmsGroupSubjectService;
	@Autowired IBmsCalcuTaskService bmsCalcuTaskService;
	@Autowired private ContractCalcuService contractCalcuService;
	@Autowired private CommonService commonService;


	//private String quoTempleteCode = null;					//签约服务中的模板编号
	private PriceGeneralQuotationEntity quoTemplete = null;	//报价模板对象
	private Map<String, Object> errorMap = null;			//用户合同在线计算
	List<String> cusList=null; 								//按重量算商品存储费的商家

	public void process(BmsCalcuTaskVo taskVo,String contractAttr){
		super.process(taskVo, contractAttr);
		serviceSubjectCode = subjectCode;
		getQuoTemplete();
		errorMap = new HashMap<String, Object>();//用户合同在线计算
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
		//查询按重量算商品存储费的商家
		Map<String,Object> map= new HashMap<String, Object>();
		map.put("groupCode", "customer_unit");
		map.put("bizType", "group_customer");
		BmsGroupVo bmsGroup=bmsGroupService.queryOne(map);
		if(bmsGroup!=null){
			cusList=bmsGroupCustomerService.queryCustomerByGroupId(bmsGroup.getId());
		}
	}
	
	@Override
	public void calcu(Map<String, Object> map){
		
		List<BizProductStorageEntity> bizList = bizProductStorageService.query(map);
		List<FeesReceiveStorageEntity> fees = new ArrayList<>();
		if(bizList == null || bizList.size() == 0){
			commonService.taskCountReport(taskVo, "STORAGE");
			return;
		}
		logger.info("taskId={} 查询行数【{}】",taskVo.getTaskId(),bizList.size());
		for (BizProductStorageEntity entity : bizList) {
			FeesReceiveStorageEntity fee = initFee(entity);
			fees.add(fee);
			if(isNoExe(entity, fee)){
				continue; //如果不计算费用,后面的逻辑不在执行，只是在最后更新数据库状态
			}
			try {
				if("BMS".equals(contractAttr)){
					calcuForBms(entity,fee);
				}
				else {
					calcuForContract(entity,fee);
				}
			} catch (Exception e) {
				// TODO: handle exception
				logger.error("计算异常",e);
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
	public FeesReceiveStorageEntity initFee(BizProductStorageEntity entity){
		
		FeesReceiveStorageEntity fee = new FeesReceiveStorageEntity();	
		fee.setCalculateTime(JAppContext.currentTimestamp());
		if(entity.getAqty()!=null){
			fee.setQuantity(Double.valueOf(0));
		}else{
			fee.setQuantity(entity.getAqty());             //商品数量
		}
		if(entity.getWeight() == null){
			fee.setWeight(Double.valueOf(0));
		}else{
			fee.setWeight(entity.getWeight());
		}
		fee.setStatus("0");		//状态
		fee.setTempretureType(entity.getTemperature());		//温度类型
		fee.setProductNo(entity.getProductId());			//商品编码
		fee.setProductName(entity.getProductName());		//商品名称
		fee.setBizId(String.valueOf(entity.getId()));      	//业务数据主键
		fee.setCost(new BigDecimal(0));						//入仓金额
		fee.setUnitPrice(0d);
		fee.setFeesNo(entity.getFeesNo());	
		fee.setSubjectCode(subjectCode);
		fee.setParam1(TemplateTypeEnum.COMMON.getCode());
		fee.setDelFlag("0");
		fee.setTempretureType(entity.getTemperature());
		/*if(StringUtils.isNotBlank(entity.getTemperature())){
			entity.setTemperature(temMap.get(entity.getTemperature()));
		}*/
		return fee;
	}
	
	@Override
	public boolean isNoExe(BizProductStorageEntity entity,FeesReceiveStorageEntity fee){
		//只有按件商家才计算
		if(cusList.size()==0 || !cusList.contains(entity.getCustomerid())){
			fee.setIsCalculated(CalculateState.No_Exe.getCode());
			fee.setCalcuMsg("只有按件商家收取按件存储费,其他不计费");
			return true;
		}
		return false;		
	}
	
	@Override
	public void calcuForBms(BizProductStorageEntity entity,FeesReceiveStorageEntity fee){
		//合同校验
		if(contractInfo == null){
			fee.setIsCalculated(CalculateState.Contract_Miss.getCode());
			fee.setCalcuMsg("bms合同缺失");
			return;
		}
		logger.info("合同信息{}",contractInfo.getContractNo());
		
		//签约服务校验
		if("fail".equals(quoTempleteCode)){
			fee.setIsCalculated(CalculateState.Quote_Miss.getCode());
			fee.setCalcuMsg("未签约服务");
			return;
		}
		//报价模板校验
		if(quoTemplete == null){
			fee.setIsCalculated(CalculateState.Quote_Miss.getCode());
			fee.setCalcuMsg("报价模板缺失");
			return;
		}
		
		//报价类型 一口价 or 阶梯价
		String priceType = quoTemplete.getPriceType(); 
		String unit = quoTemplete.getFeeUnitCode();		//计费单位  数量/重量...
		fee.setUnit(unit);//赋值计费单位
		double num = 0;
		CalcuInfoVo civo = new CalcuInfoVo();
		civo.setRuleNo("");
		civo.setChargeUnit(unit);
		switch (unit) {
		case "ITEMS":
			num = fee.getQuantity();
			break;
		case "KILOGRAM":
			//如果报价模板时计费单位是 千克，计费数量为 件数*重量
			num = fee.getWeight()*fee.getQuantity();
			break;
		default:
			logger.info("不支持计费单位【{}】",priceType);
			fee.setIsCalculated(CalculateState.Quote_Miss.getCode());
			fee.setCalcuMsg("不支持计费单位【"+priceType+"】");
			return;
		}
		
		//计算方法
		double amount=0d; //金额
		switch(priceType){
			case "PRICE_TYPE_NORMAL"://一口价	
				amount=num*quoTemplete.getUnitPrice();
				fee.setUnitPrice(quoTemplete.getUnitPrice());
				fee.setParam3(quoTemplete.getId().toString());
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
				
				if(stepQuoEntity==null){fee.setIsCalculated(CalculateState.Quote_Miss.getCode());
					fee.setCalcuMsg("阶梯报价未配置");
					return;
				}
				if(!DoubleUtil.isBlank(stepQuoEntity.getUnitPrice())){
					civo.setChargeType("unitPrice");
					civo.setChargeDescrip("金额=单价*数量");
					fee.setUnitPrice(stepQuoEntity.getUnitPrice());
					amount=num*stepQuoEntity.getUnitPrice();
				}else{
					civo.setChargeType("stepPrice");
					civo.setChargeDescrip("金额=首量价/(首量价+续价)");
					amount=stepQuoEntity.getFirstNum()<num?stepQuoEntity.getFirstPrice()+(num-stepQuoEntity.getFirstNum())/stepQuoEntity.getContinuedItem()*stepQuoEntity.getContinuedPrice():stepQuoEntity.getFirstPrice();
				}
				//判断封顶价
				if(!DoubleUtil.isBlank(stepQuoEntity.getCapPrice())){
					if(stepQuoEntity.getCapPrice()<amount){
						civo.setChargeType("capPrice");
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
		fee.setCalcuMsg("计算成功");
		fee.setIsCalculated(CalculateState.Finish.getCode());
	}
	
	@Override
	public void calcuForContract(BizProductStorageEntity entity,FeesReceiveStorageEntity fee){
		ContractQuoteQueryInfoVo queryVo = getCtConditon(entity);
		contractCalcuService.calcuForContract(entity, fee, taskVo, errorMap, queryVo,cbiVo);
		if("succ".equals(errorMap.get("success").toString())){
			if(fee.getCost().compareTo(BigDecimal.ZERO) == 1){
				fee.setIsCalculated(CalculateState.Finish.getCode());
				fee.setCalcuMsg("计算成功");
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
	public ContractQuoteQueryInfoVo getCtConditon(BizProductStorageEntity entity) {
		ContractQuoteQueryInfoVo queryVo = new ContractQuoteQueryInfoVo();
		queryVo.setCustomerId(entity.getCustomerid());
		queryVo.setBizTypeCode(ContractBizTypeEnum.STORAGE.getCode());
		queryVo.setSubjectCode(subjectCode);
		queryVo.setCurrentTime(entity.getCreateTime());
		queryVo.setWarehouseCode(entity.getWarehouseCode());
		return queryVo;

	}
	
	@Override
	public void updateBatch(List<BizProductStorageEntity> bizList,List<FeesReceiveStorageEntity> feeList) {
		StopWatch sw = new StopWatch();
		sw.start();
		feesReceiveStorageService.updateBatch(feeList);
		sw.stop();
		logger.info("taskId={} 更新仓储费用行数【{}】 耗时【{}】",taskVo.getTaskId(),feeList.size(),sw.getLastTaskTimeMillis());
	}
	
}
