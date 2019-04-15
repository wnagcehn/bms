package com.jiuyescm.common.utils;

import java.io.File;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.h2.store.fs.FileUtils;

import com.bstek.dorado.uploader.UploadFile;
import com.jiuyescm.common.utils.excel.FileReaderFactory;
import com.jiuyescm.common.utils.excel.IFileReader;
import com.jiuyescm.common.utils.upload.BaseDataType;
import com.jiuyescm.common.utils.upload.DataProperty;

public class FileOperationUtil {
	/**
	 * 检查Excel模板
	 * @param file
	 * @return
	 */
	public static boolean checkExcelTitle(UploadFile file,BaseDataType ddt) {
		String fileSuffix =StringUtils.substringAfterLast(file.getFileName(), ".");
		IFileReader reader=FileReaderFactory.getFileReader(fileSuffix);
		if (null == reader)
			return false;
		try {
			List<Map<String,String>> list=reader.getExcelTitle(file.getInputStream());
			List<DataProperty> props=ddt.getDataProps();

			for(Map<String,String> map:list){
				for(String key:map.keySet()){
					if(StringUtils.isBlank(key)){
						continue;
					}
					boolean f=false;
					for(DataProperty prop:props){
						if(prop.getPropertyName().equals(key)){
							f=true;
							break;
						}
					}
					if(!f){
						return f;
					}
				}
			}
			/*
			for(DataProperty prop:props){
				String modelName=prop.getPropertyName().toLowerCase();
				for (Map<String, String> map : list) {
					String colName=map.get(modelName);
					if(StringUtils.isEmpty(colName)){
						return false;
					}
				}
			}*/
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}
	
	public static File getDestFile(String fileName, File folder) {
		File destFile;
		// 若文件不存在直接返回
		if (!new File(folder, fileName).exists()) {
			destFile = new File(folder, fileName);
		} else {
			int i = 1, lastDotPos = fileName.lastIndexOf("."), len = fileName.length();
			String prefix = null, suffix = null, destFileName;
			// 文件名中不存在"."或者最后一位是"."的时候
			if (lastDotPos == len - 1 || lastDotPos == -1) {
				prefix = fileName;
				suffix = "";
			} /*else if (lastDotPos != -1) {
				prefix = fileName.substring(0, lastDotPos);
				suffix = fileName.substring(lastDotPos + 1);
			}*/
			do {
				if (suffix.length() == 0) {
					destFileName = String.format("%s_%d", prefix, i++);
				} else {
					destFileName = String.format("%s_%d.%s", prefix, i++,
							suffix);
				}
				destFile = new File(folder, destFileName);
			} while (destFile.exists());
		}
		FileUtils.createDirectory(destFile.getParent());
		return destFile;
	}
	
	public static long getOperationTime(long starTime){
		long endTime=System.currentTimeMillis();//结束时间
		long time=(endTime-starTime)/1000;
		return time;
	}
}
