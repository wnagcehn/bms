package com.jiuyescm.bms.common.tool;

import com.jiuyescm.bs.util.StringUtil;
import com.jiuyescm.cfm.common.JAppContext;

public class Session {

	/**
	 * 会话是否中断  true-已中断   false-未中断
	 * @return
	 */
	public static boolean isMissing(){
		if(StringUtil.isEmpty(JAppContext.currentUserID())){
			return true;
		}
		else{
			return false;
		}
	}
}
