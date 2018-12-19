package com.jiuyescm.bms.excel.callback;

import java.util.List;

import com.jiuyescm.bms.excel.data.DataRow;

public interface SheetReadCallBack {

	public void readTitle(List<String> columns);
	
	/**
	 * 成功读取sheet时的回调
	 * @return
	 */
	public void read(DataRow dr);
	
	public void finish();
	
	public void error(Exception ex);
	
}
