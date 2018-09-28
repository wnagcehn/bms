package com.jiuyescm.bms.jobhandler;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jiuyescm.bms.base.dictionary.entity.SystemCodeEntity;
import com.jiuyescm.bms.base.group.service.IBmsGroupCustomerService;
import com.jiuyescm.bms.base.group.service.IBmsGroupService;
import com.jiuyescm.bms.base.group.service.IBmsGroupSubjectService;
import com.jiuyescm.bms.base.group.vo.BmsGroupVo;

import com.jiuyescm.bms.calculate.base.IFeesCalcuService;
import com.jiuyescm.bms.chargerule.receiverule.entity.BillRuleReceiveEntity;
import com.jiuyescm.bms.common.enumtype.CalculateState;
import com.jiuyescm.bms.common.enumtype.TemplateTypeEnum;
import com.jiuyescm.bms.general.entity.BizPalletInfoEntity;
import com.jiuyescm.bms.general.entity.FeesReceiveStorageEntity;
import com.jiuyescm.bms.general.service.IBizPalletInfoRepository;
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
import com.jiuyescm.bms.receivable.storage.service.IBizProductPalletStorageService;
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

@JobHander(value="palletCalcJob")
@Service
public class PalletCalcJob extends CommonJobHandler<BizPalletInfoEntity,FeesReceiveStorageEntity> {


	@Autowired private SequenceService sequenceService;
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


	Map<String,PriceGeneralQuotationEntity> mapCusPrice=null;
	Map<String,PriceStepQuotationEntity> mapCusStepPrice=null;
	Map<String,PriceContractInfoEntity> mapContact=null;
	Map<String,BillRuleReceiveEntity> mapRule=null;
	List<String> cusList=null;
	String priceType="";
	Map<String, String> temMap=null;
	
	@Override
	protected List<BizPalletInfoEntity> queryBillList(Map<String, Object> map) {
		Long current = System.currentTimeMillis();
		List<BizPalletInfoEntity> bizList = bizPalletInfoService.querybizPallet(map);
		if(bizList!=null && bizList.size() > 0){
			XxlJobLogger.log("【托数】查询行数【{0}】耗时【{1}】",bizList.size(),(System.currentTimeMillis()-current));
			initConf();
		}
		return bizList;
		
	}
	
	@Override
	protected String[] initSubjects() {
		//这里的科目应该在科目组中配置,动态查询
		//wh_disposal(处置费)
		Map<String,String> map=bmsGroupSubjectService.getSubject("job_subject_pallet");
		if(map.size() == 0){
			String[] strs = {"wh_disposal","wh_product_storage","wh_material_storage","outstock_pallet_vm"};
			return strs;
		}else{
			List<String> strs = new ArrayList<String>();
			for(String value:map.keySet()){
				strs.add(value);
			}
			strs.add("outstock_pallet_vm");
			String[] strArray = strs.toArray(new String[strs.size()]);	
			return strArray;
		}
	}
	
	@Override
	public boolean isJoin(BizPalletInfoEntity entity) {
		if("wh_disposal".equals(SubjectId) && "instock".equals(entity.getBizType())){
			return true;
		}
		else if("wh_product_storage".equals(SubjectId) && "product".equals(entity.getBizType())){
			return true;
		}
		else if("wh_material_storage".equals(SubjectId) && "material".equals(entity.getBizType())){
			return true;
		}
		else if("outstock_pallet_vm".equals(SubjectId) && "outstock".equals(entity.getBizType())){
			return true;
		}
		else{
			return false;
		}	
	}
	
