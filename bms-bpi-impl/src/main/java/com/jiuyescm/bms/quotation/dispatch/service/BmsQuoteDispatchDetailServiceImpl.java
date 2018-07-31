/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.quotation.dispatch.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.quotation.dispatch.entity.BmsQuoteDispatchDetailEntity;
import com.jiuyescm.bms.quotation.dispatch.entity.vo.BmsQuoteDispatchDetailVo;
import com.jiuyescm.bms.quotation.dispatch.repository.IBmsQuoteDispatchDetailRepository;

/**
 * 宅配报价明细
 * @author yangss
 */
@Service("bmsQuoteDispatchDetailService")
public class BmsQuoteDispatchDetailServiceImpl implements IBmsQuoteDispatchDetailService {

	private static final Logger logger = Logger.getLogger(BmsQuoteDispatchDetailServiceImpl.class.getName());
	
	@Autowired
    private IBmsQuoteDispatchDetailRepository bmsQuoteDispatchDetailRepository;

    @Override
    public PageInfo<BmsQuoteDispatchDetailVo> query(Map<String, Object> condition,
            int pageNo, int pageSize)  throws Exception{
    	PageInfo<BmsQuoteDispatchDetailEntity> pageInfo = bmsQuoteDispatchDetailRepository.query(condition, pageNo, pageSize);
    	PageInfo<BmsQuoteDispatchDetailVo> result=new PageInfo<BmsQuoteDispatchDetailVo>();
    	
    	List<BmsQuoteDispatchDetailVo> voList = new ArrayList<BmsQuoteDispatchDetailVo>();
		PropertyUtils.copyProperties(result, pageInfo);
		if (null != pageInfo && pageInfo.getList().size() > 0) {
			for(BmsQuoteDispatchDetailEntity entity : pageInfo.getList()) {
				BmsQuoteDispatchDetailVo vo = new BmsQuoteDispatchDetailVo();
				PropertyUtils.copyProperties(vo, entity);
				voList.add(vo);
			}
			result.setList(voList);
		}
		return result;
    }
    
	@Override
	public List<BmsQuoteDispatchDetailVo> queryAllById(Map<String, Object> parameter) throws Exception {
		List<BmsQuoteDispatchDetailVo> result = null;
		List<BmsQuoteDispatchDetailEntity> list = bmsQuoteDispatchDetailRepository.queryAllById(parameter);
		if (null != list && list.size() > 0) {
			result = new ArrayList<BmsQuoteDispatchDetailVo>();
			for (BmsQuoteDispatchDetailEntity entity : list) {
				BmsQuoteDispatchDetailVo vo = new BmsQuoteDispatchDetailVo();
				PropertyUtils.copyProperties(vo, entity);
				result.add(vo);
			}
		}
		return result;
	}
	
	@Override
	public Integer getId(String temid) {
		return bmsQuoteDispatchDetailRepository.getId(temid);
	}
	
    @Override
    public int save(BmsQuoteDispatchDetailVo vo) throws Exception {
    	BmsQuoteDispatchDetailEntity entity=new BmsQuoteDispatchDetailEntity();
		PropertyUtils.copyProperties(entity, vo);
        return bmsQuoteDispatchDetailRepository.save(entity);
    }

    @Override
    public int update(BmsQuoteDispatchDetailVo vo) throws Exception {
    	BmsQuoteDispatchDetailEntity entity=new BmsQuoteDispatchDetailEntity();
		PropertyUtils.copyProperties(entity, vo);
        return bmsQuoteDispatchDetailRepository.update(entity);
    }

    @Override
    public void delete(Long id) throws Exception {
        bmsQuoteDispatchDetailRepository.delete(id);
    }

	@Override
	public int removePriceDistribution(BmsQuoteDispatchDetailVo vo) throws Exception {
		BmsQuoteDispatchDetailEntity entity=new BmsQuoteDispatchDetailEntity();
		PropertyUtils.copyProperties(entity, vo);
		return bmsQuoteDispatchDetailRepository.deletePriceDistribution(entity);
	}
	
	@Override
	public int removeDispatchByMap(Map<String, Object> condition) {
		return bmsQuoteDispatchDetailRepository.removeDispatchByMap(condition);
	}
	
	@Override
	public int insertBatchTmp(List<BmsQuoteDispatchDetailVo> list) throws Exception {
		List<BmsQuoteDispatchDetailEntity> result = new ArrayList<BmsQuoteDispatchDetailEntity>();
		if (null != list && list.size() > 0) {
			PropertyUtils.copyProperties(result, list);
			
			BmsQuoteDispatchDetailEntity entity = null;
			for (BmsQuoteDispatchDetailVo vo : list) {
				entity = new BmsQuoteDispatchDetailEntity();
				PropertyUtils.copyProperties(entity, vo);
				result.add(entity);
			}
			return bmsQuoteDispatchDetailRepository.insertBatchTmp(result);
		}
		return 0;
	}

	@Override
	public BmsQuoteDispatchDetailVo queryOne(Map<String, Object> condition) {
		// TODO Auto-generated method stub
		BmsQuoteDispatchDetailVo vo=new BmsQuoteDispatchDetailVo();
		BmsQuoteDispatchDetailEntity entity=bmsQuoteDispatchDetailRepository.queryOne(condition);
		if(entity==null){
			return null;
		}
		try {
            PropertyUtils.copyProperties(vo, entity);
        } catch (Exception ex) {
            logger.error("转换失败:{0}",ex);
        }
		return vo;
	}

}
