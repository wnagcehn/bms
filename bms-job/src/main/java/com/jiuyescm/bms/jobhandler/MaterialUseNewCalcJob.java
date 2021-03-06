package com.jiuyescm.bms.jobhandler;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.dubbo.common.utils.CollectionUtils;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import com.jiuyescm.bms.base.dict.api.IMaterialDictService;
import com.jiuyescm.bms.base.dict.vo.PubMaterialVo;
import com.jiuyescm.bms.base.dictionary.entity.SystemCodeEntity;
import com.jiuyescm.bms.base.dictionary.repository.ISystemCodeRepository;
import com.jiuyescm.bms.base.group.service.IBmsGroupSubjectService;
import com.jiuyescm.bms.biz.dispatch.entity.BizDispatchBillEntity;
import com.jiuyescm.bms.biz.storage.entity.BizOutstockPackmaterialEntity;
import com.jiuyescm.bms.chargerule.receiverule.entity.BillRuleReceiveEntity;
import com.jiuyescm.bms.common.enumtype.CalculateState;
import com.jiuyescm.bms.common.enumtype.TemplateTypeEnum;
import com.jiuyescm.bms.drools.IFeesCalcuService;
import com.jiuyescm.bms.general.entity.FeesReceiveStorageEntity;
import com.jiuyescm.bms.general.service.IFeesReceiveStorageService;
import com.jiuyescm.bms.general.service.IPriceContractInfoService;
import com.jiuyescm.bms.general.service.IStorageQuoteFilterService;
import com.jiuyescm.bms.general.service.SequenceService;
import com.jiuyescm.bms.quotation.contract.entity.PriceContractInfoEntity;
import com.jiuyescm.bms.quotation.contract.entity.PriceContractItemEntity;
import com.jiuyescm.bms.quotation.contract.repository.imp.IPriceContractDao;
import com.jiuyescm.bms.quotation.contract.repository.imp.IPriceContractItemRepository;
import com.jiuyescm.bms.quotation.storage.entity.PriceMaterialQuotationEntity;
import com.jiuyescm.bms.quotation.storage.repository.IPriceMaterialQuotationRepository;
import com.jiuyescm.bms.quotation.transport.repository.IGenericTemplateRepository;
import com.jiuyescm.bms.receivable.dispatch.service.IBizDispatchBillService;
import com.jiuyescm.bms.receivable.storage.service.IBizOutstockPackmaterialService;
import com.jiuyescm.bms.rule.receiveRule.repository.IReceiveRuleRepository;
import com.jiuyescm.bs.util.StringUtil;
import com.jiuyescm.cfm.common.JAppContext;
import com.jiuyescm.common.utils.DoubleUtil;
import com.jiuyescm.contract.quote.api.IContractQuoteInfoService;
import com.jiuyescm.contract.quote.vo.ContractBizTypeEnum;
import com.jiuyescm.contract.quote.vo.ContractQuoteInfoVo;
import com.jiuyescm.contract.quote.vo.ContractQuoteQueryInfoVo;
import com.jiuyescm.exception.BizException;
import com.jiuyescm.mdm.customer.api.IPubMaterialInfoService;
import com.jiuyescm.mdm.customer.vo.PubMaterialInfoVo;
import com.xxl.job.core.handler.annotation.JobHander;
import com.xxl.job.core.log.XxlJobLogger;

import net.sf.json.JSONObject;

@JobHander(value="materialUseNewCalcJob")
@Service
public class MaterialUseNewCalcJob extends CommonJobHandler<BizOutstockPackmaterialEntity,FeesReceiveStorageEntity> {

	//private String SubjectId = "wh_material_use";//费用类型-耗材使用费编码
	
