package com.jiuyescm.bms.excel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

import org.apache.xmlbeans.impl.xb.xsdschema.Public;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jiuyescm.bms.excel.ExcelXlsxReader;
import com.jiuyescm.bms.excel.callback.SheetReadCallBack;
import com.jiuyescm.bms.excel.data.DataColumn;
import com.jiuyescm.bms.excel.data.DataRow;
import com.jiuyescm.bms.excel.opc.OpcSheet;
import com.jiuyescm.bms.excel.utils.DateUtils;


public class Test {

	public static void main(String[] args) throws FileNotFoundException {
		
		test2();	
		//System.out.println(trans("AD"));
		
	}


	
    public static void test2() throws FileNotFoundException {
        String path = "D:\\demo-test\\file\\123.xlsx";
        File file = new File(path);
        FileInputStream inputStream = new FileInputStream(file);
        ExcelXlsxReader er;
        try {
            er = new ExcelXlsxReader(inputStream);
            List<OpcSheet> sheets = er.getSheets();
            er.readRow(1, new SheetReadCallBack() {
                @Override
                public void read(DataRow dr) {
                   /* System.out.println("----行号【" + dr.getRowNo() + "】");
                    for (DataColumn dc : dr.getColumns()) {

                        System.out.println("列名【" + dc.getColName() + "】|值【"
                                + dc.getColValue() + "】");
                    }*/
                }
                @Override
                public void finish() {
                    //System.out.println("读取完毕");
                }
                @Override
                public void readTitle(List<String> columns) {
                    //System.out.println(columns);
                }
            },3,5);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

	
}
