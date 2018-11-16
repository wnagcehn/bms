/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.billcheck.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.billcheck.BillAccountInEntity;
import com.jiuyescm.bms.billcheck.repository.IBillAccountInRepository;
import com.jiuyescm.bms.billcheck.service.IBmsBillAccountInService;
import com.jiuyescm.bms.billcheck.vo.BillAccountInVo;
import com.jiuyescm.exception.BizException;

/**
 * 
 * @author liuzhicheng
 * 
 */
@Service("bmsBillAccountInService")
public class BmsBillAccountInServiceImpl implements IBmsBillAccountInService {

	private static final Logger logger = LoggerFactory.getLogger(BmsBillAccountInServiceImpl.class);
	
	@Autowired
    private IBillAccountInRepository billAccountInRepository;

    @Override
    public PageInfo<BillAccountInVo> query(Map<String, Object> condition, int pageNo, int pageSize)  throws BizException{
    	
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
            logger.error("query转换失败:{}",ex);
        }
    	
		return result;   	
    }

    @Override
    public BillAccountInVo findById(Long id)  throws BizException{
    	
    	BillAccountInVo billAccountInVo=new BillAccountInVo();
    	BillAccountInEntity entity=billAccountInRepository.findById(id);
		if(entity==null){
			return null;
		}
		try {
            PropertyUtils.copyProperties(billAccountInVo, entity);
        } catch (Exception ex) {
            logger.error("findById转换失败:{}",ex);
        }
		return billAccountInVo;
    }

    @Override
    public BillAccountInVo save(BillAccountInVo vo)  throws BizException{
    	
    	BillAccountInVo returnVo=new BillAccountInVo();
    	BillAccountInEntity entity=new BillAccountInEntity();
		try {
            PropertyUtils.copyProperties(entity, vo);          
            BillAccountInEntity returnEntity=billAccountInRepository.save(entity);
            PropertyUtils.copyProperties(returnVo,returnEntity);
            
        } catch (Exception ex) {
            logger.error("save转换失败{}",ex);
        }
		return returnVo;
    }

    @Override
    public void update(BillAccountInVo vo)  throws BizException{
    	BillAccountInVo returnVo=new BillAccountInVo();
    	BillAccountInEntity entity=new BillAccountInEntity();
		try {
            PropertyUtils.copyProperties(entity, vo);          
            BillAccountInEntity returnEntity=billAccountInRepository.update(entity);
            PropertyUtils.copyProperties(returnVo,returnEntity);
            
        } catch (Exception ex) {
            logger.error("转换失败{}",ex);
        }
    }

    @Override
    public void delete(BillAccountInVo vo) throws BizException{
    	logger.info("delete vo[{}]",vo);
    	BillAccountInEntity accountInEntity=new BillAccountInEntity();
		try {
            PropertyUtils.copyProperties(accountInEntity, vo);          
        } catch (Exception ex) {
            logger.error("转换失败{}",ex);
        }
    	BillAccountInEntity entity=billAccountInRepository.findById(vo.getId());
    	logger.info("delete {}",entity.getConfirmStatus());
    	//0-未确认（可以删除） 1-已确认
    	if("1".equals(entity.getConfirmStatus())){
    		
    		throw new BizException("已确认状态不能删除");
    	}
    	else{
    		accountInEntity.setDelFlag("1");
    		billAccountInRepository.update(accountInEntity);
    	}
        
    }

	@Override
	public void confirm(BillAccountInVo vo) throws BizException {
		logger.info("confirm vo[{}]",vo);
    	BillAccountInEntity accountInEntity=new BillAccountInEntity();
		try {
            PropertyUtils.copyProperties(accountInEntity, vo);          
        } catch (Exception ex) {
            logger.error("转换失败{}",ex);
        }
    	BillAccountInEntity entity=billAccountInRepository.findById(vo.getId());
    	logger.info("confirm {}",entity.getConfirmStatus());
    	//0-未确认（可以删除） 1-已确认
		if(entity.getConfirmStatus().equals("1")){
    		throw new BizException("已确认状态不能再次确认");
		}else{
			accountInEntity.setConfirmStatus("1");
			billAccountInRepository.update(accountInEntity);
		}
	}
	
}
