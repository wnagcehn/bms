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
	
	/**
	 * 根据groupId查询unit
	 * @param groupId
	 * @return
	 */
	List<String> queryUnitByGroupId(int groupId);
	
	/**
	 * 根据groupCode查询unitCode和unitName
	 * @param param
	 * @return
	 */
	List<BmsGroupChargeunitEntity> queryByGroupCode(Map<String, String> param);

}
