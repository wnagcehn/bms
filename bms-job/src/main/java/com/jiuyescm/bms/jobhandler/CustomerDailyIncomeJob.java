package com.jiuyescm.bms.jobhandler;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.jiuyescm.bms.common.DateUtil;
import com.jiuyescm.bms.general.entity.FeesReceiveDispatchEntity;
import com.jiuyescm.bms.general.entity.FeesReceiveStorageEntity;
import com.jiuyescm.bms.general.entity.FeesReceiveTransportEntity;
import com.jiuyescm.bms.general.entity.ReportCustomerDailyIncomeEntity;
import com.jiuyescm.bms.general.service.IFeesReceiveDispatchService;
import com.jiuyescm.bms.general.service.IFeesReceiveStorageService;
import com.jiuyescm.bms.general.service.IFeesReceiveTransportService;
import com.jiuyescm.bms.general.service.IReportCustomerDailyIncomeService;
import com.jiuyescm.cfm.common.JAppContext;
import com.jiuyescm.exception.BizException;
import com.jiuyescm.mdm.customer.api.ICustomerService;
import com.jiuyescm.mdm.customer.vo.CustomerInfoVo;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHander;
import com.xxl.job.core.log.XxlJobLogger;

/**
 * 商家每日收入统计
 * @author yangss
 */
@JobHander(value="customerDailyIncomeJob")
@Service
public class CustomerDailyIncomeJob extends IJobHandler{

	@Autowired 
	private IFeesReceiveStorageService feesReceiveStorageService;
	@Autowired 
	private IFeesReceiveDispatchService feesReceiveDispatchService;
	@Autowired 
	private IFeesReceiveTransportService feesReceiveTransportService;
	@Autowired
	private IReportCustomerDailyIncomeService customerDailyIncomeService;
	@Autowired 
	private ICustomerService customerService;
	
	@Override
	public ReturnT<String> execute(String... params) throws Exception {
		XxlJobLogger.log("customerDailyIncomeJob start.");
        return CalcJob(params);
	}
	
