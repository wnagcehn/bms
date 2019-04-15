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

import com.jiuyescm.bms.base.group.service.IBmsGroupSubjectService;
import com.jiuyescm.bms.biz.storage.entity.BizPackStorageEntity;
import com.jiuyescm.bms.drools.IFeesCalcuService;
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
import com.jiuyescm.bs.util.StringUtil;
import com.jiuyescm.common.utils.DoubleUtil;
import com.jiuyescm.contract.quote.api.IContractQuoteInfoService;
import com.jiuyescm.contract.quote.vo.ContractBizTypeEnum;
import com.jiuyescm.contract.quote.vo.ContractQuoteInfoVo;
import com.jiuyescm.contract.quote.vo.ContractQuoteQueryInfoVo;
import com.jiuyescm.exception.BizException;
import com.xxl.job.core.handler.annotation.JobHander;
import com.xxl.job.core.log.XxlJobLogger;

@JobHander(value="packStorageNewCalcJob")
@Service
public class PackStorageNewCalcJob extends CommonJobHandler<BizPackStorageEntity,FeesReceiveStorageEntity> {

	//private String SubjectId = "wh_material_storage";		//费用类型-耗材存储费 编码 1003原编码
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
	@Autowired private IBmsGroupSubjectService bmsGroupSubjectService;
	
	protected void initConf(){
		mapCusPrice=new ConcurrentHashMap<String,PriceGeneralQuotationEntity>();
		mapCusStepPrice=new ConcurrentHashMap<String,List<PriceStepQuotationEntity>>();
		mapContact=new ConcurrentHashMap<String,PriceContractInfoEntity>();
		mapRule=new ConcurrentHashMap<String,BillRuleReceiveEntity>();
	}
	
