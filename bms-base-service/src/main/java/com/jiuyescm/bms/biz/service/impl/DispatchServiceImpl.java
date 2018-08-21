package com.jiuyescm.bms.biz.service.impl;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.biz.api.IDispatchService;
import com.jiuyescm.bms.biz.repo.IDispatchRepository;
import com.jiuyescm.bms.biz.vo.BmsDispatchVo;

@Service("dispatchService")
public class DispatchServiceImpl implements IDispatchService {

	private static final Logger logger = LoggerFactory.getLogger(DispatchServiceImpl.class.getName());
	
	@Autowired private IDispatchRepository dispatchRepositoryImpl;
	
	@Override
	public PageInfo<BmsDispatchVo> querybizData(Map<String, Object> condition,int pageNo, int pageSize) {
		// TODO Auto-generated method stub
		return dispatchRepositoryImpl.querybizData(condition, pageNo, pageSize);
	}

	@Override
	public PageInfo<BmsDispatchVo> queryAll(Map<String, Object> condition,int pageNo, int pageSize) {
		
		return dispatchRepositoryImpl.queryAll(condition, pageNo, pageSize);
		
	}

	@Override
	public PageInfo<BmsDispatchVo> queryBizOrigin(Map<String, Object> condition, int pageNo, int pageSize) {
		
		return dispatchRepositoryImpl.queryBizOrigin(condition, pageNo, pageSize);
	}

	@Override
	public PageInfo<BmsDispatchVo> queryBizModifyRecord(Map<String, Object> condition, int pageNo, int pageSize) {
		return dispatchRepositoryImpl.queryBizModifyRecord(condition, pageNo, pageSize);
	}

	@Override
	public int updateBizData(List<BmsDispatchVo> vos) {
		//1:写入到记录表
		//2:更新到outstock_info
		//扩展
		return dispatchRepositoryImpl.updateBizData(vos);
	}

	@Override
	public int reCalcu(Map<String, Object> condition) {
		return dispatchRepositoryImpl.reCalcu(condition);
	}

}
