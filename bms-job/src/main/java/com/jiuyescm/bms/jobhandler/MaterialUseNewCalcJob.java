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
import com.jiuyescm.bms.biz.dispatch.entity.BizDispatchBillEntity;
import com.jiuyescm.bms.biz.storage.entity.BizOutstockPackmaterialEntity;
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
import com.xxl.job.core.handler.annotation.JobHander;
import com.xxl.job.core.log.XxlJobLogger;

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
	
	Map<String,PriceContractInfoEntity> mapContact=null;
	Map<String,BillRuleReceiveEntity> mapRule=null;
	Map<String,List<PriceMaterialQuotationEntity>> mapCusPrice=null;
	
	protected void initConf(){
		mapCusPrice=new ConcurrentHashMap<String,List<PriceMaterialQuotationEntity>>();
		mapRule=new ConcurrentHashMap<String,BillRuleReceiveEntity>();
		mapContact=new ConcurrentHashMap<String,PriceContractInfoEntity>();
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
		storageFeeEntity.setProductType("");							//商品类型
		storageFeeEntity.setQuantity(DoubleUtil.isBlank(entity.getAdjustNum())?entity.getNum():entity.getAdjustNum());//计费数量
		storageFeeEntity.setStatus("0");								//状态
		storageFeeEntity.setOrderNo(entity.getOutstockNo());
		//根据测试的建议 吧耗材编码设置成商品编号和商品名称 zhangzw
		storageFeeEntity.setProductNo(entity.getConsumerMaterialCode());
		storageFeeEntity.setProductName(entity.getConsumerMaterialName());
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

	
	@Override
	public boolean isNoExe(BizOutstockPackmaterialEntity entity,FeesReceiveStorageEntity feeEntity) {
		if(entity.getConsumerMaterialCode()!=null && entity.getConsumerMaterialCode().contains("FSD")){
			BizDispatchBillEntity biz=bizDispatchBillService.getDispatchEntityByWaybillNo(entity.getWaybillNo());
			if(biz!=null && "1500000015".equals(biz.getCarrierId())){
				XxlJobLogger.log("-->"+entity.getId()+String.format("顺丰防水袋，不收钱", entity.getId()));
				entity.setIsCalculated(CalculateState.No_Exe.getCode());
				feeEntity.setIsCalculated(CalculateState.No_Exe.getCode());
				entity.setRemark(String.format("顺丰防水袋，不收钱", entity.getId()));
				return true;
			}
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
			XxlJobLogger.log("-->"+entity.getId()+"合同在线无此合同:"+ex.getMessage());
			entity.setRemark(ex.getMessage());
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
					entity.setRemark("报价未配置");
					return;
				}
				
				//封装数据的仓库和温度
				map.clear();
				map.put("warehouse_code", entity.getWarehouseCode());
				PriceMaterialQuotationEntity stepQuoEntity=storageQuoteFilterService.quoteMaterialFilter(list, map);			
				
				if(stepQuoEntity==null){
					XxlJobLogger.log("-->"+entity.getId()+"阶梯报价未配置");
					entity.setIsCalculated(CalculateState.Quote_Miss.getCode());
					feeEntity.setIsCalculated(CalculateState.Quote_Miss.getCode());
					entity.setRemark("阶梯报价未配置");
					feeEntity.setCalcuMsg("阶梯报价未配置");
					return;
				}
				
				XxlJobLogger.log("筛选后得到的报价结果【{0}】",JSONObject.fromObject(stepQuoEntity));
							
				feeEntity.setCost(new BigDecimal(stepQuoEntity.getUnitPrice()*feeEntity.getQuantity()).setScale(2,BigDecimal.ROUND_HALF_UP));
				feeEntity.setUnitPrice(stepQuoEntity.getUnitPrice());
				feeEntity.setParam2(stepQuoEntity.getUnitPrice().toString()+"*"+feeEntity.getQuantity().toString());
				feeEntity.setParam3(stepQuoEntity.getId()+"");
				if(feeEntity.getCost().compareTo(BigDecimal.ZERO) == 1){
					feeEntity.setIsCalculated(CalculateState.Finish.getCode());
					entity.setIsCalculated(CalculateState.Finish.getCode());
					entity.setRemark(CalculateState.Finish.getDesc());
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
			entity.setRemark(CalculateState.Sys_Error.getDesc());
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
				entity.setRemark("合同在线规则未绑定");
				feeEntity.setIsCalculated(CalculateState.Quote_Miss.getCode());
				entity.setIsCalculated(CalculateState.Quote_Miss.getCode());
				XxlJobLogger.log("-->"+entity.getId()+"计算不成功，合同在线规则未绑定");
			}
			//获取合同在线查询条件
			Map<String, Object> cond = new HashMap<String, Object>();
			feesCalcuService.ContractCalcuService(entity, cond, ruleEntity.getRule(), ruleEntity.getQuotationNo());
			XxlJobLogger.log("-->"+entity.getId()+"获取报价参数"+cond);
			ContractQuoteInfoVo rtnQuoteInfoVo = contractQuoteInfoService.queryQuotes(contractQuoteInfoVo, cond);
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
			XxlJobLogger.log("-->"+entity.getId()+"计算不成功，费用0",ex);
		}
		
	}
	
	protected boolean validateData(BizOutstockPackmaterialEntity entity,FeesReceiveStorageEntity feeEntity) {
		
		long start = System.currentTimeMillis();// 开始时间
		XxlJobLogger.log("-->"+entity.getId()+"数据主键ID:【{0}】  ",entity.getId());
		entity.setCalculateTime(feeEntity.getCalculateTime());
		Map<String,Object> map=new HashMap<String,Object>();
		long current = 0l;// 当前系统时间
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
			entity.setRemark(String.format("未查询到合同  订单号【%s】--商家【%s】", entity.getId(),customerId));
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
			entity.setRemark(String.format("未签约服务"));
			return false;
		}
		current = System.currentTimeMillis();
		XxlJobLogger.log("-->"+entity.getId()+"验证签约服务耗时：【{0}】毫秒  ",(current - start));			
		return true;
	}

	@Override
	public void updateBatch(List<BizOutstockPackmaterialEntity> ts,List<FeesReceiveStorageEntity> fs) {

		long start = System.currentTimeMillis();// 系统开始时间
		long current = 0l;// 当前系统时间
		bizOutstockPackmaterialService.updateBatch(ts);
		current = System.currentTimeMillis();
		XxlJobLogger.log("更新业务数据耗时：【{0}】毫秒",(current - start));
		start = System.currentTimeMillis();// 系统开始时间
		feesReceiveStorageService.InsertBatch(fs);
		current = System.currentTimeMillis();
		XxlJobLogger.log("新增费用数据耗时：【{0}】毫秒  ",(current - start));
		
	}
	

	

	
	
	

}
