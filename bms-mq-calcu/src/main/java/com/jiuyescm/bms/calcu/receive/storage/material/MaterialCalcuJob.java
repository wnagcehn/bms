package com.jiuyescm.bms.calcu.receive.storage.material;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.alibaba.dubbo.common.utils.CollectionUtils;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import com.jiuyescm.bms.asyn.service.IBmsCalcuTaskService;
import com.jiuyescm.bms.asyn.vo.BmsCalcuTaskVo;
import com.jiuyescm.bms.base.dict.api.IMaterialDictService;
import com.jiuyescm.bms.base.dictionary.entity.SystemCodeEntity;
import com.jiuyescm.bms.base.dictionary.repository.ISystemCodeRepository;
import com.jiuyescm.bms.base.group.service.IBmsGroupSubjectService;
import com.jiuyescm.bms.biz.dispatch.entity.BizDispatchBillEntity;
import com.jiuyescm.bms.biz.storage.entity.BizOutstockPackmaterialEntity;
import com.jiuyescm.bms.calcu.CalcuLog;
import com.jiuyescm.bms.calcu.base.ICalcuService;
import com.jiuyescm.bms.calcu.receive.CommonService;
import com.jiuyescm.bms.calcu.receive.ContractCalcuService;
import com.jiuyescm.bms.calcu.receive.BmsContractBase;
import com.jiuyescm.bms.common.enumtype.CalculateState;
import com.jiuyescm.bms.common.enumtype.TemplateTypeEnum;
import com.jiuyescm.bms.drools.IFeesCalcuService;
import com.jiuyescm.bms.general.entity.FeesReceiveStorageEntity;
import com.jiuyescm.bms.general.service.IFeesReceiveStorageService;
import com.jiuyescm.bms.general.service.IPriceContractInfoService;
import com.jiuyescm.bms.general.service.IStorageQuoteFilterService;
import com.jiuyescm.bms.general.service.SequenceService;
import com.jiuyescm.bms.quotation.contract.repository.imp.IPriceContractDao;
import com.jiuyescm.bms.quotation.contract.repository.imp.IPriceContractItemRepository;
import com.jiuyescm.bms.quotation.storage.entity.PriceGeneralQuotationEntity;
import com.jiuyescm.bms.quotation.storage.entity.PriceMaterialQuotationEntity;
import com.jiuyescm.bms.quotation.storage.repository.IPriceMaterialQuotationRepository;
import com.jiuyescm.bms.quotation.transport.repository.IGenericTemplateRepository;
import com.jiuyescm.bms.receivable.dispatch.service.IBizDispatchBillService;
import com.jiuyescm.bms.receivable.storage.service.IBizOutstockPackmaterialService;
import com.jiuyescm.bms.rule.receiveRule.repository.IReceiveRuleRepository;
import com.jiuyescm.cfm.common.JAppContext;
import com.jiuyescm.common.utils.DoubleUtil;
import com.jiuyescm.constants.CalcuNodeEnum;
import com.jiuyescm.contract.quote.api.IContractQuoteInfoService;
import com.jiuyescm.contract.quote.vo.ContractBizTypeEnum;
import com.jiuyescm.contract.quote.vo.ContractQuoteQueryInfoVo;
import com.jiuyescm.mdm.customer.api.IPubMaterialInfoService;
import com.jiuyescm.mdm.customer.vo.PubMaterialInfoVo;

@Component("materialCalcuJob")
@Scope("prototype")
public class MaterialCalcuJob extends BmsContractBase implements ICalcuService<BizOutstockPackmaterialEntity,FeesReceiveStorageEntity> {

	private Logger logger = LoggerFactory.getLogger(MaterialCalcuJob.class);
		
	@Autowired private IBizOutstockPackmaterialService bizOutstockPackmaterialService;
	@Autowired private IContractQuoteInfoService contractQuoteInfoService;
	@Autowired private IBizDispatchBillService bizDispatchBillService;
	@Autowired private IFeesCalcuService feesCalcuService;
	@Autowired private IReceiveRuleRepository receiveRuleRepository;
	@Autowired private IPriceContractDao priceContractService;
	@Autowired private IPriceContractInfoService jobPriceContractInfoService;
	@Autowired private IPriceContractItemRepository priceContractItemRepository;
	@Autowired private IPriceMaterialQuotationRepository priceMaterialQuotationRepository;
	@Autowired private IGenericTemplateRepository genericTemplateRepository;
	@Autowired private IFeesReceiveStorageService feesReceiveStorageService;
	@Autowired private SequenceService sequenceService;
	@Autowired private IBmsGroupSubjectService bmsGroupSubjectService;
	@Autowired private IStorageQuoteFilterService storageQuoteFilterService;
	@Autowired private IPubMaterialInfoService pubMaterialInfoService;
	@Autowired private ISystemCodeRepository systemCodeRepository;
	@Autowired private IMaterialDictService materialDictService;
	@Autowired private ContractCalcuService contractCalcuService;
	@Autowired private CommonService commonService;
	@Autowired IBmsCalcuTaskService bmsCalcuTaskService;

