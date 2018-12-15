package com.jiuyescm.bms.report.bill.web;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
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
import com.jiuyescm.bms.report.bill.IReportBillImportDetailService;
import com.jiuyescm.bms.report.vo.ReportBillReceiptDetailVo;
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
	
	
	@SuppressWarnings("unchecked")
	@FileProvider
	public DownloadFile getFile(Map<String, String> parameter) throws IOException{
		parameter.put("url", "group1/M00/03/70/wKgAMFwIvQOAbnKnAABABDxsRQg70.xlsx");
		parameter.put("excelName", "账单报表导出模板.xlsx");
		
		// TODO Auto-generated method stub
/*		*/
		
		final String fileName=parameter.get("excelName");
		byte[] bytes=storageClient.downloadFile(parameter.get("url"),new DownloadByteArray());
		try{
			XSSFWorkbook xssfWorkbook = new XSSFWorkbook(new ByteArrayInputStream(bytes));
			
			//=================================第一个sheet写数据===========================
			Map<String, Object> condition=new HashMap<String, Object>();
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
		    			newrow.createCell(cell.getColumnIndex()).setCellValue(map.get(cell.getStringCellValue()).toString());
		    		}
		    	}
		    }		    
		    sheet1.shiftRows(firstRow1.getRowNum()+1, firstRow1.getRowNum()+1+list1.size(), -1);//删除第一行，然后使下方单元格上移
		    
		    
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
