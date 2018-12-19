package com.jiuyescm.bms.jobhandler;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jiuyescm.bms.base.reportWarehouse.ReportWarehouseCustomerEntity;
import com.jiuyescm.bms.general.service.IReportWarehouseBizImportService;
import com.jiuyescm.cfm.common.JAppContext;
import com.jiuyescm.common.utils.DateUtil;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHander;
import com.xxl.job.core.log.XxlJobLogger;

/**
 * 各仓业务数据导入统计报表job
 * @author yangss
 */
@JobHander(value="warehouseBizImportReportJob")
@Service
public class WarehouseBizImportReportJob extends IJobHandler{

	@Autowired
	private IReportWarehouseBizImportService reportWarehouseBizImportService;
	
	@Override
	public ReturnT<String> execute(String... params) throws Exception {
		XxlJobLogger.log("WarehouseBizImportReportJob start.");
		XxlJobLogger.log("开始各仓业务数据导入统计");
        return CalcJob(params);
	}
	
	private ReturnT<String> CalcJob(String[] params) {
		
		long starttime= System.currentTimeMillis();// 系统开始时间
		Timestamp createTime = JAppContext.currentTimestamp();
		String lastMonthFirstDay = DateUtil.getFirstDayOfMonth(1,"yyyy-MM-dd");
		lastMonthFirstDay+=" 00:00:00";
		String tomorrow = DateUtil.getCurrentDateForAddDay("yyyy-MM-dd",1);
		tomorrow+=" 00:00:00";
		XxlJobLogger.log("上个月第一天：{0}",lastMonthFirstDay);
		XxlJobLogger.log("明天：{0}",tomorrow);
		//对日期进行分组
		Map<String, String> dataMap = com.jiuyescm.common.utils.DateUtil.getSplitTime(lastMonthFirstDay,tomorrow,1);
		
		for (Map.Entry<String, String> entry : dataMap.entrySet()) {
			long start = System.currentTimeMillis();// 系统开始时间
			XxlJobLogger.log("开始统计 【{0}】 ~ 【{1}】 范围的数据",entry.getKey(),entry.getValue());
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("createTime", createTime);
			param.put("delFlag", "0");
			param.put("startTime", entry.getKey());
			param.put("endTime", entry.getValue());
			int ret = reportWarehouseBizImportService.upsertPalletStorage(param);
			XxlJobLogger.log("影响行数【{0}】,耗时【{1}】毫秒",ret,(System.currentTimeMillis()-start));
		}
		
		Map<String, Object> paramMonthMap = new HashMap<String, Object>();
		XxlJobLogger.log("处理上月商家免导配置");
		paramMonthMap.put("createTime", lastMonthFirstDay);
		wh_cust(paramMonthMap);
		XxlJobLogger.log("处理当月商家免导配置");
		paramMonthMap.put("createTime", tomorrow);
		wh_cust(paramMonthMap);
		
        XxlJobLogger.log("各仓业务数据导入统计,总耗时："+ (System.currentTimeMillis() - starttime) + "毫秒");
        return ReturnT.SUCCESS;
	}
	
	private void wh_cust(Map<String, Object> paramMonthMap){
		long start = System.currentTimeMillis();// 系统开始时间
		XxlJobLogger.log("免导商家处理  {0}",paramMonthMap);
		List<ReportWarehouseCustomerEntity> list=reportWarehouseBizImportService.queryWareList(paramMonthMap);
		XxlJobLogger.log("免导商家配置数量【{0}】",list.size());
		if(list.size()>0){
			reportWarehouseBizImportService.updateReport(list);
		}
		//插入应导入仓库
		XxlJobLogger.log("插入应导入仓库");
		reportWarehouseBizImportService.insertReport(paramMonthMap);
		XxlJobLogger.log("免导配置处理耗时【{0}】毫秒",(System.currentTimeMillis()-start));
	}
}
