package com.jiuyescm.bms.test;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;

public class Testsss {

	public static void main(String[] args) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("1", null);
		for (Entry<String, Object> entry : map.entrySet()) {
			System.out.println(entry.getKey());
			System.out.println(entry.getKey());
		}
		map =new Hashtable<String, Object>();
		//map.put("1", null);
		map = new TreeMap<String,Object>();
		map.put("1", null);
		
		map = new ConcurrentHashMap<String,Object>();
		//map.put("1", null);
	}

}
