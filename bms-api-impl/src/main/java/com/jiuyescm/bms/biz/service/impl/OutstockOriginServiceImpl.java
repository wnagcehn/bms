package com.jiuyescm.bms.biz.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jiuyescm.bms.biz.api.IOutstockOriginService;
import com.jiuyescm.bms.biz.entity.BmsOutstockOriginEntity;
import com.jiuyescm.bms.biz.repo.IOutstockOriginRepository;
import com.jiuyescm.bms.biz.vo.OutstockInfoVo;

@Service("outstockOriginService")
public class OutstockOriginServiceImpl implements IOutstockOriginService{
	
	private static final Logger logger = LoggerFactory.getLogger(OutstockInfoServiceImpl.class.getName());

	@Autowired 
	private IOutstockOriginRepository repository;
	
	@Override
	public int updateList(List<OutstockInfoVo> list) {
		// TODO Auto-generated method stub
		List<BmsOutstockOriginEntity> enList=new ArrayList<BmsOutstockOriginEntity>();
		try {
			for(OutstockInfoVo vo : list) {
				BmsOutstockOriginEntity entity = new BmsOutstockOriginEntity();
	    		
	            PropertyUtils.copyProperties(entity, vo);
	            
	    		enList.add(entity);
	    	}
		} catch (Exception ex) {
        	logger.error("转换失败:{0}",ex);
        }
		
		return repository.updateList(enList);
	}

}
