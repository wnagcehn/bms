package com.jiuyescm.common.tool;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.function.ToDoubleFunction;
import java.util.function.ToIntFunction;
import java.util.function.ToLongFunction;

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

            @Override
            public Comparator<Entry<String, String>> reversed() {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public Comparator<Entry<String, String>> thenComparing(Comparator<? super Entry<String, String>> other) {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public <U> Comparator<Entry<String, String>> thenComparing(
                    Function<? super Entry<String, String>, ? extends U> keyExtractor,
                    Comparator<? super U> keyComparator) {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public <U extends Comparable<? super U>> Comparator<Entry<String, String>> thenComparing(
                    Function<? super Entry<String, String>, ? extends U> keyExtractor) {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public Comparator<Entry<String, String>> thenComparingInt(
                    ToIntFunction<? super Entry<String, String>> keyExtractor) {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public Comparator<Entry<String, String>> thenComparingLong(
                    ToLongFunction<? super Entry<String, String>> keyExtractor) {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public Comparator<Entry<String, String>> thenComparingDouble(
                    ToDoubleFunction<? super Entry<String, String>> keyExtractor) {
                // TODO Auto-generated method stub
                return null;
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
