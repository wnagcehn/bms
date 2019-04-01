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

import com.alibaba.dubbo.common.utils.CollectionUtils;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import com.jiuyescm.bms.asyn.vo.BmsCalcuTaskVo;
import com.jiuyescm.bms.base.dict.api.IMaterialDictService;
import com.jiuyescm.bms.base.dictionary.entity.SystemCodeEntity;
import com.jiuyescm.bms.base.dictionary.repository.ISystemCodeRepository;
import com.jiuyescm.bms.base.group.service.IBmsGroupSubjectService;
import com.jiuyescm.bms.biz.dispatch.entity.BizDispatchBillEntity;
import com.jiuyescm.bms.biz.storage.entity.BizOutstockPackmaterialEntity;
import com.jiuyescm.bms.calcu.base.CalcuTaskListener;
import com.jiuyescm.bms.calculate.api.IBmsCalcuService;
import com.jiuyescm.bms.calculate.vo.BmsFeesQtyVo;
import com.jiuyescm.bms.calculate.vo.CalcuContractVo;
import com.jiuyescm.bms.common.enumtype.CalculateState;
import com.jiuyescm.bms.common.enumtype.TemplateTypeEnum;
import com.jiuyescm.bms.general.entity.FeesReceiveStorageEntity;
import com.jiuyescm.bms.general.service.IFeesReceiveStorageService;
import com.jiuyescm.bms.general.service.IPriceContractInfoService;
import com.jiuyescm.bms.general.service.IStorageQuoteFilterService;
import com.jiuyescm.bms.general.service.SequenceService;
import com.jiuyescm.bms.quotation.contract.entity.PriceContractInfoEntity;
import com.jiuyescm.bms.quotation.contract.entity.PriceContractItemEntity;
import com.jiuyescm.bms.quotation.contract.repository.imp.IPriceContractDao;
import com.jiuyescm.bms.quotation.contract.repository.imp.IPriceContractItemRepository;
import com.jiuyescm.bms.quotation.storage.entity.PriceMaterialQuotationEntity;
import com.jiuyescm.bms.quotation.storage.repository.IPriceMaterialQuotationRepository;
import com.jiuyescm.bms.quotation.transport.repository.IGenericTemplateRepository;
import com.jiuyescm.bms.receivable.dispatch.service.IBizDispatchBillService;
import com.jiuyescm.bms.receivable.storage.service.IBizOutstockPackmaterialService;
import com.jiuyescm.cfm.common.JAppContext;
import com.jiuyescm.common.utils.DoubleUtil;
import com.jiuyescm.contract.quote.api.IContractQuoteInfoService;
import com.jiuyescm.contract.quote.vo.ContractBizTypeEnum;
import com.jiuyescm.contract.quote.vo.ContractQuoteQueryInfoVo;
import com.jiuyescm.mdm.customer.api.IPubMaterialInfoService;
import com.jiuyescm.mdm.customer.vo.PubMaterialInfoVo;

public class MaterialUseCalcuBase extends CalcuTaskListener<BizOutstockPackmaterialEntity,FeesReceiveStorageEntity>{

	private Logger logger = LoggerFactory.getLogger(MaterialUseCalcuBase.class);
	
	@Autowired private IBizOutstockPackmaterialService bizOutstockPackmaterialService;
	@Autowired private IContractQuoteInfoService contractQuoteInfoService;
	@Autowired private IBizDispatchBillService bizDispatchBillService;
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
	@Autowired private IBmsCalcuService bmsCalcuService;

	
	protected Map<String,PubMaterialInfoVo> materialMap = null;
	protected Map<String,SystemCodeEntity> noFeesMap=null;
	protected List<SystemCodeEntity> noCarrierMap=null;

	@Override
	protected void initDict() {
		// TODO Auto-generated method stub
		materialMap=queryAllMaterial();
		noFeesMap=queryNoFees();
		noCarrierMap=queryNoCarrier();
	}
	
	
	@Override
	protected List<BizOutstockPackmaterialEntity> queryBillList(Map<String, Object> map) {
		List<BizOutstockPackmaterialEntity> bizList = bizOutstockPackmaterialService.query(map);
		return bizList;
	}
	
