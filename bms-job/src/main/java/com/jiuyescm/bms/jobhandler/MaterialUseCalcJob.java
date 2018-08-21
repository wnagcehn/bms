package com.jiuyescm.bms.jobhandler;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jiuyescm.bms.base.calcu.vo.CalcuReqVo;
import com.jiuyescm.bms.base.calcu.vo.CalcuResultVo;
import com.jiuyescm.bms.biz.dispatch.entity.BizDispatchBillEntity;
import com.jiuyescm.bms.biz.storage.entity.BizOutstockPackmaterialEntity;
import com.jiuyescm.bms.calculate.base.IFeesCalcuService;
import com.jiuyescm.bms.chargerule.receiverule.entity.BillRuleReceiveEntity;
import com.jiuyescm.bms.common.enumtype.CalculateState;
import com.jiuyescm.bms.common.enumtype.TemplateTypeEnum;
import com.jiuyescm.bms.general.entity.FeesReceiveStorageEntity;
import com.jiuyescm.bms.general.service.IFeesReceiveStorageService;
import com.jiuyescm.bms.general.service.IPriceContractInfoService;
import com.jiuyescm.bms.general.service.IStandardReqVoService;
import com.jiuyescm.bms.general.service.SequenceService;
import com.jiuyescm.bms.quotation.contract.entity.ContractDetailEntity;
import com.jiuyescm.bms.quotation.contract.entity.PriceContractInfoEntity;
import com.jiuyescm.bms.quotation.contract.entity.PriceContractItemEntity;
import com.jiuyescm.bms.quotation.contract.repository.imp.IPriceContractDao;
import com.jiuyescm.bms.quotation.contract.repository.imp.IPriceContractItemRepository;
import com.jiuyescm.bms.quotation.storage.entity.PriceMaterialQuotationEntity;
import com.jiuyescm.bms.quotation.storage.repository.IPriceMaterialQuotationRepository;
import com.jiuyescm.bms.quotation.transport.entity.GenericTemplateEntity;
import com.jiuyescm.bms.quotation.transport.repository.IGenericTemplateRepository;
import com.jiuyescm.bms.receivable.dispatch.service.IBizDispatchBillService;
import com.jiuyescm.bms.receivable.storage.service.IBizOutstockPackmaterialService;
import com.jiuyescm.bms.rule.receiveRule.repository.IReceiveRuleRepository;
import com.jiuyescm.cfm.common.JAppContext;
import com.xxl.job.core.handler.annotation.JobHander;
import com.xxl.job.core.log.XxlJobLogger;

@JobHander(value="materialUseCalcJob")
@Service
public class MaterialUseCalcJob  extends CommonCalcJob<BizOutstockPackmaterialEntity,FeesReceiveStorageEntity>{

	@Autowired private IBizOutstockPackmaterialService bizOutstockPackmaterialService;
	@Autowired private SequenceService sequenceService;
	@Autowired private IPriceContractInfoService jobPriceContractInfoService;
	@Autowired private IFeesReceiveStorageService feesReceiveStorageService;
	@Autowired private IPriceMaterialQuotationRepository priceMaterialQuotationRepository;
	@Autowired private IReceiveRuleRepository receiveRuleRepository;
	@Autowired private IFeesCalcuService feesCalcuService;
	@Autowired private IStandardReqVoService standardReqVoServiceImpl;
	@Autowired private IPriceContractItemRepository priceContractItemRepository;
	@Autowired private IBizDispatchBillService bizDispatchBillService;
	@Autowired private IGenericTemplateRepository genericTemplateRepository;
	@Resource private IPriceContractDao priceContractService;
	
	private String BizTypeCode = "STORAGE"; //仓储费编码
	private String SubjectId = "wh_material_use";		//费用类型-耗材使用费编码  1009原编码
	private String quoType = "C";//默认使用常规报价
	
