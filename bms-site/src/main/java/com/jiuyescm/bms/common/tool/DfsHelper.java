package com.jiuyescm.bms.common.tool;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bstek.dorado.uploader.DownloadFile;
import com.bstek.dorado.uploader.annotation.FileProvider;
import com.jiuyescm.bms.file.templet.BmsTempletInfoEntity;
import com.jiuyescm.bms.file.templet.IBmsTempletInfoService;
import com.jiuyescm.framework.fastdfs.client.StorageClient;
import com.jiuyescm.framework.fastdfs.protocol.storage.callback.DownloadByteArray;

@Component("dfsHelper")
public class DfsHelper {

	@Autowired private StorageClient storageClient;
	
	@Resource private IBmsTempletInfoService bmsTempletInfoService;
	
	/**
	 * 模板下载
	 * @param parameter
	 * @return
	 * @throws IOException
	 */
	@FileProvider
	public DownloadFile downTempletExcel(Map<String, String> parameter) throws IOException {
		
		BmsTempletInfoEntity bmsTempletInfoEntity=null;
		if(parameter!=null){
			bmsTempletInfoEntity=bmsTempletInfoService.findByCode(parameter.get("templetCode"));
		
			final String fileName=bmsTempletInfoEntity.getExcelName();
			byte[] bytes=storageClient.downloadFile(bmsTempletInfoEntity.getUrl(),new DownloadByteArray());
			try{
				 XSSFWorkbook xssfWorkbook = new XSSFWorkbook(new ByteArrayInputStream(bytes));
				 xssfWorkbook.removeSheetAt(2);
				 ByteArrayOutputStream os = new ByteArrayOutputStream();
				 xssfWorkbook.write(os);
				 byte[] b1 = os.toByteArray();
				 ByteArrayInputStream newinputStream=new ByteArrayInputStream(b1);
				 return new DownloadFile(fileName, newinputStream);
				
			}
			catch(Exception ex){
				return new DownloadFile(fileName, new ByteArrayInputStream(bytes));
			}
		}
		return null;
	}
	
}
