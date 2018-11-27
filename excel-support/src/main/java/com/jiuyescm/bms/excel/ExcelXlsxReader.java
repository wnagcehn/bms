package com.jiuyescm.bms.excel;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.model.SharedStringsTable;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

import com.jiuyescm.bms.excel.callback.SheetReadCallBack;
import com.jiuyescm.bms.excel.data.DataRow;
import com.jiuyescm.bms.excel.opc.ExcelXlsxSheetReader;
import com.jiuyescm.bms.excel.opc.OpcSheet;

/**
 * 
 * @author caojianwei
 *
 * @param <T>
 */
public class ExcelXlsxReader extends DefaultHandler{
	
	private static Logger logger = LoggerFactory.getLogger(ExcelXlsxReader.class);
	
	private String filePath;			//本地文件所在路径
    private SharedStringsTable sst;		//共享字符串表
    
    
    private Map<Integer, String> rowMap = new HashMap<Integer, String>();
    
    public Map<Integer, String> getRowMap(){
    	return rowMap;
    }
    
    private OPCPackage pkg;
    private XMLReader parser;
    private XSSFReader xssfReader;
    private List<OpcSheet> sheets;
    
    public void close() throws IOException{
    	pkg.close();
    	delLocalFile();
    }
    
    public ExcelXlsxReader(InputStream inputStream) throws Exception{
    	copyFileToLoal(inputStream);
    	read();
    }

	private void copyFileToLoal(InputStream inputStream)  throws Exception {
    	String localName = UUID.randomUUID().toString();//本地文件名称
    	filePath = this.getClass().getClassLoader().getResource("").getPath() +localName+ ".xlsx"; //本地文件路径
    	logger.info(filePath);
    	OutputStream os = null;
    	try{
    		os = new FileOutputStream(filePath);
        	byte[] bs = new byte[1024];
        	int len;
        	while ((len = inputStream.read(bs)) != -1) {
                os.write(bs, 0, len);
            }
    	}
    	catch(Exception ex){
    		logger.error("远程拉取文件到本地异常",ex);
    		throw ex;
    	}
    	finally {
            try {
            	if(os !=null){
            		os.close();// 完毕，关闭所有链接
            	}
                inputStream.close();
            } catch (IOException e) {
            	logger.error("关闭流异常",e);
            	throw e;
            }
        }
    }
    
    private void delLocalFile(){
    	File file = new File(filePath); 
    	if (file.exists() && file.isFile()) {  
			if (file.delete()) {  
				logger.info("本地文件删除成功");
            } else {  
            	logger.info("本地文件删除失败");
            }  
		}
    }
    
    /*public ExcelXlsxReader(String path) throws Exception{
    	filePath = path;
    	read();
    }*/
    
    private void read() throws Exception{
    	sheets = new ArrayList<OpcSheet>();
    	pkg = OPCPackage.open(filePath);
        xssfReader = new XSSFReader(pkg);
        sst = xssfReader.getSharedStringsTable();  
        parser = XMLReaderFactory.createXMLReader("org.apache.xerces.parsers.SAXParser");
        parser.setContentHandler(this);
        XSSFReader.SheetIterator sheetsIterator = (XSSFReader.SheetIterator) xssfReader.getSheetsData();
        int sheetId = 0;
        while (sheetsIterator.hasNext()) { //遍历sheet
        	sheetId++;
            InputStream sheetStream = sheetsIterator.next(); //sheets.next()和sheets.getSheetName()不能换位置，否则sheetName报错
            String sheetName = sheetsIterator.getSheetName();
            OpcSheet opcSheet = new OpcSheet(sheetId, sheetName);
            sheets.add(opcSheet);
            sheetStream.close();
        }
    }
    
    
    
    public List<OpcSheet> getSheets(){
    	return sheets;
    }
    
    /**
     * 读取sheet内容 表格title行默认在第一行 内容默认从第二行开始
     * @param index sheet 索引值
     * @param callback
     * @throws Exception
     */
    public void readRow(int index,SheetReadCallBack callback) throws Exception{
    	new ExcelXlsxSheetReader().readSheet(index, pkg,xssfReader, sst,callback,1,2);
    }
    
    /**
     * 读取sheet内容 表格title行默认在第一行 内容默认从第二行开始
     * titleRowNo:表头结束行 contentRowNo：内容开始行
     * @param index sheet 索引值
     * @param callback
     * @throws Exception
     */
    public void readRow(int index,SheetReadCallBack callback,int titleRowNo,int contentRowNo) throws Exception{
    	new ExcelXlsxSheetReader().readSheet(index, pkg,xssfReader, sst,callback,titleRowNo,contentRowNo);
    }
    
    /**
     * 读取sheet内容 并转换为对象
     * @param index
     * @param callback
     * @return
     * @throws Exception
     */
    private <T> void readRowToObj(int index,Class<T> clazz,SheetReadCallBack callback) throws Exception{
    	//new ExcelXlsxSheetReader().readSheet(index, pkg,xssfReader, sst,callback);
    }
    
    
	
}
