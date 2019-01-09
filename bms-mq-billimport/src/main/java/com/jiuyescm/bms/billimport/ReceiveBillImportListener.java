package com.jiuyescm.bms.billimport;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.jiuyescm.bms.base.dict.api.IWarehouseDictService;
import com.jiuyescm.bms.billcheck.service.IBillCheckInfoService;
import com.jiuyescm.bms.billcheck.service.IBillReceiveMasterService;
import com.jiuyescm.bms.billcheck.vo.BillCheckInfoVo;
import com.jiuyescm.bms.billcheck.vo.BillReceiveMasterVo;
import com.jiuyescm.bms.billcheck.vo.ReportBillImportMasterVo;
import com.jiuyescm.bms.billimport.service.IBillFeesReceiveAirTempService;
import com.jiuyescm.bms.billimport.service.IBillFeesReceiveDispatchTempService;
import com.jiuyescm.bms.billimport.service.IBillFeesReceiveHandService;
import com.jiuyescm.bms.billimport.service.IBillFeesReceiveStorageTempService;
import com.jiuyescm.bms.billimport.service.IBillFeesReceiveTransportTempService;
import com.jiuyescm.bms.common.enumtype.BillCheckInvoiceStateEnum;
import com.jiuyescm.bms.common.enumtype.BillCheckReceiptStateEnum;
import com.jiuyescm.bms.common.enumtype.BillCheckStateEnum;
import com.jiuyescm.bms.common.enumtype.CheckBillStatusEnum;
import com.jiuyescm.bms.excel.ExcelXlsxReader;
import com.jiuyescm.bms.excel.data.Sheet;
import com.jiuyescm.bms.excel.data.XlsxWorkBook;
import com.jiuyescm.bms.excel.opc.OpcSheet;
import com.jiuyescm.cfm.common.JAppContext;
import com.jiuyescm.constants.BmsEnums;
import com.jiuyescm.framework.fastdfs.client.StorageClient;
import com.jiuyescm.framework.fastdfs.protocol.storage.callback.DownloadByteArray;
import com.jiuyescm.mdm.warehouse.vo.WarehouseVo;

@Service("receiveBillImportListener")
public class ReceiveBillImportListener implements MessageListener {

	private static final Logger logger = LoggerFactory
			.getLogger(ReceiveBillImportListener.class);

	@Autowired
	private StorageClient storageClient;

	@Autowired
	private IWarehouseDictService warehouseDictService;
	
	@Autowired
	private IBillReceiveMasterService billReceiveMasterService;
	
	@Autowired private IBillFeesReceiveAirTempService billFeesReceiveAirTempService;
	@Autowired private IBillFeesReceiveStorageTempService billFeesReceiveStorageTempService;
	@Autowired private IBillFeesReceiveDispatchTempService billFeesReceiveDispatchTempService;
	@Autowired private IBillFeesReceiveTransportTempService billFeesReceiveTransportTempService;
	@Autowired private IBillFeesReceiveHandService billFeesReceiveHandService;
	@Autowired private IBillCheckInfoService billCheckInfoService;	

	private XlsxWorkBook  book ;

	@Override
	public void onMessage(Message message) {
		logger.info("应收账单导入异步处理");
		String json = "";
		try {
			json = ((TextMessage) message).getText();
			logger.info("消息Json【{}】", json);
			try {
				readExcel(json);
			} catch (Throwable e) {
				e.printStackTrace();
				logger.error("获取消息失败-{}", e);
			}
		} catch (JMSException e1) {
			logger.error("获取消息失败-{}", e1);
			return;
		}
	}


