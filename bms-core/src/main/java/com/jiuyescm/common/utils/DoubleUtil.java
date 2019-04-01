package com.jiuyescm.common.utils;

public class DoubleUtil {

	/**
	 * 判断Double类型数据是否为空值
	 * @param d
	 * @return
	 * null -> true
	 * 0 -> true   0.0 -> true  -0.0 -> true
	 * 其他返回 false
	 */
	public static boolean isBlank(Double d){
		
		if(d == null){
			return true;
		}
		if(d == 0){
			return true;
		}
		return false;
	}
	
	/**
	 * 判断Integer类型数据是否为空值
	 * @param d
	 * @return
	 * null -> true
	 * 0 -> true   0.0 -> true  -0.0 -> true
	 * 其他返回 false
	 */
	public static boolean isBlank(Integer d){
		
		if(d == null){
			return true;
		}
		if(d == 0){
			return true;
		}
		return false;
	}
}
