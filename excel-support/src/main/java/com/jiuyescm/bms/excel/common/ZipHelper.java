package com.jiuyescm.bms.excel.common;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.Enumeration;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.aspectj.apache.bcel.generic.ReturnaddressType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ZipHelper {

	private Logger logger = LoggerFactory.getLogger(ZipHelper.class);
	
	private String xlsxPath;  //copy文件后的路径
	private String unzipPath; //解压后的路径
	private String classPath; //类加载路径
	private String tempPath;  //临时文件根路径
	
	public String getTempPath(){
		return tempPath;
	}
	
	public String getXlsxPath(){
		return xlsxPath;
	}
	
	public String getUnzipPath(){
		return unzipPath;
	}
	
	public ZipHelper(InputStream inputStream) throws Exception{
		
		classPath = this.getClass().getClassLoader().getResource("").getPath();
		tempPath = classPath+"temp/";
		xlsxPath = copyFileToLoal(inputStream);
		unZipFiles(xlsxPath);
		delZipFile();
		
	}
	
	private String copyFileToLoal(InputStream inputStream) throws Exception {
		
    	String localName = UUID.randomUUID().toString();//本地文件名称
    	String filePath = tempPath + localName+ ".zip"; //本地文件路径
    	unzipPath = tempPath+localName+"/";
    	File file = new File(filePath);
    	if(!file.getParentFile().exists()) {
    		file.getParentFile().mkdirs();
    	}
    	logger.info("copy file to local path="+filePath);
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
    		throw new Exception("远程拉取文件到本地异常");
    	}
    	finally {
            try {
            	if(os !=null){
            		os.close();// 完毕，关闭所有链接
            	}
                inputStream.close();
            } catch (IOException e) {
            	logger.error("关闭流异常",e);
            	throw new Exception("关闭流异常");
            }
        }
    	return filePath;
    }
	
	
	
	@SuppressWarnings("rawtypes")
	public void unZipFiles(String zipPath) throws IOException {
		
		File zipFile = new File(zipPath);
		String descDir = zipFile.getParentFile().getPath()+"/"; //解压路径
		ZipFile zip = new ZipFile(zipFile,Charset.forName("GBK"));//解决中文文件夹乱码
		String name = zip.getName().substring(zip.getName().lastIndexOf('\\')+1, zip.getName().lastIndexOf('.'));
		File pathFile = new File(descDir+name);
		if (!pathFile.exists()) {
			pathFile.mkdirs();
		}
		
		for (Enumeration<? extends ZipEntry> entries = zip.entries(); entries.hasMoreElements();) {
			ZipEntry entry = (ZipEntry) entries.nextElement();
			String zipEntryName = entry.getName();
			InputStream in = zip.getInputStream(entry);
			String outPath = (descDir + name +"/"+ zipEntryName).replaceAll("\\*", "/");
			
			// 判断路径是否存在,不存在则创建文件路径
			File file = new File(outPath.substring(0, outPath.lastIndexOf('/')));
			if (!file.exists()) {
				file.mkdirs();
			}
			// 判断文件全路径是否为文件夹,如果是上面已经上传,不需要解压
			if (new File(outPath).isDirectory()) {
				continue;
			}
 
			FileOutputStream out = new FileOutputStream(outPath);
			byte[] buf1 = new byte[1024];
			int len;
			while ((len = in.read(buf1)) > 0) {
				out.write(buf1, 0, len);
			}
			in.close();
			out.close();
		}
		zip.close();
		System.out.println("******************解压完毕********************");
		return;
	}
	
	private void delZipFile(){
    	File file = new File(xlsxPath); 
    	if (file.exists() && file.isFile()) {  
			if (file.delete()) {  
				logger.info("本地文件删除成功");
            } else {  
            	logger.info("本地文件删除失败");
            }  
		}
    }
	    
	
}
