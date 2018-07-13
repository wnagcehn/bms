package com.jiuyescm.bms.properties.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Properties;

/**
 * properties工具类
 * 
 * @author zhengyishan
 * 
 */
public class PropertiesUtil {
	private static final String PROPERTIESPATH="filepath.properties";
	public static void readFile() {// 传入参数fileName是要读取的资源文件的文件名如(file.properties)
		InputStream in = null;
		Properties pros = new Properties();
		try {

			// 前提是资源文件必须和Mytest类在同一个包下
			in = PropertiesUtil.class.getResourceAsStream(PROPERTIESPATH);
			// 得到当前类的路径，并把资源文件名作为输入流
			pros.load(in);
			@SuppressWarnings("rawtypes")
			Enumeration en = pros.propertyNames();// 得到资源文件中的所有key值
			while (en.hasMoreElements()) {
				String key = (String) en.nextElement();
				System.out.println("key=" + key + " value=" + pros.getProperty(key));
				// 输出资源文件中的key与value值
			}
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("读取资源文件出错");
		} finally {
			try {
				if (null != in) {
					in.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
				System.out.println("关闭流失败");
			}
		}

	}
	
	public static final String getImpExcelFilePath(){
		String path="";
		InputStream in = null;
		Properties pros = new Properties();
		try {
			in = PropertiesUtil.class.getResourceAsStream(PROPERTIESPATH);
			// 得到当前类的路径，并把资源文件名作为输入流
			pros.load(in);
			path=pros.getProperty("omsimpexcelpath");
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("读取资源文件出错");
		} finally {
			try {
				if (null != in) {
					in.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
				System.out.println("关闭流失败");
			}
		}
		return path;
	}
	
	public static final String getImpExcelFilePath(String keyname){
		String path="";
		InputStream in = null;
		Properties pros = new Properties();
		try {
			in = PropertiesUtil.class.getResourceAsStream(PROPERTIESPATH);
			// 得到当前类的路径，并把资源文件名作为输入流
			pros.load(in);
			path=pros.getProperty(keyname);
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("读取资源文件出错");
		} finally {
			try {
				if (null != in) {
					in.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
				System.out.println("关闭流失败");
			}
		}
		return path;
	}
	
	public static final String getSerAddress(){
		String serAddress="";
		InputStream in = null;
		Properties pros = new Properties();
		try {
			in = PropertiesUtil.class.getResourceAsStream("/print.properties");
			// 得到当前类的路径，并把资源文件名作为输入流
			pros.load(in);
			serAddress=pros.getProperty("serAddress");
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("读取资源文件出错");
		} finally {
			try {
				if (null != in) {
					in.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
				System.out.println("关闭流失败");
			}
		}
		return serAddress;
	}
	
	/**
	 * 调用物流商地址
	 * @param keyname
	 * @return
	 */
	public static final String getCarrierProp(String keyname){
		String path="";
		InputStream in = null;
		Properties pros = new Properties();
		try {
			in = PropertiesUtil.class.getResourceAsStream("/express_uri.properties");
			// 得到当前类的路径，并把资源文件名作为输入流
			pros.load(in);
			path=pros.getProperty(keyname);
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("读取调用地址资源文件出错");
		} finally {
			try {
				if (null != in) {
					in.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
				System.out.println("关闭流失败");
			}
		}
		return path;
	}
	
	/**
	 * 接口地址配置文件
	 * @param keyname
	 * @return
	 */
	public static final String getProp(String keyname){
		String path="";
		InputStream in = null;
		Properties pros = new Properties();
		try {
			in = PropertiesUtil.class.getResourceAsStream("/uri.properties");
			// 得到当前类的路径，并把资源文件名作为输入流
			pros.load(in);
			path=pros.getProperty(keyname);
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("读取调用地址资源文件出错");
		} finally {
			try {
				if (null != in) {
					in.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
				System.out.println("关闭流失败");
			}
		}
		return path;
	}
	
	public static void main(String[] args){
		//PropertiesUtil.readFile();
		System.out.println(getImpExcelFilePath());
	}
}
