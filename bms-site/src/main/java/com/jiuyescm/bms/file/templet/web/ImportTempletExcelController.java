package com.jiuyescm.bms.file.templet.web;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.annotation.DataResolver;
import com.bstek.dorado.data.entity.EntityState;
import com.bstek.dorado.data.entity.EntityUtils;
import com.bstek.dorado.data.provider.Page;
import com.bstek.dorado.uploader.DownloadFile;
import com.bstek.dorado.uploader.UploadFile;
import com.bstek.dorado.uploader.annotation.FileProvider;
import com.bstek.dorado.uploader.annotation.FileResolver;
import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.file.templet.BmsTempletInfoEntity;
import com.jiuyescm.bms.file.templet.IBmsTempletInfoService;
import com.jiuyescm.cfm.common.JAppContext;
import com.jiuyescm.framework.fastdfs.client.StorageClient;
import com.jiuyescm.framework.fastdfs.model.StorePath;
import com.jiuyescm.framework.fastdfs.protocol.storage.callback.DownloadByteArray;

@Controller("importTempletExcelController")
public class ImportTempletExcelController {

	@Autowired private StorageClient storageClient;
	@Autowired private IBmsTempletInfoService bmsTempletInfoService;
	
	private static final Logger logger = Logger.getLogger(ImportTempletExcelController.class.getName());
	
	@DataProvider  
	public void queryAll(Page<BmsTempletInfoEntity> page,Map<String,Object> parameter){
		
		PageInfo<BmsTempletInfoEntity> tmpPageInfo = bmsTempletInfoService.queryPage(parameter, page.getPageNo(),page.getPageSize());
		if (tmpPageInfo != null) {
			page.setEntities(tmpPageInfo.getList());
			page.setEntityCount((int) tmpPageInfo.getTotal());
		}
	}
	
	/**
	 * 保存数据
	 * @param datas
	 */
	@DataResolver
	public String saveAll(Collection<BmsTempletInfoEntity> datas){
		
		try {
			Timestamp nowdate = JAppContext.currentTimestamp();
			String userid=JAppContext.currentUserName();
			for(BmsTempletInfoEntity temp:datas){
				if(EntityState.NEW.equals(EntityUtils.getState(temp))){
					//****** 新增模板 ******
					temp.setDelFlag("0");
					temp.setCreator(userid);
					temp.setCreateTime(nowdate);
					bmsTempletInfoService.save(temp);
					
				}else if(EntityState.MODIFIED.equals(EntityUtils.getState(temp))){
					//****** 修改模板 ******
					temp.setLastModifier(userid);
					temp.setLastModifyTime(nowdate);
					bmsTempletInfoService.update(temp);
				}
			}
			return "SUCCESS";
			
		} catch (Exception e) {
			logger.error("【程序异常】",e);
			return "【ERROR】"+e.getMessage();
		}
	}
	
	
	@DataResolver
	public String delete(BmsTempletInfoEntity entity){
		try {
			Timestamp nowdate = JAppContext.currentTimestamp();
			String userid=JAppContext.currentUserName();
			entity.setDelFlag("1");
			entity.setLastModifier(userid);
			entity.setLastModifyTime(nowdate);
			bmsTempletInfoService.update(entity);
			return "作废成功！";
			
		} catch (Exception e) {
			logger.error("【程序异常】", e);
			return "【后台执行异常】"+e.getMessage();
		}
	}
	
	/**
	 * 模板类型  下载模板  导出模板
	 * @return
	 */
	@DataProvider
	public Map<String, String> getTempletType() {
		Map<String, String> map = new LinkedHashMap<String, String>();
		map.put("ALL", "全部");
		map.put("下载模板", "下载模板");
		map.put("导出模板", "导出模板");
		map.put("其他模板", "其他模板");
		return map;
	}
	
	/**
	 * 业务类型  仓储，干线，配送,其他
	 * @return
	 */
	@DataProvider
	public Map<String, String> getTempletBizType() {
		Map<String, String> map = new LinkedHashMap<String, String>();
		map.put("ALL", "全部");
		map.put("仓储", "仓储");
		map.put("干线", "干线");
		map.put("配送", "配送");
		map.put("其他", "其他");
		return map;
	}
	
	
	
