package com.jiuyescm.bms.correct.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.correct.BmsProductsWeightAccountEntity;
import com.jiuyescm.bms.correct.repository.IBmsProductsWeightAccountRepository;
import com.jiuyescm.bms.correct.service.IBmsProductsWeightAccountService;
import com.jiuyescm.bms.correct.vo.BmsProductsMaterialAccountVo;
import com.jiuyescm.bms.correct.vo.BmsProductsWeightAccountVo;

/**
 * ..ServiceImpl
 * @author wangchen
 * 
 */
@Service("bmsProductsWeightAccountService")
public class BmsProductsWeightAccountServiceImpl implements IBmsProductsWeightAccountService {
	private static final Logger logger = Logger.getLogger(BmsProductsWeightAccountServiceImpl.class.getName());

	@Autowired
    private IBmsProductsWeightAccountRepository bmsProductsWeightAccountRepository;

	
	/**
	 * 分页查询
	 * @param page
	 * @param param
	 * @throws Exception 
	 */
    @Override
    public PageInfo<BmsProductsWeightAccountVo> query(Map<String, Object> condition,
            int pageNo, int pageSize) throws Exception {
    	PageInfo<BmsProductsWeightAccountVo> pageVoInfo=null;
		try{
			pageVoInfo=new PageInfo<BmsProductsWeightAccountVo>();
			PageInfo<BmsProductsWeightAccountEntity> pageInfo= bmsProductsWeightAccountRepository.query(condition, pageNo, pageSize);
			PropertyUtils.copyProperties(pageVoInfo, pageInfo);
			if(pageInfo!=null&&pageInfo.getList().size()>0){
				List<BmsProductsWeightAccountVo> list=new ArrayList<BmsProductsWeightAccountVo>();
				for(BmsProductsWeightAccountEntity entity:pageInfo.getList()){
					BmsProductsWeightAccountVo vo=new BmsProductsWeightAccountVo();
					PropertyUtils.copyProperties(vo, entity);
					list.add(vo);
				}
				pageVoInfo.setList(list);
			}
		}catch(Exception e){
			logger.error("query:",e);
			throw e;
		}
		return pageVoInfo;
    }
    
     /**
	 * 查询
	 * @param page
	 * @param param
	 */
	@Override
    public List<BmsProductsWeightAccountEntity> query(Map<String, Object> condition){
		return bmsProductsWeightAccountRepository.query(condition);
	}
	
}
