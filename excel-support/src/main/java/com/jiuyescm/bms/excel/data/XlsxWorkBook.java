package com.jiuyescm.bms.excel.data;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jiuyescm.bms.excel.callback.SheetReadCallBack;
import com.jiuyescm.bms.excel.common.ZipHelper;
import com.jiuyescm.bms.excel.xmlreader.ShareStringsReader;
import com.jiuyescm.bms.excel.xmlreader.SheetReader;
import com.jiuyescm.bms.excel.xmlreader.WorkBookReader;

public class XlsxWorkBook {
	
	private String tempPath;
	
	private int status = 1;
	private String sheetPath = "xl/worksheets/";
	private List<Sheet> sheets = new ArrayList<Sheet>();
	private List<String> sst = new ArrayList<>();
	
	private Logger logger = LoggerFactory.getLogger(XlsxWorkBook.class);
	
	public List<Sheet> getSheets(){
		return sheets;
	}
	
	/**
	 * 
	 * @param excelPath xl目录所在路径
	 * @throws Exception 
	 */
	public XlsxWorkBook(InputStream inputStream) throws Exception{
		long start = System.currentTimeMillis();
		status = 1;
		logger.info("解压excel文件");
		ZipHelper zipHelper = new ZipHelper(inputStream);
		this.tempPath = zipHelper.getUnzipPath();
		logger.info("解压路径：{} 耗时【{}】",tempPath,System.currentTimeMillis()-start);
		
		
		start = System.currentTimeMillis();
		String workbookFilePath = tempPath+"xl/workbook.xml";
		logger.info("正在读取workbook 路径： {}",workbookFilePath);
		WorkBookReader wbr = new WorkBookReader(workbookFilePath);
		this.sheets = wbr.getSheets();
		logger.info("workbook 读取完毕 sheet数量： {} 耗时【{}】",this.sheets.size(),System.currentTimeMillis()-start);
		
		start = System.currentTimeMillis();
		String shareStringFilePath = tempPath+ "xl/sharedStrings.xml";
		logger.info("正在读取ShareString 路径： {}",shareStringFilePath);
		ShareStringsReader ssr = new ShareStringsReader(shareStringFilePath);
		this.sst = ssr.getSST();
		logger.info("ShareString 读取完毕  string 数量： {} 耗时【{}】",this.sst.size(),System.currentTimeMillis()-start);
	}
	
	public void readSheet(Integer sheetId,SheetReadCallBack callback) throws Exception{
		if(status == 0){
			throw new Exception("workbook is closed");
		}
		read(sheetId,callback,1,2);
	}
	
	public void readSheet(Integer sheetId,SheetReadCallBack callback,int titleRowNo,int contentRowNo ) throws Exception{
		if(status == 0){
			throw new Exception("workbook is closed");
		}
		read(sheetId,callback,titleRowNo,contentRowNo);
	}
	
	private void read(Integer sheetId,SheetReadCallBack callback,int titleRowNo,int contentRowNo){
		SheetReader sr = new SheetReader();
		String sheetFileName = tempPath+sheetPath+"sheet"+sheetId+".xml";
		sr.readSheet(sheetFileName, callback, sst, titleRowNo, contentRowNo);
		//为当前sheet设置总行数
		for (Sheet sheet : sheets) {
			if(sheet.getSheetId()==sheetId){
				sheet.setRowCount(sr.getRowCount());
			}
		}
	}
	
	public void close(){
		sst.clear();
		deleteDir(new File(tempPath));
	}
	
	private boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            //递归删除目录中的子目录下
            for (int i=0; i<children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        // 目录此时为空，可以删除
        return dir.delete();
    }
}
