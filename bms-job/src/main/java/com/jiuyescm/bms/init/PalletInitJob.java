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
import com.jiuyescm.bms.base.dictionary.entity.SystemCodeEntity;
import com.jiuyescm.bms.base.group.service.IBmsGroupCustomerService;
import com.jiuyescm.bms.base.group.service.IBmsGroupService;
import com.jiuyescm.bms.base.group.vo.BmsGroupCustomerVo;
import com.jiuyescm.bms.base.group.vo.BmsGroupVo;
import com.jiuyescm.bms.common.JobParameterHandler;
import com.jiuyescm.bms.common.enumtype.TemplateTypeEnum;
import com.jiuyescm.bms.general.entity.BizPalletInfoEntity;
import com.jiuyescm.bms.general.entity.FeesReceiveStorageEntity;
import com.jiuyescm.bms.general.service.IBizPalletInfoRepository;
import com.jiuyescm.bms.general.service.IFeesReceiveStorageService;
import com.jiuyescm.bms.general.service.ISystemCodeService;
import com.jiuyescm.common.utils.DoubleUtil;
import com.jiuyescm.framework.sequence.api.ISnowflakeSequenceService;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHander;
import com.xxl.job.core.log.XxlJobLogger;

/**
 * 托数费用初始化
 * 
 * @author wangchen
 *
 */
@JobHander(value = "palletInitJob")
@Service
public class PalletInitJob extends IJobHandler {

	@Autowired
	private IFeesReceiveStorageService feesReceiveStorageService;
	@Autowired
	private IBmsGroupCustomerService bmsGroupCustomerService;
	@Autowired
	private IBizPalletInfoRepository bizPalletInfoService;
	@Autowired
	private IBmsGroupService bmsGroupService;
	@Autowired
	private ISnowflakeSequenceService snowflakeSequenceService;
	@Autowired
	private IBmsCalcuTaskService bmsCalcuTaskService;
	@Autowired
	private ISystemCodeService systemCodeService;

	List<String> cusNames = null;
	String[] subjects = null;
    List<String> noCalculateList=null;

	@Override
	public ReturnT<String> execute(String... params) throws Exception {
		XxlJobLogger.log("PalletInitJob start.");
		XxlJobLogger.log("开始托数费用初始化任务");
		return CalcJob(params);
	}

	private ReturnT<String> CalcJob(String[] params) {
		StopWatch sw = new StopWatch();
		sw.start();
		int num = 1000;

		Map<String, Object> map = new HashMap<String, Object>();
		if (params != null && params.length > 0) {
			try {
				map = JobParameterHandler.handler(params);// 处理定时任务参数
			} catch (Exception e) {
				sw.stop();
				XxlJobLogger.log("【终止异常】,解析Job配置的参数出现错误,原因:" + e.getMessage() + ",耗时："
						+ sw.getTotalTimeMillis() + "毫秒");
				return ReturnT.FAIL;
			}
		} else {
			// 未配置最多执行多少运单
			map.put("num", num);
		}

		// 使用导入商品托数的商家
		initConf();

		// 查询所有状态为0的业务数据
		map.put("isCalculated", "0");
		
		Map<String, Object> taskVoMap = new HashMap<>();
		
		saveFees(map,taskVoMap);
		sw.stop();
		
		sendTask(taskVoMap);

		XxlJobLogger.log("初始化费用总耗时：【{0}】毫秒", sw.getTotalTimeMillis());
		return ReturnT.SUCCESS;
	}

