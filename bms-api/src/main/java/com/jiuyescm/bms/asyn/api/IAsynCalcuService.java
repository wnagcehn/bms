package com.jiuyescm.bms.asyn.api;

import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.asyn.vo.AsynCalcuVo;

public interface IAsynCalcuService {
	
	PageInfo<AsynCalcuVo> query(Map<String, Object> condition,
			int pageNo, int pageSize) throws Exception;
	
	int save(AsynCalcuVo entity) throws Exception;

	int update(AsynCalcuVo entity) throws Exception;

	int delete(AsynCalcuVo voEntity) throws Exception;

}
