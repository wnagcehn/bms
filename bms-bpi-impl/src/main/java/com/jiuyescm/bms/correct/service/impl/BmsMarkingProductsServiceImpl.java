package com.jiuyescm.bms.correct.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.correct.BmsMarkingProductsEntity;
import com.jiuyescm.bms.correct.repository.IBmsMarkingProductsRepository;
import com.jiuyescm.bms.correct.service.IBmsMarkingProductsService;
import com.jiuyescm.bms.correct.vo.BmsMarkingProductsVo;

/**
 * ..ServiceImpl
 * @author wangchen
 * 
 */
@Service("bmsMarkingProductsService")
public class BmsMarkingProductsServiceImpl implements IBmsMarkingProductsService {
	private static final Logger logger = Logger.getLogger(BmsMarkingProductsServiceImpl.class.getName());

	@Autowired
    private IBmsMarkingProductsRepository bmsMarkingProductsRepository;

	
	/**
	 * 分页查询
	 * @param page
	 * @param param
	 * @throws Exception 
	 */
    @Override
    public PageInfo<BmsMarkingProductsVo> query(Map<String, Object> condition,
            int pageNo, int pageSize) throws Exception {
    	PageInfo<BmsMarkingProductsVo> pageVoInfo=null;
		try{
			pageVoInfo=new PageInfo<BmsMarkingProductsVo>();
			PageInfo<BmsMarkingProductsEntity> pageInfo=bmsMarkingProductsRepository.query(condition,pageNo,pageSize);
			PropertyUtils.copyProperties(pageVoInfo, pageInfo);
			if(pageInfo!=null&&pageInfo.getList().size()>0){
				List<BmsMarkingProductsVo> list=new ArrayList<BmsMarkingProductsVo>();
				for(BmsMarkingProductsEntity entity:pageInfo.getList()){
					BmsMarkingProductsVo vo=new BmsMarkingProductsVo();
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
    public List<BmsMarkingProductsEntity> query(Map<String, Object> condition){
		return bmsMarkingProductsRepository.query(condition);
	}
	
	@Override
    public PageInfo<BmsMarkingProductsVo> queryByWeight(Map<String, Object> condition,
            int pageNo, int pageSize) throws Exception {
    	PageInfo<BmsMarkingProductsVo> pageVoInfo=null;
		try{
			pageVoInfo=new PageInfo<BmsMarkingProductsVo>();
			PageInfo<BmsMarkingProductsEntity> pageInfo=bmsMarkingProductsRepository.queryByWeight(condition,pageNo,pageSize);
			PropertyUtils.copyProperties(pageVoInfo, pageInfo);
			if(pageInfo!=null&&pageInfo.getList().size()>0){
				List<BmsMarkingProductsVo> list=new ArrayList<BmsMarkingProductsVo>();
				for(BmsMarkingProductsEntity entity:pageInfo.getList()){
					if (null != pageInfo.getList()) {
						BmsMarkingProductsVo vo=new BmsMarkingProductsVo();
						PropertyUtils.copyProperties(vo, entity);
						list.add(vo);
					}				
				}
				pageVoInfo.setList(list);
			}
		}catch(Exception e){
			logger.error("query:",e);
			throw e;
		}
		return pageVoInfo;
    }
	
	@Override
    public PageInfo<BmsMarkingProductsVo> queryByMaterial(Map<String, Object> condition,
            int pageNo, int pageSize) throws Exception {
    	PageInfo<BmsMarkingProductsVo> pageVoInfo=null;
		try{
			pageVoInfo=new PageInfo<BmsMarkingProductsVo>();
			PageInfo<BmsMarkingProductsEntity> pageInfo=bmsMarkingProductsRepository.queryByMaterial(condition,pageNo,pageSize);
			PropertyUtils.copyProperties(pageVoInfo, pageInfo);
			if(pageInfo!=null&&pageInfo.getList().size()>0){
				List<BmsMarkingProductsVo> list=new ArrayList<BmsMarkingProductsVo>();
				for(BmsMarkingProductsEntity entity:pageInfo.getList()){
					BmsMarkingProductsVo vo=new BmsMarkingProductsVo();
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
}
