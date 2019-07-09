package com.jiuyescm.bms.biz.storage.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jiuyescm.bms.biz.storage.entity.BizPackStorageTempEntity;
import com.jiuyescm.bms.biz.storage.repository.IBizPackStorageTempRepository;
import com.jiuyescm.bms.biz.storage.service.IBizPackStorageTempService;


@Service("bizPackStorageTempService")
public class BizPackStorageTempServiceImpl implements IBizPackStorageTempService {
	
	private @Autowired IBizPackStorageTempRepository bizPackStorageTempRepository;
	

	@Override
	public List<BizPackStorageTempEntity> queryInBiz(String taskId) {
		// TODO Auto-generated method stub
		return bizPackStorageTempRepository.queryInBiz(taskId);
	}

	@Override
	public void saveBatch(List<BizPackStorageTempEntity> list) {
		// TODO Auto-generated method stub
		bizPackStorageTempRepository.saveBatch(list);
	}

	@Override
	public int deleteBybatchNum(String taskId) {
		// TODO Auto-generated method stub
		return bizPackStorageTempRepository.deleteBybatchNum(taskId);
	}
		
}
