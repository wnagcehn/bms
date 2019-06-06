package com.jiuyescm.bms.base.group.repository;

import java.util.List;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.base.group.BmsGroupCustomerEntity;

public interface IBmsGroupCustomerRepository {

	List<BmsGroupCustomerEntity> queryAllByGroupId(int groupId);
	
	int addBatch(List<BmsGroupCustomerEntity> list);

	int delGroupCustomer(BmsGroupCustomerEntity subjectEntity);

	int updateGroupCustomer(BmsGroupCustomerEntity entity);

	PageInfo<BmsGroupCustomerEntity> queryGroupCustomer(
			BmsGroupCustomerEntity queryCondition, int pageNo, int pageSize);

	List<String> checkCustomerCodeExist(int groupId, List<String> subjectIdList);

	int queryCustomerCountByGroupId(int groupId);

	List<String> queryCustomerByGroupId(int groupId);
	
	List<String> queryCustomerByGroupCode(String groupCode);
}
