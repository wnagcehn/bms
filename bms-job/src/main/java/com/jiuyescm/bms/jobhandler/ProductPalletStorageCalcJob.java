package com.jiuyescm.bms.jobhandler;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jiuyescm.bms.base.calcu.vo.CalcuReqVo;
import com.jiuyescm.bms.base.calcu.vo.CalcuResultVo;
import com.jiuyescm.bms.base.group.service.IBmsGroupCustomerService;
import com.jiuyescm.bms.base.group.service.IBmsGroupService;
import com.jiuyescm.bms.base.group.vo.BmsGroupVo;
import com.jiuyescm.bms.biz.storage.entity.BizProductPalletStorageEntity;
import com.jiuyescm.bms.calculate.base.IFeesCalcuService;
import com.jiuyescm.bms.chargerule.receiverule.entity.BillRuleReceiveEntity;
import com.jiuyescm.bms.common.enumtype.CalculateState;
import com.jiuyescm.bms.common.enumtype.TemplateTypeEnum;
import com.jiuyescm.bms.general.entity.FeesReceiveStorageEntity;
import com.jiuyescm.bms.general.service.IFeesReceiveStorageService;
import com.jiuyescm.bms.general.service.IPriceContractInfoService;
import com.jiuyescm.bms.general.service.IStandardReqVoService;
import com.jiuyescm.bms.general.service.ISystemCodeService;
import com.jiuyescm.bms.general.service.SequenceService;
import com.jiuyescm.bms.quotation.contract.entity.PriceContractInfoEntity;
import com.jiuyescm.bms.quotation.contract.entity.PriceContractItemEntity;
import com.jiuyescm.bms.quotation.contract.repository.imp.IPriceContractItemRepository;
import com.jiuyescm.bms.quotation.storage.entity.PriceGeneralQuotationEntity;
import com.jiuyescm.bms.quotation.storage.entity.PriceStepQuotationEntity;
import com.jiuyescm.bms.quotation.storage.repository.IPriceGeneralQuotationRepository;
import com.jiuyescm.bms.quotation.storage.repository.IPriceStepQuotationRepository;
import com.jiuyescm.bms.receivable.storage.service.IBizProductPalletStorageService;
import com.jiuyescm.bms.rule.receiveRule.repository.IReceiveRuleRepository;
import com.jiuyescm.cfm.common.JAppContext;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.JobHander;
import com.xxl.job.core.log.XxlJobLogger;

@JobHander(value="productPalletStorageCalcJob")
@Service
public class ProductPalletStorageCalcJob extends CommonCalcJob<BizProductPalletStorageEntity,FeesReceiveStorageEntity> {

	@Autowired private IBizProductPalletStorageService bizProductPalletStorageService;
	@Autowired private SequenceService sequenceService;
	@Autowired private IPriceContractInfoService jobPriceContractInfoService;
	@Autowired private IFeesReceiveStorageService feesReceiveStorageService;
	@Autowired private IPriceGeneralQuotationRepository priceGeneralQuotationRepository;
	@Autowired private IPriceStepQuotationRepository  repository;
	@Autowired private IReceiveRuleRepository receiveRuleRepository;
	@Autowired private IFeesCalcuService feesCalcuService;
	@Autowired private IStandardReqVoService standardReqVoServiceImpl;
	@Autowired private IPriceContractItemRepository priceContractItemRepository;
	@Autowired private ISystemCodeService systemCodeService;
	@Autowired private IBmsGroupService bmsGroupService;
	@Autowired private IBmsGroupCustomerService bmsGroupCustomerService;
	
	private String BizTypeCode = "STORAGE"; //仓储费编码
	private String SubjectId = "wh_product_storage";		//费用类型-商品按托存储费 1002原编码 wh_product_pallet_storage
	private String quoType = "C";//默认使用常规报价
	
	Map<String,List<PriceGeneralQuotationEntity>> mapCusPrice=null;
	Map<String,List<PriceStepQuotationEntity>> mapCusStepPrice=null;
	Map<String,PriceContractInfoEntity> mapContact=null;
	Map<String,BillRuleReceiveEntity> mapRule=null;
	List<String> cusList=null;
	String priceType="";
	
