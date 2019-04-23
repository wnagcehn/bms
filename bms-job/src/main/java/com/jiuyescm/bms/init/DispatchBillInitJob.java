package com.jiuyescm.bms.init;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;

import com.alibaba.dubbo.common.utils.CollectionUtils;
import com.jiuyescm.bms.asyn.service.IBmsCalcuTaskService;
import com.jiuyescm.bms.asyn.vo.BmsCalcuTaskVo;
import com.jiuyescm.bms.base.group.service.IBmsGroupSubjectService;
import com.jiuyescm.bms.biz.dispatch.entity.BizDispatchBillEntity;
import com.jiuyescm.bms.common.JobParameterHandler;
import com.jiuyescm.bms.common.enumtype.TemplateTypeEnum;
import com.jiuyescm.bms.general.entity.FeesReceiveDispatchEntity;
import com.jiuyescm.bms.general.service.IFeesReceiveDispatchService;
import com.jiuyescm.bms.receivable.dispatch.service.IBizDispatchBillService;
import com.jiuyescm.framework.sequence.api.ISnowflakeSequenceService;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHander;
import com.xxl.job.core.log.XxlJobLogger;

/**
 * 配送费用初始化
 * 
 * @author wangchen
 *
 */
@JobHander(value = "dispatchBillInitJob")
@Service
public class DispatchBillInitJob extends IJobHandler {

	@Autowired
	private IBmsGroupSubjectService bmsGroupSubjectService;
	@Autowired
	private IBizDispatchBillService bizDispatchBillService;
	@Autowired
	private ISnowflakeSequenceService snowflakeSequenceService;
	@Autowired
	private IBmsCalcuTaskService bmsCalcuTaskService;
	@Autowired
	private IFeesReceiveDispatchService feesReceiveDispatchService;
	private String _subjectCode = "de_delivery_amount";

	String[] subjects = null;

	@Override
	public ReturnT<String> execute(String... params) throws Exception {
		XxlJobLogger.log("DispatchBillInitJob start.");
		XxlJobLogger.log("开始配送费用初始化任务");
		return CalcJob(params);
	}

	private ReturnT<String> CalcJob(String[] params) {

		StopWatch watch = new StopWatch();
		watch.start();
		int num = 1000;

		Map<String, Object> map = new HashMap<String, Object>();
		if (params != null && params.length > 0) {
			try {
				map = JobParameterHandler.handler(params);// 处理定时任务参数
			} catch (Exception e) {
				watch.stop();
				XxlJobLogger.log("【终止异常】,解析Job配置的参数出现错误,原因:" + e.getMessage() + ",耗时："
						+ watch.getTotalTimeSeconds() + "毫秒");
				return ReturnT.FAIL;
			}
		} else {
			// 未配置最多执行多少运单
			map.put("num", num);
		}

		// 初始化配置
		initConf();

		// 查询所有状态为0的业务数据
		map.put("isCalculated", "0");
		Map<String, Object> taskVoMap = new HashMap<>();
		saveFees(map,taskVoMap);

		watch.stop();	
		sendTask(taskVoMap);
		XxlJobLogger.log("初始化费用总耗时：【{0}】毫秒",  watch.getTotalTimeSeconds());
		return ReturnT.SUCCESS;

	}

