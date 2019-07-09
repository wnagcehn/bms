package com.jiuyescm.bms.init;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.dubbo.common.utils.CollectionUtils;
import com.jiuyescm.bms.asyn.service.IBmsCalcuTaskService;
import com.jiuyescm.bms.asyn.vo.BmsCalcuTaskVo;
import com.jiuyescm.bms.base.group.service.IBmsGroupCustomerService;
import com.jiuyescm.bms.common.JobParameterHandler;
import com.jiuyescm.bms.general.entity.BmsBizInstockInfoEntity;
import com.jiuyescm.bms.general.entity.FeesReceiveStorageEntity;
import com.jiuyescm.bms.general.service.IBmsBizInstockInfoRepository;
import com.jiuyescm.bms.general.service.IFeesReceiveStorageService;
import com.jiuyescm.cfm.common.JAppContext;
import com.jiuyescm.common.utils.DoubleUtil;
import com.jiuyescm.framework.sequence.api.ISnowflakeSequenceService;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHander;
import com.xxl.job.core.log.XxlJobLogger;

/**
 * 入库单费用初始化
 */
@JobHander(value = "instockFeeInitJob")
@Service
public class InstockFeeInitJob extends IJobHandler {

	@Autowired
	private IFeesReceiveStorageService feesReceiveStorageService;
	@Autowired
	private IBmsBizInstockInfoRepository bmsBizInstockInfoRepository;
	@Autowired
	private ISnowflakeSequenceService snowflakeSequenceService;
	@Autowired
	private IBmsCalcuTaskService bmsCalcuTaskService;
    @Autowired 
    private IBmsGroupCustomerService bmsGroupCustomerService;
    
	private static final String FEE_1 = "wh_instock_work";
	private static final String FEE_2 = "wh_b2c_handwork";
    List<String> noCalculateList=null;

	@Override
	public ReturnT<String> execute(String... params) throws Exception {
		XxlJobLogger.log("InstockFeeInitJob start.");
		XxlJobLogger.log("开始入库单费用初始化任务");
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
				XxlJobLogger.log("【终止异常】,解析Job配置的参数出现错误,原因:{0},耗时{1}" , e, (System.currentTimeMillis() - startTime));
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
		
		Set<String> taskSet = new HashSet<>();
		try {
			saveFees(map,taskSet);
		} catch (Exception e) {
			XxlJobLogger.log("【终止异常】,查询业务数据异常,原因: {0} ,耗时： {1}毫秒",e,((System.currentTimeMillis() - currentTime)));
			return ReturnT.FAIL;
		}
		sendTask(taskSet);
		XxlJobLogger.log("初始化费用总耗时：【{0}】毫秒", System.currentTimeMillis()
				- startTime);
		return ReturnT.SUCCESS;
	}