	@Override
	public ReturnT<String> execute(String... params) throws Exception {
	    XxlJobLogger.log("productPalletStorageCalcJob start.");
		return super.CalcJob(params);
	}
	
	@Override
	protected List<BizProductPalletStorageEntity> queryBillList(Map<String, Object> map) {
		/*return bizProductPalletStorageService.query(map);*/
		
		long operateTime = System.currentTimeMillis();
		List<String> feesNos = new ArrayList<String>();
		Map<String, Object> feesMap = new HashMap<String, Object>();
		List<BizProductPalletStorageEntity> bizList = bizProductPalletStorageService.query(map);
		if(bizList == null || bizList.size() == 0){
			
		}
		else{
			for (BizProductPalletStorageEntity entity : bizList) {
				if(StringUtils.isNotEmpty(entity.getFeesNo())){
					feesNos.add(entity.getFeesNo());
				}
				else{
					entity.setFeesNo(sequenceService.getBillNoOne(FeesReceiveStorageEntity.class.getName(), "STO", "0000000000"));
				}
			}
			try{
				if(feesNos.size()>0){
					feesMap.put("feesNos", feesNos);
					feesReceiveStorageService.deleteBatch(feesMap);
					long current = System.currentTimeMillis();// 系统开始时间
					XxlJobLogger.log("批量删除费用成功 耗时【{0}】毫秒 删除条数【{1}】",(current-operateTime),feesNos.size());
				}
			}
			catch(Exception ex){
				XxlJobLogger.log("批量删除费用失败-- {1}",ex.getMessage());
			}
		}
		return bizList;
		
	}
    /**
     * 初始化Map　等list 列表
     */
	@Override
	protected void initConf(List<BizProductPalletStorageEntity> billList) {
		mapCusStepPrice=new HashMap<String,List<PriceStepQuotationEntity>>();
		mapCusPrice=new HashMap<String,List<PriceGeneralQuotationEntity>>();
		mapContact=new HashMap<String,PriceContractInfoEntity>();
		mapRule=new HashMap<String,BillRuleReceiveEntity>();
		//指定的商家
		Map<String,Object> map= new HashMap<String, Object>();
		map.put("groupCode", "customer_unit");
		map.put("bizType", "group_customer");
		BmsGroupVo bmsGroup=bmsGroupService.queryOne(map);
		if(bmsGroup!=null){
			cusList=bmsGroupCustomerService.queryCustomerByGroupId(bmsGroup.getId());
		}
	}

