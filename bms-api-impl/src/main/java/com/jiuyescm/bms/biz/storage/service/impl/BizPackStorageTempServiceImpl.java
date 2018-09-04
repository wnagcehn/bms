package com.jiuyescm.bms.biz.storage.service.impl;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jiuyescm.bms.biz.storage.entity.BizPackStorageTempEntity;
import com.jiuyescm.bms.biz.storage.service.IBizPackStorageTempService;


@Service("bizPackStorageTempService")
public class BizPackStorageTempServiceImpl implements IBizPackStorageTempService {

	private static final Logger logger = Logger.getLogger(BizPackStorageTempServiceImpl.class.getName());
	
	

	@Override
	public List<BizPackStorageTempEntity> queryInBiz(String taskId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void saveBatch(List<BizPackStorageTempEntity> list) {
		// TODO Auto-generated method stub
		
	}
	
	
	
}
