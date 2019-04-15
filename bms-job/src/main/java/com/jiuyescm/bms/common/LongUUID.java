package com.jiuyescm.bms.common;

import java.util.UUID;

public final class LongUUID {
	private LongUUID() {

	}

	public static long uuidOfLang() {
		return UUID.randomUUID().getMostSignificantBits();
	}

	public static long uuidOfAbsLongs() {
		while (true) {
			long r = uuidOfLang();
			if (r > 0L) {
				return r;
			}
		}
	}

	public static String uuidOf36String(String prex) {
		long theValue = uuidOfAbsLongs();
		String tmp = Long.toString(theValue, 36).toUpperCase();
		String s = "";
		int len = tmp.length();
		int x = 14 - len;
		for (int i = 0; i < x; i++) {
			s = "0" + s;
		}
		if (prex == null) {
			return s + tmp;
		}
		return prex + s + tmp;
	}

	public static String uuidOf36String() {
		return uuidOf36String("E");
	}

	public static void main(String args[]) {
//		System.out.println(String.valueOf(uuidOfAbsLongs()).length());
//		System.out.println(String.valueOf(uuidOfAbsLongs()).length());
//		
//		System.out.println(uuidOf36String());
//		System.out.println(uuidOf36String());
//		System.out.println(uuidOf36String());
//		System.out.println(uuidOf36String());
//		System.out.println(uuidOf36String());
//		System.out.println(uuidOf36String());
//		System.out.println(uuidOf36String());
//		System.out.println(uuidOf36String());
//		System.out.println(uuidOf36String());
//		System.out.println(uuidOf36String());
	}
}
