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
			int pageNo, int pageSize) throws Exception {
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
	public BmsSubjectInfoEntity save(BmsSubjectInfoVo vo) {
		// TODO Auto-generated method stub
		BmsSubjectInfoEntity entity=new BmsSubjectInfoEntity();
		try {
            PropertyUtils.copyProperties(entity, vo);
            
            return bmsSubjectInfoRepository.save(entity);
        } catch (Exception ex) {
        	logger.error("转换失败:{0}",ex);
        }
		return null;
	}

	@Override
	public BmsSubjectInfoEntity update(BmsSubjectInfoVo vo) {
		// TODO Auto-generated method stub
		BmsSubjectInfoEntity entity=new BmsSubjectInfoEntity();
		try {
            PropertyUtils.copyProperties(entity, vo);
            
            return bmsSubjectInfoRepository.update(entity);
        } catch (Exception ex) {
        	logger.error("转换失败:{0}",ex);
        }
		return null;
	}

}
