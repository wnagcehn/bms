package com.jiuyescm.common.xml;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
/**
 * XML与实体映射类
 * @author zhengyishan
 *
 */
public class XStreamUtils {
    private XStream xStream=null;
	
	public static XStreamUtils getInstance() {
		return new XStreamUtils();
	}
	/**
	 * 实例化一个XStreamUtils
	 */
	private XStreamUtils(){
		//指定一个解析器new DomDriver()
		xStream=new XStream(new DomDriver());
	}
	
	/**
	 * 根据Xml转换成Object实体
	 * @author 
	 * @param clazz
	 * @param xml
	 * @return
	 */
	public Object fromXml(Class<?> clazz,String xml){
		xStream.aliasPackage("", clazz.getPackage().getName());
		return xStream.fromXML(xml);
	}
	
	/**
	 * 替换XStream在转换成XML时的包信息为""
	 * @author 
	 * @param xstream
	 * @param obj
	 * @return
	 */
	public String toXml(Object obj){
		return toXml(xStream, obj, "");
	} 
	/**
	 * 替换XStream在转换成XML时的包信息为""
	 * @author 
	 * @param xstream
	 * @param obj
	 * @return
	 */
	public String toXml(XStream xstream,Object obj){
		return toXml(xstream, obj, "");
	} 
	/**
	 * 替换XStream在转换成XML时的包信息
	 * @author 
	 * @param xstream XStream工具对象
	 * @param obj 需要转换的实体
	 * @param packageStr 需要替换的包信息
	 * @return
	 */
	public String toXml(Object obj,String packageStr){
		if(packageStr==null){
			packageStr="";
		}
		return toXml(xStream, obj, packageStr);
	} 
	/**
	 * 替换XStream在转换成XML时的包信息
	 * @author 
	 * @param xstream XStream工具对象
	 * @param obj 需要转换的实体
	 * @param packageStr 需要替换的包信息
	 * @return
	 */
	public String toXml(XStream xstream,Object obj,String packageStr){
		if(packageStr==null){
			packageStr="";
		}
		xstream.aliasPackage(packageStr, obj.getClass().getPackage().getName());
		return xstream.toXML(obj);
	} 
	/**
	 * 替换XStream在转换成XML时的包信息,适用于多种实体替换成不同的包
	 * @author 
	 * @param xstream XStream工具对象
	 * @param obj 需要转换的实体--根实体
	 * @param packageStr 需要替换的包信息
	 * @param objPackage 替换前的包信息
	 * @return
	 */
	public String toXml(String[] objPackage,String[] packageStr,Object obj){
		if(objPackage==null||packageStr==null||objPackage.length==0||packageStr.length==0||objPackage.length!=packageStr.length){
			return "package info error";
		}
		for(int i=0;i<objPackage.length;i++){
			xStream.aliasPackage(packageStr[i], objPackage[i]);
		}
		return xStream.toXML(obj);
	} 
}