	protected void initConf(){
		mapCusPrice=new ConcurrentHashMap<String,PriceGeneralQuotationEntity>();
		mapCusStepPrice=new ConcurrentHashMap<String,PriceStepQuotationEntity>();
		mapContact=new ConcurrentHashMap<String,PriceContractInfoEntity>();
		mapRule=new ConcurrentHashMap<String,BillRuleReceiveEntity>();
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("typeCode", "NO_FEES_DELIVER");
		List<SystemCodeEntity> systemCodeList = systemCodeService.querySysCodes(map);
		temMap =new LinkedHashMap<String,String>();
		if(systemCodeList!=null && systemCodeList.size()>0){
			for(int i=0;i<systemCodeList.size();i++){
				temMap.put(systemCodeList.get(i).getCode(), systemCodeList.get(i).getCodeName());
			}
		}
	}
	
	@Override
	public void calcu(BizPalletInfoEntity entity, FeesReceiveStorageEntity feeEntity) {
		ContractQuoteInfoVo modelEntity = getContractForWhat(entity);
		if(modelEntity == null || StringUtil.isEmpty(modelEntity.getTemplateCode())){
			calcuForBms(entity, feeEntity);
		}
		else{
			XxlJobLogger.log("-->"+entity.getId()+"规则编号：  "+ modelEntity.getRuleCode().trim());
			calcuForContract(entity,feeEntity,modelEntity);
		}
		
	}
	
	@Override
	public FeesReceiveStorageEntity initFeeEntity(BizPalletInfoEntity entity){
		FeesReceiveStorageEntity storageFeeEntity = new FeesReceiveStorageEntity();	

		storageFeeEntity.setCreator("system");
		storageFeeEntity.setCreateTime(entity.getCreateTime());
		storageFeeEntity.setOperateTime(entity.getCreateTime());
		storageFeeEntity.setCustomerId(entity.getCustomerId());		//商家ID
		storageFeeEntity.setCustomerName(entity.getCustomerName());		//商家名称
		storageFeeEntity.setWarehouseCode(entity.getWarehouseCode());	//仓库ID
		storageFeeEntity.setWarehouseName(entity.getWarehouseName());	//仓库名称
		storageFeeEntity.setCostType("FEE_TYPE_GENEARL");			//费用类别 FEE_TYPE_GENEARL-通用 FEE_TYPE_MATERIAL-耗材 FEE_TYPE_ADD-增值
		storageFeeEntity.setSubjectCode(SubjectId);		//费用科目
		storageFeeEntity.setOtherSubjectCode(SubjectId);
		storageFeeEntity.setProductType("");							//商品类型
		if(entity.getPalletNum()!=null){
			storageFeeEntity.setQuantity(entity.getAdjustPalletNum()==null?entity.getPalletNum():entity.getAdjustPalletNum());//商品数量
		}
		storageFeeEntity.setStatus("0");			
		storageFeeEntity.setUnit("PALLETS");
		storageFeeEntity.setTempretureType(entity.getTemperatureTypeCode());//设置温度类型  zhangzw
		storageFeeEntity.setCost(new BigDecimal(0));	//入仓金额
		storageFeeEntity.setUnitPrice(0d);
		//托数类型
		storageFeeEntity.setBizType(entity.getBizType());
		storageFeeEntity.setFeesNo(entity.getFeesNo());
		storageFeeEntity.setParam1(TemplateTypeEnum.COMMON.getCode());
		storageFeeEntity.setDelFlag("0");
		//转换温度
//		if(StringUtils.isNotBlank(entity.getTemperatureTypeCode())){
//			entity.setTemperatureTypeName(temMap.get(entity.getTemperatureTypeCode()));
//		}
		return storageFeeEntity;
	}
	
