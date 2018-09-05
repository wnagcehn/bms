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
import com.jiuyescm.bms.calculate.base.IFeesCalcuService;
import com.jiuyescm.bms.chargerule.receiverule.entity.BillRuleReceiveEntity;
import com.jiuyescm.bms.common.enumtype.CalculateState;
import com.jiuyescm.bms.common.enumtype.TemplateTypeEnum;
import com.jiuyescm.bms.general.entity.FeesReceiveStorageEntity;
import com.jiuyescm.bms.general.service.IFeesReceiveStorageService;
import com.jiuyescm.bms.general.service.IPriceContractInfoService;
import com.jiuyescm.bms.general.service.IStorageQuoteFilterService;
import com.jiuyescm.bms.quotation.contract.entity.PriceContractInfoEntity;
import com.jiuyescm.bms.quotation.contract.entity.PriceContractItemEntity;
import com.jiuyescm.bms.quotation.contract.repository.imp.IPriceContractItemRepository;
import com.jiuyescm.bms.quotation.storage.entity.PriceGeneralQuotationEntity;
import com.jiuyescm.bms.quotation.storage.entity.PriceStepQuotationEntity;
import com.jiuyescm.bms.quotation.storage.repository.IPriceGeneralQuotationRepository;
import com.jiuyescm.bms.quotation.storage.repository.IPriceStepQuotationRepository;
import com.jiuyescm.bms.receivable.storage.service.IBizInstockMasterService;
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

@JobHander(value="instockWorkFeeNewCalcJob")
@Service
public class InstockWorkFeeNewCalcJob extends CommonJobHandler<BizInStockMasterEntity,FeesReceiveStorageEntity> {
	
	private String SubjectId = "wh_instock_work";// 入库操作费
	
	@Autowired private IContractQuoteInfoService contractQuoteInfoService;
	@Autowired private IBizInstockMasterService bizInstockMasterService;
	@Autowired private IPriceContractInfoService jobPriceContractInfoService;
	@Autowired private IFeesReceiveStorageService feesReceiveStorageService;
	@Autowired private IPriceGeneralQuotationRepository priceGeneralQuotationRepository;
	@Autowired private IPriceStepQuotationRepository  repository;
	@Autowired private IReceiveRuleRepository receiveRuleRepository;
	@Autowired private IFeesCalcuService feesCalcuService;
	@Autowired private IPriceContractItemRepository priceContractItemRepository;
	@Autowired private IStorageQuoteFilterService storageQuoteFilterService;
	
	Map<String,PriceStepQuotationEntity> mapCusStepPrice=null;
	Map<String,PriceContractInfoEntity> mapContact=null;
	Map<String,BillRuleReceiveEntity> mapRule=null;
	Map<String,PriceGeneralQuotationEntity> mapCusPrice=null;
	String priceType="";

	// 查询业务数据
	@Override
	protected List<BizInStockMasterEntity> queryBillList(Map<String, Object> map) {
		List<BizInStockMasterEntity> bizList = bizInstockMasterService.getInStockWorkList(map);
		return bizList;
	}
	
	// 初始化
	@Override
	protected FeesReceiveStorageEntity initFeeEntity(BizInStockMasterEntity instock) {
		FeesReceiveStorageEntity storageFeeEntity = new FeesReceiveStorageEntity();
		
		double num=DoubleUtil.isBlank(instock.getAdjustNum())?instock.getNum():instock.getAdjustNum();
		if(!DoubleUtil.isBlank(num)){
			storageFeeEntity.setQuantity(num);//商品数量
		}
		storageFeeEntity.setCreator("system");
		storageFeeEntity.setCreateTime(instock.getCreateTime());
		storageFeeEntity.setCustomerId(instock.getCustomerid());		//商家ID
		storageFeeEntity.setCustomerName(instock.getCustomerName());	//商家名称
		storageFeeEntity.setWarehouseCode(instock.getWarehouseCode());	//仓库ID
		storageFeeEntity.setWarehouseName(instock.getWarehouseName());	//仓库名称
		storageFeeEntity.setOrderType(instock.getInstockType());		//订单类型
		storageFeeEntity.setOrderNo(instock.getInstockNo());			//oms订单号
		storageFeeEntity.setProductType("");							//商品类型
		storageFeeEntity.setStatus("0");								//状态
		storageFeeEntity.setOperateTime(instock.getCreateTime());		//操作时间
		storageFeeEntity.setCostType("FEE_TYPE_GENEARL");
		storageFeeEntity.setSubjectCode(SubjectId);		//费用科目
		storageFeeEntity.setBizId(String.valueOf(instock.getId()));//业务数据主键
		storageFeeEntity.setCost(new BigDecimal(0));					//入仓金额
		storageFeeEntity.setUnitPrice(0d);
		storageFeeEntity.setFeesNo(instock.getFeesNo());
		storageFeeEntity.setParam1(TemplateTypeEnum.COMMON.getCode());
		storageFeeEntity.setDelFlag("0");
		storageFeeEntity.setOtherSubjectCode(SubjectId);
		return storageFeeEntity;
	}
	