	private void saveFees(Map<String, Object> map,Map<String, Object> taskVoMap){

		List<BizDispatchBillEntity> bizList = null;
		List<FeesReceiveDispatchEntity> feesList = new ArrayList<FeesReceiveDispatchEntity>();
		try {
			XxlJobLogger.log("dispatchBillInitJob查询条件map:【{0}】  ", map);
			bizList = bizDispatchBillService.query(map);
			if(CollectionUtils.isNotEmpty(bizList)){
				for (BizDispatchBillEntity entity : bizList) {
					FeesReceiveDispatchEntity fee = initFees(entity);
					feesList.add(fee);				
					String customerId = entity.getCustomerid();
					String subjectCode = fee.getSubjectCode();
					String creMonth = new SimpleDateFormat("yyyyMM").format(entity.getCreateTime());
					StringBuilder sb1 = new StringBuilder();
					sb1.append(customerId).append("-").append(subjectCode).append("-").append(creMonth);
					taskVoMap.put(sb1.toString(), sb1.toString());
				}
				XxlJobLogger.log("【配送】查询行数【{0}】", bizList.size());
				
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

	
	private FeesReceiveDispatchEntity initFees(BizDispatchBillEntity entity) {
		String feesNo = "STO" + snowflakeSequenceService.nextStringId();
		// 业务数据修改
		entity.setFeesNo(feesNo);
		entity.setIsCalculated("1");
		entity.setRemark("");

		// 费用初始化
		FeesReceiveDispatchEntity feeEntity = new FeesReceiveDispatchEntity();

		feeEntity.setFeesNo(feesNo);
		feeEntity.setWaybillNo(entity.getWaybillNo()); // 运单号
		feeEntity.setOutstockNo(entity.getOutstockNo()); // 出库单号
		feeEntity.setExternalNo(entity.getExternalNo()); // 外部单号
		feeEntity.setWarehouseCode(entity.getWarehouseCode()); // 仓库ID
		feeEntity.setWarehouseName(entity.getWarehouseName()); // 仓库名称
		feeEntity.setCustomerid(entity.getCustomerid()); // 商家ID
		feeEntity.setCustomerName(entity.getCustomerName()); // 商家名称
		feeEntity.setSubjectCode(_subjectCode);
		feeEntity.setOtherSubjectCode(_subjectCode);
		feeEntity.setAcceptTime(entity.getAcceptTime()); // 揽收时间
		feeEntity.setSignTime(entity.getSignTime()); // 签收时间
		feeEntity.setBigstatus(entity.getBigstatus());
		feeEntity.setSmallstatus(entity.getSmallstatus());
		feeEntity.setDeliveryDate(entity.getCreateTime());
		feeEntity.setIsCalculated("99");
		feeEntity.setCreator("system");
		feeEntity.setCreateTime(entity.getCreateTime());// 费用表的创建时间应为业务表的创建时间
		feeEntity.setDelFlag("0");
		feeEntity.setStatus("0");
		feeEntity.setParam1(TemplateTypeEnum.COMMON.getCode());
	    feeEntity.setDeliveryid(entity.getDeliverid());         // 物流商ID
	    feeEntity.setDeliverName(entity.getDeliverName());        // 物流商名称
	    feeEntity.setTemperatureType(entity.getTemperatureTypeCode());//温度

		return feeEntity;
	}

	private void initConf() {
		subjects = initSubjects();
	}

	private String[] initSubjects() {
		// 这里的科目应该在科目组中配置,动态查询
		// de_delivery_amount(运费 )
		Map<String, String> map = bmsGroupSubjectService.getSubject("job_subject_dispatch");
		if (map.size() == 0) {
			String[] strs = { "de_delivery_amount" };
			return strs;
		} else {
			int i = 0;
			String[] strs = new String[map.size()];
			for (String value : map.keySet()) {
				strs[i] = value;
				i++;
			}
			return strs;
		}
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

	public void updateAndInsertBatch(List<BizDispatchBillEntity> billList, List<FeesReceiveDispatchEntity> feesList) {
		try {
			long start = System.currentTimeMillis();// 系统开始时间
			long current = 0L;// 当前系统时间
			bizDispatchBillService.newUpdateBatch(billList);
			current = System.currentTimeMillis();
			XxlJobLogger.log("更新业务数据耗时：【{0}】毫秒  ", (current - start));
			start = System.currentTimeMillis();// 系统开始时间
			feesReceiveDispatchService.InsertBatch(feesList);
			current = System.currentTimeMillis();
			XxlJobLogger.log("更新费用数据耗时：【{0}】毫秒 ", (current - start));
		} catch (Exception e) {
			XxlJobLogger.log("-->批量保存异常" + e.getMessage());
		}

	}

}
