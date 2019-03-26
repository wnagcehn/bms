package com.jiuyescm.bms.init;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jiuyescm.bms.asyn.service.IBmsCalcuTaskService;
import com.jiuyescm.bms.asyn.vo.BmsCalcuTaskVo;
import com.jiuyescm.bms.common.JobParameterHandler;
import com.jiuyescm.bms.general.entity.BizAddFeeEntity;
import com.jiuyescm.bms.general.entity.FeesReceiveStorageEntity;
import com.jiuyescm.bms.general.service.IBizAddFeeService;
import com.jiuyescm.bms.general.service.IFeesReceiveStorageService;
import com.jiuyescm.bms.subject.service.IBmsSubjectInfoService;
import com.jiuyescm.cfm.common.JAppContext;
import com.jiuyescm.common.utils.DoubleUtil;
import com.jiuyescm.framework.sequence.api.ISnowflakeSequenceService;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHander;
import com.xxl.job.core.log.XxlJobLogger;

/**
 * 增值费用初始化
 * @author wangchen
 *
 */
@JobHander(value="addFeeInitJob")
@Service
public class AddFeeInitJob extends IJobHandler{
	
	@Autowired private IBizAddFeeService bizAddFeeService;
	@Autowired private ISnowflakeSequenceService snowflakeSequenceService;
	@Autowired private IBmsCalcuTaskService bmsCalcuTaskService;
	@Autowired private IFeesReceiveStorageService feesReceiveStorageService;

	@Override
	public ReturnT<String> execute(String... params) throws Exception {
		XxlJobLogger.log("AddFeeInitJob start.");
		XxlJobLogger.log("开始增值费用初始化任务");
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
		List<BizAddFeeEntity> bizList = null;
		List<FeesReceiveStorageEntity> feesList = new ArrayList<FeesReceiveStorageEntity>();
		try {
			XxlJobLogger.log("addFeeInitJob查询条件map:【{0}】  ",map);
			bizList = bizAddFeeService.querybizAddFee(map);
			if(CollectionUtils.isNotEmpty(bizList)){
				XxlJobLogger.log("【增值】查询行数【{0}】耗时【{1}】", bizList.size(), (System.currentTimeMillis()-currentTime));
				//初始化费用
				initFees(bizList, feesList);
				//批量更新业务数据&批量写入费用表
				updateAndInsertBatch(bizList,feesList);
			}
			
			//只有业务数据查出来小于1000才发送mq，这时候才代表统计完成，才发送MQ
			if(CollectionUtils.isEmpty(bizList)||bizList.size()<num){
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

	private void initFees(List<BizAddFeeEntity> bizList, List<FeesReceiveStorageEntity> feesList) {
		for (BizAddFeeEntity entity : bizList) {
			FeesReceiveStorageEntity fee = new FeesReceiveStorageEntity();
			String feesNo = "STO" + snowflakeSequenceService.nextStringId();
			//更改业务数据状态
			entity.setIsCalculated("99");
		
	    	fee.setFeesNo(feesNo);
	    	if(entity.getPrice()!=null){
	    		fee.setCost(new BigDecimal(entity.getPrice()));
	    	}else{
	    		fee.setCost(new BigDecimal(0));
	    	}
	    	fee.setIsCalculated(entity.getIsCalculated());
	    	fee.setCalculateTime(JAppContext.currentTimestamp());
			fee.setUnitPrice(entity.getUnitPrice());
			fee.setSubjectCode("wh_value_add_subject");
			fee.setOtherSubjectCode(entity.getFeesType());
			fee.setCustomerId(entity.getCustomerid());
			fee.setCustomerName(entity.getCustomerName());
			fee.setWarehouseCode(entity.getWarehouseCode());
			fee.setUnit(entity.getFeesUnit());
			double num=DoubleUtil.isBlank(entity.getAdjustNum())?entity.getNum():entity.getAdjustNum();
			if(!DoubleUtil.isBlank(num)){
				fee.setQuantity(num);
			}
	        fee.setParam1(entity.getItem());
	        fee.setCustomerName(entity.getCustomerName());
	        fee.setWarehouseName(entity.getWarehouseName());	
			fee.setCreateTime(entity.getCreateTime());
			fee.setCreator("system");	
			fee.setCostType("FEE_TYPE_GENEARL");
			fee.setDelFlag("0");
			fee.setStatus("0");
			fee.setExternalProductNo(entity.getExternalNo());
			fee.setIsCalculated("99");
			fee.setParam2(new SimpleDateFormat("yyyyMM").format(entity.getCreateTime()));
			feesList.add(fee);
		}	
	}
	
	public void updateAndInsertBatch(List<BizAddFeeEntity> ts,List<FeesReceiveStorageEntity> fs) {
		long start = System.currentTimeMillis();// 系统开始时间
		long current = 0l;// 当前系统时间
		bizAddFeeService.updateList(ts);
		current = System.currentTimeMillis();
		XxlJobLogger.log("更新业务数据耗时：【{0}】毫秒",(current - start));
		start = System.currentTimeMillis();// 系统开始时间
		feesReceiveStorageService.InsertBatch(fs);
		current = System.currentTimeMillis();
		XxlJobLogger.log("新增费用数据耗时：【{0}】毫秒  ",(current - start));
	}
	
	private static final List<String> subjectList= Arrays.asList("wh_value_add_subject");
	private static final Map<String, Object> sendTaskMap = new HashMap<String, Object>();
	static{
		sendTaskMap.put("isCalculated", "99");
		sendTaskMap.put("subjectList", subjectList);
	}

	private void sendTask(List<FeesReceiveStorageEntity> feesList) throws Exception {
		//对这些费用按照商家、科目、时间排序
		List<BmsCalcuTaskVo> list=bmsCalcuTaskService.queryByMap(sendTaskMap);		
		for (BmsCalcuTaskVo vo : list) {
			String taskId = "CAL" + snowflakeSequenceService.nextStringId();
			vo.setTaskId(taskId);
			vo.setCrePerson("系统");
			vo.setCrePersonId("system");
			vo.setCreTime(JAppContext.currentTimestamp());
			bmsCalcuTaskService.sendTask(vo);
		}
	}
}
