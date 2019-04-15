package com.jiuyescm.bms.common;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.PatternSyntaxException;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
/**
 * 公共工具类
 * @author zhengyishan
 *
 */
public class PubUtil {
  /**
   * 取得PrintWriter 对象
   * @param response
   * @return
   * @throws IOException
   */
  public static PrintWriter getWriter(HttpServletResponse  response) throws IOException{
	    response.setHeader("Cache-Control", "no-cache");  
		response.setContentType("text/json;charset=UTF-8");  
		response.setCharacterEncoding("UTF-8"); 
		return response.getWriter();
  }
  
	/**
	 * 功能：取出字符串左右边全半角空格
	 * 
	 * @param str
	 * @return
	 */
	public static String trim(String strValue) {
		if (strValue != null && !"".equals(strValue)) {
			StringBuffer sb = new StringBuffer();
			StringBuffer sb1 = new StringBuffer();
			String strRet = "";
			sb.append(rTrim(strValue));
			strRet = rTrim(sb.reverse().toString());// 反转后去掉右空格
			sb1.append(strRet);
			strRet = sb1.reverse().toString();
			return strRet.trim();
		} else {
			return "";
		}
	}

	/**
	 * 功能：取出字符串左右边全半角空格
	 * 
	 * @param str
	 * @return
	 */
	public static String objTrim(Object strValue) {
		if ((strValue != null) && (!"".equals(strValue.toString()))) {
			StringBuffer sb = new StringBuffer();
			StringBuffer sb1 = new StringBuffer();
			String strRet = "";
			sb.append(rTrim(strValue.toString()));
			strRet = rTrim(sb.reverse().toString());// 反转后去掉右空格
			sb1.append(strRet);
			strRet = sb1.reverse().toString();
			return strRet.trim();
		} else {
			return "";
		}
	}

	/**
	 * 去除右边全半角空格
	 * 
	 * @param strValue
	 *            String
	 * @return String String
	 */
	public static String rTrim(String strValue) {

		if (strValue != null && !"".equals(strValue)) {
			char[] cValue = strValue.toCharArray();
			int nCur = 0;

			for (int i = cValue.length - 1; i > -1; i--) {
				if ((cValue[i] != '\u0020') && (cValue[i] != '\u3000')) {
					nCur = i;
					break;
				}
			}

			if ((nCur == 0) && ((cValue[0] == '\u0020') || (cValue[0] == '\u3000'))) {
				return "";
			}

			return strValue.substring(0, nCur + 1);
		} else {
			return "";
		}
	}

	/**
	 * 根据传入的对象判断其是否为指定长度的double
	 */
	public static boolean isDouble(Object obj, int len) {
		boolean flag = false;
		String type = "^[-+]?(\\d+(\\.\\d*)?|\\.\\d+)([eE]([-+]?([012]?\\d{1,2}|30[0-7])|-3([01]?[4-9]|[012]?[0-3])))?[dD]?$";
		if (obj.toString().matches(type)) {
			if (-1 != obj.toString().indexOf(".")) {
				String[] s = obj.toString().replace(".", ",").split(",");
				if (null != s && (s.length == 2)) {
					if (s[1].length() == len) {
						flag = true;
					}
				}
			}
		}

		return flag;
	}

	/**
	 * 判断是否是double类型的数字
	 */
	public static boolean isDouble(Object obj) {
		boolean flag = false;
		String type = "^[-+]?(\\d+(\\.\\d*)?|\\.\\d+)([eE]([-+]?([012]?\\d{1,2}|30[0-7])|-3([01]?[4-9]|[012]?[0-3])))?[dD]?$";
		if (obj.toString().matches(type)) {
			flag = true;
		}
		return flag;
	}

	/**
	 * 校验是不是整数
	 * 
	 * @param obj
	 * @return
	 */
	public static boolean isInt(Object obj) {
		boolean flag = false;
		flag = obj.toString().matches("[0-9]+");
		return flag;
	}
 /**
  * 过来特殊字符串
  * @param str
  * @return
  * @throws PatternSyntaxException
  */
	public static String StringFilter(String str) throws PatternSyntaxException {
		if(StringUtils.isNotEmpty(str)){ 
			// 清除掉所有特殊字符
			str = str.replaceAll("[&]", "");
			str = str.replaceAll("[?]", "？");
			str = str.replaceAll("[<]", "《");
			str = str.replaceAll("[>]", "》");  
			return str; 
			
		}
		return "";
	}
	
	public static <T> List<List<T>> splitList(List<T> list, int pageSize) {

		int listSize = list.size();
		int page = (listSize + (pageSize - 1)) / pageSize;

		List<List<T>> listArray = new ArrayList<List<T>>();
		for (int i = 0; i < page; i++) {
			List<T> subList = new ArrayList<T>();
			for (int j = 0; j < listSize; j++) {
				int pageIndex = ((j + 1) + (pageSize - 1)) / pageSize;
				if (pageIndex == (i + 1)) {
					subList.add(list.get(j));
				}
				if ((j + 1) == ((j + 1) * pageSize)) {
					break;
				}
			}
			listArray.add(subList);
		}
		return listArray;
	}
	
	public static void main(String[] args){
		String address="上海市'嘉&定区{3131>创??业园<";
		//System.out.println(StringFilter(address));
		String city=null;
		String a=StringFilter(city);
		//System.out.println(a);
	}
}