	public void readExcel(String json) throws Throwable {
		logger.info("JSON开始解析……");
		Map<String, Object> map = resolveJsonToMap(json);
		if (null == map) {
			return;
		}
		//MQ拿到消息，更新状态
		updateStatus(map.get("billNo").toString(), BmsEnums.taskStatus.PROCESS.getCode(), 1);
		
		logger.info("获取文件路径：{}",map.get("fullPath").toString());
//		File file = new File(map.get("fullPath").toString());
//		InputStream inputStream = new FileInputStream(file);
		
		String filePath = map.get("fullPath").toString();
		byte[] bytes = storageClient.downloadFile(filePath, new DownloadByteArray());
		InputStream inputStream = new ByteArrayInputStream(bytes);

		try {
			book  = new XlsxWorkBook(inputStream);
			//List<OpcSheet> sheets = book .getSheets();
			//logger.info("解析Excel, 获取Sheet：{}", sheets.size());
			updateStatus(map.get("billNo").toString(), BmsEnums.taskStatus.PROCESS.getCode(), 30);
			for (Sheet sheet : book.getSheets()) {
				String sheetName = sheet.getSheetName();
				logger.info("--------------准备读取sheet - {}", sheetName);
				long startTime = System.currentTimeMillis();
				// 仓储--上海01仓，北京01仓...............
				WarehouseVo warehouseVo = warehouseDictService.getWarehouseByName(sheetName);
				if (null != warehouseVo.getWarehousename()) {
					sheetName = "仓储";
				}
				// 耗材使用费
				if (sheetName.contains("耗材使用费")) {
					sheetName = "耗材使用费";
				}
				IFeesHandler handler = FeesHandlerFactory.getHandler(sheetName);
				if (null == handler) {
					continue;
				}
				logger.info("匹配Handler为: {}", handler);
				//handler.getRows();

				try {
					handler.process(book, sheet, map);
					if("fail".equals(map.get("result"))){
						break;
					}					
				} catch (Exception ex) {
					logger.error("处理异常 {}",ex);
					break;
				}
				finally {
					logger.info("------------读取耗时：【"+(System.currentTimeMillis()-startTime)+"】毫秒");
				}
			}
			
			// saveAll 保存临时表数据到正式表
			saveAll(map);
		} catch (Exception ex) {
			logger.error("readExcel 异常 {}", ex);
		}
		finally{
			book.close();
		}
		
		
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
		billReceiveMasterService.update(entity);
	}

	
	/**
	 * 解析Json成Map
	 * @param json
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private Map<String, Object> resolveJsonToMap(String json) {
		//解析JSON
		Map<String, Object> map = null;
		try {
			map = (Map<String, Object>)JSON.parse(json);
		} catch (Exception e) {
			logger.error("JSON解析异常 {}", e);
			return null;
		}
		return map;
	}
	
	public void saveAll(Map<String, Object> param){
		String billNo=param.get("billNo").toString();
		BillReceiveMasterVo billReceiveMasterVo = new BillReceiveMasterVo();
		try{
			if("sucess".equals(param.get("result").toString())){
				logger.info(billNo+"临时表数据开始写入正式表");
				updateStatus(billNo, BmsEnums.taskStatus.PROCESS.getCode(), 90);
				//将临时表的数据写入正式表（仓储、配送、干线、航空）
				billFeesReceiveHandService.saveDataFromTemp(billNo);
				//无论保存成功与否删除所有临时表的数据
				logger.info(billNo+"删除临时表数据");
				billFeesReceiveStorageTempService.deleteBatchTemp(billNo);
				billFeesReceiveDispatchTempService.deleteBatchTemp(billNo);
				billFeesReceiveTransportTempService.deleteBatchTemp(billNo);
				billFeesReceiveAirTempService.deleteBatchTemp(billNo);
				//统计金额
				logger.info(billNo+"更新总金额至主表数据");
				Double totalMoney=billFeesReceiveStorageTempService.getImportTotalAmount(billNo);
				BillReceiveMasterVo entity=new BillReceiveMasterVo();
				entity.setAmount(totalMoney);
				entity.setBillNo(billNo);
				//更新导入主表
				billReceiveMasterService.update(entity);
				logger.info(billNo+"写入账单主表");
				//账单跟踪 组装数据
				DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
				BillCheckInfoVo checkInfoVo = new BillCheckInfoVo();
				checkInfoVo.setCreateMonth(Integer.valueOf(param.get("createMonth").toString()));
				checkInfoVo.setBillNo(billNo);
				checkInfoVo.setBillName(param.get("billName").toString());
				checkInfoVo.setInvoiceId(param.get("invoiceId").toString());
				checkInfoVo.setInvoiceName(param.get("invoiceName").toString());
				
				Date d=getNewDate(param.get("billStartTime").toString());
				if(d!=null){
					checkInfoVo.setBillStartTime(d);
				}
				checkInfoVo.setFirstClassName(param.get("firstClassName").toString());
				checkInfoVo.setBizTypeName(param.get("bizTypeName").toString());
				checkInfoVo.setProjectName(param.get("projectName").toString());
				checkInfoVo.setSellerId(param.get("sellerId").toString());
				checkInfoVo.setSellerName(param.get("sellerName").toString());
				checkInfoVo.setDeptName(param.get("deptName").toString());
				checkInfoVo.setDeptCode(param.get("deptCode").toString());
				checkInfoVo.setProjectManagerId(param.get("projectManagerId").toString());
				checkInfoVo.setProjectManagerName(param.get("projectManagerName").toString());
				checkInfoVo.setBalanceId(param.get("balanceId").toString());
				checkInfoVo.setBalanceName(param.get("balanceName").toString());
				checkInfoVo.setBillCheckStatus(BmsEnums.BillCheckStateEnum.getCode(param.get("billCheckStatus").toString()));
				
				checkInfoVo.setIsneedInvoice(BmsEnums.isInvoice.getCode(param.get("isneedInvoice").toString()));
				//是否需要发票
				if(StringUtils.isNotBlank(checkInfoVo.getIsneedInvoice())){
					if("1".equals(checkInfoVo.getIsneedInvoice())){
						checkInfoVo.setInvoiceStatus(BillCheckInvoiceStateEnum.NO_INVOICE.getCode());//未开票 
					}else if("0".equals(checkInfoVo.getIsneedInvoice())){
						checkInfoVo.setInvoiceStatus(BillCheckInvoiceStateEnum.UNNEED_INVOICE.getCode());//不需要发票 
					}
				}
				
				if (BmsEnums.BillCheckStateEnum.CONFIRMED.getDesc().equals(param.get("billCheckStatus").toString())) {
					checkInfoVo.setConfirmMan(param.get("confirmMan").toString());
					checkInfoVo.setConfirmManId(param.get("confirmManId").toString());
					Date date=getNewDate(param.get("confirmDate").toString());
					if (null != date) {
						checkInfoVo.setConfirmDate(date);
					}	
				}
				//新增账单状态判断
				//1）如果对账状态为“已确认”and是否需要开票为“是”，将账单状态置为“待开票”；
				//2）如果对账状态为“已确认”and是否需要开票为“否”，将账单状态置为“待收款”；
				//3）如果对账状态不为“已确认”，将账单状态置为“待确认”；
				if(BillCheckStateEnum.CONFIRMED.getCode().equals(checkInfoVo.getBillCheckStatus()) && "1".equals(checkInfoVo.getIsneedInvoice())){
					checkInfoVo.setBillStatus(CheckBillStatusEnum.TB_INVOICE.getCode());
				}else if(BillCheckStateEnum.CONFIRMED.getCode().equals(checkInfoVo.getBillCheckStatus()) && "0".equals(checkInfoVo.getIsneedInvoice())){
					checkInfoVo.setBillStatus(CheckBillStatusEnum.TB_RECEIPT.getCode());
				}else{
					checkInfoVo.setBillStatus(CheckBillStatusEnum.TB_CONFIRMED.getCode());
				}
				BigDecimal money=BigDecimal.valueOf(totalMoney);
				checkInfoVo.setConfirmAmount(money);
				checkInfoVo.setConfirmUnInvoiceAmount(money);
				checkInfoVo.setUnReceiptAmount(money);
				checkInfoVo.setDelFlag("0");
				checkInfoVo.setCreator(param.get("creator").toString());
				checkInfoVo.setCreatorId(param.get("creatorId").toString());
				checkInfoVo.setIsapplyBad("0");
				checkInfoVo.setReceiptStatus(BillCheckReceiptStateEnum.UN_RECEIPT.getCode());//未收款
				//账单逾期时间
				Date overdueDate=getDate(checkInfoVo.getCreateMonth());
				checkInfoVo.setOverdueDate(overdueDate);
				checkInfoVo.setCreateTime(JAppContext.currentTimestamp());
				//存储金额
				billCheckInfoService.saveNew(checkInfoVo);				
				//更新导入主表		
				billReceiveMasterVo.setTaskStatus(BmsEnums.taskStatus.SUCCESS.getCode());
				billReceiveMasterVo.setRemark("导入完成");
				billReceiveMasterVo.setTaskRate(100);
				//==================导入成功后汇总各个总金额=====================
				ReportBillImportMasterVo vo=new ReportBillImportMasterVo();
				//总金额
				vo.setTotalMoney(money);
				//仓储费用
				Double storageMoney=billFeesReceiveStorageTempService.getImportStorageAmount(billNo);
				vo.setTotalStorageMoney(new BigDecimal(storageMoney));
				//配送费用
				Double dispatchMoney=billFeesReceiveDispatchTempService.getImportDispatchAmount(billNo);
				vo.setTotalDispatchMoney(new BigDecimal(dispatchMoney));
				//干线费用
				Double transportMoney=billFeesReceiveTransportTempService.getImportTransportAmount(billNo);
				vo.setTotalTransportMoney(new BigDecimal(transportMoney));
				//航空费用
				Double airMoney=billFeesReceiveAirTempService.getImportAirAmount(billNo);
				vo.setTotalAirMoney(new BigDecimal(airMoney));
				//获取所有的理赔金额
				Double abnormalMoney=billReceiveMasterService.getAbnormalMoney(entity.getBillNo());
				vo.setTotalAbnormalMoney(new BigDecimal(abnormalMoney));
				//耗材托数
				Map<String,BigDecimal> material=billReceiveMasterService.queryMaterial(billNo);
				vo.setTotalMaterialStorage(material.get("num"));
				vo.setTotalMaterialStorageMoney(material.get("amount"));
				//商品托数
				Map<String,BigDecimal> product=billReceiveMasterService.queryProduct(billNo);
				vo.setTotalProductStorage(product.get("num"));
				vo.setTotalProductStorageMoney(product.get("amount"));
				//仓租费
				Double rentMoney=billReceiveMasterService.queryStorageRent(billNo);
				vo.setTotalRentMoney(new BigDecimal(rentMoney));
				//仓储理赔费用
				Double storageAbnormal=billReceiveMasterService.queryStorageAbnormalFee(billNo);
				vo.setTotalStorageAbnormalMoney(new BigDecimal(storageAbnormal));
				//宅配理赔费用
				Double dispatchAbnormal=billReceiveMasterService.queryDispatchAbnormalFee(billNo);
				vo.setTotalDispatchAbnormalMoney(new BigDecimal(dispatchAbnormal));
				//运输理赔费用
				Double transportAbnormal=billReceiveMasterService.queryTransportAbnormalFee(billNo);
				vo.setTotalTransportAbnormlMoney(new BigDecimal(transportAbnormal));
				//航空理赔费用
				Double airAbnormal=billReceiveMasterService.queryAirAbnormalFee(billNo);
				vo.setTotalAirAbnormlMoney(new BigDecimal(airAbnormal));
				
				vo.setCreator(param.get("creator").toString());
				vo.setCreateTime(JAppContext.currentTimestamp());
				vo.setDelFlag("0");
				vo.setBillNo(billNo);
				billReceiveMasterService.insertReportMaster(vo);
				
			}else{
				//无论保存成功与否删除所有临时表的数据
				logger.info(billNo+"删除临时表数据");
				billFeesReceiveStorageTempService.deleteBatchTemp(billNo);
				billFeesReceiveDispatchTempService.deleteBatchTemp(billNo);
				billFeesReceiveTransportTempService.deleteBatchTemp(billNo);
				billFeesReceiveAirTempService.deleteBatchTemp(billNo);				
				//更新导入主表		
				billReceiveMasterVo.setTaskStatus(BmsEnums.taskStatus.FAIL.getCode());
				billReceiveMasterVo.setRemark("导入失败:"+param.get("detail").toString());
				billReceiveMasterVo.setTaskRate(99);
			}
			
		}catch (Exception e) {
			logger.error("保存异常信息 {}",e);
			billReceiveMasterVo.setTaskStatus(BmsEnums.taskStatus.FAIL.getCode());
			billReceiveMasterVo.setTaskRate(99);
			billReceiveMasterVo.setRemark(e.getMessage());
		}
		//更新主表
		billReceiveMasterVo.setBillNo(billNo);
		billReceiveMasterVo.setFinishTime(JAppContext.currentTimestamp());
		billReceiveMasterService.update(billReceiveMasterVo);
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
	
	
	public Date getNewDate(String datdString){
		try {
			datdString = datdString.replace("GMT", "").replaceAll("\\(.*\\)", "");
			SimpleDateFormat format = new SimpleDateFormat("EEE MMM dd yyyy HH:mm:ss z", Locale.ENGLISH);
			Date dateTrans = null;
			 dateTrans = format.parse(datdString);
			 String d= new SimpleDateFormat("yyyy-MM-dd").format(dateTrans);
			 SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");//yyyy-mm-dd, 会出现时间不对, 因为小写的mm是代表: 秒
			 Date utilDate = sdf.parse(d);
			 return utilDate;
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("转换异常 {}",e);
		}
		return null;
	}
}
