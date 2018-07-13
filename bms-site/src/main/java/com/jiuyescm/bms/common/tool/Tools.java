package com.jiuyescm.bms.common.tool;

import java.security.MessageDigest;

import com.thoughtworks.xstream.core.util.Base64Encoder;

public class Tools {
	public static String getMd5(String str){
		try {
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			Base64Encoder base64en = new Base64Encoder();
			String newstr = base64en.encode(md5.digest(str.getBytes("utf-8")));
			return newstr;
		} catch (Exception e) {
			return null;
		}
	}
	public static String objToString(Object obj){
		if(obj==null){
			return "";
		}else{
			return obj.toString();
		}
	}
}
