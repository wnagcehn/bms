package com.jiuyescm.bms.common.tool;

import java.util.ArrayList;
import java.util.List;
/**
 * 分页
 * @author wuliangfeng
 *
 */
public class ListTool {
	public static <T> List<List<T>> splitList(List<T> list, int pageSize) {

		int listSize = list.size();
		int page = (listSize + (pageSize - 1)) / pageSize;

		List<List<T>> listArray = new ArrayList<List<T>>();
		for (int i = 0; i < page; i++) {
			List<T> subList = new ArrayList<T>();
			for (int j = 0; j < listSize; j++) {
				int pageIndex = ((j + 1) + (pageSize - 1)) / pageSize;
				if (pageIndex == (i + 1)) {
					subList.add(list.get(j));
				}
				if ((j + 1) == ((j + 1) * pageSize)) {
					break;
				}
			}
			listArray.add(subList);
		}
		return listArray;
	}
	
}
