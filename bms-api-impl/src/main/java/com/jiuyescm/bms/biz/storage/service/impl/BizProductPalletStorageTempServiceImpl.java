package com.jiuyescm.bms.biz.storage.service.impl;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jiuyescm.bms.biz.storage.entity.BizProductPalletStorageTempEntity;
import com.jiuyescm.bms.biz.storage.repository.IBizProductPalletStorageTempRepository;
import com.jiuyescm.bms.biz.storage.service.IBizProductPalletStorageTempService;

@Service("bizProductPalletStorageTempService")
public class BizProductPalletStorageTempServiceImpl implements IBizProductPalletStorageTempService {

	private static final Logger logger = Logger.getLogger(BizProductPalletStorageTempServiceImpl.class.getName());

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
	
	
	
}
