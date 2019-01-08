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

	private String workBookName = "workbook.xml";

	public List<Sheet> getSheets() {
		return sheets;
	}

	public String getWorkBookPath() {
		return workBookPath;
	}

	public WorkBookReader(String path) throws Exception {
		this.workBookPath = path;
		try {
			File f = new File(workBookPath);
			SAXReader reader = new SAXReader();
			Document doc = reader.read(f);
			Element root = doc.getRootElement();
			Element sheetsRoot = root.element("sheets");
			Element sheetElement;
			for (Iterator i = sheetsRoot.elementIterator("sheet"); i.hasNext();) {
				sheetElement = (Element) i.next();
				String sheetName = sheetElement.attributeValue("name");
				Integer sheetId = Integer.valueOf(sheetElement.attributeValue("sheetId"));
				String sheetNo = "rId" + sheetId;
				Sheet sheet = new Sheet(sheetNo, sheetId, sheetName);
				sheets.add(sheet);
			}
		} catch (Exception e) {
			throw e;
		}
	}

}
