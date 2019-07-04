package com.jiuyescm.bms.jobhandler;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;

import com.google.common.collect.Maps;
import com.jiuyescm.bms.appconfig.TenantConfig;
import com.jiuyescm.bms.billcheck.BillCheckInfoEntity;
import com.jiuyescm.bms.billcheck.repository.IBillCheckInfoRepository;
import com.jiuyescm.bms.billcheck.service.IBillCheckInfoService;
import com.jiuyescm.bms.correct.ElConditionEntity;
import com.jiuyescm.bms.correct.repository.ElConditionRepository;
import com.jiuyescm.crm.module.api.IModuleDataOpenService;
//import com.jiuyescm.framework.sequence.api.ISnowflakeSequenceService;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHander;
import com.xxl.job.core.log.XxlJobLogger;

/**
 * 账单跟踪推送账单跟踪状态到CRM
 * @author zhaofeng
 */
@JobHander(value="crmBillCheckStatusJob")
@Service
public class CrmBillCheckStatusJob  extends IJobHandler{

    @Autowired
    private IBillCheckInfoService billCheckInfoService;
    @Autowired
    private IBillCheckInfoRepository billCheckInfoRepository;
    @Autowired
    private ElConditionRepository elConditionRepository;
    private static final String SUCCESS = "SUCCESS";
    private static final String FAIL = "FAIL";
    
	@Override
	public ReturnT<String> execute(String... params) throws Exception {
		XxlJobLogger.log("CorrectJob start.");
		XxlJobLogger.log("开始账单跟踪推送账单跟踪状态到CRM");
        return CalcJob(params);
	}
		
	private ReturnT<String> CalcJob(String[] params) {
        StopWatch sw = new StopWatch();
        Map<String, Object> map = new HashMap<String, Object>();
        Map<String, Object> elCondition = Maps.newLinkedHashMap();     
        elCondition.put("pullType", "bill_check_crm");
        List<ElConditionEntity> elList =  elConditionRepository.query(elCondition);
        if(CollectionUtils.isEmpty(elList)) return printLog("etl_condition表未配置，请去配置", sw, FAIL);
        ElConditionEntity elEntity = elList.get(0);

	    try {
	        //获取定时任务参数
	        sw.start("获取定时任务参数");
	        //开始时间
	        if (params != null && params.length > 0 && params[0]!=null) {
	            map.put("beginTime", params[0]);
	        } else {
	            // 未配置最多执行多少运单
	            // 从etl_condition表获取时间
	            XxlJobLogger.log("从etl_condition表获取时间");	           
	            Timestamp startTime = elEntity.getLastTime();
	            if(null == startTime){
	                return printLog("etl_condition表未配置时间，请去配置", sw, FAIL);
	            }
	            map.put("beginTime", startTime);
	        }
	        
	        //结束时间
	        if (params != null && params.length > 0 && params[1]!=null) {
	            map.put("endTime", params[1]);
	        } else {
	            // 未配置则取当前时间
	            map.put("endTime", new Timestamp(System.currentTimeMillis()));
	        }
        } catch (Exception e) {
            // TODO: handle exception
            XxlJobLogger.log("获取时间异常：{0}", e); 
            return printLog("获取时间异常", sw, FAIL);
        }
        elCondition.put("lastTime", map.get("endTime"));
	    try {
	        XxlJobLogger.log("查询条件map:【{0}】  ",map);        
	        //根据时间查询需要推送的账单跟踪
	        List<BillCheckInfoEntity> list=billCheckInfoRepository.queryList(map);
	        if(list.size()>0){
	            for(BillCheckInfoEntity entity:list){
	                billCheckInfoService.saveCrm(entity);
	            } 
	        } 
        } catch (Exception e) {
            // TODO: handle exception
            XxlJobLogger.log("推送异常：{0}", e); 
            elConditionRepository.updateByPullType(elCondition);
            return printLog("推送异常", sw, FAIL);
        }
        elConditionRepository.updateByPullType(elCondition);
        return ReturnT.SUCCESS;
	}
   
    /**
     * 日志输出
     * <功能描述>
     * 
     * @author wangchen870
     * @date 2019年5月18日 下午5:29:29
     *
     * @param log
     * @param sw
     * @return
     */
    private ReturnT<String> printLog(String log, StopWatch sw, String status){
        sw.stop();
        XxlJobLogger.log(log + ", 共耗时：" + sw.getTotalTimeMillis() + "毫秒");
        if (SUCCESS.equals(status)) {
            return ReturnT.SUCCESS;
        }else {
            return ReturnT.FAIL; 
        }
    }
}
