package com.jiuyescm.bms.excel.write;

import java.io.FileOutputStream;
import java.io.IOException;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SXSSFExporter extends ExportBase {

	//内存中缓存记录默认值
	private static final int rowAccessWindowSize = 1000;
	
	private Logger logger = LoggerFactory.getLogger(SXSSFExporter.class);
	
	/**
	 * 初始化SXSSF导出类
	 * @param startIndex 数据内容默认开始写入的行
	 */
	public SXSSFExporter(){
		super();
		workbook = new SXSSFWorkbook(rowAccessWindowSize);
	}
	
	public SXSSFExporter(int windowSize){
		super();
		workbook = new SXSSFWorkbook(windowSize);
	}
	
    
	@Override
    protected String saveFile() throws IOException{
    	FileOutputStream fileOut = null;
    	try{
    		fileOut = new FileOutputStream(exportFilePath);
            workbook.write(fileOut);
            SXSSFWorkbook sxssfWorkbook = (SXSSFWorkbook)workbook;
            sxssfWorkbook.dispose();
        }finally{
            if(fileOut != null){
                fileOut.close();
            }
        }
    	logger.info("export success!");
        return exportFilePath;
    }

}
