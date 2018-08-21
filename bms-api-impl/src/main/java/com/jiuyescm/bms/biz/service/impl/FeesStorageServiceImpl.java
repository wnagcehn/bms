package com.jiuyescm.bms.biz.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jiuyescm.bms.biz.api.IFeesStorageService;
import com.jiuyescm.bms.biz.entity.BmsFeesStorageEntity;
import com.jiuyescm.bms.biz.repo.IFeesStorageRepository;
import com.jiuyescm.bms.biz.vo.OutstockInfoVo;

@Service("feesStorageService")
public class FeesStorageServiceImpl implements IFeesStorageService{

	private static final Logger logger = LoggerFactory.getLogger(FeesStorageServiceImpl.class.getName());
	
	@Autowired 
	private IFeesStorageRepository feesStorageRepository;
	
	@Override
	public int updateList(List<OutstockInfoVo> list) {
		// TODO Auto-generated method stubO
		List<BmsFeesStorageEntity> enList=new ArrayList<BmsFeesStorageEntity>();
		try {
			for(OutstockInfoVo vo : list) {
				BmsFeesStorageEntity entity = new BmsFeesStorageEntity();
	    		
	            PropertyUtils.copyProperties(entity, vo);
	            
	    		enList.add(entity);
	    	}
		} catch (Exception ex) {
        	logger.error("转换失败:{0}",ex);
        }
		
		return feesStorageRepository.updateList(enList);
	}

}
