package com.jiuyescm.bms.base.group.service;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.base.group.BmsGroupChargeunitEntity;
import com.jiuyescm.bms.base.group.vo.BmsGroupChargeunitVo;

/**
 * ..Service
 * @author wangchen
 * 
 */
public interface IBmsGroupChargeunitService {

	List<BmsGroupChargeunitVo> queryAllByGroupId(int groupId) throws Exception;

	int delGroupUnit(BmsGroupChargeunitVo unitVo) throws Exception;

	int updateGroupUnit(BmsGroupChargeunitVo subjectVo) throws Exception;

	PageInfo<BmsGroupChargeunitVo> queryGroupUnit(BmsGroupChargeunitVo queryCondition, int pageNo, int pageSize)
			throws Exception;

	List<String> checkUnitCodeExist(int groupId, List<String> unitIdList);

	int addBatch(List<BmsGroupChargeunitVo> list) throws Exception;

	int queryUnitCountByGroupId(int groupId);

	List<String> queryUnitByGroupId(int groupId);


}
