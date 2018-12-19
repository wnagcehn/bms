package com.jiuyescm.common.tool;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import com.jiuyescm.common.utils.MD5Util;

public class MapTool {
	
	/**
	 * 对Map进行排序
	 * @param map
	 * @return
	 */
	public static String getUnionString(Map<String, String> map){
		
		List<Map.Entry<String,String>> list = new ArrayList<Map.Entry<String,String>>(map.entrySet());
        Collections.sort(list,new Comparator<Map.Entry<String,String>>() {
			@Override
			public int compare(Entry<String, String> o1, Entry<String, String> o2) {
				return o1.getKey().compareTo(o2.getKey());
			}
        });
        
        String ret = "";
        for(Map.Entry<String,String> mapping:list){ 
        	ret+=mapping.getKey()+":"+mapping.getValue().toString();
        } 
		return ret;
	}
	
	/**
	 * 对map进行排序，排序后进行md5签名  eg "a":"a","c":"c","b":"b" 签名字符串为 aabbcc
	 * @param map
	 * @return
	 */
	public static String getMD5String(Map<String, String> map){
		String str = getUnionString(map);
		String md5Str = MD5Util.getMd5(str);
		return md5Str;
	}
	
}
