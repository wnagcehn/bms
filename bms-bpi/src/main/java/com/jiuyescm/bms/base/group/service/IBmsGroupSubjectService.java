/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.base.group.service;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.base.group.BmsGroupSubjectEntity;
import com.jiuyescm.bms.base.group.vo.BmsGroupSubjectVo;

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
	
	List<BmsGroupSubjectEntity> queryGroupSubjectByGroupId();
	List<BmsGroupSubjectVo> queryGroupSubjectList();
	
	/**
     * 获取bms增值一级科目
     * @author caojianwei
     * @date 2019年4月17日 下午12:48:44
     * @return 
     */
    Map<String, String> getWmsValueAddSubjectGroups();
    
    /**
     * 根据一级科目组获取bms增值科目明细
     * @author caojianwei
     * @date 2019年4月17日 下午1:14:23
     * @param groupCode 一级科目组编码
     * @return
     */
    Map<String, String> getWmsValueAddSubjectDetails(String groupCode);

}
