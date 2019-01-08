package com.jiuyescm.bms.billimport.handler;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.jiuyescm.bms.base.dict.api.IWarehouseDictService;
import com.jiuyescm.bms.bill.receive.repository.IBillReceiveMasterRepository;
import com.jiuyescm.bms.billcheck.service.IBillCheckInfoService;
import com.jiuyescm.bms.billcheck.service.IBillReceiveMasterService;
import com.jiuyescm.bms.billcheck.vo.BillReceiveMasterVo;
import com.jiuyescm.bms.billimport.IFeesHandler;
import com.jiuyescm.bms.billimport.service.IBillFeesReceiveAirTempService;
import com.jiuyescm.bms.billimport.service.IBillFeesReceiveDispatchTempService;
import com.jiuyescm.bms.billimport.service.IBillFeesReceiveHandService;
import com.jiuyescm.bms.billimport.service.IBillFeesReceiveStorageTempService;
import com.jiuyescm.bms.billimport.service.IBillFeesReceiveTransportTempService;
import com.jiuyescm.bms.excel.ExcelXlsxReader;
import com.jiuyescm.bms.excel.callback.SheetReadCallBack;
import com.jiuyescm.bms.excel.data.DataColumn;
import com.jiuyescm.bms.excel.data.DataRow;
import com.jiuyescm.bms.excel.data.Sheet;
import com.jiuyescm.bms.excel.data.XlsxWorkBook;
import com.jiuyescm.bms.excel.opc.OpcSheet;
import com.jiuyescm.common.utils.excel.POISXSSUtil;
import com.jiuyescm.constants.BmsEnums;
import com.jiuyescm.exception.BizException;
import com.jiuyescm.framework.fastdfs.client.StorageClient;
import com.jiuyescm.framework.fastdfs.model.StorePath;
import com.jiuyescm.mdm.warehouse.vo.WarehouseVo;

public abstract class CommonHandler<T> implements IFeesHandler {

	private static final Logger logger = LoggerFactory.getLogger(CommonHandler.class);
	
	@Autowired private StorageClient storageClient;
	@Autowired private IBillReceiveMasterRepository billReceiveMasterRepository;
	@Autowired private IWarehouseDictService warehouseDictService;
	@Autowired private IBillReceiveMasterService billReceiveMasterService;
	@Autowired private IBillCheckInfoService billCheckInfoService;	
	@Autowired private IBillFeesReceiveAirTempService billFeesReceiveAirTempService;
	@Autowired private IBillFeesReceiveStorageTempService billFeesReceiveStorageTempService;
	@Autowired private IBillFeesReceiveDispatchTempService billFeesReceiveDispatchTempService;
	@Autowired private IBillFeesReceiveTransportTempService billFeesReceiveTransportTempService;
	@Autowired private IBillFeesReceiveHandService billFeesReceiveHandService;
	
	private int batchNum = 1000;
	protected String sheetName;
	List<T> list = new ArrayList<T>();
	List<DataRow> errMap = new ArrayList<DataRow>();
	protected Map<String,Integer> repeatMap=new HashMap<String, Integer>();
	//账单编号
	protected String billNo;
	//商家名称
	protected String customerName;
	//商家id
	protected String customerId;
	//进度
	//protected Integer taskRate = 30;
	
	private long _start = System.currentTimeMillis();

	
	@Override
	public void process(XlsxWorkBook xlsxReader, Sheet sheet, Map<String, Object> param) throws Exception{
		//this.billEntity = null;//根据param中的bill_no查询 ???
		try {
			billNo=param.get("billNo").toString();
			customerName=param.get("invoiceName").toString();
			customerId=param.get("invoiceId").toString();
			sheetName = sheet.getSheetName();
			logger.info("正在处理sheet:{}", sheetName);
			
			// 仓储--上海01仓，北京01仓...............
			WarehouseVo warehouseVo = warehouseDictService.getWarehouseByName(sheetName);
			if (null != warehouseVo.getWarehousename()) {
				sheetName = "仓储";
			}

			try {
				if("仓储".equals(sheetName)){
					sheetName = sheet.getSheetName();
					readExcel(xlsxReader,sheet,3,5);
				}else {
					readExcel(xlsxReader,sheet,1,2);
				}
			} catch (Exception e) {
				// TODO: handle exception
				logger.error("sheet解析异常：", e);
				param.put("result", "fail");
				param.put("detail","解析【"+sheetName+"】异常-"+e.getMessage());
				return;
			}
			
			logger.info(billNo+sheet+"读取完成");
			int taskRate = (int) (30+((90-30)/xlsxReader.getSheets().size())*(sheet.getSheetId()));
			//taskRate = (int) (taskRate+(90-30)*(1/(float)xlsxReader.getSheets().size()));
			updateStatus(billNo, BmsEnums.taskStatus.PROCESS.getCode(), taskRate);	
			repeatMap.clear();
			//Excel校验未通过
			if(errMap.size()>0){
				updateStatus(billNo, BmsEnums.taskStatus.FAIL.getCode(), 99);	
				String resultPath = exportErr();
				BillReceiveMasterVo billReceiveMasterVo = new BillReceiveMasterVo();
				billReceiveMasterVo.setBillNo(billNo);
				billReceiveMasterVo.setResultFilePath(resultPath);
				String file=param.get("fileName").toString();
				file=file.substring(0, file.indexOf('.'))+"(异常)"+file.substring(file.indexOf('.'));
				billReceiveMasterVo.setResultFileName(file);
				billReceiveMasterService.update(billReceiveMasterVo);
				param.put("result", "fail");
				param.put("detail", sheetName+"校验不通过");
				throw new BizException(sheetName+"校验不通过");			
			}else{
				param.put("result", "sucess");
			}
		} catch (Exception e) {
			// TODO: handle exception
			param.put("result", "fail");
			param.put("detail", e.getMessage());
		}	
	}
	
