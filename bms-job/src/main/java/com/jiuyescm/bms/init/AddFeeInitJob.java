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
import com.jiuyescm.bms.asyn.service.IBmsCalcuTaskService;
import com.jiuyescm.bms.asyn.vo.BmsCalcuTaskVo;
import com.jiuyescm.bms.base.group.service.IBmsGroupCustomerService;
import com.jiuyescm.bms.common.JobParameterHandler;
import com.jiuyescm.bms.general.entity.BizAddFeeEntity;
import com.jiuyescm.bms.general.entity.FeesReceiveStorageEntity;
import com.jiuyescm.bms.general.service.IBizAddFeeService;
import com.jiuyescm.bms.general.service.IFeesReceiveStorageService;
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
    @Autowired
    private IBmsGroupCustomerService bmsGroupCustomerService;

    List<String> noCalculateList=null;

	@Override
	public ReturnT<String> execute(String... params) throws Exception {
		XxlJobLogger.log("AddFeeInitJob start.");
		XxlJobLogger.log("开始增值费用初始化任务");
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
				XxlJobLogger.log("【终止异常】,解析Job配置的参数出现错误,原因:{0},耗时{1}毫秒" , e , watch.getTotalTimeSeconds());
	            return ReturnT.FAIL;
			}
		}else {
			//未配置最多执行多少运单
			map.put("num", num);
		}
		
		initConf();
		
		//查询所有状态为0的业务数据
		map.put("isCalculated", "0");	
		Map<String, Object> taskVoMap = new HashMap<>();	
		saveFees(map,taskVoMap);	
		watch.stop();
		sendTask(taskVoMap);
		XxlJobLogger.log("初始化费用总耗时：【{0}】毫秒", watch.getTotalTimeMillis());
        return ReturnT.SUCCESS;
	}
	
	private void saveFees(Map<String, Object> map,Map<String, Object> taskVoMap){

		List<BizAddFeeEntity> bizList = null;
		List<FeesReceiveStorageEntity> feesList = new ArrayList<FeesReceiveStorageEntity>();
		try {
			XxlJobLogger.log("addFeeInitJob查询条件map:【{0}】  ",map);
			bizList = bizAddFeeService.querybizAddFee(map);
			if(CollectionUtils.isNotEmpty(bizList)){
			    List<String> feesNos=new ArrayList<>();
				for (BizAddFeeEntity entity : bizList) {
				    if(StringUtils.isNotBlank(entity.getFeesNo())){
                        feesNos.add(entity.getFeesNo());
                    }
				    //优先判断该商家是否是不计费商家，不计费商家状态直接置为4
				    //如果是不计费的商家，则直接更新业务计算状态为4
                    if(noCalculateList.size()>0 && noCalculateList.contains(entity.getCustomerid())){
                        entity.setDelFlag("4");
                        continue;
                    }
				   
					FeesReceiveStorageEntity fee = initFees(entity);
					feesList.add(fee);				
					String creMonth = new SimpleDateFormat("yyyyMM").format(entity.getCreateTime());
					StringBuilder sb = new StringBuilder();
					sb.append(entity.getCustomerid()).append("-").append("wh_value_add_subject").append("-").append(creMonth);
					taskVoMap.put(sb.toString(), sb.toString());
				}
				XxlJobLogger.log("【增值】查询行数【{0}】", bizList.size());
				
				//如果有历史费用，则逻辑删除
				if(feesNos.size()>0){
				    Map<String,Object> condition=new HashMap<>();
				    condition.put("feesNos", feesNos);
				    long start = System.currentTimeMillis();// 系统开始时间
				    feesReceiveStorageService.updateBatchFeeNo(condition);
				    long current = System.currentTimeMillis();
			        XxlJobLogger.log("删除历史费用数据耗时：【{0}】毫秒",(current - start));
				}
				
				//批量更新业务数据&批量写入费用表
				updateAndInsertBatch(bizList,feesList);
			}
		} catch (Exception e) {
			XxlJobLogger.log("【终止异常】,查询业务数据异常,原因: {0}", e);
			return;
		}
		
		if(bizList== null || bizList.size() == 0){
			return;
		}
		else{
			saveFees(map,taskVoMap);
		}
	}

	private FeesReceiveStorageEntity initFees(BizAddFeeEntity entity) {
		FeesReceiveStorageEntity fee = new FeesReceiveStorageEntity();
		String feesNo = "STO" + snowflakeSequenceService.nextStringId();
		entity.setFeesNo(feesNo);
		
		//更改业务数据状态
		entity.setIsCalculated("1");
	
    	fee.setFeesNo(feesNo);
    	if(entity.getPrice()!=null){
    		fee.setCost(new BigDecimal(entity.getPrice()));
    	}else{
    		fee.setCost(new BigDecimal(0));
    	}
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
		return fee;
	}
	
	public void updateAndInsertBatch(List<BizAddFeeEntity> ts,List<FeesReceiveStorageEntity> fs) {
		long start = System.currentTimeMillis();// 系统开始时间
		long current = 0L;// 当前系统时间
		bizAddFeeService.updateList(ts);
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
	
    private void initConf() {
        noCalculateList=bmsGroupCustomerService.queryCustomerByGroupCode("no_calculate_customer");
    }

}
