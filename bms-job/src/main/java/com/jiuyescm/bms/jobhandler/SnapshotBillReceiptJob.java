package com.jiuyescm.bms.jobhandler;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.jiuyescm.bms.general.entity.SnapshotBillReceiptEntity;
import com.jiuyescm.bms.general.service.ISnapshotBillReceiptService;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHander;
import com.xxl.job.core.log.XxlJobLogger;

/**
 * 快照job
 * @author liuzhicheng
 */
@JobHander(value="snapshotBillReceiptJob")
@Service
public class SnapshotBillReceiptJob extends IJobHandler {
	@Autowired
	private ISnapshotBillReceiptService snapshotBillReceiptService;
	
	@Override
	public ReturnT<String> execute(String... params) throws Exception {
		XxlJobLogger.log("SnapshotBillReceiptJob start.");
		XxlJobLogger.log("开始快照任务");
        return CalcJob(params);
	}
	
	private ReturnT<String> CalcJob(String[] params) {
		long starttime= System.currentTimeMillis();// 系统开始时间
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        
        Calendar c=Calendar.getInstance(Locale.CHINA);
        c.setFirstDayOfWeek(Calendar.MONDAY);
        c.setTimeInMillis(starttime);
		XxlJobLogger.log("当前时间：{0}",format.format(c.getTime()));
        c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        String monday = format.format(c.getTime());
        c.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        String sunday = format.format(c.getTime());
		XxlJobLogger.log("周一时间：{0}",monday);
		XxlJobLogger.log("周天时间：{0}",sunday);

		//查询一星期所有预计回款
		Map<String, Object> condition = new HashMap<>();
		condition.put("monday",monday);
		condition.put("sunday",sunday);
		condition.put("deptName","销售部");
		List<SnapshotBillReceiptEntity> list = snapshotBillReceiptService.query(condition);
		XxlJobLogger.log("共查询到预计回款记录数：{0}",list.size());
		
		//写入时间
		Timestamp timestamp = new Timestamp(new Date().getTime());
		for (SnapshotBillReceiptEntity snapshotBillReceiptEntity : list) {
			snapshotBillReceiptEntity.setWriteTime(timestamp);
		}
		
		snapshotBillReceiptService.InsertBatch(list);

        XxlJobLogger.log("快照任务总耗时："+ (System.currentTimeMillis() - starttime) + "毫秒");
        return ReturnT.SUCCESS;
	}

}