	private void saveFees(Map<String, Object> map,Set<String> taskSet){
		// 业务数据
		List<BmsBizInstockInfoEntity> bizList = null;
		// 费用数据
		List<FeesReceiveStorageEntity> feesList = new ArrayList<FeesReceiveStorageEntity>();
		XxlJobLogger.log("instockFeeInitJob查询条件map:【{0}】  ", map);
		bizList = bmsBizInstockInfoRepository.getInStockInfoList(map);
		// 只要有业务数据，就进行初始化和更新写入操作
		if (CollectionUtils.isNotEmpty(bizList)) {
			XxlJobLogger.log("【业务数据】查询行数【{0}】", bizList.size());
			// 初始化费用
			initFees(bizList, feesList);
            List<String> feesNos=new ArrayList<>();
			for (BmsBizInstockInfoEntity entity : bizList) {
			    if(StringUtils.isNotBlank(entity.getFeesNo())){
                    feesNos.add(entity.getFeesNo());
                }
			  //如果是不计费的商家，则直接更新业务计算状态为4
                if(noCalculateList.size()>0 && noCalculateList.contains(entity.getCustomerId())){
                    entity.setDelFlag("4");
                    continue;
                }
			 
				//封装key
				String customerId = entity.getCustomerId();
				String creMonth = new SimpleDateFormat("yyyyMM").format(entity.getCreateTime());
				StringBuilder sb1 = new StringBuilder();
				sb1.append(customerId).append("-").append(creMonth);
				taskSet.add(sb1.toString());
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
			// 批量更新业务数据&批量写入费用表
			updateAndInsertBatch(bizList, feesList);
			//继续执行
			saveFees(map,taskSet);
		}else {
			// 没有业务数据被查出来才发送mq，这时候才代表统计完成
			return;
		}
	}

	private void initFees(List<BmsBizInstockInfoEntity> bizList,
			List<FeesReceiveStorageEntity> feesList) {
		for (BmsBizInstockInfoEntity entity : bizList) {
			//一条业务数据对应的两个科目的费用编号是一样的
			String feesNo = "STO" + snowflakeSequenceService.nextStringId();
			feesList.add(initFeesEntity(FEE_1, entity,feesNo));
			feesList.add(initFeesEntity(FEE_2, entity,feesNo));
		}
	}

	private FeesReceiveStorageEntity initFeesEntity(String code,
			BmsBizInstockInfoEntity instock,String feesNo) {
		instock.setRemark("");
		FeesReceiveStorageEntity storageFeeEntity = new FeesReceiveStorageEntity();

		double num = DoubleUtil.isBlank(instock.getAdjustQty()) ? instock
				.getTotalQty() : instock.getAdjustQty();
		if (!DoubleUtil.isBlank(num)) {
			storageFeeEntity.setQuantity(num);// 商品数量
		}
		// 重量
		Double weight = DoubleUtil.isBlank(instock.getAdjustWeight()) ? instock
				.getTotalWeight() : instock.getAdjustWeight();
		storageFeeEntity.setWeight(weight);

		// 塞箱数
		Double box = DoubleUtil.isBlank(instock.getAdjustBox()) ? instock
				.getTotalBox() : instock.getTotalBox();
		if (!DoubleUtil.isBlank(box)) {
			storageFeeEntity.setBox(box.intValue());
		}
		storageFeeEntity.setIsCalculated("99");
		storageFeeEntity.setFeesNo(feesNo);
		storageFeeEntity.setCreator("system");
		storageFeeEntity.setCreateTime(instock.getCreateTime());
		storageFeeEntity.setCustomerId(instock.getCustomerId()); // 商家ID
		storageFeeEntity.setCustomerName(instock.getCustomerName()); // 商家名称
		storageFeeEntity.setWarehouseCode(instock.getWarehouseCode()); // 仓库ID
		storageFeeEntity.setWarehouseName(instock.getWarehouseName()); // 仓库名称
		storageFeeEntity.setProductType(""); // 商品类型
		storageFeeEntity.setStatus("0"); // 状态
		storageFeeEntity.setOperateTime(instock.getCreateTime()); // 操作时间
		storageFeeEntity.setCostType("FEE_TYPE_GENEARL");
		storageFeeEntity.setSubjectCode(code); // 费用科目
		storageFeeEntity.setOtherSubjectCode(code); // 费用科目明细
		storageFeeEntity.setBizId(String.valueOf(instock.getId()));// 业务数据主键
		storageFeeEntity.setUnitPrice(0d);
		storageFeeEntity.setCost(new BigDecimal(0)); // 入仓金额
		storageFeeEntity.setDelFlag("0");
		storageFeeEntity.setbId(instock.getId());
		storageFeeEntity.setBizCalculateTime(JAppContext.currentTimestamp());
		return storageFeeEntity;
	}

	public void updateAndInsertBatch(List<BmsBizInstockInfoEntity> ts,
			List<FeesReceiveStorageEntity> fs) {
		long start = System.currentTimeMillis();// 系统开始时间
		long current = 0L;// 当前系统时间
		if (fs.size() > 0) {
			bmsBizInstockInfoRepository.updatebizInstockInfoById(fs);
			current = System.currentTimeMillis();
			XxlJobLogger.log("更新业务数据耗时：【{0}】毫秒  ", (current - start));
			start = System.currentTimeMillis();// 系统开始时间
			feesReceiveStorageService.InsertBatch(fs);
			current = System.currentTimeMillis();
			XxlJobLogger.log("新增费用数据耗时：【{0}】毫秒 ", (current - start));
		}
	}

	private void sendTask(Set<String> taskSet){
		// 对这些费用按照商家、科目、时间排序
		for (String key : taskSet) {
			String[] param = key.split("-");
			//两种费用
			for (int i = 0; i < 2; i++) {
				BmsCalcuTaskVo vo = new BmsCalcuTaskVo();
				vo.setCrePerson("system");
				vo.setCrePersonId("system");
				vo.setCreTime(JAppContext.currentTimestamp());
				vo.setCustomerId(param[0]);
				vo.setCreMonth(Integer.valueOf(param[1]));
				if(i==0){
					vo.setSubjectCode(FEE_1);
				}else {
					vo.setSubjectCode(FEE_2);
				}
				try {
					bmsCalcuTaskService.sendTask(vo);
					XxlJobLogger.log("mq发送，商家id为----{0}，业务年月为----{1}，科目id为---{2}", vo.getCustomerId(),vo.getCreMonth(),vo.getSubjectCode());
				} catch (Exception e) {
					XxlJobLogger.log("mq任务失败：商家id为----{0}，业务年月为----{1}，科目id为---{2}，错误信息：{3}", vo.getCustomerId(),vo.getCreMonth(),vo.getSubjectCode(),e);
				}
			}
		}
	}

	
    protected void initConf(){
        noCalculateList=bmsGroupCustomerService.queryCustomerByGroupCode("no_calculate_customer");
    }
}
