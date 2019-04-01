package com.jiuyescm.bms.init;

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
import com.jiuyescm.bms.base.group.service.IBmsGroupSubjectService;
import com.jiuyescm.bms.biz.dispatch.entity.BizDispatchBillEntity;
import com.jiuyescm.bms.biz.storage.repository.IBizOutstockPackmaterialRepository;
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

		long startTime = System.currentTimeMillis();
		int num = 1000;

		Map<String, Object> map = new HashMap<String, Object>();
		if (params != null && params.length > 0) {
			try {
				map = JobParameterHandler.handler(params);// 处理定时任务参数
			} catch (Exception e) {
				XxlJobLogger.log("【终止异常】,解析Job配置的参数出现错误,原因:" + e.getMessage() + ",耗时："
						+ (System.currentTimeMillis() - startTime) + "毫秒");
				return ReturnT.FAIL;
			}
		} else {
			// 未配置最多执行多少运单
			map.put("num", num);
		}

		// 初始化配置
		initConf();

		// 查询所有状态为0的业务数据
		long currentTime = System.currentTimeMillis();
		map.put("isCalculated", "0");
		List<BizDispatchBillEntity> bizList = null;
		List<FeesReceiveDispatchEntity> feesList = new ArrayList<FeesReceiveDispatchEntity>();
		try {
			XxlJobLogger.log("dispatchBillInitJob查询条件map:【{0}】  ", map);
			bizList = bizDispatchBillService.query(map);
			if (CollectionUtils.isNotEmpty(bizList)) {
				XxlJobLogger.log("【配送运单】查询行数【{0}】耗时【{1}】", bizList.size(), (System.currentTimeMillis() - currentTime));
				// 初始化费用
				initFees(bizList, feesList);
				// 批量更新业务数据&批量写入费用表
				updateAndInsertBatch(bizList, feesList);
			}

			// 只有业务数据查出来小于1000才发送mq，这时候才代表统计完成，才发送MQ
			if (CollectionUtils.isEmpty(bizList) || bizList.size() < num) {
				sendTask();
			}

		} catch (Exception e) {
			XxlJobLogger.log("【终止异常】,查询业务数据异常,原因: {0} ,耗时： {1}毫秒", e.getMessage(),
					((System.currentTimeMillis() - currentTime)));
			return ReturnT.FAIL;
		}

		XxlJobLogger.log("初始化费用总耗时：【{0}】毫秒", System.currentTimeMillis() - startTime);
		return ReturnT.SUCCESS;

	}

	private void initFees(List<BizDispatchBillEntity> bizList, List<FeesReceiveDispatchEntity> feesList) {
		for (BizDispatchBillEntity entity : bizList) {
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
			feeEntity.setIsCalculated("99");
			feeEntity.setCreator("system");
			feeEntity.setCreateTime(entity.getCreateTime());// 费用表的创建时间应为业务表的创建时间
			feeEntity.setDelFlag("0");
			feeEntity.setStatus("0");
			feeEntity.setParam1(TemplateTypeEnum.COMMON.getCode());

			feesList.add(feeEntity);
		}
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

	private void sendTask() throws Exception {
		Map<String, Object> sendTaskMap = new HashMap<String, Object>();
		sendTaskMap.put("isCalculated", "99");
		sendTaskMap.put("subjectList", Arrays.asList(subjects));
		// 对这些费用按照商家、科目、时间排序
		List<BmsCalcuTaskVo> list = bmsCalcuTaskService.queryDisByMap(sendTaskMap);
		for (BmsCalcuTaskVo vo : list) {
			vo.setCrePerson("系统");
			vo.setCrePersonId("system");
			try {
				bmsCalcuTaskService.sendTask(vo);
				XxlJobLogger.log("mq发送成功,商家id:{0},年月:{1},科目id:{2}", vo.getCustomerId(), vo.getCreMonth(),
						vo.getSubjectCode());
			} catch (Exception e) {
				XxlJobLogger.log("mq发送失败{0}", e);
			}
		}
	}

	public void updateAndInsertBatch(List<BizDispatchBillEntity> billList, List<FeesReceiveDispatchEntity> feesList) {
		try {
			long start = System.currentTimeMillis();// 系统开始时间
			long current = 0l;// 当前系统时间
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