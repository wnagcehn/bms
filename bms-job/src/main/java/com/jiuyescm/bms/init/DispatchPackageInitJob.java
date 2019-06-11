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
import com.jiuyescm.bms.biz.dispatch.entity.BizDispatchPackageEntity;
import com.jiuyescm.bms.common.JobParameterHandler;
import com.jiuyescm.bms.common.enumtype.TemplateTypeEnum;
import com.jiuyescm.bms.general.entity.FeesReceiveStorageEntity;
import com.jiuyescm.bms.general.service.IFeesReceiveStorageService;
import com.jiuyescm.bms.receivable.storage.service.IBizDispatchPackageService;
import com.jiuyescm.cfm.common.JAppContext;
import com.jiuyescm.framework.sequence.api.ISnowflakeSequenceService;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHander;
import com.xxl.job.core.log.XxlJobLogger;

/**
 * 标准耗材使用费初始化
 * @author zhaofeng
 *
 */
@JobHander(value="dispatchPackageInitJob")
@Service
public class DispatchPackageInitJob extends IJobHandler{
	
    @Autowired private IBmsGroupCustomerService bmsGroupCustomerService;
	@Autowired private IBizDispatchPackageService bizDispatchPackageService;;
	@Autowired private IFeesReceiveStorageService feesReceiveStorageService;
	@Autowired private ISnowflakeSequenceService snowflakeSequenceService;
	@Autowired private IBmsCalcuTaskService bmsCalcuTaskService;
	List<String> noCalculateList=null;

	@Override
	public ReturnT<String> execute(String... params) throws Exception {
		XxlJobLogger.log("dispatchPackageInitJob start.");
		XxlJobLogger.log("开始标准耗材使用费初始化任务");
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
		
		// 初始化配置
        initConf();
		
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
		List<BizDispatchPackageEntity> bizList = null;
		List<FeesReceiveStorageEntity> feesList = new ArrayList<FeesReceiveStorageEntity>();
		try {
			XxlJobLogger.log("dispatchPackageInitJob查询条件map:【{0}】  ",map);
			bizList = bizDispatchPackageService.query(map);
			if(CollectionUtils.isNotEmpty(bizList)){
                List<String> feesNos=new ArrayList<>();
			    for (BizDispatchPackageEntity entity : bizList) {
			        if(StringUtils.isNotBlank(entity.getFeesNo())){
                        feesNos.add(entity.getFeesNo());
                    }
			        //如果是不计费的商家，则直接更新业务计算状态为4
                    if(noCalculateList.size()>0 && noCalculateList.contains(entity.getCustomerid())){
                        entity.setDelFlag("4");
                        continue;
                    }
			       
			        FeesReceiveStorageEntity fee = initFees(entity);
					feesList.add(fee);					
					String customerId = entity.getCustomerid();
					String subjectCode = fee.getSubjectCode();
					String creMonth = new SimpleDateFormat("yyyyMM").format(entity.getCreTime());
					StringBuilder sb1 = new StringBuilder();
					sb1.append(customerId).append("-").append(subjectCode).append("-").append(creMonth);
					taskVoMap.put(sb1.toString(), sb1.toString());
				}
				XxlJobLogger.log("【耗材】查询行数【{0}】", bizList.size());
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
			XxlJobLogger.log("【终止异常】,查询业务数据异常,原因: {0}", e.getMessage());
			return;
		}
		
		if(bizList== null || bizList.size() == 0){
			return;
		}else{
			saveFees(map,taskVoMap);
		}
		
	}
	
    protected void initConf(){
        noCalculateList=bmsGroupCustomerService.queryCustomerByGroupCode("no_calculate_customer");
    }
	private FeesReceiveStorageEntity initFees(BizDispatchPackageEntity entity) {
		entity.setRemark("");
		FeesReceiveStorageEntity storageFeeEntity = new FeesReceiveStorageEntity();	
		String feesNo = "STO" + snowflakeSequenceService.nextStringId();
		entity.setFeesNo(feesNo);
		storageFeeEntity.setFeesNo(feesNo);
		storageFeeEntity.setCreator("system");
		storageFeeEntity.setCreateTime(entity.getCreTime());
		storageFeeEntity.setOperateTime(entity.getCreTime());
		storageFeeEntity.setCustomerId(entity.getCustomerid());			//商家ID
		storageFeeEntity.setCustomerName(entity.getCustomerName());		//商家名称
		storageFeeEntity.setWarehouseCode(entity.getWarehouseCode());	//仓库ID
		storageFeeEntity.setWarehouseName(entity.getWarehouseName());	//仓库名称
		storageFeeEntity.setCostType("FEE_TYPE_MATERIAL");				//费用类别 FEE_TYPE_GENEARL-通用 FEE_TYPE_MATERIAL-耗材 FEE_TYPE_ADD-增值
		storageFeeEntity.setSubjectCode("wh_stand_material_use");						//费用科目
		storageFeeEntity.setOtherSubjectCode("wh_stand_material_use");
		storageFeeEntity.setProductType("");//商品类型
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
	
	public void updateAndInsertBatch(List<BizDispatchPackageEntity> ts,List<FeesReceiveStorageEntity> fs) {
		long start = System.currentTimeMillis();// 系统开始时间
		long current = 0L;// 当前系统时间
		bizDispatchPackageService.updateBatch(ts);
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
}