	private ReturnT<String> CalcJob(String[] params) {
		long btime= System.currentTimeMillis();// 系统开始时间
		long current = 0l;// 当前系统时间
		
		String preDate = DateUtil.getCurrentRandomDate(-1);
		try {
			if(params != null && params.length > 0) {
				if (StringUtils.isNotBlank(params[0])) {
					//单次执行最多计算多少入库单
					int preDays = Integer.parseInt(params[0]);
					preDate = DateUtil.getCurrentRandomDate(-preDays);;
				}
			}
		} catch (Exception e) {
			current = System.currentTimeMillis();
            XxlJobLogger.log("【终止异常】,解析Job配置的参数出现错误,原因:" + e.getMessage() + ",耗时："+ (current - btime) + "毫秒");
            return ReturnT.FAIL;
		}
		String beginPreDate = preDate + " 00:00:00";
		String endPreDate = preDate + " 23:59:59";
		
        XxlJobLogger.log("开始时间：【{0}】", beginPreDate);
        XxlJobLogger.log("结束时间：【{0}】", endPreDate);
        Map<String, CustomerInfoVo> customerMap = new HashMap<String, CustomerInfoVo>();
        List<CustomerInfoVo> customerInfoList = customerService.queryCustomerInfo();
        if (null != customerInfoList && customerInfoList.size() > 0) {
        	for (CustomerInfoVo vo : customerInfoList) {
        		customerMap.put(vo.getCustomerId(), vo);
        	}
		}
        
        Timestamp operaterTime = JAppContext.currentTimestamp();
        Date feesDate = DateUtil.getFormatDate(preDate);
        // 逻辑删除当前日期的数据
        Map<String, Object> condition = new HashMap<String, Object>();
        condition.put("lastModifier", "system");
        condition.put("lastModifyTime", operaterTime);
        condition.put("delFlag", "1");
        condition.put("feesDate", feesDate);
        customerDailyIncomeService.update(condition);
		
        List<ReportCustomerDailyIncomeEntity> list = new ArrayList<ReportCustomerDailyIncomeEntity>();
        condition = new HashMap<String, Object>();
        condition.put("startTime", beginPreDate);
        condition.put("endTime", endPreDate);
        // 仓储
        List<FeesReceiveStorageEntity> storageList = feesReceiveStorageService.queryDailyStorageFee(condition);
        if (null != storageList && storageList.size() > 0) {
        	for (FeesReceiveStorageEntity storageEntity : storageList) {
        		String customerId = storageEntity.getCustomerId();
        		if (customerMap.containsKey(customerId)) {
					CustomerInfoVo entity = customerMap.get(customerId);
					// 封装收入实体
					ReportCustomerDailyIncomeEntity incomeEntity = encapIncomeEntity(customerId, 
							storageEntity.getOtherSubjectCode(), storageEntity.getCost(), 
							entity, feesDate, operaterTime);
					list.add(incomeEntity);
				}
			}
		}
        
        // 配送
        List<FeesReceiveDispatchEntity> dispatchList = feesReceiveDispatchService.queryDailyFees(condition);
        if (null != dispatchList && dispatchList.size() > 0) {
			for (FeesReceiveDispatchEntity dispatchEntity : dispatchList) {
				String customerId = dispatchEntity.getCustomerid();
				if (customerMap.containsKey(customerId)) {
					CustomerInfoVo entity = customerMap.get(customerId);
					// 封装收入实体
					ReportCustomerDailyIncomeEntity incomeEntity = encapIncomeEntity(customerId, 
							dispatchEntity.getOtherSubjectCode(), BigDecimal.valueOf(dispatchEntity.getAmount()), 
							entity, feesDate, operaterTime);
					list.add(incomeEntity);
				}
			}
		}
        
        // 运输
        List<FeesReceiveTransportEntity> transportList = feesReceiveTransportService.queryDailyFees(condition);
        if (null != transportList && transportList.size() > 0) {
			for (FeesReceiveTransportEntity transportEntity : transportList) {
				String customerId = transportEntity.getCustomerid();
				if (customerMap.containsKey(customerId)) {
					CustomerInfoVo entity = customerMap.get(customerId);
					// 封装收入实体
					ReportCustomerDailyIncomeEntity incomeEntity = encapIncomeEntity(customerId, 
							transportEntity.getOtherSubjectCode(), BigDecimal.valueOf(transportEntity.getTotleprice()), 
							entity, feesDate, operaterTime);
					list.add(incomeEntity);
				}
			}
		}
        
        if (!list.isEmpty()) {
        	int saveNum = customerDailyIncomeService.saveList(list);
        	if (list.size() != saveNum) {
        		XxlJobLogger.log("批量新增商家收入统计异常");
        		return ReturnT.FAIL;
			}
		}
        
		return ReturnT.SUCCESS;
	}
	
	
	private ReportCustomerDailyIncomeEntity encapIncomeEntity(String customerId, String subjectCode, 
			BigDecimal amount, CustomerInfoVo entity, Date feesDate, Timestamp operaterTime){
		String system = "system";
		
		ReportCustomerDailyIncomeEntity incomeEntity = new ReportCustomerDailyIncomeEntity();
		incomeEntity.setCustomerId(customerId);
		incomeEntity.setCustomerName(entity.getCustomerName());
		incomeEntity.setRegionId(entity.getRegionId());
		incomeEntity.setRegionName(entity.getRegionName());
		incomeEntity.setDeptName(entity.getDeptName());
		incomeEntity.setSeller(entity.getSeller());
		incomeEntity.setManager(entity.getManager());
		incomeEntity.setSettleOfficer(entity.getSettleOfficer());
		incomeEntity.setFeesDate(feesDate);
		incomeEntity.setSubjectCode(subjectCode);
		incomeEntity.setAmount(amount);
		incomeEntity.setCreator(system);
		incomeEntity.setCreateTime(operaterTime);
		incomeEntity.setDelFlag("0");
		
		return incomeEntity;
	}
}
