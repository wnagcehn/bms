package com.jiuyescm.bms.jobhandler;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jiuyescm.bms.base.group.service.IBmsGroupSubjectService;
import com.jiuyescm.bms.drools.IFeesCalcuService;
import com.jiuyescm.bms.chargerule.receiverule.entity.BillRuleReceiveEntity;
import com.jiuyescm.bms.common.enumtype.CalculateState;
import com.jiuyescm.bms.general.entity.BizAddFeeEntity;
import com.jiuyescm.bms.general.entity.FeesReceiveStorageEntity;
import com.jiuyescm.bms.general.service.IBizAddFeeService;
import com.jiuyescm.bms.general.service.IFeesReceiveStorageService;
import com.jiuyescm.bms.general.service.IPriceContractInfoService;
import com.jiuyescm.bms.general.service.SequenceService;
import com.jiuyescm.bms.quotation.contract.entity.PriceContractInfoEntity;
import com.jiuyescm.bms.quotation.contract.entity.PriceContractItemEntity;
import com.jiuyescm.bms.quotation.contract.repository.imp.IPriceContractItemRepository;
import com.jiuyescm.bms.quotation.storage.entity.PriceExtraQuotationEntity;
import com.jiuyescm.bms.quotation.storage.entity.PriceStepQuotationEntity;
import com.jiuyescm.bms.quotation.storage.repository.IPriceExtraQuotationRepository;
import com.jiuyescm.bms.quotation.transport.entity.GenericTemplateEntity;
import com.jiuyescm.bms.quotation.transport.repository.IGenericTemplateRepository;
import com.jiuyescm.bms.rule.receiveRule.repository.IReceiveRuleRepository;
import com.jiuyescm.bs.util.StringUtil;
import com.jiuyescm.cfm.common.JAppContext;
import com.jiuyescm.common.utils.DoubleUtil;
import com.jiuyescm.contract.quote.api.IContractQuoteInfoService;
import com.jiuyescm.contract.quote.vo.ContractBizTypeEnum;
import com.jiuyescm.contract.quote.vo.ContractQuoteInfoVo;
import com.jiuyescm.contract.quote.vo.ContractQuoteQueryInfoVo;
import com.jiuyescm.exception.BizException;
import com.xxl.job.core.handler.annotation.JobHander;
import com.xxl.job.core.log.XxlJobLogger;

@JobHander(value="addFeeNewCalcJob")
@Service
public class AddFeeNewCalcJob extends CommonJobHandler<BizAddFeeEntity,FeesReceiveStorageEntity> {
	
	//private String SubjectId = null;// 增值费
	
	@Autowired private IContractQuoteInfoService contractQuoteInfoService;
	@Autowired private IPriceContractInfoService jobPriceContractInfoService;
	@Autowired private IFeesReceiveStorageService feesReceiveStorageService;
	//@Autowired private IPriceGeneralQuotationRepository priceGeneralQuotationRepository;
	@Autowired private IReceiveRuleRepository receiveRuleRepository;
	@Autowired private IFeesCalcuService feesCalcuService;
	@Autowired private IPriceContractItemRepository priceContractItemRepository;
	
	@Autowired private SequenceService sequenceService;
	@Autowired private IBizAddFeeService bizAddFeeService;
	@Autowired private IGenericTemplateRepository genericTemplateRepository;
	@Autowired private IPriceExtraQuotationRepository priceExtraQuotationRepository;
	@Autowired private IBmsGroupSubjectService bmsGroupSubjectService;

	
	Map<String,PriceStepQuotationEntity> mapCusStepPrice=null;
	Map<String,PriceContractInfoEntity> mapContact=null;
	Map<String,BillRuleReceiveEntity> mapRule=null;
	Map<String,GenericTemplateEntity> mapCusPrice=null;
	String priceType="";
	
	protected void initConf(){
		mapCusPrice=new ConcurrentHashMap<String,GenericTemplateEntity>();
		mapCusStepPrice=new ConcurrentHashMap<String,PriceStepQuotationEntity>();
		mapContact=new ConcurrentHashMap<String,PriceContractInfoEntity>();
		mapRule=new ConcurrentHashMap<String,BillRuleReceiveEntity>();
	}
	