	@Override
	protected List<BizPackStorageEntity> queryBillList(Map<String, Object> map) {
		XxlJobLogger.log("packStorageNewCalcJob查询条件map:【{0}】  ",map);
		Long current = System.currentTimeMillis();
		List<BizPackStorageEntity> bizList = bizPackStorageService.query(map);		
		if(bizList!=null && bizList.size() > 0){
			XxlJobLogger.log("【配送运单】查询行数【{0}】耗时【{1}】",bizList.size(),(System.currentTimeMillis()-current));
			initConf();
		}
		return bizList;
	}
	@Override
	public FeesReceiveStorageEntity initFeeEntity(BizPackStorageEntity entity) {
		
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
	protected String[] initSubjects() {
		Map<String, String> maps = bmsGroupSubjectService.getSubject("job_subject_pack_storage");
		if (maps.size() == 0) {
			String[] str = {"wh_material_storage"};
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
	public boolean isJoin(BizPackStorageEntity t) {
		return true;
	}
	
	@Override
	public boolean isNoExe(BizPackStorageEntity t, FeesReceiveStorageEntity f) {
		return false;
	}
	
	@Override
	public void calcu(BizPackStorageEntity entity, FeesReceiveStorageEntity feeEntity) {
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
	public ContractQuoteInfoVo getContractForWhat(BizPackStorageEntity entity) {
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
			XxlJobLogger.log("-->{0}合同在线无此合同 {1}" , entity.getId() , ex.getMessage());
			entity.setRemark(entity.getRemark()+"合同在线"+ex.getMessage()+";");
		}
		return modelEntity;
	}
	@Override
	public void calcuForBms(BizPackStorageEntity entity,FeesReceiveStorageEntity feeEntity) {
		XxlJobLogger.log("-->"+entity.getId()+"bms计算");
		Map<String, Object> maps = new HashMap<String, Object>();
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
					entity.setRemark(entity.getRemark()+"计算成功;");
					break;
				case "PRICE_TYPE_STEP"://阶梯价
					
					//寻找阶梯报价
					maps.put("quotationId", generalEntity.getId());
					//根据报价单位判断
					maps.put("num", DoubleUtil.isBlank(entity.getAdjustPalletNum())?entity.getPalletNum():entity.getAdjustPalletNum());			
					//查询出的所有子报价
					List<PriceStepQuotationEntity> list=repository.queryPriceStepByQuatationId(maps);
					
					if(list==null || list.size() == 0){
						XxlJobLogger.log("-->"+entity.getId()+"阶梯报价未配置");
						entity.setIsCalculated(CalculateState.Quote_Miss.getCode());
						feeEntity.setIsCalculated(CalculateState.Quote_Miss.getCode());
						entity.setRemark(entity.getRemark()+"阶梯报价未配置;");
						return;
					}
					
					PriceStepQuotationEntity price = new PriceStepQuotationEntity();
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("warehouse_code", entity.getWarehouseCode());
					map.put("temperature_code", entity.getTemperatureTypeCode());
					price=storageQuoteFilterService.quoteFilter(list, map);
					if(price == null){
						feeEntity.setIsCalculated(CalculateState.Sys_Error.getCode());
						entity.setIsCalculated(CalculateState.Sys_Error.getCode());
						entity.setRemark(entity.getRemark()+"系统错误，报价筛选异常;");
						XxlJobLogger.log("-->"+entity.getId()+"系统错误，报价筛选异常");
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
							entity.setRemark(entity.getRemark()+"计算成功,取封顶价;");
						}
					}
					feeEntity.setParam3(price.getId()+"");
					feeEntity.setCost(BigDecimal.valueOf(amount));
					feeEntity.setParam4(priceType);
					feeEntity.setBizType(entity.getextattr1());//判断是否是遗漏数据
					entity.setRemark(entity.getRemark()+"计算成功;");
					entity.setIsCalculated(CalculateState.Finish.getCode());
					feeEntity.setIsCalculated(CalculateState.Finish.getCode());	
					break;
				default:
					entity.setIsCalculated(CalculateState.Other.getCode());
					feeEntity.setIsCalculated(CalculateState.Other.getCode());	
					entity.setRemark(entity.getRemark()+"不支持【"+priceType+"】;");
					break;
				}
			}
		}
		catch(Exception ex){
			entity.setIsCalculated(CalculateState.Sys_Error.getCode());
			feeEntity.setIsCalculated(CalculateState.Sys_Error.getCode());	
			entity.setRemark(entity.getRemark()+ex.getMessage()+";");
			XxlJobLogger.log("-->"+entity.getId()+"bms计算异常--",ex);
		}
		
	}

	public void calcuForContract(BizPackStorageEntity entity,FeesReceiveStorageEntity feeEntity,ContractQuoteInfoVo contractQuoteInfoVo) {
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
			
			
			XxlJobLogger.log("获取合同在线报价结果"+JSONObject.fromObject(rtnQuoteInfoVo));
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
		catch(Exception ex){
			feeEntity.setIsCalculated(CalculateState.Sys_Error.getCode());
			entity.setIsCalculated(CalculateState.Sys_Error.getCode());
			XxlJobLogger.log("-->"+entity.getId()+"计算不成功，费用0{0}",ex.getMessage());
			entity.setRemark(entity.getRemark()+"计算不成功:"+ex.getMessage()+";");
			feeEntity.setCalcuMsg("计算不成功:"+ex.getMessage());
		}
		
	}
	@Override
	public void updateBatch(List<BizPackStorageEntity> ts,List<FeesReceiveStorageEntity> fs) {
		long start = System.currentTimeMillis();// 系统开始时间
		long current = 0L;// 当前系统时间
		bizPackStorageService.updateBatch(ts);
		current = System.currentTimeMillis();
		XxlJobLogger.log("更新业务数据耗时：【{0}】毫秒  ",(current - start));
		start = System.currentTimeMillis();// 系统开始时
		feesReceiveStorageService.InsertBatch(fs);
		current = System.currentTimeMillis();
		XxlJobLogger.log("新增费用数据耗时：【{0}】毫秒",(current - start));
	}
	
	
	
	protected boolean validateData(BizPackStorageEntity entity,FeesReceiveStorageEntity feeEntity) {
		
		long start = System.currentTimeMillis();// 系统开始时间
		long current = 0L;// 当前系统时间
		XxlJobLogger.log("-->"+entity.getId()+"数据主键ID:【{0}】  ",entity.getId());
		
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
		    if(contractEntity!=null){
			    mapContact.put(entity.getCustomerid(), contractEntity);
		    }
		}
		if(contractEntity == null || StringUtils.isEmpty(contractEntity.getContractCode())){
			XxlJobLogger.log("-->"+entity.getId()+String.format("未查询到合同  订单号【%s】--商家【%s】", entity.getId(),entity.getCustomerid()));
			entity.setIsCalculated(CalculateState.Contract_Miss.getCode());
			feeEntity.setIsCalculated(CalculateState.Contract_Miss.getCode());
			entity.setRemark(entity.getRemark()+String.format("bms未查询到合同  订单号【%s】--商家【%s】;", entity.getId(),entity.getCustomerid()));
			return false;
		}
		XxlJobLogger.log("-->"+entity.getId()+"验证合同   耗时【{0}】毫秒  合同编号 【{1}】",(System.currentTimeMillis() - start),contractEntity.getContractCode());
		
		//----验证签约服务
		start = System.currentTimeMillis();
		map.clear();
		map.put("contractCode", contractEntity.getContractCode());
		map.put("subjectId", SubjectId);
		List<PriceContractItemEntity> contractItems = priceContractItemRepository.query(map);
		if(contractItems == null || contractItems.size() == 0 || StringUtils.isEmpty(contractItems.get(0).getTemplateId())) {
			XxlJobLogger.log("-->"+entity.getId()+"未签约服务  订单号【{0}】--商家【{1}】", entity.getId(),entity.getCustomerid());
			entity.setIsCalculated(CalculateState.Contract_Miss.getCode());
			feeEntity.setIsCalculated(CalculateState.Contract_Miss.getCode());
			entity.setRemark(entity.getRemark()+"bms未签约服务;");
			return false;
		}
		XxlJobLogger.log("-->"+entity.getId()+"验证签约服务耗时：【{0}】毫秒  ",(System.currentTimeMillis() - start));		
		
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
			XxlJobLogger.log("-->"+entity.getId()+"报价未配置");
			entity.setIsCalculated(CalculateState.Quote_Miss.getCode());
			entity.setRemark(entity.getRemark()+"bms报价未配置;");
			feeEntity.setIsCalculated(CalculateState.Quote_Miss.getCode());
			return false;
		}
		//报价模板
		priceType=quoTemplete.getPriceType(); //获取报价类型  一口价（PRICE_TYPE_NORMAL）/阶梯价（PRICE_TYPE_STEP）

		current = System.currentTimeMillis();
		XxlJobLogger.log("-->"+entity.getId()+"验证报价耗时：【{0}】毫秒  ",(current - start));
		return true;

	}

	@Override
	public Integer deleteFeesBatch(List<BizPackStorageEntity> list) {
		List<String> feesNos = new ArrayList<String>();
		Map<String, Object> feesMap = new HashMap<String, Object>();
		for (BizPackStorageEntity entity : list) {
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
