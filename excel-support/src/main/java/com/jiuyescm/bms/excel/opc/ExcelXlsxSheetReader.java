package com.jiuyescm.bms.excel.opc;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.BuiltinFormats;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.model.SharedStringsTable;
import org.apache.poi.xssf.model.StylesTable;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

import com.google.common.collect.Maps;
import com.jiuyescm.bms.excel.callback.SheetReadCallBack;
import com.jiuyescm.bms.excel.data.DataColumn;
import com.jiuyescm.bms.excel.data.DataRow;

public class ExcelXlsxSheetReader extends DefaultHandler {

	/**
	 * 单元格中的数据可能的数据类型
	 */
	enum CellDataType {
		BOOL, ERROR, FORMULA, INLINESTR, SSTINDEX, NUMBER, DATE, NULL
	}

	Map<Integer, List<String>> list = new LinkedHashMap<Integer, List<String>>();

	public SheetReadCallBack callback;

	private SharedStringsTable sst; // 共享字符串表
	private String lastIndex; // 上一次的索引值
	//private String filePath = "";// 文件的绝对路径
	//private int sheetIndex = 0;// 工作表索引
	//private String sheetName = "";// sheet名
	//private int totalRows = 0;// 总行数
	//private List<String> cellList = new ArrayList<String>();// 一行内cell集合
	private Map<String, String> cellMap = Maps.newLinkedHashMap();
	private boolean flag = false;// 判断整行是否为空行的标记
	private int curRow = 1;// 当前行
	private int curCol = 1;// 当前列
	private boolean isTElement;// T元素标识
	private String exceptionMessage;// 异常信息，如果为空则表示没有异常
	
	DataColumn _dc = new DataColumn();
	private Map<Integer, DataColumn> curDataRow = new HashMap<>();

	/**
	 * 单元格数据类型，默认为字符串类型
	 */
	private CellDataType nextDataType = CellDataType.SSTINDEX;
	private final DataFormatter formatter = new DataFormatter();
	private short formatIndex;// 单元格日期格式的索引
	private String formatString;// 日期格式字符串
	// 定义前一个元素和当前元素的位置，用来计算其中空的单元格数量，如A6和A8等
	private String preRef = null, ref = null;
	// 定义该文档一行最大的单元格数，用来补全一行最后可能缺失的单元格
	private String maxRef = null;
	private StylesTable stylesTable;// 单元格
	private OpcSheet sheet;
	// 注解名与字段名映射 <注解名,字段名>
	//private Map<String, String> titleMap = new HashMap<String, String>();

	// 列索引和列名映射
	private Map<String,Integer> headMap = new HashMap<>();
	
	//列索引和列名映射
	private Map<String, String> headAMap = new LinkedHashMap<>();

	boolean transObj = false; // 是否将行转为obj对象 true-转 false-不转
	Object sheetObj;

	private int titleRowNo;// title的行号
	private int contentRowNo;// content开始的行号

	/**
	 * 遍历工作簿中所有的电子表格 并缓存在mySheetList中
	 * 
	 * @param filename
	 * @throws Exception
	 */
	public void readSheet(int index, OPCPackage pkg, XSSFReader xssfReader,
			SharedStringsTable sst, SheetReadCallBack callback, int titleRowNo,
			int contentRowNo) throws Exception {
		this.titleRowNo = titleRowNo;
		this.contentRowNo = contentRowNo;
		this.callback = callback;
		sheet = new OpcSheet(index, null);
		this.sst = sst;
		stylesTable = xssfReader.getStylesTable();
		XMLReader parser = XMLReaderFactory
				.createXMLReader("org.apache.xerces.parsers.SAXParser");
		parser.setContentHandler(this);

		InputStream sheetStream = xssfReader.getSheet("rId" + index);
		InputSource sheetSource = new InputSource(sheetStream);
		parser.parse(sheetSource);
		callback.finish();
	}

	private void headHander(Map<String, String> cellMap) {

		Iterator<Map.Entry<String, String>> columnIterator = cellMap.entrySet().iterator();
		while (columnIterator.hasNext()) {
			Map.Entry<String, String> entry = columnIterator.next();
			String name = entry.getValue();//列名
			String titleA = entry.getKey();//列的索引
			if(name.length()>0){
				headAMap.put(titleA, entry.getValue());
			}
		}
	}

	private void rowHander(Map<String, String> cellMap) {
		DataRow drRow = new DataRow(curRow);
		
		Iterator<Map.Entry<String, String>> headIterator = headAMap.entrySet().iterator();
		while (headIterator.hasNext()) {
			Map.Entry<String, String> entry = headIterator.next();
			String titleA = entry.getKey();
			String colName = entry.getValue();
			Integer colNo = headMap.get(titleA);
			String colValue="";
			if(cellMap.containsKey(titleA)){
				colValue = cellMap.get(titleA);
			}
			DataColumn dColumn = new DataColumn(colName,colValue);
			dColumn.setColNo(colNo);
			drRow.addColumn(colNo,dColumn);
		}
		callback.read(drRow);
	}

