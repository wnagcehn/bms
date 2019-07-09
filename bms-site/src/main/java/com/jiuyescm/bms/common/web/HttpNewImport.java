package com.jiuyescm.bms.common.web;

import java.io.ByteArrayInputStream;
import java.lang.reflect.ParameterizedType;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.xssf.usermodel.XSSFFormulaEvaluator;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;

import com.bstek.dorado.uploader.UploadFile;
import com.bstek.dorado.web.DoradoContext;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.jiuyescm.bms.common.entity.ErrorMessageVo;
import com.jiuyescm.bms.file.templet.BmsTempletInfoEntity;
import com.jiuyescm.bms.file.templet.IBmsTempletInfoService;
import com.jiuyescm.common.ConstantInterface;
import com.jiuyescm.common.utils.BeanToMapUtil;
import com.jiuyescm.common.utils.excel.FileReaderFactory;
import com.jiuyescm.common.utils.excel.IFileReader;
import com.jiuyescm.common.utils.upload.BaseDataType;
import com.jiuyescm.common.utils.upload.DataProperty;
import com.jiuyescm.framework.fastdfs.client.StorageClient;
import com.jiuyescm.framework.fastdfs.protocol.storage.callback.DownloadByteArray;

public abstract class HttpNewImport<T,U> {
	
	private static final Logger logger = Logger.getLogger(HttpNewImport.class.getName());

	
	@Autowired private StorageClient storageClient;
	@Autowired private IBmsTempletInfoService bmsTempletInfoService;
	/**
	 * 获取模板编号
	 * @return
	 */
	protected abstract String getTemplateCode();
	/**
	 * 获取导入进度条的Session ID
	 * @return
	 */
	protected abstract String getSessionId();
	/**
	 * 验证 导入excel
	 * @param importList excel表格中实体数据
	 * @param infoList excel 导入验证数据错误信息
	 * @return 转换后实体数据
	 */
	protected abstract List<U> validateImportList(List<T> importList,List<ErrorMessageVo> infoList);
	/**
	 * 批量保存数据
	 * @param list 转换后实体数据
	 * @return 错误/正常  数据消息
	 * @throws Exception 
	 */
	protected abstract void saveDataBatch(List<U> list) throws Exception;
	
	private String getFullPath(){
		String templetCode=getTemplateCode();
		BmsTempletInfoEntity entity=bmsTempletInfoService.findByCode(templetCode);
		return entity.getUrl();
	}
	private List<T> validateTemplate(UploadFile file) throws Exception{
		try{
			List<T> importList=Lists.newArrayList();
			String fullPath=getFullPath();
			byte[] bytes=storageClient.downloadFile(fullPath,new DownloadByteArray());
			ByteArrayInputStream inputStream=new ByteArrayInputStream(bytes);
			XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
			XSSFFormulaEvaluator  evaluator = new XSSFFormulaEvaluator(workbook); 
		
			XSSFSheet sheet = workbook.getSheetAt(2);
			Map<String,String> mapHead=Maps.newHashMap();
			int headCount=sheet.getLastRowNum();
			for(int rowNum = 1;  rowNum <= sheet.getLastRowNum(); rowNum++){
				mapHead.put(getValue(sheet.getRow(rowNum).getCell(0),evaluator), getValue(sheet.getRow(rowNum).getCell(1),evaluator));
			}
			inputStream.close();
			
			workbook = new XSSFWorkbook(file.getInputStream());
			evaluator = new XSSFFormulaEvaluator(workbook); 
			sheet = workbook.getSheetAt(0);
			XSSFRow xssfRowHead=sheet.getRow(0);
		    int lastColNum=xssfRowHead.getPhysicalNumberOfCells();
			List<Map<String, Object>> datalist=new ArrayList<Map<String, Object>>();
			List<String> importHeadList=new ArrayList<String>();
			for(int colNum=0;colNum<lastColNum;colNum++){		
				String colName=getValue(xssfRowHead.getCell(colNum),evaluator);
				importHeadList.add(colName);
			}
			for(int rowNum = 1;  rowNum <= sheet.getLastRowNum(); rowNum++){
				Map<String, Object> map=Maps.newHashMap();
				for(int colNum=0;colNum<=lastColNum;colNum++){		
					String colName=getValue(xssfRowHead.getCell(colNum),evaluator);
					if(mapHead.containsKey(colName)){
						map.put(mapHead.get(colName),getValue(sheet.getRow(rowNum).getCell(colNum),evaluator));
					}
				}
				datalist.add(map);
			}
			if(headCount!=importHeadList.size()){//模板与实体对应关系列数不一致
				/*	throw new Exception("Excel 模板列数不匹配");*/
				throw new Exception("Excel导入格式错误请参考标准模板检查!");				
			}else{
				for(Map.Entry<String,String> entry:mapHead.entrySet()){
					if(!importHeadList.contains(entry.getKey())){
						throw new Exception("Excel 格式不正确,未匹配到列名【"+entry.getKey()+"】");
					}
				}
				importList=readExcel(datalist);
				return importList;
			}
			
		}catch(Exception e){
			throw e;
		}
	}
	
