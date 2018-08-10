package com.jiuyescm.bms.biz.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.jiuyescm.bms.biz.api.IOutstockService;
import com.jiuyescm.bms.biz.entity.BmsOutstockInfoEntity;
import com.jiuyescm.bms.biz.entity.BmsOutstockRecordEntity;
import com.jiuyescm.bms.biz.repo.IOutstockInfoRepository;
import com.jiuyescm.bms.biz.repo.IOutstockRecordRepository;
import com.jiuyescm.bms.biz.vo.BmsDispatchVo;
import com.jiuyescm.exception.BizException;

@Service("outstockService")
public class OutstockServiceImpl implements IOutstockService{

	private static final Logger logger = LoggerFactory.getLogger(OutstockServiceImpl.class.getName());

	@Autowired 
	private IOutstockRecordRepository outstockRecordrepository;
	
	@Autowired
	private IOutstockInfoRepository outstockInfoRepository;
	
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = { BizException.class })
	@Override
	public int updateBizData(BmsDispatchVo vo) {
		// TODO Auto-generated method stub

		//1:写入到记录表
		BmsOutstockRecordEntity record=new BmsOutstockRecordEntity();
		try {    		
            PropertyUtils.copyProperties(record, vo);
            outstockRecordrepository.insert(record);
            	
		} catch (Exception ex) {
        	logger.error("转换为记录实体类失败:{0}",ex);
        }
		
		//2:更新到outstock_info
		BmsOutstockInfoEntity bmsOutStockInfo=new BmsOutstockInfoEntity();
		try {    		
            PropertyUtils.copyProperties(bmsOutStockInfo, vo);
            	
		} catch (Exception ex) {
        	logger.error("转换outstock_info失败:{0}",ex);
        }
		
		return outstockInfoRepository.update(bmsOutStockInfo);
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = { BizException.class })
	@Override
	public int updateBizDataBatch(List<BmsDispatchVo> vos) {
		// TODO Auto-generated method stub
		
		//1:写入到记录表
		List<BmsOutstockRecordEntity> recordList=new ArrayList<BmsOutstockRecordEntity>();
		try {
			for(BmsDispatchVo vo : vos) {
				BmsOutstockRecordEntity record=new BmsOutstockRecordEntity();	    		
				  PropertyUtils.copyProperties(record, vo);
				  recordList.add(record);
	    	}      
            outstockRecordrepository.insertList(recordList);
	
		} catch (Exception ex) {
        	logger.error("转换为记录实体类失败:{0}",ex);
        }
		
		//2:更新到outstock_info
		List<BmsOutstockInfoEntity> bmsOutStockInfoList=new ArrayList<BmsOutstockInfoEntity>();
		try {   
			for(BmsDispatchVo vo : vos) {
				BmsOutstockInfoEntity bmsOutStockInfo=new BmsOutstockInfoEntity();	    		
				PropertyUtils.copyProperties(bmsOutStockInfo, vo);
				bmsOutStockInfoList.add(bmsOutStockInfo);
	    	}      
                   	
		} catch (Exception ex) {
        	logger.error("转换outstock_info失败:{0}",ex);
        }
		
		return  outstockInfoRepository.updateList(bmsOutStockInfoList);
	}


}
