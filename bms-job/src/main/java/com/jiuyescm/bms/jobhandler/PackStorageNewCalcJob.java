package com.jiuyescm.bms.jobhandler;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.jiuyescm.bms.biz.storage.entity.BizPackStorageEntity;
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
import com.jiuyescm.bms.receivable.storage.service.IBizPackStorageService;
import com.jiuyescm.bms.rule.receiveRule.repository.IReceiveRuleRepository;
import com.jiuyescm.common.utils.DoubleUtil;
import com.jiuyescm.contract.quote.api.IContractQuoteInfoService;
import com.jiuyescm.contract.quote.vo.ContractBizTypeEnum;
import com.jiuyescm.contract.quote.vo.ContractQuoteInfoVo;
import com.jiuyescm.contract.quote.vo.ContractQuoteQueryInfoVo;
import com.jiuyescm.exception.BizException;
import com.xxl.job.core.log.XxlJobLogger;

public class PackStorageNewCalcJob extends CommonJobHandler<BizPackStorageEntity,FeesReceiveStorageEntity> {

	private String SubjectId = "wh_material_storage";		//费用类型-耗材存储费 编码 1003原编码
	Map<String,PriceGeneralQuotationEntity> mapCusPrice=null;
	Map<String,List<PriceStepQuotationEntity>> mapCusStepPrice=null;
	Map<String,PriceContractInfoEntity> mapContact=null;
	Map<String,BillRuleReceiveEntity> mapRule=null;
	String priceType="";
	
	@Autowired private IContractQuoteInfoService contractQuoteInfoService;
	@Autowired private IBizPackStorageService bizPackStorageService;
	@Autowired private SequenceService sequenceService;
	@Autowired private IPriceContractInfoService jobPriceContractInfoService;
	@Autowired private IFeesReceiveStorageService feesReceiveStorageService;
	@Autowired private IPriceGeneralQuotationRepository priceGeneralQuotationRepository;
	@Autowired private IPriceStepQuotationRepository  repository;
	@Autowired private IReceiveRuleRepository receiveRuleRepository;
	@Autowired private IFeesCalcuService feesCalcuService;
	@Autowired private IPriceContractItemRepository priceContractItemRepository;
	@Autowired private IStorageQuoteFilterService storageQuoteFilterService;
	
