package com.jiuyescm.bms.excel.xmlreader;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import com.sun.org.apache.xerces.internal.jaxp.SAXParserFactoryImpl;

/**
 * 事件模式 读取xl/sharedStrings.xml文档
 * @author caojianwei
 *
 */
public class ShareStringsReader extends DefaultHandler  {
	
	private String shareStringPath = ""; 
	private List<String> shareStrings = new ArrayList<String>();  
	
	public ShareStringsReader(String path) {   
		this.shareStringPath = path;
		readShareStrings(shareStringPath);
	} 
	
	public List<String> getSST(){
		return shareStrings;
	}
	
	public void readShareStrings(String path){
		try{
			SAXParserFactory sf = SAXParserFactory.newInstance();   
			SAXParser sp = sf.newSAXParser();
			sp.parse(new InputSource(path), this);
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	private String _shareString = "";
	private String str = "";
	
	@Override
	public void startElement(String uri, String localName, String name,Attributes attrs) throws SAXException {
		if ("si".equals(name)) {
			_shareString = "";
			str = "";
		}
	}
	
	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		_shareString = new String(ch, start, length);
	}
	
	@Override
	public void endElement(String uri, String localName, String name) throws SAXException {
		if ("t".equals(name)) {
			str += _shareString;
		}
		if("si".equals(name)){
			shareStrings.add(str);
		}
		
		
	}
	
}