	/**
	 * 第一个执行
	 * 
	 * @param uri
	 * @param localName
	 * @param name
	 * @param attributes
	 * @throws SAXException
	 */
	@Override
	public void startElement(String uri, String localName, String name,
			Attributes attributes) throws SAXException {
		// c => 单元格

		if ("c".equals(name)) {
			// 前一个单元格的位置
			if (preRef == null) {
				preRef = attributes.getValue("r");
				preRef = replaceString(preRef);
			} else {
				preRef = ref;
			}

			// 当前单元格的位置
			ref = attributes.getValue("r");
			ref = replaceString(ref);
			// System.out.println("【startElement】  localName:"+localName+"  name:"+name+"  ref:"+ref+"  preRef:"+preRef);
			// 设定单元格类型
			this.setNextDataType(attributes);
		}

		// 当元素为t时
		if ("t".equals(name)) {
			isTElement = true;
		} else {
			isTElement = false;
		}

		// 置空
		lastIndex = "";
	}

	/**
	 * 第二个执行 得到单元格对应的索引值或是内容值 如果单元格类型是字符串、INLINESTR、数字、日期，lastIndex则是索引值
	 * 如果单元格类型是布尔值、错误、公式，lastIndex则是内容值
	 * 
	 * @param ch
	 * @param start
	 * @param length
	 * @throws SAXException
	 */
	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		lastIndex += new String(ch, start, length);
	}

	/**
	 * 第三个执行
	 * 
	 * @param uri
	 * @param localName
	 * @param name
	 * @throws SAXException
	 */
	@Override
	public void endElement(String uri, String localName, String name)
			throws SAXException {

		if ("c".equals(name)) {
			DataColumn dc = new DataColumn();
			dc.setColNo(curCol);
			curDataRow.put(curCol,dc);
			if(curRow == 1){
				headMap.put(ref,curCol);
				headAMap.put(ref,"");
			}
			curCol++;
		}
		// t元素也包含字符串
		if (isTElement) {// 这个程序没经过
			// 将单元格内容加入rowlist中，在这之前先去掉字符串前后的空白符
			String value = lastIndex.trim();
			cellMap.put(ref, value);
			// curCol++;
			isTElement = false;
			// 如果里面某个单元格含有值，则标识该行不为空行
			if (value != null && !"".equals(value)) {
				flag = true;
			}
		} else if ("v".equals(name)) {
			// v => 单元格的值，如果单元格是字符串，则v标签的值为该字符串在SST中的索引
			String value = this.getDataValue(lastIndex.trim(), "");// 根据索引值获取对应的单元格值
			// 补全单元格之间的空单元格
			if (!ref.equals(preRef)) {
				int len = countNullCell(ref, preRef);
				for (int i = 0; i < len; i++) {
					cellMap.put(ref, value);
					// curCol++;
				}
			}
			cellMap.put(ref, value);
			// 如果里面某个单元格含有值，则标识该行不为空行
			if (value != null && !"".equals(value)) {
				flag = true;
			}
		} else {
			// 如果标签名称为row，这说明已到行尾，调用optRows()方法
			if ("row".equals(name)) {
				// 默认第一行为表头，以该行单元格数目为最大数目
				if (curRow == 1) {
					maxRef = ref;
				}
				// 补全一行尾部可能缺失的单元格
				if (maxRef != null) {
					int len = countNullCell(maxRef, ref);
					for (int i = 0; i <= len; i++) {
						cellMap.put(ref, "");
					}
				}
				if (curRow <= titleRowNo) {
					headHander(cellMap);
				}
				if (flag && curRow >= contentRowNo) { // 该行不为空行且该行不是第一行，则发送（第一行为列名，不需要）
					rowHander(cellMap);
					//totalRows++;
				}
				curRow++;
				// 初始化
				cellMap = Maps.newLinkedHashMap();
				curCol = 1;
				preRef = null;
				ref = null;
				flag = false;
			}
		}
	}

	/*private String rowToString(Map<Integer, String> map) {

		return map.toString();
	}*/

	/**
	 * 处理数据类型
	 * 
	 * @param attributes
	 */
	public void setNextDataType(Attributes attributes) {
		nextDataType = CellDataType.NUMBER; // cellType为空，则表示该单元格类型为数字
		formatIndex = -1;
		formatString = null;
		String cellType = attributes.getValue("t"); // 单元格类型
		String cellStyleStr = attributes.getValue("s"); //
		String columnData = attributes.getValue("r"); // 获取单元格的位置，如A1,B1

		if ("b".equals(cellType)) { // 处理布尔值
			nextDataType = CellDataType.BOOL;
		} else if ("e".equals(cellType)) { // 处理错误
			nextDataType = CellDataType.ERROR;
		} else if ("inlineStr".equals(cellType)) {
			nextDataType = CellDataType.INLINESTR;
		} else if ("s".equals(cellType)) { // 处理字符串
			nextDataType = CellDataType.SSTINDEX;
		} else if ("str".equals(cellType)) {
			nextDataType = CellDataType.FORMULA;
		}

		if (cellStyleStr != null) { // 处理日期
			int styleIndex = Integer.parseInt(cellStyleStr);
			XSSFCellStyle style = stylesTable.getStyleAt(styleIndex);
			formatIndex = style.getDataFormat();
			formatString = style.getDataFormatString();

			if (formatString.contains("m/d/yy")) {
				nextDataType = CellDataType.DATE;
				formatString = "yyyy-MM-dd hh:mm:ss";
			}

			if (formatString == null) {
				nextDataType = CellDataType.NULL;
				formatString = BuiltinFormats.getBuiltinFormat(formatIndex);
			}
		}
	}

	/**
	 * 对解析出来的数据进行类型处理
	 * 
	 * @param value
	 *            单元格的值， value代表解析：BOOL的为0或1，
	 *            ERROR的为内容值，FORMULA的为内容值，INLINESTR的为索引值需转换为内容值，
	 *            SSTINDEX的为索引值需转换为内容值， NUMBER为内容值，DATE为内容值
	 * @param thisStr
	 *            一个空字符串
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public String getDataValue(String value, String thisStr) {
		switch (nextDataType) {
		// 这几个的顺序不能随便交换，交换了很可能会导致数据错误
		case BOOL: // 布尔值
			char first = value.charAt(0);
			thisStr = first == '0' ? "FALSE" : "TRUE";
			break;
		case ERROR: // 错误
			thisStr = "\"ERROR:" + value.toString() + '"';
			break;
		case FORMULA: // 公式
			thisStr = '"' + value.toString() + '"';
			break;
		case INLINESTR:
			XSSFRichTextString rtsi = new XSSFRichTextString(value.toString());
			thisStr = rtsi.toString();
			rtsi = null;
			break;
		case SSTINDEX: // 字符串
			String sstIndex = value.toString();
			try {
				int idx = Integer.parseInt(sstIndex);
				XSSFRichTextString rtss = new XSSFRichTextString(
						sst.getEntryAt(idx));// 根据idx索引值获取内容值
				thisStr = rtss.toString();
				rtss = null;
			} catch (NumberFormatException ex) {
				thisStr = value.toString();
			}
			break;
		case NUMBER: // 数字
			if (formatString != null) {
				thisStr = formatter.formatRawCellContents(
						Double.parseDouble(value), formatIndex, formatString)
						.trim();
			} else {
				thisStr = value;
			}
			thisStr = thisStr.replace("_", "").trim();
			break;
		case DATE: // 日期
			thisStr = formatter.formatRawCellContents(
					Double.parseDouble(value), formatIndex, formatString);
			// 对日期字符串作特殊处理，去掉T
			thisStr = thisStr.replace("T", " ");

//			List<String> list = new ArrayList<String>();
//			Pattern p = Pattern.compile("[^0-9]");
//			Matcher m = p.matcher(thisStr);
//			String result = m.replaceAll("");
//			for (int i = 0; i < result.length(); i++) {
//				list.add(result.substring(i, i + 1));
//			}
//			thisStr="";
//			if(list.size()==8){
//				thisStr=list.get(0)+list.get(1)+list.get(2)+list.get(3)+"-"+list.get(4)+list.get(5)+"-"+list.get(6)+list.get(7)+" 00:00:00";
//			}else if(list.size()>=14){
//				thisStr=list.get(0)+list.get(1)+list.get(2)+list.get(3)+"-"+list.get(4)+list.get(5)+"-"+list.get(6)+list.get(7)+" "+list.get(8)+list.get(9)+":"+list.get(10)+list.get(11)+":"+list.get(12)+list.get(13);	
//			}
			break;
		default:
			thisStr = " ";
			break;
		}
		return thisStr;
	}

	public int countNullCell(String ref, String preRef) {
		// excel2007最大行数是1048576，最大列数是16384，最后一列列名是XFD
		String xfd = ref.replaceAll("\\d+", "");
		String xfd_1 = preRef.replaceAll("\\d+", "");

		xfd = fillChar(xfd, 3, '@', true);
		xfd_1 = fillChar(xfd_1, 3, '@', true);

		char[] letter = xfd.toCharArray();
		char[] letter_1 = xfd_1.toCharArray();
		int res = (letter[0] - letter_1[0]) * 26 * 26
				+ (letter[1] - letter_1[1]) * 26 + (letter[2] - letter_1[2]);
		return res - 1;
	}

	public String fillChar(String str, int len, char let, boolean isPre) {
		int len_1 = str.length();
		if (len_1 < len) {
			if (isPre) {
				for (int i = 0; i < (len - len_1); i++) {
					str = let + str;
				}
			} else {
				for (int i = 0; i < (len - len_1); i++) {
					str = str + let;
				}
			}
		}
		return str;
	}

	/**
	 * @return the exceptionMessage
	 */
	public String getExceptionMessage() {
		return exceptionMessage;
	}
	
	private String replaceString(String titleRef){
		titleRef = titleRef.replaceAll("\\d+","");
		return titleRef;
	}
}
