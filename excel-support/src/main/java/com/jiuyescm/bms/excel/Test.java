package com.jiuyescm.bms.excel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;




import com.jiuyescm.bms.excel.ExcelXlsxReader;
import com.jiuyescm.bms.excel.callback.SheetReadCallBack;
import com.jiuyescm.bms.excel.data.DataColumn;
import com.jiuyescm.bms.excel.data.DataRow;
import com.jiuyescm.bms.excel.opc.OpcSheet;


public class Test {

	public static void main(String[] args) throws FileNotFoundException {
		
		//System.out.println("123");
		test2();	
		//System.out.println(trans("AD"));
		
	}


	
    public static void test2() throws FileNotFoundException {
        String path = "E:\\test.xlsx";
        File file = new File(path);
        FileInputStream inputStream = new FileInputStream(file);
        ExcelXlsxReader er;
        try {
            er = new ExcelXlsxReader(inputStream);
            List<OpcSheet> sheets = er.getSheets();
            for (OpcSheet opcSheet : sheets) {
            	int startRow = 1;
            	int contentRow = 2;
            	if(opcSheet.getSheetName().contains("仓")){
            		startRow = 3;
            		contentRow = 5;
            	}
            	System.out.println("");
            	System.out.println("");
            	System.out.println("");
            	System.out.println("");
            	System.out.println("");
            	System.out.println("----------- "+opcSheet.getSheetName()+" -----------");
            	er.readRow(opcSheet.getSheetId(), new SheetReadCallBack() {
                    @Override
                    public void read(DataRow dr) {
                    	StringBuilder sb  = new StringBuilder();
                    	if(dr.getRowNo()==3){
                    		 sb.append("  "+"行号"+"  ");
                             for (DataColumn dc : dr.getColumns()) {
                             	sb.append("  "+dc.getColName()+"  ");
                             }
                    	}
                    	else{
                    		sb.append("  "+dr.getRowNo()+"  ");
                    		for (DataColumn dc : dr.getColumns()) {
                             	sb.append("  "+dc.getColValue()+"  ");
                             }
                    		
                    	}
                    	
                       
                        System.out.println(sb.toString());
                    }
                    @Override
                    public void finish() {
                        System.out.println("读取完毕");
                    }
                    @Override
                    public void readTitle(List<String> columns) {
                        System.out.println("readTitle:"+columns);
                    }
					@Override
					public void error(Exception ex) {
						// TODO Auto-generated method stub
						
					}
                },startRow,contentRow);
			}
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

	
}
