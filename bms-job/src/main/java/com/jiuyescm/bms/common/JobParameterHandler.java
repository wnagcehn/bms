package com.jiuyescm.bms.common;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.xxl.job.core.log.XxlJobLogger;

/**
 * 定时任务参数处理
 * @author yangshuaishuai
 *
 */
public class JobParameterHandler {
	
	private JobParameterHandler(){
	}

	/**
	 * 处理参数
	 */
	public synchronized static Map<String, Object> handler(String[] params) throws Exception{
		
		Map<String, Object> cond = new HashMap<String, Object>();
		
		try{
			if(params != null && params.length > 0) {
		    	if (StringUtils.isNotBlank(params[0])) {
		    		//单次执行最多计算多少入库单
		    		cond.put("num", Integer.parseInt(params[0]));
				}
		        if (StringUtils.isNotBlank(params[1])) {
					//开始时间 从当前日期向前推几天，比如：3
		        	int beforeDay = Integer.parseInt(params[1]);
		        	String betinTime = DateUtil.getCurrentRandomDate(-beforeDay) + " 00:00:00";
		        	cond.put("beginTime", betinTime);
				}
		        if (StringUtils.isNotBlank(params[2])) {
					//结束时间
		        	int afterDay = Integer.parseInt(params[2]);
		        	String endTime = DateUtil.getCurrentRandomDate(afterDay) + " 00:00:00";
		        	cond.put("endTime", endTime);
				}
		        if (StringUtils.isNotBlank(params[3])) {
					//商家id
		        	cond.put("customerId", params[3].trim());
				}
		        if (StringUtils.isNotBlank(params[4])) {
					//仓库
		        	cond.put("warehouseCode", params[4].trim());
				}
		        if (StringUtils.isNotBlank(params[5])) {
					//物流商
		        	cond.put("carrierId", params[5].trim());
				}
		        if (StringUtils.isNotBlank(params[6])) {
		        	//宅配商
		        	cond.put("deliveryId", params[6].trim());
		        }
		        if (StringUtils.isNotBlank(params[7])) {
		        	//主键ID
		        	cond.put("id", Long.valueOf(params[7].trim()));
		        }
		        if (StringUtils.isNotBlank(params[8])) {
		        	//计算状态
		        	cond.put("isCalculated", params[8].trim());//重算
		        	XxlJobLogger.log("isCalculated-{0}",params[8].trim());
		        }
		        if(params.length>9){
		        	cond.put("isCalcuContract", true);//重算
		        } 
			}
			return cond;
		}
		catch(Exception ex){
			XxlJobLogger.log("【终止异常】,解析Job配置的参数出现错误,原因:" + ex.getMessage());
			return null;
		}
	}
}
