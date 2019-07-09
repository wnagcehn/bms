package com.jiuyescm.bms.biz.storage.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jiuyescm.bms.biz.storage.entity.BizProductPalletStorageTempEntity;
import com.jiuyescm.bms.biz.storage.repository.IBizProductPalletStorageTempRepository;
import com.jiuyescm.bms.biz.storage.service.IBizProductPalletStorageTempService;

@Service("bizProductPalletStorageTempService")
public class BizProductPalletStorageTempServiceImpl implements IBizProductPalletStorageTempService {

	private @Autowired IBizProductPalletStorageTempRepository bizProductPalletStorageTempRepository;
	
	@Override
	public List<BizProductPalletStorageTempEntity> queryInBiz(String taskId) {
		// TODO Auto-generated method stub
		return bizProductPalletStorageTempRepository.queryInBiz(taskId);
	}

	@Override
	public void saveBatch(List<BizProductPalletStorageTempEntity> list) {
		bizProductPalletStorageTempRepository.saveBatch(list);;
	}

	@Override
	public int deleteBybatchNum(String taskId) {
		// TODO Auto-generated method stub
		return bizProductPalletStorageTempRepository.deleteBybatchNum(taskId);
	}
	
	
	
}
