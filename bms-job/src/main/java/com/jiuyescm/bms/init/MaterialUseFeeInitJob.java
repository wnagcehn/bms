package com.jiuyescm.bms.init;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Maps;
import com.jiuyescm.bms.asyn.service.IBmsCalcuTaskService;
import com.jiuyescm.bms.asyn.vo.BmsCalcuTaskVo;
import com.jiuyescm.bms.base.dict.api.IMaterialDictService;
import com.jiuyescm.bms.base.dictionary.repository.ISystemCodeRepository;
import com.jiuyescm.bms.base.group.service.IBmsGroupSubjectService;
import com.jiuyescm.bms.biz.storage.entity.BizOutstockPackmaterialEntity;
import com.jiuyescm.bms.common.JobParameterHandler;
import com.jiuyescm.bms.common.enumtype.TemplateTypeEnum;
import com.jiuyescm.bms.drools.IFeesCalcuService;
import com.jiuyescm.bms.general.entity.FeesReceiveStorageEntity;
import com.jiuyescm.bms.general.service.IFeesReceiveStorageService;
import com.jiuyescm.bms.general.service.IPriceContractInfoService;
import com.jiuyescm.bms.general.service.IStorageQuoteFilterService;
import com.jiuyescm.bms.general.service.SequenceService;
import com.jiuyescm.bms.quotation.contract.repository.imp.IPriceContractDao;
import com.jiuyescm.bms.quotation.contract.repository.imp.IPriceContractItemRepository;
import com.jiuyescm.bms.quotation.storage.repository.IPriceMaterialQuotationRepository;
import com.jiuyescm.bms.quotation.transport.repository.IGenericTemplateRepository;
import com.jiuyescm.bms.receivable.dispatch.service.IBizDispatchBillService;
import com.jiuyescm.bms.receivable.storage.service.IBizOutstockPackmaterialService;
import com.jiuyescm.bms.rule.receiveRule.repository.IReceiveRuleRepository;
import com.jiuyescm.cfm.common.JAppContext;
import com.jiuyescm.common.utils.DoubleUtil;
import com.jiuyescm.contract.quote.api.IContractQuoteInfoService;
import com.jiuyescm.framework.sequence.api.ISnowflakeSequenceService;
import com.jiuyescm.mdm.customer.api.IPubMaterialInfoService;
import com.jiuyescm.mdm.customer.vo.PubMaterialInfoVo;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHander;
import com.xxl.job.core.log.XxlJobLogger;

/**
 * 耗材费用初始化
 * @author zhaofeng
 *
 */
@JobHander(value="materialUseFeeInitJob")
@Service
public class MaterialUseFeeInitJob extends IJobHandler{
	
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
	@Autowired private ISnowflakeSequenceService snowflakeSequenceService;
	@Autowired private IBmsCalcuTaskService bmsCalcuTaskService;

	
	Map<String,PubMaterialInfoVo> materialMap = null;
	Map<String, String> temMap = null;


	protected void initConf(){
		materialMap=queryAllMaterial();
	}

	@Override
	public ReturnT<String> execute(String... params) throws Exception {
		XxlJobLogger.log("MaterialUseFeeInitJob start.");
		XxlJobLogger.log("开始耗材费用初始化任务");
		return CalcJob(params);
	}
	
	private ReturnT<String> CalcJob(String[] params) {
		long startTime = System.currentTimeMillis();
		int num = 1000;
		
		Map<String, Object> map = new HashMap<String, Object>();
		if(params != null && params.length > 0) {
			try {
				map = JobParameterHandler.handler(params);//处理定时任务参数
			} catch (Exception e) {
				XxlJobLogger.log("【终止异常】,解析Job配置的参数出现错误,原因:" + e.getMessage() + ",耗时："+ (System.currentTimeMillis() - startTime) + "毫秒");
	            return ReturnT.FAIL;
			}
		}else {
			//未配置最多执行多少运单
			map.put("num", num);
		}
		
		//查询所有状态为0的业务数据
		long currentTime = System.currentTimeMillis();
		map.put("isCalculated", "0");
		List<BizOutstockPackmaterialEntity> bizList = null;
		List<FeesReceiveStorageEntity> feesList = new ArrayList<FeesReceiveStorageEntity>();
		try {
			XxlJobLogger.log("materialUseFeeInitJob查询条件map:【{0}】  ",map);
			bizList = bizOutstockPackmaterialService.query(map);
			//只要有业务数据，就进行初始化和更新写入操作
			if( CollectionUtils.isNotEmpty(bizList) ){
				XxlJobLogger.log("【耗材】查询行数【{0}】耗时【{1}】", bizList.size(), (System.currentTimeMillis()-currentTime));
				//初始化费用
				initFees(bizList, feesList);
				//批量更新业务数据&批量写入费用表
				updateAndInsertBatch(bizList,feesList);
			}
			//只有业务数据查出来小于1000才发送mq，这时候才代表统计完成，才发送MQ
			if( CollectionUtils.isEmpty(bizList)||bizList.size()<num){
				try {
					sendTask(feesList);
				} catch (Exception e) {
					XxlJobLogger.log("mq发送失败{0}", e);
				}
			}
		} catch (Exception e) {
			XxlJobLogger.log("【终止异常】,查询业务数据异常,原因: {0} ,耗时： {1}毫秒", e.getMessage(), ((System.currentTimeMillis() - currentTime)));
			return ReturnT.FAIL;
		}
		
		XxlJobLogger.log("初始化费用总耗时：【{0}】毫秒", System.currentTimeMillis() - startTime);
        return ReturnT.SUCCESS;
	}