	@Override
	protected void calcuForBms(BmsCalcuTaskVo vo,BizOutstockPackmaterialEntity entity,FeesReceiveStorageEntity feeEntity){
		feeEntity.setSubjectCode(vo.getSubjectCode());
		PriceContractInfoEntity contractEntity = null;
		
		//查询合同
		String customerId=entity.getCustomerId();
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
		
		
		//商家耗材报价
		map = new HashMap<String, Object>();
		map.put("contractCode", contractEntity.getContractCode());
		map.put("subjectId",SubjectId);
		map.put("materialCode",entity.getConsumerMaterialCode());
		//map.put("warehouseId", entity.getWarehouseCode());
		List<PriceMaterialQuotationEntity> list=priceMaterialQuotationRepository.queryMaterialQuatationByContract(map);
		
		if(list==null||list.size()==0){
			//XxlJobLogger.log("-->"+entity.getId()+"报价未配置");
			entity.setIsCalculated(CalculateState.Quote_Miss.getCode());
			feeEntity.setIsCalculated(CalculateState.Quote_Miss.getCode());
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
			feeEntity.setIsCalculated(CalculateState.Quote_Miss.getCode());
			entity.setRemark(entity.getRemark()+"报价未配置;");
			feeEntity.setCalcuMsg("报价未配置");
			return;
		}
		//XxlJobLogger.log("筛选后得到的报价结果【{0}】",JSONObject.fromObject(stepQuoEntity));
					
		feeEntity.setCost(new BigDecimal(stepQuoEntity.getUnitPrice()*feeEntity.getQuantity()).setScale(2,BigDecimal.ROUND_HALF_UP));
		feeEntity.setUnitPrice(stepQuoEntity.getUnitPrice());
		feeEntity.setParam2(stepQuoEntity.getUnitPrice().toString()+"*"+feeEntity.getQuantity().toString());
		feeEntity.setParam3(stepQuoEntity.getId()+"");
		if(feeEntity.getCost().compareTo(BigDecimal.ZERO) == 1){
			feeEntity.setIsCalculated(CalculateState.Finish.getCode());
			entity.setIsCalculated(CalculateState.Finish.getCode());
			entity.setRemark(entity.getRemark()+CalculateState.Finish.getDesc()+";");
			//XxlJobLogger.log("-->"+entity.getId()+"计算成功，费用【{0}】",feeEntity.getCost());
		}else{
			feeEntity.setIsCalculated(CalculateState.Quote_Miss.getCode());
			entity.setIsCalculated(CalculateState.Finish.getCode());
			//XxlJobLogger.log("-->"+entity.getId()+"计算不成功，费用【0】");
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
	protected FeesReceiveStorageEntity initFeeEntity(BmsCalcuTaskVo vo,BizOutstockPackmaterialEntity entity) {
		//打印业务数据日志
		FeesReceiveStorageEntity storageFeeEntity = new FeesReceiveStorageEntity();
		storageFeeEntity.setQuantity(DoubleUtil.isBlank(entity.getAdjustNum())?entity.getNum():entity.getAdjustNum());//计费数量
		if(materialMap!=null && materialMap.containsKey(entity.getConsumerMaterialCode())){
			String materialType=materialMap.get(entity.getConsumerMaterialCode()).getMaterialType();
			if("干冰".equals(materialType)){
				storageFeeEntity.setQuantity(DoubleUtil.isBlank(entity.getAdjustNum())?entity.getWeight():entity.getAdjustNum());//计费数量
			}else{
				storageFeeEntity.setQuantity(DoubleUtil.isBlank(entity.getAdjustNum())?entity.getNum():entity.getAdjustNum());//计费重量
			}
		}
		
		storageFeeEntity.setWeight(entity.getWeight());					//设置重量

		
		storageFeeEntity.setStatus("0");								//状态
		storageFeeEntity.setBizId(String.valueOf(entity.getId()));						//业务数据主键
		entity.setCalculateTime(JAppContext.currentTimestamp());
		storageFeeEntity.setCalculateTime(JAppContext.currentTimestamp());
		storageFeeEntity.setCost(new BigDecimal(0));					//入仓金额
		storageFeeEntity.setFeesNo(entity.getFeesNo());
		storageFeeEntity.setParam1(TemplateTypeEnum.COMMON.getCode());
		storageFeeEntity.setDelFlag("0");	
		return storageFeeEntity;
	}



	@Override
	protected boolean isNoExe(BizOutstockPackmaterialEntity entity,FeesReceiveStorageEntity feeEntity,Map<String, Object> errorMap) {
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
					entity.setIsCalculated(CalculateState.No_Exe.getCode());
					feeEntity.setIsCalculated(CalculateState.No_Exe.getCode());
					entity.setRemark(entity.getRemark()+String.format("该物流商使用的耗材不收钱", entity.getId())+";");
					return true;
				}
			}
			
		}
		
