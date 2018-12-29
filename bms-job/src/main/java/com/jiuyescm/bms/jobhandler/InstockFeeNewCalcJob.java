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
import com.jiuyescm.bms.common.enumtype.TemplateTypeEnum;
import com.jiuyescm.bms.general.entity.BmsBizInstockInfoEntity;
import com.jiuyescm.bms.general.entity.FeesReceiveStorageEntity;
import com.jiuyescm.bms.general.service.IBmsBizInstockInfoRepository;
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
import com.jiuyescm.bms.receivable.storage.service.IBizInstockDetailService;
import com.jiuyescm.bms.receivable.storage.service.IBizInstockMasterService;
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

@JobHander(value="instockFeeNewCalcJob")
@Service
public class InstockFeeNewCalcJob extends CommonJobHandler<BmsBizInstockInfoEntity,FeesReceiveStorageEntity>{

	//private String SubjectId = "wh_instock_service";//费用类型-入库验收服务费 编码 1001旧编码
	
	@Autowired private IBizInstockMasterService bizInstockMasterService;
	@Autowired private IBizInstockDetailService bizInstockDetailService;
	@Autowired private IPriceContractInfoService jobPriceContractInfoService;
	@Autowired private IFeesReceiveStorageService feesReceiveStorageService;
	@Autowired private SequenceService sequenceService;
	@Autowired private IPriceGeneralQuotationRepository priceGeneralQuotationRepository;
	@Autowired private IPriceStepQuotationRepository  repository;
	@Autowired private IReceiveRuleRepository receiveRuleRepository;
	@Autowired private IFeesCalcuService feesCalcuService;
	@Autowired private IPriceContractItemRepository priceContractItemRepository;
	@Autowired private IStorageQuoteFilterService storageQuoteFilterService;
	@Autowired private IContractQuoteInfoService contractQuoteInfoService;
	@Autowired private IBmsGroupSubjectService bmsGroupSubjectService;
	@Autowired private IBmsBizInstockInfoRepository bmsBizInstockInfoRepository;

	
	Map<String,PriceGeneralQuotationEntity> mapCusPrice=null;
	Map<String,PriceStepQuotationEntity> mapCusStepPrice=null;
	Map<String,PriceContractInfoEntity> mapContact=null;
	Map<String,BillRuleReceiveEntity> mapRule=null;
	String priceType="";
	
	protected void initConf(){
		mapCusPrice=new ConcurrentHashMap<String,PriceGeneralQuotationEntity>();
		mapCusStepPrice=new ConcurrentHashMap<String,PriceStepQuotationEntity>();
		mapContact=new ConcurrentHashMap<String,PriceContractInfoEntity>();
		mapRule=new ConcurrentHashMap<String,BillRuleReceiveEntity>();
	}
	
	@Override
	public Integer deleteFeesBatch(List<BmsBizInstockInfoEntity> list) {
		Map<String, Object> feesMap = new HashMap<String, Object>();
		List<String> feesNos = new ArrayList<String>();
		for (BmsBizInstockInfoEntity entity : list) {
			if(StringUtils.isNotEmpty(entity.getFeesNo())){
				feesNos.add(entity.getFeesNo());
			}
			else{
				entity.setFeesNo(sequenceService.getBillNoOne(BmsBizInstockInfoEntity.class.getName(), "STO", "0000000000"));
			}
		}
		try{
			if(feesNos.size()>0){
				feesMap.put("feesNos", feesNos);
				feesMap.put("subjectId", SubjectId);
				feesReceiveStorageService.deleteBatch(feesMap);
			}
		}
		catch(Exception ex){
			XxlJobLogger.log("批量删除费用失败-- {1}",ex.getMessage());
		}
		return null;
	}
	
	@Override
	protected List<BmsBizInstockInfoEntity> queryBillList(Map<String, Object> map) {
		Long current = System.currentTimeMillis();
		List<BmsBizInstockInfoEntity> bizList = bmsBizInstockInfoRepository.getInStockInfoList(map);
		if (bizList!=null && bizList.size()>0) {
			XxlJobLogger.log("【配送运单】查询行数【{0}】耗时【{1}】",bizList.size(),(System.currentTimeMillis()-current));
			initConf();
		}
		return bizList;
	}
	
