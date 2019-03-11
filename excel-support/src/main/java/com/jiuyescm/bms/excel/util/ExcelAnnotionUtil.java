package com.jiuyescm.bms.excel.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jiuyescm.bms.excel.annotation.ExcelAnnotation.ExcelField;

public class ExcelAnnotionUtil {
    
    public static Map<String,Object>  getData(Object obj) {
    	Class clazz =  obj.getClass();
        Field[] fields = clazz.getDeclaredFields(); // 返回所有的属性
        Map<String, Object> dataItem = new HashMap<String, Object>();
        for (Field field : fields) {
        	ExcelField excelField = field.getAnnotation(ExcelField.class);
        	if(null!=excelField) {
        		//暴力反射
        		field.setAccessible(true);
        		//列值
        		Object val = null;
        		//内容
    			try {
    				val =	field.get(obj);
    			} catch (IllegalArgumentException | IllegalAccessException e) {
    				e.printStackTrace();
    			}
    	        dataItem.put("XH"+excelField.num(), val);
        	}
		}
		return dataItem;
	}
    
    public static List<Map<String, Object>>  getTitle(Class clazz) {
        Field[] fields = clazz.getDeclaredFields(); // 返回所有的属性 
    	List<Map<String, Object>> headInfoList = new ArrayList<Map<String,Object>>();
        for (Field field : fields) {
        	ExcelField excelField = field.getAnnotation(ExcelField.class);
        	if(null!=excelField) {
        		//暴力反射
        		field.setAccessible(true);
        		//表头
                Map<String, Object> itemMap = new HashMap<String, Object>();
                itemMap.put("title", excelField.title());
                itemMap.put("columnWidth", 50);
                itemMap.put("dataKey","XH"+excelField.num() );
                headInfoList.add(itemMap);
        	}
		}
		return headInfoList;
	}
    
    public static List<Map<String, Object>> getDataList(List list){
        List<Map<String, Object>> dataList = new ArrayList<Map<String,Object>>();
        for (int i = 0; i < list.size(); i++) dataList.add(getData(list.get(i)));
		return dataList;
    }
}