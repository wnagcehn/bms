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
        	String path = "E:/user/desktop/liuzhicheng/Desktop/1.xlsx";
    		File file = new File(path);
            FileInputStream inputStream = new FileInputStream(file);
    		
            XlsxWorkBook book = new XlsxWorkBook(inputStream);
            for (Sheet sheet : book.getSheets()) {
    			System.out.println("sheet名称【"+sheet.getSheetName()+"】 id【"+sheet.getSheetId()+"】");
    			book.readSheet(sheet.getSheetId(), new SheetReadCallBack() {
    				
    				@Override
    				public void readTitle(List<String> columns) {
    					StringBuilder sb  = new StringBuilder();
    					sb.append("\t行号\t");
    					for (String string : columns) {
    						sb.append("\t"+string+"\t");
    					}
    					System.out.println(sb.toString());
    				}
    				
    				@Override
    				public void read(DataRow dr) {
    					StringBuilder sb  = new StringBuilder();
    					sb.append("\t"+dr.getRowNo()+"\t");
                		for (DataColumn dc : dr.getColumns()) {
                         	sb.append("\t"+dc.getColValue()+"\t");
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
    		book.close();
    		/*while(true){
    			Thread.sleep(1000);
    		}*/
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

	
}