	@Override
	public FeesReceiveStorageEntity initFeeEntity(BmsBizInstockInfoEntity instock){
		
		FeesReceiveStorageEntity storageFeeEntity = new FeesReceiveStorageEntity();
		
		double num=DoubleUtil.isBlank(instock.getAdjustQty())?instock.getTotalQty():instock.getAdjustQty();
		if(!DoubleUtil.isBlank(num)){
			storageFeeEntity.setQuantity(num);//商品数量
		}
		//重量
		Double weight = DoubleUtil.isBlank(instock.getAdjustWeight())?instock.getTotalWeight():instock.getAdjustWeight();
		storageFeeEntity.setWeight(weight);
		
		//塞箱数
		Double box=DoubleUtil.isBlank(instock.getAdjustBox())?instock.getTotalBox():instock.getTotalBox();
		if(!DoubleUtil.isBlank(box)){
			storageFeeEntity.setBox(box.intValue());
		}
		
		storageFeeEntity.setCreator("system");
		storageFeeEntity.setCreateTime(instock.getCreateTime());
		storageFeeEntity.setCustomerId(instock.getCustomerId());		//商家ID
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
		storageFeeEntity.setOtherSubjectCode(SubjectId);		//费用科目明细
		storageFeeEntity.setBizId(String.valueOf(instock.getId()));//业务数据主键
		storageFeeEntity.setUnitPrice(0d);
		storageFeeEntity.setCost(new BigDecimal(0));					//入仓金额
		storageFeeEntity.setFeesNo(instock.getFeesNo());
		storageFeeEntity.setParam1(TemplateTypeEnum.COMMON.getCode());
		storageFeeEntity.setDelFlag("0");
		return storageFeeEntity;
	}
	
	@Override
	protected String[] initSubjects() {
		Map<String, String> maps = bmsGroupSubjectService.getSubject("job_subject_instock");
		if (maps.size() == 0) {
			String[] str = {"wh_instock_work", "wh_b2c_handwork"};
			return str;
		}	
		String[] strs = new String[maps.size()];
		int i = 0;
		for (String val : maps.keySet()) {
			if (i < maps.keySet().size()) {
				strs[i] = val;
				i++;
			}
		}
		return strs;
	}

	@Override
	public boolean isJoin(BmsBizInstockInfoEntity t) {
		return true;
	}
	
	@Override
	public boolean isNoExe(BmsBizInstockInfoEntity entity,FeesReceiveStorageEntity feeEntity) {

		return false;
	}
	
	@Override
	public void calcu(BmsBizInstockInfoEntity entity, FeesReceiveStorageEntity feeEntity) {
		ContractQuoteInfoVo modelEntity = getContractForWhat(entity);
		if(modelEntity == null || StringUtil.isEmpty(modelEntity.getTemplateCode())){
			calcuForBms(entity, feeEntity);
		}
		else{
			XxlJobLogger.log("-->"+entity.getId()+"规则编号：  ", modelEntity.getRuleCode().trim());
			calcuForContract(entity,feeEntity,modelEntity);
		}
		
	}
	