	@Autowired private IBizOutstockPackmaterialService bizOutstockPackmaterialService;
	@Autowired private IContractQuoteInfoService contractQuoteInfoService;
	@Autowired private IBizDispatchBillService bizDispatchBillService;
	@Autowired private IFeesCalcuService feesCalcuService;
	@Autowired private IReceiveRuleRepository receiveRuleRepository;
	@Autowired private IPriceContractDao priceContractService;
	@Autowired private IPriceContractInfoService jobPriceContractInfoService;
	@Autowired private IPriceContractItemRepository priceContractItemRepository;
	@Autowired private IPriceMaterialQuotationRepository priceMaterialQuotationRepository;
	@Autowired private IGenericTemplateRepository genericTemplateRepository;
	@Autowired private IFeesReceiveStorageService feesReceiveStorageService;
	@Autowired private SequenceService sequenceService;
	@Autowired private IBmsGroupSubjectService bmsGroupSubjectService;
	@Autowired private IStorageQuoteFilterService storageQuoteFilterService;
	@Autowired private IPubMaterialInfoService pubMaterialInfoService;
	@Autowired private ISystemCodeRepository systemCodeRepository;
	@Autowired private IMaterialDictService materialDictService;

	
	Map<String,PubMaterialInfoVo> materialMap = null;
	Map<String,PriceContractInfoEntity> mapContact=null;
	Map<String,BillRuleReceiveEntity> mapRule=null;
	Map<String,List<PriceMaterialQuotationEntity>> mapCusPrice=null;
	Map<String,SystemCodeEntity> noFeesMap=null;
	List<SystemCodeEntity> noCarrierMap=null;
	
	protected void initConf(){
		mapCusPrice=new ConcurrentHashMap<String,List<PriceMaterialQuotationEntity>>();
		mapRule=new ConcurrentHashMap<String,BillRuleReceiveEntity>();
		mapContact=new ConcurrentHashMap<String,PriceContractInfoEntity>();
		materialMap=queryAllMaterial();
		noFeesMap=queryNoFees();
		noCarrierMap=queryNoCarrier();
	}
	