	@Override
	public boolean isNoExe(BizPalletInfoEntity entity,FeesReceiveStorageEntity feeEntity) {
		//1.出库托数不计算费用
		if("outstock".equals(entity.getBizType())){
			entity.setIsCalculated(CalculateState.No_Exe.getCode());
			feeEntity.setIsCalculated(CalculateState.No_Exe.getCode());
			entity.setRemark("出库托数不计算费用");
			return true;
		}
		//2.商家已经按件收取存储费,按托存储不计费
		if ("product".equals(entity.getBizType())) {
			//指定的商家
			Map<String,Object> map= new HashMap<String, Object>();
			map.put("groupCode", "customer_unit");
			map.put("bizType", "group_customer");
			BmsGroupVo bmsGroup=bmsGroupService.queryOne(map);
			if(bmsGroup!=null){
				cusList=bmsGroupCustomerService.queryCustomerByGroupId(bmsGroup.getId());
			}
			
			//如果商家已经按件收取存储费，则按托存储不计费
			if(cusList.size()>0 && cusList.contains(entity.getCustomerId())){
				XxlJobLogger.log("-->"+entity.getId()+"商家已经按件收取存储费,按托存储不计费");
				entity.setIsCalculated(CalculateState.No_Exe.getCode());
				feeEntity.setIsCalculated(CalculateState.No_Exe.getCode());
				entity.setRemark("商家已经按件收取存储费,按托存储不计费");
				return true;
			}
		}
		return false;	
	}
	
	@Override
	public ContractQuoteInfoVo getContractForWhat(BizPalletInfoEntity entity) {
		ContractQuoteQueryInfoVo queryVo = new ContractQuoteQueryInfoVo();
		queryVo.setCustomerId(entity.getCustomerId());
		queryVo.setBizTypeCode(ContractBizTypeEnum.STORAGE.getCode());
		queryVo.setSubjectCode(SubjectId);
		queryVo.setCurrentTime(entity.getCreateTime());
		queryVo.setWarehouseCode(entity.getWarehouseCode());
		XxlJobLogger.log("-->"+entity.getId()+"查询合同在线参数【{0}】",JSONObject.fromObject(queryVo));
		ContractQuoteInfoVo modelEntity = new ContractQuoteInfoVo();
		try{
			modelEntity = contractQuoteInfoService.queryUniqueColumns(queryVo);
			XxlJobLogger.log("-->"+entity.getId()+"查询出的合同在线结果【{0}】",JSONObject.fromObject(modelEntity));
		}
		catch(BizException ex){
			XxlJobLogger.log("-->"+entity.getId()+"合同在线无此合同:"+ex.getMessage());
			entity.setRemark(ex.getMessage());
		}
		return modelEntity;
	}
	
