/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.billcheck.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.billcheck.BillAccountInfoEntity;
import com.jiuyescm.bms.billcheck.repository.IBillAccountInfoRepository;
import com.jiuyescm.bms.billcheck.service.IBmsAccountInfoService;
import com.jiuyescm.bms.billcheck.vo.BillAccountInfoVo;

/**
 * 
 * @author stevenl
 * 
 */
@Service("bmsBillAccountInfoService")
public class BmsBillAccountInfoServiceImpl implements IBmsAccountInfoService {

	private static final Logger logger = Logger.getLogger(BmsBillAccountInfoServiceImpl.class.getName());

	@Resource
	private IBillAccountInfoRepository billAccountInfoRepository;
	
	@Override
	public PageInfo<BillAccountInfoVo> query(Map<String, Object> condition,
			int pageNo, int pageSize) {
		// TODO Auto-generated method stub
		PageInfo<BillAccountInfoEntity> pageInfo=billAccountInfoRepository.query(condition, pageNo, pageSize);
		PageInfo<BillAccountInfoVo> result=new PageInfo<BillAccountInfoVo>();
		
		try {
			List<BillAccountInfoVo> voList = new ArrayList<BillAccountInfoVo>();
	    	for(BillAccountInfoEntity entity : pageInfo.getList()) {
	    		BillAccountInfoVo vo = new BillAccountInfoVo(); 		
	            PropertyUtils.copyProperties(vo, entity);  
	    		voList.add(vo);
	    	}
	    	result.setList(voList);
		} catch (Exception ex) {
            logger.error("转换失败:{0}",ex);
        }
    	
		return result;
	}

	@Override
	public BillAccountInfoVo findByCustomerId(String customerId) {
		// TODO Auto-generated method stub
		BillAccountInfoVo billAccountInfoVo=new BillAccountInfoVo();
		BillAccountInfoEntity entity=billAccountInfoRepository.findByCustomerId(customerId);
		if(entity==null){
			return null;
		}
		try {
            PropertyUtils.copyProperties(billAccountInfoVo, entity);
        } catch (Exception ex) {
            logger.error("转换失败:{0}",ex);
        }
		return billAccountInfoVo;
	}

	@Override
	public BillAccountInfoVo save(BillAccountInfoVo vo) {
		// TODO Auto-generated method stub
		BillAccountInfoVo returnVo=new BillAccountInfoVo();
		BillAccountInfoEntity entity=new BillAccountInfoEntity();
		try {
            PropertyUtils.copyProperties(entity, vo);          
            BillAccountInfoEntity returnEntity=billAccountInfoRepository.save(entity);
            PropertyUtils.copyProperties(returnVo,returnEntity);
            
        } catch (Exception ex) {
            logger.error("转换失败");
        }
		return returnVo;
	}

	@Override
	public BillAccountInfoVo update(BillAccountInfoVo vo) {
		// TODO Auto-generated method stub
		BillAccountInfoVo returnVo=new BillAccountInfoVo();
		BillAccountInfoEntity entity=new BillAccountInfoEntity();
		try {
            PropertyUtils.copyProperties(entity, vo);          
            BillAccountInfoEntity returnEntity=billAccountInfoRepository.update(entity);
            PropertyUtils.copyProperties(returnVo,returnEntity);
            
        } catch (Exception ex) {
            logger.error("转换失败");
        }
		return returnVo;
	}

	@Override
	public void delete(Long id) {
		// TODO Auto-generated method stub
		
	}
	
	
	
}
