package com.jiuyescm.bms.biz.repo.impl;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.biz.repo.IDispatchRepository;
import com.jiuyescm.bms.biz.vo.BmsDispatchVo;
import com.jiuyescm.cfm.persistence.mybatis.MyBatisDao;

@Repository("dispatchRepositoryImpl")
public class DispatchRepositoryImpl extends MyBatisDao<BmsDispatchVo> implements IDispatchRepository {

	private static final Logger logger = LoggerFactory.getLogger(DispatchRepositoryImpl.class.getName());
	
	@Override
	public PageInfo<BmsDispatchVo> querybizData(Map<String, Object> condition,int pageNo, int pageSize) {
		
		try{
			List<BmsDispatchVo> list = selectList("com.jiuyescm.bms.OutStockDataMapper.querybizData", condition, new RowBounds(
	                pageNo, pageSize));
			PageInfo<BmsDispatchVo> pageInfo = new PageInfo<BmsDispatchVo>(list);
	        return pageInfo;
		}
		catch(Exception ex){
			logger.error("querybizData查询异常", ex);
			return null;
		}
	}

	@Override
	public PageInfo<BmsDispatchVo> queryAll(Map<String, Object> condition,int pageNo, int pageSize) {
		try{
			List<BmsDispatchVo> list = selectList("com.jiuyescm.bms.OutStockDataMapper.queryAll", condition, new RowBounds(pageNo, pageSize));
			PageInfo<BmsDispatchVo> pageInfo = new PageInfo<BmsDispatchVo>(list);
	        return pageInfo;
		}
		catch(Exception ex){
			logger.error("queryAll查询异常", ex);
			return null;
		}
	}

	@Override
	public PageInfo<BmsDispatchVo> queryBizOrigin(
			Map<String, Object> condition, int pageNo, int pageSize) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PageInfo<BmsDispatchVo> queryBizModifyRecord(
			Map<String, Object> condition, int pageNo, int pageSize) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int updateBizData(List<BmsDispatchVo> vos) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int reCalcu(Map<String, Object> condition) {
		// TODO Auto-generated method stub
		return 0;
	}

}
