package com.jiuyescm.bms.excel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.poi.xssf.streaming.SXSSFWorkbook;

public class ExcelAnnotionTest {
    public static void main(String[] args) throws IOException {
    	ExcelAnnotionUtil util = new ExcelAnnotionUtil();
    	//表头
    	Class<TitleDemo> clazz = TitleDemo.class;
    	List<Map<String, Object>> headInfoList = (List<Map<String, Object>>) util.getTitle(clazz);
    	//创建行对象集合
    	ArrayList<TitleDemo> demos = new ArrayList<>();
    	for (int i = 0; i < 100; i++) {
    		TitleDemo demo = new TitleDemo("1901","销售"+i,"区域"+i,10000D*i+"");
        	demos.add(demo);
		}
    	//内容
        List<Map<String, Object>> dataList = util.getDataList(demos);
        
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