	// 查询业务数据
	@Override
	protected List<BizAddFeeEntity> queryBillList(Map<String, Object> map) {
		XxlJobLogger.log("addFeeNewCalcJob查询条件map:【{0}】  ",map);
		Long current = System.currentTimeMillis();
		List<BizAddFeeEntity> bizList = bizAddFeeService.querybizAddFee(map);
		if(bizList!=null && bizList.size()>0){
			XxlJobLogger.log("【配送运单】查询行数【{0}】耗时【{1}】",bizList.size(),(System.currentTimeMillis()-current));
			initConf();
		}
		return bizList;
	}
	
	// 初始化
	@Override
	public FeesReceiveStorageEntity initFeeEntity(BizAddFeeEntity entity) {
		
		//费用科目赋值
		//SubjectId = entity.getFeesType();
		entity.setRemark("");
		FeesReceiveStorageEntity fee = new FeesReceiveStorageEntity();
    	fee.setFeesNo(entity.getFeesNo());
    	if(entity.getPrice()!=null){
    		fee.setCost(new BigDecimal(entity.getPrice()));
    	}else{
    		fee.setCost(new BigDecimal(0));
    	}
    	fee.setIsCalculated(entity.getIsCalculated());
    	fee.setCalculateTime(JAppContext.currentTimestamp());
		fee.setUnitPrice(entity.getUnitPrice());
		fee.setSubjectCode("wh_value_add_subject");
		fee.setOtherSubjectCode(entity.getFeesType());
		fee.setCustomerId(entity.getCustomerid());
		fee.setCustomerName(entity.getCustomerName());
		fee.setWarehouseCode(entity.getWarehouseCode());
		fee.setUnit(entity.getFeesUnit());
		double num=DoubleUtil.isBlank(entity.getAdjustNum())?entity.getNum():entity.getAdjustNum();
		if(!DoubleUtil.isBlank(num)){
			fee.setQuantity(num);
		}
        fee.setParam1(entity.getItem());
        fee.setCustomerName(entity.getCustomerName());
        fee.setWarehouseName(entity.getWarehouseName());	
		fee.setCreateTime(entity.getCreateTime());
		fee.setCreator("system");	
		fee.setCostType("FEE_TYPE_GENEARL");
		fee.setDelFlag("0");
		fee.setStatus("0");
		fee.setExternalProductNo(entity.getExternalNo());
		return fee;
	}
	
	@Override
	protected String[] initSubjects() {
		
		/*Map<String, String> maps = bmsGroupSubjectService.getSubject("job_subject_add_fee");
		if (maps.size() == 0) {
			String[] str = {"wh_product_refreeze", "wh_overtime_instock", "wh_full_check",
							"wh_paste_label", "wh_fruit_devanning", "wh_prepackaging",
							"wh_invoice_make", "wh_charge_pack", "wh_gift_bag",
							"wh_overtime_work", "wh_return_storage", "wh_icebag_refreeze"};
			return str;
		}	
		String[] strs = new String[maps.size()];
		int i = 0;
		for (String val : maps.keySet()) {
			if (i < maps.keySet().size()) {
				strs[i] = val;
				i++;
			}
		}*/
		String[] strs = {"wh_value_add_subject"};
		return strs;
	}

	@Override
	public boolean isJoin(BizAddFeeEntity t) {
		return true;
	}
	
	// 判断是否有不计算费用的
	@Override
	public boolean isNoExe(BizAddFeeEntity entity,FeesReceiveStorageEntity feeEntity) {
		return false;
	}
	
	@Override
	public void calcu(BizAddFeeEntity entity, FeesReceiveStorageEntity feeEntity) {
		ContractQuoteInfoVo modelEntity = getContractForWhat(entity);
		if(modelEntity == null || StringUtil.isEmpty(modelEntity.getTemplateCode())){
			calcuForBms(entity, feeEntity);
		}
		else{
			XxlJobLogger.log("-->"+entity.getId()+"规则编号【{0}】 ", modelEntity.getRuleCode());
			calcuForContract(entity,feeEntity,modelEntity);
		}
		
	}
	