	@Override
	protected void calcuService(BizProductPalletStorageEntity entity,
			List<FeesReceiveStorageEntity> feesList) {
		
		FeesReceiveStorageEntity storageFeeEntity=initFeeEntity(entity);
		try{
			entity.setCalculateTime(JAppContext.currentTimestamp());
			storageFeeEntity.setCalculateTime(entity.getCalculateTime());
			String customerId=entity.getCustomerId();
			CalcuReqVo reqVo = new CalcuReqVo();
			//如果有调整托数按照调整托数算钱  185需求
			entity.setPalletNum(entity.getAdjustPalletNum()==null?entity.getPalletNum():entity.getAdjustPalletNum());
			reqVo.setBizData(entity);
			BillRuleReceiveEntity ruleEntity=mapRule.get(customerId);
			reqVo.setRuleNo(ruleEntity.getQuotationNo());
			reqVo.setRuleStr(ruleEntity.getRule());
			storageFeeEntity.setRuleNo(ruleEntity.getQuotationNo());
			
			if(mapCusPrice.containsKey(entity.getCustomerId())){
				reqVo.setQuoEntites(mapCusPrice.get(customerId));
			}
			if(mapCusStepPrice.containsKey(customerId)){
				reqVo.setQuoEntites(mapCusStepPrice.get(customerId));
			}
			
		
			long start = System.currentTimeMillis();// 系统开始时间
			long current = 0l;// 当前系统时间
			CalcuResultVo resultVo = feesCalcuService.FeesCalcuService(reqVo);
			current= System.currentTimeMillis();
			if("succ".equals(resultVo.getSuccess())){
				XxlJobLogger.log("调用规则引擎成功   耗时【{0}】毫秒  费用【{1}】 ",(current - start),resultVo.getPrice());	
				switch(priceType){
				case "PRICE_TYPE_NORMAL":
					PriceGeneralQuotationEntity generalEntity=getgeneralEntityById(mapCusPrice.get(customerId),resultVo.getQuoId());
					storageFeeEntity.setUnitPrice(generalEntity.getUnitPrice());
					storageFeeEntity.setCost(resultVo.getPrice());
					break;
				case "PRICE_TYPE_STEP":
					PriceStepQuotationEntity stepQuoEntity=getStepQuotationById(mapCusStepPrice.get(customerId),resultVo.getQuoId());
					storageFeeEntity.setUnitPrice(stepQuoEntity.getUnitPrice());
					storageFeeEntity.setCost(resultVo.getPrice());
					break;
					default:
						break;
				}
				storageFeeEntity.setParam2(resultVo.getMethod());//
				storageFeeEntity.setParam3(resultVo.getQuoId());
				storageFeeEntity.setParam4(priceType);
				storageFeeEntity.setBizType(entity.getextattr1());//判断是否是遗漏数据
				entity.setRemark("计算成功");
				entity.setIsCalculated(CalculateState.Finish.getCode());
				storageFeeEntity.setIsCalculated(CalculateState.Finish.getCode());
				feesList.add(storageFeeEntity);
			}else{
				XxlJobLogger.log("调用规则引擎失败   耗时【{0}】毫秒   ",(current - start));
				String ruleNo="";
				if(ruleEntity!=null){
					ruleNo=ruleEntity.getQuotationNo();
				}
				XxlJobLogger.log("费用["+ruleNo+"]计算失败--"+resultVo.getMsg());
				entity.setIsCalculated(CalculateState.Quote_Miss.getCode());
				storageFeeEntity.setIsCalculated(CalculateState.Quote_Miss.getCode());
				entity.setRemark("费用计算失败["+ruleNo+"]:"+resultVo.getMsg());
				feesList.add(storageFeeEntity);
			}
			
		}catch(Exception ex){
			XxlJobLogger.log("费用计算异常--"+ex.getMessage());
			entity.setIsCalculated(CalculateState.Sys_Error.getCode());
			storageFeeEntity.setIsCalculated(CalculateState.Sys_Error.getCode());
			entity.setRemark("费用计算异常:"+ex.getMessage());
			feesList.add(storageFeeEntity);
		}
	}

	/**
	 * 保存数据
	 */
	@Override
	protected void saveBatchData(List<BizProductPalletStorageEntity> billList,
			List<FeesReceiveStorageEntity> feesList) {
		long start = System.currentTimeMillis();// 系统开始时间
		long current = 0l;// 当前系统时间
		bizProductPalletStorageService.updateBatch(billList);
		current = System.currentTimeMillis();
		XxlJobLogger.log("更新业务数据耗时：【{0}】毫秒  ",(current - start));
		start = System.currentTimeMillis();// 系统开始时间
		for(FeesReceiveStorageEntity feeEntity:feesList){
			feesReceiveStorageService.Insert(feeEntity);
		}
		current = System.currentTimeMillis();
		XxlJobLogger.log("新增费用数据耗时：【{0}】毫秒",(current - start));
		
	}
    