	private void initFees(List<BizOutstockPackmaterialEntity> bizList, List<FeesReceiveStorageEntity> feesList) {
		for (BizOutstockPackmaterialEntity entity : bizList) {
			entity.setRemark("");
			FeesReceiveStorageEntity storageFeeEntity = new FeesReceiveStorageEntity();	
			String feesNo = "STO" + snowflakeSequenceService.nextStringId();
			entity.setFeesNo(feesNo);
			storageFeeEntity.setFeesNo(feesNo);
			storageFeeEntity.setCreator("system");
			storageFeeEntity.setCreateTime(entity.getCreateTime());
			storageFeeEntity.setOperateTime(entity.getCreateTime());
			storageFeeEntity.setCustomerId(entity.getCustomerId());			//商家ID
			storageFeeEntity.setCustomerName(entity.getCustomerName());		//商家名称
			storageFeeEntity.setWarehouseCode(entity.getWarehouseCode());	//仓库ID
			storageFeeEntity.setWarehouseName(entity.getWarehouseName());	//仓库名称
			storageFeeEntity.setCostType("FEE_TYPE_MATERIAL");				//费用类别 FEE_TYPE_GENEARL-通用 FEE_TYPE_MATERIAL-耗材 FEE_TYPE_ADD-增值
			storageFeeEntity.setSubjectCode("wh_material_use");						//费用科目
			storageFeeEntity.setOtherSubjectCode("wh_material_use");
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
			storageFeeEntity.setCost(new BigDecimal(0));					//入仓金额
			storageFeeEntity.setParam1(TemplateTypeEnum.COMMON.getCode());
			storageFeeEntity.setDelFlag("0");
			storageFeeEntity.setIsCalculated("99");
			entity.setIsCalculated("1");
			entity.setCalculateTime(JAppContext.currentTimestamp());
			feesList.add(storageFeeEntity);
		}	
	}
	
	public void updateAndInsertBatch(List<BizOutstockPackmaterialEntity> ts,List<FeesReceiveStorageEntity> fs) {
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

	private void sendTask(List<FeesReceiveStorageEntity> feesList) throws Exception {
		List<String> subjectList=new ArrayList<>();
		subjectList.add("wh_material_use");
		//对这些费用按照商家、科目、时间排序
		Map<String,Object> map=new HashMap<>();
		map.put("isCalculated", "99");
		map.put("subjectList", subjectList);
		
		List<BmsCalcuTaskVo> list=bmsCalcuTaskService.queryByMap(map);
		
		for (BmsCalcuTaskVo vo : list) {
			vo.setCrePerson("系统");
			vo.setCrePersonId("system");
			try {
				bmsCalcuTaskService.sendTask(vo);
			} catch (Exception e) {
				// TODO: handle exception
				XxlJobLogger.log("发送mq消息失败 ",e);
			}
		}
	}
	
	/**
	 * 查询耗材编码-耗材映射
	 * @return
	 */
	public Map<String,PubMaterialInfoVo> queryAllMaterial(){
		Map<String,Object> condition=Maps.newHashMap();
		List<PubMaterialInfoVo> tmscodels = pubMaterialInfoService.queryList(condition);
		Map<String,PubMaterialInfoVo> map=Maps.newLinkedHashMap();
		for(PubMaterialInfoVo materialVo:tmscodels){
			if(!StringUtils.isBlank(materialVo.getBarcode())){
				map.put(materialVo.getBarcode().trim(),materialVo);
			}
		}
		return map;
	}
}