	// 合同在线获取模板
	@Override
	public ContractQuoteInfoVo getContractForWhat(BizAddFeeEntity entity) {

		ContractQuoteQueryInfoVo queryVo = new ContractQuoteQueryInfoVo();
		queryVo.setCustomerId(entity.getCustomerid());
		queryVo.setBizTypeCode(ContractBizTypeEnum.STORAGE.getCode());
		queryVo.setSubjectCode(entity.getFeesType());
		queryVo.setCurrentTime(entity.getCreateTime());
		queryVo.setWarehouseCode(entity.getWarehouseCode());
		XxlJobLogger.log("-->"+entity.getId()+"查询合同在线参数【{0}】",JSONObject.fromObject(queryVo));
		ContractQuoteInfoVo modelEntity = new ContractQuoteInfoVo();
		try{
			modelEntity = contractQuoteInfoService.queryUniqueColumns(queryVo);
			XxlJobLogger.log("-->"+entity.getId()+"查询出的合同在线结果【{0}】",JSONObject.fromObject(modelEntity));
		}
		catch(BizException ex){
			XxlJobLogger.log("-->{0}合同在线无此合同 {1}" , entity.getId() , ex.getMessage());
			entity.setRemark(entity.getRemark()+"合同在线"+ex.getMessage()+";");
		}
		return modelEntity;
	}
	
	// 根据bms计算
	@Override
	public void calcuForBms(BizAddFeeEntity entity,FeesReceiveStorageEntity storageFeeEntity){
		XxlJobLogger.log("-->"+entity.getId()+"bms计算");
		//合同报价校验  false-不通过  true-通过
		if(validateData(entity, storageFeeEntity)){
			try{
				entity.setCalculateTime(JAppContext.currentTimestamp());
				storageFeeEntity.setCalculateTime(entity.getCalculateTime());
				String customerId=entity.getCustomerid();
				
				//报价模板
				GenericTemplateEntity generalEntity=mapCusPrice.get(customerId);
				//数量
				double num=DoubleUtil.isBlank(entity.getAdjustNum())?entity.getNum():entity.getAdjustNum();
						
				//计算方法
				double amount=0d;
				//一口价				
	            //费用 = 商品数量*模板单价
				Map<String, Object> param = new HashMap<>();
				param.put("templateId", generalEntity.getId().toString());
				param.put("subjectId", entity.getFeesType());
				List<PriceExtraQuotationEntity> extraList= priceExtraQuotationRepository.queryPriceByParam(param);
				if (extraList == null || extraList.size() <= 0) {
					entity.setIsCalculated(CalculateState.Other.getCode());
					storageFeeEntity.setIsCalculated(CalculateState.Other.getCode());
					entity.setRemark(entity.getRemark()+"没有维护增值费一口价报价;");
				}else {
					amount=num*extraList.get(0).getUnitPrice();							
					storageFeeEntity.setUnitPrice(extraList.get(0).getUnitPrice());
					storageFeeEntity.setParam3(generalEntity.getId()+"");

					storageFeeEntity.setCost(BigDecimal.valueOf(amount));
					storageFeeEntity.setParam4(priceType);
					entity.setRemark(entity.getRemark()+"计算成功;");
					entity.setIsCalculated(CalculateState.Finish.getCode());
					storageFeeEntity.setIsCalculated(CalculateState.Finish.getCode());	
				}				
			}catch(Exception ex){
				entity.setIsCalculated(CalculateState.Sys_Error.getCode());
				storageFeeEntity.setIsCalculated(CalculateState.Sys_Error.getCode());
				entity.setRemark(entity.getRemark()+"费用计算异常:"+ex.getMessage()+";");
				}
		}
	}
	
