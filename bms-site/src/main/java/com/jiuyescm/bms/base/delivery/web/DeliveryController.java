package com.jiuyescm.bms.base.delivery.web;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.IteratorUtils;
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
import com.jiuyescm.cfm.common.JAppContext;
import com.jiuyescm.framework.fastdfs.client.StorageClient;
import com.jiuyescm.framework.fastdfs.protocol.storage.callback.DownloadByteArray;
import com.jiuyescm.mdm.deliver.api.IDeliverService;
import com.jiuyescm.mdm.deliver.vo.DeliverVo;

@Controller("deliveryController")
public class DeliveryController {
	@Autowired private StorageClient storageClient;
	@Resource private IDeliverService deliverService;
	
	@DataProvider
	public void query(Page<DeliverVo> page,Map<String,Object> parameter) {
		if(null==parameter){
			parameter=new HashMap<String,Object>();
		}
		
		String userid=JAppContext.currentUserID();
		parameter.put("userid", userid);
		PageInfo<DeliverVo> tmpPageInfo = deliverService.queryDeliver(parameter, page.getPageNo(), page.getPageSize());
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
			List<Map<String,Object>> list1=new ArrayList<Map<String,Object>>();
			Map<String,Object> map1=new HashMap<String,Object>();
			map1.put("createMonth", "1809");
			map1.put("invoiceName", "北京行知守仁科技有限公司");
			map1.put("billName", "北京行知守仁科技有限公司");
			map1.put("billStartTime", JAppContext.currentTimestamp());
			Map<String,Object> map2=new HashMap<String,Object>();
			map2.put("createMonth", "1801");
			map2.put("invoiceName", "上海乐亲电子商务有限公司");
			map2.put("billName", "上海乐亲电子商务有限公司");
			map2.put("unInvoiceAmount", 12.45);
			list1.add(map1);	
			list1.add(map2);
			
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
		    
		    
			//=================================第二个sheet写数据===========================
		    List<Map<String,Object>> list2=new ArrayList<Map<String,Object>>();
			Map<String,Object> map21=new HashMap<String,Object>();
			map21.put("createMonth", "1809");
			map21.put("invoiceName", "北京我爱小城信息科技有限公司");
			map21.put("billName", "北京我爱小城信息科技有限公司");
			map21.put("invoiceStorage", 11.45);
			Map<String,Object> map22=new HashMap<String,Object>();
			map22.put("createMonth", "1801");
			map22.put("invoiceName", "新疆晨报苹果（个人）");
			map22.put("billName", "新疆晨报苹果（个人）");
			map22.put("shunfengCount", 12);
			list2.add(map21);	
			list2.add(map22);
			//第二个sheet写数据
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
		    			newrow.createCell(cell.getColumnIndex()).setCellValue(map.get(cell.getStringCellValue()).toString());
		    		}
		    	}
		    }		    
		    sheet2.shiftRows(firstRow2.getRowNum()+1, firstRow2.getRowNum()+1+list2.size(), -1);//删除第一行，然后使下方单元格上移
		   
			//=================================第三个sheet写数据===========================
		    List<Map<String,Object>> list3=new ArrayList<Map<String,Object>>();
			Map<String,Object> map31=new HashMap<String,Object>();
			map31.put("createMonth", "1809");
			map31.put("invoiceName", "深圳壹心贸易有限公司");
			map31.put("billName", "深圳壹心贸易有限公司");
			map31.put("abnormalDispatch", 11.45);
			Map<String,Object> map32=new HashMap<String,Object>();
			map32.put("createMonth", "1801");
			map32.put("invoiceName", "新疆叫了只炸鸡");
			map32.put("billName", "新疆叫了只炸鸡");
			map32.put("shunfengCount", 12);
			map32.put("transferPallet", 12);	
			list3.add(map31);	
			list3.add(map32);
			//第二个sheet写数据
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
		    			newrow.createCell(cell.getColumnIndex()).setCellValue(map.get(cell.getStringCellValue()).toString());
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
