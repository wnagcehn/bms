/**
 * 
 */
package com.jiuyescm.bms.jobhandler;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jiuyescm.bms.base.reportWarehouse.ReportWarehouseCustomerEntity;
import com.jiuyescm.bms.common.DateUtil;
import com.jiuyescm.bms.general.service.IReportWarehouseBizImportService;
import com.jiuyescm.cfm.common.JAppContext;
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
        return CalcJob(params);
	}
	
	private ReturnT<String> CalcJob(String[] params) {
		long btime= System.currentTimeMillis();// 系统开始时间
		long current = 0l;// 当前系统时间
		
		// 默认上个月的第一天
		int preMonth = 1;
		try {
			if(params != null && params.length > 0) { 
				
				if (StringUtils.isNotBlank(params[0])) {
					// 指定月份
					preMonth = Integer.parseInt(params[0]);
				}
			}
		} catch (Exception e) {
			current = System.currentTimeMillis();
            XxlJobLogger.log("【终止异常】,解析Job配置的参数出现错误,原因:" + e.getMessage() + ",耗时："+ (current - btime) + "毫秒");
            return ReturnT.FAIL;
		}
		
		// 获取指定月份的第一天，天数，最后一天
		String curDate = DateUtil.getFirstDayOfMonth(preMonth);
		int days = DateUtil.getDaysOfMonth(curDate);
		String specifyDate = DateUtil.getSpecifyDate(curDate, days);
		
		// 5天一个批次执行
		Map<String, Object> param = null;
		int factor = 5;
		int loop = days / factor;
		Timestamp createTime = JAppContext.currentTimestamp();
		//插入的行数
		int num = 0;
		for (int i = 1; i <= loop; i++) {
			String beginDate = DateUtil.getSpecifyDate(curDate, (i-1) * factor);
			String endDate = DateUtil.getSpecifyDate(curDate, i * factor);
			if (i == loop) {
				endDate = specifyDate;
			}
			
			beginDate += " 00:00:00";
			endDate += " 00:00:00";
			XxlJobLogger.log("开始时间：【{0}】", beginDate);
			XxlJobLogger.log("结束时间：【{0}】", endDate);
			
			param = new HashMap<String, Object>();
			param.put("createTime", createTime);
			param.put("delFlag", "0");
			param.put("startTime", curDate);
			param.put("endTime", endDate);
			
			// 统计商品托数
			num+=reportWarehouseBizImportService.upsertPalletStorage(param);
			// 统计耗材
			num+=reportWarehouseBizImportService.upsertPackMaterial(param);
		}
        
		//查询已经设置的商家仓库
		if(num>0){
			List<ReportWarehouseCustomerEntity> list=reportWarehouseBizImportService.queryWareList(param);
			if(list.size()>0){
				reportWarehouseBizImportService.updateReport(list);
			}
			//插入应导入仓库
			reportWarehouseBizImportService.insertReport(param);
		}
		
        current = System.currentTimeMillis();
        XxlJobLogger.log("各仓业务数据导入统计,耗时："+ (current - btime) + "毫秒");
        return ReturnT.SUCCESS;
	}
	
	public static void main(String[] args) {
		// 默认统计当天
		String curDate = DateUtil.getFirstDayOfMonth(2);
		int days = DateUtil.getDaysOfMonth(curDate);
		String specifyDate = DateUtil.getSpecifyDate(curDate, days);
		
		System.out.println(curDate);
		System.out.println(days);
		System.out.println(specifyDate);
		
		int factor = 5;
		int loop = days / factor;
		System.out.println(loop);
		for (int i = 1; i <= loop; i++) {
			System.out.println(i);
			String beginDate = DateUtil.getSpecifyDate(curDate, (i-1)*factor);
			beginDate += " 00:00:00";
			System.out.println("begin=" + beginDate);
			String endDate = DateUtil.getSpecifyDate(curDate, i*factor);
			if (i == loop) {
				endDate = specifyDate;
			}
			endDate += " 00:00:00";
			System.out.println("end=" + endDate);
		}
	}
}
