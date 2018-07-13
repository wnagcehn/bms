package com.jiuyescm.bs.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class DegistUtil {
	
	public static String makeSign(String digestString, String encode) throws NoSuchAlgorithmException, UnsupportedEncodingException {

		MessageDigest md = MessageDigest.getInstance("MD5");
		md.update(digestString.getBytes(encode));
		byte[] b = md.digest();
		
		return (new sun.misc.BASE64Encoder()).encode(b);
	}
		
	
	public static void main(String[] args) throws Exception {
		
	}
}