	@Override
	public ContractQuoteInfoVo getContractForWhat(BmsBizInstockInfoEntity entity) {
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
			XxlJobLogger.log("-->{0}合同在线无此合同 {1}" , entity.getId() , ex.getMessage());
			entity.setRemark(entity.getRemark()+"合同在线"+ex.getMessage()+";");
		}
		return modelEntity;
	}
	
	@Override
	public void calcuForBms(BmsBizInstockInfoEntity entity,FeesReceiveStorageEntity feeEntity){
		XxlJobLogger.log("-->"+entity.getId()+"bms计算");
		//合同报价校验  false-不通过  true-通过
		try{
			if(validateData(entity, feeEntity)){
				if(mapCusPrice.containsKey(entity.getCustomerId()+SubjectId)){
					entity.setCalculateTime(JAppContext.currentTimestamp());
					feeEntity.setCalculateTime(entity.getCalculateTime());
					String customerId=entity.getCustomerId();
			
					//报价模板
					PriceGeneralQuotationEntity generalEntity=mapCusPrice.get(customerId+SubjectId);
					priceType=generalEntity.getPriceType();
					//计费单位 
					String unit=generalEntity.getFeeUnitCode();
					//数量
					double num=DoubleUtil.isBlank(entity.getAdjustQty())?entity.getTotalQty():entity.getAdjustQty();
							
					//计算方法
					double amount=0d;
					switch(priceType){
					case "PRICE_TYPE_NORMAL"://一口价				
			            //如果计费单位是 件 -> 费用 = 商品数量*模板单价
						//如果计费单位是 箱 -> 费用 = 商品箱数*模板单价
						//如果计费单位是 吨 -> 费用 = 商品重量*模板单价/1000
						//如果计费单位是 千克 -> 费用 = 商品重量*模板单价
						if ("ITEMS".equals(unit)) {
							amount=num*generalEntity.getUnitPrice();
						}else if ("CARTON".equals(unit)) {
							amount=feeEntity.getBox()*generalEntity.getUnitPrice();
						}else if ("TONS".equals(unit)) {
							if ((double)feeEntity.getWeight()/1000 < 1) {
								amount=1d*generalEntity.getUnitPrice();
							}else {
								amount=(double)feeEntity.getWeight()*generalEntity.getUnitPrice()/1000;
							}						
						}else if ("KILOGRAM".equals(unit)) {
							amount=feeEntity.getWeight()*generalEntity.getUnitPrice();
						}
						feeEntity.setUnitPrice(generalEntity.getUnitPrice());
						feeEntity.setParam3(generalEntity.getId()+"");
						break;
					case "PRICE_TYPE_STEP"://阶梯价						
						Map<String,Object> map=new HashMap<String,Object>();
						map.put("quotationId", generalEntity.getId());
						//根据报价单位判断
						map.put("num", num);			
						//查询出的所有子报价
						List<PriceStepQuotationEntity> list=repository.queryPriceStepByQuatationId(map);
						
						if(list==null || list.size() == 0){
							XxlJobLogger.log("-->"+entity.getId()+"阶梯报价未配置");
							entity.setIsCalculated(CalculateState.Quote_Miss.getCode());
							feeEntity.setIsCalculated(CalculateState.Quote_Miss.getCode());
							entity.setRemark(entity.getRemark()+"阶梯报价未配置;");
							feeEntity.setCalcuMsg("阶梯报价未配置");
							return;
						}
						
						//封装数据的仓库和温度
						map.clear();
						map.put("warehouse_code", entity.getWarehouseCode());
						PriceStepQuotationEntity stepQuoEntity=storageQuoteFilterService.quoteFilter(list, map);			
						
						if(stepQuoEntity==null){
							XxlJobLogger.log("-->"+entity.getId()+"阶梯报价未配置");
							entity.setIsCalculated(CalculateState.Quote_Miss.getCode());
							feeEntity.setIsCalculated(CalculateState.Quote_Miss.getCode());
							entity.setRemark(entity.getRemark()+"阶梯报价未配置;");
							feeEntity.setCalcuMsg("阶梯报价未配置");
							return;
						}
						
						XxlJobLogger.log("筛选后得到的报价结果【{0}】",JSONObject.fromObject(stepQuoEntity));

		                // 如果计费单位是 件
						if ("ITEMS".equals(unit)) {//按件
							if(!DoubleUtil.isBlank(stepQuoEntity.getUnitPrice())){
								feeEntity.setUnitPrice(stepQuoEntity.getUnitPrice());
								amount=num*stepQuoEntity.getUnitPrice();
							}else{
								amount=stepQuoEntity.getFirstNum()<num?stepQuoEntity.getFirstPrice()+(num-stepQuoEntity.getFirstNum())/stepQuoEntity.getContinuedItem()*stepQuoEntity.getContinuedPrice():stepQuoEntity.getFirstPrice();
							}
						}else if ("TONS".equals(unit)) {//按吨
							if(!DoubleUtil.isBlank(stepQuoEntity.getUnitPrice())){
								amount=(double)feeEntity.getWeight()*stepQuoEntity.getUnitPrice()/1000;
								feeEntity.setUnitPrice(stepQuoEntity.getUnitPrice());
							}else{
								amount=stepQuoEntity.getFirstNum()<feeEntity.getWeight()?stepQuoEntity.getFirstPrice()+(feeEntity.getWeight()-stepQuoEntity.getFirstNum())/stepQuoEntity.getContinuedItem()*stepQuoEntity.getContinuedPrice():stepQuoEntity.getFirstPrice();
								amount=(double)amount/1000;
							}		
						}else if("CARTON".equals(unit)){//按箱
							if(!DoubleUtil.isBlank(stepQuoEntity.getUnitPrice())){
								amount=feeEntity.getBox()*stepQuoEntity.getUnitPrice();
								feeEntity.setUnitPrice(stepQuoEntity.getUnitPrice());
							}else{
								amount=stepQuoEntity.getFirstNum()<feeEntity.getBox()?stepQuoEntity.getFirstPrice()+(feeEntity.getBox()-stepQuoEntity.getFirstNum())/stepQuoEntity.getContinuedItem()*stepQuoEntity.getContinuedPrice():stepQuoEntity.getFirstPrice();
							}
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
					//feeEntity.setBizType(entity.getextattr1());//判断是否是遗漏数据
					entity.setRemark(entity.getRemark()+"计算成功;");
					entity.setIsCalculated(CalculateState.Finish.getCode());
					feeEntity.setCalcuMsg("计算成功");
					feeEntity.setIsCalculated(CalculateState.Finish.getCode());
				}
			}
		}
		catch(Exception ex){
			feeEntity.setCalcuMsg(CalculateState.Sys_Error.getDesc());
			feeEntity.setIsCalculated(CalculateState.Sys_Error.getCode());
			entity.setIsCalculated(CalculateState.Sys_Error.getCode());
			XxlJobLogger.log("-->"+entity.getId()+"系统异常，费用【0】");
		}
	}


	public void calcuForContract(BmsBizInstockInfoEntity biz,FeesReceiveStorageEntity fee,ContractQuoteInfoVo contractQuoteInfoVo) {
		// TODO Auto-generated method stub
		XxlJobLogger.log("-->"+biz.getId()+"合同在线计算");
		fee.setCalculateTime(JAppContext.currentTimestamp());
		try{
			Map<String, Object> con = new HashMap<>();
			con.put("quotationNo", contractQuoteInfoVo.getRuleCode());
			BillRuleReceiveEntity ruleEntity = receiveRuleRepository.queryOne(con);
			if (null == ruleEntity) {
				biz.setRemark(biz.getRemark()+"合同在线规则未绑定;");
				fee.setCalcuMsg("合同在线规则未绑定");
				fee.setIsCalculated(CalculateState.Quote_Miss.getCode());
				biz.setIsCalculated(CalculateState.Quote_Miss.getCode());
				XxlJobLogger.log("-->"+biz.getId()+"计算不成功，合同在线规则未绑定");
			}
			
			//获取合同在线查询条件
			Map<String, Object> cond = new HashMap<String, Object>();
			feesCalcuService.ContractCalcuService(biz, cond, ruleEntity.getRule(), ruleEntity.getQuotationNo());
			XxlJobLogger.log("-->"+biz.getId()+"获取报价参数"+cond);
			ContractQuoteInfoVo rtnQuoteInfoVo = null;
			try {
				
			    if(cond == null || cond.size() == 0){
					XxlJobLogger.log("-->"+biz.getId()+"规则引擎拼接条件异常");
					fee.setIsCalculated(CalculateState.Sys_Error.getCode());
					biz.setIsCalculated(CalculateState.Sys_Error.getCode());
					biz.setRemark(biz.getRemark()+"系统规则引擎异常;");
					return;
				}
				
				rtnQuoteInfoVo = contractQuoteInfoService.queryQuotes(contractQuoteInfoVo, cond);
			} catch (BizException e) {
				// TODO: handle exception
				fee.setIsCalculated(CalculateState.Quote_Miss.getCode());
				biz.setIsCalculated(CalculateState.Quote_Miss.getCode());
				XxlJobLogger.log("-->"+biz.getId()+"获取合同在线报价异常:"+e.getMessage());
				biz.setRemark(biz.getRemark()+"获取合同在线报价异常:"+e.getMessage()+";");
				fee.setCalcuMsg("获取合同在线报价异常:"+e.getMessage());
				return;
			}
			
			XxlJobLogger.log("获取合同在线报价结果"+JSONObject.fromObject(rtnQuoteInfoVo));
			for (Map<String, String> map : rtnQuoteInfoVo.getQuoteMaps()) {
				XxlJobLogger.log("-->"+biz.getId()+"报价信息 -- "+map);
			}
			//调用规则计算费用
			Map<String, Object> feesMap = feesCalcuService.ContractCalcuService(fee, rtnQuoteInfoVo.getQuoteMaps(), ruleEntity.getRule(), ruleEntity.getQuotationNo());

			if(fee.getCost().compareTo(BigDecimal.ZERO) == 1){
				fee.setCalcuMsg(CalculateState.Finish.getDesc());
				fee.setIsCalculated(CalculateState.Finish.getCode());
				biz.setIsCalculated(CalculateState.Finish.getCode());
				XxlJobLogger.log("-->"+biz.getId()+"计算成功，费用【{0}】",fee.getCost());
			}
			else{
				fee.setCalcuMsg(CalculateState.Quote_Miss.getDesc());
				fee.setIsCalculated(CalculateState.Quote_Miss.getCode());
				biz.setIsCalculated(CalculateState.Quote_Miss.getCode());
				XxlJobLogger.log("-->"+biz.getId()+"计算不成功，费用【0】");
			}
		}
		catch(Exception ex){
			fee.setIsCalculated(CalculateState.Sys_Error.getCode());
			biz.setIsCalculated(CalculateState.Sys_Error.getCode());
			XxlJobLogger.log("-->"+biz.getId()+"计算不成功，费用0{0}",ex.getMessage());
			biz.setRemark(biz.getRemark()+"计算不成功:"+ex.getMessage()+";");
			fee.setCalcuMsg("计算不成功:"+ex.getMessage());
		}
		
	}
	
	protected boolean validateData(BmsBizInstockInfoEntity entity,
			FeesReceiveStorageEntity feeEntity) {
		
		XxlJobLogger.log("-->"+entity.getId()+"数据主键ID:【{0}】  ",entity.getId());
		Timestamp time=JAppContext.currentTimestamp();
		entity.setCalculateTime(time);
		Map<String,Object> map=new HashMap<String,Object>();
		String customerId=entity.getCustomerId();
		feeEntity.setCalculateTime(time);
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
			XxlJobLogger.log("-->"+entity.getId()+String.format("未查询到合同  订单号【%s】--商家【%s】", entity.getId(),customerId));
			entity.setIsCalculated(CalculateState.Contract_Miss.getCode());
			feeEntity.setIsCalculated(CalculateState.Contract_Miss.getCode());
			entity.setRemark(entity.getRemark()+"bms未查询到合同");
			feeEntity.setCalcuMsg("未查询到合同");
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
			entity.setRemark(entity.getRemark()+"bms未签约服务");
			feeEntity.setCalcuMsg("未签约服务");
			return false;
		}
		current = System.currentTimeMillis();
		XxlJobLogger.log("-->"+entity.getId()+"验证签约服务耗时：【{0}】毫秒  ",(current - start));		
		
		/*验证报价 报价*/
		start = System.currentTimeMillis();// 系统开始时间
		PriceGeneralQuotationEntity quoTemplete=null;
		if(!mapCusPrice.containsKey(customerId+SubjectId)){
			map.clear();
			map.put("subjectId",SubjectId);
			map.put("quotationNo", contractItems.get(0).getTemplateId());
			quoTemplete=priceGeneralQuotationRepository.query(map);
			if(quoTemplete != null){
				mapCusPrice.put(customerId+SubjectId, quoTemplete);//加入缓存
			}
		}else{
			quoTemplete=mapCusPrice.get(customerId+SubjectId);
		}
		if(quoTemplete==null){
			XxlJobLogger.log("-->"+entity.getId()+"报价未配置");
			entity.setIsCalculated(CalculateState.Quote_Miss.getCode());
			feeEntity.setIsCalculated(CalculateState.Quote_Miss.getCode());
			entity.setRemark(entity.getRemark()+"bms报价未配置");
			feeEntity.setCalcuMsg("报价未配置");
			return false;
		}
		
		
		
		/*//报价模板
		PriceGeneralQuotationEntity priceGeneral=quoTemplete;
		priceType=quoTemplete.getPriceType();
		List<PriceStepQuotationEntity> list=new ArrayList<PriceStepQuotationEntity>();
		PriceStepQuotationEntity price=new PriceStepQuotationEntity();
		if(priceType.equals("PRICE_TYPE_STEP")){//阶梯价格
			//寻找阶梯报价
			map.clear();
			map.put("quotationId", priceGeneral.getId());
			//根据报价单位判断
			map.put("num", DoubleUtil.isBlank(entity.getAdjustQty())?entity.getTotalQty():entity.getAdjustQty());			
			//查询出的所有子报价
			list=repository.queryPriceStepByQuatationId(map);
			
			if(list==null || list.size() == 0){
				XxlJobLogger.log("-->"+entity.getId()+"阶梯报价未配置");
				entity.setIsCalculated(CalculateState.Quote_Miss.getCode());
				feeEntity.setIsCalculated(CalculateState.Quote_Miss.getCode());
				entity.setRemark("阶梯报价未配置");
				feeEntity.setCalcuMsg("阶梯报价未配置");
				return  false;
			}
			
			//封装数据的仓库和温度
			map.clear();
			map.put("warehouse_code", entity.getWarehouseCode());
			price=storageQuoteFilterService.quoteFilter(list, map);			
			
			if(price==null){
				XxlJobLogger.log("-->"+entity.getId()+"阶梯报价未配置");
				entity.setIsCalculated(CalculateState.Quote_Miss.getCode());
				feeEntity.setIsCalculated(CalculateState.Quote_Miss.getCode());
				entity.setRemark("阶梯报价未配置");
				feeEntity.setCalcuMsg("阶梯报价未配置");
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
			feeEntity.setCalcuMsg("报价【"+priceGeneral.getQuotationNo()+"】类型未知");
			return  false;
		}*/
		current = System.currentTimeMillis();
		XxlJobLogger.log("-->"+entity.getId()+"验证报价耗时：【{0}】毫秒  ",(current - start));
		return true;
	}
	
	@Override
	public void updateBatch(List<BmsBizInstockInfoEntity> ts,List<FeesReceiveStorageEntity> fs) {

		long start = System.currentTimeMillis();// 系统开始时间
		long current = 0l;// 当前系统时间
		if (fs.size() > 0) {
			bmsBizInstockInfoRepository.updateInstockBatchByFees(fs);
			current = System.currentTimeMillis();
			XxlJobLogger.log("更新业务数据耗时：【{0}】毫秒  ",(current - start));
			start = System.currentTimeMillis();// 系统开始时间
			feesReceiveStorageService.InsertBatch(fs);
			current = System.currentTimeMillis();
			XxlJobLogger.log("新增费用数据耗时：【{0}】毫秒 ",(current - start));
		}	
	}

}
