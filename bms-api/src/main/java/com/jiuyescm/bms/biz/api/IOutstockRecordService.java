package com.jiuyescm.bms.biz.api;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.biz.vo.OutstockInfoVo;
import com.jiuyescm.bms.biz.vo.OutstockRecordVo;

public interface IOutstockRecordService {
	
	/**
	 * 查询出所有的改动记录
	 * @param condition
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
    PageInfo<OutstockRecordVo> query(Map<String, Object> condition, int pageNo,
            int pageSize);
	
	
	public int insert(OutstockInfoVo vo);
	
	public int insertList(List<OutstockInfoVo> list);
}