	@Override
	protected List<BizPackStorageEntity> queryBillList(Map<String, Object> map) {
		long operateTime = System.currentTimeMillis();
		List<String> feesNos = new ArrayList<String>();
		Map<String, Object> feesMap = new HashMap<String, Object>();
		List<BizPackStorageEntity> bizList = bizPackStorageService.query(map);
		if(bizList == null || bizList.size() == 0){
			
		}
		else{
			for (BizPackStorageEntity entity : bizList) {
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
	protected FeesReceiveStorageEntity initFeeEntity(BizPackStorageEntity entity) {
		
		FeesReceiveStorageEntity feeEntity = new FeesReceiveStorageEntity();	
		feeEntity.setCreator("system");
		feeEntity.setCreateTime(entity.getCurTime());			//库存日期，导入的时候放到这个字段了
		feeEntity.setOperateTime(entity.getCurTime());
		feeEntity.setCustomerId(entity.getCustomerid());			//商家ID
		feeEntity.setCustomerName(entity.getCustomerName());		//商家名称
		feeEntity.setWarehouseCode(entity.getWarehouseCode());	//仓库ID
		feeEntity.setWarehouseName(entity.getWarehouseName());	//仓库名称
		feeEntity.setCostType("FEE_TYPE_GENEARL");				//费用类别 FEE_TYPE_GENEARL-通用 FEE_TYPE_MATERIAL-耗材 FEE_TYPE_ADD-增值
		feeEntity.setSubjectCode(SubjectId);						//费用科目
		feeEntity.setOtherSubjectCode(SubjectId);
		feeEntity.setProductType("");							//商品类型
		
		double num=DoubleUtil.isBlank(entity.getAdjustPalletNum())?entity.getPalletNum():entity.getAdjustPalletNum();
		if(!DoubleUtil.isBlank(num)){
			feeEntity.setQuantity(num);//商品数量
		}
		
		feeEntity.setStatus("0");		
		feeEntity.setUnit("PALLETS");
		feeEntity.setTempretureType(entity.getTemperatureTypeCode());//设置温度类型  zhangzw
		feeEntity.setBizId(String.valueOf(entity.getId()));						//业务数据主键
		feeEntity.setCost(new BigDecimal(0));						//入仓金额
		feeEntity.setUnitPrice(0d);
		feeEntity.setFeesNo(entity.getFeesNo());
		feeEntity.setParam1(TemplateTypeEnum.COMMON.getCode());
		feeEntity.setDelFlag("0");
		
		entity.setCalculateTime(feeEntity.getCalculateTime());
		return feeEntity;
		
	}
	@Override
	protected boolean isNoExe(BizPackStorageEntity t, FeesReceiveStorageEntity f) {
		return false;
	}
	@Override
	protected ContractQuoteInfoVo getContractForWhat(BizPackStorageEntity entity) {
		ContractQuoteQueryInfoVo queryVo = new ContractQuoteQueryInfoVo();
		queryVo.setCustomerId(entity.getCustomerid());
		queryVo.setBizTypeCode(ContractBizTypeEnum.STORAGE.getCode());
		queryVo.setSubjectCode(SubjectId);
		queryVo.setCurrentTime(entity.getCreateTime());
		
		ContractQuoteInfoVo modelEntity = new ContractQuoteInfoVo();
		try{
			modelEntity = contractQuoteInfoService.queryUniqueColumns(queryVo);
		}
		catch(BizException ex){
			XxlJobLogger.log("合同在线无此合同",ex);
		}
		return modelEntity;
	}
	@Override
	protected void calcuForBms(BizPackStorageEntity entity,FeesReceiveStorageEntity feeEntity) {
		try{
			if(validateData(entity, feeEntity)){
				String customerId=entity.getCustomerid();
				PriceGeneralQuotationEntity generalEntity=mapCusPrice.get(customerId);
				double num = feeEntity.getQuantity();
				double amount=0d;
				switch(priceType){
				case "PRICE_TYPE_NORMAL"://一口价				
		            // -> 费用 = 托数*模板单价
					amount = num * generalEntity.getUnitPrice();					
					feeEntity.setUnitPrice(generalEntity.getUnitPrice());
					feeEntity.setParam3(generalEntity.getId()+"");
					feeEntity.setIsCalculated(CalculateState.Finish.getCode());
					entity.setIsCalculated(CalculateState.Finish.getCode());
					entity.setRemark("计算成功");
					break;
				case "PRICE_TYPE_STEP"://阶梯价
					List<PriceStepQuotationEntity> list=mapCusStepPrice.get(customerId);
					PriceStepQuotationEntity price = new PriceStepQuotationEntity();
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("warehouse_code", entity.getWarehouseCode());
					map.put("temperature_code", entity.getTemperatureTypeCode());
					price=storageQuoteFilterService.quoteFilter(list, map);
					if(price == null){
						feeEntity.setIsCalculated(CalculateState.Sys_Error.getCode());
						entity.setIsCalculated(CalculateState.Sys_Error.getCode());
						entity.setRemark("系统错误，报价帅选异常");
						XxlJobLogger.log("系统错误，报价帅选异常");
						return;
					}
					else{
						if(!DoubleUtil.isBlank(price.getUnitPrice())){
							amount = num * price.getUnitPrice();
							feeEntity.setUnitPrice(price.getUnitPrice());
						}else{
							if(price.getFirstNum()<num){
								amount = price.getFirstPrice() + ((num-price.getFirstNum())/price.getContinuedItem())*price.getContinuedPrice();
							}
							else{
								amount = price.getFirstPrice();//重量界限内，直接取首价
							}
						}
					}
					
					//判断封顶价
					if(!DoubleUtil.isBlank(price.getCapPrice())){
						if(price.getCapPrice()<amount){
							amount=price.getCapPrice();
							entity.setRemark("计算成功,取封顶价");
						}
					}
					feeEntity.setParam3(price.getId()+"");
					feeEntity.setCost(BigDecimal.valueOf(amount));
					feeEntity.setParam4(priceType);
					feeEntity.setBizType(entity.getextattr1());//判断是否是遗漏数据
					entity.setRemark("计算成功");
					entity.setIsCalculated(CalculateState.Finish.getCode());
					feeEntity.setIsCalculated(CalculateState.Finish.getCode());	
					break;
				default:
					entity.setIsCalculated(CalculateState.Other.getCode());
					feeEntity.setIsCalculated(CalculateState.Other.getCode());	
					entity.setRemark("不支持【"+priceType+"】");
					break;
				}
			}
		}
		catch(Exception ex){
			entity.setIsCalculated(CalculateState.Sys_Error.getCode());
			feeEntity.setIsCalculated(CalculateState.Sys_Error.getCode());	
			entity.setRemark(ex.getMessage());
			XxlJobLogger.log("bms计算异常--",ex);
		}
		
	}
	@Override
	protected void calcuForContract(BizPackStorageEntity entity,FeesReceiveStorageEntity feeEntity) {
		try{
			Map<String, Object> con = new HashMap<>();
			con.put("quotationNo", contractQuoteInfoVo.getRuleCode());
			BillRuleReceiveEntity ruleEntity = receiveRuleRepository.queryOne(con);
			//获取合同在线查询条件
			Map<String, Object> cond = feesCalcuService.ContractCalcuService(entity, contractQuoteInfoVo.getUniqueMap(), ruleEntity.getRule(), ruleEntity.getQuotationNo());
			ContractQuoteInfoVo rtnQuoteInfoVo = contractQuoteInfoService.queryQuotes(contractQuoteInfoVo, cond);
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
		catch(Exception ex){
			feeEntity.setIsCalculated(CalculateState.Sys_Error.getCode());
			entity.setIsCalculated(CalculateState.Sys_Error.getCode());
			XxlJobLogger.log("计算不成功，费用【0】");
		}
		
	}
	@Override
	protected void updateBatch(List<BizPackStorageEntity> ts,List<FeesReceiveStorageEntity> fs) {
		long start = System.currentTimeMillis();// 系统开始时间
		long current = 0l;// 当前系统时间
		bizPackStorageService.updateBatch(ts);
		current = System.currentTimeMillis();
		XxlJobLogger.log("更新业务数据耗时：【{0}】毫秒  ",(current - start));
		start = System.currentTimeMillis();// 系统开始时间
		for(FeesReceiveStorageEntity feeEntity:fs){
			feesReceiveStorageService.Insert(feeEntity);
		}
		current = System.currentTimeMillis();
		XxlJobLogger.log("新增费用数据耗时：【{0}】毫秒",(current - start));
	}
	
	
	
	protected boolean validateData(BizPackStorageEntity entity,FeesReceiveStorageEntity feeEntity) {
		
		long start = System.currentTimeMillis();// 系统开始时间
		long current = 0l;// 当前系统时间
		XxlJobLogger.log("数据主键ID:【{0}】  ",entity.getId());
		
		//----验证合同
		PriceContractInfoEntity contractEntity = null;
		Map<String,Object> map=new HashMap<String,Object>();
		
		String customerId=entity.getCustomerid();
		if(mapContact.containsKey(customerId)){
			contractEntity=mapContact.get(customerId);
		}
		else{
			map.put("customerid", customerId);
			map.put("contractTypeCode", "CUSTOMER_CONTRACT");
		    contractEntity = jobPriceContractInfoService.queryContractByCustomer(map);
		    mapContact.put(entity.getCustomerid(), contractEntity);
		}
		if(contractEntity == null || StringUtils.isEmpty(contractEntity.getContractCode())){
			XxlJobLogger.log(String.format("未查询到合同  订单号【%s】--商家【%s】", entity.getId(),entity.getCustomerid()));
			entity.setIsCalculated(CalculateState.Contract_Miss.getCode());
			feeEntity.setIsCalculated(CalculateState.Contract_Miss.getCode());
			entity.setRemark(String.format("未查询到合同  订单号【%s】--商家【%s】", entity.getId(),entity.getCustomerid()));
			return false;
		}
		XxlJobLogger.log("验证合同   耗时【{0}】毫秒  合同编号 【{1}】",(System.currentTimeMillis() - start),contractEntity.getContractCode());
		
		//----验证签约服务
		start = System.currentTimeMillis();
		map.clear();
		map.put("contractCode", contractEntity.getContractCode());
		map.put("subjectId", SubjectId);
		List<PriceContractItemEntity> contractItems = priceContractItemRepository.query(map);
		if(contractItems == null || contractItems.size() == 0 || StringUtils.isEmpty(contractItems.get(0).getTemplateId())) {
			XxlJobLogger.log("未签约服务  订单号【{0}】--商家【{1}】", entity.getId(),entity.getCustomerid());
			entity.setIsCalculated(CalculateState.Contract_Miss.getCode());
			feeEntity.setIsCalculated(CalculateState.Contract_Miss.getCode());
			entity.setRemark("未签约服务");
			return false;
		}
		XxlJobLogger.log("验证签约服务耗时：【{0}】毫秒  ",(System.currentTimeMillis() - start));		
		
		start = System.currentTimeMillis();// 系统开始时间
		
		
		/*验证报价模板 */
		PriceGeneralQuotationEntity quoTemplete=null; //报价模板
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
			entity.setRemark("报价未配置");
			feeEntity.setIsCalculated(CalculateState.Quote_Miss.getCode());
			return false;
		}
		//报价模板
		//PriceGeneralQuotationEntity priceGeneral=quoTemplete;
		priceType=quoTemplete.getPriceType(); //获取报价类型  一口价（PRICE_TYPE_NORMAL）/阶梯价（PRICE_TYPE_STEP）
		List<PriceStepQuotationEntity> list=new ArrayList<PriceStepQuotationEntity>();
		//PriceStepQuotationEntity price=new PriceStepQuotationEntity();
		if(priceType.equals("PRICE_TYPE_STEP")){//阶梯价格
			//寻找阶梯报价
			map.clear();
			map.put("quotationId", quoTemplete.getId());
			//根据报价单位判断
			map.put("num", DoubleUtil.isBlank(entity.getAdjustPalletNum())?entity.getPalletNum():entity.getAdjustPalletNum());			
			//查询出的所有子报价
			list=repository.queryPriceStepByQuatationId(map);
			
			if(list==null || list.size() == 0){
				XxlJobLogger.log("阶梯报价未配置");
				entity.setIsCalculated(CalculateState.Quote_Miss.getCode());
				feeEntity.setIsCalculated(CalculateState.Quote_Miss.getCode());
				entity.setRemark("阶梯报价未配置");
				return false;
			}
			mapCusStepPrice.put(customerId, list);
			//封装数据的仓库和温度
			/*map.clear();
			map.put("warehouse_code", entity.getWarehouseCode());
			map.put("temperature_code", entity.getTemperatureTypeCode());
			price=storageQuoteFilterService.quoteFilter(list, map);
			mapCusStepPrice.put(customerId,price);
			
			if(price==null){
				XxlJobLogger.log("阶梯报价未配置");
				entity.setIsCalculated(CalculateState.Quote_Miss.getCode());
				entity.setRemark("阶梯报价未配置");
				feeEntity.setIsCalculated(CalculateState.Quote_Miss.getCode());
				return  false;
			}*/
		}else if(priceType.equals("PRICE_TYPE_NORMAL")){//一口价
			XxlJobLogger.log("一口价计费");
		}else{//报价类型缺失
			XxlJobLogger.log("报价类型未知");
			entity.setIsCalculated(CalculateState.Quote_Miss.getCode());
			feeEntity.setIsCalculated(CalculateState.Quote_Miss.getCode());
			entity.setRemark("报价【"+quoTemplete.getQuotationNo()+"】类型未知");
			return  false;
		}
		current = System.currentTimeMillis();
		XxlJobLogger.log("验证报价耗时：【{0}】毫秒  ",(current - start));
		return true;

	}

}