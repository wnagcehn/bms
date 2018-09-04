package com.jiuyescm.bms.biz.storage.repository.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.jiuyescm.bms.biz.storage.entity.BizProductPalletStorageTempEntity;
import com.jiuyescm.bms.biz.storage.repository.IBizProductPalletStorageTempRepository;
import com.jiuyescm.cfm.persistence.mybatis.MyBatisDao;

@Repository("bizProductPalletStorageTempRepository")
public class BizProductPalletStorageTempRepositoryImpl extends MyBatisDao<BizProductPalletStorageTempEntity> implements IBizProductPalletStorageTempRepository {

	private static final Logger logger = Logger.getLogger(BizProductPalletStorageTempRepositoryImpl.class.getName());

	public BizProductPalletStorageTempRepositoryImpl() {
		super();
	}

	@Override
	public List<BizProductPalletStorageTempEntity> queryInBiz(String taskId) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("taskId", taskId);
		return this.selectList("com.jiuyescm.bms.biz.storage.mapper.BizProductPalletStorageTempMapper.queryInBiz", map);
		
	}

	@Override
	public void saveBatch(List<BizProductPalletStorageTempEntity> list) {
		SqlSession session = getSqlSessionTemplate();
		int ret = session.insert("com.jiuyescm.bms.biz.storage.mapper.BizProductPalletStorageTempMapper.save", list);
		logger.info("保存行数【"+ret+"】");
	}
	
	
	
}
