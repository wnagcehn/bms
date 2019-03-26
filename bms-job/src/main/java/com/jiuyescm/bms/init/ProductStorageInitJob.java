package com.jiuyescm.bms.init;

import java.math.BigDecimal;
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
import com.jiuyescm.bms.biz.storage.entity.BizProductStorageEntity;
import com.jiuyescm.bms.common.JobParameterHandler;
import com.jiuyescm.bms.common.enumtype.TemplateTypeEnum;
import com.jiuyescm.bms.general.entity.FeesReceiveStorageEntity;
import com.jiuyescm.bms.general.service.IFeesReceiveStorageService;
import com.jiuyescm.bms.receivable.storage.service.IBizProductStorageService;
import com.jiuyescm.cfm.common.JAppContext;
import com.jiuyescm.framework.sequence.api.ISnowflakeSequenceService;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHander;
import com.xxl.job.core.log.XxlJobLogger;

/**
 * 商品按件存储费用初始化
 */
@JobHander(value = "productStorageInitJob")
@Service
public class ProductStorageInitJob extends IJobHandler {
	@Autowired
	private IFeesReceiveStorageService feesReceiveStorageService;
	@Autowired
	private IBizProductStorageService bizProductStorageService;
	@Autowired
	private ISnowflakeSequenceService snowflakeSequenceService;
	@Autowired
	private IBmsCalcuTaskService bmsCalcuTaskService;

	private static final String FEE_1 = "wh_product_storage";

