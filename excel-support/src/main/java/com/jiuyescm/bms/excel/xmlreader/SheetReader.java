package com.jiuyescm.bms.excel.xmlreader;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.google.common.collect.Maps;
import com.jiuyescm.bms.excel.callback.SheetReadCallBack;
import com.jiuyescm.bms.excel.data.DataColumn;
import com.jiuyescm.bms.excel.data.DataRow;
import com.jiuyescm.bms.excel.data.Sheet;

/**
 * 事件模式 读取xl/worksheets/sheet.xml 文档
 * @author caojianwei
 *
 */
public class SheetReader extends DefaultHandler {

	private int curRow=0;		 		//当前行号 从1开始
	private String curColNo = "";		//当前列对应的列值 eg A B AA...
	private String colValue;	 		//单元格的值
	private boolean isString = false; 	//单元格是否为字符串类型 true-字符串 false-非字符串
	
	private int titleRowNo = 1;			// 将第一行到titleRowNo行作为标题行
	private int contentRowNo = 2;		// content开始的行号
	
	private Map<String,DataColumn> cellMap = Maps.newLinkedHashMap(); //(A,dc)
	private Map<String,DataColumn> titleMap = Maps.newLinkedHashMap();
	
	public SheetReadCallBack callback;//回调接口
	private List<String> sst;
	
	public void readSheet(String path,SheetReadCallBack callback,List<String> sst,int titleRowNo,int contentRowNo){
		this.callback = callback;
		this.sst = sst;
		this.titleRowNo = titleRowNo;
		this.contentRowNo = contentRowNo;
		try{
			SAXParserFactory sf = SAXParserFactory.newInstance();   
			SAXParser sp = sf.newSAXParser();
			sp.parse(new InputSource(path), this);
			//callback.finish();
		}
		catch(Exception ex){
			callback.error(ex);
			ex.printStackTrace();
		}
	}
	
	private void headHander(){
		List<String> columns = new ArrayList<>();
		Iterator<Map.Entry<String, DataColumn>> columnIterator = titleMap.entrySet().iterator();
		
		while (columnIterator.hasNext()) {
			Map.Entry<String, DataColumn> entry = columnIterator.next();
			columns.add(entry.getValue().getTitleName());
		}
		callback.readTitle(columns);
	}
	
	private void rowHander(){
		DataRow row = new DataRow();
		row.setRowNo(curRow);
		/*Iterator<Map.Entry<String, DataColumn>> columnIterator = cellMap.entrySet().iterator();
		while (columnIterator.hasNext()) {
			Map.Entry<String, DataColumn> entry = columnIterator.next();
			row.addColumn(entry.getValue());
		}*/
		
		Iterator<Map.Entry<String, DataColumn>> columnIterator = titleMap.entrySet().iterator();
		while (columnIterator.hasNext()) {
			Map.Entry<String, DataColumn> entry = columnIterator.next();
			if(cellMap.containsKey(entry.getKey())){
				row.addColumn(cellMap.get(entry.getKey()));
			}
			else{
				DataColumn cell = new DataColumn(entry.getKey(), entry.getValue().getTitleName(), null);
				row.addColumn(cell);
			}
		}
		callback.read(row);
	}
	
	@Override
	public void startElement(String uri, String localName, String name,Attributes attrs) throws SAXException {
		// <row>:开始处理某一行 
		if ("row".equals(name)) {
			//获取当前行号
			curRow = Integer.valueOf(attrs.getValue("r"));
		}
		// <c>:开始处理某行的某一列
		if ("c".equals(name)) {
			//如果单元格内容为字符串
			curColNo = attrs.getValue("r");
			String cellType = attrs.getValue("t");
			isString = "s".equals(cellType)?true:false;
			curColNo = replaceString(curColNo);
		}
		colValue = "";
	}
	
	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		colValue += new String(ch, start, length);
	}
	
	@Override
	public void endElement(String uri, String localName, String name) throws SAXException {
		if("v".equals(name)){
			if(colValue == null || colValue.length() == 0){
				return;
			}
			String value = isString?sst.get(Integer.valueOf(colValue)):colValue;
			if(curRow <= titleRowNo){
				DataColumn cell = new DataColumn(curColNo, value, value);
				titleMap.put(curColNo, cell);
			}
			else{
				if(titleMap.containsKey(curColNo)){
					//读取行内容
					DataColumn cell = new DataColumn(curColNo, titleMap.get(curColNo).getTitleName(), value);
					cellMap.put(curColNo, cell);
				}
			}
		}
		if ("row".equals(name)) {
			//读完表头
			if(curRow == titleRowNo){
				headHander();
				return;
			}
			if(curRow >= contentRowNo){
				rowHander();
			}
			cellMap = Maps.newLinkedHashMap();
			cellMap.clear();
		}
	}
	
	private String replaceString(String titleRef){
		titleRef = titleRef.replaceAll("\\d+","");
		return titleRef;
	}

	public int getRowCount() {
		return curRow-titleRowNo;
	}
	
	
}
