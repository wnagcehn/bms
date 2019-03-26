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
import com.jiuyescm.cfm.common.JAppContext;
import com.jiuyescm.common.utils.DoubleUtil;
import com.jiuyescm.framework.sequence.api.ISnowflakeSequenceService;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHander;
import com.xxl.job.core.log.XxlJobLogger;

/**
 * 托数费用初始化
 * @author wangchen
 *
 */
@JobHander(value="palletInitJob")
@Service
public class PalletInitJob extends IJobHandler{
	
	@Autowired private IFeesReceiveStorageService feesReceiveStorageService;
	@Autowired private IBmsGroupCustomerService bmsGroupCustomerService;
	@Autowired private IBizPalletInfoRepository bizPalletInfoService;
	@Autowired private IBmsGroupService bmsGroupService;
	@Autowired private ISnowflakeSequenceService snowflakeSequenceService;
	@Autowired private IBmsCalcuTaskService bmsCalcuTaskService;
	@Autowired private ISystemCodeService systemCodeService;
	
	List<String> cusNames = null;
	Map<String, SystemCodeEntity> sysMap = null;

	@Override
	public ReturnT<String> execute(String... params) throws Exception {
		XxlJobLogger.log("PalletInitJob start.");
		XxlJobLogger.log("开始托数费用初始化任务");
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
		
		//使用导入商品托数的商家
		initConf();
		
		//查询所有状态为0的业务数据
		long currentTime = System.currentTimeMillis();
		map.put("isCalculated", "0");
		List<BizPalletInfoEntity> bizList = null;
		List<FeesReceiveStorageEntity> feesList = new ArrayList<FeesReceiveStorageEntity>();
		try {
			XxlJobLogger.log("palletInitJob查询条件map:【{0}】  ",map);
			bizList = bizPalletInfoService.querybizPallet(map);
			if(CollectionUtils.isNotEmpty(bizList)){
				XxlJobLogger.log("【托数】查询行数【{0}】耗时【{1}】", bizList.size(), (System.currentTimeMillis()-currentTime));			
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

	private void initConf() {
		Map<String, Object> cond= new HashMap<String, Object>();
		cond.put("groupCode", "Product_Pallet");
		cond.put("bizType", "group_customer");
		BmsGroupVo bmsGroup=bmsGroupService.queryOne(cond);
		if(bmsGroup!=null){
			cusNames = new ArrayList<String>();
			List<BmsGroupCustomerVo> cusList = null;
			try {
				cusList = bmsGroupCustomerService.queryAllByGroupId(bmsGroup.getId());
			} catch (Exception e) {
				XxlJobLogger.log("查询使用导入商品托数的商家异常:【{0}】", e);
			}
			for(BmsGroupCustomerVo vo:cusList){
				cusNames.add(vo.getCustomerid());
			}
		}
		
		//费用科目code=>entity
		sysMap = systemCodeService.querySysCodesMap("PALLET_CAL_FEE");
	}
	
	private void initFees(List<BizPalletInfoEntity> bizList, List<FeesReceiveStorageEntity> feesList){
		for (BizPalletInfoEntity entity : bizList) {
			entity.setRemark("");
			FeesReceiveStorageEntity feesEntity = new FeesReceiveStorageEntity();
			String subjectId = "";
			String feesNo = "STO"+snowflakeSequenceService.nextStringId();
			
			//更改业务数据状态
			entity.setIsCalculated("99");
			
			feesEntity.setCreator("system");
			feesEntity.setCreateTime(entity.getCreateTime());
			feesEntity.setOperateTime(entity.getCreateTime());
			feesEntity.setCustomerId(entity.getCustomerId());		//商家ID
			feesEntity.setCustomerName(entity.getCustomerName());	//商家名称
			feesEntity.setWarehouseCode(entity.getWarehouseCode());	//仓库ID
			feesEntity.setWarehouseName(entity.getWarehouseName());	//仓库名称
			feesEntity.setCostType("FEE_TYPE_GENEARL");				//费用类别 FEE_TYPE_GENEARL-通用 FEE_TYPE_MATERIAL-耗材 FEE_TYPE_ADD-增值
			feesEntity.setProductType("");							//商品类型
			//费用科目
			if ("product".equals(entity.getBizType())) {
				subjectId = "wh_product_storage";
			}else if ("material".equals(entity.getBizType())) {
				subjectId = "wh_material_storage";
			}else if ("instock".equals(entity.getBizType())) {
				subjectId = "wh_disposal";
			}else if ("outstock".equals(entity.getBizType())) {
				subjectId = "outstock_pallet_vm";
			}
			feesEntity.setSubjectCode(subjectId);			
			feesEntity.setOtherSubjectCode(subjectId);
			//如果商家不在《使用导入商品托数的商家》, 更新计费来源是系统, 同时使用系统托数计费
			//如果商家在《使用导入商品托数的商家》,更新计费来源是导入,同时使用导入托数计费
			double num = 0d;
			if (cusNames.contains(entity.getCustomerId())) {
				entity.setChargeSource("import");
			}else {
				entity.setChargeSource("system");
			}
			//调整托数优先级最高
			if (DoubleUtil.isBlank(entity.getAdjustPalletNum())) {
				if (cusNames.contains(entity.getCustomerId())) {
					num = entity.getPalletNum();
				}else {
					num = entity.getSysPalletNum();
				}
			}else {
				num = entity.getAdjustPalletNum();
			}
			
			feesEntity.setQuantity(num);									//数量
			feesEntity.setUnit("PALLETS");									//单位
			feesEntity.setTempretureType(entity.getTemperatureTypeCode());	//设置温度类型 
			feesEntity.setCost(new BigDecimal(0));							//入仓金额
			feesEntity.setUnitPrice(0d);									//单价
			feesEntity.setBizType(entity.getBizType());						//托数类型
			feesEntity.setFeesNo(feesNo);
			feesEntity.setParam1(TemplateTypeEnum.COMMON.getCode());
			feesEntity.setParam2(new SimpleDateFormat("yyyyMM").format(entity.getCreateTime()));
			feesEntity.setDelFlag("0");
			feesEntity.setIsCalculated("99");
			feesEntity.setCalcuMsg("");
			feesList.add(feesEntity);
		}
	}

	private void updateAndInsertBatch(List<BizPalletInfoEntity> bizList, List<FeesReceiveStorageEntity> feesList) {
		long start = System.currentTimeMillis();// 系统开始时间
		long current = 0l;// 当前系统时间
		if(feesList.size()>0){
			bizPalletInfoService.updatebizPallet(bizList);
			current = System.currentTimeMillis();
		    XxlJobLogger.log("更新业务数据耗时：【{0}】毫秒 更新行数【{1}】",(current - start),bizList.size());
		    start = System.currentTimeMillis();// 系统开始时间
		    feesReceiveStorageService.InsertBatch(feesList);
		    current = System.currentTimeMillis();
			XxlJobLogger.log("新增费用数据耗时：【{0}】毫秒  更新行数【{1}】",(current - start),feesList.size());
		}	
	}
	
	private static final List<String> subjectList= Arrays.asList("wh_disposal","wh_product_storage","wh_material_storage","outstock_pallet_vm");
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
