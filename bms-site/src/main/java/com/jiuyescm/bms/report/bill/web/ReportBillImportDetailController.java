package com.jiuyescm.bms.report.bill.web;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.IteratorUtils;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.data.provider.Page;
import com.bstek.dorado.uploader.DownloadFile;
import com.bstek.dorado.uploader.annotation.FileProvider;
import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.file.templet.BmsTempletInfoEntity;
import com.jiuyescm.bms.file.templet.IBmsTempletInfoService;
import com.jiuyescm.bms.report.bill.IReportBillImportDetailService;
import com.jiuyescm.bms.report.vo.ReportBillBizDetailVo;
import com.jiuyescm.bms.report.vo.ReportBillReceiptDetailVo;
import com.jiuyescm.bms.report.vo.ReportBillStorageDetailVo;
import com.jiuyescm.framework.fastdfs.client.StorageClient;
import com.jiuyescm.framework.fastdfs.protocol.storage.callback.DownloadByteArray;

/**
 * 应收账单明细报表
 * @author zhaofeng
 *
 */
@Controller("reportBillImportDetailController")
public class ReportBillImportDetailController {
	private static final Logger logger = Logger.getLogger(ReportBillImportDetailController.class.getName());
	
	@Resource
	private IReportBillImportDetailService reportBillImportDetailService;
	@Autowired private StorageClient storageClient;
	@Autowired private IBmsTempletInfoService bmsTempletInfoService;

	/**
	 * 查询收入明细
	 * @param page
	 * @param parameter
	 */
	@DataProvider
	public void queryReceiptDetail(Page<ReportBillReceiptDetailVo> page, Map<String, Object> parameter) {
		PageInfo<ReportBillReceiptDetailVo> tmpPageInfo = reportBillImportDetailService.queryReceipt(parameter, page.getPageNo(), page.getPageSize());
		if (tmpPageInfo != null) {
			page.setEntities(tmpPageInfo.getList());
			page.setEntityCount((int) tmpPageInfo.getTotal());
		}
	}
	
	/**
	 * 查询仓储明细
	 * @param page
	 * @param parameter
	 */
	@DataProvider
	public void queryStorageDetail(Page<ReportBillStorageDetailVo> page, Map<String, Object> parameter) {
		PageInfo<ReportBillStorageDetailVo> tmpPageInfo = reportBillImportDetailService.queryStorage(parameter, page.getPageNo(), page.getPageSize());
		if (tmpPageInfo != null) {
			page.setEntities(tmpPageInfo.getList());
			page.setEntityCount((int) tmpPageInfo.getTotal());
		}
	}
	
	/**
	 * 查询业务明细
	 * @param page
	 * @param parameter
	 */
	@DataProvider
	public void queryBizDetail(Page<ReportBillBizDetailVo> page, Map<String, Object> parameter) {
		PageInfo<ReportBillBizDetailVo> tmpPageInfo = reportBillImportDetailService.queryBiz(parameter, page.getPageNo(), page.getPageSize());
		if (tmpPageInfo != null) {
			page.setEntities(tmpPageInfo.getList());
			page.setEntityCount((int) tmpPageInfo.getTotal());
		}
	}
	