    private FeesReceiveStorageEntity initFeeEntity(BizProductPalletStorageEntity entity){
		
		FeesReceiveStorageEntity storageFeeEntity = new FeesReceiveStorageEntity();
		
		storageFeeEntity.setCreator("system");
		storageFeeEntity.setCreateTime(entity.getStockTime());
		storageFeeEntity.setOperateTime(entity.getStockTime());
		storageFeeEntity.setCustomerId(entity.getCustomerId());		//商家ID
		storageFeeEntity.setCustomerName(entity.getCustomerName());		//商家名称
		storageFeeEntity.setWarehouseCode(entity.getWarehouseCode());	//仓库ID
		storageFeeEntity.setWarehouseName(entity.getWarehouseName());	//仓库名称
		storageFeeEntity.setCostType("FEE_TYPE_GENEARL");			//费用类别 FEE_TYPE_GENEARL-通用 FEE_TYPE_MATERIAL-耗材 FEE_TYPE_ADD-增值
		storageFeeEntity.setSubjectCode(SubjectId);		//费用科目
		storageFeeEntity.setOtherSubjectCode(SubjectId);
		storageFeeEntity.setProductType("");							//商品类型
		if(entity.getPalletNum()!=null){
			storageFeeEntity.setQuantity((new Double(entity.getAdjustPalletNum()==null?entity.getPalletNum():entity.getAdjustPalletNum())).intValue());//商品数量
		}
		storageFeeEntity.setStatus("0");			
		storageFeeEntity.setUnit("PALLETS");
		storageFeeEntity.setTempretureType(entity.getTemperatureTypeCode());//设置温度类型  zhangzw
		storageFeeEntity.setCost(new BigDecimal(0));	//入仓金额
		storageFeeEntity.setBizId(entity.getDataNum());//业务数据主键
		storageFeeEntity.setFeesNo(entity.getFeesNo());
		storageFeeEntity.setParam1(TemplateTypeEnum.COMMON.getCode());
		storageFeeEntity.setDelFlag("0");
		return storageFeeEntity;
	}

    private PriceStepQuotationEntity getStepQuotationById(
			List<PriceStepQuotationEntity> priceStepList, String priceId) {
		PriceStepQuotationEntity stepQuotationEntity=null;
		for(PriceStepQuotationEntity entity:priceStepList){
			if(entity.getId().toString().equals(priceId)){
				stepQuotationEntity=entity;
				break;
			}
		}
		return stepQuotationEntity;
	}

