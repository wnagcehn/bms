package com.jiuyescm.bms.excel.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.ToDoubleFunction;
import java.util.function.ToIntFunction;
import java.util.function.ToLongFunction;

import org.apache.commons.collections.CollectionUtils;

import com.jiuyescm.bms.excel.annotation.ExcelAnnotation.ExcelField;

/**
 * 
 * @author liuzhicheng
 *
 */
public class ExcelAnnotionUtil {
    
    private static Map<String,Object>  getData(Object obj) {
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
                itemMap.put("dataKey","XH"+excelField.num());
                itemMap.put("num", excelField.num());
                headInfoList.add(itemMap);
        	}
		}
        if (CollectionUtils.isNotEmpty(headInfoList)) {
            Collections.sort(headInfoList, new Comparator<Map<String, Object>>(){
                /*
                 * 返回负数表示：m1小于m2，
                 * 返回0 表示：m1和m2相等，
                 * 返回正数表示：m1大于m2
                 */
                public int compare(Map<String, Object> m1, Map<String, Object> m2) {
                    if(((Integer) m1.get("num")) > ((Integer)m2.get("num"))){
                        return 1;
                    }
                    if(((Integer) m1.get("num")) == ((Integer)m2.get("num"))){
                        return 0;
                    }
                    return -1;
                }

                @Override
                public Comparator<Map<String, Object>> reversed() {
                    // TODO Auto-generated method stub
                    return null;
                }

                @Override
                public Comparator<Map<String, Object>> thenComparing(Comparator<? super Map<String, Object>> other) {
                    // TODO Auto-generated method stub
                    return null;
                }

                @Override
                public <U> Comparator<Map<String, Object>> thenComparing(
                        Function<? super Map<String, Object>, ? extends U> keyExtractor,
                        Comparator<? super U> keyComparator) {
                    // TODO Auto-generated method stub
                    return null;
                }

                @Override
                public <U extends Comparable<? super U>> Comparator<Map<String, Object>> thenComparing(
                        Function<? super Map<String, Object>, ? extends U> keyExtractor) {
                    // TODO Auto-generated method stub
                    return null;
                }

                @Override
                public Comparator<Map<String, Object>> thenComparingInt(
                        ToIntFunction<? super Map<String, Object>> keyExtractor) {
                    // TODO Auto-generated method stub
                    return null;
                }

                @Override
                public Comparator<Map<String, Object>> thenComparingLong(
                        ToLongFunction<? super Map<String, Object>> keyExtractor) {
                    // TODO Auto-generated method stub
                    return null;
                }

                @Override
                public Comparator<Map<String, Object>> thenComparingDouble(
                        ToDoubleFunction<? super Map<String, Object>> keyExtractor) {
                    // TODO Auto-generated method stub
                    return null;
                }
                
            });
        }
		return headInfoList;
	}
    
    public static List<Map<String, Object>> getDataList(List list){
        List<Map<String, Object>> dataList = new ArrayList<Map<String,Object>>();
        for (int i = 0; i < list.size(); i++) dataList.add(getData(list.get(i)));
		return dataList;
    }
}