	@SuppressWarnings("unchecked")
	@FileProvider
	public DownloadFile getFile(Map<String, Object> parameter) throws IOException{
		
		
		BmsTempletInfoEntity template=bmsTempletInfoService.findByCode("bill_report_export");

		final String fileName=template.getExcelName();
		byte[] bytes=storageClient.downloadFile(template.getUrl(),new DownloadByteArray());
		try{
			XSSFWorkbook xssfWorkbook = new XSSFWorkbook(new ByteArrayInputStream(bytes));
			
			//=================================第一个sheet写数据===========================
			Map<String, Object> condition=new HashMap<String, Object>();
			condition.put("createMonth", parameter.get("receiptCreateMonth"));
			condition.put("invoiceName", parameter.get("receiptInvoiceName"));
			List<Map<String,Object>> list1=reportBillImportDetailService.queryReceiptExport(condition);
			
		    XSSFSheet sheet1=xssfWorkbook.getSheetAt(0);
		    XSSFRow firstRow1=sheet1.getRow(1);
		    Iterator<Cell> cellList1=firstRow1.iterator();	
		    //第一行对应得字段
		    List<Cell> myList1=IteratorUtils.toList(cellList1);		    		    
		    for(int i=0;i<list1.size();i++){
		    	Map<String,Object> map=list1.get(i);
		    	XSSFRow newrow =sheet1.createRow(i+2);
		    	for(Cell cell:myList1){
		    		if(map.containsKey(cell.getStringCellValue())){
		    			if (null != map.get(cell.getStringCellValue()) && map.get(cell.getStringCellValue()) instanceof BigDecimal) {
		    				newrow.createCell(cell.getColumnIndex()).setCellValue(new BigDecimal(map.get(cell.getStringCellValue()).toString()).setScale(2, BigDecimal.ROUND_HALF_UP).toString());
						}else {
							newrow.createCell(cell.getColumnIndex()).setCellValue(map.get(cell.getStringCellValue()).toString());
						}	
		    		}
		    	}
		    }		    
		    sheet1.shiftRows(firstRow1.getRowNum()+1, firstRow1.getRowNum()+1+list1.size(), -1);//删除第一行，然后使下方单元格上移		    
		    
		    
			//=================================第二个sheet写数据===========================
		    condition.clear();			
		    condition.put("createMonth", parameter.get("storageCreateMonth"));
			condition.put("invoiceName", parameter.get("storageInvoiceName"));
			List<Map<String,Object>> list2=reportBillImportDetailService.queryStorageExport(condition);
			
		    XSSFSheet sheet2=xssfWorkbook.getSheetAt(1);
		    XSSFRow firstRow2=sheet2.getRow(2);
		    Iterator<Cell> cellList2=firstRow2.iterator();	
		    //第一行对应得字段
		    List<Cell> myList2=IteratorUtils.toList(cellList2);		    		    
		    for(int i=0;i<list2.size();i++){
		    	Map<String,Object> map=list2.get(i);
		    	
		    	XSSFRow newrow =sheet2.createRow(i+3);
		    	for(Cell cell:myList2){
		    		if(map.containsKey(cell.getStringCellValue())){
		    			if (null != map.get(cell.getStringCellValue()) && map.get(cell.getStringCellValue()) instanceof BigDecimal) {
		    				newrow.createCell(cell.getColumnIndex()).setCellValue(new BigDecimal(map.get(cell.getStringCellValue()).toString()).setScale(2, BigDecimal.ROUND_HALF_UP).toString());
						}else {
							newrow.createCell(cell.getColumnIndex()).setCellValue(map.get(cell.getStringCellValue()).toString());
						}	
		    		}
		    	}
		    }		    
		    sheet2.shiftRows(firstRow2.getRowNum()+1, firstRow2.getRowNum()+1+list2.size(), -1);//删除第一行，然后使下方单元格上移
		    
			//=================================第三个sheet写数据===========================
		    condition.clear();			
		    condition.put("createMonth", parameter.get("bizCreateMonth"));
			condition.put("billName", parameter.get("bizBillName"));
			List<Map<String,Object>> list3=reportBillImportDetailService.queryBizExport(condition);
			
		    XSSFSheet sheet3=xssfWorkbook.getSheetAt(2);
		    XSSFRow firstRow3=sheet3.getRow(3);
		    Iterator<Cell> cellList3=firstRow3.iterator();	
		    //第一行对应得字段
		    List<Cell> myList3=IteratorUtils.toList(cellList3);		    		    
		    for(int i=0;i<list3.size();i++){
		    	Map<String,Object> map=list3.get(i);
		    	XSSFRow newrow =sheet3.createRow(i+4);
		    	for(Cell cell:myList3){
		    		if(map.containsKey(cell.getStringCellValue())){
		    			if (null != map.get(cell.getStringCellValue()) && map.get(cell.getStringCellValue()) instanceof BigDecimal) {
		    				newrow.createCell(cell.getColumnIndex()).setCellValue(new BigDecimal(map.get(cell.getStringCellValue()).toString()).setScale(2, BigDecimal.ROUND_HALF_UP).toString());
						}else {
							newrow.createCell(cell.getColumnIndex()).setCellValue(map.get(cell.getStringCellValue()).toString());
						}	
		    		}
		    	}
		    }		    
		    sheet3.shiftRows(firstRow3.getRowNum()+1, firstRow3.getRowNum()+1+list3.size(), -1);//删除第一行，然后使下方单元格上移
		    
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			xssfWorkbook.write(os);
			byte[] b1 = os.toByteArray();
			return new DownloadFile(fileName, new ByteArrayInputStream(b1));
		}
		catch(Exception ex){
			return new DownloadFile(fileName, new ByteArrayInputStream(bytes));
		}
	}
}
