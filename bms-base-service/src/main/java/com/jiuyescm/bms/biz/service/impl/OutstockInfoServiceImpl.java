package com.jiuyescm.bms.biz.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jiuyescm.bms.biz.api.IOutstockInfoService;
import com.jiuyescm.bms.biz.entity.BmsOutstockInfoEntity;
import com.jiuyescm.bms.biz.repo.IOutstockInfoRepository;
import com.jiuyescm.bms.biz.vo.OutstockInfoVo;

@Service("outstockInfoService")
public class OutstockInfoServiceImpl implements IOutstockInfoService{

	private static final Logger logger = LoggerFactory.getLogger(OutstockInfoServiceImpl.class.getName());
	
	@Autowired 
	private IOutstockInfoRepository repository;
	
	@Override
	public int updateList(List<OutstockInfoVo> list) {
		// TODO Auto-generated method stub
		List<BmsOutstockInfoEntity> enList=new ArrayList<BmsOutstockInfoEntity>();
		try {
			for(OutstockInfoVo vo : list) {
				BmsOutstockInfoEntity entity = new BmsOutstockInfoEntity();
	    		
	            PropertyUtils.copyProperties(entity, vo);
	            
	    		enList.add(entity);
	    	}
		} catch (Exception ex) {
        	logger.error("转换失败:{0}",ex);
        }
		
		return repository.updateList(enList);
	}

}
