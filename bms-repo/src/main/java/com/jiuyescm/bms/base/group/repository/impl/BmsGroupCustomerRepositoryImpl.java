package com.jiuyescm.bms.base.group.repository.impl;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import com.jiuyescm.bms.base.group.BmsGroupCustomerEntity;
import com.jiuyescm.bms.base.group.repository.IBmsGroupCustomerRepository;
import com.jiuyescm.cfm.persistence.mybatis.MyBatisDao;

@Repository("bmsGroupCustomerRepository")
public class BmsGroupCustomerRepositoryImpl extends MyBatisDao<BmsGroupCustomerEntity> implements IBmsGroupCustomerRepository{

	@Override
	public List<BmsGroupCustomerEntity> queryAllByGroupId(int groupId) {
		return this.selectList("com.jiuyescm.bms.base.group.mapper.BmsGroupCustomerMapper.queryAllByGroupId", groupId);
	}

	@Override
	public int addBatch(List<BmsGroupCustomerEntity> list) {
		return this.insertBatch("com.jiuyescm.bms.base.group.mapper.BmsGroupCustomerMapper.insertEntity", list);
	}

	@Override
	public int delGroupCustomer(BmsGroupCustomerEntity entity) {
		return this.update("com.jiuyescm.bms.base.group.mapper.BmsGroupCustomerMapper.deleteEntity", entity);
	}

	@Override
	public int updateGroupCustomer(BmsGroupCustomerEntity entity) {
		// TODO Auto-generated method stub
		return this.update("com.jiuyescm.bms.base.group.mapper.BmsGroupCustomerMapper.update", entity);
	}

	@Override
	public PageInfo<BmsGroupCustomerEntity> queryGroupCustomer(
			BmsGroupCustomerEntity queryCondition, int pageNo, int pageSize) {
		SqlSession session=this.getSqlSessionTemplate();
		List<BmsGroupCustomerEntity> list = session.selectList("com.jiuyescm.bms.base.group.mapper.BmsGroupCustomerMapper.queryGroupSubject", 
				queryCondition, new RowBounds(pageNo, pageSize));
		return new PageInfo<BmsGroupCustomerEntity>(list);
	}

	@Override
	public List<String> checkCustomerCodeExist(int groupId,
			List<String> customerIdList) {
		Map<String,Object> map=Maps.newHashMap();
		map.put("groupId", groupId);
		map.put("customerIdList", customerIdList);
		SqlSession session = getSqlSessionTemplate();
		return session.selectList("com.jiuyescm.bms.base.group.mapper.BmsGroupCustomerMapper.checkSubjectCodeExist", map);
	}

	@Override
	public int queryCustomerCountByGroupId(int groupId) {
		int obj=selectOneForInt("com.jiuyescm.bms.base.group.mapper.BmsGroupCustomerMapper.querySubjectCountByGroupId", groupId);
		return obj;
	}

	@Override
	public List<String> queryCustomerByGroupId(int groupId) {
		// TODO Auto-generated method stub
		SqlSession session = getSqlSessionTemplate();
		return session.selectList("com.jiuyescm.bms.base.group.mapper.BmsGroupCustomerMapper.queryCustomerByGroupId", groupId);
	}

}
