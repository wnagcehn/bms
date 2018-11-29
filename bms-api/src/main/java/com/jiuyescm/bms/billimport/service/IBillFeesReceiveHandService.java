package com.jiuyescm.bms.billimport.service;


/**
 * ..Service
 * @author liuzhicheng
 * 
 */
public interface IBillFeesReceiveHandService {
	
	/**
	 * 从临时表保存数据到正式表
	 * @param billNo
	 * @return
	 */
	int saveDataFromTemp(String billNo);
}
