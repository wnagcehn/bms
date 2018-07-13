package com.jiuyescm.bms.file.templet.repository.impl;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.file.templet.BmsTempletInfoEntity;
import com.jiuyescm.bms.file.templet.repository.IBmsTempletInfoRepository;
import com.jiuyescm.cfm.persistence.mybatis.MyBatisDao;

@Repository("bmsTempletInfoRepositoryImpl")
public class BmsTempletInfoRepositoryImpl extends MyBatisDao implements IBmsTempletInfoRepository {

	private static final Logger logger = Logger.getLogger(BmsTempletInfoRepositoryImpl.class.getName());
	
	public BmsTempletInfoRepositoryImpl() {
		super();
	}
	
	@Override
	public PageInfo<BmsTempletInfoEntity> queryPage(Map<String, Object> condition, int pageNo, int pageSize) {
		try{
			List<BmsTempletInfoEntity> list = selectList("com.jiuyescm.bms.file.templet.BmsTempletInfoMapper.query", condition, new RowBounds(pageNo, pageSize));
	        PageInfo<BmsTempletInfoEntity> pageInfo = new PageInfo<BmsTempletInfoEntity>(list);
	        return pageInfo;
		}
		catch(Exception ex){
			logger.error("【数据库操作异常】",ex);
			return null;
		}
	}

	@Override
	public List<BmsTempletInfoEntity> queryList(Map<String, Object> condition) {
		try{
			List<BmsTempletInfoEntity> list = selectList("com.jiuyescm.bms.file.templet.BmsTempletInfoMapper.query", condition);
	        return list;
		}
		catch(Exception ex){
			logger.error("【数据库操作异常】",ex);
			return null;
		}
	}
	
	@Override
	public BmsTempletInfoEntity findById(Long id) {
		try{
			BmsTempletInfoEntity entity = (BmsTempletInfoEntity) selectOne("com.jiuyescm.bms.file.templet.BmsTempletInfoMapper", id);
			return entity;
		}
		catch(Exception ex){
			logger.error("【数据库操作异常】",ex);
			return null;
		}
	}
	

	@Override
	public BmsTempletInfoEntity findByCode(String templetCode) {
		try{
			BmsTempletInfoEntity entity = (BmsTempletInfoEntity) selectOne("com.jiuyescm.bms.file.templet.BmsTempletInfoMapper.findByCode", templetCode);
			return entity;
		}
		catch(Exception ex){
			logger.error("【数据库操作异常】",ex);
			return null;
		}
	}

	@Override
	public BmsTempletInfoEntity save(BmsTempletInfoEntity entity) {
		try{
			insert("com.jiuyescm.bms.file.templet.BmsTempletInfoMapper.save", entity);
			return entity;
		}
		catch(Exception ex){
			logger.error("【数据库操作异常】",ex);
			return null;
		}
	}

	@Override
	public BmsTempletInfoEntity update(BmsTempletInfoEntity entity) {
		try{
			update("com.jiuyescm.bms.file.templet.BmsTempletInfoMapper.update", entity);
			return entity;
		}
		catch(Exception ex){
			logger.error("【数据库操作异常】",ex);
			return null;
		}
	}

	

}
