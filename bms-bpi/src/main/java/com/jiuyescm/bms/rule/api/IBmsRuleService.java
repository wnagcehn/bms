package com.jiuyescm.bms.rule.api;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.rule.vo.BmsRuleVo;

public interface IBmsRuleService {

	/**
	 * 查询合同在线 计算规则
	 */
	PageInfo<BmsRuleVo> query(Map<String, Object> condition, int pageNo, int pageSize) throws Exception;
	
	/**
	 * 查询合同在线计算规则
	 * @param quotationNo 	规则编号
	 * @param quotationName 规则名称
	 * @param bizTypeCode 	业务类型 STORAGE-仓储  DISPATCH-配送
	 * @param subjectId 	费用科目编码
	 * @return
	 * @throws Exception
	 */
	List<BmsRuleVo> queryForContract(String quotationNo,String quotationName,String bizTypeCode,String subjectId);
}