		//指定耗材不计费
		if(entity.getConsumerMaterialCode()!=null && noFeesMap.containsKey(entity.getConsumerMaterialCode())){
				//XxlJobLogger.log("-->"+entity.getId()+String.format("不计算耗材使用费的耗材", entity.getId()));
				entity.setIsCalculated(CalculateState.No_Exe.getCode());
				feeEntity.setIsCalculated(CalculateState.No_Exe.getCode());
				entity.setRemark(entity.getRemark()+String.format("耗材不收钱", entity.getId())+";");
				return true;
		}
		return false;
	}

	@Override
	protected ContractQuoteQueryInfoVo getCtConditon(BmsCalcuTaskVo vo,BizOutstockPackmaterialEntity entity) {
		ContractQuoteQueryInfoVo queryVo = new ContractQuoteQueryInfoVo();
		queryVo.setCustomerId(entity.getCustomerId());
		queryVo.setBizTypeCode(ContractBizTypeEnum.STORAGE.getCode());
		queryVo.setSubjectCode(vo.getSubjectCode());
		queryVo.setCurrentTime(entity.getCreateTime());
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

	/**
	 * 查询耗材编码-耗材映射
	 * @return
	 */
	public Map<String,PubMaterialInfoVo> queryAllMaterial(){
		Map<String,Object> condition=Maps.newHashMap();
		List<PubMaterialInfoVo> tmscodels = pubMaterialInfoService.queryList(condition);
		Map<String,PubMaterialInfoVo> map=Maps.newLinkedHashMap();
		for(PubMaterialInfoVo materialVo:tmscodels){
			if(!StringUtils.isBlank(materialVo.getBarcode())){
				map.put(materialVo.getBarcode().trim(),materialVo);
			}
		}
		return map;
	}
	
	/**
	 * 查询不计算耗材
	 * @return
	 */
	public Map<String,SystemCodeEntity> queryNoFees(){
		Map<String,Object> condition=Maps.newHashMap();
		condition.put("typeCode", "NO_FEES_MATERIAL");
		PageInfo<SystemCodeEntity> page = systemCodeRepository.query(condition, 1, 999999);
		Map<String,SystemCodeEntity> map=new HashMap<String, SystemCodeEntity>();
		if(page.getList()!=null||page.getList().size()>0){
			List<SystemCodeEntity> list = page.getList();
			for(SystemCodeEntity entity:list){
					map.put(entity.getCode(),entity);
			}
		}
		return map;
	}
	
	/**
	 * 查询不计算的物流商
	 * @return
	 */
	public List<SystemCodeEntity> queryNoCarrier(){
		List<SystemCodeEntity> list=new ArrayList<SystemCodeEntity>();
		Map<String,Object> condition=Maps.newHashMap();
		condition.put("typeCode", "CARRIER_FREE");
		PageInfo<SystemCodeEntity> page = systemCodeRepository.query(condition, 1, 999999);
		if(page.getList()!=null||page.getList().size()>0){
			list = page.getList();
		}
		return list;
	}

}
