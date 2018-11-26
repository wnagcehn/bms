package com.jiuyescm.bms.billimport.handler;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.jiuyescm.bms.bill.receive.entity.BillReceiveMasterEntity;
import com.jiuyescm.bms.bill.receive.repository.IBillReceiveMasterRepository;
import com.jiuyescm.bms.billimport.IFeesHandler;
import com.jiuyescm.bms.excel.ExcelXlsxReader;
import com.jiuyescm.bms.excel.callback.SheetReadCallBack;
import com.jiuyescm.bms.excel.data.DataColumn;
import com.jiuyescm.bms.excel.data.DataRow;
import com.jiuyescm.bms.excel.opc.OpcSheet;
import com.jiuyescm.bs.util.StringUtil;
import com.jiuyescm.common.utils.excel.POISXSSUtil;
import com.jiuyescm.framework.fastdfs.client.StorageClient;
import com.jiuyescm.framework.fastdfs.model.StorePath;

public abstract class CommonHandler<T> implements IFeesHandler {

	private static final Logger logger = LoggerFactory.getLogger(CommonHandler.class);
	
	@Autowired private StorageClient storageClient;
	@Autowired private IBillReceiveMasterRepository billReceiveMasterRepository;
	
	private int batchNum = 1000;
	private String sheetName;
	List<T> list;
	List<DataRow> errMap = new ArrayList<DataRow>();
	
	private BillReceiveMasterEntity billEntity;
	
	@Override
	public void process(ExcelXlsxReader xlsxReader, OpcSheet sheet, Map param) throws Exception{
		this.billEntity = null;//根据param中的bill_no查询
		sheetName = sheet.getSheetName();
		
		if(sheetName.equals("仓储")){
			readExcel(xlsxReader,sheet,3,5);
		}else {
			readExcel(xlsxReader,sheet,1,2);
		}
			
		if(errMap.size()>0){
			exportErr();
		}
	}
	
	private void readExcel(ExcelXlsxReader xlsxReader, OpcSheet sheet,int titleRowNo,int contentRowNo) throws Exception{
		xlsxReader.readRow(1, new SheetReadCallBack() {
			@Override
			public void read(DataRow dr) {
				System.out.println("----行号【" + dr.getRowNo() + "】");
				try {
					T entity = transRowToObj(dr);
					list.add(entity);
					if(list.size()==batchNum){
						saveTo();
					}
				} catch (Exception e) {
					DataColumn dColumn = new DataColumn("异常描述",e.getMessage());
					dr.addColumn(dColumn);
					errMap.add(dr);
				}
			}

			@Override
			public void finish() {
				if(list.size()>0){
					saveTo();
				}
				System.out.println("读取完毕");
			}
		},titleRowNo,contentRowNo);
	}
	
	public abstract T transRowToObj(DataRow dr) throws Exception;
	
	public abstract void transErr(DataRow dr) throws Exception; 
	
	
	
	/**
	 * 分批保存数据到临时表
	 * @throws Exception
	 */
	public void saveTo(){
		if(errMap.size()==0){
			save();
		}
		list.clear();
	}
	
	public abstract void save();
	
	public void exportErr() throws Exception{
		
		if(!StringUtil.isEmpty(billEntity.getResultFilePath())){
			logger.info("删除历史结果文件");
			boolean resultF = storageClient.deleteFile(billEntity.getResultFilePath());
			if(resultF){
				logger.info("删除历史结果文件-成功");
			}
			else{
				logger.info("删除历史结果文件-失败");
			}
		}
		
		POISXSSUtil poiUtil = new POISXSSUtil();
		SXSSFWorkbook workbook = new SXSSFWorkbook(1000);
    	List<Map<String, Object>> headDetailMapList = null;//getBizHead(exportColumns); 
		List<Map<String, Object>> dataDetailList = null;//getBizHeadItem();
		poiUtil.exportExcel2FilePath(poiUtil, workbook, sheetName,1, headDetailMapList, dataDetailList);
    	ByteArrayOutputStream os = new ByteArrayOutputStream();
		workbook.write(os);
		byte[] b1 = os.toByteArray();
		StorePath resultStorePath = storageClient.uploadFile(new ByteArrayInputStream(b1), b1.length, "xlsx");
	    String resultFullPath = resultStorePath.getFullPath();
	    
	    //billReceiveMasterRepository.delete(null);  //删除临时表数据
	    billReceiveMasterRepository.update(null); //更新账单导入主表状态和结果文件路径
	    logger.info("上传结果文件到FastDfs - 成功");
	}
	

	
}