	private void saveFees(Map<String, Object> map, Map<String, Object> taskVoMap) {
		List<BizPalletInfoEntity> bizList = null;
		List<FeesReceiveStorageEntity> feesList = new ArrayList<FeesReceiveStorageEntity>();
		try {
			XxlJobLogger.log("palletInitJob查询条件map:【{0}】  ", map);
			bizList = bizPalletInfoService.querybizPallet(map);
			if (CollectionUtils.isNotEmpty(bizList)) {
                List<String> feesNos=new ArrayList<>();
			    for (BizPalletInfoEntity entity : bizList) {
			        //如果是不计费的商家，则直接更新业务计算状态为4
	                if(noCalculateList.size()>0 && noCalculateList.contains(entity.getCustomerId())){
	                    entity.setDelFlag("4");
	                    continue;
	                }
			        if(StringUtils.isNotBlank(entity.getFeesNo())){
                        feesNos.add(entity.getFeesNo());
                    }
			        FeesReceiveStorageEntity fee = initFees(entity);
					feesList.add(fee);
					
					if ("outstock".equals(entity.getBizType())) {
						continue;
					}
					String creMonth = new SimpleDateFormat("yyyyMM").format(entity.getCreateTime());
					StringBuilder sb = new StringBuilder(entity.getCustomerId()).append("-").append(fee.getSubjectCode()).append("-").append(creMonth);
					taskVoMap.put(sb.toString(), sb.toString());
				}
				XxlJobLogger.log("【托数】查询行数【{0}】", bizList.size());
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
			}
		} catch (Exception e) {
			XxlJobLogger.log("【终止异常】,查询业务数据异常,原因: {0}", e.getMessage());
			return;
		}	
		
		if(bizList== null || bizList.size() == 0){
			return;
		}else {
			saveFees(map, taskVoMap);
		}
	}

	private void initConf() {
		//subjects = initSubjects();

		Map<String, Object> cond = new HashMap<String, Object>();
		cond.put("groupCode", "Product_Pallet");
		cond.put("bizType", "group_customer");
		BmsGroupVo bmsGroup = bmsGroupService.queryOne(cond);
		if (bmsGroup != null) {
			cusNames = new ArrayList<String>();
			List<BmsGroupCustomerVo> cusList = null;
			try {
				cusList = bmsGroupCustomerService.queryAllByGroupId(bmsGroup.getId());
			} catch (Exception e) {
				XxlJobLogger.log("查询使用导入商品托数的商家异常:【{0}】", e);
			}
			for (BmsGroupCustomerVo vo : cusList) {
				cusNames.add(vo.getCustomerid());
			}
		}
		//不计费商家
        noCalculateList=bmsGroupCustomerService.queryCustomerByGroupCode("no_calculate_customer");

	}

	private FeesReceiveStorageEntity initFees(BizPalletInfoEntity entity) {
		entity.setRemark("");
		FeesReceiveStorageEntity feesEntity = new FeesReceiveStorageEntity();
		String subjectId = "";
		String feesNo = "STO" + snowflakeSequenceService.nextStringId();
		entity.setFeesNo(feesNo);

		// 更改业务数据状态
		entity.setIsCalculated("1");

		feesEntity.setCost(BigDecimal.ZERO);
		feesEntity.setCreator("system");
		feesEntity.setCreateTime(entity.getCreateTime());
		feesEntity.setOperateTime(entity.getCreateTime());
		feesEntity.setCustomerId(entity.getCustomerId()); // 商家ID
		feesEntity.setCustomerName(entity.getCustomerName()); // 商家名称
		feesEntity.setWarehouseCode(entity.getWarehouseCode()); // 仓库ID
		feesEntity.setWarehouseName(entity.getWarehouseName()); // 仓库名称
		feesEntity.setProductType(""); // 商品类型
		// 如果商家不在《使用导入商品托数的商家》, 更新计费来源是系统, 同时使用系统托数计费
		// 如果商家在《使用导入商品托数的商家》,更新计费来源是导入,同时使用导入托数计费
		double num = 0d;
		if (cusNames.contains(entity.getCustomerId())) {
			entity.setChargeSource("import");
		} else {
			entity.setChargeSource("system");
		}
		// 调整托数优先级最高
		if (DoubleUtil.isBlank(entity.getAdjustPalletNum())) {
			if (cusNames.contains(entity.getCustomerId())) {
				num = entity.getPalletNum();
			} else {
				num = entity.getSysPalletNum();
			}
		} else {
			num = entity.getAdjustPalletNum();
		}

		feesEntity.setQuantity(num); // 数量
		feesEntity.setUnit("pallet"); // 单位
		feesEntity.setTempretureType(entity.getTemperatureTypeCode()); // 设置温度类型
		feesEntity.setUnitPrice(0d); // 单价
		feesEntity.setBizType(entity.getBizType()); // 托数类型
		feesEntity.setFeesNo(feesNo);
		feesEntity.setParam1(TemplateTypeEnum.COMMON.getCode());
		feesEntity.setIsCalculated("99");
		feesEntity.setDelFlag("0");
		// 费用科目
		if ("product".equals(entity.getBizType())) {
			subjectId = "wh_product_storage";
		} else if ("material".equals(entity.getBizType())) {
			subjectId = "wh_material_storage";
		} else if ("instock".equals(entity.getBizType())) {
			subjectId = "wh_disposal";
		} else if ("outstock".equals(entity.getBizType())) {
			//如果是出库托数,生成费用为0,不发MQ
			subjectId = "outstock_pallet_vm";
			entity.setIsCalculated("5");
			feesEntity.setIsCalculated("5");
		}
		feesEntity.setSubjectCode(subjectId);
		feesEntity.setOtherSubjectCode(subjectId);
		return feesEntity;
	}

