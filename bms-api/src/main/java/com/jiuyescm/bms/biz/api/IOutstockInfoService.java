package com.jiuyescm.bms.biz.api;

import java.util.List;

import com.jiuyescm.bms.biz.vo.OutstockInfoVo;

public interface IOutstockInfoService {
	public int updateList(List<OutstockInfoVo> list);
	
	public int update(OutstockInfoVo vo);
}
