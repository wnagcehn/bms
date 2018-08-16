package com.jiuyescm.bms.base.group.repository;

import java.util.Map;
import java.util.List;
import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.base.group.BmsGroupChargeunitEntity;

/**
 * ..Repository
 * @author wangchen
 * 
 */
public interface IBmsGroupChargeunitRepository {

	List<BmsGroupChargeunitEntity> queryAllByGroupId(int groupId);

	int addBatch(List<BmsGroupChargeunitEntity> list);

	int delGroupUnit(BmsGroupChargeunitEntity entity);

	int updateGroupUnit(BmsGroupChargeunitEntity entity);

	PageInfo<BmsGroupChargeunitEntity> queryGroupUnit(BmsGroupChargeunitEntity queryCondition, int pageNo,
			int pageSize);

	List<String> checkUnitCodeExist(int groupId, List<String> customerIdList);

	int queryUnitCountByGroupId(int groupId);

	List<String> queryUnitByGroupId(int groupId);

}