	private PriceGeneralQuotationEntity getgeneralEntityById(
			List<PriceGeneralQuotationEntity> priceGenerallist, String priceId) {
		PriceGeneralQuotationEntity generalentity=null;
		for(PriceGeneralQuotationEntity entity:priceGenerallist){
			if(entity.getId().toString().equals(priceId)){
				generalentity=entity;
				break;
			}
		}
		return generalentity;
	}
	/**
	 * 验证数据  初始化数据
	 */
	@Override
	protected boolean validateData(BizProductPalletStorageEntity entity,List<FeesReceiveStorageEntity> feesList) {
		
		XxlJobLogger.log("数据主键ID:【{0}】  ",entity.getId());
		entity.setCalculateTime(JAppContext.currentTimestamp());
		Map<String,Object> map=new HashMap<String,Object>();
		String customerId=entity.getCustomerId();
		FeesReceiveStorageEntity storageFeeEntity = initFeeEntity(entity);
		storageFeeEntity.setCalculateTime(JAppContext.currentTimestamp());
		long start = System.currentTimeMillis();// 系统开始时间
		long current = 0l;// 当前系统时间
		
		//如果商家已经按件收取存储费，则按托存储不计费
		if(cusList.size()>0 && cusList.contains(entity.getCustomerId())){
			XxlJobLogger.log("商家已经按件收取存储费,按托存储不计费");
			entity.setIsCalculated(CalculateState.No_Exe.getCode());
			storageFeeEntity.setIsCalculated(CalculateState.No_Exe.getCode());
			entity.setRemark("商家已经按件收取存储费,按托存储不计费");
			feesList.add(storageFeeEntity);
			return false;
		}
		
		/*验证商家是否合同存在*/
		PriceContractInfoEntity contractEntity=null;
		if(mapContact.containsKey(customerId)){
			contractEntity=mapContact.get(customerId);
		}else{
			Map<String,Object> aCondition=new HashMap<>();
			aCondition.put("customerid",customerId);
			aCondition.put("contractTypeCode", "CUSTOMER_CONTRACT");
		    contractEntity = jobPriceContractInfoService.queryContractByCustomer(aCondition);
		    mapContact.put(customerId, contractEntity);
		}
		if(contractEntity == null || StringUtils.isEmpty(contractEntity.getContractCode())){
			XxlJobLogger.log(String.format("未查询到有效合同  订单号【%s】--商家【%s】", entity.getId(),customerId));
			entity.setIsCalculated(CalculateState.Contract_Miss.getCode());
			storageFeeEntity.setIsCalculated(CalculateState.Contract_Miss.getCode());
			entity.setRemark("未查询到有效合同");
			feesList.add(storageFeeEntity);
			return false;
		}
		current = System.currentTimeMillis();
		XxlJobLogger.log("验证合同   耗时【{0}】毫秒  合同编号 【{1}】",(current - start),contractEntity.getContractCode());
		
		//----验证签约服务
		start = System.currentTimeMillis();// 系统开始时间
		Map<String,Object> contractItems_map=new HashMap<String,Object>();
		contractItems_map.put("contractCode", contractEntity.getContractCode());
		contractItems_map.put("subjectId", SubjectId);
		List<PriceContractItemEntity> contractItems = priceContractItemRepository.query(contractItems_map);
		if(contractItems == null || contractItems.size() == 0 || StringUtils.isEmpty(contractItems.get(0).getTemplateId())) {
			XxlJobLogger.log("未签约服务  订单号【{0}】--商家【{1}】", entity.getId(),entity.getCustomerId());
			entity.setIsCalculated(CalculateState.Contract_Miss.getCode());
			storageFeeEntity.setIsCalculated(CalculateState.Contract_Miss.getCode());
			entity.setRemark("未签约服务");
			feesList.add(storageFeeEntity);
			return false;
		}
		current = System.currentTimeMillis();
		XxlJobLogger.log("验证签约服务耗时：【{0}】毫秒  ",(current - start));			
		
		
		
		start = System.currentTimeMillis();// 系统开始时间
		/*验证报价 报价*/
		List<PriceGeneralQuotationEntity> priceGenerallist=null;
		if(!mapCusPrice.containsKey(customerId)){
			map.clear();
			map.put("contractCode", contractEntity.getContractCode());
			map.put("subjectId",SubjectId);
			priceGenerallist=priceGeneralQuotationRepository.queryPriceGeneralByContract(map);
			if(priceGenerallist!=null){
				mapCusPrice.put(customerId, priceGenerallist);
			}
		}else{
			priceGenerallist=mapCusPrice.get(customerId);
		}
		if(priceGenerallist==null||priceGenerallist.size()==0){
			XxlJobLogger.log("报价未配置");
			entity.setIsCalculated(CalculateState.Quote_Miss.getCode());
			storageFeeEntity.setIsCalculated(CalculateState.Quote_Miss.getCode());
			entity.setRemark("报价未配置");
			feesList.add(storageFeeEntity);
			return false;
		}
		priceType=priceGenerallist.get(0).getPriceType();
		List<PriceStepQuotationEntity> priceStepList=null;
		if(priceType.equals("PRICE_TYPE_STEP")){//阶梯价格
			//寻找阶梯报价
			if(!mapCusStepPrice.containsKey(customerId)){
				map.clear();
				map.put("quotationId", priceGenerallist.get(0).getId());
				priceStepList=repository.queryPriceStepByQuatationId(map);
				mapCusStepPrice.put(customerId,priceStepList);
			}else{
				priceStepList=mapCusStepPrice.get(customerId);
			}
			if(priceStepList==null||priceStepList.size()==0){
				XxlJobLogger.log("阶梯报价未配置");
				entity.setIsCalculated(CalculateState.Quote_Miss.getCode());
				storageFeeEntity.setIsCalculated(CalculateState.Quote_Miss.getCode());
				entity.setRemark("阶梯报价未配置");
				feesList.add(storageFeeEntity);
				return  false;
			}
		}else if(priceType.equals("PRICE_TYPE_NORMAL")){//一口价
			
		}else{//报价类型缺失
			XxlJobLogger.log("报价类型未知");
			entity.setIsCalculated(CalculateState.Quote_Miss.getCode());
			storageFeeEntity.setIsCalculated(CalculateState.Quote_Miss.getCode());
			entity.setRemark("报价【"+priceGenerallist.get(0).getQuotationNo()+"】类型未知");
			feesList.add(storageFeeEntity);
			return  false;
		}
		current = System.currentTimeMillis();
		XxlJobLogger.log("验证报价耗时：【{0}】毫秒  ",(current - start));
		start = System.currentTimeMillis();// 系统开始时间
		/*查找商家 规则*/
		map.clear();
		BillRuleReceiveEntity ruleEntity=null;
		if(mapRule.containsKey(customerId)){
			ruleEntity=mapRule.get(customerId);
		}else{
			map.put("customerid",customerId);
			map.put("subjectId", SubjectId);
		    ruleEntity=receiveRuleRepository.queryByCustomerId(map);
		    mapRule.put(customerId, ruleEntity);
		}
		
		if(ruleEntity == null){
			XxlJobLogger.log("规则未配置");
			entity.setIsCalculated(CalculateState.Quote_Miss.getCode());
			storageFeeEntity.setIsCalculated(CalculateState.Quote_Miss.getCode());
			entity.setRemark("规则未配置");
			feesList.add(storageFeeEntity);
			return  false;
		}
		current = System.currentTimeMillis();
		XxlJobLogger.log("验证规则耗时：【{0}】毫秒  ",(current - start));
		return true;
	}

