package com.jiuyescm.excel.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang3.time.DateUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;

import com.google.common.collect.Maps;
import com.jiuyescm.bs.util.StringUtil;
import com.jiuyescm.cfm.common.JAppContext;

public class Excel07Reader{
	
	private static final Logger logger = Logger.getLogger(Excel07Reader.class.getName());

	private String filePath = ""; 	//本地Excel路径
    private int rowCount = 0;		//Excel行数
    private Map<Integer, Map<String,String>> contents = new HashMap<Integer, Map<String,String>>(); //Excel内容
    
    private List<String> colForRead = new ArrayList<String>(); 		//仅读这些列
    private int readType = 0;  //读取类型  0-全部读取  1-仅读给定列  2-除给定列外都读取
    private Map<String, String> headColumn = new HashMap<String, String>(); //表头信息
    private Map<Integer, String> originColumn = Maps.newLinkedHashMap(); //原生表头 <列索引,列名>
    
    private String msg = "";
    
    /**
     * 读取Excel行数 不包含标题行
     * @return
     */
    public int getRowCount(){
    	return rowCount;
    }
    
    /**
     * 读取excel列数
     * @return
     */
    public int getColCount(){
    	return this.headColumn.size();
    }
    
    /**
     * 读取文件内容 不包含标题行
     * @return
     */
    public Map<Integer, Map<String, String>> getContents(){
    	return contents;
    }
    
    public String getMsg() {
		return msg;
	}
    
    public Map<String, String> getHeadColumn(){
    	return headColumn;
    }
    
    public Map<Integer, String> getOriginColumn(){
		return originColumn;
	}
    
    public Excel07Reader(){
    	
    }
	
    
    /**
     * 
     * @param inputStream 
     * @param readType 		//读取类型  0-全部读取  1-仅读给定列  2-除给定列外都读取
     * @param colForRead	
     * readType = 0   colForRead可以为null，将读取多有数据
     * readType = 1   colForRead不能为空，将读取指定的列
     * readType = 2   colForRead不能为空，将读取出指定列之外，所有的列
     * @throws Exception
     */
    public Excel07Reader(InputStream inputStream,int readType,List<String> colForRead) throws Exception{
    	this.readType = readType;
    	this.colForRead = colForRead;
    	read(inputStream);
    }
    
    private void read(InputStream inputStream) throws Exception{

    	if(readType > 0){
    		if(colForRead == null || colForRead.size() == 0){
    			throw new Exception("指定读取的列不能为空！");
    		}
    	}
    	copyFileToLoal(inputStream); //复制远程文件到本地
    	try{
    		Excel07ReadForAll read = new Excel07ReadForAll();
        	read.readFirstSheet(filePath, readType, colForRead);
        	this.contents = read.getContents();
        	this.contents.remove(1);
        	this.rowCount = this.contents.size();
        	originColumn = read.getOriginColumn();
        	Map<String, String> headMap = read.getHeadColumn();
        	for (Map.Entry<String, String> entry : headMap.entrySet()) { 
        		this.headColumn.put(entry.getValue(), entry.getKey());
        	}
        	delLocalFile();
    	}
    	catch(Exception ex){
    		logger.error("excel读取异常",ex);
    		delLocalFile();
    		throw ex;
    	}
    }
    
    /**
     * 复制远程文件到本地
     * @param inputStream
     */
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
    
    /**
     * 删除本地文件
     */
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

    /**
     * 将excel中的内容转换为timestamp类型
     * @param value
     * @return
     * @throws ParseException 
     */
    public Timestamp changeValueToTimestamp(String value) throws ParseException{
    	if(StringUtil.isNumeric(value)){
    		Date date = HSSFDateUtil.getJavaDate(Double.valueOf(value));
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            value = dateFormat.format(date);
    	}
    	String[] dataPatterns = new String[] { "yyyy-MM-dd hh:mm:ss", "yyyy-MM-dd", "dd-MM月 -yy","yyyy/MM/dd","yyyy/MM/dd HH:mm:ss","yyyy年MM月dd日" };
		Date date = DateUtils.parseDate(value, dataPatterns);
		Timestamp ts=new Timestamp(date.getTime());
		return ts;
    }
	
	
}
