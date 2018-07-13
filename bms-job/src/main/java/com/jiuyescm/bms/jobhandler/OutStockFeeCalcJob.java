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
import com.jiuyescm.bms.biz.storage.entity.BizOutstockMasterEntity;
import com.jiuyescm.bms.calculate.base.IFeesCalcuService;
import com.jiuyescm.bms.chargerule.receiverule.entity.BillRuleReceiveEntity;
import com.jiuyescm.bms.common.enumtype.CalculateState;
import com.jiuyescm.bms.common.enumtype.TemplateTypeEnum;
import com.jiuyescm.bms.general.entity.FeesReceiveStorageEntity;
import com.jiuyescm.bms.general.service.IFeesReceiveStorageService;
import com.jiuyescm.bms.general.service.IPriceContractInfoService;
import com.jiuyescm.bms.general.service.IStandardReqVoService;
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
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.JobHander;
import com.xxl.job.core.log.XxlJobLogger;

@JobHander(value="outStockFeeCalcJob")
@Service
public class OutStockFeeCalcJob extends CommonCalcJob<BizOutstockMasterEntity,FeesReceiveStorageEntity>{

	@Autowired private IBizOutstockMasterService bizOutstockMasterService;
	@Autowired private IPriceContractInfoService jobPriceContractInfoService;
	@Autowired private SequenceService sequenceService;
	@Autowired private IFeesReceiveStorageService feesReceiveStorageService;
	@Autowired private IPriceGeneralQuotationRepository priceGeneralQuotationRepository;
	@Autowired private IPriceStepQuotationRepository  repository;
	@Autowired private IReceiveRuleRepository receiveRuleRepository;
	@Autowired private IFeesCalcuService feesCalcuService;
	@Autowired private IPriceContractItemRepository priceContractItemRepository;
	@Autowired private IStandardReqVoService standardReqVoServiceImpl;
	
	
	private String SubjectId = "wh_b2c_work";		//费用类型-B2C订单操作费 1004原编码
	private String quoType = "C";//默认使用常规报价
	private String standardCustID = "001";
	
	Map<String,List<PriceGeneralQuotationEntity>> mapCusPrice=null;
	Map<String,List<PriceStepQuotationEntity>> mapCusStepPrice=null;
	Map<String,PriceContractInfoEntity> mapContact=null;
	Map<String,BillRuleReceiveEntity> mapRule=null;
	String priceType="";
	
	@Override
	public ReturnT<String> execute(String... params) throws Exception {
		XxlJobLogger.log("outStockFeeCalcJob start.");
		return super.CalcJob(params);
	}
	
