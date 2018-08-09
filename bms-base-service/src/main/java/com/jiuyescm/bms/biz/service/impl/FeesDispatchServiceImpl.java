package com.jiuyescm.bms.biz.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.jiuyescm.bms.biz.api.IFeesDispatchService;
import com.jiuyescm.bms.biz.entity.BmsFeesDispatchEntity;
import com.jiuyescm.bms.biz.repo.IFeesDispatchRepository;
import com.jiuyescm.bms.biz.vo.BmsDispatchVo;

public class FeesDispatchServiceImpl implements IFeesDispatchService{

	private static final Logger logger = LoggerFactory.getLogger(DispatchServiceImpl.class.getName());
	
	@Autowired 
	private IFeesDispatchRepository feesDispatchRepository;
	
	@Override
	public int updateList(List<BmsDispatchVo> list) {
		// TODO Auto-generated method stub
		List<BmsFeesDispatchEntity> enList=new ArrayList<BmsFeesDispatchEntity>();
		try {
			for(BmsDispatchVo vo : list) {
				BmsFeesDispatchEntity entity = new BmsFeesDispatchEntity();
	    		
	            PropertyUtils.copyProperties(entity, vo);
	            
	    		enList.add(entity);
	    	}
		} catch (Exception ex) {
        	logger.error("转换失败:{0}",ex);
        }
		return feesDispatchRepository.updateList(enList);
	}
	
}
