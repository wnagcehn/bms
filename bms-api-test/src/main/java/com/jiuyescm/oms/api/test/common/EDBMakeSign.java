package com.jiuyescm.oms.api.test.common;

import java.io.UnsupportedEncodingException; 
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException; 

public class EDBMakeSign {

	public static String sign(String timestamp,String secretKey) {
		String sign = byte2Hex(digest(String.format("%s%S", timestamp, secretKey)));
		return sign;
	}

	private static String byte2Hex(byte[] bytes) {
		char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
		int j = bytes.length;
		char str[] = new char[j * 2];
		int k = 0;
		for (byte byte0 : bytes) {
			str[k++] = hexDigits[byte0 >>> 4 & 0xf];
			str[k++] = hexDigits[byte0 & 0xf];
		}
		return new String(str);
	}

	private static byte[] digest(String message) {
		try {
			MessageDigest md5Instance = MessageDigest.getInstance("MD5");
			md5Instance.update(message.getBytes("UTF-8"));
			return md5Instance.digest();
		} catch (UnsupportedEncodingException e) {
			return null;
		} catch (NoSuchAlgorithmException e) {
			return null;
		}
	}

}