	@Override
	public void calcuForBms(BizPalletInfoEntity entity,FeesReceiveStorageEntity feeEntity){
		//合同报价校验  false-不通过  true-通过
		XxlJobLogger.log("-->"+entity.getId()+"bms计算");
		try{
			if(validateData(entity, feeEntity)){
				if(mapCusPrice.containsKey(entity.getCustomerId())){
					entity.setCalculateTime(JAppContext.currentTimestamp());
					feeEntity.setCalculateTime(entity.getCalculateTime());
					String customerId=entity.getCustomerId();
					//报价模板
					PriceGeneralQuotationEntity generalEntity=mapCusPrice.get(customerId);
					//数量
					double num=DoubleUtil.isBlank(entity.getAdjustPalletNum())?entity.getPalletNum():entity.getAdjustPalletNum();
					//如果有调整托数按照调整托数算钱  185需求
					entity.setPalletNum(num);
							
					//计算方法
					double amount=0d;
					switch(priceType){
					case "PRICE_TYPE_NORMAL"://一口价				
			            // -> 费用 = 托数*模板单价
						amount=num*generalEntity.getUnitPrice();					
						feeEntity.setUnitPrice(generalEntity.getUnitPrice());
						feeEntity.setParam3(generalEntity.getId()+"");
						break;
					case "PRICE_TYPE_STEP"://阶梯价
						PriceStepQuotationEntity stepQuoEntity=mapCusStepPrice.get(customerId);
						if(!DoubleUtil.isBlank(stepQuoEntity.getUnitPrice())){
							amount=num*stepQuoEntity.getUnitPrice();
							feeEntity.setUnitPrice(stepQuoEntity.getUnitPrice());
						}else{
							amount=stepQuoEntity.getFirstNum()<num?stepQuoEntity.getFirstPrice()+(num-stepQuoEntity.getFirstNum())/stepQuoEntity.getContinuedItem()*stepQuoEntity.getContinuedPrice():stepQuoEntity.getFirstPrice();
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
					
					feeEntity.setCost(BigDecimal.valueOf(amount));
					feeEntity.setParam4(priceType);
					entity.setRemark("计算成功");
					entity.setIsCalculated(CalculateState.Finish.getCode());
					feeEntity.setIsCalculated(CalculateState.Finish.getCode());
				}
			}
		}
		catch(Exception ex){
			feeEntity.setIsCalculated(CalculateState.Sys_Error.getCode());
			entity.setIsCalculated(CalculateState.Sys_Error.getCode());
			XxlJobLogger.log("-->"+entity.getId()+"系统异常，费用【0】");
		}
	}


	public void calcuForContract(BizPalletInfoEntity biz,
			FeesReceiveStorageEntity fee,ContractQuoteInfoVo contractQuoteInfoVo) {
		// TODO Auto-generated method stub
		XxlJobLogger.log("-->"+biz.getId()+"合同在线计算");
		try{
			Map<String, Object> con = new HashMap<>();
			con.put("quotationNo", contractQuoteInfoVo.getRuleCode());
			BillRuleReceiveEntity ruleEntity = receiveRuleRepository.queryOne(con);
			if (null == ruleEntity) {
				biz.setRemark("合同在线规则未绑定");
				fee.setIsCalculated(CalculateState.Quote_Miss.getCode());
				biz.setIsCalculated(CalculateState.Quote_Miss.getCode());
				XxlJobLogger.log("-->"+biz.getId()+"计算不成功，合同在线规则未绑定");
			}
			//获取合同在线查询条件s
			Map<String, Object> cond = new HashMap<String, Object>();
			feesCalcuService.ContractCalcuService(biz, cond, ruleEntity.getRule(), ruleEntity.getQuotationNo());
			XxlJobLogger.log("-->"+biz.getId()+"获取报价参数"+cond);
			ContractQuoteInfoVo rtnQuoteInfoVo = contractQuoteInfoService.queryQuotes(contractQuoteInfoVo, cond);
			XxlJobLogger.log("-->"+biz.getId()+"获取合同在线报价结果"+JSONObject.fromObject(rtnQuoteInfoVo));
			for (Map<String, String> map : rtnQuoteInfoVo.getQuoteMaps()) {
				XxlJobLogger.log("-->"+biz.getId()+"报价信息 -- "+map);
			}
			//调用规则计算费用
			Map<String, Object> feesMap = feesCalcuService.ContractCalcuService(fee, rtnQuoteInfoVo.getQuoteMaps(), ruleEntity.getRule(), ruleEntity.getQuotationNo());

			if(fee.getCost().compareTo(BigDecimal.ZERO) == 1){
				fee.setIsCalculated(CalculateState.Finish.getCode());
				biz.setIsCalculated(CalculateState.Finish.getCode());
				XxlJobLogger.log("-->"+biz.getId()+"计算成功，费用【{0}】",fee.getCost());
			}
			else{
				fee.setIsCalculated(CalculateState.Quote_Miss.getCode());
				biz.setIsCalculated(CalculateState.Quote_Miss.getCode());
				XxlJobLogger.log("-->"+biz.getId()+"计算不成功，费用【0】");
			}
		}
		catch(Exception ex){
			fee.setIsCalculated(CalculateState.Sys_Error.getCode());
			biz.setIsCalculated(CalculateState.Sys_Error.getCode());
			XxlJobLogger.log("-->"+biz.getId()+"计算不成功，费用0"+ex);
		}
		
	}
   
	/**
	 * 验证数据  初始化数据
	 */
	protected boolean validateData(BizPalletInfoEntity entity,FeesReceiveStorageEntity feeEntity) {
		
		XxlJobLogger.log("-->"+entity.getId()+"数据主键ID:【{0}】  ",entity.getId());
		entity.setCalculateTime(JAppContext.currentTimestamp());
		Map<String,Object> map=new HashMap<String,Object>();
		String customerId=entity.getCustomerId();
		feeEntity.setCalculateTime(JAppContext.currentTimestamp());
		long start = System.currentTimeMillis();// 系统开始时间
		long current = 0l;// 当前系统时间
		
		
		
		/*验证商家是否合同存在*/
		PriceContractInfoEntity contractEntity=null;
		if(mapContact.containsKey(customerId)){
			contractEntity=mapContact.get(customerId);
		}else{
			Map<String,Object> aCondition=new HashMap<>();
			aCondition.put("customerid",customerId);
			aCondition.put("contractTypeCode", "CUSTOMER_CONTRACT");
		    contractEntity = jobPriceContractInfoService.queryContractByCustomer(aCondition);
		    if(contractEntity!=null){
			    mapContact.put(customerId, contractEntity);
		    }
		}
		if(contractEntity == null || StringUtils.isEmpty(contractEntity.getContractCode())){
			XxlJobLogger.log("-->"+entity.getId()+String.format("未查询到有效合同  订单号【%s】--商家【%s】", entity.getId(),customerId));
			entity.setIsCalculated(CalculateState.Contract_Miss.getCode());
			feeEntity.setIsCalculated(CalculateState.Contract_Miss.getCode());
			entity.setRemark("未查询到有效合同");
			return false;
		}
		current = System.currentTimeMillis();
		XxlJobLogger.log("-->"+entity.getId()+"验证合同   耗时【{0}】毫秒  合同编号 【{1}】",(current - start),contractEntity.getContractCode());
		
		//----验证签约服务
		start = System.currentTimeMillis();// 系统开始时间
		Map<String,Object> contractItems_map=new HashMap<String,Object>();
		contractItems_map.put("contractCode", contractEntity.getContractCode());
		contractItems_map.put("subjectId", SubjectId);
		List<PriceContractItemEntity> contractItems = priceContractItemRepository.query(contractItems_map);
		if(contractItems == null || contractItems.size() == 0 || StringUtils.isEmpty(contractItems.get(0).getTemplateId())) {
			XxlJobLogger.log("-->"+entity.getId()+"未签约服务  订单号【{0}】--商家【{1}】", entity.getId(),entity.getCustomerId());
			entity.setIsCalculated(CalculateState.Contract_Miss.getCode());
			feeEntity.setIsCalculated(CalculateState.Contract_Miss.getCode());
			entity.setRemark("未签约服务");
			return false;
		}
		current = System.currentTimeMillis();
		XxlJobLogger.log("-->"+entity.getId()+"验证签约服务耗时：【{0}】毫秒  ",(current - start));			
		
		
		
		start = System.currentTimeMillis();// 系统开始时间
		/*验证报价 报价*/
		PriceGeneralQuotationEntity quoTemplete=null;
		if(!mapCusPrice.containsKey(customerId)){
			map.clear();
			map.put("subjectId",SubjectId);
			map.put("quotationNo", contractItems.get(0).getTemplateId());
			quoTemplete=priceGeneralQuotationRepository.query(map);
			if(quoTemplete != null){
				mapCusPrice.put(customerId, quoTemplete);//加入缓存
			}
		}else{
			quoTemplete=mapCusPrice.get(customerId);
		}
		if(quoTemplete==null){
			XxlJobLogger.log("-->"+entity.getId()+"报价未配置");
			entity.setIsCalculated(CalculateState.Quote_Miss.getCode());
			feeEntity.setIsCalculated(CalculateState.Quote_Miss.getCode());
			entity.setRemark("报价未配置");
			return false;
		}
		//报价模板
		PriceGeneralQuotationEntity priceGeneral=quoTemplete;
		priceType=priceGeneral.getPriceType();
		List<PriceStepQuotationEntity> list=new ArrayList<PriceStepQuotationEntity>();
		PriceStepQuotationEntity price=new PriceStepQuotationEntity();
		if(priceType.equals("PRICE_TYPE_STEP")){//阶梯价格
			//寻找阶梯报价
			map.clear();
			map.put("quotationId", priceGeneral.getId());
			//根据报价单位判断
			map.put("num", DoubleUtil.isBlank(entity.getAdjustPalletNum())?entity.getPalletNum():entity.getAdjustPalletNum());			
			//查询出的所有子报价
			list=repository.queryPriceStepByQuatationId(map);
			
			if(list==null || list.size() == 0){
				XxlJobLogger.log("-->"+entity.getId()+"阶梯报价未配置");
				entity.setIsCalculated(CalculateState.Quote_Miss.getCode());
				feeEntity.setIsCalculated(CalculateState.Quote_Miss.getCode());
				entity.setRemark("阶梯报价未配置");
				return  false;
			}
			
			//封装数据的仓库和温度
			map.clear();
			map.put("warehouse_code", entity.getWarehouseCode());
			map.put("temperature_code", entity.getTemperatureTypeCode());
			price=storageQuoteFilterService.quoteFilter(list, map);
			
			if(price==null){
				XxlJobLogger.log("-->"+entity.getId()+"阶梯报价未配置");
				entity.setIsCalculated(CalculateState.Quote_Miss.getCode());
				feeEntity.setIsCalculated(CalculateState.Quote_Miss.getCode());
				entity.setRemark("阶梯报价未配置");
				return  false;
			}else {
				XxlJobLogger.log("筛选后得到的报价结果【{0}】",JSONObject.fromObject(price));
				mapCusStepPrice.put(customerId,price);
			}
		}else if(priceType.equals("PRICE_TYPE_NORMAL")){//一口价
			
		}else{//报价类型缺失
			XxlJobLogger.log("-->"+entity.getId()+"报价类型未知");
			entity.setIsCalculated(CalculateState.Quote_Miss.getCode());
			feeEntity.setIsCalculated(CalculateState.Quote_Miss.getCode());
			entity.setRemark("报价【"+priceGeneral.getQuotationNo()+"】类型未知");
			return  false;
		}
		current = System.currentTimeMillis();
		XxlJobLogger.log("-->"+entity.getId()+"验证报价耗时：【{0}】毫秒  ",(current - start));
		return true;
	}
	
	@Override
	public void updateBatch(List<BizPalletInfoEntity> ts,List<FeesReceiveStorageEntity> fs) {

		long start = System.currentTimeMillis();// 系统开始时间
		long current = 0l;// 当前系统时间
		bizPalletInfoService.updatebizPallet(ts);
		current = System.currentTimeMillis();
		XxlJobLogger.log("更新业务数据耗时：【{0}】毫秒  ",(current - start));
		start = System.currentTimeMillis();// 系统开始时间
		feesReceiveStorageService.InsertBatch(fs);
		current = System.currentTimeMillis();
		XxlJobLogger.log("新增费用数据耗时：【{0}】毫秒 ",(current - start));
		
	}

	@Override
	public Integer deleteFeesBatch(List<BizPalletInfoEntity> list) {
		List<String> feesNos = new ArrayList<String>();
		Map<String, Object> feesMap = new HashMap<String, Object>();
		for (BizPalletInfoEntity entity : list) {
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
	
	/**
	 * bizType转subjectId
	 * @param bizType
	 * @return
	 */
//	private String bizTypeTranSubjectId(String bizType){
//		String subjectId = "";
//		if ("product".equals(bizType)) {
//			subjectId = "wh_product_storage"; 
//		}else if ("material".equals(bizType)) {
//			subjectId = "wh_material_storage";
//		}else if ("instock".equals(bizType)) {
//			subjectId = "wh_disposal";
//		}
//		return subjectId;
//	}
}