	private void calcuForContract(BizAddFeeEntity entity,FeesReceiveStorageEntity feeEntity,ContractQuoteInfoVo contractQuoteInfoVo){
		XxlJobLogger.log("-->"+entity.getId()+"合同在线计算");
		Map<String, Object> con = new HashMap<>();
		//List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		//if(StringUtil.isEmpty(contractQuoteInfoVo.getRuleCode()))
		if(StringUtils.isEmpty(contractQuoteInfoVo.getRuleCode())){
			XxlJobLogger.log("-->"+entity.getId()+"合同在线未绑定规则");
			feeEntity.setIsCalculated(CalculateState.Sys_Error.getCode());
			entity.setIsCalculated(CalculateState.Sys_Error.getCode());
			entity.setRemark(entity.getRemark()+"合同在线未绑定规则;");
			return;
		}
		con.put("quotationNo", contractQuoteInfoVo.getRuleCode());
		
		BillRuleReceiveEntity ruleEntity = receiveRuleRepository.queryOne(con);
		if (null == ruleEntity) {
			entity.setRemark(entity.getRemark()+"合同在线规则未绑定;");
			feeEntity.setIsCalculated(CalculateState.Quote_Miss.getCode());
			entity.setIsCalculated(CalculateState.Quote_Miss.getCode());
			XxlJobLogger.log("-->"+entity.getId()+"计算不成功，合同在线规则未绑定");
		}
		//获取合同在线查询条件
		Map<String, Object> cond = new HashMap<String, Object>();
		feesCalcuService.ContractCalcuService(entity, cond, ruleEntity.getRule(), ruleEntity.getQuotationNo());
		XxlJobLogger.log("-->"+entity.getId()+"获取报价参数【{0}】",cond);

	    if(cond == null || cond.size() == 0){
			XxlJobLogger.log("-->"+entity.getId()+"规则引擎拼接条件异常");
			feeEntity.setIsCalculated(CalculateState.Sys_Error.getCode());
			entity.setIsCalculated(CalculateState.Sys_Error.getCode());
			entity.setRemark(entity.getRemark()+"系统规则引擎异常;");
			return;
		}
		
		ContractQuoteInfoVo rtnQuoteInfoVo = contractQuoteInfoService.queryQuotes(contractQuoteInfoVo, cond);

		XxlJobLogger.log("-->"+entity.getId()+"获取合同在线报价结果【{0}】",JSONObject.fromObject(rtnQuoteInfoVo));
		for (Map<String, String> map : rtnQuoteInfoVo.getQuoteMaps()) {
			XxlJobLogger.log("-->"+entity.getId()+"报价信息 -- "+map);
		}
		//调用规则计算费用
		Map<String, Object> feesMap = feesCalcuService.ContractCalcuService(feeEntity, rtnQuoteInfoVo.getQuoteMaps(), ruleEntity.getRule(), ruleEntity.getQuotationNo());

		if(feeEntity.getCost().compareTo(BigDecimal.ZERO) == 1){
			feeEntity.setIsCalculated(CalculateState.Finish.getCode());
			entity.setIsCalculated(CalculateState.Finish.getCode());
			XxlJobLogger.log("-->"+entity.getId()+"计算成功，费用【{0}】",feeEntity.getCost());
		}
		else{
			feeEntity.setIsCalculated(CalculateState.Quote_Miss.getCode());
			entity.setIsCalculated(CalculateState.Quote_Miss.getCode());
			XxlJobLogger.log("-->"+entity.getId()+"计算不成功，费用【0】");
		}
	}
	
	
	protected boolean validateData(BizAddFeeEntity entity,
			FeesReceiveStorageEntity storageFeeEntity) {
		
		XxlJobLogger.log("数据主键ID:【{0}】  ",entity.getId());
		Timestamp time=JAppContext.currentTimestamp();
		entity.setCalculateTime(time);
		Map<String,Object> map=new HashMap<String,Object>();
		String customerId=entity.getCustomerid();
		//FeesReceiveStorageEntity storageFeeEntity = initFeeEntity(entity);
		storageFeeEntity.setCalculateTime(time);
		long start = System.currentTimeMillis();// 系统开始时间
		long current = 0l;// 当前系统时间
		entity.setCalculateTime(JAppContext.currentTimestamp());
		PriceContractInfoEntity contractEntity =null;
		if(mapContact.containsKey(customerId)){
			contractEntity=mapContact.get(customerId);
		}else{
			map.clear();
			map.put("customerid", customerId);
			map.put("contractTypeCode", "CUSTOMER_CONTRACT");
		    contractEntity = jobPriceContractInfoService.queryContractByCustomer(map);
		    if(contractEntity!=null){
			    mapContact.put(customerId, contractEntity);
		    }
		}
		if(contractEntity == null || StringUtils.isEmpty(contractEntity.getContractCode())){
			XxlJobLogger.log(String.format("-->"+entity.getId()+"未查询到合同  订单号【%s】--商家【%s】", entity.getId(),customerId));
			entity.setIsCalculated(CalculateState.Contract_Miss.getCode());
			storageFeeEntity.setIsCalculated(CalculateState.Contract_Miss.getCode());
			entity.setRemark(entity.getRemark()+"未查询到合同;");
			//feesList.add(storageFeeEntity);
			return false;
		}
		current = System.currentTimeMillis();
		XxlJobLogger.log("-->"+entity.getId()+"验证合同   耗时【{0}】毫秒  合同编号 【{1}】",(current - start),contractEntity.getContractCode());
		
		//----验证签约服务
		start = System.currentTimeMillis();// 系统开始时间
		Map<String,Object> contractItems_map=new HashMap<String,Object>();
		contractItems_map.put("contractCode", contractEntity.getContractCode());
		contractItems_map.put("subjectId", "wh_value_add_subject");
		List<PriceContractItemEntity> contractItems = priceContractItemRepository.query(contractItems_map);
		if(contractItems == null || contractItems.size() == 0 || StringUtils.isEmpty(contractItems.get(0).getTemplateId())) {
			XxlJobLogger.log("-->"+entity.getId()+"未签约服务  订单号【{0}】--商家【{1}】", entity.getId(),entity.getCustomerid());
			entity.setIsCalculated(CalculateState.Contract_Miss.getCode());
			storageFeeEntity.setIsCalculated(CalculateState.Contract_Miss.getCode());
			entity.setRemark(entity.getRemark()+"未签约服务;");
			//feesList.add(storageFeeEntity);
			return false;
		}
		current = System.currentTimeMillis();
		XxlJobLogger.log("-->"+entity.getId()+"验证签约服务耗时：【{0}】毫秒  ",(current - start));		
		
		/*验证报价 报价*/
		start = System.currentTimeMillis();// 系统开始时间
		GenericTemplateEntity quoTemplete=null;
		if(!mapCusPrice.containsKey(customerId)){
			
			map.clear();
			//map.put("subjectId","wh_value_add_subject");
			//quoTemplete=priceGeneralQuotationRepository.query(map);
			map.put("templateCode", contractItems.get(0).getTemplateId());
			quoTemplete=genericTemplateRepository.query(map);
			if(quoTemplete != null){
				mapCusPrice.put(customerId, quoTemplete);//加入缓存
			}
		}else{
			quoTemplete=mapCusPrice.get(customerId);
		}
		if(quoTemplete==null){
			XxlJobLogger.log("-->"+entity.getId()+"报价未配置");
			entity.setIsCalculated(CalculateState.Quote_Miss.getCode());
			storageFeeEntity.setIsCalculated(CalculateState.Quote_Miss.getCode());
			entity.setRemark(entity.getRemark()+"报价未配置;");
			//feesList.add(storageFeeEntity);
			return false;
		}
		current = System.currentTimeMillis();
		XxlJobLogger.log("-->"+entity.getId()+"验证报价耗时：【{0}】毫秒  ",(current - start));
		return true;
	}
	
	/**
	 * 批量更新
	 */
	@Override
	public void updateBatch(List<BizAddFeeEntity> ts,List<FeesReceiveStorageEntity> fs) {

		long start = System.currentTimeMillis();// 系统开始时间
		long current = 0l;// 当前系统时间
		bizAddFeeService.updateList(ts);
		current = System.currentTimeMillis();
		XxlJobLogger.log("更新业务数据耗时：【{0}】毫秒",(current - start));
		start = System.currentTimeMillis();// 系统开始时间
		feesReceiveStorageService.InsertBatch(fs);
		current = System.currentTimeMillis();
		XxlJobLogger.log("新增费用数据耗时：【{0}】毫秒  ",(current - start));
	}

	@Override
	public Integer deleteFeesBatch(List<BizAddFeeEntity> list) {
		List<String> feesNos = new ArrayList<String>();
		Map<String, Object> feesMap = new HashMap<String, Object>();
		for (BizAddFeeEntity entity : list) {
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
			}
		}
		catch(Exception ex){
			XxlJobLogger.log("批量删除费用失败-- {1}",ex.getMessage());
		}
		return null;
	}
}
