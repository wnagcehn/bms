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
		
		final String fileName=parameter.get("excelName");
		byte[] bytes=storageClient.downloadFile(parameter.get("url"),new DownloadByteArray());
		try{
			XSSFWorkbook xssfWorkbook = new XSSFWorkbook(new ByteArrayInputStream(bytes));
			
			//第一个sheet写数据
		    XSSFSheet sheet0=xssfWorkbook.getSheetAt(0);
		    XSSFRow row=sheet0.getRow(1);
		    Iterator<Cell> cellList=row.iterator();	
		    //第一行对应得字段
		    List<Cell> myList=IteratorUtils.toList(cellList);		    		    
		    for(int i=0;i<list1.size();i++){
		    	Map<String,Object> map=list1.get(i);
		    	XSSFRow newrow =sheet0.createRow(i+2);
		    	for(Cell cell:myList){
		    		if(map.containsKey(cell.getStringCellValue())){
		    			newrow.createCell(cell.getColumnIndex()).setCellValue(map.get(cell.getStringCellValue()).toString());
		    		}
		    	}
		    }		    
		    sheet0.shiftRows(row.getRowNum()+1, row.getRowNum()+1+list1.size(), -1);//删除第一行，然后使下方单元格上移
		       
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