	@Override
	protected void calcuStandardService(List<BizProductPalletStorageEntity> billList) {
		for (BizProductPalletStorageEntity entity : billList) {
			if(CalculateState.Quote_Miss.getCode().equals(entity.getIsCalculated())){
				try{
					calcu(entity);
				}
				catch(Exception ex){
					XxlJobLogger.log("采用【标准报价】计算异常 "+ ex.getMessage());	
				}
			}
		}
	}
	
	private void calcu(BizProductPalletStorageEntity entity){
		long start = System.currentTimeMillis();// 系统开始时间
		
		CalcuReqVo reqVo = standardReqVoServiceImpl.getStorageReqVo(SubjectId);
		if("true".equals(reqVo.getParams().get("succ"))){
			XxlJobLogger.log("消息【{0}】 规则编号【{1}】",reqVo.getParams().get("msg").toString(),reqVo.getRuleNo());	
			reqVo.setBizData(entity);
			CalcuResultVo resultVo = feesCalcuService.FeesCalcuService(reqVo);
			if("succ".equals(resultVo.getSuccess())){
				FeesReceiveStorageEntity storageFeeEntity = new FeesReceiveStorageEntity();
				storageFeeEntity.setFeesNo(entity.getFeesNo());
				storageFeeEntity.setParam1(TemplateTypeEnum.STANDARD.getCode());
				storageFeeEntity.setParam2(resultVo.getMethod());//
				storageFeeEntity.setParam3(resultVo.getQuoId());
				entity.setRemark("计算成功");
				entity.setIsCalculated(CalculateState.Finish.getCode());
				storageFeeEntity.setIsCalculated(CalculateState.Finish.getCode());
				storageFeeEntity.setCost(resultVo.getPrice());
				feesReceiveStorageService.updateOne(storageFeeEntity);
			}
			else{
				entity.setRemark("【标准报价】费用计算失败:"+resultVo.getMsg());
			}
		}
		else{
			XxlJobLogger.log(reqVo.getParams().get("msg").toString());	
			entity.setRemark((String) reqVo.getParams().get("msg"));
		}
		bizProductPalletStorageService.update(entity);
		long current = System.currentTimeMillis();;// 当前系统时间
		XxlJobLogger.log("【标准报价】调用规则引擎   耗时【{0}】毫秒  费用【{1}】 ",(current - start));	
	}

}
