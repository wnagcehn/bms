package com.jiuyescm.common.utils;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.jiuyescm.common.tool.JsonPluginsUtil;

/**
 * 转换器 1:将javaBean转换成Map 2:将map转换成Javabean
 * 
 * @author cf
 */
public class BeanConverter {
	
	private static final Logger logger = Logger.getLogger(BeanConverter.class.getName());

	
	/**
	 * 将javaBean转换成Map
	 * 
	 * @param javaBean
	 *            javaBean
	 * @return Map对象
	 */
	
	 private BeanConverter(){
		 
	 }
	
	public static Map<String, Object> toMap(Object javaBean) {
		Map<String, Object> result = new HashMap<String, Object>();
		Method[] methods = javaBean.getClass().getDeclaredMethods();
		for (Method method : methods) {
			try {
				if (method.getName().startsWith("get")) {
					String field = method.getName();
					field = field.substring(field.indexOf("get") + 3);
					field = field.toLowerCase().charAt(0) + field.substring(1);
					Object value = method.invoke(javaBean, (Object[]) null);
					result.put(field, null == value ? "" : value.toString());
				}
			} catch (Exception e) {
				System.out.println("javaBean转换成Map报错！");
				//e.printStackTrace();
				logger.info(e);
			}
		}
		return result;
	}

	/**
	 * 将map转换成Javabean
	 * 
	 * @param javabean
	 *            javaBean
	 * @param data
	 *            map数据
	 */
	public static Object toJavaBean(Object javabean, Map<String, String> data) {
		Method[] methods = javabean.getClass().getDeclaredMethods();
		for (Method method : methods) {
			try {
				if (method.getName().startsWith("set")) {
					String field = method.getName();
					field = field.substring(field.indexOf("set") + 3);
					field = field.toLowerCase().charAt(0) + field.substring(1);
					method.invoke(javabean, new Object[] { data.get(field) });
				}
			} catch (Exception e) {
				System.out.println("map转换成Javabean报错！");
				//e.printStackTrace();
				logger.info(e);
			}
		}
		return javabean;
	}
}
