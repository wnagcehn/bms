package com.jiuyescm.bms.excel;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.model.SharedStringsTable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

import com.jiuyescm.bms.excel.callback.SheetReadCallBack;
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
    
    public ExcelXlsxReader(String path) throws Exception{
    	sheets = new ArrayList<OpcSheet>();
    	filePath = path;
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
     * 读取sheet内容
     * @param index sheet 索引值
     * @return
     * @throws Exception
     */
    public OpcSheet readRow(int index,SheetReadCallBack callback) throws Exception{
    	OpcSheet sheet = new ExcelXlsxSheetReader().readSheet(index, pkg,xssfReader, sst,callback,null,false);
    	return sheet;
    }
    
    /**
     * 读取sheet内容 并转换为对象
     * @param index
     * @param callback
     * @return
     * @throws Exception
     */
    public <T> OpcSheet readRowToObj(int index,Class<T> clazz,SheetReadCallBack callback) throws Exception{
    	OpcSheet sheet = new ExcelXlsxSheetReader().readSheet(index, pkg,xssfReader, sst,callback,clazz,true);
    	return sheet;
    }
	
}
