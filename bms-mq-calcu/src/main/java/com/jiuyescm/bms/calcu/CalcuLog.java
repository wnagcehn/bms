package com.jiuyescm.bms.calcu;

import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.baidu.disconf.core.common.utils.GsonUtils;
import com.google.gson.Gson;
import com.jiuyescm.bms.calculate.vo.CalcuBaseInfoVo;

public class CalcuLog {

	private static Logger logger = LoggerFactory.getLogger(CalcuLog.class);
	
	public static void printLog(CalcuBaseInfoVo t){
		try{
			String jsonString = JSONObject.fromObject(t).toString();
			String jsonString1 = GsonUtils.toJson(t);
			String jsonString2 = JSON.toJSONString(t);
			logger.info("{}",jsonString);
			logger.info("{}",jsonString1);
			logger.info("{}",jsonString2);
		}catch(Exception ex){
			
		}
	}
	
}
