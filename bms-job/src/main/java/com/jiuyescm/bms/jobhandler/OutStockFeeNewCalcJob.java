package com.jiuyescm.bms.jobhandler;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jiuyescm.bms.biz.storage.entity.BizInStockMasterEntity;
import com.jiuyescm.bms.biz.storage.entity.BizOutstockMasterEntity;
import com.jiuyescm.bms.calculate.base.IFeesCalcuService;
import com.jiuyescm.bms.chargerule.receiverule.entity.BillRuleReceiveEntity;
import com.jiuyescm.bms.common.enumtype.CalculateState;
import com.jiuyescm.bms.common.enumtype.TemplateTypeEnum;
import com.jiuyescm.bms.general.entity.FeesReceiveStorageEntity;
import com.jiuyescm.bms.general.service.IFeesReceiveStorageService;
import com.jiuyescm.bms.general.service.IPriceContractInfoService;
import com.jiuyescm.bms.general.service.IStorageQuoteFilterService;
import com.jiuyescm.bms.general.service.SequenceService;
import com.jiuyescm.bms.quotation.contract.entity.PriceContractInfoEntity;
import com.jiuyescm.bms.quotation.contract.entity.PriceContractItemEntity;
import com.jiuyescm.bms.quotation.contract.repository.imp.IPriceContractItemRepository;
import com.jiuyescm.bms.quotation.storage.entity.PriceGeneralQuotationEntity;
import com.jiuyescm.bms.quotation.storage.entity.PriceStepQuotationEntity;
import com.jiuyescm.bms.quotation.storage.repository.IPriceGeneralQuotationRepository;
import com.jiuyescm.bms.quotation.storage.repository.IPriceStepQuotationRepository;
import com.jiuyescm.bms.receivable.storage.service.IBizInstockMasterService;
import com.jiuyescm.bms.receivable.storage.service.IBizOutstockMasterService;
import com.jiuyescm.bms.rule.receiveRule.repository.IReceiveRuleRepository;
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
	
	private String SubjectId = "wh_b2c_work";		//费用类型-B2C订单操作费 1004原编码
	
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
	
	Map<String,PriceGeneralQuotationEntity> mapCusPrice=null;
	Map<String,PriceStepQuotationEntity> mapCusStepPrice=null;
	Map<String,PriceContractInfoEntity> mapContact=null;
	Map<String,BillRuleReceiveEntity> mapRule=null;
	
	String priceType="";

	// 查询业务数据	
	@Override
	protected List<BizOutstockMasterEntity> queryBillList(Map<String, Object> map) {
		List<BizOutstockMasterEntity> bizList = bizOutstockMasterService.query(map);
		return bizList;		
	}
	
	// 初始化
	@Override
	protected FeesReceiveStorageEntity initFeeEntity(BizOutstockMasterEntity outstock) {
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
		if(outstock.getResizeNum()!=null){
			storageFeeEntity.setQuantity(outstock.getResizeNum()==null?outstock.getTotalQuantity():outstock.getResizeNum());//商品数量
		}
		
		storageFeeEntity.setStatus("0");								//状态
		storageFeeEntity.setWeight(outstock.getTotalWeight());
		storageFeeEntity.setOperateTime(outstock.getCreateTime());
		storageFeeEntity.setCostType("FEE_TYPE_GENEARL");
		storageFeeEntity.setUnitPrice(0d);
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
		storageFeeEntity.setLastModifyTime(JAppContext.currentTimestamp());
		return storageFeeEntity;
	}
	
	// 判断是否有不计算费用的
	@Override
	protected boolean isNoExe(BizOutstockMasterEntity entity,FeesReceiveStorageEntity feeEntity) {
		return false;
	}
	
	// 合同在线获取模板
	@Override
	protected ContractQuoteInfoVo getContractForWhat(BizOutstockMasterEntity entity) {

		ContractQuoteQueryInfoVo queryVo = new ContractQuoteQueryInfoVo();
		queryVo.setCustomerId(entity.getCustomerid());
		queryVo.setBizTypeCode(ContractBizTypeEnum.STORAGE.getCode());
		queryVo.setSubjectCode(SubjectId);
		queryVo.setCurrentTime(entity.getCreateTime());
		queryVo.setWarehouseCode(entity.getWarehouseCode());
		XxlJobLogger.log("查询合同在线参数",JSONObject.fromObject(queryVo));
		ContractQuoteInfoVo modelEntity = new ContractQuoteInfoVo();
		try{
			modelEntity = contractQuoteInfoService.queryUniqueColumns(queryVo);
			XxlJobLogger.log("查询出的合同在线结果",JSONObject.fromObject(modelEntity));
		}
		catch(BizException ex){
			XxlJobLogger.log("合同在线无此合同",ex);
		}
		return modelEntity;
	}
	
	// 根据bms计算
	@Override
	protected void calcuForBms(BizOutstockMasterEntity entity,FeesReceiveStorageEntity storageFeeEntity){
		XxlJobLogger.log("bms计算");
		//合同报价校验  false-不通过  true-通过
		if(validateData(entity, storageFeeEntity)){
			try{
				entity.setCalculateTime(JAppContext.currentTimestamp());
				storageFeeEntity.setCalculateTime(entity.getCalculateTime());
				String customerId=entity.getCustomerid();	
		/*		if(null!=entity.getAdjustBoxnum()){//如果调整的不为空 则调整算钱
					entity.setBoxnum(entity.getAdjustBoxnum());
				}
				//原编码1007
				if("wh_b2b_work".equals(SubjectId)&&null!=entity.getBoxnum()){
					storageFeeEntity.setQuantity(entity.getBoxnum());
				}*/
				
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
						amount=entity.getTotalQuantity()*generalEntity.getUnitPrice();
					}else if("SKU".equals(unit)){//按sku
						amount=entity.getTotalVarieties()*generalEntity.getUnitPrice();
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
							amount=stepQuoEntity.getUnitPrice();
							storageFeeEntity.setUnitPrice(stepQuoEntity.getUnitPrice());
						}else{
							amount=stepQuoEntity.getFirstNum()<entity.getTotalQuantity()?stepQuoEntity.getFirstPrice()+(entity.getTotalQuantity()-stepQuoEntity.getFirstNum())/stepQuoEntity.getContinuedItem()*stepQuoEntity.getContinuedPrice():stepQuoEntity.getFirstPrice();
						}
					}else if("SKU".equals(unit)){//按sku
						if(!DoubleUtil.isBlank(stepQuoEntity.getUnitPrice())){
							amount=stepQuoEntity.getUnitPrice();
							storageFeeEntity.setUnitPrice(stepQuoEntity.getUnitPrice());
						}else{
							amount=stepQuoEntity.getFirstNum()<entity.getTotalVarieties()?stepQuoEntity.getFirstPrice()+(entity.getTotalVarieties()-stepQuoEntity.getFirstNum())/stepQuoEntity.getContinuedItem()*stepQuoEntity.getContinuedPrice():stepQuoEntity.getFirstPrice();
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
				entity.setIsCalculated(CalculateState.Finish.getCode());
				storageFeeEntity.setIsCalculated(CalculateState.Finish.getCode());
				//feesList.add(storageFeeEntity);
			}catch(Exception ex){
				entity.setIsCalculated(CalculateState.Sys_Error.getCode());
				storageFeeEntity.setIsCalculated(CalculateState.Sys_Error.getCode());
				entity.setRemark("费用计算异常:"+ex.getMessage());
				//feesList.add(storageFeeEntity);
			}
		}
	}
	
	@Override
	protected void calcuForContract(BizOutstockMasterEntity entity,FeesReceiveStorageEntity feeEntity){
		XxlJobLogger.log("合同在线计算");
		Map<String, Object> con = new HashMap<>();
		//List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		con.put("quotationNo", contractQuoteInfoVo.getRuleCode());
		BillRuleReceiveEntity ruleEntity = receiveRuleRepository.queryOne(con);
		if (null == ruleEntity) {
			entity.setRemark("合同在线规则未配置");
			feeEntity.setIsCalculated(CalculateState.Quote_Miss.getCode());
			entity.setIsCalculated(CalculateState.Quote_Miss.getCode());
			XxlJobLogger.log("计算不成功，合同在线规则未配置");
		}
		//获取合同在线查询条件
		Map<String, Object> cond = feesCalcuService.ContractCalcuService(entity, contractQuoteInfoVo.getUniqueMap(), ruleEntity.getRule(), ruleEntity.getQuotationNo());
		XxlJobLogger.log("获取报价参数",cond);
		ContractQuoteInfoVo rtnQuoteInfoVo = contractQuoteInfoService.queryQuotes(contractQuoteInfoVo, cond);
//		if (null != rtnQuoteInfoVo.getQuoteMaps()) {
//			list.add(rtnQuoteInfoVo.getQuoteMaps().get(0));
//		}
		XxlJobLogger.log("获取合同在线报价结果",JSONObject.fromObject(rtnQuoteInfoVo));
		for (Map<String, String> map : rtnQuoteInfoVo.getQuoteMaps()) {
			XxlJobLogger.log("报价信息 -- "+map);
		}
		//调用规则计算费用
		Map<String, Object> feesMap = feesCalcuService.ContractCalcuService(feeEntity, rtnQuoteInfoVo.getQuoteMaps(), ruleEntity.getRule(), ruleEntity.getQuotationNo());

		if(feeEntity.getCost().compareTo(BigDecimal.ZERO) == 1){
			feeEntity.setIsCalculated(CalculateState.Finish.getCode());
			entity.setIsCalculated(CalculateState.Finish.getCode());
			XxlJobLogger.log("计算成功，费用【{0}】",feeEntity.getCost());
		}
		else{
			feeEntity.setIsCalculated(CalculateState.Quote_Miss.getCode());
			entity.setIsCalculated(CalculateState.Quote_Miss.getCode());
			XxlJobLogger.log("计算不成功，费用【0】");
		}
	}
	
	
	protected boolean validateData(BizOutstockMasterEntity entity,FeesReceiveStorageEntity storageFeeEntity) {
		XxlJobLogger.log("数据主键ID:【{0}】  ",entity.getId());
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
			XxlJobLogger.log(String.format("B2B订单暂不支持计算  订单号【%s】--商家【%s】", entity.getId(),entity.getCustomerid()));
			entity.setIsCalculated(CalculateState.No_Exe.getCode());
			storageFeeEntity.setIsCalculated(CalculateState.No_Exe.getCode());
			entity.setRemark(String.format("B2B订单暂不支持计算   订单号【%s】--商家【%s】", entity.getId(),entity.getCustomerid()));
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
		    mapContact.put(entity.getCustomerid(), contractEntity);
		}
		if(contractEntity == null || StringUtils.isEmpty(contractEntity.getContractCode())){
			XxlJobLogger.log(String.format("未查询到合同  订单号【%s】--商家【%s】", entity.getId(),entity.getCustomerid()));
			entity.setIsCalculated(CalculateState.Contract_Miss.getCode());
			storageFeeEntity.setIsCalculated(CalculateState.Contract_Miss.getCode());
			entity.setRemark(String.format("未查询到合同  订单号【%s】--商家【%s】", entity.getId(),entity.getCustomerid()));
			//feesList.add(storageFeeEntity);
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
			XxlJobLogger.log(String.format("未签约服务  订单号【%s】--商家【%s】", entity.getId(),entity.getCustomerid()));
			entity.setIsCalculated(CalculateState.Contract_Miss.getCode());
			storageFeeEntity.setIsCalculated(CalculateState.Contract_Miss.getCode());
			entity.setRemark(String.format("未签约服务  订单号【%s】--商家【%s】", entity.getId(),entity.getCustomerid()));
			//feesList.add(storageFeeEntity);
			return false;
		}
		current = System.currentTimeMillis();
		XxlJobLogger.log("验证签约服务耗时：【{0}】毫秒  ",(current - start));
		
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
			XxlJobLogger.log("报价未配置");
			entity.setIsCalculated(CalculateState.Quote_Miss.getCode());
			storageFeeEntity.setIsCalculated(CalculateState.Quote_Miss.getCode());
			entity.setRemark("报价未配置");
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
				XxlJobLogger.log("阶梯报价未配置");
				entity.setIsCalculated(CalculateState.Quote_Miss.getCode());
				storageFeeEntity.setIsCalculated(CalculateState.Quote_Miss.getCode());
				entity.setRemark("阶梯报价未配置");
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
				XxlJobLogger.log("阶梯报价未配置");
				entity.setIsCalculated(CalculateState.Quote_Miss.getCode());
				storageFeeEntity.setIsCalculated(CalculateState.Quote_Miss.getCode());
				entity.setRemark("阶梯报价未配置");
				//feesList.add(storageFeeEntity);
				return  false;
			}
		}else if(priceType.equals("PRICE_TYPE_NORMAL")){//一口价
			
		}else{//报价类型缺失
			XxlJobLogger.log("报价类型未知");
			entity.setIsCalculated(CalculateState.Quote_Miss.getCode());
			storageFeeEntity.setIsCalculated(CalculateState.Quote_Miss.getCode());
			entity.setRemark("报价【"+quoTemplete.getQuotationNo()+"】类型未知");
			//feesList.add(storageFeeEntity);
			return  false;
		}
		current = System.currentTimeMillis();
		XxlJobLogger.log("验证报价耗时：【{0}】毫秒  ",(current - start));	
		return true;

	}

	@Override
	protected void updateBatch(List<BizOutstockMasterEntity> billList,List<FeesReceiveStorageEntity> feesList) {

		long start = System.currentTimeMillis();// 系统开始时间
		long current = 0l;// 当前系统时间
		bizOutstockMasterService.updateOutstockBatch(billList);
	    current = System.currentTimeMillis();
	    XxlJobLogger.log("更新业务数据耗时：【{0}】毫秒",(current - start));
	    start = System.currentTimeMillis();// 系统开始时间
	    feesReceiveStorageService.updateBatch(feesList);
		current = System.currentTimeMillis();
		XxlJobLogger.log("新增费用数据耗时：【{0}】毫秒  ",(current - start));
		
	}

}
