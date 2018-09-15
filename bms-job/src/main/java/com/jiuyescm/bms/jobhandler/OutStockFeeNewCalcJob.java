package com.jiuyescm.bms.jobhandler;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jiuyescm.bms.base.dictionary.entity.SystemCodeEntity;
import com.jiuyescm.bms.base.group.service.IBmsGroupSubjectService;
import com.jiuyescm.bms.biz.storage.entity.BizOutstockMasterEntity;
import com.jiuyescm.bms.calculate.base.IFeesCalcuService;
import com.jiuyescm.bms.chargerule.receiverule.entity.BillRuleReceiveEntity;
import com.jiuyescm.bms.common.enumtype.CalculateState;
import com.jiuyescm.bms.common.enumtype.TemplateTypeEnum;
import com.jiuyescm.bms.general.entity.FeesReceiveStorageEntity;
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
import com.jiuyescm.bms.receivable.storage.service.IBizOutstockMasterService;
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

@JobHander(value="outStockFeeNewCalcJob")
@Service
public class OutStockFeeNewCalcJob extends CommonJobHandler<BizOutstockMasterEntity,FeesReceiveStorageEntity> {
	
	//private String SubjectId = "wh_b2c_work";		//费用类型-B2C订单操作费 1004原编码
	
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
	
	Map<String,PriceGeneralQuotationEntity> mapCusPrice=null;
	Map<String,PriceStepQuotationEntity> mapCusStepPrice=null;
	Map<String,PriceContractInfoEntity> mapContact=null;
	
	String priceType="";
	
	Map<String, String> temMap = null;

	// 查询业务数据	
	@Override
	protected List<BizOutstockMasterEntity> queryBillList(Map<String, Object> map) {
		List<BizOutstockMasterEntity> bizList = bizOutstockMasterService.query(map);
		if(bizList.size()>0){
			initConf();
		}
		return bizList;
	}
	
