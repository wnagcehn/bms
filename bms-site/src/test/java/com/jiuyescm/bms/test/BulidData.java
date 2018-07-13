package com.jiuyescm.bms.test;

import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class BulidData {
	private static ObjectMapper mapper = new ObjectMapper();

	public static Runnable buildMap(String url, Map<String, String> map) {
		StringBuilder str = new StringBuilder();
		for (String key : map.keySet()) {
			str.append(key).append("=").append(map.get(key)).append("&");
		}
		str.deleteCharAt(str.length() - 1);
		return new HttpPost(new BomData(url, str.toString()));
	}

	public static Runnable buildJson(String url, Object obj) {
		try {
			HttpPost post = new HttpPost(new BomData(url,
					mapper.writeValueAsString(obj)));
			post.setJson(true);
			return post;
		} catch (JsonProcessingException e) {
			return null;
		}
	}
}
