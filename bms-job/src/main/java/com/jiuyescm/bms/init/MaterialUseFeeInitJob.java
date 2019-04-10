package com.jiuyescm.bms.init;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;

import com.alibaba.dubbo.common.utils.CollectionUtils;
import com.google.common.collect.Maps;
import com.jiuyescm.bms.asyn.service.IBmsCalcuTaskService;
import com.jiuyescm.bms.asyn.vo.BmsCalcuTaskVo;
import com.jiuyescm.bms.biz.storage.entity.BizOutstockPackmaterialEntity;
import com.jiuyescm.bms.common.JobParameterHandler;
import com.jiuyescm.bms.common.enumtype.TemplateTypeEnum;
import com.jiuyescm.bms.general.entity.FeesReceiveStorageEntity;
import com.jiuyescm.bms.general.service.IFeesReceiveStorageService;
import com.jiuyescm.bms.receivable.storage.service.IBizOutstockPackmaterialService;
import com.jiuyescm.cfm.common.JAppContext;
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
	@Autowired private IFeesReceiveStorageService feesReceiveStorageService;
	@Autowired private IPubMaterialInfoService pubMaterialInfoService;
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
		StopWatch watch = new StopWatch();
		watch.start();
		int num = 1000;
		
		Map<String, Object> map = new HashMap<String, Object>();
		if(params != null && params.length > 0) {
			try {
				map = JobParameterHandler.handler(params);//处理定时任务参数
			} catch (Exception e) {
				watch.stop();
				XxlJobLogger.log("【终止异常】,解析Job配置的参数出现错误,原因:" + e.getMessage() + ",耗时："+ watch.getTotalTimeSeconds() + "毫秒");
	            return ReturnT.FAIL;
			}
		}else {
			//未配置最多执行多少运单
			map.put("num", num);
		}
		
		//查询所有状态为0的业务数据
		map.put("isCalculated", "0");
		Map<String, Object> taskVoMap = new HashMap<>();
		
		saveFees(map,taskVoMap);
		
		watch.stop();
		
		sendTask(taskVoMap);

		XxlJobLogger.log("初始化费用总耗时：【{0}】毫秒", watch.getTotalTimeSeconds());
        return ReturnT.SUCCESS;
	}

	
	private void saveFees(Map<String, Object> map,Map<String, Object> taskVoMap){
		List<BizOutstockPackmaterialEntity> bizList = null;
		List<FeesReceiveStorageEntity> feesList = new ArrayList<FeesReceiveStorageEntity>();
		try {
			XxlJobLogger.log("materialUseFeeInitJob查询条件map:【{0}】  ",map);
			bizList = bizOutstockPackmaterialService.query(map);
			if(CollectionUtils.isNotEmpty(bizList)){
				for (BizOutstockPackmaterialEntity entity : bizList) {
					FeesReceiveStorageEntity fee = initFees(entity);
					feesList.add(fee);					
					String customerId = entity.getCustomerId();
					String subjectCode = fee.getSubjectCode();
					String creMonth = new SimpleDateFormat("yyyyMM").format(entity.getCreateTime());
					StringBuilder sb1 = new StringBuilder();
					sb1.append(customerId).append("-").append(subjectCode).append("-").append(creMonth);
					taskVoMap.put(sb1.toString(), sb1.toString());
				}
				XxlJobLogger.log("【耗材】查询行数【{0}】", bizList.size());
				
				//批量更新业务数据&批量写入费用表
				updateAndInsertBatch(bizList,feesList);
			}
		} catch (Exception e) {
			XxlJobLogger.log("【终止异常】,查询业务数据异常,原因: {0}", e.getMessage());
			return;
		}
		
		if(bizList== null || bizList.size() == 0){
			return;
		}else{
			saveFees(map,taskVoMap);
		}
		
	}
	
	
	private FeesReceiveStorageEntity initFees(BizOutstockPackmaterialEntity entity) {
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
		storageFeeEntity.setQuantity(0d);//计费数量
		storageFeeEntity.setStatus("0");								//状态
		storageFeeEntity.setOrderNo(entity.getOutstockNo());
		storageFeeEntity.setBizId(String.valueOf(entity.getId()));						//业务数据主键
		storageFeeEntity.setWeight(0d);					//设置重量
		storageFeeEntity.setCost(new BigDecimal(0));					//入仓金额
		storageFeeEntity.setParam1(TemplateTypeEnum.COMMON.getCode());
		storageFeeEntity.setDelFlag("0");
		storageFeeEntity.setIsCalculated("99");
		entity.setIsCalculated("1");
		entity.setCalculateTime(JAppContext.currentTimestamp());
		return storageFeeEntity;
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

	private void sendTask(Map<String, Object> taskVos) {

		for (String key : taskVos.keySet()) { 
			String[] param = key.split("-");
			BmsCalcuTaskVo vo = new BmsCalcuTaskVo();
			try{
				vo.setCrePerson("系统");
				vo.setCrePersonId("system");
				vo.setCustomerId(param[0]);
				vo.setSubjectCode(param[1]);
				vo.setCreMonth(Integer.valueOf(param[2]));
				bmsCalcuTaskService.sendTask(vo);
				XxlJobLogger.log("mq发送成功,商家id:{0},年月:{1},科目id:{2}", vo.getCustomerId(),vo.getCreMonth(),vo.getSubjectCode());
			}
			catch(Exception ex){
				XxlJobLogger.log("mq发送失败{0}", ex);
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