	private String getValue(Cell cell,FormulaEvaluator evaluator) {   
	          
	        String value = "";  
	        if(cell==null)
	        	return value;
	        switch (cell.getCellType()) {  
	            case HSSFCell.CELL_TYPE_NUMERIC:                        //数值型  
	                if (HSSFDateUtil.isCellDateFormatted(cell)) {       //如果是时间类型  
	                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
	                    value = format.format(cell.getDateCellValue());  
	                } else { 
	                	cell.setCellType(Cell.CELL_TYPE_STRING);  
	                	String temp = cell.getStringCellValue();  
						if(temp.indexOf(".")>-1){  
							value = String.valueOf(new Double(temp)).trim();  
						}else{  
							value = temp.trim();  
						}  
	                	 //纯数字  
	                    value = cell.toString();
	                }  
	                break;  
	            case HSSFCell.CELL_TYPE_STRING:                         //字符串型  
	                value = cell.getStringCellValue();  
	                break;  
	            case HSSFCell.CELL_TYPE_BOOLEAN:                        //布尔  
	                value = " " + cell.getBooleanCellValue();  
	                break;  
	            case HSSFCell.CELL_TYPE_BLANK:                          //空值  
	                value = "";  
	                break;  
	            case HSSFCell.CELL_TYPE_ERROR:                          //故障  
	                value = "";  
	                break;  
	            case HSSFCell.CELL_TYPE_FORMULA:                        //公式型  
	                try {  
	                    CellValue cellValue;  
	                    cellValue = evaluator.evaluate(cell);  
	                    switch (cellValue.getCellType()) {              //判断公式类型  
	                        case Cell.CELL_TYPE_BOOLEAN:  
	                            value  = String.valueOf(cellValue.getBooleanValue());  
	                            break;  
	                        case Cell.CELL_TYPE_NUMERIC:  
	                            // 处理日期    
	                            if (DateUtil.isCellDateFormatted(cell)) {    
	                               SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");    
	                               Date date = cell.getDateCellValue();    
	                               value = format.format(date);  
	                            } else {    
	                               value  = String.valueOf(cellValue.getNumberValue());  
	                            }  
	                            break;  
	                        case Cell.CELL_TYPE_STRING:  
	                            value  = cellValue.getStringValue();  
	                            break;  
	                        case Cell.CELL_TYPE_BLANK:  
	                            value = "";  
	                            break;  
	                        case Cell.CELL_TYPE_ERROR:  
	                            value = "";  
	                            break;  
	                        case Cell.CELL_TYPE_FORMULA:  
	                            value = "";  
	                            break;  
	                    }  
	                } catch (Exception e) {  
	                    value = cell.getStringCellValue().toString();  
	                    cell.getCellFormula(); 
	                    logger.error(e);
	                }  
	                break;  
	            default:  
	                value = cell.getStringCellValue().toString();  
	                break;  
	        }  
	        return value;  
	    }
	//protected String 
	public Map<String,Object> importFile(UploadFile file){
		String sessionId=getSessionId();
		DoradoContext.getAttachedRequest().getSession().setAttribute(sessionId, 0);//开始处理
		Map<String, Object> map = new HashMap<String, Object>();
		
		List<ErrorMessageVo> infoList = new ArrayList<ErrorMessageVo>();// 校验信息（报错提示）
		ErrorMessageVo errorVo = null;
		try{

			DoradoContext.getAttachedRequest().getSession().setAttribute(sessionId, 1);//验证模板
			List<T> importList=validateTemplate(file);
			DoradoContext.getAttachedRequest().getSession().setAttribute(sessionId, 3);//读取Excel
			//DoradoContext.getAttachedRequest().getSession().setAttribute(sessionId, 3);//开始验证数据
			List<U> list=validateImportList(importList,infoList);//验证导入书库
			if(list==null||list.size()==0){
				if(infoList.size()==0){
					errorVo = new ErrorMessageVo();
					errorVo.setLineNo(1);
					errorVo.setMsg("无数据需要保存!");
					infoList.add(errorVo);
				}
			}
			if(infoList.size()>0){
				map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
				DoradoContext.getAttachedRequest().getSession().setAttribute(sessionId, 6);//异常结束
				return map;
			}
			DoradoContext.getAttachedRequest().getSession().setAttribute(sessionId, 4);//开始保存数据
			saveDataBatch(list);//批量保存数据
			DoradoContext.getAttachedRequest().getSession().setAttribute(sessionId, 5);//保存结束
			map.put(ConstantInterface.ImportExcelStatus.IMP_SUCC, list);
			
			/*long length = file.getSize();
			StorePath storePath = storageClient.uploadFile(file.getInputStream(), length, "xlsx");
			String fullPath = storePath.getFullPath();
			map.put("url", fullPath);*/
			return map;
		}catch(Exception e){
			DoradoContext.getAttachedRequest().getSession().setAttribute(sessionId, 6);//异常结束
			errorVo = new ErrorMessageVo();
			errorVo.setMsg("Excel导入异常 ,异常原因："+e.getMessage());
			logger.error(e.getMessage());
			infoList.add(errorVo);
			map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
			return map;
		}
	}
	
