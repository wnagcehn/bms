package com.jiuyescm.bms.jobhandler;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import com.jiuyescm.bms.appconfig.TenantConfig;
import com.jiuyescm.bms.billcheck.BillCheckInfoEntity;
import com.jiuyescm.bms.billcheck.repository.IBillCheckInfoRepository;
import com.jiuyescm.bms.common.enumtype.CheckBillStatusEnum;
import com.jiuyescm.bms.correct.ElConditionEntity;
import com.jiuyescm.bms.correct.repository.ElConditionRepository;
import com.jiuyescm.cfm.common.JAppContext;
import com.jiuyescm.crm.module.api.IModuleDataOpenService;
import com.jiuyescm.crm.module.vo.FieldDataOpenVO;
import com.jiuyescm.crm.module.vo.ModuleDataOpenVO;
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
    private TenantConfig tenantConfig;
    @Resource
    private IBillCheckInfoRepository billCheckInfoRepository;
    @Autowired
    private IModuleDataOpenService moduleDataOpenService;
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
	                saveCrm(entity);
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

	 public void saveCrm(BillCheckInfoEntity entity) {
	     try {
	        entity.setCrmAsynTime(JAppContext.currentTimestamp());
            // 根据id获取账单
            Map<String, Object> conditionMap = new HashMap<>();
            conditionMap.put("id", entity.getId());
            List<BillCheckInfoEntity> entities = billCheckInfoRepository.queryId(conditionMap);
            if(CollectionUtils.isEmpty(entities)){
                XxlJobLogger.log("根据账单ID：{0}" , entity.getId()+"未找到账单");
                return;
            }
            List<FieldDataOpenVO> listFieldDataOpenVO = getFieldDataOpenVOList(entities.get(0));
            if(CollectionUtils.isEmpty(listFieldDataOpenVO)){
                XxlJobLogger.log("不向crm推送数据：{0}" , entity.getId());
                return;
            }
            //封装CRM参数
            ModuleDataOpenVO moduleVo = new ModuleDataOpenVO();
            moduleVo.setFieldDataVos(listFieldDataOpenVO);
            moduleVo.setUniqueCheckFieldApiKey("id");
            Long tenantId = tenantConfig.getTenantId();
            String json = JSON.toJSON(moduleVo).toString();
            XxlJobLogger.log("发送CRM参数：{0}" ,json);
            Long result = moduleDataOpenService.saveModuleData(tenantId, "bill", moduleVo);
            XxlJobLogger.log("接收crm结果：{0}" , result);
            entity.setCrmStatus(1l);          
        } catch (Exception e) {
            XxlJobLogger.log("CRM保存接口失败:{0}", e);
            entity.setCrmStatus(2l);
        }
	    
	    try {
	        billCheckInfoRepository.update(entity);
        } catch (Exception e) {
            // TODO: handle exception
            XxlJobLogger.log(entity.getId()+"更新推送状态和时间失败:{0}", e);
        }
	     
	 }

	 
	 private List<FieldDataOpenVO> getFieldDataOpenVOList(BillCheckInfoEntity entity) {
        // 查询mk_id
        Map<String, Object> mkConditionMap = new HashMap<>();
        String invoiceName = entity.getInvoiceName();
        mkConditionMap.put("invoiceName", invoiceName);
        List<BillCheckInfoEntity> mkEntities = billCheckInfoRepository.querySourceId(mkConditionMap);
        if (CollectionUtils.isEmpty(mkEntities)) {
            XxlJobLogger.log("根据invoiceName在pub_customer_base未查询到商家：{0}" , invoiceName);
            return null;
        }
        BillCheckInfoEntity mkEntity = mkEntities.get(0);
        String sourceIdString = mkEntity.getSourceId();
        if(StringUtils.isBlank(sourceIdString)){
            XxlJobLogger.log("source_id为空：{0}" , invoiceName);
            return null;
        }
        Long sId = null;
        try {
            sId = Long.valueOf(sourceIdString);
        } catch (NumberFormatException e) {
            XxlJobLogger.log("source_id转换long失败：{0}" , invoiceName);
            e.printStackTrace();
            return null;
        }
        FieldDataOpenVO vo1 = getFieldDataOpenVO("mk_id",sId, "customer", true);
        // 获取账单状态
        Map<String, String> enumMap = CheckBillStatusEnum.getMap();
        String billStatusString = enumMap.get(entity.getBillStatus());
        FieldDataOpenVO vo2 = getFieldDataOpenVO("bill_status", billStatusString, null, null);
        // 获取总金额
        FieldDataOpenVO vo3 = getFieldDataOpenVO("confirm_amount", entity.getConfirmAmount(), null, null);
        // 获取已回款
        FieldDataOpenVO vo4 = getFieldDataOpenVO("receipt_amount", entity.getReceiptAmount(), null, null);
        // 获取待回款
        FieldDataOpenVO vo5 = getFieldDataOpenVO("un_receipt_amount", entity.getUnReceiptAmount(), null, null);
        // 获取商家合同名称
        FieldDataOpenVO vo6 = getFieldDataOpenVO("invoice_name", entity.getInvoiceName(), null, null);
        // 获取业务月份
        FieldDataOpenVO vo7 = getFieldDataOpenVO("create_month", entity.getCreateMonth(), null, null);
        // 获取账单编号
        FieldDataOpenVO vo8 = getFieldDataOpenVO("id", entity.getId(), null, null);
        // 获取账单名称
        FieldDataOpenVO vo9 = getFieldDataOpenVO("bill_name", entity.getBillName(), null, null);
        List<FieldDataOpenVO> list = new ArrayList<FieldDataOpenVO>();
        list.add(vo1);
        list.add(vo2);
        list.add(vo3);
        list.add(vo4);
        list.add(vo5);
        list.add(vo6);
        list.add(vo7);
        list.add(vo8);
        list.add(vo9);
        return list;
    }

    private FieldDataOpenVO getFieldDataOpenVO(String fieldApiKey, Object fieldValue, String relModule, Boolean bool) {
        FieldDataOpenVO vo = new FieldDataOpenVO();
        vo.setFieldApiKey(fieldApiKey);
        vo.setFieldValue(fieldValue);
        vo.setRelModule(relModule);
        vo.setRelationField(bool);
        return vo;
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