	// 判断是否有不计算费用的
	@Override
	protected boolean isNoExe(BizInStockMasterEntity entity,FeesReceiveStorageEntity feeEntity) {
		return false;
	}
	
	// 合同在线获取模板
	@Override
	protected ContractQuoteInfoVo getContractForWhat(BizInStockMasterEntity entity) {

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
		}
		catch(BizException ex){
			XxlJobLogger.log("合同在线无此合同",ex);
		}
		return modelEntity;
	}
	
	// 根据bms计算
	@Override
	protected void calcuForBms(BizInStockMasterEntity entity,FeesReceiveStorageEntity storageFeeEntity){
		XxlJobLogger.log("bms计算");
		//合同报价校验  false-不通过  true-通过
		if(validateData(entity, storageFeeEntity)){
			try{
				entity.setCalculateTime(JAppContext.currentTimestamp());
				storageFeeEntity.setCalculateTime(entity.getCalculateTime());
				String customerId=entity.getCustomerid();
				
				//报价模板
				PriceGeneralQuotationEntity generalEntity=mapCusPrice.get(customerId);
				//数量
				double num=DoubleUtil.isBlank(entity.getAdjustNum())?entity.getNum():entity.getAdjustNum();
						
				//计算方法
				double amount=0d;
				switch(priceType){
				case "PRICE_TYPE_NORMAL"://一口价				
		            //如果计费单位是 件 -> 费用 = 商品数量*模板单价
					amount=num*generalEntity.getUnitPrice();							
					storageFeeEntity.setUnitPrice(generalEntity.getUnitPrice());
					storageFeeEntity.setParam3(generalEntity.getId()+"");
					break;
				case "PRICE_TYPE_STEP"://阶梯价
					PriceStepQuotationEntity stepQuoEntity=mapCusStepPrice.get(customerId);
	                // 如果计费单位是 件
					if(!DoubleUtil.isBlank(stepQuoEntity.getUnitPrice())){
						amount=stepQuoEntity.getUnitPrice();
						storageFeeEntity.setUnitPrice(stepQuoEntity.getUnitPrice());
					}else{
						amount=stepQuoEntity.getFirstNum()<num?stepQuoEntity.getFirstPrice()+(num-stepQuoEntity.getFirstNum())/stepQuoEntity.getContinuedItem()*stepQuoEntity.getContinuedPrice():stepQuoEntity.getFirstPrice();
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
				storageFeeEntity.setBizType(entity.getextattr1());//判断是否是遗漏数据
				entity.setRemark("计算成功");
				entity.setIsCalculated(CalculateState.Finish.getCode());
				storageFeeEntity.setIsCalculated(CalculateState.Finish.getCode());			
			}catch(Exception ex){
				entity.setIsCalculated(CalculateState.Sys_Error.getCode());
				storageFeeEntity.setIsCalculated(CalculateState.Sys_Error.getCode());
				entity.setRemark("费用计算异常:"+ex.getMessage());
				}
		}
	}
	
	@Override
	protected void calcuForContract(BizInStockMasterEntity entity,FeesReceiveStorageEntity feeEntity){
		XxlJobLogger.log("合同在线计算");
		Map<String, Object> con = new HashMap<>();
		//List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		con.put("quotationNo", contractQuoteInfoVo.getRuleCode());
		BillRuleReceiveEntity ruleEntity = receiveRuleRepository.queryOne(con);
		//获取合同在线查询条件
		Map<String, Object> cond = feesCalcuService.ContractCalcuService(entity, contractQuoteInfoVo.getUniqueMap(), ruleEntity.getRule(), ruleEntity.getQuotationNo());
		ContractQuoteInfoVo rtnQuoteInfoVo = contractQuoteInfoService.queryQuotes(contractQuoteInfoVo, cond);
//		if (null != rtnQuoteInfoVo.getQuoteMaps()) {
//			list.add(rtnQuoteInfoVo.getQuoteMaps().get(0));
//		}
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
	
	
	protected boolean validateData(BizInStockMasterEntity entity,
			FeesReceiveStorageEntity storageFeeEntity) {
		
		mapCusStepPrice=new HashMap<String,PriceStepQuotationEntity>();
		mapCusPrice=new HashMap<String,PriceGeneralQuotationEntity>();
		mapContact=new HashMap<String,PriceContractInfoEntity>();
		mapRule=new HashMap<String,BillRuleReceiveEntity>();
		
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
		    mapContact.put(customerId, contractEntity);
		}
		if(contractEntity == null || StringUtils.isEmpty(contractEntity.getContractCode())){
			XxlJobLogger.log(String.format("未查询到合同  订单号【%s】--商家【%s】", entity.getId(),customerId));
			entity.setIsCalculated(CalculateState.Contract_Miss.getCode());
			storageFeeEntity.setIsCalculated(CalculateState.Contract_Miss.getCode());
			entity.setRemark("未查询到合同");
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
			XxlJobLogger.log("未签约服务  订单号【{0}】--商家【{1}】", entity.getId(),entity.getCustomerid());
			entity.setIsCalculated(CalculateState.Contract_Miss.getCode());
			storageFeeEntity.setIsCalculated(CalculateState.Contract_Miss.getCode());
			entity.setRemark("未签约服务");
			//feesList.add(storageFeeEntity);
			return false;
		}
		current = System.currentTimeMillis();
		XxlJobLogger.log("验证签约服务耗时：【{0}】毫秒  ",(current - start));		
		
		/*验证报价 报价*/
		start = System.currentTimeMillis();// 系统开始时间
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
			XxlJobLogger.log("报价未配置");
			entity.setIsCalculated(CalculateState.Quote_Miss.getCode());
			storageFeeEntity.setIsCalculated(CalculateState.Quote_Miss.getCode());
			entity.setRemark("报价未配置");
			//feesList.add(storageFeeEntity);
			return false;
		}
		//报价模板
		PriceGeneralQuotationEntity priceGeneral=quoTemplete;
		priceType=quoTemplete.getPriceType();
		List<PriceStepQuotationEntity> list=new ArrayList<PriceStepQuotationEntity>();
		PriceStepQuotationEntity price=new PriceStepQuotationEntity();
		if(priceType.equals("PRICE_TYPE_STEP")){//阶梯价格
			//寻找阶梯报价
			map.clear();
			map.put("quotationId", priceGeneral.getId());
			//根据报价单位判断
			map.put("num", DoubleUtil.isBlank(entity.getAdjustNum())?entity.getNum():entity.getAdjustNum());			
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
			entity.setRemark("报价【"+priceGeneral.getQuotationNo()+"】类型未知");
			//feesList.add(storageFeeEntity);
			return  false;
		}
		current = System.currentTimeMillis();
		XxlJobLogger.log("验证报价耗时：【{0}】毫秒  ",(current - start));
		return true;
	}

	@Override
	protected void updateBatch(List<BizInStockMasterEntity> ts,List<FeesReceiveStorageEntity> fs) {

		long start = System.currentTimeMillis();// 系统开始时间
		long current = 0l;// 当前系统时间
		bizInstockMasterService.updateInstockWorkBatch(ts);
		current = System.currentTimeMillis();
		XxlJobLogger.log("更新业务数据耗时：【{0}】毫秒",(current - start));
		start = System.currentTimeMillis();// 系统开始时间
		feesReceiveStorageService.updateBatch(fs);
		current = System.currentTimeMillis();
		XxlJobLogger.log("新增费用数据耗时：【{0}】毫秒  ",(current - start));
		
	}
}
