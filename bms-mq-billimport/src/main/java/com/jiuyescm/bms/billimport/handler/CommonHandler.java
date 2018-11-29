package com.jiuyescm.bms.billimport.handler;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import redis.clients.jedis.params.sortedset.ZAddParams;

import com.jiuyescm.bms.base.dict.api.IWarehouseDictService;
import com.jiuyescm.bms.bill.receive.entity.BillReceiveMasterEntity;
import com.jiuyescm.bms.bill.receive.repository.IBillReceiveMasterRepository;
import com.jiuyescm.bms.billcheck.service.IBillReceiveMasterService;
import com.jiuyescm.bms.billcheck.vo.BillReceiveMasterVo;
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
import com.jiuyescm.mdm.warehouse.vo.WarehouseVo;

public abstract class CommonHandler<T> implements IFeesHandler {

	private static final Logger logger = LoggerFactory.getLogger(CommonHandler.class);
	
	@Autowired private StorageClient storageClient;
	@Autowired private IBillReceiveMasterRepository billReceiveMasterRepository;
	@Autowired private IWarehouseDictService warehouseDictService;
	@Autowired private IBillReceiveMasterService billReceiveMasterService;
	
	private int batchNum = 1000;
	private String sheetName;
	List<T> list = new ArrayList<T>();
	List<DataRow> errMap = new ArrayList<DataRow>();
	protected Map<String,Integer> repeatMap=new HashMap<String, Integer>();
	
	private BillReceiveMasterEntity billEntity;
	
	@Override
	public void process(ExcelXlsxReader xlsxReader, OpcSheet sheet, Map param) throws Exception{
		this.billEntity = null;//根据param中的bill_no查询
		sheetName = sheet.getSheetName();
		// 仓储--上海01仓，北京01仓...............
		WarehouseVo warehouseVo = warehouseDictService.getWarehouseByName(sheetName);
		if (null != warehouseVo.getWarehousename()) {
			sheetName = "仓储";
		}
		// 耗材使用费
		if (sheetName.contains("耗材使用费")) {
			sheetName = "耗材使用费";
		}
		System.out.println(sheetName);
		if(sheetName.equals("仓储")){
			readExcel(xlsxReader,sheet,3,5);
		}else {
			readExcel(xlsxReader,sheet,1,2);
		}
			
		System.out.println("errMap.size()--"+errMap.size());
		
		if(errMap.size()>0){
			exportErr();
		}
		repeatMap.clear();
	}
	
	private void readExcel(ExcelXlsxReader xlsxReader, OpcSheet sheet,int titleRowNo,int contentRowNo) throws Exception{
		xlsxReader.readRow(1, new SheetReadCallBack() {
			@Override
			public void read(DataRow dr) {
				System.out.println("----行号【" + dr.getRowNo() + "】");
				try {
					List<T> entityList = transRowToObj(dr);
					for( int i = 0 ; i < entityList.size() ; i++){
						list.add(entityList.get(i));
					}
					if(list.size()==batchNum){
						saveTo();
					}
				} catch (Exception e) {
					DataColumn dColumn = new DataColumn("异常描述",dr.getRowNo()+"行："+e.getMessage());
					dr.addColumn(dColumn);
					errMap.add(dr);
				}
			}

			@Override
			public void finish() {
				System.out.println("list.size()--"+list.size());
				if(list.size()>0){
					saveTo();
				}
				System.out.println("读取完毕");
			}
		},titleRowNo,contentRowNo);
	}
	
	public abstract List<T> transRowToObj(DataRow dr) throws Exception;
	
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
		
//		if(!StringUtil.isEmpty(billEntity.getResultFilePath())){
//			logger.info("删除历史结果文件");
//			boolean resultF = storageClient.deleteFile(billEntity.getResultFilePath());
//			if(resultF){
//				logger.info("删除历史结果文件-成功");
//			}
//			else{
//				logger.info("删除历史结果文件-失败");
//			}
//		}
		
		POISXSSUtil poiUtil = new POISXSSUtil();
		SXSSFWorkbook workbook = new SXSSFWorkbook(10000);		
    	List<Map<String, Object>> headDetailMapList = new ArrayList<Map<String,Object>>();//getBizHead(exportColumns); 
		List<Map<String, Object>> dataDetailList = new ArrayList<Map<String,Object>>();//getBizHeadItem();

		//遍历表头
		for(int i=0;errMap.get(0).getColumns().size()>i;i++){
	        Map<String, Object> itemMap = new HashMap<String, Object>();
	        itemMap.put("title", errMap.get(0).getColumns().get(i).getColName());
	        if(errMap.get(0).getColumns().size()==i+1){
		        itemMap.put("columnWidth", 1000);
	        }else{
		        itemMap.put("columnWidth", 30);
	        }
	        int a = i+1;
	        itemMap.put("dataKey", "XH"+a);
	        headDetailMapList.add(itemMap);
		}
		
		//遍历内容
		for(int i =0;errMap.size()>i;i++){
	        Map<String, Object> dataItem = new HashMap<String, Object>();
			for(int v =0;errMap.get(i).getColumns().size()>v;v++){
		        int a = v+1;
		        dataItem.put("XH"+a, errMap.get(i).getColumns().get(v).getColValue());
			}
	        dataDetailList.add(dataItem);
		}

//		poiUtil.exportExcel2FilePath(poiUtil, workbook, sheetName,1, headDetailMapList, dataDetailList);
//    	ByteArrayOutputStream os = new ByteArrayOutputStream();
//		workbook.write(os);
//		byte[] b1 = os.toByteArray();
//		StorePath resultStorePath = storageClient.uploadFile(new ByteArrayInputStream(b1), b1.length, "xlsx");
//	    String resultFullPath = resultStorePath.getFullPath();
	    
	    //billReceiveMasterRepository.delete(null);  //删除临时表数据
//	    billReceiveMasterRepository.update(null); //更新账单导入主表状态和结果文件路径
//	    logger.info("上传结果文件到FastDfs - 成功");
	    
        try {
        	poiUtil.exportExcel2FilePath(poiUtil,workbook,"test sheet 1",1, headDetailMapList, dataDetailList);
//        	poiUtil.exportExcel2FilePath(poiUtil,hssfWorkbook,"test sheet 1",dataList.size()+1, headInfoList, dataList);
//        	poiUtil.exportExcelFilePath(poiUtil,hssfWorkbook,"test sheet 2","e:\\tmp\\customer2.xlsx", headInfoList, dataList);
        	poiUtil.write2FilePath(workbook, "D:\\testhaha.xlsx");
		} catch (IOException e) {
			logger.error("写入文件异常", e);
		}
	    errMap.clear();
	}
	
	
	/**
	 * 更新主表导入状态
	 * @param map
	 * @param status
	 */
	private void updateStatus(Map<String, Object> map, String status, int rate) {
		BillReceiveMasterVo entity = new BillReceiveMasterVo();
		entity.setBillNo(map.get("billNo").toString());
		entity.setTaskStatus(status);
		entity.setTaskRate(rate);
		billReceiveMasterService.update(entity);
	}
	
}
