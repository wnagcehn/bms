package com.jiuyescm.bms.biz.api;

import java.util.List;

import com.jiuyescm.bms.biz.vo.OutstockInfoVo;

public interface IOutstockRecordService {
	public int insert(OutstockInfoVo vo);
	
	public int insertList(List<OutstockInfoVo> list);
}
