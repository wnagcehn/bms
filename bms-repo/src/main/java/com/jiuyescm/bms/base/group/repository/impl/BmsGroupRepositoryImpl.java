package com.jiuyescm.bms.base.group.repository.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.google.common.collect.Maps;
import com.jiuyescm.bms.base.group.BmsGroupEntity;
import com.jiuyescm.bms.base.group.repository.IBmsGroupRepository;
import com.jiuyescm.cfm.persistence.mybatis.MyBatisDao;

@Repository("bmsGroupRepository")
public class BmsGroupRepositoryImpl extends MyBatisDao<BmsGroupEntity> implements IBmsGroupRepository{

	@Override
	public int addGroup(BmsGroupEntity entity) {
		return this.insert("com.jiuyescm.bms.base.group.mapper.BmsGroupMapper.addGroup", entity);
	}

	@Override
	public int deleteGroup(int id) {
		return this.delete("com.jiuyescm.bms.base.group.mapper.BmsGroupMapper.deleteGroup", id);
	}

	@Override
	public int updateGroup(BmsGroupEntity entity) {
		return this.update("com.jiuyescm.bms.base.group.mapper.BmsGroupMapper.updateGroup", entity);
	}

	@Override
	public List<BmsGroupEntity> queryAllGroup() {
		return this.selectList("com.jiuyescm.bms.base.group.mapper.BmsGroupMapper.queryAll", null);
	}
	
	@Override
	public List<BmsGroupEntity> findAreaEnumList(Map<String, String> param) {
		param.put("bizType", "sale_area");
		return this.selectList("com.jiuyescm.bms.base.group.mapper.BmsGroupMapper.findAreaEnumList", param);
	}

	@Override
	public List<BmsGroupEntity> queryDataByParentId(int parentId) {
		Map<String,Object> map=Maps.newHashMap();
		map.put("parentId", parentId);
		return this.selectList("com.jiuyescm.bms.base.group.mapper.BmsGroupMapper.queryDataByParentId", map);
	}

	@Override
	public int queryChildGroupCount(int id) {
		int k=0;
		Object obj=this.selectOneForObject("com.jiuyescm.bms.base.group.mapper.BmsGroupMapper.queryChildGroupCount", id);
		if(obj!=null){
			k=Integer.valueOf(obj.toString());
		}
		return k;
	}

	@Override
	public List<Integer> queryAllGroupId(int groupId) {
		// TODO Auto-generated method stub
		List<Integer> inList=new ArrayList<Integer>();
		List<BmsGroupEntity> list=selectList("com.jiuyescm.bms.base.group.mapper.BmsGroupMapper.queryAllGroupId", groupId);
		for(BmsGroupEntity entity:list){
			if(groupId!=entity.getId()){
				inList.add(entity.getId());
			}
		}
		return inList;
	}

	@Override
	public List<BmsGroupEntity> queryDataByParentIdAndBizType(int pid,
			String bizTypeCode) {
		Map<String,Object> map=Maps.newHashMap();
		map.put("parentId", pid);
		map.put("bizTypeCode", bizTypeCode);
		return this.selectList("com.jiuyescm.bms.base.group.mapper.BmsGroupMapper.queryDataByParentIdAndBizType", map);
	}

	@Override
	public boolean checkGroup(BmsGroupEntity entity) {
		Object obj=this.selectOneForObject("com.jiuyescm.bms.base.group.mapper.BmsGroupMapper.checkGroup", entity);
		if(obj!=null){
			int k=Integer.valueOf(obj.toString());
			return k>0?false:true;
		}
		return true;
	}

	@Override
	public BmsGroupEntity queryOne(Map<String, Object> condition) {
		// TODO Auto-generated method stub
		
		return (BmsGroupEntity) selectOne("com.jiuyescm.bms.base.group.mapper.BmsGroupMapper.queryOne", condition);
	}

}
