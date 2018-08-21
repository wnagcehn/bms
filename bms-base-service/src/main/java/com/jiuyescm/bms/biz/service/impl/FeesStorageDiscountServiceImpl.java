package com.jiuyescm.bms.biz.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jiuyescm.bms.biz.api.IFeesStorageDiscountService;
import com.jiuyescm.bms.biz.entity.BmsFeesStorageDiscountEntity;
import com.jiuyescm.bms.biz.repo.IFeesStorageDiscountRepository;
import com.jiuyescm.bms.biz.vo.OutstockInfoVo;

@Service("feesStorageDiscountService")
public class FeesStorageDiscountServiceImpl implements IFeesStorageDiscountService{

	private static final Logger logger = LoggerFactory.getLogger(DispatchServiceImpl.class.getName());

	@Autowired 
	private IFeesStorageDiscountRepository repository;
	
	@Override
	public int updateList(List<OutstockInfoVo> list) {
		// TODO Auto-generated method stub
		List<BmsFeesStorageDiscountEntity> enList=new ArrayList<BmsFeesStorageDiscountEntity>();
		try {
			for(OutstockInfoVo vo : list) {
				BmsFeesStorageDiscountEntity entity = new BmsFeesStorageDiscountEntity();  		
	            PropertyUtils.copyProperties(entity, vo);           
	    		enList.add(entity);
	    	}
		} catch (Exception ex) {
        	logger.error("转换失败:{0}",ex);
        }
		return repository.updateList(enList);
	}

}
