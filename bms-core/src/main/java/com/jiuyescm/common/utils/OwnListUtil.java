package com.jiuyescm.common.utils;

import java.util.ArrayList;
import java.util.List;


/**
 * 集合工具类
 * @author chenfei
 *
 */
public class OwnListUtil {
	/**
	 * 将List按照指定大小分段
	 * @param list
	 * @param pageSize
	 * @return
	 */
	public static <T> List<List<T>> splitList(List<T> list, int pageSize) {
		List<List<T>> listArray = new ArrayList<List<T>>();

		ArrayList<T> al = new ArrayList<T>();
		for (T x : list) {
			al.add(x);
			if (pageSize == al.size()) {
				listArray.add(al);
				al = new ArrayList<T>();
			}
		}
		if (0 != al.size()) {
			listArray.add(al);
		}
		return listArray;
	}
}