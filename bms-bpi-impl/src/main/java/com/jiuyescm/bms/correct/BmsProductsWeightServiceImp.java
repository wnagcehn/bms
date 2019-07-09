package com.jiuyescm.bms.correct;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.jiuyescm.bms.correct.repository.IBmsProductsWeightRepository;
import com.jiuyescm.bms.correct.service.IBmsProductsWeightService;
import com.jiuyescm.bms.correct.vo.BmsMarkingProductsVo;
import com.jiuyescm.bms.correct.vo.BmsProductsWeightAccountVo;

@Service("bmsProductsWeightService")
public class BmsProductsWeightServiceImp implements IBmsProductsWeightService{
	
	private static final Logger logger = Logger.getLogger(BmsProductsWeightServiceImp.class.getName());

	
	@Resource
	private IBmsProductsWeightRepository bmsProductsWeightRepository;
	
	@Override
	public List<BmsProductsWeightAccountVo> queyAllMax(
			Map<String, Object> condition) {
		// TODO Auto-generated method stub
		List<BmsProductsWeightAccountEntity> list=bmsProductsWeightRepository.queyAllMax(condition);
		List<BmsProductsWeightAccountVo> voList = new ArrayList<BmsProductsWeightAccountVo>();
    	for(BmsProductsWeightAccountEntity entity : list) {
    		BmsProductsWeightAccountVo vo = new BmsProductsWeightAccountVo();
    		try {
                PropertyUtils.copyProperties(vo, entity);
            } catch (Exception ex) {
               logger.error("转换失败",ex);
            }
    		voList.add(vo);
    	}
		return voList;
	}

	@Override
	public List<BmsMarkingProductsVo> queryMark(Map<String, Object> condition) {
		// TODO Auto-generated method stub
		List<BmsMarkingProductsEntity> list=bmsProductsWeightRepository.queryMark(condition);
		List<BmsMarkingProductsVo> voList = new ArrayList<BmsMarkingProductsVo>();
    	for(BmsMarkingProductsEntity entity : list) {
    		BmsMarkingProductsVo vo = new BmsMarkingProductsVo();
    		try {
                PropertyUtils.copyProperties(vo, entity);
            } catch (Exception ex) {
               logger.error("转换失败",ex);
            }
    		voList.add(vo);
    	}
		return voList;
	}

	@Override
	public int updateMarkList(List<BmsMarkingProductsVo> list) {
		// TODO Auto-generated method stub
		List<BmsMarkingProductsEntity> enList=new ArrayList<BmsMarkingProductsEntity>();
		for(BmsMarkingProductsVo vo : list) {
			BmsMarkingProductsEntity entity = new BmsMarkingProductsEntity();
    		try {
                PropertyUtils.copyProperties(entity, vo);
            } catch (Exception ex) {
                logger.error("转换失败",ex);
            }
    		enList.add(entity);
    	}
		return bmsProductsWeightRepository.updateMarkList(enList);
	}

	@Override
	public BmsMarkingProductsVo queryMarkVo(Map<String, Object> condition) {
		// TODO Auto-generated method stub
		BmsMarkingProductsEntity entity=bmsProductsWeightRepository.queryMarkVo(condition);
		try {
			BmsMarkingProductsVo vo=new BmsMarkingProductsVo();
            PropertyUtils.copyProperties(vo, entity);
            return vo;
        } catch (Exception ex) {
            logger.error("转换失败",ex);
        }
		return null;
	}

	@Override
	public BmsMarkingProductsVo queryOneMaterial(Map<String, Object> condition) {
		// TODO Auto-generated method stub
		BmsMarkingProductsEntity entity=bmsProductsWeightRepository.queryOneMaterial(condition);
		try {
			BmsMarkingProductsVo vo=new BmsMarkingProductsVo();
            PropertyUtils.copyProperties(vo, entity);
            return vo;
        } catch (Exception ex) {
            logger.error("转换失败",ex);
        }
		return null;
	}
	
	
	@Override
	public List<BmsProductsWeightAccountVo> queyWeightCount(
			Map<String, Object> condition) {
		// TODO Auto-generated method stub
		List<BmsProductsWeightAccountEntity> list=bmsProductsWeightRepository.queyWeightCount(condition);
		List<BmsProductsWeightAccountVo> voList = new ArrayList<BmsProductsWeightAccountVo>();
    	for(BmsProductsWeightAccountEntity entity : list) {
    		BmsProductsWeightAccountVo vo = new BmsProductsWeightAccountVo();
    		try {
                PropertyUtils.copyProperties(vo, entity);
            } catch (Exception ex) {
               logger.error("转换失败",ex);
            }
    		voList.add(vo);
    	}
		return voList;
	}

	@Override
	public int saveList(List<BmsProductsWeightAccountVo> list) {
		// TODO Auto-generated method stub
		List<BmsProductsWeightAccountEntity> enList=new ArrayList<BmsProductsWeightAccountEntity>();
		for(BmsProductsWeightAccountVo vo : list) {
			BmsProductsWeightAccountEntity entity = new BmsProductsWeightAccountEntity();
    		try {
                PropertyUtils.copyProperties(entity, vo);
            } catch (Exception ex) {
                logger.error("转换失败",ex);
            }
    		enList.add(entity);
    	}
		return bmsProductsWeightRepository.saveList(enList);
	}

	@Override
	public int updateMark(Map<String, Object> condition) {
		// TODO Auto-generated method stub
		return bmsProductsWeightRepository.updateMark(condition);
	}

	@Override
	public int saveWeight(Map<String, Object> condition) {
		// TODO Auto-generated method stub
		return bmsProductsWeightRepository.saveWeight(condition);
	}



}
