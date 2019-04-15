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
import com.jiuyescm.bms.biz.storage.entity.BizProductStorageEntity;
import com.jiuyescm.bms.drools.IFeesCalcuService;
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
import com.jiuyescm.bms.receivable.storage.service.IBizProductStorageService;
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

@JobHander(value="productStorageNewCalcJob")
@Service
public class ProductStorageNewCalcJob extends CommonJobHandler<BizProductStorageEntity,FeesReceiveStorageEntity>{

	//private String SubjectId = "wh_product_storage";		//费用类型-商品存储费
	
	@Autowired private IContractQuoteInfoService contractQuoteInfoService;
	@Autowired private IBizProductStorageService bizProductStorageService;
	@Autowired private IFeesCalcuService feesCalcuService;
	@Autowired private IPriceContractInfoService jobPriceContractInfoService;
	@Autowired private SequenceService sequenceService;
	@Autowired private IPriceGeneralQuotationRepository priceGeneralQuotationRepository;
	@Autowired private IPriceStepQuotationRepository  repository;
	@Autowired private IReceiveRuleRepository receiveRuleRepository;
	@Autowired private IFeesReceiveStorageService feesReceiveStorageService;
	@Autowired private IPriceContractItemRepository priceContractItemRepository;
	@Autowired private IBmsGroupService bmsGroupService;
	@Autowired private IBmsGroupCustomerService bmsGroupCustomerService;
	@Autowired private IStorageQuoteFilterService storageQuoteFilterService;
	@Autowired private ISystemCodeService systemCodeService;
	@Autowired private IBmsGroupSubjectService bmsGroupSubjectService;

	
	List<SystemCodeEntity> scList;
	Map<String,PriceGeneralQuotationEntity> mapCusPrice=null;
	Map<String,PriceStepQuotationEntity> mapCusStepPrice=null;
	Map<String,PriceContractInfoEntity> mapContact=null;
	Map<String,BillRuleReceiveEntity> mapRule=null;
	List<String> cusList=null;
	String priceType="";
	List<SystemCodeEntity> systemCodeList=null;
	Map<String, String> temMap=null;
	
	@Override
	protected List<BizProductStorageEntity> queryBillList(Map<String, Object> map) {
		Long current = System.currentTimeMillis();
		List<BizProductStorageEntity> bizList = bizProductStorageService.query(map);
		//有待计算的数据时初始化配置
		if(bizList!=null && bizList.size() > 0){
			XxlJobLogger.log("【配送运单】查询行数【{0}】耗时【{1}】",bizList.size(),(System.currentTimeMillis()-current));
			initConf();
		}
		return bizList;	
	}
	
