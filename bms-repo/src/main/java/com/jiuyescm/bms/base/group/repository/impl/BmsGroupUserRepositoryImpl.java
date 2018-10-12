package com.jiuyescm.bms.base.group.repository.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.omg.CORBA.OBJ_ADAPTER;
import org.springframework.stereotype.Repository;

import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import com.jiuyescm.bms.base.file.entity.FileExportTaskEntity;
import com.jiuyescm.bms.base.group.BmsGroupUserEntity;
import com.jiuyescm.bms.base.group.repository.IBmsGroupUserRepository;
import com.jiuyescm.cfm.persistence.mybatis.MyBatisDao;

@Repository("bmsGroupUserRepository")
public class BmsGroupUserRepositoryImpl extends MyBatisDao<BmsGroupUserEntity> implements IBmsGroupUserRepository {

	@Override
	public int addGroupUser(BmsGroupUserEntity entity) {
		return this.insert("com.jiuyescm.bms.base.group.mapper.BmsGroupUserMapper.addGroupUser", entity);
	}

	@Override
	public int deleteGroupUser(int id) {
		return this.delete("com.jiuyescm.bms.base.group.mapper.BmsGroupUserMapper.deleteGroupUser", id);
	}

	@Override
	public int updateGroupUser(BmsGroupUserEntity entity) {
		return this.update("com.jiuyescm.bms.base.group.mapper.BmsGroupUserMapper.updateGroupUser", entity);
	}

	@Override
	public List<BmsGroupUserEntity> queryAllGroupUser() {
		return this.selectList("com.jiuyescm.bms.base.group.mapper.BmsGroupUserMapper.queryAll", null);
	}
	
	@Override
	public BmsGroupUserEntity queryAreaGroupId(Map<String, Object> condition) {
		List<BmsGroupUserEntity> list = selectList("com.jiuyescm.bms.base.group.mapper.BmsGroupUserMapper.queryAreaGroupId", condition);
		return list.size()>0?list.get(0):null;
	}

	@Override
	public PageInfo<BmsGroupUserEntity> query(Map<String, Object> condition,
			int pageNo, int pageSize) {
        List<BmsGroupUserEntity> list = selectList("com.jiuyescm.bms.base.group.mapper.BmsGroupUserMapper.query", 
        		condition, new RowBounds(pageNo, pageSize));
		return new PageInfo<BmsGroupUserEntity>(list);
	}

	@Override
	public String checkExistGroupName(String userId) {
		Object obj=this.selectOneForObject("com.jiuyescm.bms.base.group.mapper.BmsGroupUserMapper.queryUserGroupName", userId);
		if(obj!=null){
			return obj.toString();
		}else{
			return "";
		}
	}
	
	@Override
	public String checkUserGroupName(Map<String, Object> param) {
		param.put("bizType", "sale_area");
		Object obj=this.selectOneForObject("com.jiuyescm.bms.base.group.mapper.BmsGroupUserMapper.checkUserGroupName", param);
		if(obj!=null){
			return obj.toString();
		}else{
			return "";
		}
	}

	@Override
	public int queryUserCountByGroupId(int groupId) {
		Object obj=this.selectOneForObject("com.jiuyescm.bms.base.group.mapper.BmsGroupUserMapper.queryUserCountByGroupId", groupId);
		if(obj!=null){
			return Integer.valueOf(obj.toString());
		}else{
			return 0;
		}
	}

	@Override
	public BmsGroupUserEntity queryEntityByUserId(String userId) {
		return this.selectOne("com.jiuyescm.bms.base.group.mapper.BmsGroupUserMapper.queryEntityByUserId", userId);
	}

	@Override
	public List<BmsGroupUserEntity> queryAllByGroupId(List<Integer> groupIds) {
		Map<String,Object> maps=Maps.newHashMap();
		maps.put("groupIds", groupIds);
		return this.selectList("com.jiuyescm.bms.base.group.mapper.BmsGroupUserMapper.queryAllByGroupId", maps);
	}

}
