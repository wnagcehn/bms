package com.jiuyescm.common.utils;

import java.security.MessageDigest;

import org.apache.log4j.Logger;

import com.thoughtworks.xstream.core.util.Base64Encoder;

public class MD5Util {

	private static final Logger logger = Logger.getLogger(MD5Util.class.getName());
	
	/**
	 * MD5加密
	 * @param str
	 * @return
	 */
	public static String getMd5(String str){
		try {
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			Base64Encoder base64en = new Base64Encoder();
			String newstr = base64en.encode(md5.digest(str.getBytes("utf-8")));
			return newstr;
		} catch (Exception e) {
			logger.error("MD5异常", e);
			return null;
		}
	}
}
