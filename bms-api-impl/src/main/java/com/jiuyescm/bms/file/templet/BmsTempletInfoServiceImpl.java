package com.jiuyescm.bms.file.templet;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.file.templet.repository.IBmsTempletInfoRepository;

@Service("bmsTempletInfoService")
public class BmsTempletInfoServiceImpl implements IBmsTempletInfoService {

	@Autowired private IBmsTempletInfoRepository bmsTempletInfoRepositoryImpl;
	
	@Override
	public PageInfo<BmsTempletInfoEntity> queryPage(Map<String, Object> condition, int pageNo, int pageSize) {
		return bmsTempletInfoRepositoryImpl.queryPage(condition, pageNo, pageSize);
	}

	@Override
	public List<BmsTempletInfoEntity> queryList(Map<String, Object> condition) {
		return bmsTempletInfoRepositoryImpl.queryList(condition);
	}

	@Override
	public BmsTempletInfoEntity findById(Long id) {
		return bmsTempletInfoRepositoryImpl.findById(id);
	}

	@Override
	public BmsTempletInfoEntity findByCode(String templetCode) {
		return bmsTempletInfoRepositoryImpl.findByCode(templetCode);
	}

	@Override
	public BmsTempletInfoEntity save(BmsTempletInfoEntity entity) {
		return bmsTempletInfoRepositoryImpl.save(entity);
	}

	@Override
	public BmsTempletInfoEntity update(BmsTempletInfoEntity entity) {
		return bmsTempletInfoRepositoryImpl.update(entity);
	}

}
