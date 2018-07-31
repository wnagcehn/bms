package com.jiuyescm.bms.base.group.service;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.base.group.BmsGroupSubjectEntity;
import com.jiuyescm.bms.base.group.vo.BmsGroupSubjectVo;
import com.jiuyescm.bms.quotation.contract.entity.PriceContractDiscountItemEntity;

public interface IBmsGroupSubjectService {

	List<BmsGroupSubjectVo> queryAllByGroupId(int groupId) throws Exception;
	int addBatch(List<BmsGroupSubjectVo> list) throws Exception;
	int delGroupSubject(BmsGroupSubjectVo subjectVo) throws Exception;
	int updateGroupSubject(BmsGroupSubjectVo subjectVo) throws Exception;
	PageInfo<BmsGroupSubjectVo> queryGroupSubject(
			BmsGroupSubjectVo queryCondition, int pageNo, int pageSize) throws Exception;
	List<String> checkSubjectCodeExist(int groupId, List<String> subjectCodeList);
	int querySubjectCountByGroupId(int id);
	
	Map<String,String> getImportSubject(String groupCode);
	
	Map<String,String> getExportSubject(String groupCode);
	
	Map<String,String> getSubject(String groupCode);

	List<BmsGroupSubjectEntity> queryAllByGroupIdAndBizTypeCode(Map<String, Object> param);

}