	@Override
	protected List<BizOutstockMasterEntity> queryBillList(Map<String, Object> map) {

		long operateTime = System.currentTimeMillis();
		List<String> feesNos = new ArrayList<String>();
		Map<String, Object> feesMap = new HashMap<String, Object>();
		List<BizOutstockMasterEntity> bizList = bizOutstockMasterService.query(map);
		if(bizList == null || bizList.size() == 0){
			
		}
		else{
			for (BizOutstockMasterEntity entity : bizList) {
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

	@Override
	protected void initConf(List<BizOutstockMasterEntity> billList) {
		mapCusStepPrice=new HashMap<String,List<PriceStepQuotationEntity>>();
		mapCusPrice=new HashMap<String,List<PriceGeneralQuotationEntity>>();
		mapContact=new HashMap<String,PriceContractInfoEntity>();
		mapRule=new HashMap<String,BillRuleReceiveEntity>();
	}

	@Override
	protected void calcuService(BizOutstockMasterEntity entity,List<FeesReceiveStorageEntity> feesList) {

		FeesReceiveStorageEntity storageFeeEntity=initFeeEntity(entity);
		try{
			entity.setCalculateTime(JAppContext.currentTimestamp());
			storageFeeEntity.setCalculateTime(entity.getCalculateTime());
			String customerId=entity.getCustomerid();
			BillRuleReceiveEntity ruleEntity=mapRule.get(customerId);
			
			CalcuReqVo reqVo = new CalcuReqVo();
			if(null!=entity.getAdjustBoxnum()){//如果调整的不为空 则调整算钱
				entity.setBoxnum(entity.getAdjustBoxnum());
			}
			//原编码1007
			if("wh_b2b_work".equals(SubjectId)&&null!=entity.getBoxnum()){
				storageFeeEntity.setQuantity(entity.getBoxnum());
			}
			reqVo.setBizData(entity);
			reqVo.setRuleNo(ruleEntity.getQuotationNo());
			reqVo.setRuleStr(ruleEntity.getRule());
			storageFeeEntity.setRuleNo(ruleEntity.getQuotationNo());
			if(mapCusPrice.containsKey(customerId)){
				reqVo.setQuoEntites(mapCusPrice.get(customerId));
			}
			if(mapCusStepPrice.containsKey(customerId)){
				reqVo.setQuoEntites(mapCusStepPrice.get(customerId));
			}
			
			long start = System.currentTimeMillis();// 系统开始时间
			long current = 0l;// 当前系统时间
			CalcuResultVo resultVo = feesCalcuService.FeesCalcuService(reqVo);
			current = System.currentTimeMillis();
			if("succ".equals(resultVo.getSuccess())){
				XxlJobLogger.log("调用规则引擎   耗时【{0}】毫秒  费用【{1}】 ",(current - current),resultVo.getPrice());	
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
				storageFeeEntity.setBizType(entity.getextattr1());//用于判断是否是遗漏数据
				entity.setRemark("计算成功");
				entity.setIsCalculated(CalculateState.Finish.getCode());
				storageFeeEntity.setIsCalculated(CalculateState.Finish.getCode());
				feesList.add(storageFeeEntity);
			}else{
				XxlJobLogger.log("调用规则引擎失败   耗时【{0}】毫秒   ",(current - start));
				XxlJobLogger.log("费用计算失败--"+resultVo.getMsg());
				entity.setIsCalculated(CalculateState.Quote_Miss.getCode());
				storageFeeEntity.setIsCalculated(CalculateState.Quote_Miss.getCode());
				entity.setRemark("费用计算失败:"+resultVo.getMsg());
				feesList.add(storageFeeEntity);
			}
			
		}catch(Exception ex){
			entity.setIsCalculated(CalculateState.Sys_Error.getCode());
			storageFeeEntity.setIsCalculated(CalculateState.Sys_Error.getCode());
			entity.setRemark("费用计算异常:"+ex.getMessage());
			feesList.add(storageFeeEntity);
		}
	}

	/**
	 * 批量保存数据
	 */
	@Override
	protected void saveBatchData(List<BizOutstockMasterEntity> billList,
			List<FeesReceiveStorageEntity> feesList) {
		long start = System.currentTimeMillis();// 系统开始时间
		long current = 0l;// 当前系统时间
		bizOutstockMasterService.updateOutstockBatch(billList);
	    current = System.currentTimeMillis();
	    XxlJobLogger.log("更新业务数据耗时：【{0}】毫秒",(current - start));
	    start = System.currentTimeMillis();// 系统开始时间
	    feesReceiveStorageService.InsertBatch(feesList);
		/*for(FeesReceiveStorageEntity feeEntity:feesList){
			feesReceiveStorageService.Insert(feeEntity);
		}*/
		current = System.currentTimeMillis();
		XxlJobLogger.log("新增费用数据耗时：【{0}】毫秒  ",(current - start));
	}

	@Override
	protected boolean validateData(BizOutstockMasterEntity entity,
			List<FeesReceiveStorageEntity> feesList) {
		XxlJobLogger.log("数据主键ID:【{0}】  ",entity.getId());
		entity.setCalculateTime(JAppContext.currentTimestamp());
		Map<String,Object> map=new HashMap<String,Object>();
		String customerId=entity.getCustomerid();
		FeesReceiveStorageEntity storageFeeEntity = initFeeEntity(entity);
		storageFeeEntity.setCalculateTime(JAppContext.currentTimestamp());
		long start = System.currentTimeMillis();// 系统开始时间
		long current = 0l;// 当前系统时间
		entity.setCalculateTime(JAppContext.currentTimestamp());
		PriceContractInfoEntity contractEntity =null;
		if(mapContact.containsKey(entity.getCustomerid())){
			contractEntity=mapContact.get(entity.getCustomerid());
		}else{
			Map<String,Object> aCondition=new HashMap<>();
			aCondition.put("customerid", entity.getCustomerid());
			aCondition.put("contractTypeCode", "CUSTOMER_CONTRACT");
		    contractEntity = jobPriceContractInfoService.queryContractByCustomer(aCondition);
		    mapContact.put(entity.getCustomerid(), contractEntity);
		}
		if(contractEntity == null || StringUtils.isEmpty(contractEntity.getContractCode())){
			XxlJobLogger.log(String.format("未查询到合同  订单号【%s】--商家【%s】", entity.getId(),entity.getCustomerid()));
			entity.setIsCalculated(CalculateState.Contract_Miss.getCode());
			storageFeeEntity.setIsCalculated(CalculateState.Contract_Miss.getCode());
			entity.setRemark(String.format("未查询到合同  订单号【%s】--商家【%s】", entity.getId(),entity.getCustomerid()));
			feesList.add(storageFeeEntity);
			return false;
		}
		current = System.currentTimeMillis();
		XxlJobLogger.log("验证合同   耗时【{0}】毫秒  合同编号 【{1}】",(current - start),contractEntity.getContractCode());
		
		//----验证签约服务
		start = System.currentTimeMillis();// 系统开始时间
		Map<String,Object> contractItems_map=new HashMap<String,Object>();
		contractItems_map.put("contractCode", contractEntity.getContractCode());
		setSubjectId(entity);//获取编码
		contractItems_map.put("subjectId", SubjectId);
		List<PriceContractItemEntity> contractItems = priceContractItemRepository.query(contractItems_map);
		if(contractItems == null || contractItems.size() == 0 || StringUtils.isEmpty(contractItems.get(0).getTemplateId())) {
			XxlJobLogger.log(String.format("未签约服务  订单号【%s】--商家【%s】", entity.getId(),entity.getCustomerid()));
			entity.setIsCalculated(CalculateState.Contract_Miss.getCode());
			storageFeeEntity.setIsCalculated(CalculateState.Contract_Miss.getCode());
			entity.setRemark(String.format("未签约服务  订单号【%s】--商家【%s】", entity.getId(),entity.getCustomerid()));
			feesList.add(storageFeeEntity);
			return false;
		}
		current = System.currentTimeMillis();
		XxlJobLogger.log("验证签约服务耗时：【{0}】毫秒  ",(current - start));
		
		start = System.currentTimeMillis();// 验证报价是否配置
		/*验证报价 报价*/
		List<PriceGeneralQuotationEntity> priceGenerallist= new ArrayList<PriceGeneralQuotationEntity>();
		PriceGeneralQuotationEntity quoTemplete = null;
		if(!mapCusPrice.containsKey(customerId)){ //内存不包含，查询报价模板
			map.clear();
			map.put("subjectId",SubjectId);
			map.put("quotationNo", contractItems.get(0).getTemplateId());
			quoTemplete=priceGeneralQuotationRepository.query(map);
			if(quoTemplete != null){
				priceGenerallist.add(quoTemplete);
				mapCusPrice.put(customerId, priceGenerallist);//加入缓存
			}
		}
		else{
			priceGenerallist=mapCusPrice.get(customerId);
		}
		if(priceGenerallist==null || priceGenerallist.size()==0){
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
	private FeesReceiveStorageEntity initFeeEntity(BizOutstockMasterEntity outstock){
		FeesReceiveStorageEntity storageFeeEntity = new FeesReceiveStorageEntity();
		storageFeeEntity.setCreator("system");
		storageFeeEntity.setCreateTime(outstock.getCreateTime());
		storageFeeEntity.setCustomerId(outstock.getCustomerid());		//商家ID
		storageFeeEntity.setCustomerName(outstock.getCustomerName());	//商家名称
		storageFeeEntity.setWarehouseCode(outstock.getWarehouseCode());	//仓库ID
		storageFeeEntity.setWarehouseName(outstock.getWarehouseName());	//仓库名称
		storageFeeEntity.setOrderType(outstock.getBillTypeName());		//订单类型
		if(null!=outstock.getTotalVarieties()){
			storageFeeEntity.setVarieties(outstock.getTotalVarieties().intValue());
		}
		storageFeeEntity.setOrderNo(outstock.getOutstockNo());			//oms订单号
		storageFeeEntity.setProductType("");							//商品类型
		if(outstock.getTotalQuantity()!=null){
			storageFeeEntity.setQuantity((new Double(outstock.getTotalQuantity())).intValue());//商品数量
		}
		
		storageFeeEntity.setStatus("0");								//状态
		storageFeeEntity.setWeight(outstock.getTotalWeight());
		storageFeeEntity.setOperateTime(outstock.getCreateTime());
		storageFeeEntity.setCostType("FEE_TYPE_GENEARL");
		storageFeeEntity.setCost(new BigDecimal(0));	//入仓金额
		storageFeeEntity.setSubjectCode(SubjectId);
		storageFeeEntity.setOtherSubjectCode(SubjectId);
		storageFeeEntity.setBizId(String.valueOf(outstock.getId()));//业务数据主键
		storageFeeEntity.setFeesNo(outstock.getFeesNo());
		if(StringUtils.isEmpty(outstock.getTemperatureTypeCode())){
			outstock.setTemperatureTypeCode("LD");
			storageFeeEntity.setTempretureType("LD");
		}
		else{
			storageFeeEntity.setTempretureType(outstock.getTemperatureTypeCode());
		}
		if(StringUtils.isEmpty(outstock.getTemperatureTypeName())){
			outstock.setTemperatureTypeName("冷冻");
		}
		storageFeeEntity.setDelFlag("0");
		storageFeeEntity.setParam1(TemplateTypeEnum.COMMON.getCode());
		return storageFeeEntity;
	}

	@Override
	protected void calcuStandardService(List<BizOutstockMasterEntity> billList) {
		for (BizOutstockMasterEntity entity : billList) {
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
	
	private void calcu(BizOutstockMasterEntity entity){
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
		bizOutstockMasterService.update(entity);
		long current = System.currentTimeMillis();;// 当前系统时间
		XxlJobLogger.log("【标准报价】调用规则引擎   耗时【{0}】毫秒  费用【{1}】 ",(current - start));	
	}
	
	void setSubjectId(BizOutstockMasterEntity entity)
	{
		
		List<Map<String,String>>  list = bizOutstockMasterService.getTypeList("B2B_TYPE");
		
		for(Map<String,String> map:list)
		{
			if(null!=entity.getBillTypeCode()&&entity.getBillTypeCode().equals(map.get("code")))
			{
				this.SubjectId = map.get("code_name");
			    break;
			}
				
		}
		
	}

}
