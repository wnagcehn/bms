package com.jiuyescm.bms.init;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;

import com.alibaba.dubbo.common.utils.CollectionUtils;
import com.jiuyescm.bms.asyn.service.IBmsCalcuTaskService;
import com.jiuyescm.bms.asyn.vo.BmsCalcuTaskVo;
import com.jiuyescm.bms.base.dictionary.entity.SystemCodeEntity;
import com.jiuyescm.bms.base.group.service.IBmsGroupCustomerService;
import com.jiuyescm.bms.base.group.service.IBmsGroupSubjectService;
import com.jiuyescm.bms.biz.storage.entity.BizOutstockMasterEntity;
import com.jiuyescm.bms.common.JobParameterHandler;
import com.jiuyescm.bms.common.enumtype.TemplateTypeEnum;
import com.jiuyescm.bms.general.entity.FeesReceiveStorageEntity;
import com.jiuyescm.bms.general.service.IFeesReceiveStorageService;
import com.jiuyescm.bms.general.service.ISystemCodeService;
import com.jiuyescm.bms.receivable.storage.service.IBizOutstockMasterService;
import com.jiuyescm.cfm.common.JAppContext;
import com.jiuyescm.framework.sequence.api.ISnowflakeSequenceService;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHander;
import com.xxl.job.core.log.XxlJobLogger;

/**
 * 出库单费用初始化
 */
@JobHander(value="outstockFeeInitJob")
@Service
public class OutstockFeeInitJob extends IJobHandler{

	@Autowired private IFeesReceiveStorageService feesReceiveStorageService;
	@Autowired private IBizOutstockMasterService bizOutstockMasterService;
	@Autowired private ISnowflakeSequenceService snowflakeSequenceService;
	@Autowired private IBmsCalcuTaskService bmsCalcuTaskService;
	@Autowired private IBmsGroupSubjectService bmsGroupSubjectService;
	@Autowired private ISystemCodeService systemCodeService;
    @Autowired private IBmsGroupCustomerService bmsGroupCustomerService;

	
	/**
	 * 计费科目列表
	 */
	public String[] subjects = null;
	Map<String, String> temMap = null;
    List<String> noCalculateList=null;

	
	@Override
	public ReturnT<String> execute(String... params) throws Exception {
		XxlJobLogger.log("OutstockFeeInitJob start.");
		XxlJobLogger.log("开始出库单费用初始化任务");
		return CalcJob(params);
	}
	
	private ReturnT<String> CalcJob(String[] params) {
		StopWatch sw = new StopWatch();
		sw.start();
		int num = 1000;
		
		Map<String, Object> map = new HashMap<String, Object>();
		if(params != null && params.length > 0) {
			try {
				map = JobParameterHandler.handler(params);//处理定时任务参数
			} catch (Exception e) {
				sw.stop();
				XxlJobLogger.log("【终止异常】,解析Job配置的参数出现错误,原因:{0},耗时{1}", e,sw.getTotalTimeMillis());
	            return ReturnT.FAIL;
			}
		}else {
			//未配置最多执行多少运单
			map.put("num", num);
		}
		
		//初始化配置
		initConf();
		
		//查询所有状态为0的业务数据
		map.put("isCalculated", "0");
		
		Map<String, Object> taskVoMap = new HashMap<>();
		saveFees(map,taskVoMap);
		sw.stop();
		
		sendTask(taskVoMap);
		
		XxlJobLogger.log("初始化费用总耗时：【{0}】毫秒", sw.getTotalTimeMillis());
        return ReturnT.SUCCESS;
	}
	
