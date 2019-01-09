package com.jiuyescm.bms.excel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;






import com.jiuyescm.bms.excel.ExcelXlsxReader;
import com.jiuyescm.bms.excel.callback.SheetReadCallBack;
import com.jiuyescm.bms.excel.data.DataColumn;
import com.jiuyescm.bms.excel.data.DataRow;
import com.jiuyescm.bms.excel.data.Sheet;
import com.jiuyescm.bms.excel.data.XlsxWorkBook;
import com.jiuyescm.bms.excel.opc.OpcSheet;


public class Test {

	public static void main(String[] args) throws FileNotFoundException {
		
		//System.out.println("123");
		test2();	
		//System.out.println(trans("AD"));
		
	}


	
    public static void test2() throws FileNotFoundException {
        try{
        	String path = "E:/深圳山海味道农贸科技有限公司-2018-11-预账单 (1).xlsx";
    		File file = new File(path);
            FileInputStream inputStream = new FileInputStream(file);
            XlsxWorkBook book = new XlsxWorkBook(inputStream);
            List<Sheet> list = book.getSheets();
            
            for (Sheet sheet : book.getSheets()) {
    			System.out.println("sheet名称【"+sheet.getSheetName()+"】 id【"+sheet.getSheetId()+"】");
    			book.readSheet(sheet.getSheetId(), new SheetReadCallBack() {
    				
    				@Override
    				public void readTitle(List<String> columns) {
    					StringBuilder sb  = new StringBuilder();
    					sb.append("行号     ");
    					for (String string : columns) {
    						sb.append("  "+string+"  ");
    					}
    					System.out.println(sb.toString());
    				}
    				
    				@Override
    				public void read(DataRow dr) {
    					StringBuilder sb  = new StringBuilder();
    					sb.append("  "+dr.getRowNo()+"  ");
                		for (DataColumn dc : dr.getColumns()) {
                         	sb.append("  "+dc.getColValue()+"  ");
                        }
                		System.out.println(sb.toString());
    				}
    				
    				@Override
    				public void finish() {
    					System.out.println("*********** 读取完毕 ***********");
    				}
    				
    				@Override
    				public void error(Exception ex) {
    					// TODO Auto-generated method stub
    					
    				}
    			});
    		}
    		//System.out.println("读取行数： "+rows);
    		book.close();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

	
}
