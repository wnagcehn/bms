package com.jiuyescm.bms.excel.callback;

import com.jiuyescm.bms.excel.data.DataRow;

public interface SheetReadCallBack {

	/**
	 * 成功读取sheet时的回调
	 * @return
	 */
	public void read(DataRow dr);
	
	public void finish();
	
}