	private void saveFees(Map<String, Object> map, Map<String, Object> taskVoMap) {
		//业务数据
		List<BizOutstockMasterEntity> bizList = null;
		//费用数据
		List<FeesReceiveStorageEntity> feesList = new ArrayList<FeesReceiveStorageEntity>();
		try {
			XxlJobLogger.log("outstockFeeInitJob查询条件map:【{0}】  ",map);
			bizList = bizOutstockMasterService.query(map);
			//只要有业务数据，就进行初始化和更新写入操作
			if (CollectionUtils.isNotEmpty(bizList)) {
			    List<String> feesNos=new ArrayList<>();
				for (BizOutstockMasterEntity entity : bizList) {
				    if(StringUtils.isNotBlank(entity.getFeesNo())){
                        feesNos.add(entity.getFeesNo());
                    }
				    //如果是不计费的商家，则直接更新业务计算状态为4
                    if(noCalculateList.size()>0 && noCalculateList.contains(entity.getCustomerid())){
                        entity.setDelFlag("4");
                        continue;
                    }
				  
					List<FeesReceiveStorageEntity> fees = initFees(entity);
					feesList.addAll(fees);
					for (FeesReceiveStorageEntity fee : fees) {
						//feesList.add(fee);
						String creMonth = new SimpleDateFormat("yyyyMM").format(entity.getCreateTime());
						StringBuilder sb = new StringBuilder();
						sb.append(entity.getCustomerid()).append("-").append(fee.getSubjectCode()).append("-").append(creMonth);
						taskVoMap.put(sb.toString(), sb.toString());
					}
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
			XxlJobLogger.log("【终止异常】,查询业务数据异常,原因: {0}", e);
			return;
		}
		
		if(bizList== null || bizList.size() == 0){
			return;
		}else {
			saveFees(map, taskVoMap);
		}
	}

	private void initConf(){
		//初始化科目
		subjects=initSubjects();		
		//初始化温度类型
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("typeCode", "TEMPERATURE_TYPE");
		List<SystemCodeEntity> systemCodeList = systemCodeService.querySysCodes(map);
		temMap =new ConcurrentHashMap<String,String>();
		if(systemCodeList!=null && systemCodeList.size()>0){
			for(int i=0;i<systemCodeList.size();i++){
				temMap.put(systemCodeList.get(i).getCode(), systemCodeList.get(i).getCodeName());
			}
		}
		//不计算商家集合
        noCalculateList=bmsGroupCustomerService.queryCustomerByGroupCode("no_calculate_customer");

		
	}
	
	private List<FeesReceiveStorageEntity> initFees(BizOutstockMasterEntity entity) {
		List<FeesReceiveStorageEntity> feesList = new ArrayList<FeesReceiveStorageEntity>();
		String feesNo = "STO" + snowflakeSequenceService.nextStringId();
		entity.setFeesNo(feesNo);
		for (String SubjectId : subjects) {
			FeesReceiveStorageEntity storageFeeEntity = new FeesReceiveStorageEntity();
			storageFeeEntity.setFeesNo(feesNo);
			//如果是【B2B订单操作费】 或【出库装车费】,并且是B2B出库单   ( B2bFlag 0-B2C  1-B2B)
			if(("wh_b2b_work".equals(SubjectId) ||"wh_b2b_handwork".equals(SubjectId)) 
					&& "1".equals(entity.getB2bFlag())){
				initFeesEntity(SubjectId, entity,storageFeeEntity);
				feesList.add(storageFeeEntity);
			}
			else if("wh_b2c_work".equals(SubjectId) && "0".equals(entity.getB2bFlag())){
				initFeesEntity(SubjectId, entity,storageFeeEntity);
				feesList.add(storageFeeEntity);
			}
		}
		return feesList;
	}
	
	private FeesReceiveStorageEntity initFeesEntity(String subjectId,BizOutstockMasterEntity outstock,FeesReceiveStorageEntity storageFeeEntity) {
		outstock.setRemark("");
		outstock.setIsCalculated("1");
		outstock.setCalculateTime(JAppContext.currentTimestamp());
		storageFeeEntity.setCreator("system");
		storageFeeEntity.setCreateTime(outstock.getCreateTime());
		storageFeeEntity.setCustomerId(outstock.getCustomerid());		//商家ID
		storageFeeEntity.setCustomerName(outstock.getCustomerName());	//商家名称
		storageFeeEntity.setWarehouseCode(outstock.getWarehouseCode());	//仓库ID
		storageFeeEntity.setWarehouseName(outstock.getWarehouseName());	//仓库名称
		storageFeeEntity.setOrderType(outstock.getBillTypeName());		//订单类型
		//塞品种数
		storageFeeEntity.setVarieties(0);	
		//塞件数
		storageFeeEntity.setQuantity(0d);
		//塞重量
		storageFeeEntity.setWeight(0d);
		//塞箱数
		storageFeeEntity.setBox(0);
		
		storageFeeEntity.setOrderNo(outstock.getOutstockNo());			//oms订单号
		storageFeeEntity.setProductType("");							//商品类型		
		storageFeeEntity.setStatus("0");								//状态
		storageFeeEntity.setOperateTime(outstock.getCreateTime());
		storageFeeEntity.setCostType("FEE_TYPE_GENEARL");
		storageFeeEntity.setUnitPrice(0d);
		storageFeeEntity.setCost(new BigDecimal(0));	//入仓金额
		storageFeeEntity.setSubjectCode(subjectId);
		storageFeeEntity.setOtherSubjectCode(subjectId);
		storageFeeEntity.setBizId(String.valueOf(outstock.getId()));//业务数据主键
		storageFeeEntity.setFeesNo(outstock.getFeesNo());
		
		//XxlJobLogger.log("-->"+outstock.getId()+"温度类型的map【{0}】",temMap);
		
		if(StringUtils.isEmpty(outstock.getTemperatureTypeCode())){
			outstock.setTemperatureTypeCode("LD");
			storageFeeEntity.setTempretureType("LD");
		}
		else{
			storageFeeEntity.setTempretureType(outstock.getTemperatureTypeCode());
			
			//XxlJobLogger.log("-->"+outstock.getId()+"业务数据中温度类型code"+outstock.getTemperatureTypeCode());

			outstock.setTemperatureTypeName(temMap.get(outstock.getTemperatureTypeCode()));
			
			//XxlJobLogger.log("-->"+outstock.getId()+"业务数据中温度类型"+outstock.getTemperatureTypeName());
		}
		if(StringUtils.isEmpty(outstock.getTemperatureTypeName())){
			outstock.setTemperatureTypeName("冷冻");
		}
		storageFeeEntity.setDelFlag("0");
		storageFeeEntity.setParam1(TemplateTypeEnum.COMMON.getCode());
		storageFeeEntity.setLastModifyTime(JAppContext.currentTimestamp());
		storageFeeEntity.setIsCalculated("99");
		
		return storageFeeEntity;
	}



	private void updateAndInsertBatch(List<BizOutstockMasterEntity> ts,List<FeesReceiveStorageEntity> fs) {

		long start = System.currentTimeMillis();// 系统开始时间
		long current = 0L;// 当前系统时间
		if (fs.size() > 0) {
			bizOutstockMasterService.updateOutstockBatch(ts);
			current = System.currentTimeMillis();
			XxlJobLogger.log("更新业务数据耗时：【{0}】毫秒  ",(current - start));
			start = System.currentTimeMillis();// 系统开始时间
			feesReceiveStorageService.InsertBatch(fs);
			current = System.currentTimeMillis();
			XxlJobLogger.log("新增费用数据耗时：【{0}】毫秒 ",(current - start));
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

	
	
	public String[] initSubjects() {
		//这里的科目应该在科目组中配置,动态查询
		//wh_b2c_work(B2C订单操作费 )    wh_b2b_work(B2B订单操作费)     wh_b2b_handwork(出库装车费)
		Map<String,String> map=bmsGroupSubjectService.getSubject("job_subject_outstock");
		if(map.size() == 0){
			String[] strs = {"wh_b2c_work","wh_b2b_work","wh_b2b_handwork"};
			return strs;
		}else{
			int i=0;
			String[] strs=new String[map.size()];
			for(String value:map.keySet()){
				strs[i]=value;	
				i++;
			}
			return strs;
		}
		
	}
	
	/**
	 * 判断Integer类型数据是否为空值
	 * @param d
	 * @return
	 * null -> true
	 * 0 -> true   0.0 -> true  -0.0 -> true
	 * 其他返回 false
	 */
	public boolean isBlank(Integer d){
		
		if(d == null){
			return true;
		}
		if(d == 0){
			return true;
		}
		return false;
	}
}