	@Override
	public Integer deleteFeesBatch(List<BizOutstockMasterEntity> list) {
		Map<String, Object> feesMap = new HashMap<String, Object>();
		List<String> feesNos = new ArrayList<String>();
		for (BizOutstockMasterEntity entity : list) {
			if(StringUtils.isNotEmpty(entity.getFeesNo())){
				feesNos.add(entity.getFeesNo());
			}
			else{
				entity.setFeesNo(sequenceService.getBillNoOne(BizOutstockMasterEntity.class.getName(), "STO", "0000000000"));
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
	
	protected void initConf(){
		mapCusPrice=new ConcurrentHashMap<String,PriceGeneralQuotationEntity>();
		mapCusStepPrice=new ConcurrentHashMap<String,PriceStepQuotationEntity>();
		mapContact=new ConcurrentHashMap<String,PriceContractInfoEntity>();
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("typeCode", "TEMPERATURE_TYPE");
		List<SystemCodeEntity> systemCodeList = systemCodeService.querySysCodes(map);
		temMap =new ConcurrentHashMap<String,String>();
		if(systemCodeList!=null && systemCodeList.size()>0){
			for(int i=0;i<systemCodeList.size();i++){
				temMap.put(systemCodeList.get(i).getCode(), systemCodeList.get(i).getCodeName());
			}
		}
	}
	
	@Override
	protected String[] initSubjects() {
		//这里的科目应该在科目组中配置,动态查询
		//wh_b2c_work(B2C订单操作费 )    wh_b2b_work(B2B订单操作费)     wh_b2b_handwork(出库装车费)
		Map<String,String> map=bmsGroupSubjectService.getSubject("job_subject_outstock");
		if(map.size() == 0){
			String[] strs = {"wh_b2c_work","wh_b2b_work","wh_b2b_handwork"};
			return strs;
		}else{
			int i=0;
			String[] strs=new String[map.size()];
			for(String value:map.keySet()){
				strs[i]=value;	
				i++;
			}
			return strs;
		}
		
	}
	
	@Override
	public boolean isJoin(BizOutstockMasterEntity entity) {
		//如果是【B2B订单操作费】 或【出库装车费】,并且是B2B出库单   ( B2bFlag 0-B2C  1-B2B)
		if(("wh_b2b_work".equals(SubjectId) ||"wh_b2b_handwork".equals(SubjectId)) 
				&& "1".equals(entity.getB2bFlag())){
			return true;
		}
		else if("wh_b2c_work".equals(SubjectId) && "0".equals(entity.getB2bFlag())){
			return true;
		}
		else{
			return false;
		}
	}
	
	@Override
	public void calcu(BizOutstockMasterEntity entity, FeesReceiveStorageEntity feeEntity) {
		ContractQuoteInfoVo modelEntity = getContractForWhat(entity);
		if(modelEntity == null || StringUtil.isEmpty(modelEntity.getTemplateCode())){
			calcuForBms(entity, feeEntity);
		}
		else{
			XxlJobLogger.log("-->"+entity.getId()+"规则编号【{0}】", modelEntity.getRuleCode().trim());
			calcuForContract(entity,feeEntity,modelEntity);
		}
		
	}
	
	// 初始化
	@Override
	public FeesReceiveStorageEntity initFeeEntity(BizOutstockMasterEntity outstock) {
		FeesReceiveStorageEntity storageFeeEntity = new FeesReceiveStorageEntity();
		storageFeeEntity.setCreator("system");
		storageFeeEntity.setCreateTime(outstock.getCreateTime());
		storageFeeEntity.setCustomerId(outstock.getCustomerid());		//商家ID
		storageFeeEntity.setCustomerName(outstock.getCustomerName());	//商家名称
		storageFeeEntity.setWarehouseCode(outstock.getWarehouseCode());	//仓库ID
		storageFeeEntity.setWarehouseName(outstock.getWarehouseName());	//仓库名称
		storageFeeEntity.setOrderType(outstock.getBillTypeName());		//订单类型
		
		
		//塞品种数
		Double varieties=outstock.getResizeVarieties()==null?outstock.getTotalVarieties():outstock.getResizeVarieties();
		if(!DoubleUtil.isBlank(varieties)){
			storageFeeEntity.setVarieties(varieties.intValue());
		}
		
		//塞件数
		Double charge_qty = DoubleUtil.isBlank(outstock.getResizeNum())?outstock.getTotalQuantity():outstock.getResizeNum();
		storageFeeEntity.setQuantity(charge_qty);
		//塞重量
		
		Double charge_weight = DoubleUtil.isBlank(outstock.getResizeWeight())?outstock.getTotalWeight():outstock.getResizeWeight();
		storageFeeEntity.setWeight(charge_weight);
		//塞箱数
		//Double charge_box = DoubleUtil.isBlank(outstock.getAdjustBoxnum())?outstock.getBoxnum():outstock.getAdjustBoxnum();
		storageFeeEntity.setBox(outstock.getAdjustBoxnum()==null?outstock.getBoxnum():outstock.getAdjustBoxnum());
		
		storageFeeEntity.setOrderNo(outstock.getOutstockNo());			//oms订单号
		storageFeeEntity.setProductType("");							//商品类型		
		storageFeeEntity.setStatus("0");								//状态
		storageFeeEntity.setOperateTime(outstock.getCreateTime());
		storageFeeEntity.setCostType("FEE_TYPE_GENEARL");
		storageFeeEntity.setUnitPrice(0d);
		storageFeeEntity.setCost(new BigDecimal(0));	//入仓金额
		storageFeeEntity.setSubjectCode(SubjectId);
		storageFeeEntity.setOtherSubjectCode(SubjectId);
		storageFeeEntity.setBizId(String.valueOf(outstock.getId()));//业务数据主键
		storageFeeEntity.setFeesNo(outstock.getFeesNo());
		
		XxlJobLogger.log("-->"+outstock.getId()+"温度类型的map【{0}】",temMap);

		
		if(StringUtils.isEmpty(outstock.getTemperatureTypeCode())){
			outstock.setTemperatureTypeCode("LD");
			storageFeeEntity.setTempretureType("LD");
		}
		else{
			storageFeeEntity.setTempretureType(outstock.getTemperatureTypeCode());
			
			XxlJobLogger.log("-->"+outstock.getId()+"业务数据中温度类型code"+outstock.getTemperatureTypeCode());

			outstock.setTemperatureTypeName(temMap.get(outstock.getTemperatureTypeCode()));
			
			XxlJobLogger.log("-->"+outstock.getId()+"业务数据中温度类型"+outstock.getTemperatureTypeName());
		}
		if(StringUtils.isEmpty(outstock.getTemperatureTypeName())){
			outstock.setTemperatureTypeName("冷冻");
		}
		storageFeeEntity.setDelFlag("0");
		storageFeeEntity.setParam1(TemplateTypeEnum.COMMON.getCode());
		storageFeeEntity.setLastModifyTime(JAppContext.currentTimestamp());
		return storageFeeEntity;
	}
	
	// 判断是否有不计算费用的
	@Override
	public boolean isNoExe(BizOutstockMasterEntity entity,FeesReceiveStorageEntity feeEntity) {
		return false;
	}
	
	// 合同在线获取模板
	@Override
	public ContractQuoteInfoVo getContractForWhat(BizOutstockMasterEntity entity) {

		ContractQuoteQueryInfoVo queryVo = new ContractQuoteQueryInfoVo();
		queryVo.setCustomerId(entity.getCustomerid());
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
			XxlJobLogger.log("-->"+entity.getId()+"合同在线无此合同"+ex.getMessage());
		}
		return modelEntity;
	}
	
	// 根据bms计算
	@Override
	public void calcuForBms(BizOutstockMasterEntity entity,FeesReceiveStorageEntity storageFeeEntity){
		XxlJobLogger.log("-->"+entity.getId()+"bms计算");
		//合同报价校验  false-不通过  true-通过
		if(validateData(entity, storageFeeEntity)){
			try{
				entity.setCalculateTime(JAppContext.currentTimestamp());
				storageFeeEntity.setCalculateTime(entity.getCalculateTime());
				String customerId=entity.getCustomerid();	
				
				//报价模板
				PriceGeneralQuotationEntity generalEntity=mapCusPrice.get(customerId);
				//计费单位 
				String unit=generalEntity.getFeeUnitCode();
				//计算方法
				double amount=0d;
				switch(priceType){
				case "PRICE_TYPE_NORMAL"://一口价				
					//如果计费单位是 单 -> 费用 = 模板价格
		            //如果计费单位是 件 -> 费用 = 商品件数*模板价格
		            //如果计费单位是 sku数 -> 费用 = 商家sku数*模板价格
					if("BILL".equals(unit)){//按单
						amount=generalEntity.getUnitPrice();				
					}else if("ITEMS".equals(unit)){//按件				
						amount=storageFeeEntity.getQuantity()*generalEntity.getUnitPrice();
					}else if("SKU".equals(unit)){//按sku
						amount=storageFeeEntity.getVarieties()*generalEntity.getUnitPrice();
					}else if("CARTON".equals(unit)){
						amount=storageFeeEntity.getBox()*generalEntity.getUnitPrice();
					}
					storageFeeEntity.setUnitPrice(generalEntity.getUnitPrice());
					storageFeeEntity.setParam3(generalEntity.getId()+"");
					break;
				case "PRICE_TYPE_STEP"://阶梯价
					PriceStepQuotationEntity stepQuoEntity=mapCusStepPrice.get(customerId);
					// 如果计费单位是 单 ->费用 = 单价 （根据仓库和温度和上下限帅选出唯一报价）
	                // 如果计费单位是 件或者sku数 
	                //   如果单价为空或为0，-> 费用 = 首价+（商品件数-首量）/续量 * 续价
	                //   如果单价>0 ， 费用=单价*数量
					if("BILL".equals(unit)){//按单
						amount=stepQuoEntity.getUnitPrice();
					}else if("ITEMS".equals(unit)){//按件	
						if(!DoubleUtil.isBlank(stepQuoEntity.getUnitPrice())){
							amount=storageFeeEntity.getQuantity()*stepQuoEntity.getUnitPrice();
							storageFeeEntity.setUnitPrice(stepQuoEntity.getUnitPrice());
						}else{
							amount=stepQuoEntity.getFirstNum()<storageFeeEntity.getQuantity()?stepQuoEntity.getFirstPrice()+(storageFeeEntity.getQuantity()-stepQuoEntity.getFirstNum())/stepQuoEntity.getContinuedItem()*stepQuoEntity.getContinuedPrice():stepQuoEntity.getFirstPrice();
						}
					}else if("SKU".equals(unit)){//按sku
						if(!DoubleUtil.isBlank(stepQuoEntity.getUnitPrice())){
							amount=storageFeeEntity.getVarieties()*stepQuoEntity.getUnitPrice();
							storageFeeEntity.setUnitPrice(stepQuoEntity.getUnitPrice());
						}else{
							amount=stepQuoEntity.getFirstNum()<storageFeeEntity.getVarieties()?stepQuoEntity.getFirstPrice()+(storageFeeEntity.getVarieties()-stepQuoEntity.getFirstNum())/stepQuoEntity.getContinuedItem()*stepQuoEntity.getContinuedPrice():stepQuoEntity.getFirstPrice();
						}
					}else if("CARTON".equals(unit)){//按箱
						if(!DoubleUtil.isBlank(stepQuoEntity.getUnitPrice())){
							amount=storageFeeEntity.getBox()*stepQuoEntity.getUnitPrice();
							storageFeeEntity.setUnitPrice(stepQuoEntity.getUnitPrice());
						}else{
							amount=stepQuoEntity.getFirstNum()<storageFeeEntity.getBox()?stepQuoEntity.getFirstPrice()+(storageFeeEntity.getBox()-stepQuoEntity.getFirstNum())/stepQuoEntity.getContinuedItem()*stepQuoEntity.getContinuedPrice():stepQuoEntity.getFirstPrice();
						}
					}			
					//判断封顶价
					if(!DoubleUtil.isBlank(stepQuoEntity.getCapPrice())){
						if(stepQuoEntity.getCapPrice()<amount){
							amount=stepQuoEntity.getCapPrice();
						}
					}
					
					storageFeeEntity.setParam3(stepQuoEntity.getId()+"");
					break;
				default:
					break;
				}
				
				storageFeeEntity.setCost(BigDecimal.valueOf(amount));
				storageFeeEntity.setParam4(priceType);
				storageFeeEntity.setBizType(entity.getextattr1());//用于判断是否是遗漏数据
				entity.setRemark("计算成功");
				storageFeeEntity.setCalcuMsg("计算成功");
				entity.setIsCalculated(CalculateState.Finish.getCode());
				storageFeeEntity.setIsCalculated(CalculateState.Finish.getCode());
				//feesList.add(storageFeeEntity);
			}catch(Exception ex){
				entity.setIsCalculated(CalculateState.Sys_Error.getCode());
				storageFeeEntity.setIsCalculated(CalculateState.Sys_Error.getCode());
				entity.setRemark("费用计算异常:"+ex.getMessage());
				storageFeeEntity.setCalcuMsg("费用计算异常:"+ex.getMessage());
				//feesList.add(storageFeeEntity);
			}
		}
		XxlJobLogger.log("-->"+entity.getId()+"id【{0}】费用【{1}】",entity.getId(),storageFeeEntity.getCost());
	}
	

	public void calcuForContract(BizOutstockMasterEntity entity,FeesReceiveStorageEntity feeEntity,ContractQuoteInfoVo contractQuoteInfoVo){
		XxlJobLogger.log("-->"+entity.getId()+"合同在线计算");
		feeEntity.setCalculateTime(JAppContext.currentTimestamp());
		Map<String, Object> con = new HashMap<>();
		//List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		con.put("quotationNo", contractQuoteInfoVo.getRuleCode());
		BillRuleReceiveEntity ruleEntity = receiveRuleRepository.queryOne(con);
		if (null == ruleEntity) {
			entity.setRemark("合同在线规则未绑定");
			feeEntity.setCalcuMsg("合同在线规则未绑定");
			feeEntity.setIsCalculated(CalculateState.Quote_Miss.getCode());
			entity.setIsCalculated(CalculateState.Quote_Miss.getCode());
			XxlJobLogger.log("-->"+entity.getId()+"计算不成功，合同在线规则未绑定");
		}
		//获取合同在线查询条件
		XxlJobLogger.log("-->"+entity.getId()+"计算时查询出的合同在线结果【{0}】",JSONObject.fromObject(contractQuoteInfoVo));

		Map<String, Object> cond = new HashMap<String, Object>();
		feesCalcuService.ContractCalcuService(entity, cond, ruleEntity.getRule(), ruleEntity.getQuotationNo());
		XxlJobLogger.log("-->"+entity.getId()+"获取报价参数"+cond);
		ContractQuoteInfoVo rtnQuoteInfoVo = contractQuoteInfoService.queryQuotes(contractQuoteInfoVo, cond);
		XxlJobLogger.log("获取合同在线报价结果【{0}】",JSONObject.fromObject(rtnQuoteInfoVo));
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
	
	
	protected boolean validateData(BizOutstockMasterEntity entity,FeesReceiveStorageEntity storageFeeEntity) {
		XxlJobLogger.log("-->"+entity.getId()+"数据主键ID:【{0}】  ",entity.getId());
		entity.setCalculateTime(JAppContext.currentTimestamp());
		Map<String,Object> map=new HashMap<String,Object>();
		String customerId=entity.getCustomerid();
		//FeesReceiveStorageEntity storageFeeEntity = initFeeEntity(entity);
		storageFeeEntity.setCalculateTime(JAppContext.currentTimestamp());
		long start = System.currentTimeMillis();// 系统开始时间
		long current = 0l;// 当前系统时间
		entity.setCalculateTime(JAppContext.currentTimestamp());
		
		//==============判断是否是B2B，B2B暂不计算
		if("1".equals(entity.getB2bFlag())){
			XxlJobLogger.log("-->"+entity.getId()+String.format("B2B订单暂不支持计算  订单号【%s】--商家【%s】", entity.getId(),entity.getCustomerid()));
			entity.setIsCalculated(CalculateState.No_Exe.getCode());
			storageFeeEntity.setIsCalculated(CalculateState.No_Exe.getCode());
			entity.setRemark(String.format("B2B订单暂不支持计算   订单号【%s】--商家【%s】", entity.getId(),entity.getCustomerid()));
			storageFeeEntity.setCalcuMsg(String.format("B2B订单暂不支持计算   订单号【%s】--商家【%s】", entity.getId(),entity.getCustomerid()));
			//feesList.add(storageFeeEntity);
			return false;
		}
		
		//=========================验证合同================
		PriceContractInfoEntity contractEntity =null;
		if(mapContact.containsKey(entity.getCustomerid())){
			contractEntity=mapContact.get(entity.getCustomerid());
		}else{
			Map<String,Object> aCondition=new HashMap<>();
			aCondition.put("customerid", entity.getCustomerid());
			aCondition.put("contractTypeCode", "CUSTOMER_CONTRACT");
		    contractEntity = jobPriceContractInfoService.queryContractByCustomer(aCondition);
		    if(contractEntity!=null){
			    mapContact.put(entity.getCustomerid(), contractEntity);
		    }
		}
		if(contractEntity == null || StringUtils.isEmpty(contractEntity.getContractCode())){
			XxlJobLogger.log("-->"+entity.getId()+String.format("未查询到合同  订单号【%s】--商家【%s】", entity.getId(),entity.getCustomerid()));
			entity.setIsCalculated(CalculateState.Contract_Miss.getCode());
			storageFeeEntity.setIsCalculated(CalculateState.Contract_Miss.getCode());
			entity.setRemark(String.format("未查询到合同  订单号【%s】--商家【%s】", entity.getId(),entity.getCustomerid()));
			storageFeeEntity.setCalcuMsg(String.format("未查询到合同  订单号【%s】--商家【%s】", entity.getId(),entity.getCustomerid()));
			//feesList.add(storageFeeEntity);
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
			XxlJobLogger.log("-->"+entity.getId()+String.format("未签约服务  订单号【%s】--商家【%s】", entity.getId(),entity.getCustomerid()));
			entity.setIsCalculated(CalculateState.Contract_Miss.getCode());
			storageFeeEntity.setIsCalculated(CalculateState.Contract_Miss.getCode());
			entity.setRemark(String.format("未签约服务  订单号【%s】--商家【%s】", entity.getId(),entity.getCustomerid()));
			storageFeeEntity.setCalcuMsg(String.format("未签约服务  订单号【%s】--商家【%s】", entity.getId(),entity.getCustomerid()));
			//feesList.add(storageFeeEntity);
			return false;
		}
		current = System.currentTimeMillis();
		XxlJobLogger.log("-->"+entity.getId()+"验证签约服务耗时：【{0}】毫秒  ",(current - start));
		
		//================================查询主报价==================================
		start = System.currentTimeMillis();// 验证报价是否配置
		/*验证报价 报价*/
		PriceGeneralQuotationEntity quoTemplete = null;
		if(!mapCusPrice.containsKey(customerId)){ //内存不包含，查询报价模板
			map.clear();
			map.put("subjectId",SubjectId);
			map.put("quotationNo", contractItems.get(0).getTemplateId());
			quoTemplete=priceGeneralQuotationRepository.query(map);
			if(quoTemplete != null){
				mapCusPrice.put(customerId, quoTemplete);//加入缓存
			}
		}
		else{
			quoTemplete=mapCusPrice.get(customerId);
		}
		if(quoTemplete==null){
			XxlJobLogger.log("-->"+entity.getId()+"报价未配置");
			entity.setIsCalculated(CalculateState.Quote_Miss.getCode());
			storageFeeEntity.setIsCalculated(CalculateState.Quote_Miss.getCode());
			entity.setRemark("报价未配置");
			storageFeeEntity.setCalcuMsg("报价未配置");
			//feesList.add(storageFeeEntity);
			return false;
		}
		//报价模板
		PriceGeneralQuotationEntity priceGeneral=quoTemplete;
		//报价类型（常规报价、阶梯报价）
		priceType=priceGeneral.getPriceType();
		
		
		//=====================================查询子报价==================================
		List<PriceStepQuotationEntity> list=new ArrayList<PriceStepQuotationEntity>();
		PriceStepQuotationEntity price=new PriceStepQuotationEntity();
		if(priceType.equals("PRICE_TYPE_STEP")){//阶梯价格
			//寻找阶梯报价
			map.clear();
			map.put("quotationId", priceGeneral.getId());
			//根据报价单位判断
			if("BILL".equals(priceGeneral.getFeeUnitCode())){//按单
				map.put("num", "1");
			}else if("ITEMS".equals(priceGeneral.getFeeUnitCode())){//按件
				map.put("num", entity.getTotalQuantity());
			}else if("SKU".equals(priceGeneral.getFeeUnitCode())){//按sku
				map.put("num", entity.getTotalVarieties());
			}
			//查询出的所有子报价
			list=repository.queryPriceStepByQuatationId(map);
			
			if(list==null || list.size() == 0){
				XxlJobLogger.log("-->"+entity.getId()+"阶梯报价未配置");
				entity.setIsCalculated(CalculateState.Quote_Miss.getCode());
				storageFeeEntity.setIsCalculated(CalculateState.Quote_Miss.getCode());
				entity.setRemark("阶梯报价未配置");
				storageFeeEntity.setCalcuMsg("阶梯报价未配置");
				//feesList.add(storageFeeEntity);
				return  false;
			}
			
			//封装数据的仓库和温度
			map.clear();
			map.put("warehouse_code", entity.getWarehouseCode());
			map.put("temperature_code", entity.getTemperatureTypeCode());
			
			price=storageQuoteFilterService.quoteFilter(list, map);
			
			mapCusStepPrice.put(customerId,price);

			if(price==null){
				XxlJobLogger.log("-->"+entity.getId()+"阶梯报价未配置");
				entity.setIsCalculated(CalculateState.Quote_Miss.getCode());
				storageFeeEntity.setIsCalculated(CalculateState.Quote_Miss.getCode());
				entity.setRemark("阶梯报价未配置");
				storageFeeEntity.setCalcuMsg("阶梯报价未配置");
				//feesList.add(storageFeeEntity);
				return  false;
			}
		}else if(priceType.equals("PRICE_TYPE_NORMAL")){//一口价
			
		}else{//报价类型缺失
			XxlJobLogger.log("-->"+entity.getId()+"报价类型未知");
			entity.setIsCalculated(CalculateState.Quote_Miss.getCode());
			storageFeeEntity.setIsCalculated(CalculateState.Quote_Miss.getCode());
			entity.setRemark("报价【"+quoTemplete.getQuotationNo()+"】类型未知");
			storageFeeEntity.setCalcuMsg("报价【"+quoTemplete.getQuotationNo()+"】类型未知");
			//feesList.add(storageFeeEntity);
			return  false;
		}
		current = System.currentTimeMillis();
		XxlJobLogger.log("-->"+entity.getId()+"验证报价耗时：【{0}】毫秒  ",(current - start));	
		return true;

	}

	@Override
	public void updateBatch(List<BizOutstockMasterEntity> billList,List<FeesReceiveStorageEntity> feesList) {

		long start = System.currentTimeMillis();// 系统开始时间
		long current = 0l;// 当前系统时间
		if(feesList.size()>0){
			bizOutstockMasterService.updateOutstockBatchByFees(feesList);
			current = System.currentTimeMillis();
		    XxlJobLogger.log("更新业务数据耗时：【{0}】毫秒 更新行数【{1}】 费用科目【{2}】",(current - start),billList.size(),SubjectId);
		    start = System.currentTimeMillis();// 系统开始时间
		    feesReceiveStorageService.InsertBatch(feesList);
		    current = System.currentTimeMillis();
			XxlJobLogger.log("新增费用数据耗时：【{0}】毫秒  更新行数【{1}】  费用科目【{2}】",(current - start),feesList.size(),SubjectId);
		}
	}

}