	@Override
	protected String[] initSubjects() {
		//这里的科目应该在科目组中配置,动态查询
		//wh_product_storage(商品存储费 )
		Map<String,String> map=bmsGroupSubjectService.getSubject("job_subject_product");
		if(map.size() == 0){
			String[] strs = {"wh_product_storage"};
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
	public boolean isJoin(BizProductStorageEntity entity) {
		return true;		
	}
	
	
	@Override
	public void calcu(BizProductStorageEntity entity, FeesReceiveStorageEntity feeEntity) {
		ContractQuoteInfoVo modelEntity = getContractForWhat(entity);
		if(modelEntity == null || StringUtil.isEmpty(modelEntity.getTemplateCode())){
			calcuForBms(entity, feeEntity);
		}
		else{
			XxlJobLogger.log("-->"+entity.getId()+"规则编号：  "+modelEntity.getRuleCode().trim());
			calcuForContract(entity,feeEntity,modelEntity);
		}
		
	}
	
	protected void initConf(){
		mapCusStepPrice=new ConcurrentHashMap<String,PriceStepQuotationEntity>();
		mapCusPrice=new ConcurrentHashMap<String,PriceGeneralQuotationEntity>();
		mapContact=new ConcurrentHashMap<String,PriceContractInfoEntity>();
		mapRule=new ConcurrentHashMap<String,BillRuleReceiveEntity>();
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("typeCode", "NO_FEES_DELIVER");
		List<SystemCodeEntity> systemCodeList = systemCodeService.querySysCodes(map);
		temMap =new LinkedHashMap<String,String>();
		if(systemCodeList!=null && systemCodeList.size()>0){
			for(int i=0;i<systemCodeList.size();i++){
				map.put(systemCodeList.get(i).getCode(), systemCodeList.get(i).getCodeName());
			}
		}
	}
	
	@Override
	public FeesReceiveStorageEntity initFeeEntity(BizProductStorageEntity entity){		
		entity.setRemark("");
		FeesReceiveStorageEntity storageFeeEntity = new FeesReceiveStorageEntity();	
		storageFeeEntity.setCreator("system");
		storageFeeEntity.setCreateTime(entity.getCreateTime());
		storageFeeEntity.setOperateTime(entity.getCreateTime());
		storageFeeEntity.setCustomerId(entity.getCustomerid());		    //商家ID
		storageFeeEntity.setCustomerName(entity.getCustomerName());		//商家名称
		storageFeeEntity.setWarehouseCode(entity.getWarehouseCode());	//仓库ID
		storageFeeEntity.setWarehouseName(entity.getWarehouseName());	//仓库名称
		storageFeeEntity.setCostType("FEE_TYPE_GENEARL");			    //费用类别 FEE_TYPE_GENEARL-通用 FEE_TYPE_MATERIAL-耗材 FEE_TYPE_ADD-增值
		storageFeeEntity.setSubjectCode(SubjectId);		                //费用科目 todo
		storageFeeEntity.setOtherSubjectCode(SubjectId);
		storageFeeEntity.setProductType("");							//商品类型
		if(entity.getAqty()!=null){
			storageFeeEntity.setQuantity(entity.getAqty());             //商品数量
		}

		storageFeeEntity.setStatus("0");								//状态
		storageFeeEntity.setWeight(entity.getWeight());
		storageFeeEntity.setTempretureType(entity.getTemperature());	//温度类型
		storageFeeEntity.setProductNo(entity.getProductId());			//商品编码
		storageFeeEntity.setProductName(entity.getProductName());		//商品名称
		storageFeeEntity.setUnit("ITEMS");
		storageFeeEntity.setBizId(String.valueOf(entity.getId()));      //业务数据主键
		storageFeeEntity.setCost(new BigDecimal(0));					//入仓金额
		storageFeeEntity.setUnitPrice(0d);
		storageFeeEntity.setFeesNo(entity.getFeesNo());		
		storageFeeEntity.setParam1(TemplateTypeEnum.COMMON.getCode());
		storageFeeEntity.setDelFlag("0");
		//转换温度
		if(StringUtils.isNotBlank(entity.getTemperature())){
			entity.setTemperature(temMap.get(entity.getTemperature()));
		}
		return storageFeeEntity;
	}

	@Override
	public boolean isNoExe(BizProductStorageEntity entity,FeesReceiveStorageEntity feeEntity) {
		//指定的商家
		Map<String,Object> map= new HashMap<String, Object>();
		map.put("groupCode", "customer_unit");
		map.put("bizType", "group_customer");
		BmsGroupVo bmsGroup=bmsGroupService.queryOne(map);
		if(bmsGroup!=null){
			cusList=bmsGroupCustomerService.queryCustomerByGroupId(bmsGroup.getId());
		}
		
		//只有按件商家才计算
		if(cusList.size()==0 || !cusList.contains(entity.getCustomerid())){
			XxlJobLogger.log("-->"+entity.getId()+"只有按件商家收取按件存储费,其他不计费");
			entity.setIsCalculated(CalculateState.No_Exe.getCode());
			feeEntity.setIsCalculated(CalculateState.No_Exe.getCode());
			entity.setRemark(entity.getRemark()+"只有按件商家收取按件存储费,其他不计费;");
			return true;
		}
		return false;
	}
	
	@Override
	public ContractQuoteInfoVo getContractForWhat(BizProductStorageEntity entity) {
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
			XxlJobLogger.log("-->"+entity.getId()+"合同在线无此合同:"+ex.getMessage());
			entity.setRemark(entity.getRemark()+"合同在线"+ex.getMessage()+";");
		}
		return modelEntity;
	}
	
	@Override
	public void calcuForBms(BizProductStorageEntity entity,FeesReceiveStorageEntity feeEntity){
		XxlJobLogger.log("-->"+entity.getId()+"bms计算");
		//合同报价校验  false-不通过  true-通过
		if(validateData(entity, feeEntity)){
			if(mapCusPrice.containsKey(entity.getCustomerid()+SubjectId)){
				entity.setCalculateTime(JAppContext.currentTimestamp());
				feeEntity.setCalculateTime(entity.getCalculateTime());
				
				String customerId=entity.getCustomerid();
				//报价模板
				PriceGeneralQuotationEntity generalEntity=mapCusPrice.get(customerId+SubjectId);	
				if("ITEMS".equals(generalEntity.getFeeUnitCode())){//按件
					feeEntity.setQuantity(entity.getAqty());
				}else if("KILOGRAM".equals(generalEntity.getFeeUnitCode())){//按重量
					feeEntity.setQuantity(entity.getWeight()*entity.getAqty());
				}
				priceType=generalEntity.getPriceType();
				
				//计算方法
				double amount=0d;
				switch(priceType){
				case "PRICE_TYPE_NORMAL"://一口价				
		            // -> 费用 = 数量*模板单价
					if("ITEMS".equals(generalEntity.getFeeUnitCode())){//按件
						amount=entity.getAqty()*generalEntity.getUnitPrice();
					}else if("KILOGRAM".equals(generalEntity.getFeeUnitCode())){//按重量
						amount=entity.getWeight()*entity.getAqty()*generalEntity.getUnitPrice();
					}
					feeEntity.setUnitPrice(generalEntity.getUnitPrice());
					feeEntity.setParam3(generalEntity.getId()+"");
					break;
				case "PRICE_TYPE_STEP"://阶梯价		
					Map<String,Object> map=new HashMap<String,Object>();

					List<PriceStepQuotationEntity> list=new ArrayList<PriceStepQuotationEntity>();
					//寻找阶梯报价
					map.clear();
					map.put("quotationId", generalEntity.getId());
					//根据报价单位判断
					if("ITEMS".equals(generalEntity.getFeeUnitCode())){//按件
						map.put("num", entity.getAqty());	
					}else if("KILOGRAM".equals(generalEntity.getFeeUnitCode())){//按重量
						map.put("num", entity.getWeight()*entity.getAqty());
					}
								
					//查询出的所有子报价
					list=repository.queryPriceStepByQuatationId(map);
						
					if(list==null || list.size() == 0){
						XxlJobLogger.log("-->"+entity.getId()+"阶梯报价未配置");
						entity.setIsCalculated(CalculateState.Quote_Miss.getCode());
						feeEntity.setIsCalculated(CalculateState.Quote_Miss.getCode());
						entity.setRemark(entity.getRemark()+"阶梯报价未配置;");
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
						return;
					}
					
					XxlJobLogger.log("筛选后得到的报价结果【{0}】",JSONObject.fromObject(stepQuoEntity));

			
					if("ITEMS".equals(generalEntity.getFeeUnitCode())){//按件
						if(!DoubleUtil.isBlank(stepQuoEntity.getUnitPrice())){
							feeEntity.setUnitPrice(stepQuoEntity.getUnitPrice());
							amount=entity.getAqty()*stepQuoEntity.getUnitPrice();
						}else{
							amount=stepQuoEntity.getFirstNum()<entity.getAqty()?stepQuoEntity.getFirstPrice()+(entity.getAqty()-stepQuoEntity.getFirstNum())/stepQuoEntity.getContinuedItem()*stepQuoEntity.getContinuedPrice():stepQuoEntity.getFirstPrice();
						}
					}else if("KILOGRAM".equals(generalEntity.getFeeUnitCode())){//按重量
						if(!DoubleUtil.isBlank(stepQuoEntity.getUnitPrice())){
							feeEntity.setUnitPrice(stepQuoEntity.getUnitPrice());
							amount=entity.getWeight()*entity.getAqty()*stepQuoEntity.getUnitPrice();
						}else{
							amount=stepQuoEntity.getFirstNum()<entity.getWeight()*entity.getAqty()?stepQuoEntity.getFirstPrice()+(entity.getWeight()*entity.getAqty()-stepQuoEntity.getFirstNum())/stepQuoEntity.getContinuedItem()*stepQuoEntity.getContinuedPrice():stepQuoEntity.getFirstPrice();
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
				feeEntity.setBizType(entity.getextattr1());//判断是否是遗漏数据
				entity.setRemark(entity.getRemark()+"计算成功;");
				entity.setIsCalculated(CalculateState.Finish.getCode());
				feeEntity.setIsCalculated(CalculateState.Finish.getCode());				
				XxlJobLogger.log("-->"+entity.getId()+String.format("====================================[%s]计算完毕============================================",entity.getId()));
			}
		}
	}

	public void calcuForContract(BizProductStorageEntity entity,FeesReceiveStorageEntity feeEntity,ContractQuoteInfoVo contractQuoteInfoVo){
		XxlJobLogger.log("-->"+entity.getId()+"合同在线计算");
		try{
			Map<String, Object> con = new HashMap<>();
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
			XxlJobLogger.log("-->"+entity.getId()+"获取报价参数"+cond);
			ContractQuoteInfoVo rtnQuoteInfoVo = null;		
			try {
			    if(cond == null || cond.size() == 0){
					XxlJobLogger.log("-->"+entity.getId()+"规则引擎拼接条件异常");
					feeEntity.setIsCalculated(CalculateState.Sys_Error.getCode());
					entity.setIsCalculated(CalculateState.Sys_Error.getCode());
					entity.setRemark(entity.getRemark()+"系统规则引擎异常;");
					return;
				}
				
				rtnQuoteInfoVo = contractQuoteInfoService.queryQuotes(contractQuoteInfoVo, cond);
			} catch (BizException e) {
				// TODO: handle exception
				feeEntity.setIsCalculated(CalculateState.Quote_Miss.getCode());
				entity.setIsCalculated(CalculateState.Quote_Miss.getCode());
				XxlJobLogger.log("-->"+entity.getId()+"获取合同在线报价异常:"+e.getMessage());
				entity.setRemark(entity.getRemark()+"获取合同在线报价异常:"+e.getMessage()+";");
				feeEntity.setCalcuMsg("获取合同在线报价异常:"+e.getMessage());
				return;
			}
			//ContractQuoteInfoVo rtnQuoteInfoVo = contractQuoteInfoService.queryQuotes(contractQuoteInfoVo, cond);
			XxlJobLogger.log("-->"+entity.getId()+"获取合同在线报价结果"+JSONObject.fromObject(rtnQuoteInfoVo));
			for (Map<String, String> map : rtnQuoteInfoVo.getQuoteMaps()) {
				XxlJobLogger.log("-->"+entity.getId()+"报价信息 -- "+map);
			}
			//调用规则计算费用
			Map<String, Object> feesMap = feesCalcuService.ContractCalcuService(feeEntity, rtnQuoteInfoVo.getQuoteMaps(), ruleEntity.getRule(), ruleEntity.getQuotationNo());

			if(feeEntity.getCost().compareTo(BigDecimal.ZERO) == 1){
				feeEntity.setIsCalculated(CalculateState.Finish.getCode());
				entity.setIsCalculated(CalculateState.Finish.getCode());
				entity.setRemark(entity.getRemark()+"计算成功;");
				XxlJobLogger.log("-->"+entity.getId()+"计算成功，费用【{0}】",feeEntity.getCost());
			}
			else{
				feeEntity.setIsCalculated(CalculateState.Quote_Miss.getCode());
				entity.setIsCalculated(CalculateState.Quote_Miss.getCode());
				entity.setRemark(entity.getRemark()+"计算不成功，费用【0】;");
				XxlJobLogger.log("-->"+entity.getId()+"计算不成功，费用【0】");
			}
		}
		catch(Exception ex){
			feeEntity.setIsCalculated(CalculateState.Sys_Error.getCode());
			entity.setIsCalculated(CalculateState.Sys_Error.getCode());
			entity.setRemark(entity.getRemark()+"计算不成功，费用【0】"+ex.getMessage()+";");
			feeEntity.setCalcuMsg("计算不成功:"+ex.getMessage());
			XxlJobLogger.log("-->"+entity.getId()+"计算不成功，费用0{0}",ex.getMessage());
		}
		
	}

	
	
	@Override
	public void updateBatch(List<BizProductStorageEntity> ts,List<FeesReceiveStorageEntity> fs) {

		long start = System.currentTimeMillis();// 系统开始时间
		long current = 0L;// 当前系统时间
		bizProductStorageService.updateBatch(ts);
		current = System.currentTimeMillis();
		XxlJobLogger.log("更新业务数据耗时：【{0}】毫秒",(current - start));
		start = System.currentTimeMillis();// 系统开始时间
		feesReceiveStorageService.InsertBatch(fs);
		current = System.currentTimeMillis();
		XxlJobLogger.log("新增费用数据耗时：【{0}】毫秒  ",(current - start));
		
	}

	/**
	 * 验证业务数据
	 */
	protected boolean validateData(BizProductStorageEntity entity,
			FeesReceiveStorageEntity fee) {
		
		XxlJobLogger.log("-->"+entity.getId()+"数据主键ID:【{0}】  ",entity.getId());
		entity.setCalculateTime(JAppContext.currentTimestamp());
		Map<String,Object> map=new HashMap<String,Object>();
		String customerId=entity.getCustomerid();
		FeesReceiveStorageEntity storageFeeEntity = initFeeEntity(entity);
		storageFeeEntity.setCalculateTime(JAppContext.currentTimestamp());
		
		long start = System.currentTimeMillis();// 系统开始时间
		long current = 0L;// 当前系统时间
		
		/*验证商家是否合同存在*/
		PriceContractInfoEntity contractEntity=null;
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
			entity.setRemark(entity.getRemark()+String.format("bms未查询到合同  订单号【%s】--商家【%s】;", entity.getId(),entity.getCustomerid()));
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
			XxlJobLogger.log("-->"+entity.getId()+"未签约服务  订单号【{0}】--商家【{1}】", entity.getId(),entity.getCustomerid());
			entity.setIsCalculated(CalculateState.Contract_Miss.getCode());
			storageFeeEntity.setIsCalculated(CalculateState.Contract_Miss.getCode());
			entity.setRemark(entity.getRemark()+"bms未签约服务;");
			return false;
		}
		current = System.currentTimeMillis();
		XxlJobLogger.log("-->"+entity.getId()+"验证签约服务耗时：【{0}】毫秒  ",(current - start));				
		
		
		start = System.currentTimeMillis();// 系统开始时间
		/*验证报价 报价*/
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
			storageFeeEntity.setIsCalculated(CalculateState.Quote_Miss.getCode());
			entity.setRemark(entity.getRemark()+"bms报价未配置;");
			return false;
		}

		current = System.currentTimeMillis();
		XxlJobLogger.log("-->"+entity.getId()+"验证报价耗时：【{0}】毫秒  ",(current - start));
		return true;
	}

	@Override
	public Integer deleteFeesBatch(List<BizProductStorageEntity> list) {
		Map<String, Object> feesMap = new HashMap<String, Object>();
		List<String> feesNos = new ArrayList<String>();
		for (BizProductStorageEntity entity : list) {
			if(StringUtils.isNotEmpty(entity.getFeesNo())){
				feesNos.add(entity.getFeesNo());
			}
			else{
				entity.setFeesNo(sequenceService.getBillNoOne(BizProductStorageEntity.class.getName(), "STO", "0000000000"));
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
