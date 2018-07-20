package com.jiuyescm.bms.subject;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.base.dictionary.entity.BmsSubjectInfoEntity;
import com.jiuyescm.bms.base.dictionary.repository.IBmsSubjectInfoRepository;
import com.jiuyescm.bms.subject.service.IBmsSubjectInfoService;
import com.jiuyescm.bms.subject.vo.BmsSubjectInfoVo;

@Service("bmsSubjectService")
public class BmsSubjectInfoServiceImpl implements IBmsSubjectInfoService{

	private static final Logger logger = Logger.getLogger(BmsSubjectInfoServiceImpl.class.getName());

	@Resource
	private IBmsSubjectInfoRepository bmsSubjectInfoRepository;
	
	@Override
	public List<BmsSubjectInfoVo> querySubject(BmsSubjectInfoVo queryVo) {
		// TODO Auto-generated method stub
		List<BmsSubjectInfoVo> voList=new ArrayList<BmsSubjectInfoVo>();
		try{
			BmsSubjectInfoEntity queryEntity=new BmsSubjectInfoEntity();
		    PropertyUtils.copyProperties(queryEntity, queryVo);
			
			List<BmsSubjectInfoEntity> list=bmsSubjectInfoRepository.querySubject(queryEntity);
			for(BmsSubjectInfoEntity entity : list) {
				BmsSubjectInfoVo vo = new BmsSubjectInfoVo();
	            PropertyUtils.copyProperties(vo, entity);
	    		voList.add(vo);
	    	}
		}catch(Exception e){
			logger.error("query error:",e);
		}
		return voList;
	}

	@Override
	public PageInfo<BmsSubjectInfoVo> query(BmsSubjectInfoVo queryCondition,
			int pageNo, int pageSize){
		// TODO Auto-generated method stub
		PageInfo<BmsSubjectInfoVo> result=new PageInfo<BmsSubjectInfoVo>();

		try {
			BmsSubjectInfoEntity queryEntity=new BmsSubjectInfoEntity();
			PropertyUtils.copyProperties(queryEntity, queryCondition);
			PageInfo<BmsSubjectInfoEntity> pageInfo=bmsSubjectInfoRepository.query(queryEntity, pageNo, pageSize);			
			List<BmsSubjectInfoVo> voList = new ArrayList<BmsSubjectInfoVo>();
	    	for(BmsSubjectInfoEntity entity : pageInfo.getList()) {
	    		BmsSubjectInfoVo vo = new BmsSubjectInfoVo();    		
	            PropertyUtils.copyProperties(vo, entity);          
	    		voList.add(vo);
	    	}		
	    	result.setList(voList);
			return result;
		} catch (Exception ex) {
         	logger.error("转换失败:{0}",ex);
        }
    	
    	return result;
	}

	@Override
	public BmsSubjectInfoVo save(BmsSubjectInfoVo vo) {
		// TODO Auto-generated method stub
		BmsSubjectInfoEntity entity=new BmsSubjectInfoEntity();
		BmsSubjectInfoVo resultVo=new BmsSubjectInfoVo();

		try {
            PropertyUtils.copyProperties(entity, vo);       
            BmsSubjectInfoEntity resultEntity=bmsSubjectInfoRepository.save(entity);          
            PropertyUtils.copyProperties(resultVo, resultEntity);         
            return resultVo;
        } catch (Exception ex) {
        	logger.error("转换失败:{0}",ex);
        }
		return null;
	}

	@Override
	public BmsSubjectInfoVo update(BmsSubjectInfoVo vo) {
		// TODO Auto-generated method stub
		BmsSubjectInfoEntity entity=new BmsSubjectInfoEntity();
		BmsSubjectInfoVo resultVo=new BmsSubjectInfoVo();
		try {
            PropertyUtils.copyProperties(entity, vo);
            BmsSubjectInfoEntity resultEntity=bmsSubjectInfoRepository.update(entity);
            PropertyUtils.copyProperties(resultVo, resultEntity);
            return resultVo;
        } catch (Exception ex) {
        	logger.error("转换失败:{0}",ex);
        }
		return null;
	}

	@Override
	public BmsSubjectInfoVo queryOne(Long id) {
		// TODO Auto-generated method stub
		BmsSubjectInfoVo resultVo=new BmsSubjectInfoVo();
		try {
            BmsSubjectInfoEntity resultEntity=bmsSubjectInfoRepository.queryOne(id);
            PropertyUtils.copyProperties(resultVo, resultEntity);
            return resultVo;
        } catch (Exception ex) {
        	logger.error("转换失败:{0}",ex);
        }
		return null;
	}

}
