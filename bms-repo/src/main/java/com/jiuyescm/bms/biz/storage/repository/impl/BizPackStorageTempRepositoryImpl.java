package com.jiuyescm.bms.biz.storage.repository.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.google.common.collect.Maps;
import com.jiuyescm.bms.biz.storage.entity.BizPackStorageTempEntity;
import com.jiuyescm.bms.biz.storage.repository.IBizPackStorageTempRepository;
import com.jiuyescm.cfm.persistence.mybatis.MyBatisDao;

@Repository("bizPackStorageTempRepository")
public class BizPackStorageTempRepositoryImpl extends MyBatisDao<BizPackStorageTempEntity> implements IBizPackStorageTempRepository {

	private static final Logger logger = Logger.getLogger(BizPackStorageTempRepositoryImpl.class.getName());

	public BizPackStorageTempRepositoryImpl() {
		super();
	}

	@Override
	public List<BizPackStorageTempEntity> queryInBiz(String taskId) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("taskId", taskId);
		return this.selectList("com.jiuyescm.bms.biz.storage.mapper.BizPackStorageTempMapper.queryInBiz", map);
	}

	@Override
	public void saveBatch(List<BizPackStorageTempEntity> list) {
		SqlSession session = getSqlSessionTemplate();
		int ret = session.insert("com.jiuyescm.bms.biz.storage.mapper.BizPackStorageTempMapper.save", list);
		logger.info("保存行数【"+ret+"】");
	}

	@Override
	public int deleteBybatchNum(String taskId) {
		// TODO Auto-generated method stub
		Map<String,String> map=Maps.newHashMap();
		map.put("taskId", taskId);
		int k=this.delete("com.jiuyescm.bms.biz.storage.mapper.BizPackStorageTempMapper.deleteBybatchNum", map);
		logger.info("删除耗材临时表 行数【"+k+"】,批次号【"+taskId+"】");
		return k;
	}
	
}
