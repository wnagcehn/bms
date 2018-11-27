package com.jiuyescm.bms.subject;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.base.dictionary.entity.BmsSubjectInfoEntity;
import com.jiuyescm.bms.base.dictionary.repository.IBmsSubjectInfoRepository;
import com.jiuyescm.bms.subject.service.IBmsSubjectInfoService;
import com.jiuyescm.bms.subject.vo.BmsSubjectInfoVo;
import com.jiuyescm.constants.RedisCache;
import com.jiuyescm.framework.redis.callback.GetDataCallBack;
import com.jiuyescm.framework.redis.client.IRedisClient;

@Service("bmsSubjectService")
public class BmsSubjectInfoServiceImpl implements IBmsSubjectInfoService{

	private static final Logger logger = Logger.getLogger(BmsSubjectInfoServiceImpl.class.getName());

	@Autowired private IRedisClient redisClient;
	@Resource private IBmsSubjectInfoRepository bmsSubjectInfoRepository;
	
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
	    	PropertyUtils.copyProperties(result, pageInfo); 
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

	@Override
	public BmsSubjectInfoVo querySubjectByCode(final String inOutTypecode,final String bizType, final String subjectCode) {
		
		BmsSubjectInfoVo vo = redisClient.get(subjectCode, RedisCache.SUBJECTCODE_SPACE,BmsSubjectInfoVo.class, new GetDataCallBack<BmsSubjectInfoVo>(){

			@Override
			public int getExpiredTime() {
				return RedisCache.halfHour;
			}

			@Override
			public BmsSubjectInfoVo invoke() {
				BmsSubjectInfoVo vo = new BmsSubjectInfoVo();
				vo.setInOutTypecode(inOutTypecode);
				vo.setBizTypecode(bizType);
				vo.setSubjectCode(subjectCode);
				List<BmsSubjectInfoVo> vos = querySubject(vo);
				return vos.get(0);
			}
		});
		return vo;
	}

	@Override
	public BmsSubjectInfoVo querySubjectByName(final String inOutTypecode,final String bizType, final String subjectName) {
		BmsSubjectInfoVo vo = redisClient.get(subjectName, RedisCache.SUBJECTCODE_SPACE,BmsSubjectInfoVo.class, new GetDataCallBack<BmsSubjectInfoVo>(){

			@Override
			public int getExpiredTime() {
				return RedisCache.halfHour;
			}

			@Override
			public BmsSubjectInfoVo invoke() {
				BmsSubjectInfoVo vo = new BmsSubjectInfoVo();
				vo.setInOutTypecode(inOutTypecode);
				vo.setBizTypecode(bizType);
				vo.setSubjectCode(subjectName);
				List<BmsSubjectInfoVo> vos = querySubject(vo);
				return vos.get(0);
			}
		});
		return vo;
	}

}