	@Override
	public ReturnT<String> execute(String... params) throws Exception {
		XxlJobLogger.log("ProductStorageInitJob start.");
		XxlJobLogger.log("开始商品按件存储费用初始化任务");
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
				XxlJobLogger.log("【终止异常】,解析Job配置的参数出现错误,原因:" + e.getMessage()
						+ ",耗时：" + (System.currentTimeMillis() - startTime)
						+ "毫秒");
				return ReturnT.FAIL;
			}
		} else {
			// 未配置最多执行多少运单
			map.put("num", num);
		}

		// 查询所有状态为0的业务数据
		long currentTime = System.currentTimeMillis();
		map.put("isCalculated", "0");
		// 业务数据
		List<BizProductStorageEntity> bizList = null;
		// 费用数据
		List<FeesReceiveStorageEntity> feesList = new ArrayList<FeesReceiveStorageEntity>();
		try {
			XxlJobLogger.log("productStorageInitJob查询条件map:【{0}】  ", map);
			bizList = bizProductStorageService.query(map);
			// 只要有业务数据，就进行初始化和更新写入操作
			if (CollectionUtils.isNotEmpty(bizList)) {
				XxlJobLogger.log("【业务数据】查询行数【{0}】耗时【{1}】", bizList.size(),
						(System.currentTimeMillis() - currentTime));
				// 初始化费用
				initFees(bizList, feesList);
				// 批量更新业务数据&批量写入费用表
				updateAndInsertBatch(bizList, feesList);
			}
			// 只有业务数据查出来小于1000才发送mq，这时候才代表统计完成，才发送MQ
			if (CollectionUtils.isEmpty(bizList) || bizList.size() < num) {
				try {
					sendTask(feesList);
				} catch (Exception e) {
					XxlJobLogger.log("mq发送失败{0}", e);
				}
			}
		} catch (Exception e) {
			XxlJobLogger.log("【终止异常】,查询业务数据异常,原因: {0} ,耗时： {1}毫秒",
					e.getMessage(),
					((System.currentTimeMillis() - currentTime)));
			return ReturnT.FAIL;
		}

		XxlJobLogger.log("初始化费用总耗时：【{0}】毫秒", System.currentTimeMillis()
				- startTime);
		return ReturnT.SUCCESS;
	}

	private void initFees(List<BizProductStorageEntity> bizList,
			List<FeesReceiveStorageEntity> feesList) {
		for (BizProductStorageEntity entity : bizList) {
			feesList.add(initFeesEntity(FEE_1, entity));
		}
	}

	private FeesReceiveStorageEntity initFeesEntity(String code,
			BizProductStorageEntity entity) {
		entity.setRemark("");
		FeesReceiveStorageEntity storageFeeEntity = new FeesReceiveStorageEntity();
		storageFeeEntity.setIsCalculated("99");
		String feesNo = "STO" + snowflakeSequenceService.nextStringId();
		storageFeeEntity.setFeesNo(feesNo);
		storageFeeEntity.setCreator("system");
		storageFeeEntity.setCreateTime(entity.getCreateTime());
		storageFeeEntity.setOperateTime(entity.getCreateTime());
		storageFeeEntity.setCustomerId(entity.getCustomerid()); // 商家ID
		storageFeeEntity.setCustomerName(entity.getCustomerName()); // 商家名称
		storageFeeEntity.setWarehouseCode(entity.getWarehouseCode()); // 仓库ID
		storageFeeEntity.setWarehouseName(entity.getWarehouseName()); // 仓库名称
		storageFeeEntity.setCostType("FEE_TYPE_GENEARL"); // 费用类别
															// FEE_TYPE_GENEARL-通用
															// FEE_TYPE_MATERIAL-耗材
															// FEE_TYPE_ADD-增值
		storageFeeEntity.setSubjectCode(FEE_1); // 费用科目 todo
		storageFeeEntity.setOtherSubjectCode(FEE_1);
		storageFeeEntity.setProductType(""); // 商品类型
		if (entity.getAqty() != null) {
			storageFeeEntity.setQuantity(entity.getAqty()); // 商品数量
		}
		storageFeeEntity.setStatus("0"); // 状态
		storageFeeEntity.setWeight(entity.getWeight());
		storageFeeEntity.setTempretureType(entity.getTemperature()); // 温度类型
		storageFeeEntity.setProductNo(entity.getProductId()); // 商品编码
		storageFeeEntity.setProductName(entity.getProductName()); // 商品名称
		storageFeeEntity.setUnit("ITEMS");
		storageFeeEntity.setBizId(String.valueOf(entity.getId())); // 业务数据主键
		storageFeeEntity.setCost(new BigDecimal(0)); // 入仓金额
		storageFeeEntity.setUnitPrice(0d);
		storageFeeEntity.setFeesNo(entity.getFeesNo());
		storageFeeEntity.setParam1(TemplateTypeEnum.COMMON.getCode());
		storageFeeEntity.setDelFlag("0");
		return storageFeeEntity;
	}

	public void updateAndInsertBatch(List<BizProductStorageEntity> ts,
			List<FeesReceiveStorageEntity> fs) {
		long start = System.currentTimeMillis();// 系统开始时间
		long current = 0l;// 当前系统时间
		bizProductStorageService.updateBatch(ts);
		current = System.currentTimeMillis();
		XxlJobLogger.log("更新业务数据耗时：【{0}】毫秒", (current - start));
		start = System.currentTimeMillis();// 系统开始时间
		feesReceiveStorageService.InsertBatch(fs);
		current = System.currentTimeMillis();
		XxlJobLogger.log("新增费用数据耗时：【{0}】毫秒  ", (current - start));
	}

	private static final List<String> subjectList = Arrays.asList(FEE_1);
	private static final Map<String, Object> sendTaskMap = new HashMap<String, Object>();
	static {
		sendTaskMap.put("isCalculated", "99");
		sendTaskMap.put("subjectList", subjectList);
	}

	private void sendTask(List<FeesReceiveStorageEntity> feesList)
			throws Exception {
		// 对这些费用按照商家、科目、时间排序
		List<BmsCalcuTaskVo> list = bmsCalcuTaskService.queryByMap(sendTaskMap);
		for (BmsCalcuTaskVo vo : list) {
			String taskId = "STO" + snowflakeSequenceService.nextStringId();
			vo.setTaskId(taskId);
			vo.setCrePerson("system");
			vo.setCrePersonId("system");
			vo.setCreTime(JAppContext.currentTimestamp());
			// 为了区分商品按件存储费和商品按托存储费
			vo.setFeesType("item");
			bmsCalcuTaskService.sendTask(vo);
			XxlJobLogger.log("mq发送，taskId为----{0}", taskId);
		}
	}

}