	private void readExcel(XlsxWorkBook xlsxReader, Sheet sheet,int titleRowNo,int contentRowNo) throws Exception{
		_start = System.currentTimeMillis();
		xlsxReader.readSheet(sheet.getSheetId(), new SheetReadCallBack() {
			@Override
			public void read(DataRow dr) {
				try {
					List<T> entityList = transRowToObj(dr);
					
					for( int i = 0 ; i < entityList.size() ; i++){
						list.add(entityList.get(i));
					}

					if(list.size()>=batchNum){
						int result=saveTo();
						if(result<=0){
							throw new BizException("title_error","excel处理异常：批量插入"+sheetName+"费用异常");
						}
					}
				} catch (Exception e) {
					DataColumn dColumn = new DataColumn("异常描述","第"+dr.getRowNo()+"行："+e.getMessage());
					dr.addColumn(dColumn);
					errMap.add(dr);
				}
			}

			@Override
			public void finish() {
				if(list.size()>0){
					int result=saveTo();
					if(result<=0){
						throw new BizException("title_error","excel处理异常：批量插入"+sheetName+"费用异常");
					}
				}
				logger.info("读取完毕");
			}
			
			@Override
			public void readTitle(List<String> columns) {
				try{
					String ret = validate(columns);
					if("SUCC".equals(ret)){
						logger.info("表头行：{}",columns);
					}
					else{
						throw new BizException("title_error",ret);
					}
				}
				catch(Exception ex){
					throw new BizException("title_error","excel处理异常："+ex.getMessage());
				}
			}

			@Override
			public void error(Exception ex) {
				throw new BizException("read_error","excel读取异常："+ex.getMessage());
			}
			
		},titleRowNo,contentRowNo);
	}
	
	public abstract List<T> transRowToObj(DataRow dr) throws Exception;
	
	public abstract void transErr(DataRow dr) throws Exception; 
	
	/**
	 * 
	 * @param columns
	 * @return SUCC - 校验成功
	 * @throws Exception
	 */
	public abstract String validate(List<String> columns) throws Exception;
	/**
	 * 分批保存数据到临时表
	 * @throws Exception
	 */
	public int saveTo(){
		int result=0;
		if(errMap.size()==0){
			logger.info("读取【{}】行验证耗时{}",list.size(),(System.currentTimeMillis()-_start));
			result=save();
			_start =  System.currentTimeMillis();
		}else{
			logger.info("错误信息: {}", errMap);
			logger.info("errMap.size: {}", errMap.size());
		}
		
		list.clear();
		return result;
	}
	
	public abstract int save();
	
	public String exportErr() throws Exception{
		
		POISXSSUtil poiUtil = new POISXSSUtil();
		SXSSFWorkbook workbook = new SXSSFWorkbook(10000);		
    	List<Map<String, Object>> headDetailMapList = new ArrayList<Map<String,Object>>();//getBizHead(exportColumns); 
		List<Map<String, Object>> dataDetailList = new ArrayList<Map<String,Object>>();//getBizHeadItem();

		//遍历表头
		for(int i=0;errMap.get(0).getColumns().size()>i;i++){
	        Map<String, Object> itemMap = new HashMap<String, Object>();
	        itemMap.put("title", errMap.get(0).getColumns().get(i).getTitleName());
	        if(errMap.get(0).getColumns().size()==i+1){
		        itemMap.put("columnWidth", 100);
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

		poiUtil.exportExcel2FilePath(poiUtil, workbook, sheetName,1, headDetailMapList, dataDetailList);
    	ByteArrayOutputStream os = new ByteArrayOutputStream();
		workbook.write(os);
		byte[] b1 = os.toByteArray();
		StorePath resultStorePath = storageClient.uploadFile(new ByteArrayInputStream(b1), b1.length, "xlsx");
	    String resultFullPath = resultStorePath.getFullPath();
	   // System.out.println(resultFullPath);
	    
	    errMap.clear();
	    return resultFullPath;
	}
	
	/**
	 * 更新主表导入状态
	 * @param map
	 * @param status
	 */
	public void updateStatus(String billNo, String status, int rate) {
		BillReceiveMasterVo entity = new BillReceiveMasterVo();
		entity.setBillNo(billNo);
		entity.setTaskStatus(status);
		entity.setTaskRate(rate);
		if("SUCCESS".equals(status)){
			entity.setRemark("导入成功");
		}
		billReceiveMasterService.update(entity);
	}
	
	public Date getDate(int createMonth){
		try {
			String createDate="20"+createMonth+"";
			int year=Integer.parseInt(createDate.substring(0, 4));
			int month=Integer.parseInt(createDate.substring(4, 6));
			
			if(month<=0){
				year=year-1;
			}
			 Calendar cal = Calendar.getInstance();    
	         cal.set(Calendar.YEAR, year);     
	         cal.set(Calendar.MONTH, month);     
	         cal.set(Calendar.DAY_OF_MONTH,20);  
			
	         String date=new SimpleDateFormat( "yyyy-MM-dd ").format(cal.getTime()); 
	         
	         DateFormat df = new SimpleDateFormat("yyyy-MM-dd");		
	         
	 		 Date date1 = df.parse(date);
	 		 
	 		 return date1;
 		} catch (ParseException e) {
 			// TODO Auto-generated catch block
 			e.printStackTrace();
 		}
		return null;
	}
}
