package com.jiuyescm.bms.base.group.repository.impl;

import java.util.List;
import java.util.Map;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import com.jiuyescm.cfm.persistence.mybatis.MyBatisDao;
import com.jiuyescm.bms.base.group.BmsGroupChargeunitEntity;
import com.jiuyescm.bms.base.group.repository.IBmsGroupChargeunitRepository;

/**
 * ..RepositoryImpl
 * @author wangchen
 * 
 */
@Repository("bmsGroupChargeunitRepository")
public class BmsGroupChargeunitRepositoryImpl extends MyBatisDao<BmsGroupChargeunitEntity> implements IBmsGroupChargeunitRepository {

	@Override
	public List<BmsGroupChargeunitEntity> queryAllByGroupId(int groupId) {
		return this.selectList("com.jiuyescm.bms.base.group.BmsGroupChargeunitMapper.queryAllByGroupId", groupId);
	}

	@Override
	public int addBatch(List<BmsGroupChargeunitEntity> list) {
		return this.insertBatch("com.jiuyescm.bms.base.group.BmsGroupChargeunitMapper.insertEntity", list);
	}

	@Override
	public int delGroupUnit(BmsGroupChargeunitEntity entity) {
		return this.update("com.jiuyescm.bms.base.group.BmsGroupChargeunitMapper.deleteEntity", entity);
	}

	@Override
	public int updateGroupUnit(BmsGroupChargeunitEntity entity) {
		// TODO Auto-generated method stub
		return this.update("com.jiuyescm.bms.base.group.BmsGroupChargeunitMapper.update", entity);
	}

	@Override
	public PageInfo<BmsGroupChargeunitEntity> queryGroupUnit(
			BmsGroupChargeunitEntity queryCondition, int pageNo, int pageSize) {
		SqlSession session=this.getSqlSessionTemplate();
		List<BmsGroupChargeunitEntity> list = session.selectList("com.jiuyescm.bms.base.group.BmsGroupChargeunitMapper.queryGroupUnit", 
				queryCondition, new RowBounds(pageNo, pageSize));
		return new PageInfo<BmsGroupChargeunitEntity>(list);
	}

	@Override
	public List<String> checkUnitCodeExist(int groupId,
			List<String> unitCodeList) {
		Map<String,Object> map=Maps.newHashMap();
		map.put("groupId", groupId);
		map.put("unitCodeList", unitCodeList);
		SqlSession session = getSqlSessionTemplate();
		return session.selectList("com.jiuyescm.bms.base.group.BmsGroupChargeunitMapper.checkSubjectCodeExist", map);
	}

	@Override
	public int queryUnitCountByGroupId(int groupId) {
		int obj=selectOneForInt("com.jiuyescm.bms.base.group.BmsGroupChargeunitMapper.querySubjectCountByGroupId", groupId);
		return obj;
	}

	@Override
	public List<String> queryUnitByGroupId(int groupId) {
		// TODO Auto-generated method stub
		SqlSession session = getSqlSessionTemplate();
		return session.selectList("com.jiuyescm.bms.base.group.BmsGroupChargeunitMapper.queryUnitByGroupId", groupId);
	}
	
}
