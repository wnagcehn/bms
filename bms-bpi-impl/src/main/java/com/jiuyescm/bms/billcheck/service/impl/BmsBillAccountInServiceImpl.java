/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.billcheck.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.billcheck.BillAccountInEntity;
import com.jiuyescm.bms.billcheck.repository.IBillAccountInRepository;
import com.jiuyescm.bms.billcheck.service.IBmsBillAccountInService;
import com.jiuyescm.bms.billcheck.vo.BillAccountInVo;

/**
 * 
 * @author chenwenxin
 * 
 */
@Service("bmsBillAccountInService")
public class BmsBillAccountInServiceImpl implements IBmsBillAccountInService {

	private static final Logger logger = Logger.getLogger(BmsBillAccountInServiceImpl.class.getName());
	
	@Autowired
    private IBillAccountInRepository billAccountInRepository;

    @Override
    public PageInfo<BillAccountInVo> query(Map<String, Object> condition,
            int pageNo, int pageSize) {
    	
    	PageInfo<BillAccountInEntity> pageInfo=billAccountInRepository.query(condition, pageNo, pageSize);
		PageInfo<BillAccountInVo> result=new PageInfo<BillAccountInVo>();
		
		try {
			List<BillAccountInVo> voList = new ArrayList<BillAccountInVo>();
	    	for(BillAccountInEntity entity : pageInfo.getList()) {
	    		BillAccountInVo vo = new BillAccountInVo(); 		
	            PropertyUtils.copyProperties(vo, entity);  
	    		voList.add(vo);
	    	}
	    	result.setList(voList);
	    	PropertyUtils.copyProperties(result, pageInfo); 
		} catch (Exception ex) {
            logger.error("转换失败:{0}",ex);
        }
    	
		return result;   	
    }

    @Override
    public BillAccountInVo findById(Long id) {
    	
    	BillAccountInVo billAccountInVo=new BillAccountInVo();
    	BillAccountInEntity entity=billAccountInRepository.findById(id);
		if(entity==null){
			return null;
		}
		try {
            PropertyUtils.copyProperties(billAccountInVo, entity);
        } catch (Exception ex) {
            logger.error("转换失败:{0}",ex);
        }
		return billAccountInVo;
    }

    @Override
    public BillAccountInVo save(BillAccountInVo vo) {
    	
    	BillAccountInVo returnVo=new BillAccountInVo();
    	BillAccountInEntity entity=new BillAccountInEntity();
		try {
            PropertyUtils.copyProperties(entity, vo);          
            BillAccountInEntity returnEntity=billAccountInRepository.save(entity);
            PropertyUtils.copyProperties(returnVo,returnEntity);
            
        } catch (Exception ex) {
            logger.error("转换失败");
        }
		return returnVo;
    }

    @Override
    public BillAccountInVo update(BillAccountInVo vo) {
    	BillAccountInVo returnVo=new BillAccountInVo();
    	BillAccountInEntity entity=new BillAccountInEntity();
		try {
            PropertyUtils.copyProperties(entity, vo);          
            BillAccountInEntity returnEntity=billAccountInRepository.update(entity);
            PropertyUtils.copyProperties(returnVo,returnEntity);
            
        } catch (Exception ex) {
            logger.error("转换失败");
        }
		return returnVo;
    }

    @Override
    public void delete(Long id) {
        billAccountInRepository.delete(id);
    }
	
}