	/**
	 * 泛型 读取Excel
	 * @param file
	 * @param bs
	 * @return
	 * @throws Exception
	 */
	private List<T> readExcel(
			UploadFile file, BaseDataType bs) throws Exception {
		String fileSuffix = StringUtils.substringAfterLast(file.getFileName(), ".");
		IFileReader reader = FileReaderFactory.getFileReader(fileSuffix);
		try {
			List<Map<String, String>> list = reader.getFileContent(file.getInputStream());
			List<Map<String, String>> datas = Lists.newArrayList();
			List<DataProperty> props = bs.getDataProps();
			for (Map<String, String> map : list) {
				Map<String, String> data = Maps.newHashMap();
				for (DataProperty prop : props) {
					
					data.put(prop.getPropertyId(), map.get(prop.getPropertyName().toLowerCase()));
				}
				datas.add(data);
			}
			List<T> modelList = Lists.newArrayList();	
			Class<T> entityClass = (Class<T>) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
			for (Map<String, String> data : datas) {
				T p = (T) BeanToMapUtil.convertMapNull(entityClass, data);
				modelList.add(p);
			}
			return modelList;
		} catch (Exception e) {
			throw e;
		}
	}
	
	private List<T> readExcel(
			List<Map<String, Object>> datas) throws Exception {
		try {
			List<T> modelList = Lists.newArrayList();	
			Class<T> entityClass = (Class<T>) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
			for (Map<String, Object> data : datas) {
				T p = (T) BeanToMapUtil.convertMapNull(entityClass, data);
				modelList.add(p);
			}
			return modelList;
		} catch (Exception e) {
			throw e;
		}
	}
	
	/**
	 * 获取处理进度 0-开始处理 1-验证模板 2-读取Excel 3-开始验证数据 4-开始保存数据 5-保存结束 6-异常结束
	 * @return
	 */
	protected int getProgressStatus(){
		String sessionId=getSessionId();
		Object progressFlag = DoradoContext.getAttachedRequest().getSession().getAttribute(sessionId);
		if (progressFlag == null){
			DoradoContext.getAttachedRequest().getSession().setAttribute(sessionId, 0);
			return 0;
		}
		return (int)(DoradoContext.getAttachedRequest().getSession().getAttribute(sessionId)); 
	}
	/**
	 *重置处理进度
	 */
	protected void setProgressStatus() {
		String sessionId=getSessionId();
		Object progressFlag = DoradoContext.getAttachedRequest().getSession().getAttribute(sessionId);
		if (progressFlag == null){
			DoradoContext.getAttachedRequest().getSession().setAttribute(sessionId, 0);
		} else {
			DoradoContext.getAttachedRequest().getSession().setAttribute(sessionId, 0);
		} 
	}
}