	@FileResolver
	public String uploadTempletForExcel(final UploadFile file, Map<String, Object> parameter) throws IOException {
		
		// Map<String, Object> retmap=new HashMap<String, Object>();
		 BmsTempletInfoEntity entity = new BmsTempletInfoEntity();
		 try{
			 XSSFWorkbook xssfWorkbook = new XSSFWorkbook(file.getInputStream());
		/*	 if(xssfWorkbook.getNumberOfSheets()!=3){
				 return "标准模板 有且只能有3个sheet，请检查";
			 }
			 if(!"业务数据".equals(xssfWorkbook.getSheetAt(0).getSheetName())){
				 return "标准模板 第一个sheet名称必须为【业务数据】";
			 }
			 if(!"导入说明".equals(xssfWorkbook.getSheetAt(1).getSheetName())){
				 return "标准模板 第二个sheet名称必须为【导入说明】";
			 }
			 if(!"配置".equals(xssfWorkbook.getSheetAt(2).getSheetName())){
				 return "标准模板 第三个sheet名称必须为【配置】";
			 }*/
			 long length = file.getSize();
			 StorePath storePath = storageClient.uploadFile(file.getInputStream(), length, "xlsx");
			 String fullPath = storePath.getFullPath();
			 entity.setLastModifier(JAppContext.currentUserName());
			 entity.setLastModifyTime(JAppContext.currentTimestamp());
			 entity.setUrl(fullPath);
			 entity.setExcelName(file.getFileName());
			 if(parameter.containsKey("id")){
				 String id=parameter.get("id").toString();
				 entity.setId(Integer.parseInt(id));
			 }
			 else{
				 return "【上传失败】--未选择模板！";
			 }
			 bmsTempletInfoService.update(entity);
			 return "上传成功";
		 }
		 catch(Exception ex){
			 logger.error("【程序异常】",ex);
			 return "上传失败" + ex.getMessage();
		 }
		
		
		
		/*storageClient.downloadFile(fullPath, new DownloadCallback<InputStream>(){
			@Override
		    public InputStream receive(InputStream inputStream) throws IOException {
				XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
				POIUtil poiUtil = new POIUtil();
				HSSFWorkbook hssfWorkbook = poiUtil.getHSSFWorkbook();
				
				XSSFSheet sheet = workbook.getSheetAt(0);
				Row row = sheet.getRow(0);
				int cellIndex =0;
				for (Cell c : row) {
					String returnStr = c.getStringCellValue();
					System.out.println(returnStr);
					cellIndex++;
				}
		        return inputStream;
		    }
		});
		return null;*/
	}
	
	@FileProvider
	public DownloadFile downTempletExcelImport(Map<String, String> parameter) throws IOException {
		
		final String fileName=parameter.get("excelName");
		byte[] bytes=storageClient.downloadFile(parameter.get("url"),new DownloadByteArray());
		try{
			XSSFWorkbook xssfWorkbook = new XSSFWorkbook(new ByteArrayInputStream(bytes));
/*			xssfWorkbook.removeSheetAt(2);
*/			ByteArrayOutputStream os = new ByteArrayOutputStream();
			xssfWorkbook.write(os);
			byte[] b1 = os.toByteArray();
			return new DownloadFile(fileName, new ByteArrayInputStream(b1));
		}
		catch(Exception ex){
			logger.error("服务器模板格式不正确", ex);
			return new DownloadFile(fileName, new ByteArrayInputStream(bytes));
		}
	}
	
	private void excelToServer(){
		/*POIUtil poiUtil = new POIUtil();
    	HSSFWorkbook hssfWorkbook = poiUtil.getHSSFWorkbook();
    	this.appendExcelSheet(poiUtil,hssfWorkbook,path+File.separator+voEntity.getTitleName()+".xls",voEntity);
    	ByteArrayOutputStream os = new ByteArrayOutputStream();
    	hssfWorkbook.write(os);
    	byte[] b = os.toByteArray();
    	ByteArrayInputStream is = new ByteArrayInputStream(b);
		return new DownloadFile("cjw.xls", is);*/
	}
	
	
	

}