	@Override
	public Integer deleteFeesBatch(List<BizOutstockPackmaterialEntity> list) {
		List<String> feesNos = new ArrayList<String>();
		Map<String, Object> feesMap = new HashMap<String, Object>();
		for (BizOutstockPackmaterialEntity entity : list) {
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
	
	@Override
	protected List<BizOutstockPackmaterialEntity> queryBillList(Map<String, Object> map) {
		XxlJobLogger.log("materialUseNewCalcJob查询条件map:【{0}】  ",map);
		Long current = System.currentTimeMillis();
		List<BizOutstockPackmaterialEntity> bizList = bizOutstockPackmaterialService.query(map);
		if(bizList!=null && bizList.size() > 0){
			XxlJobLogger.log("【配送运单】查询行数【{0}】耗时【{1}】",bizList.size(),(System.currentTimeMillis()-current));
			initConf();
		}
		return bizList;
	}
	
	@Override
	public FeesReceiveStorageEntity initFeeEntity(BizOutstockPackmaterialEntity entity) {
		entity.setRemark("");
		FeesReceiveStorageEntity storageFeeEntity = new FeesReceiveStorageEntity();	
		storageFeeEntity.setCreator("system");
		storageFeeEntity.setCreateTime(entity.getCreateTime());
		storageFeeEntity.setOperateTime(entity.getCreateTime());
		storageFeeEntity.setCustomerId(entity.getCustomerId());			//商家ID
		storageFeeEntity.setCustomerName(entity.getCustomerName());		//商家名称
		storageFeeEntity.setWarehouseCode(entity.getWarehouseCode());	//仓库ID
		storageFeeEntity.setWarehouseName(entity.getWarehouseName());	//仓库名称
		storageFeeEntity.setCostType("FEE_TYPE_MATERIAL");				//费用类别 FEE_TYPE_GENEARL-通用 FEE_TYPE_MATERIAL-耗材 FEE_TYPE_ADD-增值
		storageFeeEntity.setSubjectCode(SubjectId);						//费用科目
		storageFeeEntity.setOtherSubjectCode(SubjectId);
		storageFeeEntity.setProductType("");//商品类型
		//根据测试的建议 吧耗材编码设置成商品编号和商品名称 zhangzw
		storageFeeEntity.setProductNo(entity.getConsumerMaterialCode());
		storageFeeEntity.setProductName(entity.getConsumerMaterialName());
		storageFeeEntity.setQuantity(DoubleUtil.isBlank(entity.getAdjustNum())?entity.getNum():entity.getAdjustNum());//计费数量
		if(materialMap!=null && materialMap.containsKey(entity.getConsumerMaterialCode())){
			String materialType=materialMap.get(entity.getConsumerMaterialCode()).getMaterialType();
			if("干冰".equals(materialType)){
				storageFeeEntity.setQuantity(DoubleUtil.isBlank(entity.getAdjustNum())?entity.getWeight():entity.getAdjustNum());//计费数量
			}else{
				storageFeeEntity.setQuantity(DoubleUtil.isBlank(entity.getAdjustNum())?entity.getNum():entity.getAdjustNum());//计费重量
			}
		}
		storageFeeEntity.setStatus("0");								//状态
		storageFeeEntity.setOrderNo(entity.getOutstockNo());
		storageFeeEntity.setBizId(String.valueOf(entity.getId()));						//业务数据主键
		storageFeeEntity.setWeight(entity.getWeight());					//设置重量
		entity.setCalculateTime(JAppContext.currentTimestamp());
		storageFeeEntity.setCalculateTime(JAppContext.currentTimestamp());
		storageFeeEntity.setCost(new BigDecimal(0));					//入仓金额
		storageFeeEntity.setFeesNo(entity.getFeesNo());
		storageFeeEntity.setParam1(TemplateTypeEnum.COMMON.getCode());
		storageFeeEntity.setDelFlag("0");
		return storageFeeEntity;
	}
	

	@Override
	protected String[] initSubjects() {
		Map<String, String> maps = bmsGroupSubjectService.getSubject("job_subject_material_use");
		if (maps.size() == 0) {
			String[] str = {"wh_material_use"};
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
	public boolean isJoin(BizOutstockPackmaterialEntity t) {
		return true;
	}

	
//	@Override
//	public boolean isNoExe(BizOutstockPackmaterialEntity entity,FeesReceiveStorageEntity feeEntity) {
//		if(entity.getConsumerMaterialCode()!=null && entity.getConsumerMaterialCode().contains("FSD")){
//			BizDispatchBillEntity biz=bizDispatchBillService.getDispatchEntityByWaybillNo(entity.getWaybillNo());
//			if(biz!=null && "1500000015".equals(biz.getCarrierId())){
//				XxlJobLogger.log("-->"+entity.getId()+String.format("顺丰防水袋，不收钱", entity.getId()));
//				entity.setIsCalculated(CalculateState.No_Exe.getCode());
//				feeEntity.setIsCalculated(CalculateState.No_Exe.getCode());
//				entity.setRemark(String.format("顺丰防水袋，不收钱", entity.getId()));
//				return true;
//			}
//		}
//		return false;
//	}
	
	@Override
	public boolean isNoExe(BizOutstockPackmaterialEntity entity,FeesReceiveStorageEntity feeEntity) {
		//运单
		BizDispatchBillEntity biz=bizDispatchBillService.getDispatchEntityByWaybillNo(entity.getWaybillNo());
		//耗材类型
		String materialType = "";
		if(materialMap!=null && materialMap.containsKey(entity.getConsumerMaterialCode())){
			materialType=materialMap.get(entity.getConsumerMaterialCode()).getMaterialType();
		}

		//物流商对应得耗材不计费
		if(biz!=null && StringUtils.isNotBlank(materialType)){
			for (SystemCodeEntity scEntity : noCarrierMap) {
				if(scEntity.getCode().equals(biz.getCarrierId()) && scEntity.getExtattr1().equals(materialType)){
					XxlJobLogger.log("-->"+entity.getId()+String.format("该物流商使用的耗材不收钱", entity.getId()));
					entity.setIsCalculated(CalculateState.No_Exe.getCode());
					feeEntity.setIsCalculated(CalculateState.No_Exe.getCode());
					entity.setRemark(entity.getRemark()+String.format("该物流商使用的耗材不收钱", entity.getId())+";");
					return true;
				}
			}
			
		}
		
		//指定耗材不计费
		if(entity.getConsumerMaterialCode()!=null && noFeesMap.containsKey(entity.getConsumerMaterialCode())){
				XxlJobLogger.log("-->"+entity.getId()+String.format("不计算耗材使用费的耗材", entity.getId()));
				entity.setIsCalculated(CalculateState.No_Exe.getCode());
				feeEntity.setIsCalculated(CalculateState.No_Exe.getCode());
				entity.setRemark(entity.getRemark()+String.format("耗材不收钱", entity.getId())+";");
				return true;
		}
		return false;
	}

	@Override
	public void calcu(BizOutstockPackmaterialEntity entity, FeesReceiveStorageEntity feeEntity) {
		ContractQuoteInfoVo modelEntity = getContractForWhat(entity);
		if(modelEntity == null || StringUtil.isEmpty(modelEntity.getTemplateCode())){
			calcuForBms(entity, feeEntity);
		}
		else{
			XxlJobLogger.log("-->"+entity.getId()+"规则编号：  "+modelEntity.getRuleCode().trim());
			calcuForContract(entity,feeEntity,modelEntity);
		}
		
	}
	
	@Override
	public ContractQuoteInfoVo getContractForWhat(BizOutstockPackmaterialEntity entity) {

		ContractQuoteQueryInfoVo queryVo = new ContractQuoteQueryInfoVo();
		queryVo.setCustomerId(entity.getCustomerId());
		queryVo.setBizTypeCode(ContractBizTypeEnum.STORAGE.getCode());
		queryVo.setSubjectCode(SubjectId);
		queryVo.setCurrentTime(entity.getCreateTime());
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
	public void calcuForBms(BizOutstockPackmaterialEntity entity,FeesReceiveStorageEntity feeEntity){
		//合同报价校验  false-不通过  true-通过
		XxlJobLogger.log("-->"+entity.getId()+"bms计算");
		try{
			if(validateData(entity, feeEntity)){
				
				//商家耗材报价
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("contractCode", mapContact.get(entity.getCustomerId()).getContractCode());
				map.put("subjectId",SubjectId);
				map.put("materialCode",entity.getConsumerMaterialCode());
				//map.put("warehouseId", entity.getWarehouseCode());
				List<PriceMaterialQuotationEntity> list=priceMaterialQuotationRepository.queryMaterialQuatationByContract(map);
				
				if(list==null||list.size()==0){
					XxlJobLogger.log("-->"+entity.getId()+"报价未配置");
					entity.setIsCalculated(CalculateState.Quote_Miss.getCode());
					feeEntity.setIsCalculated(CalculateState.Quote_Miss.getCode());
					entity.setRemark(entity.getRemark()+"报价未配置;");
					return;
				}
				
				PriceMaterialQuotationEntity stepQuoEntity = null;
				
				//判断耗材类型是否为泡沫箱
				PubMaterialVo materialInfoVo = materialDictService.getMaterialByCode(entity.getConsumerMaterialCode());
				if (null != materialInfoVo && "泡沫箱".equals(materialInfoVo.getMaterialType())) {
					//获取当前月份
					Calendar cale = Calendar.getInstance();
					Timestamp creTime = entity.getCreateTime();
					cale.setTime((Date)creTime);
					int month = cale.get(Calendar.MONTH) + 1;				
					//当前月份在夏季，筛出全国仓和其他仓最低报价
					List<SystemCodeEntity> pmxTimes = systemCodeRepository.findEnumList("PMX_TIME");
					String season = "";//季节
					boolean exe = false;
					for (SystemCodeEntity time : pmxTimes) {
						if (month >= Integer.parseInt(time.getExtattr1()) && month <= Integer.parseInt(time.getExtattr2())) {
							season = time.getCode();
							exe = true;
							//仓库过滤
							map.clear();
							map.put("warehouse_code", entity.getWarehouseCode());
							List<PriceMaterialQuotationEntity> pmxPriceList = storageQuoteFilterService.quotePMXmaterialFilter(list, map);
							if ("SUMMER".equals(season)) {
								//夏季
								if (CollectionUtils.isNotEmpty(pmxPriceList)) {
									if (pmxPriceList.size() == 2) {
										if (pmxPriceList.get(0).getUnitPrice() > pmxPriceList.get(1).getUnitPrice()) {
											stepQuoEntity = pmxPriceList.get(0);
										}else {
											stepQuoEntity = pmxPriceList.get(1);
										}
									}else {
										stepQuoEntity = pmxPriceList.get(0);
									}
								}else {
									stepQuoEntity = null;
								}
							}else {
								//其余季节
								if (CollectionUtils.isNotEmpty(pmxPriceList)) {
									if (pmxPriceList.size() == 2) {
										if (pmxPriceList.get(0).getUnitPrice() > pmxPriceList.get(1).getUnitPrice()) {
											stepQuoEntity = pmxPriceList.get(1);
										}else {
											stepQuoEntity = pmxPriceList.get(0);
										}
									}else {
										stepQuoEntity = pmxPriceList.get(0);
									}					
								}else {
									stepQuoEntity = null;
								}
							}
						}
					}
					if (!exe) {
						//封装数据的仓库
						map.clear();
						map.put("warehouse_code", entity.getWarehouseCode());
						stepQuoEntity=storageQuoteFilterService.quoteMaterialFilter(list, map);
					}
				}else {
					//封装数据的仓库
					map.clear();
					map.put("warehouse_code", entity.getWarehouseCode());
					stepQuoEntity=storageQuoteFilterService.quoteMaterialFilter(list, map);
				}

				if(stepQuoEntity==null){
					XxlJobLogger.log("-->"+entity.getId()+"报价未配置");
					entity.setIsCalculated(CalculateState.Quote_Miss.getCode());
					feeEntity.setIsCalculated(CalculateState.Quote_Miss.getCode());
					entity.setRemark(entity.getRemark()+"报价未配置;");
					feeEntity.setCalcuMsg("报价未配置");
					return;
				}
				//XxlJobLogger.log("筛选后得到的报价结果【{0}】",JSONObject.fromObject(stepQuoEntity));
							
				feeEntity.setCost(new BigDecimal(stepQuoEntity.getUnitPrice()*feeEntity.getQuantity()).setScale(2,BigDecimal.ROUND_HALF_UP));
				feeEntity.setUnitPrice(stepQuoEntity.getUnitPrice());
				feeEntity.setParam2(stepQuoEntity.getUnitPrice().toString()+"*"+feeEntity.getQuantity().toString());
				feeEntity.setParam3(stepQuoEntity.getId()+"");
				if(feeEntity.getCost().compareTo(BigDecimal.ZERO) == 1){
					feeEntity.setIsCalculated(CalculateState.Finish.getCode());
					entity.setIsCalculated(CalculateState.Finish.getCode());
					entity.setRemark(entity.getRemark()+CalculateState.Finish.getDesc()+";");
					XxlJobLogger.log("-->"+entity.getId()+"计算成功，费用【{0}】",feeEntity.getCost());
				}else{
					feeEntity.setIsCalculated(CalculateState.Quote_Miss.getCode());
					entity.setIsCalculated(CalculateState.Finish.getCode());
					XxlJobLogger.log("-->"+entity.getId()+"计算不成功，费用【0】");
				}
			}
		}
		catch(Exception ex){
			feeEntity.setIsCalculated(CalculateState.Sys_Error.getCode());
			entity.setIsCalculated(CalculateState.Sys_Error.getCode());
			entity.setRemark(entity.getRemark()+CalculateState.Sys_Error.getDesc()+";");
			XxlJobLogger.log("-->"+entity.getId()+"系统异常，费用【0】");
		}
	}
	

	public void calcuForContract(BizOutstockPackmaterialEntity entity,FeesReceiveStorageEntity feeEntity,ContractQuoteInfoVo contractQuoteInfoVo){
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
					entity.setRemark(entity.getRemark()==null?"":entity.getRemark()+"系统规则引擎异常;");
					return;
				}
				
				rtnQuoteInfoVo = contractQuoteInfoService.queryQuotes(contractQuoteInfoVo, cond);
			} catch (BizException e) {
				// TODO: handle exception
				feeEntity.setIsCalculated(CalculateState.Quote_Miss.getCode());
				entity.setIsCalculated(CalculateState.Quote_Miss.getCode());
				XxlJobLogger.log("-->"+entity.getId()+"获取合同在线报价异常:"+e.getMessage());
				entity.setRemark(entity.getRemark()==null?"":entity.getRemark()+"获取合同在线报价异常:"+e.getMessage()+";");
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
				entity.setRemark("计算成功");
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
	
	protected boolean validateData(BizOutstockPackmaterialEntity entity,FeesReceiveStorageEntity feeEntity) {
		
		long start = System.currentTimeMillis();// 开始时间
		XxlJobLogger.log("-->"+entity.getId()+"数据主键ID:【{0}】  ",entity.getId());
		entity.setCalculateTime(feeEntity.getCalculateTime());
		Map<String,Object> map=new HashMap<String,Object>();
		long current = 0L;// 当前系统时间
		String customerId = entity.getCustomerId();
		
		//验证商家合同
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
			entity.setRemark(entity.getRemark()+String.format("bms未查询到合同  订单号【%s】--商家【%s】;", entity.getId(),customerId));
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
			XxlJobLogger.log("-->"+entity.getId()+String.format("未签约服务  主键【%s】--商家【%s】", entity.getId(),entity.getCustomerId()));
			entity.setIsCalculated(CalculateState.Contract_Miss.getCode());
			feeEntity.setIsCalculated(CalculateState.Contract_Miss.getCode());
			entity.setRemark(entity.getRemark()+String.format("bms未签约服务")+";");
			return false;
		}
		current = System.currentTimeMillis();
		XxlJobLogger.log("-->"+entity.getId()+"验证签约服务耗时：【{0}】毫秒  ",(current - start));			
		return true;
	}

	@Override
	public void updateBatch(List<BizOutstockPackmaterialEntity> ts,List<FeesReceiveStorageEntity> fs) {

		long start = System.currentTimeMillis();// 系统开始时间
		long current = 0L;// 当前系统时间
		bizOutstockPackmaterialService.updateBatch(ts);
		current = System.currentTimeMillis();
		XxlJobLogger.log("更新业务数据耗时：【{0}】毫秒",(current - start));
		start = System.currentTimeMillis();// 系统开始时间
		feesReceiveStorageService.InsertBatch(fs);
		current = System.currentTimeMillis();
		XxlJobLogger.log("新增费用数据耗时：【{0}】毫秒  ",(current - start));
		
	}
	

	
	/**
	 * 查询耗材编码-耗材映射
	 * @return
	 */
	public Map<String,PubMaterialInfoVo> queryAllMaterial(){
		Map<String,PubMaterialInfoVo> map=Maps.newLinkedHashMap();
		Map<String,Object> condition=Maps.newHashMap();
		List<PubMaterialInfoVo> tmscodels = pubMaterialInfoService.queryList(condition);
		
		for(PubMaterialInfoVo materialVo:tmscodels){
			if(!StringUtils.isBlank(materialVo.getBarcode())){
				map.put(materialVo.getBarcode().trim(),materialVo);
			}
		}
		return map;
	}
	
	/**
	 * 查询不计算耗材
	 * @return
	 */
	public Map<String,SystemCodeEntity> queryNoFees(){
		Map<String,Object> condition=Maps.newHashMap();
		condition.put("typeCode", "NO_FEES_MATERIAL");
		PageInfo<SystemCodeEntity> page = systemCodeRepository.query(condition, 1, 999999);
		Map<String,SystemCodeEntity> map=new HashMap<String, SystemCodeEntity>();
		if(page.getList()!=null||page.getList().size()>0){
			List<SystemCodeEntity> list = page.getList();
			for(SystemCodeEntity entity:list){
					map.put(entity.getCode(),entity);
			}
		}
		return map;
	}
	
	/**
	 * 查询不计算的物流商
	 * @return
	 */
	public List<SystemCodeEntity> queryNoCarrier(){
		List<SystemCodeEntity> list=new ArrayList<SystemCodeEntity>();
		Map<String,Object> condition=Maps.newHashMap();
		condition.put("typeCode", "CARRIER_FREE");
		PageInfo<SystemCodeEntity> page = systemCodeRepository.query(condition, 1, 999999);
		if(page.getList()!=null||page.getList().size()>0){
			list = page.getList();
		}
		return list;
	}

}
