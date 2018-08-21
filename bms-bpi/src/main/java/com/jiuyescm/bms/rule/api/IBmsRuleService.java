package com.jiuyescm.bms.rule.api;

import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.rule.vo.BmsRuleVo;

public interface IBmsRuleService {

	PageInfo<BmsRuleVo> query(Map<String, Object> condition, int pageNo, int pageSize) throws Exception;
}
