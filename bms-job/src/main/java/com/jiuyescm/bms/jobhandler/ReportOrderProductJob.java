package com.jiuyescm.bms.jobhandler;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jiuyescm.bms.correct.ElConditionEntity;
import com.jiuyescm.bms.correct.repository.ElConditionRepository;
import com.jiuyescm.common.utils.DateUtil;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHander;
import com.xxl.job.core.log.XxlJobLogger;

/**
 * 打标统计job
 * @author liuzhicheng
 */
@JobHander(value="reportOrderProductJob")
@Service
public class ReportOrderProductJob  extends IJobHandler{

		@Autowired
		private ElConditionRepository elConditionRepository;
		
		@Override
		public ReturnT<String> execute(String... params) throws Exception {
			XxlJobLogger.log("ReportOrderProductJob start.");
			XxlJobLogger.log("打标统计任务");
	        return CalcJob(params);
		}
		
		private ReturnT<String> CalcJob(String[] params) {
			
			long starttime= System.currentTimeMillis();// 系统开始时间
			//查询etl_condition表
			Map<String, Object> elCondition = new HashMap<>();
			elCondition.put("pullType", "ReportOrderProduct");
			List<ElConditionEntity> elList =  elConditionRepository.query(elCondition);
			if(CollectionUtils.isEmpty(elList))return null;
			ElConditionEntity elEntity = elList.get(0);
			//开始月时间
			Timestamp startTimestamp =elEntity.getLastTime();
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String startMonthFirstDay = df.format(startTimestamp);
			//结束月时间
			Date date = new Date(startTimestamp.getTime());
			Calendar cl = Calendar.getInstance();
			cl.setTime(date);
			cl.add(Calendar.MONTH, 1);
			String endMonthFirstDay = DateUtil.getFirstDayOfMonth(cl.get(Calendar.YEAR),cl.get(Calendar.MONTH)+1);
			endMonthFirstDay+=" 00:00:00";
			XxlJobLogger.log("开始月第一天：{0}",startMonthFirstDay);
			XxlJobLogger.log("结束月第一天：{0}",endMonthFirstDay);
			//对日期进行分组
			Map<String, String> dataMap = com.jiuyescm.common.utils.DateUtil.getSplitTime(startMonthFirstDay,endMonthFirstDay,1);
			//去除结束月第一天
			dataMap.remove(endMonthFirstDay+".0");
	
			//因为数据量大，使用一天天更新（这种方式更新，如果product_mark为null，会出现重复值，但是生产上不会有null出现）
			Map<String, Object> param = new HashMap<>();
			for (Map.Entry<String, String> entry : dataMap.entrySet()) {
				param.put("startTime", entry.getKey());
				param.put("endTime", entry.getValue());
				elConditionRepository.save(param);
			}
			
			//对etl_condition表更新（默认只对上个月进行统计）
			Timestamp lastTimestamp = Timestamp.valueOf(endMonthFirstDay);
			cl.setTime(new Date());
			cl.add(Calendar.MONTH, -1);
			Timestamp lastTwoMonth = new Timestamp(cl.getTime().getTime());
			if(lastTimestamp.before(lastTwoMonth)){
				 elEntity.setLastTime(lastTimestamp);
				 elEntity.setCurrTime(new Timestamp(new Date().getTime()));
				elConditionRepository.update(elEntity);
			}

	        XxlJobLogger.log("任务总耗时："+ (System.currentTimeMillis() - starttime) + "毫秒");
	        return ReturnT.SUCCESS;
		}
}