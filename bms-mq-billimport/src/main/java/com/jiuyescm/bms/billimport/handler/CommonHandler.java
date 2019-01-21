package com.jiuyescm.bms.billimport.handler;

import java.io.File;
import java.io.IOException;
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

import com.jiuyescm.bms.base.dict.api.ICarrierDictService;
import com.jiuyescm.bms.base.dict.api.ICustomerDictService;
import com.jiuyescm.bms.base.dict.api.IMaterialDictService;
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
import com.jiuyescm.bms.excel.callback.SheetReadCallBack;
import com.jiuyescm.bms.excel.data.DataColumn;
import com.jiuyescm.bms.excel.data.DataRow;
import com.jiuyescm.bms.excel.data.Sheet;
import com.jiuyescm.bms.excel.data.XlsxWorkBook;
import com.jiuyescm.common.utils.excel.POISXSSUtil;
import com.jiuyescm.constants.BmsEnums;
import com.jiuyescm.exception.BizException;
import com.jiuyescm.framework.fastdfs.client.StorageClient;
import com.jiuyescm.framework.fastdfs.model.StorePath;

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
	@Autowired private ICustomerDictService customerDictService;
	@Autowired private ICarrierDictService carrierDictService;
	@Autowired private IMaterialDictService materialDictService;
	
	private POISXSSUtil poiUtil = null;
	private SXSSFWorkbook workbook = null;
	
	protected Map<String, String> warehouseMap = new HashMap<>();
	protected Map<String, String> customerMap = new HashMap<>();
	protected Map<String, String> materialMap = new HashMap<>();
	protected Map<String, String> carrierMap = new HashMap<>();
	
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
	private long _start = System.currentTimeMillis();
	private boolean isOK = true; //sheet读取是否成功  true-成功  false-失败
	private boolean isUploadFile = true;
	List<Map<String, Object>> headDetailMapList = new ArrayList<Map<String,Object>>();
	int lineNo = 1;

	public void initKeyValue(){
		isOK = true; //sheet读取是否成功  true-成功  false-失败
		isUploadFile = true;
		poiUtil = new POISXSSUtil();
		workbook = poiUtil.getXSSFWorkbook();
		warehouseMap = warehouseDictService.getWarehouseDictForValue();
		customerMap = customerDictService.getCustomerDictForValue();
		materialMap = materialDictService.getMaterialDictForValue();
		carrierMap = carrierDictService.getCarrierDictForValue();
	}
	
	@Override
	public void process(XlsxWorkBook xlsxReader, Sheet sheet, Map<String, Object> param) throws Exception{
		initKeyValue();
		try {
			//获取任务ID
			param.put("detail", "");
			String taskId = param.get("billNo").toString();
			billNo=param.get("billNo").toString();
			customerName=param.get("invoiceName").toString();
			customerId=param.get("invoiceId").toString();
			sheetName = sheet.getSheetName();
			logger.info("任务ID：{}，正在处理sheet:{}", taskId,sheetName);
			
			// 仓储--上海01仓，北京01仓...............
			if (null != warehouseMap.get(sheetName)) {
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
				logger.error("任务ID：{}，readExcel方法内错误导致sheet解析异常：{}",taskId,e.getMessage());
				param.put("result", "fail");
				isOK = false;
				param.put("detail","解析【"+sheetName+"】异常-"+e.getMessage());
			}
			
			logger.info("任务ID：{}，SheetName：{}读取完成",taskId,sheetName);
			int taskRate = (int) (30+((90-30)/xlsxReader.getSheets().size())*(sheet.getSheetId()));
			updateStatus(billNo, BmsEnums.taskStatus.PROCESS.getCode(), taskRate);	
			logger.info("任务ID：{}，更新任务进度为{}%", taskId,taskRate);
			repeatMap.clear();
			//Excel校验未通过
			logger.info("任务ID：{}，isOK={}", taskId,isOK);
			if(!isOK){
				updateStatus(billNo, BmsEnums.taskStatus.FAIL.getCode(), 99);
				logger.info("任务ID：{}，更新任务状态为失败，进度为99%", taskId);
				BillReceiveMasterVo billReceiveMasterVo = new BillReceiveMasterVo();
				if(isUploadFile){
					String path = xlsxReader.getTempPath();
					path = path.substring(0,path.length() - 1)+".xlsx";
					String resultPath = upLoadExcel(path);
					billReceiveMasterVo.setResultFilePath(resultPath);
					String file=param.get("fileName").toString();
					file=file.substring(0, file.indexOf('.'))+"(异常)"+file.substring(file.indexOf('.'));
					billReceiveMasterVo.setResultFileName(file);
					logger.info("任务ID：{}，异常结果文件上传至FastDFS的路径resultPath：{}", taskId,resultPath);
				}
				//将异常结果文件上传至FastDFS
				billReceiveMasterVo.setBillNo(billNo);
				billReceiveMasterVo.setRemark(param.get("detail").toString());
				billReceiveMasterService.update(billReceiveMasterVo);
				param.put("result", "fail");
				param.put("detail", sheetName+"校验不通过");
				throw new BizException(sheetName+"校验不通过");			
			}else{
				param.put("result", "sucess");
			}
		} catch (Exception e) {
			param.put("result", "fail");
			param.put("detail", e.getMessage());
		}	
	}
	
	private void readExcel(XlsxWorkBook xlsxReader, final Sheet sheet,int titleRowNo,int contentRowNo) throws Exception{
		_start = System.currentTimeMillis();
		xlsxReader.readSheet(sheet.getSheetId(), new SheetReadCallBack() {
			@Override
			public void readTitle(List<String> columns) {
				try{
					String ret = validate(columns);
					if("SUCC".equals(ret)){
						logger.info("任务ID：{}，表头行：{}",billNo,columns);
					}
					else{
						throw new BizException("title_error",ret);
					}
				}
				catch(Exception ex){
					isUploadFile = false;
					throw new BizException("title_error",ex.getMessage());
				}
			}
			
			@Override
			public void read(DataRow dr) {
				try {
					//使用list接收，因为可能出现一行数据对应多个entity
					List<T> entityList = new ArrayList<>();
					DataColumn dColumn = new DataColumn("","");
					dColumn.setTitleName("错误描述");
					try{
						entityList = transRowToObj(dr);
					}
					catch(Exception ex){
						dColumn.setColValue("第"+dr.getRowNo()+"行："+ex.getMessage());
						isOK = false;
					}
					finally{
						dr.addColumn(dColumn);
						errMap.add(dr);
						if(headDetailMapList==null || headDetailMapList.size()==0){
							initHeadDetailMapList(dr);
						}
					}
					//将行转换的实体转换到全局list中
					for (T t : entityList) {
						list.add(t);
					}
					//如果list大于一定的数量（batchNum），则批量写入临时表，分批执行，降低内存消耗
					if(list.size()>=batchNum||errMap.size()>=batchNum){
						saveTo();
					}
				} catch (Exception e) {
					isOK = false;
					logger.error("系统错误：",e);
				}
			}

			@Override
			public void finish() {
				
				if(sheet.getRowCount()<=0){
					isOK = false;
					isUploadFile = false;
					logger.info("任务ID：{}，sheetName={} 未读到任何行",billNo,sheetName);
					throw new BizException("sheet_error","sheet【"+sheetName+"】未读取到任何行");
				}
				else{
					saveTo();
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
		logger.info("任务ID：{}，读取行数【{}】  对象数【{}】验证耗时{}",billNo,errMap.size(),list.size(),(System.currentTimeMillis()-_start));
		int result=0;
		if(isOK){
			if(list.size()>0){
				result=save();
				if(result<=0){
					isOK = false;
					logger.error("任务ID：{}，{}批量插入临时表异常",billNo,sheetName);
				}
			}
			else{
				logger.error("任务ID：{}，{}无数据批量写入",billNo,sheetName);
			}
		}
		try{
			exportErr();
			errMap.clear();
		}
		catch(Exception ex){
			isOK = false;
			logger.error("写入结果文件异常",ex);
		}
				
		list.clear();
		return result;
	}
	
	public abstract int save();
	
	public void initHeadDetailMapList(DataRow dr){
		headDetailMapList = new ArrayList<Map<String,Object>>();//getBizHead(exportColumns);
		for(int i=0;dr.getColumns().size()>i;i++){
	        Map<String, Object> itemMap = new HashMap<String, Object>();
	        itemMap.put("title", dr.getColumns().get(i).getTitleName());
	        //设置单元格大小
	        if(dr.getColumns().size()==i+1){
		        itemMap.put("columnWidth", 100);
	        }else{
		        itemMap.put("columnWidth", 30);
	        }
	        int a = i+1;
	        itemMap.put("dataKey", "XH"+a);
	        headDetailMapList.add(itemMap);
		}
	}
	
	public void exportErr() throws Exception{
		List<Map<String, Object>> dataDetailList = new ArrayList<Map<String,Object>>();//getBizHeadItem();
		//遍历内容
		for(int i =0;errMap.size()>i;i++){
	        Map<String, Object> dataItem = new HashMap<String, Object>();
			for(int v =0;errMap.get(i).getColumns().size()>v;v++){
		        int a = v+1;
		        dataItem.put("XH"+a, errMap.get(i).getColumns().get(v).getColValue());
			}
	        dataDetailList.add(dataItem);
		}
		poiUtil.exportExcel2FilePath(poiUtil, workbook, sheetName,lineNo, headDetailMapList, dataDetailList);
		if(dataDetailList !=null){
			lineNo += errMap.size();
		}
	}
	
	private String upLoadExcel(String path) throws IOException{
		poiUtil.write2FilePath(workbook,path);
		StorePath resultStorePath = storageClient.uploadFile(path);
	    String resultFullPath = resultStorePath.getFullPath();
	    workbook.dispose();
	    headDetailMapList.clear();
	    File file = new File(path); 
    	if (file.exists() && file.isFile()) {  
			if (file.delete()) {  
				logger.info("本地excel文件删除成功");
            } else {  
            	logger.info("本地excel文件删除失败");
            }  
		}
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
 			e.printStackTrace();
 		}
		return null;
	}
}
