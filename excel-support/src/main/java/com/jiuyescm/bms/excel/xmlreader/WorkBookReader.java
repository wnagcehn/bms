package com.jiuyescm.bms.excel.xmlreader;

import java.io.*;
import java.util.*;

import org.dom4j.*;
import org.dom4j.io.*;

import com.jiuyescm.bms.excel.data.Sheet;


/**
 * dom模式读取 xl/workbook.xml文档
 * 
 * @author caojianwei
 *
 */
public class WorkBookReader {

	private List<Sheet> sheets = new ArrayList<>(); // workbook中包含的sheet
	private String workBookPath; // workbook.xml 文件路径(xl目录路径）

	private String relsPath = "xl/_rels/workbook.xml.rels";
	private String workBookName = "xl/workbook.xml";
	
	private Map<String, String> sheetPathMap = new HashMap<String, String>();
	private Map<Integer, String> sheetIdMap = new HashMap<Integer, String>();

	public List<Sheet> getSheets() {
		return sheets;
	}

	public String getWorkBookPath() {
		return workBookPath;
	}
	
	public Map<Integer, String> getSheetIdMap(){
		return sheetIdMap;
	}
	
	private void readSheetRels(String path) throws Exception {
		String sheetPath = path + relsPath;
		try {
			File f = new File(sheetPath);
			SAXReader reader = new SAXReader();
			Document doc = reader.read(f);
			Element root = doc.getRootElement();
			Element sheetElement;
			for (Iterator i = root.elementIterator("Relationship"); i.hasNext();) {
				sheetElement = (Element) i.next();
				String sheetId = sheetElement.attributeValue("Id");
				String target = sheetElement.attributeValue("Target");
				sheetPathMap.put(sheetId, target);
			}
		} catch (Exception e) {
			throw e;
		}
	}

	public WorkBookReader(String path) throws Exception {
		try {
			readSheetRels(path);
			File f = new File(path+workBookName);
			SAXReader reader = new SAXReader();
			Document doc = reader.read(f);
			Element root = doc.getRootElement();
			Element sheetsRoot = root.element("sheets");
			Element sheetElement;
			int index = 1;
			for (Iterator i = sheetsRoot.elementIterator("sheet"); i.hasNext();) {
				sheetElement = (Element) i.next();
				Integer sheetId = index;
				String sheetPath = sheetPathMap.get(sheetElement.attributeValue("id"));
				String sheetName = sheetElement.attributeValue("name");
				String sheetNo = String.valueOf(sheetId);
				sheetIdMap.put(sheetId, sheetPath);
				//String sheetNo =sheetElement.attributeValue("id");
				//Integer sheetId = Integer.valueOf(sheetNo.replace("rId", ""));
				Sheet sheet = new Sheet(sheetNo, sheetId, sheetName);
				sheet.setSheetPath(sheetPath);
				sheets.add(sheet);
				index++;
			}
		} catch (Exception e) {
			throw e;
		}
	}

}
