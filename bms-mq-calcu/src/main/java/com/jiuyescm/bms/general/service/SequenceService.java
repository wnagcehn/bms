package com.jiuyescm.bms.general.service;

import java.util.List;

/**
 * 流水号获取service
 * @author zhengyishan
 *
 */
public interface SequenceService {
	/**
	 * 单个获取流水号
	 * @param idName
	 * @param startName
	 * @param formatStr
	 * @return
	 */
	public String getBillNoOne(String idName, String startName, String formatStr);
	
	/**
	 * 批量获取流水号
	 * @param idName
	 * @param startName
	 * @param size
	 * @param formatStr
	 * @return
	 */
	public List<String> getBillNoList(String idName,String startName,int size, String formatStr);

}
