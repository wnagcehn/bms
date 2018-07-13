package com.jiuyescm.excel.util;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.scripting.xmltags.IfSqlNode;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.model.SharedStringsTable;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.apache.xerces.parsers.SAXParser;
import org.xml.sax.InputSource;
import org.xml.sax.Attributes;  
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import com.google.common.collect.Maps;

public class Excel07ReadForAll extends DefaultHandler{

	protected SharedStringsTable sst; 	//共享字符串表   
	protected String lastContents;  	//上一次的内容  
	protected boolean nextIsString;     //当前单元格内容是否为字符串
	protected int curRow = 0;   		//当前行   从第一行开始读取
	protected int curCol = 0;  			//当前列  
	private int readType = 0;  			//读取类型  0-全部读取  1-仅读给定列  2-除给定列外都读取
	
	private Map<Integer, Map<String,String>> contents = Maps.newLinkedHashMap();
	//new HashMap<Integer, Map<String,String>>(); //所有excel内容
	private Map<String, String> headColumn = new HashMap<String, String>(); //表头信息
	private Map<String, String> curCell = new HashMap<String, String>(); 	//单元格内容
	private List<String> effectColumns = new ArrayList<String>();
	
	private Map<String, String> colForRead = new HashMap<String, String>(); 		//仅读这些列
	
	private Map<Integer, String> originColumn = Maps.newLinkedHashMap(); //原生表头 <列索引,列名>
	
	private boolean ifRead = true;
	
	public Map<String, String> getHeadColumn(){
		return headColumn;
	}
	
	public Map<Integer, Map<String, String>> getContents(){
    	return contents;
    }
	
	public Map<Integer, String> getOriginColumn(){
		return originColumn;
	}
	
	
	/**
     * 
     * @param filename 	文件路径
     * @param colMap 	列-属性映射关系
     * @throws Exception
     */
    public void readFirstSheet(String filename,int readType,List<String> columnsForRead) throws Exception {  
    	this.readType = readType;
    	if(readType == 1 || readType == 2){
    		effectColumns = columnsForRead;
    	}
    	
    	OPCPackage pkg = OPCPackage.open(filename);  
    	InputStream sheet2 = null;
    	try{
            XSSFReader r = new XSSFReader(pkg);  
            SharedStringsTable sst = r.getSharedStringsTable();  
            XMLReader parser = fetchSheetParser(sst);  
            sheet2 = r.getSheet("rId"+1); 
            InputSource sheetSource = new InputSource(sheet2);  
            parser.parse(sheetSource); 
    	}
    	catch(Exception ex){
    		throw ex;
    	}
    	finally{
    		if (sheet2!=null) {
    			sheet2.close();  
			}
            pkg.close();
    	}
    }  
	
    private XMLReader fetchSheetParser(SharedStringsTable sst)  throws SAXException {  
    	XMLReader parser = new SAXParser();
        this.sst = sst;  
        parser.setContentHandler(this);  
        return parser;  
    }  
    
    public void startElement(String uri, String localName, String name, Attributes attributes) throws SAXException {  
        // c => 单元格  
        if ("c".equals(name)) {  
            // 如果下一个元素是 SST 的索引，则将nextIsString标记为true  
            String cellType = attributes.getValue("t");  
            curCol = getColIndex(attributes.getValue("r")); //当前列索引
            if(colForRead.size()==0){
            	ifRead = true;
            }
            else{
            	if(colForRead.containsKey(String.valueOf(curCol))){
            		ifRead = true;
            	}
            	else{
            		ifRead = false;
            	}
            }
            if ("s".equals(cellType)) {  
                nextIsString = true;  
            } else {  
                nextIsString = false;  
            }
        } 
        lastContents = "";  // 置空  
    }  
    
    /**
     * 读数据
     */
    public void endElement(String uri, String localName, String name) throws SAXException { 
    	
    	if (nextIsString) {
            try {
            	//如果存储格式是字符串，那么就必须去sharedStrings.xml文件中查找内容
                int idx = Integer.parseInt(lastContents);
                lastContents = new XSSFRichTextString(sst.getEntryAt(idx)).toString();
            } catch (Exception e)  { }
        }   
        if ("v".equals(name)) {
        	if(ifRead){
        		String value = lastContents.trim();
                //value = value.equals("")?" ":value;
        		if(value.equals("")){
        			return;
        		}
        		if(curRow==0){
                    curCell.put(String.valueOf(curCol), value); //将单元格内容添加到单元格列表中
                    originColumn.put(curCol, value);
        		}
        		else{
        			curCell.put(headColumn.get(String.valueOf(curCol)), value);
        		}
        		
        	}
        }
        
        else {
            //如果标签名称为 row ，这说明已到行尾，调用 optRows() 方法 
            if (name.equals("row")) {
                curRow++;
                //System.out.println("第【"+curRow+"】行读取完毕！");
                Map<String, String> colMapTemp = new HashMap<String, String>();
                colMapTemp.putAll(curCell);
                contents.put(curRow, colMapTemp); //将行信息添加至行集合中
                if(curRow == 1){
                	handColForRead(colMapTemp); //如果是第一行  colMapTemp表示表头信息
                }
                curCell.clear();
            }
        }
    }
  
    public void characters(char[] ch, int start, int length) throws SAXException {
        lastContents += new String(ch, start, length);  //得到单元格内容的值  
    }  
    
    /**
     * 获取列号
     * @param rowStr
     * @return
     */
    private int getColIndex(String rowStr){  
         rowStr = rowStr.replaceAll("[^A-Z]", "");  
         byte[] rowAbc = rowStr.getBytes();  
         int len = rowAbc.length;  
         float num = 0;  
         for (int i=0;i<len;i++){  
             num += (rowAbc[i]-'A'+1)*Math.pow(26,len-i-1 );  
         }  
         return (int) num;  
     } 
    
    private void handColForRead(Map<String, String> colMapTemp){
    	if(readType == 2 || readType == 0){
    		colForRead.putAll(colMapTemp);
    	}
    	//colMapTemp - 表头信息
    	for (String colname : effectColumns) {
    		for (Map.Entry<String, String> entry : colMapTemp.entrySet()) { 
    			if(readType == 1){
    				if(entry.getValue().equals(colname)){
						colForRead.put(entry.getKey(), entry.getValue());
	    				break;
	    			 }
    			}
    			else if(readType == 2){
    				if(entry.getValue().equals(colname)){
						colForRead.remove(entry.getKey());
	    				break;
	    			 }
    			}
    			else{
    				colForRead.put(entry.getKey(), entry.getValue());
					break;
    			}
    		}
		}
    	headColumn.putAll(colForRead);
    }
}
