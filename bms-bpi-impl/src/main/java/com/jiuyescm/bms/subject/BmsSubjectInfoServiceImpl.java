package com.jiuyescm.bms.subject;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

	
	private static final Logger logger = LoggerFactory.getLogger(BmsSubjectInfoServiceImpl.class.getName()); 

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
            if(resultVo!=null){
            	String code = resultVo.getInOutTypecode()+resultVo.getBizTypecode()+resultVo.getSubjectCode();
                String name = resultVo.getInOutTypecode()+resultVo.getBizTypecode()+resultVo.getSubjectName();
                logger.info("code:{} | name:{}", code,name);
                redisClient.set(code, RedisCache.SUBJECTCODE_SPACE, resultVo, RedisCache.expiredTime);//按code缓存
                redisClient.set(name, RedisCache.SUBJECTNAME_SPACE, resultVo, RedisCache.expiredTime);//按name缓存
            }
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
        	String code = resultVo.getInOutTypecode()+resultVo.getBizTypecode()+resultVo.getSubjectCode();
            String name = resultVo.getInOutTypecode()+resultVo.getBizTypecode()+resultVo.getSubjectName();
            logger.info("code:{} | name:{}", code,name);
            if("0".equals(resultVo.getDelFlag())){
                redisClient.set(code, RedisCache.SUBJECTCODE_SPACE, resultVo, RedisCache.expiredTime);//按code缓存
                redisClient.set(name, RedisCache.SUBJECTNAME_SPACE, resultVo, RedisCache.expiredTime);//按name缓存
            }else if ("1".equals(resultVo.getDelFlag())) {            	
                redisClient.del(code, RedisCache.SUBJECTCODE_SPACE);
                redisClient.del(name, RedisCache.SUBJECTNAME_SPACE);
			}
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
            	String code = resultVo.getInOutTypecode()+resultVo.getBizTypecode()+resultVo.getSubjectCode();
                String name = resultVo.getInOutTypecode()+resultVo.getBizTypecode()+resultVo.getSubjectName();
                logger.info("code:{} | name:{}", code,name);
                redisClient.set(code, RedisCache.SUBJECTCODE_SPACE, resultVo, RedisCache.expiredTime);//按code缓存
                redisClient.set(name, RedisCache.SUBJECTNAME_SPACE, resultVo, RedisCache.expiredTime);//按name缓存
            return resultVo;
        } catch (Exception ex) {
        	logger.error("转换失败:{0}",ex);
        }
		return null;
	}

	@Override
	public BmsSubjectInfoVo querySubjectByCode(final String inOutTypecode,final String bizType, final String subjectCode) {
		String code = inOutTypecode + bizType + subjectCode;
		BmsSubjectInfoVo vo = redisClient.get(code, RedisCache.SUBJECTCODE_SPACE,BmsSubjectInfoVo.class, new GetDataCallBack<BmsSubjectInfoVo>(){

			@Override
			public int getExpiredTime() {
				return RedisCache.fiveMinutes;
			}

			@Override
			public BmsSubjectInfoVo invoke() {
				BmsSubjectInfoVo vo = new BmsSubjectInfoVo();
				vo.setInOutTypecode(inOutTypecode);
				vo.setBizTypecode(bizType);
				vo.setSubjectCode(subjectCode);
				List<BmsSubjectInfoVo> vos = querySubject(vo);
				if(vos == null || vos.size()==0){
					return new BmsSubjectInfoVo();
				}
				return vos.get(0);
			}
		});
		return vo;
	}

	@Override
	public BmsSubjectInfoVo querySubjectByName(final String inOutTypecode,final String bizType, final String subjectName) {
		String name = inOutTypecode + bizType + subjectName;
		BmsSubjectInfoVo vo = redisClient.get(name, RedisCache.SUBJECTCODE_SPACE,BmsSubjectInfoVo.class, new GetDataCallBack<BmsSubjectInfoVo>(){

			@Override
			public int getExpiredTime() {
				return RedisCache.fiveMinutes;
			}

			@Override
			public BmsSubjectInfoVo invoke() {
				BmsSubjectInfoVo vo = new BmsSubjectInfoVo();
				vo.setInOutTypecode(inOutTypecode);
				vo.setBizTypecode(bizType);
				vo.setSubjectName(subjectName);
				List<BmsSubjectInfoVo> vos = querySubject(vo);
				if(vos == null || vos.size()==0){
					return new BmsSubjectInfoVo();
				}
				return vos.get(0);
			}
		});
		return vo;
	}

	@Override
	public BmsSubjectInfoVo queryReceiveByCode(String subjectCode) {
		BmsSubjectInfoVo vo = new BmsSubjectInfoVo();
		vo.setInOutTypecode("INPUT");
		vo.setSubjectCode(subjectCode);
		List<BmsSubjectInfoVo> vos = querySubject(vo);
		if(vos == null || vos.size() == 0){
			return null;
		}
		return vos.get(0);
	}

}