	private PriceGeneralQuotationEntity quoTemplete = null;
	private Map<String, Object> errorMap = null;
	Map<String,PubMaterialInfoVo> materialMap = null;
	List<SystemCodeEntity> noCarrierMap=null;
	Map<String,SystemCodeEntity> noFeesMap=null;

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
			//quoTemplete = priceGeneralQuotationRepository.query(map);
		}
	}
	
	@Override
	public void initConf(){
		Map<String,Object> condition=Maps.newHashMap();
		List<PubMaterialInfoVo> tmscodels = pubMaterialInfoService.queryList(condition);
		materialMap=Maps.newLinkedHashMap();
		for(PubMaterialInfoVo materialVo:tmscodels){
			if(!StringUtils.isBlank(materialVo.getBarcode())){
				materialMap.put(materialVo.getBarcode().trim(),materialVo);
			}
		}
		
		noCarrierMap=new ArrayList<SystemCodeEntity>();
		condition.clear();
		condition.put("typeCode", "CARRIER_FREE");
		PageInfo<SystemCodeEntity> page = systemCodeRepository.query(condition, 1, 999999);
		if(page.getList()!=null||page.getList().size()>0){
			noCarrierMap = page.getList();
		}
		
		condition.clear();
		condition.put("typeCode", "NO_FEES_MATERIAL");
		page = systemCodeRepository.query(condition, 1, 999999);
		noFeesMap=new HashMap<String, SystemCodeEntity>();
		if(page.getList()!=null||page.getList().size()>0){
			List<SystemCodeEntity> list = page.getList();
			for(SystemCodeEntity entity:list){
				noFeesMap.put(entity.getCode(),entity);
			}
		}
	}
	
	@Override
	public void calcu(Map<String, Object> map){
		
		List<BizOutstockPackmaterialEntity> bizList = bizOutstockPackmaterialService.query(map);
		List<FeesReceiveStorageEntity> fees = new ArrayList<>();
		if(bizList == null || bizList.size() == 0){
			commonService.taskCountReport(taskVo, "STORAGE");
			return;
		}
		logger.info("taskId={} 查询行数【{}】",taskVo.getTaskId(),bizList.size());
		for (BizOutstockPackmaterialEntity entity : bizList) {
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
	public FeesReceiveStorageEntity initFee(BizOutstockPackmaterialEntity entity){
		//打印业务数据日志
		CalcuLog.printLog(CalcuNodeEnum.BIZ.getCode().toString(), "", entity, cbiVo);
		FeesReceiveStorageEntity fee = new FeesReceiveStorageEntity();	
		fee.setCostType("FEE_TYPE_MATERIAL");				//费用类别 FEE_TYPE_GENEARL-通用 FEE_TYPE_MATERIAL-耗材 FEE_TYPE_ADD-增值
		fee.setSubjectCode(subjectCode);					//费用科目
		fee.setOtherSubjectCode(subjectCode);
		fee.setProductType("");//商品类型
		fee.setProductNo(entity.getConsumerMaterialCode());
		fee.setProductName(entity.getConsumerMaterialName());
		fee.setQuantity(DoubleUtil.isBlank(entity.getAdjustNum())?entity.getNum():entity.getAdjustNum());//计费数量
		if(materialMap!=null && materialMap.containsKey(entity.getConsumerMaterialCode())){
			String materialType=materialMap.get(entity.getConsumerMaterialCode()).getMaterialType();
			if("干冰".equals(materialType)){
				fee.setQuantity(DoubleUtil.isBlank(entity.getAdjustNum())?entity.getWeight():entity.getAdjustNum());//计费数量
			}else{
				fee.setQuantity(DoubleUtil.isBlank(entity.getAdjustNum())?entity.getNum():entity.getAdjustNum());//计费重量
			}
		}
		fee.setStatus("0");								//状态
		fee.setWeight(entity.getWeight());				//设置重量
		fee.setCalculateTime(JAppContext.currentTimestamp());
		fee.setCost(new BigDecimal(0));					//入仓金额
		fee.setFeesNo(entity.getFeesNo());
		fee.setParam1(TemplateTypeEnum.COMMON.getCode());
		return fee;
		
	}
	
	@Override
	public boolean isNoExe(BizOutstockPackmaterialEntity entity,FeesReceiveStorageEntity fee){
		//运单
		BizDispatchBillEntity biz=bizDispatchBillService.getDispatchEntityByWaybillNo(entity.getWaybillNo());
		//耗材类型
		String materialType = "";
		if(materialMap!=null && materialMap.containsKey(entity.getConsumerMaterialCode())){
			materialType=materialMap.get(entity.getConsumerMaterialCode()).getMaterialType();
		}

		//物流商对应得耗材不计费
		if(biz!=null && StringUtils.isNotBlank(materialType)){
			for (SystemCodeEntity scEntity : noCarrierMap) {
				if(scEntity.getCode().equals(biz.getCarrierId()) && scEntity.getExtattr1().equals(materialType)){
					//XxlJobLogger.log("-->"+entity.getId()+String.format("该物流商使用的耗材不收钱", entity.getId()));
					fee.setIsCalculated(CalculateState.No_Exe.getCode());
					fee.setCalcuMsg("该物流商使用的耗材不收钱");
					return true;
				}
			}
			
		}
		
		//指定耗材不计费
		if(entity.getConsumerMaterialCode()!=null && noFeesMap.containsKey(entity.getConsumerMaterialCode())){
			fee.setIsCalculated(CalculateState.No_Exe.getCode());
			fee.setCalcuMsg("耗材不收钱");
			return true;
		}
		return false;		
	}
	
	@Override
	public void calcuForBms(BizOutstockPackmaterialEntity entity,FeesReceiveStorageEntity fee){
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
		
		try{
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("contractCode", contractInfo.getContractNo());
			map.put("subjectId",subjectCode);
			map.put("materialCode",entity.getConsumerMaterialCode());
			List<PriceMaterialQuotationEntity> list=priceMaterialQuotationRepository.queryMaterialQuatationByContract(map);
			
			if(list==null||list.size()==0){
				//XxlJobLogger.log("-->"+entity.getId()+"报价未配置");
				entity.setIsCalculated(CalculateState.Quote_Miss.getCode());
				fee.setIsCalculated(CalculateState.Quote_Miss.getCode());
				fee.setCalcuMsg("报价未配置");
				entity.setRemark(entity.getRemark()+"报价未配置;");
				return;
			}
			
			PriceMaterialQuotationEntity stepQuoEntity = null;
			
			//判断耗材类型是否为泡沫箱
			PubMaterialInfoVo materialInfoVo = materialDictService.getMaterialByCode(entity.getConsumerMaterialCode());
			if (null != materialInfoVo && "泡沫箱".equals(materialInfoVo.getMaterialType())) {
				//获取当前月份
				Calendar cale = Calendar.getInstance();
				Timestamp creTime = entity.getCreateTime();
				cale.setTime((Date)creTime);
				int month = cale.get(Calendar.MONTH) + 1;				
				//当前月份在夏季，筛出全国仓和其他仓最低报价
				List<SystemCodeEntity> pmxTimes = systemCodeRepository.findEnumList("PMX_TIME");
				String season = "";//季节
				boolean exe = false;
				for (SystemCodeEntity time : pmxTimes) {
					if (month >= Integer.parseInt(time.getExtattr1()) && month <= Integer.parseInt(time.getExtattr2())) {
						season = time.getCode();
						exe = true;
						//仓库过滤
						map.clear();
						map.put("warehouse_code", entity.getWarehouseCode());
						List<PriceMaterialQuotationEntity> pmxPriceList = storageQuoteFilterService.quotePMXmaterialFilter(list, map);
						if ("SUMMER".equals(season)) {
							//夏季
							if (CollectionUtils.isNotEmpty(pmxPriceList)) {
								if (pmxPriceList.size() == 2) {
									if (pmxPriceList.get(0).getUnitPrice() > pmxPriceList.get(1).getUnitPrice()) {
										stepQuoEntity = pmxPriceList.get(0);
									}else {
										stepQuoEntity = pmxPriceList.get(1);
									}
								}else {
									stepQuoEntity = pmxPriceList.get(0);
								}
							}else {
								stepQuoEntity = null;
							}
						}else {
							//其余季节
							if (CollectionUtils.isNotEmpty(pmxPriceList)) {
								if (pmxPriceList.size() == 2) {
									if (pmxPriceList.get(0).getUnitPrice() > pmxPriceList.get(1).getUnitPrice()) {
										stepQuoEntity = pmxPriceList.get(1);
									}else {
										stepQuoEntity = pmxPriceList.get(0);
									}
								}else {
									stepQuoEntity = pmxPriceList.get(0);
								}					
							}else {
								stepQuoEntity = null;
							}
						}
					}
				}
				if (!exe) {
					//封装数据的仓库
					map.clear();
					map.put("warehouse_code", entity.getWarehouseCode());
					stepQuoEntity=storageQuoteFilterService.quoteMaterialFilter(list, map);
				}
			}else {
				//封装数据的仓库
				map.clear();
				map.put("warehouse_code", entity.getWarehouseCode());
				stepQuoEntity=storageQuoteFilterService.quoteMaterialFilter(list, map);
			}

			if(stepQuoEntity==null){
				//XxlJobLogger.log("-->"+entity.getId()+"报价未配置");
				entity.setIsCalculated(CalculateState.Quote_Miss.getCode());
				fee.setIsCalculated(CalculateState.Quote_Miss.getCode());
				entity.setRemark(entity.getRemark()+"报价未配置;");
				fee.setCalcuMsg("报价未配置");
				return;
			}
			//XxlJobLogger.log("筛选后得到的报价结果【{0}】",JSONObject.fromObject(stepQuoEntity));
						
			fee.setCost(new BigDecimal(stepQuoEntity.getUnitPrice()*fee.getQuantity()).setScale(2,BigDecimal.ROUND_HALF_UP));
			fee.setUnitPrice(stepQuoEntity.getUnitPrice());
			fee.setParam2(stepQuoEntity.getUnitPrice().toString()+"*"+fee.getQuantity().toString());
			fee.setParam3(stepQuoEntity.getId()+"");
			if(fee.getCost().compareTo(BigDecimal.ZERO) == 1){
				fee.setIsCalculated(CalculateState.Finish.getCode());
				fee.setCalcuMsg(CalculateState.Finish.getDesc());
				entity.setIsCalculated(CalculateState.Finish.getCode());
				entity.setRemark(entity.getRemark()+CalculateState.Finish.getDesc()+";");
				//XxlJobLogger.log("-->"+entity.getId()+"计算成功，费用【{0}】",fee.getCost());
			}else{
				fee.setIsCalculated(CalculateState.Sys_Error.getCode());
				fee.setCalcuMsg("未计算出金额");
				//XxlJobLogger.log("-->"+entity.getId()+"计算不成功，费用【0】");
			}
		}catch(Exception ex){
			fee.setIsCalculated(CalculateState.Sys_Error.getCode());
			fee.setCalcuMsg("计算错误");
			//XxlJobLogger.log("-->"+entity.getId()+"系统异常，费用【0】");
		}
		
		
	}
	
	@Override
	public void calcuForContract(BizOutstockPackmaterialEntity entity,FeesReceiveStorageEntity fee){
		ContractQuoteQueryInfoVo queryVo = getCtConditon(entity);
		contractCalcuService.calcuForContract(entity, fee, taskVo, errorMap, queryVo,cbiVo);
		if("succ".equals(errorMap.get("success").toString())){
			if(fee.getCost().compareTo(BigDecimal.ZERO) == 1){
				fee.setIsCalculated(CalculateState.Finish.getCode());
				fee.setCalcuMsg(CalculateState.Finish.getDesc());
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
	public ContractQuoteQueryInfoVo getCtConditon(BizOutstockPackmaterialEntity entity) {
		ContractQuoteQueryInfoVo queryVo = new ContractQuoteQueryInfoVo();
		queryVo.setCustomerId(entity.getCustomerId());
		queryVo.setBizTypeCode(ContractBizTypeEnum.STORAGE.getCode());
		queryVo.setSubjectCode(subjectCode);
		queryVo.setCurrentTime(entity.getCreateTime());
		queryVo.setWarehouseCode(entity.getWarehouseCode());
		return queryVo;

	}
	
	@Override
	public void updateBatch(List<BizOutstockPackmaterialEntity> bizList,List<FeesReceiveStorageEntity> feeList) {
		feesReceiveStorageService.updateBatch(feeList);
	}


	

	
	
}