	Map<String,PriceContractInfoEntity> mapContact=null;
	Map<String,BillRuleReceiveEntity> mapRule=null;
	Map<String,List<PriceMaterialQuotationEntity>> mapCusPrice=null;
	@Override
	protected List<BizOutstockPackmaterialEntity> queryBillList(Map<String, Object> map) {
		/*XxlJobLogger.log("materialUseCalcJob start.");
		return bizOutstockPackmaterialService.query(map);*/
		
		long operateTime = System.currentTimeMillis();
		List<String> feesNos = new ArrayList<String>();
		Map<String, Object> feesMap = new HashMap<String, Object>();
		List<BizOutstockPackmaterialEntity> bizList = bizOutstockPackmaterialService.query(map);
		if(bizList == null || bizList.size() == 0){
			
		}
		else{
			for (BizOutstockPackmaterialEntity entity : bizList) {
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
	protected void initConf(List<BizOutstockPackmaterialEntity> billList) {
		mapContact=new HashMap<String,PriceContractInfoEntity>();
		mapRule=new HashMap<String,BillRuleReceiveEntity>();	
		mapCusPrice=new HashMap<String,List<PriceMaterialQuotationEntity>>();
	}

	@Override
	protected void calcuService(BizOutstockPackmaterialEntity entity,
			List<FeesReceiveStorageEntity> feesList) {
		FeesReceiveStorageEntity storageFeeEntity=initFeeEntity(entity);
		try{
			entity.setCalculateTime(JAppContext.currentTimestamp());
			storageFeeEntity.setCalculateTime(entity.getCalculateTime());
			String customerId=entity.getCustomerId();
			CalcuReqVo<PriceMaterialQuotationEntity> reqVo = new CalcuReqVo<PriceMaterialQuotationEntity>();
			reqVo.setBizData(entity);
			BillRuleReceiveEntity ruleEntity=mapRule.get(customerId);
			reqVo.setRuleNo(ruleEntity.getQuotationNo());
			reqVo.setRuleStr(ruleEntity.getRule());
			storageFeeEntity.setRuleNo(ruleEntity.getQuotationNo());
			
			PriceMaterialQuotationEntity quoEntity=new PriceMaterialQuotationEntity();
			if(mapCusPrice.containsKey(customerId)){
				quoEntity.setList(mapCusPrice.get(customerId));
			}
			
			reqVo.setQuoEntity(quoEntity);
		
			long start = System.currentTimeMillis();// 系统开始时间
			long current = 0l;// 当前系统时间
			CalcuResultVo resultVo = feesCalcuService.FeesCalcuService(reqVo);
			current = System.currentTimeMillis();
			if("succ".equals(resultVo.getSuccess())){
				XxlJobLogger.log("调用规则引擎   耗时【{0}】毫秒  费用【{1}】 ",(current - current),resultVo.getPrice());	
				PriceMaterialQuotationEntity matchEntity=queryMatchEntityById(mapCusPrice.get(customerId),resultVo.getQuoId());
				storageFeeEntity.setUnitPrice(matchEntity.getUnitPrice());
				storageFeeEntity.setCost(resultVo.getPrice());
				if(StringUtils.isNotEmpty(entity.getSpecDesc()))
				{
					storageFeeEntity.setExternalProductNo(entity.getSpecDesc());//耗材规格
				}
				entity.setRemark("计算成功");
				storageFeeEntity.setParam2(resultVo.getMethod());//
				storageFeeEntity.setParam3(resultVo.getQuoId());
				storageFeeEntity.setBizType(entity.getextattr1());//判断是否是遗漏数据
				storageFeeEntity.setIsCalculated(CalculateState.Finish.getCode());
				entity.setIsCalculated(CalculateState.Finish.getCode());
				feesList.add(storageFeeEntity);
			}else{
				XxlJobLogger.log("调用规则引擎失败   耗时【{0}】毫秒   ",(current - start));
				XxlJobLogger.log("费用计算失败--"+resultVo.getMsg());
				storageFeeEntity.setParam2(resultVo.getMethod());
				entity.setIsCalculated(CalculateState.Quote_Miss.getCode());
				storageFeeEntity.setIsCalculated(CalculateState.Quote_Miss.getCode());
				entity.setRemark("费用计算失败:"+resultVo.getMsg());
				feesList.add(storageFeeEntity);
			}
			XxlJobLogger.log(String.format("====================================[%s]计算完毕============================================",entity.getId()));
			
		}catch(Exception ex){
			entity.setIsCalculated(CalculateState.Sys_Error.getCode());
			storageFeeEntity.setIsCalculated(CalculateState.Sys_Error.getCode());
			entity.setRemark("费用计算异常:"+ex.getMessage());
			feesList.add(storageFeeEntity);
		}
		
	}
	
	@Override
	protected void saveBatchData(List<BizOutstockPackmaterialEntity> billList,
			List<FeesReceiveStorageEntity> feesList) {
		long start = System.currentTimeMillis();// 系统开始时间
		long current = 0l;// 当前系统时间
		bizOutstockPackmaterialService.updateBatch(billList);
		current = System.currentTimeMillis();
		XxlJobLogger.log("更新业务数据耗时：【{0}】毫秒",(current - start));
		start = System.currentTimeMillis();// 系统开始时间
		feesReceiveStorageService.InsertBatch(feesList);
		current = System.currentTimeMillis();
		XxlJobLogger.log("新增费用数据耗时：【{0}】毫秒  ",(current - start));
	}

	@Override
	protected boolean validateData(BizOutstockPackmaterialEntity entity,
			List<FeesReceiveStorageEntity> feesList) {
		XxlJobLogger.log("数据主键ID:【{0}】  ",entity.getId());
		Timestamp time=JAppContext.currentTimestamp();
		entity.setCalculateTime(time);
		Map<String,Object> map=new HashMap<String,Object>();
		String customerId=entity.getCustomerId();
		entity.setNum(entity.getAdjustNum()==null?entity.getNum():entity.getAdjustNum());
		entity.setCalculateTime(time);
		/*boolean isInsert = StringUtils.isEmpty(entity.getFeesNo())?true:false; //true-新增  false-更新
		if(isInsert){
			String feesNo = sequenceService.getBillNoOne(FeesReceiveStorageEntity.class.getName(), "STO", "0000000000");
			entity.setFeesNo(feesNo);
		}*/
		
		FeesReceiveStorageEntity storageFeeEntity = initFeeEntity(entity);
		storageFeeEntity.setCalculateTime(time);
		long start = System.currentTimeMillis();// 系统开始时间
		long current = 0l;// 当前系统时间

		entity.setCalculateTime(JAppContext.currentTimestamp());
		
		//判断当前的耗材是否是防水袋，防水袋并且物流商是顺丰的不收费
		if(entity.getConsumerMaterialCode()!=null && entity.getConsumerMaterialCode().contains("FSD")){
			BizDispatchBillEntity biz=bizDispatchBillService.getDispatchEntityByWaybillNo(entity.getWaybillNo());
			if(biz!=null && "1500000015".equals(biz.getCarrierId())){
				XxlJobLogger.log(String.format("顺丰防水袋，不收钱", entity.getId(),customerId));
				entity.setIsCalculated(CalculateState.No_Exe.getCode());
				storageFeeEntity.setIsCalculated(CalculateState.No_Exe.getCode());
				entity.setRemark(String.format("顺丰防水袋，不收钱", entity.getId(),customerId));
				feesList.add(storageFeeEntity);
				return false;
			}
		}
		
		
		
		//验证商家合同
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
			entity.setRemark(String.format("未查询到合同  订单号【%s】--商家【%s】", entity.getId(),customerId));
			feesList.add(storageFeeEntity);
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
			XxlJobLogger.log(String.format("未签约服务  主键【%s】--商家【%s】", entity.getId(),entity.getCustomerId()));
			entity.setIsCalculated(CalculateState.Contract_Miss.getCode());
			storageFeeEntity.setIsCalculated(CalculateState.Contract_Miss.getCode());
			entity.setRemark(String.format("未签约服务"));
			feesList.add(storageFeeEntity);
			return false;
		}
		current = System.currentTimeMillis();
		XxlJobLogger.log("验证签约服务耗时：【{0}】毫秒  ",(current - start));		
		start = System.currentTimeMillis();// 系统开始时间
		
		
		//商家耗材报价
		map.clear();
		map.put("contractCode", contractEntity.getContractCode());
		map.put("subjectId",SubjectId);
		map.put("materialCode",entity.getConsumerMaterialCode());
		map.put("warehouseId", entity.getWarehouseCode());
		List<PriceMaterialQuotationEntity> materialList=priceMaterialQuotationRepository.queryMaterialQuatationByContract(map);
		mapCusPrice.put(customerId, materialList);
		if(materialList==null||materialList.size()==0){
			XxlJobLogger.log("报价未配置");
			entity.setIsCalculated(CalculateState.Quote_Miss.getCode());
			storageFeeEntity.setIsCalculated(CalculateState.Quote_Miss.getCode());
			entity.setRemark("报价未配置");
			feesList.add(storageFeeEntity);
			return false;
		}
		current = System.currentTimeMillis();
		XxlJobLogger.log("验证报价耗时：【{0}】毫秒  ",(current - start));
		start = System.currentTimeMillis();// 系统开始时间
		/*查找商家 规则*/
		map.clear();
		BillRuleReceiveEntity ruleEntity=null;
		if(mapRule.containsKey(customerId)){
			ruleEntity=mapRule.get(customerId);
		}else{
//			map.put("customerid",customerId);
//			map.put("subjectId", SubjectId);
//		    ruleEntity=receiveRuleRepository.queryByCustomerId(map);
			//根据合同编码和费用科目查出模板编码
			Map<String, String> param = new HashMap<>();
			param.put("contractCode", contractEntity.getContractCode());
			param.put("subjectId",SubjectId);
			ContractDetailEntity cdEntity = priceContractService.queryTempByContractCodeAndSubjectId(param);
			if (null != cdEntity) {
				//根据模板编码查出报价中的规则编号
				Map<String, Object> cond = new HashMap<>();
				cond.put("templateCode", cdEntity.getTemplateId());
				GenericTemplateEntity gtmEntity = genericTemplateRepository.query(cond);
				//根据规则编号查出规则
				Map<String, Object> con = new HashMap<>();
				con.put("quotationNo", gtmEntity.getQuotationNo());
				ruleEntity = receiveRuleRepository.queryOne(con);
			}
		    mapRule.put(customerId, ruleEntity);
		}
		
		if(ruleEntity == null){
			XxlJobLogger.log("规则未配置");
			entity.setIsCalculated(CalculateState.Quote_Miss.getCode());
			storageFeeEntity.setIsCalculated(CalculateState.Quote_Miss.getCode());
			entity.setRemark("规则未配置");
			feesList.add(storageFeeEntity);
			return  false;
		}
		current = System.currentTimeMillis();
		XxlJobLogger.log("验证规则耗时：【{0}】毫秒  ",(current - start));
		return true;
	}
	
	private FeesReceiveStorageEntity initFeeEntity(BizOutstockPackmaterialEntity entity){
		FeesReceiveStorageEntity storageFeeEntity = new FeesReceiveStorageEntity();	
		storageFeeEntity.setCreator("system");
		//费用表的创建时间应为业务表的创建时间
		storageFeeEntity.setCreateTime(entity.getCreateTime());
		storageFeeEntity.setOperateTime(entity.getCreateTime());
		storageFeeEntity.setCustomerId(entity.getCustomerId());			//商家ID
		storageFeeEntity.setCustomerName(entity.getCustomerName());		//商家名称
		storageFeeEntity.setWarehouseCode(entity.getWarehouseCode());	//仓库ID
		storageFeeEntity.setWarehouseName(entity.getWarehouseName());							//仓库名称
		storageFeeEntity.setCostType("FEE_TYPE_MATERIAL");				//费用类别 FEE_TYPE_GENEARL-通用 FEE_TYPE_MATERIAL-耗材 FEE_TYPE_ADD-增值
		storageFeeEntity.setSubjectCode(SubjectId);						//费用科目
		storageFeeEntity.setOtherSubjectCode(SubjectId);
		storageFeeEntity.setProductType("");							//商品类型
		if(entity.getNum()!=null){
			storageFeeEntity.setQuantity((new Double(entity.getNum())).intValue());//商品数量
		}
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
	private PriceMaterialQuotationEntity queryMatchEntityById(
			List<PriceMaterialQuotationEntity> materialList, String priceId) {
		PriceMaterialQuotationEntity matchEntity=null;
		for(PriceMaterialQuotationEntity entity:materialList){
			if(entity.getId().toString().equals(priceId)){
				matchEntity=entity;
				break;
			}
		}
		return matchEntity;
	}

	@Override
	protected void calcuStandardService(List<BizOutstockPackmaterialEntity> billList) {
		for (BizOutstockPackmaterialEntity entity : billList) {
			if(CalculateState.Quote_Miss.getCode().equals(entity.getIsCalculated())){
				try{
					calcu(entity);
				}
				catch(Exception ex){
					XxlJobLogger.log("采用【标准报价】计算异常 "+ ex.getMessage());	
				}
			}
		}
	}
	
	
	private void calcu(BizOutstockPackmaterialEntity entity){
		
		long start = System.currentTimeMillis();// 系统开始时间
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("subjectId", SubjectId); //费用科目必填
		map.put("storageTemplateType" , "STORAGE_MATERIAL_PRICE_TEMPLATE");
		map.put("materialCode",entity.getConsumerMaterialCode());
		map.put("warehouseId", entity.getWarehouseCode());
		
		CalcuReqVo reqVo = standardReqVoServiceImpl.getMaterialReqVo(map);
		if("true".equals(reqVo.getParams().get("succ"))){
			XxlJobLogger.log("消息【{0}】 规则编号【{1}】",reqVo.getParams().get("msg").toString(),reqVo.getRuleNo());	
			reqVo.setBizData(entity);
			CalcuResultVo resultVo = feesCalcuService.FeesCalcuService(reqVo);
			if("succ".equals(resultVo.getSuccess())){
				FeesReceiveStorageEntity storageFeeEntity = new FeesReceiveStorageEntity();
				storageFeeEntity.setFeesNo(entity.getFeesNo());
				storageFeeEntity.setParam1(TemplateTypeEnum.STANDARD.getCode());
				storageFeeEntity.setParam2(resultVo.getMethod());//
				storageFeeEntity.setParam3(resultVo.getQuoId());
				entity.setRemark("计算成功");
				entity.setIsCalculated(CalculateState.Finish.getCode());
				storageFeeEntity.setIsCalculated(CalculateState.Finish.getCode());
				storageFeeEntity.setCost(resultVo.getPrice());
				feesReceiveStorageService.updateOne(storageFeeEntity);
			}
			else{
				entity.setRemark("【标准报价】费用计算失败:"+resultVo.getMsg());
			}
		}
		else{
			XxlJobLogger.log(reqVo.getParams().get("msg").toString());	
			entity.setRemark((String) reqVo.getParams().get("msg"));
		}
		bizOutstockPackmaterialService.update(entity);
		long current = System.currentTimeMillis();;// 当前系统时间
		XxlJobLogger.log("【标准报价】调用规则引擎   耗时【{0}】毫秒  费用【{1}】 ",(current - start));	
	}


}
