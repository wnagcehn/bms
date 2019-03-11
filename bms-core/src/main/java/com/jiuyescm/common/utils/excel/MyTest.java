package com.jiuyescm.common.utils.excel;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import com.jiuyescm.common.utils.excel.MyAnnotation.MyFieldAnnotation;

public class MyTest {
    
    public static Map<String,Object>  getData(Object obj) {
    	Class clazz =  obj.getClass();
        Field[] fields = clazz.getDeclaredFields(); // 返回所有的属性
        Map<String, Object> dataItem = new HashMap<String, Object>();
        for (Field field : fields) {
        	MyFieldAnnotation myFieldAnnotation = field.getAnnotation(MyFieldAnnotation.class);
        	if(null!=myFieldAnnotation) {
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
    	        dataItem.put("XH"+myFieldAnnotation.num(), val);
        	}
		}
		return dataItem;
	}
    
    public static List<Map<String, Object>>  getTitle(Class clazz) {
        Field[] fields = clazz.getDeclaredFields(); // 返回所有的属性 
    	List<Map<String, Object>> headInfoList = new ArrayList<Map<String,Object>>();
        for (Field field : fields) {
        	MyFieldAnnotation myFieldAnnotation = field.getAnnotation(MyFieldAnnotation.class);
        	if(null!=myFieldAnnotation) {
        		//暴力反射
        		field.setAccessible(true);
        		//表头
                Map<String, Object> itemMap = new HashMap<String, Object>();
                itemMap.put("title", myFieldAnnotation.title());
                itemMap.put("columnWidth", 50);
                itemMap.put("dataKey","XH"+myFieldAnnotation.num() );
                headInfoList.add(itemMap);
        	}
		}
		return headInfoList;
	}
    
    public static List<Map<String, Object>> getDataList(ArrayList list){
        List<Map<String, Object>> dataList = new ArrayList<Map<String,Object>>();
        for (int i = 0; i < list.size(); i++) dataList.add(getData(list.get(i)));
		return dataList;
    }
    
    public static void main(String[] args) throws IOException {
    	//表头
    	Class<Demo> clazz = Demo.class;
    	List<Map<String, Object>> headInfoList = (List<Map<String, Object>>) getTitle(clazz);
    	//创建行对象集合
    	ArrayList<Demo> demos = new ArrayList<>();
    	for (int i = 0; i < 100; i++) {
        	Demo demo = new Demo("有注解的：1sa","有注解的：23x",i+"","无注解：2123");
        	demos.add(demo);
		}
    	//内容
        List<Map<String, Object>> dataList = getDataList(demos);
        
    	POISXSSUtil poiUtil = new POISXSSUtil();
    	SXSSFWorkbook hssfWorkbook = poiUtil.getXSSFWorkbook();
    	//循环写入
    	for (int i = 0; i <3; i++) {
    		// poiUtil.exportExcel2FilePath(poiUtil,hssfWorkbook,"test sheet 1",1, headInfoList, dataList);
    		poiUtil.exportExcel2FilePath(poiUtil,hssfWorkbook,"test sheet 1", headInfoList, dataList);
    		// poiUtil.exportExcelFilePath(poiUtil,hssfWorkbook,"test sheet 2","e:\\tmp\\customer2.xlsx", headInfoList, dataList);
		}
    	poiUtil.write2FilePath(hssfWorkbook, "e:\\my-test.xlsx");
	}
}