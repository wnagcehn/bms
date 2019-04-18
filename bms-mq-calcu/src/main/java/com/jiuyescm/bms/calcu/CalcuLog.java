package com.jiuyescm.bms.calcu;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jiuyescm.bms.calculate.vo.CalcuBaseInfoVo;

import net.sf.json.JSONObject;

public class CalcuLog {

	private static Logger logger = LoggerFactory.getLogger(CalcuLog.class);
	
	public static void printLog(CalcuBaseInfoVo t){
		try{
		    t.setUniqueKey(t.getFeesNo() + t.getSubjectCode());
			String jsonString = JSONObject.fromObject(t).toString();
			logger.info("{}",jsonString);
		}catch(Exception ex){
			
		}
	}
	
	/**
	 * 日志打印
	 * @param node    日志节点
	 * @param descrip 异常描述
	 * @param data	     实体对象
	 * @param t		     日志对象
	 */
	public static void printLog(String node,String descrip,Object data,CalcuBaseInfoVo t){
		t.setNode(node);
		t.setDescrip(descrip);
		t.setData(data);
		printLog(t);
	}
	
}