	private void updateAndInsertBatch(List<BizPalletInfoEntity> bizList, List<FeesReceiveStorageEntity> feesList) {
		StopWatch watch = new StopWatch();
		if (feesList.size() > 0) {
			watch.start("biz");
			bizPalletInfoService.updatebizPallet(bizList);
			watch.stop();
			XxlJobLogger.log("更新业务数据耗时：【{0}】毫秒 更新行数【{1}】", watch.getLastTaskTimeMillis(), bizList.size());
			watch.start("fee");
			feesReceiveStorageService.InsertBatch(feesList);
			watch.stop();
			XxlJobLogger.log("新增费用数据耗时：【{0}】毫秒  更新行数【{1}】", watch.getLastTaskTimeMillis(), feesList.size());
		}
	}

//	private String[] initSubjects() {
//		// 这里的科目应该在科目组中配置,动态查询
//		// wh_disposal(处置费)
//		Map<String, String> map = bmsGroupSubjectService.getSubject("job_subject_pallet");
//		if (map.size() == 0) {
//			String[] strs = { "wh_disposal", "wh_product_storage", "wh_material_storage", "outstock_pallet_vm" };
//			return strs;
//		} else {
//			List<String> strs = new ArrayList<String>();
//			for (String value : map.keySet()) {
//				strs.add(value);
//			}
//			strs.add("outstock_pallet_vm");
//			String[] strArray = strs.toArray(new String[strs.size()]);
//			return strArray;
//		}
//	}

	private void sendTask(Map<String, Object> taskVos) {
		for (String key : taskVos.keySet()) { 
			String[] param = key.split("-");
			BmsCalcuTaskVo vo = new BmsCalcuTaskVo();
			Map<String, SystemCodeEntity> sysMap = getEnumList("PALLET_CAL_FEE");
			try{
				vo.setCrePerson("系统");
				vo.setCrePersonId("system");
				vo.setCustomerId(param[0]);
				vo.setSubjectCode(param[1]);
				vo.setSubjectName(sysMap.get(param[1]).getCodeName());
				vo.setCreMonth(Integer.valueOf(param[2]));
				bmsCalcuTaskService.sendTask(vo);
				XxlJobLogger.log("mq发送成功,商家id:{0},年月:{1},科目id:{2}", vo.getCustomerId(),vo.getCreMonth(),vo.getSubjectCode());
			}
			catch(Exception ex){
				XxlJobLogger.log("mq发送失败{0}", ex);
			}
		} 
	}
	
	public Map<String, SystemCodeEntity> getEnumList(String typeCode) {	
		Map<String, SystemCodeEntity> tmscodels = systemCodeService.querySysCodesMap(typeCode);
		return tmscodels;
	}

}
